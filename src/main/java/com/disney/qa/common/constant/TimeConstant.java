package com.disney.qa.common.constant;

import com.zebrunner.carina.utils.Configuration;

public class TimeConstant {
    
    private TimeConstant() {
    }

	public static final int SHORT_TIMEOUT = 5;
	
	public static final int MINIMUM_TIMEOUT = 2;

	public static final int PAGE_OPENING_TIMEOUT = 15;
	
	public static final int LONG_OPENING_TIMEOUT = 30;
	
	public static final int DEFAULT_SWIPE_TIMEOUT = 1000;
	
	public static final int IMPLICIT_TIMEOUT = Configuration.getInt(Configuration.Parameter.EXPLICIT_TIMEOUT) / 3;

}
