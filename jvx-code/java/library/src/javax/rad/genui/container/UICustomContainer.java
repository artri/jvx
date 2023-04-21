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
package javax.rad.genui.container;

import javax.rad.genui.UIContainer;
import javax.rad.genui.UIFactoryManager;
import javax.rad.model.ui.ITranslatable;
import javax.rad.ui.IContainer;

/**
 * Platform and technology independent container.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * 
 * @author Martin Handsteiner
 */
public class UICustomContainer extends UIContainer<IContainer>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>UICustomContainer</code>.
     *
     * @param pContainer the Container.
     * @see IContainer
     */
	public UICustomContainer(IContainer pContainer)
	{
		super(pContainer);
	}

	/**
     * Creates a new instance of <code>UICustomContainer</code>.
     *
	 * @param pCustomContainer the custom container.
	 */
	public UICustomContainer(Object pCustomContainer)
	{
		super(UIFactoryManager.getFactory().createCustomContainer(pCustomContainer));
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
	
}	// UICustomContainer
