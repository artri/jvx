/*
 * Copyright 2020 SIB Visions GmbH
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
 * 04.04.2020 - [JR] - creation (copied from com.sibvisions.rad.application.ApplicationUtil)
 * 28.03.2010 - [JR] - #47: default choice cell editor mapped
 * 29.11.2010 - [JR] - getLauncher implemented
 * 07.01.2011 - [JR] - removed static block -> moved to application
 * 11.07.2011 - [RH] - #421 (Vx): ApplicationUtil should have a Boolean ChoiceCellEditor
 * 09.11.2011 - [RH] - #501 (Vx): ApplicationUtil should have a scaleImage method 
 * 15.11.2011 - [JR] - getImageData, convertAlignment implemented
 * 03.04.2014 - [RZ] - #998: added default choice cell editors which can handle null values
 */
package javax.rad.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.rad.genui.UIColor;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UICheckBoxCellEditor;
import javax.rad.genui.celleditor.UIChoiceCellEditor;
import javax.rad.genui.celleditor.UIImageViewer;
import javax.rad.genui.celleditor.UINumberCellEditor;
import javax.rad.genui.celleditor.UITextCellEditor;
import javax.rad.genui.control.UICellFormat;
import javax.rad.io.IFileHandle;
import javax.rad.model.ColumnView;
import javax.rad.model.IDataBook;
import javax.rad.model.ui.IControl;
import javax.rad.model.ui.IEditorControl;
import javax.rad.model.ui.ITreeControl;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.IImage.ImageType;
import javax.rad.ui.control.IChart;
import javax.rad.ui.control.ITable;

import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ImageUtil;

/**
 * The <code>ApplicationUtil</code> is a utility class which provides methods and constants which will
 * be used from applications.
 * 
 * @author René Jahn
 */
