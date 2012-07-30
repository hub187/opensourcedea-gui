package org.opensourcedea.gui.utils;


public class Sys {
	
	public static OS getOS() {
		if(System.getProperty("os.name").startsWith("Windows")) {
			return OS.WINDOWS;
		}
		else {
			return OS.LINUX;
		}
	}
	
}
