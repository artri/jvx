/*
 * Copyright 2017 SIB Visions GmbH
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
 * 31.08.2017 - [JR] - creation
 */
package javax.rad.server;

import com.sibvisions.util.ICloseable;

/**
 * The <code>ICloseableSession</code> defines a manually closeable session.
 * 
 * @author Ren� Jahn
 */
public interface ICloseableSession extends ICloseable, 
                                           ISession
{
}   // ICloseableSession
