package org.opensourcedea.gui.maingui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.dea.SolverReturnStatus;
import org.opensourcedea.exception.IncompatibleModelTypeException;
import org.opensourcedea.exception.ProblemNotSolvedProperlyException;
import org.opensourcedea.gui.parameters.OSDEAConstants;
import org.opensourcedea.gui.parameters.OSDEAParameters;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class SolvingThread extends Thread {

	private DEAProblem deap;
	private Display display;
	private Navigation nav;
	private OSDEA_StatusLine stl;
	private Button solveButton;
	private volatile boolean stopRequested = false;
	boolean cancelled = true;
	private LDEAProblem ldeap;
	private DEAPProblemComposite comp;
	private SolvingProgress progress;

	public SolvingThread(LDEAProblem ldeap, DEAProblem deap, Display display, Navigation nav, OSDEA_StatusLine stl, Button solveButton,
			DEAPProblemComposite comp, SolvingProgress progress) {
		this.deap = deap;
		this.display = display;
		this.nav = nav;
		this.stl = stl;
		this.solveButton = solveButton;
		this.ldeap = ldeap;
		this.comp = comp;
		this.progress = progress;
	}

	public void run() {

		try {

			final int nbDMUs = deap.getNumberOfDMUs();

			for(int i = 0; i < nbDMUs; i++) {
				if(stopRequested) {
					Thread.currentThread().interrupt();
					display.syncExec(new Runnable() {
							public void run() {
								stl.setNotificalLabelDelayStandard("Solving cancelled.");
								comp.hideProgressGroup();
							}});
					return;
				}
				solve(deap, nbDMUs, i);
			}
			
			display.syncExec(new Runnable() {
				public void run() {
					comp.showProgressGroupSolved();
				}
			});
			
			copyAndDisplaySolution(deap);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			ldeap.setSolved(false);
			display.syncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
							"could not be solved properly!");
					stl.setNotificalLabelDelayStandard("Solving error.");
					comp.hideProgressGroup();
				}});
		}


	}


	public void sendStopRequest() {
		stopRequested = true;

		if (this != null) {
			Thread.currentThread().interrupt();
		}
	}

	
	private void copyAndDisplaySolution(DEAProblem deap) throws ProblemNotSolvedProperlyException, IncompatibleModelTypeException {
		if(deap.getOptimisationStatus() == SolverReturnStatus.OPTIMAL_SOLUTION_FOUND){
			ldeap.setSolved(true);
			ldeap.initDEAPSolution(deap.getNumberOfDMUs(), deap.getNumberOfVariables());
			ldeap.getLdeapSolution().setObjectives(deap.getObjectives());
			ldeap.getLdeapSolution().setProjections(deap.getProjections());
			ldeap.getLdeapSolution().setReferenceSets(deap.getReferenceSet());
			ldeap.getLdeapSolution().setSlacks(deap.getSlacks());
			ldeap.getLdeapSolution().setWeights(deap.getWeight());
			ldeap.getLdeapSolution().setStatus(deap.getOptimisationStatus());

			if(ldeap.getModelType().getReturnToScale() == ReturnsToScale.DECREASING ||
					ldeap.getModelType().getReturnToScale() == ReturnsToScale.INCREASING ||
					ldeap.getModelType().getReturnToScale() == ReturnsToScale.GENERAL) {

				for(int i = 0; i < deap.getlBConvexityConstraintWeights().length; i++){
					ldeap.getLdeapSolution().setlBConvexityConstraintWeights(i, deap.getlBConvexityConstraintWeight(i));
				}
				for(int i = 0; i < deap.getuBConvexityConstraintWeights().length; i++){
					ldeap.getLdeapSolution().setuBConvexityConstraintWeight(i, deap.getuBConvexityConstraintWeight(i));
				}

			}

			if(ldeap.getModelType().getReturnToScale() == ReturnsToScale.VARIABLE) {
				for(int i = 0; i < deap.getU0Weights().length; i++){
					ldeap.getLdeapSolution().setU0Weight(i, deap.getU0Weight(i));
				}
			}

			ldeap.setModified(true);

		}
		else {
			ldeap.setSolved(false);
			display.syncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
							"could not be solved optimally! Check the data.");
				}});
		}
		
		
		display.syncExec(new Runnable() {
			public void run() {
				if(ldeap.isSolved()) {
					nav.displaySolution();
				}
			}});
		
	}
	

	private void solve(final DEAProblem deap, final int nbDMUs, int i) {

		final int pos = i;
		display.syncExec(new Runnable() {
			public void run() {
				Integer dmu = pos + 1;
				try {
					deap.solveOne(pos);
					Integer perc = 100 * dmu / nbDMUs;
					progress.updateProgressLabelText(OSDEAConstants.getSolvedDMUsProgress(dmu, perc, (Integer)nbDMUs));
					//System.out.println("Solved dmu " + ((Integer)pos).toString());
				} catch (Exception e) {
					//
				}

				if (progress.getProgressBar().isDisposed ()) return;
				double prog = OSDEAParameters.getProgressBarMaximum()*(((double)pos+1)/(double)nbDMUs);
				progress.getProgressBar().setSelection((int)prog);

				System.out.println("Updated progress bar" + prog); 

			}});

	}


}

