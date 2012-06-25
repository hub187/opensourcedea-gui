package org.opensourcedea.gui.exceptions;

public class UnvalidVariableChoiceException extends Exception {
	
	
	private static final long serialVersionUID = 1L;
	
	
	public UnvalidVariableChoiceException() {
		super("There is either no input or no output.");
	}
	
	
}
