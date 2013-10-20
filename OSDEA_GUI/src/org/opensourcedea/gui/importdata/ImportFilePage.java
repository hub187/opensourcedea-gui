package org.opensourcedea.gui.importdata;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.gui.utils.Images;



public class ImportFilePage extends WizardPage
{
	
	public static final String PAGE_NAME = "ImportFilePage";
	private String fileName = "";	
	private Text pathText;
	private static final String[] filterNames = {
		"csv file (*.csv)"
	};
	private static final String[] filterExts = {
		"*.csv"
	};
	private Composite topLevel;
	private Label descriptionLabel;
	private FormData formData;
	private Label fromFileLabel;
	private Color white;
	private Button button;
	private final String instructions;
	
	
	public ImportFilePage(String instr, Display display)
	{	
		super(PAGE_NAME, "Import Data",null);

		setImageDescriptor(ImageDescriptor.createFromImage(Images.getFullImageRegistry(display).get("importWizard")));
		setDescription("Browse to a file you want to import.");
		setPageComplete(false);
		instructions = instr;
	}
	
	public void createControl(Composite parent)
	{	
		

	    getShell().setImage(Images.getFullImageRegistry(getShell().getDisplay()).get("importWizard"));
	    getShell().setText("Import Data"); //doesn't work for some reason
		
		
		topLevel = new Composite(parent, SWT.NONE);
		
		topLevel.setLayout(new FormLayout());
		
		descriptionLabel = new Label(topLevel, SWT.NONE);
		descriptionLabel.setText("Browse to the CVS file you want to import.\n\n" +
		"For the import to be successful:\n" + instructions);
		formData = new FormData();
		formData.left = new FormAttachment(2);
		formData.top = new FormAttachment(2);
		formData.right = new FormAttachment(98);
		descriptionLabel.setLayoutData(formData);
		
		fromFileLabel = new Label(topLevel, SWT.NONE);
		fromFileLabel.setText("From file:");
		formData = new FormData();
		formData.left = new FormAttachment(2);
		formData.top = new FormAttachment(descriptionLabel, 30);
		fromFileLabel.setLayoutData(formData);
		
		pathText = new Text(topLevel, SWT.BORDER);
		pathText.setEditable(false);
		white = new Color(Display.getCurrent(), 255, 255, 255);
		pathText.setBackground(white);
		formData = new FormData();
		formData.width = 380;
		formData.left = new FormAttachment(2);
		formData.right = new FormAttachment(100, -110);
		formData.top = new FormAttachment(fromFileLabel, 25);
		pathText.setLayoutData(formData);
		
		button = new Button(topLevel, SWT.PUSH);
		button.setText("  Browse...  ");
		formData = new FormData();
		formData.left = new FormAttachment(pathText, 10);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(fromFileLabel, 22);
		button.setLayoutData(formData);
		
		button.setFocus();
		
		button.addMouseListener(new MouseListener()  {
		      public void mouseDown(MouseEvent e) {
//		    	  	assignFileName();
		        }
		        public void mouseUp(MouseEvent e) {
		        	assignFileName(); 
		        }
		        public void mouseDoubleClick(MouseEvent e) {
		        	
		        }
		      });
		
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				assignFileName();
	
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
//				assignFileName();				
			}
			
		});
		
		
		setControl(topLevel);

	}
	
	
	private void assignFileName() {
		
		fileName = IOManagement.getFilePath(getShell(), filterNames, filterExts);
		if(fileName != null){
			pathText.setText(fileName);
			setPageComplete(true);
		}
	}

	public String getFileName() {
		if(fileName != null){
			return fileName;
		}
		return "";
	}

	
	
	
	
}

