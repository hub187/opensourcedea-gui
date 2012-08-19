package org.opensourcedea.ldeaproblem;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.AddDEAProblemThread;
import org.opensourcedea.gui.maingui.INavigationItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class DEAProblemTreeItem implements INavigationItem {
	
	
	private final String rawDataItemText = "Raw Data", variablesItemText = "Variables", modelDetailsItemText = "Model details",
	solutionItemText = "Solution", objectivesItemText = "Objectives", projectionsItemText = "Projections", lambdasItemText = "Lambdas",
	peerGroupItemText = "Peer Group", slacksItemText = "Slacks", weightsItemText = "Weights";
	
	private OSDEA_StatusLine stl;
	private Navigation nav;
	private Composite dataPanel;
	private TreeItem deapTreeItem;
	
	public DEAProblemTreeItem(OSDEA_StatusLine stl, Navigation nav, Composite dataPanel, TreeItem deapTreeItem) {
		this.stl = stl;
		this.nav = nav;
		this.dataPanel = dataPanel;
		this.deapTreeItem = deapTreeItem;
	}
	
	@Override
	public void addItemToNav(String itemName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addItemToNav(String itemName, Object domainObject, String stlString) {
		
		Object[] treeArr = new Object[11];
//		treeArr[0] = tree;
		treeArr[1] = rawDataItemText;
		treeArr[2] = variablesItemText;
		treeArr[3] = modelDetailsItemText;
		treeArr[4] = solutionItemText;
		treeArr[5] = objectivesItemText;
		treeArr[6] = projectionsItemText;
		treeArr[7] = lambdasItemText;
		treeArr[8] = peerGroupItemText;
		treeArr[9] = slacksItemText;
		treeArr[10] = weightsItemText;
		
		AddDEAProblemThread addProbThread = new AddDEAProblemThread((LDEAProblem)domainObject, itemName, stl.getCompStatusLine().getDisplay(),
				stl, nav, treeArr, stlString, dataPanel, deapTreeItem);
		addProbThread.start();
		
	}
	
	
	
	
}
