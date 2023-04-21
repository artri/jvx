/*
 * Copyright 2013 SIB Visions GmbH
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
 * 01.10.2013 - [JR] - creation
 */
package com.sibvisions.rad.application;

import java.util.ArrayList;
import java.util.List;

import javax.rad.application.IContent;
import javax.rad.application.ILauncher;
import javax.rad.application.IWorkScreenApplication;
import javax.rad.application.genui.Content;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIColor;
import javax.rad.genui.UIImage;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.Style;
import javax.rad.ui.component.IButton;

import com.sibvisions.rad.application.event.DialogHandler;
import com.sibvisions.rad.application.event.type.ICancelDialogListener;
import com.sibvisions.rad.application.event.type.IOkDialogListener;

/**
 * The <code>Dialog</code> class is a dialog content that shows a component and allows
 * configuration of available buttons. The layout is always the same. There's the 
 * custom component in the center area and buttons in the south area.
 * 
 * @author René Jahn
 */
public class Dialog extends Content
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the possible button modes. */
	public enum ButtonMode
	{
		/** only OK button. */
		Ok,
		/** OK and Cancel buttons. */
		OkCancel,
		/** only Cancel button. */
		Cancel,
		/** only custom buttons, if set. */
		Custom
	};
	
	/** The enum for modality mode. */
	public enum Modality
	{
		/** modal mode. */
		Modal,
		/** not modal mode. */
		NotModal,
		/** use default modal mode. */
		Default
	};
	
	/** the used component. */
	private transient IComponent component;
	
	/** the internal cached application in case of manual show. */
	private transient IWorkScreenApplication application;
	
	/** the dialog layout. */
	private transient UIBorderLayout 	blThis 				= new UIBorderLayout(0, 0);
	/** the button area layout. */
	private transient UIFormLayout 		folButtons 			= new UIFormLayout();
	/** the left button area layout. */
	private transient UIFlowLayout 		floButtonsLeft 		= new UIFlowLayout();
	/** the center button area layout. */
	private transient UIFlowLayout 		floButtonsCenter 	= new UIFlowLayout();
	/** the right button area layout. */
	private transient UIFlowLayout 		floButtonsRight 	= new UIFlowLayout();
	
	/** the button area. */
	private transient UIPanel 			panButtons 			= new UIPanel(folButtons);
	/** the left button area. */
	private transient UIPanel 			panButtonsLeft 		= new UIPanel(floButtonsLeft);
	/** the center button area. */
	private transient UIPanel 			panButtonsCenter 	= new UIPanel(floButtonsCenter);
	/** the right button area. */
	private transient UIPanel 			panButtonsRight 	= new UIPanel(floButtonsRight);
	
	/** the default OK button. */
	private transient UIButton 			butOK 				= new UIButton("OK");
	/** the default Cancel button. */
	private transient UIButton 			butCancel 			= new UIButton("Cancel");

	/** the button line. */
	private transient UIIcon 			icoButtonLine;

	/** the button mode. */
	private transient ButtonMode 		buttonMode;

	/** the custom buttons for the left area. */
	private transient List<IButton> 	liButtonsLeft;
	/** the custom buttons for the center area. */
	private transient List<IButton> 	liButtonsCenter;
	/** the custom buttons for the right area. */
	private transient List<IButton> 	liButtonsRight;
	
	/** the "ok" event handler. */
	private transient DialogHandler<IOkDialogListener>     eventOk;
	/** the "cancel" event handler. */
	private transient DialogHandler<ICancelDialogListener> eventCancel;
	
	/** the optional title. */
	private transient String 			sTitle;

	/** whether or not the dialog is modal. */
	private transient boolean 			bModal = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Dialog</code> with the given component to show.
	 * 
	 * @param pComponent the dialog component
	 */
	public Dialog(IComponent pComponent)
	{
		component = pComponent;
		
		Style.addStyleNames(butOK, "ok", "f_text");
		butOK.setImage(UIImage.getImage(UIImage.OK_SMALL));
		butOK.eventAction().addListener(this, "doOk");
		butOK.setDefaultButton(true);
		butOK.setBackground(null);
		
		Style.addStyleNames(butCancel, "cancel", "f_text");
		butCancel.eventAction().addListener(this, "doCancel");
		butCancel.setFocusable(false);
		butCancel.setBackground(null);
		
		folButtons.setMargins(12, 8, 8, 8);
		folButtons.setResponsive(Boolean.FALSE);
		
		floButtonsLeft.setMargins(0, 0, 0, 0);
		floButtonsCenter.setMargins(0, 0, 0, 0);
		floButtonsRight.setMargins(0, 0, 0, 0);
		
		blThis.setMargins(0, 0, 0, 0);
		
		setLayout(blThis);

		panButtonsLeft.setBackground(null);
		panButtonsCenter.setBackground(null);
		panButtonsRight.setBackground(null);
		
		panButtons.setBackground(UIColor.white);
		
		icoButtonLine = new UIIcon();
		icoButtonLine.setBackground(UIColor.gray);
		
		panButtons.add(icoButtonLine, folButtons.getConstraints(folButtons.getTopAnchor(),
													  			folButtons.getLeftAnchor(),
													  			folButtons.createAnchor(folButtons.getTopAnchor(), 1),
													  			folButtons.getRightAnchor()));

		panButtons.add(panButtonsLeft, folButtons.getLeftAlignedConstraints(0, 0));
		panButtons.add(panButtonsCenter, folButtons.getHCenterConstraints(0, 0, -1, 0));
		panButtons.add(panButtonsRight, folButtons.getRightAlignedConstraints(-1, 0));
		
		add(component, UIBorderLayout.CENTER);
		add(panButtons, UIBorderLayout.SOUTH);
		
		setButtonMode(ButtonMode.Ok);
		
		setPreferredSize(300, 150);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <OP> void setOpener(OP pOpener)
	{
		if (component instanceof IContent)
		{
			((IContent)component).setOpener(pOpener);
		}
		
		super.setOpener(pOpener);
		
		if (pOpener instanceof IComponent)
		{
			ILauncher launcher = ApplicationUtil.getLauncher((IComponent)pOpener);
			
			if (launcher instanceof UILauncher)
			{
				if (((UILauncher)launcher).isMobileEnvironment())
				{
					folButtons.setMargins(0, 5, 5, 5);
					
					panButtons.remove(icoButtonLine);
					
					panButtons.setBackground(null);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyVisible()
	{
		if (component instanceof IContent)
		{
			((IContent)component).notifyVisible();
		}
		
		super.notifyVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDestroy()
	{
		if (component instanceof IContent)
		{
			((IContent)component).notifyDestroy();
		}
		
		super.notifyDestroy();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the component.
	 * 
	 * @return the component
	 */
	public IComponent getComponent()
	{
		return component;
	}
	
    /**
     * Sets the component.
     * 
     * @param pComponent the component
     */
    public void setComponent(IComponent pComponent)
    {
        if (component != pComponent)
        {
            if (isNotified() && component instanceof IContent)
            {
                ((IContent)component).notifyDestroy();
            }
            
            if (component != null)
            {
                remove(component);
            }
            
            component = pComponent;
            
            if (component != null)
            {
                add(component, UIBorderLayout.CENTER, 0);
            }
            
            if (isNotified() && component instanceof IContent)
            {
                ((IContent)component).setOpener(getOpener());

                ((IContent)component).notifyVisible();
            }
        }
    }
    
	/**
	 * Sets the button mode.
	 * 
	 * @param pMode the mode
	 */
	public void setButtonMode(ButtonMode pMode)
	{
		buttonMode = pMode;
		
		configureButtons();
	}

	/**
	 * Gets the current button mode.
	 * 
	 * @return the mode
	 */
	public ButtonMode getButtonMode()
	{
		return buttonMode;
	}
	
	/**
	 * Invoked if cancel was pressed or window is destroyed.
	 */
	public void doCancel()
	{
		if (eventCancel != null)
		{
			eventCancel().dispatchEvent(this);
		}
	}

	/**
	 * Invoked if ok was pressed.
	 */
	public void doOk()
	{
		if (eventOk != null)
		{
			eventOk.dispatchEvent(this);
		}
	}
	
	/**
	 * Shows the Buttons dependent of the button mode.
	 * 
	 * @see #setButtonMode(ButtonMode)
	 */
	protected void configureButtons()
	{
		panButtonsLeft.removeAll();
		panButtonsCenter.removeAll();
		panButtonsRight.removeAll();
		
		switch(buttonMode)
		{
			case Ok:
				panButtonsCenter.add(butOK);
				
				panButtonsCenter.setVisible(true);
				panButtonsLeft.setVisible(false);
				panButtonsRight.setVisible(false);
				
				panButtons.setVisible(true);
				break;
			case OkCancel:
				panButtonsLeft.add(butCancel);
				panButtonsRight.add(butOK);
				
				panButtonsCenter.setVisible(false);
				panButtonsLeft.setVisible(true);
				panButtonsRight.setVisible(true);
				
				panButtons.setVisible(true);
				break;
			case Cancel:
				panButtonsCenter.add(butCancel);
				
				panButtonsCenter.setVisible(true);
				panButtonsLeft.setVisible(false);
				panButtonsRight.setVisible(false);

				panButtons.setVisible(true);
				break;
			default:
				int iCount = 0;
				
				if (liButtonsLeft != null)
				{
					for (int i = 0, anz = liButtonsLeft.size(); i < anz; i++, iCount++)
					{
						panButtonsLeft.add(liButtonsLeft.get(i));
					}
				}
				
				if (liButtonsCenter != null)
				{
					for (int i = 0, anz = liButtonsCenter.size(); i < anz; i++, iCount++)
					{
						panButtonsCenter.add(liButtonsCenter.get(i));
					}
				}

				if (liButtonsRight != null)
				{
					for (int i = 0, anz = liButtonsRight.size(); i < anz; i++, iCount++)
					{
						panButtonsRight.add(liButtonsRight.get(i));
					}
				}
				
				panButtons.setVisible(iCount > 0);
		}
	}

	/**
	 * Adds a button to the left area.
	 * 
	 * @param pButton the button
	 */
	public void addLeftButton(IButton pButton)
	{
		removeButton(pButton);
		
		if (liButtonsLeft == null)
		{
			liButtonsLeft = new ArrayList<IButton>();
		}
		
		liButtonsLeft.add(pButton);
		
		Style.addStyleNames(pButton, "custom", "f_text");

		pButton.setBackground(null);
		
		configureButtons();
	}
	
	/**
	 * Adds a button to the center area.
	 * 
	 * @param pButton the button
	 */
	public void addCenterButton(IButton pButton)
	{
		removeButton(pButton);

		if (liButtonsCenter == null)
		{
			liButtonsCenter = new ArrayList<IButton>();
		}
		
		liButtonsCenter.add(pButton);
		
		Style.addStyleNames(pButton, "custom", "f_text");
		
		pButton.setBackground(null);
		
        configureButtons();
	}

	/**
	 * Adds a button to the right area.
	 * 
	 * @param pButton the button
	 */
	public void addRightButton(IButton pButton)
	{
		removeButton(pButton);

		if (liButtonsRight == null)
		{
			liButtonsRight = new ArrayList<IButton>();
		}
		
		liButtonsRight.add(pButton);
		
		Style.addStyleNames(pButton, "custom", "f_text");
		
		pButton.setBackground(null);
		
        configureButtons();
	}
	
	/**
	 * Removes a button.
	 * 
	 * @param pButton the button
	 */
	public void removeButton(IButton pButton)
	{
		if (liButtonsLeft != null)
		{
			liButtonsLeft.remove(pButton);
			
			if (liButtonsLeft.isEmpty())
			{
				liButtonsLeft = null;
			}
		}

		if (liButtonsCenter != null)
		{
			liButtonsCenter.remove(pButton);

			if (liButtonsCenter.isEmpty())
			{
				liButtonsCenter = null;
			}
		}
		
		if (liButtonsRight != null)
		{
			liButtonsRight.remove(pButton);
			
			if (liButtonsRight.isEmpty())
			{
				liButtonsRight = null;
			}
		}
		
		IContainer conParent = pButton.getParent();
		
		if (conParent != null)
		{
			conParent.remove(pButton);
		}
		
		Style.removeStyleNames(pButton, "custom", "f_text");
	}

	/**
	 * Gets the handler for ok event.
	 * 
	 * @return the event handler
	 */
	public DialogHandler<IOkDialogListener> eventOk()
	{
		if (eventOk == null)
		{
			eventOk = new DialogHandler<IOkDialogListener>(IOkDialogListener.class);
		}
		
		return eventOk;
	}
	
	/**
	 * Gets the handler for cancel event.
	 * 
	 * @return the event handler
	 */
	public DialogHandler<ICancelDialogListener> eventCancel()
	{
		if (eventCancel == null)
		{
			eventCancel = new DialogHandler<ICancelDialogListener>(ICancelDialogListener.class);
		}
		
		return eventCancel;
	}
	
	/**
	 * Opens the dialog as internal frame.
	 * 
	 * @param pApplication the application
	 * @param pTitle the title
	 * @param pModal <code>true</code> if the frame sould be modal
	 * @param pContent the content to show
	 * @return the opened frame
	 */
	public static UIInternalFrame openInternalFrame(Application pApplication, String pTitle, boolean pModal, IComponent pContent)
	{
		if (pContent instanceof Content)
		{
			((Content)pContent).setOpener(pApplication);
		}
		
		UIInternalFrame ifrDialog = new UIInternalFrame(pApplication.getDesktopPane());
		ifrDialog.setResizable(true);
		ifrDialog.setTitle(pTitle != null ? pTitle : pContent.getName());
		
		ifrDialog.add(pContent);
		ifrDialog.eventWindowClosing().addListener(ifrDialog, "dispose");
		ifrDialog.setIconImage(null);
		ifrDialog.setMaximizable(false);
		ifrDialog.setIconifiable(false);
		ifrDialog.setModal(pModal);

		if (pContent instanceof Content)
		{
			ifrDialog.eventWindowClosing().addListener((Content)pContent, "notifyDestroy", 0);

			if (pContent instanceof Dialog)
			{
				ifrDialog.eventWindowClosing().addListener((Dialog)pContent, "doCancel", 0);

				((Dialog)pContent).eventOk().addListener((Dialog)pContent, "notifyDestroy");
				((Dialog)pContent).eventOk().addListener(ifrDialog, "dispose");

				((Dialog)pContent).eventCancel().addListener((Dialog)pContent, "notifyDestroy");
				((Dialog)pContent).eventCancel().addListener(ifrDialog, "dispose");
			}
		}
		
		pApplication.configureFrame(ifrDialog);
		
		if (pContent.isPreferredSizeSet())
		{
			ifrDialog.setPreferredSize(pContent.getPreferredSize());
		}
		
		ifrDialog.pack();
		ifrDialog.centerRelativeTo(pApplication.getDesktopPane());
		
		if (pContent instanceof Content)
		{
			((Content)pContent).notifyVisible();
		}
		
		ifrDialog.setVisible(true);
		
		return ifrDialog;
	}
	
	/**
	 * Opens the dialog.
	 * 
	 * @param pApplication the associated application
	 * @throws Throwable if it fails
	 */
   	public void open(IWorkScreenApplication pApplication) throws Throwable
   	{
   		if (isNotified())
   		{
   			close();
   		}
   		
    	pApplication.openContent((IComponent)getOpener(), sTitle != null ? sTitle : "", bModal, this);

    	application = pApplication;
   	}
   	
   	/**
   	 * Closes this dialog.
   	 */
   	public void close()
   	{
   		if (application != null)
   		{
   			application.close(this);
   		}
   		else
   		{
   			ApplicationUtil.getApplication(this).close(this);
   		}
   	}

   	/**
   	 * Sets the title.
   	 * 
   	 * @param pTitle the title
   	 */
   	public void setTitle(String pTitle)
   	{
   		sTitle = pTitle;
   	}
   	
   	/**
   	 * Gets the title.
   	 * 
   	 * @return the title
   	 */
   	public String getTitle()
   	{
   		return sTitle;
   	}
   	
   	/**
   	 * Sets the dialog modal.
   	 * 
   	 * @param pModal <code>true</code> if modal, <code>false</code> otherwise.
   	 */
   	public void setModal(boolean pModal)
   	{
   		bModal = pModal;
   	}
   	
   	/**
   	 * Returns if the dialog is modal.
   	 * 
   	 * @return <code>true</code> if modal, <code>false</code> otherwise.
   	 */
   	public boolean isModal()
   	{
   		return bModal;
   	}
	
	/**
	 * Gets the default OK button.
	 * 
	 * @return the OK button
	 */
	public IButton getOkButton()
	{
		return butOK;
	}
	
	/**
	 * Gets the default Cancel button.
	 * 
	 * @return the Cancel button
	 */
	public IButton getCancelButton()
	{
		return butCancel;
	}
	
	/**
	 * Gets the panel that contains all button panels.
	 * 
	 * @return the panel
	 */
	protected UIPanel getButtonPanel()
	{
		return panButtons;
	}
	
	/**
	 * Gets the panel that contains all "left" buttons.
	 * 
	 * @return the panel
	 */
	protected UIPanel getLeftButtonPanel()
	{
		return panButtonsLeft;
	}

	/**
	 * Gets the panel that contains all "center" buttons.
	 * 
	 * @return the panel
	 */
	protected UIPanel getCenterButtonPanel()
	{
		return panButtonsCenter;
	}
	
	/**
	 * Gets the panel that contains all "right" buttons.
	 * 
	 * @return the panel
	 */
	protected UIPanel getRightButtonPanel()
	{
		return panButtonsRight;
	}

}	//Dialog
