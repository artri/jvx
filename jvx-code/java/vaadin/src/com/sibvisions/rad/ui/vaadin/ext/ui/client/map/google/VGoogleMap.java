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
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google;

import java.util.HashMap;
import java.util.Map;

import javax.rad.model.IDataBook;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObject;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObjectType;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.Point;

/**
 * The client-side (GWT) widget that will render the map view a GoogleMap.
 * 
 * @author Lukas Katic
 */
public class VGoogleMap extends Widget
{

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The classname of this map.*/
    public static final String CLASSNAME = "v-map";

    
    /** The {@link Element} containing the map. */
    private Element gwtMapElement = null;
    
    /** The {@link Style} for the map. */
    private Style gwtMapElementStyle = null;

    /** The Google geocoder.*/
    private JavaScriptObject geocoder = null;

    /** The viewed map as a {@linkplain JavaScriptObject}.*/
    private JavaScriptObject map = null;

    /** The marker used for the point Selection.*/
    private JavaScriptObject selectedMarker;

    /** The {@link GoogleConnector}. */
    public GoogleConnector connector = null;

    /** A list of all markers where they can be easily be found by their groupName.*/
    private Map<String, JavaScriptObject> mapObjects = new HashMap<>();

    /** Keeps track if the map got initialized.*/
    private boolean elementInitialized = false;

