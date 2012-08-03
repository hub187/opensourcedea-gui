package org.opensourcedea.gui.menu;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class FileOpenItemListener implements SelectionListener {
	
	private Navigation nav;
	private OSDEA_StatusLine stl;
	private static final String[] filterNames = {
		"DEA Problem file (*.deap)", "All Files (*.*)"
	};
	private static final String[] filterExts = {
		"*.deap", "*.*"
	};
	
	
	public FileOpenItemListener(Navigation nav, OSDEA_StatusLine stl) {
		this.nav = nav;
		this.stl = stl;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		
		FileDialog openDialog = new FileDialog(nav.getShell(), SWT.OPEN);
		openDialog.setFilterNames(filterNames);
		openDialog.setFilterExtensions(filterExts);
		
		String fileName = openDialog.open();
		
		if(fileName != null) {
			try {
				FileInputStream fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				LDEAProblem ldeap = (LDEAProblem)ois.readObject();
				fis.close();
				ois.close();
				
				TreeItem ti = nav.addDEAProblem(ldeap.getModelName(), ldeap, "File opened successfully.");
				nav.addFilePath(fileName, ti);
//				stl.setNotificalLabelDelayStandard("File opened successfully.");
				
			}
			catch (Exception ex) {
				MessageDialog.open(SWT.ERROR, nav.getShell(), "Error", "An error occured and the file could not be opened properly." +
						"\n\nThis is likely because the file you are trying to open was saved with an older and non-compatible " +
						"version of OSDEA. The only way you can open that file would be to use the version of OSDEA you used to save the file.", SWT.NONE);
				stl.setNotificalLabelDelayStandard("File could not be opened!");
			}
		}
		else {
			stl.setNotificalLabelDelayStandard("File Opening Cancelled.");
		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		

	}

}
