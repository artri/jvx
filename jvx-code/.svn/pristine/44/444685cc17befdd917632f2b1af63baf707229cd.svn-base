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
 * 04.10.2017 - [RZ] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.events;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.Registration;

/**
 * The {@link RegistrationContainer} is a simple helper utility which allows to
 * add and track multiple {@link Registration}s and remove them with one call.
 * 
 * @author Robert Zenz
 */
public class RegistrationContainer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 
	/** The registered {@link Registration}s. */
	protected List<Registration> registrations = new ArrayList<Registration>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link RegistrationContainer}.
	 */
	public RegistrationContainer()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a {@link Registration} for tracking.
	 *
	 * @param pRegistration the {@link Registration} for tracking.
	 */
	public void add(Registration pRegistration)
	{
		if (pRegistration == null)
		{
			return;
		}
		
		synchronized (registrations)
		{
			registrations.add(pRegistration);
		}
	}
	
	/**
	 * Removes all tracked {@link Registration}s.
	 * 
	 * @see Registration#remove()
	 */
	public void removeAll()
	{
		synchronized (registrations)
		{
			while (!registrations.isEmpty())
			{
				registrations.remove(0).remove();
			}
		}
	}
	
}	// RegistrationContainer
