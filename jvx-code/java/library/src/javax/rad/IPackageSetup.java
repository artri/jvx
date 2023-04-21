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
 * 01.10.2008 - [JR] - creation
 * 05.10.2008 - [JR] - CHANGE_PASSWORD defined
 * 09.04.2009 - [JR] - Refactored and made an interface
 * 10.10.2010 - [JR] - JVx 0.8, Server 0.4.1
 * 08.01.2011 - [JR] - JVx 0.9 beta-3
 * 01.04.2011 - [JR] - JVx 0.9
 * 31.05.2011 - [JR] - JVx 1.0 beta-1
 * 30.06.2011 - [JR] - JVx 1.0 beta-2
 * 27.10.2011 - [JR] - JVx 1.0 beta-6
 * 23.12.2011 - [JR] - JVx 1.0
 * 15.01.2013 - [JR] - JVx 1.1
 * 24.01.2013 - [JR] - JVx 1.2 beta
 * 04.10.2013 - [JR] - JVx 1.2 (= GA)
 * 05.10.2013 - [JR] - JVx 1.2.1
 * 20.12.2013 - [JR] - JVx 2.0 beta
 * 02.12.2014 - [JR] - JVx 2.1, JVx 2.2 beta1
 */
package javax.rad;

/**
 * The <code>IPackageSetup</code> class defines package relevant version information
 * and properties.
 * 
 * @author René Jahn
 */
public interface IPackageSetup
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Constant JVx OpenSource version value. */
	public static final String JVX_OS_VERSION = "3.0 (beta)";

	/** Constant version value. */
	public static final String SPEC_VERSION = "0.4.1";
	
}	// IPackageSetup
