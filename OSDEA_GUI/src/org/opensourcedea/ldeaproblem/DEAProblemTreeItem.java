package org.opensourcedea.ldeaproblem;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.AddDEAProblemThread;
import org.opensourcedea.gui.maingui.INavigationItem;
import org.opensourcedea.gui.maingui.LambdasComposite;
import org.opensourcedea.gui.maingui.ModelDetailsComposite;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.maingui.ObjectivesComposite;
import org.opensourcedea.gui.maingui.PeerGroupComposite;
import org.opensourcedea.gui.maingui.ProjectionsComposite;
import org.opensourcedea.gui.maingui.SlacksComposite;
import org.opensourcedea.gui.maingui.VariablesComposite;
import org.opensourcedea.gui.maingui.WeightsComposite;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class DEAProblemTreeItem implements INavigationItem {
	
	
	private TreeItem deapTreeItem, rawDataTreeItem, variablesTreeItem, modelDetailsTreeItem,
	solutionTreeItem, objectivesTreeItem, projectionsTreeItem, lambdasTreeItem,
	referenceSetTreeItem, slacksTreeItem, weightsTreeItem;

	private final String rawDataItemText = "Raw Data", variablesItemText = "Variables", modelDetailsItemText = "Model details",
			solutionItemText = "Solution", objectivesItemText = "Objectives", projectionsItemText = "Projections", lambdasItemText = "Lambdas",
			peerGroupItemText = "Peer Group", slacksItemText = "Slacks", weightsItemText = "Weights";

	private OSDEA_StatusLine stl;
	private Navigation nav;
	private Composite dataPanel;
	private LDEAProblem ldeap;
	private Display display;
	
	public DEAProblemTreeItem(LDEAProblem ldeap, OSDEA_StatusLine stl, Navigation nav, Composite dataPanel, TreeItem deapTreeItem) {
		this.stl = stl;
		this.nav = nav;
		this.dataPanel = dataPanel;
		this.deapTreeItem = deapTreeItem;
		this.display = stl.getCompStatusLine().getDisplay();
		this.ldeap = ldeap;
	}

	@Override
	public void addItemToNav(String itemName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addItemToNav(String itemName, Object domainObject, String stlString) {

		Object[] treeTextArr = new Object[10];
		treeTextArr[0] = rawDataItemText;
		treeTextArr[1] = variablesItemText;
		treeTextArr[2] = modelDetailsItemText;
		treeTextArr[3] = solutionItemText;
		treeTextArr[4] = objectivesItemText;
		treeTextArr[5] = projectionsItemText;
		treeTextArr[6] = lambdasItemText;
		treeTextArr[7] = peerGroupItemText;
		treeTextArr[8] = slacksItemText;
		treeTextArr[9] = weightsItemText;
		
		TreeItem[] treeItArr = new TreeItem[11];
		treeItArr[0] = deapTreeItem;
		treeItArr[1] = rawDataTreeItem;
		treeItArr[2] = variablesTreeItem;
		treeItArr[3] = modelDetailsTreeItem;
		treeItArr[4] = solutionTreeItem;
		treeItArr[5] = objectivesTreeItem;
		treeItArr[6] = projectionsTreeItem;
		treeItArr[7] = lambdasTreeItem;
		treeItArr[8] = referenceSetTreeItem;
		treeItArr[9] = slacksTreeItem;
		treeItArr[10] = weightsTreeItem;
		
		AddDEAProblemThread addProbThread = new AddDEAProblemThread((LDEAProblem)domainObject, itemName, display,
				stl, nav, treeTextArr, treeItArr, stlString, dataPanel);
		addProbThread.start();

	}

	@Override
	public void displaySolution() {
		//Objectives
		ObjectivesComposite objComp = (ObjectivesComposite)objectivesTreeItem.getData();
		objComp.displaySolution(ldeap);

		//Projections
		ProjectionsComposite projComp = (ProjectionsComposite)projectionsTreeItem.getData();
		projComp.displaySolution(ldeap);

		//Lambdas
		LambdasComposite lambdaComp = (LambdasComposite)lambdasTreeItem.getData();
		lambdaComp.displaySolution(ldeap);

		//PeerGroup
		PeerGroupComposite peerComp = (PeerGroupComposite)referenceSetTreeItem.getData();
		peerComp.displaySolution(ldeap);

		//Slacks
		SlacksComposite slackComp = (SlacksComposite)slacksTreeItem.getData();
		slackComp.displaySolution(ldeap);

		//Weight
		WeightsComposite weightsComp = (WeightsComposite)weightsTreeItem.getData();
		weightsComp.displaySolution(ldeap);

		//deactivating widgets
		ModelDetailsComposite modDetailsComp = (ModelDetailsComposite)modelDetailsTreeItem.getData();
		modDetailsComp.setWidgetsEnabled(false);

		VariablesComposite varComp = (VariablesComposite)variablesTreeItem.getData();
		varComp.setWidgetsEnabled(false);

	}




}
