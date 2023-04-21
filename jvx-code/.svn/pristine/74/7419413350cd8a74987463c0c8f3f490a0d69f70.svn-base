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
 * 15.04.2013 - [JR] - creation
 */
package org.vaadin.viritin.util;

import java.util.UUID;

import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

/**
 * A helper that provides access to browser cookies.
 *
 * @author Matti Tahvonen
 */
public final class BrowserCookie 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor because <code>BrowserCookie</code> is a utility
     * class.
     */
    private BrowserCookie()
    {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets a cookie via {@link JavaScript} call.
     * 
     * @param pName the cookie name
     * @param pValue the cookie value
     */
    public static void setCookie(String pName, String pValue) 
    {
        setCookie(pName, pValue, "/");
    }
    
    /**
     * Sets a cookie via {@link JavaScript} call.
     * 
     * @param pName the cookie name
     * @param pValue the cookie value
     * @param pPath the cookie path
     */
    public static void setCookie(String pName, String pValue, String pPath) 
    {
        if (pValue == null)
        {
            JavaScript.getCurrent().execute(String.format("date = new Date();date.setDate(date.getDate() -1); " + 
                                                          "document.cookie = \"%s=%s; expires=\" + date;", pName, pValue, pPath));
        }
        else
        {
            JavaScript.getCurrent().execute(String.format("document.cookie = \"%s=%s; path=%s\";", pName, pValue, pPath));
        }
    }

    /**
     * Gets a cookie value via {@link JavaScript} call.
     * 
     * @param pName the cookie name
     * @param pCallback the callback for the async result
     */
    public static void detectCookieValue(String pName, final Callback pCallback) 
    {
        final String sCallbackId = "viritincookiecb" + UUID.randomUUID().toString().substring(0, 8);
        
        JavaScript.getCurrent().addFunction(sCallbackId, new JavaScriptFunction() 
        {
            @Override
            public void call(JsonArray arguments) 
            {
                JavaScript.getCurrent().removeFunction(sCallbackId);
                
                if (arguments.length() == 0) 
                {
                    pCallback.onValueDetected(null);
                } 
                else 
                {
                    pCallback.onValueDetected(arguments.getString(0));
                }
            }
        });
        
        JavaScript.getCurrent().execute(String.format("var nameEQ = \"%2$s=\";var ca = document.cookie.split(';');" + 
                                                      "for(var i=0;i < ca.length;i++) {var c = ca[i];while (c.charAt(0)==' ') c = c.substring(1,c.length); " +
                                                      "if (c.indexOf(nameEQ) == 0) {%1$s( c.substring(nameEQ.length,c.length)); return;};} %1$s();", 
                                        sCallbackId, pName));

    }

    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>Callback</code> is the interface for async results.
     */
    public interface Callback 
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Method definitions
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Will be invoked if value was detected.
         * 
         * @param pValue the detected value
         */
        public void onValueDetected(String pValue);
    }
    
}   // BrowserCookie
