package org.opensourcedea.gui.menu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class LDEAPSaver {
	
	private OSDEA_StatusLine stl;
	private Navigation nav;
	private static final String[] filterNames = {
		"DEA Problem file (*.deap)", "All Files (*.*)"
	};
	private static final String[] filterExts = {
		"*.deap", "*.*"
	};
	
	public LDEAPSaver(Navigation nav, OSDEA_StatusLine stl) {
		this.nav = nav;
		this.stl = stl;
	}
	
	
	
	
	/**
	 * Checks if some problems were modified.
	 * Return false if user pressed 'Cancel'.
	 * Return true if user pressed either no or saved the file.
	 * @return
	 */
	public boolean checkBeforeClosing() {
		boolean hasUnsaved = false;

		for(TreeItem ti : nav.getAllTreeItems()) {
			if(nav.getLDEAProblem(ti).isModified()) {
				hasUnsaved = true;
			}
		}

		if(hasUnsaved) {
			MessageDialog dg = new MessageDialog(nav.getShell(),
					"Save",
					null,
					"Do you want to save unsaved problems before closing OSDEA?",
					MessageDialog.QUESTION_WITH_CANCEL, 
					new String[]{
				IDialogConstants.YES_LABEL, 
				IDialogConstants.NO_LABEL, 
				IDialogConstants.CANCEL_LABEL},
				0
					);
			switch(dg.open()) {
			case 0: 
				LDEAPSaver saver = new LDEAPSaver(nav, stl);
				saver.saveAll();
				break;
			case 1:
				break;
			case 2:
				return false;
			}
		}
		
		return true;
	}
	
	
	
	
	public void saveFile(LDEAProblem ldeap) {
    	
		if(ldeap == null){
			stl.setNotificalLabelDelayStandard("You need to select a problem to save first!");
			return;
		}
		
		if(nav.getFilePath() != null) {
    		saveFile(nav.getFilePath(), ldeap);
    	}
    	else {
    		saveFileAs(ldeap);
    	}
	}
	
	
	public void saveAll(){
		TreeItem[] tia = nav.getAllTreeItems();

    	for(TreeItem ti : tia) {

    		LDEAProblem ldeap = nav.getLDEAProblem(ti);

    		//WIP
    		if(nav.getFilePath(ti) != null) {
    			saveFile(nav.getFilePath(ti), ldeap);
    		}
    		else {
    			saveFileAs(ldeap, ti);
    		}
    	}

	}
	
    public void saveFile(String fileName, LDEAProblem ldeap) {
    	
		if(ldeap == null){
			stl.setNotificalLabelDelayStandard("You need to select a problem to save first!");
			return;
		}
    	
    	try {
    		ldeap.setModified(false);
    		FileOutputStream fOut = new FileOutputStream(fileName);
    		ObjectOutputStream out = new ObjectOutputStream(fOut);
    		out.writeObject(ldeap);
    		out.close();
    		fOut.close();
    		stl.setNotificalLabelDelayStandard("Problem saved.");
		} catch (IOException e) {
			stl.setNotificalLabelDelayStandard("Error! The DEA Problem could not be saved!");
		}
    }
    
    public void saveFileAs(LDEAProblem ldeap) {
		if(ldeap == null){
			stl.setNotificalLabelDelayStandard("You need to select a problem to save first!");
			return;
		}
		
    	saveFileAs(ldeap, nav.getSelectedTreeItem());
    }

    
    public void saveFileAs(LDEAProblem ldeap, TreeItem ti) {
    	
		if(ldeap == null || ti == null){
			stl.setNotificalLabelDelayStandard("You need to select a problem to save first!");
			return;
		}
    	
    	FileDialog dialog = new FileDialog(nav.getShell(), SWT.SAVE);
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExts);
		if(ldeap.getModelName() != ""){
			dialog.setFileName(ldeap.getModelName() + ".deap");
		}


		String fileName = null;
		
		boolean dontCare = false;
		while(!dontCare) {
			fileName = dialog.open();
			if (fileName == null) {
				break;
			}
			File f = new File(fileName);
			if(f.exists()){
				if(MessageDialog.openQuestion(nav.getShell(), "Overwrite", "The file already exists. Do you want to overwrite it?") == 
						true) {
					dontCare = true;
				}
			}else{
				dontCare = true;
			}
		}
		
		
		if(fileName != null) {
			
			String newFileName = IOManagement.getFileName(fileName, false);
			ldeap.setModelName(newFileName);
			
			saveFile(fileName, ldeap);
			
			if(nav.getFilePath() != null) {
				nav.updateFilePath(fileName, ti);
			}
			else {
				nav.addFilePath(fileName, ti);
			}
			
			nav.setDEAProblemText(newFileName, ti);
			
		}
		else {
			stl.setNotificalLabelDelayStandard("Save Cancelled");
		}
    }

}
