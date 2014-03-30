package org.kits.trax.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String toString(Date date) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(date);	    
	}
	
	public static Date toDate(String dateStr) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    return df.parse(dateStr);
	}
}
