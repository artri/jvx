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
 * 24.09.2013 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl;

import java.util.ArrayList;
import java.util.List;

import javax.rad.ui.IAlignmentConstants;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFactoryComponent;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;
import javax.rad.ui.Style;
import javax.rad.ui.event.ComponentHandler;
import javax.rad.ui.event.FocusHandler;
import javax.rad.ui.event.KeyHandler;
import javax.rad.ui.event.MouseHandler;
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
import javax.rad.util.EventHandler;
import javax.rad.util.RuntimeEventHandler;

import com.sibvisions.rad.ui.vaadin.ext.VaadinUtil;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.CssExtensionAttribute;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.AttributesExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicAttributes;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.IDynamicCss;
import com.sibvisions.rad.ui.vaadin.impl.layout.AbstractVaadinClientLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinAbsoluteLayout;
import com.sibvisions.rad.ui.vaadin.impl.layout.VaadinFormLayout;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;

/**
 * The <code>VaadinComponentBase</code> class is the vaadin implementation of {@link IComponent} and the
 * base class of all components.
 * 
 * @author René Jahn
 * 
 * @param <CR> an instance of {@link Component}
 * @param <C> an instance of {@link Component}
 */
public abstract class VaadinComponentBase<CR extends Component, C extends Component> extends VaadinResource<CR, C>                   
                                                                                     implements IFactoryComponent, 
                                                                                                IAlignmentConstants,
                                                                                                IDynamicCss,
                                                                                                IDynamicAttributes
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** container where the component is added. */
	protected IContainer 						parent					= null;

	/** the factory. */
	private VaadinFactory						factory;
	
	/** CssExtension to send style attributes to the client component. */
	private CssExtension 						cssExtension 			= null;
	
	/** AttributesExtension to send attributes to the client component. */
	private AttributesExtension				    attExtension			= null;
	
	/** the Event Source. */
	protected IComponent						eventSource				= this;
	
	/** EventHandler for mousePressed. */
	protected MouseHandler<IMousePressedListener>  eventMousePressed	= null;

	/** EventHandler for mouseReleased. */
	protected MouseHandler<IMouseReleasedListener> eventMouseReleased   = null;

	/** EventHandler for mouseClicked. */
	protected MouseHandler<IMouseClickedListener>  eventMouseClicked	= null;

	/** EventHandler for mouseEntered. */
	private MouseHandler<IMouseEnteredListener> eventMouseEntered		= null;

	/** EventHandler for mouseExited. */
	private MouseHandler<IMouseExitedListener>  eventMouseExited		= null;

	/** EventHandler for keyPressed. */
	private KeyHandler<IKeyPressedListener>     eventKeyPressed			= null;

	/** EventHandler for keyReleased. */
	private KeyHandler<IKeyReleasedListener>    eventKeyReleased		= null;

	/** EventHandler for keyTyped. */
	private KeyHandler<IKeyTypedListener>		eventKeyTyped			= null;

	/** EventHandler for componentMoved. */
	protected ComponentHandler<IComponentMovedListener> eventComponentMoved	    = null;

	/** EventHandler for componentResized. */
	protected ComponentHandler<IComponentResizedListener> eventComponentResized	= null;
	
	/** EventHandler for focusGained. */
	protected FocusHandler<IFocusGainedListener>  eventFocusGained        = null;
	/** EventHandler for focusLost. */
	protected FocusHandler<IFocusLostListener>    eventFocusLost          = null;
	
	/** the background color of the component. **/
	protected IColor background = null;
	
	/** the foreground color of the component. **/
	protected IColor foreground = null;
	
	/** the cursor of the component. **/
	protected ICursor cursor = null;
	
	/** the font of the component. **/
	protected IFont font = null;
	
	/** the bounds of the component. **/
	protected IRectangle bounds = new VaadinRectangle(0, 0, -1, -1);
	
	/** the location of the component. **/
	protected IPoint location = null;
	
	/** the size of the component. **/
	protected IDimension preferredSize = null;
	
	/** the minimum - Size of the component. **/
	protected IDimension minimumSize = null;
	
	/** the maximum - Size of the component. **/
	protected IDimension maximumSize = null;
	
	/** the Constraint is needed for the FormLayout. */
	protected Object constraints = null;
	
    /** the component name. */
    private String sName = null;

    /** the tooltip text of the component. **/
    private String toolTipText = null;

    /** custom set style names. */
    private String[] sOldStyleNames;
    
    /** the intern styles. */
    private List<String> liInternStyles;

    /** tjhe tab index. */
    private Integer tabIndex = null;

    /** the vertical Position of the text. **/
    protected int verticalAlignment = IAlignmentConstants.ALIGN_CENTER;
    
    /** the horizontal Position of the text. **/
    protected int horizontalAlignment = IAlignmentConstants.ALIGN_LEFT;

    /** whether the component is focusable. **/
	private boolean focusable = false;
	
	/** whether the component is enabled. **/
	private boolean enabled = true;
	
	/** whether the component is visible. **/
	private boolean visible = true;

	/** The {@link Registration} of the {@link ContextClickListener}. */
	private Registration contextClickListenerRegistration = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>VaadinComponentBase</code>.
	 * 
	 * @param pComponent an instance of {@link Component}.
	 * @see IComponent
	 */
	protected VaadinComponentBase(C pComponent)
	{
		super(pComponent);
		
		init();
		
		resource.setSizeUndefined();
	}

	/**
	 * Initializes the component.
	 */
	private void init()
	{
		if (resource instanceof Focusable)
		{
			setFocusable(true);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface Implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public VaadinFactory getFactory()
	{
		return factory;
	}

    /**
     * {@inheritDoc}
     */
    public void setFactory(IFactory pFactory)
    {
        factory = (VaadinFactory)pFactory;
    }
    
	/**
	 * {@inheritDoc}
	 */
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		CssExtension csse = getCssExtension();
		
		csse.removeAttribute("text-align");
		
		if (pHorizontalAlignment == IAlignmentConstants.ALIGN_LEFT)
		{
			csse.addAttribute("text-align", "left");
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_CENTER)
		{
			csse.addAttribute("text-align", "center");
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_RIGHT)
		{
			csse.addAttribute("text-align", "right");
		}
		else if (pHorizontalAlignment == IAlignmentConstants.ALIGN_STRETCH)
		{
			resource.setWidth("100%");
		}
		
		horizontalAlignment = pHorizontalAlignment;		
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		CssExtension csse = getCssExtension();
		
		csse.removeAttribute("vertical-align");
		
		if (pVerticalAlignment == IAlignmentConstants.ALIGN_BOTTOM)
		{
			csse.addAttribute("vertical-align", "bottom");
		}
		else if (pVerticalAlignment == IAlignmentConstants.ALIGN_CENTER)
		{
			csse.addAttribute("vertical-align", "middle");
		}
		else if (pVerticalAlignment == IAlignmentConstants.ALIGN_TOP)
		{
			csse.addAttribute("vertical-align", "top");
		}
		else if (pVerticalAlignment == IAlignmentConstants.ALIGN_STRETCH)
		{
			resource.setHeight("100%");
		}

		verticalAlignment = pVerticalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return sName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String pName)
	{
		if (sName != null)
		{
			removeInternStyleName(StringUtil.convertToName(sName).toLowerCase());
		}
		
		sName = pName;
		
		if (sName != null)
		{
			addInternStyleName(StringUtil.convertToName(sName).toLowerCase());
		}
		
	    setId(StringUtil.sanitizeId(pName));
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getPreferredSize()
	{
		if (preferredSize != null)
		{
			return new VaadinDimension(preferredSize);
		}
		else
		{
			return new VaadinDimension(-1, -1);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreferredSize(IDimension pPreferredSize)
	{
		preferredSize = pPreferredSize;
		
		if (getParent() != null && getParent().getLayout() instanceof AbstractVaadinClientLayout)
        {
		    ((AbstractVaadinClientLayout)getParent().getLayout()).repaintLayout(true);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreferredSizeSet()
	{
		return preferredSize != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMinimumSize()
	{
		if (minimumSize != null)
		{
			return new VaadinDimension(minimumSize);
		}
		
		return new VaadinDimension(0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinimumSize(IDimension pMinimumSize)
	{
		minimumSize = pMinimumSize;
        
        if (getParent() != null && getParent().getLayout() instanceof AbstractVaadinClientLayout)
        {
            ((AbstractVaadinClientLayout)getParent().getLayout()).repaintLayout(true);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMinimumSizeSet()
	{
		return minimumSize != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMaximumSize()
	{
		if (maximumSize != null)
		{
			return new VaadinDimension(maximumSize);
		}
		
		return new VaadinDimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaximumSize(IDimension pMaximumSize)
	{
		maximumSize = pMaximumSize;
        
        if (getParent() != null && getParent().getLayout() instanceof AbstractVaadinClientLayout)
        {
            ((AbstractVaadinClientLayout)getParent().getLayout()).repaintLayout(true);
        }
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximumSizeSet()
	{
		return maximumSize != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
	{
		return background;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBackground(IColor pBackground)
	{
		background = pBackground;
		
		//#1946 (maybe overwritten in a sub class like VaadinSingleComponentContainer)
		setBackgroundColorAttribute(pBackground);
		setBackgroundImageAttribute(pBackground != null ? "none" : null);
		setBoxShadowAttribute(pBackground != null ? "none" : null);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBackgroundSet()
	{
		return background != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
	{
		return foreground;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pForeground)
	{
		foreground = pForeground;
		
		if (pForeground != null) 
		{
			getCssExtension().addAttribute("color", ((VaadinColor)pForeground).getStyleValueRGB());	
		}
		else
		{
			getCssExtension().removeAttribute("color");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isForegroundSet()
	{
		return foreground != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getCursor()
	{
		return cursor;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCursor(ICursor pCursor)
	{
        if (cursor != null)
        {
            removeInternStyleName(((VaadinCursor)cursor).getStyleName());
        }

        cursor = pCursor;

        if (pCursor != null)
		{
			addInternStyleName(((VaadinCursor)pCursor).getStyleName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCursorSet()
	{
		return cursor != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
	{
		return font;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
	{		
		font = pFont;
		
		if (pFont != null) 
		{
			VaadinFont vaadinFont = new VaadinFont(pFont.getFontName(), pFont.getStyle(), pFont.getSize());

			getCssExtension().addAttributes(vaadinFont.getStyleAttributes(null, CssExtensionAttribute.SELF));	
		}
		else
		{
			CssExtension csse = getCssExtension();
			
			csse.removeAttribute("font-weight");
			csse.removeAttribute("font-style");
			csse.removeAttribute("font-family");
			csse.removeAttribute("font-size");
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFontSet()
	{
		return font != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
	{
		return toolTipText;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setToolTipText(String pToolTipText)
	{
		if (resource instanceof AbstractComponent) 
		{
			toolTipText = pToolTipText;
			
			if (pToolTipText != null && pToolTipText.startsWith("<html>"))
			{
                ((AbstractComponent)resource).setDescription(pToolTipText, ContentMode.HTML);
			}
			else
			{
			    ((AbstractComponent)resource).setDescription(pToolTipText);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFocusable()
	{
		return focusable;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFocusable(boolean pFocusable)
	{
		if (resource instanceof Focusable)
		{
			focusable = pFocusable;
		}
		else
		{
			focusable = false;
		}
		
		setFocusableIntern();
	}

    /**
     * Allows extending isFocusable, without changing the state of isFocusable.
     *  
     * @return isFocusable.
     */
    protected boolean isFocusableIntern()
    {
        return isFocusable();
    }
    
	/**
	 * Sets focusable without changing the property.
	 */
	protected void setFocusableIntern()
	{
		if (isFocusableIntern() && isEnabled())
		{
			removeInternStyleName("v-notfocusable");
			
			if (resource instanceof Focusable)
			{
				setTabIndex(tabIndex);
			}
		}
		else
		{
			addInternStyleName("v-notfocusable");
			
			if (resource instanceof Focusable)
			{
				((Focusable)resource).setTabIndex(-2);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getTabIndex()
	{
		return tabIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTabIndex(Integer pTabIndex)
	{
		tabIndex = pTabIndex;

		if (resource instanceof Focusable)
		{
			if (focusable)
			{
				if (pTabIndex == null)
				{
					((Focusable)resource).setTabIndex(0);
				}
				else
				{
					((Focusable)resource).setTabIndex(tabIndex.intValue());
				}
			}
			else
			{
				((Focusable)resource).setTabIndex(-2);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void requestFocus()
	{
		if (resource != null && isFocusable() && resource instanceof Focusable)
		{
			((Focusable)resource).focus();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IContainer getParent()
	{
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParent(IContainer pParent)
	{
		if (pParent == null)
		{
			if (parent != null && parent.indexOf(this) >= 0)
			{
				throw new IllegalArgumentException("Can't unset " + "parent, because this component is still added!");
			}
		}
		else
		{
			if (pParent.indexOf(this) < 0)
			{
				throw new IllegalArgumentException("Can't set parent, " + "because this component is not added!");
			}
		}
		
		parent = pParent;		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
	{
		visible = pVisible;
		
		resource.setVisible(pVisible);
		
		if (getParent() != null && getParent().getLayout() instanceof VaadinFormLayout)
		{
			if (pVisible && !((VaadinFormLayout) getParent().getLayout()).isComponentAdded(this))
			{
				((VaadinFormLayout) getParent().getLayout()).markDirty(); // Repaint Layout if component is not added in gridLayout.
																		  // This is possible if two components are added in the same cell.
																		  // Only the first visible component is added.
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean pEnabled)
	{
		enabled = pEnabled;
		
		resource.setEnabled(pEnabled);
		
		setFocusableIntern();
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocationRelativeTo(IComponent pComponent)
	{
		return getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation)
	{
		setLocation(pLocation);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocation()
	{
		return new VaadinPoint(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLocation(IPoint pLocation)
	{
		bounds.setX(pLocation.getX());
		bounds.setY(pLocation.getY());
		
		if (getParent() != null && getParent().getLayout() instanceof VaadinAbsoluteLayout)
		{
			((VaadinAbsoluteLayout) getParent().getLayout()).markDirty();
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getSize()
	{
		return new VaadinDimension(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSize(IDimension pSize)
	{
		if (pSize == null)
		{
			getResource().setSizeUndefined();
			
			bounds.setWidth(-1);
			bounds.setHeight(-1);
		}
		else
		{
			bounds.setWidth(pSize.getWidth());
			bounds.setHeight(pSize.getHeight());
			
			if (pSize.getWidth() < 0)
			{
				VaadinUtil.setComponentWidth(getResource(), VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			else
			{
				VaadinUtil.setComponentWidth(getResource(), pSize.getWidth(), Unit.PIXELS);
			}
			
			if (pSize.getHeight() < 0)
			{
				VaadinUtil.setComponentHeight(getResource(), VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
			}
			else
			{
				VaadinUtil.setComponentHeight(getResource(), pSize.getHeight(), Unit.PIXELS);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IRectangle getBounds()
	{
		return new VaadinRectangle(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBounds(IRectangle pBounds)
	{
		setLocation(pBounds);
		
		setSize(pBounds);

		if (getParent() != null && getParent().getLayout() instanceof VaadinAbsoluteLayout)
		{
			((VaadinAbsoluteLayout) getParent().getLayout()).markDirty();
		}	

	}

	/**
	 * {@inheritDoc}
	 */
	public IComponent getEventSource()
	{
		return eventSource;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEventSource(IComponent pEventSource)
	{
		eventSource = pEventSource;
	}

    /**
     * {@inheritDoc}
     */
    public void setStyle(Style pStyle)
    {
        //This method tries to remove only "custom" style names. 
        //All directly set stylenames (via intern methods or via resource) should be kept

    	String[] sNames;
    	
        if (pStyle != null)
        {
            sNames = pStyle.getStyleNames();
        }
        else
        {
        	sNames = null;
        }
            
        //only remove missing elements
        String[] sRemoveNames;
        
        if (sNames != null)
        {
        	sRemoveNames = ArrayUtil.removeAll(sOldStyleNames, sNames);
        }
        else
        {
        	sRemoveNames = sOldStyleNames;
        }
        
        if (liInternStyles != null)
        {
            //don't remove intern names
            sRemoveNames = ArrayUtil.removeAll(sRemoveNames, liInternStyles.toArray(new String[liInternStyles.size()]));
        }

        if (sRemoveNames != null)
        {
            for (int i = 0; i < sRemoveNames.length; i++)
            {
                resource.removeStyleName(sRemoveNames[i]);
            }
        }
        
        if (sNames != null)
        {
            for (int i = 0; i < sNames.length; i++)
            {
                resource.addStyleName(sNames[i]);
            }
        }
        
        sOldStyleNames = sNames;
    }
    
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        Style style = Style.parse(resource.getStyleName());

        String[] sStyleNames = style.getStyleNames();
        
        //we remove intern styles because we won't remove
        if (liInternStyles != null)
        {
            sStyleNames = ArrayUtil.removeAll(sStyleNames, liInternStyles.toArray(new String[liInternStyles.size()]));
        }
        
        if (sStyleNames == null || sStyleNames.length == 0)
        {
            return new Style();
        }
        else
        {
            return new Style(sStyleNames);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public IImage capture(int pWidth, int pHeight)
    {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMousePressedListener> eventMousePressed()
	{
		if (eventMousePressed == null)
		{
			eventMousePressed = new MouseHandler<IMousePressedListener>(IMousePressedListener.class);
		}
		return eventMousePressed;
	}

	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseReleasedListener> eventMouseReleased()
	{
		if (eventMouseReleased == null)
		{
			eventMouseReleased = new MouseHandler<IMouseReleasedListener>(IMouseReleasedListener.class);
			
			attachContextClickListenerToResource();
		}
		return eventMouseReleased;
	}

	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseClickedListener> eventMouseClicked()
	{
		if (eventMouseClicked == null)
		{
			eventMouseClicked = new MouseHandler<IMouseClickedListener>(IMouseClickedListener.class);
		}
		
		return eventMouseClicked;
	}

	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseEnteredListener> eventMouseEntered()
	{
		if (eventMouseEntered == null)
		{
			eventMouseEntered = new MouseHandler<IMouseEnteredListener>(IMouseEnteredListener.class);
		}
		return eventMouseEntered;
	}

	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMouseExitedListener> eventMouseExited()
	{
		if (eventMouseExited == null)
		{
			eventMouseExited = new MouseHandler<IMouseExitedListener>(IMouseExitedListener.class);
		}
		return eventMouseExited;
	}

	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyPressedListener> eventKeyPressed()
	{
		if (eventKeyPressed == null)
		{
			eventKeyPressed = new KeyHandler<IKeyPressedListener>(IKeyPressedListener.class);
		}
		return eventKeyPressed;
	}

	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyReleasedListener> eventKeyReleased()
	{
		if (eventKeyReleased == null)
		{
			eventKeyReleased = new KeyHandler<IKeyReleasedListener>(IKeyReleasedListener.class);
		}
		return eventKeyReleased;
	}

	/**
	 * {@inheritDoc}
	 */
	public KeyHandler<IKeyTypedListener> eventKeyTyped()
	{
		if (eventKeyTyped == null)
		{
			eventKeyTyped = new KeyHandler<IKeyTypedListener>(IKeyTypedListener.class);
		}
		return eventKeyTyped;
	}

	/**
	 * {@inheritDoc}
	 */
	public ComponentHandler<IComponentMovedListener> eventComponentMoved()
	{
		if (eventComponentMoved == null)
		{
			eventComponentMoved = new ComponentHandler<IComponentMovedListener>(IComponentMovedListener.class);
		}
		return eventComponentMoved;
	}

	/**
	 * {@inheritDoc}
	 */
	public ComponentHandler<IComponentResizedListener> eventComponentResized()
	{
		if (eventComponentResized == null)
		{
			eventComponentResized = new ComponentHandler<IComponentResizedListener>(IComponentResizedListener.class);
		}
		return eventComponentResized;
	}

    /**
	 * {@inheritDoc}
	 */
    public FocusHandler<IFocusGainedListener> eventFocusGained()
    {
		if (eventFocusGained == null)
		{
			eventFocusGained = new FocusHandler<IFocusGainedListener>(IFocusGainedListener.class);
			
			addFocusListener();
		}
		return eventFocusGained;
    }
	
    /**
	 * {@inheritDoc}
	 */
    public FocusHandler<IFocusLostListener> eventFocusLost()
    {
		if (eventFocusLost == null)
		{
			eventFocusLost = new FocusHandler<IFocusLostListener>(IFocusLostListener.class);
			
			addBlurListener();
		}
		return eventFocusLost;
    }	
    
    /**
     * {@inheritDoc}
     * 
     * @throws RuntimeException if resource is not an instance of {@link AbstractComponent}
     */
    public CssExtension getCssExtension()
    {
        if (cssExtension == null || cssExtension.getComponent() != resource)
        {
            //Extension is only possible for an AbstractComponent instance
            if (resource != null && resource instanceof AbstractComponent) 
            {
            	if (cssExtension != null)
            	{
            		cssExtension.getComponent().removeExtension(cssExtension);
            	}
            	
                cssExtension = new CssExtension();
                
                configureCssExtension(cssExtension);
                
                cssExtension.extend((AbstractComponent)resource);
            }
            else
            {
                throw new RuntimeException("Cant extend " + resource);
            }
        }
        
        return cssExtension;
    }    
    
	/**
	 * {@inheritDoc}
	 */
    public AttributesExtension getAttributesExtension()
    {
        if (attExtension == null || attExtension.getComponent() != resource)
        {
            //Extension is only possible for an AbstractComponent instance
            if (resource != null && resource instanceof AbstractComponent) 
            {
            	if (attExtension != null)
            	{
            		attExtension.getComponent().removeExtension(attExtension);
            	}
            	
            	attExtension = new AttributesExtension();
            	
            	configureAttributesExtension(attExtension);

            	attExtension.extend((AbstractComponent)resource);
            }
            else
            {
                throw new RuntimeException("Cant extend " + resource);
            }
        }
        
        return attExtension;
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
	 * Component depending add of listener.
	 */
	protected void addFocusListener()
	{
	}
	
	/**
	 * Component depending add of listener.
	 */
	protected void addBlurListener()
	{
	}
	
	/**
	 * Gets the id of this component.
	 * 
	 * @return the id.
	 */
	public String getId()
	{
		return resource.getId();
	}
	
	/**
	 * Sets the id of this component. Be careful with this method because a change of
	 * the id may cause problems!
	 * 
	 * @param pId the id
	 */
	protected void setId(String pId)
	{
		if (resource != null)
		{
			resource.setId(pId);
		}
	}
	
	/**
	 * Gets the constraints of this component.
	 * 
	 * @return the constraints of this component.
	 */
	public Object getConstraints()
    {
    	return constraints;
    }

	/**
	 * Sets the constraints of this component.
	 * 
	 * @param pConstraints the constraints of this component.
	 */
	public void setConstraints(Object pConstraints)
    {
		constraints = pConstraints;
    }	
	
	/**
	 * Sets the width of the component to 100%.
	 */
	public void setWidthFull()
	{
		if (VaadinUtil.isParentWidthDefined(resource))
		{
			VaadinUtil.setComponentWidth((AbstractComponent)resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentWidth((AbstractComponent)resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}
	}
	
	/**
	 * Sets the height of the component to 100%.
	 */
	public void setHeightFull()
	{
		if (VaadinUtil.isParentHeightDefined(resource))
		{
			VaadinUtil.setComponentHeight((AbstractComponent)resource, 100f, Unit.PERCENTAGE);
		}
		else
		{
			VaadinUtil.setComponentHeight((AbstractComponent)resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
		}	
	}
	
	/**
	 * Sets the width of the component to undefined.
	 */
	public void setWidthUndefined()
	{
		VaadinUtil.setComponentWidth((AbstractComponent)resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}
	
	/**
	 * Sets the height of the component to undefined.
	 */
	public void setHeightUndefined()
	{
		VaadinUtil.setComponentHeight((AbstractComponent)resource, VaadinUtil.SIZE_UNDEFINED, Unit.PIXELS);
	}	
	
	/**
	 * Sets the width and height of the component to 100%.
	 */
	public void setSizeFull()
	{
		setWidthFull();
		setHeightFull();
	}
	
	/**
	 * Sets the width and height of the component to undefined.
	 */
	public void setSizeUndefined()
	{
		setWidthUndefined();
		setHeightUndefined();
	}		

	/**
	 * Gets whether the size was set to custom values.
	 * 
	 * @return <code>true</code> if the size was set to custom values, <code>false</code> otherwise
	 */
	public boolean isSizeSet()
	{
	    return bounds.getWidth() >= 0 && bounds.getHeight() >= 0;
	}
	
	/**
	 * Attaches a {@link ContextClickListener} to the current resource, if that
	 * is possible.
	 * <p>
	 * This method should be called by extending classes every time they change
	 * the resource.
	 * <p>
	 * The resource has to be an {@link AbstractComponent} for this to work. If
	 * there was a previous registration, the previous is being removed before
	 * the new one is (eventually) attached.
	 */
	protected void attachContextClickListenerToResource()
	{
		if (contextClickListenerRegistration != null) 
		{
			contextClickListenerRegistration.remove();
			contextClickListenerRegistration = null;
		}
		
		if (resource instanceof AbstractComponent)
		{
			contextClickListenerRegistration = ((AbstractComponent)resource).addContextClickListener(this::doContextClick);
		}
	}
	
	/**
	 * Gets whether a {@link ContextClickListener} is attached.
	 * 
	 * @return <code>true</code> if attached, <code>false</code> otherwise
	 */
	protected boolean isContextClickListenerAttached()
	{
		return contextClickListenerRegistration != null;
	}
	
	/**
	 * Event handler when the context click occurs.
	 * <p>
	 * This is normally the right click on the component itself. Is only called
	 * when the {@link #getResource() reosurce} is an {@link AbstractComponent}.
	 * 
	 * @param pEvent the {@link ContextClickEvent} that occured.
	 */
	protected void doContextClick(ContextClickEvent pEvent)
	{
		dispatchMouseEvent(eventMousePressed, pEvent, UIMouseEvent.MOUSE_PRESSED);
		dispatchMouseEvent(eventMouseClicked, pEvent, UIMouseEvent.MOUSE_CLICKED);
		dispatchMouseEvent(eventMouseReleased, pEvent, UIMouseEvent.MOUSE_RELEASED);
	}
	
	/**
	 * Convenience method for creating a new {@link UIMouseEvent}.
	 * 
	 * @param pId the ID of the new event.
	 * @param pEvent the base {@link ClickEvent}.
	 * @return the created {@link UIMouseEvent}.
	 */
	protected UIMouseEvent createMouseEvent(int pId, ClickEvent pEvent)
	{
    	int clickCount = pEvent.isDoubleClick() ? 2 : 1;
    	boolean popupTrigger = clickCount == 1 && pEvent.getButton() == MouseButton.RIGHT && pId == UIMouseEvent.MOUSE_RELEASED; 
    	
    	int modifiers = 0;
        if (pEvent.isShiftKey())
        {
            modifiers |= UIMouseEvent.SHIFT_MASK;
        }
        if (pEvent.isCtrlKey())
        {
            modifiers |= UIMouseEvent.CTRL_MASK;
        }
        if (pEvent.isAltKey())
        {
            modifiers |= UIMouseEvent.ALT_MASK;
        }
        if (pEvent.isMetaKey())
        {
            modifiers |= UIMouseEvent.META_MASK;
        }
        if (pEvent.getButton() == MouseButton.LEFT)
        {
            modifiers |= UIMouseEvent.BUTTON1_MASK;
        }
        if (pEvent.getButton() == MouseButton.MIDDLE)
        {
            modifiers |= UIMouseEvent.BUTTON2_MASK;
        }
        if (pEvent.getButton() == MouseButton.RIGHT)
        {
            modifiers |= UIMouseEvent.BUTTON3_MASK;
        }
    	
    	return new UIMouseEvent(eventSource, pId, System.currentTimeMillis(), modifiers, 
                                pEvent.getClientX(), pEvent.getClientY(), clickCount, popupTrigger);
	}
	
	/**
	 * Dispatches the given mouse event.
	 * 
	 * @param pHandler the {@link MouseHandler} to dispatch on, can be
	 *                 {@code null} or an empty handler, in which case no event
	 *                 is dispatched.
	 * @param pClickEvent the base {@link ClickEvent}.
	 * @param pEventId the ID of the event to dispatch.
	 */
	protected void dispatchMouseEvent(MouseHandler<?> pHandler, ClickEvent pClickEvent, int pEventId)
	{
		if (EventHandler.isDispatchable(pHandler))
		{
			dispatchEvent(pHandler, createMouseEvent(pEventId, pClickEvent));
		}
	}
	
	/**
	 * Dispatches the event on the given {@link RuntimeEventHandler}.
	 * 
	 * @param pHandler the {@link RuntimeEventHandler}, can be {@code null} or
	 *                 empty, in which case no event is dispatched.
	 * @param pEvent the event to be dispatched.
	 */
	protected void dispatchEvent(RuntimeEventHandler<?> pHandler, Object pEvent)
	{
		if (EventHandler.isDispatchable(pHandler))
		{
			getFactory().synchronizedDispatchEvent(pHandler, pEvent);
		}
	}
	
	/**
	 * Adds a style name to the intern cache. An intern style won't be removed
	 * via {@link #setStyle(Style)}.
	 * 
	 * @param pStyle the style name
	 */
	protected void addInternStyleName(String pStyle)
	{
        if (pStyle == null)
        {
            return;
        }

        if (liInternStyles == null)
	    {
	        liInternStyles = new ArrayList<String>();
	    }

        for (String sPart : pStyle.split(" "))
        {
    	    if (liInternStyles.indexOf(sPart) < 0)
    	    {
    	        liInternStyles.add(sPart);
    	    }
        }
        
	    resource.addStyleName(pStyle);
	}
	
    /**
     * Removes a style name from the intern cache.
     * 
     * @param pStyle the style name
     * @see #addInternStyleName(String)
     */
	protected void removeInternStyleName(String pStyle)
	{
	    if (pStyle == null)
	    {
	        return;
	    }
	    
	    if (liInternStyles != null)
	    {
	        liInternStyles.remove(pStyle);
	        
	        if (liInternStyles.isEmpty())
	        {
	            liInternStyles = null;
	        }
	    }
	    
	    //don't remove the stylename if it was set as "user-defined" style
	    //this could happend e.g. with buttons and images because images set the image name as style
	    //and if the image name is the same as a custom style, it would be removed -> this shouldn't happen
	    if (sOldStyleNames != null && ArrayUtil.contains(sOldStyleNames, pStyle))
	    {
	        return;
	    }
	    
	    resource.removeStyleName(pStyle);
	}
	
	/**
	 * Sets the background-color CSS attribute.
	 * 
	 * @param pColor the color or <code>null</code> to remove the attribute
	 */
	protected void setBackgroundColorAttribute(IColor pColor)
	{
		if (pColor != null) 
		{
			getCssExtension().addAttribute("background-color", ((VaadinColor)pColor).getStyleValueRGB());
			
		}
		else
		{
			getCssExtension().removeAttribute("background-color");
		}		
	}
	
	/**
	 * Sets the background-image CSS attribute.
	 * 
	 * @param pImage the image name or <code>null</code> to remove the attribute
	 */
	protected void setBackgroundImageAttribute(String pImage)
	{
		if (pImage != null)
		{
			getCssExtension().addAttribute("background-image", pImage);	
		}
		else
		{
			getCssExtension().removeAttribute("background-image");
		}
	}

	/**
	 * Sets the box shadow attribute CSS attribute.
	 * 
	 * @param pShadow the box shadow or <code>null</code> to remove the attribute
	 */
	protected void setBoxShadowAttribute(String pShadow)
	{
		if (pShadow != null)
		{
			getCssExtension().addAttribute("box-shadow", pShadow);	
		}
		else
		{
			getCssExtension().removeAttribute("box-shadow");
		}	
	}
	
    /**
     * Configures a newly created {@link CssExtension}.
     * 
     * @param pExtension the extension
     */
    protected void configureCssExtension(CssExtension pExtension)
    {
    }

    /**
     * Configures a newly created {@link AttributesExtension}.
     * 
     * @param pExtension the extension
     */
    protected void configureAttributesExtension(AttributesExtension pExtension)
    {
    }
	
}	// VaadinComponentBase
