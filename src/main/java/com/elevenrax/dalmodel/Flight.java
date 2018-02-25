package com.elevenrax.dalmodel;

import java.util.Date;

/**
 * A model representing a flight on offer
 */
public class Flight {
	
	// UNIQUE (across all airlines) flight number
	private String flightNumber; 
	// Name of Airline
	private String airline;
	// Departing Airport for Flight 
	private String departingAirport;
	// Arriving Airport for Flight
	private String arrivingAirport;
	// Departing time in Unix Time
	private long departingTime;	
	// Arriving time in Unix Time
	private long arrivingTime;

	
	public Flight(String flightNumber, String airline, String departingAirport, String arrivingAirport,
			long departingTime, long arrivingTime) {
		this.flightNumber = flightNumber;
		this.airline = airline;
		this.departingAirport = departingAirport;
		this.arrivingAirport = arrivingAirport;
		this.departingTime = departingTime;
		this.arrivingTime = arrivingTime;
	}


	public String getFlightNumber() {
		return flightNumber;
	}


	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}


	public String getAirline() {
		return airline;
	}


	public void setAirline(String airline) {
		this.airline = airline;
	}


	public String getDepartingAirport() {
		return departingAirport;
	}


	public void setDepartingAirport(String departingAirport) {
		this.departingAirport = departingAirport;
	}


	public String getArrivingAirport() {
		return arrivingAirport;
	}


	public void setArrivingAirport(String arrivingAirport) {
		this.arrivingAirport = arrivingAirport;
	}


	public String getDepartingTime() {
		return getUserReadableDateTime(departingTime);
	}


	public void setDepartingTime(long departingTime) {
		this.departingTime = departingTime;
	}

	
	public String getArrivingTime() {
		return getUserReadableDateTime(arrivingTime);
	}


	public void setArrivingTime(long arrivingTime) {
		this.arrivingTime = arrivingTime;
	}

	// Converts UnixTime into human-readable time
	public String getUserReadableDateTime(long timestamp) {
		if (timestamp == -1) return "";
		Date dateTime = new Date((long)timestamp*1000);
		return dateTime.toString();
	}
}
