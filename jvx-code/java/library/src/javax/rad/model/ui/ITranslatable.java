/*
 * Copyright 2011 SIB Visions GmbH
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
 * 30.03.2011 - [JR] - creation
 * 22.05.2014 - [JR] - #1044: set/isTranslationEnabled introduced
 */
package javax.rad.model.ui;

import javax.rad.util.TranslationMap;

/**
 * The <code>ITranslatable</code> is the interface for text oriented objects which needs
 * translation support.
 * 
 * @author René Jahn
 */
public interface ITranslatable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the possible translations.
	 * 
	 * @param pTranslation the translation mapping
	 */
	public void setTranslation(TranslationMap pTranslation);
	
	/**
	 * Gets the possible translation mapping.
	 * 
	 * @return the translation mapping
	 */
	public TranslationMap getTranslation();
	
    /**
     * Sets the translation en- or disabled.
     * 
     * @param pEnabled <code>true</code> to enable the translation, <code>false</code> to disable
     */
    public void setTranslationEnabled(boolean pEnabled);
    
    /**
     * Gets whether the translation is en- or disabled.
     * 
     * @return <code>true</code> if translation is performed, <code>false</code> otherwise
     */
    public boolean isTranslationEnabled();

}	// ITranslatable
