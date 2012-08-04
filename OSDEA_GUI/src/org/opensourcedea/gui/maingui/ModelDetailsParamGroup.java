package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.list.ComputedList;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.opensourcedea.dea.EfficiencyType;
import org.opensourcedea.dea.ModelOrientation;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.gui.utils.Dimensions;
import org.opensourcedea.ldeaproblem.LDEAProblem;



public class ModelDetailsParamGroup {

	private final Composite comp;
	private final Button resetButton;
	private final Group paramGroup;
	private final ComboViewer orientationCombo;
	private final ComboViewer efficiencyCombo;
	private final ComboViewer rtsCombo;
	private final Spinner rtsLBSpinner;
	private final Spinner rtsUBSpinner;
	private final Label rtsUBLabelText;
	private final Group locDescGroup;
	private final ComboViewer locModTypesCombo;
	private final Label rtsLBLabel;
	private final Label rtsUBLabel;
	private final IObservableValue orientationObservable;
	private final IObservableValue efficiencyObservable;
	private final IObservableValue rtsObservable;

	final String allOr;
	final String inputOr;
	final String outputOr;
	final String nonOr;

	final String allRts;
	final String constRts;
	final String varRts;
	final String genRts;
	final String incRts;
	final String decRts;

	final String allEff;
	final String techEff;
	final String mixEff;

	private DataBindingContext bindingContext;
	private IObservableList filteredList;
	private LDEAProblem parentLdeap;

