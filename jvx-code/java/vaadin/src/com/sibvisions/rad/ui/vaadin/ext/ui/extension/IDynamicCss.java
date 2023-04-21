/*
 * Copyright 2015 SIB Visions GmbH
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
 * 01.10.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.extension;

/**
 * The <code>IDynamicCss</code> is a marker interface and defines the access to a dynamically created
 * {@link CssExtension} instance.
 * 
 * @author René Jahn
 */
public interface IDynamicCss
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the dynamically created {@link CssExtension}.
     * 
     * @return the css extension
     */
    public CssExtension getCssExtension();
    
}   // IDynamicCss
