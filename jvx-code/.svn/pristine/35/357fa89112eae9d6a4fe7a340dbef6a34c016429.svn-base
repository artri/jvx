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
 * 18.12.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service.mixin;

import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * The <code>DBAccessMixin</code> controls ser-/deserialization of {@link com.sibvisions.rad.persist.jdbc.DBAccess} instances.
 * If an instance is used as property of another object, it will be completely ignored and not serialized. 
 * If an instance will be accessed directly, it will be serialized without ignored properties.
 * 
 * @author René Jahn
 */
@JsonIgnoreType
public abstract class DBAccessMixin
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getDriver();
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getUrl();
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getUsername();
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getPassword();
    
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getConnection();
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract Properties getDBProperties();
    /**
     * No access.
     * @param pName unused
     * @return null
     */
    @JsonIgnore abstract String getDBProperty(String pName);
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String isAutoCommit();
    /**
     * No access.
     * @return null
     */
    @JsonIgnore abstract String getDefaultSchema();
    
}   // DBAccessMixin
