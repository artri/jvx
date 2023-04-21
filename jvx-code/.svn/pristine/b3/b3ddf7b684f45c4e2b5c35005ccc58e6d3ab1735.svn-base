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
 * 20.08.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin;

import javax.rad.application.genui.Application;
import javax.rad.application.genui.UILauncher;
import javax.rad.genui.UIFont;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.ui.IAlignmentConstants;
import javax.servlet.annotation.WebServlet;

import com.sibvisions.rad.ui.vaadin.impl.VaadinUI;
import com.sibvisions.rad.ui.vaadin.impl.annotation.Configuration;
import com.sibvisions.rad.ui.vaadin.impl.annotation.Parameter;
import com.sibvisions.rad.ui.vaadin.impl.component.VaadinLabel;
import com.sibvisions.rad.ui.vaadin.server.VaadinServlet;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;

/**
 * The <code>MinimalApplication</code> is a simple JVx application without deployment descriptor.
 * 
 * @author René Jahn
 */
@Push
@Widgetset(value = "com.sibvisions.rad.ui.vaadin.ext.ui.Widgetset")
@Configuration({@Parameter(name = "main", value = "com.sibvisions.rad.ui.vaadin.MinimalApplication$MyApplication")})
@SuppressWarnings("serial")
public class MinimalApplication extends VaadinUI
{
    /**
     * Configures the pseudo servlet for using annotations.
     */
    @WebServlet(value = {"/minimal/*", "/VAADIN/*"}, asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MinimalApplication.class)
    public static class Servlet extends VaadinServlet 
    {
    }   // Servlet

    /**
     * The JVx application.
     */
    public static class MyApplication extends Application
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>MyApplication</code>.
         * 
         * @param pLauncher the launcher
         */
        public MyApplication(UILauncher pLauncher)
        {
            super(pLauncher);
            
            setName("Simple application");

            setLayout(new UIBorderLayout());
            
            UILabel lblHello = new UILabel("Hello JVx' vaadinUI");
            lblHello.setFont(UIFont.getDefaultFont().deriveFont(UIFont.BOLD, 16));
            lblHello.setHorizontalAlignment(IAlignmentConstants.ALIGN_CENTER);
            //example CSS without custom css file
            ((VaadinLabel)lblHello.getUIResource()).getCssExtension().addAttribute("margin-top", "100px");
            
            add(lblHello, UIBorderLayout.CENTER);
        }        
        
    }   // MyApplication
    
}   // MinimalApplication
