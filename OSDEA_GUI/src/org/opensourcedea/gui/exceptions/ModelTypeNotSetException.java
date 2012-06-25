package org.opensourcedea.gui.exceptions;

public class ModelTypeNotSetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ModelTypeNotSetException() {
		super("The Model Type was not set in the Model Details section.");
	}

}
