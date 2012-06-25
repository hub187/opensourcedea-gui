package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class FileSaveAsItemListener implements SelectionListener {
	
	private Navigation nav;
	private OSDEA_StatusLine stl;

	
	
	public FileSaveAsItemListener(Navigation parentNavigation, OSDEA_StatusLine stl) {
		nav = parentNavigation;
		this.stl = stl;
	}
	
    public void widgetSelected(SelectionEvent event) {

    	
    	LDEAProblem ldeap = nav.getSelectedDEAProblem();
    	LDEAPSaver saver = new LDEAPSaver(nav, stl);

    	saver.saveFileAs(ldeap);
 
        
    	
    }
    
    


    public void widgetDefaultSelected(SelectionEvent event) {
    	
    }
	
}
