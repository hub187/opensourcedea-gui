package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

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
import org.opensourcedea.dea.NonZeroLambda;
import org.opensourcedea.gui.parameters.OSDEAParameters;
import org.opensourcedea.gui.utils.Dimensions;
import org.opensourcedea.gui.utils.MathUtils;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class LambdasComposite extends Composite {
	
	private Label lambdasLabel;
	private ScrolledComposite sComp;
	private Composite tableComp;
	private Composite comp;
	
	public LambdasComposite(Composite parentComp) {
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
		
		lambdasLabel = new Label(comp, SWT.NONE);
		
		resetComposite();
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
		sComp.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	
	public void resetComposite() {
		lambdasLabel.setText("You need to solve the problem first.");
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		lambdasLabel.setLayoutData(fdata);
				
		fdata = new FormData();
		fdata.top = new FormAttachment(lambdasLabel, 20);
		fdata.left = new FormAttachment(0, 20);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.width = 300;
		
		if(tableComp != null) {
			tableComp.dispose();
		}
		
		tableComp = new Composite(comp, SWT.BORDER);
		tableComp.setLayoutData(fdata);
		tableComp.setLayout(new FillLayout());
	}
	
	public void displaySolution(LDEAProblem ldeap) {
		
		ArrayList<Integer> efficientReferencedDMUs = new ArrayList<Integer>();
		for(int i = 0; i < ldeap.getLdeapSolution().getReferenceSet().length; i++) {
			Iterator<NonZeroLambda> it = ldeap.getLdeapSolution().getReferenceSet()[i].iterator();
			while(it.hasNext()) {
				NonZeroLambda tempNzl = it.next();
				if(!efficientReferencedDMUs.contains(tempNzl.getDMUIndex())){
					efficientReferencedDMUs.add(tempNzl.getDMUIndex());
				}
			}
		}
		Collections.sort(efficientReferencedDMUs);
		
		
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("DMU Names");
		Iterator<Integer> it = efficientReferencedDMUs.iterator();
		while(it.hasNext()) {
			headers.add(ldeap.getDMUNames().get(it.next()));
		}
		
		int nbNzl = efficientReferencedDMUs.size();
		
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
		for(int i = 0; i < ldeap.getLdeapSolution().getReferenceSet().length; i++) {
			Iterator<NonZeroLambda> itNzl = ldeap.getLdeapSolution().getReferenceSet()[i].iterator();
			HashMap<Integer, Double> nzlHashMap = new HashMap<Integer, Double>();
			while(itNzl.hasNext()) {
				NonZeroLambda tempNzl = itNzl.next();
				nzlHashMap.put(tempNzl.getDMUIndex(), tempNzl.getLambdaValue());
			}
			
			ArrayList<String> tempArr = new ArrayList<String>();
			tempArr.add(ldeap.getDMUNames().get(i));
			for(int j = 0; j < nbNzl; j++) {
				if(nzlHashMap.containsKey(efficientReferencedDMUs.get(j))) {
					tempArr.add(Double.toString(MathUtils.round(nzlHashMap.get(efficientReferencedDMUs.get(j)),OSDEAParameters.getRoundingDecimals())));
				}
				else {
					tempArr.add("0");
				}
			}
			
			data.add(tempArr);
		}
		

		
		
		
		GenericTable solTable = new GenericTable(tableComp, headers, data);
		solTable.setTable();
		
		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		tableComp.setBackground(grey);
		tableComp.layout();
		
		lambdasLabel.setText("The problem lambdas are as follows: ");
		
		comp.layout();
		
	}
	
}
