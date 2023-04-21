/*
 * Copyright 2009 SIB Visions GmbH
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
 * 14.11.2008 - [HM] - creation
 * 09.06.2009 - [JR] - added predefined image handling (image library)
 * 29.07.2009 - [JR] - checked uiResource null value
 * 04.08.2009 - [JR] - added check and question icons
 * 21.02.2011 - [JR] - #62: saveAs with output stream and type 
 * 01.05.2011 - [JR] - clearImageCache implemented
 * 25.10.2011 - [JR] - added navigation and edit images 
 * 14.06.2013 - [JR] - #672: cache image with factory name
 * 10.12.2014 - [JR] - #1213: clearImage, clearImageMappings, getImageNames introduced
 */
package javax.rad.genui;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.rad.ui.IFactory;
import javax.rad.ui.IImage;

/**
 * Platform and technology independent image definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Image
 */
public class UIImage extends UIFactoryResource<IImage> 
                     implements IImage
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the name of the small(default: 16x16 px) exit image (used for menuitems or buttons). */
	public static final String EXIT_SMALL = "EXIT_SMALL";

	/** the name of the large(default: 24x24 px) exit image (used for toolbar buttons or icons). */
	public static final String EXIT_LARGE = "EXIT_LARGE";
	
	/** the name of the small(default: 16x16 px) login image (used for menuitems or buttons). */
	public static final String LOGIN_SMALL = "LOGIN_SMALL";

	/** the name of the large(default: 24x24 px) login image (used for toolbar buttons or icons). */
	public static final String LOGIN_LARGE = "LOGIN_LARGE";
	
	/** the name of the small(default: 16x16 px) logout image (used for menuitems or buttons). */
	public static final String LOGOUT_SMALL = "LOGOUT_SMALL";

	/** the name of the large(default: 24x24 px) logout image (used for toolbar buttons or icons). */
	public static final String LOGOUT_LARGE = "LOGOUT_LARGE";
	
	/** the name of the small(default: 16x16 px) change password image (used for menuitems or buttons). */
	public static final String CHANGE_PASSWORD_SMALL = "CHANGE_PASSWORD_SMALL";

	/** the name of the large(default: 24x24 px) change password image (used for toolbar buttons or icons). */
	public static final String CHANGE_PASSWORD_LARGE = "CHANGE_PASSWORD_LARGE";
	
	/** the name of the small(default: 16x16 px) help image (used for menuitems or buttons). */
	public static final String HELP_SMALL = "HELP_SMALL";

	/** the name of the large(default: 24x24 px) help image (used for toolbar buttons or icons). */
	public static final String HELP_LARGE = "HELP_LARGE";
	
	/** the name of the small(default: 16x16 px) about image (used for menuitems or buttons). */
	public static final String ABOUT_SMALL = "ABOUT_SMALL";
	
	/** the name of the large(default: 24x24 px) about image (used for toolbar buttons or icons). */
	public static final String ABOUT_LARGE = "ABOUT_LARGE";
	
	/** the name of the small(default: 16x16 px) add image (used for menuitems or buttons). */
	public static final String ADD_SMALL = "ADD_SMALL";
	
	/** the name of the large(default: 24x24 px) add image (used for toolbar buttons or icons). */
	public static final String ADD_LARGE = "ADD_LARGE";

	/** the name of the small(default: 16x16 px) remove image (used for menuitems or buttons). */
	public static final String REMOVE_SMALL = "REMOVE_SMALL";
	
	/** the name of the large(default: 24x24 px) remove image (used for toolbar buttons or icons). */
	public static final String REMOVE_LARGE = "REMOVE_LARGE";

	/** the name of the small(default: 16x16 px) save image (used for menuitems or buttons). */
	public static final String SAVE_SMALL = "SAVE_SMALL";
	
	/** the name of the large(default: 24x24 px) save image (used for toolbar buttons or icons). */
	public static final String SAVE_LARGE = "SAVE_LARGE";
	
	/** the name of the small(default: 16x16 px) save all image (used for menuitems or buttons). */
	public static final String SAVE_ALL_SMALL = "SAVE_ALL_SMALL";
	
	/** the name of the large(default: 24x24 px) save all image (used for toolbar buttons or icons). */
	public static final String SAVE_ALL_LARGE = "SAVE_ALL_LARGE";

	/** the name of the small(default: 16x16 px) reload image (used for menuitems or buttons). */
	public static final String RELOAD_SMALL = "RELOAD_SMALL";
	
	/** the name of the large(default: 24x24 px) reload image (used for toolbar buttons or icons). */
	public static final String RELOAD_LARGE = "RELOAD_LARGE";
	
	/** the name of the small(default: 16x16 px) redo image (used for menuitems or buttons). */
	public static final String REDO_SMALL = "REDO_SMALL";
	
	/** the name of the large(default: 24x24 px) redo image (used for toolbar buttons or icons). */
	public static final String REDO_LARGE = "REDO_LARGE";
	
	/** the name of the small(default: 16x16 px) undo image (used for menuitems or buttons). */
	public static final String UNDO_SMALL = "UNDO_SMALL";
	
	/** the name of the large(default: 24x24 px) undo image (used for toolbar buttons or icons). */
	public static final String UNDO_LARGE = "UNDO_LARGE";
	
	/** the name of the small(default: 16x16 px) move up image (used for menuitems or buttons). */
	public static final String MOVE_UP_SMALL = "MOVE_UP_SMALL";
	
	/** the name of the large(default: 24x24 px) move up image (used for toolbar buttons or icons). */
	public static final String MOVE_UP_LARGE = "MOVE_UP_LARGE";

	/** the name of the small(default: 16x16 px) move down image (used for menuitems or buttons). */
	public static final String MOVE_DOWN_SMALL = "MOVE_DOWN_SMALL";
	
	/** the name of the large(default: 24x24 px) move down image (used for toolbar buttons or icons). */
	public static final String MOVE_DOWN_LARGE = "MOVE_DOWN_LARGE";

	/** the name of the small(default: 16x16 px) move first image (used for menuitems or buttons). */
	public static final String MOVE_FIRST_SMALL = "MOVE_FIRST_SMALL";
	
	/** the name of the large(default: 24x24 px) move first image (used for toolbar buttons or icons). */
	public static final String MOVE_FIRST_LARGE = "MOVE_FIRST_LARGE";

	/** the name of the small(default: 16x16 px) move last image (used for menuitems or buttons). */
	public static final String MOVE_LAST_SMALL = "MOVE_LAST_SMALL";
	
	/** the name of the large(default: 24x24 px) move last image (used for toolbar buttons or icons). */
	public static final String MOVE_LAST_LARGE = "MOVE_LAST_LARGE";

	/** the name of the small(default: 16x16 px) move top image (used for menuitems or buttons). */
	public static final String MOVE_TOP_SMALL = "MOVE_TOP_SMALL";
	
	/** the name of the large(default: 24x24 px) move top image (used for toolbar buttons or icons). */
	public static final String MOVE_TOP_LARGE = "MOVE_TOP_LARGE";

	/** the name of the small(default: 16x16 px) move bottom image (used for menuitems or buttons). */
	public static final String MOVE_BOTTOM_SMALL = "MOVE_BOTTOM_SMALL";
	
	/** the name of the large(default: 24x24 px) move bottom image (used for toolbar buttons or icons). */
	public static final String MOVE_BOTTOM_LARGE = "MOVE_BOTTOM_LARGE";
	
	/** the name of the small(default: 16x16 px) move next image (used for menuitems or buttons). */
	public static final String MOVE_NEXT_SMALL = "MOVE_NEXT_SMALL";
	
	/** the name of the large(default: 24x24 px) move next image (used for toolbar buttons or icons). */
	public static final String MOVE_NEXT_LARGE = "MOVE_NEXT_LARGE";

	/** the name of the small(default: 16x16 px) move previous image (used for menuitems or buttons). */
	public static final String MOVE_PREVIOUS_SMALL = "MOVE_PREVIOUS_SMALL";
	
	/** the name of the large(default: 24x24 px) move previous image (used for toolbar buttons or icons). */
	public static final String MOVE_PREVIOUS_LARGE = "MOVE_PREVIOUS_LARGE";
	
	/** the name of the small(default: 16x16 px) export image (used for menuitems or buttons). */
	public static final String EXPORT_SMALL = "EXPORT_SMALL";
	
	/** the name of the large(default: 24x24 px) export image (used for toolbar buttons or icons). */
	public static final String EXPORT_LARGE = "EXPORT_LARGE";

	/** the name of the small(default: 16x16 px) import image (used for menuitems or buttons). */
	public static final String IMPORT_SMALL = "IMPORT_SMALL";
	
	/** the name of the large(default: 24x24 px) import image (used for toolbar buttons or icons). */
	public static final String IMPORT_LARGE = "IMPORT_LARGE";

	/** the name of the small(default: 16x16 px) copy image (used for menuitems or buttons). */
	public static final String COPY_SMALL = "COPY_SMALL";
	
	/** the name of the large(default: 24x24 px) copy image (used for toolbar buttons or icons). */
	public static final String COPY_LARGE = "COPY_LARGE";
	
	/** the name of the small(default: 16x16 px) paste image (used for menuitems or buttons). */
	public static final String PASTE_SMALL = "PASTE_SMALL";
	
	/** the name of the large(default: 24x24 px) paste image (used for toolbar buttons or icons). */
	public static final String PASTE_LARGE = "PASTE_LARGE";

	/** the name of the small(default: 16x16 px) cut image (used for menuitems or buttons). */
	public static final String CUT_SMALL = "CUT_SMALL";
	
	/** the name of the large(default: 24x24 px) cut image (used for toolbar buttons or icons). */
	public static final String CUT_LARGE = "CUT_LARGE";
	
	/** the name of the small(default: 16x16 px) search image (used for menuitems or buttons). */
	public static final String SEARCH_SMALL = "SEARCH_SMALL";
	
	/** the name of the large(default: 24x24 px) search image (used for toolbar buttons or icons). */
	public static final String SEARCH_LARGE = "SEARCH_LARGE";
	
	/** the name of the small(default: 16x16 px) ok image (used for menuitems or buttons). */
	public static final String OK_SMALL = "OK_SMALL";
	
	/** the name of the large(default: 24x24 px) ok image (used for toolbar buttons or icons). */
	public static final String OK_LARGE = "OK_LARGE";

	/** the name of the small(default: 16x16 px) cancel image (used for menuitems or buttons). */
	public static final String CANCEL_SMALL = "CANCEL_SMALL";
	
	/** the name of the large(default: 24x24 px) canel image (used for toolbar buttons or icons). */
	public static final String CANCEL_LARGE = "CANCEL_LARGE";
	
	/** the name of the small(default: 16x16 px) information image, for messages (used for menuitems or buttons). */
	public static final String MESSAGE_INFO_SMALL = "MESSAGE_INFO_SMALL";
	
	/** the name of the large(default: 32x32 px) information image, for messages (used for toolbar buttons or icons). */
	public static final String MESSAGE_INFO_LARGE = "MESSAGE_INFO_LARGE";
	
	/** the name of the small(default: 16x16 px) warning image, for messages (used for menuitems or buttons). */
	public static final String MESSAGE_WARNING_SMALL = "MESSAGE_WARNING_SMALL";
	
	/** the name of the large(default: 32x32 px) warning image, for messages (used for toolbar buttons or icons). */
	public static final String MESSAGE_WARNING_LARGE = "MESSAGE_WARNING_LARGE";

	/** the name of the small(default: 16x16 px) error image, for messages (used for menuitems or buttons). */
	public static final String MESSAGE_ERROR_SMALL = "MESSAGE_ERROR_SMALL";
	
	/** the name of the large(default: 32x32 px) error image, for messages (used for toolbar buttons or icons). */
	public static final String MESSAGE_ERROR_LARGE = "MESSAGE_ERROR_LARGE";

	/** the name of the small(default: 16x16 px) question image, for messages (used for menuitems or buttons). */
	public static final String MESSAGE_QUESTION_SMALL = "MESSAGE_QUESTION_SMALL";
	
	/** the name of the large(default: 32x32 px) question image, for messages (used for toolbar buttons or icons). */
	public static final String MESSAGE_QUESTION_LARGE = "MESSAGE_QUESTION_LARGE";

	/** the name of the small(default: 16x16 px) check image (used for menuitems or buttons). */
	public static final String CHECK_SMALL = "CHECK_SMALL";

	/** the name of the large(default: 24x24 px) check image (used for toolbar buttons or icons). */
	public static final String CHECK_LARGE = "CHECK_LARGE";
	
	/** the name of the small(default: 16x16 px) yes/selected check image (used for menuitems or buttons). */
	public static final String CHECK_YES_SMALL = "CHECK_YES_SMALL";

	/** the name of the large(default: 24x24 px) yes/selected check image (used for toolbar buttons or icons). */
	public static final String CHECK_YES_LARGE = "CHECK_YES_LARGE";

	/** the name of the small(default: 16x16 px) no/selected check image (used for menuitems or buttons). */
	public static final String CHECK_NO_SMALL = "CHECK_NO_SMALL";

	/** the name of the large(default: 24x24 px) no/selected check image (used for toolbar buttons or icons). */
	public static final String CHECK_NO_LARGE = "CHECK_NO_LARGE";
	
	/** the name of the small(default: 16x16 px) register image (used for menuitems or buttons). */
	public static final String REGISTER_SMALL = "REGISTER_SMALL";
	
	/** the name of the large(default: 24x24 px) register image (used for toolbar buttons or icons). */
	public static final String REGISTER_LARGE = "REGISTER_LARGE";

	/** the name of the small(default: 16x16 px) edit image (used for menuitems or buttons). */
	public static final String EDIT_SMALL = "EDIT_SMALL";
	
	/** the name of the large(default: 24x24 px) edit image (used for toolbar buttons or icons). */
	public static final String EDIT_LARGE = "EDIT_LARGE";

	/** the name of the small(default: 16x16 px) trash image (used for menuitems or buttons). */
    public static final String TRASH_SMALL = "TRASH_SMALL";
    
    /** the name of the large(default: 24x24 px) trash image (used for toolbar buttons or icons). */
    public static final String TRASH_LARGE = "TRASH_LARGE";

	
	/** contains created UIImages. */
	private static Hashtable<String, UIImage> htImageCache = new Hashtable<String, UIImage>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	//initializes the predefined images
	static
	{
		setDefaults();
	}
	
	/**
     * Creates a new instance of <code>UIImage</code> based on an
     * {@link IImage}.
     *
     * @param pImage the Image.
     * @see IImage
     */
	protected UIImage(IImage pImage)
	{
		super(pImage);
	}
	
	/**
	 * Creates a new instance of <code>UIImage</code> for specific byte data.
	 * 
	 * @param pData the image data
	 */
	public UIImage(byte[] pData)
	{
		this(null, pData);
	}
	
	/**
	 * Creates a new instance of <code>UIImage</code> for the specified name.
	 * 
	 * @param pImageName the name of the image
	 */
	public UIImage(String pImageName)
	{
		super(UIFactoryManager.getFactory().getImage(pImageName));
	}
	
	/**
	 * Creates a new instance of <code>UIImage</code> for specific byte data
	 * and an image name.
	 * 
	 * @param pImageName the name of the image specified with <code>byData</code>
	 * @param pData the image data
	 */
	public UIImage(String pImageName, byte[] pData)
	{
		super(UIFactoryManager.getFactory().getImage(pImageName, pData));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getImageName()
	{
	    IImage res = getUIResource();
	    
	    if (res == null)
	    {
	        return null;
	    }
	    else
	    {
	        return res.getImageName();
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getWidth()
    {
	    IImage res = getUIResource();
	    
		if (res == null)
		{
			return 0;
		}
		else
		{
			return res.getWidth();
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public int getHeight()
    {
        IImage res = getUIResource();

        if (res == null)
		{
			return 0;
		}
		else
		{
			return res.getHeight();
		}
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void saveAs(OutputStream pOut, ImageType pType) throws IOException
	{
	    IImage res = getUIResource();

		if (res == null)
		{
			throw new IOException("No image: null");
		}
		else
		{
			res.saveAs(pOut, pType);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
    {
		if (pObject instanceof IImage)
		{
			return this == pObject;
		}
		
    	return false;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
    {
		return System.identityHashCode(this);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
    {
		return getClass().getName() + "[" + getImageName() + ", " + getWidth() + "x" + getHeight() + "]";
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets te default image mappings.
	 */
	public static void setDefaults()
	{
	    synchronized (htImageCache)
	    {
    	    clearImageMappings();
    
    	    setImageMapping(EXIT_SMALL, "/javax/rad/genui/images/16x16/exit.png");
    		setImageMapping(EXIT_LARGE, "/javax/rad/genui/images/24x24/exit.png");
    
    		setImageMapping(LOGIN_SMALL, "/javax/rad/genui/images/16x16/login.png");
    		setImageMapping(LOGIN_LARGE, "/javax/rad/genui/images/24x24/login.png");
    
    		setImageMapping(LOGOUT_SMALL, "/javax/rad/genui/images/16x16/logout.png");
    		setImageMapping(LOGOUT_LARGE, "/javax/rad/genui/images/24x24/logout.png");
    		
    		setImageMapping(CHANGE_PASSWORD_SMALL, "/javax/rad/genui/images/16x16/changepassword.png");
    		setImageMapping(CHANGE_PASSWORD_LARGE, "/javax/rad/genui/images/24x24/changepassword.png");
    		
    		setImageMapping(HELP_SMALL, "/javax/rad/genui/images/16x16/help.png");
    		setImageMapping(HELP_LARGE, "/javax/rad/genui/images/24x24/help.png");
    		
    		setImageMapping(ABOUT_SMALL, "/javax/rad/genui/images/16x16/about.png");
    		setImageMapping(ABOUT_LARGE, "/javax/rad/genui/images/32x32/about.png");
    		
    		setImageMapping(ADD_SMALL, "/javax/rad/genui/images/16x16/add.png");
    		setImageMapping(ADD_LARGE, "/javax/rad/genui/images/24x24/add.png");
    
    		setImageMapping(REMOVE_SMALL, "/javax/rad/genui/images/16x16/remove.png");
    		setImageMapping(REMOVE_LARGE, "/javax/rad/genui/images/24x24/remove.png");
    
    		setImageMapping(SAVE_SMALL, "/javax/rad/genui/images/16x16/save.png");
    		setImageMapping(SAVE_LARGE, "/javax/rad/genui/images/24x24/save.png");
    
    		setImageMapping(SAVE_ALL_SMALL, "/javax/rad/genui/images/16x16/save_all.png");
    		setImageMapping(SAVE_ALL_LARGE, "/javax/rad/genui/images/24x24/save_all.png");
    
    		setImageMapping(RELOAD_SMALL, "/javax/rad/genui/images/16x16/reload.png");
    		setImageMapping(RELOAD_LARGE, "/javax/rad/genui/images/24x24/reload.png");
    
    		setImageMapping(REDO_SMALL, "/javax/rad/genui/images/16x16/redo.png");
    		setImageMapping(REDO_LARGE, "/javax/rad/genui/images/24x24/redo.png");
    
    		setImageMapping(UNDO_SMALL, "/javax/rad/genui/images/16x16/undo.png");
    		setImageMapping(UNDO_LARGE, "/javax/rad/genui/images/24x24/undo.png");
    
    		setImageMapping(MOVE_UP_SMALL, "/javax/rad/genui/images/16x16/up.png");
    		setImageMapping(MOVE_UP_LARGE, "/javax/rad/genui/images/24x24/up.png");
    		
    		setImageMapping(MOVE_DOWN_SMALL, "/javax/rad/genui/images/16x16/down.png");
    		setImageMapping(MOVE_DOWN_LARGE, "/javax/rad/genui/images/24x24/down.png");
    
    		setImageMapping(MOVE_FIRST_SMALL, "/javax/rad/genui/images/16x16/first.png");
    		setImageMapping(MOVE_FIRST_LARGE, "/javax/rad/genui/images/24x24/first.png");
    
    		setImageMapping(MOVE_LAST_SMALL, "/javax/rad/genui/images/16x16/last.png");
    		setImageMapping(MOVE_LAST_LARGE, "/javax/rad/genui/images/24x24/last.png");
    
    		setImageMapping(MOVE_TOP_SMALL, "/javax/rad/genui/images/16x16/top.png");
    		setImageMapping(MOVE_TOP_LARGE, "/javax/rad/genui/images/24x24/top.png");
    
    		setImageMapping(MOVE_BOTTOM_SMALL, "/javax/rad/genui/images/16x16/bottom.png");
    		setImageMapping(MOVE_BOTTOM_LARGE, "/javax/rad/genui/images/24x24/bottom.png");
    
    		setImageMapping(MOVE_NEXT_SMALL, "/javax/rad/genui/images/16x16/next.png");
    		setImageMapping(MOVE_NEXT_LARGE, "/javax/rad/genui/images/24x24/next.png");
    
    		setImageMapping(MOVE_PREVIOUS_SMALL, "/javax/rad/genui/images/16x16/previous.png");
    		setImageMapping(MOVE_PREVIOUS_LARGE, "/javax/rad/genui/images/24x24/previous.png");
    		
    		setImageMapping(EXPORT_SMALL, "/javax/rad/genui/images/16x16/export.png");
    		setImageMapping(EXPORT_LARGE, "/javax/rad/genui/images/24x24/export.png");
    
    		setImageMapping(IMPORT_SMALL, "/javax/rad/genui/images/16x16/import.png");
    		setImageMapping(IMPORT_LARGE, "/javax/rad/genui/images/24x24/import.png");
    
    		setImageMapping(COPY_SMALL, "/javax/rad/genui/images/16x16/copy.png");
    		setImageMapping(COPY_LARGE, "/javax/rad/genui/images/24x24/copy.png");
    
    		setImageMapping(PASTE_SMALL, "/javax/rad/genui/images/16x16/paste.png");
    		setImageMapping(PASTE_LARGE, "/javax/rad/genui/images/24x24/paste.png");
    
    		setImageMapping(CUT_SMALL, "/javax/rad/genui/images/16x16/cut.png");
    		setImageMapping(CUT_LARGE, "/javax/rad/genui/images/24x24/cut.png");
    
    		setImageMapping(SEARCH_SMALL, "/javax/rad/genui/images/16x16/search.png");
    		setImageMapping(SEARCH_LARGE, "/javax/rad/genui/images/24x24/search.png");
    
    		setImageMapping(OK_SMALL, "/javax/rad/genui/images/16x16/ok.png");
    		setImageMapping(OK_LARGE, "/javax/rad/genui/images/24x24/ok.png");
    
    		setImageMapping(CANCEL_SMALL, "/javax/rad/genui/images/16x16/cancel.png");
    		setImageMapping(CANCEL_LARGE, "/javax/rad/genui/images/24x24/cancel.png");
    
    		setImageMapping(MESSAGE_INFO_SMALL, "/javax/rad/genui/images/16x16/information.png");
    		setImageMapping(MESSAGE_INFO_LARGE, "/javax/rad/genui/images/32x32/information.png");
    
    		setImageMapping(MESSAGE_WARNING_SMALL, "/javax/rad/genui/images/16x16/warning.png");
    		setImageMapping(MESSAGE_WARNING_LARGE, "/javax/rad/genui/images/32x32/warning.png");
    
    		setImageMapping(MESSAGE_ERROR_SMALL, "/javax/rad/genui/images/16x16/error.png");
    		setImageMapping(MESSAGE_ERROR_LARGE, "/javax/rad/genui/images/32x32/error.png");
    
    		setImageMapping(MESSAGE_QUESTION_SMALL, "/javax/rad/genui/images/16x16/question.png");
    		setImageMapping(MESSAGE_QUESTION_LARGE, "/javax/rad/genui/images/32x32/question.png");
    
    		setImageMapping(CHECK_SMALL, "/javax/rad/genui/images/16x16/check.png");
    		setImageMapping(CHECK_LARGE, "/javax/rad/genui/images/24x24/check.png");
    	
    		setImageMapping(CHECK_YES_SMALL, "/javax/rad/genui/images/16x16/check_yes.png");
    		setImageMapping(CHECK_YES_LARGE, "/javax/rad/genui/images/24x24/check_yes.png");
    
    		setImageMapping(CHECK_NO_SMALL, "/javax/rad/genui/images/16x16/check_no.png");
    		setImageMapping(CHECK_NO_LARGE, "/javax/rad/genui/images/24x24/check_no.png");
    
    		setImageMapping(REGISTER_SMALL, "/javax/rad/genui/images/16x16/register.png");
    		setImageMapping(REGISTER_LARGE, "/javax/rad/genui/images/24x24/register.png");
    		
    		setImageMapping(EDIT_SMALL, "/javax/rad/genui/images/16x16/pencil.png");
    		setImageMapping(EDIT_LARGE, "/javax/rad/genui/images/24x24/pencil.png");
    		
            setImageMapping(TRASH_SMALL, "/javax/rad/genui/images/16x16/trash.png");
            setImageMapping(TRASH_LARGE, "/javax/rad/genui/images/24x24/trash.png");
	    }
	}
	
	/**
	 * Gets the cached instance of an image resource. If the image was
	 * not cached, then a new instance of the image will be created.
	 * 
	 * @param pImageName the path for the image (filename or resource path)
	 * @return the image instance for <code>pImagePath</code>
	 */
    public static UIImage getImage(String pImageName)
	{
    	if (pImageName == null)
    	{
    		return null;
    	}
    	
    	pImageName = getImageMapping(pImageName);
    	
    	UIImage image = htImageCache.get(pImageName);
    	
    	if (image == null)
    	{
    	    IFactory factory = UIFactoryManager.getFactory();
    	    
    	    if (factory != null)
    	    {
            	IImage res = factory.getImage(pImageName);
        		
        		if (res != null)
        		{
    	    		image = new UIImage(res);
    	    		
    	    		htImageCache.put(pImageName, image);
        		}
    	    }
    	}
    	
		return image;
	}
    
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @return the image name.
     */
    public static String getImageMapping(String pMappingName)
    {
        IFactory factory = UIFactoryManager.getFactory();
        
        if (factory != null)
        {
            return factory.getImageMapping(pMappingName);
        }
        else
        {
            return pMappingName;
        }
    }
    
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @param pImageName the image name.
     */
    public static void setImageMapping(String pMappingName, String pImageName)
    {
    	IFactory factory = UIFactoryManager.getFactory();
    	
    	if (factory != null)
    	{
    	    factory.setImageMapping(pMappingName, pImageName);
    	
        	//clear cache because it is possible that the image has changed
        	if (pImageName != null)
        	{
        		htImageCache.remove(pImageName);
        	}
    	}
    }
    
    /**
     * Gets all used mapping names.
     * 
     * @return the mapping names
     */
    public static String[] getImageMappingNames()
    {
        IFactory factory = UIFactoryManager.getFactory();
        
        if (factory != null)
        {
            return factory.getImageMappingNames();
        }
        else
        {
            return new String[0];
        }
    }
    
    /**
     * Gets all available image names.
     * 
     * @return the image names
     */
    public static String[] getImageNames()
    {
        return htImageCache.keySet().toArray(new String[htImageCache.size()]);
    }

    /**
     * Removes all image mappings and also clears the image cache for mapped images.
     */
    public static void clearImageMappings()
    {
        IFactory factory = UIFactoryManager.getFactory();
        
        if (factory != null)
        {
    	    synchronized (htImageCache)
    	    {
	            for (String sName : factory.getImageMappingNames())
	            {
	                clearImage(getImageMapping(sName));
	    
	                factory.setImageMapping(sName, null);
	            }
    	    }
        }
    }

    /**
     * Clears the cache for the given image.
     * 
     * @param pImageName the image name
     */
    public static void clearImage(String pImageName)
    {
    	if (pImageName != null)
    	{
	        IFactory factory = UIFactoryManager.getFactory();
	
	        if (factory != null)
	        {
	            factory.setImageMapping(null, pImageName);
	            
	            htImageCache.remove(pImageName);
	        }
    	}
    }
    
    /**
     * Removes all images from the cache.
     */
    public static void clearImageCache()
    {
        Hashtable<String, UIImage> htCopy = (Hashtable<String, UIImage>)htImageCache.clone();
        
        for (Entry<String, UIImage> entry : htCopy.entrySet())
        {
            clearImage(entry.getKey());
        }
    }
    
} 	// UIImage
