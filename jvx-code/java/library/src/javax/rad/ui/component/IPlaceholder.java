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
 * 18.09.2018 - [HM] - creation
 */
package javax.rad.ui.component;

/**
 * Platform and technology independent Placeholder definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IPlaceholder
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the placeholder text.
     * If no placeholder text is set, the column label is used.
     * 
     * @return the placeholder text.
     */
    public String getPlaceholder();
    
    /**
     * Sets the placeholder text.
     * If no placeholder text is set, the column label is used.
     * 
     * @param pPlaceholder the placeholder text.
     */
    public void setPlaceholder(String pPlaceholder);
    
}	// IPlaceholder
