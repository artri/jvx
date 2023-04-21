/*
 * Copyright 2020 SIB Visions GmbH
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
 * 15.06.2020 - [LK] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component.map.google;

import java.io.InputStream;
import java.util.Properties;

import javax.rad.genui.component.UIMap;
import javax.rad.ui.IFactory;
import javax.rad.util.ExceptionHandler;

import com.sibvisions.rad.ui.vaadin.ext.ui.map.google.GoogleMapComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinFactory;
import com.sibvisions.rad.ui.vaadin.impl.component.map.VaadinMap;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The {@link VaadinGoogleMap} is the vaadin implementation for the
 * {@linkplain UIMap} component showing a Google map.
 * 
 * @author Lukas Katic
 */
public class VaadinGoogleMap extends VaadinMap<GoogleMapComponent> 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of {@link VaadinGoogleMap} with a new {@link GoogleMapComponent}.
     */
    public VaadinGoogleMap() 
    {
        this(new GoogleMapComponent());
    }

    /**
     * Creates a new instance of {@link VaadinGoogleMap} with given {@link GoogleMapComponent}.
     * 
     * @param pComponent The component as {@link GoogleMapComponent}
     */
    protected VaadinGoogleMap(GoogleMapComponent pComponent)
    {
        super(pComponent);
    }
 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFactory(IFactory pFactory) 
    {
        super.setFactory(pFactory);
        
        //Factory prop
        String key = (String)getFactory().getProperty(VaadinFactory.PROPERTY_COMPONENT_MAP_GOOGLE_KEY);
        
        //check properties file
        if (StringUtil.isEmpty(key)) 
        {
        	key = null;
        	
            InputStream fileStream = null;
            try 
            {
                fileStream = ResourceUtil.getResourceAsStream("/googlemap.properties");
                
                if (fileStream != null) 
                {
            		Properties prop = new Properties();
            		prop.load(fileStream);
                   
            		key = prop.getProperty("apikey");
                }
            }
            catch (Exception e)
            { 
                ExceptionHandler.show(e);
            } 
            finally 
            {
                CommonUtil.close(fileStream);
            }
            
            if (key != null)
            {
            	getFactory().setProperty(VaadinFactory.PROPERTY_COMPONENT_MAP_GOOGLE_KEY, key);
            }
        }
        
        resource.getState().apiKey = CommonUtil.nvl(key, "");
    }
    
} 	// VaadinGoogleMap
