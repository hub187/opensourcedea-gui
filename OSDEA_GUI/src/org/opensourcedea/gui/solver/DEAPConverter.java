package org.opensourcedea.gui.solver;

import java.util.ArrayList;

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
			
			ArrayList<VariableOrientation> varOr = ldeap.getVariableOrientation();
			int nbSelectedVariables = 0;
			
			for(int i = 0; i < varOr.size();i++){
				if(varOr.get(i) == VariableOrientation.INPUT | varOr.get(i) == VariableOrientation.OUTPUT){
					nbSelectedVariables = nbSelectedVariables + 1;
				}
			}

			
			
			
			
			int nbDMUs = ldeap.getDataMatrix().size();
			DEAProblem deap = new DEAProblem(nbDMUs, nbSelectedVariables);

			
			
			//PICK UP HERE
			deap.setDataMatrix(ldeap.getDataMatrix().toArray(new double[nbDMUs][nbSelectedVariables]));
			
			
			
			
			double[][] dataMatrix = new double[nbDMUs][nbSelectedVariables];
			
			
			
			
			deap.setDMUNames(ldeap.getDMUNames().toArray(new String[nbDMUs]));
			deap.setModelName(ldeap.getModelName());
			deap.setModelType(ldeap.getModelType());
			deap.setRTSLowerBound(ldeap.getRtsLowerBound());
			deap.setRTSUpperBound(ldeap.getRtsUpperBound());
			deap.setVariableNames(ldeap.getVariableNames().toArray(new String[nbSelectedVariables]));
			deap.setVariableOrientations(ldeap.getVariableOrientation().toArray(new VariableOrientation[nbSelectedVariables]));
			deap.setVariableTypes(ldeap.getVariableType().toArray(new VariableType[nbSelectedVariables]));
			
			
			
//			int nbDMUs = ldeap.getDataMatrix().size();
//			int nbVariables = ldeap.getVariableNames().size();
//
//			DEAProblem deap = new DEAProblem(nbDMUs, nbVariables);
//
//			deap.setDataMatrix(ldeap.getDataMatrix().toArray(new double[nbDMUs][nbVariables]));
//			deap.setDMUNames(ldeap.getDMUNames().toArray(new String[nbDMUs]));
//			deap.setModelName(ldeap.getModelName());
//			deap.setModelType(ldeap.getModelType());
//			deap.setRTSLowerBound(ldeap.getRtsLowerBound());
//			deap.setRTSUpperBound(ldeap.getRtsUpperBound());
//			deap.setVariableNames(ldeap.getVariableNames().toArray(new String[nbVariables]));
//			deap.setVariableOrientations(ldeap.getVariableOrientation().toArray(new VariableOrientation[nbVariables]));
//			deap.setVariableTypes(ldeap.getVariableType().toArray(new VariableType[nbVariables]));


			return deap;
		}
		catch (Exception e) {
			return null;
		}
	}





}
