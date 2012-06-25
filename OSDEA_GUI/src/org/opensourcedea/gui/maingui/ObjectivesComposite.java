package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ObjectivesComposite extends Composite {
	
	private Label objectivesLabel;
	
	public ObjectivesComposite(Composite parentComp) {
		super(parentComp, 0);
		
		objectivesLabel = new Label(this, SWT.NONE);
		
		objectivesLabel.setText("This is the Objectives Composite");
		
		objectivesLabel.setLocation(20, 20);
		
		objectivesLabel.pack();

		
	}
	
}
