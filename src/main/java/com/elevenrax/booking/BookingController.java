package com.elevenrax.booking;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elevenrax.dalmodel.Booking;
import com.elevenrax.model.UserAuth;
import com.elevenrax.utils.IpLogger;
import com.elevenrax.utils.UserValidation;

/**
 * Controller to retrieve existing Bookings.
 */
@RestController
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	/**
	 * The `/bookings` endpoint will take a UserAuth object @see com.elevenrax.model.UserAuth
	 * and then verify the user credentials. On successful verification, an Array of JsonObject's 
	 * will be returned representing Bookings @see com.elevenrax.dalmodel.Booking
	 * 
	 * An unverified user will receive a 401 Unauthorised status.
	 * A valid user without bookings will receive a 404 Not Found status.
	 * 
	 * @param auth - The UserAuth object comprising a username and password to verify user has the
	 * 		right to access the booking
	 * @return Upon success, a JsonArray of JsonObject's representing 
	 */
	@RequestMapping(method=RequestMethod.POST, value="/bookings")
	public ResponseEntity< List<Booking> > getBooking(@RequestBody UserAuth auth) {
				
		boolean isValidUser = UserValidation.isValidUsername(auth);
		if (!isValidUser) {
			logger.info("Invalid username: username=" + auth.getUsername() + ", password=" + 
					auth.getPassword() + ", ip=" + IpLogger.getClientsIP() );
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		boolean isValidPassword = UserValidation.isValidPassword(auth);
		if (!isValidPassword) {
			logger.info("Invalid password: username=" + auth.getUsername() + ", password=" + 
					auth.getPassword() + ", ip=" + IpLogger.getClientsIP() );
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
				
		List<Booking> bookings = bookingService.getAllBookingsForUser(auth.getUsername());
		if (bookings == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(bookings, HttpStatus.OK) ;
	}


}
