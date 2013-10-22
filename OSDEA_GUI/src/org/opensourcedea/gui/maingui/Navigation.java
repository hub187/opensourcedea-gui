package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.opensourcedea.dea.DEAPSolution;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class Navigation extends Composite {
	
	
	private Tree tree;
	

	

	
	
	
	private OSDEA_StatusLine stl;
	
	private final String rawDataItemText = "Raw Data", variablesItemText = "Variables", modelDetailsItemText = "Model details",
	solutionItemText = "Solution", objectivesItemText = "Objectives", projectionsItemText = "Projections", lambdasItemText = "Lambdas",
	peerGroupItemText = "Peer Group", slacksItemText = "Slacks", weightsItemText = "Weights";
	
	private HashMap<TreeItem, String> filePathHashMap = new HashMap<TreeItem, String>();
	
	private final OSDEAMainComposite osdeaMainComp;
	
	final Composite parent;
	
	
	public Navigation (final Composite parent, OSDEA_StatusLine parentStl) {
		
		super(parent, SWT.BORDER);
		this.parent = parent;

		
		stl = parentStl;
		
		osdeaMainComp = (OSDEAMainComposite)parent;
		
		tree = new Tree(this, SWT.NONE);
		
		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TreeItem it = (TreeItem)e.item;
				TreeItem parentIt = getSelectedDEAProblemTreeItem();
				
				
				if(parentIt != null){
					
					//Set position stl label
					if(parentIt == it) {
						stl.setPositionLabel(parentIt.getText());
						checkProblemStatus();
					}
					else {
						stl.setPositionLabel(parentIt.getText() + ":" + it.getText());
					}
					
					
					//Set status stl label
					if(it.getText().equals(rawDataItemText)) {
						if(hasData()) {
							stl.setStatusLabel("Data imported.");
						}
						else {
							stl.setStatusLabel("You need to import data.");
						}
					}
					else if(it.getText().equals(variablesItemText)) {
						if(hasData()) {
							((VariablesComposite)getVariableTreeItem().getData()).updateStatusStl();
						}
						else {
							stl.setStatusLabel("You need to import data before you can configure variables.");
						}
					}
					else if(it.getText().equals(modelDetailsItemText)) {
						((ModelDetailsComposite)getModelDetailsTreeItem().getData()).updateStatusStl();
					}
					
					
					
					//Set top composite in stack layout
					Object[] objArr = (Object[])parentIt.getData();

					if(it == parentIt) {
						osdeaMainComp.setDataPanelTopControl((DEAPProblemComposite)objArr[1]);
						
					}
					else{
						osdeaMainComp.setDataPanelTopControl((Control)it.getData());
					}					

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
		});
		
	
		this.setLayout(new FillLayout());
					
	}
	
	private VariablesStatusEnum checkVarStatus() {

		VariablesComposite varComp = (VariablesComposite)getVariableTreeItem().getData();
		
		return varComp.checkIfVarAreOK();

	}
	
	private DEAPProblemComposite getSelectedDEAProblemComposite() {
		return (DEAPProblemComposite)((Object[])getSelectedDEAProblemTreeItem().getData())[1];
	}
	
	private boolean areModelDetailsOK() {
		if( ((LDEAProblem)((Object[])getSelectedDEAProblemTreeItem().getData())[0]).getModelDetails().getModelType() !=null )  {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void checkProblemStatus() {
				
		DEAPProblemComposite comp = getSelectedDEAProblemComposite();
		
		boolean areDataOK = false;
		boolean isModelOK = false;
		boolean isSolved = false;
		
		VariablesStatusEnum varStatus = checkVarStatus();
		isModelOK = areModelDetailsOK();
		areDataOK = hasData();
		isSolved = isSolved();
		
		comp.setProblemStatus(areDataOK, varStatus, isModelOK, isSolved);
		
	}
	
	
	private boolean hasData() {
		if(((LDEAProblem)((Object[])getSelectedDEAProblemTreeItem().getData())[0]).getDataMatrix() != null){
			if( !((LDEAProblem)((Object[])getSelectedDEAProblemTreeItem().getData())[0]).getDataMatrix().isEmpty())  {
				return true;
			}
		}
		return false;
	}
	
	private void refreshVarList(){
		((VariablesComposite)getVariableTreeItem().getData()).refreshVarList();
	}
	
	private void setRawDataTable(LDEAProblem ldeap) {
		
		((RawDataComposite)getRawDataTreeItem().getData()).displayData(ldeap);

	}
	
	public TreeItem addDEAProblem(String deaProblemName, LDEAProblem ldeap, String stlStr) {
		
		TreeItem deaProblemTreeItem = new TreeItem (tree, 0);
		
		Object[] treeArr = new Object[11];
		treeArr[0] = tree;
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
		
		AddDEAProblemThread addProbThread = new AddDEAProblemThread(ldeap, deaProblemName, this, osdeaMainComp.getDataPanel(),
				stl, deaProblemTreeItem, treeArr, stlStr);
		
		addProbThread.start();

		/* This *should* work as this is the ref of deaProblemTreeItem that is passed in the method 
		 * and the thread doesn't instantiate a new ref (i.e. no TreeItem deaProblemTreeItem = new TreeItem etc..).
		 * However, I will need to check the behaviour when opening massive problems (problems that will take several seconds to open).
		 * */
		
		return deaProblemTreeItem;
		
	}
	
	/*
	 * Used by listeners when creating new problems
	 */
	public void addDEAProblem(String deaProblemName) {

		LDEAProblem ldeap = new LDEAProblem();
		ldeap.setModelName(deaProblemName);
		
		addDEAProblem(deaProblemName, ldeap, "Created new DEA Problem.");		
		
	}
	
	public void closeDEAProblem() {

		TreeItem ti = getSelectedDEAProblemTreeItem();
		
		ti.dispose();
		
		if(getAllTreeItems().length == 0){
			osdeaMainComp.setDataPanelTopControlAsInstr();
		}
		
		stl.resetAllLabels();
		
	}

	
	
	/*
	 * Used by DEA Problem Composite when setting problem name
	 */
	public void setDEAProblemText(String str) {

		setDEAProblemText(str, getSelectedDEAProblemTreeItem());
		
	}
	
	public void setDEAProblemText(String str, TreeItem ti) {
		if(ti != null) {
			TreeItem deapIt = ti;
			deapIt.setText(str);
			stl.setPositionLabel(str);
			if(getFilePath() != null) {
				String newFilePath = IOManagement.getNewFileName(getFilePath(ti), str);
				updateFilePath(newFilePath, ti);
			}
		}
	}
	
	public void setModelLDEAProblemName(String newModelName) {
		LDEAProblem ldeap = getSelectedDEAProblem();
		if(ldeap != null) {
			ldeap.setModelName(newModelName);
		}
	}
	
	
	private TreeItem getSelectedDEAProblemTreeItem() {
		if(tree.getSelectionCount() > 0) {
			TreeItem[] it = tree.getSelection();
			TreeItem deapIt = it[0];
			while(deapIt.getParentItem() != null) {
				deapIt = deapIt.getParentItem();
			}
			return deapIt;
		}
		return null;
	}
	
	public TreeItem[] getAllTreeItems() {
		
		return tree.getItems();	
	}
	
	
	
	private boolean isSolved() {
		if(getSelectedDEAProblem().isSolved()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the selected DEAProblemTreeItem reference
	 * @return TreeItem DEAProblemTreeItem OR 'null' if no DEAProblem is selected.
	 */
	public LDEAProblem getSelectedDEAProblem() {
		
		try {
			return getLDEAProblem(getSelectedDEAProblemTreeItem());
		}
		catch (Exception e) {
			return null;
		}
	
	}
	
	
	public LDEAProblem getLDEAProblem(TreeItem ti) {
		return (LDEAProblem)((Object[])ti.getData())[0];
	}
	
	
	public TreeItem getSelectedTreeItem() {
		return getSelectedDEAProblemTreeItem();
	}

	

	
	private TreeItem getRawDataTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(rawDataItemText)) {
				return it;
			}
		}
		return null;
	}
	
	private TreeItem getVariableTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(variablesItemText)) {
				return it;
			}
		}
		return null;
	}
	
	private TreeItem getModelDetailsTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(modelDetailsItemText)) {
				return it;
			}
		}
		return null;
	}
	
	private TreeItem getObjectivesTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(objectivesItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}
	
	private TreeItem getProjectionsTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(projectionsItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}
	
	private TreeItem getLambdasTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(lambdasItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}
	
	private TreeItem getPeerGroupTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(peerGroupItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}
	
	private TreeItem getSlacksTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(slacksItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}
	
	private TreeItem getWeightsTreeItem() {
		TreeItem parent = getSelectedDEAProblemTreeItem();
		
		for(TreeItem it : parent.getItems()) {
			if(it.getText().equals(solutionItemText)) {
				for(TreeItem innerIt : it.getItems()) {
					if(innerIt.getText().equals(weightsItemText)) {
						return innerIt;
					}
				}
			}
		}
		
		return null;
	}

	
		
	
	/*
	 * used by the import file wizard. Allows the wizard to only read data and present it to the nav via this methods (thus the
	 * wizard doesn't know about the inner structure of the program).
	 */
	public void importData(ArrayList<double[]> dataMatrix, ArrayList<String> dmuNames, ArrayList<String> variableNames) { //, ArrayList<ArrayList<String>> fullDataMatrixString) {
		
		boolean backupProblem = false;
		LDEAProblem ldeap = null;
		ArrayList<double[]> backupDataMatrix = null;
		ArrayList<String> backupDMUNames = null;
		ArrayList<String> backupVariableNames = null;
		
		
		try {
			ldeap = (LDEAProblem)((Object[])getSelectedDEAProblemTreeItem().getData())[0];
			if(ldeap.getDMUNames() !=null && ldeap.getDataMatrix() != null && ldeap.getVariableNames() != null) {
				backupDataMatrix = ldeap.getDataMatrix();
				backupDMUNames = ldeap.getDMUNames();
				backupVariableNames = ldeap.getVariableNames();
				
			}
		} catch (Exception e) {
			backupProblem = true;
		}
		
		
		if(!backupProblem) {
			try {
				
				ArrayList<String> headers = new ArrayList<String>();
				headers.add("DMU Names");
				headers.addAll(variableNames);
				
				ldeap.setDataMatrix(dataMatrix);
				ldeap.setDMUNames(dmuNames);
				ldeap.setVariableNames(variableNames);
				refreshVarList();
				setRawDataTable(ldeap);
				checkProblemStatus();
				ldeap.setModified(true);
				stl.setNotificalLabelDelayStandard("Successful Imported " + ldeap.getDMUNames().size() + " DMUs, " + ldeap.getVariableNames().size() + " Variables.");
			}
			catch (Exception e) {
				ldeap.setDataMatrix(backupDataMatrix);
				ldeap.setDMUNames(backupDMUNames);
				ldeap.setVariableNames(backupVariableNames);
				MessageDialog.openWarning(this.getShell(),
						"Warning", "The data file to import could be read correctly but could not be displayed.\n" +
						"This should not happen so is likely a bug. Please report it so we can fix it!");
			}
		}

	}
	
	
	
	
	public void displaySolution() {
		LDEAProblem ldeap = getSelectedDEAProblem();
		
		//Objectives
		ObjectivesComposite objComp = (ObjectivesComposite)getObjectivesTreeItem().getData();
		objComp.displaySolution(ldeap);
		
		//Projections
		ProjectionsComposite projComp = (ProjectionsComposite)getProjectionsTreeItem().getData();
		projComp.displaySolution(ldeap);
		
		//Lambdas
		LambdasComposite lambdaComp = (LambdasComposite)getLambdasTreeItem().getData();
		lambdaComp.displaySolution(ldeap);
		
		//PeerGroup
		PeerGroupComposite peerComp = (PeerGroupComposite)getPeerGroupTreeItem().getData();
		peerComp.displaySolution(ldeap);
		
		//Slacks
		SlacksComposite slackComp = (SlacksComposite)getSlacksTreeItem().getData();
		slackComp.displaySolution(ldeap);
		
		//Weight
		WeightsComposite weightsComp = (WeightsComposite)getWeightsTreeItem().getData();
		weightsComp.displaySolution(ldeap);
		
		//deactivating widgets
		ModelDetailsComposite modDetailsComp = (ModelDetailsComposite)getModelDetailsTreeItem().getData();
		modDetailsComp.setWidgetsEnabled(false);
		
		VariablesComposite varComp = (VariablesComposite)getVariableTreeItem().getData();
		varComp.setWidgetsEnabled(false);
		
	}
	
	public void deleteSolution() {
		
		LDEAProblem ldeap = getSelectedDEAProblem();
		DEAPSolution backup = ldeap.getLdeapSolution();
		boolean modified = ldeap.isModified();
		
		
		try {
			ldeap.deleteSolution();

			//Objectives
			ObjectivesComposite objComp = (ObjectivesComposite)getObjectivesTreeItem().getData();
			objComp.resetComposite();

			//Projections
			ProjectionsComposite projComp = (ProjectionsComposite)getProjectionsTreeItem().getData();
			projComp.resetComposite();

			//Lambdas
			LambdasComposite lambdaComp = (LambdasComposite)getLambdasTreeItem().getData();
			lambdaComp.resetComposite();		
			
			//Peer Group
			PeerGroupComposite peerComp = (PeerGroupComposite)getPeerGroupTreeItem().getData();
			peerComp.resetComposite();
			
			//Slacks
			SlacksComposite slackComp = (SlacksComposite)getSlacksTreeItem().getData();
			slackComp.resetComposite();

			//Weight
			WeightsComposite weightsComp = (WeightsComposite)getWeightsTreeItem().getData();
			weightsComp.resetComposite();
			
			
			//activating widgets
			ModelDetailsComposite modDetailsComp = (ModelDetailsComposite)getModelDetailsTreeItem().getData();
			modDetailsComp.setWidgetsEnabled(true);
			
			VariablesComposite varComp = (VariablesComposite)getVariableTreeItem().getData();
			varComp.setWidgetsEnabled(true);
			
			
			
			ldeap.setSolved(false);
			ldeap.setModified(true);
			
			
			stl.setNotificalLabelDelayStandard("Problem reset successfully.");
		}
		catch (Exception e) {
			
			ldeap.setLdeapSolution(backup);
			displaySolution();
			ldeap.setSolved(true);
			ldeap.setModified(modified);
			
			getDisplay().syncExec(new Runnable() {
				public void run() {
					MessageDialog.openWarning(getShell(), "Reset error", "The problem" +
							"could not be reset! Previous solution was restored.");
				}
			});
			stl.setNotificalLabelDelayStandard("There was a problem resetting the solution.");
			stl.setStatusLabel("Problem Solved");
		}
		
	}
	
	public void completeProblemReset() {
		

		((LDEAProblem)((Object[])getSelectedDEAProblemTreeItem().getData())[0]).resetLDEAProblem();
		
		try {
			RawDataComposite rawComp = (RawDataComposite)getRawDataTreeItem().getData();
			rawComp.resetComposite();
			
			VariablesComposite varComp = (VariablesComposite)getVariableTreeItem().getData();
			varComp.cleanAllLists();
			
			ModelDetailsComposite modComp = (ModelDetailsComposite)getModelDetailsTreeItem().getData();
			modComp.resetModDetailsComposite();
			
			deleteSolution();
			
			checkProblemStatus();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void addFilePath(String fullFilePath) {
		addFilePath(fullFilePath, getSelectedDEAProblemTreeItem());
	}
	
	public void addFilePath(String fullFilePath, TreeItem ti) {
		filePathHashMap.put(ti, fullFilePath);
	}
	
	public String getFilePath() {
		
		return getFilePath(getSelectedDEAProblemTreeItem());
		
	}
	
	public String getFilePath(TreeItem ti) {
		if(filePathHashMap.containsKey(ti)) {
			return filePathHashMap.get(ti);
		}
		return null;
	}
	
	public void updateFilePath(String newFilePath) {
		updateFilePath(newFilePath, getSelectedDEAProblemTreeItem());
	}
	
	public void updateFilePath(String newFilePath, TreeItem ti) {
		filePathHashMap.put(ti, newFilePath);
	}
	
	
	public void fireSelectionChange(TreeItem item) {
		Event event = new Event();
		event.item = item;
		tree.select(item);
		tree.notifyListeners(SWT.Selection, event);
	}
	
}
