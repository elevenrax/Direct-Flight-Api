package com.elevenrax.utils;

import com.elevenrax.dal.CustomerDb;
import com.elevenrax.dalmodel.Customer;
import com.elevenrax.model.UserAuth;

/**
 * Handles User validation. To be used amongst Controllers where 
 * user authentication is needed.
 */
public class UserValidation {
	
	/**
	 * Confirms a `username` corresponds to an account.
	 * @param auth - The UserAuth object send in the RequestBody
	 * @return Whether the username corresponds to an account.
	 */
	public static boolean isValidUsername(UserAuth auth) {
		Customer cust = CustomerDb.getInstance().findCustomer(auth.getUsername());
		if (cust == null) return false;
		return true;
	}
	
	/**
	 * Confirms that a valid password has been supplied for the supplied username
	 * @param auth - The UserAuth object send in the RequestBody
	 * @return Whether the password supplied is valid given the supplied username
	 */
	public static boolean isValidPassword(UserAuth auth) {
		Customer cust = CustomerDb.getInstance().findCustomer(auth.getUsername());
		return cust.checkPassword(auth.getPassword());
	}

}
