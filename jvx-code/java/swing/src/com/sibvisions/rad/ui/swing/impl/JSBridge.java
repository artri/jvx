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
 * 24.01.2009 - [JR] - creation
 * 26.01.2009 - [JR] - openWindow implemented
 * 13.02.2009 - [JR] - openWindow: added some open properties
 * 25.02.2009 - [JR] - openWindog: focus workaround (jdk1.6 >= u10)
 * 13.11.2009 - [JR] - #8
 *                     setCookie: don't set 'null' value and set expire to the past
 * 16.08.2013 - [JR] - removed focus handling in openWindow (locked applet after opening window)
 * 22.08.2013 - [JR] - open window via Thread (solves focus problem and brings window to front)                      
 */
package com.sibvisions.rad.ui.swing.impl;

import java.applet.Applet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JSBridge</code> encapsulates the access to javascript functions. It's only
 * possible to access javascript in a web browser. The constructor will throw an exception
 * if it's not possible to access the javascript engine.
 * 
 * @author René Jahn
 */
public class JSBridge
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the JSObject reference. */
	private static Class<?> jsObject;
	
	/** The call method. */
	private static Method metCall; 
	/** The getMember method. */
	private static Method metGetMember; 
	/** The setMember method. */
	private static Method metSetMember; 
	/** The getWindow method. */
	private static Method metGetWindow; 
	
	/** the window object of the configured applet. */
	private Object objWindow;
	
	/** the document object. */
	private Object objDocument;
	
	/** the cookie expires date format. */
	private static final SimpleDateFormat FORMAT_EXPIRE = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);

	static
	{
		//cookie support
		FORMAT_EXPIRE.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		//get reusable objects
		
		try 
		{
			jsObject = Class.forName("netscape.javascript.JSObject");
		}
		catch (Throwable pThrowable)
		{
			jsObject = null;
		}
		
		if (jsObject != null)
		{
    		try 
    		{
    			metCall = jsObject.getMethod("call", new Class[] {String.class, Object[].class});
    		}
    		catch (Throwable pThrowable)
    		{
    			metCall = null;
    		}
    		try 
    		{
    			metGetMember = jsObject.getMethod("getMember", new Class[] {String.class});
    		}
    		catch (Throwable pThrowable)
    		{
    			metGetMember = null;
    		}
    		try 
    		{
    			metSetMember = jsObject.getMethod("setMember", new Class[] {String.class, Object.class});
    		}
    		catch (Throwable pThrowable)
    		{
    			metSetMember = null;
    		}
    		try 
    		{
    			metGetWindow = jsObject.getMethod("getWindow", new Class[] {Applet.class});
    		}
    		catch (Throwable pThrowable)
    		{
    			metGetWindow = null;
    		}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>JSBridge</code> for an applet.
	 * 
	 * @param pApplet the applet
	 * @throws Throwable if it's not possible to detect the window object of the applet
	 */
	public JSBridge(Applet pApplet) throws Throwable
	{
		objWindow = getWindow(pApplet);
		
		objDocument = getIntern(objWindow, "document");
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the window title.
	 * 
	 * @param pTitle the title
	 * @throws Throwable if the access to the document object fails
	 */
	public void setTitle(String pTitle) throws Throwable
	{
		if (pTitle == null)
		{
			pTitle = "";
		}
		
		setIntern(objDocument, "title", pTitle);
	}
	
	/**
	 * Gets the current window title.
	 * 
	 * @return the title
	 * @throws Throwable if the access to the document object fails
	 */
	public String getTitle() throws Throwable
	{
		String sTitle = (String)getIntern(objDocument, "title");
		
		if ("".equals(sTitle))
		{
			return null;
		}
		
		return sTitle;
	}
	
	/**
	 * Sets the location.
	 * 
	 * @param pLocation the location
	 * @throws Throwable if the access to the location object fails
	 */
	public void setLocation(String pLocation) throws Throwable
	{
		setIntern(objWindow, "location.href", pLocation);
	}
	
	/**
	 * Gets the current location.
	 * 
	 * @return the location
	 * @throws Throwable if the access to the location object fails
	 */
	public String getLocation() throws Throwable
	{
		return (String)getIntern(objWindow, "location.href");
	}
	
	/**
	 * Closes the window. This operation doesn't work with firefox if
	 * the window was not opened through a script.
	 * 
	 * @throws Throwable if the close operation fails
	 */
	public void close() throws Throwable
	{
		call("self.close");
	}
	
	/**
	 * Requests the focus.
	 * 
	 * @throws Throwable if the focus can't be set
	 */
	public void requestFocus() throws Throwable
	{
		call("self.focus");
	}
	
	/**
	 * Opens a new browser window.
	 * 
	 * @param pURL the url for the window
	 * @param pTarget the target name
	 * @param iX the x location on screen
	 * @param iY the y location on screen
	 * @param iWidth the window width
	 * @param iHeight the window height
	 * @param pCenter <code>true</code> to center the window (only used if height or width are defined)
	 * @throws Throwable if it's not possible to open the window
	 */
	public void openWindow(final String pURL, 
			               final String pTarget,
			               final int iX,
			               final int iY,
			               final int iWidth, 
			               final int iHeight,
			               final boolean pCenter) throws Throwable
	{
		final StringBuilder sbOptions = new StringBuilder();

		if (iX >= 0)
		{
			sbOptions.append(",left=");
			if (pCenter)
			{
				int iScreenWidth = ((Number)get("window.screen.width")).intValue();
				
				sbOptions.append((iScreenWidth - iWidth) / 2);
			}
			else
			{
				sbOptions.append(iX);
			}
		}

		if (iY >= 0)
		{
			sbOptions.append(",top=");
			if (pCenter)
			{
				int iScreenHeight = ((Number)get("window.screen.height")).intValue();
				
				sbOptions.append((iScreenHeight - iHeight) / 2);
			}
			else
			{
				sbOptions.append(iY);
			}
		}

		if (iWidth > 0)
		{
			sbOptions.append(",width=");
			sbOptions.append(iWidth);
		}

		if (iHeight > 0)
		{
			sbOptions.append(",height=");
			sbOptions.append(iHeight);
		}
		
		Thread th = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(150);
					
					final Object oWinOpened = callIntern(objWindow, 
														 "window.open", 
														 pURL, 
														 pTarget, 
														 "status=no,location=no,menubar=no,toolbar=no,dependent=yes,resizable=yes" + sbOptions.toString());

					if (oWinOpened != null)
					{
						callIntern(oWinOpened, "focus");
					}
				}
				catch (Throwable th)
				{
					th.printStackTrace();
				}
			}
		});
		
		th.start();
	}
	
	/**
	 * Calls a javascript method.
	 * 
	 * @param pMethod the method name
	 * @param pParams the parameters
	 * @return the result of the method call or <code>null</code> if the method doesn't return a value
	 * @throws Throwable if the method is not available or the call failed
	 */
	public Object call(String pMethod, Object... pParams) throws Throwable 
	{
		return callIntern(objWindow, pMethod, pParams);
	}
	
	/**
	 * Calls a javascript method.
	 * 
	 * @param pObject the object from which the method should be called
	 * @param pMethod the method name
	 * @param pParams the parameters
	 * @return the result of the method call or <code>null</code> if the method doesn't return a value
	 * @throws Throwable if the method is not available or the call failed
	 */
	private Object callIntern(Object pObject, String pMethod, Object... pParams) throws Throwable 
	{
		try 
		{
			return invoke(pObject, metCall, pMethod, new Object[] {pParams});
		} 
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
	}

	/**
	 * Sets a named member of a JavaScript object. 
	 * Equivalent to "this.<i>name</i> = <i>value</i>" in JavaScript.
	 * 
	 * @param pMember the member name
	 * @param pValue the value for the member 
	 * @throws Throwable if the member or the parent object is not available
	 */
	public void set(String pMember, Object pValue) throws Throwable
	{
		setIntern(objWindow, pMember, pValue);
	}
	
	/**
	 * Sets a named member of a JavaScript object. 
	 * Equivalent to "this.<i>name</i> = <i>value</i>" in JavaScript.
	 * 
	 * @param pObject the object from which the method should be called
	 * @param pMember the member name
	 * @param pValue the value for the member 
	 * @throws Throwable if the member or the parent object is not available
	 */
	private void setIntern(Object pObject, String pMember, Object pValue) throws Throwable
	{
		try
		{
			invoke(pObject, metSetMember, pMember, pValue);
		}
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
	}
	
	/**
	 * Retrieves a named member of a JavaScript object.
	 * Equivalent to "this.<i>name</i>" in JavaScript.
	 * 
	 * @param pMember the member name
	 * @return the value
	 * @throws Throwable if the member is or the parent object is not available
	 */
	public Object get(String pMember) throws Throwable
	{
		return getIntern(objWindow, pMember);
	}

	/**
	 * Retrieves a named member of a JavaScript object.
	 * Equivalent to "this.<i>name</i>" in JavaScript.
	 * 
	 * @param pObject the object from which the method should be called
	 * @param pMember the member name
	 * @return the value
	 * @throws Throwable if the member is or the parent object is not available
	 */
	public Object getIntern(Object pObject, String pMember) throws Throwable
	{
		try
		{
			return invoke(pObject, metGetMember, pMember);
		}
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
	}
	
	/**
	 * Gets the window object of an applet.
	 * 
	 * @param pApplet an applet
	 * @return the window object of the applet or <code>null</code> if the applet was not found
	 * @throws Throwable if the detection of the window object doesn't work
	 */
	private Object getWindow(Applet pApplet) throws Throwable
	{
		try 
		{
			return metGetWindow.invoke(objWindow, new Object[] {pApplet});
		} 
		catch (InvocationTargetException pInvocationTargetException)
		{
			throw pInvocationTargetException.getCause();
		}
	}
	
	/**
	 * Invokes a method with a named object and parameters.
	 * 
	 * @param pObject the object from which the method should be invoked
	 * @param pMethod the method to invoke
	 * @param pName the name of the object (e.g. window.location.href)
	 * @param pParams the params for the call
	 * @return the result of the invokation
	 * @throws Throwable if the desired object or member is not accessible or available
	 */
	private Object invoke(Object pObject, Method pMethod, String pName, Object... pParams) throws Throwable
	{
		ArrayUtil<String> auNames = StringUtil.separateList(pName, ".", true);
		
		Object[] oParams = new Object[1];
		

		//append additional parameters
		if (pParams != null)
		{
			oParams = ArrayUtil.addAll(oParams, pParams); 
		}
		
		if (auNames.size() > 1)
		{
			//get the callable object
			Object oInvoke = pObject;
			for (int i = 0, anz = auNames.size() - 1; i < anz; i++)
			{
				oInvoke = metGetMember.invoke(oInvoke, auNames.get(i));
			}
		
			//use the "object name"
			oParams[0] = auNames.get(auNames.size() - 1);
		
			return pMethod.invoke(oInvoke, oParams);
		}
		else
		{
			oParams[0] = pName;
			
			return pMethod.invoke(pObject, oParams);
		}
	}
	
	/**
	 * Sets the value for a cookie.
	 * 
	 * @param pName the cookie name
	 * @param pValue the value to set
	 * @param pSecondsValid sets the maximum age of the cookie in seconds 
	 * @throws Throwable if the cookie can not be set 
	 */
	public void setCookie(String pName, String pValue, long pSecondsValid) throws Throwable
	{
		String sCookie = "0x" + CodecUtil.encodeHex(pName);
		
		//delete cookie
		if (pValue == null)
		{
			pSecondsValid = -1;
			
			sCookie += "=";
		}
		else
		{
			sCookie += "=0x" + CodecUtil.encodeHex(pValue); 
		}

		sCookie += "; expires=" + FORMAT_EXPIRE.format(new Date(System.currentTimeMillis() + pSecondsValid * 1000L)) + "; path=/";
		
		setIntern(objDocument, "cookie", sCookie);
	}
	
	/**
	 * Gets the value of a cookie.
	 * 
	 * @param pName the cookie name
	 * @return the value or <code>null</code> if the cookie is not available
	 * @throws Throwable if it's not possible to access the cookie list 
	 */
	public String getCookie(String pName) throws Throwable
	{
		String sSearchName = pName + "=";
		String sSearchNameHex = "0x" + CodecUtil.encodeHex(pName) + "=";
		
		String sCookies = (String)getIntern(objDocument, "cookie");

		
		if (sCookies != null)
		{

			List<String> liCookies = StringUtil.separateList(sCookies, ";", true);

			for (String sCookie : liCookies)
			{
				if (sCookie.startsWith(sSearchName))
				{
					return sCookie.substring(sSearchName.length());
				}
				else if (sCookie.startsWith(sSearchNameHex))
				{
					return CodecUtil.decodeHex(sCookie.substring(sSearchNameHex.length() + 2));
				}
			}
		}
		
		return null;
	}
	
}	// JSBridge
