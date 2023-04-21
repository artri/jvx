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
package com.sibvisions.rad.ui.swing.impl.component.map;

import java.awt.Color;
import java.awt.Image;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IMap;
import javax.rad.util.TranslationMap;
import javax.swing.ImageIcon;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

import com.sibvisions.rad.ui.swing.impl.SwingComponent;

/**
 * The {@link SwingMap} is the Swing specific implementation of {@link IMap}.
 * <p>
 * It wraps a {@link JXMapKit}.
 * 
 * @author Robert Zenz
 */
public class SwingMap extends SwingComponent<JXMapKit> 
                      implements IMap
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the map painter. */
    private SwingMapPainter painter = null;
    
    /** the action listener for point selection. */
    private SwingMapActionListener listener = null;
    
    /** the center location. */
    private IMapLocation centerLocation = null;
    
    /** the line color. */
    private IColor lineColor = null;
    
    /** the fill color. */
    private IColor fillColor = null;
    
    /** the marker image. */
    private IImage markerImage = null;
    
    /** the current zommLevel of the map.*/
    private int zoomLevel = 9;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@link SwingMap}.
     */
    public SwingMap()
    {
        this(new JXMapKit()
        {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
            {
                // Stop animation thread, if component is not showing anymore.
                return isShowing() && super.imageUpdate(img, infoflags, x, y, w, h);
            }
        });
        
        resource.setMiniMapVisible(false);
        resource.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        resource.setZoomSliderVisible(false);
        setZoomLevel(zoomLevel);
        
        painter = new SwingMapPainter(resource);

        JXMapViewer viewer = resource.getMainMap();
        viewer.setOverlayPainter(painter);
        
        listener = new SwingMapActionListener(resource.getMainMap());

        viewer.addPropertyChangeListener(listener);
        viewer.addMouseListener(listener);
        viewer.addMouseWheelListener(listener);
        viewer.addMouseMotionListener(listener);
    }
    
    /**
     * Creates a new instance of {@link SwingMap} for a specific map implementation.
     *
     * @param pJXMapKit the {@link JXMapKit}.
     */
    protected SwingMap(JXMapKit pJXMapKit)
    {
        super(pJXMapKit);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        painter.notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public void notifyRepaint()
    {
        painter.notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveEditing() throws ModelException
    {
        // Editing is not supported.
    }
    
    /**
     * {@inheritDoc}
     */
    public IMapLocation createLocation(double pLatitude, double pLongitude)
    {
    	return new SwingMapLocation(pLatitude, pLongitude);    	
    }
    
    /**
     * {@inheritDoc}
     */
    public void setGroupColumnName(String pGroupColumnName)
    {
        painter.setGroupColumnName(pGroupColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getGroupColumnName()
    {
        return painter.getGroupColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
        painter.setLatitudeColumnName(pLatitudeColumnName);
        listener.setLatitudeColumnName(pLatitudeColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getLatitudeColumnName()
    {
        return painter.getLatitudeColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
        painter.setLongitudeColumnName(pLongitudeColumnName);
        listener.setLongitudeColumnName(pLongitudeColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getLongitudeColumnName()
    {
        return painter.getLongitudeColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName)
    {
        painter.setMarkerImageColumnName(pMarkerImageColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getMarkerImageColumnName()
    {
        return painter.getMarkerImageColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setGroupsDataBook(IDataBook pBook)
    {
        painter.setGroupsDataBook(pBook);
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getGroupsDataBook()
    {
        return painter.getGroupsDataBook();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPointsDataBook(IDataBook pBook)
    {
        painter.setPointsDataBook(pBook);
        listener.setDataBook(pBook);
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getPointsDataBook()
    {
        return painter.getPointsDataBook();
    }    

    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        painter.setTranslation(pTranslation);
    }

    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return painter.getTranslation();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        painter.setTranslationEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return painter.isTranslationEnabled();
    }
    
    /**
     * {@inheritDoc}
     */
    public String translate(String pValue)
    {
        return painter.translate(pValue);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setCenter(IMapLocation pLocation)
    {
    	centerLocation = pLocation;
    	
    	if (centerLocation == null)
    	{
    		resource.setCenterPosition(new GeoPosition(0, 0));
    	}
    	else
    	{
    		resource.setCenterPosition((GeoPosition)pLocation.getResource());
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public IMapLocation getCenter()
    {
        return centerLocation;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setZoomLevel(int pZoomLevel)
    {
        zoomLevel = Math.max(0, 19 - Math.max(0, pZoomLevel)); 

        resource.setZoom(zoomLevel);
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
    public void setPointSelectionEnabled(boolean pEnabled)
    {
         listener.setPointSelectionEnabled(pEnabled);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionEnabled()
    {
        return listener.isPointSelectionEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void setPointSelectionLockedOnCenter(boolean pEnabled)
    {
        listener.setPointSelectionLockedOnCenter(pEnabled);
    }  

    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionLockedOnCenter()
    {
        return listener.isPointSelectionLockedOnCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarker(IImage pImage)
    {
    	markerImage = pImage;
    	
    	if (pImage == null)
    	{
    		painter.setMarkerImage(null);
    	}
    	else
    	{
    		painter.setMarkerImage((ImageIcon)pImage.getResource());
    	}
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
    	
    	if (lineColor == null)
    	{
    		painter.setLineColor(null);
    	}
    	else
    	{
            painter.setLineColor((Color)lineColor.getResource());
    	}
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
    	
    	if (fillColor == null)
    	{
    		painter.setFillColor(null);
    	}
    	else
    	{
    		painter.setFillColor((Color)fillColor.getResource());
    	}
    }

    /**
     * {@inheritDoc}
     */
    public IColor getFillColor()
    {
        return fillColor;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User defined
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sets address of the tile server.
     * <p>
     * The tile server is expected to behave the same as the OpenStreetMap
     * server and should be in the format:
     * {@code http://tile.openstreetmap.org}.
     * 
     * @param pTileServerAddress the tile server address
     */
    public void setTileServerAddress(String pTileServerAddress)
    {
        resource.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo("", pTileServerAddress)));
    }
    
} 	// SwingMap
