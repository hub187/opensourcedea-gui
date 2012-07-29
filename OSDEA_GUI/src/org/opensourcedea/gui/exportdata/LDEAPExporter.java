package org.opensourcedea.gui.exportdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class LDEAPExporter {

	private LDEAProblem ldeap;
	private Shell shell;
	private static final String[] filterNames = {
		"Excel 2007 File (*.xls)"
	};
	private static final String[] filterExts = {
		"*.xls"
	};


	public LDEAPExporter(LDEAProblem ldeap, Shell shell) {
		this.ldeap = ldeap;
		this.shell = shell;

	}


	public void exportToFile() throws IOException {

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExts);
		if(ldeap.getModelName() != ""){
			dialog.setFileName(ldeap.getModelName() + ".xls");
		}


		String fileName = null;

		boolean dontCare = false;
		while(!dontCare) {
			fileName = dialog.open();
			if (fileName == null) {
				break;
			}
			File f = new File(fileName);
			if(f.exists()){
				if(MessageDialog.openQuestion(shell, "Overwrite", "The file already exists. Do you want to overwrite it?") == 
						true) {
					dontCare = true;
				}
			}else{
				dontCare = true;
			}
		}


		if(fileName != null) {
			FileOutputStream out = new FileOutputStream(fileName);
			Workbook wb = new HSSFWorkbook();
			
			buildFile(wb);			

			wb.write(out);
			out.close();


		}
	}
	
	
	private void buildFile(Workbook wb) {
		
		//Sheet order MATTERS! (for the setSheetName methods)
		
		//Model Details
		exportModelDetails(wb);
		wb.setSheetName(0, "Model details");
		
		//Raw Data
		exportRawData(wb);
		wb.setSheetName(1, "Raw Data");
		
		//Variables
		exportVariables(wb);
		wb.setSheetName(2, "Variables");
		
		
		
	}
	

	private void exportModelDetails(Workbook wb) {
		Sheet modelDetailsSheet = wb.createSheet();
		
		Row row = null;
		Cell cell = null; 

		row = modelDetailsSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Model Name");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelName());
		
		row = modelDetailsSheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("Model Type");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelType().getName().toString());
		
		row = modelDetailsSheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue("Model Orientation");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelType().getOrientation().toString());
		
		row = modelDetailsSheet.createRow(3);
		cell = row.createCell(0);
		cell.setCellValue("Model Efficiency Type");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelType().getEfficiencyType().toString());
		
		row = modelDetailsSheet.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue("Model RTS");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelType().getReturnToScale().toString());
		
		row = modelDetailsSheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellValue("Model Description");
		cell = row.createCell(1);
		cell.setCellValue(ldeap.getModelType().getDescription());
		
		if(ldeap.getModelType().getReturnToScale() != ReturnsToScale.CONSTANT &&
				ldeap.getModelType().getReturnToScale() != ReturnsToScale.VARIABLE) {

			row = modelDetailsSheet.createRow(6);
			cell = row.createCell(0);
			cell.setCellValue("Return To Scale Lower Bound");
			cell = row.createCell(1);
			cell.setCellValue(Double.toString(ldeap.getModelDetails().getRtsLB()));
			
			row = modelDetailsSheet.createRow(7);
			cell = row.createCell(0);
			cell.setCellValue("Return To Scale Upper Bound");
			cell = row.createCell(1);
			cell.setCellValue(Double.toString(ldeap.getModelDetails().getRtsUB()));
			
		}

		

		
		
	}


	private void exportRawData(Workbook wb) {
		Sheet dataSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 

		row = dataSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Names");

		for(int i = 0; i < ldeap.getVariableNames().size(); i++) {
			cell = row.createCell(i + 1);
			cell.setCellValue(ldeap.getVariableNames().get(i));
		}

		for(int rowNum = 0; rowNum < ldeap.getDataMatrix().size(); rowNum++) {

			row = dataSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));

			for(int cellNum = 0; cellNum < ldeap.getDataMatrix().get(0).length; cellNum++) {
				cell = row.createCell(cellNum + 1);
				cell.setCellValue(ldeap.getDataMatrix().get(rowNum)[cellNum]);

			}
		}
	}


	private void exportVariables(Workbook wb) {
		Sheet varSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 
		
		row = varSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Variable Name");
		cell = row.createCell(1);
		cell.setCellValue("Variable Orientation");
		cell = row.createCell(1);
		cell.setCellValue("Variable Type");
		
		for(int rowNum = 0; rowNum < ldeap.getNumberOfVariables(); rowNum++) {
			row = varSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getVariableNames().get(rowNum));
			cell = row.createCell(1);
			cell.setCellValue(ldeap.getVariableOrientation().get(rowNum).toString());
			cell = row.createCell(2);
			cell.setCellValue(ldeap.getVariableType().get(rowNum).toString());
		}
		

	}





}

