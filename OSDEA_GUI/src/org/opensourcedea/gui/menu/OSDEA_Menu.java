package org.opensourcedea.gui.menu;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.Images;


public class OSDEA_Menu {
	
	  private Menu menuBar, fileMenu, toolMenu, helpMenu;
	  private MenuItem fileMenuHeader, toolMenuHeader, helpMenuHeader;
	  private MenuItem fileNewItem, fileOpenItem, fileSaveItem, fileSaveAsItem, fileSaveAllItem, fileCloseItem, fileExitItem, toolImportItem,
	  toolExportItem, helpGetHelpItem, helpAboutItem;
	  private Image newImage, openImage, saveImage, saveAllImage, exitImage, importImage, exportImage, helpImage, closeImage;
	  private Shell shell;
	  private Navigation nav;
	  private OSDEA_StatusLine stl;
	  private ImageRegistry imgReg;
	
	
	public OSDEA_Menu(Shell parentShell, OSDEA_StatusLine parentStl, Navigation parentNavigation) {
		shell = parentShell;
		this.nav = parentNavigation;
		stl = parentStl;

		
	}
	
	
	public Menu getMenu() {
		
		imgReg = Images.getFullImageRegistry(shell.getDisplay());
		newImage = imgReg.get("new");
		openImage = imgReg.get("open");
		saveImage = imgReg.get("save");
		exitImage = imgReg.get("exit");
		importImage = imgReg.get("import");
		exportImage = imgReg.get("export");
		helpImage = imgReg.get("help");
		saveAllImage = imgReg.get("saveAll");
		closeImage = imgReg.get("close");
		
		menuBar = new Menu(shell, SWT.BAR);
		
	    menuBar = new Menu(shell, SWT.BAR);
	    
	    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);
	    
	    fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileNewItem.setText("&New\tCtrl+N");
	    fileNewItem.setAccelerator(SWT.CTRL + 'N');
	    fileNewItem.setImage(newImage);
	    
	    fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileOpenItem.setText("&Open\tCtrl+O");
	    fileOpenItem.setAccelerator(SWT.CTRL + 'O');
	    fileOpenItem.setImage(openImage);
	    
	    fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveItem.setText("&Save\tCtrl+S");
	    fileSaveItem.setAccelerator(SWT.CTRL + 'S');
	    fileSaveItem.setImage(saveImage);
	    
	    fileSaveAsItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveAsItem.setText("Save As");
	    fileSaveAsItem.setImage(saveImage);
	    
	    fileSaveAllItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveAllItem.setText("&Save All\tCtrl+Shift+S");
	    fileSaveItem.setAccelerator(SWT.CTRL + SWT.SHIFT + 'S');
	    fileSaveAllItem.setImage(saveAllImage);
	    
	    fileCloseItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileCloseItem.setText("&Close\tCtrl+C");
	    fileCloseItem.setAccelerator(SWT.CTRL + 'C');
	    fileCloseItem.setImage(closeImage);
	    
	    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("&Exit\tAlt+F4");
	    fileExitItem.setAccelerator(SWT.ALT + SWT.F4);
	    fileExitItem.setImage(exitImage);
	    
	    
	    toolMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    toolMenuHeader.setText("&Tools");
	    
	    toolMenu = new Menu(shell, SWT.DROP_DOWN);
	    toolMenuHeader.setMenu(toolMenu);
	    
	    toolImportItem = new MenuItem(toolMenu, SWT.PUSH);
	    toolImportItem.setText("Import Data");
	    toolImportItem.setImage(importImage);
	    
	    toolExportItem = new MenuItem(toolMenu, SWT.PUSH);
	    toolExportItem.setText("Export Data");
	    toolExportItem.setImage(exportImage);
	    
	    
	    
	    helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    helpMenuHeader.setText("&Help");

	    helpMenu = new Menu(shell, SWT.DROP_DOWN);
	    helpMenuHeader.setMenu(helpMenu);

	    helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpGetHelpItem.setText("&Get Help");
	    helpGetHelpItem.setImage(helpImage);
	    
	    helpAboutItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpAboutItem.setText("&About");
	    
	    
	    
	    fileNewItem.addSelectionListener(new FileNewItemListener(nav, stl));
	    fileOpenItem.addSelectionListener(new FileOpenItemListener(nav, stl));
	    fileSaveItem.addSelectionListener(new FileSaveItemListener(nav, stl));
	    fileSaveAsItem.addSelectionListener(new FileSaveAsItemListener(nav, stl));
	    fileSaveAllItem.addSelectionListener(new FileSaveAllItemListener(nav, stl));
	    fileCloseItem.addSelectionListener(new FileCloseItemListener(nav, stl));
	    fileExitItem.addSelectionListener(new FileExitItemListener(nav, stl));
	    toolImportItem.addSelectionListener(new ToolImportItemListener(shell, nav, stl));
	    toolExportItem.addSelectionListener(new ToolExportItemListener());
	    helpGetHelpItem.addSelectionListener(new HelpGetHelpItemListener(stl));
	    helpAboutItem.addSelectionListener(new HelpAboutItemListener(shell));


	    return menuBar;
	    
	}

	
}
