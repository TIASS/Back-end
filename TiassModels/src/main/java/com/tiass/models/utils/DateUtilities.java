package com.tiass.models.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtilities{
	
	public static int getAge(Calendar cal) {
		Calendar now = new GregorianCalendar();
		int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH)) || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
			res--;
		}
		return res;
	}
	
	public static long getAgeDays(Calendar cal, TimeUnit timeUnit) {
		Calendar now = Calendar.getInstance();
		long diffInMillies = now.getTime().getTime() - cal.getTime().getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	/*
	 * public static long getAgeDays(Calendar cal) { Calendar now = new
	 * GregorianCalendar(); int days = 0; try { long mil = now.getTimeInMillis()
	 * - cal.getTimeInMillis(); days = (int) mil / (1000 * 60 * 60 * 24); }
	 * catch (Exception e) { e.printStackTrace(System.out); days = 0; }
	 * 
	 * return days; }
	 */
	
	public static boolean isAfterOrEquals(Calendar cal, Calendar compare) {
		boolean itIs = false;
		
		if (cal != null && compare != null && (cal.after(compare) || (cal.compareTo(compare) > -1))) {
			itIs = true;
		}
		return itIs;
	}
	
	/**
	 * Convert a millisecond duration to a string format
	 * 
	 * @param millis
	 *            A duration to convert to a string form
	 * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
	 */
	public static long[] getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}
		
		long l[] = new long[4];
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		l[0] = days;
		l[1] = hours;
		l[2] = minutes;
		l[3] = seconds;
		
		return l;
	}
	
	public static String formatDurationBreakdown(long days, long hours, long minutes, long seconds, String _days, String _hours, String _minutes, String _seconds, String text, boolean higher) {
		
		if (higher) {
			if (days > 0) {
				hours = 0;
				minutes = 0;
				seconds = 0;
			} else if (hours > 0) {
				minutes = 0;
				seconds = 0;
			} else if (minutes > 0) {
				seconds = 0;
			}
		}
		text = String.format(text, ((days > 0) ? days + " " + _days : ""), ((hours > 0) ? hours + " " + _hours : ""), ((minutes > 0) ? minutes + " " + _minutes : ""), ((seconds > 0) ? seconds + " " + _seconds : ""));
		return text;
	}


	/**
	 * convert date string using format of the user
	 * 
	 * @param c
	 * @param format
	 * @return
	 */
	public static Calendar calendarLongIn(long date, boolean zeroprecision) {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			//System.out.println("calendarLongIn 1 :" + calendar.getTimeInMillis());
			if (zeroprecision) {
				/*
				 * calendar.set(Calendar.HOUR_OF_DAY, 0);
				 * calendar.set(Calendar.MINUTE, 0);
				 * calendar.set(Calendar.SECOND, 0);
				 * calendar.set(Calendar.MILLISECOND, 0);
				 */
			}
			calendar.setLenient(false);
			//System.out.println("calendarLongIn 2 :" + calendar.getTimeInMillis());

			return calendar;
		} catch (IllegalArgumentException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	/**
	 *  
	 * @param date
	 * @return
	 */
	public static Calendar calendarToGMT(Calendar date) {
		if (date == null) {
			return null;
		}
		TimeZone tz = TimeZone.getDefault();
		Date ret = new Date(date.getTimeInMillis() - tz.getRawOffset());
		// if we are now in DST, back off by the delta. Note that we are
		// checking the GMT date, this is the KEY.
		if (tz.inDaylightTime(ret)) {
			Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
			// check to make sure we have not crossed back into standard time
			// this happens when we are on the cusp of DST (7pm the day before
			// the change for PDT)
			if (tz.inDaylightTime(dstDate)) {
				ret = dstDate;
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ret);
		return calendar;
	}
	/**
	 *  
	 * @param date
	 * @param offset
	 * @return
	 */
	public static Calendar calendarFromGMT(Calendar cal, String offset) {
		Calendar calendar = Calendar.getInstance();
		/*
		 * yyyy-MMM-dd HH:mm:ss
		 */
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT" + offset));
		try {
			calendar.setTime(dateFormatGmt.parse(dateFormatGmt.format(cal.getTime())));
		} catch (ParseException e) {
			calendar = null;
			e.printStackTrace(System.out);
		}
		return calendar;

	}
	/**
	 * Gets the End date of a Calendar Instance
	 */
	public static Calendar getEndOfDay(Calendar calendar) {
		if(calendar ==null)
			calendar = Calendar.getInstance();
		
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar ;
	}
	
	/**
	 * Gets the Start date of a Calendar Instance
	 */
	public static Calendar getStartOfDay(Calendar calendar) { 
		if(calendar ==null)
			calendar = Calendar.getInstance();
		
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar ;
	}
}