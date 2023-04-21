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
 * 
 */
package com.sibvisions.rad.ui.swing.impl.component.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rad.genui.UIFactoryManager;
import javax.rad.model.IDataBook;
import javax.rad.model.IDataRow;
import javax.rad.model.ModelException;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.ui.IControl;
import javax.rad.ui.component.IMap;
import javax.rad.util.ExceptionHandler;
import javax.rad.util.TranslationMap;
import javax.swing.ImageIcon;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SwingMapPainter</code> is an {@link AbstractPainter} extension which
 * paints points from {@link IDataBook}s.
 * 
 * @author Robert Zenz
 */
class SwingMapPainter extends AbstractPainter<JXMapViewer> 
                      implements IControl, 
                                 Runnable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the default icon used for points. */
    private static final ImageIcon IMAGE_DEFAULT_MARKER = JVxUtil.createIcon("/com/sibvisions/rad/ui/swing/ext/images/map_defaultmarker.png");

    /** the default fill color. */
	private static final Color COLOR_DEFAULT_FILL = new Color(202, 39, 41, 41);
	
    /** the default line color. */
    private static final Color COLOR_DEFAULT_LINE = new Color(200, 0, 0, 210);
    
    /** the default thickness of lines. */
    private static final int LINE_SIZE = 2;
    
    /** the default diameter of points. */
    private static final int POINT_SIZE = 7;
    
    
    /** the map. */
    private JXMapKit map = null;

    /** the databook used for groups. */
    private IDataBook groups = null;
    
    /** the databook used for points. */
    private IDataBook points = null;

    /** the translation map. */
    private TranslationMap translationMap = null;

    /** the fill color. */
    private Color fillColor = COLOR_DEFAULT_FILL;
    
    /** the line color. */
    private Color lineColor = COLOR_DEFAULT_LINE;
    
    /** the image used for points. */
    private ImageIcon markerImage = IMAGE_DEFAULT_MARKER;
    
    /** the name of the column for the groups. */
    private String groupColumnName = IMap.COLUMNNAME_GROUP;
    
    /** the name of the column for the latitude. */
    private String latitudeColumnName = IMap.COLUMNNAME_LATITUDE;
    
    /** the name of the column for the longitude. */
    private String longitudeColumnName = IMap.COLUMNNAME_LONGITUDE;
    
    /** the name of the column which is used for the markers image .*/
    private String markerImageColumnName = IMap.COLUMNNAME_MARKERIMAGE;

    /** whether already notified.. */
    private boolean notified = false;

    /** whether translation is enabled. */
    private boolean translationEnabled = true;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>SwingMapPainter</code>.
     *
     * @param pMap the {@link JXMapKit}.
     */
    public SwingMapPainter(JXMapKit pMap)
    {
        super(false);
        
        map = pMap;
        
        setAntialiasing(true);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        // Editing not supported.
    }
    
    /**
     * {@inheritDoc}
     */
    public void notifyRepaint()
    {
        if (!notified && map != null)
        {
            notified = true;
            
            UIFactoryManager.getFactory().invokeLater(this);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        map.repaint();
        
        notified = false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveEditing() throws ModelException
    {
        // Editing not supported.
    }
    
    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslationMap)
    {
        translationMap = pTranslationMap;
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
        translationEnabled = pEnabled;
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
    public String translate(String pValue)
    {
        if (translationMap != null && translationEnabled)
        {
            return translationMap.get(pValue);
        }
        
        return pValue;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPaint(Graphics2D pGraphics, JXMapViewer pMap, int pWidth, int pHeight)
    {
    	GeoPosition selectedPosition = null;

        TileFactory tileFactory = pMap.getTileFactory();
        Rectangle viewportBounds = pMap.getViewportBounds();
        pGraphics.translate(-viewportBounds.getX(), -viewportBounds.getY());     
        
        Map<String, List<GeoPosition>> mapObjectsToDraw = new HashMap<String, List<GeoPosition>>();
        
        try
        {
            if (groups != null)
            {
            	IDataRow row;
            	
            	Number numLatitude;
            	Number numLongitude;
            	
            	int iSelected = groups.getSelectedRow();
            	
                for (int i = 0; i < groups.getRowCount(); i++)
                {
                	row = groups.getDataRow(i);
                    
                    String groupName = row.getValueAsString(groupColumnName);

                    if (!mapObjectsToDraw.containsKey(groupName))
                    {
                        mapObjectsToDraw.put(groupName, new ArrayList<GeoPosition>());
                    }
                    
                    numLatitude = (Number)row.getValue(latitudeColumnName);
                    numLongitude = (Number)row.getValue(longitudeColumnName);
                    
                	if (numLatitude != null && numLongitude != null)
                	{
	                    GeoPosition pos = new GeoPosition(numLatitude.doubleValue(),
	                                                      numLongitude.doubleValue());
	                    
	                    mapObjectsToDraw.get(groupName).add(pos);
	                    
	                	if (i == iSelected)
	                	{
	                		selectedPosition = pos;
	                	}
                	}                    
                }
            }
        }
        catch (Exception ex) 
        {
            mapObjectsToDraw.clear();
            
            ExceptionHandler.show(ex);
        }
        
        for (List<GeoPosition> position : mapObjectsToDraw.values())
        {
            if (fillColor != null && position.size() > 2)
            {
                Polygon polygon = new Polygon();
                  
                for (GeoPosition point : position)
                {
                	if (selectedPosition == null)
                	{
                		selectedPosition = point;
                	}
                	
                    Point2D currentPoint = tileFactory.geoToPixel(point, pMap.getZoom());
                    polygon.addPoint((int)currentPoint.getX(), (int)currentPoint.getY());
                }
                
                drawPolygon(pGraphics, polygon, lineColor, fillColor);    
            }
            else if (lineColor != null && position.size() > 1)
            {
                for (int i = 0, cnt = position.size(); i < cnt - 1; i++)
                {
                	if (selectedPosition == null)
                	{
                		selectedPosition = position.get(i);
                	}

                	drawLine(pGraphics,
                             tileFactory.geoToPixel(position.get(i), pMap.getZoom()),
                             tileFactory.geoToPixel(position.get(i + 1), pMap.getZoom()),
                             lineColor);
                }
            }
        }
        
        try
        {
            if (points != null)
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
	                    GeoPosition point = new GeoPosition(numLatitude.doubleValue(),
	                                                        numLongitude.doubleValue());
	                 
                    	//overwrite firstPoint if set because point overrules group
	                    if (iSelected < 0 && i == 0)
	                    {
	                    	selectedPosition = point;
	                    }	
	                    else if (i == iSelected)
	                    {
	                    	selectedPosition = point;
	                    }
	                    
	                    ImageIcon ico = null;
	                    
	                    if (hasMarkerImage)
	                    {
	                    	if (binary)
	                    	{
	                    		byte[] byData = BinaryDataType.getContent(row.getValue(markerImageColumnName));
	                    		
	                    		if (byData != null)
	                    		{
	                    			ico = new ImageIcon(byData);
	                    		}
	                    	}
	                    	else
	                    	{
	                    		String sImageName = row.getValueAsString(markerImageColumnName);
	                    		
	                    		if (!StringUtil.isEmpty(sImageName))
	                    		{
	                    			ico = JVxUtil.getIcon(sImageName);
	                    		}
	                    	}
	                    }
	                    
	                    drawMarker(pGraphics, 
	                              tileFactory.geoToPixel(point, pMap.getZoom()), 
	                              lineColor, 
	                              ico != null ? ico : markerImage);
                    }
                }
            }   

            GeoPosition posCenter = map.getCenterPosition();
            
            //default position if no position was set
        	if (posCenter.getLatitude() == 0 && posCenter.getLongitude() == 0 && selectedPosition != null)
            {
            	map.setCenterPosition(selectedPosition);
            }
        }
        catch (Exception ex) 
        {
            ExceptionHandler.show(ex);
        }
            
        pGraphics.translate(viewportBounds.getX(), viewportBounds.getY());
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      
    /**
     * Gets the name of the column which is used for the group name.
     * 
     * @return the name of the column which is used as group name.
     * @see #setGroupColumnName(String)
     */
    public String getGroupColumnName()
    {
        return groupColumnName;
    }
    
    /**
     * Sets the name of the column which is used for the group name.
     * 
     * @param pGroupColumnName the name of the column which is used for grouping
     *                         the points
     * @see #getGroupColumnName()
     */
    public void setGroupColumnName(String pGroupColumnName)
    {
    	if (pGroupColumnName == null)
    	{
    		groupColumnName = IMap.COLUMNNAME_GROUP;
    	}
    	else
    	{
    		groupColumnName = pGroupColumnName;
    	}
    }    
    
    /**
     * Gets the image for the point marker.
     * 
     * @return the marker image
     */
    public ImageIcon getMarkerImage()
    {
        return markerImage;
    }

    /**
     * Sets the image for the point marker.
     * 
     * @param pImage the marker image to use or <code>null</code> to use the default image
     */
    public void setMarkerImage(ImageIcon pImage)
    {
    	if (pImage == null)
    	{
    		markerImage = IMAGE_DEFAULT_MARKER;
    	}
    	else
    	{
    		markerImage = pImage;
    	}
    }

    /**
     * Gets the databook which is used for groups.
     * 
     * @return the groups databook
     * @see #setGroupsDataBook(IDataBook)
     */
    public IDataBook getGroupsDataBook()
    {
        return groups;
    }
    
    /**
     * Sets the databook which is used for groups.
     * 
     * @param pGroups the groups databook
     * @see #getGroupsDataBook()
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
     * Gets the databook which is used for points.
     * 
     * @return the points databook
     * @see #setPointsDataBook(IDataBook)
     */
    public IDataBook getPointsDataBook()
    {
        return points;
    }
    
    /**
     * Sets the databook which is used for points.
     * 
     * @param pPoints the {@link IDataBook} which is used to retrieve
     *            the points/coordinates.
     * @see #getPointsDataBook()
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
     * Sets the name of the column which is used for getting the latitude.
     * <p>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLatitudeColumnName the name of the column which is used for
     *                            getting the latitude
     * @see #getLatitudeColumnName()
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
    	if (latitudeColumnName == null)
    	{
    		latitudeColumnName = IMap.COLUMNNAME_LATITUDE;
    	}
    	else
    	{
    		latitudeColumnName = pLatitudeColumnName;
    	}
    }
    
    /**
     * Gets the name of the column which is used for the latitude.
     * 
     * @return the name of the column which is used for the latitude.
     * @see #setLatitudeColumnName(String)
     */
    public String getLatitudeColumnName()
    {
        return latitudeColumnName;
    }
    
    /**
     * Sets the name of the column which is used for getting the longitude.
     * <p>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLongitudeColumnName the name of the column which is used for
     *                             getting the longitude
     * @see #getLongitudeColumnName()
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
    	if (pLongitudeColumnName == null)
    	{
    		longitudeColumnName = IMap.COLUMNNAME_LONGITUDE;
    	}
    	else
    	{
    		longitudeColumnName = pLongitudeColumnName;
    	}
    }
    
    /**
     * Gets the name of the column which is used for the longitude.
     * 
     * @return the name of the column which is used for the longitude
     * @see #setLongitudeColumnName(String)
     */
    public String getLongitudeColumnName()
    {
        return longitudeColumnName;
    }
    
    /**
     * Sets the name of the column which is used for getting the marker image.
     * 
     * @param pMarkerImageColumnName the name of the column
     * @see #getMarkerImageColumnName()
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName)
    {
        markerImageColumnName = pMarkerImageColumnName;
    }
    
    /** 
     * Gets the name of the column which is used for getting the marker image.
     * 
     * @return the name of the column
     * @see #setMarkerImageColumnName(String)
     */
    public String getMarkerImageColumnName()
    {
        return markerImageColumnName;
    }
    
    /** 
     * Sets the color for lines.
     * 
     * @param pColor the line color
     */
    public void setLineColor(Color pColor)
    {
    	if (pColor == null)
    	{
    		lineColor = COLOR_DEFAULT_LINE;
    	}
    	else
    	{
    		lineColor = pColor;
    	}
    }
    
    /** 
     * Gets the color for lines.
     * 
     * @return the line color
     */
    public Color getLineColor()
    {
        return lineColor;
    }
    
    /** 
     * Sets the fill color.
     * 
     * @param pColor the fill color
     */
    public void setFillColor(Color pColor)
    {
    	if (pColor == null)
    	{
    		fillColor = COLOR_DEFAULT_FILL;
    	}
    	else
    	{
    		fillColor = pColor;
    	}
    }
    
    /** 
     * Gets the fill color.
     * 
     * @return the fill color.
     */
    public Color getFillColor()
    {
        return fillColor;
    }
    
    /**
     * Draws a line between the given points.
     * 
     * @param pGraphics the context
     * @param pPointStart the start point
     * @param pPointEnd the end point
     * @param pLineColor the line color
     */
    private void drawLine(Graphics2D pGraphics, Point2D pPointStart, Point2D pPointEnd, Color pLineColor)
    {
        pGraphics.setColor(pLineColor);
        pGraphics.setStroke(new BasicStroke(LINE_SIZE));
        pGraphics.drawLine(
                (int)pPointStart.getX(),
                (int)pPointStart.getY(),
                (int)pPointEnd.getX(),
                (int)pPointEnd.getY());
    }
    
    /**
     * Draws a marker at the given point.
     * 
     * @param pGraphics the context
     * @param pPoint the position
     * @param pColor the color.
     * @param pImage the marker image.
     */
    private void drawMarker(Graphics2D pGraphics, Point2D pPoint, Color pColor, ImageIcon pImage)
    {
        if (pImage == null)
        {
            pGraphics.setColor(pColor);
            pGraphics.fillOval(
                    (int)pPoint.getX() - POINT_SIZE / 2,
                    (int)pPoint.getY() - POINT_SIZE / 2,
                    POINT_SIZE,
                    POINT_SIZE);
        }
        else
        {
            pGraphics.drawImage(
            		pImage.getImage(),
                    (int)pPoint.getX() - (pImage.getIconWidth() / 2),
                    (int)pPoint.getY() - pImage.getIconHeight(),
                    map);
        }
    }
    
    /**
     * Draws the given polygon.
     * 
     * @param pGraphics the context
     * @param pPolygon the polygon
     * @param pLineColor the line color
     * @param pFillColor the fill color
     */
    private void drawPolygon(Graphics2D pGraphics, Polygon pPolygon, Color pLineColor, Color pFillColor)
    {
        pGraphics.setColor(pFillColor);
        pGraphics.fillPolygon(pPolygon);
        
        pGraphics.setColor(pLineColor);
        pGraphics.setStroke(new BasicStroke(LINE_SIZE));
        pGraphics.drawPolygon(pPolygon);
    }
    
}	// SwingMapPainter
