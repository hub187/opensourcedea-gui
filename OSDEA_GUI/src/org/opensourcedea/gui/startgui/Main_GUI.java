package org.opensourcedea.gui.startgui;



import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.OSDEAMainComposite;
import org.opensourcedea.gui.menu.OSDEA_CoolBar;
import org.opensourcedea.gui.menu.OSDEA_Menu;
import org.opensourcedea.gui.utils.Images;


public class Main_GUI {

  private Display display;
  private Shell shell;
  private CoolBar coolBar;
  private Composite statusLine;
  private OSDEA_StatusLine stl;
  private FormData formData;
  private DialogCrash dialogCrash;
  
  
  @SuppressWarnings("unused")
  public static void main(String[] args) {
	  
	  Main_GUI OSDEAGUI = new Main_GUI();
	  
  }
  
  public Main_GUI() {

    display = new Display();
    
    shell = new Shell(display);
    shell.setText("OSDEA");
    shell.setLayout(new FillLayout());
	shell.setSize(1000,550);
	ImageRegistry imgReg = Images.getMainGUIImageRegistry(display);
    shell.setImage(imgReg.get("potIcon"));
    
    MessageDialog.setDefaultImage(imgReg.get("potIcon"));

    
    stl = new OSDEA_StatusLine(shell);
    statusLine = stl.getCompStatusLine();
    
    OSDEAMainComposite mainComp = new OSDEAMainComposite(shell, stl, SWT.BORDER);
    
    coolBar = new OSDEA_CoolBar(shell, mainComp.getNavigation(), stl).getCoolBar();
    
    
    
    shell.setMenuBar(new OSDEA_Menu(shell, stl, mainComp.getNavigation()).getMenu());
    
    
	formData = new FormData();
	formData.left = new FormAttachment(0);
	formData.right = new FormAttachment(100);
	formData.top = new FormAttachment(coolBar);
	formData.bottom = new FormAttachment(statusLine);
	mainComp.setLayoutData(formData);
	
	
	stl.setNotificalLabelDelayStandard("OSDEA is ready!");
	

	try {
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	catch (Exception e) {
		e.printStackTrace();
		dialogCrash = new DialogCrash(shell);
		StackTraceElement[] est = e.getStackTrace();
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(e.toString());
		strBuff.append("\n");
		for(int i = 0; i < est.length; i++) {
			strBuff.append(est[i].toString());
			strBuff.append("\n");
		}
		dialogCrash.open(strBuff.toString());

	}



	
    
    display.dispose();
    

  }
  

  
}
