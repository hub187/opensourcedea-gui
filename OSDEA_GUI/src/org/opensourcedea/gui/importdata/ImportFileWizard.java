package org.opensourcedea.gui.importdata;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.IOManagement;
import org.opensourcedea.gui.utils.Images;
import au.com.bytecode.opencsv.CSVReader;

public class ImportFileWizard extends Wizard {
	
	private Navigation navigation;
	private CSVReader reader;
	private String [] nextLine;
	private ArrayList<String> variableNames = new ArrayList<String>();
	private ArrayList<String> dmuNames = new ArrayList<String>();
	private ArrayList<double[]> dataMatrix = new ArrayList<double[]>();
	private OSDEA_StatusLine stl;
	private final String instructions = "- The file needs to be in a .csv or .xlsformat.\n" +
			"- The first row of the file (or selected SpreadSheet) must contain the headers.\n" +
			"- Headers cannot be empty.\n" +
			"- The DMU Names must be in the first column.\n" +
			"- DMU names cannot be empty.";
	
	
	public ImportFileWizard(Navigation parentNavigation, OSDEA_StatusLine stl)
	{
		super();
		navigation = parentNavigation;
		this.stl = stl;
	}
	
	public void addPages()
	{
		addPage(new ImportFilePage(instructions, this.getShell().getDisplay()));
		setDefaultPageImageDescriptor(ImageDescriptor.createFromImage(Images.getFullImageRegistry(this.getShell().getDisplay()).get("importWizard")));
	}
	
	
	public boolean performFinish()
	{
		ImportFilePage dirPage =
				(ImportFilePage)getPage(ImportFilePage.PAGE_NAME);
		
		String fileName = dirPage.getFileName();
		
		boolean dataNOK = true;
		
		if (IOManagement.getExtension(fileName).equals("csv")) {
			dataNOK = readDataCSV(fileName);
		}
		else if (IOManagement.getExtension(fileName).equals("xls")) {
			dataNOK = readDataXLS(fileName, dirPage.getSpreadSheetName(), dirPage.getSelectedXLSWB());
		}
		else if (IOManagement.getExtension(fileName).equals("xlsx")) {
			dataNOK = readDataXLSX(fileName, dirPage.getSpreadSheetName(), dirPage.getSelectedXlsxWB());
		}
		
		//Depending on whether data were OK or not, import the data or quit
		if (!dataNOK) {
			navigation.importData(dataMatrix, dmuNames, variableNames);
			return true;
		}
		else {
			return false;
		}

		
	}


