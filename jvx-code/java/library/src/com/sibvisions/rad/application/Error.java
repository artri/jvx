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
 * 02.12.2008 - [JR] - creation
 * 08.12.2008 - [JR] - updateAnchors implemented
 * 26.02.2008 - [HM] - getLocalizedMessage replaced with getMessage, because translation
 *                     should work without localization
 * 15.01.2013 - [JR] - #625: added methods
 * 19.10.2015 - [JR] - set style information
 * 21.07.2020 - [JR] - #2329: notifyVisible, notifyDestroy events   
 * 09.06.2021 - [JR] - detail visibility            
 */
package com.sibvisions.rad.application;

import javax.rad.application.IContent;
import javax.rad.application.genui.event.ContentHandler;
import javax.rad.application.genui.event.type.content.INotifyDestroyListener;
import javax.rad.application.genui.event.type.content.INotifyVisibleListener;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIImage;
import javax.rad.genui.celleditor.UITextCellEditor;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.model.ColumnDefinition;
import javax.rad.model.ColumnView;
import javax.rad.model.IRowDefinition;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.ObjectDataType;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.Style;
import javax.rad.ui.celleditor.ITextCellEditor;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.util.RootCauseException;
import javax.rad.util.RootCauseSecurityException;

import com.sibvisions.rad.application.event.ErrorHandler;
import com.sibvisions.rad.model.mem.MemDataBook;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * The <code>Error</code> is designed to visualize errors occured 
 * in an application. It displays the whole error chain.
 * 
 * @author René Jahn
 */
