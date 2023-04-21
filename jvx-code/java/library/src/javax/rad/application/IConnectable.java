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
 * 20.11.2008 - [HM] - creation
 */
package javax.rad.application;

import javax.rad.remote.AbstractConnection;

/**
 * Platform and technology independent connectable definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface IConnectable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Sets the communication connection.
     *
     * @param pConnection the connection
     */
    public void setConnection(AbstractConnection pConnection);
    
	/**
	 * Gets the communication connection.
	 * 
	 * @return the connection
	 */
	public AbstractConnection getConnection();
	
	/**
	 * Checks if the communication is established.
	 * 
	 * @return <code>true</code> if the connection is established
	 */
	public boolean isConnected();
	
}	// IConnectable
