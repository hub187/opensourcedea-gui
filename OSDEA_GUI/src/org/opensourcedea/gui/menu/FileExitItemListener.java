package org.opensourcedea.gui.menu;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class FileExitItemListener implements SelectionListener {
	
	private Shell shell;
	private Display display;
	private Navigation nav;
	private OSDEA_StatusLine stl;
	
	public FileExitItemListener(Navigation nav, OSDEA_StatusLine stl) {
		shell = nav.getShell();
		display = nav.getDisplay();
		this.nav = nav;
		this.stl = stl;
	}
	
    public void widgetSelected(SelectionEvent event) {
    	
    	boolean hasUnsaved = false;
    	
    	for(TreeItem ti : nav.getAllTreeItems()) {
    		if(((LDEAProblem)nav.getDEAProblem(ti)).isModified()) {
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
				return;
			}
    	}

    	
    	shell.close();
	    display.dispose();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    	
    }
}
