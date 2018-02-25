package com.elevenrax.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.elevenrax.dalmodel.*;

/**
 * This represents data persistence for the purposes of the exercise. 
 * This class is used to ensure that we make bookings only for existing users. 
 * Passwords are stored using OpenBSDs Blowfish hashing algorithm. 
 * As the spec does not require user management, no getter/setters are implemented. Out of scope.
 */
public class CustomerDb {
	
    private static CustomerDb instance;

	List<Customer> customers;
	
	private CustomerDb() {
		customers = new ArrayList<>();
		customers.add(new Customer("nathan", "securepassword123", "nate@gmail.com"));
		customers.add(new Customer("pbandj", "ilovemydog", "paula@hotmail.com"));
		customers.add(new Customer("f1estee", "Al4kk0w4s!z", "alex@gmail.com"));
	}
	
    public static CustomerDb getInstance(){
        if(instance == null){
            instance = new CustomerDb();
        }
        return instance;
    }
	
	/**
	 * Determines that a user exists. 
	 * 
	 * @param user - The username to search for
	 * @return Whether a customer exists
	 */
	public boolean userExists(String user) {
		return customers.stream().anyMatch(cust -> cust.getUsername().equals(user));
	}
	
	
	/**
	 * Finds a Customer Object based on a username provided
	 * @param username - The identifier of a customer object
	 * @return The customer object if found, null otherwise
	 */
	public Customer findCustomer(String username) {
		Optional<Customer> customer = customers.stream()
				.filter(cust -> cust.getUsername().equals(username))
				.findFirst();
		
		if (customer.isPresent()) return customer.get();
		else return null;
	}

}
