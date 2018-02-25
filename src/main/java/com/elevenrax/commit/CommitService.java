package com.elevenrax.commit;

import org.springframework.stereotype.Service;

import com.elevenrax.dal.*;
import com.elevenrax.dalmodel.Booking;
import com.elevenrax.dalmodel.Flight;
import com.elevenrax.model.UserBooking;

/**
 * Business Logic for the Commit Controller.
 * Checks User requested flights against our flightDb, if all flights requested exist
 * and are valid, we make the booking. Else, we do not make the booking. 
 */
@Service
public class CommitService {
	
	private BookingDb bookingDb;
	private FlightDb flightDb;
	
	public CommitService() {
		bookingDb = BookingDb.getInstance();
		flightDb = FlightDb.getInstance();
	}
	
	/**
	 * Checks User requested flights in `booking` against our flightDb, if all flights requested exist
	 * and are valid, we make the booking. Otherwise, we do not make the booking. 
	 * @param booking - @see com.elevenrax.model.UserBooking 
	 * @return
	 */
	public boolean makeBookingForAuthorisedUser(UserBooking booking) {
		
		if (!booking.hasFlights()) return false;
		
		for (String userRequestedFlight : booking.getFlightnumbers()) {
			if (!flightDb.flightExists(userRequestedFlight)) {
				return false;
			}
		}
		
		Booking userBooking = new Booking(booking.getUsername());
		for (String userRequestedFlight : booking.getFlightnumbers()) {
			Flight requestedFlight = flightDb.getFlight(userRequestedFlight);
			userBooking.addBookedFlights(requestedFlight);
		}
	
		bookingDb.addBooking(booking.getUsername(), userBooking);
		
		return true;
	}

}
