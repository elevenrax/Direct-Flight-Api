#!/bin/python3

import requests
import inspect
import time
import os

from termcolor import colored

# Supress "Self-Signed" Certificate InsecureRequestWarning
from requests.packages.urllib3.exceptions import InsecureRequestWarning
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)


os.system('mvn spring-boot:run &> /dev/null &')
time.sleep(6)

passed = 0   # Track tests
failed = 0   # Track tests

def lineno():
    return "(" + str(inspect.currentframe().f_back.f_lineno) + ")"


def assertStatus(testdetails, actual, expected):
    global passed, failed
    if actual == expected:
        passed += 1
        print(colored("[Succeeded] ", 'green') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(expected) + ", " + str(actual) + ")")
    else:
        failed += 1
        print(colored("[Failed] ", 'red') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(expected) + ", " + str(actual) + ")")


def assertRecordAdded(testdetails, num_of_records_actual, num_of_records_expected):
    global passed, failed
    if num_of_records_actual == num_of_records_expected:
        passed += 1
        print(colored("[Succeeded] ", 'green') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(num_of_records_expected) + ", " + str(num_of_records_actual) + ")")
    else:
        failed += 1
        print(colored("[Failed] ", 'red') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(num_of_records_expected) + ", " + str(num_of_records_actual) + ")")


def assertCorrectValue(testdetails, actual, expected):
    global passed, failed
    if actual == expected:
        passed += 1
        print(colored("[Succeeded] ", 'green') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(expected) + ", " + str(actual) + ")")
    else:
        failed += 1
        print(colored("[Failed] ", 'red') + testdetails + "\n"
              + "\t\t\t\t\texpected v actual: (" + str(expected) + ", " + str(actual) + ")")


def assertDenyConnection(testdetails, test_passed):
    global passed, failed
    if test_passed:
        passed += 1
        print(colored("[Succeeded] ", 'green') + testdetails)
    else:
        failed += 1
        print(colored("[Failed] ", 'red') + testdetails)


# Base URL
URL = "https://localhost:8443"


print("TESTS:")

#################
# Security Test #
#################
try:
    print("\nTest: HTTPS only.")
    payload = {'username': 'nathan', 'password': 'securepassword123'}
    r = requests.post("http://localhost:8080" + '/bookings', json=payload, verify=False)
    assertDenyConnection(lineno() + " /Booking: Attempt to use unsafe http on port 8080. Expect RefusedConnection, did not get.", test_passed=False)
except (ConnectionRefusedError, ConnectionError, 
    requests.packages.urllib3.exceptions.NewConnectionError, 
    requests.packages.urllib3.exceptions.MaxRetryError, 
    requests.exceptions.ConnectionError):
    assertDenyConnection(lineno() + " /Booking: Attempt to use unsafe http on port 8080. Expect RefusedConnection, got.", test_passed=True)
except Exception:
    '''
    '''

#######################
# Empty Bookings Test #
#######################

print("\nTest: Retrieve Empty Booking")
payload = {'username': 'nathan', 'password': 'securepassword123'}
r = requests.post(URL + '/bookings', json=payload, verify=False)
assertStatus(lineno() + " /booking: Get Booking for user with no Bookings.", r.status_code, 404)

print("\nTest: Retrieve Empty Booking using malformed payload (no password)")
payload = {'username': 'nathan'}
r = requests.post(URL + '/bookings', json=payload, verify=False)
assertStatus(lineno() + " /booking: no password attribute supplied.", r.status_code, 401)

print("\nTest: Retrieve empty Booking using invalid password")
payload = {'username': 'nathan', 'password': 'wrongpassword'}
r = requests.post(URL + '/bookings', json=payload, verify=False)
assertStatus(lineno() + " /booking: Try to get booking for user but supply invalid password.", r.status_code, 401)

print("\nTest: Retrieve empty Booking using invalid username")
payload = {'username': 'abcde', 'password': 'wrongpassword'}
r = requests.post(URL + '/bookings', json=payload, verify=False)
assertStatus(lineno() + " /booking: Try to get booking for user but supply invalid username.", r.status_code, 401)


#######################
#     Commit Test     #
#######################

print("\nTest: Add a valid booking and verify results")
payload = {'username': 'nathan', 'password': 'securepassword123', 'flightnumbers': ['UA0932']}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo = r2.json()[0]['bookedFlights'][0]['flightNumber']
assertRecordAdded(lineno() + " /commit: Make valid booking, for valid user. Check /bookings for success.", len(r2.json()), 1)
assertCorrectValue(lineno() + " /commit: Check flightnumber is correct.", flightNo, "UA0932")

print("\nTest: Add a second valid booking with two flights and verify results")
payload = {'username': 'nathan', 'password': 'securepassword123', 'flightnumbers': ['QF090', 'QF092']}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo1 = r2.json()[1]['bookedFlights'][0]['flightNumber']
flightNo2 = r2.json()[1]['bookedFlights'][1]['flightNumber']
assertRecordAdded(lineno() + " /commit: Make valid booking, for valid user. Check /bookings for success.", len(r2.json()), 2)
assertCorrectValue(lineno() + " /commit: Check flightnumber 1 is correct.", flightNo1, "QF090")
assertCorrectValue(lineno() + " /commit: Check flightnumber 2 is correct.", flightNo2, "QF092")

print("\nTest: Add a third INVALID booking and verify results")
payload = {'username': 'nathan', 'password': 'securepassword123', 'flightnumbers': ['NOT_A_FLIGHT']}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo1 = r2.json()[1]['bookedFlights'][0]['flightNumber']
assertStatus(lineno() + " /commit: Check bad request given invalid flight provided.", r1.status_code, 400)
assertRecordAdded(lineno() + " /commit: Check count of bookings remains unchanged.", len(r2.json()), 2)

print("\nTest: Add a booking using incorrect login name")
payload = {'username': 'nathanzzzzz', 'password': 'securepassword123', 'flightnumbers': ['QF090']}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo1 = r2.json()[1]['bookedFlights'][0]['flightNumber']
assertStatus(lineno() + " /commit: Check Unauthorised given invalid flight provided.", r1.status_code, 401)
assertRecordAdded(lineno() + " /commit: Check count of bookings remains unchanged.", len(r2.json()), 2)

print("\nTest: Add a booking using incorrect password")
payload = {'username': 'nathan', 'password': 'not_my_pass', 'flightnumbers': ['QF090']}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo1 = r2.json()[1]['bookedFlights'][0]['flightNumber']
assertStatus(lineno() + " /commit: Check Unauthorised given invalid flight provided.", r1.status_code, 401)
assertRecordAdded(lineno() + " /commit: Check count of bookings remains unchanged.", len(r2.json()), 2)

print("\nTest: Add a booking using malformed payload i.e. no flightnumber attributes")
payload = {'username': 'nathan', 'password': 'securepassword123'}
r1 = requests.post(URL + '/commit', json=payload, verify=False)
payload = {'username': 'nathan', 'password': 'securepassword123'}
r2 = requests.post(URL + '/bookings', json=payload, verify=False)
flightNo1 = r2.json()[1]['bookedFlights'][0]['flightNumber']
assertStatus(lineno() + " /commit: Check Bad Request given malformed payload.", r1.status_code, 400)
assertRecordAdded(lineno() + " /commit: Check count of bookings remains unchanged.", len(r2.json()), 2)


#######################
#       Summary       #
#######################

print('\nSummary:')
print(colored('Passed: ', 'green') + str(passed))
print(colored('Failed: ', 'red') + str(failed))



os.system('kill $(ps aux | grep \'[s]pring-boot\' | awk \'{print $2}\')')

