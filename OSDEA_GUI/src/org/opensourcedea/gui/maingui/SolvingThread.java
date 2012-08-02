package org.opensourcedea.gui.maingui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.exception.IncompatibleModelTypeException;
import org.opensourcedea.exception.ProblemNotSolvedProperlyException;
import org.opensourcedea.gui.parameters.OSDEAConstants;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class SolvingThread extends Thread {

	private DEAProblem deap;
	private Display display;
	private Navigation nav;
	private OSDEA_StatusLine stl;
	private volatile boolean stopRequested = false;
	private volatile boolean errorStop = false;
	private String errStr = "";
	private LDEAProblem ldeap;
	private DEAPProblemComposite comp;
	private SolvingProgress progress;



	public SolvingThread(LDEAProblem ldeap, DEAProblem deap, Display display, Navigation nav, OSDEA_StatusLine stl, Button solveButton,
			DEAPProblemComposite comp, SolvingProgress progress) {

		this.deap = deap;
		this.display = display;
		this.nav = nav;
		this.stl = stl;
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
							comp.showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
						}});
					return;
				}
				if(errorStop) {
					Thread.currentThread().interrupt();
					display.syncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(nav.getShell(), "Error!", errStr);
							stl.setNotificalLabelDelayStandard("Solving error!");
							comp.showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
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
					comp.showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
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

		ldeap.copyLDEAPSolution(deap);

		display.syncExec(new Runnable() {
			public void run() {
				if(ldeap.isSolved()) {
					stl.setStatusLabel("Problem Solved");
					nav.displaySolution();
				}
				else {
					MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
							"could not be solved optimally! Check the data.");
				}
			}});

	}


	private void solve(final DEAProblem deap, final int nbDMUs, int i) {
		
		final int pos = i;
		final Integer dmu = pos + 1;
		try {
			deap.solveOne(pos);
		} catch (Exception e) {
			errorStop = true;
			errStr = "There was an error while solving the problem. It is likely this is caused by some inconsistencies in the data.";
			if (this != null) {
				Thread.currentThread().interrupt();
			}
			return;
		}
		catch (Error e1) { //java.lang.NoClassDefFoundError || UnsatisfiedLinkError
			errorStop = true;
			errStr = "The lpsolve library could not be found. Please check the installation instructions and make sure the lpsolve libraries " +
					"are correctly installed on your system and restart OSDEA.";
			if (this != null) {
				Thread.currentThread().interrupt();
			}
			return;
		}
		display.syncExec(new Runnable() {
			public void run() {
				Integer perc = 100 * dmu / nbDMUs;
				progress.updateProgressLabelText(OSDEAConstants.getSolvedDMUsProgress(dmu, (Integer)nbDMUs, perc));

				progress.setProgressBar(((double)pos+1)/(double)nbDMUs);

			}});

	}


}

