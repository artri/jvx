/*
 * Copyright 2019 SIB Visions GmbH
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
 * 26.03.2019 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.richtextarea;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.PostLayoutListener;
import com.vaadin.client.ui.richtextarea.VRichTextToolbar;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;
import com.vaadin.ui.RichTextArea;

/**
 * The <code>RichTextAreaConnector</code> adds focus and blur support to {@link RichTextArea}.
 * 
 * @author René Jahn
 */
@Connect(value = RichTextArea.class, loadStyle = LoadStyle.LAZY)
public class RichTextAreaConnector extends com.vaadin.client.ui.richtextarea.RichTextAreaConnector implements PostLayoutListener, FocusHandler, BlurHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the connected richt text area. */
	private com.google.gwt.user.client.ui.RichTextArea rta;
	
	/** the connected formatter. */
	private VRichTextToolbar rtt;

    /** the connected formatter panel. */
    private FlowPanel rttOuter;

	/** blur handling: focus immediate after blur. */
	private Object oBlurFocus;
	/** blur handling: blur source. */
	private Object oBlurBlur;

	/** focus handling: blur immediate after focus. */
	private Object oFocusBlur;
	/** focus handling: focus source. */
	private Object oFocusFocus;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init()
	{
		super.init();
		
        checkListeners();
	}
	
    @Override
    public void sendValueChange() 
    {
        super.sendValueChange();
        
        getLayoutManager().setNeedsMeasure(this);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStateChanged(StateChangeEvent pEvent)
	{
		super.onStateChanged(pEvent);
		
		checkListeners();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void layout() 
    {
        // layout is done in postLayout
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postLayout()
    {
        if (!isUndefinedHeight())
        {
            int rootElementInnerHeight = getWidget().getOffsetHeight() - 2; // 2 px for border
            int formatterHeight = getWidget().formatter.getOffsetHeight(); // for some reason formatter has no height

            int editorHeight = rootElementInnerHeight - formatterHeight;
            if (editorHeight < 0)
            {
                editorHeight = 0;
            }
            getWidget().rta.setHeight(editorHeight + "px");
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	static native void consoleLog(String message) /*-{
//        try {
//            console.log(message);
//        } catch (e) {
//        }
//    }-*/;    
	
	/**
	 * Installs the focus/blur listeners if rich text area has changed (e.g. readonly/editable).
	 */
	private void checkListeners()
	{
		com.google.gwt.user.client.ui.RichTextArea wrta = getWidget().rta; 
	
		//it's possible that the RichTextArea instance is different!
		if (rta != wrta)
		{
	    	rta = wrta;
			
	    	rta.addDomHandler(this, BlurEvent.getType());
			rta.addDomHandler(this, FocusEvent.getType());
			
			rtt = new VRichTextToolbar(getWidget().rta)
			{
			    @Override
			    protected void initWidget(Widget widget) 
			    {
			        super.initWidget(widget);
			        rttOuter = (FlowPanel)widget;
			    }
			};
		        
            FlowPanel parent = (FlowPanel)getWidget().formatter.getParent();
            parent.remove(getWidget().formatter);
            parent.insert(rtt, 0);
	        
	        for (int i = 0, count = rttOuter.getWidgetCount(); i < count; i++)
	        {
	            FlowPanel panel = (FlowPanel)rttOuter.getWidget(i);

	            for (int j = 0, jcount = panel.getWidgetCount(); j < jcount; j++)
	            {
	                Widget widget = panel.getWidget(j);
	                
	                widget.addDomHandler(this, BlurEvent.getType());
	                widget.addDomHandler(this, FocusEvent.getType());
		        }
	        }
	        
	        getWidget().formatter = rtt;
	        getWidget().setReadOnly(getWidget().isReadOnly());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onFocus(FocusEvent event) 
    {
        if (oFocusFocus == null)
        {
            oBlurFocus = event.getSource();
            
            oFocusBlur = null;
            oFocusFocus = event.getSource();
            
            Timer t = new Timer() 
            {
                public void run() 
                {
                    if (oFocusBlur == null)
                    {
                        ((FocusAndBlurServerRpc)getRpcProxy(FocusAndBlurServerRpc.class)).focus();
                    }
                    
                    oFocusFocus = null;
                }
            };

            // Schedule the timer to run "later"
            t.schedule(80);
        }
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void onBlur(BlurEvent event) 
    {
        if (oBlurBlur == null)
        {
            oFocusBlur = event.getSource();
            
            oBlurFocus = null;
            oBlurBlur = event.getSource();
            
            Timer t = new Timer() 
            {
                public void run() 
                {
                    if (oBlurFocus == null)
                    {
                        ((FocusAndBlurServerRpc)getRpcProxy(FocusAndBlurServerRpc.class)).blur();
                    }
                    
                    oBlurBlur = null;
                }
            };

            // Schedule the timer to run "later"
            t.schedule(80);
        }
    }

    
}
