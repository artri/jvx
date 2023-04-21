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
package com.sibvisions.rad.ui.vaadin.impl.component.map;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IMap;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.IMapRpc;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObject;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.MapObjectType;
import com.sibvisions.rad.ui.vaadin.ext.ui.client.map.MapState.Point;
import com.sibvisions.rad.ui.vaadin.ext.ui.map.MapComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinColor;
import com.sibvisions.rad.ui.vaadin.impl.VaadinComponent;
import com.sibvisions.rad.ui.vaadin.impl.VaadinImage;
import com.vaadin.server.ResourceReference;
import com.vaadin.shared.communication.URLReference;

/**
 * The {@link VaadinMap} is the vaadin implementation for the
 * {@link IMap} component showing a map.
 * 
 * @author Lukas Katic
 * @param <C> An instance of {@link MapComponent}
 */
public abstract class VaadinMap<C extends MapComponent> extends VaadinComponent<C> 
                                                        implements IMap,
                                                                   IMapRpc,
                                                                   Runnable
{    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /** The default fill color. */
	private static final VaadinColor COLOR_DEFAULT_FILL = new VaadinColor(202, 39, 41, 41);
	
    /** The default line color. */
    private static final VaadinColor COLOR_DEFAULT_LINE = new VaadinColor(200, 0, 0, 210);

    
    /** The image to use for markers. */
    private VaadinImage markerImage = null;
    
    /** The color for lines. */
    private IColor lineColor;
    
    /** The color for areas. */
    private IColor fillColor;
    
    /** The current center of the map view. */
    private IMapLocation center;
    
    /** The {@linkplain IDataBook} for all regions and their points. */
    private IDataBook groups = null;
    
    /** The {@linkplain IDataBook} for all markers. */
    private IDataBook points = null;

    /** The {@link TranslationMap}. */
    private TranslationMap translationMap = null;
    
    /** The name of the column for the groups in given {@linkplain IDataBook groupDataBook}. */
    private String groupColumnName = IMap.COLUMNNAME_GROUP;
    
    /** The name of the column for the latitude in given {@linkplain IDataBook dataBooks}. */
    private String latitudeColumnName = IMap.COLUMNNAME_LATITUDE;
    
    /** The name of the column for the longitude in given {@linkplain IDataBook dataBooks}. */
    private String longitudeColumnName = IMap.COLUMNNAME_LONGITUDE;
    
    /** The name of the column for the displayed markerImage in given {@linkplain IDataBook pointsDataBook}. */
    private String markerImageColumnName = IMap.COLUMNNAME_MARKERIMAGE;
    
    /** The current zoom of the map view. */
    private int zoomLevel = 9;

    /** Keeps track whether the translation is enabled or not. */
    private boolean translationEnabled = true;
    
	/** whether notifyRepaint is called the first time. */
	private boolean firstNotifyRepaintCall = true;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link VaadinMap}.
     *
     * @param pComponent The component in use.
     */
    protected VaadinMap(C pComponent)
    {
        super(pComponent);
        
        resource.registerRpc(this, IMapRpc.class);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void savePointSelection(double pLat, double pLng)
    {
    	if (points != null)
    	{
	        try
	        {
	        	if (points.getSelectedRow() < 0)
	        	{
	        		points.insert(false);
	        	}
	            
	            points.setValues(new String[] {latitudeColumnName, longitudeColumnName}, 
	                             new Object[] {BigDecimal.valueOf(pLat), BigDecimal.valueOf(pLng)});
	            
	            points.saveSelectedRow();
	        }
	        catch (Exception ex) 
	        {
	            /*Can only get in here when: groupDataBook == null
	                                         !groupDataBook.hasColumn(groupColumnName)
	                                         !groupDataBook.hasColumn(latitudeColumnName)
	                                         !groupDataBook.hasColumn(longitudeColumnName) */
	        	
	            ExceptionHandler.raise(ex);
	        }
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
		try
		{
			updateState();
		}
		finally
		{
			firstNotifyRepaintCall = true;
		}		
	}
    
    /**
     * {@inheritDoc}
     */
    public void notifyRepaint()
    {
    	if (firstNotifyRepaintCall)
		{
    		firstNotifyRepaintCall = false;
			
			getFactory().invokeLater(this);
		}
    }

    /**
     * {@inheritDoc}
     */
    public void saveEditing() throws ModelException
    {
        //Editing not supported.
    }

    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        //Editing not supported.
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
    public void setTranslationEnabled(boolean pTranslationEnabled)
    {
        translationEnabled = pTranslationEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return translationEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        if (translationMap != null && translationEnabled)
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
    	return new VaadinMapLocation(pLatitude, pLongitude);
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupColumnName(String pGroupColumnName)
    {
        groupColumnName = pGroupColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getGroupColumnName()
    {
        return groupColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
        latitudeColumnName = pLatitudeColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getLatitudeColumnName()
    {
        return latitudeColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
        longitudeColumnName = pLongitudeColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getLongitudeColumnName()
    {
        return longitudeColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName)
    {
        markerImageColumnName = pMarkerImageColumnName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getMarkerImageColumnName()
    {
        return markerImageColumnName;
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupsDataBook(IDataBook pGroups)
    {
        if (groups != null)
        {
            groups.removeControl(this);
        }
        
        groups = pGroups;
        
        if (groups != null)
        {
            groups.addControl(this);
        }
        
        notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataBook getGroupsDataBook()
    {
        return groups;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPointsDataBook(IDataBook pPoints)
    {
        if (points != null)
        {
            points.removeControl(this);
        }
        
        points = pPoints;
        
        if (points != null)
        {
            points.addControl(this);
        }
        
        notifyRepaint();
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getPointsDataBook()
    {
        return points;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setCenter(IMapLocation pLocation)
    {
    	center = pLocation;
    	
    	if (center == null)
    	{
    		resource.getState().position = new Point(0d, 0d);
    	}
    	else
    	{
            resource.getState().position = (Point)center.getResource();
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public IMapLocation getCenter()
    {
        return center;
    }

    /**
     * {@inheritDoc}
     */
    public void setZoomLevel(int pZoomLevel)
    {
        zoomLevel = pZoomLevel;
        resource.getState().zoomLevel = pZoomLevel;
    }    
    
    /**
     * {@inheritDoc}
     */
    public int getZoomLevel()
    {
        return zoomLevel;
    }

    /**
     * {@inheritDoc}
     */
    public void setPointSelectionEnabled(boolean pEnable)
    {
        resource.getState().pointSelection = pEnable;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionEnabled()
    {
        return resource.getState().pointSelection;
    }

    /**
     * {@inheritDoc}
     */
    public void setPointSelectionLockedOnCenter(boolean pEnable)
    {
        resource.getState().pointSelectionLockedOnCenter = pEnable;
    }  

    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionLockedOnCenter()
    {
        return resource.getState().pointSelectionLockedOnCenter;
    }
        
    /**
     * {@inheritDoc}
     */
    public void setMarker(IImage pImage)
    {
        if (pImage == null)
        {
            markerImage = null;
        }
        else
        {
            markerImage = (VaadinImage)pImage.getResource();
        }
        
        notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public IImage getMarker()
    {
        return markerImage;
    }

    /**
     * {@inheritDoc}
     */
    public void setLineColor(IColor pColor)
    {
        lineColor = pColor;
        notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public IColor getLineColor()
    {
        return lineColor;
    }

    /**
     * {@inheritDoc}
     */
    public void setFillColor(IColor pColor)
    {
    	fillColor = pColor;
        
        notifyRepaint();
    }

    /**
     * {@inheritDoc}
     */
    public IColor getFillColor()
    {
        return fillColor;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Updates the state with current groups and points.
     */
    private void updateState()
    {
        Map<String, MapObject> stateObjects = new HashMap<String, MapObject>();
        
        Point selectedPoint = null;
        
        if (groups != null)
        {
            try
            {
            	IDataRow row;
            	
            	Number numLatitude;
            	Number numLongitude;
            	
            	String groupName;
            	
            	int iSelected = groups.getSelectedRow();
            	
                for (int i = 0; i < groups.getRowCount(); i++)
                {
                    row = groups.getDataRow(i);
                    
                    groupName = row.getValueAsString(groupColumnName);
                    
                    if (!stateObjects.containsKey(groupName))
                    {
                        MapObject object = new MapObject();
                        object.groupName = groupName;
                        object.lineColor = lineColor != null ? ((VaadinColor)lineColor).getStyleValueRGB() : COLOR_DEFAULT_LINE.getStyleValueRGB();
                        object.fillColor = fillColor != null ? ((VaadinColor)fillColor).getStyleValueRGB() : COLOR_DEFAULT_FILL.getStyleValueRGB();
                        object.type = MapObjectType.POLYGON;
                        
                        stateObjects.put(groupName, object);
                    }
                    
                    numLatitude = (Number)row.getValue(latitudeColumnName);
                    numLongitude = (Number)row.getValue(longitudeColumnName);
                    
                    if (numLatitude != null && numLongitude != null)
                    {
	                    Point point = new Point();
	                    point.latitude = numLatitude.doubleValue();
	                    point.longitude = numLongitude.doubleValue();
	                    
	                    stateObjects.get(groupName).points.add(point);
	                    
	                    if (iSelected < 0 && i == 0)
	                    {
	                    	selectedPoint = point;
	                    }	
	                    else if (i == iSelected)
	                    {
	                    	selectedPoint = point;
	                    }
                    }                    
                }
            }
            catch (Exception ex)
            {
                ExceptionHandler.show(ex);
            }
        }
        
        if (points != null)
        {
            try
            {
            	IDataRow row;
            	
            	int iMarkerColPos = points.getRowDefinition().getColumnDefinitionIndex(markerImageColumnName);
            	
            	boolean hasMarkerImage = iMarkerColPos >= 0;
            	boolean binary = false;
            	
            	if (hasMarkerImage)
            	{
            		IDataType dtype = points.getRowDefinition().getColumnDefinition(iMarkerColPos).getDataType();
            		
            		if (dtype.getTypeIdentifier() == BinaryDataType.TYPE_IDENTIFIER)
            		{
            			binary = true;
            		}
            	}
            	
            	Number numLatitude;
            	Number numLongitude;

            	int iSelected = points.getSelectedRow();
            	
                for (int i = 0; i < points.getRowCount(); i++)
                {
                	row = points.getDataRow(i);
                    
                    numLatitude = (Number)row.getValue(latitudeColumnName);
                    numLongitude = (Number)row.getValue(longitudeColumnName);

                    if (numLatitude != null && numLongitude != null)
                    {
	                    MapObject object = new MapObject();
	                    object.groupName = "SinglePoint_" + i;
	                    
	                    if (hasMarkerImage && row.getValue(markerImageColumnName) != null)
	                    {
	                    	VaadinImage image = null;
	                    	
	                    	if (binary)
	                    	{
	                    		byte[] byData = BinaryDataType.getContent(row.getValue(markerImageColumnName));
	                    		
	                    		if (byData != null)
	                    		{
	                    			image = VaadinImage.getImage("marker_" + points.getName() + "_" + i, byData);
	                    		}
	                    	}
	                    	else
	                    	{
	                        	image = VaadinImage.getImage(row.getValueAsString(markerImageColumnName));
	                    	}
	                    	
	                    	if (image != null)
	                    	{
		                    	object.image = createURLReference(image);
		                        object.imageAnchorX = (int)(image.getWidth() / 2f);
		                        object.imageAnchorY = image.getHeight();
	                    	}
	                    }
	                    else if (markerImage != null)
	                    {
	                        object.image = createURLReference(markerImage);
	                        object.imageAnchorX = (int)(markerImage.getWidth() / 2f);
	                        object.imageAnchorY = markerImage.getHeight();
	                    }
                    
	                    Point point = new Point();
	                    point.latitude = numLatitude.doubleValue();
	                    point.longitude = numLongitude.doubleValue();
	                    point.selected = iSelected == i;
	                    
	                    object.points.add(point);
	                    
	                    stateObjects.put(object.groupName, object);
	                    
	                    if (iSelected < 0 && i == 0)
	                    {
	                    	selectedPoint = point;
	                    }	
	                    else if (i == iSelected)
	                    {
	                    	selectedPoint = point;
	                    }
                    }
                }
            }
            catch (Exception ex)
            {
                ExceptionHandler.show(ex);
            }
        }
        
		Point ptCenter = resource.getState().position;
        
        //default position if no position was set
    	if (ptCenter.latitude == 0 && ptCenter.longitude == 0 && selectedPoint != null)
        {
    		resource.getState().position = selectedPoint;
        }
        
        resource.getState().mapObjects = new ArrayList<MapObject>(stateObjects.values());
    }
    
    /**
     * Creates the <code>URLReference</code> from given {@linkplain VaadinImage}.
     * 
     * @param pImage The image to create the <code>URLReference</code> for.
     * @return The <code>URLReference</code> for the client to get given image.
     */
    private URLReference createURLReference(VaadinImage pImage)
    {
        URLReference result;
        if (pImage.getName().contains("https://") || pImage.getName().contains("http://"))
        {
            result = new URLReference();
            result.setURL(pImage.getName());
        }
        else
        {
            result = ResourceReference.create(pImage.getResource(), resource, pImage.getStyleName());
        }
        
        return result;
    }
    
} 	// VaadinMap
