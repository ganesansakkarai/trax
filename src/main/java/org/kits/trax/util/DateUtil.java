package org.kits.trax.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String toString(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static String toString(Long timeStamp) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
		String dateStr = df.format(new Date(timeStamp));
		try {
	        System.out.println(timeStamp + " = " + DateUtil.toLong(dateStr));
        } catch (ParseException e) {
	        e.printStackTrace();
        }
		return dateStr;
	}
	
	public static Long toLong(String dateStr) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
		Long ts = df.parse(dateStr).getTime();
		return ts;
	}

	public static Date toDate(String dateStr) throws ParseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
		return df.parse(dateStr);
	}
}
