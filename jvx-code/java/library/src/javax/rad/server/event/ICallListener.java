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
 * 27.05.2015 - [JR] - creation
 */
package javax.rad.server.event;

import javax.rad.server.event.type.IAfterCallListener;
import javax.rad.server.event.type.IAfterLastCallListener;
import javax.rad.server.event.type.IBeforeCallListener;
import javax.rad.server.event.type.IBeforeFirstCallListener;

/**
 * The listener interface for receiving events from {@link javax.rad.server.ICallHandler}.
 * 
 * @author René Jahn
 */
public interface ICallListener extends IBeforeFirstCallListener,
                                       IAfterLastCallListener,
                                       IBeforeCallListener,
                                       IAfterCallListener
{
}	// ICallListener
