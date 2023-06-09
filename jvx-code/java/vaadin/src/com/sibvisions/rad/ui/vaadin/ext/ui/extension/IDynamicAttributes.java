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
 * 04.02.2020 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

/**
 * The <code>IDynamicAttributes</code> is a marker interface and defines the access to a dynamically created
 * {@link AttributesExtension} instance.
 * 
 * @author Ren� Jahn
 */
public interface IDynamicAttributes
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the dynamically created {@link AttributesExtension}.
     * 
     * @return the attributes extension
     */
    public AttributesExtension getAttributesExtension();
    
}   // IDynamicAttributes
