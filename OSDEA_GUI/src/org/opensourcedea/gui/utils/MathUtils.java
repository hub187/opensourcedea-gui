package org.opensourcedea.gui.utils;

public class MathUtils {
	
	
	//From http://stackoverflow.com/users/706282/amit
	//at: http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
	public static double round(double valueToRound, int numberOfDecimalPlaces)
	{
	    double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
	    double interestedInZeroDPs = valueToRound * multipicationFactor;
	    return Math.round(interestedInZeroDPs) / multipicationFactor;
	}
	
}
