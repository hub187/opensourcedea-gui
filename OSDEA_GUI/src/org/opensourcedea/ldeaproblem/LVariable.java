package org.opensourcedea.ldeaproblem;

import java.io.Serializable;
import java.util.ArrayList;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;

public class LVariable implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7420233566317128565L;
	private ArrayList<String> variableName;
	private ArrayList<VariableOrientation> variableOrientation;
	private ArrayList<VariableType> variableType;
	
	
	public ArrayList<String> getVariableNames() {
		return variableName;
	}
	public void setVariableNames(ArrayList<String> variableNames) {
		this.variableName = variableNames;
	}
	
	
	public ArrayList<VariableOrientation> getVariableOrientations() {
		return variableOrientation;
	}
	public void setVariableOrientations(ArrayList<VariableOrientation> variableOrientation) {
		this.variableOrientation = variableOrientation;
	}
	
	
	public ArrayList<VariableType> getVariableTypes() {
		return variableType;
	}
	public void setVariableTypes(ArrayList<VariableType> variableType) {
		this.variableType = variableType;
	}
	
	
}
