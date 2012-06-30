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
import org.opensourcedea.gui.utils.MathUtils;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class ObjectivesComposite extends Composite {
	
	private Label objectivesLabel;
	private Composite tableComp;
	private Composite comp;
	private int precision = 6;
	
	public ObjectivesComposite(Composite parentComp) {
		super(parentComp, 0);
		
		this.setLayout(new FormLayout());
		ScrolledComposite sComp = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
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
		
		
		objectivesLabel = new Label(comp, SWT.NONE);
		objectivesLabel.setText("You need to solve the problem first.");
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		objectivesLabel.setLayoutData(fdata);
				

		fdata = new FormData();
		fdata.top = new FormAttachment(objectivesLabel, 20);
		fdata.left = new FormAttachment(0, 20);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.width = 300;
//		fdata.right = new FormAttachment(100, -25);

		tableComp = new Composite(comp, SWT.BORDER);
		tableComp.setLayoutData(fdata);
		tableComp.setLayout(new FillLayout());

		
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
//		sComp.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		Point prefSize = comp.computeSize(350, 300);
//		prefSize.x = prefSize.x + 20;
//		prefSize.y = prefSize.y + 50;
		sComp.setMinSize(prefSize);
		
		
	}
	
	
	public void displayObjectives(LDEAProblem ldeap) {
		
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("DMU Names");
		headers.add("Objective Value");
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < ldeap.getDMUNames().size(); i++) {
			ArrayList<String> tempArr = new ArrayList<String>();
			tempArr.add(ldeap.getDMUNames().get(i));
			if(MathUtils.round(ldeap.getLdeapSolution().getObjective(i), precision) == 1) {
				tempArr.add("1");
			}
			else{
				tempArr.add(Double.toString(ldeap.getLdeapSolution().getObjective(i)));
			}
			data.add(tempArr);
		}
		
		
		GenericTable solTable = new GenericTable(tableComp, headers, data);
		solTable.setTable();
		
		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		tableComp.setBackground(grey);
		tableComp.layout();
		
		objectivesLabel.setText("The problem objectives are as follows: ");
		
		comp.layout();
		
	}
	
	

	
}
