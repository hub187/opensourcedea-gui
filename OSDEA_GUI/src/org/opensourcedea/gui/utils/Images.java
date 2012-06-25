package org.opensourcedea.gui.utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;


public class Images {
	
	public static ImageRegistry getHelpImageRegistry(Display display) {
		ImageRegistry imgReg = new ImageRegistry(display);
		imgReg.put("help", ImageDescriptor.createFromFile(Images.class, "../images/help.png"));
		return imgReg;
	}
	
	/**
	 * Set the help icon with some tooltip text. This method can ONLY be used on a composite which has a FormLayout.
	 * @param comp the composite
	 * @param tooltipText the tooltip text to use.
	 */
	public static void setHelpIcon(Composite comp, String tooltipText, int offsetTop, int offsetRight) {
		Canvas helpCanvas = new Canvas(comp, SWT.NONE);
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0, offsetTop);
		fdata.right = new FormAttachment(100, -offsetRight);
		fdata.height = 16;
		fdata.width = 16;
		helpCanvas.setLayoutData(fdata);
		Images.paintCanvas(helpCanvas, "help", getHelpImageRegistry(helpCanvas.getDisplay()));
		helpCanvas.setToolTipText(tooltipText);
	}
	
	public static ImageRegistry getFullImageRegistry(Display display) {
		ImageRegistry imgReg = new ImageRegistry(display);
		
		imgReg.put("new", ImageDescriptor.createFromFile(Images.class, "../images/page_white.png"));
		imgReg.put("help", ImageDescriptor.createFromFile(Images.class, "../images/help.png"));
		imgReg.put("accept", ImageDescriptor.createFromFile(Images.class, "../images/accept.png"));
		imgReg.put("error", ImageDescriptor.createFromFile(Images.class, "../images/error.png"));
		imgReg.put("deaProblem", ImageDescriptor.createFromFile(Images.class, "../images/folder_page.png"));
		imgReg.put("rawData", ImageDescriptor.createFromFile(Images.class, "../images/table.png"));
		imgReg.put("variables", ImageDescriptor.createFromFile(Images.class, "../images/variable.png"));
		imgReg.put("modelDetails", ImageDescriptor.createFromFile(Images.class, "../images/widgets.png"));
		imgReg.put("solution", ImageDescriptor.createFromFile(Images.class, "../images/folder_star.png"));
		imgReg.put("objective", ImageDescriptor.createFromFile(Images.class, "../images/investment_menu_quality.png"));
		imgReg.put("projections", ImageDescriptor.createFromFile(Images.class, "../images/edit_path.png"));
		imgReg.put("lambdas", ImageDescriptor.createFromFile(Images.class, "../images/lambda-small.png"));
		imgReg.put("referenceSet", ImageDescriptor.createFromFile(Images.class, "../images/group.png"));
		imgReg.put("slacks", ImageDescriptor.createFromFile(Images.class, "../images/go-last-2.png"));
		imgReg.put("weights", ImageDescriptor.createFromFile(Images.class, "../images/balance.png"));
		imgReg.put("importWizard", ImageDescriptor.createFromFile(Images.class, "../images/document-import-2.png"));
		imgReg.put("exclamation", ImageDescriptor.createFromFile(Images.class, "../images/exclamation.png"));
		imgReg.put("open", ImageDescriptor.createFromFile(Images.class, "../images/folder.png"));
		imgReg.put("save", ImageDescriptor.createFromFile(Images.class, "../images/disk.png"));
		imgReg.put("saveAll",  ImageDescriptor.createFromFile(Images.class, "../images/disk_multiple.png"));
		imgReg.put("exit", ImageDescriptor.createFromFile(Images.class, "../images/cross.png"));
		imgReg.put("import", ImageDescriptor.createFromFile(Images.class, "../images/page_white_put.png"));
		imgReg.put("export", ImageDescriptor.createFromFile(Images.class, "../images/page_white_get.png"));

		
		return imgReg;
	}
	
	public static ImageRegistry getMainGUIImageRegistry(Display display) {
		ImageRegistry imgReg = new ImageRegistry(display);
		imgReg.put("potIcon", ImageDescriptor.createFromFile(Images.class, "../images/pot2422-48x48x32.png"));
//		imgReg.put("exclamation", ImageDescriptor.createFromFile(Images.class, "../images/exclamation.png"));
		return imgReg;
	}
	
	
	
	public static void paintCanvas(final Canvas canvas, final String resource) {
		paintCanvas(canvas, resource, getFullImageRegistry(canvas.getDisplay()));
	}
	
	
	private static void paintCanvas(final Canvas canvas, final String resource, final ImageRegistry imgReg) {

		canvas.addPaintListener (new PaintListener () {
			public void paintControl (PaintEvent e) {
				//http://www.eclipse.org/forums/index.php/mv/tree/141414/#page_top Ryan
				GC gc2 = new GC(canvas);
				gc2.setBackground(canvas.getBackground());
				gc2.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
				gc2.dispose();	
				e.gc.drawImage (imgReg.get(resource), 0, 0);

			}
		});
		
		canvas.redraw();
	}
	
	
	@SuppressWarnings("unused")
	private static Image scaleImage(Display display, Image originalImage) {

		double factor = 1d/1;

		final int width = originalImage.getBounds().width; 
		final int height = originalImage.getBounds().height; 

		Image scaledImage = new Image(display, originalImage.getImageData().scaledTo((int)(width * factor),(int)(height * factor)));

		return scaledImage;
	}
	
}
