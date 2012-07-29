package org.opensourcedea.gui.menu;

import java.io.IOException;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.opensourcedea.gui.exportdata.LDEAPExporter;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class ToolExportItemListener implements SelectionListener {
	
	private Navigation nav;
	private OSDEA_StatusLine stl;
	
	public ToolExportItemListener(Navigation nav, OSDEA_StatusLine stl) {
		this.nav = nav;
		this.stl = stl;
	}

	public void exportData() {

		if(nav.getSelectedDEAProblem() == null){
			stl.setNotificalLabelDelayStandard("Close failed!");
			return;
		}

		LDEAProblem ldeap = nav.getSelectedDEAProblem();

		LDEAPExporter exporter = new LDEAPExporter(ldeap, nav.getShell());
		try {
			exporter.exportToFile();
		} catch (IOException e) {
			stl.setNotificalLabelDelayStandard("The DEA Problem could not be exported!");
			return;
		}
		
		stl.setNotificalLabelDelayStandard("DEA Problem was exported successfully!");

	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		exportData();

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		exportData();

	}

}
