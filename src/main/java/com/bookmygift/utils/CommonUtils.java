package com.bookmygift.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtils {

	private CommonUtils() {

	}

	public static final String AUTHORIZATION = "Authorization";
	
	public static HttpServletRequest getRequest() {
		
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
		
	}
	
	

}