    /** Keeps track if the service did load.*/
    private boolean serviceLoaded = false;
    

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@linkplain VGoogleMap}.
     */
    public VGoogleMap()
    {
        super();
        
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLoad()
    {
        super.onLoad();
        
        if (gwtMapElement == null)
        {
            gwtMapElement = Document.get().createDivElement();
            gwtMapElementStyle = gwtMapElement.getStyle();
            gwtMapElementStyle.setLeft(0, Unit.PX);
            gwtMapElementStyle.setTop(0, Unit.PX);
            gwtMapElementStyle.setHeight(100, Unit.PCT);
            gwtMapElementStyle.setWidth(100, Unit.PCT);
            gwtMapElementStyle.setPosition(Position.ABSOLUTE);
            gwtMapElementStyle.setCursor(Cursor.DEFAULT);
            getElement().appendChild(gwtMapElement);
            
            initializeGoogleMapsServiceIfNeeded();
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Adds/Updates all Markers/Polygons on the map and removes old ones.
     */
    public void update()
    {
        if (connector != null && map != null)
        {
            for (String key : mapObjects.keySet())
            {
                removeDrawable(mapObjects.get(key));
            }
            
            mapObjects.clear();
            
            Point pt;
            
            JavaScriptObject marker;

            for (MapObject stateObject : connector.getState().mapObjects)
            {
                if (stateObject.type.equals(MapObjectType.POINT))
                {
                    pt = stateObject.points.get(0);
                    
                    marker = createMarker(map, 
			                              pt.latitude, 
			                              pt.longitude, 
			                              stateObject.image != null ? stateObject.image.getURL() : null);
                    
                    mapObjects.put(stateObject.groupName, marker);
                    
                    if (pt.selected)
                    {
                    	selectedMarker = marker;
                    }
                }
                else if (stateObject.type.equals(MapObjectType.POLYGON))
                {
                    double[][] coordinatesPath = new double[stateObject.points.size()][2];
                    
                    for (int i = 0, cnt = stateObject.points.size(); i < cnt; i++)
                    {
                    	pt = stateObject.points.get(i);
                    	
                        coordinatesPath[i][0] = pt.latitude;
                        coordinatesPath[i][1] = pt.longitude;
                    }
                    
                    mapObjects.put(stateObject.groupName, createPolygon(map, coordinatesPath, stateObject.lineColor, stateObject.fillColor));
                }
            }
        }
    }
    
    /**
     * Updates the maps position and zoomLevel.
     */
    public void updateMapView()
    {
        if (map != null)
        {
            setMapPosition(map, connector.getState().position.latitude, connector.getState().position.longitude);
            setMapZoom(map, connector.getState().zoomLevel);
        }
    }
    
    /**
     * Initializes the maps service.
     */
    protected void initializeGoogleMapsServiceIfNeeded()
    {
        if (!serviceLoaded && !elementInitialized)
        {
            serviceLoaded = true;
            
            loadGoogleMapsIfNeeded(connector.getState().apiKey);
        }
    }
    
    /**
     * Initializes the element into the {@linkplain Widget}.
     */
    protected void initializeElement()
    {      
        map = createMap(gwtMapElement);
        geocoder = createGeocoder();
        update();
    }
    
    /**
     * Updates the element and its map.
     */
    public void updateElement() 
    {
        if (map != null) 
        {
            MapState state = connector.getState();
            
            setMapPosition(map, state.position.latitude, state.position.longitude);
            setMapZoom(map, state.zoomLevel);
            
            update();
        }
        
    };
    
    /**
     * Creates a new Google geocoder.
     * 
     * @return The new geocoder.
     */
    private native JavaScriptObject createGeocoder()
    /*-{
        return new $wnd.google.maps.Geocoder();
    }-*/;

    /**
     * Creates a new map with its eventListeners.
     * 
     * @param pMapElement The container for the new map.
     * @return The new Map.
     */
    private native JavaScriptObject createMap(Element pMapElement)
    /*-{
        var _this = this;

        var map = new $wnd.google.maps.Map(pMapElement, 
        {
            center: 
            {
                lat: _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::getDefaultLatitude()(),
                lng: _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::getDefaultLongitude()()
            },
            disableDefaultUI: true,
            fullscreenControl: true,
            mapTypeControl: false,
            rotateControl: false,
            scaleControl: true,
            draggableCursor: 'default',
            streetViewControl: false,
            zoom: 9,
            zoomControl: true
        });
        
        map.addListener("click", $entry(function(event)
        {
            if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::isPointSelectionOnClick()())
            {
            	if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::isPointSelectionLockedOnCenter()())
            	{
					//map.setCenter(event.latlng);
                     
                    //currently, we won't manual changes of center 
                    return;
            	}
            
            	_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::sendSelection(*)(event.latLng.lat(), event.latLng.lng());
            }
        }));

		var doDrag = function(event)
        {
            if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::isPointSelectionLockedOnCenter()()) 
            {
            	var selectedMarker = _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::selectedMarker;
            
                if (selectedMarker !== null && selectedMarker !== undefined)
                {
	                selectedMarker.setPosition(map.getCenter());
	            }
            }
        };

        //map.addListener("drag", doDrag);
        map.addListener("center_changed", doDrag);

        var doDragEnd = $entry(function(event)
        {
            if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::isPointSelectionOnClick()()
                && _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::isPointSelectionLockedOnCenter()()) 
            {
            	var pos = map.getCenter();

                var selectedMarker = _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::selectedMarker;
                
                //marker available -> only send changed value
                if (selectedMarker !== null && selectedMarker !== undefined)
                {
            		selectedMarker.setPosition(pos);                

                	_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::sendSelection(*)(pos.lat(), pos.lng());
                }
                else
                {
                    _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::sendSelection(*)(pos.lat(), pos.lng());
                }
            }        
        });
        
        //map.addListener("dragend", doDragEnd);
        //idle is fired after dragend (after move is finished)
        map.addListener("idle", doDragEnd);
        
        return map;
    }-*/;
    
    /** 
     * Sends an rpc call to save the pointSelection into the {@linkplain IDataBook groupDataBoob} in use.
     * 
     * @param pLat The Latitude of the markers Position.
     * @param pLng The Longitude of the markers Position.
     */
    private void sendSelection(double pLat, double pLng) 
    {
        connector.getRpc().savePointSelection(pLat, pLng);
    }
    
    /**
     * Gets whether point Selection is enabled.
     * 
     * @return Whether pointSelection is enabled or not.
     */
    private boolean isPointSelectionOnClick()
    {
        return connector.getState().pointSelection;
    }
    
    /**
     * Gets whether point selection is locked on center of the Map.
     * 
     * @return Whether pointSelection is enabled or not.
     */
    private boolean isPointSelectionLockedOnCenter()
    {
        return connector.getState().pointSelectionLockedOnCenter;
    }
    
    /**
     * Draws a new polygon onto given map.
     * 
     * @param pMap The map where the new Polygon should be drawn onto.
     * @param pPath The list of coordinates creating a path for the border of the new polygon.
     * @param pLineColor The color of the Border.
     * @param pFillColor The color for the Polygon to be filled with.
     * @return The new polygon as a {@linkplain JavaScriptObject}.
     */
    public native JavaScriptObject createPolygon(JavaScriptObject pMap, double[][] pPath, String pLineColor, String pFillColor) 
    /*-{
           var coords = [];
           
           for(var z = 0; z < pPath.length; z++)
           {
               coords.push(new $wnd.google.maps.LatLng(pPath[z][0],pPath[z][1]));
           }
           
           var polygon = new $wnd.google.maps.Polygon({
               paths: coords,
               strokeColor: pLineColor,
               strokeOpacity: 0.8,
               strokeWeight: 2,
               fillColor: pFillColor,
               fillOpacity: 0.4
           });
           polygon.setMap(pMap);
           
           return polygon;
    }-*/;
    
    /**
     * Draws a new marker onto given map.
     * 
     * @param pMap The map where the new marker should be drawn onto.
     * @param pLat The latitude of the markers position.
     * @param pLng The longitude of the markers position.
     * @param pImageUrl The image to be display.
     * @return The new marker as a {@linkplain JavaScriptObject}.
     */
    public native JavaScriptObject createMarker(JavaScriptObject pMap, double pLat, double pLng, String pImageUrl) 
    /*-{
        if (typeof pImageUrl !== "undefined" && pImageUrl != null)
        {
            return new $wnd.google.maps.Marker({
                    position: {lat: pLat, lng: pLng},
                        icon: pImageUrl,
                        map: pMap
                    });
        }
        else
        {
            return new $wnd.google.maps.Marker({
                    position: {lat: pLat, lng: pLng},
                        map: pMap
                    });
        }
    }-*/;    
    
    /**
     * Removes given {@linkplain JavaScriptObject} from its map.
     * 
     * @param pDrawable The {@linkplain JavaScriptObject} that should be removed.
     */
    private native void removeDrawable(JavaScriptObject pDrawable)
    /*-{
        pDrawable.setMap(null);
    }-*/;
        
    /**
     * Sets the center of given map on a specific point.
     * 
     * @param pMap The map which should be changed.
     * @param pLat The latitude of the new position.
     * @param pLng The longitude of the new position.
     */
    private native void setMapPosition(JavaScriptObject pMap, double pLat, double pLng)
    /*-{
        pMap.setCenter(new $wnd.google.maps.LatLng(pLat, pLng));
    }-*/;
    
    /**
     * Sets the zoom level of given map.
     * 
     * @param pMap The map which should be changed.
     * @param pZoom The new zoom level.
     */
    public native void setMapZoom(JavaScriptObject pMap, int pZoom)
    /*-{
        pMap.setZoom(pZoom);
    }-*/; 
    
    /**
     * Gets the default latitude from the state.
     * 
     * @return the default latitude
     */
    public double getDefaultLatitude()
    {
    	Point pos = connector.getState().position;
    	
    	if (pos != null)
    	{
    		return pos.latitude;
    	}
    	
    	return 0;
    }
    
    /**
     * Gets the default longitude from the state.
     * 
     * @return the default longitude
     */
    public double getDefaultLongitude()
    {
    	Point pos = connector.getState().position;
    	
    	if (pos != null)
    	{
    		return pos.longitude;
    	}
    	
    	return 0;
    }
    
    /**
     * Loads the google map.
     * 
     * @param pApiKey The key needed for API access.
     */
    private native void loadGoogleMapsIfNeeded(String pApiKey)
    /*-{
        if (typeof $wnd.googleMapsIsLoading === "undefined")
        {
            $wnd.googleMapsIsLoading = false;
        }
        
        if (typeof $wnd.googleMapsIsLoaded === "undefined")
        {
            $wnd.googleMapsIsLoaded = false;
        }
        
        if (typeof $wnd.googleMapsLoadList === "undefined")
        {
            $wnd.googleMapsLoadList = [];
        }
        
        var _this = this;
        
        var initializeFunction = function()
        {
            console.info("Google Maps Services loaded, initializing component.");
            _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.google.VGoogleMap::initializeElementIfNeeded()();
        }
        
        if ($wnd.googleMapsIsLoaded)
        {
            initializeFunction();
        }
        else if ($wnd.googleMapsIsLoading)
        {
            console.info("Google Maps Services is loading, deferring initialisation.");
            $wnd.googleMapsLoadList.push(initializeFunction);
        }
        else
        {
            console.info("Google Maps Services not loaded, loading service.");
            $wnd.googleMapsIsLoading = true;
            
            $wnd.googleMapsLoadList.push(initializeFunction);
            
            $wnd.afterGoogleMapsLoaded = function()
            {
                $wnd.googleMapsIsLoaded = true;
                $wnd.googleMapsIsLoading = false;
                
                while ($wnd.googleMapsLoadList.length > 0)
                {
                    $wnd.googleMapsLoadList.pop()();
                }
            };
            
            var scriptElement = $doc.createElement("script");
            scriptElement.src = "https://maps.googleapis.com/maps/api/js?key=" + pApiKey + "&libraries=places&callback=afterGoogleMapsLoaded";
            scriptElement.async = false;
            
            $doc.body.appendChild(scriptElement);
        }
    }-*/;
    
    /**
     * Initializes the element.
     */
    private void initializeElementIfNeeded()
    {
        if (!elementInitialized)
        {
            elementInitialized = true;
            
            initializeElement();
        }
    }
    
    /**
     * Sets the connector for the communication with the server-sided implementation.
     * 
     * @param pConnector The {@linkplain GoogleConnector}.
     */
    public void setConnector(GoogleConnector pConnector)
    {
        connector = pConnector;
    }
    
} 	// VGoogleMap