public class Error extends UIInternalFrame
                   implements IContent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the message opener. */
	private transient Object opener;
	
	/** the "notify visible" event. */
	private transient ContentHandler<INotifyVisibleListener> eventNotifyVisible;
	/** the "notify destroy" event. */
	private transient ContentHandler<INotifyDestroyListener> eventNotifyDestroy;

	/** the hide details event. */
	private transient ErrorHandler eventHideDetails;
	/** the show details event. */
	private transient ErrorHandler eventShowDetails;
	
	/** the layout. */
	private transient UIFormLayout 	 layMain 		= new UIFormLayout();
	/** the layout. */
	private transient UIBorderLayout layDetails 	= new UIBorderLayout();
	/** the details panel. */
	private transient UIGroupPanel 	 gpanDetails 	= new UIGroupPanel();
	
	/** the error description. */ 
	private transient UITextArea 	 taMessage 		= new UITextArea();
	/** the icon. */
	private transient UIIcon 		 icon 			= new UIIcon();

	/** the ok/close button. */
	private transient UIButton 		 butOK 			= new UIButton();
	/** the details button. */
	private transient UIToggleButton butDetails 	= new UIToggleButton();
	
	/** the error text. */
	private transient UIEditor editErrorDetail 		= new UIEditor();
	/** the error details. */
	private transient UITable  tblError 			= new UITable();
	
	/** the representation of the error chain. */
	private transient MemDataBook 	 mdbError 		= new MemDataBook();
	
    /** whether the content is already destroyed. */
    private transient boolean bDestroyed;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Error</code> for a special desktop.
	 * 
	 * @param pDesktop the desktop for showing the frame
	 * @throws Exception if it's not possible to build the UI
	 */
	public Error(IDesktopPanel pDesktop) throws Exception
	{
		super(pDesktop);
		
		init();
	}

	/**
	 * Builds the UI.
	 * 
	 * @throws Exception if the error table can not be opened
	 */
	private void init() throws Exception
	{
	    Style.addStyleNames(this, "internalframe-error");
		setTitle("Information");
		setResizable(true);
		setIconImage(null);
		setMaximizable(false);
		setIconifiable(false);
		setModal(true);
		
		setMinimumSize(200, 140);

		//----------------------------------------------------------------
		// Standard components

		icon.setImage(UIImage.getImage(UIImage.MESSAGE_ERROR_LARGE));
		icon.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);

		taMessage.setMinimumSize(0, 50);
		taMessage.setPreferredSize(470, 70);
		taMessage.setWordWrap(true);
		taMessage.setName("errordialog-message");
		taMessage.setEditable(false);
		taMessage.setBackground(UIColor.white);
		
		butOK.eventAction().addListener(this, "doOk");
		butOK.setText("OK");
		butOK.setDefaultButton(true);
		
		butDetails.eventAction().addListener(this, "doDetails");
		butDetails.setText("Details");

		//----------------------------------------------------------------
		// Detail components
		
		mdbError.setName("Error");
		mdbError.setUpdateEnabled(false);
		mdbError.setDeleteEnabled(false);
		
		IRowDefinition rowdef = mdbError.getRowDefinition();
		
		rowdef.addColumnDefinition(new ColumnDefinition("ERROR"));
		rowdef.addColumnDefinition(new ColumnDefinition("DETAIL"));
		rowdef.addColumnDefinition(new ColumnDefinition("CAUSE", new ObjectDataType()));
		rowdef.setColumnView(null, new ColumnView("ERROR"));
		rowdef.getColumnDefinition("ERROR").setLabel("Cause(s) of failure");
		
		mdbError.open();
		
		tblError.setDataBook(mdbError);
		tblError.setPreferredSize(0, 90);
		tblError.setMaximumSize(Integer.MAX_VALUE, 90);
		tblError.setAutoResize(true);
		
		editErrorDetail.setCellEditor(new UITextCellEditor(ITextCellEditor.TEXT_PLAIN_MULTILINE));
		editErrorDetail.setDataRow(mdbError);
		editErrorDetail.setColumnName("DETAIL");
		editErrorDetail.setPreferredSize(0, 200);

		//----------------------------------------------------------------
		// Details
		//----------------------------------------------------------------

		layDetails.setMargins(2, 2, 2, 2);
		
		gpanDetails.setText("Details");
		gpanDetails.setLayout(layDetails);
		gpanDetails.add(tblError, UIBorderLayout.NORTH);
		gpanDetails.add(editErrorDetail, UIBorderLayout.CENTER);
		
		//----------------------------------------------------------------
		// Standard
		//----------------------------------------------------------------
		
		layMain.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
		layMain.setMargins(7, 7, 7, 7);
		layMain.setAnchorConfiguration("t1=7,b-2=-7");
		
		setLayout(layMain);
		
		add(icon,		 layMain.getConstraints(0, 0));
		add(taMessage,	 layMain.getConstraints(1, 0, -1, -2));
		add(butOK, 	     layMain.getConstraints(-1, -1)); 
		add(butDetails,  layMain.getConstraints(-2, -1));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void notifyVisible()
    {
        bDestroyed = false;

        if (eventNotifyVisible != null)
        {
        	eventNotifyVisible.dispatchEvent(this);
        }
    }

    /**
	 * {@inheritDoc}
	 */
	public void notifyDestroy()
	{
	    bDestroyed = true;
	    
	    if (eventNotifyDestroy != null)
	    {
	    	eventNotifyDestroy.dispatchEvent(this);
	    }
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean isDestroyed()
	{
	    return bDestroyed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <OP> void setOpener(OP pOpener)
	{
		opener = pOpener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <OP> OP getOpener()
	{
		return (OP)opener;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Adds an error to the list of errors.
	 * 
	 * @param pError the error
	 */
	public void addError(Throwable pError)
	{
		if (pError != null)
		{
			mdbError.setInsertEnabled(true);
			
			Throwable root = null;
			
			Throwable cause = pError;
			
			do
			{
				if (cause instanceof RootCauseSecurityException 
					|| cause instanceof RootCauseException)
				{
					root = cause;
				}
				
				cause = cause.getCause();
			}
			while (root == null && cause != null);
			
			cause = pError;

			try
			{
				String sMessage;
				String sError;
				
				do
				{
					sError = cause.getMessage();
					
					if (sError == null || sError.trim().length() == 0)
					{
						sError = cause.getClass().getName();
						sMessage = cause.getClass().getSimpleName();
					}
					else
					{
						sMessage = sError;
					}
					
					mdbError.insert(false);
					mdbError.setValues(new String[] {"ERROR", "DETAIL", "CAUSE"}, 
							           new Object[] {sError, ExceptionUtil.dump(cause, false), cause});
					
					cause = cause.getCause();
				}
				while (cause != null);
				
				mdbError.saveAllRows();

				if (root != null)
				{
					taMessage.setText(root.getMessage());
				}
				else
				{
					taMessage.setText(sMessage);
				}
			}
			catch (ModelException me)
			{
				//it's not possible to report the error to the ExceptionHandler,
				//because it produces an error!
				me.printStackTrace();
				debug(me);
			}
			
			mdbError.setInsertEnabled(false);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Updates the Layout anchors dependent of the visible details.
	 */
	protected void updateAnchors()
	{
		if (butDetails.isSelected())
		{
			add(taMessage,	 layMain.getConstraints(1, 0, -1, 0));
			add(butOK, 	     layMain.getConstraints(-1, 1)); 
			add(butDetails,  layMain.getConstraints(-2, 1));
			add(gpanDetails, layMain.getConstraints(0, 2, -1, -1));
		}
		else
		{
			remove(gpanDetails);
			add(taMessage,	 layMain.getConstraints(1, 0, -1, -2));
			add(butOK, 	     layMain.getConstraints(-1, -1)); 
			add(butDetails,  layMain.getConstraints(-2, -1));
		}
	}
	
	/**
	 * Gets the message text area.
	 *  
	 * @return the text area
	 */
	protected UITextArea getTextArea()
	{
		return taMessage;
	}
	
	/**
	 * Gets the OK button.
	 * 
	 * @return the button
	 */
	protected UIButton getOKButton()
	{
		return butOK;
	}
	
	/**
	 * Gets the details button.
	 * 
	 * @return the button
	 */
	protected UIToggleButton getDetailsButton()
	{
		return butDetails;
	}
	
	/**
	 * Gets the details group panel.
	 * 
	 * @return the group panel
	 */
	protected UIGroupPanel getDetailsGroupPanel()
	{
		return gpanDetails;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return the message
	 */
	public String getMessage()
	{
		return taMessage.getText();
	}
	
	/**
	 * Sets details option visibility.
	 * 
	 * @param pVisible <code>true</code> if visible, <code>false</code> otherwise
	 */
	public void setDetailsVisible(boolean pVisible)
	{
		butDetails.setVisible(pVisible);
	}
	
	/**
	 * Gets whether details option is visible.
	 * 
	 * @return <code>true</code> if visible, <code>false</code> otherwise
	 */
	public boolean isDetailsVisible()
	{
		return butDetails.isVisible();
	}
	
	/**
	 * Gets all available errors.
	 * 
	 * @return all reported errors
	 */
	public Throwable[] getErrors()
	{
		try
		{
			Throwable[] thErrors = new Throwable[mdbError.getRowCount()];
			
			for (int i = 0, anz = mdbError.getRowCount(); i < anz; i++)
			{
				thErrors[i] = (Throwable)mdbError.getDataRow(i).getValue("CAUSE");
			}
			
			return thErrors;
		}
		catch (ModelException me)
		{
			throw new RuntimeException(me);
		}
	}

	/**
	 * Gets all available error messages.
	 * 
	 * @return all reported errors
	 */
	public String[] getMessages()
	{
		try
		{
			String[] sErrors = new String[mdbError.getRowCount()];
			
			for (int i = 0, anz = mdbError.getRowCount(); i < anz; i++)
			{
				sErrors[i] = mdbError.getDataRow(i).getValueAsString("ERROR");
			}
			
			return sErrors;
		}
		catch (ModelException me)
		{
			throw new RuntimeException(me);
		}
	}
	
	/**
	 * Gets the "hide details" event handler.
	 * 
	 * @return the event handler
	 */
	public ErrorHandler eventHideDetails()
	{
		if (eventHideDetails == null)
		{
			eventHideDetails = new ErrorHandler("hideDetails");
		}
		
		return eventHideDetails;
	}
	
	/**
	 * Gets the "show details" event handler.
	 * 
	 * @return the event handler
	 */
	public ErrorHandler eventShowDetails()
	{
		if (eventShowDetails == null)
		{
			eventShowDetails = new ErrorHandler("showDetails");
		}
		
		return eventShowDetails;
	}
	
	/**
	 * Gets the event handler for {@link #notifyVisible()}.
	 * 
	 * @return the event handler
	 */
	public ContentHandler<INotifyVisibleListener> eventNotifyVisible()
	{
		if (eventNotifyVisible == null)
		{
			eventNotifyVisible = new ContentHandler<INotifyVisibleListener>(INotifyVisibleListener.class);
		}
		
		return eventNotifyVisible;
	}
	
	/**
	 * Gets the event handler for {@link #notifyDestroy()}.
	 * 
	 * @return the event handler
	 */
	public ContentHandler<INotifyDestroyListener> eventNotifyDestroy()
	{
		if (eventNotifyDestroy == null)
		{
			eventNotifyDestroy = new ContentHandler<INotifyDestroyListener>(INotifyDestroyListener.class);
		}
		
		return eventNotifyDestroy;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Actions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Closes the content.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doOk(UIActionEvent pEvent)
	{
		dispose();
	}

	/**
	 * Shows the details.
	 * 
	 * @param pEvent the triggering event
	 */
	public void doDetails(UIActionEvent pEvent)
	{
		if (butDetails.isVisible())
		{
			updateAnchors();
	
			pack();
			
			if (butDetails.isSelected())
			{
				if (eventShowDetails != null)
				{
					eventShowDetails.dispatchEvent(this);
				}
			}
			else
			{
				if (eventHideDetails != null)
				{
					eventHideDetails.dispatchEvent(this);
				}
			}
		}
	}
	
}	// Error
