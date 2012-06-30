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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.opensourcedea.gui.utils.Images;

public class InstructionsComposite extends Composite {
	
	
	private Composite comp;
	private ScrolledComposite sComp;
	
	
	InstructionsComposite(Composite parentComp) {
		super(parentComp, SWT.NONE);
		
		
		this.setLayout(new GridLayout());
		
		
		
		sComp = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		sComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sComp.setLayout(new FillLayout());
		
		comp = new Composite(sComp, SWT.NONE);
		comp.setLayout(new FormLayout());
		
		createTitle();
		
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
		sComp.setMinSize(400, 300);
		
		
	}
	
	
	private void createTitle() {
		
		Composite titleComp = new Composite(comp, SWT.NONE);
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
