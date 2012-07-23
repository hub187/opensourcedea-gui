package org.opensourcedea.ldeaproblem;

import java.io.Serializable;
import java.util.ArrayList;

import org.opensourcedea.dea.DEAPSolution;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;



public class LDEAProblem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4244977207238712260L;
	private ArrayList<String> dmuNames;
	private ArrayList<double[]> dataMatrix;
	private LModelDetails modelDetails;
	private LVariable variable;
	private boolean isSolved;
	private boolean isModified;
	private DEAPSolution ldeapSolution;

	



	public LDEAProblem() {
		modelDetails = new LModelDetails();
		variable = new LVariable();
		setModified(true);
		setSolved(false);
		
	}
	
	public void initDEAPSolution(int nbDMUs, int nbVariables) {
		ldeapSolution = new DEAPSolution(nbDMUs, nbVariables);
	}
	
	public void deleteSolution() {
		
	}
	
	
	

	public DEAPSolution getLdeapSolution() {
		return ldeapSolution;
	}




	public void setLdeapSolution(DEAPSolution ldeapSolution) {
		this.ldeapSolution = ldeapSolution;
	}




	public boolean isSolved() {
		return isSolved;
	}


	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}
	
	
	public boolean isModified() {
		return isModified;
	}


	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}


	public LModelDetails getModelDetails() {
		return modelDetails;
	}
	
	public LVariable getLModelVariables() {
		return variable;
	}
	
	
	public void setDMUNames(ArrayList<String> parentDMUNames) {
		dmuNames = parentDMUNames;
	}
	
	public ArrayList<String> getDMUNames() {
		return dmuNames;
	}

	public ArrayList<double[]> getDataMatrix() {
		return dataMatrix;
	}

	public void setDataMatrix(ArrayList<double[]> dataMatrix) {
		this.dataMatrix = dataMatrix;
	}
	
	
	
	
	
	public ModelType getModelType() {
		return modelDetails.getModelType();
	}
	public void setModelType(ModelType modelType) {
		this.modelDetails.setModelType(modelType);
	}
	
	
	public double getRtsLowerBound() {
		return modelDetails.getRtsLB();
	}
	public void setRtsLowerBound(double rtsLowerBound) {
		this.modelDetails.setRtsLB(rtsLowerBound);
	}
	
	
	public double getRtsUpperBound() {
		return modelDetails.getRtsUB();
	}
	public void setRtsUpperBound(double rtsUpperBound) {
		this.modelDetails.setRtsUB(rtsUpperBound);
	}
	
	

	
	public String getModelName() {
		return this.modelDetails.getModelName();
	}
	public void setModelName(String modelName) {
		this.modelDetails.setModelName(modelName);
	}
	
	
	
	
	
	public ArrayList<String> getVariableNames() {
		return variable.getVariableNames();
	}
	public void setVariableNames(ArrayList<String> variableNames) {
		this.variable.setVariableNames(variableNames);
	}
	
	
	public ArrayList<VariableOrientation> getVariableOrientation() {
		return this.variable.getVariableOrientations();
	}
	public void setVariableOrientation(ArrayList<VariableOrientation> variableOrientation) {
		this.variable.setVariableOrientations(variableOrientation);
	}
	
	
	public ArrayList<VariableType> getVariableType() {
		return this.variable.getVariableTypes();
	}
	public void setVariableType(ArrayList<VariableType> variableType) {
		this.variable.setVariableTypes(variableType);
	}

	
	
}
