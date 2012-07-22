package org.opensourcedea.gui.maingui;


import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.dea.SolverReturnStatus;
import org.opensourcedea.gui.parameters.OSDEAParameters;
import org.opensourcedea.gui.solver.DEAPConverter;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class DEAPProblemComposite extends Composite {

	private final StyledText deaPNameText;
	private final Navigation nav;
	private final LDEAProblem ldeap;
	private ProblemStatus probStatus;
	private SolvingProgress progress;
	private FormData fdata;
	
	private ScrolledComposite sComp;
	private Composite comp;
	private Button solveButton;
	private final OSDEA_StatusLine stl;
	
	private Thread solvingThread;


	public DEAPProblemComposite(Composite parentComp, LDEAProblem parentLdeap, Navigation parentNav, final OSDEA_StatusLine stl) {
		super(parentComp, 0);

		ldeap = parentLdeap;
		nav = parentNav;
		this.stl = stl;

		this.setLayout(new GridLayout());



		sComp = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		sComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sComp.setLayout(new FormLayout());

		comp = new Composite(sComp, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(0);
		formData.bottom = new FormAttachment(100);
		comp.setLayoutData(formData);
		comp.setLayout(new FormLayout());


		deaPNameText = new StyledText(comp, SWT.BORDER);
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		fdata.width = 200;
		deaPNameText.setLayoutData(fdata);


		String helpText = "If you need help, hover your mouse over the yellow warning icons.";
		Images.setHelpIcon(comp, helpText, 10, 20);

		//Create the Actions / Status Group
		probStatus = new ProblemStatus(comp, deaPNameText);
		probStatus.setActionsGroup();
		

		solveButton = new Button(comp, SWT.PUSH);
		solveButton.setText("Solve the DEA Problem...");
		fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(probStatus.getRemActionsGroup(), 20);
		solveButton.setEnabled(false);
		solveButton.setLayoutData(fdata);

		solveButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {

			}

			@Override
			public void mouseUp(MouseEvent e) {
				
				DEAPConverter converter = new DEAPConverter();
				final DEAProblem deap = converter.convertLDEAP(nav.getSelectedDEAProblem());
				
				comp.getDisplay().syncExec(new Runnable() {
					public void run() {
						updateLayout();
					}});
				
				if(solvingThread != null){
					if(solvingThread.isAlive()){
						return;
					}
				}
				
				solvingThread = new Thread() {
					public void run() {

						try {

							solve(deap);

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
								
								comp.getDisplay().syncExec(new Runnable() {
									public void run() {
										stl.setStatusLabel("Problem solved successfully");
										solveButton.setText("Problem Solved");
										solveButton.setEnabled(false);
									}});
								
								ldeap.setModified(true);

							}
							else {
								ldeap.setSolved(false);
								comp.getDisplay().syncExec(new Runnable() {
									public void run() {
										MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
												"could not be solved optimally! Check the data.");
									}});
							}
						}
						catch (Exception ex) {
							ex.printStackTrace();
							ldeap.setSolved(false);
							comp.getDisplay().syncExec(new Runnable() {
								public void run() {
									MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
											"could not be solved properly!");
								}});
						}
						
						
						comp.getDisplay().syncExec(new Runnable() {
							public void run() {
								if(ldeap.isSolved()) {
									nav.displaySolution();
								}
							}});
						
						
					}
				};
				
				solvingThread.start();
			
				

			
			}

		});




		progress = new SolvingProgress(comp, probStatus.getRemActionsGroup(), solveButton);


		Realm.runWithDefault(SWTObservables.getRealm(parentComp.getDisplay()), new Runnable() {
			public void run() {

				deaPNameText.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent e) {

					}

					@Override
					public void keyReleased(KeyEvent e) {
						String typedString = deaPNameText.getText();
						int pos = deaPNameText.getCaretOffset();
						String checkedString = IOManagement.deleteInvalidCharacters(typedString);
						if(!typedString.equals(checkedString)) {
							deaPNameText.setText(checkedString);
							if(pos > 1) {
								deaPNameText.setCaretOffset(pos - 1);
								stl.setStatusLabelBlinkThenClear("Invalid Characters: '\\, /, :, *, ?, <, >, |' are not allowed!", 2, 4000);
							}
						}

						nav.setDEAProblemText(checkedString);
					}

				});

				DataBindingContext dbc = new DataBindingContext();

				IObservableValue modelObservable = BeansObservables.observeValue(ldeap.getModelDetails(), "modelName");
				dbc.bindValue(SWTObservables.observeText(deaPNameText, SWT.Modify), modelObservable, null, null);

			}
		});


		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
		sComp.setMinSize(400, 300);


	}




	public void setDataOK() {
		Images.paintCanvas(probStatus.getDataCanvas(), "accept");
		probStatus.getDataLabel().setText("Data were imported successfully.");
		probStatus.getDataLabel().pack();
	}


	public void setVariablesOK() {
		Images.paintCanvas(probStatus.getVariablesCanvas(), "accept");
		probStatus.getVariablesLabel().setText("Variables are configured correctly.");
		probStatus.getVariablesLabel().pack();
	}

	public void setVariablesNOK() {
		Images.paintCanvas(probStatus.getVariablesCanvas(), "error");
		probStatus.getVariablesLabel().setText("Configure the problem variables!");
		probStatus.getVariablesLabel().pack();
		setAllNOK();
	}

	public void setModelDetailsOK() {
		Images.paintCanvas(probStatus.getmodelDetailsCanvas(), "accept");
		probStatus.getmodelDetailsLabel().setText("A DEA Problem was selected.");
		probStatus.getmodelDetailsLabel().pack();
	}

	public void setModelDetailsNOK() {
		Images.paintCanvas(probStatus.getmodelDetailsCanvas(), "error");
		probStatus.getmodelDetailsLabel().setText("Configure the DEA model type!");
		probStatus.getmodelDetailsLabel().pack();
		setAllNOK();
	}

	public void setAllOK() {
		probStatus.getRemActionsGroup().setText("You're all set!");
		solveButton.setEnabled(true);
		stl.setNotificalLabelDelayStandard("You are ready to solve!");

	}

	public void setAllNOK() {
		probStatus.getRemActionsGroup().setText("You still need to:");
		solveButton.setEnabled(false);
	} 

	
	
	
	


	private void solve(final DEAProblem deap) {

		final int nbDMUs = deap.getNumberOfDMUs();

		for(int i = 0; i < nbDMUs; i++) {
			final int pos = i;
			comp.getDisplay().syncExec(new Runnable() {
				public void run() {
					Integer dmu = pos + 1;
					try {
						deap.solveOne(pos);
						Integer perc = 100 * dmu / nbDMUs;
						progress.updateProgressLabelText("Solved DMU " + dmu.toString() + " of " + nbDMUs + " (" + perc.toString() + "%).");
						System.out.println("Solved dmu " + ((Integer)pos).toString());
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

	
	private void updateLayout() {
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.top = new FormAttachment(probStatus.getRemActionsGroup(), 20);
		progress.getProgressGroup().setVisible(true);
		progress.getProgressGroup().setLayoutData(formData);
		
		fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(progress.getProgressGroup(), 20);
		solveButton.setLayoutData(fdata);
		
		comp.layout();
	}
	


	
	public class SolvingThread extends Thread {
		
	}
	




}
