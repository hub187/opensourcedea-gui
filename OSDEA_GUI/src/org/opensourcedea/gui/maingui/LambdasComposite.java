package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LambdasComposite extends Composite {
	
	private Label lambdasLabel;
	
	public LambdasComposite(Composite parentComp) {
		super(parentComp, 0);
		
		lambdasLabel = new Label(this, SWT.NONE);
		
		lambdasLabel.setText("This is the Lambdas Composite");
		
		lambdasLabel.setLocation(20, 20);
		
		lambdasLabel.pack();

		
	}
	
}