public class ApplicationUtil 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
	/** Red cell. */
	public static final UICellFormat RED_CELL 		= new UICellFormat(UIColor.red);
	/** Orange cell. */
	public static final UICellFormat ORANGE_CELL 	= new UICellFormat(UIColor.orange);
	/** Yellow cell. */
	public static final UICellFormat YELLOW_CELL 	= new UICellFormat(UIColor.yellow);

	/** Blue cell. */
	public static final UICellFormat BLUE_CELL 		= new UICellFormat(new UIColor(50, 150, 255));
	/** Green cell. */
	public static final UICellFormat GREEN_CELL 	= new UICellFormat(UIColor.green);
	
    /** Text cell editor. */
    public static final UITextCellEditor   TEXT_EDITOR              = new UITextCellEditor();
	/** Passwort cell editor. */
	public static final UITextCellEditor   PASSWORD_EDITOR 			= new UITextCellEditor(UITextCellEditor.TEXT_PLAIN_PASSWORD);
	/** Centered text cell editor. */
	public static final UITextCellEditor   CENTERED_TEXT_EDITOR		= new UITextCellEditor(IAlignmentConstants.ALIGN_CENTER);
    /** Multiline (text area) cell editor. */
    public static final UITextCellEditor   MULTILINE_EDITOR         = new UITextCellEditor(UITextCellEditor.TEXT_PLAIN_WRAPPEDMULTILINE);

    /** Centered number cell editor. */
    public static final UINumberCellEditor NUMBER_EDITOR            = new UINumberCellEditor();
	/** Centered number cell editor. */
	public static final UINumberCellEditor CENTERED_NUMBER_EDITOR	= new UINumberCellEditor(IAlignmentConstants.ALIGN_CENTER);

	/** Yes/No choice cell editor. */
	public static final UIChoiceCellEditor YESNO_EDITOR 			= createYNChoiceCellEditor();
	/** Yes/No/null choice cell editor. */
	public static final UIChoiceCellEditor YESNONULL_EDITOR			= createYNNullChoiceCellEditor();
	
	/** True/False choice cell editor for BooleanDataTypes. */
	public static final UIChoiceCellEditor TRUEFALSE_EDITOR			= createTFChoiceCellEditor();
	/** True/False/null choice cell editor for BooleanDataTypes. */
	public static final UIChoiceCellEditor TRUEFALSENULL_EDITOR		= createTFNullChoiceCellEditor();

    /** Yes/No checkbox cell editor. */
    public static final UICheckBoxCellEditor YESNO_CHECKBOX         = createYNCheckBoxCellEditor();
    /** True/False checkbox cell editor. */
    public static final UICheckBoxCellEditor TRUEFALSE_CHECKBOX     = createTFCheckBoxCellEditor();
	
	/** The Image Viewer. The aspect ratio is preserved. */
	public static final UIImageViewer	   IMAGE_VIEWER				= new UIImageViewer(UIImageViewer.ALIGN_STRETCH, UIImageViewer.ALIGN_STRETCH);

	/** The Image Viewer. The aspect ratio is not preserved.*/
    public static final UIImageViewer      IMAGE_VIEWER_NO_ASPECT_RATIO = createImageViewerNoAspectRatio();

    /** The Image Viewer. The aspect ratio is preserved. */
    public static final UIImageViewer      IMAGE_VIEWER_CENTERED    = new UIImageViewer();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because <code>ApplictionUtil</code> is a utility class.
	 */
	protected ApplicationUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

    /**
     * Returns a new image viewer, that does not preserve the aspect ratio.
     * 
     * @return a new image viewer, that does not preserve the aspect ratio
     */
    public static UIImageViewer createImageViewerNoAspectRatio()
    {
        UIImageViewer imageViewerNoAspectRatio = new UIImageViewer(UIImageViewer.ALIGN_STRETCH, UIImageViewer.ALIGN_STRETCH);
        imageViewerNoAspectRatio.setPreserveAspectRatio(false);
        
        return imageViewerNoAspectRatio;
    }
    
    /**
     * Returns a new checkbox cell editor with True, False values.
     * 
     * @return a new checkbox cell editor with True, False values
     */
    public static UICheckBoxCellEditor createTFCheckBoxCellEditor()
    {
        return new UICheckBoxCellEditor(Boolean.TRUE, Boolean.FALSE);
    }
    
    /**
     * Returns a new checkbox cell editor with "Y", "N" values.
     * 
     * @return a new checkbox cell editor with "Y", "N" values
     */
    public static UICheckBoxCellEditor createYNCheckBoxCellEditor()
    {
        return new UICheckBoxCellEditor("Y", "N");
    }

    /**
	 * Returns a new choice cell editor with True, False values.
	 * 
	 * @return a new choice cell editor with True, False values
	 */
	public static UIChoiceCellEditor createTFChoiceCellEditor()
	{
		return 	new UIChoiceCellEditor(
				new Object [] { Boolean.TRUE, Boolean.FALSE},
	         	new String [] { UIImage.CHECK_YES_SMALL,
				 		        UIImage.CHECK_SMALL},
	                            UIImage.CHECK_SMALL);
	}
	
	/**
	 * Returns a new choice cell editor with True, False, null values.
	 * 
	 * @return a new choice cell editor with True, False, null values
	 */
	public static UIChoiceCellEditor createTFNullChoiceCellEditor()
	{
		return 	new UIChoiceCellEditor(
				new Object [] { Boolean.TRUE, Boolean.FALSE, null},
	         	new String [] { UIImage.CHECK_YES_SMALL,
				 		        UIImage.CHECK_SMALL,
				 		        UIImage.CHECK_NO_SMALL},
	                            UIImage.CHECK_SMALL);
	}
	
	/**
	 * Returns a new choice cell editor with "Y", "N" values.
	 * 
	 * @return a new choice cell editor with "Y", "N" values
	 */
	public static UIChoiceCellEditor createYNChoiceCellEditor()
	{
		return new UIChoiceCellEditor(new Object [] {"Y", "N"},
					              	  new String [] {UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL},
					              	  UIImage.CHECK_SMALL);
	}	
	
	/**
	 * Returns a new choice cell editors with "Y", "N", null values.
	 * 
	 * @return a new choice cell editor with "Y", "N", null values
	 */
	public static UIChoiceCellEditor createYNNullChoiceCellEditor()
	{
		return new UIChoiceCellEditor(new Object [] {"Y", "N", null},
					              	  new String [] {UIImage.CHECK_YES_SMALL, UIImage.CHECK_SMALL, UIImage.CHECK_NO_SMALL},
					              	  UIImage.CHECK_SMALL);
	}	
	
	/**
	 * Gets all visible columns on screen.
	 * 
	 * @param pDataBook the data book
	 * @return all visible columns on screen
	 */
	public static String[] getAllVisibleColumns(IDataBook pDataBook)
	{
		ArrayList<String> columns = new ArrayList<String>();
		
		for (IControl control : pDataBook.getControls())
		{
			if (control instanceof IEditorControl)
			{
				String columnName = ((IEditorControl)control).getColumnName();
				if (columnName != null && !columns.contains(columnName))
				{
					columns.add(columnName);
				}
			}
            else if (control instanceof ITable)
            {
                ColumnView columnView = ((ITable)control).getColumnView();
                if (columnView != null)
                {
                    for (String columnName : columnView.getColumnNames())
                    {
                        if (!columns.contains(columnName))
                        {
                            columns.add(columnName);
                        }
                    }
                }
            }
            else if (control instanceof IChart)
            {
                String columnName = ((IChart)control).getXColumnName();
                if (columnName != null && !columns.contains(columnName))
                {
                    columns.add(columnName);
                }
                
                String[] columnNames = ((IChart)control).getYColumnNames();
                if (columnNames != null)
                {
                    for (String colName : columnNames)
                    {
                        if (!columns.contains(colName))
                        {
                            columns.add(colName);
                        }
                    }
                }
            }
			else if (control instanceof ITreeControl)
			{
				String[] columnNames = pDataBook.getRowDefinition().getColumnView(ITreeControl.class).getColumnNames();
				if (columnNames != null && columnNames.length > 0)
				{
				    String columnName = columnNames[0];
	                if (!columns.contains(columnName))
	                {
	                    columns.add(columnName);
	                }
				}
			}
		}
		return columns.toArray(new String[columns.size()]);
	}

	/**
	 * Gets the launcher for a specific component. The component should be added.
	 * 
	 * @param pComponent any added component
	 * @return the launcher or <code>null</code> if the component is not added
	 */
	public static ILauncher getLauncher(IComponent pComponent)
	{
		while (pComponent != null && !(pComponent instanceof ILauncher))
		{
			pComponent = pComponent.getParent();
		}
		
		return (ILauncher)pComponent;
	}
	
	/**
	 * Gets the application where this component is added.
	 * 
	 * @param pComponent the component
	 * @return the application or <code>null</code> otherwise
	 */
	public static IWorkScreenApplication getApplication(IComponent pComponent)
	{
		while (pComponent != null && !(pComponent instanceof IWorkScreenApplication))
		{
		    if (pComponent instanceof IWorkScreen)
		    {
		        return ((IWorkScreen)pComponent).getApplication();
		    }
		    
			pComponent = pComponent.getParent();
		}
		
		return (IWorkScreenApplication)pComponent;
	}

	/**
	 * Gets the work-screen where this component is added.
	 * 
	 * @param pComponent the component
	 * @return the application or <code>null</code> otherwise
	 */
	public static IWorkScreen getWorkScreen(IComponent pComponent)
	{
		while (pComponent != null && !(pComponent instanceof IWorkScreen))
		{
			pComponent = pComponent.getParent();
		}
		
		return (IWorkScreen)pComponent;
	}	
	
	/**
	 * Gets the raw image data.
	 * 
	 * @param pImage the image
	 * @return the image data as bytes
	 * @throws IOException if image conversion fails
	 */
	public static byte[] getImageData(IImage pImage) throws IOException
	{
		if (pImage == null)
		{
			return null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
		try
		{
			pImage.saveAs(baos, ImageType.PNG);
			
			return baos.toByteArray();
		}
		finally
		{
			try
			{
				baos.close();
			}
			catch (Exception e)
			{
				//nothing to be done
			}
		}
	}

	/**
	 * Converts an alignment "name" to the constant value.
	 * 
	 * @param pAlign the alignment: "left", "right", "top", "bottom", "center", "stretch"
	 * @return the alignment from {@link IAlignmentConstants}
	 */
	public static int convertAlignment(String pAlign)
	{
		if (pAlign != null)
		{
			String sAlign = pAlign.toLowerCase();
			
			if ("left".equals(sAlign))
			{
				return IAlignmentConstants.ALIGN_LEFT;
			}
			else if ("right".equals(sAlign))
			{
				return IAlignmentConstants.ALIGN_RIGHT;
			}
			else if ("top".equals(sAlign))
			{
				return IAlignmentConstants.ALIGN_TOP;
			}
			else if ("bottom".equals(sAlign))
			{
				return IAlignmentConstants.ALIGN_BOTTOM;
			}
			else if ("stretch".equals(sAlign))
			{
				return IAlignmentConstants.ALIGN_STRETCH;
			}
		}
		
		return IAlignmentConstants.ALIGN_CENTER;
	}	
	
	/**
	 * It reads the file handle and scales the image to the determined width and height. 
	 * 
	 * @param pFileHandle the FileHandle to use
	 * @param pWidth the width to use
	 * @param pHeight the heigth to use
	 * @return the scaled image with the determined width and height
	 * @throws IOException if the image can't read or scaled
	 */
	public static byte[] scaleImage(IFileHandle pFileHandle, int pWidth, int pHeight) throws IOException
	{
		String sFormat = FileUtil.getExtension(pFileHandle.getFileName().toLowerCase());

		if ("png".equals(sFormat) || "jpg".equals(sFormat) || "gif".equals(sFormat))
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageUtil.createScaledImage(pFileHandle.getInputStream(), pWidth, pHeight, true, stream, sFormat);
			stream.close();

			return stream.toByteArray();
		}
		else
		{
			throw new IOException("Image format '" + sFormat + "' not supported. Use 'png', 'jpg' or 'gif'!");
		}
	}	
	
}	// ApplicationUtil
