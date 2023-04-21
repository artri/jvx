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
 * 17.06.2020 - [LK] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.communication.URLReference;

/**
* The {@link MapState} is an {@link AbstractComponentState} extension which
* contains the state of the map.
* 
* @author Robert Zenz
*/
public class MapState extends AbstractComponentState
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The {@link MapObjectType} defines the type of a {@link MapObject}. */
    public enum MapObjectType
    {
        /** A point/marker. */
        POINT,
        /** A polygon. */
        POLYGON;
        
    }  
    
    /** The center of the map. */
    public Point position = new Point(0.0d, 0.0d);

    /** The {@link MapObject objects} to draw. */
    public List<MapObject> mapObjects = new ArrayList<MapObject>();
    
    /** The address of the tile server. */
    public String tileServerAddress;
    
    /** The apiKey needed for the Google map. */
    public String apiKey = "";

    /** The zoom level. */
    public int zoomLevel = 9;

    /** Keeps track if the pointSelection is enabled. */
    public boolean pointSelection = false;
    
    /** Keeps track if the pointSelection should constantly have a Marker on the center of the map. */
    public boolean pointSelectionLockedOnCenter = false;
    
    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The {@link MapObject} is a simple value container which represents one
     * object on the map.
     * 
     * @author Robert Zenz
     */
    public static class MapObject implements Serializable
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** The name of the group. */
        public String groupName = null;
        
        /** The image to be Displayed. */
        public URLReference image = null;
        
        /** The color of the line. */
        public String lineColor = null;
        
        /** The color of the area. */
        public String fillColor = null;
        
        /** The {@link Point points}. */
        public List<Point> points = null;
        
        /** The the X value of the point where the <code>image</code> is pined onto the map. */
        public int imageAnchorX = 0;
        
        /** The the Y value of the point where the <code>image</code> is pined onto the map. */
        public int imageAnchorY = 0;
        
        /** The {@link MapObjectType type} of this mapObject. */
        public MapObjectType type = MapObjectType.POINT;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of {@link MapObject}.
         */
        public MapObject()
        {
            super();
            
            points = new ArrayList<Point>();
        }
        
        /**
         * Creates a new instance of {@link MapObject}.
         *
         * @param pPoints the {@link List points}.
         */
        public MapObject(List<Point> pPoints)
        {
            super();
            
            points = pPoints;
        }
        
    }   // MapObject  
    
    /**
     * The {@link Point} is a simple value container which represents a point on
     * the map.
     * 
     * @author Robert Zenz
     */
    public static class Point implements Serializable
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /** The latitude. */
        public double latitude = 0.0d;
        
        /** The longitude. */
        public double longitude = 0.0d;
        
        /** whether the point is selected. */
        public boolean selected = false;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Creates a new instance of {@link Point}.
         */
        public Point()
        {
            super();
        }
        
        /**
         * Creates a new instance of {@link Point}.
         *
         * @param pLatitude the latitude.
         * @param pLongitude the longitude.
         */
        public Point(double pLatitude, double pLongitude)
        {
            super();
            
            latitude = pLatitude;
            longitude = pLongitude;
        }
        
    } 	// Point
    
} 	//MapState
