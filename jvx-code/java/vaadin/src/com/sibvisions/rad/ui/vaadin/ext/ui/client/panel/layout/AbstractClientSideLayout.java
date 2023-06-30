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
 * 09.03.2016 - [RZ] - creation
 * 08.04.2019 - [DJ] - #1941: vertical alignement in labels corrected
 * 24.04.2019 - [DJ] - #2015: performance tuning, measure only once
 * 25.04.2019 - [DJ] - TextArea/TextField width measurement with specified columns fix
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.TableConnector;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.IClientSideLayout;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.LayoutedPanelConnector;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Margins;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.richtextarea.RichTextAreaConnector;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.connectors.grid.GridConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.client.ui.VRichTextArea;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.splitpanel.AbstractSplitPanelConnector;
import com.vaadin.client.ui.splitpanel.HorizontalSplitPanelConnector;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;
import com.vaadin.v7.client.ui.VScrollTable;
import com.vaadin.v7.client.ui.VScrollTable.HeaderCell;

/**
 * The {@link AbstractClientSideLayout} is an abstract implementation of
 * {@link IClientSideLayout} and provides the basis for layouts.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractClientSideLayout implements IClientSideLayout
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** A value indicating that no size is set. */
    protected static final int NO_SIZE = 0;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The cache for the minimum sizes. */
    private Map<Connector, Size> minimumSizeCache = new HashMap<Connector, Size>();
    
    /** The parent {@link LayoutedPanelConnector}. */
    protected LayoutedPanelConnector parent;
    
    /** The cache for the preferred sizes. */
    private Map<Connector, Size> preferredSizeCache = new HashMap<Connector, Size>();

    /** The {@link Margins}. */
    protected Margins margins = new Margins(0, 0, 0, 0);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link AbstractClientSideLayout}.
     */
    protected AbstractClientSideLayout()
    {
        super();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void clearCaches()
    {
        preferredSizeCache.clear();
        minimumSizeCache.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    public void layoutComponents()
    {
        updateFromState();
    }
    
    /**
     * {@inheritDoc}
     */
    public Size getPreferredSize()
    {
        return new Size();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setParent(LayoutedPanelConnector pParent)
    {
        parent = pParent;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
//    public static final native void consoleLog(String message) /*-{
//        try {
//            console.log(message);
//        } catch (e) {
//        }
//    }-*/;    
    
    /**
     * Calculates the preferred size of the given {@link ComponentConnector}.
     * 
     * @param pConnector the {@link ComponentConnector} to measure.
     * @param pWidthHint the width hint.
     * @return the preferred size.
     */
    protected Size calculatePreferredSize(ComponentConnector pConnector, int pWidthHint)
    {
        int preferredWidth = 0;
        int preferredHeight = 0;
        
        Widget widget = pConnector.getWidget();
        Element element = widget.getElement();
        
        AbstractComponentState state = pConnector.getState();
        
        // Check if there is a set preferred size.
        Size preferredSize = parent.getPreferredSize(pConnector);
        if (preferredSize != null)
        {
            if (preferredWidth <= 0 && preferredSize.width > 0)
            {
                preferredWidth = preferredSize.width;
                
                parent.setDebugAttribute(pConnector, "preferred-width-method", "preferred-size-set");
            }
            if (preferredHeight <= 0 && preferredSize.height > 0)
            {
                preferredHeight = preferredSize.height;
                
                parent.setDebugAttribute(pConnector, "preferred-height-method", "preferred-size-set");
            }
        }

        // Ticket 3117: remove preferred size calculation from size. 
        //              This was in the beginning the only way, but is and has not to be used. 
        //              It will also not work in Swing or any other technology.  
//        // Check if there is a fixed size set.
//        // This should be done after checking, if preferred size is set.
//        // There are component implementations like VaadinIcon, where a size is implicit set (the reason is not clear, but code is very complicated)
//        // and then the preferred size is not working.
//        Size fixedSize = parent.getSize(pConnector);
//        if (fixedSize != null)
//        {
//            if (preferredWidth <= 0 && fixedSize.width > 0)
//            {
//                preferredWidth = fixedSize.width;
//                
//                parent.setDebugAttribute(pConnector, "preferred-width-method", "size-set");
//            }
//            if (preferredHeight <= 0 && fixedSize.height > 0)
//            {
//                preferredHeight = fixedSize.height;
//                
//                parent.setDebugAttribute(pConnector, "preferred-height-method", "size-set");
//            }
//        }
        
        // Maybe the component does have a width set.
        String stateWidth = state.width;
        String stateHeight = state.height;
        boolean useStateWidth = preferredWidth <= 0 && stateWidth != null;
        boolean useStateHeight = preferredHeight <= 0 && stateHeight != null;
        boolean isWidthEm = useStateWidth && (stateWidth.endsWith("em") || stateWidth.endsWith("ex"));
        boolean isHeightEm = useStateHeight && (stateHeight.endsWith("em") || stateHeight.endsWith("ex"));
        // VTextField/VTextArea special handling, in AbstractVaadinTextField count of columns is set as width in em units
        if (isWidthEm && (widget instanceof VTextField || widget instanceof VRichTextArea))
        {
            element.setAttribute("preferred-columns", stateWidth);
        }
        if (useStateHeight && widget instanceof VRichTextArea)
        {
            if (isHeightEm)
            {
                element.setAttribute("preferred-rows", stateHeight);
            }
            else
            {
                element.setAttribute("preferred-rows", "");
            }
        }

        Size measuredSize = null;
        if (useStateWidth)
        {
            if (stateWidth.endsWith("px"))
            {
                try
                {
                    preferredWidth = Integer.parseInt(stateWidth.substring(0, stateWidth.length() - 2));
                    
                    parent.setDebugAttribute(pConnector, "preferred-width-method", "from-state-in-pixels");
                }
                catch (NumberFormatException e)
                {
                    // Ignore any exception.
                }
            }
            else if (isWidthEm)
            {
                measuredSize = measureSize(widget, NO_SIZE);
                preferredWidth = measuredSize.width;
                
                parent.setDebugAttribute(pConnector, "preferred-width-method", "from-state-in-characters");
            }
        }
        
        // Maybe the component does have a height set.
        if (useStateHeight)
        {
            if (stateHeight.endsWith("px"))
            {
                try
                {
                    preferredHeight = Integer.parseInt(stateHeight.substring(0, stateHeight.length() - 2));
                    
                    parent.setDebugAttribute(pConnector, "preferred-height-method", "from-state-in-pixels");
                }
                catch (NumberFormatException e)
                {
                    // Ignore any exception.
                }
            }
            else if (isHeightEm)
            {
                // performance optimisation in order to prevent multiple measurements
                if (measuredSize == null)
                {
                    measuredSize = measureSize(widget, NO_SIZE);
                    
                    parent.setDebugAttribute(pConnector, "preferred-height-method", "from-state-in-characters");
                }
                preferredHeight = measuredSize.height;
            }
        }
        
        // Let's see if it is a ILayoutedPanel which we can ask for its
        // size.
        LayoutedPanelConnector layoutedPanel = getLayoutedPanel(pConnector);
        
        if (layoutedPanel != null)
        {
            if (pConnector instanceof SimpleManagedLayout)
            {
                // TODO HACK We need to let it perform a layout here to make sure we get the correct size.
                // Unfortunately, that is for sure not the best solution.
                ((SimpleManagedLayout)pConnector).layout();
            }
            
            Size size = layoutedPanel.getPreferredPanelSize();
            
            if (size != null)
            {
                int diffWidth = 0;
                int diffHeight = 0;
                
                if (pConnector != layoutedPanel)
                {
                    // Get the size difference between the parent and the
                    // panel so that we add it to the size.
                    Element panelElement = layoutedPanel.getWidget().getElement();
                    
                    diffWidth = Math.max(0, element.getOffsetWidth() - panelElement.getOffsetWidth());
                    diffHeight = Math.max(0, element.getOffsetHeight() - panelElement.getOffsetHeight());
                }
                
                if (preferredWidth <= 0 && size.width > 0)
                {
                    preferredWidth = size.width + diffWidth;
                    
                    parent.setDebugAttribute(pConnector, "preferred-width-method", "layouted-panel-calculated");
                }
                if (preferredHeight <= 0 && size.height > 0)
                {
                    preferredHeight = size.height + diffHeight;
                    
                    parent.setDebugAttribute(pConnector, "preferred-height-method", "layouted-panel-calculated");
                }
            }
        }
        
        if (preferredWidth <= 0 || preferredHeight <= 0)
        {
            if (pConnector instanceof SimpleManagedLayout)
            {
                // TODO HACK We need to let it perform a layout here to make sure we get the correct size.
                // Unfortunately, that is for sure not the best solution.
                ((SimpleManagedLayout)pConnector).layout();
            }
        }
        
        // Lastly, try to measure it by forcing it to size itself.
        boolean bMeasuredWithHint = false;
        if (preferredWidth <= 0)
        {
            // TODO HACK Tables can't measure themselves for some reason.
            // We will assume a hardcoded size of 400x300.
            if (pConnector instanceof GridConnector
                    || pConnector instanceof com.vaadin.v7.client.connectors.GridConnector)
            {
                preferredWidth = 400;

                parent.setDebugAttribute(pConnector, "preferred-width-method", "harcoded-400");
            }
            else
            {
                // performance optimisation in order to prevent multiple measurements
                if (measuredSize == null || pWidthHint <= NO_SIZE)
                {
                    measuredSize = measureSize(pConnector, widget, pWidthHint);
                    
                    parent.setDebugAttribute(pConnector, "preferred-width-method", "measured(" + pWidthHint + ")");
                    
                    bMeasuredWithHint = true;
                }
                preferredWidth = measuredSize.width;
            }
        }
        
        // Lastly, try to measure it by forcing it to size itself.
        if (preferredHeight <= 0)
        {
            // TODO HACK Tables can't measure themselves for some reason.
            // We will assume a hardcoded size of 400x300.
            if (pConnector instanceof GridConnector
                    || pConnector instanceof com.vaadin.v7.client.connectors.GridConnector)
            {
                preferredHeight = 300;
                
                parent.setDebugAttribute(pConnector, "preferred-height-method", "hardcoded-300");
            }
            else
            {
                // performance optimisation in order to prevent multiple measurements
                if (!bMeasuredWithHint && (measuredSize == null || pWidthHint <= NO_SIZE))
                {
                    measuredSize = measureSize(pConnector, widget, pWidthHint);
                    
                    parent.setDebugAttribute(pConnector, "preferred-height-method", "measured");
                }
                preferredHeight = measuredSize.height;
            }
        }
        
        Margins mar = parent.getMargins(pConnector);
        preferredWidth = preferredWidth + mar.getHorizontal();
        preferredHeight = preferredHeight + mar.getVertical();
        
        return limitPreferredSize(pConnector, preferredWidth, preferredHeight);
    }

    /**
     * Limits the preferred size with min size and max size, if set.
     * 
     * @param pConnector the connector
     * @param pPreferredWidth the preferredWidth
     * @param pPreferredHeight the preferredHeight
     * @return the limited preferred size
     */
    protected Size limitPreferredSize(ComponentConnector pConnector, int pPreferredWidth, int pPreferredHeight)
    {
        Size minimumSize = parent.getMinimumSize(pConnector);
        if (minimumSize != null)
        {
            if (minimumSize.width > pPreferredWidth)
            {
                pPreferredWidth = minimumSize.width;
            }
            if (minimumSize.height > pPreferredHeight)
            {
                pPreferredHeight = minimumSize.height;
            }
        }
        Size maximumSize = parent.getMaximumSize(pConnector);
        if (maximumSize != null)
        {
            if (maximumSize.width < pPreferredWidth)
            {
                pPreferredWidth = maximumSize.width;
            }
            if (maximumSize.height < pPreferredHeight)
            {
                pPreferredHeight = maximumSize.height;
            }
        }
        
        parent.setDebugAttribute(pConnector, "preferred-width", Integer.toString(pPreferredWidth));
        parent.setDebugAttribute(pConnector, "preferred-height", Integer.toString(pPreferredHeight));
        
        return new Size(pPreferredWidth, pPreferredHeight);
    }
    
    /**
     * Gets the minimum size of the given component.
     * 
     * @param pConnector the connector of the component.
     * @return the size.
     */
    protected Size getMinimumSize(ComponentConnector pConnector)
    {
        Size minimumSize = minimumSizeCache.get(pConnector);
        
        if (minimumSize == null)
        {
            minimumSize = new Size();
            minimumSizeCache.put(pConnector, minimumSize);
            
            // First check if there is a set minimum size.
            Size size = parent.getMinimumSize(pConnector);
            if (size != null)
            {
                minimumSize.width = size.width;
                minimumSize.height = size.height;
            }
            
            Size maximumSize = parent.getMaximumSize(pConnector);
            if (maximumSize != null)
            {
                if (maximumSize.width < minimumSize.width)
                {
                    minimumSize.width = maximumSize.width;
                }
                if (maximumSize.height < minimumSize.height)
                {
                    minimumSize.height = maximumSize.height;
                }
            }
        }
        
        parent.setDebugAttribute(pConnector, "minimum-size", minimumSize);
        
        return minimumSize;
    }
    
    /**
     * Gets the preferred {@link Size} of the given {@link ComponentConnector}
     * or calculates it if needed.
     * <p>
     * The calculated value will be cached and the value from the cache will be
     * returned for subsequent calls.
     * 
     * @param pConnector the {@link ComponentConnector} to get the preferred
     *            {@link Size} of.
     * @return the preferred {@link Size} of the given
     *         {@link ComponentConnector}.
     */
    protected Size getPreferredSize(ComponentConnector pConnector)
    {
        Size preferredSize = preferredSizeCache.get(pConnector);

        if (preferredSize == null)
        {
            Widget wConnector = pConnector.getWidget();
            
            // If wrapper set, we apply styles on wrapper instead of widget itself.
            Widget widget = parent.getWrapper(wConnector);
            if (widget == null)
            {
                widget = wConnector;
            }
            
            Element element = widget.getElement();
            Style style = element.getStyle();
            
            String currentWidth = style.getWidth();
            
            int widthHint;
            if (currentWidth != null && currentWidth.endsWith("px"))
            {
                widthHint = Integer.parseInt(currentWidth.substring(0, currentWidth.length() - 2));
            }
            else
            {
                widthHint = NO_SIZE;
            }
            
            preferredSize = calculatePreferredSize(pConnector, widthHint);
            preferredSizeCache.put(pConnector, preferredSize);
        }
        
        return preferredSize;
    }
    
    /**
     * Updates the preferred size cache of the component.
     * 
     * @param pPreferredWidth the preferred width
     * @param pPreferredHeight the preferred height
     */
    protected void updatePreferredSizeCache(int pPreferredWidth, int pPreferredHeight)
    {
        ComponentConnector pConnector = parent.getParent();
        ServerConnector parentLayout = pConnector.getParent();
        if (parentLayout instanceof LayoutedPanelConnector)
        {
            LayoutedPanelConnector layoutedPanel = (LayoutedPanelConnector)parentLayout;
            
            AbstractClientSideLayout layout = (AbstractClientSideLayout)layoutedPanel.getLayout();
            
            if (layout != null
                    && layout.preferredSizeCache.get(pConnector) != null)
            {
                // Check if there is a set preferred size.
                Size preferredSize = layoutedPanel.getPreferredSize(pConnector);
                if (preferredSize == null)
                {
                    // Get the size difference between the parent and the
                    // panel so that we add it to the size.
                    Widget widget = pConnector.getWidget();
                    Element element = widget.getElement();
                    Element panelElement = parent.getWidget().getElement();
                    
                    Margins mar = layoutedPanel.getMargins(pConnector);
                    
                    pPreferredWidth += Math.max(0, element.getOffsetWidth() - panelElement.getOffsetWidth()) + mar.getHorizontal();
                    pPreferredHeight += Math.max(0, element.getOffsetHeight() - panelElement.getOffsetHeight()) + mar.getVertical();
                    
                    layout.preferredSizeCache.put(pConnector, limitPreferredSize(pConnector, pPreferredWidth, pPreferredHeight));
                }
            }
        }
    }
    
    
    /**
     * Checks if this layout is contained inside a scrollpanel.
     * 
     * @return {@code true} if this layout is contained inside a scrollpanel.
     */
    protected boolean isInsideScrollableContainer()
    {
        Connector connector = parent.getParent();
        
        while (connector instanceof ComponentConnector
//              && !(connector instanceof AbstractSplitPanelConnector) // preferred size is now supported
//              && !(connector instanceof TabsheetConnector)           // preferred size is now supported
                && !(connector instanceof WindowConnector))
        {
            Element element = ((ComponentConnector)connector).getWidget().getElement();
            
            if (element != null && element.getClassName().contains("v-scrolling"))
            {
                return true;
            }
            
            connector = connector.getParent();
        }
        
        return false;
    }
    
    /**
     * Gets whether the connected parent widget is scrollable.
     * 
     * @return <code>true</code> if widget of parent is scrollable, <code>false</code> otherwise
     */
    protected boolean isScrollableContainer()
    {
        Connector connector = parent.getParent();
        
        if (connector instanceof ComponentConnector
//                && !(connector instanceof AbstractSplitPanelConnector) // preferred size is now supported
//                && !(connector instanceof TabsheetConnector)           // preferred size is now supported
                && !(connector instanceof WindowConnector))
        {
            Element element = ((ComponentConnector)connector).getWidget().getElement();
            
            if (element != null && element.getClassName().contains("v-scrolling"))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets if the given {@link Element} is containing text.
     * 
     * @param pElement the {@link Element} to test.
     * @return {@code true} if the given {@link Element} contains text.
     */
    protected boolean isOnlyText(Element pElement)
    {
        if (pElement != null && pElement.getChildCount() == 1)
        {
            Node child = pElement.getChild(0);
            
            if (child.getNodeType() == Node.TEXT_NODE)
            {
                String value = child.getNodeValue();
                
                return value != null && value.length() > 0;
            }
        }
        
        return false;
    }

    /**
     * Measures the size of the given {@link Widget} with the possibility to measure some connector types differently.
     * 
     * @param pConnector the {@link ComponentConnector}
     * @param pWidget the {@link Widget} to measure.
     * @param pWidthHint the width hint.
     * @return the measured size
     */
    protected Size measureSize(ComponentConnector pConnector, Widget pWidget, int pWidthHint)
    {
        Size measuredSize;
        
        if (pConnector instanceof TableConnector)
        {
            
            VScrollTable table = (VScrollTable)pWidget;

            int width = 0;
            int height = 0;
            
            if (table.sizeNeedsInit && table.lastMeasuredWidth > 0)
            {
                width = table.lastMeasuredWidth;  
            }
            else
            {
                boolean hasMeasuredWidth = false;
                int count = table.tHead.getVisibleCellCount();
                for (int i = 0; i < count; i++)
                {
                    HeaderCell hc = table.tHead.getHeaderCell(i);
    
                    int naturalWidth = hc.getNaturalColumnWidth(i);
                    
                    width += naturalWidth + 2;
                    if (naturalWidth > 0)
                    {
                        hasMeasuredWidth = true;
                    }
                    
                    if (height == 0)
                    {
                        height = hc.getOffsetHeight();
                    }
                }
                
                if (width > 0)
                {
                    width += WidgetUtil.getNativeScrollbarSize() + 2;
                }

                if (hasMeasuredWidth)
                {
                    if (width != table.getElement().getOffsetWidth())
                    {
                        width += 2;
                    }
                
                    table.lastMeasuredWidth = width;
                }
            }
            
            height += table.scrollBody.getOffsetHeight() + 3;

            if (table.hasHorizontalScrollbar())
            {
                height += WidgetUtil.getNativeScrollbarSize();
            }

//          consoleLog("Table Height: " + height + "   " + Math.round(table.scrollBody.getRowHeight()) + "  " + 
//                           table.scrollBody.getOffsetHeight());
//
//            consoleLog("Table Width: " + width + "  " + table.sizeNeedsInit + "  " + table.tHead.getVisibleCellCount());
//          for (int i = 0, count = table.tHead.getVisibleCellCount(); i < count; i++)
//          {
//              HeaderCell hc = table.tHead.getHeaderCell(i);
//              
//              consoleLog("Table Column: " + hc.getColKey() + "  " + hc.getWidth() + "  " + hc.getOffsetWidth() + "  " + 
//                           hc.getOffsetHeight() + "  " + hc.getNaturalColumnWidth(i) + "  " + hc.getNaturalColumnWidth(-1));
//          }

            measuredSize = new Size(width, height);
        }
        else if (pConnector instanceof TabsheetConnector)
        {
            TabsheetConnector tabSheet = (TabsheetConnector)pConnector;
            
            List<ComponentConnector> children = tabSheet.getChildComponents();
            if (children.isEmpty())
            {
                measuredSize = new Size(0, 0);
            }
            else
            {
                measuredSize = calculatePreferredSize(children.get(0), pWidthHint);
//                consoleLog("Tabsheet measure with calculate: " + tabSheet.getState().selected + "  " + measuredSize);
            }

            measuredSize.height = measuredSize.height + tabSheet.getWidget().tabs.getOffsetHeight();
        }
        else if (pConnector instanceof AbstractSplitPanelConnector)
        {
            AbstractSplitPanelConnector splitPanel = (AbstractSplitPanelConnector)pConnector;
            
            boolean horizontal = splitPanel instanceof HorizontalSplitPanelConnector;
            
            int width = 0;
            int height = 0;
//            int i = 0;
            for (ComponentConnector component : ((AbstractSplitPanelConnector)pConnector).getChildComponents())
            {
//                i++;
                Size size = calculatePreferredSize(component, pWidthHint);
//                consoleLog("Split measure with calculate: " + i + "  " + size);

                if (horizontal)
                {
                    width += size.width;
                    if (size.height > height)
                    {
                        height = size.height;
                    }
                }
                else
                {
                    if (size.width > width)
                    {
                        width = size.width;
                    }
                    height += size.height;
                }
            }
            
            if (horizontal)
            {
                width += splitPanel.getWidget().splitter.getOffsetWidth();
//                consoleLog("Split measure horizontal splitter: " + splitPanel.getWidget().splitter.getOffsetWidth());
            }
            else
            {
                height += splitPanel.getWidget().splitter.getOffsetHeight();
//                consoleLog("Split measure vertical splitter: " + splitPanel.getWidget().splitter.getOffsetHeight());
            }
            
            measuredSize = new Size(width, height);
        }
        else if ("com.vaadin.addon.charts.shared.ChartConnector".equals(pConnector.getClass().getName()))
        {
            measuredSize = new Size(75, 50);
        }
        else
        {
            measuredSize = measureSize(pWidget, pWidthHint);
        }
        
        return measuredSize;
    }

    /**
     * Measures the size of the given {@link Widget} by setting the given size and then returning the offset size.
     * After that, the previous values for the width and the height are set back.
     * 
     * @param pWidget the {@link Widget} to measure.
     * @param pWidthHint the width hint.
     * @return the measured size
     */
    protected Size measureSize(Widget pWidget, int pWidthHint)
    {
        Element element = pWidget.getElement();
        Style style = element.getStyle();
        boolean bWrapped = Boolean.parseBoolean(element.getAttribute("wrapped"));
        
        String previousWidth = style.getWidth();
        String previousHeight = style.getHeight();
        String previousTop = style.getTop();
        String previousLeft = style.getLeft();
        if (bWrapped)
        {
            style.setProperty("position", "fixed");
            String fontSize = style.getFontSize();
            if (fontSize == null || fontSize.trim().length() == 0)
            {
                style.setProperty("line-height", "");
            }
            else
            {
                style.setProperty("line-height", "unset");
            }
        }
        style.setProperty("top", "0");
        style.setProperty("left", "0");
        
        String preferredRows = null;
        if (element.hasAttribute("preferred-rows"))
        {
            preferredRows = element.getAttribute("preferred-rows");
            pWidget.setHeight(preferredRows);
        }
        else
        {
            style.setProperty("height", "");
        }

        String preferredColumns = null;
        if (element.hasAttribute("preferred-columns"))
        {
            preferredColumns = element.getAttribute("preferred-columns");
            pWidget.setWidth(preferredColumns);
        }
        else
        {
            style.setProperty("width", "");

            if (bWrapped)
            {
                String lastOriginalWidth = element.getAttribute("last-original-width");

                if (pWidthHint > NO_SIZE && !String.valueOf(pWidthHint).equals(lastOriginalWidth)) // size is like last preferred size. 
                {                                                      // we do not change the last preferred size until we are resize.
                    int originalWidth;
                    if (BrowserInfo.get().isIE())
                    {
                        originalWidth = element.getOffsetWidth() + 1;
                    }
                    else
                    {
                        originalWidth = (int)Math.ceil(WidgetUtil.getRequiredWidthBoundingClientRectDouble(element));
                    }
                        
                    String sOriginalWidth = String.valueOf(originalWidth);
                    if (sOriginalWidth.equals(lastOriginalWidth))
                    {
                        if (originalWidth < pWidthHint)
                        {
                            pWidthHint = originalWidth;
                        }
    
                        style.setProperty("width", Integer.toString(pWidthHint) + "px");
                    }
                    else
                    {
                        element.setAttribute("last-original-width", sOriginalWidth);
                    }
                }
            }
        }
        
        int measuredWidth = 0;
        int measuredHeight = 0;
        
        // Internet Explorer fails with an "Unknown Error" when it tries to
        // access bounds of *some* elements. The method "getBoundingClientRect"
        // does exist as a method on the defined object, however, invoking it
        // will result in the JavaScript engine failing with the aforementioned
        // "Unknown Error". Which is quite helpful, actually. When executed from
        // the debugging console, the method can be invoked and works.
        // So we are taking the cheap route and just use the offset height
        // instead when being run from the Internet Explorer, because that one
        // never fails. Unfortunately, that will most likely lead to broken
        // layouts in some circumstances, but I guess that is better than not
        // having anything at all.
        if (BrowserInfo.get().isIE())
        {
            measuredWidth = element.getOffsetWidth();
            measuredHeight = element.getOffsetHeight();
            
            if (bWrapped)
            {
                measuredWidth++;
            }
        }
        else
        {
            measuredWidth = (int)Math.ceil(WidgetUtil.getRequiredWidthBoundingClientRectDouble(element));
            measuredHeight = (int)Math.ceil(WidgetUtil.getRequiredHeightBoundingClientRectDouble(element));
        }

        if (bWrapped)
        {
            if (measuredWidth <= 1) // 1 pixel, for measuredWidth++ in ie environment
            {
                element.setAttribute("last-original-width", "");
            }
            
            style.setProperty("position", "");
        }
        style.setProperty("top", previousTop);
        style.setProperty("left", previousLeft);
        if (preferredColumns == null)
        {
            style.setProperty("width", previousWidth);
        }
        else
        {
            pWidget.setWidth(previousWidth);
        }
        if (preferredRows == null)
        {
            style.setProperty("height", previousHeight);
        }
        else
        {
            pWidget.setHeight(previousHeight);
        }
        
        return new Size(measuredWidth, measuredHeight);
    }
    
//    /**
//     * Forces Post Layout on all direct LayoutedPanel children. 
//     * @param pConnector the connector
//     */
//    protected void forcePostLayout(HasComponentsConnector pConnector)
//    {
//        for (ComponentConnector component : pConnector.getChildComponents())
//        {
//            LayoutedPanelConnector layoutedPanel = getLayoutedPanel(component);
//            if (layoutedPanel != null)
//            {
//                layoutedPanel.postLayout();
//            }
//        }
//    }
    
    /**
     * Resizes and relocates the given {@link ComponentConnector}.
     * 
     * @param pConnector the {@link ComponentConnector}.
     * @param pTop the new top/y position.
     * @param pLeft the new left/x position.
     * @param pWidth the new width.
     * @param pHeight the new height.
     */
    protected void resizeRelocate(ComponentConnector pConnector, int pLeft, int pTop, int pWidth, int pHeight)
    {
        Widget wConnector = pConnector.getWidget();
        
        // If wrapper set, we apply styles on wrapper instead of widget itself.
        Widget widget = parent.getWrapper(wConnector);
        if (widget == null)
        {
            widget = wConnector;
        }
        
        boolean changed = false;
        
        if (!widget.getStyleName().contains("v-has-width"))
        {
            widget.addStyleName("v-has-width");
            
            changed = true;
        }
        
        if (!widget.getStyleName().contains("v-has-height"))
        {
            widget.addStyleName("v-has-height");
            
            changed = true;
        }
        
        Style style = widget.getElement().getStyle();
        
        if (!(pTop + "px").equals(style.getTop()))
        {
            style.setTop(pTop, Unit.PX);
            
            changed = true;
        }
        
        if (!(pLeft + "px").equals(style.getLeft()))
        {
            style.setLeft(pLeft, Unit.PX);
            
            changed = true;
        }
        
        // We need to check here for positive numbers because there is
        // a check in these methods which throws an exception if
        // a negative number is set.
        // Given that negative numbers will not do anything anyway,
        // we'll simply set it to zero.
        
        int sanitizedWidth = Math.max(pWidth, 0);
        int sanitizedHeight = Math.max(pHeight, 0);
        
        if (!(sanitizedWidth + "px").equals(style.getWidth()))
        {
            style.setWidth(sanitizedWidth, Unit.PX);
            
            changed = true;
        }
        
        if (!(sanitizedHeight + "px").equals(style.getHeight()))
        {
            style.setHeight(sanitizedHeight, Unit.PX);
            
            changed = true;
        }
        
        if (changed)
        {
            // TODO This is a workaround for the tabsheet content having
            // the wrong size after switching tabs.
            if (pConnector instanceof TabsheetConnector)
            {
                TabsheetConnector tabSheet = (TabsheetConnector)pConnector;
                
                tabSheet.layout();

                // This helps to restore the Tabs, if they were wrong, but if the tabs are too small, they are displayed wrong anyway...
                tabSheet.getWidget().scrollToActiveTab(); // New function without gaining the focus.
//                tabSheet.getWidget().showAllTabs();
//                tabSheet.getWidget().focus();
                
//              consoleLog("Tabsheet setSize: " + tabSheet.getState().selected + "  " + pWidth + "x" + pHeight);
                
//                forcePostLayout(tabSheet); // ForceLayout is due to fixed cache in LayoutedPanelConnector not necessary anymore
            }
            else if (pConnector instanceof AbstractSplitPanelConnector)
            {
                ((AbstractSplitPanelConnector)pConnector).layout();
//                forcePostLayout((AbstractSplitPanelConnector)pConnector); // ForceLayout is due to fixed cache in LayoutedPanelConnector not necessary anymore
            }
            else if (pConnector instanceof RichTextAreaConnector)
            {
                ((RichTextAreaConnector)pConnector).postLayout();
            }
            // TODO This is a workaround for the table which does not correctly
            // size itself after having been set a new size.
            else if (pConnector instanceof TableConnector)
            {
            	// the width and heigth has to be set into the state, as otherwise, 
            	// the widget thinks, it is dynamicly, and does not layout correctly.
                ((TableConnector)pConnector).getState().width = style.getWidth();
                ((TableConnector)pConnector).getState().height = style.getHeight();
                ((TableConnector)pConnector).layoutHorizontally();
                ((TableConnector)pConnector).layoutVertically();
                ((TableConnector)pConnector).postLayout();
//                // This would prevent scrollbar flicker, but initial column width is calculate without data... This is worse than fickering scrollbars
//                // So we now only call this if there is only 1 column.
//                if (((TableConnector)pConnector).getWidget().tHead.getVisibleCellCount() == 1)
//                {
//                    ((TableConnector)pConnector).getWidget().triggerLazyColumnAdjustment(true);
//                }
            }
            // TODO This is a workaround for the grid which does not correctly
            // size itself after having been set a new size.
            // This also includes the TreeGridConnector, as it extends from
            // the GridConnector.
            else if (pConnector instanceof GridConnector)
            {
                ((GridConnector)pConnector).layout();
            }
            // TODO This is a workaround for the grid which does not correctly
            // size itself after having been set a new size.
            else if (pConnector instanceof com.vaadin.v7.client.connectors.GridConnector)
            {
                ((com.vaadin.v7.client.connectors.GridConnector)pConnector).layout();
            }
            else if ("com.vaadin.addon.charts.shared.ChartConnector".equals(pConnector.getClass().getName()))
            {
                parent.getLayoutManager().reportOuterWidth(pConnector, sanitizedWidth);
                parent.getLayoutManager().reportOuterHeight(pConnector, sanitizedHeight);
            }
            else if (widget instanceof VOpenStreetMap)
            {
                ((VOpenStreetMap)widget).notifyResized();
            }
            else
            {
                LayoutedPanelConnector layoutedPanel = getLayoutedPanel(pConnector);
                if (layoutedPanel != null)
                {
                    layoutedPanel.postLayout();
                }
            }
        }
    }
    
    /**
     * Updates the internal state from the state.
     */
    protected void updateFromState()
    {
        margins.set(parent.getLayoutData("margins"));
    }
    
    /**
     * Gets the {@link LayoutedPanelConnector} from the given
     * {@link ComponentConnector}, which may it either be directly or its only
     * child.
     * 
     * @param pConnector the given {@link ComponentConnector}.
     * @return the {@link LayoutedPanelConnector}.
     */
    private LayoutedPanelConnector getLayoutedPanel(ComponentConnector pConnector)
    {
        if (pConnector instanceof LayoutedPanelConnector)
        {
            return (LayoutedPanelConnector)pConnector;
        }
//      else if ((pConnector instanceof SimplePanelConnector || pConnector instanceof PanelConnector)
//              && (pConnector.getChildren().size() == 1
//                      || (pConnector.getChildren().size() == 2
//                              && pConnector.getChildren().get(1) instanceof CssExtensionConnector))
//              && pConnector.getChildren().get(0) instanceof LayoutedPanelConnector)
//      {
//          // I'd like to take a short moment to explain this statement to you.
//          //
//          // First, we check if the given connector has a certain type,
//          // because these types may contain only a single child.
//          // Then we check if the first child is the panel we want,
//          // or if there are two children and the second is the CSS
//          // Extension and the other is the panel we want.
//          return (LayoutedPanelConnector)pConnector.getChildren().get(0);
//      }
        else if (pConnector instanceof HasComponentsConnector)
        {
            List<ComponentConnector> children = ((HasComponentsConnector)pConnector).getChildComponents();
            
            if (!children.isEmpty() && children.get(0) instanceof LayoutedPanelConnector)
            {
                return (LayoutedPanelConnector)children.get(0);
            }
        }
        return null;
    }
    
}   // AbstractClientSideLayout
