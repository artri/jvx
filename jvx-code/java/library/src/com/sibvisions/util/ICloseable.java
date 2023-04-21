/*
 * Copyright 2014 SIB Visions GmbH
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
 * 15.05.2014 - [JR] - creation
 */
package com.sibvisions.util;

/**
 * A <code>ICloseable</code> is a source or destination of data that can be closed. 
 * The close method is invoked to release resources that the object is 
 * holding (such as open files).
 *
 * @author Ren� Jahn
 */
public interface ICloseable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Closes the object (stream, connection, ...) and releases any system resources
     * associated with it. If the object is already closed, then invoking this
     * method has no effect.
     * 
     * @throws Throwable if an error occurs
     */
    public void close() throws Throwable;
    
}   // ICloseable
