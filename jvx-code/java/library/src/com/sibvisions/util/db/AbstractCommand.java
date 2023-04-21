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

import java.util.List;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractCommand</code> is a default <code>ICommand</code> implementation and should be used as superclass for 
 * all commands.
 * 
 * @author René Jahn
 */
public abstract class AbstractCommand implements ICommand
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the command. */
	private String sCommand;
	
	/** the command name. */
	private String sCommandName;
	
	/** the command parameters. */
	private String[] saParameters;
	
	/** the start position. */
	private int iPosition;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public void setPosition(int pPosition)
	{
		iPosition = pPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPosition()
	{
		return iPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCommand(String pCommand)
	{
		sCommand = pCommand;
		
		int iPos = sCommand.indexOf('(');
		
		sCommandName = sCommand.substring(0, iPos);
		
		String sParams = sCommand.substring(iPos + 1, sCommand.length() - 1);
		
		if (sParams.trim().length() > 0)
		{
			List<String> liParameters = StringUtil.separateList(sParams, ",", true);
			
			saParameters = new String[liParameters.size()];
			
			String sParam;
			
			for (int i = 0, anz = liParameters.size(); i < anz; i++)
			{
				sParam = liParameters.get(i);
				
				if (sParam.charAt(0) == '\"' || sParam.charAt(0) == '\'')
				{
					sParam = sParam.substring(1, sParam.length() - 1);
				}
				
				saParameters[i] = sParam;
			}
		}
		else
		{
			saParameters = new String[0];
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getCommand()
	{
		return sCommand;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Gets the command name.
	 * 
	 * @return the command name
	 */
	public String getCommandName()
	{
		return sCommandName;
	}
	
	/**
	 * Gets the command parameters.
	 * 
	 * @return the parameters
	 */
	public String[] getCommandParameters()
	{
		return saParameters;
	}

}	// AbstractCommand
