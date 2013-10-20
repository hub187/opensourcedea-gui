package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.ModelVariablesType;
import org.opensourcedea.dea.VariableOrientation;
import org.opensourcedea.dea.VariableType;
import org.opensourcedea.gui.exceptions.ModelTypeNotSetException;
import org.opensourcedea.gui.exceptions.NoVariableException;
import org.opensourcedea.gui.exceptions.UnselectedVariablesException;
import org.opensourcedea.gui.exceptions.UnvalidVariableChoiceException;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.Dimensions;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.ldeaproblem.LDEAProblem;
import org.opensourcedea.ldeaproblem.LVariable;


@SuppressWarnings("unused")
public class VariablesComposite extends Composite {
	
	
	private final Navigation nav;
	private LDEAProblem ldeap;
	private ArrayList<String> varl = new ArrayList<String>();
	private final List varList;
	private final Variables var;
	private final StandardInputs inputs;
	private final StandardOutputs outputs;
	private final NDInputs ndInputs;
	private final NDOutputs ndOutputs;
	private final NCInputs ncInputs;
	private final NCOutputs ncOutputs;
	final List inputList;
	final List outputList;
	final List ndInputList;
	final List ndOutputList;
	final List ncInputList;
	final List ncOutputList;
	private OSDEA_StatusLine stl;
	
