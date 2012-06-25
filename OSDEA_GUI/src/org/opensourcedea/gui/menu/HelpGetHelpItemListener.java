package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class HelpGetHelpItemListener implements SelectionListener {
    
	private OSDEA_StatusLine stl;

	
	public HelpGetHelpItemListener(OSDEA_StatusLine parentStl) {
		stl = parentStl;
	}
	
	public void widgetSelected(SelectionEvent event) {
    	stl.setNotificalLabelDelayStandard("You're on your own for the time being! :p");
    }

    public void widgetDefaultSelected(SelectionEvent event) {
    	stl.setNotificalLabelDelayStandard("You're on your own for the time being! :p");
	}
}
