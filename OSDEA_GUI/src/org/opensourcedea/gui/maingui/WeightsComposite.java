package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WeightsComposite extends Composite {
	
	private Label weightsLabel;
	
	public WeightsComposite(Composite parentComp) {
		super(parentComp, 0);
		
		weightsLabel = new Label(this, SWT.NONE);
		
		weightsLabel.setText("This is the Weights Composite");
		
		weightsLabel.setLocation(20, 20);
		
		weightsLabel.pack();

		
	}
	
}
