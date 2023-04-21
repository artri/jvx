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
 * 16.11.2008 - [HM] - creation
 * 25.11.2008 - [JR] - dispatchEvent: fixed NullPointerException
 * 13.12.2008 - [JR] - dispatchEvent: throwed throwable during method call [BUGFIX]
 * 17.02.2009 - [JR] - setBusy, callIntern implemented
 * 03.04.2009 - [JR] - dispatchCall: always use the eventDispatcher from the component instead
 *                     of the component itself (normally the same)
 * 31.12.2010 - [JR] - translate: removed isNotified() check -> if we have a translation then use it
 * 18.03.2011 - [JR] - #313: component moved/resized implemented
 * 31.03.2011 - [JR] - #161: ITranslatable interface added
 * 14.05.2011 - [JR] - updateTranslation: parent instance check - otherwise StackOverflow
 * 27.05.2011 - [JR] - #372: 
 *                     * updateTranslation checks translation loop
 *                     * setTranslation unsets translation map parent if it was changed from the component
 * 04.10.2011 - [JR] - #477: beforeAddNotify implemented    
 * 02.11.2011 - [JR] - #493: en/disable translation
 *                   - #494: don't create an automatic translation chain
 * 04.04.2014 - [RZ] - #1: added eventKey(Key) which allows listening for a certain key
 * 05.04.2014 - [JR] - #1001: don't change text if translation is disabled
 * 23.06.2016 - [JR] - invokeInThread now returns the Thread instance     
 * 18.07.2019 - [JR] - #2041: keep event source 
 * 18.03.2020 - [JR] - #2238: use componentUIResource instead of uiResource           
 */
package javax.rad.genui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.rad.model.ui.ITranslatable;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;
import javax.rad.ui.Style;
import javax.rad.ui.event.ComponentHandler;
import javax.rad.ui.event.FocusHandler;
import javax.rad.ui.event.IKeyListener;
import javax.rad.ui.event.Key;
import javax.rad.ui.event.KeyHandler;
import javax.rad.ui.event.MouseHandler;
import javax.rad.ui.event.UIKeyEvent;
import javax.rad.ui.event.UIMouseEvent;
import javax.rad.ui.event.type.component.IComponentMovedListener;
import javax.rad.ui.event.type.component.IComponentResizedListener;
import javax.rad.ui.event.type.focus.IFocusGainedListener;
import javax.rad.ui.event.type.focus.IFocusLostListener;
import javax.rad.ui.event.type.key.IKeyPressedListener;
import javax.rad.ui.event.type.key.IKeyReleasedListener;
import javax.rad.ui.event.type.key.IKeyTypedListener;
import javax.rad.ui.event.type.mouse.IMouseClickedListener;
import javax.rad.ui.event.type.mouse.IMouseEnteredListener;
import javax.rad.ui.event.type.mouse.IMouseExitedListener;
import javax.rad.ui.event.type.mouse.IMousePressedListener;
import javax.rad.ui.event.type.mouse.IMouseReleasedListener;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.util.EventHandler;
import javax.rad.util.ITranslator;
import javax.rad.util.TranslationMap;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.TextType;

/**
 * Platform and technology independent component.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * 
 * @author Martin Handsteiner
 * 
 * @param <C> instance of IComponent
 */
