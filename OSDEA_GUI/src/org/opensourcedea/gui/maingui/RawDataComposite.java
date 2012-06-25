 package org.opensourcedea.gui.maingui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.utils.Images;

public class RawDataComposite extends Composite {

	private Label rawDataLabel;
	private Composite comp;
	private GenericTable tableClass;

	public RawDataComposite(Composite parentComp) {
		super(parentComp, 0);
		
		createControls();

	}
	
	public RawDataComposite(Composite parentComp, ArrayList<String> variableName, ArrayList<String> dmuNames, ArrayList<double[]> dataMatrix) {
		super(parentComp, 0);

		createControls();
		
		ArrayList<String> headers = variableName;
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < dmuNames.size(); i++) {
			ArrayList<String> tempArr = new ArrayList<String>();
			tempArr.add(dmuNames.get(i));
			for(double d : dataMatrix.get(i)) {
				tempArr.add(Double.toString(d));
			}
			data.add(tempArr);
		}
		
		setRawDataTable(headers, data);
		
		
	}
	
	
	private void createControls() {
		
		this.setLayout(new FormLayout());

		rawDataLabel = new Label(this, SWT.NONE);
		rawDataLabel.setText("Once you have imported data, you can view them here:");
		FormData fData = new FormData();
		fData.top = new FormAttachment(0, 15);
		fData.left = new FormAttachment(0, 20);
		rawDataLabel.setLayoutData(fData);

		String helpText = "The imported data will be visible here.\n" +
				"If you have not imported some data already, go to:\nTools\\Import or from the toolbar icon.";
		Images.setHelpIcon(this, helpText, 15, 25);

		fData = new FormData();
		fData.top = new FormAttachment(rawDataLabel, 20);
		fData.left = new FormAttachment(0, 20);
		fData.bottom = new FormAttachment(100, -10);
		fData.right = new FormAttachment(100, -25);

		comp = new Composite(this, SWT.BORDER);
		comp.setLayoutData(fData);
		comp.setLayout(new FillLayout());
	}



	public void setRawDataTable(ArrayList<String> headers, ArrayList<ArrayList<String>> data) {

		tableClass = new GenericTable(comp, headers, data);
		tableClass.setTable();

		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		comp.setBackground(grey);
		comp.layout();

		rawDataLabel.setText("Imported data:");


	}

}
