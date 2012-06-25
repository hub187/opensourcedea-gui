package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SlacksComposite extends Composite {
	
	private Label slacksLabel;
	
	public SlacksComposite(Composite parentComp) {
		super(parentComp, 0);
		
		slacksLabel = new Label(this, SWT.NONE);
		
		slacksLabel.setText("This is the Slacks Composite");
		
		slacksLabel.setLocation(20, 20);
		
		slacksLabel.pack();

		
	}
	
}
