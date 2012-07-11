package org.opensourcedea.gui.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Dimensions {
	
	/**
	 * Returns an int corresponding to the width of the longest String (in pixels).
	 * @param comp A composite on which a temporary Label can be displayed.
	 * @param varl the ArrayList of String used to calculate the width of the longest string.
	 * @return the width of the longest string
	 */
	public static int getMaxStringWidth(Composite comp, ArrayList<String> varl) {
		
		String longest = "";
		Iterator<String> it = varl.iterator();
		while(it.hasNext()) {
			String temp = it.next();
			if(temp.length() > longest.length()) {
				longest = temp;
			}
		}

		Label tempL = new Label(comp, SWT.NONE);
		tempL.setText(longest);
		
		GC gc = new GC(tempL);
		Point size = gc.textExtent(longest);
		gc.dispose ();

		tempL.dispose();
		
		return size.x;
		
	}
	
	public static int getTotalStringLength(Composite comp, ArrayList<String> varl) {
		
		int extraPerItem = 50;
		int length = extraPerItem;
		Label tempL = null;
		
		for(String str : varl){
			tempL = new Label(comp, SWT.NONE);
			tempL.setText(str);
			
			GC gc = new GC(tempL);
			Point size = gc.textExtent(str);
			length = length + size.x + extraPerItem;
			gc.dispose ();

		}
		
		tempL.dispose();
		
		
		return length;
	}
	
}
