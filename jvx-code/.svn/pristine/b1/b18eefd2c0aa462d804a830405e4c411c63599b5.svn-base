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
 * 01.10.2008 - [HM] - creation
 * 05.10.2008 - [JR] - setBackground: fixed NullPointerException if color parameter is null
 * 04.12.2008 - [JR] - removed getResourceToAdd
 * 18.03.2011 - [JR] - #313: component moved/resized implemented
 * 22.07.2011 - [JR] - get/setBounds: check parent in hierarchy
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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
import javax.rad.ui.event.UIComponentEvent;
import javax.rad.ui.event.UIFocusEvent;
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

import com.sibvisions.util.type.ImageUtil;

/**
 * A <em>component</em> is an object having a graphical representation
 * that can be displayed on the screen and that can interact with the
 * user.
 * 
 * @author Martin Handsteiner
 * @param <C> AWT Component implementation
 */
public class AwtComponent<C extends Component> extends AwtResource<C> 
                                               implements IFactoryComponent, 
                                                          IAlignmentConstants, 
                                                          MouseListener, 
                                               			  KeyListener,
                                               			  ComponentListener,
                                               			  FocusListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Container where the component is added. */
	protected IContainer parent	= null;
	
	/** The factory that created this component. */
	protected IFactory factory = null;
	
	/** The Event Source. */
	protected IComponent eventSource = this;

	/** the style. */
	private Style style = null;
	
	/** EventHandler for mouePressed. */
	protected MouseHandler<IMousePressedListener> eventMousePressed = null;
	/** EventHandler for mouseReleased. */
	protected MouseHandler<IMouseReleasedListener> eventMouseReleased = null;
	/** EventHandler for mouseClicked. */
	protected MouseHandler<IMouseClickedListener> eventMouseClicked = null;
	/** EventHandler for mouseEntered. */
	protected MouseHandler<IMouseEnteredListener> eventMouseEntered = null;
	/** EventHandler for mouseExited. */
	protected MouseHandler<IMouseExitedListener> eventMouseExited = null;
	
	/** EventHandler for keyPressed. */
	protected KeyHandler<IKeyPressedListener> eventKeyPressed = null;
	/** EventHandler for keyReleased. */
	protected KeyHandler<IKeyReleasedListener> eventKeyReleased = null;
	/** EventHandler for keyTyped. */
	protected KeyHandler<IKeyTypedListener> eventKeyTyped = null;
	
	/** EventHandler for componentMoved. */
	protected ComponentHandler<IComponentMovedListener> eventComponentMoved = null;
	/** EventHandler for componentResized. */
	protected ComponentHandler<IComponentResizedListener> eventComponentResized = null;
	
	/** EventHandler for focusGained. */
	private FocusHandler<IFocusGainedListener> eventFocusGained = null;
	/** EventHandler for focusLost. */
	private FocusHandler<IFocusLostListener> eventFocusLost = null;
	
	/** the font. */
	private IFont font;
	
	/** X Alignment. */
	private int	horizontalAlignment;
	
	/** Y Alignment. */
	private int	verticalAlignment;

	/** tab index. */
	private Integer	tabIndex = null;

	/** whether the mouse listener was added. */
	protected boolean bMouseListener = false;
	/** whether the key listener was added. */
	protected boolean bKeyListener = false;
	/** whether the component listener was added. */
	protected boolean bComponentListener = false;
	/** whether the focus listener was added. */
	protected boolean bFocusListener = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Create a new instance of <code>AwtComponent</code>.
	 * 
	 * @param pComponent AWT Component implementation.
	 */
	protected AwtComponent(C pComponent)
	{
		super(pComponent);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return resource.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String pName)
	{
		resource.setName(pName);
	}

	/**
	 * {@inheritDoc}
	 */
	public IFactory getFactory()
	{
		return factory;
	}

    /**
     * {@inheritDoc}
     */
    public void setFactory(IFactory pFactory)
    {
        factory = pFactory;
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
		verticalAlignment = pVerticalAlignment;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getPreferredSize()
	{
		Dimension preferredSize = resource.getPreferredSize();
		if (preferredSize == null)
		{
			return null;
		}
		else
		{
			return new AwtDimension(preferredSize);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPreferredSize(IDimension pPreferredSize)
	{
		if (pPreferredSize == null)
		{
			resource.setPreferredSize(null);
		}
		else
		{
			resource.setPreferredSize((Dimension)pPreferredSize.getResource());
		}

		invalidateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPreferredSizeSet()
	{
		return resource.isPreferredSizeSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMinimumSize()
	{
		Dimension minimumSize = resource.getMinimumSize();
		if (minimumSize == null)
		{
			return null;
		}
		else
		{
			return new AwtDimension(minimumSize);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMinimumSize(IDimension pMinimumSize)
	{
		if (pMinimumSize == null)
		{
			resource.setMinimumSize(null);
		}
		else
		{
			resource.setMinimumSize((Dimension)pMinimumSize.getResource());
		}

		invalidateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMinimumSizeSet()
	{
		return resource.isMinimumSizeSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getMaximumSize()
	{
		Dimension maximumSize = resource.getMaximumSize();
		if (maximumSize == null)
		{
			return null;
		}
		else
		{
			return new AwtDimension(maximumSize);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMaximumSize(IDimension pMaximumSize)
	{
		if (pMaximumSize == null)
		{
			resource.setMaximumSize(null);
		}
		else
		{
			resource.setMaximumSize((Dimension)pMaximumSize.getResource());
		}
		
		invalidateLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMaximumSizeSet()
	{
		return resource.isMaximumSizeSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getBackground()
	{
		Color color = resource.getBackground();
		if (color == null)
		{
			return null;
		}
		else
		{
			return new AwtColor(color);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBackground(IColor pBackground)
	{
		if (pBackground == null)
		{
			resource.setBackground(null);
		}
		else
		{
			resource.setBackground((Color)pBackground.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBackgroundSet()
	{
		return resource.isBackgroundSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor getForeground()
	{
		Color color = resource.getForeground();
		if (color == null)
		{
			return null;
		}
		else
		{
			return new AwtColor(color);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	public void setForeground(IColor pForeground)
	{
		if (pForeground == null)
		{
			resource.setForeground(null);
		}
		else
		{
			resource.setForeground((Color)pForeground.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isForegroundSet()
	{
		return resource.isForegroundSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getCursor()
	{
		Cursor cursor = resource.getCursor();
		if (cursor == null)
		{
			return null;
		}
		else
		{
			return new AwtCursor(cursor);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCursor(ICursor pCursor)
	{
		if (pCursor == null)
		{
//			resource.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			resource.setCursor(null);
		}
		else
		{
			resource.setCursor((Cursor)pCursor.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCursorSet()
	{
		return resource.isCursorSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFont getFont()
	{
		Font ftRes = resource.getFont();
		if (ftRes == null)
		{
			font = null;
		}
		else if (font == null || font.getResource() != ftRes)
		{
			font = new AwtFont(ftRes);
		}

		return font;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFont(IFont pFont)
	{
		font = pFont;
		
		if (pFont == null) 
		{
			resource.setFont(null);
		}
		else
		{
			resource.setFont((Font)pFont.getResource());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFontSet()
	{
		return resource.isFontSet();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setToolTipText(String pText)
	{
		// do nothing
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setFocusable(boolean pFocusable)
	{
		resource.setFocusable(pFocusable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFocusable()
	{
		return resource.isFocusable();
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
	}

	/**
	 * {@inheritDoc}
	 */
	public void requestFocus()
    {
		resource.requestFocus();
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
				throw new IllegalArgumentException("Can't unset parent, because this component is still added!");
			}
		}
		else
		{
			if (pParent.indexOf(this) < 0)
			{
				throw new IllegalArgumentException("Can't set parent, because this component is not added!");
			}
		}
  
		parent = pParent;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible()
	{
		return resource.isVisible();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setVisible(boolean pVisible)
	{
		resource.setVisible(pVisible);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled()
	{
		return resource.isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean pEnable)
	{
		resource.setEnabled(pEnable);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocationRelativeTo(IComponent pComponent)
	{
		Point point = resource.getLocation();
		if (pComponent != null)
		{
			Container logicalParent = (Container)pComponent.getResource();
			Container physicalParent = resource.getParent();

			if (logicalParent.isShowing() && logicalParent != physicalParent && physicalParent != null)
			{
				Point logicalLoc = logicalParent.getLocationOnScreen();
				Point physicalLoc = physicalParent.getLocationOnScreen();

				point.x += physicalLoc.x - logicalLoc.x;
				point.y += physicalLoc.y - logicalLoc.y;
			}
		}
		return new AwtPoint(point);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation)
	{
		Point point = (Point)pLocation.getResource();
		if (pComponent != null)
		{
			Container logicalParent = (Container)pComponent.getResource();
			Container physicalParent = resource.getParent();

			if (logicalParent.isShowing() && logicalParent != physicalParent && physicalParent != null)
			{
				Point logicalLoc = logicalParent.getLocationOnScreen();
				Point physicalLoc = physicalParent.getLocationOnScreen();

				point.x -= physicalLoc.x - logicalLoc.x;
				point.y -= physicalLoc.y - logicalLoc.y;
			}
		}
		resource.setLocation(point);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IPoint getLocation()
	{
		return getLocationRelativeTo(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLocation(IPoint pLocation)
	{
		setLocationRelativeTo(parent, pLocation);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension getSize()
	{
		return new AwtDimension(resource.getSize());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSize(IDimension pSize)
	{
	    if (pSize != null)
	    {
    	    resource.setSize((Dimension)pSize.getResource());
    		
    		if (!resource.isShowing())
    		{
    			validate(resource);
    		}
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	public IRectangle getBounds()
	{
		Rectangle bounds = resource.getBounds();
		if (parent != null)
		{
			Container logicalParent = (Container)parent.getResource();
			Container physicalParent = resource.getParent();

			if (logicalParent.isShowing() && physicalParent != null && logicalParent != physicalParent)
			{
				Point logicalLoc = logicalParent.getLocationOnScreen();
				Point physicalLoc = resource.getParent().getLocationOnScreen();

				bounds.x += physicalLoc.x - logicalLoc.x;
				bounds.y += physicalLoc.y - logicalLoc.y;
			}
		}
		return new AwtRectangle(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBounds(IRectangle pBounds)
	{
		Rectangle bounds = (Rectangle)pBounds.getResource();
		if (parent != null)
		{
			Container logicalParent = (Container)parent.getResource();
			Container physicalParent = resource.getParent();

			if (logicalParent.isShowing() && physicalParent != null && logicalParent != physicalParent)
			{
				Point logicalLoc = logicalParent.getLocationOnScreen();
				Point physicalLoc = resource.getParent().getLocationOnScreen();

				bounds.x -= physicalLoc.x - logicalLoc.x;
				bounds.y -= physicalLoc.y - logicalLoc.y;
			}
		}
		resource.setBounds(bounds);

		if (!resource.isShowing())
		{
			validate(resource);
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
        if (pStyle != null && pStyle.getStyleNames().length > 0)
        {
            style = pStyle.clone();
        }
        else
        {
            style = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Style getStyle()
    {
        if (style == null)
        {
            return new Style();
        }
        
        return style.clone();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public MouseHandler<IMousePressedListener> eventMousePressed()
	{
		if (eventMousePressed == null)
		{
			eventMousePressed = new MouseHandler<IMousePressedListener>(IMousePressedListener.class);
			
			addMouseListener();
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
			
			addMouseListener();
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
			
			addMouseListener();
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
			
			addMouseListener();
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
			
			addMouseListener();
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
			
			addKeyListener();
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

			addKeyListener();
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
			
			addKeyListener();
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
			
			addComponentListener();
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
			
			addComponentListener();
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
			
			addFocusListener();
		}
		return eventFocusLost;
    }	
	
	/**
	 * {@inheritDoc}
	 */
	public IImage capture(int pWidth, int pHeight)
	{
		return new AwtImage(null, createImage(resource, pWidth, pHeight));
	}
	
	/**
	 * {@inheritDoc}
	 */
    public void mousePressed(MouseEvent pMouseEvent)
    {
    	if (eventMousePressed != null)
    	{
    		eventMousePressed.dispatchEvent(createMouseEvent(UIMouseEvent.MOUSE_PRESSED, pMouseEvent));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void mouseReleased(MouseEvent pMouseEvent)
    {
    	if (eventMouseReleased != null)
    	{
    		eventMouseReleased.dispatchEvent(createMouseEvent(UIMouseEvent.MOUSE_RELEASED, pMouseEvent));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void mouseClicked(MouseEvent pMouseEvent)
    {
    	if (eventMouseClicked != null)
    	{
    		eventMouseClicked.dispatchEvent(createMouseEvent(UIMouseEvent.MOUSE_CLICKED, pMouseEvent));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void mouseEntered(MouseEvent pMouseEvent)
    {
    	if (eventMouseEntered != null)
    	{
    		eventMouseEntered.dispatchEvent(createMouseEvent(UIMouseEvent.MOUSE_ENTERED, pMouseEvent));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void mouseExited(MouseEvent pMouseEvent)
    {
    	if (eventMouseExited != null)
    	{
    		eventMouseExited.dispatchEvent(createMouseEvent(UIMouseEvent.MOUSE_EXITED, pMouseEvent));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void keyPressed(KeyEvent pKeyEvent)
    {
    	if (eventKeyPressed != null)
    	{
    		eventKeyPressed.dispatchEvent(new UIKeyEvent(eventSource, 
    													 UIKeyEvent.KEY_PRESSED, 
    													 pKeyEvent.getWhen(), 
    													 pKeyEvent.getModifiers(), 
    													 pKeyEvent.getKeyCode(), 
    													 pKeyEvent.getKeyChar()));
    	}
    }

	/**
	 * {@inheritDoc}
	 */
    public void keyReleased(KeyEvent pKeyEvent)
    {
    	if (eventKeyReleased != null)
    	{
    		eventKeyReleased.dispatchEvent(new UIKeyEvent(eventSource, 
     													  UIKeyEvent.KEY_RELEASED, 
     													  pKeyEvent.getWhen(), 
     													  pKeyEvent.getModifiers(), 
    													  pKeyEvent.getKeyCode(), 
    													  pKeyEvent.getKeyChar()));
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void keyTyped(KeyEvent pKeyEvent)
    {
    	if (eventKeyTyped != null)
    	{
    		eventKeyTyped.dispatchEvent(new UIKeyEvent(eventSource, 
   													   UIKeyEvent.KEY_TYPED, 
   													   pKeyEvent.getWhen(), 
   													   pKeyEvent.getModifiers(), 
   													   pKeyEvent.getKeyCode(), 
   													   pKeyEvent.getKeyChar()));
    	}
    }
    
	/**
	 * {@inheritDoc}
	 */
	public void componentHidden(ComponentEvent e)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void componentShown(ComponentEvent e)
	{
	}

	/**
	 * {@inheritDoc}
	 */
	public void componentMoved(ComponentEvent e)
	{
    	if (eventComponentMoved != null)
    	{
    		eventComponentMoved.dispatchEvent(new UIComponentEvent(eventSource, 
    															   UIComponentEvent.COMPONENT_MOVED, 
    															   System.currentTimeMillis(),
    															   0));
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void componentResized(ComponentEvent e)
	{
    	if (eventComponentResized != null)
    	{
    		eventComponentResized.dispatchEvent(new UIComponentEvent(eventSource, 
    															     UIComponentEvent.COMPONENT_RESIZED, 
    															     System.currentTimeMillis(),
    															     0));
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusGained(FocusEvent e)
	{
    	if (eventFocusGained != null)
    	{
    		eventFocusGained.dispatchEvent(new UIFocusEvent(eventSource, 
    															     UIFocusEvent.FOCUS_GAINED, 
    															     System.currentTimeMillis(),
    															     0));
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusLost(FocusEvent e)
	{
    	if (eventFocusLost != null)
    	{
    		eventFocusLost.dispatchEvent(new UIFocusEvent(eventSource, 
    																 UIFocusEvent.FOCUS_LOST, 
    															     System.currentTimeMillis(),
    															     0));
    	}
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
	 * Creates a UIMouseEvent with corrected location.
	 * 
	 * @param pId the new mouse event id.
	 * @param pMouseEvent plattform dependent mouse event.
	 * @return the UIMouseEvent.
	 */
	protected UIMouseEvent createMouseEvent(int pId, MouseEvent pMouseEvent)
	{
		int x = pMouseEvent.getX();
		int y = pMouseEvent.getY();
		Component comp = pMouseEvent.getComponent();
		Component eventSourceResource = (Component)eventSource.getResource(); 
		while (comp != eventSourceResource)
		{
			x += comp.getX();
			y += comp.getY();
			comp = comp.getParent();
		}
		return new UIMouseEvent(eventSource, 
				 pId, 
			     pMouseEvent.getWhen(), 
			     pMouseEvent.getModifiers(), 
			     x, 
			     y, 
			     pMouseEvent.getClickCount(), 
			     pMouseEvent.isPopupTrigger());
	}
	
	/**
	 * Adds a mouse listener for this component, if not already added.
	 */
	protected void addMouseListener()
	{
		if (!bMouseListener)
		{
			bMouseListener = true;
			resource.addMouseListener(this);
		}
	}
	
	/**
	 * Adds a key listener for this component, if not already added.
	 */
	protected void addKeyListener()
	{
		if (!bKeyListener)
		{
			bKeyListener = true;
			resource.addKeyListener(this);
		}
	}
    
	/**
	 * Adds a component listener for this component, if not already added.
	 */
	protected void addComponentListener()
	{
		if (!bComponentListener)
		{
			bComponentListener = true;
			resource.addComponentListener(this);
		}
	}

	/**
	 * Adds a component listener for this component, if not already added.
	 */
	protected void addFocusListener()
	{
		if (!bFocusListener)
		{
			bFocusListener = true;
			resource.addFocusListener(this);
		}
	}

	/**
	 * If container is already invalid, the layout is no longer informed about new calculation.
	 */
	public void invalidateLayout()
	{
		Container cont = resource.getParent(); 
		while (cont != null && cont.getLayout() != null)
		{
			cont.invalidate();
			
			cont = cont.getParent();
		}
	}
	
	/**
	 * Layouts the component.
	 * @param pComponent the component.
	 */
	public void validate(Component pComponent)
	{
		pComponent.doLayout();
		
		if (pComponent instanceof Container)
		{
			Container cont = (Container)pComponent;
			
			for (int i = 0; i < cont.getComponentCount(); i++)
			{
				validate(cont.getComponent(i));
			}
		}
	}
	
	/**
	 * Creates an image from a component.
	 * 
	 * @param pComponent the component
	 * @param pWidth the expected image width
	 * @param pHeight the expected image height
	 * @return the component image
	 */
	protected Image createImage(Component pComponent, int pWidth, int pHeight)
	{
		BufferedImage image = new BufferedImage(pComponent.getWidth(), pComponent.getHeight(), BufferedImage.BITMASK);
		
		Graphics g = image.getGraphics();
		
		if (g instanceof Graphics2D)
		{
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        //changes the size -> don't set
	        //g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}
		
		pComponent.validate();
		pComponent.paint(g);
		g.dispose();

		return ImageUtil.getScaledImage(image, pWidth, pHeight, false);
	}

}	// AwtComponent
