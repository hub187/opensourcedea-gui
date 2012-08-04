package org.opensourcedea.gui.menu;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.importdata.ImportFileWizard;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class ToolImportItemListener implements SelectionListener {
	
	private Shell shell;
	private Navigation nav;
	private OSDEA_StatusLine stl;
	private ImportFileWizard wizard;
	private WizardDialog wizardDialog;
	
	
	public ToolImportItemListener(Shell parentShell, Navigation parentNavigation, OSDEA_StatusLine parentStl) {
		shell = parentShell;
		nav = parentNavigation;
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
		
		if(nav.getSelectedDEAProblem().isSolved()){
			if(MessageDialog.openQuestion(nav.getShell(), "Reset DEA Problem", "You are trying to import data but the " +
					"problem is already solved. If you click 'Yes', the problem will be completely reset (including variable and " +
					"model settings.\n\n Do you wish to continue?")) {
				nav.completeProblemReset();
			}
			else {
				return;
			}
				
		}
		
		wizard = new ImportFileWizard(nav, stl);
		wizardDialog = new WizardDialog(
				shell,
				wizard);
		wizardDialog.create();
		wizardDialog.open();
		
		

	}

}
