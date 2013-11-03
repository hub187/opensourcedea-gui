package org.opensourcedea.gui.importdata;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.utils.Images;

public class ChooseSpreadSheetComposite extends Dialog {

	private final ImageRegistry imgReg;
	private final Image potIcon;
	private ArrayList<String> spreadsheetNames;
	private int dialogHeigh = 250;
	private String selectedSpreadsheet = null;
	private List listOfSS;

	public ChooseSpreadSheetComposite (Shell parent, ArrayList<String> parentSpreadsheetNames) {
		super (parent);

		imgReg = Images.getMainGUIImageRegistry(parent.getDisplay());
		potIcon = imgReg.get("potIcon");
		spreadsheetNames = parentSpreadsheetNames;

	}
	
	
	public String getSelectedSpreadsheet() {
		return selectedSpreadsheet;
	}
	
	@Override
	protected void okPressed() {
		selectedSpreadsheet = listOfSS.getSelection()[0].toString();
		setReturnCode(OK);
		close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		Label label = new Label(container, SWT.WRAP);
		label.setText("Please select the spreadsheet where your data are.");
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(0, 10);
		label.setLayoutData(formData);


		listOfSS = new List(container, SWT.V_SCROLL);
		Iterator<String> it = spreadsheetNames.iterator();
		while (it.hasNext()) {
			listOfSS.add(it.next());
		}
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(label, 10);
		formData.bottom = new FormAttachment(100, 0);
		listOfSS.setLayoutData(formData);

		return container;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Select your spreadsheet");
		shell.setImage(potIcon);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, dialogHeigh);
	}


}
