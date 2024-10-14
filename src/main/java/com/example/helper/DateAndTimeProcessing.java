package com.example.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateAndTimeProcessing {


	  public static long convertToUnixTimestamp(String dateStr) {
	        LocalDate date = LocalDate.parse(dateStr);
	        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
	    }

	  public static LocalDate unixToDate(long unixTimestamp) {
	        Instant instant = Instant.ofEpochSecond(unixTimestamp);
	        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
	    }

}
