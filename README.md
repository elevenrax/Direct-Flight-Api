
## Assumptions

1. Flight Numbers are guaranteed unique identifiers not only within carriers, but across carriers too. Therefore a flight `Q100` as an example, will exist only once. Under this assumption, to book a flight, a user need only supply the flight number. 

2. For simplicity, Unix Time was used. ZonedDateTime would be a better choice to handle time zone's of flights.

3. Per the spec, did not need to worry about payment/availability. Also did not account for class (i.e. business or economy) or seating etc. 

4. The spec says to make `/bookings` a GET. But we need to send password information to authenticate the session. GET can have a Request Body, however the standard states `The GET method means retrieve whatever information ([...]) is identified by the Request-URI.` And only the Request-URI. Therefore POST is a better approach to be used here.

## Issues Running Application?

You may have an issue running the app due to the Web App requiring HTTPS. To fix, make your own SSL self-signed certificate and trying running again. 

```bash
# Change Directory into resources directory
cd PROJECT_ROOT/src/main/resources

# If already exists, delete keystore.p12
rm -F keystore.p12

# Generate a new SSL self-signed certficate called tomcat
keytool -genkey -alias tomcat
 -storetype PKCS12 -keyalg RSA -keysize 2048
 -keystore keystore.p12 -validity 3650
```


## API Manual 

#### /booking POST

Retreive's the complete list of booking's for a single user.

To do so send a POST to the /booking endpoint with the following details in the body of the request:

```json
{
	"username":"username",
	"password":"password"
}
```


###### Returns:

If credentials are valid, returns a Json array containing Booking objects. A booking object can contain 1 or more flight objects under `bookedFlights`. 

###### Example: 
```json
[
    {
        "bookingId": 1,
        "username": "nathan",
        "bookedon": "Sun Feb 25 11:46:34 AEDT 2018",
        "bookedFlights": [
            {
                "flightNumber": "UA0932",
                "airline": "United Airlines",
                "departingAirport": "CBR",
                "arrivingAirport": "LAX",
                "departingTime": "Thu Mar 01 09:00:00 AEDT 2018",
                "arrivingTime": "Fri Mar 02 09:00:00 AEDT 2018"
            }
        ]
    },
    {
        "bookingId": 2,
        "username": "nathan",
        "bookedon": "Sun Feb 25 11:46:35 AEDT 2018",
        "bookedFlights": [
            {
                "flightNumber": "QF090",
                "airline": "Qantas",
                "departingAirport": "CBR",
                "arrivingAirport": "TXL",
                "departingTime": "Sun Apr 01 09:00:00 AEST 2018",
                "arrivingTime": "Mon Apr 02 23:00:00 AEST 2018"
            },
            {
                "flightNumber": "QF092",
                "airline": "Qantas",
                "departingAirport": "SXF",
                "arrivingAirport": "CBR",
                "departingTime": "Sun Apr 22 09:00:00 AEST 2018",
                "arrivingTime": "Mon Apr 23 23:00:00 AEST 2018"
            }
        ]
    }
]
```


###### HttpStatus

200 Ok: On success

401 Unauthorised: Invalid credentials. 

404 Not Found: No bookings on file.

###### Example Use

```bash
curl -H "Content-Type: application/json" -X POST -d '{"username":"xyz", "password":"xyz"}' https://localhost:8443/bookings
```


#### /commit POST

Makes a new booking. 

To do so send a POST to the /booking endpoint with the following details in the body of the request:

`username` and `password` authorise the user. 
`flightnumbers` are an array of flights you wish to book.

```json
{
	"username":"username",
	"password":"password",
	"flightnumbers": ["UA0932", "QF090", "QF092"]
}
```


###### Returns

The endpoint should return a URI to the new resource, however the spec did not require a /bookings/{id} GET endpoint. So the URI is not returned. Just the status.

###### HttpStatus

201 Created: On success

401 Unauthorised: Invalid credentials. 

400 Bad Request: If one or more flights do not exist in `com.elevenrax.dal.FlightDb`

###### Example Use

```bash
curl -H "Content-Type: application/json" -X POST -d '{"username":"xyz", "password":"xyz", "flightnumbers":["UA0932", "QF090", "QF092"]}' https://localhost:8443/commit
```


## Integration Tests

To run the tests: 
```bash
# Install Dependency for colorised output
pip3 install termcolor   

# Navigate to root of project
cd PROJECT_ROOT/

# Run tests.
# Will start/stop spring-boot web app itself
# if fails on first launch, run again.
python3 integration-tests.py
```



## com.elevenrax.dal

The Data Access Layer.

This package simulates persistent storage using in-memory data structures. Two are populated with dummy data as we need (1) `CustomerDb`: to verify against when we make/retrieve bookings and (2) `FlightDb`: as a booking is esentially a set of flights tied to a user. See both of those classes for the dummy data provided. 

The `BookingDb` is the datastore that records bookings made by users. 

#### Recommendation

Were this to be developed further, we would need to think more about our data storage solutions. 


Our __CustomerDb__ is Read Heavy. We expect users to sign up, but it is more likely that existing users will be queried to exist as they sign on and make bookings. Our data persistence should therefore prioritise read performance over write performance. 

A Sql Database with a clustered index on `username` is a possible solution. A down side of this approach is that as we add a new user, insertion is slow as we must reform the clustered index on each insert. Also Sql Databases do not do horizontal scaling well.


The __BookingDb__ however is Write Heavy. Whilst users will check on bookings from time-to-time, the performance of storing a booking is the most frequent use-case to consider.

MongoDb is a possible solution. However, as we expect to query bookings from time to time, we would need to design clusters for faster reads. 


The __FlightDb__ would ideally be queried via third-party Restful Apis of partner airlines and we would not need to implement data persistance for this service. 
