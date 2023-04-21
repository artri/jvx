/*
 * Copyright 2017 SIB Visions GmbH
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
 * 22.10.2018 - [HM] - creation
 */
package javax.rad.application.genui.responsive;

import java.lang.ref.WeakReference;

import javax.rad.application.ApplicationUtil;
import javax.rad.application.IApplication;
import javax.rad.application.ILauncher;
import javax.rad.application.IWorkScreen;
import javax.rad.application.genui.UILauncher;
import javax.rad.application.genui.WorkScreen;
import javax.rad.application.genui.event.type.screen.IParameterChangedListener;
import javax.rad.genui.UIResource;
import javax.rad.genui.event.IUIResourceListener;
import javax.rad.genui.event.ResourceEvent;
import javax.rad.type.bean.BeanType;
import javax.rad.ui.IComponent;
import javax.rad.util.RuntimeEventHandler;

/**
 * The <code>ResponsiveUtil</code> is a helper for setting global responsive states.
 * 
 * @author Martin Handsteiner
 */
public class ResponsiveUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Display modes. */
	public enum LayoutMode
	{
		/** minimal layout mode (e.g. smartphone). */
		Mini,
		/** small layout mode (e.g. tablet). */
		Small,
		/** full layout mode (e.g. desktop). */
		Full;
		
		/**
		 * Resolves the layout modeby a string. The detection is case insensitive.
		 * 
		 * @param pMode the mode name
		 * @return the layout mode or <code>null</code> if mode is null
		 */
		public static LayoutMode resolve(Object pMode)
		{
			if (pMode instanceof LayoutMode)
			{
				return (LayoutMode)pMode;
			}
			
			if (pMode != null)
			{
				String sName = pMode.toString().toUpperCase();
				
				if ("MINI".equals(sName))
				{
					return Mini;
				}
				else if ("SMALL".equals(sName))
				{
					return Small;
				}
				else if ("FULL".equals(sName))
				{
					return Full;
				}
				
				throw new IllegalArgumentException("Invalid LayoutMode '" + pMode + "'");
			}
			
			return null;
		}
	} 
	
	/** the default max width for mini mode. */
	public static final int DEFAULT_MAXWIDTH_MINI = 520;
	/** the default max width for small mode. */
	public static final int DEFAULT_MAXWIDTH_SMALL = 950;

	/** the parameter name for responsive flag. */
	public static final String PARAM_RESPONSIVE		= "Application.responsive";
	
	/** the parameter name for the mini width. */
	public static final String PARAM_MINI_WIDTH     = "Application.responsive.mini.width";
	/** the parameter name for the small width. */
	public static final String PARAM_SMALL_WIDTH    = "Application.responsive.small.width";

	/** the parameter name for the application layout mode. Originally from IWebConfiguration. */
    public static final String PARAM_LAYOUTMODE		= "Application.config.layoutMode";
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Hidden constructor because <code>ResponsiveUtil</code> is a utility class. 
     */
    protected ResponsiveUtil()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the responsive flag for the screen.
	 * 
	 * @param pComponent the component.
	 * @return the responsive flag.
	 */
	public static boolean isResponsive(IComponent pComponent)
	{
		try
		{
			IWorkScreen screen = ApplicationUtil.getWorkScreen(pComponent);
			
			//screen setting can't be overruled
			if (screen != null && screen.isParameterSet(PARAM_RESPONSIVE))
			{
				return Boolean.parseBoolean((String)screen.getParameter(PARAM_RESPONSIVE));
			}
			
			ILauncher launcher = ApplicationUtil.getLauncher(pComponent);

			return launcher != null && Boolean.parseBoolean(launcher.getParameter(PARAM_RESPONSIVE));
		}
		catch (Exception ex)
		{
			return false;
		}
	}
	
	/**
	 * Gets the maximum width for the gien mode. The max width can be configured as launcher parameter
	 * or otherwise the default max width will be used.
	 * 
	 * @param pLauncher the launcher
	 * @param pMode the layout mode
	 * @return the maximium width for the given layout mode
	 */
	public static int getMaxWidth(ILauncher pLauncher, LayoutMode pMode)
	{
		switch (pMode)
		{
			case Mini:
				try
				{
					return Integer.parseInt((String)pLauncher.getParameter(PARAM_MINI_WIDTH));
				}
				catch (Exception e)
				{
					return DEFAULT_MAXWIDTH_MINI;
				}
			case Small:
				try
				{
					return Integer.parseInt((String)pLauncher.getParameter(PARAM_SMALL_WIDTH));
				}
				catch (Exception e)
				{
					return DEFAULT_MAXWIDTH_SMALL;
				}
			default:
				return Integer.MAX_VALUE;
		}
	}

    /**
     * Gets the responsive parameter for the given resource and property.
     * 
     * @param pComponent the component
     * @param pResource the resource
     * @param pProperty the property
     * @param pMode the mode
     * @param pDefaultSmall the default value for small modus
     * @param pDefaultMini the default value for mini modus
     * @return the value
     */
    public static String getResponsiveParameter(IComponent pComponent, IResponsiveResource pResource, 
                                                String pMode, String pProperty,
                                                Object pDefaultSmall, Object pDefaultMini)
    {
        return getResponsiveParameter(pComponent, pResource, LayoutMode.resolve(pMode), pProperty, pDefaultSmall, pDefaultMini);
    }
    
	/**
	 * Gets the responsive parameter for the given resource and property.
	 * 
	 * @param pComponent the component
	 * @param pResource the resource
	 * @param pProperty the property
	 * @param pMode the mode
	 * @param pDefaultSmall the default value for small modus
	 * @param pDefaultMini the default value for mini modus
	 * @return the value
	 */
	public static String getResponsiveParameter(IComponent pComponent, IResponsiveResource pResource, 
												LayoutMode pMode, String pProperty,
			                                    Object pDefaultSmall, Object pDefaultMini)
	{
		try
		{
			boolean bSmall = LayoutMode.Small.equals(pMode);
			boolean bMini  = LayoutMode.Mini.equals(pMode);
			
			if (bSmall || bMini)
			{
				String propertyName = pProperty + "." + pMode;
				
				if (pResource instanceof UIResource)
				{
					Object value = ((UIResource)pResource).getObject(propertyName);
					
					if (value != null)
					{
						return value.toString();
					}
				}

				IWorkScreen screen = ApplicationUtil.getWorkScreen(pComponent);
				ILauncher launcher = ApplicationUtil.getLauncher(pComponent);

				String result = null;
				
				//use screen-layer if screen has responsive setting
				if (screen != null && screen.isParameterSet(PARAM_RESPONSIVE))
				{
					result = (String)screen.getParameter(PARAM_RESPONSIVE + "." + pResource.getClass().getSimpleName() + "." + propertyName);
				}
				else if (launcher != null)
				{
					result = launcher.getParameter(PARAM_RESPONSIVE + "." + pResource.getClass().getSimpleName() + "." + propertyName);
				}
				
				if (result == null)
				{
					if (bSmall && pDefaultSmall != null)
					{
						result = pDefaultSmall.toString();
					}
					else if (bMini && pDefaultMini != null)
					{
						result = pDefaultMini.toString();
					}
				}
				
				if (result != null)
				{
					return result;
				}
			}

			Object value = BeanType.getBeanType(pResource).get(pResource, pProperty);
				
			if (value != null)
			{
				return value.toString();
			}
		}
		catch (Exception ex)
		{
			// Do nothing
		}
		
		return null;
	}
	
	/**
	 * Sets the responsive flag for an application.
	 * 
	 * @param pApplication the application.
	 * @param pResponsive the responsive flag.
	 */
	public static void setResponsive(IApplication pApplication, boolean pResponsive)
	{
		pApplication.getLauncher().setParameter(PARAM_RESPONSIVE, Boolean.valueOf(pResponsive).toString());
	}

	/**
	 * Sets the responsive flag for a work-screen.
	 * 
	 * @param pScreen the screen.
	 * @param pResponsive the responsive flag.
	 */
	public static void setResponsive(IWorkScreen pScreen, boolean pResponsive)
	{
		pScreen.setParameter(PARAM_RESPONSIVE, Boolean.valueOf(pResponsive).toString());
	}

	/**
	 * Adds the component to retrieve responsive mode changed events.
	 * 
	 * @param pComponent the component that listens.
	 */
	public static void addComponent(IResponsiveComponent pComponent)
	{
		addResource(pComponent, pComponent);
	}

	/**
	 * Adds the component to retrieve responsive mode changed events.
	 * 
	 * @param pComponent the component.
	 * @param pResource the resource that listens.
	 */
	public static void addResource(IComponent pComponent, IResponsiveResource pResource)
	{
		IWorkScreen screen = ApplicationUtil.getWorkScreen(pComponent);
		
		ResponsiveModeForwarder listener = new ResponsiveModeForwarder(pResource);
		
		if (screen instanceof WorkScreen)
		{
			((WorkScreen)screen).eventParameterChanged(PARAM_LAYOUTMODE).addListener(listener);
		}

		UILauncher launcher = (UILauncher)ApplicationUtil.getLauncher(pComponent);
		
		if (launcher != null)
		{
			launcher.eventResourceChanged(PARAM_LAYOUTMODE).addListener(listener);
		}
	}

	/**
	 * Removes the component to retrieve responsive mode changed events.
	 * 
	 * @param pComponent the component that listens.
	 */
	public static void removeComponent(IResponsiveComponent pComponent)
	{
		removeResource(pComponent, pComponent);
	}

	/**
	 * Removes the component to retrieve responsive mode changed events.
	 * 
	 * @param pComponent the component.
	 * @param pResource the resource that listens.
	 */
	public static void removeResource(IComponent pComponent, IResponsiveResource pResource)
	{
		IWorkScreen screen = ApplicationUtil.getWorkScreen(pComponent);
		
		if (screen instanceof WorkScreen)
		{
			ResponsiveModeForwarder.removeListener(((WorkScreen)screen).eventParameterChanged(PARAM_LAYOUTMODE), pResource);
		}
		
		UILauncher launcher = (UILauncher)ApplicationUtil.getLauncher(pComponent);
		
		if (launcher != null)
		{
			ResponsiveModeForwarder.removeListener(launcher.eventResourceChanged(PARAM_LAYOUTMODE), pResource);
		}
	}

	/**
	 * Gets the current responsive mode. 
	 * 
	 * @param pComponent the component.
	 * @return the current responsive mode or <code>null</code> if component is not in the hierarchy of an {@link ILauncher}.
	 */
	public static LayoutMode getCurrentMode(IComponent pComponent)
	{
		IWorkScreen screen = ApplicationUtil.getWorkScreen(pComponent);
		
		if (screen != null && screen.isParameterSet(PARAM_RESPONSIVE))
		{
			return LayoutMode.resolve(screen.getParameter(PARAM_LAYOUTMODE));
		}

		ILauncher launcher = ApplicationUtil.getLauncher(pComponent);
		
		if (launcher != null)
		{
			return LayoutMode.resolve(launcher.getParameter(PARAM_LAYOUTMODE));
		}
		
		return null;
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************	
	
	/**
	 * The <code>ResponsiveModeForwarder</code> is a forwards layout mode changes to the {@link IResponsiveResource}.
	 * 
	 * @author René Jahn
	 */
	public static final class ResponsiveModeForwarder implements IUIResourceListener,
	                                                             IParameterChangedListener
    {
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	    /** the listener. */
		private WeakReference<IResponsiveResource> weakResource;
		/** The hashCode of the resource. */
		private int hashCode; 
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>ResponsiveModeForwarder</code>.
		 * 
		 * @param pResource the listener
		 */
		private ResponsiveModeForwarder(IResponsiveResource pResource)
		{
            hashCode = pResource.hashCode();
			weakResource = new WeakReference(pResource);
		}
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Interface implementation
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * {@inheritDoc} 
		 */
		public void parameterChanged(WorkScreen pScreen, String pParameter, Object pOldValue, Object pNewValue) throws Throwable 
		{
		    IResponsiveResource resource = weakResource.get();
		    if (resource != null)
		    {
		        resource.responsiveModeChanged(LayoutMode.resolve(pOldValue), LayoutMode.resolve(pNewValue));
		    }
		}

		/**
		 * {@inheritDoc} 
		 */
		public void resourceChanged(ResourceEvent pEvent) throws Throwable 
		{
            IResponsiveResource resource = weakResource.get();
            if (resource != null)
            {
                resource.responsiveModeChanged(LayoutMode.resolve(pEvent.getOldValue()), LayoutMode.resolve(pEvent.getNewValue()));
            }
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
        /**
         * {@inheritDoc} 
         */
		@Override
		public boolean equals(Object pObject)
		{
            IResponsiveResource resource = weakResource.get();
            if (pObject instanceof ResponsiveModeForwarder)
		    {
                IResponsiveResource objectResource = ((ResponsiveModeForwarder)pObject).weakResource.get();
                
                return resource == objectResource 
                        || (objectResource != null && objectResource.equals(resource))
                        || (resource != null && resource.equals(objectResource));
		    }
		    else
		    {
		        return resource == pObject;
		    }
		}
		
        /**
         * {@inheritDoc} 
         */
        @Override
        public int hashCode()
        {
            return hashCode;
        }
        
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Removes the listener resource from the event handler.
		 * 
		 * @param pEventHandler the event handler
		 * @param pResource the listener resource
		 */
		public static void removeListener(RuntimeEventHandler pEventHandler, IResponsiveResource pResource)
		{
			Object[] listeners = pEventHandler.getListeners();
			
	        for (int i = listeners.length - 1; i >= 0; i--)
	        {
	            Object listener = listeners[i];
	            
	            if (listener instanceof ResponsiveModeForwarder)
	            {
	                IResponsiveResource listenerResource = ((ResponsiveModeForwarder)listener).weakResource.get();
	                
    	            if (listenerResource == null || listenerResource == pResource)
    	            {
    	                pEventHandler.removeListener(i);
    	            }
	            }
	        }
		}
		
    }	// ResponsiveModeForwarder
	
}   // ResponsiveUtil
