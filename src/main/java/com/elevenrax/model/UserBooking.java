package com.elevenrax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for RequestBody of /commit
 */
public class UserBooking extends UserAuth {

	// List of flights user wishes to book
	private List<String> flightnumbers;
	

	public UserBooking() {
		super();
		flightnumbers = new ArrayList<>();
	}

	public UserBooking(String username, String password) {
		super(username, password);
		flightnumbers = new ArrayList<>();
	}
	
	public UserBooking(String username, String password, List<String> flightnumbers) {
		super(username, password);
		this.flightnumbers = flightnumbers;
	}

	public List<String> getFlightnumbers() {
		return flightnumbers;
	}

	public void addFlight(String flightNumber) {
		flightnumbers.add(flightNumber);
	}

	public void setFlightnumbers(List<String> flightNumbers) {
		this.flightnumbers = flightNumbers;
	}
	
	public boolean hasFlights() {
		return flightnumbers.size() > 0;
	}
	
}
