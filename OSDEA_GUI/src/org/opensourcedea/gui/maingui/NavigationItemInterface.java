package org.opensourcedea.gui.maingui;

import org.eclipse.swt.widgets.Tree;


public interface NavigationItemInterface {
	
	public void addItem(String itemName);
	
	public void addItem(String itemName, Object domainObject, String stlString, Tree tree);
	
	
	
}
