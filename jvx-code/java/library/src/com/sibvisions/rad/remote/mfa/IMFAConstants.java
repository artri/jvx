/*
 * Copyright 2022 SIB Visions GmbH
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
 * 30.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.remote.mfa;

import javax.rad.remote.IConnectionConstants;

/**
 * The <code>IMFAConstants</code> defines constants for the
 * multi-factor handling between client and server.
 *  
 * @author René Jahn
 */
public interface IMFAConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** no state. */
	public static final int STATE_NONE 		= -1;
	/** init state. */
	public static final int STATE_INIT 		= 0;
	/** retry state. */
	public static final int STATE_RETRY 	= 1;
	
	/** no custom type. */
	public static final int TYPE_NONE 		= -1;
	/** wait type. */
	public static final int TYPE_WAIT 		= 0;
	/** text input type. */
	public static final int TYPE_TEXTINPUT 	= 1;
	/** URL type. */
	public static final int TYPE_URL 		= 2;
	
	/** the payload property name. */
	public static final String PROP_PAYLOAD = IConnectionConstants.MFA_PAYLOAD;

}	// IMFAConstants

