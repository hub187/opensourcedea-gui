package org.opensourcedea.ldeaproblem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.opensourcedea.dea.DEAPSolution;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.NonZeroLambda;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.dea.SolverReturnStatus;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;
import org.opensourcedea.exception.IncompatibleModelTypeException;
import org.opensourcedea.exception.ProblemNotSolvedProperlyException;



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


	public void copyLDEAPSolution(DEAProblem deap) throws ProblemNotSolvedProperlyException, IncompatibleModelTypeException {
		if(deap.getOptimisationStatus() == SolverReturnStatus.OPTIMAL_SOLUTION_FOUND){
			setSolved(true);
			initDEAPSolution(deap.getNumberOfDMUs(), deap.getNumberOfVariables());
			getLdeapSolution().setObjectives(deap.getObjectives());
			getLdeapSolution().setProjections(deap.getProjections());
			getLdeapSolution().setReferenceSets(deap.getReferenceSet());
			getLdeapSolution().setSlacks(deap.getSlacks());
			getLdeapSolution().setWeights(deap.getWeight());
			getLdeapSolution().setStatus(deap.getOptimisationStatus());
			getLdeapSolution().setEfficient(deap.getEfficiencyStatus());

			if(getModelType().getReturnToScale() == ReturnsToScale.DECREASING ||
					getModelType().getReturnToScale() == ReturnsToScale.INCREASING ||
					getModelType().getReturnToScale() == ReturnsToScale.GENERAL) {

				for(int i = 0; i < deap.getlBConvexityConstraintWeights().length; i++){
					getLdeapSolution().setlBConvexityConstraintWeights(i, deap.getlBConvexityConstraintWeight(i));
				}
				for(int i = 0; i < deap.getuBConvexityConstraintWeights().length; i++){
					getLdeapSolution().setuBConvexityConstraintWeight(i, deap.getuBConvexityConstraintWeight(i));
				}

			}

			if(getModelType().getReturnToScale() == ReturnsToScale.VARIABLE) {
				for(int i = 0; i < deap.getU0Weights().length; i++){
					getLdeapSolution().setU0Weight(i, deap.getU0Weight(i));
				}
			}


			setModified(true);

		}
		else {
			setSolved(false);
		}
	}


	public void setLdeapSolution(DEAPSolution ldeapSolution) {
		this.ldeapSolution = ldeapSolution;
	}


	public DEAPSolution getLdeapSolution() {
		return ldeapSolution;
	}


	public void deleteSolution() {
		ldeapSolution = null;
	}


	public Integer getNumberOfVariables() {
		if(getVariableNames() != null) {
			return getVariableNames().size();
		}
		else {
			return null;
		}

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


	/**
	 * Returns the sorted list of efficient DMUs that are found at least once in the reference set of non-efficient DMUs.
	 * @return ArrayList<Integer> returnEfficientReferencedDMUs
	 */
	public ArrayList<Integer> returnEfficientReferencedDMUs() {

		ArrayList<Integer> efficientReferencedDMUs = new ArrayList<Integer>();
		for(int i = 0; i < getLdeapSolution().getReferenceSet().length; i++) {
			Iterator<NonZeroLambda> it = getLdeapSolution().getReferenceSet()[i].iterator();
			while(it.hasNext()) {
				NonZeroLambda tempNzl = it.next();
				if(!efficientReferencedDMUs.contains(tempNzl.getDMUIndex())){
					efficientReferencedDMUs.add(tempNzl.getDMUIndex());
				}
			}
		}
		Collections.sort(efficientReferencedDMUs);

		return efficientReferencedDMUs;
	}


	/**
	 * Returns the processed lambdas. The process lambdas take the form of a matrix where each row corresponds to a DMU (DMU index is preserved) 
	 * and each column corresponds to the efficient DMUs (sorted list). The value in each cell is the double corresponding to each non-zero lambda.
	 * Please note that it is necessary to add the DMU Name if this matrix is to be displayed.
	 * @return ArrayList<ArrayList<Double>> returnProcessedLambdas
	 */
	public ArrayList<ArrayList<Double>> returnProcessedLambdas() {

		ArrayList<Integer> efficientReferencedDMUs = returnEfficientReferencedDMUs();

		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < getLdeapSolution().getReferenceSet().length; i++) {
			Iterator<NonZeroLambda> itNzl = getLdeapSolution().getReferenceSet()[i].iterator();
			HashMap<Integer, Double> nzlHashMap = new HashMap<Integer, Double>();
			while(itNzl.hasNext()) {
				NonZeroLambda tempNzl = itNzl.next();
				nzlHashMap.put(tempNzl.getDMUIndex(), tempNzl.getLambdaValue());
			}

			ArrayList<Double> tempArr = new ArrayList<Double>();
			for(int j = 0; j < efficientReferencedDMUs.size(); j++) {
				if(nzlHashMap.containsKey(efficientReferencedDMUs.get(j))) {
					tempArr.add(nzlHashMap.get(efficientReferencedDMUs.get(j)));
				}
				else {
					tempArr.add(0.0);
				}
			}

			data.add(tempArr);
		}
		return data;
	}


	public ArrayList<ArrayList<String>> returnPeerGroup() {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < getLdeapSolution().getReferenceSet().length; i++) {
			ArrayList<String> tempArr = new ArrayList<String>();
			tempArr.add(getDMUNames().get(i));

			StringBuffer strb = new StringBuffer();
			Iterator<NonZeroLambda> nzl = getLdeapSolution().getReferenceSet(i).iterator();

			while(nzl.hasNext()) {
				strb.append(getDMUNames().get(nzl.next().getDMUIndex()));
				if(nzl.hasNext()) {
					strb.append(", ");
				}
				else {
					strb.append(".");
				}
			}

			tempArr.add(strb.toString());

			data.add(tempArr);

		}

		return data;

	}


}
