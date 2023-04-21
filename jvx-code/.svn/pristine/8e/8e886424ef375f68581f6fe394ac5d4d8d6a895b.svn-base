/*
 * Copyright 2009 SIB Visions GmbH
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
 * 17.11.2008 - [HM] - creation
 * 22.04.2011 - [JR] - getMaximizedConstraints implemented
 * 24.10.2012 - [JR] - #604: added constructor
 * 19.03.2021 - [JR] - new helper constructors
 */
package javax.rad.genui.layout;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.rad.application.genui.responsive.IResponsiveResource;
import javax.rad.application.genui.responsive.ResponsiveUtil;
import javax.rad.genui.UIComponent;
import javax.rad.genui.UIContainer;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIInsets;
import javax.rad.genui.UILayout;
import javax.rad.genui.celleditor.UICellEditor;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.control.UIEditor;
import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.IDimension;
import javax.rad.ui.IInsets;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.component.IRadioButton;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.ui.layout.IFormLayout.IConstraints;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Platform and technology independent form oriented layout.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UIFormLayout extends UILayout<IFormLayout, IConstraints> 
						  implements IFormLayout, IResponsiveResource
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      
    /** LayoutMode. */
    public enum LayoutMode
    {
        /** Default. */
        Default,
        /** Single Column. */
        SingleColumn
    } 

    /** the factory default layout modes. */
    private static HashMap<ResponsiveUtil.LayoutMode, LayoutMode> hmpFactoryDefaultLayoutMode = new HashMap<ResponsiveUtil.LayoutMode, LayoutMode>(); 
    /** the default layout modes. */
    private static HashMap<ResponsiveUtil.LayoutMode, LayoutMode> hmpDefaultLayoutMode = new HashMap<ResponsiveUtil.LayoutMode, LayoutMode>(); 

    /** The default vertical label gap. */
    private static int defaultVerticalGap = 5;
    
    /** The default horizontal label gap. */
    private static int defaultHorizontalGap = 5;

    /** The default horizontal label gap. */
    private static IInsets defaultMargins = new UIInsets(10, 10, 10, 10);

    /** The components who's preferred size should be ignored in single column mode. */ 
    private static HashSet<Class> componentsToIgnorePreferredWidth = new HashSet<Class>();
    /** The components that should be attached on right side in single mode. */ 
    private static HashSet<Class> componentsToAttachInSameRow = new HashSet<Class>();
    
    /** Stored anchor positions. */
    private transient Map<IAnchor, Integer> storedAnchorPositions = new HashMap<IAnchor, Integer>();
    /** Stored anchor auto sizes. */
    private transient Map<IAnchor, Boolean> storedAnchorAutoSizes = new HashMap<IAnchor, Boolean>();
    /** Stored maximum sizes. */
    private transient Map<IComponent, IDimension> storedMaximumSizes = new HashMap<IComponent, IDimension>();

    /** the layout modes. */
    private transient HashMap<ResponsiveUtil.LayoutMode, LayoutMode> hmpLayoutMode = new HashMap<ResponsiveUtil.LayoutMode, LayoutMode>();
    /** the current layout mode. */
    private transient LayoutMode currentLayoutMode;

    /** true, if component changed should be ignored. */
    private transient boolean ignoreComponentChanged = false;

    /** The container. */
    private transient UIContainer container = null;

    /** The current responsive mode. */
    protected transient ResponsiveUtil.LayoutMode currentResponsiveMode;
    
    /** The constraints for all components used by this layout. */
    protected transient HashMap<IComponent, IConstraints> currentComponentConstraints = new HashMap<IComponent, IConstraints>();
    
    /** Responsive flag. */
    private transient Boolean responsive = null;

    /** is notified. */
    private transient boolean isNotified = false;
        
    /** design time. */
    private transient boolean designMode = Beans.isDesignTime();

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
    static
    {
        hmpFactoryDefaultLayoutMode.put(ResponsiveUtil.LayoutMode.Full, LayoutMode.Default); 
        hmpFactoryDefaultLayoutMode.put(ResponsiveUtil.LayoutMode.Mini, LayoutMode.SingleColumn); 
        
        hmpDefaultLayoutMode.putAll(hmpFactoryDefaultLayoutMode);
    }
    
    /**
     * Creates a new instance of <code>UIFormLayout</code>.
     *
     * @see IFormLayout
     */
	public UIFormLayout()
	{
		this(UIFactoryManager.getFactory().createFormLayout());
	}

	/**
	 * Creates a new instance of <code>UIFormLayout</code> with responsive flag.
	 * 
	 * @param pResponsive responsive
	 */
	public UIFormLayout(boolean pResponsive)
	{
		this();
		
		setResponsive(Boolean.valueOf(pResponsive));
	}
	
    /**
     * Creates a new instance of <code>UIFormLayout</code> with custom margins.
     * 
     * @param pTop top margin
     * @param pLeft left margin
     * @param pBottom bottom margin
     * @param pRight right margin
     */
    public UIFormLayout(int pTop, int pLeft, int pBottom, int pRight)
    {
        this();
        
        super.setMargins(pTop, pLeft, pBottom, pRight);
    }
    
    /**
     * Creates a new instance of <code>UIFormLayout</code> with custom gaps.
     * 
     * @param pHorizontalGap horizontal gap
     * @param pVerticalGap vertical gap
     */
    public UIFormLayout(int pHorizontalGap, int pVerticalGap)
    {
        this();
        
        super.setHorizontalGap(pHorizontalGap);
        super.setVerticalGap(pVerticalGap);
    }
    
	/**
	 * Creates a new instance of <code>UIFormLayout</code> with custom anchor configuration.
	 * 
	 * @param pAnchorConfiguration the anchor configuration
	 */
	public UIFormLayout(String pAnchorConfiguration)
	{
		this();
		
		setAnchorConfiguration(pAnchorConfiguration);
	}
	
	/**
	 * Creates a new instance of <code>UIFormLayout</code> with custom margins and anchor configuration.
	 * 
	 * @param pTop top margin
	 * @param pLeft left margin
	 * @param pBottom bottom margin
	 * @param pRight right margin
	 * @param pAnchorConfiguration the anchor configuration
	 */
	public UIFormLayout(int pTop, int pLeft, int pBottom, int pRight, String pAnchorConfiguration)
	{
		this();
		
		super.setMargins(pTop, pLeft, pBottom, pRight);
		setAnchorConfiguration(pAnchorConfiguration);
	}

    /**
     * Creates a new instance of <code>UIFormLayout</code> with the given
     * layout.
     *
     * @param pLayout the layout
     * @see IFormLayout
     */
	protected UIFormLayout(IFormLayout pLayout)
	{
		super(pLayout);

		hmpLayoutMode.putAll(hmpDefaultLayoutMode);
		
		currentLayoutMode = getLayoutMode();

        super.setHorizontalGap(getDefaultHorizontalGap());
		super.setVerticalGap(getDefaultVerticalGap());
		super.setMargins(getDefaultMargins());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
	public int getHorizontalAlignment()
	{
		return uiResource.getHorizontalAlignment();
	}
	
    /**
     * {@inheritDoc}
     */
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		uiResource.setHorizontalAlignment(pHorizontalAlignment);
	}
	
    /**
     * {@inheritDoc}
     */
	public int getVerticalAlignment()
	{
		return uiResource.getVerticalAlignment();
	}

    /**
     * {@inheritDoc}
     */
    public void setVerticalAlignment(int pVerticalAlignment)
    {
    	uiResource.setVerticalAlignment(pVerticalAlignment);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public int getNewlineCount()
    {
    	return uiResource.getNewlineCount();
    }
    
	/**
	 * {@inheritDoc}
	 */
    public void setNewlineCount(int pNewlineCount)
    {
    	uiResource.setNewlineCount(pNewlineCount);
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftAnchor()
    {
		return uiResource.getLeftAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightAnchor()
    {
		return uiResource.getRightAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopAnchor()
    {
		return uiResource.getTopAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomAnchor()
    {
		return uiResource.getBottomAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getLeftMarginAnchor()
    {
		return uiResource.getLeftMarginAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getRightMarginAnchor()
    {
		return uiResource.getRightMarginAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getTopMarginAnchor()
    {
		return uiResource.getTopMarginAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor getBottomMarginAnchor()
    {
		return uiResource.getBottomMarginAnchor();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getHorizontalAnchors()
	{
    	return uiResource.getHorizontalAnchors();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IAnchor[] getVerticalAnchors()
	{
		return uiResource.getVerticalAnchors();
	}

	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor)
    {
    	return uiResource.createAnchor(pRelatedAnchor);
    }

	/**
	 * {@inheritDoc}
	 */
	public IAnchor createAnchor(IAnchor pRelatedAnchor, int pPosition)
    {
		return uiResource.createAnchor(pRelatedAnchor, pPosition);
    }
	
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(int pColumn, int pRow)
    {
    	return uiResource.getConstraints(pColumn, pRow);
    }
    
	/**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	return uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    }

    /**
	 * {@inheritDoc}
	 */
    public IConstraints getConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
    {
    	return uiResource.getConstraints(pTopAnchor, pLeftAnchor, pBottomAnchor, pRightAnchor);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isResponsive()
    {
        if (responsive == null)
        {
            return Boolean.valueOf(ResponsiveUtil.isResponsive(container));
        }
        else
        {
            return responsive;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setResponsive(Boolean pResponsive)
    {
        responsive = pResponsive;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isResponsiveSet()
    {
        return responsive != null;
    }
    
    /**
     * {@inheritDoc}
     */
    public void responsiveModeChanged(ResponsiveUtil.LayoutMode pOld, ResponsiveUtil.LayoutMode pNew)
    {
        configureResponsiveMode(pNew);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify(UIContainer pContainer)
    {
        container = pContainer;
                
        currentResponsiveMode = null; // Refresh layout mode.
        configureResponsiveMode(ResponsiveUtil.getCurrentMode(container));
        
        isNotified = true;
        componentChanged(null, null);

        ResponsiveUtil.addResource(container, this);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify(UIContainer pContainer)
    {
        isNotified = false;

        ResponsiveUtil.removeResource(container, this);
        
        // container = null; // for now, we keep it, as just the container is removed, but has maybe still the same container, next addNotify will clearify.
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void componentChanged(UIContainer pContainer, UIComponent pComponent)
    {
        if (!ignoreComponentChanged && (pContainer == null || pContainer.indexOf(pComponent) >= 0))
        {
            ignoreComponentChanged = true;
            try
            {
                configureConstraints();
            }
            finally
            {
                ignoreComponentChanged = false;
            }
        }
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Adds a component class who's preferred width should be ignored. 
     * @param pClazz the component class
     */
    public static void registerComponentToIgnorePreferredWidth(Class pClazz)
    {
        componentsToIgnorePreferredWidth.add(pClazz);
    }
    
    /**
     * Adds a component class who's preferred width should be ignored. 
     * @param pClazz the component class
     */
    public static void unregisterComponentToIgnorePreferredWidth(Class pClazz)
    {
        componentsToIgnorePreferredWidth.remove(pClazz);
    }

    /**
     * Gets all component classes who's preferred width should be ignored. 
     * @return all component classes who's preferred width should be ignored. 
     */
    public static Class[] getComponentsToIgnorePreferredWidth()
    {
        return componentsToIgnorePreferredWidth.toArray(new Class[componentsToIgnorePreferredWidth.size()]);
    }
    
    /**
     * Adds a component class which should be attached in same row in single column mode. 
     * @param pClazz the component class
     */
    public static void registerComponentToAttachInSameRow(Class pClazz)
    {
        componentsToAttachInSameRow.add(pClazz);
    }
    
    /**
     * Adds a component class which should be attached in same row in single column mode. 
     * @param pClazz the component class
     */
    public static void unregisterComponentToAttachInSameRow(Class pClazz)
    {
        componentsToAttachInSameRow.remove(pClazz);
    }

    /**
     * Gets all component classee which should be attached in same row in single column mode. 
     * @return all component classes which should be attached in same row in single column mode. 
     */
    public static Class[] getComponentsToAttachInSameRow()
    {
        return componentsToAttachInSameRow.toArray(new Class[componentsToAttachInSameRow.size()]);
    }
    
    /**
     * Returns the default margins.
     * 
     * @return returns the default margins.
     */
    public static IInsets getDefaultMargins()
    {
        return defaultMargins;
    }

    /**
     * Sets the default margins.
     * 
     * @param pDefaultMargins the default margins.
     */
    public static void setDefaultMargins(IInsets pDefaultMargins)
    {
        if (pDefaultMargins == null)
        {
            pDefaultMargins = new UIInsets(10, 10, 10, 10);
        }

        defaultMargins = pDefaultMargins;
    }
    
    /**
     * Returns the default horizontal gap between components.
     * 
     * @return returns the default horizontal gap between components.
     */
    public static int getDefaultHorizontalGap()
    {
        return defaultHorizontalGap;
    }

    /**
     * Sets the default horizontal gap between components.
     * 
     * @param pDefaultHorizontalGap the default horizontal gap between components.
     */
    public static void setDefaultHorizontalGap(int pDefaultHorizontalGap)
    {
        defaultHorizontalGap = pDefaultHorizontalGap;
    }

    /**
     * Returns the default vertical gap between components.
     * 
     * @return returns the default vertical gap between components.
     */
    public static int getDefaultVerticalGap()
    {
        return defaultVerticalGap;
    }

    /**
     * Sets the default vertical gap between components.
     * 
     * @param pDefaultVerticalGap the default vertical gap between components
     */
    public static void setDefaultVerticalGap(int pDefaultVerticalGap)
    {
        defaultVerticalGap = pDefaultVerticalGap;
    }
    
	/**
	 * Gets the left anchor of the given column.
	 * 
	 * @param pColumnIndex the column index.
	 * @return the left IAnchor 
	 */
	public IAnchor getColumnLeftAnchor(int pColumnIndex)
	{
		return getConstraints(pColumnIndex, 0).getLeftAnchor();
	}
	
	/**
	 * Gets the right anchor of the given column.
	 * 
	 * @param pColumnIndex the column index.
	 * @return the right IAnchor 
	 */
	public IAnchor getColumnRightAnchor(int pColumnIndex)
	{
		return getConstraints(pColumnIndex, 0).getRightAnchor();
	}
	
	/**
	 * Gets the top anchor of the given row.
	 * 
	 * @param pRowIndex the row index.
	 * @return the top IAnchor 
	 */
	public IAnchor getRowTopAnchor(int pRowIndex)
	{
		return getConstraints(0, pRowIndex).getTopAnchor();
	}
	
	/**
	 * Gets the bottom anchor of the given row.
	 * 
	 * @param pRowIndex the row index.
	 * @return the bottom IAnchor 
	 */
	public IAnchor getRowBottomAnchor(int pRowIndex)
	{
		return getConstraints(0, pRowIndex).getBottomAnchor();
	}

	/**
     * Gets the anchor configuration for default anchors.
     * 
     * @return the anchor configuration for default anchors.
     */
    public String getAnchorConfiguration()
    {
        if (currentLayoutMode == LayoutMode.SingleColumn)
        {
            restoreAnchorsFromSingleColumnMode();
        }
        
        try
        {
            return getAnchorConfigurationIntern();
        }
        finally
        {
            if (currentLayoutMode == LayoutMode.SingleColumn)
            {
                configureAnchorsForSingleColumnMode();
            }
        }
    }
    
    /**
     * Sets the anchor configuration for default anchors.
     * 
     * @param pAnchorConfiguration the anchor configuration for default anchors.
     */
    public void setAnchorConfiguration(String pAnchorConfiguration)
    {
        if (currentLayoutMode == LayoutMode.SingleColumn)
        {
            restoreAnchorsFromSingleColumnMode();
        }
        
        try
        {
            setAnchorConfigurationIntern(pAnchorConfiguration);
        }
        finally
        {
            if (currentLayoutMode == LayoutMode.SingleColumn)
            {
                configureAnchorsForSingleColumnMode();
            }
        }
    }
    
	/**
	 * Gets the anchor configuration for default anchors.
	 * 
	 * @return the anchor configuration for default anchors.
	 */
	protected String getAnchorConfigurationIntern()
	{
		StringBuilder anchorConfiguration = new StringBuilder();
		
		IAnchor[] anchors = getHorizontalAnchors();
		
		int i = 0;
		IAnchor anchor = getColumnRightAnchor(i);
		IAnchor nextAnchor = getColumnRightAnchor(i + 1);
		while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
		{
			if (i > 0 && getColumnLeftAnchor(i).getPosition() != getHorizontalGap())
			{
				anchorConfiguration.append('l');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(getColumnLeftAnchor(i).getPosition());
				anchorConfiguration.append(',');
			}
			if (!anchor.isAutoSize())
			{
				anchorConfiguration.append('r');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(anchor.getPosition());
				anchorConfiguration.append(',');
			}
			
			i++;
			anchor = nextAnchor;
			nextAnchor = getColumnRightAnchor(i + 1);
		}
		if (i > 0 && getColumnLeftAnchor(i).getPosition() != getHorizontalGap())
		{
			anchorConfiguration.append('l');
			anchorConfiguration.append(i);
			anchorConfiguration.append('=');
			anchorConfiguration.append(getColumnLeftAnchor(i).getPosition());
			anchorConfiguration.append(',');
		}
		i = -1;
		anchor = getColumnLeftAnchor(i);
		nextAnchor = getColumnLeftAnchor(i - 1);
		while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
		{
			if (i < -1 && getColumnRightAnchor(i).getPosition() != -getHorizontalGap())
			{
				anchorConfiguration.append('r');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(getColumnRightAnchor(i).getPosition());
				anchorConfiguration.append(',');
			}
			if (!anchor.isAutoSize())
			{
				anchorConfiguration.append('l');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(anchor.getPosition());
				anchorConfiguration.append(',');
			}
			
			i--;
			anchor = nextAnchor;
			nextAnchor = getColumnLeftAnchor(i - 1);
		}
		if (i < -1 && getColumnRightAnchor(i).getPosition() != -getHorizontalGap())
		{
			anchorConfiguration.append('r');
			anchorConfiguration.append(i);
			anchorConfiguration.append('=');
			anchorConfiguration.append(getColumnRightAnchor(i).getPosition());
			anchorConfiguration.append(',');
		}
		
		anchors = getVerticalAnchors();
		
		i = 0;
		anchor = getRowBottomAnchor(i);
		nextAnchor = getRowBottomAnchor(i + 1);
		while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
		{
			if (i > 0 && getRowTopAnchor(i).getPosition() != getVerticalGap())
			{
				anchorConfiguration.append('t');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(getRowTopAnchor(i).getPosition());
				anchorConfiguration.append(',');
			}
			if (!anchor.isAutoSize())
			{
				anchorConfiguration.append('b');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(anchor.getPosition());
				anchorConfiguration.append(',');
			}
			
			i++;
			anchor = nextAnchor;
			nextAnchor = getRowBottomAnchor(i + 1);
		}
		if (i > 0 && getRowTopAnchor(i).getPosition() != getVerticalGap())
		{
			anchorConfiguration.append('t');
			anchorConfiguration.append(i);
			anchorConfiguration.append('=');
			anchorConfiguration.append(getRowTopAnchor(i).getPosition());
			anchorConfiguration.append(',');
		}
		i = -1;
		anchor = getRowTopAnchor(i);
		nextAnchor = getRowTopAnchor(i - 1);
		while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
				|| ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
		{
			if (i < -1 && getRowBottomAnchor(i).getPosition() != -getVerticalGap())
			{
				anchorConfiguration.append('b');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(getRowBottomAnchor(i).getPosition());
				anchorConfiguration.append(',');
			}
			if (!anchor.isAutoSize())
			{
				anchorConfiguration.append('t');
				anchorConfiguration.append(i);
				anchorConfiguration.append('=');
				anchorConfiguration.append(anchor.getPosition());
				anchorConfiguration.append(',');
			}
			
			i--;
			anchor = nextAnchor;
			nextAnchor = getRowTopAnchor(i - 1);
		}
		if (i < -1 && getRowBottomAnchor(i).getPosition() != -getVerticalGap())
		{
			anchorConfiguration.append('b');
			anchorConfiguration.append(i);
			anchorConfiguration.append('=');
			anchorConfiguration.append(getRowBottomAnchor(i).getPosition());
			anchorConfiguration.append(',');
		}
		
		if (anchorConfiguration.length() > 0)
		{
			anchorConfiguration.setLength(anchorConfiguration.length() - 1);
			return anchorConfiguration.toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the anchor configuration for default anchors.
	 * 
	 * @param pAnchorConfiguration the anchor configuration for default anchors.
	 */
	protected void setAnchorConfigurationIntern(String pAnchorConfiguration)
	{
		if (pAnchorConfiguration != null)
		{
			String[] anchorConfigurations = pAnchorConfiguration.split(",");
			
			for (int i = 0; i < anchorConfigurations.length; i++)
			{
				String anchorConfig = anchorConfigurations[i].trim();
				
				int eqIndex = anchorConfig.indexOf("=");
				
				if (eqIndex > 0)
				{
					try
					{
						char anchorType = anchorConfig.charAt(0);
						int  colOrRow   = Integer.parseInt(anchorConfig.substring(1, eqIndex).trim());
						int  position   = Integer.parseInt(anchorConfig.substring(eqIndex + 1).trim());
						
						IAnchor anchor;
						switch (anchorType)
						{
							case 'l': anchor = getColumnLeftAnchor(colOrRow); break;
							case 'r': anchor = getColumnRightAnchor(colOrRow); break;
							case 't': anchor = getRowTopAnchor(colOrRow); break;
							case 'b': anchor = getRowBottomAnchor(colOrRow); break;
							default: anchor = null; break;
						}
						if (anchor != null)
						{
							anchor.setAutoSize(false);
							anchor.setPosition(position);
						}
					}
					catch (Exception ex)
					{
						// Ignore wrong configured anchors...
					}
				}
			}
		}
	}
	
	/**
	 * Creates vertical centered constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getVCenterConstraints(int pColumn, int pRow)
    {
    	return getVCenterConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates vertical centered constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column count.
	 * @param pEndRow the row count.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getVCenterConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setTopAnchor(createAnchor(result.getTopAnchor()));
    	result.setBottomAnchor(createAnchor(result.getBottomAnchor()));
    	
    	return result;
    }
    
	/**
	 * Creates vertical centered constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getHCenterConstraints(int pColumn, int pRow)
    {
    	return getHCenterConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates vertical centered constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column count.
	 * @param pEndRow the row count.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getHCenterConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setLeftAnchor(createAnchor(result.getLeftAnchor()));
    	result.setRightAnchor(createAnchor(result.getRightAnchor()));
    	
    	return result;
    }
    
	/**
	 * Creates horizontal and vertical centered constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getCenterConstraints(int pColumn, int pRow)
    {
    	return getHCenterConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates horizontal and vertical centered constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column count.
	 * @param pEndRow the row count.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getCenterConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setLeftAnchor(createAnchor(result.getLeftAnchor()));
    	result.setRightAnchor(createAnchor(result.getRightAnchor()));
    	result.setTopAnchor(createAnchor(result.getTopAnchor()));
    	result.setBottomAnchor(createAnchor(result.getBottomAnchor()));
    	
    	return result;
    }
    
	/**
	 * Creates the constraints left of the center constraint.
	 * 
	 * @param pCenterComponent the center component.
	 * @return the constraints left of the center constraint.
	 */
    public IConstraints getLeftSpaceConstraints(IComponent pCenterComponent)
    {
    	IConstraints centerConstraints = getConstraints(pCenterComponent);
    	
		return getConstraints(centerConstraints.getTopAnchor(), 
							  createAnchor(centerConstraints.getLeftAnchor().getRelatedAnchor(), 0), 
							  centerConstraints.getBottomAnchor(), 
							  centerConstraints.getLeftAnchor());
    }
    
	/**
	 * Creates the constraints right of the center constraint.
	 * 
	 * @param pCenterComponent the center component.
	 * @return the constraints right of the center constraint.
	 */
    public IConstraints getRightSpaceConstraints(IComponent pCenterComponent)
    {
    	IConstraints centerConstraints = getConstraints(pCenterComponent);
    	
		return getConstraints(centerConstraints.getTopAnchor(), 
							  centerConstraints.getRightAnchor(), 
							  centerConstraints.getBottomAnchor(), 
							  createAnchor(centerConstraints.getRightAnchor().getRelatedAnchor(), 0));
    }
    
	/**
	 * Creates the constraints top of the center constraint.
	 * 
	 * @param pCenterComponent the center component.
	 * @return the constraints top of the center constraint.
	 */
    public IConstraints getTopSpaceConstraints(IComponent pCenterComponent)
    {
    	IConstraints centerConstraints = getConstraints(pCenterComponent);
    	
		return getConstraints(createAnchor(centerConstraints.getTopAnchor().getRelatedAnchor(), 0),
							  centerConstraints.getLeftAnchor(), 
							  centerConstraints.getTopAnchor(), 
							  centerConstraints.getRightAnchor());
    }
    
	/**
	 * Creates the constraints bottom of the center constraint.
	 * 
	 * @param pCenterComponent the center component.
	 * @return the constraints bottom of the center constraint.
	 */
    public IConstraints getBottomSpaceConstraints(IComponent pCenterComponent)
    {
    	IConstraints centerConstraints = getConstraints(pCenterComponent);
    	
		return getConstraints(centerConstraints.getBottomAnchor(), 
							  centerConstraints.getLeftAnchor(), 
							  createAnchor(centerConstraints.getBottomAnchor().getRelatedAnchor(), 0),
							  centerConstraints.getRightAnchor());
    }
    
	/**
	 * Creates top aligned constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getTopAlignedConstraints(int pColumn, int pRow)
    {
    	return getTopAlignedConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates top aligned constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column.
	 * @param pEndRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getTopAlignedConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setBottomAnchor(null);
    	
    	return result;
    }
    
	/**
	 * Creates bottom aligned constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getBottomAlignedConstraints(int pColumn, int pRow)
    {
    	return getBottomAlignedConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates bottom aligned constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column.
	 * @param pEndRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getBottomAlignedConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setTopAnchor(null);
    	
    	return result;
    }
    
	/**
	 * Creates left aligned constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getLeftAlignedConstraints(int pColumn, int pRow)
    {
    	return getLeftAlignedConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates left aligned constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column.
	 * @param pEndRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getLeftAlignedConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setRightAnchor(null);
    	
    	return result;
    }
    
	/**
	 * Creates right aligned constraints for the given column and row.
	 * 
	 * @param pColumn the column.
	 * @param pRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getRightAlignedConstraints(int pColumn, int pRow)
    {
    	return getRightAlignedConstraints(pColumn, pRow, pColumn, pRow);
    }
    
	/**
	 * Creates right aligned constraints for the given column and row.
	 * 
	 * @param pBeginColumn the column.
	 * @param pBeginRow the row.
	 * @param pEndColumn the column.
	 * @param pEndRow the row.
	 * @return the constraints for the given column and row.
	 */
    public IConstraints getRightAlignedConstraints(int pBeginColumn, int pBeginRow, int pEndColumn, int pEndRow)
    {
    	IConstraints result = uiResource.getConstraints(pBeginColumn, pBeginRow, pEndColumn, pEndRow);
    	result.setLeftAnchor(null);
    	
    	return result;
    }
    
    /**
     * Creates maximized constraints for top, left, bottom and right anchor.
     *  
     * @return the maximized constraints.
     */
    public IConstraints getMaximizedConstraints()
    {
    	return uiResource.getConstraints(getTopAnchor(), getLeftAnchor(), getBottomAnchor(), getRightAnchor());
    }
    
    /**
     * Configures the current responsive mode.
     * 
     * @param pMode the current responsive mode
     */
    protected void configureResponsiveMode(ResponsiveUtil.LayoutMode pMode)
    {
        if (isResponsive().booleanValue() && !designMode)
        {
            if (currentResponsiveMode != pMode)
            {
                currentResponsiveMode = pMode;

                configureResponsiveModeIntern(currentResponsiveMode);
            }
        }
    }
    
    /**
     * Configures the current responsive mode.
     * 
     * @param pMode the current responsive mode
     */
    protected void configureResponsiveModeIntern(ResponsiveUtil.LayoutMode pMode)
    {
        setCurrentLayoutMode(LayoutMode.valueOf(
                        ResponsiveUtil.getResponsiveParameter(container, this, pMode, "layoutMode", 
                                                              getLayoutModeSmall(), getLayoutModeMini())));
    }
    
    /**
     * True, if layout is notified.
     * @return True, if layout is notified.
     */
    protected boolean isNotified()
    {
        return isNotified;
    }
    
    
    /**
     * Gets the container to this layout.
     * 
     * @return the container to this layout.
     */
    protected UIContainer getContainer()
    {
        return container;
    }
    
    /**
     * Gets the default layout mode for the given {@link javax.rad.application.genui.responsive.ResponsiveUtil.LayoutMode}.
     * @param pMode the layout mode
     * @return the default layout mode
     */
    protected static LayoutMode getDefaultLayoutMode(ResponsiveUtil.LayoutMode pMode)
    {
        if (pMode == null)
        {
            pMode = ResponsiveUtil.LayoutMode.Full;
        }
        
        return hmpDefaultLayoutMode.get(pMode);
    }
    
    /**
     * Sets the default layout mode for the given {@link javax.rad.application.genui.responsive.ResponsiveUtil.LayoutMode}.
     * @param pMode the layout mode
     * @param pLayoutMode the layout mode
     */
    protected static void setDefaultLayoutMode(ResponsiveUtil.LayoutMode pMode, LayoutMode pLayoutMode)
    {
        if (pMode == null)
        {
            pMode = ResponsiveUtil.LayoutMode.Full;
        }
        
        hmpDefaultLayoutMode.put(pMode, pLayoutMode != null ? pLayoutMode : hmpFactoryDefaultLayoutMode.get(pMode));
    }
    
    /**
     * Gets the default layout mode.
     * @return the default layout mode
     */
    public static LayoutMode getDefaultLayoutMode()
    {
        return getDefaultLayoutMode(ResponsiveUtil.LayoutMode.Full);
    }
    
    /**
     * Sets the default layout mode.
     * @param pLayoutMode the layout mode
     */
    public static void setDefaultLayoutMode(LayoutMode pLayoutMode)
    {
        setDefaultLayoutMode(ResponsiveUtil.LayoutMode.Full, pLayoutMode);
    }
    
    /**
     * Gets the default layout mode for small.
     * @return the default layout mode
     */
    public static LayoutMode getDefaultLayoutModeSmall()
    {
        return getDefaultLayoutMode(ResponsiveUtil.LayoutMode.Small);
    }
    
    /**
     * Sets the default layout mode for small.
     * @param pLayoutMode the layout mode
     */
    public static void setDefaultLayoutModeSmall(LayoutMode pLayoutMode)
    {
        setDefaultLayoutMode(ResponsiveUtil.LayoutMode.Small, pLayoutMode);
    }
    
    /**
     * Gets the default layout mode for mini.
     * @return the default layout mode
     */
    public static LayoutMode getDefaultLayoutModeMini()
    {
        return getDefaultLayoutMode(ResponsiveUtil.LayoutMode.Mini);
    }
    
    /**
     * Sets the default layout mode for mini.
     * @param pLayoutMode the layout mode
     */
    public static void setDefaultLayoutModeMini(LayoutMode pLayoutMode)
    {
        setDefaultLayoutMode(ResponsiveUtil.LayoutMode.Mini, pLayoutMode);
    }

    /**
     * Gets the layout mode for the given {@link javax.rad.application.genui.responsive.ResponsiveUtil.LayoutMode}.
     * 
     * @param pMode the layout mode
     * @return the default layout mode
     */
    protected LayoutMode getLayoutMode(ResponsiveUtil.LayoutMode pMode)
    {
        if (pMode == null)
        {
            pMode = ResponsiveUtil.LayoutMode.Full;
        }
        
        LayoutMode labPos = hmpLayoutMode.get(pMode);
        if (labPos == null)
        {
            labPos = hmpLayoutMode.get(ResponsiveUtil.LayoutMode.Full);
        }
        return labPos;
    }
    
    /**
     * Sets the layout mode for the given {@link javax.rad.application.genui.responsive.ResponsiveUtil.LayoutMode}.
     * 
     * @param pMode the layout mode
     * @param pLayoutMode the layout mode
     */
    protected void setLayoutMode(ResponsiveUtil.LayoutMode pMode, LayoutMode pLayoutMode)
    {
        if (pMode == null)
        {
            pMode = ResponsiveUtil.LayoutMode.Full;
        }
        
        hmpLayoutMode.put(pMode, pLayoutMode != null ? pLayoutMode : hmpDefaultLayoutMode.get(pMode));
        
        if (pMode == currentResponsiveMode || currentResponsiveMode == null)
        {
            setCurrentLayoutMode(getLayoutMode());
        }
    }
    
    /**
     * Gets the layout mode.
     * 
     * @return the layout mode.
     */
    public LayoutMode getLayoutMode()
    {
        return getLayoutMode(ResponsiveUtil.LayoutMode.Full);
    }
    
    /**
     * Sets the layout mode.
     * 
     * @param pLayoutMode the layout mode.
     */
    public void setLayoutMode(LayoutMode pLayoutMode)
    {
        setLayoutMode(ResponsiveUtil.LayoutMode.Full, pLayoutMode);
    }
    
    /**
     * True, if layout mode is set.
     * 
     * @return True, if layout mode is set
     */
    public boolean isLayoutModeSmallSet()
    {
        return hmpLayoutMode.get(ResponsiveUtil.LayoutMode.Small) != null;
    }
    
    /**
     * Gets the layout mode for small.
     * @return the layout mode for small.
     */
    public LayoutMode getLayoutModeSmall()
    {
        return getLayoutMode(ResponsiveUtil.LayoutMode.Small);
    }
    
    /**
     * Sets the layout mode for small.
     * 
     * @param pLayoutMode the layout mode for small.
     */
    public void setLayoutModeSmall(LayoutMode pLayoutMode)
    {
        setLayoutMode(ResponsiveUtil.LayoutMode.Small, pLayoutMode);
    }
    
    /**
     * True, if layout mode is set.
     * 
     * @return True, if layout mode is set
     */
    public boolean isLayoutModeMiniSet()
    {
        return hmpLayoutMode.get(ResponsiveUtil.LayoutMode.Mini) != null 
                && hmpLayoutMode.get(ResponsiveUtil.LayoutMode.Mini) != hmpDefaultLayoutMode.get(ResponsiveUtil.LayoutMode.Mini);
    }
    
    /**
     * Gets the layout mode for mini.
     * @return the layout mode for mini.
     */
    public LayoutMode getLayoutModeMini()
    {
        return getLayoutMode(ResponsiveUtil.LayoutMode.Mini);
    }
    
    /**
     * Sets the layout mode for mini.
     * 
     * @param pLayoutMode the layout mode for mini.
     */
    public void setLayoutModeMini(LayoutMode pLayoutMode)
    {
        setLayoutMode(ResponsiveUtil.LayoutMode.Mini, pLayoutMode);
    }
    
    /**
     * Gets the current layout mode.
     * @return the current layout mode.
     */
    public LayoutMode getCurrentLayoutMode()
    {
        return currentLayoutMode;
    }
    
    /**
     * Sets the current layout mode.
     * 
     * @param pLayoutMode the current layout mode.
     */
    protected void setCurrentLayoutMode(LayoutMode pLayoutMode)
    {
        if (pLayoutMode == null)
        {
            pLayoutMode = LayoutMode.Default;
        }
        
        if (pLayoutMode != currentLayoutMode)
        {
            currentLayoutMode = pLayoutMode;

            if (currentLayoutMode == LayoutMode.SingleColumn)
            {
                configureAnchorsForSingleColumnMode();
            }
            else
            {
                restoreAnchorsFromSingleColumnMode();
            }

            configureConstraints();
        }
    }

    /**
     * Restores the vertical anchors after switching back from single mode.
     */
    private void restoreAnchorsFromSingleColumnMode()
    {
        for (Map.Entry<IAnchor, Integer> entry : storedAnchorPositions.entrySet())
        {
            IAnchor anchor = entry.getKey();

            anchor.setPosition(entry.getValue().intValue());
            anchor.setAutoSize(storedAnchorAutoSizes.get(anchor).booleanValue());
        }
        for (Map.Entry<IComponent, IDimension> entry : storedMaximumSizes.entrySet())
        {
            IComponent component = entry.getKey();

            component.setMaximumSize(entry.getValue());
        }
    }

    /**
     * Configures the vertical anchors after switching to single mode.
     */
    private void configureAnchorsForSingleColumnMode()
    {
        storedAnchorPositions.clear();
        storedAnchorAutoSizes.clear();
        
        IAnchor[] anchors = getVerticalAnchors();
        int verticalGap = getVerticalGap();
        
        int i = 0;
        IAnchor anchor = getRowBottomAnchor(i);
        IAnchor nextAnchor = getRowBottomAnchor(i + 1);
        while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
                || ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
                || ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
        {
            if (i > 0)
            {
                IAnchor topAnchor = getRowTopAnchor(i);
                storedAnchorPositions.put(topAnchor, Integer.valueOf(topAnchor.getPosition()));
                storedAnchorAutoSizes.put(topAnchor, Boolean.valueOf(topAnchor.isAutoSize()));
                
                topAnchor.setPosition(verticalGap);
            }
            storedAnchorPositions.put(anchor, Integer.valueOf(anchor.getPosition()));
            storedAnchorAutoSizes.put(anchor, Boolean.valueOf(anchor.isAutoSize()));
            
            anchor.setPosition(0);
            anchor.setAutoSize(true);
            
            i++;
            anchor = nextAnchor;
            nextAnchor = getRowBottomAnchor(i + 1);
        }
        if (i > 0)
        {
            IAnchor topAnchor = getRowTopAnchor(i);
            storedAnchorPositions.put(topAnchor, Integer.valueOf(topAnchor.getPosition()));
            storedAnchorAutoSizes.put(topAnchor, Boolean.valueOf(topAnchor.isAutoSize()));
            
            topAnchor.setPosition(verticalGap);
        }
        i = -1;
        anchor = getRowTopAnchor(i);
        nextAnchor = getRowTopAnchor(i - 1);
        while (ArrayUtil.indexOfReference(anchors, anchor) >= 0
                || ArrayUtil.indexOfReference(anchors, nextAnchor) >= 0
                || ArrayUtil.indexOfReference(anchors, nextAnchor.getRelatedAnchor()) >= 0)
        {
            if (i < -1)
            {
                IAnchor bottomAnchor = getRowBottomAnchor(i);
                storedAnchorPositions.put(bottomAnchor, Integer.valueOf(bottomAnchor.getPosition()));
                storedAnchorAutoSizes.put(bottomAnchor, Boolean.valueOf(bottomAnchor.isAutoSize()));

                bottomAnchor.setPosition(-verticalGap);
            }
            storedAnchorPositions.put(anchor, Integer.valueOf(anchor.getPosition()));
            storedAnchorAutoSizes.put(anchor, Boolean.valueOf(anchor.isAutoSize()));
            
            anchor.setPosition(0);
            anchor.setAutoSize(true);
            
            i--;
            anchor = nextAnchor;
            nextAnchor = getRowTopAnchor(i - 1);
        }
        if (i < -1)
        {
            IAnchor bottomAnchor = getRowBottomAnchor(i);
            storedAnchorPositions.put(bottomAnchor, Integer.valueOf(bottomAnchor.getPosition()));
            storedAnchorAutoSizes.put(bottomAnchor, Boolean.valueOf(bottomAnchor.isAutoSize()));

            bottomAnchor.setPosition(-verticalGap);
        }
        
        storedMaximumSizes.clear();
        
        for (IComponent comp : componentConstraints.keySet())
        {
            if (componentsToIgnorePreferredWidth.contains(comp.getClass()))
            {
                IDimension maxSize = comp.getMaximumSize();
                
                int maxHeight;
                if (comp.isMaximumSizeSet())
                {
                    storedMaximumSizes.put(comp, maxSize);
                    maxHeight = maxSize.getHeight();
                }
                else
                {
                    storedMaximumSizes.put(comp, null);
                    maxHeight = Integer.MAX_VALUE;
                }
                
                comp.setMaximumSize(new UIDimension(0, maxHeight));
            }
        }
    }

    /**
     * True, if the component should be added with VCenter constraint, as it is possibly higher as normal editors are.
     * 
     * @param pComponent the component
     * @return True, if the component should be added with VCenter constraint.
     */
    protected boolean isVCenterComponent(IComponent pComponent)
    {
        return pComponent instanceof IButton && !(pComponent instanceof ICheckBox || pComponent instanceof IRadioButton);
    }

    /**
     * True, if the component is a possible attachable component.
     * This means, that the component is small and belongs to the right side of the strechted component in this row.
     * 
     * @param pComponent the component
     * @return True, if the component is a possible attachable component.
     */
    protected boolean isPossibleAttachedComponent(IComponent pComponent)
    {
        if (componentsToAttachInSameRow.contains(pComponent.getClass()))
        {
            return true;
        }
        else if (pComponent instanceof IButton)
        {
            IButton button = (IButton)pComponent;
            String text = button.getText();
            if (text == null || text.length() < 8)
            {
                return true;
            }
        }
        else if (pComponent instanceof UIEditor)
        {
            UIEditor editor = (UIEditor)pComponent;
            
            try
            {
                return isPossibleAttachedComponent(editor, UICellEditor.getCellEditor(editor));
            }
            catch (Exception ex)
            {
                // Ignore
            }
        }
        
        return false;
    }
    
    /**
     * True, if the component is a possible attachable component.
     * This means, that the component is small and belongs to the right side of the strechted component in this row.
     * 
     * @param pEditor the component
     * @param pCellEditor the cell editor
     * @return True, if the component is a possible attachable component.
     * @throws Exception if it fails.
     */
    protected boolean isPossibleAttachedComponent(UIEditor pEditor, ICellEditor pCellEditor) throws Exception
    {
        if (pCellEditor instanceof IChoiceCellEditor<?>)
        {
            return true;
        }
        else if (pCellEditor instanceof ICheckBoxCellEditor<?>)
        {
            ICheckBoxCellEditor checkBoxCellEditor = (ICheckBoxCellEditor)pCellEditor;
            
            String label = checkBoxCellEditor.getText();
            if (label == null)
            {
                label = pEditor.getDataRow().getRowDefinition().getColumnDefinition(pEditor.getColumnName()).getLabel();
            }
            
            return StringUtil.isEmpty(label);
        }
        
        return false;
    }
    
    /**
     * configures all component and label constraints.
     */
    protected void configureConstraints()
    {
        currentComponentConstraints.clear();
        
        if (isNotified && componentConstraints.size() > 0)
        {
            IContainer parent = ((UIComponent<?>)componentConstraints.keySet().iterator().next()).getParent();
            
            if (currentLayoutMode == LayoutMode.Default || designMode)
            {
                for (int i = 0, count = parent.getComponentCount(); i < count; i++)
                {
                    IComponent comp = parent.getComponent(i);
                    
                    if (!(comp instanceof IInternalFrame))
                    {
                        setConstraintsIntern(comp, componentConstraints.get(comp));
                    }
                }
            }
            else
            {
                int topComponentCount = 0;
                IAnchor topAnchor = getTopAnchor();
                ArrayList<IComponent> components = new ArrayList<IComponent>();
                HashMap<IComponent, IComponent> attachedComponents = new HashMap<IComponent, IComponent>();
                
                boolean existsStretchComponent = false;
                IComponent lastEditor = null;
                IConstraints lastConstraints = null;
                
                for (int i = 0, count = parent.getComponentCount(); i < count; i++)
                {
                    IComponent comp = parent.getComponent(i);
                    
                    if (!(comp instanceof IInternalFrame))
                    {
                        IConstraints defaultConstraints = (IConstraints)componentConstraints.get(comp);
                        IAnchor top = defaultConstraints.getTopAnchor();
                        IAnchor bottom = defaultConstraints.getBottomAnchor();
                        IAnchor left = defaultConstraints.getLeftAnchor();
                        boolean isTopTop = top.getBorderAnchor() == topAnchor;
                        boolean isBottomTop = bottom.getBorderAnchor() == topAnchor;

                        if (lastConstraints != null
                                && (top == lastConstraints.getTopAnchor() || top.getRelatedAnchor() == lastConstraints.getTopAnchor()) 
                                && (bottom == lastConstraints.getBottomAnchor() || bottom.getRelatedAnchor() == lastConstraints.getBottomAnchor()) 
                                && (left.getRelatedAnchor() == lastConstraints.getRightAnchor() || lastConstraints.getRightAnchor().getRelatedAnchor() == left) 
                                && isPossibleAttachedComponent(comp))
                        {
                            attachedComponents.put(lastEditor, comp);
                            lastConstraints = null;
                        }
                        else
                        {
                            components.add(comp);
    
                            if (isTopTop && isBottomTop)
                            {
                                topComponentCount = components.size();
                                existsStretchComponent = false;
                            }
                            else if (topComponentCount == components.size() - 1 && isTopTop && !isBottomTop)
                            {
                                existsStretchComponent = true;
                            }

                            if (comp instanceof IEditor || comp instanceof UILabel)
                            {
                                lastEditor = comp;
                                lastConstraints = defaultConstraints;
                            }
                            else
                            {
                                lastConstraints = null;
                            }
                        }
                    }
                }
                
                IAnchor topGap = null;
                for (int i = 0; i < topComponentCount; i++)
                {
                    IComponent comp = components.get(i);
                    IConstraints defaultConstraints = (IConstraints)componentConstraints.get(comp);
                    IAnchor top = defaultConstraints.getTopAnchor();
                    IAnchor bottom = defaultConstraints.getBottomAnchor();
                    
                    IComponent attachedComponent = attachedComponents.get(comp);
                    IConstraints constraints;
                    if (attachedComponent == null)
                    {
                        constraints = getConstraints(0,  i, -1, i);
                    }
                    else
                    {
                        constraints = getConstraints(0,  i, -2, i);
                        
                        IConstraints attachedConstraints;
                        if (isVCenterComponent(attachedComponent))
                        {
                            attachedConstraints = getVCenterConstraints(-1,  i, -1, i);
                        }
                        else
                        {
                            attachedConstraints = getConstraints(-1,  i, -1, i);
                        }
                        currentComponentConstraints.put(attachedComponent, attachedConstraints);
                        setConstraintsIntern(attachedComponent, attachedConstraints);
                    }
                    IAnchor newTop = constraints.getTopAnchor();
                    IAnchor newBottom = constraints.getBottomAnchor();
                    
                    if (bottom.getRelatedAnchor() == top && CommonUtil.equals(storedAnchorAutoSizes.get(bottom), Boolean.FALSE))
                    {
                        newBottom.setPosition(storedAnchorPositions.get(bottom).intValue());
                        newBottom.setAutoSize(false);
                    }
                    if (top != topGap && !top.isAutoSize() && storedAnchorPositions.get(top) != null)
                    {
                        topGap = top;
                        
                        newTop.setPosition(storedAnchorPositions.get(top).intValue());
                    }
                    
                    currentComponentConstraints.put(comp, constraints);
                    setConstraintsIntern(comp, constraints);
                }
                int size = components.size();
                
                if (existsStretchComponent)
                {
                    IComponent stretchComponent = components.get(topComponentCount);
                    
                    IConstraints defaultConstraints = (IConstraints)componentConstraints.get(stretchComponent);
                    IAnchor top = defaultConstraints.getTopAnchor();
                    IAnchor bottom = defaultConstraints.getBottomAnchor();
                    
                    IComponent attachedComponent = attachedComponents.get(stretchComponent);
                    IConstraints constraints;
                    if (attachedComponent == null)
                    {
                        constraints = getConstraints(0,  topComponentCount, -1, topComponentCount - size);
                    }
                    else
                    {
                        constraints = getConstraints(0,  topComponentCount, -2, topComponentCount - size);

                        IConstraints attachedConstraints;
                        if (isVCenterComponent(attachedComponent))
                        {
                            attachedConstraints = getVCenterConstraints(-1,  topComponentCount, -1, topComponentCount - size);
                        }
                        else
                        {
                            attachedConstraints = getConstraints(-1,  topComponentCount, -1, topComponentCount - size);
                        }
                        currentComponentConstraints.put(attachedComponent, attachedConstraints);
                        setConstraintsIntern(attachedComponent, attachedConstraints);
                    }
                    IAnchor newTop = constraints.getTopAnchor();
                    IAnchor newBottom = constraints.getBottomAnchor();
                    
                    if (!top.isAutoSize() && storedAnchorPositions.get(top) != null)
                    {
                        newTop.setPosition(storedAnchorPositions.get(top).intValue());
                    }
                    if (!bottom.isAutoSize() && storedAnchorPositions.get(bottom) != null)
                    {
                        newBottom.setPosition(storedAnchorPositions.get(bottom).intValue());
                    }
                    
                    currentComponentConstraints.put(stretchComponent, constraints);
                    setConstraintsIntern(stretchComponent, constraints);
                    
                    topComponentCount++;
                }
                IAnchor bottomGap = null;
                for (int i = size - 1; i >= topComponentCount; i--)
                {
                    IComponent comp = components.get(i);
                    IConstraints defaultConstraints = (IConstraints)componentConstraints.get(comp);
                    IAnchor top = defaultConstraints.getTopAnchor();
                    IAnchor bottom = defaultConstraints.getBottomAnchor();
                    
                    IComponent attachedComponent = attachedComponents.get(comp);
                    IConstraints constraints;
                    if (attachedComponent == null)
                    {
                        constraints = getConstraints(0,  i - size, -1, i - size);
                    }
                    else
                    {
                        constraints = getConstraints(0,  i - size, -2, i - size);
                        
                        IConstraints attachedConstraints;
                        if (isVCenterComponent(attachedComponent))
                        {
                            attachedConstraints = getVCenterConstraints(-1,  i - size, -1, i - size);
                        }
                        else
                        {
                            attachedConstraints = getConstraints(-1,  i - size, -1, i - size);
                        }
                        currentComponentConstraints.put(attachedComponent, attachedConstraints);
                        setConstraintsIntern(attachedComponent, attachedConstraints);
                    }
                    IAnchor newTop = constraints.getTopAnchor();
                    IAnchor newBottom = constraints.getBottomAnchor();
                    
                    if (top.getRelatedAnchor() == bottom && CommonUtil.equals(storedAnchorAutoSizes.get(top), Boolean.FALSE))
                    {
                        newTop.setPosition(storedAnchorPositions.get(top).intValue());
                        newTop.setAutoSize(false);
                    }
                    if (bottom != bottomGap && !bottom.isAutoSize() && storedAnchorPositions.get(bottom) != null)
                    {
                        bottomGap = bottom;
                        
                        newBottom.setPosition(storedAnchorPositions.get(bottom).intValue());
                    }
                    
                    currentComponentConstraints.put(comp, constraints);
                    setConstraintsIntern(comp, constraints);
                }
            }
        }
    }
    
	//****************************************************************
	// Subinterface definition
	//****************************************************************
/*
    *//**
	 * The <code>UIAnchor</code> gives the possible horizontal and vertical positions.
	 * 
	 * @author Martin Handsteiner
	 *//*
	public static class UIAnchor extends UIResource<IAnchor> 
	                      		 implements IAnchor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		*//**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 *//*
		public UIAnchor(IAnchor pRelatedAnchor)
		{
			super(pRelatedAnchor.getLayout().createAnchor(pRelatedAnchor));
		}

		*//**
		 * Constructs an anchor relative to pRelatedAnchor with pPosition pixels.
		 * 
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pPosition the position relative to the related anchor.
		 *//*
		public UIAnchor(IAnchor pRelatedAnchor, int pPosition)
		{
			super(pRelatedAnchor.getLayout().createAnchor(pRelatedAnchor, pPosition));
		}
		
		*//**
		 * Constructs an centered anchor between the related and second related anchor.
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pSecondRelatedAnchor the second related anchor for this anchor.
		 * @param pRelativePosition the relative position between first and second anchor.
		 *//*
		public UIAnchor(IAnchor pRelatedAnchor, IAnchor pSecondRelatedAnchor, float pRelativePosition)
		{
			super(pRelatedAnchor.getLayout().createAnchor(pRelatedAnchor, pSecondRelatedAnchor, pRelativePosition));
		}

		*//**
		 * Constructs an centered anchor between the related and second related anchor.
		 * @param pRelatedAnchor the related anchor for this anchor.
		 * @param pSecondRelatedAnchor the second related anchor for this anchor.
		 *//*
		public UIAnchor(IAnchor pRelatedAnchor, IAnchor pSecondRelatedAnchor)
		{
			super(pRelatedAnchor.getLayout().createAnchor(pRelatedAnchor, pSecondRelatedAnchor));
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		*//**
		 * {@inheritDoc}
		 *//*
		public IFormLayout getLayout()
	    {
	    	return uiResource.getLayout();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public int getOrientation()
	    {
	    	return uiResource.getOrientation();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getRelatedAnchor()
	    {
	    	return uiResource.getRelatedAnchor();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public boolean isAutoSize()
	    {
	    	return uiResource.isAutoSize();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public void setAutoSize(boolean pAutoSize)
	    {
	    	uiResource.setAutoSize(pAutoSize);
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public int getPosition()
	    {
	    	return uiResource.getPosition();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public void setPosition(int pPosition)
	    {
	    	uiResource.setPosition(pPosition);
	    }
		
		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getSecondRelatedAnchor()
		{
	    	return uiResource.getSecondRelatedAnchor();
		}

		*//**
		 * {@inheritDoc}
		 *//*
		public float getRelativePosition()
		{
	    	return uiResource.getRelativePosition();
		}

		*//**
		 * {@inheritDoc}
		 *//*
		public void setRelativePosition(float pRelativePosition)
		{
	    	uiResource.setRelativePosition(pRelativePosition);
		}
		
	}	// UIAnchor
	
	*//**
	 * The <code>UIConstraint</code> stores the top, left, bottom and right Anchor for layouting a component.
	 * 
	 * @author Martin Handsteiner
	 *//*
	public static class UIConstraint extends UIResource<IConstraints> 
	                                 implements IConstraints
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		*//**
		 * Creates the default constraints for the given anchors.
		 * 
		 * @param pTopAnchor the top anchor.
		 * @param pLeftAnchor the left anchor.
		 * @param pBottomAnchor the bottom anchor.
		 * @param pRightAnchor the right anchor.
		 *//*
	    public UIConstraint(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
	    {
	    	super(createConstraints(pTopAnchor, pLeftAnchor, pBottomAnchor, pRightAnchor));
	    }
	    
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getLeftAnchor()
	    {
	    	return uiResource.getLeftAnchor();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getRightAnchor()
	    {
	    	return uiResource.getRightAnchor();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getTopAnchor()
	    {
	    	return uiResource.getTopAnchor();
	    }

		*//**
		 * {@inheritDoc}
		 *//*
		public IAnchor getBottomAnchor()
	    {
	    	return uiResource.getBottomAnchor();
	    }

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    
	    *//**
	     * creates a new Constraint with the IFormLayout that is related to the Anchors.
	     * @param pTopAnchor the Top Anchor.
	     * @param pLeftAnchor the Left Anchor.
	     * @param pBottomAnchor the Bottom Anchor.
	     * @param pRightAnchor the Right Anchor.
	     * @return the IConstraint.
	     *//*
	    private static IConstraints createConstraints(IAnchor pTopAnchor, IAnchor pLeftAnchor, IAnchor pBottomAnchor, IAnchor pRightAnchor)
	    {
	    	IFormLayout layout = null;
	    	if (pTopAnchor != null)
	    	{
	    		layout = pTopAnchor.getLayout();
	    	}
	    	else if (pLeftAnchor != null)
	    	{
	    		layout = pLeftAnchor.getLayout();
	    	}
	    	else if (pBottomAnchor != null)
	    	{
	    		layout = pBottomAnchor.getLayout();
	    	}
	    	else
	    	{
	    		layout = pRightAnchor.getLayout();
	    	}
	    	return layout.getConstraints(pTopAnchor, pLeftAnchor, pBottomAnchor, pRightAnchor);
	    }
		
	}	// UIConstraint
*/    
}	// UIFormLayout
