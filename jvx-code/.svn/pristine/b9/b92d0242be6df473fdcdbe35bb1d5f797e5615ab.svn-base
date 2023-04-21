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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.component.IMap;
import javax.rad.util.ExceptionHandler;
import javax.swing.SwingUtilities;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * The <code>SwingMapActionListener</code> is used by the {@linkplain SwingMap} to be interactive and make the 
 * point selection possible.
 * 
 * @author Lukas Katic
 */
class SwingMapActionListener implements MouseListener,
                                        MouseMotionListener,
                                        MouseWheelListener,
                                        PropertyChangeListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the databook which contains available points.*/
    private IDataBook dataBook = null;
    
    /** the displayed map.*/
    private JXMapViewer map;
    
    /** the name of the column for the latitude in given {@linkplain IDataBook groupDataBook}.*/
    private String latitudeColumnName = IMap.COLUMNNAME_LATITUDE;
    
    /** the name of the column for the longitude in given {@linkplain IDataBook groupDataBook}. */
    private String longitudeColumnName = IMap.COLUMNNAME_LONGITUDE;
    
    /** whether the point selection is enabled.*/
    private boolean pointSelection = false;
    
    /** whether the center location is locked. */
    private boolean pointSelectionLockedOnCenter = false;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of {@linkplain SwingMapActionListener}.
     * 
     * @param pMap the displayed map
     */
    public SwingMapActionListener(JXMapViewer pMap) 
    {
        map = pMap;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent pEvent) 
    {
        if (pointSelection || pointSelectionLockedOnCenter)
        {  
        	if (dataBook != null)
        	{
	        	try
	        	{
		        	if (dataBook.getSelectedRow() < 0)
		        	{
		        		dataBook.insert(false);
		        	}
	        	}
		        catch (ModelException ex)
		        {
		            ExceptionHandler.raise(ex);
		        }
        	}
        }    	
    }
    
    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent pEvent)
    {
        if (pointSelection)
        {
        	if (dataBook != null)
        	{
	            if (pointSelectionLockedOnCenter)
        		{
	            	//we won't re-center
	            	/*
        			GeoPosition pos = map.getCenterPosition();

        			try
        			{
		                dataBook.setValues(new String[] {latitudeColumnName, longitudeColumnName}, 
	                                       new Object[] {BigDecimal.valueOf(pos.getLatitude()), BigDecimal.valueOf(pos.getLongitude())});
	                }
	                catch (Exception ex)
	                {
	                    ExceptionHandler.raise(ex);
	                }
	                */
        		}
	            else
	            {
		            boolean leftclick = SwingUtilities.isLeftMouseButton(pEvent);
		            boolean singleClick = pEvent.getClickCount() == 1;
		        
		            if (leftclick && singleClick) 
		            {
		                Rectangle bounds = map.getViewportBounds();
		                
		                int x = bounds.x + pEvent.getX();
		                int y = bounds.y + pEvent.getY();
		                
		                Point pixelcoordinates = new Point(x, y);
		                GeoPosition mapCoordinates = map.getTileFactory().pixelToGeo(pixelcoordinates, map.getZoom());
		                
		                try 
		                {
		                    dataBook.setValues(new String[] {latitudeColumnName, longitudeColumnName},
		                                       new Object[] {BigDecimal.valueOf(mapCoordinates.getLatitude()), BigDecimal.valueOf(mapCoordinates.getLongitude())});
		                }
		                catch (Exception ex)
		                {
		                    ExceptionHandler.raise(ex);
		                }
		            }
	            }
        	}
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent pEvent)
    {
        if (!pointSelection || !pointSelectionLockedOnCenter)
        {
            return;
        }
        
        if (dataBook != null)
        {
	        try 
	        {
	            dataBook.saveSelectedRow();
	        }
	        catch (Exception ex)
	        {
	            ExceptionHandler.raise(ex);
	        }
        }
    }
    
    /**
     * {@inheritDoc}
     */
	public void mouseEntered(MouseEvent pEvent) 
	{
	}

    /**
     * {@inheritDoc}
     */
	public void mouseExited(MouseEvent pEvent) 
	{
	}
	
    /**
     * {@inheritDoc}
     */
    public void mouseDragged(MouseEvent pEvent) 
    {
    	updateCenterMarker();
    }

    /**
     * {@inheritDoc}
     */
	public void mouseMoved(MouseEvent pEvent) 
	{
	}
	
    /**
     * {@inheritDoc}
     */
    public void mouseWheelMoved(MouseWheelEvent pEvent)
    {
        mouseDragged(pEvent);
    }
    
    /**
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent pEvent)
    {
    	if ("zoom".equals(pEvent.getPropertyName()))
    	{
    		//zoom property will be changed, before center is changed -> invoke later to avoid
    		//getting wrong center position
    		SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
    			{
    				updateCenterMarker();
    			}
			});
    	}
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Sets the databook which is used for the points.
     * 
     * @param pDataBook the databook which should be used
     * @see #getGroupDataBook()
     */
    public void setDataBook(IDataBook pDataBook)
    {
        dataBook = pDataBook;   
    }

    /**
     * Gets the databook which is used for the points.
     * 
     * @return the databook in use
     * @see #setGroupDataBook(IDataBook)
     */
    public IDataBook getDataBook()
    {
        return dataBook;
    }
    
    /**
     * Sets the displayed map.
     * 
     * @param pMap the map
     */
    public void setMap(JXMapViewer pMap)
    {
        map = pMap;
    }
    /**
     * Gets the displayed map.
     * 
     * @return the map
     */
    public JXMapViewer getMap()
    {
        return map;
    }

    /**
     * Defines whether the point selection should be enabled or not.
     * 
     * @param pEnable <code>true</code> to enable point selection, <code>false</code> otherwise
     * @see #isPointSelectionEnabled()
     */
    public void setPointSelectionEnabled(boolean pEnable)
    {
        pointSelection = pEnable;
    }
    
    /**
     * Returns whether the point selection is enabled or not.
     * 
     * @return <code>true</code> if point selection is enabled, <code>false</code> otherwise
     * @see #setPointSelection(boolean)
     */
    public boolean isPointSelectionEnabled()
    {
        return pointSelection;
    }

    /**
     * Defines whether the pointSelection should be locked on the center of the map or not.
     * 
     * @param pEnable The new state.
     * @see #isPointSelectionLockedOnCenter()
     */
    public void setPointSelectionLockedOnCenter(boolean pEnable)
    {
        pointSelectionLockedOnCenter = pEnable;
    }
    
    /**
     * Returns whether the point selection is locked on the center of the map or not.
     * 
     * @return <code>true</code> if center is locked, <code>false</code> otherwise
     * @see #setPointSelectionLockedOnCenter(boolean)
     */
    public boolean isPointSelectionLockedOnCenter()
    {
        return pointSelectionLockedOnCenter;
    }

    /**
     * Sets the name of the column which is used for getting the latitude.
     * <p>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLatitudeColumnName the name of the column which is used for
     *                           getting the latitude.
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
        latitudeColumnName = pLatitudeColumnName;
    }

    /**
     * Gets the name of the column which is used for the latitude.
     * 
     * @return the name of the column which is used for the latitude.
     */
    public String getLatitudeColumnName()
    {
        return latitudeColumnName;
    }

    /**
     * Sets the name of the column which is used for getting the longitude.
     * <p/>
     * The value of the column must be a {@link BigDecimal}.
     * 
     * @param pLongitudeColumnName the name of the column which is used for
     *                             getting the longitude
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
        longitudeColumnName = pLongitudeColumnName;
    }
    /**
     * Gets the name of the column which is used for the longitude.
     * 
     * @return the name of the column which is used for the longitude
     */
    public String getLongitudeColumnName()
    {
        return longitudeColumnName;
    }
    
    /**
     * Updates the center marker, if necessary.
     */
    private void updateCenterMarker()
    {
        if (pointSelection && pointSelectionLockedOnCenter)
        {   
        	if (dataBook != null)
        	{
	            GeoPosition pos = map.getCenterPosition();
	        
	            try
	            {
	                dataBook.setValues(new String[] {latitudeColumnName, longitudeColumnName}, 
	                                   new Object[] {BigDecimal.valueOf(pos.getLatitude()), BigDecimal.valueOf(pos.getLongitude())});
	            }
	            catch (Exception ex)
	            {
	                ExceptionHandler.raise(ex);
	            }
        	}
        }
    }

} 	// SwingMapActionListener
