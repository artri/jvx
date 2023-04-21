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
 * 19.02.2010 - [JR] - creation
 * 04.04.2020 - [JR] - moved to superclass
 */
package com.sibvisions.rad.application;

import javax.rad.ui.IComponent;
import javax.rad.ui.IContainer;
import javax.rad.ui.control.ITable;

/**
 * The <code>ApplicationUtil</code> is a utility class which provides methods and constants which will
 * be used from applications.
 * 
 * @author René Jahn
 */
public class ApplicationUtil extends javax.rad.application.ApplicationUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Invisible constructor, because <code>ApplictionUtil</code> is a utility class.
	 */
	protected ApplicationUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the {@link ITable} which is the first in the component tree.
	 * 
	 * @param pComponent the starting component
	 * @return the first found {@link ITable}
	 */
	public static ITable getFirstTable(IComponent pComponent)
	{
		if (pComponent instanceof ITable)
		{
			return (ITable)pComponent;
		}
		else if (pComponent instanceof IContainer)
		{
			IContainer container = (IContainer)pComponent;
			
			for (int i = 0, count = container.getComponentCount(); i < count; i++)
			{
				ITable table = getFirstTable(container.getComponent(i));
				
				if (table != null)
				{
					return table;
				}
			}
		}
		
		return null;
	}
	
}	// ApplicationUtil