	public ModelDetailsParamGroup(Composite comp, DataBindingContext bindingContext, LDEAProblem parentLdeap, ComboViewer modTypeCombo,
			Group descGroup, ArrayList<String> orList, ArrayList<String> rtsList, ArrayList<String> effList) {
		this.comp = comp;
		this.bindingContext = bindingContext;
		this.parentLdeap = parentLdeap;
		this.locModTypesCombo = modTypeCombo;
		this.locDescGroup = descGroup;


		allOr = orList.get(0);
		inputOr = orList.get(1);
		outputOr = orList.get(2);
		nonOr = orList.get(3);

		allRts = rtsList.get(0);
		constRts = rtsList.get(1);
		varRts = rtsList.get(2);
		genRts = rtsList.get(3);
		incRts = rtsList.get(4);
		decRts = rtsList.get(5);

		allEff = effList.get(0);
		techEff = effList.get(1);
		mixEff = effList.get(2);



		paramGroup = new Group(comp, SWT.NONE);
		paramGroup.setText("Model Characteristics");
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 70);
		fdata.height = 80;
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -20);
		paramGroup.setLayoutData(fdata);

		resetButton = new Button(paramGroup, SWT.PUSH);
		resetButton.setText("Reset Filters");
		resetButton.setLocation(10, 25);
		resetButton.pack();


		ArrayList<String> orientationList = new ArrayList<String>();
		orientationList.add(allOr);
		orientationList.add(inputOr);
		orientationList.add(outputOr);
		orientationList.add(nonOr);

		ArrayList<String> efficiencyList = new ArrayList<String>();
		efficiencyList.add(allEff);
		efficiencyList.add(techEff);
		efficiencyList.add(mixEff);

		//Combo boxes all have the same width based on the longest String across all of them
		int orWidth = Dimensions.getMaxStringWidth(paramGroup, orientationList);
		int effWidth = Dimensions.getMaxStringWidth(paramGroup, efficiencyList);
		int rtsWidth = Dimensions.getMaxStringWidth(paramGroup, rtsList);
		int width = Math.max(orWidth, effWidth);
		width = Math.max(width, rtsWidth) + 30;

		orientationCombo = new ComboViewer(paramGroup, SWT.READ_ONLY);
		orientationCombo.setContentProvider(ArrayContentProvider.getInstance());


		orientationCombo.setInput(orientationList);
		orientationCombo.getCombo().setBounds(10, 60, width, 20);
		orientationCombo.getCombo().select(0);


		efficiencyCombo = new ComboViewer(paramGroup, SWT.READ_ONLY);
		efficiencyCombo.setContentProvider(ArrayContentProvider.getInstance());

		efficiencyCombo.setInput(efficiencyList);
		int effComboX = 30 + width;
		efficiencyCombo.getCombo().setBounds(effComboX /*160*/, 60, 160, 20);
		efficiencyCombo.getCombo().select(0);


		rtsCombo = new ComboViewer(paramGroup, SWT.READ_ONLY);
		rtsCombo.setContentProvider(ArrayContentProvider.getInstance());
		rtsCombo.setInput(rtsList);
		int rtsComboX = 10 + (20 + width) * 2;
		rtsCombo.getCombo().setBounds(rtsComboX, 60, 160, 20);
		rtsCombo.getCombo().select(0);

		rtsLBLabel = new Label(paramGroup, SWT.NONE);
		rtsLBLabel.setLocation(10, 105);
		rtsLBLabel.setText("RTS Lower Bound Value:");
		rtsLBLabel.pack();

		rtsLBSpinner = new Spinner (paramGroup, SWT.BORDER);
		rtsLBSpinner.setMinimum(0);
		rtsLBSpinner.setMaximum(10000);
		rtsLBSpinner.setDigits(2);
		rtsLBSpinner.setSelection(0);
		rtsLBSpinner.setIncrement(1);
		rtsLBSpinner.setPageIncrement(100);
		rtsLBSpinner.setLocation(150, 100);
		rtsLBSpinner.pack();

		rtsUBLabel = new Label(paramGroup, SWT.NONE);
		rtsUBLabel.setLocation(250, 105);
		rtsUBLabel.setText("RTS Upper Bound Value:");
		rtsUBLabel.pack();

		rtsUBSpinner = new Spinner (paramGroup, SWT.BORDER);
		rtsUBSpinner.setMinimum(100);
		rtsUBSpinner.setMaximum(100000);
		rtsUBSpinner.setDigits(2);
		rtsUBSpinner.setSelection(100);
		rtsUBSpinner.setIncrement(1);
		rtsUBSpinner.setPageIncrement(100);
		rtsUBSpinner.setLocation(390, 100);
		rtsUBSpinner.pack();


		rtsUBLabelText = new Label(paramGroup, SWT.NONE);
		rtsUBLabelText.setBounds(390, 105, 85, 20);
		rtsUBLabelText.setText("INFINITY (1E30)");



		bindRtsLBSpinner(rtsLBSpinner);
		bindRtsUBSpinner(rtsUBSpinner);


		addResetButtonListener(resetButton);


		orientationObservable = SWTObservables.observeSelection(orientationCombo.getCombo());
		efficiencyObservable = SWTObservables.observeSelection(efficiencyCombo.getCombo());
		rtsObservable = SWTObservables.observeSelection(rtsCombo.getCombo());

		computeModTypesList();

		addRtsComboListener(rtsCombo);




	}




	public IObservableList getFilteredList() {
		return filteredList;
	}

	public ComboViewer getOrientationCombo() {
		return orientationCombo;
	}

	public ComboViewer getEfficiencyCombo() {
		return efficiencyCombo;
	}

	public ComboViewer getRtsCombo() {
		return rtsCombo;
	}

	public Spinner getRtsLBSpinner() {
		return rtsLBSpinner;
	}

	public Spinner getRtsUBSpinner() {
		return rtsUBSpinner;
	}

	public Label getRtsUBLabelText() {
		return rtsUBLabelText;
	}

	public Button getResetButton() {
		return resetButton;
	}

	public Group getParamGroup() {
		return paramGroup;
	}

	public void disableSpinners() {
		rtsLBSpinner.setEnabled(false);
		rtsUBSpinner.setEnabled(false);
		rtsLBSpinner.setVisible(true);
		rtsUBSpinner.setVisible(true);
		rtsUBLabelText.setVisible(false);
	}

	public void setSmallParamGroupSize(Group paramGroup, Group descGroup) {


		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 70);
		fdata.height = 80;
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -10);
		paramGroup.setLayoutData(fdata);

		fdata = new FormData();
		fdata.top = new FormAttachment(0, 180);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -10);
		descGroup.setLayoutData(fdata);
		descGroup.setLayout(new FormLayout());

		paramGroup.getDisplay().asyncExec(new Runnable() {
			public void run() {
				comp.layout();
			}
		});

	}

	public void setBigParamGroupSize(Group paramGroup, Group descGroup) {

		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, 70);
		fdata.height = 120;
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -10);
		paramGroup.setLayoutData(fdata);

		fdata = new FormData();
		fdata.top = new FormAttachment(0, 210);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -10);
		descGroup.setLayoutData(fdata);
		descGroup.setLayout(new FormLayout());

		comp.layout();

	}



	private void bindRtsLBSpinner(Spinner rtsLBSpinner) {


		IConverter rtsLBconverterT2M = new IConverter() {

			@Override
			public Object convert(Object arg0) {
				Integer i = new Integer(0);
				i = (Integer) arg0;
				int got = i;
				double ret = (double)got / 100;
				return ret;
			}

			@Override
			public Object getFromType() {
				return int.class;
			}

			@Override
			public Object getToType() {
				return double.class;
			}

		};

		UpdateValueStrategy rtsLBstrategyT2M = new UpdateValueStrategy();
		rtsLBstrategyT2M. setConverter(rtsLBconverterT2M);

		IConverter rtsLBconverterM2T = new IConverter() {

			@Override
			public Object convert(Object arg0) {
				Integer i = new Integer(0);
				i = (Integer) arg0;
				return (int)i;
			}

			@Override
			public Object getFromType() {
				return double.class;
			}

			@Override
			public Object getToType() {
				return int.class;
			}

		};

		UpdateValueStrategy rtsLBstrategyM2T = new UpdateValueStrategy();
		rtsLBstrategyM2T. setConverter(rtsLBconverterM2T);


		bindingContext.bindValue(SWTObservables.observeSelection(rtsLBSpinner),
				BeansObservables.observeValue(parentLdeap.getModelDetails(), "rtsLB"),
				rtsLBstrategyT2M, rtsLBstrategyM2T);



	}

	private void bindRtsUBSpinner(Spinner rtsUBSpinner) {
		IConverter rtsUBconverterT2M = new IConverter() {

			@Override
			public Object convert(Object arg0) {
				Integer i = new Integer(0);
				i = (Integer) arg0;
				int got = i;
				double ret = (double)got / 100;
				return ret;
			}

			@Override
			public Object getFromType() {
				return int.class;
			}

			@Override
			public Object getToType() {
				return double.class;
			}

		};

		UpdateValueStrategy rtsUBstrategyT2M = new UpdateValueStrategy();
		rtsUBstrategyT2M. setConverter(rtsUBconverterT2M);

		IConverter rtsUBconverterM2T = new IConverter() {

			@Override
			public Object convert(Object arg0) {
				Integer i = new Integer(0);
				i = (Integer) arg0;
				return (int)i;
			}

			@Override
			public Object getFromType() {
				return double.class;
			}

			@Override
			public Object getToType() {
				return int.class;
			}

		};

		UpdateValueStrategy rtsUBstrategyM2T = new UpdateValueStrategy();
		rtsUBstrategyM2T. setConverter(rtsUBconverterM2T);

		bindingContext.bindValue(SWTObservables.observeSelection(rtsUBSpinner),
				BeansObservables.observeValue(parentLdeap.getModelDetails(), "rtsUB"),
				rtsUBstrategyT2M, rtsUBstrategyM2T);
	}


	private void computeModTypesList() {
		filteredList = new ComputedList() {
			@SuppressWarnings({ "rawtypes" })
			protected List calculate() {
				ArrayList result = new ArrayList();

				for(ModelType modType : ModelType.values()) {

					if(orientationObservable.getValue() != null){
						if(new String(inputOr).equals(orientationObservable.getValue()) &&
								modType.getOrientation() == ModelOrientation.INPUT_ORIENTED) {
							checkEfficiencies(modType, result);
						}
						else if(new String(outputOr).equals(orientationObservable.getValue()) &&
								modType.getOrientation() == ModelOrientation.OUTPUT_ORIENTED) {
							checkEfficiencies(modType, result);
						}
						else if(new String(nonOr).equals(orientationObservable.getValue()) &&
								modType.getOrientation() == ModelOrientation.NON_ORIENTED) {
							checkEfficiencies(modType, result);
						}
						else if(new String("").equals(orientationObservable.getValue())) {
							checkEfficiencies(modType, result);
						}
						else if (new String(allOr).equals(orientationObservable.getValue())) {
							checkEfficiencies(modType, result);
						}
					}

				}

				return result;
			}

			@SuppressWarnings({ "rawtypes" })
			private void checkEfficiencies(ModelType modType, ArrayList result) {
				if(efficiencyObservable.getValue() != null){
					if(new String(techEff).equals(efficiencyObservable.getValue()) &&
							modType.getEfficiencyType() == EfficiencyType.TECH) {
						checkRTS(modType, result);
					}
					else if(new String(mixEff).equals(efficiencyObservable.getValue()) &&
							modType.getEfficiencyType() == EfficiencyType.MIX) {
						checkRTS(modType, result);
					}
					else if(new String(allEff).equals(efficiencyObservable.getValue())) {
						checkRTS(modType, result);
					}
				}
			}


			@SuppressWarnings({ "unchecked", "rawtypes" })
			private void checkRTS(ModelType modType, ArrayList result) {
				if(rtsObservable.getValue() != null){
					if(new String(constRts).equals(rtsObservable.getValue()) &&
							modType.getReturnToScale() == ReturnsToScale.CONSTANT) {
						result.add(modType);
					}
					else if(new String(varRts).equals(rtsObservable.getValue()) &&
							modType.getReturnToScale() == ReturnsToScale.VARIABLE) {
						result.add(modType);
					}
					else if(new String(genRts).equals(rtsObservable.getValue()) &&
							modType.getReturnToScale() == ReturnsToScale.GENERAL) {
						result.add(modType);
					}
					else if(new String(incRts).equals(rtsObservable.getValue()) &&
							modType.getReturnToScale() == ReturnsToScale.INCREASING) {
						result.add(modType);
					}
					else if(new String(decRts).equals(rtsObservable.getValue()) &&
							modType.getReturnToScale() == ReturnsToScale.DECREASING) {
						result.add(modType);
					}
					else if(new String(allRts).equals(rtsObservable.getValue())) {
						result.add(modType);
					}
				}
			}


		};
	}


	private void addResetButtonListener(Button resetButton) {
		resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				resetFilters();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				resetFilters();

			}
		});
	}
	
	public void resetFilters() {
		orientationCombo.getCombo().select(0);
		efficiencyCombo.getCombo().select(0);
		rtsCombo.getCombo().select(0);
	}


	private void addRtsComboListener(ComboViewer rtsCombo) {
		rtsCombo.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if(!selection.isEmpty()) {
					String selectedRTS = (String)selection.getFirstElement();

					if(new String(constRts).equals(selectedRTS) ||
							new String(varRts).equals(selectedRTS)) {
						disableSpinners();
						setSmallParamGroupSize(paramGroup, locDescGroup);
					}
					else if(new String(allRts).equals(selectedRTS)) {
						IStructuredSelection selectedModType = (IStructuredSelection) locModTypesCombo.getSelection();
						if(selectedModType.isEmpty()){
							disableSpinners();
							setSmallParamGroupSize(paramGroup, locDescGroup);
						}
						else {
							ModelType modType = (ModelType)selectedModType.getFirstElement();

							if(modType.getReturnToScale() == ReturnsToScale.GENERAL) {
								rtsLBSpinner.setEnabled(true);
								rtsUBSpinner.setEnabled(true);
								setBigParamGroupSize(paramGroup, locDescGroup);
							}
							else if (modType.getReturnToScale() == ReturnsToScale.INCREASING) {
								disableSpinners();
								setBigParamGroupSize(paramGroup, locDescGroup);
							}
							else if(modType.getReturnToScale() == ReturnsToScale.DECREASING) {
								disableSpinners();
								setBigParamGroupSize(paramGroup, locDescGroup);
							}

						}
					}
					else if(new String(decRts).equals(selectedRTS)){
						rtsLBSpinner.setSelection(0);
						rtsUBSpinner.setSelection(100);
						disableSpinners();
						setBigParamGroupSize(paramGroup, locDescGroup);
					}
					else if(new String(incRts).equals(selectedRTS)){
						rtsLBSpinner.setSelection(100);
						rtsLBSpinner.setEnabled(false);
						rtsUBSpinner.setEnabled(false);
						rtsUBSpinner.setVisible(false);
						rtsUBLabelText.setVisible(true);
						setBigParamGroupSize(paramGroup, locDescGroup);
					}
					else if(new String(genRts).equals(selectedRTS)){
						rtsLBSpinner.setSelection(80);
						rtsUBSpinner.setSelection(120);
						rtsLBSpinner.setEnabled(true);
						rtsUBSpinner.setEnabled(true);
						rtsUBSpinner.setVisible(true);
						rtsUBLabelText.setVisible(false);
						setBigParamGroupSize(paramGroup, locDescGroup);
					}


				}


			}

		});
	}





}

