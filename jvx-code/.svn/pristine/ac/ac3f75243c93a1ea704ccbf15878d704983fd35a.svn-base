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
 * 20.11.2008 - [JR] - getResourceAsImage: used image cache
 *                   - UI redesign
 * 02.07.2009 - [JR] - getImage from byte[]
 * 23.07.2009 - [JR] - getIcon: checked null resource [BUGFIX]
 */
package com.sibvisions.rad.ui.awt.impl;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.ICursor;
import javax.rad.ui.IDimension;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFont;
import javax.rad.ui.IInsets;
import javax.rad.ui.IPoint;
import javax.rad.ui.IRectangle;

import com.sibvisions.rad.ui.AbstractFactory;

/**
 * The <code>AwtFactory</code> class encapsulates methods to
 * create and access AWT components.
 * 
 * @author Martin Handsteiner
 */
public abstract class AwtFactory extends AbstractFactory implements IFactory
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Create a new instance of <code>AwtFactory</code> to create and
	 * access AWT components.
	 */
	public AwtFactory()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String[] getAvailableFontFamilyNames()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public IFont createFont(String pName, int pStyle, int pSize)
	{
		return new AwtFont(pName, pStyle, pSize);
	}

	/**
	 * {@inheritDoc}
	 */
	public IColor createColor(int pRGB)
	{
		return new AwtColor(pRGB);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getPredefinedCursor(int pType)
	{
		return AwtCursor.getPredefinedCursor(pType);
	}

	/**
	 * {@inheritDoc}
	 */
	public ICursor getSystemCustomCursor(String pCursorName)
	{
		return AwtCursor.getSystemCustomCursor(pCursorName);
	}

	/**
	 * {@inheritDoc}
	 */
	public IPoint createPoint(int pX, int pY)
	{
		return new AwtPoint(pX, pY);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDimension createDimension(int pWidth, int pHeight)
	{
		return new AwtDimension(pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IRectangle createRectangle(int pX, int pY, int pWidth, int pHeight)
	{
		return new AwtRectangle(pX, pY, pWidth, pHeight);
	}

	/**
	 * {@inheritDoc}
	 */
	public IInsets createInsets(int pTop, int pLeft, int pBottom, int pRight)
	{
		return new AwtInsets(pTop, pLeft, pBottom, pRight);
	}

    /**
	 * {@inheritDoc}
	 */
	public IComponent createCustomComponent(Object pCustomComponent) 
	{
		AwtComponent result = new AwtComponent((Component)pCustomComponent);
		result.setFactory(this);
		return result;
	}

    /**
	 * {@inheritDoc}
	 */
	public IContainer createCustomContainer(Object pCustomContainer) 
	{
		AwtContainer result = new AwtContainer((Container)pCustomContainer);
		result.setFactory(this);
		return result;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the modifiers of the current event, if it's a InputEvent or ActionEvent.
     * 
     * @return the event modifiers
     */
    public static int getCurrentModifiers() 
    {
        int modifiers = 0;
        
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        
        if (currentEvent instanceof InputEvent) 
        {
            modifiers = ((InputEvent)currentEvent).getModifiers();
        } 
        else if (currentEvent instanceof ActionEvent) 
        {
            modifiers = ((ActionEvent)currentEvent).getModifiers();
        }
        
        return modifiers;
    }
    
    /**
     * Returns the timestamp of the most recent event that had a timestamp, and
     * that was dispatched from the <code>EventQueue</code> associated with the
     * calling thread. If an event with a timestamp is currently being
     * dispatched, its timestamp will be returned. If no events have yet 
     * been dispatched, the EventQueue's initialization time will be 
     * returned instead.In the current version of 
     * the JDK, only <code>InputEvent</code>s,
     * <code>ActionEvent</code>s, and <code>InvocationEvent</code>s have
     * timestamps; however, future versions of the JDK may add timestamps to
     * additional event types. Note that this method should only be invoked
     * from an application's event dispatching thread. If this method is
     * invoked from another thread, the current system time (as reported by
     * <code>System.currentTimeMillis()</code>) will be returned instead.
     *
     * @return the timestamp of the last <code>InputEvent</code>,
     *         <code>ActionEvent</code>, or <code>InvocationEvent</code> to be
     *         dispatched, or <code>System.currentTimeMillis()</code> if this
     *         method is invoked on a thread other than an event dispatching
     *         thread
     * @see java.awt.event.InputEvent#getWhen
     * @see java.awt.event.ActionEvent#getWhen
     * @see java.awt.event.InvocationEvent#getWhen
     */
    public static long getMostRecentEventTime()
    {
    	return EventQueue.getMostRecentEventTime();    	
    }
	
}	// AwtFactory