public abstract class UIComponent<C extends IComponent> extends UIResource<C> 
                                                        implements IComponent,
                                                                   ITranslatable,
                                                                   ITranslator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** This EventHandler is used to create dynamic Runnable interfaces. */
	private static EventHandler<Runnable> runnableEventHandler = new EventHandler<Runnable>(Runnable.class);

	/** the component logger. */
	private transient ILogger logger = null;

	/** the UIParent of this UIComponent. */
	protected transient IContainer parent = null;
	
	/** the UIParent of this UICompoennt. */
	protected transient IPopupMenu popupMenu = null;
	
	/** the expected parent for beforeAddNotify. */
	protected transient IComponent parentExpected = null;

	/** the translation map for this component. */
	private transient TranslationMap tmpUserdefined = null;
	
	/** the reference to a translation map of a parent. */
	private transient TranslationMap tmpCurrent = null;
	
    /** the map which maps {@link Key}s to {@link KeyHandler}s. */
    private transient Map<Key, KeyHandler<? super IKeyListener>> mpEventKeys;
    
	/** The component thats uiResource will be added to the parent. */
	private transient UIComponent<?> uiComponent = this;
	/** The original ui resource. */
	private transient IComponent componentUIResource;
	
	/** The location offset due to complex parent. */
	private transient IPoint locationOffset = null;

    /** the tooltip. */
	private transient String sToolTip = null;
	
    /** The name explicit set for this {@link UIComponent}. */
    private transient String sName = null;
    
    /** The default/generated name for this {@link UIComponent}. */
    private transient String sDefaultName = null;
    
    /** The root name explicitly set. */
    private transient String sRootName = null;
	
    /** The current default name. */
    private transient String sCurDefaultName = null;
	
    /** The current root name. */
    private transient String sCurRootName = null;
	
    /** The already existing names under the root / if sRootName is null, this is the same as parents. */
    private transient Set<String> stExistingNames = null;

	/** the time of the last translation modification. */
	protected transient long lLastTranslationModified = -1;
	
	/** the flag indicates whether the component was "before" notified. */
	private transient boolean bBeforeNotified = false;
	
	/** the flag indicates whether the component was notified. */
	private transient boolean bNotified = false;
	
	/** whether translate should translate texts. */
	protected transient boolean bTranslate = true;
	
	/** whether this component is enabled. */
	private transient boolean bEnabled = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>UIComponent</code>.
     *
     * @param pComponent the Component.
     * @see IComponent
     */
	protected UIComponent(C pComponent)
	{
		super(pComponent);
		
        pComponent.setEventSource(this);

        if (pComponent instanceof UIComponent)
        {
            uiComponent = (UIComponent)pComponent;
            componentUIResource = uiComponent.componentUIResource;
            
        }
        else
        {
            componentUIResource = pComponent;
        }
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
    {
		if (sName == null)
		{
			return componentUIResource.getName();
		}
		else
		{
			return sName;
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public void setName(String pName)
    {
		sName = pName;
		
		componentUIResource.setName(pName);
    }

	/**
	 * {@inheritDoc}
	 */
	public IFactory getFactory()
    {
    	return uiResource.getFactory();
    }

	/**
	 * {@inheritDoc}
	 */
	public IDimension getPreferredSize()
    {
    	return getComponentUIResource().getPreferredSize();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setPreferredSize(IDimension pPreferredSize)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pPreferredSize instanceof UIDimension)
		{
		    pPreferredSize = ((UIDimension)pPreferredSize).uiResource;
		}

        getComponentUIResource().setPreferredSize(pPreferredSize);
        if (getComponentUIResource() != uiResource && !(uiResource instanceof IContainer)) // For simple components apply the sizes also to the ui resource for mobile compatibility
        {
            uiResource.setPreferredSize(pPreferredSize);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreferredSizeSet()
    {
    	return getComponentUIResource().isPreferredSizeSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMinimumSize()
    {
    	return getComponentUIResource().getMinimumSize();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setMinimumSize(IDimension pMinimumSize)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
        if (pMinimumSize instanceof UIDimension)
        {
            pMinimumSize = ((UIDimension)pMinimumSize).uiResource;
        }

        getComponentUIResource().setMinimumSize(pMinimumSize);
        if (getComponentUIResource() != uiResource && !(uiResource instanceof IContainer)) // For simple components apply the sizes also to the ui resource for mobile compatibility
        {
            uiResource.setMinimumSize(pMinimumSize);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isMinimumSizeSet()
    {
    	return getComponentUIResource().isMinimumSizeSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMaximumSize()
    {
    	return getComponentUIResource().getMaximumSize();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setMaximumSize(IDimension pMaximumSize)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
        if (pMaximumSize instanceof UIDimension)
        {
            pMaximumSize = ((UIDimension)pMaximumSize).uiResource;
        }

        getComponentUIResource().setMaximumSize(pMaximumSize);
        if (getComponentUIResource() != uiResource && !(uiResource instanceof IContainer)) // For simple components apply the sizes also to the ui resource for mobile compatibility
        {
            uiResource.setMaximumSize(pMaximumSize);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximumSizeSet()
    {
    	return getComponentUIResource().isMaximumSizeSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
    {
    	return uiResource.getBackground();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setBackground(IColor pBackground)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pBackground instanceof UIColor)
		{
	    	uiResource.setBackground(((UIColor)pBackground).getUIResource());
		}
		else
		{
	    	uiResource.setBackground(pBackground);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isBackgroundSet()
    {
    	return uiResource.isBackgroundSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
    {
    	return uiResource.getForeground();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pForeground)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pForeground instanceof UIColor)
		{
	    	uiResource.setForeground(((UIColor)pForeground).getUIResource());
		}
		else
		{
	    	uiResource.setForeground(pForeground);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isForegroundSet()
    {
    	return uiResource.isForegroundSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public ICursor getCursor()
    {
    	return uiResource.getCursor();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setCursor(ICursor pCursor)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pCursor instanceof UICursor)
		{
	    	uiResource.setCursor(((UICursor)pCursor).uiResource);
		}
		else
		{
	    	uiResource.setCursor(pCursor);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isCursorSet()
    {
    	return uiResource.isCursorSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
    {
    	return uiResource.getFont();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pFont instanceof UIFont)
		{
	    	uiResource.setFont(((UIFont)pFont).getUIResource());
		}
		else
		{
	    	uiResource.setFont(pFont);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isFontSet()
    {
    	return uiResource.isFontSet();
    }

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
    {
    	return sToolTip;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolTipText(String pText)
    {
		sToolTip = pText;

		uiResource.setToolTipText(translate(pText));
    }

	/**
	 * {@inheritDoc}
	 */
	public void setFocusable(boolean pFocusable)
    {
    	uiResource.setFocusable(pFocusable);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFocusable()
    {
    	return uiResource.isFocusable();
    }

	/**
	 * {@inheritDoc}
	 */
	public Integer getTabIndex()
	{
		return uiResource.getTabIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabIndex(Integer pTabIndex)
	{
		uiResource.setTabIndex(pTabIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void requestFocus()
    {
		// Request focus has to address the uiResource 
		// (for eg. ToolbarPanel with Table inside -> 
		//    Table is uiResource, 
		//    ToolbarPanel is the componentUIResource 
		// The ToolbarPanel is used for add, so visibility, location, bounds, ... should address the componentUIResource
		// The Table is the control, so focus, enable, font, ... should address the uiResource.
    	uiResource.requestFocus();
    }

	/**
	 * {@inheritDoc}
	 */
	public IContainer getParent()
    {
        if (parent != null)
        {
            return parent;
        }
        else if (parentExpected instanceof IContainer)
        {
            return (IContainer)parentExpected;
        }
        else 
        {
            return null;
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setParent(IContainer pParent)
	{
		if (pParent == null)
		{
			if (parent != null && (parent.indexOf(this) >= 0 || (parent instanceof UIContainer && ((UIContainer)parent).contains(this))))
			{
				throw new IllegalArgumentException("Can't unset parent, because this component is still added!");
			}
		}
		else
		{
			if (pParent.indexOf(this) < 0 && !(pParent instanceof UIContainer && ((UIContainer)pParent).contains(this)))
			{
				throw new IllegalArgumentException("Can't set parent, because this component is not added!");
			}
		}
  
		boolean bUpdateTrans = parent != null && parent != pParent;
		
		parent = pParent;
		
		if (bUpdateTrans && isNotified())
		{
			updateTranslation();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible()
    {
    	return getComponentUIResource().isVisible();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
    {
		getComponentUIResource().setVisible(pVisible);
		if (getComponentUIResource() != uiResource)
		{
		    uiResource.setVisible(pVisible);
		}
		
		if (parent instanceof UIContainer)
		{
			((UIContainer)parent).componentChanged(this);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean pEnable)
    {
	    setUIResourceEnabled(pEnable);
    	
        bEnabled = pEnable;
    }

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled()
    {
    	return bEnabled;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocationRelativeTo(IComponent pComponent)
    {
    	return uiResource.getLocationRelativeTo(pComponent);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation)
    {
    	uiResource.setLocationRelativeTo(pComponent, pLocation);
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocation()
    {
		IPoint loc = getComponentUIResource().getLocation();
		
		IPoint locOffset = getLocationOffset();
		if (locOffset != null)
		{
			loc.setX(loc.getX() + locOffset.getX());
			loc.setY(loc.getY() + locOffset.getY());
		}
		
		return loc;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setLocation(IPoint pLocation)
    {
		setLocation(pLocation.getX(), pLocation.getY());
    }

	/**
	 * {@inheritDoc}
	 */
	public IDimension getSize()
    {
    	return getComponentUIResource().getSize();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setSize(IDimension pSize)
    {
	    if (pSize != null)
	    {
	        setSize(pSize.getWidth(), pSize.getHeight());
	    }
	    else
	    {
	        getComponentUIResource().setSize(null);
	    }
    }

	/**
	 * {@inheritDoc}
	 */
	public IRectangle getBounds()
    {
		IRectangle rect = getComponentUIResource().getBounds();
		
		IPoint locOffset = getLocationOffset();
		if (locOffset != null)
		{
			rect.setX(rect.getX() + locOffset.getX());
			rect.setY(rect.getY() + locOffset.getY());
		}
		
		return rect;
    }

	/**
	 * {@inheritDoc}
	 */
	public void setBounds(IRectangle pBounds)
    {
		setBounds(pBounds.getX(), pBounds.getY(), pBounds.getWidth(), pBounds.getHeight());
    }

	/**
	 * {@inheritDoc}
	 */
	public Object getResource()
    {
		return getComponentUIResource().getResource();
    }

	/**
	 * {@inheritDoc}
	 */
	public IComponent getEventSource()
	{
		return uiResource.getEventSource();
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void setStyle(Style pStyle)
    {
        uiResource.setStyle(pStyle);
    }
    
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        return uiResource.getStyle();
    }

	/**
	 * {@inheritDoc}
	 */
	public void setEventSource(IComponent pEventSource)
	{
		uiResource.setEventSource(pEventSource);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMousePressedListener> eventMousePressed()
	{
		return uiResource.eventMousePressed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseReleasedListener> eventMouseReleased()
	{
		return uiResource.eventMouseReleased();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseClickedListener> eventMouseClicked()
	{
		return uiResource.eventMouseClicked();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseEnteredListener> eventMouseEntered()
	{
		return uiResource.eventMouseEntered();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseExitedListener> eventMouseExited()
	{
		return uiResource.eventMouseExited();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyPressedListener> eventKeyPressed()
	{
		return uiResource.eventKeyPressed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyReleasedListener> eventKeyReleased()
	{
		return uiResource.eventKeyReleased();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyTypedListener> eventKeyTyped()
	{
		return uiResource.eventKeyTyped();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ComponentHandler<IComponentResizedListener> eventComponentResized()
	{
		return uiResource.eventComponentResized();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ComponentHandler<IComponentMovedListener> eventComponentMoved()
	{
		return uiResource.eventComponentMoved();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public FocusHandler<IFocusGainedListener> eventFocusGained()
	{
		return uiResource.eventFocusGained();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public FocusHandler<IFocusLostListener> eventFocusLost()
	{
		return uiResource.eventFocusLost();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IImage capture(int iWidth, int iHeight)
	{
		return getComponentUIResource().capture(iWidth, iHeight);
	}
	
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        if (uiComponent != this)
        {
        	uiComponent.setTranslation(pTranslation);
        }

        if (tmpCurrent != pTranslation)
        {
            lLastTranslationModified = -1;
        }
        
        tmpUserdefined = pTranslation;
        tmpCurrent = pTranslation;
        
        if (isNotified())
        {
            updateTranslation();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return tmpUserdefined;
    }
    
    //ITRANSLATABLE
	
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslate = pEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslate;
    }
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object pObject)
	{
		return this == pObject;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return System.identityHashCode(this);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the technological component enabled or disabled. 
     * 
     * @return true, if the component is enabled.
     */
    protected boolean isUIResourceEnabled()
    {
        return uiResource.isEnabled();
    }
    
	/**
	 * Sets the technological component enabled or disabled. 
	 * 
	 * @param pEnable true, if the component should be set enabled.
	 */
	protected void setUIResourceEnabled(boolean pEnable)
	{
	    uiResource.setEnabled(pEnable);
	}
	
	/**
	 * A simple helper method that allows to add a counter to a name until it
	 * is unique.
	 * 
	 * A simple counter will be appended to the given name, which will be
	 * incremented as long as the given set does already hold the name. If it
	 * doesn't hold the name, that name will be returned. It will not be added
	 * to the given set. 
	 * 
	 * @param pName the name to increment if necessary.
	 * @param pAlreadyExistingNames the {@link Set} that holds all names.
	 * @param pAppendOne if {@code 1} should be appended to the name or not.
	 * @return the name, if necessary with an appended counter.
	 */
	protected static String incrementNameIfExists(String pName, Set<String> pAlreadyExistingNames, boolean pAppendOne)
	{
		if (pName == null)
		{
			return pName;
		}
		
		String incrementedName = pName;
		int counter = 1;
		
		if (pAppendOne)
		{
			incrementedName = incrementedName + Integer.toString(counter);
		}

		while (pAlreadyExistingNames != null && pAlreadyExistingNames.contains(incrementedName))
		{
			counter++;
			incrementedName = pName + Integer.toString(counter);
		}
		
		return incrementedName;
	}

    /**
     * Sets the popup menu that should be displayed by this component.
     * @return the popup menu.
     */
    public IPopupMenu getPopupMenu()
    {
    	return popupMenu;
    }
    
    /**
     * Sets the popup menu that should be displayed by this component.
     * @param pPopupMenu the popup menu.
     */
    public void setPopupMenu(IPopupMenu pPopupMenu)
    {
    	if (popupMenu != null)
    	{
    		eventMousePressed().removeListener(this, "doTriggerPopMenu");
    		eventMouseReleased().removeListener(this, "doTriggerPopMenu");
    	}
    	
    	popupMenu = pPopupMenu;
    	
    	if (popupMenu != null)
    	{
    		eventMousePressed().addListener(this, "doTriggerPopMenu");
    		eventMouseReleased().addListener(this, "doTriggerPopMenu");
    	}
    }
	
    /**
     * Gets the default/generated name for this {@link UIComponent}. Can return
     * {@code null} if {@link #addNotify()} wasn't called by now.
     * 
     * @return the default/generated name. {@code null} if {@link #addNotify()}
     *         hasn't been called by now.
     */
	public String getDefaultName()
	{
		if (sDefaultName == null)
		{
			return sCurDefaultName;
		}
		else
		{
			return sDefaultName;
		}
	}
	
    /**
     * Gets the name of the root {@link UIComponent} of this {@link UIComponent}.
     * Returns {@code null} if there is none set.
     * 
     * @return the name of the root {@link UIComponent}. {@code null} if there is none.
     */
	public final String getRootName()
	{
		if (sRootName == null)
		{
			return sCurRootName;
		}
		else
		{
			return sRootName;
		}
	}
    
    /**
     * Triggers popup menu to be shown.
     * @param pMouseEvent the mouse event.
     */
    public void doTriggerPopMenu(UIMouseEvent pMouseEvent)
    {
    	if (pMouseEvent.isPopupTrigger() && popupMenu != null)
    	{
    		popupMenu.show(this, pMouseEvent.getX(), pMouseEvent.getY());
    	}
    }
    
    /**
     * Set the preferred size with primitive types.
     * 
     * @param pWidth the width.
     * @param pHeight the height.
     */
    public void setPreferredSize(int pWidth, int pHeight)
    {
        setPreferredSize(getFactory().createDimension(pWidth, pHeight));
    }

    /**
     * Set the minimum size with primitive types.
     * 
     * @param pWidth the width.
     * @param pHeight the height.
     */
    public void setMinimumSize(int pWidth, int pHeight)
    {
        setMinimumSize(getFactory().createDimension(pWidth, pHeight));
    }

    /**
     * Set the maximum size with primitive types.
     * 
     * @param pWidth the width.
     * @param pHeight the height.
     */
    public void setMaximumSize(int pWidth, int pHeight)
    {
        setMaximumSize(getFactory().createDimension(pWidth, pHeight));
    }

    /**
     * Set the location with primitive types.
     * 
     * @param pX the width.
     * @param pY the width.
     */
    public void setLocation(int pX, int pY)
    {
        int xOfs = 0;
        int yOfs = 0;
		IPoint locOffset = getLocationOffset();
		if (locOffset != null)
		{
			xOfs = locOffset.getX();
			yOfs = locOffset.getY();
		}
        
        getComponentUIResource().setLocation(getFactory().createPoint(pX - xOfs, pY - yOfs));
    }

    /**
     * Set the size with primitive types.
     * 
     * @param pWidth the width.
     * @param pHeight the height.
     */
    public void setSize(int pWidth, int pHeight)
    {
        getComponentUIResource().setSize(getFactory().createDimension(pWidth, pHeight));
    }

    /**
     * Set the bounds with primitive types.
     * 
     * @param pX the width.
     * @param pY the width.
     * @param pWidth the width.
     * @param pHeight the height.
     */
    public void setBounds(int pX, int pY, int pWidth, int pHeight)
    {
        int xOfs = 0;
        int yOfs = 0;
		IPoint locOffset = getLocationOffset();
		if (locOffset != null)
		{
			xOfs = locOffset.getX();
			yOfs = locOffset.getY();
		}

        getComponentUIResource().setBounds(getFactory().createRectangle(pX - xOfs, pY - yOfs, pWidth, pHeight));
    }

	/**
	 * Gets the genui component, thats ui resource is added to the parents ui resource.
	 * @return the genui component, thats ui resource is added to the parents ui resource.
	 */
	public UIComponent<?> getUIComponent()
	{
		// for compatibility reasons, check, if getComponentUIResource is overwritten.
		if (uiComponent == this)
		{
			IComponent compUIResource = getComponentUIResource();
			
			if (compUIResource != componentUIResource)
			{
				uiComponent = (UIComponent<?>)compUIResource.getEventSource();
				
				//#2041
				//to keep the current component accessible for underlying technology
				uiComponent.setEventSource(this);
			}
		}
				
		return uiComponent;
	}
	
	/**
	 * Sets the genui component, thats ui resource is added to the parents ui resource.
	 * @param pComponent the genui component, thats ui resource is added to the parents ui resource.
	 */
	protected void setUIComponent(UIComponent<?> pComponent)
	{
		uiComponent = pComponent;
		
		//#2041
		//to keep the current component accessible for underlying technology
		uiComponent.setEventSource(this);
	}
	
	/**
	 * Gets the component which will be added/removed to an {@link UIContainer} instead
	 * of this component. Thats needed to create complex Controls.
	 *
	 * Example Control: an extended Table Control with Toolbar. 
	 * So a ToolbarPanel with Table inside -&gt; 
	 *    Table is uiResource, 
	 *    ToolbarPanel is the componentUIResource 
	 * The ToolbarPanel is used for add, so visibility, location, bounds, ... should address the componentUIResource
	 * The Table is the control, so focus, enable, font, ... should address the uiResource.
	 * 
	 * @return the component which should be added/removed
	 */
	public IComponent getComponentUIResource()
	{
		return uiComponent.componentUIResource;
	}
	
	/**
	 * Gets the component which will be added/removed to an {@link UIContainer} instead
	 * of this component. Thats needed to create complex Controls.
	 *
	 * Example Control: an extended Table Control with Toolbar. 
	 * So a ToolbarPanel with Table inside -&gt; 
	 *    Table is uiResource, 
	 *    ToolbarPanel is the componentUIResource 
	 * The ToolbarPanel is used for add, so visibility, location, bounds, ... should address the componentUIResource
	 * The Table is the control, so focus, enable, font, ... should address the uiResource.
	 * 
	 * @return the component which should be added/removed
	 */
	protected IPoint getLocationOffset()
	{
		if (parent instanceof UIComponent)
		{
			IComponent parentUIResource = ((UIComponent<IComponent>)parent).getComponentUIResource();
			
			IComponent componentUIResourceParent = getComponentUIResource().getParent();
			
			if (parentUIResource != componentUIResourceParent && componentUIResourceParent != null)
			{
				IPoint point = componentUIResourceParent.getLocation();
				
				int x = point.getX();
				int y = point.getY();
				
				componentUIResourceParent = componentUIResourceParent.getParent();
					
				while (componentUIResourceParent != parentUIResource && componentUIResourceParent != null)
				{
					point = componentUIResourceParent.getLocation();
					x += point.getX();
					y += point.getY();
					
					componentUIResourceParent = componentUIResourceParent.getParent();
				}
				
				if (componentUIResourceParent == null)
				{
					locationOffset = null;
				}
				else
				{
					locationOffset = uiResource.getFactory().createPoint(x, y);
				}
			}
		}
		else if (parent != null)
		{
			locationOffset = null;
		}

		return locationOffset;
	}
	
	/**
	 * Logs debug information.
	 * 
	 * @param pInfo the debug information
	 */
	public void debug(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.debug(pInfo);
	}

	/**
	 * Logs information.
	 * 
	 * @param pInfo the information
	 */
	public void info(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}
		
		logger.info(pInfo);
	}
	
	/**
	 * Logs error information.
	 * 
	 * @param pInfo the error information
	 */
	public void error(Object... pInfo)
	{
		if (logger == null)
		{
			logger = LoggerFactory.getInstance(getClass());
		}

		logger.error(pInfo);
	}

	/**
	 * Invoked before this component is added.
	 *  
	 * @param pParent the parent
	 */
	public void beforeAddNotify(IComponent pParent)
	{
	    IComponent originalParentExpected = parentExpected;
		parentExpected = pParent;
		
		bBeforeNotified = true;
		
		try
		{
			updateTranslation();
		}
		finally
		{
		    parentExpected = originalParentExpected;
		}
	}

	/**
	 * Gets whether this component is notified before it was added.
	 *  
	 * @return <code>true</code> this component is notified, <code>false</code> otherwise
	 */
	public boolean isBeforeNotified()
	{
		return bBeforeNotified;
	}
	
    /**
     * Makes this <code>UIComponent</code> displayable by adding it to an {@link UIContainer}. 
     * This method is called internally by the genui and should not be called directly.
     * 
     * @see #removeNotify
     * @see #isNotified()
     */
	public void addNotify()
	{
		bNotified = true;
		
		if (!bBeforeNotified)
		{
			updateTranslation();
		}
		
		nameComponent();
	}
	
	/**
     * Determines whether this <code>UIComponent</code> is displayable. A component is 
     * displayable when it is added to an {@link UIContainer}.
     * <p>
     * An <code>UIComponent</code> is made displayable either when it is added to
     * a displayable containment hierarchy or when its containment
     * hierarchy is made displayable.
     * A containment hierarchy is made displayable when its ancestor 
     * {@link javax.rad.genui.container.UIWindow} is either packed or made visible.
     * <p>
     * An <code>UIComponent</code> is made undisplayable either when it is removed from
     * a displayable containment hierarchy or when its containment hierarchy
     * is made undisplayable.  A containment hierarchy is made 
     * undisplayable when its ancestor {@link javax.rad.genui.container.UIWindow} is disposed.
	 *  
	 * @return <code>true</code> if the component is displayable, <code>false</code> otherwise
	 * @see UIContainer#add(IComponent, Object, int)
	 * @see UIContainer#remove(int)
	 * @see javax.rad.genui.container.UIWindow#setVisible(boolean)
	 * @see javax.rad.genui.container.UIWindow#pack()
	 * @see javax.rad.genui.container.UIWindow#dispose()
	 */
	public boolean isNotified()
	{
		return bNotified;
	}
	
    /** 
     * Makes this <code>UIComponent</code> undisplayable by removing it to an {@link UIContainer}.
     * <p>
     * This method is called by the genui internally and should not be called directly. 
     * Code overriding this method should call <code>super.removeNotify</code> as the first line 
     * of the overriding method.
     *
     * @see #addNotify
     * @see #isNotified()
     */
	public void removeNotify()
	{
		bBeforeNotified = false;
		bNotified = false;
		
		// Always remove the name, as it is always added to the set.
		if (stExistingNames != null)
		{
			stExistingNames.remove(getName());
		}

//		uiResource.setName(null); // it should not heart, if the name is still available after remove. 
		
		sCurDefaultName = null;
		sCurRootName = null; // clear temporary root name.
		
//		if (uiComponent != this)
//		{
//			uiComponent.setName(null);
//		}
		
		stExistingNames = null; // clear reference
	}
	
	/**
	 * Gets the current translation mapping usable for this <code>UIComponent</code>.
	 * 
	 * @return the current translation mapping
	 */
	protected TranslationMap getCurrentTranslation()
	{
		return tmpCurrent;
	}
	
	/**
	 * Translates the given text with the available translation. If this component has no translation,
	 * then the translation from the parent component will be used.
	 * 
	 * @param pText the text to translate
	 * @return the translated text or <code>pText</code> if no translation was found
	 */
	public String translate(String pText)
	{
		if (bTranslate && tmpCurrent != null)
		{
			return tmpCurrent.translate(pText);
		}
		else
		{
			return pText;
		}
	}

	/**
	 * Notification for updating the translation. This method will be called when 
	 * the <code>UIComponent</code> will be added to a displayable containment hierarchy, 
	 * when its containment hierarchy is made displayable or the translation table will
	 * be changed.
	 * 
	 * @see #setTranslation(TranslationMap)
	 * @see #addNotify()
	 */
	public void updateTranslation()
	{
		IComponent comParent = parent == null ? parentExpected : parent;
		
		if (comParent != null && tmpUserdefined == null)
		{
			if (tmpCurrent != ((UIComponent)comParent).tmpCurrent)
			{
				lLastTranslationModified = -1;
			}
			
			tmpCurrent = ((UIComponent)comParent).tmpCurrent;
		}
		
		if (isTranslationChanged())
		{
			if (bTranslate && sToolTip != null)
			{
				uiResource.setToolTipText(translate(sToolTip));
			}
			
			if (tmpCurrent != null)
			{
				lLastTranslationModified = tmpCurrent.lastModified();
			}
		}
	}
	
	/**
	 * Dispatches the given {@link UIKeyEvent} to all EventHandlers that have
	 * subscribed to the {@link Key} of the event.
	 * 
	 * @param pKeyEvent the {@link UIKeyEvent} to dispatch
	 */
	public void doEventKey(UIKeyEvent pKeyEvent)
	{
		Key key = Key.getKey(pKeyEvent);
		
		if (mpEventKeys.containsKey(key))
		{
			mpEventKeys.get(key).dispatchEvent(pKeyEvent);
		}
	}
	
	/**
	 * The EventHandler for the given {@link Key}.
	 * 
	 * @param pKey the {@link Key} whose EventHandler is returned
	 * @return the EventHandler for the given {@link Key}
	 */
	public KeyHandler<? super IKeyListener> eventKey(Key pKey)
	{
		if (mpEventKeys == null)
		{
			mpEventKeys = new HashMap<Key, KeyHandler<? super IKeyListener>>();
			
			eventKeyPressed().addListener(this, "doEventKey");
			eventKeyReleased().addListener(this, "doEventKey");
			eventKeyTyped().addListener(this, "doEventKey");
		}
		
		if (!mpEventKeys.containsKey(pKey))
		{
		    KeyHandler<? super IKeyListener> handler;
			
			switch (pKey.getKeyEventType())
			{
				case UIKeyEvent.KEY_PRESSED:
					handler = new KeyHandler(IKeyPressedListener.class);
					break;
				case UIKeyEvent.KEY_RELEASED:
                    handler = new KeyHandler(IKeyReleasedListener.class);
					break;
				case UIKeyEvent.KEY_TYPED:
                    handler = new KeyHandler(IKeyTypedListener.class);
					break;
				default:
					throw new IllegalArgumentException("Key-Event '" + pKey.getKeyEventType() + "' is not supported!");
					
			}

			mpEventKeys.put(pKey, handler);
			
			return handler;
		}
		else
		{
	        return mpEventKeys.get(pKey);
		}
	}
	
	/**
	 * Gets whether the translation was changed.
	 * 
	 * @return <code>true</code> if translation was changed, <code>false</code> otherwise
	 */
	protected boolean isTranslationChanged()
	{
		IComponent comParent = parent == null ? parentExpected : parent;
		
		return lLastTranslationModified == -1 
			   //this check is important because it is possible that a nested class calls isTranslationChanged()
			   //before the check in updateTranslation was executed!
			   || (comParent != null && tmpCurrent != ((UIComponent)comParent).tmpCurrent)
			   //good if this method is called after updateTranslation()
		       || (tmpCurrent != null && tmpCurrent.lastModified() != lLastTranslationModified);
	}

    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously on the event dispatching thread. 
     * This will happen after all pending events have been processed. This method 
     * should be used when an application thread needs to update the GUI.
     *  
     * @param pRunnable the asynchronous call
     * @see #invokeAndWait(Runnable)
     */
    public static void invokeLater(Runnable pRunnable)
    {
        UIFactoryManager.getFactory().invokeLater(pRunnable);
    }
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously on the event dispatching thread. 
     * This will happen after all pending events have been processed. This method 
     * should be used when an application thread needs to update the GUI.
     *  
     * @param pObject the object
     * @param pMethod the asynchronous call
     * @see #invokeAndWait(Runnable)
     */
    public static void invokeLater(Object pObject, String pMethod)
    {
        UIFactoryManager.getFactory().invokeLater(runnableEventHandler.createListener(pObject, pMethod));
    }
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed synchronously on the event dispatching thread. This call blocks 
     * until all pending events have been processed and (then) <code>pRunnable.run()</code> returns. 
     * This method should be used when an application thread needs to update the GUI.
     * 
     * @param pRunnable the call
     * @throws Exception if the call caueses an exception
     */
    public static void invokeAndWait(Runnable pRunnable) throws Exception
    {
        UIFactoryManager.getFactory().invokeAndWait(pRunnable);
    }
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed synchronously on the event dispatching thread. This call blocks 
     * until all pending events have been processed and (then) <code>pRunnable.run()</code> returns. 
     * This method should be used when an application thread needs to update the GUI.
     * 
     * @param pRunnable the object
     * @param pMethod the call
     * @throws Exception if the call caueses an exception
     */
    public static void invokeAndWait(Object pRunnable, String pMethod) throws Exception
    {
        UIFactoryManager.getFactory().invokeAndWait(runnableEventHandler.createListener(pRunnable, pMethod));
    }
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously in a new thread. 
     * Action calls and UI Calls in the thread should be synchronized with the event dispatching thread
     * by using invokeLater or invokeAndWait.
     * This gives the IFactory implementation a chance to decide how and when to run the threads.
     * 
     * @param pRunnable the call
     * @return the thread
     */
    public static Thread invokeInThread(Runnable pRunnable)
    {
        return UIFactoryManager.getFactory().invokeInThread(pRunnable);
    }
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously in a new thread. 
     * Action calls and UI Calls in the thread should be synchronized with the event dispatching thread
     * by using invokeLater or invokeAndWait.
     * This gives the IFactory implementation a chance to decide how and when to run the threads.
     * 
     * @param pRunnable the object
     * @param pMethod the call
     * @return the thread
     */
    public static Thread invokeInThread(Object pRunnable, String pMethod)
    {
        return UIFactoryManager.getFactory().invokeInThread(runnableEventHandler.createListener(pRunnable, pMethod));
    }
	
	/**
	 * Creates a name for this {@link UIComponent}.
	 * 
	 * The returned name needs to be unique in the current root/workscreen and
	 * is ideally, but not necessarily, prefixed with the {@link #sRootName},
	 * if there is any.
	 * 
	 * Overriding classes should be aware that this method will be called in
	 * {@link #addNotify()} and the returned name will only be assigned to the
	 * {@link UIComponent} if there isn't already a name set. Also
	 * {@link #stExistingNames} can be used to find out if a name has already
	 * been assigned, and {@link #sRootName} to get the root name.
	 * 
	 * In rare cases {@link #stExistingNames} and/or {@link #sRootName} might
	 * be {@code null} when this method is called.
	 *  
	 * @return a unique name for this {@link UIComponent}.
	 * @see #createSimplifiedClassName()
	 * @see #getExistingNames()
	 * @see #getRootName()
	 * @see #incrementNameIfExists(String, Set, boolean)
	 */
	protected String createComponentName()
	{
		String name = createSimplifiedClassName();
		
		if (getParent() != null)
		{
			name = getParent().getName() + "_" + name;
		}
		
		return incrementNameIfExists(name, stExistingNames, true);
	}
	
	/**
	 * Creates a {@link String} consisting of the root name (if any) and
	 * the upper characters of the simple class name. That {@link String} can be
	 * used as a prefix if an extending class wants to provide a custom name.
	 * 
	 * Example: Root is a workscreen with the name {@code BookinWorkScreen} and
	 * the current component is a label, the prefix will be {@code NTWS1_L}.
	 *  
	 * @return the prefix.
	 * @see #createComponentName()
	 * @see #createSimplifiedClassName()
	 */
	protected String createComponentNamePrefix()
	{
		String name = createSimplifiedClassName();
		
		String rootName = getRootName();
		if (!StringUtil.isEmpty(rootName))
		{
			name = rootName + "_" + name;
		}
		
		return name;
	}
	
	/**
	 * Creates a {@link String} consisting of only the upper characters of the
	 * simple class name and an UI prefix (if any) removed.
	 * 
	 * Example: UITable becomes T, UIPasswordField becomes PF.
	 * 
	 * @return the simplified class name.
	 * @see #createComponentName()
	 * @see #createComponentNamePrefix()
	 */
	protected String createSimplifiedClassName()
	{
		String name = getClass().getSimpleName();
		
		if (name.startsWith("UI"))
		{
			name = name.substring(2);
		}
		
		return StringUtil.getText(name, TextType.UpperCase);
	}
	
	/**
	 * Gets the {@link Set} which contains the already existing {@link UIComponent}
	 * names under the current root. Might be {@code null} if it was not set.
	 * 
	 * @return the list of already existing names under the current root.
	 */
	protected final Set<String> getExistingNames()
	{
		return stExistingNames;
	}
	
	/**
	 * Sets the default name for this {@link UIComponent}.
	 * 
	 * @param pDefaultName the new default name.
	 */
	protected void setDefaultName(String pDefaultName)
	{
		sDefaultName = pDefaultName;
	}
	
	/**
	 * Sets the root name to the given one.
	 * 
	 * This also resets the list of already existing names, making the current
	 * {@link UIComponent} the effective root for all naming of it's children.
	 * 
	 * @param pRootName the new root name.
	 * @see #getExistingNames()
	 * @see #getRootName()
	 */
	protected void setRootName(String pRootName)
	{
		sRootName = pRootName;
	}
	
	/**
	 * Names this {@link UIComponent}. A name will be created by calling
	 * {@link #createComponentName()} and setting that name.
	 * 
	 * This method will also copy the {@link Set} of already existing names
	 * into the {@link #stExistingNames local field} and the root name
	 * into the {@link #sRootName local field}.
	 * 
	 * @see #createComponentName()
	 * @see #createComponentNamePrefix()
	 * @see #stExistingNames
	 * @see #sRootName
	 */
	private void nameComponent()
	{
		if (sRootName == null && parent instanceof UIComponent)
		{
			stExistingNames = ((UIComponent)parent).stExistingNames;
			sCurRootName = ((UIComponent)parent).sCurRootName;
		}
		else
		{
			sCurRootName = sRootName;
			stExistingNames = new HashSet<String>();
		}

		if (sDefaultName == null)
		{
			sCurDefaultName = createComponentName();
		}
		else
		{
			sCurDefaultName = sDefaultName;
		}
		
		String name;
		if (sName == null)
		{
			name = sCurDefaultName;
		}
		else
		{
			name = sName;
		}
		
		componentUIResource.setName(name);
		
		if (sCurRootName == null)
		{
			sCurRootName = name;
		}

		if (uiComponent != this)
		{
	        if (uiComponent.componentUIResource == componentUIResource)
			{
				// Ensure, that the covered name is the same, as the name of this.
				uiComponent.setName(name);
			}
			else
			{
				// Mark uiComponent as wrapper if it is not this component.
				uiComponent.setName(name + "-wrapper");
			}
		}
		
		if (!StringUtil.isEmpty(name))
		{
			stExistingNames.add(name);
		}
	}
	
	/**
	 * Adds style names to the component style info.
	 * 
	 * @param pName the style names
	 */
	public void addStyleNames(String... pName)
	{
		Style.addStyleNames(this, pName);
	}
	
	/**
	 * Removes style names from the component style info.
	 * 
	 * @param pName the style names
	 */
	public void removeStyleNames(String... pName)
	{
		Style.removeStyleNames(this, pName);
	}
	
}	// UIComponent
