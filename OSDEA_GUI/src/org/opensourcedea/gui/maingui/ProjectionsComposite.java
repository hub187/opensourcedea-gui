package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ProjectionsComposite extends Composite {
	
	private Label projectionsLabel;
	
	public ProjectionsComposite(Composite parentComp) {
		super(parentComp, 0);
		
		projectionsLabel = new Label(this, SWT.NONE);
		
		projectionsLabel.setText("This is the Projections Composite");
		
		projectionsLabel.setLocation(20, 20);
		
		projectionsLabel.pack();

		
	}
	
}
