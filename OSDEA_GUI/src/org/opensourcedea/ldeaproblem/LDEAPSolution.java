
/*	<DEASolver (googleproject opensourcedea) is a java DEA solver.>
    Copyright (C) <2010>  <Hubert Paul Bernard VIRTOS>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    
    @author Hubert Paul Bernard VIRTOS
    
*/

package org.opensourcedea.ldeaproblem;

import java.util.ArrayList;
import java.io.Serializable;

import org.opensourcedea.dea.NonZeroLambda;
import org.opensourcedea.dea.SolverReturnStatus;


/**
 * This class defines a DEA Problem Solution. All the methods in this class are used 
 * to set or get part of a DEA Problem Solution (DEAPSolution).
 * </br>
 * @author Hubert Virtos
 *
 */
public class LDEAPSolution implements Serializable {
	
	/**
	 * The auto-generated serial version UID
	 */
	private static final long serialVersionUID = 2402167210320751288L;
	
	private ArrayList<Double> objectives;
	private ArrayList<double[]> slacks;
	private ArrayList<NonZeroLambda>[] referenceSets;
	private ArrayList<double[]> weights;
	private ArrayList<double[]> u0Weights;
	private ArrayList<double[]> lBConvexityConstraintWeights;
	private ArrayList<double[]> uBConvexityConstraintWeights;
	private ArrayList<double[]> projections;
	private SolverReturnStatus status;
	
	
	
	public void deleteSolution() {
		
	}
	
	
	public ArrayList<Double> getObjectives() {
		return objectives;
	}
	public void setObjectives(ArrayList<Double> objectives) {
		this.objectives = objectives;
	}
	
	
	public ArrayList<double[]> getSlacks() {
		return slacks;
	}
	public void setSlacks(ArrayList<double[]> slacks) {
		this.slacks = slacks;
	}
	
	
	public ArrayList<NonZeroLambda>[] getReferenceSets() {
		return referenceSets;
	}
	public void setReferenceSets(ArrayList<NonZeroLambda>[] referenceSets) {
		this.referenceSets = referenceSets;
	}
	
	
	public ArrayList<double[]> getWeights() {
		return weights;
	}
	public void setWeights(ArrayList<double[]> weights) {
		this.weights = weights;
	}
	
	
	public ArrayList<double[]> getU0Weights() {
		return u0Weights;
	}
	public void setU0Weights(ArrayList<double[]> u0Weights) {
		this.u0Weights = u0Weights;
	}
	
	
	public ArrayList<double[]> getlBConvexityConstraintWeights() {
		return lBConvexityConstraintWeights;
	}
	public void setlBConvexityConstraintWeights(
			ArrayList<double[]> lBConvexityConstraintWeights) {
		this.lBConvexityConstraintWeights = lBConvexityConstraintWeights;
	}
	
	
	public ArrayList<double[]> getuBConvexityConstraintWeights() {
		return uBConvexityConstraintWeights;
	}
	public void setuBConvexityConstraintWeights(
			ArrayList<double[]> uBConvexityConstraintWeights) {
		this.uBConvexityConstraintWeights = uBConvexityConstraintWeights;
	}
	
	
	public ArrayList<double[]> getProjections() {
		return projections;
	}
	public void setProjections(ArrayList<double[]> projections) {
		this.projections = projections;
	}
	
	
	public SolverReturnStatus getStatus() {
		return status;
	}
	public void setStatus(SolverReturnStatus status) {
		this.status = status;
	}
	




	
	
	
}

