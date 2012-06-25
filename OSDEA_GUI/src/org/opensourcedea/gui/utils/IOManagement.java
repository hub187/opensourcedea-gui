package org.opensourcedea.gui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class IOManagement {
	
	/**
	 * Open a file dialog box and returns the filepath. Might be null. 
	 * @param shell
	 * @param filterNames
	 * @param filterExts
	 * @return
	 */
	public static String getFilePath(Shell shell, String[] filterNames, String[] filterExts) {


		String fileName = "";

		FileDialog dlg = new FileDialog(shell, SWT.OPEN);
		dlg.setFilterNames(filterNames);
		dlg.setFilterExtensions(filterExts);

		try {
			fileName = dlg.open();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		return fileName;
	}
	
	
	/**
	 * Checks of the file is of the given extension type.
	 * @param fileFullPath
	 * @param fileExtensionType
	 * @return boolean
	 */
	public static boolean isFileOfType(String fileFullPath, String fileExtensionType) {
		int i = fileFullPath.lastIndexOf('.');
		if (i > 0 && i < fileFullPath.length() - 1)
			if (fileFullPath.substring(i + 1).toLowerCase().equals(fileExtensionType))
				return true;
		return false;
	}
	
	
	/**
	 * Returns the file extension WITHOUT the dot '.'
	 * @param fileName
	 * @return the file extension
	 */
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		return fileName.substring(i + 1);
	}
	
	
	/**
	 * Gets a fullFilePath (has the filename + ext at the end) and replaces the filename in the full path with
	 * the new given filename. The given fileName should not have an extension.
	 * @param fullFilePath
	 * @param newFileName
	 * @return String of the new fullFilePath with the updated fileName.
	 */
	public static String getNewFileName(String fullFilePath, String newFileName) {

		//MIGHT ONLY WORK ON WINDOWS!!!!
		int i = fullFilePath.lastIndexOf("\\");
		String simplePath = fullFilePath.substring(0, i + 1);
		String newFullFilePath = simplePath + newFileName + "." + getExtension(fullFilePath);
		return newFullFilePath;

	}
	
	
	/**
	 * Returns the file name (with or without its extension) from a absolute system path name (i.e. the path AND the filename).
	 * @param fullFilePath
	 * @param withExtension
	 * @return the name of the file.
	 */
	public static String getFileName(String fullFilePath, boolean withExtension) {
		int i = fullFilePath.lastIndexOf("\\");
		int j = fullFilePath.lastIndexOf(".");

		if(withExtension) {
			return fullFilePath.substring(i + 1);
		}
		if(j > i) {
			return fullFilePath.substring(i + 1, j);
		}
		return fullFilePath.substring(i + 1);
	}
	
	
	public static String deleteInvalidCharacters(String strToTest) {
		String[] invalidCharacters = new String[] {"\\", "/", ":", "*", "?", "<", ">", "|"};
		
		for(String ch : invalidCharacters) {
			while(strToTest.indexOf(ch) > 0) {
				int pos = strToTest.indexOf(ch);
				strToTest = strToTest.substring(0, pos) +
						strToTest.substring(pos + 1, strToTest.length());
			}
		}
		
		return strToTest;
	}
	

}
