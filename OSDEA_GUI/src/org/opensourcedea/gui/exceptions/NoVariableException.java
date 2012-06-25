package org.opensourcedea.gui.exceptions;

public class NoVariableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoVariableException() {
		super("There is no variable because no data was imported.");
	}

}
