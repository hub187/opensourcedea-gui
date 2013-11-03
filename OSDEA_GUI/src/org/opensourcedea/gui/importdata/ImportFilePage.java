package org.opensourcedea.gui.importdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
		"Excel 2007 file (*.xls)",
		"csv file (*.csv)"
	};
	private static final String[] filterExts = {
		"*.xls",
		"*.csv"
	};
	private Composite topLevel;
	private Label descriptionLabel;
	private FormData formData;
	private Label fromFileLabel;
	private Color white;
	private Button button;
	private final String instructions;
	private String spreadSheetName;
	private HSSFWorkbook wb;
	
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
		formData.top = new FormAttachment(fromFileLabel, 25);
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
			
			Integer returnInt = 1;
			ArrayList<String> spreadsheetNames = new ArrayList<String>();
			String extension = IOManagement.getExtension(this.getFileName());
			
			if (extension.equals("xls")) {
				try {
					HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(this.getFileName()));
					for (int k = 0; k < wb.getNumberOfSheets(); k++) {
						HSSFSheet sheet = wb.getSheetAt(k);
						spreadsheetNames.add(sheet.getSheetName());
					}
					
					ChooseSpreadSheetComposite choseSS = new ChooseSpreadSheetComposite(getShell(), spreadsheetNames);
					returnInt = choseSS.open();
					
					if(returnInt == 0) {
						fromFileLabel.setText("From file (selected SpreadSheet is: " + choseSS.getSelectedSpreadsheet() + "):");
						fromFileLabel.pack();
						setSelectedWb(wb);
						setSpreadSheetName(choseSS.getSelectedSpreadsheet());
						setPageComplete(true);
					}
					else {
						fromFileLabel.setText("From file:");
						fromFileLabel.pack();
						setSpreadSheetName(null);
						setSelectedWb(null);
						setPageComplete(false);
					}
				} catch (FileNotFoundException e) {
					setPageComplete(false);
				} catch (IOException e) {
					setPageComplete(false);
				}
			}
			//at the moment this means this is a csv file
			else {
				setPageComplete(true);
			}
		}
	}
	
	
	
	
	public String getFileName() {
		if(fileName != null){
			return fileName;
		}
		return "";
	}

	public String getSpreadSheetName() {
		return spreadSheetName;
	}

	public void setSpreadSheetName(String spreadSheetName) {
		this.spreadSheetName = spreadSheetName;
	}

	public HSSFWorkbook getSelectedWb() {
		return wb;
	}

	public void setSelectedWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	
	
	
	
}

