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
 * 27.11.2020 - [JR] - ensure technology resource for colors and image
 */
package javax.rad.genui.component;

import javax.rad.genui.UIColor;
import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.rad.model.IDataBook;
import javax.rad.model.ModelException;
import javax.rad.ui.IColor;
import javax.rad.ui.IImage;
import javax.rad.ui.component.IMap;

/**
 * The {@link UIMap} is the technology-independent implementation/wrapper for the
 * Map component.
 * 
 * @author Robert Zenz
 */
public class UIMap extends UIComponent<IMap> 
                   implements IMap
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the image. */
	private transient IImage image;
	
	/** the line color. */
	private transient IColor colLine;
	
	/** the fill color. */
	private transient IColor colFill;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>UIMap</code>.
     */
    public UIMap()
    {
        super(UIFactoryManager.getFactory().createMap());
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    public void cancelEditing()
    {
        uiResource.cancelEditing();
    }
    
    /**
     * {@inheritDoc}
     */
    public void notifyRepaint()
    {
        uiResource.notifyRepaint();
    }
    
    /**
     * {@inheritDoc}
     */

    public void saveEditing() throws ModelException
    {
        uiResource.saveEditing();
    }

    /**
     * {@inheritDoc}
     */
    public IMapLocation createLocation(double pLatitude, double pLongitude)
    {
    	return uiResource.createLocation(pLatitude, pLongitude);
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupColumnName(String pGroupColumnName)
    {
        uiResource.setGroupColumnName(pGroupColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getGroupColumnName()
    {
        return uiResource.getGroupColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLatitudeColumnName(String pLatitudeColumnName)
    {
        uiResource.setLatitudeColumnName(pLatitudeColumnName);
    }

    /**
     * {@inheritDoc}
     */
    public String getLatitudeColumnName()
    {
        return uiResource.getLatitudeColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLongitudeColumnName(String pLongitudeColumnName)
    {
        uiResource.setLongitudeColumnName(pLongitudeColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getLongitudeColumnName()
    {
        return uiResource.getLongitudeColumnName();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarkerImageColumnName(String pMarkerImageColumnName)
    {
        uiResource.setMarkerImageColumnName(pMarkerImageColumnName);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getMarkerImageColumnName()
    {
        return uiResource.getMarkerImageColumnName();
    }

    /**
     * {@inheritDoc}
     */
    public void setGroupsDataBook(IDataBook pBook)
    {
        uiResource.setGroupsDataBook(pBook);
    }
    
    /**
     * {@inheritDoc}
     */
    public IDataBook getGroupsDataBook()
    {
        return uiResource.getGroupsDataBook();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPointsDataBook(IDataBook pBook)
    {
        uiResource.setPointsDataBook(pBook);
    }

    /**
     * {@inheritDoc}
     */
    public IDataBook getPointsDataBook()
    {
        return uiResource.getPointsDataBook();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setCenter(IMapLocation pLocation)
    {
        uiResource.setCenter(pLocation);
    }
    
    /**
     * {@inheritDoc}
     */
    public IMapLocation getCenter()
    {
        return uiResource.getCenter();
    }

    /**
     * {@inheritDoc}
     */
    public void setZoomLevel(int pZoomLevel)
    {
        uiResource.setZoomLevel(pZoomLevel);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPointSelectionEnabled(boolean pEnable)
    {
        uiResource.setPointSelectionEnabled(pEnable);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionEnabled()
    {
        return uiResource.isPointSelectionEnabled();
    }

    /**
     * {@inheritDoc}
     */
    public void setPointSelectionLockedOnCenter(boolean pEnable)
    {
        uiResource.setPointSelectionLockedOnCenter(pEnable);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isPointSelectionLockedOnCenter()
    {
        return uiResource.isPointSelectionLockedOnCenter();
    }
    
    /**
     * {@inheritDoc}
     */
    public int getZoomLevel()
    {
        return uiResource.getZoomLevel();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setMarker(IImage pImage)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pImage instanceof UIImage)
		{
	    	uiResource.setMarker(((UIImage)pImage).getUIResource());
		}
		else
		{
	    	uiResource.setMarker(pImage);
		}
    	
		image = pImage;
    }
    
    /**
     * {@inheritDoc}
     */
    public IImage getMarker()
    {
    	return image;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLineColor(IColor pColor)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pColor instanceof UIColor)
		{
	    	uiResource.setLineColor(((UIColor)pColor).getUIResource());
		}
		else
		{
	    	uiResource.setLineColor(pColor);
		}
    	
		colLine = pColor;
    }
    
    /**
     * {@inheritDoc}
     */
    public IColor getLineColor()
    {
    	return colLine;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setFillColor(IColor pColor)
    {
		// ensure that the factory gets his own resource, to prevent exception on factory internal casts!
		if (pColor instanceof UIColor)
		{
	    	uiResource.setFillColor(((UIColor)pColor).getUIResource());
		}
		else
		{
	    	uiResource.setFillColor(pColor);
		}
    	
		colFill = pColor;
    }
    
    /**
     * {@inheritDoc}
     */
    public IColor getFillColor()
    {
        return colFill;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTranslation()
    {
		boolean bChanged = isTranslationChanged();
    	
    	super.updateTranslation();
    	
    	if (bChanged)
    	{
    		uiResource.setTranslation(getCurrentTranslation());
    	}
    }
    
}	// UIMap
