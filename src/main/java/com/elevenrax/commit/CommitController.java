package com.elevenrax.commit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elevenrax.model.UserBooking;
import com.elevenrax.utils.IpLogger;
import com.elevenrax.utils.UserValidation;

/**
 * Controller for committing new Bookings
 */
@RestController
public class CommitController {
	
	@Autowired
	private CommitService commitService;
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    
    /**
     * The `/commit` endpoint will take a UserBooking object @see com.elevenrax.model.UserBooking
     * and then verify the user creditials supplied. On successful verification, the supplied flightnumbers
     * will be verified. If all flights exist within FlightDb, the booking will be made. Else, no booking will be made.
     * Ideally, this method should return the Uri to the new resource, but as the spec does not have an /bookings/{id} endpoint,
     * this cannot be done. Therefore we return only status codes. 
     * 
     * @param booking - The Booking object comprising username, password and a List of flights the user wishes to book
     * @return 201 Created on success. 400 if a non-existent flight is POSTed. 401 if incorrect user credentials. 
     */
	@RequestMapping(method=RequestMethod.POST, value="/commit")
	public ResponseEntity<Void> commit(@RequestBody UserBooking booking) {
		
		boolean isValidUser = UserValidation.isValidUsername(booking);
		if (!isValidUser) {
			logger.info("Invalid username: username=" + booking.getUsername() + ", password=" + 
					booking.getPassword() + ", ip=" + IpLogger.getClientsIP() );
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		boolean isValidPassword = UserValidation.isValidPassword(booking);
		if (!isValidPassword) {
			logger.info("Invalid password: username=" + booking.getUsername() + ", password=" + 
					booking.getPassword() + ", ip=" + IpLogger.getClientsIP() );
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		
		boolean isBooked = commitService.makeBookingForAuthorisedUser(booking);
		
		if (isBooked) return new ResponseEntity<>(null, HttpStatus.CREATED);
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			
	}

}
