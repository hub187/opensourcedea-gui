package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

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
    	
   	
    	LDEAPSaver saver = new LDEAPSaver(nav, stl);
    	if(!saver.checkBeforeClosing()) {
    		return;
    	}
    	
    	
    	shell.close();
	    display.dispose();
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    	
    }
}
