package org.opensourcedea.gui.maingui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.opensourcedea.gui.parameters.OSDEAGUIParameters;


/**
 * Class managing the progress group of the DEAProblemComposite.
 * @author Hubert Virtos
 *
 */
public class SolvingProgress {
	private final ProgressBar progressBar;
	private final Label progressStatusLabel;
	private final Group progressGroup;
	
	public SolvingProgress(Composite comp, Group remActionsGroup, Button solveButton) {
		
		
		progressGroup = new Group(comp, SWT.NONE);
		progressGroup.setText("Solving Problem");
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 20);
		formData.right = new FormAttachment(100, -20);
		formData.top = new FormAttachment(solveButton, 20);
		progressGroup.setVisible(false);
		progressGroup.setLayoutData(formData);
		progressGroup.setLayout(new FormLayout());


		progressStatusLabel = new Label(progressGroup, SWT.NONE);
		progressStatusLabel.setText("");
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 10);
		formData.height = 20;
		progressStatusLabel.setVisible(true);
		progressStatusLabel.setLayoutData(formData);

		progressBar = new ProgressBar(progressGroup, SWT.BORDER);
		progressBar.setMaximum(OSDEAGUIParameters.getProgressBarMaximum());
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(progressStatusLabel, 5);
		formData.bottom = new FormAttachment(100, -15);
		formData.height = 20;
		progressBar.setVisible(true);
		progressBar.setLayoutData(formData);


	}
	
	
	public Group getProgressGroup() {
		return progressGroup;
	}
	
	
	public void setProgressBar(double percentage) {
		if (progressBar.isDisposed ()) {
			return;
		}
		else {
			progressBar.setSelection((int)(OSDEAGUIParameters.getProgressBarMaximum() * percentage));
		}
	}
	



	public void updateProgressLabelText(String str) {
		progressStatusLabel.setText(str);
		progressStatusLabel.pack();
	}
}
