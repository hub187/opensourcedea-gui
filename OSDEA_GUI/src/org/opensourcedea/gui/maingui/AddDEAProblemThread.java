package org.opensourcedea.gui.maingui;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class AddDEAProblemThread extends Thread {


	private LDEAProblem ldeap;
	private String deaProblemName;
	private Composite dataPanel;
	private OSDEA_StatusLine stl;
	private Navigation nav;
	private String stlStr;	
	
	private TreeItem deapTreeItem, rawDataTreeItem, variablesTreeItem, modelDetailsTreeItem,
	solutionTreeItem, objectivesTreeItem, projectionsTreeItem, lambdasTreeItem,
	referenceSetTreeItem, slacksTreeItem, weightsTreeItem;

	private Image deaProblemImage, rawDataImage, iosImage, deaModelImage, solutionImage,
	objectivesImage, projectionsImage, lambdasImage, referenceSetImage, slacksImage,
	weightsImage;

	private final String rawDataItemText, variablesItemText, modelDetailsItemText,
	solutionItemText, objectivesItemText, projectionsItemText, lambdasItemText,
	peerGroupItemText, slacksItemText, weightsItemText;

	private final ImageRegistry imgReg;

	

	public AddDEAProblemThread(LDEAProblem ldeap, String deaProblemName, Display display,
			OSDEA_StatusLine stl, Navigation nav, Object[] params, String stlStr, Composite dataPanel,
			TreeItem deapTreeItem) {
		this.ldeap = ldeap;
		this.deaProblemName = deaProblemName;
		this.stl = stl;
		this.stlStr = stlStr;
		this.dataPanel = dataPanel;
		this.deapTreeItem = deapTreeItem;
		
		//TO CHANGE
		this.nav = nav;

		imgReg = Images.getFullImageRegistry(display);
		deaProblemImage = imgReg.get("deaProblem");
		rawDataImage = imgReg.get("rawData");
		iosImage = imgReg.get("variables");
		deaModelImage = imgReg.get("modelDetails");
		solutionImage = imgReg.get("solution");
		objectivesImage = imgReg.get("objective");
		projectionsImage = imgReg.get("projections");

		lambdasImage = imgReg.get("lambdas");
		referenceSetImage = imgReg.get("referenceSet");
		slacksImage = imgReg.get("slacks");
		weightsImage = imgReg.get("weights");

		rawDataItemText = (String)params[1];
		variablesItemText = (String)params[2];
		modelDetailsItemText = (String)params[3];
		solutionItemText = (String)params[4];
		objectivesItemText = (String)params[5];
		projectionsItemText = (String)params[6];
		lambdasItemText = (String)params[7];
		peerGroupItemText = (String)params[8];
		slacksItemText = (String)params[9];
		weightsItemText = (String)params[10];


	}

	public void run() {



		nav.getDisplay().syncExec(new Runnable() {
			public void run() {
				addProblem();

			}
		});


	}


	private void addProblem() {
		//Takes time to create (apparently more on first time) => to investigate
		DEAPProblemComposite problemComp = new DEAPProblemComposite(dataPanel, ldeap, nav, stl);
		RawDataComposite dataComp = null;
		dataComp = new RawDataComposite(dataPanel, ldeap);

		VariablesComposite variablesComp = new VariablesComposite(dataPanel, nav, ldeap, stl);
		//Takes time to create (apparently more on first time) => to investigate
		ModelDetailsComposite modelComp = new ModelDetailsComposite(dataPanel, ldeap, stl);
		SolutionComposite solutionComp = new SolutionComposite(dataPanel);
		ObjectivesComposite objectivesComp = new ObjectivesComposite(dataPanel);
		ProjectionsComposite projectionsComp = new ProjectionsComposite(dataPanel);
		LambdasComposite lambdasComp = new LambdasComposite(dataPanel);
		PeerGroupComposite peerComp = new PeerGroupComposite(dataPanel);
		SlacksComposite slacksComp = new SlacksComposite(dataPanel);
		WeightsComposite weightsComp = new WeightsComposite(dataPanel);


		Object[] deaPTreeItemData = new Object[2];
		deaPTreeItemData[0] = ldeap;
		deaPTreeItemData[1] = problemComp;


		deapTreeItem.setText (deaProblemName);
		deapTreeItem.setImage(deaProblemImage);
		deapTreeItem.setData(deaPTreeItemData);

		rawDataTreeItem = new TreeItem (deapTreeItem, 0);
		rawDataTreeItem.setText (rawDataItemText);
		rawDataTreeItem.setImage(rawDataImage);
		rawDataTreeItem.setData(dataComp);

		variablesTreeItem = new TreeItem (deapTreeItem, 0);
		variablesTreeItem.setText (variablesItemText);
		variablesTreeItem.setImage(iosImage);
		variablesTreeItem.setData(variablesComp);

		modelDetailsTreeItem = new TreeItem (deapTreeItem, 0);
		modelDetailsTreeItem.setText (modelDetailsItemText);
		modelDetailsTreeItem.setImage(deaModelImage);
		modelDetailsTreeItem.setData(modelComp);


		solutionTreeItem = new TreeItem (deapTreeItem, 0);
		solutionTreeItem.setText (solutionItemText);
		solutionTreeItem.setImage(solutionImage);
		solutionTreeItem.setData(solutionComp);


		objectivesTreeItem = new TreeItem (solutionTreeItem, 0);
		objectivesTreeItem.setText (objectivesItemText);
		objectivesTreeItem.setImage(objectivesImage);
		objectivesTreeItem.setData(objectivesComp);


		projectionsTreeItem = new TreeItem (solutionTreeItem, 0);
		projectionsTreeItem.setText (projectionsItemText);
		projectionsTreeItem.setImage(projectionsImage);
		projectionsTreeItem.setData(projectionsComp);


		lambdasTreeItem = new TreeItem (solutionTreeItem, 0);
		lambdasTreeItem.setText (lambdasItemText);
		lambdasTreeItem.setImage(lambdasImage);
		lambdasTreeItem.setData(lambdasComp);


		referenceSetTreeItem = new TreeItem (solutionTreeItem, 0);
		referenceSetTreeItem.setText (peerGroupItemText);
		referenceSetTreeItem.setImage(referenceSetImage);
		referenceSetTreeItem.setData(peerComp);

		slacksTreeItem = new TreeItem (solutionTreeItem, 0);
		slacksTreeItem.setText (slacksItemText);
		slacksTreeItem.setImage(slacksImage);
		slacksTreeItem.setData(slacksComp);


		weightsTreeItem = new TreeItem (solutionTreeItem, 0);
		weightsTreeItem.setText (weightsItemText);
		weightsTreeItem.setImage(weightsImage);
		weightsTreeItem.setData(weightsComp);


		nav.fireSelectionChange(deapTreeItem);

		if(ldeap.isSolved()){
			nav.displaySolution();
		}


		stl.setNotificalLabelDelayStandard(stlStr);


		this.interrupt();
	}

}