		/**
		 * Checks data for XLS files and put it in dataMatrix if OK)
		 * @param fileName The name of the file to read data from
		 * @return boolean 'true' if there was a problem, 'false' if there was no problem and data could be imported without any issue.
		 */
		private boolean readDataXLSX(String fileName, String spreadSheetName, XSSFWorkbook wb) {

			try {
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					Sheet sheet = wb.getSheetAt(i);
					if (sheet.getSheetName().equals(spreadSheetName)) {

						String str = ""; //checking for empty headers or values
						int rows = sheet.getPhysicalNumberOfRows();

						//Get Headers - get Variable Names
						Row row = sheet.getRow(0);
						if (row != null) {
							for(int c = 1; c < row.getPhysicalNumberOfCells(); c++) {
								if(str.equals(row.getCell(c).getStringCellValue())) {
									MessageDialog.openWarning(this.getShell(), "Warning", "The variable name after variable '" + variableNames.get(variableNames.size() - 1) +
											"' is empty.\n\n" +
											"The data will not be imported!");
									stl.setNotificalLabelDelayStandard("Data not imported: a Variable Name was empty!");
									return true;
								}
								variableNames.add(row.getCell(c).getStringCellValue());
							}
						}
						else {
							//something wrong file empty => set return false & leave
							return false;
						}


						//next rows
						for (int r = 1; r < rows; r++) {
							row = sheet.getRow(r);
							if (row == null) {
								return false;
							}
							if(str.equals(row.getCell(0).getStringCellValue())){
								MessageDialog.openWarning(this.getShell(), "Warning", "A DMU Name is empty.\n\n" +
										"The data will not be imported!");
								stl.setNotificalLabelDelayStandard("Data not imported: a DMU Name was empty!");
								return true;
							}
							dmuNames.add(row.getCell(0).getStringCellValue());



							//Data
							double[] tempArr2 = new double[row.getPhysicalNumberOfCells() - 1];
							ArrayList<String> tempArrl = new ArrayList<String>();
							tempArrl.add(row.getCell(0).getStringCellValue());
							for(int c = 1; c < row.getPhysicalNumberOfCells(); c++) {
								tempArr2[c - 1] = row.getCell(c).getNumericCellValue();
							}

							dataMatrix.add(tempArr2);

						}


					}
				}

				return false;
			}
			catch (Exception e) {
				//something went wrong obviously...
				return true;
			}



		}

	
	/**
	 * Checks data for XLS files and put it in dataMatrix if OK)
	 * @param fileName The name of the file to read data from
	 * @return boolean 'true' if there was a problem, 'false' if there was no problem and data could be imported without any issue.
	 */
	private boolean readDataXLS(String fileName, String spreadSheetName, HSSFWorkbook wb) {

		try {
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				if (sheet.getSheetName().equals(spreadSheetName)) {

					String str = ""; //checking for empty headers or values
					int rows = sheet.getPhysicalNumberOfRows();

					//Get Headers - get Variable Names
					HSSFRow row = sheet.getRow(0);
					if (row != null) {
						for(int c = 1; c < row.getPhysicalNumberOfCells(); c++) {
							if(str.equals(row.getCell(c).getStringCellValue())) {
								MessageDialog.openWarning(this.getShell(), "Warning", "The variable name after variable '" + variableNames.get(variableNames.size() - 1) +
										"' is empty.\n\n" +
										"The data will not be imported!");
								stl.setNotificalLabelDelayStandard("Data not imported: a Variable Name was empty!");
								return true;
							}
							variableNames.add(row.getCell(c).getStringCellValue());
						}
					}
					else {
						//something wrong file empty => set return false & leave
						return false;
					}


					//next rows
					for (int r = 1; r < rows; r++) {
						row = sheet.getRow(r);
						if (row == null) {
							return false;
						}
						if(str.equals(row.getCell(0).getStringCellValue())){
							MessageDialog.openWarning(this.getShell(), "Warning", "A DMU Name is empty.\n\n" +
									"The data will not be imported!");
							stl.setNotificalLabelDelayStandard("Data not imported: a DMU Name was empty!");
							return true;
						}
						dmuNames.add(row.getCell(0).getStringCellValue());



						//Data
						double[] tempArr2 = new double[row.getPhysicalNumberOfCells() - 1];
						ArrayList<String> tempArrl = new ArrayList<String>();
						tempArrl.add(row.getCell(0).getStringCellValue());
						for(int c = 1; c < row.getPhysicalNumberOfCells(); c++) {
							tempArr2[c - 1] = row.getCell(c).getNumericCellValue();
						}

						dataMatrix.add(tempArr2);

					}


				}
			}
			
			return false;
		}
		catch (Exception e) {
			//something went wrong obviously...
			return true;
		}


		
	}
	
	
	
	/**
	 * Checks data for CSV files and put it in dataMatrix if OK)
	 * @param fileName The name of the file to read data from
	 * @return boolean 'true' if there was a problem, 'false' if there was no problem and data could be imported without any issue.
	 */
	private boolean readDataCSV(String fileName) {
		String str = "";
		try {
			try {
				reader = new CSVReader(new FileReader(fileName));

				//First line = headers
				nextLine = reader.readNext();
				if (nextLine != null) {
					for(int i = 1; i < nextLine.length; i++) {
						if(str.equals(nextLine[i])){
							MessageDialog.openWarning(this.getShell(), "Warning", "The variable name after variable '" + variableNames.get(variableNames.size() - 1) +
									"' is empty.\n\n" +
									"The data will not be imported!");
							stl.setNotificalLabelDelayStandard("Data not imported: a Variable Name was empty!");
							return true;
						}
						variableNames.add(nextLine[i]);
					}
				}
				else {
					//something wrong file empty => set return false & leave
					return false;
				}

				while ((nextLine = reader.readNext()) != null) {

					/* Could write some code to catch exception individually (to give the user some feedback as to
					 * what failed if the import fails.*/

					//dmuName
					if(str.equals(nextLine[0])){
						MessageDialog.openWarning(this.getShell(), "Warning", "A DMU Name is empty.\n\n" +
								"The data will not be imported!");
						stl.setNotificalLabelDelayStandard("Data not imported: a DMU Name was empty!");
						return true;
					}
					dmuNames.add(nextLine[0]);
					
					

					//Data
					double[] tempArr2 = new double[nextLine.length - 1];
					ArrayList<String> tempArrl = new ArrayList<String>();
					tempArrl.add(nextLine[0]);
					for(int i = 1; i < nextLine.length; i++) {
						tempArr2[i - 1] = Double.parseDouble(nextLine[i]);
					}

					dataMatrix.add(tempArr2);

				}
			} catch (FileNotFoundException e) {
//				e.printStackTrace();
				MessageDialog.openWarning(this.getShell(), "Warning", "The file could not be found.\n\n" +
						"The data will not be imported!");
				stl.setNotificalLabelDelayStandard("Data not imported. File Not Found!");
				return true;
			}
			catch (IOException ioe) {
//				ioe.printStackTrace();
				MessageDialog.openWarning(this.getShell(), "Warning", "An IO Exception occured.\n\n" +
						"The data will not be imported!");
				stl.setNotificalLabelDelayStandard("Data not imported. IO Exception!");
				return true;
			}			

		}
		catch (Exception e) {
			MessageDialog.openWarning(this.getShell(), "Warning", "An error occured during the import. Check the data file thoroughly:\n" +
					instructions + "\n\n" +
					"The data will not be imported!");
			stl.setNotificalLabelDelayStandard("Problem Importing Data. Data not imported!");
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	
	public boolean performCancel()
	{
		stl.setNotificalLabelDelayStandard("Import Cancelled!");
		return true;
	}
	



}
