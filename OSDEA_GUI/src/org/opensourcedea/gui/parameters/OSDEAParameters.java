package org.opensourcedea.gui.parameters;

public class OSDEAParameters {
	
	private static int progressBarMaximum = 1000;
	private static int roundingDecimals = 3;
	private static int decimalsToEvaluateEfficiency = 8;
	
	public static int getProgressBarMaximum() {
		return progressBarMaximum;
	}

	public static void setProgressBarMaximum(int progressBarMaximum) {
		OSDEAParameters.progressBarMaximum = progressBarMaximum;
	}

	public static int getRoundingDecimals() {
		return roundingDecimals;
	}

	public static void setRoundingDecimals(int roundingDecimals) {
		OSDEAParameters.roundingDecimals = roundingDecimals;
	}

	public static int getDecimalsToEvaluateEfficiency() {
		return decimalsToEvaluateEfficiency;
	}

	public static void setDecimalsToEvaluateEfficiency(
			int decimalsToEvaluateEfficiency) {
		OSDEAParameters.decimalsToEvaluateEfficiency = decimalsToEvaluateEfficiency;
	}
	
}
