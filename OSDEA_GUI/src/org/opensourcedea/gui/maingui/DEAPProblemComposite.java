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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.opensourcedea.dea.DEAProblem;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.dea.SolverReturnStatus;
import org.opensourcedea.gui.solver.DEAPConverter;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class DEAPProblemComposite extends Composite {

	private final StyledText deaPNameText;
	private final Navigation nav;
	private final LDEAProblem ldeap;
	private Group remActionsGroup;
	private FormData fdata;
	private Canvas dataCanvas;
	private Label dataLabel;
	private Label variablesLabel;
	private Canvas variablesCanvas;
	private Label modelDetailsLabel;
	private Canvas modelDetailsCanvas;
	private ScrolledComposite sComp;
	private Composite comp;
	private Button solveButton;
	private final OSDEA_StatusLine stl;
	
	private final String dataHelp = "In order to import some data, you need to click Tools/import or click on the import icon in the ToolBar.\n" +
			"This will open a wizard which will allow you to import data from a csv file.";
	private final String varHelp = "In order to configure the variables, you need to have imported some data.\n" +
			"Once data has been imported, select the Variables tree item on the right hand side and assign each variable to a box.\n" +
			"All variables need to be assigned and there must be at least 1 input. and 1 output.";
	private final String modelHelp = "In order to select a model type, select the Model Details tree item on the right.\n" +
			"You can then select a model type from the main Combo Box .\n" +
			"If you want, you can use the filters to help you in selecting a model type.";
	
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
		setActionsGroup();

		
		solveButton = new Button(comp, SWT.PUSH);
		solveButton.setText("Solve the DEA Problem...");
		fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(remActionsGroup, 20);
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
				DEAProblem deap = converter.convertLDEAP(nav.getSelectedDEAProblem());
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
						
						stl.setStatusLabel("Problem solved successfully");
						
						solveButton.setText("Problem Solved");
						solveButton.setEnabled(false);
						ldeap.setModified(true);
						
					}
					else {
						ldeap.setSolved(false);
						MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
								"could not be solved optimally! Check the data.");
					}
				}
				catch (Exception ex) {
					ldeap.setSolved(false);
					MessageDialog.openWarning(nav.getShell(), "Solve error", "The problem" +
							"could not be solved properly!");
				}
				
				
				
				if(ldeap.isSolved()) {
					nav.displaySolution();
				}
			}

		});
		


		
		Progress progress = new Progress(comp);
		

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
		Images.paintCanvas(dataCanvas, "accept");
		dataLabel.setText("Data were imported successfully.");
		dataLabel.pack();
	}


	public void setVariablesOK() {
		Images.paintCanvas(variablesCanvas, "accept");
		variablesLabel.setText("Variables are configured correctly.");
		variablesLabel.pack();
	}
	
	public void setVariablesNOK() {
		Images.paintCanvas(variablesCanvas, "error");
		variablesLabel.setText("Configure the problem variables!");
		variablesLabel.pack();
		setAllNOK();
	}

	public void setModelDetailsOK() {
		Images.paintCanvas(modelDetailsCanvas, "accept");
		modelDetailsLabel.setText("A DEA Problem was selected.");
		modelDetailsLabel.pack();
	}
	
	public void setModelDetailsNOK() {
		Images.paintCanvas(modelDetailsCanvas, "error");
		modelDetailsLabel.setText("Configure the DEA model type!");
		modelDetailsLabel.pack();
		setAllNOK();
	}

	public void setAllOK() {
		remActionsGroup.setText("You're all set!");
		solveButton.setEnabled(true);
		stl.setNotificalLabelDelayStandard("You are ready to solve!");
		
	}
	
	public void setAllNOK() {
		remActionsGroup.setText("You still need to:");
		solveButton.setEnabled(false);
	} 

	
	

	private void setActionsGroup() {
		
		remActionsGroup = new Group(comp, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		remActionsGroup.setLayout(gridLayout);
		remActionsGroup.setText("You still need to:");
		fdata = new FormData();
		fdata.top = new FormAttachment(deaPNameText, 20);
		fdata.left = new FormAttachment(0, 20);
		fdata.height = 100;
		fdata.right = new FormAttachment(100, -20);
		remActionsGroup.setLayoutData(fdata);


		
		
		dataCanvas = new Canvas(remActionsGroup, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		gdata.grabExcessVerticalSpace = true;
		dataCanvas.setLayoutData(gdata);
		Images.paintCanvas(dataCanvas, "error");
		dataCanvas.setToolTipText(dataHelp);

		dataLabel = new Label(remActionsGroup, SWT.NONE);
		dataLabel.setText("Import some data!");
	
		
		
		variablesCanvas = new Canvas(remActionsGroup, SWT.NONE);
		gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		gdata.grabExcessVerticalSpace = true;
		variablesCanvas.setLayoutData(gdata);
		Images.paintCanvas(variablesCanvas, "error");
		variablesCanvas.setToolTipText(varHelp);
		
		variablesLabel = new Label(remActionsGroup, SWT.NONE);
		variablesLabel.setText("Configure the problem variables!");


		
		modelDetailsCanvas = new Canvas(remActionsGroup, SWT.NONE);
		gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		gdata.grabExcessVerticalSpace = true;
		modelDetailsCanvas.setLayoutData(gdata);
		Images.paintCanvas(modelDetailsCanvas, "error");
		modelDetailsCanvas.setToolTipText(modelHelp);
		
		modelDetailsLabel = new Label(remActionsGroup, SWT.NONE);
		modelDetailsLabel.setText("Configure the DEA model type!");

		
		
	}
	
	
	private void solve(DEAProblem deap) {
		for(int i = 0; i < deap.getNumberOfDMUs(); i++) {
			
		}
	}



	private class Progress {
		
		public Progress(Composite comp) {
			Group progressGroup = new Group(comp, SWT.NONE);
			progressGroup.setText("Solving Problem");
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 20);
			formData.right = new FormAttachment(100, -20);
			formData.top = new FormAttachment(solveButton, 20);
			progressGroup.setVisible(true);
			progressGroup.setLayoutData(formData);
			progressGroup.setLayout(new FormLayout());

			
			final ProgressBar progressBar = new ProgressBar(progressGroup, SWT.BORDER);
			formData = new FormData();
			formData.left = new FormAttachment(0, 5);
			formData.top = new FormAttachment(0, 10);
			formData.height = 20;
			formData.width = 400;
			progressBar.setVisible(true);
			progressBar.setLayoutData(formData);
			
			final Label progressStatusLabel = new Label(progressGroup, SWT.NONE);
			progressStatusLabel.setText("Solving DMU x of y");
			formData = new FormData();
			formData.left = new FormAttachment(0, 5);
			formData.top = new FormAttachment(progressBar, 10);
			formData.bottom = new FormAttachment(100, -5);
			formData.height = 20;
			progressStatusLabel.setVisible(true);
			progressStatusLabel.setLayoutData(formData);
		}
		
		public void updateProgressBar() {
			
		}
		
	}





}
