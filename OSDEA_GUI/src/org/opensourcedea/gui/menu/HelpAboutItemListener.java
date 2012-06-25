package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

public class HelpAboutItemListener implements SelectionListener {
	
	private Shell shell;
	
	public HelpAboutItemListener(Shell parentShell) {
		shell = parentShell;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		HelpAboutDialog aboutDialog = new HelpAboutDialog(shell);
		aboutDialog.open();

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		HelpAboutDialog aboutDialog = new HelpAboutDialog(shell);
		aboutDialog.open();

	}
	
	

}
