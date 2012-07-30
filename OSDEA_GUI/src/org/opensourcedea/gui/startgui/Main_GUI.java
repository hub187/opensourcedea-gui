package org.opensourcedea.gui.startgui;



import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.OSDEAMainComposite;
import org.opensourcedea.gui.menu.LDEAPSaver;
import org.opensourcedea.gui.menu.OSDEA_CoolBar;
import org.opensourcedea.gui.menu.OSDEA_Menu;
import org.opensourcedea.gui.utils.Images;
import org.opensourcedea.gui.utils.OS;
import org.opensourcedea.gui.utils.Sys;


public class Main_GUI {

  private Display display;
  private Shell shell;
  private CoolBar coolBar;
  private OSDEA_StatusLine stl;
  private FormData fdata;
  private DialogCrash dialogCrash;
  
  
  @SuppressWarnings("unused")
  public static void main(String[] args) {
	  
	  Main_GUI OSDEAGUI = new Main_GUI();
	  
  }
  
  public Main_GUI() {

    display = new Display();
    
    
    
    shell = new Shell(display);
    shell.setText("OSDEA");
    shell.setLayout(new FormLayout());
	shell.setSize(1000,550);
	ImageRegistry imgReg = Images.getMainGUIImageRegistry(display);
    shell.setImage(imgReg.get("potIcon"));
    
    MessageDialog.setDefaultImage(imgReg.get("potIcon"));

    
    stl = new OSDEA_StatusLine(shell);
    
    final OSDEAMainComposite mainComp = new OSDEAMainComposite(shell, stl, SWT.NONE);
    
    coolBar = new OSDEA_CoolBar(shell, mainComp.getNavigation(), stl).getCoolBar();

    shell.setMenuBar(new OSDEA_Menu(shell, stl, mainComp.getNavigation()).getMenu());
    
    
    //Layouts
    
    //stl
    fdata = new FormData();
    fdata.left = new FormAttachment(0);
    fdata.right = new FormAttachment(100);
    fdata.bottom = new FormAttachment(100);
    //to compensate for potential SWT linux bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=386271
    int height = Sys.getOS() == OS.WINDOWS ? 15 : 35;
    fdata.height = height;
    stl.getCompStatusLine().setLayoutData(fdata);
    
    //Main Comp
	fdata = new FormData();
	fdata.left = new FormAttachment(0);
	fdata.right = new FormAttachment(100);
	fdata.top = new FormAttachment(coolBar);
	fdata.bottom = new FormAttachment(stl.getCompStatusLine());
	mainComp.setLayoutData(fdata);
	
	//CoolBar
	fdata = new FormData();
	fdata.left = new FormAttachment(0);
	fdata.right = new FormAttachment(100);
	fdata.top = new FormAttachment(0);
	coolBar.setLayoutData(fdata);
	
	//Menu
	fdata = new FormData();
	fdata.left = new FormAttachment(0);
	fdata.right = new FormAttachment(100);
	fdata.top = new FormAttachment(0);
	coolBar.setLayoutData(fdata);
	
	
	stl.setNotificalLabelDelayStandard("OSDEA is ready!");
	
	
	shell.layout();
	
	shell.addShellListener(new ShellAdapter()
	{
		public void shellClosed(ShellEvent e)
		{
			LDEAPSaver saver = new LDEAPSaver(mainComp.getNavigation(), stl);
			if(!saver.checkBeforeClosing()) {
				e.doit = false;
			}
		}
	});
	

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
