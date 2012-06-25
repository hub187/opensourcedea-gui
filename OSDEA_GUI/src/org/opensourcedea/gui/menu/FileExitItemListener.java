package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FileExitItemListener implements SelectionListener {
	
	private Shell shell;
	private Display display;
	
	public FileExitItemListener(Shell parentShell) {
		shell = parentShell;
		display = parentShell.getDisplay();
	}
	
    public void widgetSelected(SelectionEvent event) {
    	shell.close();
	    display.dispose();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    	shell.close();
    	display.dispose();
    }
}
