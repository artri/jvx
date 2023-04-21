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
package com.sibvisions.rad.ui.swing.impl.component.map;

import javax.rad.ui.component.IMap.IMapLocation;

import org.jxmapviewer.viewer.GeoPosition;

import com.sibvisions.rad.ui.awt.impl.AwtResource;

/**
 * The <code>SwingMapLocation</code> is the swing implementation of {@link IMapLocation}. 
 * 
 * @author René Jahn
 */
public class SwingMapLocation extends AwtResource<GeoPosition>
                              implements IMapLocation 
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>SwingMapLocation</code> for the given
	 * position.
	 * 
	 * @param pPosition the position
	 */
	public SwingMapLocation(GeoPosition pPosition)
	{
		super(pPosition);
	}

	/**
	 * Creates a new instance of <code>SwingMapLocation</code> for the given
	 * coordinates.
	 * 
	 * @param pLatitude the latitude
	 * @param pLongitude the longitude
	 */
	public SwingMapLocation(double pLatitude, double pLongitude)
	{
		this(new GeoPosition(pLatitude, pLongitude));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}	
	 */
	public double getLatitude() 
	{
		return resource.getLatitude();
	}

	/**
	 * {@inheritDoc}	
	 */
	public double getLongitude() 
	{
		return resource.getLongitude();
	}

}	// SwingMapLocation
