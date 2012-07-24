package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.utils.Images;

public class SolutionComposite extends Composite {
	
	private Label solutionLabel1;
	private Label solutionLabel2;
	private Canvas solCanvas;
	
	public SolutionComposite(Composite parentComp) {
		super(parentComp, 0);
		
		this.setLayout(new FormLayout());
		
		Composite innerComp = new Composite(this, SWT.NONE);
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100);
		fdata.height = 30;
		innerComp.setLayoutData(fdata);
		
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		innerComp.setLayout(glayout);
		
		
		solCanvas = new Canvas(innerComp, SWT.NONE);
		Images.paintCanvas(solCanvas, "solution");
		GridData gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		gdata.verticalAlignment = SWT.CENTER;
		gdata.grabExcessVerticalSpace = true;
		solCanvas.setLayoutData(gdata);
		
		solutionLabel1 = new Label(innerComp, SWT.WRAP);
		solutionLabel1.setText("Solution");
		gdata = new GridData();
		gdata.heightHint = 16;
		gdata.verticalAlignment = SWT.CENTER;
		gdata.horizontalAlignment = SWT.LEFT;
		gdata.grabExcessVerticalSpace = true;
		solutionLabel1.setLayoutData(gdata);
		
		solutionLabel2 = new Label(this, SWT.WRAP);
		solutionLabel2.setText("After you've solved the problem, you can view the solution by expanding the solution item in the tree and " +
		"selecting the part of the solution you want.");
		fdata = new FormData();
		fdata.top = new FormAttachment(innerComp, 10);
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -20);
		solutionLabel2.setLayoutData(fdata);
		
	
	}
	
}
