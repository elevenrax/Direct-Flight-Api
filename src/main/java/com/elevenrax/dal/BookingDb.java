package com.elevenrax.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elevenrax.dalmodel.Booking;

/**
 * This represents Data Persistence for the purposes of the exercise and is done in-memory.
 * As the two end points 1. get all bookings for a user, and 2. commit a new booking. 
 *    This class does not offer functionality to delete, update etc. as that functionality is out of scope.
 */
public class BookingDb {
	
    private static BookingDb instance;

	
	// Repository of all bookings
	private Map<String, List<Booking>> bookings;
	
	private BookingDb() {
		bookings = new HashMap<>();
	}
	
	
    public static BookingDb getInstance(){
        if(instance == null){
            instance = new BookingDb();
        }
        return instance;
    }
	
	/**
	 * Adds a booking to the database.
	 * @param username - The user who is making the booking
	 * @param booking - An object representing a booking
	 * @return Whether the booking was successfully made
	 */
	public boolean addBooking(String username, Booking booking) {
		List<Booking> userBookings = bookings.get(username);
		if (userBookings == null) {
			userBookings = new ArrayList<>();
			userBookings.add(booking);
			bookings.put(username, userBookings);
			return true;
		}
		else {
			userBookings.add(booking);
			return true;
		}
	}
	
	
	/**
	 * Gets all bookings for a given user
	 * @param username - The user whose bookings we want to retrieve
	 * @return The complete list of bookings or null if no booking exists for a user.
	 */
	public List<Booking> getBookings(String username) {
		if (bookings.containsKey(username)) {
			return bookings.get(username);
		}
		return null;
	}

}
