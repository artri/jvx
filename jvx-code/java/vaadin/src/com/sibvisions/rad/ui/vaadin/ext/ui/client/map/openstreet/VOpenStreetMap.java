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
 * 
 * 15.06.2020 - [LK] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObject;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObjectType;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.Point;

/**
 * The client-side (GWT) widget that will render the map view with an OpenStreetMap.
 * 
 * @author Lukas Katic
 */
public class VOpenStreetMap extends Widget
{

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The classname of this map. */
    public static final String CLASSNAME = "v-map";

    
    /** The {@link Element} containing the map. */
    private Element gwtMapElement = null;
    
    /** The {@link Style} for the map. */
    private Style gwtMapElementStyle = null;
    
    /** The {@link OpenStreetConnector}. */
    public OpenStreetConnector connector = null;

    /** The {@link JavaScriptObject map}. */
    private JavaScriptObject map = null;

    /** The {@link JavaScriptObject tile layer}. */
    private JavaScriptObject tileLayer = null;
    
    /** Marker used for the address selection.*/
    private JavaScriptObject selectedMarker;

    /** The currently displayed objects. */
    private List<JavaScriptObject> mapObjects = new ArrayList<JavaScriptObject>();

    /** The current tile server address. */
    private String currentTileServerAddress = null;
      
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link VOpenStreetMap}.
     */
    public VOpenStreetMap()
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
        
        if (!isLeafletLoaded())
        {
            eval(IMapResources.INSTANCE.getLeafletScript().getText());
        }
        
        Element cssElement = Document.get().createStyleElement();
        cssElement.setInnerHTML(IMapResources.INSTANCE.getLeafletSheet().getText());
        getElement().appendChild(cssElement);
        
        gwtMapElement = Document.get().createDivElement();
        gwtMapElementStyle = gwtMapElement.getStyle();
        gwtMapElementStyle.setLeft(0, Unit.PX);
        gwtMapElementStyle.setTop(0, Unit.PX);
        gwtMapElementStyle.setWidth(100, Unit.PCT);
        gwtMapElementStyle.setHeight(100, Unit.PCT);
        gwtMapElementStyle.setPosition(Position.ABSOLUTE);
        gwtMapElementStyle.setCursor(Cursor.DEFAULT);
        getElement().appendChild(gwtMapElement);
        
        map = createMap(gwtMapElement);
        
        currentTileServerAddress = connector.getState().tileServerAddress;
        
        if (currentTileServerAddress != null && currentTileServerAddress.endsWith("/"))
        {
        	currentTileServerAddress = currentTileServerAddress.substring(0, currentTileServerAddress.length() - 1);
        }
        
        tileLayer = attachTileLayer(map, currentTileServerAddress);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates the map with its EventListeners.
     * 
     * @param pElement The container for the map to be displayed.
     * @return The initialized map as {@linkplain JavaScriptObject}.
     */
    private native JavaScriptObject createMap(Element pElement) 
    /*-{
        var _this = this;

        var map = $wnd.L.map(pElement);
        map.iconCache = {};
        
        var scale = $wnd.L.control.scale();
        scale.addTo(map);
        
        map._onResize();
        
        var doClick = $entry(function(event) 
        {
            if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::isPointSelectionOnClick()())
            {   
                if (_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::isPointSelectionLockedOnCenter()())
                {
                    //map.setView(event.latlng, map.getZoom());
                     
                    //currently, we won't manual changes of center 
                    return;
                }
                
            	_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::sendSelection(*)(event.latlng.lat, event.latlng.lng);
            }
        });
        
        var doMove = function(event)
        {
            if(_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::isPointSelectionLockedOnCenter()())
            {
                var selectedMarker = _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::selectedMarker;
            
            	if (selectedMarker !== null && selectedMarker !== undefined)
            	{
                	selectedMarker.setLatLng(map.getCenter());
                }
            }
        };
        
        var doMoveEnd = $entry(function(event)
        {
            if(_this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::isPointSelectionOnClick()()
               && _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::isPointSelectionLockedOnCenter()())
            {
            	var pos = map.getCenter();
            
               _this.@com.sibvisions.rad.ui.vaadin.ext.ui.client.map.openstreet.VOpenStreetMap::sendSelection(*)(pos.lat, pos.lng);
            }
        });
        
        map.on('click', doClick);
        map.on('move', doMove);
        map.on('zoom', doMove);
        map.on('moveend', doMoveEnd);
        
        return map;
    }-*/;
    
    /** 
     * Sends an rpc call to save the pointSelection.
     * 
     * @param pLat The Latitude of the Position.
     * @param pLng The Longitude of the Position.
     */
    private void sendSelection(double pLat, double pLng)
    {
        connector.getRpc().savePointSelection(pLat, pLng);
    }
    
    /**
     * Adds/Updates all Markers/Polygons on the map and removes old ones.
     */
    public void update()
    {
        if (connector != null && map != null)
        {
            for (JavaScriptObject object : mapObjects)
            {
                removeObject(object);
            }
            
            mapObjects.clear();
            
            for (MapObject mapObject : connector.getState().mapObjects)
            {
                if (mapObject.points != null && !mapObject.points.isEmpty())
                {
                	if (mapObject.type == MapObjectType.POLYGON)
                	{
                        mapObjects.add(createPolygon(map,
				                                  convertPointsToJavaScriptArray(mapObject.points),
				                                  mapObject.lineColor,
				                                  mapObject.fillColor,
				                                  mapObject.groupName));
                	}
                	else if (mapObject.type == MapObjectType.POINT)
                    {
                		JavaScriptObject jsPoint;
                		
                        for (Point point : mapObject.points)
                        {
                        	if (mapObject.image != null)
                        	{
	                        	jsPoint = createMarker(map,
			                    				   convertPointToJavaScriptArray(point),
			                    				   mapObject.image.getURL(),
				                                   mapObject.groupName,
				                                   mapObject.imageAnchorX,
				                                   mapObject.imageAnchorY);
                        	}
                        	else
                        	{
	                        	jsPoint = createMarker(map,
			                    				   convertPointToJavaScriptArray(point),
			                    				   getDefaultMarkerResourceUrl(),
				                                   mapObject.groupName,
				                                   (int)(getDefaultMarkerWidth() / 2f),
				                                   getDefaultMarkerHeight());
                        	}
                        	
                            mapObjects.add(jsPoint);
                            
                            if (point.selected)
                            {
                            	selectedMarker = jsPoint;
                            }
                        }
                    }
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
        	Point ptCenter = connector.getState().position;
        	
        	if (ptCenter != null)
        	{
        		setMapView(map, ptCenter.latitude, ptCenter.longitude, connector.getState().zoomLevel);
        	}
        }
    }
    
    /**
     * Converts the given {@link Point} to a {@link JsArrayMixed}.
     * 
     * @param pPoint The {@link Point} to convert.
     * @return the {@link JsArrayMixed} representing the given {@link Point}.
     */
    private JsArrayMixed convertPointToJavaScriptArray(Point pPoint)
    {
        JsArrayMixed arrayPoint = JsArrayMixed.createArray().cast();
        arrayPoint.push(pPoint.latitude);
        arrayPoint.push(pPoint.longitude);
        
        return arrayPoint;
    }
    
    /**
     * Converts the given {@link Point Points} to a {@link JsArrayMixed}.
     * 
     * @param pPoints the {@link Point Points} to convert.
     * @return the {@link JsArrayMixed} representing the given {@link Point Points}.
     */
    private JsArrayMixed convertPointsToJavaScriptArray(List<Point> pPoints)
    {
        JsArrayMixed arrayPoints = JsArrayMixed.createArray().cast();
        
        for (Point point : pPoints)
        {
            arrayPoints.push(convertPointToJavaScriptArray(point));
        }
        
        return arrayPoints;
    }
    
    /**
     * Notify the inner map, that it was resized.
     */
    public void notifyResized()
    {
        notifyResized(map);
    }
    
    /**
     * Notify the inner map, that it was resized.
     * 
     * @param pObject the {@link JavaScriptObject object} to remove from its parent.
     */
    private native void notifyResized(JavaScriptObject pObject)
    /*-{
        pObject._onResize();
    }-*/;
    
    /**
     * Removes the given {@link JavaScriptObject object} from its parent.
     * 
     * @param pObject the {@link JavaScriptObject object} to remove from its
     *            parent.
     */
    private native void removeObject(JavaScriptObject pObject)
    /*-{
        pObject.remove();
    }-*/;
    
    /**
     * Attaches a new tile layer to the given {@link JavaScriptObject map}.
     * 
     * @param pMap the {@link JavaScriptObject map}.
     * @param pTileServerAddress the address of the tile server.
     * @return the {@link JavaScriptObject tile layer}.
     */
    private native JavaScriptObject attachTileLayer(JavaScriptObject pMap, String pTileServerAddress)
    /*-{
        var tileLayer = $wnd.L.tileLayer(pTileServerAddress + "/{z}/{x}/{y}.png", {
            attribution: ""
        });
        
        tileLayer.addTo(pMap);
        
        return tileLayer;
    }-*/;
    
    /**
     * Adds a polygon to the given {@link JavaScriptObject map}.
     * 
     * @param pMap The {@link JavaScriptObject map}.
     * @param pPoints Tthe {@link JsArrayMixed path} to use.
     * @param pFillColor the color of the region within.
     * @param pLineColor the color of the border.
     * @param pClassNames the classnames.
     * @return the {@link JavaScriptObject polygon}.
     */
    private native JavaScriptObject createPolygon(JavaScriptObject pMap, JsArrayMixed pPoints, String pLineColor, String pFillColor, String pClassNames)
    /*-{
        var polygon = $wnd.L.polygon(pPoints, {
                className: pClassNames,
                fillColor: pFillColor,
                color: pLineColor,
                fillOpacity: 0.4,
                opacity: 1.0
        });
        
        polygon.addTo(pMap);
        
        return polygon;
    }-*/;
    
    /**
     * Adds a marker to the given {@link JavaScriptObject map}.
     * 
     * @param pMap the {@link JavaScriptObject map}.
     * @param pPoint the {@link JsArrayMixed point} to use.
     * @param pImageUrl the URL of the image to use.
     * @param pClassNames the classnames.
     * @param pAnchorX The x value of the icons anchor.
     * @param pAnchorY The y value of the icons anchor.
     * @return the {@link JavaScriptObject point/marker}.
     */
    private native JavaScriptObject createMarker(JavaScriptObject pMap, JsArrayMixed pPoint, String pImageUrl, String pClassNames, int pAnchorX, int pAnchorY)
    /*-{
        var icon = pMap.iconCache[pImageUrl];
        
        if (icon === undefined)
        {
            icon = $wnd.L.icon
            (
                {
                    iconUrl: pImageUrl,
                    iconAnchor: [pAnchorX, pAnchorY]
                }
            );
            
            pMap.iconCache[pImageUrl] = icon;
        }
        
        var point = $wnd.L.marker
        (
            pPoint, 
        	{
                className: pClassNames,
                icon: icon,
                title: pImageUrl
        	}
        );
        
        point.addTo(pMap);
        
        return point;
    }-*/;
    
    /**
     * Checks if the global Leaflet object is already loaded.
     * 
     * @return {@code true} if the global Leaflet object is already loaded.
     */
    private native boolean isLeafletLoaded()
    /*-{
        return typeof $wnd.L !== "undefined";
    }-*/;
    
    /**
     * Evaluates the given script/content as JavaScript in the context of the
     * current window.
     * 
     * @param pScriptContent the script/content to evaluate.
     */
    private native void eval(String pScriptContent)
    /*-{
        $wnd.eval(pScriptContent);
    }-*/;
    
    /**
     * Centers the given {@link JavaScriptObject map} at the given position.
     * 
     * @param pMap the {@link JavaScriptObject map}.
     * @param pLatitude the latitude of the center.
     * @param pLongitude the longitude of the center.
     */
    private native void setMapView(JavaScriptObject pMap, double pLatitude, double pLongitude)
    /*-{
        pMap.setView([pLatitude, pLongitude], pMap.getZoom());
    }-*/;
    
    /**
     * Centers the given {@link JavaScriptObject map} at the given position and applies given zoomLevel.
     * 
     * @param pMap the {@link JavaScriptObject map}.
     * @param pLatitude the latitude of the center.
     * @param pLongitude the longitude of the center.
     * @param pZoom the zoom level.
     */
    private native void setMapView(JavaScriptObject pMap, double pLatitude, double pLongitude, int pZoom)
    /*-{
        pMap.setView([pLatitude, pLongitude], pZoom);
    }-*/;
    
    /**
     * Gets the markerImage from {@linkplain IMapResources} as Base64 String.
     * 
     * @return The markerImage from {@linkplain IMapResources} as Base64 String.
     */
    private String getDefaultMarkerResourceUrl()
    {
        return IMapResources.INSTANCE.getDefaultMarkerImage().getSafeUri().asString();
    }
    
    /**
     * Gets the width of the default marker.
     * 
     * @return the width
     */
    private int getDefaultMarkerWidth()
    {
    	return IMapResources.INSTANCE.getDefaultMarkerImage().getWidth();	
    }
    
    /**
     * Gets the height of the default marker.
     * 
     * @return the height
     */
    private int getDefaultMarkerHeight()
    {
    	return IMapResources.INSTANCE.getDefaultMarkerImage().getHeight();	
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
     * Sets the connector for the communication with the server-side implementation.
     * 
     * @param pConnector The {@linkplain OpenStreetConnector}.
     */
    public void setConnector(OpenStreetConnector pConnector)
    {
        connector = pConnector;
    }
    
} 	// VOpenStreetMap
