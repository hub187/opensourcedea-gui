package org.opensourcedea.gui.maingui;

import java.util.ArrayList;
import java.util.Collections;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.opensourcedea.dea.EfficiencyType;
import org.opensourcedea.dea.ModelOrientation;
import org.opensourcedea.dea.ModelType;
import org.opensourcedea.dea.ReturnsToScale;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.ldeaproblem.LDEAProblem;

public class ModelDetailsComposite extends Composite {


	final String allOr = "ALL ORIENTATIONS";
	final String inputOr = "INPUT ORIENTED";
	final String outputOr = "OUTPUT ORIENTED";
	final String nonOr = "NON ORIENTED";

	final String allRts = "ALL RTS";
	final String constRts = "CONSTANT RTS";
	final String varRts = "VARIABLE RTS";
	final String genRts = "GENERAL RTS";
	final String incRts = "INCREASING RTS";
	final String decRts = "DECREASING RTS";

	final String allEff = "ALL EFFICIENCIES";
	final String techEff = "TECHNICAL EFFICIENCY";
	final String mixEff = "MIX EFFICIENCY";

	final OSDEA_StatusLine stl;

	private ComboViewer orientationCombo;
	private ComboViewer efficiencyCombo;
	private ComboViewer rtsCombo;
	private Spinner rtsLBSpinner;
	private Spinner rtsUBSpinner;
	private Label rtsUBLabelText;
	private LDEAProblem ldeap;
	private Button resetButton;

	private ComboViewer modTypesCombo;
	
	
	private final ScrolledComposite sComp;
	private final Composite comp;
	private final Group descGroup;
	private final Text description;
	private ModelDetailsParamGroup paramGroup;
	private DataBindingContext bindingContext;
	

