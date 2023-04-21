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
 * 23.06.2020 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.component.map;

import javax.rad.ui.component.IMap.IMapLocation;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.Point;
import com.sibvisions.rad.ui.vaadin.impl.VaadinResourceBase;

/**
 * The <code>VaadinMapLocation</code> is the {@link IMapLocation} implementation for vaadin.
 * 
 * @author René Jahn
 */
public class VaadinMapLocation extends VaadinResourceBase<Point> 
                               implements IMapLocation
{
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>VaadinMapLocation</code>.
     *
     * @param pLatitude the latitude.
     * @param pLongitude the longitude. 
     */
    public VaadinMapLocation(double pLatitude, double pLongitude)
	{
    	super(new Point(pLatitude, pLongitude));
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
	public double getLatitude() 
	{
		return resource.latitude;
	}

    /**
     * {@inheritDoc}
     */
	public double getLongitude()
	{
		return resource.longitude;
	}

}	// VaadinMapLocation
