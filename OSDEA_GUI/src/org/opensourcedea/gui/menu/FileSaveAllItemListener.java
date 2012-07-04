package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class FileSaveAllItemListener implements SelectionListener {
	
	private Navigation nav;
	private OSDEA_StatusLine stl;

	
	
	public FileSaveAllItemListener(Navigation parentNavigation, OSDEA_StatusLine stl) {
		nav = parentNavigation;
		this.stl = stl;
	}
	
    public void widgetSelected(SelectionEvent event) {

    	
    	
    	LDEAPSaver saver = new LDEAPSaver(nav, stl);
    	saver.saveAll();
    	
    	
    }
    
    


    public void widgetDefaultSelected(SelectionEvent event) {
    	
    }
	
}
