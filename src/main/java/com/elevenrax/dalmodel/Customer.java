package com.elevenrax.dalmodel;

import org.mindrot.jbcrypt.BCrypt;

/**
 * A model representing a customer
 */
public class Customer {
	
	private String username;
	private String password;
	private String email;
	
	// Other properties omitted for brevity. Purpose of exercise is the endpoints, 
	// this class is just to simulate a persistent data store
	
	public Customer(String username, String password, String email) {
		this.username = username;
		// Store password using Blowfish hashing code, because we care about our users.
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());		
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Takes the submitted plain-text passwords and then compares hash with hash to authorise.
	 * @param submittedPassword - The user supplied password
	 * @return Whether the supplied password and our password on file have the same hash (and are the same password).
	 */
	public boolean checkPassword(String submittedPassword) {
		if (BCrypt.checkpw(submittedPassword, password)) {
			return true;
		}
		return false;
	}

}
