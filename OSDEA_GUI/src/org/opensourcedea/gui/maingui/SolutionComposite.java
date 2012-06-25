package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SolutionComposite extends Composite {
	
	private Label solutionLabel;
	
	public SolutionComposite(Composite parentComp) {
		super(parentComp, 0);
		
		solutionLabel = new Label(this, SWT.NONE);
		
		solutionLabel.setText("This is the Solution Composite");
		
		solutionLabel.setLocation(20, 20);
		
		solutionLabel.pack();

		
	}
	
}
