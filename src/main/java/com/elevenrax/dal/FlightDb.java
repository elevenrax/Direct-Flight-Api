package com.elevenrax.dal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.elevenrax.dalmodel.Flight;

/**
 * A Mock Datastore of flights for purposes of activity.
 * Purpose is to provide a repository of Flights to book against. 
 * Class only offers barebones features.
 *
 */
public class FlightDb {
	
    private static FlightDb instance;
	
	Map<String, Flight> flights;
	
	private FlightDb() {
		flights = new HashMap<>();
		
		/*
		 *  Populate with Dummy Data
		 */
		
		
		// Canberra to L.A
		Flight flight = new Flight(
			"UA0932",
			"United Airlines",
			"CBR",
			"LAX",
			getUnixTime("01 Mar 2018 09:00"),
			getUnixTime("02 Mar 2018 09:00"));
		
		flights.put(flight.getFlightNumber(), flight);
		
		// Canberra to Berlin(TXL)
		flight = new Flight(
				"QF090",
				"Qantas",
				"CBR",
				"TXL",
				getUnixTime("01 Apr 2018 09:00"),
				getUnixTime("02 Apr 2018 23:00"));
		
		flights.put(flight.getFlightNumber(), flight);
			
		// Canberra to Berlin(SXF)
		flight = new Flight(
				"QF092",
				"Qantas",
				"SXF",
				"CBR",
				getUnixTime("22 Apr 2018 09:00"),
				getUnixTime("23 Apr 2018 23:00"));
		
		flights.put(flight.getFlightNumber(), flight);
			
		// Canberra to Nadi, Fiji
		flight = new Flight(
				"VA0252",
				"Virgin Airlines",
				"CBR",
				"NAN",
				getUnixTime("02 Mar 2018 09:00"),
				getUnixTime("02 Mar 2018 11:00"));
		
		flights.put(flight.getFlightNumber(), flight);	
		
		// Canberra to Hong Kong
		flight = new Flight(
				"HX3000",
				"Hong Kong Airlines",
				"CBR",
				"HKG",
				getUnixTime("02 Nov 2018 14:30"),
				getUnixTime("02 Nov 2018 17:00"));
		
		flights.put(flight.getFlightNumber(), flight);	
	}
	
    public static FlightDb getInstance(){
        if(instance == null){
            instance = new FlightDb();
        }
        return instance;
    }
	
	public Flight getFlight(String flightNumber) {
		return flights.get(flightNumber);
	}
	
	public boolean flightExists(String flightNumber) {
		Flight flight = flights.get(flightNumber);
		if (flight == null) return false;
		return true;
	}

	
	/**
	 * Converts a string date-time into Unix time
	 * @param dateString - A string of form dd MMM yyyy hh:mm z
	 * @return The corresponding unix timestamp or -1 on a failed conversion
	 */
	private long getUnixTime(String dateString) {
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm");
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			long unixTime = (long) date.getTime()/1000;
			return unixTime;
		}
		return -1;
	}

}
