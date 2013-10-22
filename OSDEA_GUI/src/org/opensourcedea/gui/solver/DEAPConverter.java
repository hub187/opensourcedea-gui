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

			
			//Variables for LDEAP conversion into DEAP
			double[][] sourceDataMatrix = ldeap.getDataMatrix().toArray(new double[nbDMUs][nbSelectedVariables]);
			double[][] destDataMatrix = new double[nbDMUs][nbSelectedVariables];
			
			String[] sourceVariableNames = ldeap.getVariableNames().toArray(new String[nbSelectedVariables]);
			String[] destVariableNames = new String[nbSelectedVariables];
			
			VariableOrientation[] sourceVariableOrientations = ldeap.getVariableOrientation().toArray(new VariableOrientation[nbSelectedVariables]);
			VariableOrientation[] destVariableOrientations = new VariableOrientation[nbSelectedVariables];
			VariableType[] sourceVariableTypes = ldeap.getVariableType().toArray(new VariableType[nbSelectedVariables]);
			VariableType[] destVariableTypes = new VariableType[nbSelectedVariables];
			
			//Copy the interesting values of the ldeap Data Matrix into the deap data matrix 
			int destColIndex = 0;
			for(int j = 0; j < varOr.size();j++){
				if(varOr.get(j) == VariableOrientation.INPUT | varOr.get(j) == VariableOrientation.OUTPUT){
					//we're in a column we need to copy, now let's loop through all the rows
					
					for(int i = 0; i < nbDMUs;i++) {
						System.arraycopy(sourceDataMatrix[i], j, destDataMatrix[i], destColIndex, 1);
					}
					
					destVariableNames[destColIndex] = sourceVariableNames[j];
					destVariableOrientations[destColIndex] = sourceVariableOrientations[j];
					destVariableTypes[destColIndex] = sourceVariableTypes[j];
					destColIndex = destColIndex + 1;
				}
			}
			//Now copy the matrix corresponding to the variables that were selected in on the GUI
			deap.setDataMatrix(destDataMatrix);
			deap.setVariableNames(destVariableNames);
			deap.setDMUNames(ldeap.getDMUNames().toArray(new String[nbDMUs]));
			deap.setModelName(ldeap.getModelName());
			deap.setModelType(ldeap.getModelType());
			deap.setRTSLowerBound(ldeap.getRtsLowerBound());
			deap.setRTSUpperBound(ldeap.getRtsUpperBound());
			deap.setVariableOrientations(destVariableOrientations);
			deap.setVariableTypes(destVariableTypes);
			
			
			
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
