package org.opensourcedea.gui.exportdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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


		if(ldeap.isSolved()) {
			//Objectives
			exportObjectives(wb);
			wb.setSheetName(3, "Objectives");

			//Projections
			exportProjections(wb);
			wb.setSheetName(4, "Projections");

			//Lambdas
			exportLambdas(wb);
			wb.setSheetName(5, "Lambdas");
			
			
			//Peer Group
			exportPeerGroup(wb);
			wb.setSheetName(6, "Peer Group");
			
			//Slacks
			exportSlacks(wb);
			wb.setSheetName(7, "Slacks");
			
			//Weights
			exportWeights(wb);
			wb.setSheetName(8, "Weights");
		}


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
		cell.setCellValue("DMU Name");

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
		cell = row.createCell(2);
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

	private void exportObjectives(Workbook wb) {
		Sheet objSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 

		row = objSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		cell = row.createCell(1);
		cell.setCellValue("Objective Value");
		cell = row.createCell(2);
		cell.setCellValue("Efficient");

		for(int rowNum = 0; rowNum < ldeap.getLdeapSolution().getObjectives().length; rowNum++) {
			row = objSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));
			cell = row.createCell(1);
			cell.setCellValue(ldeap.getLdeapSolution().getObjective(rowNum));
			cell = row.createCell(2);
			String eff = ldeap.getLdeapSolution().getEfficient()[rowNum] ? "Yes" : "";
			cell.setCellValue(eff);
		}
	}


	private void exportProjections(Workbook wb) {
		Sheet projSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 

		row = projSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		for(int cellNum = 0; cellNum < ldeap.getNumberOfVariables(); cellNum++) {
			cell = row.createCell(cellNum + 1);
			cell.setCellValue(ldeap.getVariableNames().get(cellNum));
		}

		for(int rowNum = 0; rowNum < ldeap.getLdeapSolution().getProjections().length; rowNum++) {
			row = projSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));
			for(int cellNum = 0; cellNum < ldeap.getNumberOfVariables(); cellNum++) {
				cell = row.createCell(cellNum + 1);
				cell.setCellValue(ldeap.getLdeapSolution().getProjections(rowNum)[cellNum]);
			}
		}	
	}


	private void exportLambdas(Workbook wb) {

		Sheet lambdaSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 
		row = lambdaSheet.createRow(0);
		
		ArrayList<Integer> efficientReferencedDMUs = ldeap.returnEfficientReferencedDMUs();
		
		int i = 1;
		
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		Iterator<Integer> it = efficientReferencedDMUs.iterator();
		while(it.hasNext()) {
			cell = row.createCell(i++);
			cell.setCellValue(ldeap.getDMUNames().get(it.next()));
		}
		
		ArrayList<ArrayList<Double>> data = ldeap.returnProcessedLambdas();
		for(int rowNum = 0; rowNum < data.size(); rowNum++) {
			row = lambdaSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));
			int l = 1;
			Iterator<Double> itd = data.get(rowNum).iterator();
			while(itd.hasNext()) {
				cell = row.createCell(l++);
				cell.setCellValue(itd.next());
			}
		}
	}

	
	private void exportPeerGroup(Workbook wb) {
		Sheet pgSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 
		row = pgSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		cell = row.createCell(1);
		cell.setCellValue("Peer Group");
		
		ArrayList<ArrayList<String>> peerGroup = ldeap.returnPeerGroup();
		
		for(int rowNum = 0; rowNum < peerGroup.size(); rowNum++) {
			row = pgSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(peerGroup.get(rowNum).get(0));
			cell = row.createCell(1);
			cell.setCellValue(peerGroup.get(rowNum).get(1));
		}
	}
	
	private void exportSlacks(Workbook wb) {
		Sheet slacksSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 
		row = slacksSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		for(int i = 0; i < ldeap.getNumberOfVariables(); i++) {
			cell = row.createCell(i + 1);
			cell.setCellValue(ldeap.getVariableNames().get(i));
		}
		
		for(int rowNum = 0; rowNum < ldeap.getLdeapSolution().getSlacks().length; rowNum++) {
			row = slacksSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));
			for(int i = 0; i < ldeap.getNumberOfVariables(); i++) {
				cell = row.createCell(i + 1);
				cell.setCellValue(ldeap.getLdeapSolution().getSlack(rowNum, i));
			}
		}
	}
	
	private void exportWeights(Workbook wb) {
		Sheet weightsSheet = wb.createSheet();

		Row row = null;
		Cell cell = null; 
		row = weightsSheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("DMU Name");
		for(int i = 0; i < ldeap.getNumberOfVariables(); i++) {
			cell = row.createCell(i + 1);
			cell.setCellValue(ldeap.getVariableNames().get(i));
		}
		
		for(int rowNum = 0; rowNum < ldeap.getLdeapSolution().getWeights().length; rowNum++) {
			row = weightsSheet.createRow(rowNum + 1);
			cell = row.createCell(0);
			cell.setCellValue(ldeap.getDMUNames().get(rowNum));
			for(int i = 0; i < ldeap.getNumberOfVariables(); i++) {
				cell = row.createCell(i + 1);
				cell.setCellValue(ldeap.getLdeapSolution().getWeight(rowNum, i));
			}
		}
	}


}

