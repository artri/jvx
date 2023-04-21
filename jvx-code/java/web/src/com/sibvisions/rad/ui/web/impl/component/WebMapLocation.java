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
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.ui.component.IMap.IMapLocation;

import com.sibvisions.rad.ui.web.impl.WebResource;

/**
 * Web server implementation of {@link IMapLocation}.
 * 
 * @author Ren� Jahn
 */
public class WebMapLocation extends WebResource 
                            implements IMapLocation 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/** the latitude. */
	private double latitude;
	/** the longitude. */
	private double longitude;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    /**
     * Creates a new instance of <code>WebMapLocation</code>.
     *
     * @param pLatitude the latitude.
     * @param pLongitude the longitude. 
     */
    public WebMapLocation(double pLatitude, double pLongitude)
	{
    	latitude = pLatitude;
    	longitude = pLongitude;
	}
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
	 * {@inheritDoc}
	 */
    public String getAsString()
    {
    	return latitude + "," + longitude; 
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * {@inheritDoc}
     */
	public double getLatitude() 
	{
		return latitude;
	}

    /**
     * {@inheritDoc}
     */
	public double getLongitude()
	{
		return longitude;
	}

}	// WebMapLocation
