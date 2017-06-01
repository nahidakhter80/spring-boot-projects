package com.schedule.generator.util;

import java.util.Comparator;

public class NumericStringComparator implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		Integer num1 = new Integer(s1);
		Integer num2 = new Integer(s2);
		
		return num1.compareTo(num2);
	}

}
