package org.opensourcedea.gui.maingui;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.utils.Images;

public class InstructionsComposite extends Composite {
	
	
	private Composite comp;
	private ScrolledComposite sComp;
	private Composite titleComp;
	private Composite mainComp;
	
	InstructionsComposite(Composite parentComp) {
		super(parentComp, SWT.NONE);
		
		
		this.setLayout(new GridLayout());
		
		
		
		sComp = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		sComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sComp.setLayout(new FillLayout());
		
		comp = new Composite(sComp, SWT.NONE);
		comp.setLayout(new FormLayout());
		
		Images.setHelpIcon(comp, "This is an help icon. Look out for these if you're stuck.",
				10, 10);
		
		createTitle();
		createMainContent();
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
		sComp.setMinSize(400, 300);
		
		
	}
	
	
	private void createMainContent() {
		mainComp = new Composite(comp, SWT.NONE);
		FormData fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		fdata.top = new FormAttachment(titleComp, 20);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.right = new FormAttachment(100, -20);
		mainComp.setLayoutData(fdata);
		mainComp.setLayout(new FormLayout());		
		
		
		Label label = new Label(mainComp, SWT.NONE);
		fdata = new FormData();
		fdata.left = new FormAttachment(0);
		fdata.top = new FormAttachment(0);
		label.setLayoutData(fdata);
		String instr = "OSDEA is an Open Source Data Envelopment Analysis solver which can solve" +
				"many different types of DEA Problems.\n\n\nTo get started, you can either:";
		label.setText(instr);
		
		
		Composite instrContentComp = new Composite(mainComp, SWT.NONE);
		fdata = new FormData();
		fdata.top = new FormAttachment(label, 10);
//		fdata.bottom = new FormAttachment(100);
		fdata.left = new FormAttachment(0);
		fdata.right = new FormAttachment(100);
		instrContentComp.setLayoutData(fdata);
		
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		gLayout.horizontalSpacing = 10;
		gLayout.verticalSpacing = 10;
		instrContentComp.setLayout(gLayout);
		
		Label label0 = new Label(instrContentComp, SWT.NONE);
		label0.setText(" -  Create a new DEA Problem by clicking on the New Icon: ");
		
		Canvas dataCanvas = new Canvas(instrContentComp, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		dataCanvas.setLayoutData(gdata);
		Images.paintCanvas(dataCanvas, "new");

		
		Label label1 = new Label(instrContentComp, SWT.NONE);
		label1.setText(" -  Or open an existing DEA Problem file by clicking on the Open Icon: ");
		
		dataCanvas = new Canvas(instrContentComp, SWT.NONE);
		gdata = new GridData();
		gdata.widthHint = 16;
		gdata.heightHint = 16;
		dataCanvas.setLayoutData(gdata);
		Images.paintCanvas(dataCanvas, "open");
		
		Label easyLabel = new Label(mainComp, SWT.NONE);
		fdata = new FormData();
		fdata.top = new FormAttachment(instrContentComp, 30);
		fdata.left = new FormAttachment(0);
		easyLabel.setLayoutData(fdata);
		easyLabel.setText("If you are familiar with Data envelopment Analysis, using OSDEA should be straight forward." +
				"\n\nLook out for the helps icons on the top right hand corner of each screen. Hover your mouse on those" +
				" if you're stuck!");
		
		
	}
	
	
	private void createTitle() {
		
		titleComp = new Composite(comp, SWT.NONE);
		FormData fdata = new FormData();
		fdata.left = new FormAttachment(0, 20);
		titleComp.setLayoutData(fdata);
		GridLayout gLayout = new GridLayout();
		gLayout.numColumns = 2;
		titleComp.setLayout(gLayout);
		
		
		Canvas dataCanvas = new Canvas(titleComp, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 36;
		gdata.heightHint = 36;
		gdata.grabExcessVerticalSpace = true;
		dataCanvas.setLayoutData(gdata);
		ImageRegistry imgReg = Images.getMainGUIImageRegistry(this.getDisplay());
		Images.paintCanvas(dataCanvas, "potIconSmall", imgReg);
		dataCanvas.setToolTipText("Visit us at www.opensourcedea.org");
		
		String osdeaText = "Open Source DEA";
		StyledText title = new StyledText(titleComp, SWT.NONE);
		title.setEnabled(false);
		title.setText(osdeaText);
		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = osdeaText.length();
		styleRange.fontStyle = SWT.BOLD;
//		styleRange.foreground = this.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		FontData fontData = new FontData();
		fontData.setHeight(16);
		Font font = new Font(this.getDisplay(), fontData);
		
		styleRange.font = font;
		title.setStyleRange(styleRange);
		Color grey = new Color (Display.getCurrent (), 240, 240, 240);
		title.setBackground(grey);
		
		gdata = new GridData();
		gdata.horizontalIndent = 15;
		gdata.grabExcessVerticalSpace = true;
		title.setLayoutData(gdata);
		
	}

}