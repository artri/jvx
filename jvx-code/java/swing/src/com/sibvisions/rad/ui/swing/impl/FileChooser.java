/*
 * Copyright 2012 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 03.04.2012 - [JR] - creation
 * 20.12.2012 - [JR] - selectDirectory implemented
 * 02.08.2014 - [JR] - save last selected dir and file in any case (finally)
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.rad.application.IFileHandleReceiver;
import javax.rad.application.genui.UILauncher;
import javax.rad.io.DirectoryHandle;
import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sibvisions.util.type.FileUtil;

/**
 * The <code>FileChooser</code> encapsulates the access to AWT or Swing File chooser. It stores
 * the last used directory/file and reuses the last selection for a new selection.
 * 
 * @author René Jahn
 */
public class FileChooser
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the last selected directory for the "open" filechooser. */
	private File fiLastSelectedDir = null;
	
	/** the last selected directory for the "save" filechooser. */
	private File fiLastSavedDir = null;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Shows a "Save as" dialog. If the parameter <code>FileChooser.native</code> is <code>true</code>, an AWT {@link FileDialog}
	 * will be used, otherwise a {@link JFileChooser}.
	 * 
	 * @param pLauncher the UI launcher
	 * @param pFrame the parent frame
	 * @param pFileHandle the file handle
	 * @param pTitle the dialog title
	 * @throws IOException if file access is not possible
	 * @throws SecurityException if a security manager denies the access
	 */
	public void saveAs(final UILauncher pLauncher, Frame pFrame, IFileHandle pFileHandle, String pTitle) throws IOException
	{
		if (Boolean.valueOf(pLauncher.getParameter("FileChooser.native")).booleanValue())
		{
    		FileDialog fdlg = new FileDialog(pFrame);
    		fdlg.setTitle(pLauncher.translate(pTitle == null ? "Save as..." : pTitle));
    		fdlg.setMode(FileDialog.SAVE);
    		
    		if (fiLastSavedDir == null && fdlg.getFile() != null)
    		{
    			fiLastSavedDir = new File(fdlg.getFile());
    		}
    		
    		if (fiLastSavedDir != null)
    		{
    			if (!fiLastSavedDir.isDirectory())
    			{
    				fiLastSavedDir = fiLastSavedDir.getParentFile();
    			}

    			fdlg.setDirectory(fiLastSavedDir.getAbsolutePath());
    		}
    		
    		fdlg.setFile(pFileHandle.getFileName());

    		try
    		{
    			Method mImage = fdlg.getClass().getMethod("setIconImage", Image.class);

    			mImage.invoke(fdlg, ((Frame)pFrame).getIconImage());
    		}
    		catch (Exception e)
    		{
    			//nothing to be done
    		}
    		
    		fdlg.setVisible(true);

    		String sCurrent = fdlg.getFile();

    		if (sCurrent != null)
    		{
    			File file = new File(fdlg.getDirectory(), sCurrent);
    			
    			if (file.isDirectory())
    			{
    				fiLastSavedDir = file;
    			}
    			else
    			{
    				fiLastSavedDir = file.getParentFile();
    			}

    			FileUtil.copy(pFileHandle.getInputStream(), true, new FileOutputStream(file), true);
    		}
		}
		else
		{
	    	JFileChooser fileChooser = new JFileChooser()
	    	{
	    		public void approveSelection() 
	    		{
	                File f = getSelectedFile();
	                
	                if (f.exists() && getDialogType() == SAVE_DIALOG) 
	                {
	                    int result = JOptionPane.showOptionDialog(this, 
	                    		                                  "<html>" + pLauncher.translate(f.getName() + " already exists.") + 
	                    		                                    "<br>" + pLauncher.translate("Replace the file?") + "</html>", 
	                    		                                  pLauncher.translate("Confirmation"), 
	                    		                                  JOptionPane.YES_NO_OPTION, 
	                    		                                  JOptionPane.WARNING_MESSAGE, 
	                    		                                  null, 
	                    		                                  null, 
	                    		                                  null); 
	                    
	                    switch (result) 
	                    {
		                    case JOptionPane.YES_OPTION:
		                        super.approveSelection();
		                        return;
		                    //case JOptionPane.CANCEL_OPTION:
		                    //    cancelSelection();
		                    //    return;
		                    default:
		                        return;
	                    }
	                }
	                
	                super.approveSelection();
	            }
	    	};
	
	    	fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			fileChooser.setDialogTitle(pLauncher.translate(pTitle == null ? "Save as..." : pTitle));
			if (fiLastSavedDir == null)
			{
				fiLastSavedDir = fileChooser.getCurrentDirectory();
			}
			
			fileChooser.setSelectedFile(new File(fiLastSavedDir, pFileHandle.getFileName()));
	
	    	int result = fileChooser.showSaveDialog(pFrame);
	    	
			if (result == JFileChooser.APPROVE_OPTION)
			{
		    	File file = fileChooser.getSelectedFile();
		    	
	    		fiLastSavedDir = file.getParentFile();

		    	FileUtil.copy(pFileHandle.getInputStream(), true, new FileOutputStream(file), true);
			}
		}
	}
	
	/**
	 * Shows an "open" dialog. If the parameter <code>FileChooser.native</code> is <code>true</code>, an AWT {@link FileDialog}
	 * will be used, otherwise a {@link JFileChooser}.
	 * 
	 * @param pLauncher the UI launcher
	 * @param pFrame the parent frame
	 * @param pTitle the dialog title
	 * @return the opened file handles.
	 * @throws IOException if file access fails
	 */
	public IFileHandle[] open(UILauncher pLauncher, Frame pFrame, String pTitle) throws IOException
	{
		if (Boolean.valueOf(pLauncher.getParameter("FileChooser.native")).booleanValue())
		{
			FileDialog fdlg = new FileDialog(pFrame);
			fdlg.setTitle(pLauncher.translate(pTitle == null ? "Open file" : pTitle));
			fdlg.setMode(FileDialog.LOAD);
			
    		if (fiLastSelectedDir == null && fdlg.getFile() != null)
    		{
    			fiLastSelectedDir = new File(fdlg.getFile());
    		}
  		
    		if (fiLastSelectedDir != null)
    		{
    			if (!fiLastSelectedDir.isDirectory())
    			{
    				fdlg.setDirectory(fiLastSelectedDir.getParent());
    			}
    		}
    		
    		try
    		{
    			Method mImage = fdlg.getClass().getMethod("setIconImage", Image.class);

    			mImage.invoke(fdlg, ((Frame)pFrame).getIconImage());
    		}
    		catch (Exception e)
    		{
    			//nothing to be done
    		}
    		
    		fdlg.setVisible(true);
    		
    		File[] files = fdlg.getFiles();
    		
			if (files != null && files.length > 0)
			{
				File fiLast = files[files.length - 1];
				
				if (fiLast.isDirectory())
				{
					fiLastSelectedDir = fiLast;
				}
				else
				{
					fiLastSelectedDir = fiLast.getParentFile();
				}
				
				IFileHandle[] fileHandles = new IFileHandle[files.length];
				
				for (int i = 0; i < files.length; i++)
				{
					fileHandles[i] = new FileHandle(files[i]);
				}
				
				return fileHandles;
			}
			else
			{
	    		String sCurrent = fdlg.getFile();
	
	    		if (sCurrent != null)
	    		{
	    			File file = new File(fdlg.getDirectory(), sCurrent);
	    			
	    			fiLastSelectedDir = file.getParentFile();
	    			
	    			if (file.isDirectory())
	    			{
	    				fiLastSelectedDir = file;
	    			}
	    			else
	    			{
		    			fiLastSelectedDir = file.getParentFile();
	    			}
	    			
					return new FileHandle[] {new FileHandle(file)};
	    		}
			}
		}
		else
		{
	    	JFileChooser fileChooser = new JFileChooser();
	    	fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			
			fileChooser.setDialogTitle(pLauncher.translate(pTitle == null ? "Open file" : pTitle));
	    	fileChooser.setMultiSelectionEnabled(true);
	    	fileChooser.setCurrentDirectory(fiLastSelectedDir);
	    	
	    	int result = fileChooser.showOpenDialog(pFrame);
	    	
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File[] files = fileChooser.getSelectedFiles();
				
				if (files != null && files.length > 0)
				{
					File fiLast = files[files.length - 1];
					
					if (fiLast.isDirectory())
					{
						fiLastSelectedDir = fiLast;
					}
					else
					{
						fiLastSelectedDir = fiLast.getParentFile();
					}
					IFileHandle[] fileHandles = new IFileHandle[files.length];
					for (int i = 0; i < files.length; i++)
					{
						fileHandles[i] = new FileHandle(files[i]);
					}
					return fileHandles;
				}
			}
		}
		return null;
	}
	
	/**
	 * Shows an "open directory" dialog. If the parameter <code>FileChooser.native</code> is <code>true</code>, an AWT {@link FileDialog}
	 * will be used, otherwise a {@link JFileChooser}. On Windows OS' a {@link JFileChooser} will be used because {@link FileDialog} does
	 * not support directory selection on Windows.
	 * 
	 * @param pLauncher the UI launcher
	 * @param pFrame the parent frame
	 * @param pFileHandleReceiver the filehandle receiver for selected directory
	 * @param pTitle the dialog title
	 * @throws IOException if file access is not possible
	 * @throws SecurityException if a security manager denies the access
	 */
	public void selectDirectory(UILauncher pLauncher, Frame pFrame, IFileHandleReceiver pFileHandleReceiver, String pTitle) throws IOException
	{
		if (Boolean.valueOf(pLauncher.getParameter("FileChooser.native")).booleanValue() && !SwingFactory.isWindows())
		{
			try
			{
				System.setProperty("apple.awt.fileDialogForDirectories", "true");
			
				FileDialog fdlg = new FileDialog(pFrame);
				fdlg.setTitle(pLauncher.translate(pTitle == null ? "Select directory" : pTitle));
				fdlg.setMode(FileDialog.LOAD);
				fdlg.setFilenameFilter(new FilenameFilter()
				{
					public boolean accept(File pDir, String pName)
					{
						if (pName != null)
						{
							return new File(pDir, pName).isDirectory();
						}
						else
						{
							return pDir.isDirectory();
						}
					}
				});
				
	    		if (fiLastSelectedDir == null && fdlg.getDirectory() != null)
	    		{
	    			fiLastSelectedDir = new File(fdlg.getDirectory());
	    		}
	    		
	    		try
	    		{
	    			Method mImage = fdlg.getClass().getMethod("setIconImage", Image.class);
	
	    			mImage.invoke(fdlg, ((Frame)pFrame).getIconImage());
	    		}
	    		catch (Exception e)
	    		{
	    			//nothing to be done
	    		}
	    		
	    		fdlg.setVisible(true);
	    		
	    		String sCurrent = fdlg.getDirectory();
	
	    		if (sCurrent != null)
	    		{
	    			File file = new File(sCurrent);
	    			
	    			fiLastSelectedDir = file;

					pFileHandleReceiver.receiveFileHandle(new DirectoryHandle(file));
	    		}
			}
			finally
			{
				System.setProperty("apple.awt.fileDialogForDirectories", "false");				
			}
		}
		else
		{
	    	JFileChooser fileChooser = new JFileChooser();
	    	fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
	    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			fileChooser.setDialogTitle(pLauncher.translate(pTitle == null ? "Select directory" : pTitle));
	    	fileChooser.setMultiSelectionEnabled(false);
	    	fileChooser.setCurrentDirectory(fiLastSelectedDir);
	    	
	    	int result = fileChooser.showOpenDialog(pFrame);
	    	
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File file = fileChooser.getSelectedFile();

				if (file != null)
				{
					fiLastSelectedDir = file;

					pFileHandleReceiver.receiveFileHandle(new DirectoryHandle(file));
				}
			}
		}
	}
	
}	// FileChooser