	public VariablesComposite(Composite parentComp, Navigation nav, final LDEAProblem ldeap, OSDEA_StatusLine stl) {
		super(parentComp, 0);
		
		this.setLayout(new GridLayout());
		
		this.nav = nav;
		this.ldeap = ldeap;
		this.stl = stl;
		
		
		
		ScrolledComposite sComp = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		sComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sComp.setLayout(new FormLayout());
		
		
		Composite comp = new Composite(sComp, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.top = new FormAttachment(0);
		formData.bottom = new FormAttachment(100);
		comp.setLayoutData(formData);
		comp.setLayout(new FormLayout());
		
		
		String helpText = "You can only configure the variables after you've imported some data\n" +
				"(you can import data from Tools\\Import or from the toolbar icon).\n" +
				"Once you've imported some data, the variables will show in the main Variable list.\n" +
				"Select some variables and click on the arrow button to put them where you want.\n" +
				"All the variables will need to be either in a input or an output list.";
		Images.setHelpIcon(comp, helpText, 10, 20);
		
		
		//Creating widgets on the composite
		final Label varLabel = new Label(comp, SWT.NONE);
		varList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		//List
		int setWidth = 0;
		
		if(ldeap.getVariableNames() !=null) {
			Iterator<String> it = ldeap.getVariableNames().iterator();
			while(it.hasNext()) {
				varl.add(it.next());
			}
			setWidth = Dimensions.getMaxStringWidth(this, varl);// getVarListWidth(varl);
			setWidth = Math.max(setWidth, 150);
		}
		else {
			setWidth = 150;
		}
		
		
		
		Label inputLabel = new Label(comp, SWT.NONE);
		inputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Label outputLabel = new Label(comp, SWT.NONE);
		outputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Label ndInputLabel = new Label(comp, SWT.NONE);
		ndInputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Label ndOutputLabel = new Label(comp, SWT.NONE);
		ndOutputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Label ncInputLabel = new Label(comp, SWT.NONE);
		ncInputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Label ncOutputLabel = new Label(comp, SWT.NONE);
		ncOutputList = new List(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Button toButton = null;
		Button backButton = null;
		
		
		boolean openingFile = false;
		if(ldeap.getVariableNames() != null && ldeap.getVariableOrientation() != null
				&& ldeap.getVariableType() != null) {
			
			openingFile = true;
			for(int i = 0; i < ldeap.getVariableNames().size(); i++) {
				
				if(ldeap.getVariableOrientation().get(i) == VariableOrientation.INPUT) {
					switch (ldeap.getVariableType().get(i)) {
					case STANDARD:
						inputList.add(ldeap.getVariableNames().get(i));
						break;
					case NON_DISCRETIONARY:
						ndInputList.add(ldeap.getVariableNames().get(i));
						break;
					case NON_CONTROLLABLE:
						ncInputList.add(ldeap.getVariableNames().get(i));
						break;
					default:
						varList.add(ldeap.getVariableNames().get(i));
						break;
					}

				}
				else {
					switch (ldeap.getVariableType().get(i)) {
					case STANDARD:
						outputList.add(ldeap.getVariableNames().get(i));
						break;
					case NON_DISCRETIONARY:
						ndOutputList.add(ldeap.getVariableNames().get(i));
						break;
					case NON_CONTROLLABLE:
						ncOutputList.add(ldeap.getVariableNames().get(i));
						break;
					default:
						varList.add(ldeap.getVariableNames().get(i));
						break;
					}
				}
				
			}
		}
		
		
		//instantiating classes
		if(openingFile){
			var = new Variables(comp, varLabel, varList, setWidth);
		}
		else {
			var = new Variables(comp, varLabel, varList, varl, setWidth);
		}
		
		inputs = new StandardInputs(comp, inputLabel, inputList, var, toButton, backButton, setWidth, this);
		
		outputs = new StandardOutputs(comp, outputLabel, outputList, varList, toButton, backButton, inputList, setWidth, this);
		
		ndInputs = new NDInputs(comp, ndInputLabel, ndInputList, inputList, toButton, backButton, setWidth, this);
		
		ndOutputs = new NDOutputs(comp, ndOutputLabel, ndOutputList, outputList, toButton, backButton, ndInputList, setWidth, this);
		
		ncInputs = new NCInputs(comp, ncInputLabel, ncInputList, ndInputList, toButton, backButton, setWidth, this);
		
		ncOutputs = new NCOutputs(comp, ncOutputLabel, ncOutputList, ndOutputList, toButton, backButton, ncInputList, setWidth, this);
		
		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);
//		sComp.setMinSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		Point prefSize = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		prefSize.x = prefSize.x + 20;
//		prefSize.y = prefSize.y + 50;
		sComp.setMinSize(prefSize);

		
		final Button resetButton = new Button(comp, SWT.PUSH);
		resetButton.setText("Reset");
		formData = new FormData();
		formData.left = new FormAttachment(0, 20);
		formData.top = new FormAttachment(0, 5);
		resetButton.setLayoutData(formData);
		resetButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				if(ldeap.getVariableNames() != null) {
					cleanAllLists();
					Iterator<String> it = ldeap.getVariableNames().iterator();
					while(it.hasNext()) {
						varList.add(it.next());
					}
				}

				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
		});
		
		
	}
	
	
	public void setWidgetsEnabled(boolean enabled) {
		inputList.setEnabled(enabled);
		inputList.deselectAll();
		outputList.setEnabled(enabled);
		outputList.deselectAll();
		ndInputList.setEnabled(enabled);
		ndInputList.deselectAll();
		ndOutputList.setEnabled(enabled);
		ndOutputList.deselectAll();
		ncInputList.setEnabled(enabled);
		ncInputList.deselectAll();
		ncOutputList.setEnabled(enabled);
		ncOutputList.deselectAll();
	}
	
	public void cleanAllLists() {
		varList.removeAll();
		inputList.removeAll();
		outputList.removeAll();
		ndInputList.removeAll();
		ndOutputList.removeAll();
	}
	
	public void refreshVarList() {
		if(/*varList.getItems().length == 0 && */ldeap.getVariableNames() != null) {
			cleanAllLists();
			var.refreshVarList(ldeap.getVariableNames());
			updateStatusStl();
		}
	}
	
	
	
	
	//mmmmmmm, some nice boilerplate code! :/
	private void storeDataToModel() {
		
		
		ArrayList<VariableOrientation>  varOr = new ArrayList<VariableOrientation>();
		ArrayList<VariableType> varType = new ArrayList<VariableType>();

		
		if(ldeap.getLModelVariables().getVariableNames() == null){
			return;
		}
		
		Iterator<String> it = ldeap.getLModelVariables().getVariableNames().iterator();
		while(it.hasNext()) {
			String tempVar = it.next();
			
			for(String str : varList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(null);
					varType.add(null);
				}
			}
			
			for(String str : inputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.INPUT);
					varType.add(VariableType.STANDARD);
				}
			}
			
			for(String str : outputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.OUTPUT);
					varType.add(VariableType.STANDARD);
				}
			}
			
			for(String str : ndInputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.INPUT);
					varType.add(VariableType.NON_DISCRETIONARY);
				}
			}
			
			for(String str : ndOutputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.OUTPUT);
					varType.add(VariableType.NON_DISCRETIONARY);
				}
			}
			
			for(String str : ncInputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.INPUT);
					varType.add(VariableType.NON_CONTROLLABLE);
				}
			}
			
			for(String str : ncOutputList.getItems()) {
				if(str.equals(tempVar)) {
					varOr.add(VariableOrientation.OUTPUT);
					varType.add(VariableType.NON_CONTROLLABLE);
				}
			}
			
			
		}
		
		ldeap.setVariableOrientation(varOr);
		ldeap.setVariableType(varType);
		ldeap.setModified(true);
		
		updateStatusStl();
		
		
	}
	
	public void updateStatusStl() {
		boolean areAllAVarOK = true;
		
		switch (checkIfVarAreOK()) {
		case allVariablesCorrectlySet: stl.setStatusLabel("Variables correctly configured."); break;
		case NotAllVariablesSelected: stl.setStatusLabel("Variables correctly configured but some variables are not selected."); break;
		default: stl.setStatusLabel("Missing inputs or outputs."); break;
		}

	}
	
	
