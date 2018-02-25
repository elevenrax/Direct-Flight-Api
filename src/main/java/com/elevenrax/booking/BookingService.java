package com.elevenrax.booking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elevenrax.dal.BookingDb;
import com.elevenrax.dalmodel.Booking;

/**
 * Handles Retrieval of bookings from the BookingsDb
 */
@Service
public class BookingService {
	
	private BookingDb bookingDb;
	
	public BookingService() {
		bookingDb = BookingDb.getInstance();
	}
	
	/**
	 * Get's a given user's bookings
	 * @param username - The user whose bookings we want
	 * @return The bookings a user has made
	 */
	public List<Booking> getAllBookingsForUser(String username) {
		return bookingDb.getBookings(username);
	}

}
