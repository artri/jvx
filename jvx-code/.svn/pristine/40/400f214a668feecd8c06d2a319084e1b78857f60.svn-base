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
 * 19.08.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.impl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The <code>Parameter</code> annotation is used in {@link Configuration} for using multiple parameters with
 * only one annotation. It's a helper annotation.
 *  
 * @author René Jahn
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Properties
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** 
     * The parameter name.
     * 
     * @return the name 
     */
    public String name();
    
    /** 
     * The value. 
     *
     * @return the value
     */
    public String value();
    
}   // Parameter
