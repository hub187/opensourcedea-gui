package org.opensourcedea.gui.menu;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.opensourcedea.gui.maingui.Navigation;
import org.opensourcedea.gui.startgui.OSDEA_StatusLine;
import org.opensourcedea.gui.utils.Images;

public class OSDEA_CoolBar {
	
	private Image newImage, openImage, saveImage, importImage;
	private Shell shell;
	private Navigation nav;
	private OSDEA_StatusLine stl;
	private CoolBar coolBar;
	private ToolBar fileToolBar, toolsToolBar;
	private ToolItem newToolItem, openToolItem, saveToolItem, importToolItem;
	private CoolItem fileCoolItem, toolsCoolItem;
	private Point fileSizePoint, filePreferredPoint, toolsSizePoint, toolsPreferredPoint;
	private FormData coolData;
	private final ImageRegistry imgReg;
	
	
	public OSDEA_CoolBar(Shell parentShell, Navigation parentNavigation, OSDEA_StatusLine parentStl) {
		shell = parentShell;
		this.nav = parentNavigation;
		stl = parentStl;
		
		imgReg = Images.getFullImageRegistry(parentShell.getDisplay());
		newImage = imgReg.get("new");
		openImage = imgReg.get("open");
		saveImage = imgReg.get("save");
		importImage = imgReg.get("import");
		
	}
	
	public CoolBar getCoolBar() {
		
		coolBar = new CoolBar(shell, SWT.NONE);
		
		
		//File toolbar
		fileToolBar = new ToolBar(coolBar, SWT.FLAT);
		
		
		//New Item
		newToolItem = new ToolItem(fileToolBar, SWT.PUSH);
		newToolItem.setImage(newImage);
		newToolItem.setToolTipText("Create a new DEA Problem");
		newToolItem.addSelectionListener(new FileNewItemListener(nav, stl));
		
		//Open Item
		openToolItem = new ToolItem(fileToolBar, SWT.PUSH);
		openToolItem.setImage(openImage);
		openToolItem.addSelectionListener(new FileOpenItemListener(nav, stl));
		
		//Save Item
		saveToolItem = new ToolItem(fileToolBar, SWT.PUSH);
		saveToolItem.setImage(saveImage);
		saveToolItem.addSelectionListener(new FileSaveItemListener(nav, stl));
			
		
	    fileToolBar.pack();
	    fileSizePoint = fileToolBar.getSize();
	    fileCoolItem = new CoolItem(coolBar, SWT.NONE);
	    fileCoolItem.setControl(fileToolBar);
	    filePreferredPoint = fileCoolItem.computeSize(fileSizePoint.x, fileSizePoint.y);
	    fileCoolItem.setPreferredSize(filePreferredPoint);
		

	    
	    //Tools toolbar
	    toolsToolBar = new ToolBar(coolBar, SWT.FLAT);
	    
	    //New Item
	    importToolItem = new ToolItem(toolsToolBar, SWT.PUSH);
	    importToolItem.setImage(importImage);
	    importToolItem.addSelectionListener(new ToolImportItemListener(shell, nav, stl));	    
	    
	    
	    toolsToolBar.pack();
	    toolsSizePoint = toolsToolBar.getSize();
	    toolsCoolItem = new CoolItem(coolBar, SWT.NONE);
	    toolsCoolItem.setControl(toolsToolBar);
	    toolsPreferredPoint = toolsCoolItem.computeSize(toolsSizePoint.x, toolsSizePoint.y);
	    toolsCoolItem.setPreferredSize(toolsPreferredPoint);
	    
	    
	    
	    
		coolData = new FormData();
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top = new FormAttachment(0);
		coolBar.setLayoutData(coolData);
		
		coolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				shell.layout();
			}
		});
		
		
		coolBar.setLocked(true);
		
		return coolBar;
	}
	
}
