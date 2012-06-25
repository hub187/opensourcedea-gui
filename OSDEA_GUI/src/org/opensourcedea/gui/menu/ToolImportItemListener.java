package org.opensourcedea.gui.menu;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.importdata.ImportFileWizard;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class ToolImportItemListener implements SelectionListener {
	
	private Shell shell;
	private Navigation navigation;
	private OSDEA_StatusLine stl;
	private ImportFileWizard wizard;
	private WizardDialog wizardDialog;
	
	
	public ToolImportItemListener(Shell parentShell, Navigation parentNavigation, OSDEA_StatusLine parentStl) {
		shell = parentShell;
		navigation = parentNavigation;
		stl = parentStl;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		importData();

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		importData();

	}
	
	private void importData() {

		wizard = new ImportFileWizard(navigation, stl);
		wizardDialog = new WizardDialog(
				shell,
				wizard);
		wizardDialog.create();
		int returnCode = wizardDialog.open();
		if(returnCode == Dialog.OK) {
//			stl.setBarLabel0("Import Successful");
		}
		else {
//			stl.setBarLabel2("Import Cancelled!");
		}

	}

}
