package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.utils.Images;

public class ProblemStatus {
	
	private Group remActionsGroup;
	private Canvas dataCanvas;
	private Label dataLabel;
	private Label variablesLabel;
	private Canvas variablesCanvas;
	private Label modelDetailsLabel;
	private Canvas modelDetailsCanvas;
	
	private final String dataHelp = "In order to import some data, you need to click Tools/import or click on the import icon in the ToolBar.\n" +
			"This will open a wizard which will allow you to import data from a csv file.";
	private final String varHelp = "In order to configure the variables, you need to have imported some data.\n" +
			"Once data has been imported, select the Variables tree item on the right hand side and assign each variable to a box.\n" +
			"All variables need to be assigned and there must be at least 1 input. and 1 output.";
	private final String modelHelp = "In order to select a model type, select the Model Details tree item on the right.\n" +
			"You can then select a model type from the main Combo Box .\n" +
			"If you want, you can use the filters to help you in selecting a model type.";
	
	private Composite comp;
	private StyledText deaPNameText;
	
	
	public ProblemStatus(Composite comp, StyledText deaPNameText) {
		this.comp = comp;
		this.deaPNameText= deaPNameText;
	}
	
	
	public Canvas getDataCanvas(){
		return dataCanvas;
	}
	
	public Label getDataLabel() {
		return dataLabel;
	}
	
	public Canvas getVariablesCanvas(){
		return variablesCanvas;
	}
	
	public Label getVariablesLabel() {
		return variablesLabel;
	}
	
	public Canvas getmodelDetailsCanvas(){
		return modelDetailsCanvas;
	}
	
	public Label getmodelDetailsLabel() {
		return modelDetailsLabel;
	}
	
	public Group getRemActionsGroup() {
		return remActionsGroup;
	}
	
	public void setActionsGroup() {

		remActionsGroup = new Group(comp, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		remActionsGroup.setLayout(gridLayout);
		remActionsGroup.setText("You still need to:");
		FormData fdata = new FormData();
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
	
	
	
}
