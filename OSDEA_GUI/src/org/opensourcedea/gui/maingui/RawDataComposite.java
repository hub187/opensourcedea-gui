 package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.parameters.OSDEAParameters;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.gui.utils.MathUtils;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class RawDataComposite extends Composite {

	private Label rawDataLabel;
	private Composite comp;
	private GenericTable tableClass;

	
	public RawDataComposite(Composite parentComp, LDEAProblem ldeap) {
		super(parentComp, 0);

		createControls();
		
		if(ldeap.getDataMatrix() != null && ldeap.getVariableNames() != null) {
			setRawDataTable(ldeap);
		}
		
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



	public void setRawDataTable(LDEAProblem ldeap) {// ArrayList<ArrayList<String>> data) {
		
		
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("DMU Names");
		headers.addAll(ldeap.getVariableNames());
		
		ArrayList<double[]> data = ldeap.getDataMatrix();
		
		//Iterates through data
		ArrayList<ArrayList<String>> strData = new ArrayList<ArrayList<String>>();
		Iterator<double[]> dmus = data.iterator();
		int i = 0;
		while(dmus.hasNext()) {
			double[] arr = dmus.next();
			ArrayList<String> tempArrl = new ArrayList<String>();
			tempArrl.add(ldeap.getDMUNames().get(i));
			for(double d : arr) {
				tempArrl.add(Double.toString(MathUtils.round(d,OSDEAParameters.getRoundingDecimals())));
			}
			i++;
			strData.add(tempArrl);
		}
		
		tableClass = new GenericTable(comp, headers, strData);
		tableClass.setTable();

		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		comp.setBackground(grey);
		comp.layout();

		rawDataLabel.setText("Imported data:");


	}

}
