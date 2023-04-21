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
package com.sibvisions.rad.ui.web.impl.component;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IMap;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.web.impl.WebColor;
import com.sibvisions.rad.ui.web.impl.WebComponent;

/**
 * Web server implementation of {@link IMap}.
 * 
 * @author Lukas Katic
 */
public class WebMap extends WebComponent 
                    implements IMap
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The default color for lines. */
    private static IColor colLineDefault = new WebColor(200, 0, 0, 210);
    
    /** The default color for areas. */
    private static IColor colFillDefault = new WebColor(202, 39, 41, 41);

	
    /** The {@link TranslationMap}. */
    private TranslationMap translationMap = null;
    
    /** The groups databook. */
    private IDataBook idbGroups;
    
    /** The points databook. */
    private IDataBook idbPoints;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>WebMap</code>.
     */
    public WebMap()
    {
    	setLineColor(colLineDefault);
    	setFillColor(colFillDefault);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface Implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        //Editing not supported
    }

    /**
     * {@inheritDoc}
     */
    public void notifyRepaint()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void saveEditing() throws ModelException
    {
        //Editing not supported
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        translationMap = pTranslation;
    }

    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return translationMap;
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        setProperty("translationEnabled", Boolean.valueOf(pEnabled));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return getProperty("translationEnabled", Boolean.FALSE).booleanValue();
    }

    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        if (translationMap != null && isTranslationEnabled())
        {
            return translationMap.get(pText);
        }
        
        return pText;
    }

	/**
     * {@inheritDoc}
     */
    public IMapLocation createLocation(double pLatitude, double pLongitude) 
    {
        return new WebMapLocation(pLatitude, pLongitude);
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupColumnName(String pGroupColumnName)
    {
        setProperty("groupColumnName", pGroupColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getGroupColumnName()
    {
        return getProperty("groupColumnName", IMap.COLUMNNAME_GROUP);
    }

    /**
     * {@inheritDoc}
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
        setProperty("latitudeColumnName", pLatitudeColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getLatitudeColumnName()
    {
        return getProperty("latitudeColumnName", IMap.COLUMNNAME_LATITUDE);
    }

    /**
     * {@inheritDoc}
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
        setProperty("longitudeColumnName", pLongitudeColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getLongitudeColumnName()
    {
        return getProperty("longitudeColumnName", IMap.COLUMNNAME_LONGITUDE);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName)
    {
        setProperty("markerImageColumnName", pMarkerImageColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getMarkerImageColumnName()
    {
        return getProperty("markerImageColumnName", IMap.COLUMNNAME_MARKERIMAGE);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPointSelectionLockedOnCenter(boolean pEnable)
    {
        setProperty("pointSelectionLockedOnCenter", Boolean.valueOf(pEnable));
    }  
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionLockedOnCenter()
    {
        return getProperty("pointSelectionLockedOnCenter", Boolean.FALSE).booleanValue();
    } 

    /**
     * {@inheritDoc}
     */
    public void setCenter(IMapLocation pLocation)
    {
        setProperty("center", pLocation, true);
    }

    /**
     * {@inheritDoc}
     */
    public IMapLocation getCenter()
    {
        return getProperty("center", null);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setZoomLevel(int pZoomLevel)
    {
        setProperty("zoomLevel", Integer.valueOf(pZoomLevel), true);
    }
    
    /**
     * {@inheritDoc}
     */
    public int getZoomLevel()
    {
        return getProperty("zoomLevel", Integer.valueOf(9)).intValue();
    }    

    /**
     * {@inheritDoc}
     */
    public void setPointSelectionEnabled(boolean pEnable)
    {
        setProperty("pointSelectionEnabled", Boolean.valueOf(pEnable));
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionEnabled()
    {
        return getProperty("pointSelectionEnabled", Boolean.FALSE).booleanValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setMarker(IImage pImage)
    {
        setProperty("marker", pImage);
    }

    /**
     * {@inheritDoc}
     */
    public IImage getMarker()
    {
        return getProperty("marker", null);
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupsDataBook(IDataBook pBook)
    {
    	if (idbGroups != null)
    	{
    		idbGroups.removeControl(this);
    	}
    	
    	idbGroups = pBook;
    	
    	if (idbGroups != null)
    	{
    		idbGroups.addControl(this);
    	}
    	
        setProperty("groupDataBook", pBook);
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getGroupsDataBook()
    {
        return idbGroups;
    }

    /**
     * {@inheritDoc}
     */
    public void setPointsDataBook(IDataBook pBook)
    {
    	if (idbPoints != null)
    	{
    		idbPoints.removeControl(this);
    	}

    	idbPoints = pBook;
    	
    	if (idbPoints != null)
    	{
    		idbPoints.addControl(this);
    	}

    	setProperty("pointsDataBook", pBook);
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getPointsDataBook()
    {
        return idbPoints;
    }

    /**
     * {@inheritDoc}
     */
    public void setLineColor(IColor pColor)
    {
        setProperty("lineColor", pColor);
    }

    /**
     * {@inheritDoc}
     */
    public IColor getLineColor()
    {
        return getProperty("lineColor", colLineDefault);
    }

    /**
     * {@inheritDoc}
     */
    public void setFillColor(IColor pColor)
    {
        setProperty("fillColor", pColor);
    }

    /**
     * {@inheritDoc}
     */
    public IColor getFillColor()
    {
        return getProperty("fillColor", colFillDefault);
    }
    
} 	//WebMap
