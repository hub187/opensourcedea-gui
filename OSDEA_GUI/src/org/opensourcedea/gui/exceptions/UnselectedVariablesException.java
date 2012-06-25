package org.opensourcedea.gui.exceptions;

public class UnselectedVariablesException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	
	public UnselectedVariablesException() {
		super("At least one variable was not configured as an input or an output.");
	}
	
	public UnselectedVariablesException(String detailMsg) {
		super(detailMsg);
	}
	
	
}
