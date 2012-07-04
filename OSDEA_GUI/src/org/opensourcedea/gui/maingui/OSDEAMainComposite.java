package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;

public class OSDEAMainComposite extends Composite {

	private Navigation navigation;
	private OSDEA_StatusLine stl;
	private Composite dataPanel;
	private final StackLayout stackLayout = new StackLayout();
	private final InstructionsComposite instrComp;

	public OSDEAMainComposite(Composite parent, OSDEA_StatusLine parentStl, int style) {
		super(parent, style);
	
		stl = parentStl;

		
		dataPanel = new Composite(this, SWT.BORDER);
		dataPanel.setLayout(stackLayout);
		
		instrComp = new InstructionsComposite(dataPanel);
		setDataPanelTopControl(instrComp);

		
		
		

		
		navigation = new Navigation(this, stl);
		navigation.setLocation(0,0);
		navigation.pack();
		

		
		final Sash sash = new Sash (this, SWT.VERTICAL);

		
		final FormLayout form = new FormLayout ();
		this.setLayout (form);
		
		int startSize = 220;

		
		FormData navData = new FormData();
		navData.left = new FormAttachment(0, 0);
		navData.right = new FormAttachment(sash, 0);
		navData.top = new FormAttachment(0, 0);
		navData.bottom = new FormAttachment(100, 0);
		navigation.setLayoutData(navData);

		final int minSize = 20;


		
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(0, startSize);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData (sashData);
		
		final OSDEAMainComposite mainComp = this;
		
		sash.addListener (SWT.Selection, new Listener () {
			public void handleEvent(Event e) {
				Rectangle rect = sash.getBounds();
				Rectangle osdeaCompRect =  mainComp.getClientArea ();
				int right = osdeaCompRect.width - rect.width - minSize;
				e.x = Math.max (Math.min (e.x, right), minSize);
				if (e.x != rect.x)  {
					sashData.left = new FormAttachment (0, e.x);
					mainComp.layout ();
				}
			}
		});
		
		FormData dataPanelData = new FormData();
		dataPanelData.left = new FormAttachment(sash, 0);
		dataPanelData.right = new FormAttachment(100, 0);
		dataPanelData.top = new FormAttachment(0, 0);
		dataPanelData.bottom = new FormAttachment(100, 0);
		dataPanel.setLayoutData(dataPanelData);
		
		
		


	}

	public Navigation getNavigation() {
		return navigation;
	}
	
	public void setDataPanelTopControl(Control compName) {		
		stackLayout.topControl = compName;
		dataPanel.layout();
	}
	
	public void setDataPanelTopControlAsInstr() {
		stackLayout.topControl = instrComp;
		dataPanel.layout();
	}
	
	public Composite getDataPanel() {
		return dataPanel;
	}

}
