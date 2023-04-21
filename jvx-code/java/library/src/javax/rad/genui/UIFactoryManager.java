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
 * 14.11.2008 - [HM] - creation
 * 10.09.2012 - [JR] - #596: checked classes instead of instances
 * 14.06.2013 - [JR] - #672: changed thread factory check
 * 10.12.2021 - [JR] - #2832: clone now throws a RuntimeException
 */
package javax.rad.genui;

import java.lang.reflect.Method;
import java.util.WeakHashMap;

import javax.rad.model.ui.ICellEditor;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.ui.IColor;
import javax.rad.ui.IFactory;
import javax.rad.ui.IFont;
import javax.rad.ui.IImage;

import com.sibvisions.util.type.ResourceUtil;

/**
 * Platform and technology independent UIFactoryManager.
 * This is a static provider for the current IFactory.
 * 
 * @author Martin Handsteiner
 */
public class UIFactoryManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The global factory instance. */
	private static IFactory factory = null;
	
	/** The global factory class. */
	private static Class factoryClass = null;
	
	/** The thread local factory instance. */
	private static ThreadLocal<IFactory> threadLocal = null;
	
	/** The "list" of already used factories. */
	private static WeakHashMap<Class, Boolean> whmpFactories = new WeakHashMap<Class, Boolean>();  
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creation of <code>UIFactoryManager</code> is not allowed.
	 */
	protected UIFactoryManager()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	  
	/**
	 * Gets the global {@link IFactory} singleton instance.
	 * 
	 * @return the global {@link IFactory} singleton instance.
	 * @see IFactory
	 */
	public static IFactory getFactory()
	{
		if (threadLocal != null)
		{
			IFactory tempFactory = threadLocal.get();
			
			if (tempFactory != null)
			{
				return tempFactory;
			}
		}
		return factory;
	}
	
	/**
	 * Sets the global {@link IFactory} singleton instance.
	 * 
	 * @param pFactory the factory
	 */
	public static void setFactoryInstance(IFactory pFactory)
	{
		if (factory == null)
		{
			factory = pFactory;
			factoryClass = pFactory.getClass();
			
			UIImage.setDefaults();
		}
		else if (pFactory == null || factory.getClass() != pFactory.getClass())
		{
			throw new IllegalStateException("Only one factory class is supported per virtual machine instance!");
		}
	}
	
	/**
	 * Creates the global {@link IFactory} singleton instance.
	 * 
	 * @param pFactoryClass the factory class.
	 * @return the singleton {@link IFactory} instance.
	 * @see IFactory
	 */
	public static IFactory getFactoryInstance(Class pFactoryClass)
	{
		try
		{
			if (factory == null)
			{
				if (threadLocal != null)
				{
					throw new IllegalStateException("A static factory instance can not be created in threaded factory mode!");
				}

				factory = (IFactory)pFactoryClass.newInstance();
				
				factoryClass = pFactoryClass;

				UIImage.setDefaults();
			}
			else if (factoryClass != pFactoryClass)
			{
				throw new IllegalStateException("Only one factory class is supported per virtual machine instance!");
			}
		}
		catch (Exception ex)
		{
			throw new IllegalArgumentException("It is impossible to instanciate an IFactory with the given class!", ex);
		}
		return factory;
	}
	
	/**
	 * Registers the {@link IFactory} instance for the current Thread.
	 * 
	 * @param pFactory the factory.
	 * @see IFactory
	 */
	public static synchronized void registerThreadFactoryInstance(IFactory pFactory)
	{
		try
		{
			if (factory != null)
			{
				throw new IllegalStateException("The threaded factory registration can not be used in static factory mode!");
			}
			else if (threadLocal == null)
			{
				threadLocal = new ThreadLocal<IFactory>();
			}
			else
			{
				// don't allow different factories for the same Thread!
				Object objCurrent = threadLocal.get();
				
				if (objCurrent != null)
				{
					if (objCurrent.getClass() != pFactory.getClass())
					{
						throw new IllegalStateException("Only one factory class is supported per thread! [" + 
					                                    objCurrent.getClass().getSimpleName() + " <> " + pFactory.getClass().getSimpleName() + "]");
					}				
				}
			}
			
			threadLocal.set(pFactory);

			//initialize per factory
			if (whmpFactories.get(pFactory.getClass()) == null)
			{
				whmpFactories.put(pFactory.getClass(), Boolean.TRUE);

				UIImage.setDefaults();
			}
		}
		catch (Exception pEx)
		{
			throw new IllegalArgumentException("It is impossible to register the given IFactory!", pEx);
		}
	}
	
	/**
	 * Unregisters the {@link IFactory} instance for the current Thread.
	 * If the factory is not unregistered, it is overwritten in the next registration of any Factory
	 * in this thread.
	 * The factories are stored in a weak hash map. But anyway it is the best way to finally unregister
	 * the Factory if it should not be used anymore, to get deterministic behaviour. 
	 * 
	 * @see IFactory
	 */
	public static synchronized void unregisterThreadFactoryInstance()
	{
		if (threadLocal != null)
		{
			threadLocal.set(null);
		}
	}
	
	/**
	 * Creates a clone of the given resource.
	 *
	 * @param <UI> the IResource type
	 * @param pSourceResource the technology dependent resource
	 * @return the clone
	 */
	public static <UI> UI cloneResource(UI pSourceResource)
	{
		return cloneResource(pSourceResource, true);
	}
	
	/**
	 * Creates a clone of the given resource.
	 * 
	 * @param <UI> the IResource type
	 * @param pSourceResource the technology dependent resource
	 * @param pStrictMode <code>true</code> to throw an exception if this method didn't create a "clone".
	 * @return the cloned resource or the source resource if no clone was created and strict mode is not
	 *         set
	 * @throws RuntimeException if strict mode is enabled and no clone was created
	 */
	private static <UI> UI cloneResource(UI pSourceResource, boolean pStrictMode)
	{
		if (pSourceResource == null)
		{
			return null;
		}
		
		if (pSourceResource instanceof IColor)
		{		
			IColor color = (IColor)pSourceResource;
			
			return (UI)UIFactoryManager.getFactory().createColor(color.getRGBA());
		}
		else if (pSourceResource instanceof IImage)
		{		
			IImage image = (IImage)pSourceResource;
			
			//this won't return a new instance or a clone -> and this is the reason for strict mode
			return (UI)UIFactoryManager.getFactory().getImage(image.getImageName());
		}
		else if (pSourceResource instanceof IFont)
		{		
			IFont font = (IFont)pSourceResource;
			
			return (UI)UIFactoryManager.getFactory().createFont(font.getName(), font.getStyle(), font.getSize());
		}
		else if (pSourceResource instanceof ICellEditor)
		{
			ICellEditor cedOrig = ((ICellEditor)pSourceResource);
			
			Class[] clsIfaces = ResourceUtil.getInterfaces(cedOrig.getClass(), false, ICellEditor.class);

			for (int i = 0; i < clsIfaces.length; i++)
			{
				if (ICellEditor.class.isAssignableFrom(clsIfaces[i]))
				{
					IFactory uifactory = UIFactoryManager.getFactory();
					
					Method[] metFactory = uifactory.getClass().getMethods();
					
					for (int j = 0; j < metFactory.length; j++)
					{
						if (metFactory[j].getReturnType() == clsIfaces[i])
						{
							try
							{
								UI cedNew = (UI)metFactory[j].invoke(uifactory);

								Class<?> clazz = clsIfaces[i];

								//copy values, but only ICellEditor properties
								do
								{
									BeanType btOrig = BeanType.getBeanType(clazz);
									
									Bean bnOrig = new Bean(cedOrig);
									Bean bnNew  = new Bean(cedNew);
									
									for (String sProperty : btOrig.getPropertyNames())
									{
										bnNew.put(sProperty, cloneResource(bnOrig.get(sProperty), false));
									}
									
									clazz = clazz.getSuperclass();
								}
								while (clazz != null && ICellEditor.class.isAssignableFrom(clazz));
								
								return cedNew;
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		if (pStrictMode)
		{
			new RuntimeException("Unsupported object type: " + pSourceResource.getClass().getName());
		}
		
		return pSourceResource;
	}
	
}	// UIFactoryManager
