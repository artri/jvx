/*
 * Copyright 2016 SIB Visions GmbH
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
 * 07.03.2016 - [RZ] - creation
 * 08.04.2019 - [DJ] - #1941: vertical alignment in labels corrected
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout.BorderLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout.FlowLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout.FormLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout.GridLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout.NullLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.panel.LayoutedPanel;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.PostLayoutListener;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.label.LabelConnector;

/**
 * The {@link LayoutedPanelConnector} is the main implementation of the
 * {@link VLayoutedPanel}.
 * 
 * @author Robert Zenz
 */
@Connect(LayoutedPanel.class)
public class LayoutedPanelConnector extends AbstractLayoutConnector implements SimpleManagedLayout, PostLayoutListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cached {@link Size} of the canvas. */
	private Size cachedCanvasSize = null;
	
	/** The cached {@link Size} of the panel. */
	private Size cachedPanelSize = null;
	
	/** The name of the current layout that is used. */
	private String currentLayoutName = null;
	
	/** If debugging is active. */
	private boolean debugging = false;
	
	/** A counter how often the layout method was called. */
	private int debugLayoutCounter = 0;
	
	/** A counter how often the post method was called. */
	private int debugPostCounter = 0;
	
	/** The used {@link IClientSideLayout}. */
	private IClientSideLayout layout;
	
	/** Map containing wrapper widgets needed for correct css layouting. */
	private Map<Widget, SimplePanel> wrapperWidgets = new HashMap<Widget, SimplePanel>();

	/** static changed id to determine any user made change, to avoid endless loops without user. */
    private static int changedId = 0;
    
    /** the current changed id to determine any user made change, to avoid endless loops without user. */
    private int currentChangedId = -1;
    
    /** 1 repeat of same layout path has to be allowed to avoid any layout chaos. */
    private int postLayoutCount = 0;
    
    /** The last panel size. */
    private Size lastPanelSize = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
		Window.addResizeHandler(new ResizeHandler() 
		{
			@Override
			public void onResize(ResizeEvent event) 
			{
				layoutChanged("Window Resize");
			}
		}); 
    }
    
	/**
	 * Creates a new instance of {@link LayoutedPanelConnector}.
	 */
	public LayoutedPanelConnector()
	{
		super();
		
		debugging = determineIfDebuggingIsActive();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent)
	{
		VLayoutedPanel widget = getWidget();
		Widget wChild;
		
        for (ComponentConnector child : connectorHierarchyChangeEvent.getOldChildren()) 
        {
            if (child.getParent() != this) 
            {
            	wChild = child.getWidget();

            	if (child instanceof LabelConnector)
                {
                    SimplePanel simplePanel = wrapperWidgets.remove(wChild);
                    simplePanel.remove(wChild);
                    
                    wChild.getElement().removeAttribute("wrapped");
                    
                    widget.remove(simplePanel);
                }
                else
                {
                    widget.remove(wChild);
                }
            }
        }

        List<ComponentConnector> liChildren = getChildComponents();
        
        ComponentConnector child;
        
        for (int i = 0, cnt = liChildren.size(); i < cnt; i++) 
        {
        	child = liChildren.get(i);
        	
            wChild = child.getWidget();
            
            if (child instanceof LabelConnector)
            {
                //Labels are wrapped by SimplePanel
                //and both panel and label get special display properties
                //in order to get vertical alignment working.
            	
            	SimplePanel simplePanel = wrapperWidgets.get(wChild);
            	if (simplePanel == null)
            	{
	                simplePanel = new SimplePanel(wChild);
	
	                Element childElement = wChild.getElement();
	                Element panelElement = simplePanel.getElement();
	                
	                panelElement.getStyle().setDisplay(Display.TABLE);
	                
	                childElement.getStyle().setDisplay(Display.TABLE_CELL);
	                childElement.setAttribute("wrapped", "true");

	                wrapperWidgets.put(wChild, simplePanel);
            	}
            	
                widget.addOrMove(simplePanel, i);
            }
            else
            {
            	widget.addOrMove(wChild, i);
            }
        }

		layoutChanged("Hierachy change");

		postLayout(); // This is necessary, because setting components visible(false) will change the hierarchy, but postLayout is not called by vaadin...
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LayoutedPanelState getState()
	{
		return (LayoutedPanelState)super.getState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VLayoutedPanel getWidget()
	{
		return (VLayoutedPanel)super.getWidget();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		super.init();

		// We need to register our element as dependency so that our layout
		// method is triggered every time that element changes its size.
		getLayoutManager().registerDependency(this, getWidget().getElement());
		
	}

//	native static void consoleLog(String message) /*-{
//	    try {
//	        console.log(message);
//	    } catch (e) {
//	    }
//	}-*/;    
	
	/**
	 * Increases the layout changed counter.
	 * 
	 * @param pReason the change reason
	 */
    public static final void layoutChanged(String pReason)
    {
//    	consoleLog("layoutChanged " + pReason);
    	changedId++;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layout()
	{
		debugLayoutCounter++;
		setDebugAttribute(this, "layout-counter", Integer.valueOf(debugLayoutCounter));
		setDebugAttribute(this, "constraints-count", Integer.valueOf(getState().constraints.size()));
		
		if (debugging)
		{
			for (ComponentConnector connector : getChildComponents())
			{
				setDebugAttribute(connector, "constraint", getConstraint(connector));
			}
		}
		
		updateLayout();
		
        lastPanelSize = null; // init for  first post layout
        
		if (layout != null)
		{
    		checkDirectParentSize();
    		
//    		consoleLog("LayoutedPanelConnector layout " + getParent().getWidget().getElement().getId() + "  " + getState().layoutName + "  " + 
//    		        getPanelSize() + " #" + getChildComponents().size());
    
    		layout.layoutComponents(true);
		}
		
        clearCaches();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postLayout()
	{
		debugPostCounter++;
		setDebugAttribute(this, "post-counter", Integer.valueOf(debugPostCounter));
		
		updateLayout();
		
		if (layout != null)
		{
			Size newPanelSize = getPanelSize();
			// Check, if something is changed.
			if (currentChangedId != changedId 
				|| lastPanelSize == null 
				|| newPanelSize.width != lastPanelSize.width 
				|| newPanelSize.height != lastPanelSize.height)
			{
//				consoleLog("LayoutedPanelConnector postLayout " + getParent().getWidget().getElement().getId() + "  " + getState().layoutName + ": do Layout!!!! " + 
//				        newPanelSize + " " + lastPanelSize + " | " + changedId + " " + currentChangedId + " #" + getChildComponents().size());
				
				lastPanelSize = newPanelSize;
				currentChangedId = changedId;
				postLayoutCount = 0;
			}
			else // We made already exact this layout calculation!
			{
                postLayoutCount++;
				if (postLayoutCount > 2) // There is a special case with tabset in scrollpanel in tabset, that needs 4 layout paths, to ensure correct size. 
				{
//                  consoleLog("LayoutedPanelConnector postLayout " + getParent().getWidget().getElement().getId() + "  " + getState().layoutName + ": " + postLayoutCount + 
//                      "st ignore Layout " + newPanelSize + " " + lastPanelSize + " | " + changedId + " " + currentChangedId + " #" + getChildComponents().size());
				    return;
				}
//				else
//				{
//                  consoleLog("LayoutedPanelConnector postLayout " + getParent().getWidget().getElement().getId() + "  " + getState().layoutName + ": " + postLayoutCount + 
//                      "st not ignore Layout " + newPanelSize + " " + lastPanelSize + " | " + changedId + " " + currentChangedId + " #" + getChildComponents().size());
//				}
			}

			layout.layoutComponents(false);
		}
		
        clearCaches();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnregister()
	{
		getLayoutManager().unregisterDependency(this, getWidget().getElement());
		
		super.onUnregister();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public final ComponentConnector getParent()
    {
        return (ComponentConnector)super.getParent();
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(ServerConnector pParent)
	{
		super.setParent(pParent);
		
		layoutChanged("set Parent");

//		postLayout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCaption(ComponentConnector pConnector)
	{
		// Nothing to do.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the {@link Size} of the canvas.
	 * <p>
	 * Every {@link VLayoutedPanel} is assumed to be consisting of two
	 * elements, the panel itself and a "canvas" element in it. If there is no
	 * canvas element, this method can return the size of the panel itself.
	 * 
	 * @return the {@link Size} of the canvas.
	 * @see #setCanvasSize(Size)
	 */
	public Size getCanvasSize()
	{
		if (cachedCanvasSize == null)
		{
		    Element canvas = getWidget().getElement(); 
		    
			cachedCanvasSize = new Size(
			        canvas.getOffsetWidth(),
			        canvas.getOffsetHeight());
			
			setDebugAttribute(this, "last-canvas-size", cachedCanvasSize);
		}
		
		return new Size(cachedCanvasSize.width, cachedCanvasSize.height);
	}
	
	/**
	 * Gets the {@link ComponentConnector} of the
	 * {@link VLayoutedPanel}.
	 * 
	 * @return the {@link ComponentConnector}.
	 */
	public ComponentConnector getComponentConnector()
	{
		return this;
	}
	
	/**
	 * Gets the constraint for the given {@link Connector}, returns {@code null}
	 * if there is none.
	 * 
	 * @param pConnector the {@link Connector} for which to get the constraint.
	 * @return the constraint for the given {@link Connector}, {@code null} if
	 *         there is none.
	 */
	public String getConstraint(Connector pConnector)
	{
		return getState().constraints.get(pConnector);
	}
	
	/**
	 * Gets the layout data with the given name, returns {@code null} if there
	 * is none.
	 * 
	 * @param pName the name of the data.
	 * @return the layout data with the given name, {@code null} if there is
	 *         none.
	 */
	public String getLayoutData(String pName)
	{
		return getState().layoutData.get(pName);
	}
	
	/**
	 * Gets the {@link Margins} for the given {@link Connector}.
	 * <p>
	 * May not return {@code null}, but instead a {@link Margins} object which
	 * has all values set to zero.
	 * 
	 * @param pConnector the {@link Connector} for which to get the
	 *            {@link Margins}.
	 * @return the {@link Margins} for the given {@link Connector}.
	 */
	public Margins getMargins(Connector pConnector)
	{
		return new Margins(getState().componentMargins.get(pConnector));
	}
	
	/**
	 * Gets the maximum {@link Size} for the given {@link Connector}.
	 * <p>
	 * May not return {@code null}, but instead a {@link Size} object which has
	 * all values set to zero.
	 * 
	 * @param pConnector the {@link Connector} for which to get the {@link Size}
	 *            .
	 * @return the maximum {@link Size} for the given {@link Connector}.
	 */
	public Size getMaximumSize(Connector pConnector)
	{
		return Size.getSize(getState().maximumSizes.get(pConnector));
	}
	
	/**
	 * Gets the minimum {@link Size} for the given {@link Connector}.
	 * <p>
	 * May not return {@code null}, but instead a {@link Size} object which has
	 * all values set to zero.
	 * 
	 * @param pConnector the {@link Connector} for which to get the {@link Size}
	 *            .
	 * @return the minimum {@link Size} for the given {@link Connector}.
	 */
	public Size getMinimumSize(Connector pConnector)
	{
		return Size.getSize(getState().minimumSizes.get(pConnector));
	}
	
	/**
	 * Gets the {@link Size} of the panel.
	 * 
	 * @return the {@link Size} of the panel.
	 */
	public Size getPanelSize()
	{
		if (cachedPanelSize == null)
		{
            Element canvas = getWidget().getElement(); 
	        Style style = canvas.getStyle();
	        
	        String previousWidth = style.getWidth();
	        String previousHeight = style.getHeight();

	        style.setProperty("width", "100%");
	        style.setProperty("height", "100%");
		    
		    cachedPanelSize = new Size(
                    canvas.getOffsetWidth(),
                    canvas.getOffsetHeight());
            
            style.setProperty("width", previousWidth);
            style.setProperty("height", previousHeight);
		    
//            Element panelElement = ((AbstractComponentConnector)getParent()).getWidget().getElement();
//            
//            cachedPanelSize = new Size(
//			        panelElement.getOffsetWidth(),
//			        panelElement.getOffsetHeight());
			
			setDebugAttribute(this, "last-panel-size", cachedPanelSize);
		}
		
		return new Size(cachedPanelSize.width, cachedPanelSize.height);
	}
	
	/**
	 * Gets the preferred {@link Size} of this panel.
	 * 
	 * @return the preferred {@link Size} of this panel. {@code null} if there
	 *         is no layout set or if there is no preferred size.
	 */
	public Size getPreferredPanelSize()
	{
		cachedCanvasSize = null;
		cachedPanelSize = null;
		
		updateLayout();
		
		if (layout != null)
		{
			return layout.getPreferredSize();
		}
		
		return null;
	}
	
	/**
	 * Gets the preferred {@link Size} for the given {@link Connector}.
	 * <p>
	 * May not return {@code null}, but instead a {@link Size} object which has
	 * all values set to zero.
	 * 
	 * @param pConnector the {@link Connector} for which to get the {@link Size}
	 *            .
	 * @return the preferred {@link Size} for the given {@link Connector}.
	 */
	public Size getPreferredSize(Connector pConnector)
	{
		return Size.getSize(getState().preferredSizes.get(pConnector));
	}
	
	/**
	 * Gets the {@link Size} for the given {@link Connector}.
	 * <p>
	 * May not return {@code null}, but instead a {@link Size} object which has
	 * all values set to zero.
	 * 
	 * @param pConnector the {@link Connector} for which to get the {@link Size}
	 *            .
	 * @return the {@link Size} for the given {@link Connector}.
	 */
	public Size getSize(Connector pConnector)
	{
		return Size.getSize(getState().sizes.get(pConnector));
	}
	
	/**
	 * Sets the {@link Size} of the canvas element.
	 * <p>
	 * If the panel does not have a canvas element, this should set the size of
	 * the panel.
	 * 
	 * @param pSize the new {@link Size} for the canvas element.
	 * @see #getCanvasSize()
	 */
	public void setCanvasSize(Size pSize)
	{
		Style style = getWidget().getCanvas().getStyle();
		
		if (!(pSize.width + "px").equals(style.getWidth()))
		{
			style.setWidth(pSize.width, Unit.PX);
		}
		
		if (!(pSize.height + "px").equals(style.getHeight()))
		{
			style.setHeight(pSize.height, Unit.PX);
		}
		
		setDebugAttribute(this, "canvas-size", pSize);
		
		cachedCanvasSize = null;
	}
	
	/**
	 * Sets the a debug attribute on the given {@link ComponentConnector} if
	 * debugging is enabled.
	 * 
	 * @param pConnector the {@link ComponentConnector target}.
	 * @param pName the name of the attribute.
	 * @param pValues the value of the attribute.
	 */
	public void setDebugAttribute(ComponentConnector pConnector, String pName, Object... pValues)
	{
		if (debugging)
		{
			String formattedValue = null;
			
			if (pValues == null || pValues.length == 0)
			{
				formattedValue = "";
			}
			else if (pValues.length == 1)
			{
				if (pValues[0] != null)
				{
					formattedValue = pValues[0].toString();
				}
				else
				{
					formattedValue = "null";
				}
			}
			else
			{
				StringBuilder builder = new StringBuilder();
				
				for (Object value : pValues)
				{
					builder.append(value);
				}
				
				formattedValue = builder.toString();
			}
			
			pConnector.getWidget().getElement().setAttribute("debug-" + pName, formattedValue);
		}
	}
	
	/**
	 * Checks (and if required corrects) the size of the "direct" parent.
	 * <p>
	 * "Direct" in this case refers to the panel in which this layout is
	 * embedded, so not the parent of the panel itself.
	 */
	protected void checkDirectParentSize()
	{
		// Note that this logic was mainly written to fix misbehaving internal
		// frames, which require that the content does have a set size (for
		// some reason). Except when you resize it (or jsut touch the resize
		// handle), then everything is fine.
		//
		// The only sane workaround I could find for that is to "fix" the size
		// of the parent panel of the layout by setting it here. I tried to make
		// this solution as generic as possible, but there is a good chance that
		// this actually interefers with certain "special" corner cases. Though,
		// I can not think of one that might occur within JVx.
		Element parentElement = ((ComponentConnector)getParent()).getWidget().getElement();
		Style parentStyle = parentElement.getStyle();
		
		// Deferred because it might be costly, and we do not want to do this
		// unnecessarily.
		Size preferredSize = null;
		
		if (parentStyle.getWidth() == null || parentStyle.getWidth().isEmpty())
		{
			// Okay, at this point the parent does not have a set width, which
			// most likely means that we are not inside another LayoutedPanel.
			
			if (parentElement.getOffsetWidth() <= 0)
			{
				// Neither does the parent have an actual width, so we will set
				// the preferred size of the layout to make sure that
				// *something* does have an actual size.
				
				preferredSize = getPreferredPanelSize();
				
				parentStyle.setWidth(preferredSize.width, Unit.PX);
			}
		}
		
		// See the comments above, the height logic works exactly the same way.
		if (parentStyle.getHeight() == null || parentStyle.getHeight().isEmpty())
		{
			if (parentElement.getOffsetHeight() <= 0)
			{
				if (preferredSize == null)
				{
					preferredSize = getPreferredPanelSize();
				}
				
				parentStyle.setHeight(preferredSize.height, Unit.PX);
			}
		}
	}
	
	/**
	 * Clears any caches.
	 */
	public void clearCaches()
	{
		cachedCanvasSize = null;
		cachedPanelSize = null;
		
		if (layout != null)
		{
            // Clear caches.
            layout.clearCaches();
		}
	}
	
	/**
	 * Creates the {@link IClientSideLayout} for the given name.
	 * 
	 * @param pLayoutName the {@link String layout name}.
	 * @return the {@link IClientSideLayout}.
	 * @throws IllegalArgumentException if no layout name was provided.
	 * @throws IllegalStateException if the layout name was unknown.
	 */
	protected IClientSideLayout createLayout(String pLayoutName)
	{
		if (pLayoutName == null || pLayoutName.length() == 0)
		{
			throw new IllegalArgumentException("No layout name was set.");
		}
		
		if (pLayoutName.equals("BorderLayout"))
		{
			return new BorderLayout();
		}
		else if (pLayoutName.equals("FlowLayout"))
		{
			return new FlowLayout();
		}
		else if (pLayoutName.equals("FormLayout"))
		{
			return new FormLayout();
		}
		else if (pLayoutName.equals("GridLayout"))
		{
			return new GridLayout();
		}
		else if (pLayoutName.equals("NullLayout"))
		{
			return new NullLayout();
		}
		
		throw new IllegalStateException("There is no layout known under the name \"" + pLayoutName + "\".");
	}
	
	/**
	 * Determines whether debugging should be active or not.
	 * <p>
	 * This function is called once when the class is created. Extending classes
	 * can either override this function or {@link #isDebuggingEnabled()} to
	 * alter the debug behavior.
	 * 
	 * @return {@code true} if it should be active.
	 */
	protected boolean determineIfDebuggingIsActive()
	{
		return ApplicationConfiguration.isDebugMode()
				|| "true".equalsIgnoreCase(Window.Location.getParameter("layout-debugging"));
	}
	
	/**
	 * Gets whether layout debugging is enabled or not.
	 * <p>
	 * This function is called whenever it is necessary to determine whether
	 * debugging is enabled, which means that it returns {@link #debugging} and
	 * that value should only be set as needed. That is done in the constructor
	 * when {@link #determineIfDebuggingIsActive()} is called.
	 * <p>
	 * Extending classes should consider to only override
	 * {@link #determineIfDebuggingIsActive()}, because this function might be
	 * called quite often.
	 * 
	 * @return {@code true} when debugging is enabled.
	 */
	public boolean isDebuggingEnabled()
	{
		return debugging;
	}
	
	/**
	 * Updates the {@link IClientSideLayout} that is used.
	 */
	protected void updateLayout()
	{
		String layoutName = getState().layoutName;
		
		setDebugAttribute(this, "layout-name", layoutName);
		
		if (layoutName == null || layoutName.isEmpty())
		{
			if (layout != null)
			{
				layout.setParent(null);
			}
			
			layout = null;
			currentLayoutName = null;
			
			setDebugAttribute(this, "layout-class", "null");
			
			// There is no layout set, so good luck with that!
			return;
		}
		else if (!layoutName.equals(currentLayoutName))
		{
			if (layout != null)
			{
				layout.setParent(null);
			}
			
			layout = createLayout(layoutName);
			currentLayoutName = layoutName;
			
			layout.setParent(this);
			
			setDebugAttribute(this, "layout-class", layout.getClass().getName());
		}
	}

	/**
	 * Gets the layout of this layouted panel connector.
	 * 
	 * @return the layout of this layouted panel connector.
	 */
	public IClientSideLayout getLayout()
	{
	    return layout;
	}
	
	/**
	 * Gets the wrapper widget from the {@code wrapperWidgets}.
	 * 
	 * @param key wrapped widget.
	 * @return Returns wrapper.
	 */
    public Widget getWrapper(Object key)
    {
        return wrapperWidgets.get(key);
    }
	
}	// LayoutedPanelConnector
