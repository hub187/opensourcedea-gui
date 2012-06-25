package org.opensourcedea.gui.menu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class FileNewItemListener implements SelectionListener {
	
	private Navigation navigation;
	private OSDEA_StatusLine stl;
	
	public FileNewItemListener(Navigation parentNavigation, OSDEA_StatusLine stl) {
		navigation = parentNavigation;
		this.stl = stl;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		addNew(navigation);

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		addNew(navigation);

	}
	
	private void addNew(Navigation nav) {
		nav.addDEAProblem("New DEA Problem");
		stl.setNotificalLabelDelayStandard("Created new DEA Problem.");
		
	}

}
