package org.opensourcedea.gui.menu;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class FileCloseItemListener implements SelectionListener {
	
	private OSDEA_StatusLine stl;
	private Navigation nav;
	
	public FileCloseItemListener(Navigation nav, OSDEA_StatusLine stl) {
		this.nav = nav;
		this.stl = stl;
	}
	
	public void widgetSelected(SelectionEvent event) {


		if(nav.getSelectedDEAProblem() == null){
			stl.setNotificalLabelDelayStandard("Close failed!");
			return;
		}

		LDEAProblem ldeap = nav.getSelectedDEAProblem();

		if(ldeap.isModified()) {

			MessageDialog dg = new MessageDialog(nav.getShell(),
					"Save",
					null,
					"The DEA Problem '" + ldeap.getModelName() + "' has not been saved. Do you want to save it before closing it?",
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
				saver.saveFile(ldeap);
				break;
			case 1:
				break;
			case 2:
				return;
			}

			nav.closeDEAProblem();
		}
		else {
			nav.closeDEAProblem();
		}

	}

    public void widgetDefaultSelected(SelectionEvent event) {

    }
}
