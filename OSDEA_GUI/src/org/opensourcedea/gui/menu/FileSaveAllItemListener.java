package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class FileSaveAllItemListener implements SelectionListener {
	
	private Navigation nav;
	private OSDEA_StatusLine stl;

	
	
	public FileSaveAllItemListener(Navigation parentNavigation, OSDEA_StatusLine stl) {
		nav = parentNavigation;
		this.stl = stl;
	}
	
    public void widgetSelected(SelectionEvent event) {

    	
    	
    	LDEAPSaver saver = new LDEAPSaver(nav, stl);

    	//    	saver.saveFileAs(ldeap);

    	TreeItem[] tia = nav.getAllTreeItems();

    	for(TreeItem ti : tia) {

    		LDEAProblem ldeap = nav.getSelectedDEAProblem(ti);

    		//WIP
    		if(nav.getFilePath(ti) != null) {
    			saver.saveFile(nav.getFilePath(ti), ldeap);
    		}
    		else {
    			saver.saveFileAs(ldeap, ti);
    		}


    	}
    	
    }
    
    


    public void widgetDefaultSelected(SelectionEvent event) {
    	
    }
	
}
