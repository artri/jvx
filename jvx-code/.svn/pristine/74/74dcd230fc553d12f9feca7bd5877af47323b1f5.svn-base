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
 * 21.07.2009 - [JR] - isDisposed defined
 */
package javax.rad.ui.container;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowClosedListener;
import javax.rad.ui.event.type.window.IWindowClosingListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;
import javax.rad.ui.event.type.window.IWindowDeiconifiedListener;
import javax.rad.ui.event.type.window.IWindowIconifiedListener;
import javax.rad.ui.event.type.window.IWindowOpenedListener;
import javax.rad.util.TranslationMap;

/**
 * Platform and technology independent Window definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see	java.awt.Window
 * @see	javax.swing.JWindow
 */
public interface IWindow extends IContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Causes this IWindow to be sized to fit the preferred size
	 * and layouts of its subcomponents. If the window and/or its owner
	 * are not yet displayable, both are made displayable before
	 * calculating the preferred size. The IWindow will be validated
	 * after the preferredSize is calculated.
	 */
	public void pack();

	/**
	 * Releases all of the native screen resources used by this
	 * <code>IWindow</code>, its subcomponents, and all of its owned
	 * children. That is, the resources for these <code>IComponent</code>s
	 * will be destroyed, any memory they consume will be returned to the
	 * OS, and they will be marked as undisplayable.
	 * <p>
	 * The <code>IWindow</code> and its subcomponents can be made displayable
	 * again by rebuilding the native resources with a subsequent call to
	 * <code>pack</code> or <code>show</code>. The states of the recreated
	 * <code>Window</code> and its subcomponents will be identical to the
	 * states of these objects at the point where the <code>IWindow</code>
	 * was disposed (not accounting for additional modifications between
	 * those actions).
	 * 
	 * @see #pack
	 * @see #setVisible(boolean)
	 */
	public void dispose();

	/**
	 * Checks whether the window is disposed already.
	 * 
	 * @return <code>true</code> if the window is disposed, otherwiese <code>false</code>
	 */
	public boolean isDisposed();
	
	/**
	 * Returns whether this IWindow is active. Only a Frame or a Dialog may be
	 * active. The native windowing system may denote the active Window or its
	 * children with special decorations, such as a highlighted title bar. The
	 * active Window is always either the focused Window, or the first Frame or
	 * Dialog that is an owner of the focused Window.
	 *
	 * @return whether this is the active Window.
	 */
	public boolean isActive();

	/**
	 * If this IWindow is visible, brings this IWindow to the front and may make
	 * it the focused Window.
	 * <p>
	 * Places this IWindow at the top of the stacking order and shows it in
	 * front of any other Windows in this VM. No action will take place if this
	 * Window is not visible. Some platforms do not allow Windows which own
	 * other Windows to appear on top of those owned Windows. Some platforms
	 * may not permit this VM to place its Windows above windows of native
	 * applications, or Windows of other VMs. This permission may depend on
	 * whether a Window in this VM is already focused. Every attempt will be
	 * made to move this Window as high as possible in the stacking order;
	 * however, developers should not assume that this method will move this
	 * Window above all other windows in every situation.
	 * <p>
	 * Because of variations in native windowing systems, no guarantees about
	 * changes to the focused and active Windows can be made. Developers must
	 * never assume that this Window is the focused or active Window until this
	 * Window receives a WINDOW_GAINED_FOCUS or WINDOW_ACTIVATED event. On
	 * platforms where the top-most window is the focused window, this method
	 * will <b>probably</b> focus this Window, if it is not already focused. On
	 * platforms where the stacking order does not typically affect the focused
	 * window, this method will <b>probably</b> leave the focused and active
	 * Windows unchanged.
	 * <p>
	 * If this method causes this Window to be focused, and this Window is a
	 * Frame or a Dialog, it will also become activated. If this Window is
	 * focused, but it is not a Frame or a Dialog, then the first Frame or
	 * Dialog that is an owner of this Window will be activated.
	 * <p>
	 * If this window is blocked by modal dialog, then the blocking dialog
	 * is brought to the front and remains above the blocked window.
	 *
	 * @see #toBack
	 */
	public void toFront();

	/**
	 * If this IWindow is visible, sends this IWindow to the back and may cause
	 * it to lose focus or activation if it is the focused or active Window.
	 * <p>
	 * Places this IWindow at the bottom of the stacking order and shows it
	 * behind any other Windows in this VM. No action will take place is this
	 * Window is not visible. Some platforms do not allow Windows which are
	 * owned by other Windows to appear below their owners. Every attempt will
	 * be made to move this Window as low as possible in the stacking order;
	 * however, developers should not assume that this method will move this
	 * Window below all other windows in every situation.
	 * <p>
	 * Because of variations in native windowing systems, no guarantees about
	 * changes to the focused and active Windows can be made. Developers must
	 * never assume that this Window is no longer the focused or active Window
	 * until this Window receives a WINDOW_LOST_FOCUS or WINDOW_DEACTIVATED
	 * event. On platforms where the top-most window is the focused window,
	 * this method will <b>probably</b> cause this Window to lose focus. In
	 * that case, the next highest, focusable Window in this VM will receive
	 * focus. On platforms where the stacking order does not typically affect
	 * the focused window, this method will <b>probably</b> leave the focused
	 * and active Windows unchanged.
	 *
	 * @see #toFront
	 */
	public void toBack();
	
    /**
     * Centers the window relative to the specified
     * component. If the component is not currently showing,
     * or <code>pComponent</code> is <code>null</code>, the 
     * window is centered on the screen.
     * 
     * @param pComponent the component in relation to which the window's location
     *                   is determined
     */	
	public void centerRelativeTo(IComponent pComponent);
	
    /**
     * Sets the translation for the window. A window might have components which
     * needs to be translated, e.g. Buttons or Menu in titlebar.
     * 
     * @param pTranslation the translation mapping
     */
    public void setTranslation(TranslationMap pTranslation);
    
    /**
     * Gets the translation for the window.
     * 
     * @return the translation mapping
     */
    public TranslationMap getTranslation();
	
    /**
     * The EventHandler for the window opened event.
     * 
     * @return the EventHandler for the window opened event.
     */
	public WindowHandler<IWindowOpenedListener> eventWindowOpened();
	
    /**
     * The EventHandler for the window closing event.
     * 
     * @return the EventHandler for the window closing event.
     */
	public WindowHandler<IWindowClosingListener> eventWindowClosing();
	
    /**
     * The EventHandler for the window closed event.
     * 
     * @return the EventHandler for the window closed event.
     */
	public WindowHandler<IWindowClosedListener> eventWindowClosed();
	
    /**
     * The EventHandler for the window iconified event.
     * 
     * @return the EventHandler for the window iconified event.
     */
	public WindowHandler<IWindowIconifiedListener> eventWindowIconified();
	
    /**
     * The EventHandler for the window deiconified event.
     * 
     * @return the EventHandler for the window deiconified event.
     */
	public WindowHandler<IWindowDeiconifiedListener> eventWindowDeiconified();
	
    /**
     * The EventHandler for the window activated event.
     * 
     * @return the EventHandler for the window activated event.
     */
	public WindowHandler<IWindowActivatedListener> eventWindowActivated();
	
    /**
     * The EventHandler for the window deactivated event.
     * 
     * @return the EventHandler for the window deactivated event.
     */
	public WindowHandler<IWindowDeactivatedListener> eventWindowDeactivated();
	
}	// IWindow
