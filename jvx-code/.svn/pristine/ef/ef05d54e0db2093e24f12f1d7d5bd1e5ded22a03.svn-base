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
 * 06.10.2008 - [JR] - creation
 * 06.11.2008 - [JR] - extends WorkScreenBase instead of AbstractApplicationBase
 * 22.02.2009 - [JR] - doClose implemented
 * 04.08.2009 - [JR] - QUESTION icon supported
 * 12.05.2010 - [JR] - setEventHandler
 * 04.08.2011 - [JR] - setFont for TextArea because the default font is monospaced
 * 28.04.2016 - [JR] - #1600: use margin anchors for message
 * 11.08.2017 - [JR] - get methods for buttons
 * 21.07.2020 - [JR] - #2329: notifyVisible, notifyDestroy events               
 */
package com.sibvisions.rad.application;

import javax.rad.application.IContent;
import javax.rad.application.IMessageConstants;
import javax.rad.application.genui.event.ContentHandler;
import javax.rad.application.genui.event.type.content.INotifyDestroyListener;
import javax.rad.application.genui.event.type.content.INotifyVisibleListener;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.Style;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIEvent;
import javax.rad.ui.event.UIWindowEvent;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Message</code> class is an {@link UIInternalFrame} to
 * display messages on the screen. The message contains an icon, 
 * the message and message dependent buttons.
 *  
 * @author René Jahn
 */
