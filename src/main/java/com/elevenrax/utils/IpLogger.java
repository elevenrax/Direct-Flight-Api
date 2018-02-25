package com.elevenrax.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IpLogger {
	
	/**
	 * Obtains the client's IP address for logging unsuccessful login attempts. 
	 * @return The IP address of the requesting client.
	 */
	public static String getClientsIP() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		return request.getRemoteAddr();
	}

}
