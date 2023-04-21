/*
 * Copyright 2015 SIB Visions GmbH
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
 * 18.11.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.window;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.LayoutedPanelConnector;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.panel.helper.Size;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.LayoutManager;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractHasComponentsConnector;
import com.vaadin.client.ui.VWindow;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.window.WindowMode;

/**
 * The <code>InternalFrameConnector</code> extends the {@link WindowConnector} and supports
 * setting visibility of maximizable feature.
 * 
 * @author René Jahn
 */
@Connect(com.sibvisions.rad.ui.vaadin.ext.ui.InternalFrame.class)
public class InternalFrameConnector extends WindowConnector
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The last packed state. */
    int lastPack = 0;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    native static void consoleLog(String message) /*-{
//      try {
//          console.log(message);
//      } catch (e) {
//      }
//    }-*/;    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void postLayout() 
    {
        VWindow window = getWidget();

        if (getState().pack == 0)
        {
            lastPack = 0; 
            
//            consoleLog("undoPack, reset lastPack to 0");
        }
        else if (getState().pack != lastPack && getState().windowMode != WindowMode.MAXIMIZED) 
        {
//            consoleLog("do Pack!!! " + getState().pack + "  " + lastPack);
            super.updateComponentSize();
            LayoutedPanelConnector con = findLayoutedPanelConnector(getContent());

            if (con != null)
            {
//                consoleLog("found Connector for calculation!!!");

                LayoutManager lm = getLayoutManager();
                Style contentStyle = window.contents.getStyle();
    
                int headerHeight = lm.getOuterHeight(window.header);
                contentStyle.setPaddingTop(headerHeight, Unit.PX);
                contentStyle.setMarginTop(-headerHeight, Unit.PX);
    
                int footerHeight = lm.getOuterHeight(window.footer);
                contentStyle.setPaddingBottom(footerHeight, Unit.PX);
                contentStyle.setMarginBottom(-footerHeight, Unit.PX);
    
                int minWidth = lm.getOuterWidth(window.header)
                        - lm.getInnerWidth(window.header);
                int minHeight = footerHeight + headerHeight + 1;
    
                Size size = con.getPreferredPanelSize();

                int w = Math.max(size.width, minWidth);
                int h = size.height + minHeight;
                
                updateWidgetSize(w + "px", h + "px");

//                consoleLog("new Size: " + w + "px" + "  " + h + "px");

                getConnection().updateVariable(window.id, "width", w, false);
                getConnection().updateVariable(window.id, "height", h, true);

//                consoleLog("send Size to Server");

                LayoutManager layoutManager = getLayoutManager();
                layoutManager.setNeedsMeasure(this);

                lastPack = getState().pack;
                
//                consoleLog("finished: " + getState().pack + "  " + lastPack);
            }
        }

        super.postLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStateChanged(StateChangeEvent pEvent)
    {
        super.onStateChanged(pEvent);

        updateMaximizable();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateWindowMode() 
    {
        super.updateWindowMode();

        updateMaximizable();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public InternalFrameState getState()
    {
        return (InternalFrameState)super.getState();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Updates the visibility of maximize button.
     */
    private void updateMaximizable()
    {
        InternalFrameState state = getState();
        
        getWidget().updateMaximizeRestoreClassName(state.resizable && state.maximizable, state.windowMode);
    }

    /**
     * Determines the under laying LayoutedPanelConnector to calculate preferredSize.
     * @param pConnector the root connector
     * @return the LayoutedPanelConnector
     */
    public LayoutedPanelConnector findLayoutedPanelConnector(ComponentConnector pConnector)
    {
        if (pConnector instanceof LayoutedPanelConnector)
        {
            return (LayoutedPanelConnector)pConnector;
        }
        else if (pConnector instanceof AbstractHasComponentsConnector)
        {
            AbstractHasComponentsConnector panel = (AbstractHasComponentsConnector)pConnector;
            for (ComponentConnector comp : panel.getChildComponents())
            {
                LayoutedPanelConnector con = findLayoutedPanelConnector(comp);
                if (con != null)
                {
                    return con;
                }
            }
        }
        return null;
    }

}   // InternalFrameConnector
