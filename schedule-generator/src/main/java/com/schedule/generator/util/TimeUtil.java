package com.schedule.generator.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {

	public TimeUtil() {
	}
	
	public static String addMinutes(String time, int mins){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");	 
		Date d = null;
		DateTime dt = null;
		DateTime newTime = null;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
		try {
			d = format.parse(time);
			dt = new DateTime(d);
			newTime = dt.plusMinutes(mins);
		 } catch (Exception e) {
			e.printStackTrace();
		 } 
		return fmt.print(newTime);
	}
	
	public static String roundTimeToReduce(String time){
		String startStr =  time.substring(0, (time.length()-1));
		String lastDigitStr = time.substring((time.length()-1), time.length());
		int lastDigitInt = Integer.parseInt(lastDigitStr);
		if (lastDigitInt >=1 && lastDigitInt <=4) {
			lastDigitInt = 0;
		} else if(lastDigitInt >=5 && lastDigitInt <=9) {
			lastDigitInt = 5;
		}

		return startStr + lastDigitInt;
	}
	
	public static String roundTimeToIncrease(String time){
		String startStr =  time.substring(0, (time.length()-2));
		String lastDigitStr = time.substring((time.length()-1), time.length());
		String secondLastDigitStr = time.substring((time.length()-2), (time.length()-1));
		
		int lastDigitInt = Integer.parseInt(lastDigitStr);
		int secondLastDigitInt = Integer.parseInt(secondLastDigitStr);
		
		if (lastDigitInt >=1 && lastDigitInt <=5) {
			lastDigitInt = 5;
		} else if(lastDigitInt >=6 && lastDigitInt <=9) {
			lastDigitInt = 0;
			secondLastDigitInt++;
		}

		if (secondLastDigitInt == 6 && lastDigitInt == 0) {
			secondLastDigitInt = 0;
			lastDigitInt = 0;
			
			String hours = startStr.substring(0, 2);
			Integer newHours = new Integer(hours);
			++newHours;
			
			startStr = newHours + ":";
			if(startStr.equals("24:"))
				startStr = "00:";
		}
		return startStr + secondLastDigitInt + lastDigitInt;
	}
	
	public static int getHoursDifference(Date d1, Date d2){
		int hours = 0;
		try {	 
			DateTime dt1 = new DateTime(d1);
			DateTime dt2 = new DateTime(d2);
	 
			hours = Hours.hoursBetween(dt1, dt2).getHours() % 24;			
		 } catch (Exception e) {
			e.printStackTrace();
		 } 
		return hours;
	}
	
	public static String minusMinutes(String strDate, int mins){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
		Date d = null;
		DateTime dt = null;
		try {
			d = format.parse(strDate);
			dt = new DateTime(d);
			dt = dt.minusMinutes(mins);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return fmt.print(dt);
	}
	
	public static Date stringToTime(String strDate) {
		Date date = null;
		try {
			date = new SimpleDateFormat("hh:mm", Locale.ENGLISH).parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String timeToString(Date date) {
		String strDate = "00:00";
		strDate = new SimpleDateFormat("HH:mm").format(date);
		return strDate;
	}
	
	public static String getDuration(Date date1, Date date2){
		//System.out.println(date1 + "\t" + date2);
		long diff = date2.getTime() - date1.getTime();
        
        if (diff < 0) {
        	diff = date2.getTime() - date1.getTime() + 24*60*60*1000;
        }
        int timeInSeconds = (int)diff / 1000;
        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        String diffTime = (hours<10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
        
		return diffTime;
	}
	
	public static int getMinuteDiffetence(Date date1, Date date2){
		long diff = date2.getTime() - date1.getTime();  
        int timeInSeconds = (int)diff / 1000;
        int minutes;
        minutes = timeInSeconds / 60;        
		return minutes;
	}
}
