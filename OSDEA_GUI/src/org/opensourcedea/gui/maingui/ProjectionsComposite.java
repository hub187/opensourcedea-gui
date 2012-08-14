package org.opensourcedea.gui.maingui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.parameters.OSDEAGUIParameters;
import org.opensourcedea.gui.utils.Dimensions;
import org.opensourcedea.gui.utils.MathUtils;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class ProjectionsComposite extends Composite {
	
	private Label projectionsLabel;
	private ScrolledComposite sComp;
	private Composite tableComp;
	private Composite comp;
	
	public ProjectionsComposite(Composite parentComp) {
		super(parentComp, 0);
		
		this.setLayout(new FormLayout());
		sComp = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fdata = new FormData();
		fdata.bottom = new FormAttachment(100);
		fdata.top = new FormAttachment(0);
		fdata.left = new FormAttachment(0);
		fdata.right = new FormAttachment(100);
		sComp.setLayoutData(fdata);
		sComp.setLayout(new FormLayout());
		
		
		comp = new Composite(sComp, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(0);
		formData.bottom = new FormAttachment(100);
		comp.setLayoutData(formData);
		comp.setLayout(new FormLayout());
		
		projectionsLabel = new Label(comp, SWT.NONE);
		
		resetComposite();
		
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
		sComp.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		
	}
	
	public void resetComposite() {
		
		projectionsLabel.setText("You need to solve the problem first.");
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		projectionsLabel.setLayoutData(fdata);
				

		fdata = new FormData();
		fdata.top = new FormAttachment(projectionsLabel, 20);
		fdata.left = new FormAttachment(0, 20);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.width = 300;
		
		if(tableComp !=null) {
			tableComp.dispose();
		}

		tableComp = new Composite(comp, SWT.BORDER);
		tableComp.setLayoutData(fdata);
		tableComp.setLayout(new FillLayout());
		
		comp.layout();
	}
	
	
	public void displaySolution(LDEAProblem ldeap) {
		
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("DMU Names");
		headers.addAll(ldeap.getVariableNames());
		int nbVar = ldeap.getVariableNames().size();
		
		int width = Dimensions.getTotalStringLength(tableComp, headers);
		FormData fdata = (FormData) tableComp.getLayoutData();
		fdata.width = Math.max(300, width);
		tableComp.setLayoutData(fdata);
		tableComp.layout();
		Point prefSize = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		prefSize.x = prefSize.x + 20;
		prefSize.y = prefSize.y + 50;
		sComp.setMinSize(prefSize);
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < ldeap.getDMUNames().size(); i++) {
			ArrayList<String> tempArr = new ArrayList<String>();
			tempArr.add(ldeap.getDMUNames().get(i));
			for(int j = 0; j < nbVar; j++) {
				tempArr.add(Double.toString(MathUtils.round(ldeap.getLdeapSolution().getProjections()[i][j],OSDEAGUIParameters.getRoundingDecimals())));
			}
			data.add(tempArr);
		}
		
		
		
		GenericTable solTable = new GenericTable(tableComp, headers, data);
		solTable.setTable();
		
		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		tableComp.setBackground(grey);
		tableComp.layout();
		
		projectionsLabel.setText("The problem projections are as follows: ");
		
		comp.layout();
		
	}
	
	
}
