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
 * 25.01.2009 - [JR] - capture defined
 * 18.03.2011 - [JR] - #313: component moved/resized implemented
 */
package javax.rad.ui;

import javax.rad.ui.event.ComponentHandler;
import javax.rad.ui.event.FocusHandler;
import javax.rad.ui.event.KeyHandler;
import javax.rad.ui.event.MouseHandler;
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
import javax.rad.util.INamedObject;

/**
 * Platform and technology independent component definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Component
 * @see	javax.swing.JComponent
 */
public interface IComponent extends IResource, 
                                    INamedObject
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the name of the object.
	 * 
	 * Implementing classes need to make sure that no name is set during the
	 * creation of the component. A unique name (based on the position of
	 * the component in the component tree) will be generated and set when
	 * the component is added, but only if the name of the component is {@code null}.
	 * 
	 * @return this component's name
	 * @see #setName
	 */
	public String getName();

	/**
	 * Sets the name of the object to the specified string.
	 * 
	 * @param pName the string that is to be this 
	 *        component's name
	 * @see #getName
	 */
	public void setName(String pName);

	/**
	 * Gets the factory that created this NamedObject.
	 * 
	 * @return the factory that created this NamedObject
	 */
	public IFactory getFactory();

	/* 
	 * Properties for Layouting
	 */

	/**
	 * Gets the preferred size of this component.
	 * 
	 * @return an <code>IDimension</code> object indicating this component's preferred size
	 * @see #getMinimumSize
	 * @see java.awt.LayoutManager
	 */
	public IDimension getPreferredSize();

	/**
	 * Sets the preferred size of this component to a constant
	 * value.  Subsequent calls to <code>getPreferredSize</code> will always
	 * return this value.  Setting the preferred size to <code>null</code>
	 * restores the default behavior.
	 *
	 * @param pPreferredSize the new preferred size, or null
	 * @see #getPreferredSize
	 * @see #isPreferredSizeSet
	 */
	public void setPreferredSize(IDimension pPreferredSize);

	/**
	 * Returns true if the preferred size has been set to a
	 * non-<code>null</code> value otherwise returns false.
	 *
	 * @return true if <code>setPreferredSize</code> has been invoked
	 *         with a non-null value.
	 */
	public boolean isPreferredSizeSet();

	/**
	 * Gets the mininimum size of this component.
	 * 
	 * @return an <code>IDimension</code> object indicating this component's minimum size
	 * @see #getPreferredSize
	 * @see java.awt.LayoutManager
	 */
	public IDimension getMinimumSize();

	/**
	 * Sets the minimum size of this component to a constant
	 * value.  Subsequent calls to <code>getMinimumSize</code> will always
	 * return this value.  Setting the minimum size to <code>null</code>
	 * restores the default behavior.
	 *
	 * @param pMinimumSize the new minimum size of this component
	 * @see #getMinimumSize
	 * @see #isMinimumSizeSet
	 */
	public void setMinimumSize(IDimension pMinimumSize);

	/**
	 * Returns whether or not <code>setMinimumSize</code> has been
	 * invoked with a non-null value.
	 *
	 * @return true if <code>setMinimumSize</code> has been invoked with a
	 *         non-null value.
	 */
	public boolean isMinimumSizeSet();

	/**
	 * Gets the maximum size of this component.
	 * 
	 * @return an <code>IDimension</code> object indicating this component's maximum size
	 * @see #getMinimumSize
	 * @see #getPreferredSize
	 * @see java.awt.LayoutManager
	 */
	public IDimension getMaximumSize();

	/**
	 * Sets the maximum size of this component to a constant
	 * value.  Subsequent calls to <code>getMaximumSize</code> will always
	 * return this value.  Setting the maximum size to <code>null</code>
	 * restores the default behavior.
	 *
	 * @param pMaximumSize a <code>IDimension</code> containing the 
	 *        desired maximum allowable size
	 * @see #getMaximumSize
	 * @see #isMaximumSizeSet
	 */
	public void setMaximumSize(IDimension pMaximumSize);

	/**
	 * Returns true if the maximum size has been set to a non-<code>null</code>
	 * value otherwise returns false.
	 *
	 * @return true if <code>maximumSize</code> is non-<code>null</code>,
	 *          false otherwise
	 */
	public boolean isMaximumSizeSet();

	/* 
	 * Properties for Look & Feel
	 */

	/**
	 * Gets the background color of this component.
	 * 
	 * @return this component's background color; if this component does
	 *         not have a background color, the background color of its 
	 *         parent is returned
	 * @see #setBackground
	 */
	public IColor getBackground();

	/**
	 * Sets the background color of this component.
	 * <p>
	 * The background color affects each component differently and the
	 * parts of the component that are affected by the background color 
	 * may differ between operating systems.
	 *
	 * @param pBackground the color to become this component's color;
	 *        if this parameter is <code>null</code>, then this
	 *        component will inherit the background color of its parent
	 * @see #getBackground
	 */
	public void setBackground(IColor pBackground);

	/**
	 * Returns whether the background color has been explicitly set for this
	 * Component. If this method returns <code>false</code>, this Component is
	 * inheriting its background color from an ancestor.
	 *
	 * @return <code>true</code> if the background color has been explicitly
	 *         set for this Component; <code>false</code> otherwise.
	 */
	public boolean isBackgroundSet();

	/**
	 * Gets the foreground color of this component.
	 * 
	 * @return this component's foreground color; if this component does
	 *         not have a foreground color, the foreground color of its parent
	 *         is returned
	 * @see #setForeground
	 */
	public IColor getForeground();

	/**
	 * Sets the foreground color of this component.
	 * 
	 * @param pForeground the color to become this component's 
	 *        foreground color; if this parameter is <code>null</code>
	 *        then this component will inherit the foreground color of 
	 *        its parent
	 * @see #getForeground
	 */
	public void setForeground(IColor pForeground);

	/**
	 * Returns whether the foreground color has been explicitly set for this
	 * Component. If this method returns <code>false</code>, this Component is
	 * inheriting its foreground color from an ancestor.
	 *
	 * @return <code>true</code> if the foreground color has been explicitly
	 *         set for this Component; <code>false</code> otherwise.
	 */
	public boolean isForegroundSet();

	/**
	 * Gets the <code>ICursor</code> set in the component. If the component does
	 * not have a cursor set, the cursor of its parent is returned.
	 * If no cursor is set in the entire hierarchy, 
	 * <code>Cursor.DEFAULT_CURSOR</code> is returned.
	 * 
	 * @return the <code>ICursor</code> set in the component
	 * @see #setCursor
	 */
	public ICursor getCursor();

	/**
	 * Sets the cursor image to the specified cursor.  This cursor
	 * image is displayed when the <code>contains</code> method for
	 * this component returns true for the current cursor location, and
	 * this Component is visible, displayable, and enabled. Setting the
	 * cursor of a <code>Container</code> causes that cursor to be displayed
	 * within all of the container's subcomponents, except for those
	 * that have a non-<code>null</code> cursor. 
	 * <p>
	 * The method may have no visual effect if the Java platform
	 * implementation and/or the native system do not support
	 * changing the mouse cursor shape.
	 * 
	 * @param pCursor One of the constants defined by the <code>Cursor</code> 
	 *        class; if this parameter is <code>null</code> then this component 
	 *        will inherit the cursor of its parent
	 * @see #isEnabled()
	 * @see #getCursor()
	 * @see java.awt.Toolkit#createCustomCursor
	 * @see ICursor
	 */
	public void setCursor(ICursor pCursor);

	/**
	 * Returns whether the cursor has been explicitly set for this Component.
	 * If this method returns <code>false</code>, this Component is inheriting
	 * its cursor from an ancestor.
	 *
	 * @return <code>true</code> if the cursor has been explicitly set for this
	 *         Component; <code>false</code> otherwise.
	 */
	public boolean isCursorSet();

	/**
	 * Gets the font of this component.
	 * 
	 * @return this component's font; if a font has not been set
	 *         for this component, the font of its parent is returned
	 * @see #setFont
	 */
	public IFont getFont();

	/**
	 * Sets the <code>IFont</code> of this component.
	 * 
	 * @param pFont the <code>IFont</code> to become this component's font;
	 *        if this parameter is <code>null</code> then this
	 *        component will inherit the font of its parent
	 * @see #getFont
	 */
	public void setFont(IFont pFont);

	/**
	 * Returns whether the font has been explicitly set for this Component. If
	 * this method returns <code>false</code>, this Component is inheriting its
	 * font from an ancestor.
	 *
	 * @return <code>true</code> if the font has been explicitly set for this
	 *         Component; <code>false</code> otherwise.
	 */
	public boolean isFontSet();

    /**
     * Returns the tooltip string that has been set with
     * <code>setToolTipText</code>.
     *
     * @return the text of the tool tip
     * @see #setToolTipText(String)
     */
	public String getToolTipText();
	
    /**
     * Registers the text to display in a tool tip.
     * The text displays when the cursor lingers over the component.
     * 
	 * @param pText the string to display; if the text is <code>null</code>,
     *              the tool tip is turned off for this component
	 */
	public void setToolTipText(String pText);

	/**
	 * Sets the focusable state of this component to the specified value.
	 * 
	 * @param pFocusable indicates whether this Component is focusable
	 */
	public void setFocusable(boolean pFocusable);
	
	/**
	 * Returns whether this component can be focused.
	 * 
	 * @return <code>true</code> if this Component is focusable,
     *         <code>false</code> otherwise.
	 */
	public boolean isFocusable();

	/**
	 * Returns the desired tab index.
	 * If several editors have the same tab index, or it is null the behaviour is technology dependend.
	 * 
	 * @return the desired tab index.
	 */
	public Integer getTabIndex();

	/**
	 * Sets the desired tab index.
	 * If several editors have the same tab index, or it is null the behaviour is technology dependend.
	 * 
	 * @param pTabIndex the desired tab index.
	 */
	public void setTabIndex(Integer pTabIndex);

    /**
     * Requests that this Component get the input focus, and that this
     * Component's top-level ancestor become the focused Window. This component
     * must be displayable, visible, and focusable for the request to be
     * granted. Every effort will be made to honor the request; however, in
     * some cases it may be impossible to do so. Developers must never assume
     * that this Component is the focus owner until this Component receives a
     * FOCUS_GAINED event. If this request is denied because this Component's
     * top-level Window cannot become the focused Window, the request will be
     * remembered and will be granted when the Window is later focused by the
     * user.
     * <p>
     * This method cannot be used to set the focus owner to no Component at
     * all. Use <code>KeyboardFocusManager.clearGlobalFocusOwner()</code>
     * instead.
     * <p>
     * Because the focus behavior of this method is platform-dependent,
     * developers are strongly encouraged to use
     * <code>requestFocusInWindow</code> when possible.
     */
	public void requestFocus();

	/* 
	 * Operational Properties and Functions 
	 */

	/**
	 * Gets the parent of this component.
	 * 
	 * @return the parent container of this component
	 */
	public IContainer getParent();
	
	/**
	 * Sets the parent of this component.
	 * 
	 * @param pParent the parent container of this component
	 */
	public void setParent(IContainer pParent);

	/**
	 * Determines whether this component should be visible when its
	 * parent is visible. Components are 
	 * initially visible, with the exception of top level components such 
	 * as <code>Frame</code> objects.
	 * 
	 * @return <code>true</code> if the component is visible,
	 *         <code>false</code> otherwise
	 * @see #setVisible
	 */
	public boolean isVisible();

	/**
	 * Shows or hides this component depending on the value of parameter
	 * <code>b</code>.
	 * @param pVisible if <code>true</code>, shows this component; otherwise, 
	 *        hides this component
	 * @see #isVisible
	 */
	public void setVisible(boolean pVisible);

    /**
     * Enables or disables this component, depending on the value of the
     * parameter <code>pEnable</code>. An enabled component can respond to user
     * input and generate events. Components are enabled initially by default.
     *
     * @param pEnable if <code>true</code>, this component is 
     *            enabled; otherwise this component is disabled
     * @see #isEnabled
     */
	public void setEnabled(boolean pEnable);

    /**
     * Determines whether this component is enabled. An enabled component
     * can respond to user input and generate events. Components are
     * enabled initially by default. A component may be enabled or disabled by
     * calling its <code>setEnabled</code> method.
     * 
     * @return <code>true</code> if the component is enabled, <code>false</code> otherwise
     * @see #setEnabled
     */
	public boolean isEnabled();
	
    /**
     * Gets the location relative to the given component.
     * 
     * @param pComponent the component in relation to which the window's location is determined.
     * @return the location.
     */	
	public IPoint getLocationRelativeTo(IComponent pComponent);
	
    /**
     * Sets the location relative to the given component.
     * 
     * @param pComponent the component in relation to which the window's location is determined.
     * @param pLocation the location.
     */	
	public void setLocationRelativeTo(IComponent pComponent, IPoint pLocation);
	
	/**
	 * Gets the location of this component in the form of a
	 * point specifying the component's top-left corner.
	 * The location will be relative to the parent's coordinate space.
	 * <p>
	 * Due to the asynchronous nature of native event handling, this
	 * method can return outdated values (for instance, after several calls
	 * of <code>setLocation()</code> in rapid succession).  For this
	 * reason, the recommended method of obtaining a component's position is 
	 * within <code>java.awt.event.ComponentListener.componentMoved()</code>,
	 * which is called after the operating system has finished moving the 
	 * component.
	 * </p>
	 * @return an instance of <code>IPoint</code> representing
	 *         the top-left corner of the component's bounds in
	 *         the coordinate space of the component's parent
	 * @see #setLocation(IPoint)
	 */
	public IPoint getLocation();

	/**
	 * Moves this component to a new location. The top-left corner of
	 * the new location is specified by point <code>p</code>. Point
	 * <code>p</code> is given in the parent's coordinate space.
	 * 
	 * @param pLocation the point defining the top-left corner 
	 *        of the new location, given in the coordinate space of this 
	 *        component's parent
	 * @see #getLocation()
	 * @see #setBounds(IRectangle)
	 */
	public void setLocation(IPoint pLocation);

	/**
	 * Returns the size of this component in the form of a
	 * <code>IDimension</code> object. The <code>height</code>
	 * field of the <code>IDimension</code> object contains
	 * this component's height, and the <code>width</code>
	 * field of the <code>IDimension</code> object contains
	 * this component's width.
	 * 
	 * @return an <code>IDimension</code> object that indicates the
	 *         size of this component
	 * @see #setSize
	 */
	public IDimension getSize();

	/**
	 * Resizes this component so that it has width <code>d.width</code>
	 * and height <code>d.height</code>.
	 * 
	 * @param pSize the <code>IDimension</code> specifying the new size 
	 *        of this component
	 * @see #setSize
	 * @see #setBounds
	 */
	public void setSize(IDimension pSize);

	/**
	 * Gets the bounds of this component in the form of a
	 * <code>IRectangle</code> object. The bounds specify this
	 * component's width, height, and location relative to
	 * its parent.
	 * 
	 * @return a rectangle indicating this component's bounds
	 * @see #setBounds
	 * @see #getLocation
	 * @see #getSize
	 */
	public IRectangle getBounds();

	/**
	 * Moves and resizes this component to conform to the new
	 * bounding rectangle <code>pBounds</code>. This component's new
	 * position is specified by <code>pBounds.x</code> and <code>pBounds.y</code>,
	 * and its new size is specified by <code>pBounds.width</code> and
	 * <code>pBounds.height</code>
	 * 
	 * @param pBounds the new bounding rectangle for this component
	 * @see #getBounds
	 * @see #setLocation(IPoint)
	 * @see #setSize(IDimension)
	 */
	public void setBounds(IRectangle pBounds);

	/**
	 * Gets the wanted event source for this component.
	 * This gives wrapper implementations like genui a 
	 * simple mechanism to control the source of component events. 
	 * 
	 * @return the event source 
	 */
	public IComponent getEventSource();

	/**
	 * Sets the wanted event source for this component.
	 * This gives wrapper implementations like genui a 
	 * simple mechanism to control the source of component events. 
	 * 
	 * @param pEventSource the event source 
	 */
	public void setEventSource(IComponent pEventSource);

	/**
	 * Sets the style definition. A style can be used to customize a component.
	 * 
	 * @param pStyle the style
	 */
    public void setStyle(Style pStyle);
    
    /**
     * Gets the style definition.
     * 
     * @return the style definition
     * @see #setStyle(Style)
     */
    public Style getStyle();
	
    /**
     * The EventHandler for the mouse pressed event.
     * 
     * @return the EventHandler for the mouse pressed event.
     */
	public MouseHandler<IMousePressedListener> eventMousePressed();
	
    /**
     * The EventHandler for the mouse released event.
     * 
     * @return the EventHandler for the mouse released event.
     */
	public MouseHandler<IMouseReleasedListener> eventMouseReleased();
	
    /**
     * The EventHandler for the mouse clicked event.
     * 
     * @return the EventHandler for the mouse clicked event.
     */
	public MouseHandler<IMouseClickedListener> eventMouseClicked();
	
    /**
     * The EventHandler for the mouse entered event.
     * 
     * @return the EventHandler for the mouse entered event.
     */
	public MouseHandler<IMouseEnteredListener> eventMouseEntered();
	
    /**
     * The EventHandler for the mouse exited event.
     * 
     * @return the EventHandler for the mouse exited event.
     */
	public MouseHandler<IMouseExitedListener> eventMouseExited();
	
    /**
     * The EventHandler for the key pressed event.
     * 
     * @return the EventHandler for the key pressed event.
     */
	public KeyHandler<IKeyPressedListener> eventKeyPressed();
	
    /**
     * The EventHandler for the key released event.
     * 
     * @return the EventHandler for the key released event.
     */
	public KeyHandler<IKeyReleasedListener> eventKeyReleased();
	
    /**
     * The EventHandler for the key typed event.
     * 
     * @return the EventHandler for the key typed event.
     */
	public KeyHandler<IKeyTypedListener> eventKeyTyped();
	
	/**
	 * The ComponentHandler for the resized event.
	 * 
	 * @return the ComponentHandler for the resized event.
	 */
	public ComponentHandler<IComponentResizedListener> eventComponentResized();
	
	/**
	 * The ComponentHandler for the moved event.
	 * 
	 * @return the ComponentHandler for the moved event.
	 */
	public ComponentHandler<IComponentMovedListener> eventComponentMoved();
	
	/**
	 * The FocusHandler for the focus gained event.
	 * 
	 * @return the FocusHandler for the focus gained event.
	 */
	public FocusHandler<IFocusGainedListener> eventFocusGained();
	
	/**
	 * The FocusHandler for the focus lost event.
	 * 
	 * @return the FocusHandler for the focus lost event.
	 */
	public FocusHandler<IFocusLostListener> eventFocusLost();
	
	/**
	 * Creates an image/screenshot of the component.
	 *
	 * @param iWidth the expected width
	 * @param iHeight the expected height
	 * @return the image/screenshot
	 */
	public IImage capture(int iWidth, int iHeight);
	
}	// IComponent
