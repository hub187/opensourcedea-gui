package org.opensourcedea.gui.startgui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opensourcedea.gui.utils.Images;

public class DialogCrash extends Dialog {
	  
	  private StyledText errorText;
	  private Image exclamationImage;
	  private Shell parent;
	  private Shell dialog;
	  private Label labelIcon;
	  private FormData formData;
	  private Text text;
	  private Display display;
	  
	  
	  DialogCrash(Shell parent) {
	    super(parent);
	  }
	  

	  public String open(String exStr) {
	    parent = getParent();
	    
	    exclamationImage = Images.getMainGUIImageRegistry(display).get("exclamation");
	    
	    dialog = new Shell(parent, SWT.DIALOG_TRIM
	        | SWT.APPLICATION_MODAL);
	    dialog.setSize(450, 500);
	    dialog.setText("Application Failure");
	    dialog.setImage(exclamationImage);
	    
	    dialog.setLayout(new FormLayout());
	    
	    labelIcon = new Label(dialog, SWT.NONE);
	    labelIcon.setImage(exclamationImage);
	    
	    formData = new FormData();
	    formData.left = new FormAttachment(2);
	    formData.top = new FormAttachment(5);
	    labelIcon.setLayoutData(formData);
	    
	    text = new Text(dialog, SWT.MULTI | SWT.WRAP);
	    text.setText("An exception has occured. OSDEA is still in BETA and does not unfortunately yet manages exception safely." +
	    		"This means that your work since your last save is lost." +
	    		"\n\nSo that this doesn't happen again, please report the bug trace below at opensourcedea@gmail.com so we can help improving OSDEA!");
	    text.setEditable(false);
	    formData = new FormData();
	    formData.left = new FormAttachment(labelIcon, 10);
	    formData.top = new FormAttachment(5);
	    formData.right = new FormAttachment(98);
	    text.setLayoutData(formData);
	    
	    errorText = new StyledText(dialog, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    errorText.setText(exStr);
	    
	    errorText.setEditable(false);
	    errorText.setFocus();
	    
	    formData = new FormData();
	    formData.left = new FormAttachment(labelIcon, 20);
	    formData.top = new FormAttachment(text, 30);
	    formData.right = new FormAttachment(98);
	    formData.bottom = new FormAttachment(95);
	    errorText.setLayoutData(formData);
	    
	    dialog.open();
	    display = parent.getDisplay();
	    while (!dialog.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    
	    
	    
	    exclamationImage.dispose();
	    display.dispose();
	    
	    return "After Dialog";
	  }

	  
}
