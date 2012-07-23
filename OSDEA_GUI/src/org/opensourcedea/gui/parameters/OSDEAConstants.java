package org.opensourcedea.gui.parameters;

public class OSDEAConstants {
	
	public static String solveButtonText = "Solve the DEA Problem...";
	
	public static String getSolvedDMUsProgress(Integer dmu, Integer nbDMUs, Integer perc) {
		return "Solved DMU " + dmu.toString() + " of " + nbDMUs.toString() + " (" + perc.toString() + "%).";
	}
	
}