//	public void checkIfVarAreOK() throws NoVariableException, UnselectedVariablesException, UnvalidVariableChoiceException {
//		Integer nbVar = null;
//		if(ldeap.getNumberOfVariables() != null) {
//			nbVar = ldeap.getNumberOfVariables();
//		}
//		else {
//			throw new NoVariableException();
//		}
//		
//		if(varList.getItemCount() > 0) {
//			throw new UnselectedVariablesException();
//		}
//
//		if(nbVar != null) {
//			if(inputList.getItemCount() + ndInputList.getItemCount() + ncInputList.getItemCount() == 0 ||
//					outputList.getItemCount() + ndOutputList.getItemCount() + ncOutputList.getItemCount() == 0) {
//				throw new UnvalidVariableChoiceException();
//			}
//		}
//	}
	
	public VariablesStatusEnum checkIfVarAreOK()  {
		
		Integer nbVar = null;
		
		if(ldeap.getNumberOfVariables() != null) {
			nbVar = ldeap.getNumberOfVariables();
		}
		else {
			return VariablesStatusEnum.NoVariables;
		}
		
		if(nbVar != null) {
			if(inputList.getItemCount() + ndInputList.getItemCount() + ncInputList.getItemCount() == 0) {
				return VariablesStatusEnum.NoInputSelected;
			}
			if(outputList.getItemCount() + ndOutputList.getItemCount() + ncOutputList.getItemCount() == 0) {
				return VariablesStatusEnum.NoOutputSelected;
			}
		}
		
		if(varList.getItemCount() > 0) {
			return VariablesStatusEnum.NotAllVariablesSelected;
		}
				
		return VariablesStatusEnum.allVariablesCorrectlySet;


	}
	
	
	
	
	
	class Variables {
		
		List varList;
		
		public Variables(Composite parent, Label varLabel, List varList, ArrayList<String> varl, int setWidth) {
			
			this.varList = varList;
			
			FormData flData = new FormData();
			
//			inputLabel = new Label(parent, SWT.NONE);
			varLabel.setText("VARIABLES");
			flData.top = new FormAttachment(0, 40);
			flData.left = new FormAttachment(0, 20);
			varLabel.setLayoutData(flData);
			
			Iterator<String> it = varl.iterator();
			while(it.hasNext()) {
				varList.add(it.next());
			}
			
		
			FormData fData = new FormData();
			fData.top = new FormAttachment(varLabel, 10);
			fData.bottom = new FormAttachment(100, -10);
			fData.left = new FormAttachment(0, 20);
			fData.width = setWidth;
			varList.setLayoutData(fData);

		}

		public Variables(Composite parent, Label varLabel, List varList, int setWidth) {
			
			this.varList = varList;
			
			FormData flData = new FormData();
			
//			inputLabel = new Label(parent, SWT.NONE);
			varLabel.setText("VARIABLES");
			flData.top = new FormAttachment(0, 40);
			flData.left = new FormAttachment(0, 20);
			varLabel.setLayoutData(flData);
			
		
			FormData fData = new FormData();
			fData.top = new FormAttachment(varLabel, 10);
			fData.bottom = new FormAttachment(100, -10);
			fData.left = new FormAttachment(0, 20);
			fData.width = setWidth;
			varList.setLayoutData(fData);

		}
		
		
				
		public ArrayList<String> getSelectedItems() {
			ArrayList<String> arr = new ArrayList<String>();
			for(int i : varList.getSelectionIndices()) {
				arr.add(varList.getItem(i));
			}
			
			
			return arr;
		}
		
		public void removeSelectedItems() {
			while(varList.getSelectionIndices().length != 0) {
				varList.remove(varList.getSelectionIndices()[0]);
			}
		}
		
		public void addItems(ArrayList<String> arr) {
			if(arr !=null) {
				Iterator<String> it = arr.iterator();
				while(it.hasNext()) {
					varList.add(it.next());
				}
			}
		}
		
		public void refreshVarList(ArrayList<String> varl){
			varList.removeAll();
			Iterator<String> it = varl.iterator();
			while(it.hasNext()) {
				varList.add(it.next());
			}
		}
		
		public List getVarList() {
			return varList;
		}
		
		
		
	}
	
	
	class StandardInputs {

		
		
		public StandardInputs(Composite parent, Label inputLabel, final List inputList, final Variables var, Button toButton,
				Button backButton, int setWidth, final VariablesComposite varComp) {
			
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(25, 10);
			toButtonFD.left = new FormAttachment(var.getVarList(), 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(25, 40);
			backButtonFD.left = new FormAttachment(var.getVarList(), 10);
			backButton.setLayoutData(backButtonFD);
			
			
			FormData flData = new FormData();
			inputLabel = new Label(parent, SWT.NONE);
			inputLabel.setText("INPUTS");
			flData.top = new FormAttachment(0, 40);
			flData.left = new FormAttachment(toButton, 10);
			inputLabel.setLayoutData(flData);
			
			

			FormData fData = new FormData();
			fData.top = new FormAttachment(inputLabel, 10);
			fData.bottom = new FormAttachment(50, 10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			inputList.setLayoutData(fData);
			
			
			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {

					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						inputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {

					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : inputList.getSelectionIndices()) {
						arr.add(inputList.getItem(i));
					}
					var.addItems(arr);
					while(inputList.getSelectionIndices().length != 0) {
						inputList.remove(inputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
		}

		
	}
	
	
	class StandardOutputs {

		
		public StandardOutputs(Composite parent, Label outputLabel, final List outputList, List varList, Button toButton,
				Button backButton, List inputList, int setWidth, final VariablesComposite varComp) {
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(75, -10);
			toButtonFD.left = new FormAttachment(varList, 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(75, 20);
			backButtonFD.left = new FormAttachment(varList, 10);
			backButton.setLayoutData(backButtonFD);
			
			FormData flData = new FormData();
			outputLabel = new Label(parent, SWT.NONE);
			outputLabel.setText("OUTUTS");
			flData.top = new FormAttachment(inputList, 10);
			flData.left = new FormAttachment(toButton, 10);
			outputLabel.setLayoutData(flData);
			
			

			FormData fData = new FormData();
			fData.top = new FormAttachment(outputLabel, 10);
			fData.bottom = new FormAttachment(100, -10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			outputList.setLayoutData(fData);
			
			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						outputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : outputList.getSelectionIndices()) {
						arr.add(outputList.getItem(i));
					}
					var.addItems(arr);
					while(outputList.getSelectionIndices().length != 0) {
						outputList.remove(outputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
		}
		
	}
	
	
	class NDInputs {

		
		public NDInputs(Composite parent, Label ndInputLabel, final List ndInputList, List inputList, Button toButton,
				Button backButton, int setWidth, final VariablesComposite varComp) {
			
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(25, 10);
			toButtonFD.left = new FormAttachment(inputList, 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(25, 40);
			backButtonFD.left = new FormAttachment(inputList, 10);
			backButton.setLayoutData(backButtonFD);
			
			
			FormData flData = new FormData();
			ndInputLabel = new Label(parent, SWT.NONE);
			ndInputLabel.setText("NON-DISCRETIONARY INPUTS");
			ndInputLabel.setToolTipText("Non-discretionary inputs are input for which slacks are allowed\n" +
					"but do not count towards the overall DMU objective.");
			flData.top = new FormAttachment(0, 40);
			flData.left = new FormAttachment(toButton, 10);
			ndInputLabel.setLayoutData(flData);
			
		

			FormData fData = new FormData();
			fData.top = new FormAttachment(ndInputLabel, 10);
			fData.bottom = new FormAttachment(50, 10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			ndInputList.setLayoutData(fData);
			
			
			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						ndInputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : ndInputList.getSelectionIndices()) {
						arr.add(ndInputList.getItem(i));
					}
					var.addItems(arr);
					while(ndInputList.getSelectionIndices().length != 0) {
						ndInputList.remove(ndInputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
				}

				@Override
				public void mouseUp(MouseEvent e) {

					
				}
				
			});
			
			
			
		}

		
	}
	
	
	class NDOutputs {

		
		public NDOutputs(Composite parent, Label ndOutputLabel, final List ndOutputList, List outputList, Button toButton,
				Button backButton, List ndInputList, int setWidth, final VariablesComposite varComp) {
			
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(75, -10);
			toButtonFD.left = new FormAttachment(outputList, 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(75, 20);
			backButtonFD.left = new FormAttachment(outputList, 10);
			backButton.setLayoutData(backButtonFD);
			
			
			FormData flData = new FormData();
			ndOutputLabel = new Label(parent, SWT.NONE);
			ndOutputLabel.setText("NON-DISCRETIONARY OUTPUTS");
			ndOutputLabel.setToolTipText("Non-discretionary outputs are output for which slacks are allowed\n" +
					"but do not count towards the overall DMU objective.");
			flData.top = new FormAttachment(ndInputList, 10);
			flData.left = new FormAttachment(toButton, 10);
			ndOutputLabel.setLayoutData(flData);
			
			

			FormData fData = new FormData();
			fData.top = new FormAttachment(ndOutputLabel, 10);
			fData.bottom = new FormAttachment(100, -10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			ndOutputList.setLayoutData(fData);

			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						ndOutputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {

					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {

					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : ndOutputList.getSelectionIndices()) {
						arr.add(ndOutputList.getItem(i));
					}
					var.addItems(arr);
					while(ndOutputList.getSelectionIndices().length != 0) {
						ndOutputList.remove(ndOutputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
			
		}
		
	}
	
	
	class NCInputs {

		
		public NCInputs(Composite parent, Label ncInputLabel, final List ncInputList, List ndinputList, Button toButton,
				Button backButton, int setWidth, final VariablesComposite varComp) {
			
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(25, 10);
			toButtonFD.left = new FormAttachment(ndinputList, 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(25, 40);
			backButtonFD.left = new FormAttachment(ndinputList, 10);
			backButton.setLayoutData(backButtonFD);
			
			
			FormData flData = new FormData();
			ncInputLabel = new Label(parent, SWT.NONE);
			ncInputLabel.setText("NON-CONTROLLABLE INPUTS");
			ncInputLabel.setToolTipText("Non-controllable inputs are input for which slacks are NOT allowed.");
			flData.top = new FormAttachment(0, 40);
			flData.left = new FormAttachment(toButton, 10);
			ncInputLabel.setLayoutData(flData);
			
		

			FormData fData = new FormData();
			fData.top = new FormAttachment(ncInputLabel, 10);
			fData.bottom = new FormAttachment(50, 10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			ncInputList.setLayoutData(fData);
			
			
			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						ncInputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : ncInputList.getSelectionIndices()) {
						arr.add(ncInputList.getItem(i));
					}
					var.addItems(arr);
					while(ncInputList.getSelectionIndices().length != 0) {
						ncInputList.remove(ncInputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {

					
				}
				
			});
			
			
			
		}

		
	}
	
	
	class NCOutputs {

		
		public NCOutputs(Composite parent, Label ncOutputLabel, final List ncOutputList, List ndOutputList, Button toButton,
				Button backButton, List ncInputList, int setWidth, final VariablesComposite varComp) {
			
			
			toButton = new Button(parent, SWT.PUSH);
			toButton.setText(">>>");
			FormData toButtonFD = new FormData();
			toButtonFD.top = new FormAttachment(75, -10);
			toButtonFD.left = new FormAttachment(ndOutputList, 10);
			toButton.setLayoutData(toButtonFD);
			
			
			
			backButton = new Button(parent, SWT.PUSH);
			backButton.setText("<<<");
			FormData backButtonFD = new FormData();
			backButtonFD.top = new FormAttachment(75, 20);
			backButtonFD.left = new FormAttachment(ndOutputList, 10);
			backButton.setLayoutData(backButtonFD);
			
			
			FormData flData = new FormData();
			ncOutputLabel = new Label(parent, SWT.NONE);
			ncOutputLabel.setText("NON-CONTROLLABLE OUTPUTS");
			ncOutputLabel.setToolTipText("Non-controllable outputs are output for which slacks are NOT allowed.");
			flData.top = new FormAttachment(ncInputList, 10);
			flData.left = new FormAttachment(toButton, 10);
			ncOutputLabel.setLayoutData(flData);
			
			

			FormData fData = new FormData();
			fData.top = new FormAttachment(ncOutputLabel, 10);
			fData.bottom = new FormAttachment(100, -10);
			fData.left = new FormAttachment(toButton, 10);
			fData.width = setWidth;
			ncOutputList.setLayoutData(fData);

			
			toButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = var.getSelectedItems();
					Iterator<String> it = arr.iterator();
					while(it.hasNext()) {
						ncOutputList.add(it.next());
					}
					var.removeSelectedItems();
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {

					
				}
				
			});
			
			
			backButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {

					
				}

				@Override
				public void mouseDown(MouseEvent e) {
					ArrayList<String> arr = new ArrayList<String>();
					for(int i : ncOutputList.getSelectionIndices()) {
						arr.add(ncOutputList.getItem(i));
					}
					var.addItems(arr);
					while(ncOutputList.getSelectionIndices().length != 0) {
						ncOutputList.remove(ncOutputList.getSelectionIndices()[0]);
					}
					
					varComp.storeDataToModel();
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}
				
			});
			
			
			
		}
		
	}
		
	
	
}
