/*
 * Copyright 2011 SIB Visions GmbH
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
 * 12.11.2011 - [JR] - creation
 */
package com.sibvisions.util.db;

/**
 * The <code>ICommand</code> interface defines a command for db script import. All implementing classes
 * are checked automaticall from the {@link DBImporter}.
 * 
 * @author René Jahn
 */
interface ICommand
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the start position in the statement for the command.
	 * 
	 * @param pPosition the start index
	 */
	public void setPosition(int pPosition);
	
	/**
	 * Gets the start position in the statement for the command.
	 * 
	 * @return the start index
	 */
	public int getPosition();

	/**
	 * Sets the whole command, with name and parameters.
	 * 
	 * @param pCommand the command string
	 */
	public void setCommand(String pCommand);
	
	/**
	 * Gets the command.
	 * 
	 * @return the command string, with name and parameters.
	 */
	public String getCommand();
	
	/**
	 * Gets the value for the command.
	 * 
	 * @return the value
	 * @throws Exception if value detection fails
	 */
	public Object getValue() throws Exception;
	
}	// ICommand
