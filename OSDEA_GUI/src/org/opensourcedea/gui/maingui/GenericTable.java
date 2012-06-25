package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class GenericTable {
	
	private TableViewer tableViewer;
	private Table table;
	private ArrayList<String> headers;
	private ArrayList<ArrayList<String>> data;
	private Composite parent;
	private Composite tableComposite;
	
	
	
	public GenericTable(Composite parent, ArrayList<String> headers, ArrayList<ArrayList<String>> data) {
		this.parent = parent;
		this.headers = headers;
		this.data = data;
		parent.setLayout(new FormLayout());
	}
	
	
	
	
	public Composite setTable() {
		
		
		//Cleaning existing controls if they are any
		if(parent.getChildren().length > 0) {
			for(Control cont : parent.getChildren()) {
				cont.dispose();
			}
		}

		
		//Create the composite
		tableComposite = new Composite(parent, SWT.NONE);
		FormData fdata = new FormData();
		fdata.bottom = new FormAttachment(100);
		fdata.top = new FormAttachment(0);
		fdata.left = new FormAttachment(0);
		fdata.right = new FormAttachment(100);
		tableComposite.setLayoutData(fdata);

		//Add TableColumnLayout
		TableColumnLayout layout = new TableColumnLayout();
		tableComposite.setLayout(layout);
		
		
		//Only useful if composite displayed on a Window
//		Window.setExceptionHandler(new IExceptionHandler() {
//			@Override
//			public void handleException(Throwable t) {
//					t.printStackTrace();
//			}
//		});

		
		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		
		
		Label tempL = new Label(tableComposite, SWT.NONE);
	
		TableViewerColumn tableViewerColumn;
		TableColumn tblColumn;
		Iterator<String> it = headers.iterator();
		while(it.hasNext()) {
			
			tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			tblColumn = tableViewerColumn.getColumn();

			String headerText = it.next();
			tempL.setText(headerText);
			GC gc = new GC(tempL);
			Point size = gc.textExtent(headerText);
			gc.dispose ();
			
			layout.setColumnData(tblColumn, new ColumnWeightData(1, size.x + 20, true));
			tblColumn.setText(headerText);
			
		}
		
		tempL.dispose();
		
		
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(new ContentProvider());
		
		//this method directly set the input as a Set<ArrayList<String>>.
		tableViewer.setInput(data);

		return tableComposite;

		
	}
	

	
	private static class ContentProvider implements IStructuredContentProvider {
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {

			ArrayList<ArrayList<String>> set = (ArrayList<ArrayList<String>>) inputElement;
			return set.toArray();
			
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	
	
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		@SuppressWarnings("unchecked")
		public String getColumnText(Object element, int columnIndex) {
			ArrayList<String> arr = (ArrayList<String>) element;
			String result = "";
			result = arr.get(columnIndex);
			return result;
		}
	}
	
	
	

	
}
