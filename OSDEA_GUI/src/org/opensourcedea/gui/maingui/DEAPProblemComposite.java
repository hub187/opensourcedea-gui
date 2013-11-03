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
import org.opensourcedea.gui.parameters.OSDEAConstants;
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
	private DEAPProblemComposite thisComp;
	private SolvingThread solvingThread;
	
	
	

	public DEAPProblemComposite(Composite parentComp, LDEAProblem parentLdeap, Navigation parentNav, final OSDEA_StatusLine stl) {
		super(parentComp, 0);

		ldeap = parentLdeap;
		nav = parentNav;
		this.stl = stl;
		thisComp = this;
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
		fdata.width = 350;
		deaPNameText.setLayoutData(fdata);


		String helpText = "If you need help, hover your mouse over the yellow warning icons.";
		Images.setHelpIcon(comp, helpText, 10, 20);

		//Create the Actions / Status Group
		probStatus = new ProblemStatus(comp, deaPNameText);
		probStatus.setActionsGroup();
		
		
		//Create the progress group
		progress = new SolvingProgress(comp, probStatus.getRemActionsGroup(), solveButton);
		
		
		solveButton = new Button(comp, SWT.PUSH);
		
		//Sets correct layout for solveButton and progressGroup when progress group needs to be hidden
		if(!ldeap.isSolved()) {
			showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
		}
		else {
			showProgressGroupSolved();
		}
		
		
		

		solveButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseUp(MouseEvent e) {
				
				//First check whether this is a problem reset (if it is then return)
				if(ldeap.isSolved()){
					if(MessageDialog.openConfirm(nav.getShell(), "Confirm Reset", "The DEA Problem was already solved. " +
							"Resetting the DEA problem will delete the exiting solution and problem parameters." +
							"\n\nDo you want to reset the problem and discard the solution and problem parameters?")) {
						nav.deleteSolution();
						showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
						return;
					}
					else {
						stl.setNotificalLabelDelayStandard("Reset cancelled");
						return;
					}
				}
				
				
				DEAPConverter converter = new DEAPConverter();
				final DEAProblem deap = converter.convertLDEAP(nav.getSelectedDEAProblem());
				
				
				comp.getDisplay().syncExec(new Runnable() {
					public void run() {
						showProgressGroupNotSolved("Cancel");
					}});

				if(solvingThread != null){
					if(solvingThread.isAlive()){
						solvingThread.sendStopRequest();
						return;
					}
				}

				solvingThread = new SolvingThread(ldeap, deap, comp.getDisplay(), nav, stl, solveButton, thisComp, progress);			
				solvingThread.start();

			}

		});


		


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




	private void setData(boolean dataOK) {

		if(dataOK){
			Images.paintCanvas(probStatus.getDataCanvas(), "accept");
			probStatus.getDataLabel().setText("Data were imported successfully.");
			probStatus.getDataLabel().pack();
		}
		else {
			Images.paintCanvas(probStatus.getDataCanvas(), "error");
			probStatus.getDataLabel().setText("Import some data!");
			probStatus.getDataLabel().pack();
		}
		
		probStatus.setDataToolTips(dataOK);		
	}


	private void setVariables(VariablesStatusEnum varStatus) {
		
		switch (varStatus) {
		case allVariablesCorrectlySet: {
			Images.paintCanvas(probStatus.getVariablesCanvas(), "accept");
			probStatus.getVariablesLabel().setText("Variables are configured correctly.");
			probStatus.getVariablesLabel().pack();
			break;
		}
		case NotAllVariablesSelected: {
			Images.paintCanvas(probStatus.getVariablesCanvas(), "accept");
			probStatus.getVariablesLabel().setText("Variables are configured correctly but some variables have not been selected.");
			probStatus.getVariablesLabel().pack();
			break;
		}
		default: {
			Images.paintCanvas(probStatus.getVariablesCanvas(), "error");
			probStatus.getVariablesLabel().setText("Configure the problem variables!");
			probStatus.getVariablesLabel().pack();
			setAllNOK();
			break;
		}
		}
		
		probStatus.setVarToolTips(varStatus);

	}

	private void setModelDetails(boolean modelDetailsOK) {
		if(modelDetailsOK) {
			Images.paintCanvas(probStatus.getmodelDetailsCanvas(), "accept");
			probStatus.getmodelDetailsLabel().setText("A DEA Problem was selected.");
			probStatus.getmodelDetailsLabel().pack();
		}
		else {
			Images.paintCanvas(probStatus.getmodelDetailsCanvas(), "error");
			probStatus.getmodelDetailsLabel().setText("Configure the DEA model type!");
			probStatus.getmodelDetailsLabel().pack();
			setAllNOK();
		}
		
		probStatus.setModelDetailsToolTips(modelDetailsOK);
	}

	
	private void setAllOK() {
		probStatus.getRemActionsGroup().setText("You're all set!");
		solveButton.setEnabled(true);
		if(!nav.getSelectedDEAProblem().isSolved()){
			stl.setNotificalLabelDelayStandard("You are ready to solve!");
		}
	}

	private void setAllNOK() {
		probStatus.getRemActionsGroup().setText("You still need to:");
		solveButton.setEnabled(false);
	} 



	public void showProgressGroupNotSolved(String solveButtonText) {
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.top = new FormAttachment(probStatus.getRemActionsGroup(), 20);
		progress.getProgressGroup().setVisible(true);
		progress.getProgressGroup().setLayoutData(formData);
		
		progress.setProgressBar(0);
		if(ldeap.getDataMatrix() != null){
			progress.updateProgressLabelText(OSDEAConstants.getSolvedDMUsProgress(0, ldeap.getDataMatrix().size(), 0));
		}
		else {
			progress.updateProgressLabelText(OSDEAConstants.getSolvedDMUsProgress(0, 0, 0));
		}
		
		solveButton.setText(solveButtonText);
		
		fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(progress.getProgressGroup(), 20);
		solveButton.setLayoutData(fdata);
		solveButton.pack();

	}
	
	public void showProgressGroupSolved() {
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.top = new FormAttachment(probStatus.getRemActionsGroup(), 20);
		progress.getProgressGroup().setVisible(true);
		progress.getProgressGroup().setLayoutData(formData);
		Integer nbDMUs = ldeap.getDMUNames().size();
		progress.updateProgressLabelText(OSDEAConstants.getSolvedDMUsProgress(nbDMUs, nbDMUs, 100));
		progress.setProgressBar(100);

		
		fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(progress.getProgressGroup(), 20);
		solveButton.setLayoutData(fdata);
		solveButton.setText("Reset DEA Problem");
		solveButton.pack();

	}
	
	/**
	 * Gets an array of boolean about the status of the DEAProblem and then updates the main composite accordingly (Icons, Text & Status Line). 
	 * @param statuses boolean[] {dataStatus, variableStatus, modelStatus, isSolved}
	 */
	public void setProblemStatus(boolean dataStatus, VariablesStatusEnum varStatus, boolean modelDetailsStatus, boolean isSolved) {
		
		if(dataStatus){
			setData(true);
		}
		else {
			setData(false);
		}
		
		setVariables(varStatus);
		

		
	
		if(modelDetailsStatus){
			setModelDetails(true);
		}
		else {
			setModelDetails(false);
		}
		
		
		
		
		
		
		if((varStatus == VariablesStatusEnum.allVariablesCorrectlySet | varStatus == VariablesStatusEnum.NotAllVariablesSelected) && dataStatus && modelDetailsStatus) {
			setAllOK();
			if(isSolved) {
				stl.setStatusLabel("Problem Solved");
				showProgressGroupSolved();
			}
			else {
				stl.setStatusLabel("You are ready to solve");
				showProgressGroupNotSolved(OSDEAConstants.solveButtonText);
			}
		}
		else {
			stl.setStatusLabel("You still have a few more things to do");
		}
		
		
		
	}
	


}
