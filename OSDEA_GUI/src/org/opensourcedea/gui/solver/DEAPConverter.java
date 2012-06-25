package org.opensourcedea.gui.solver;

import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class DEAPConverter {
	
	
	
	public DEAProblem convertLDEAP(LDEAProblem ldeap) {
		
		if(ldeap == null) {
			return null;
		}
		
		try {
		
		int nbDMUs = ldeap.getDataMatrix().size();
		int nbVariables = ldeap.getVariableNames().size();
		
		DEAProblem deap = new DEAProblem(nbDMUs, nbVariables);
		
		deap.setDataMatrix(ldeap.getDataMatrix().toArray(new double[nbDMUs][nbVariables]));
		deap.setDMUNames(ldeap.getDMUNames().toArray(new String[nbDMUs]));
		deap.setModelName(ldeap.getModelName());
		deap.setModelType(ldeap.getModelType());
		deap.setRTSLowerBound(ldeap.getRtsLowerBound());
		deap.setRTSUpperBound(ldeap.getRtsUpperBound());
		deap.setVariableNames(ldeap.getVariableNames().toArray(new String[nbVariables]));
		deap.setVariableOrientations(ldeap.getVariableOrientation().toArray(new VariableOrientation[nbVariables]));
		deap.setVariableTypes(ldeap.getVariableType().toArray(new VariableType[nbVariables]));
		
		
		return deap;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	
	
	
	
}
