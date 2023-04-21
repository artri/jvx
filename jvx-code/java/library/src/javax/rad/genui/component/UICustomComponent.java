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
 * 20.05.2009 - [HM] - creation
 */
package javax.rad.genui.component;

import javax.rad.genui.UIComponent;
import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ui.ITranslatable;
import javax.rad.ui.IComponent;

/**
 * Platform and technology independent component.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UICustomComponent extends UIComponent<IComponent>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>UICustomComponent</code>.
     *
     * @param pComponent the Component.
     * @see IComponent
     */
	public UICustomComponent(IComponent pComponent)
	{
		super(pComponent);
	}

	/**
     * Creates a new instance of <code>UICustomComponent</code>.
     *
	 * @param pCustomComponent the custom component.
	 */
	public UICustomComponent(Object pCustomComponent)
	{
		this(UIFactoryManager.getFactory().createCustomComponent(pCustomComponent));
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
			Object res = getResource();
			
			if (res instanceof ITranslatable)
			{
				((ITranslatable)res).setTranslation(getCurrentTranslation());
			}
		}
	}
	
}	// UICustomComponent
