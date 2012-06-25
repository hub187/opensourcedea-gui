package org.opensourcedea.gui.menu;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.opensourcedea.gui.utils.Images;

public class HelpAboutDialog extends Dialog {
	
	private Object result;
	private Display display;
	private Shell parent;
	private Shell shell;
	private final Image potIcon;
	private final ImageRegistry imgReg;
	private Label aboutDlgLabel;
	private Button okButton;
	private FormData labelFormData,textBoxFormData, okButtonFormData;
	private Color osdeaBlue;
	private String aboutDlgLabelText;
	private final String gpl3Link = "http://gplv3.fsf.org/";
	private final String osdeaLink = "http://www.opensourcedea.org";
	private final String lpsolve = "lpsolve";
	private final String javaWrapper = "java wrapper";
	private final String sireaLink = "SIREA";
	private final String fffLink = "FAMFAMFAM";
	private StyleRange style;

	

	
	public HelpAboutDialog (Shell parent, int style) {
		super (parent, style);
		
		imgReg = Images.getFullImageRegistry(parent.getDisplay());
		potIcon = imgReg.get("potIcon");
		
	}
	
	public HelpAboutDialog (Shell parent) {
		this (parent, 0);
	}
	
	public Object open () {
		parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setTouchEnabled(true);
		shell.setText("About OSDEA");
//		potIcon = new Image(shell.getDisplay(), this.getClass().getResourceAsStream("pot2422-48x48x32.png"));
		shell.setImage(potIcon);
		
		shell.setSize(500, 330);
		shell.setLayout(new FormLayout());
		
		
		aboutDlgLabel = new Label(shell,SWT.CENTER | SWT.BORDER);
		labelFormData = new FormData();
		labelFormData.bottom = new FormAttachment(0, 260);
		labelFormData.right = new FormAttachment(0, 85);
		labelFormData.top = new FormAttachment(0);
		labelFormData.left = new FormAttachment(0);
		aboutDlgLabel.setLayoutData(labelFormData);
		aboutDlgLabel.setImage(potIcon);
		osdeaBlue = new Color (Display.getCurrent (), 63, 76, 107);
		aboutDlgLabel.setBackground(osdeaBlue);

		
		aboutDlgLabelText = "OSDEA is a free open source software licensed under GPL3 (http://gplv3.fsf.org/).\n\n" +
				"OSDEA is written in java using SWT & jFace." +
				"Version 0.50\n\n" +
				"More information on OSDEA can be found at http://www.opensourcedea.org.\n\n" +
				"This software uses lpsolve. The lpsolve java wrapper was developed by Juergen Ebert.\n\n" +
				"OSDEA pot icon from SIREA's virtual kitchen (licensed under Creative Commons).\n\n" +
				"OSDEA silk icons are a Creative Commons courtesy of Mark James from FAMFAMFAM.";
		
		

		
		final StyledText styledText = new StyledText (shell, SWT.MULTI | SWT.BORDER | SWT.WRAP);
		styledText.setMargins(5, 5, 5, 5);
		styledText.setText(aboutDlgLabelText);
		styledText.setEditable(false);
		
		
		style = new StyleRange();
		style.underline = true;
		style.underlineStyle = SWT.UNDERLINE_LINK;
		
		int[] ranges = {aboutDlgLabelText.indexOf(gpl3Link), gpl3Link.length(), aboutDlgLabelText.indexOf(osdeaLink), osdeaLink.length(),
				aboutDlgLabelText.indexOf(lpsolve), lpsolve.length(), aboutDlgLabelText.indexOf(javaWrapper), javaWrapper.length(),
				aboutDlgLabelText.indexOf(sireaLink), sireaLink.length(), aboutDlgLabelText.indexOf(fffLink), fffLink.length()}; 
		
		StyleRange[] styles = {style, style, style, style, style, style};
		styledText.setStyleRanges(ranges, styles);
		
		styledText.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				if ((event.stateMask ^ SWT.MOD1) != 0) {
					try {
						int offset = styledText.getOffsetAtLocation(new Point (event.x, event.y));
						StyleRange style = styledText.getStyleRangeAtOffset(offset);
						if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
							
							if(offset >= styledText.getText().indexOf(gpl3Link) && 
									offset <= styledText.getText().indexOf(gpl3Link) + gpl3Link.length()) {
								org.eclipse.swt.program.Program.launch(gpl3Link.toString());
							}
							else if(offset >= styledText.getText().indexOf(osdeaLink) && 
									offset <= styledText.getText().indexOf(osdeaLink) + osdeaLink.length()) {
								org.eclipse.swt.program.Program.launch(osdeaLink.toString());
							}
							else if(offset >= styledText.getText().indexOf(lpsolve) && 
									offset <= styledText.getText().indexOf(lpsolve) + lpsolve.length()) {
								org.eclipse.swt.program.Program.launch("http://lpsolve.sourceforge.net/");
							}
							else if(offset >= styledText.getText().indexOf(javaWrapper) && 
									offset <= styledText.getText().indexOf(javaWrapper) + javaWrapper.length()) {
								org.eclipse.swt.program.Program.launch("http://lpsolve.sourceforge.net/5.5/Java.htm");
							}
							else if(offset >= styledText.getText().indexOf(sireaLink) && 
									offset <= styledText.getText().indexOf(sireaLink) + sireaLink.length()) {
								org.eclipse.swt.program.Program.launch("http://www.sireasgallery.com/iconset/virtualkitchen/");
							}
							else if(offset >= styledText.getText().indexOf(fffLink) && 
									offset <= styledText.getText().indexOf(fffLink) + fffLink.length()) {
								org.eclipse.swt.program.Program.launch("http://www.famfamfam.com/lab/icons/silk/");
							}
							
							
							else {
								System.out.println("something wrong happened. Cannot open the link");
							}
							

						}
					} catch (IllegalArgumentException e) {

					}
					
				}
			}
		});
		
		
		
		textBoxFormData = new FormData();
		textBoxFormData.bottom = new FormAttachment(0, 260);
		textBoxFormData.right = new FormAttachment(0, 494);
		textBoxFormData.top = new FormAttachment(0);
		textBoxFormData.left = new FormAttachment(0, 85);

		styledText.setLayoutData(textBoxFormData);
		styledText.setTouchEnabled(true);

		okButton = new Button(shell, SWT.PUSH);
		okButtonFormData = new FormData();
		okButtonFormData.right = new FormAttachment(0, 294);
		okButtonFormData.top = new FormAttachment(styledText, 12);
		okButtonFormData.bottom = new FormAttachment(100, -10);
		okButtonFormData.left = new FormAttachment(0, 214);
		okButton.setLayoutData(okButtonFormData);
		okButton.setText("OK");
		okButton.addMouseListener(new MouseListener()  {
		      public void mouseDown(MouseEvent e) {
		    	  shell.close();
		        }

		        public void mouseUp(MouseEvent e) {
		        	shell.close();
		        }

		        public void mouseDoubleClick(MouseEvent e) {

		        }
		      });
		

		
		shell.open();
		display = parent.getDisplay();
		
		while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
		}
		
		
		osdeaBlue.dispose();
//		potIcon.dispose();
//		display.dispose();
		
		return result;
	}
}
