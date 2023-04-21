/*
 * Copyright 2016 SIB Visions GmbH
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
 * 12.02.2016 - [JR] - creation
 */
package com.sibvisions.rad.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>Inherit</code> annotation should be used if an action was defined in a parent LCO
 * and the action call through the LCO should be executed with the scope of the caller LCO and
 * not with parent scope, e.g.
 * 
 * <pre>
 * public class Session extends Application
 * {
 *     public long getId()
 *     {
 *         return System.identityHashCode(this);
 *     }
 *
 *     public long getId2()
 *     {
 *         return System.identityHashCode(this);
 *     }
 *     
 *     {@literal @}Inherit
 *     public long getIdInherit()
 *     {
 *         return System.identityHashCode(this);
 *     }
 * }
 * 
 * public class Students extends Session
 * {
 *     public long getId2()
 *     {
 *         return super.getId2();
 *     }
 * }
 * 
 * studentsConnection.callAction("getId");
 * </pre>
 * 
 * The action call of getId will be executed in the Session LCO (scope) and the action call of
 * getIdInherit will be executed in the Students LCO (scope). Different LCO means different 
 * states and different instances of objects.
 * 
 * An action call of getId2 would be executed in the Students LCO because the action was overwritten.
 * 
 * @author René Jahn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface Inherit
{
}   // Inherit