public class Message extends UIInternalFrame
                     implements IContent,
                                IMessageConstants
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the message opener. */
    private transient Object		 opener;

    /** the internal eventhandler for eventdispatching. */
    private static ActionHandler 	 eventMessage = new ActionHandler();

	/** the "notify visible" event. */
	private transient ContentHandler<INotifyVisibleListener> eventNotifyVisible;
	/** the "notify destroy" event. */
	private transient ContentHandler<INotifyDestroyListener> eventNotifyDestroy;
	
	/** the ok listener. */
    private transient IActionListener alOk;

	/** the not ok listener. */
    private transient IActionListener alNotOk;

    /** the cancel listener. */
    private transient IActionListener alCancel;
    
	/** the center layout. */
	private transient UIFormLayout    flCenter 	  = new UIFormLayout();
	
	/** the center panel. */
	private transient UIPanel 		  panCenter   = new UIPanel();
	
    /** the area for displaying the message. */
    private transient UITextArea      taMessage   = new UITextArea();
    
    /** the label for displaying the message. */
    private transient UILabel	      lblMessage  = new UILabel();

    /** the ok/yes button. */
    private transient UIButton        butOK       = null;
    
    /** the no button. */
    private transient UIButton        butNotOK    = null;

    /** the cancel/no button. */
    private transient UIButton        butCancel   = null;
    
	/** the text/message. */
	private transient String          sText 	  = null;

	/** the ok method name. */
    private transient String          sOkAction;

	/** the not ok method name. */
    private transient String          sNotOkAction;

    /** the cancel method name. */
    private transient String          sCancelAction;

    /** the message type. */
    private transient int             iIconType;
    
    /** the button display type. */
    private transient int             iButtonType;
    
    /** whether the content is already destroyed. */
    private transient boolean 		  bDestroyed;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage)
    {
    	this(pDesktop, pIconType, pButtonType, pMessage, (String)null, (String)null, (String)null);
    }

    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOkAction the action to call when ok or yes was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage,
                   String pOkAction)
    {
    	this(pDesktop, pIconType, pButtonType, pMessage, pOkAction, null, null);
    }
    
    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOkAction the action to call when ok or yes was clicked
     * @param pCancelAction the action to call when cancel or close was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage, 
                   String pOkAction, 
                   String pCancelAction)
    {
    	this(pDesktop, pIconType, pButtonType, pMessage, pOkAction, null, pCancelAction);
    }

    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOk the listener to notify when ok or yes was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage, 
                   IActionListener pOk)
    {
    	this(pDesktop, pIconType, pButtonType, pMessage, pOk, null, null);
    }
    
    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOk the listener to notify when ok or yes was clicked
     * @param pCancel the listener to notiry when cancel or close was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage, 
                   IActionListener pOk, 
                   IActionListener pCancel)
    {
    	this(pDesktop, pIconType, pButtonType, pMessage, pOk, null, pCancel);
    }
    
    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOkAction the action to call when ok or yes was clicked
     * @param pNotOkAction the action to call when not ok or no was clicked
     * @param pCancelAction the action to call when cancel or close was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage, 
                   String pOkAction,
                   String pNotOkAction,
                   String pCancelAction)
    {
        super(pDesktop);

        iIconType = pIconType;
        iButtonType = pButtonType;

        sOkAction = pOkAction;
        sNotOkAction = pNotOkAction;
        sCancelAction = pCancelAction;
        
        taMessage.setFont(UIFont.getDefaultFont());
        taMessage.setRows(0);
        taMessage.setColumns(0);
        
        init();
        
        //after init
        setText(pMessage);
    }
    
    /**
     * Creates a new instance of <code>Message</code>.
     * 
     * @param pDesktop the desktop for showing the frame
     * @param pIconType the message type
     * @param pButtonType the type for the visible buttons
     * @param pMessage the message
     * @param pOk the listener to notify when ok or yes was clicked
     * @param pNotOk the listener to notify when not ok or no was clicked
     * @param pCancel the listener to notify when cancel or close was clicked
     */
    public Message(UIDesktopPanel pDesktop, 
                   int pIconType, 
                   int pButtonType, 
                   String pMessage, 
                   IActionListener pOk,
                   IActionListener pNotOk,
                   IActionListener pCancel)
    {
        super(pDesktop);

        iIconType = pIconType;
        iButtonType = pButtonType;

        alOk = pOk;
        alNotOk = pNotOk;
        alCancel = pCancel;
        
        taMessage.setFont(UIFont.getDefaultFont());
        taMessage.setRows(0);
        taMessage.setColumns(0);
        
        init();
        
        //after init
        setText(pMessage);
    }
    
    /**
     * Initializes the UI components.
     */
    protected void init()
    {
        UIIcon icon = null;
        
        UIFlowLayout fllButtons = new UIFlowLayout();
        fllButtons.setMargins(8, 0, 8, 0);
        fllButtons.setComponentAlignment(IAlignmentConstants.ALIGN_STRETCH);
        
        UIPanel panButtons = new UIPanel(fllButtons);
        Style.addStyleNames(panButtons, "msgbuttons");
        
        flCenter.setHorizontalGap(10);
        
        panCenter.setLayout(flCenter);
        panCenter.setBackground(UIColor.white);
        
        Style.addStyleNames(panCenter, "msgdetails");
        
        //Handle Message Type
        
        String sName;
        String sStyle;

        UIImage image = null;
        
        switch (iIconType)
        {
            case MESSAGE_ICON_INFO:
            	sStyle = "infomsg";
                sName = "Information";
                image = UIImage.getImage(UIImage.MESSAGE_INFO_LARGE);
                break;
            case MESSAGE_ICON_WARNING:
            	sStyle = "warnmsg";
                sName = "Warning";
                image = UIImage.getImage(UIImage.MESSAGE_WARNING_LARGE);
                break;
            case MESSAGE_ICON_ERROR:
            	sStyle = "errormsg";
                sName = "Error";
                image = UIImage.getImage(UIImage.MESSAGE_ERROR_LARGE);
                break;
            case MESSAGE_ICON_QUESTION:
            	sStyle = "questionmsg";
                sName = "Question";
                image = UIImage.getImage(UIImage.MESSAGE_QUESTION_LARGE);
                break;
            case MESSAGE_ICON_NONE:
            	sStyle = "nonemsg";
            	sName = "";
            	lblMessage.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
            	taMessage.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
                break;
            default:
            	sStyle = "defaultmsg";
                sName = "";
                break;
        }
        
        if (image != null)
        {
            icon = new UIIcon();
            icon.setImage(image);
            icon.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
            
            Style.addStyleNames(icon, "msgicon");
            
            panCenter.add(icon);
        }
        
        setTitle(sName);
        setIconImage(null);
        setMaximizable(false);
        setIconifiable(false);
        setResizable(false);
        setModal(true); 
        
        Style.addStyleNames(this, sStyle);
        
        //Message
        
        taMessage.setEditable(false);
        taMessage.setBackground(UIColor.white);
        taMessage.setBorderVisible(false);
        taMessage.setWordWrap(true);
        
        Style.addStyleNames(taMessage, "msgtext");
        
        UIIcon icoLine = new UIIcon();
        icoLine.setBackground(UIColor.gray);
        
        Style.addStyleNames(icoLine, "msgline");
        
        panCenter.add(icoLine, flCenter.getConstraints(flCenter.createAnchor(flCenter.getBottomAnchor(), -1),
                                                       flCenter.getLeftAnchor(),
                                                       flCenter.getBottomAnchor(),
                                                       flCenter.getRightAnchor()));

        //Create Buttons
        
        switch (iButtonType)
        {
            case IMessageConstants.MESSAGE_BUTTON_OK:
                butOK     = createButton("OK", "doOk");
                butOK.setDefaultButton(true);

                Style.addStyleNames(butOK, "msgbutok");
                break;
            case IMessageConstants.MESSAGE_BUTTON_OK_CANCEL:
                butOK     = createButton("OK", "doOk");
                
                Style.addStyleNames(butOK, "msgbutok");

                butCancel = createButton("Cancel", "doCancel");
                butCancel.setDefaultButton(true);

                Style.addStyleNames(butCancel, "msgbutcancel");

                break;
            case IMessageConstants.MESSAGE_BUTTON_YES_NO:
                butOK     = createButton("Yes", "doOk");
                
                Style.addStyleNames(butOK, "msgbutyes");

                butCancel = createButton("No", "doCancel");
                butCancel.setDefaultButton(true);
                
                Style.addStyleNames(butCancel, "msgbutno");
                break;
            case IMessageConstants.MESSAGE_BUTTON_YES_NO_CANCEL:
                butOK     = createButton("Yes", "doOk");
                
                Style.addStyleNames(butOK, "msgbutyes");

                butNotOK  = createButton("No", "doNotOk");
                
                Style.addStyleNames(butNotOK, "msgbutno");

                butCancel = createButton("Cancel", "doCancel");
                butCancel.setDefaultButton(true);
                
                Style.addStyleNames(butCancel, "msgbutcancel");

                break;
            case IMessageConstants.MESSAGE_BUTTON_NONE:
            	//no buttons
            default:
                //do nothing
                break;
        }
        
        if (butOK != null)
        {
            panButtons.add(butOK);
        }
        
        if (butNotOK != null)
        {
            panButtons.add(butNotOK);
        }
        
        if (butCancel != null)
        {
            panButtons.add(butCancel);
        }
        
        //add Components
        
        UIBorderLayout blMain = new UIBorderLayout();
        
        setLayout(blMain);
        
        add(panCenter, UIBorderLayout.CENTER);
        
        if (panButtons.getComponentCount() > 0)
        {
            blMain.setVerticalGap(1);
        	add(panButtons, UIBorderLayout.SOUTH);
        }
        else
        {
        	icoLine.setVisible(false);
        }
        
        eventWindowClosing().addListener(this, "doClose");

        panCenter.setMinimumSize(250, 50);
        panCenter.setMaximumSize(800, 600);
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
     * Creates an action button for this message.
     * 
     * @param pText the button text
     * @param pAction the re-dispatch action
     * @return the created button
     */
    protected UIButton createButton(String pText, String pAction)
    {
        UIButton button = new UIButton();
        
        button.setText(pText);
        button.eventAction().addListener(this, pAction);
        button.setPreferredSize(new UIDimension(80, 25));
        
        return button;
    }
    
    /**
     * Calls an action from the the opener if the opener is known.
     * 
     * @param pEvent the source event
     * @param pAction the specified action name
     * @throws Throwable if the call throws an error
     */
    private void callAction(UIEvent pEvent, String pAction) throws Throwable
    {
        if (pAction != null && opener != null)
        {
            IActionListener listener = eventMessage.createListener(opener, pAction);
            
            listener.action(new UIActionEvent(this,
                                              UIActionEvent.ACTION_PERFORMED,
                                              pEvent.getWhen(),
                                              pEvent.getModifiers(),
                                              null));
        }
    }
    
    /**
     * Sends an action notification to the given listener.
     * 
     * @param pEvent the source event
     * @param pListener the listener
     * @throws Throwable if the call throws an error
     */
    private void callAction(UIEvent pEvent, IActionListener pListener) throws Throwable
    {
    	if (pListener != null)
    	{        
    		pListener.action(new UIActionEvent(this,
	                                           UIActionEvent.ACTION_PERFORMED,
	                                           pEvent.getWhen(),
	                                           pEvent.getModifiers(),
	                                           null));
    	}
    }
    
    /**
     * Gets the OK button.
     * 
     * @return the button
     */
    public UIButton getOKButton()
    {
        return butOK;
    }
    
    /**
     * Gets the not OK button.
     * 
     * @return the button
     */
    public UIButton getNotOKButton()
    {
        return butNotOK;
    }
    
    /**
     * Gets the cancel button.
     * 
     * @return the button
     */
    public UIButton getCancelButton()
    {
        return butCancel;
    }
    
    /**
     * Gets the message area.
     * 
     * @return the message area
     */
    protected UITextArea getMessageArea()
    {
    	return taMessage;
    }
    
    /**
     * Gets the message label.
     * 
     * @return the message label
     */
    protected UILabel getMessageLabel()
    {
    	return lblMessage;
    }
    
    /**
     * Gets the message component.
     * 
     * @return the component
     */
    public ILabel getMessageComponent()
    {
    	if (taMessage.isVisible())
    	{
    		return taMessage;
    	}
    	else
    	{
    		return lblMessage;
    	}
    }
    
    /**
     * Gets the icon type.
     * 
     * @return the icon type
     */
    public int getIconType()
    {
    	return iIconType;
    }
    
    /**
     * Gets the button type.
     * 
     * @return the button type
     */
    public int getButtonType()
    {
    	return iButtonType;
    }
    
    /**
     * Gets the message text.
     * 
     * @return the text
     */
    public String getText()
    {
    	return sText;
    }
    
    /**
     * Sets the message text.
     * 
     * @param pText the text
     */
    public void setText(String pText)
    {
    	sText = pText;
    	
    	taMessage.setText(pText);
    	lblMessage.setText(pText);

    	addMessageComponent();
    	
    	boolean bHtml = sText != null && sText.startsWith("<html>");
    	
    	taMessage.setVisible(!bHtml);
    	lblMessage.setVisible(bHtml);
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
	
	/**
	 * Adds the message component(s).
	 */
	protected void addMessageComponent()
	{
		//centered looks better for "short" messages (#1600)
        if (StringUtil.countCharacter(sText, '\n') < 1)
        {
            panCenter.add(taMessage, flCenter.getVCenterConstraints(1,  0, -1, -1));
        }
        else
        {
            panCenter.add(taMessage, flCenter.getConstraints(1, 0, -1, -1));
        }
        
        panCenter.add(lblMessage, flCenter.getConstraints(1, 0, -1, -1));
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Actions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Closes the message when the OK/Yes button was clicked and re-dispatches
     * the event to the {@link #getOpener()}, if an action is defined for the 
     * OK/Yes button.
     * 
     * @param pEvent the event from the button
     * @throws Throwable if the an action is configured but the call throws an error
     */
    public void doOk(UIActionEvent pEvent) throws Throwable
    {
        dispose();
        
        if (alOk != null)
        {
        	callAction(pEvent, alOk);
        }
        else
        {
        	callAction(pEvent, sOkAction);
        }
    }

    /**
     * Closes the message when the not OK button was clicked and re-dispatches
     * the event to the {@link #getOpener()}, if an action is defined for the 
     * not OK button.
     * 
     * @param pEvent the event from the button
     * @throws Throwable if the an action is configured but the call throws an error
     */
    public void doNotOk(UIActionEvent pEvent) throws Throwable
    {
        dispose();
        
        if (alNotOk != null)
        {
        	callAction(pEvent, alNotOk);
        }
        else
        {
        	callAction(pEvent, sNotOkAction);
        }
    }
    
    /**
     * Closes the message when the Cancel/No button was clicked and re-dispatches
     * the event to the {@link #getOpener()}, if an action is defined for the 
     * Cancel/No button.
     * 
     * @param pEvent the event from the button
     * @throws Throwable if the a cancel action is configured but the call throws an error
     */
    public void doCancel(UIActionEvent pEvent) throws Throwable
    {
        dispose();

        if (alCancel != null)
        {
        	callAction(pEvent, alCancel);
        }
        else
        {
        	callAction(pEvent, sCancelAction);
        }
    }
    
    /**
     * Closes the message when the x button in the title was clicked. If a cancel
     * action was defined, it will be called.
     * 
     * @param pEvent the event from the frame
     * @throws Throwable if the a cancel action is configured but the call throws an error
     */
    public void doClose(UIWindowEvent pEvent) throws Throwable
    {
        dispose();
        
        if (alCancel != null)
        {
        	callAction(pEvent, alCancel);
        }
        else
        {
        	callAction(pEvent, sCancelAction);
        }
    }
    
}   // Message
