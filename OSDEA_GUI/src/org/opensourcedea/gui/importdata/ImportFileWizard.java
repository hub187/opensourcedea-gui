package org.opensourcedea.gui.importdata;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
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
	private final String instructions = "- The file needs to be in a .csv format.\n" +
			"- The first row of the file must contain the headers.\n" +
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
		
			
		String str = "";
		
		try {
			try {
				reader = new CSVReader(new FileReader(dirPage.getFileName()));

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

					/* Need to write some code to catch exception individually (to give the user some feedback as to
					 * what failed if the import fails.*/

					//dmuName
					if(str.equals(nextLine[0])){
						MessageDialog.openWarning(this.getShell(), "Warning", "The DMU Name after DMU '" + dmuNames.get(dmuNames.size() - 1) +
								"' is empty.\n\n" +
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
		
		navigation.importData(dataMatrix, dmuNames, variableNames);

		return true;
	}
	
	
	public boolean performCancel()
	{
		stl.setNotificalLabelDelayStandard("Import Cancelled!");
		return true;
	}
	



}