	public ModelDetailsComposite(Composite parentComp, final LDEAProblem parentLdeap, OSDEA_StatusLine stl) {
		super(parentComp, 0);

		this.stl = stl;
		this.ldeap = parentLdeap;
		
		sComp = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);
		comp = new Composite(sComp, SWT.NONE);
		descGroup = new Group(comp, SWT.NONE);
		description = new Text(descGroup, SWT.WRAP | SWT.MULTI);
		bindingContext = null;
		final ArrayList<String> orList = new ArrayList<String>();
		Collections.addAll(orList, new String[]{allOr, inputOr, outputOr, nonOr}); 
		final ArrayList<String> rtsList = new ArrayList<String>();
		Collections.addAll(rtsList, new String[]{allRts, constRts, varRts, genRts, incRts, decRts});
		final ArrayList<String> effList = new ArrayList<String>();
		Collections.addAll(effList, new String[]{allEff, techEff, mixEff});
		paramGroup = null;
		
		
		Realm.runWithDefault(SWTObservables.getRealm(parentComp.getDisplay()), new Runnable() { 
			public void run() {
				bindingContext = new DataBindingContext();
				paramGroup = new ModelDetailsParamGroup(comp, bindingContext, ldeap, modTypesCombo, descGroup,
						orList,  rtsList, effList);
				
				createControls();
			}
		});

	}


	private void createControls() {

		this.setLayout(new GridLayout());

		
		sComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sComp.setLayout(new FormLayout());

		
		FormData fdata = new FormData();
		fdata.left = new FormAttachment(0);
		fdata.right = new FormAttachment(100);
		fdata.top = new FormAttachment(0);
		fdata.bottom = new FormAttachment(100);
		comp.setLayoutData(fdata);
		comp.setLayout(new FormLayout());

		String helpText = "You can select the DEA model you need from the main ComboBox.\n" +
				"You can use the filters to narrow down the models.";
		Images.setHelpIcon(comp, helpText, 10, 20);




		



		Label rawDataLabel = new Label(comp, SWT.NONE);
		rawDataLabel.setText("Please chose a Model Type:");
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.left = new FormAttachment(0, 20);
		rawDataLabel.setLayoutData(fdata);



		//DESCRIPTION GROUP
		
		descGroup.setText("Model Description");
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 180);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.left = new FormAttachment(0, 20);
		fdata.right = new FormAttachment(100, -20);
		descGroup.setLayoutData(fdata);
		descGroup.setLayout(new FormLayout());

		
		Color tr = new Color(comp.getDisplay(), 240, 240, 240);
		description.setBackground(tr);
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 10);
		fdata.bottom = new FormAttachment(100, -10);
		fdata.left = new FormAttachment(0, 10);
		fdata.right = new FormAttachment(100, -10);
		description.setLayoutData(fdata);



		//MAIN MODEL TYPE COMBO
		modTypesCombo = new ComboViewer(comp, SWT.READ_ONLY);
		fdata = new FormData();
		fdata.top = new FormAttachment(0, 35);
		fdata.left = new FormAttachment(0, 20);
		modTypesCombo.getCombo().setLayoutData(fdata);


		final IObservableValue widgetObservable = ViewersObservables.observeSingleSelection(modTypesCombo);
		bindingContext.bindValue(widgetObservable, PojoObservables.observeValue(ldeap.getModelDetails(), "modelType"));
		widgetObservable.addChangeListener(new IChangeListener() {

			@Override
			public void handleChange(ChangeEvent arg0) {
				if(widgetObservable.getValue() !=null) {
					stl.setNotificalLabelDelayStandard("Model Type set to: " + widgetObservable.getValue());
					ldeap.setModified(true);
				}
				else {
					stl.setNotificalLabelDelayStandard("Model Type Reseted");
				}

			}

		});


		




		modTypesCombo.setContentProvider(new ObservableListContentProvider());

		modTypesCombo.setInput(paramGroup.getFilteredList());


		//get ref of buttons and combos from Param Group
		orientationCombo = paramGroup.getOrientationCombo();
		efficiencyCombo = paramGroup.getEfficiencyCombo();
		rtsCombo = paramGroup.getRtsCombo();
		rtsLBSpinner = paramGroup.getRtsLBSpinner();
		rtsUBSpinner = paramGroup.getRtsUBSpinner();
		rtsUBLabelText = paramGroup.getRtsUBLabelText();
		resetButton = paramGroup.getResetButton();






		//LISTENERS
		modTypesCombo.addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{	
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if(!selection.isEmpty()) {

					ModelType selectedType = (ModelType)selection.getFirstElement();
					modTypeComboSelectionChanged(selectedType, description, paramGroup, descGroup);
				}
				else {
					description.setText("");
				}



			}
		});
		
		


		sComp.setContent(comp);
		sComp.setExpandVertical(true);
		sComp.setExpandHorizontal(true);

		Point prefSize = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		prefSize.x = prefSize.x + 10;
		prefSize.y = prefSize.y + 50;
		sComp.setMinSize(prefSize);



		if(ldeap.getModelType() != null) {
			String modType = ldeap.getModelType().toString();
			int i = 0;
			for(ModelType tempMod : ModelType.values()) {
				if(modType.equals(tempMod.toString())){
					modTypesCombo.getCombo().select(i);
					this.getDisplay().syncExec(new Runnable() {
						public void run() {
							modTypeComboSelectionChanged(ldeap.getModelType(), description, paramGroup, descGroup);
						}
					});

					break;
				}
				i++;
			}
		}


	}
	
	
	public void resetModDetailsComposite() {
		modTypesCombo.getCombo().deselectAll();
		paramGroup.resetFilters();
		
	}


	public void updateStatusStl() {
		if(ldeap.getModelType() != null) {
			stl.setStatusLabel("The Model Type is configured.");
		}
		else {
			stl.setStatusLabel("You need to select a Model Type.");
		}
	}

	public void setWidgetsEnabled(boolean enabled) {
		modTypesCombo.getCombo().setEnabled(enabled);
		orientationCombo.getCombo().setEnabled(enabled);
		efficiencyCombo.getCombo().setEnabled(enabled);
		rtsCombo.getCombo().setEnabled(enabled);
		rtsUBSpinner.setEnabled(enabled);
		rtsLBSpinner.setEnabled(enabled);
		resetButton.setEnabled(enabled);
		
	}


	private void modTypeComboSelectionChanged(ModelType selectedType, Text description, ModelDetailsParamGroup paramGroup, Group descGroup) {

		description.setText(selectedType.getDescription());


		if(selectedType.getOrientation() == ModelOrientation.INPUT_ORIENTED){
			orientationCombo.getCombo().select(1);
		}
		else if(selectedType.getOrientation() == ModelOrientation.OUTPUT_ORIENTED){
			orientationCombo.getCombo().select(2);
		}
		else if(selectedType.getOrientation() == ModelOrientation.NON_ORIENTED){
			orientationCombo.getCombo().select(3);
		}


		if(selectedType.getEfficiencyType() == EfficiencyType.TECH){
			efficiencyCombo.getCombo().select(1);
		}
		else if(selectedType.getEfficiencyType() == EfficiencyType.MIX){
			efficiencyCombo.getCombo().select(2);
		}


		if(selectedType.getReturnToScale() == ReturnsToScale.CONSTANT) {
			rtsCombo.getCombo().select(1);
			ldeap.setRtsLowerBound(0);
			ldeap.setRtsUpperBound(1);
			rtsLBSpinner.setSelection(0);
			rtsUBSpinner.setSelection(100);
			paramGroup.disableSpinners();
			paramGroup.setSmallParamGroupSize(paramGroup.getParamGroup(), descGroup);
		}
		else if(selectedType.getReturnToScale() == ReturnsToScale.VARIABLE) {
			rtsCombo.getCombo().select(2);
			ldeap.setRtsLowerBound(0);
			ldeap.setRtsUpperBound(1);
			rtsLBSpinner.setSelection(0);
			rtsUBSpinner.setSelection(100);
			paramGroup.disableSpinners();
			paramGroup.setSmallParamGroupSize(paramGroup.getParamGroup(), descGroup);
		}
		else if(selectedType.getReturnToScale() == ReturnsToScale.GENERAL) {
			rtsCombo.getCombo().select(3);
			ldeap.setRtsLowerBound(0.8);
			ldeap.setRtsUpperBound(1.2);
			rtsLBSpinner.setSelection(80);
			rtsUBSpinner.setSelection(120);
			rtsLBSpinner.setEnabled(true);
			rtsUBSpinner.setEnabled(true);
			rtsUBSpinner.setVisible(true);
			rtsUBLabelText.setVisible(false);
			paramGroup.setBigParamGroupSize(paramGroup.getParamGroup(), descGroup);
		}
		else if(selectedType.getReturnToScale() == ReturnsToScale.INCREASING) {
			rtsCombo.getCombo().select(4);
			rtsLBSpinner.setSelection(100);
			rtsLBSpinner.setEnabled(false);
			rtsUBSpinner.setEnabled(false);
			rtsUBSpinner.setVisible(false);
			rtsUBLabelText.setVisible(true);
			paramGroup.setBigParamGroupSize(paramGroup.getParamGroup(), descGroup);
		}
		else if(selectedType.getReturnToScale() == ReturnsToScale.DECREASING) {
			rtsCombo.getCombo().select(5);
			ldeap.setRtsLowerBound(0);
			ldeap.setRtsUpperBound(1);
			rtsLBSpinner.setSelection(0);
			rtsUBSpinner.setSelection(100);
			paramGroup.disableSpinners();
			paramGroup.setBigParamGroupSize(paramGroup.getParamGroup(), descGroup);
		}


		updateStatusStl();



	}


}
