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
package javax.rad.ui.component;

import java.math.BigDecimal;

import javax.rad.model.IDataBook;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.ui.IControl;
import javax.rad.ui.IColor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IImage;
import javax.rad.ui.IResource;


/**
 * The {@link IMap} is the technology-independent interface which outlines the
 * functionality for the Map component.
 * 
 * @author Robert Zenz
 * @author Lukas Katic
 */
public interface IMap extends IComponent, 
                              IControl
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The name of the column which is used for grouping points.*/
    public String COLUMNNAME_GROUP = "GROUP";
    
    /** The name of the column which is used for the latitude.*/
    public String COLUMNNAME_LATITUDE = "LATITUDE";
    
    /** The name of the column which is used for the longitude.*/
    public String COLUMNNAME_LONGITUDE = "LONGITUDE";
    
    /** The name of the column which is used for the markers image to be displayed.*/
    public String COLUMNNAME_MARKERIMAGE = "MARKER_IMAGE";
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Create a new instance of <code>IMapLocation</code>.
     * 
     * @param pLatitude the latitude.
     * @param pLongitude the longitude.
     * @return the <code>IMapLocation</code>
     */
    public IMapLocation createLocation(double pLatitude, double pLongitude);
    
    /**
     * Sets the {@link IDataBook groupDataBook} which is used for retrieving the groups and its points in order to draw regions.
     * 
     * @param pBook The {@link IDataBook} which should be used.
     * @see #getGroupsDataBook()
     */
    public void setGroupsDataBook(IDataBook pBook);
    
    /**
     * Gets the {@link IDataBook groupDataBook} which is used for drawing regions.
     * 
     * @return The {@linkplain IDataBook} in use.
     * @see #setGroupsDataBook(IDataBook)
     */
    public IDataBook getGroupsDataBook();
    
    /**
     * Sets the {@link IDataBook pointsDataBook} which is used for retrieving points in order to draw markers.
     * 
     * @param pBook The {@link IDataBook} which should be used.
     * @see #getPointsDataBook()
     */
    public void setPointsDataBook(IDataBook pBook);
    
    /**
     * Gets the {@link IDataBook pointsDataBook} which is used for drawing markers.
     * 
     * @return The {@linkplain IDataBook} in use.
     * @see #setPointsDataBook(IDataBook)
     */
    public IDataBook getPointsDataBook();
    
    /**
     * Sets the name of the column which is used for grouping points in the groupDataBook.
     * <p>
     * The values of the column must be {@link Object#equals(Object)} to each
     * other to be counted as group.
     * 
     * @param pGroupColumnName The name of the column.
     * @see #getGroupColumnName()
     * @see #setGroupsDataBook(IDataBook)
     */
    public void setGroupColumnName(String pGroupColumnName);
    
    /**
     * Gets the name of the column which is used for grouping points in the groupDataBook.
     * 
     * @return The name of the column.
     * @see #setGroupColumnName(String)
     * @see #getGroupsDataBook()
     */
    public String getGroupColumnName();
    
    /**
     * Sets the name of the column which is used for the latitude.
     * <p>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLatitudeColumnName The name of the column.
     * @see #getLatitudeColumnName()
     */
    public void setLatitudeColumnName(String pLatitudeColumnName);
    
    /**
     * Gets the name of the column which is used for the latitude.
     * 
     * @return The name of the column.
     * @see #setLatitudeColumnName(String)
     */
    public String getLatitudeColumnName();
    
    /**
     * Sets the name of the column which is used for the longitude.
     * <p>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLongitudeColumnName The name of the column.
     * @see #getLongitudeColumnName()
     */
    public void setLongitudeColumnName(String pLongitudeColumnName);
    
    /**
     * Gets the name of the column which is used for the longitude.
     * 
     * @return The name of the column.
     * @see #setLongitudeColumnName(String)
     */
    public String getLongitudeColumnName();
    
    /**
     * Sets the name of the column which is used for a markers image to be displayed.
     * <p>
     * The value of the column must be a {@link BinaryDataType}.
     * 
     * @param pMarkerImageColumnName The name of the column.
     * @see #getMarkerImageColumnName()
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName);
    
    /** 
     * Gets the name of the column which is used for a markers image to be displayed.
     * 
     * @return The name of the column.
     * @see #setMarkerImageColumnName(String)
     */
    public String getMarkerImageColumnName();

    /**
     * Sets the view of the map on given position.
     * 
     * @param pLocation The Map location.
     * @see #getCenter()
     */
    public void setCenter(IMapLocation pLocation);
    
    /**
     * Gets the center of the map view. 
     * 
     * @return The Map location.
     * @see #setCenter(IMapLocation)
     */
    public IMapLocation getCenter();
    
    /**
     * Sets the zoom level of the map view.
     * 
     * @param pZoomLevel The zoom level.
     * @see #getZoomLevel()
     */
    public void setZoomLevel(int pZoomLevel);
    
    /**
     * Gets the current ZoomLevel of the map view.
     * 
     * @return The current zoomLevel.
     * @see #setZoomLevel(int)
     */
    public int getZoomLevel();
    
    /**
     * Defines whether the pointSelection should be enabled or not.<p>
     * For the pointSelection to work a {@linkplain IDataBook groupDataBook} is mandatory. <br>
     * When enabled, the selection will be saved into the given {@linkplain IDataBook groupDataBook} and grouped as "AutoGen_PointSelection".
     * 
     * @param pEnable The state of the pointSelection.
     * @see #setGroupsDataBook(IDataBook)
     * @see #isPointSelectionEnabled()
     */
    public void setPointSelectionEnabled(boolean pEnable);
    
    /**
     * Returns whether the pointSelection is enabled or not.
     * 
     * @return The state of the pointSelection.
     * @see #setPointSelectionEnabled(boolean)
     */
    public boolean isPointSelectionEnabled();
    
    /**
     * Defines whether the pointSelection should be locked on the center of the map or not.<p>
     * This method will only take effect when the pointSelection is enabled. Otherwise the given value will be set but ignored.
     * 
     * @param pEnable The state of pointSelectionLockedOnCenter
     * @see #setPointSelectionEnabled(boolean)
     * @see #isPointSelectionLockedOnCenter()
     */
    public void setPointSelectionLockedOnCenter(boolean pEnable);
    
    /**
     * Returns whether the pointSelection is locked on the center of the map or not.
     * 
     * @return The current state of pointSelectionLockedOnCenter.
     * @see #setPointSelectionLockedOnCenter(boolean)
     */
    public boolean isPointSelectionLockedOnCenter();
    
    /**
     * Sets the image displayed when a Marker on the map doesn't have his own image.
     * 
     * @param pImage The new image.
     */
    public void setMarker(IImage pImage);
    
    /**
     * Gets the image displayed when a Marker on the map doesn't have his own image.
     * 
     * @return The current image used for Markers.
     */
    public IImage getMarker();
    
    /** 
     * Sets the color for drawn Lines.
     * 
     * @param pColor The new color.
     */
    public void setLineColor(IColor pColor);
    
    /** 
     * Gets the color for drawn Lines.
     * 
     * @return The current color used for lines.
     */
    public IColor getLineColor();

    /** 
     * Sets the color used to fill Polygons.
     * 
     * @param pColor The new color.
     */
    public void setFillColor(IColor pColor);
    
    /** 
     * Gets the color used to fill Polygons.
     * 
     * @return The current color used to fill Polygons.
     */
    public IColor getFillColor();

    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>IMapLocation</code> is the technology-independent interface for 
     * a map location.
     * 
     * @author René Jahn
     */
    public static interface IMapLocation extends IResource
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Method definitions
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Gets the latitude.
    	 * 
    	 * @return The latitude.
    	 */
    	public double getLatitude();
    	
    	/**
    	 * Gets the longitude.
    	 * 
    	 * @return The longitude.
    	 */
    	public double getLongitude();
    	
    }	// IMapLocation
    
//    /**
//     * The <code>MapLocation</code> defines a geo location.
//     * 
//     * @author René Jahn
//     */
//    public static class MapLocation
//    {
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        // Class members
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    	
//    	/** The latitude. */
//    	private double latitude;
//    	
//    	/** The longitude. */
//    	private double longitude;
//    	
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        // Initialization
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    	
//    	/**
//    	 * Creates a new instance of <code>MapLoation</code>.
//    	 * 
//    	 * @param pLatitude The latitude.
//    	 * @param pLongitude The longitude.
//    	 */
//    	public MapLocation(double pLatitude, double pLongitude)
//    	{
//    		latitude = pLatitude;
//    		longitude = pLongitude;
//    	}
//    	
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        // User-defined methods
//        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    	
//    	/**
//    	 * Gets the latitude.
//    	 * 
//    	 * @return The latitude.
//    	 */
//    	public double getLatitude()
//    	{
//    		return latitude;
//    	}
//    	
//    	/**
//    	 * Gets the longitude.
//    	 * 
//    	 * @return The longitude.
//    	 */
//    	public double getLongitude()
//    	{
//    		return longitude;
//    	}
//    	
//    }	// MapLocation
    
} 	// IMap
