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
 * 11.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import javax.rad.ui.celleditor.ITextCellEditor;

/**
 * The {@link AbstractTextCellEditor} is an {@link ITextCellEditor}
 * implementation, which provides a base implementation.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractTextCellEditor extends AbstractInplaceCellEditor 
                                             implements ITextCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The content type of this. */
    protected transient String contentType = TEXT_PLAIN_SINGLELINE;
	
	/** The char that is used to mask passwords. */
	private transient char echoChar = '*';
	
	/** The buffer from which the masked passwords are derived. */
	private transient StringBuilder passwordMaskBuffer = new StringBuilder();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractTextCellEditor}.
	 */
	protected AbstractTextCellEditor()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link AbstractTextCellEditor}.
	 *
	 * @param pInitialContentType the {@link String initial content type}.
	 */
	protected AbstractTextCellEditor(String pInitialContentType)
	{
		this();
		
		setContentType(pInitialContentType);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	public String getContentType()
	{
		return contentType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setContentType(String pContentType)
	{
		if (pContentType == null)
		{
			contentType = TEXT_PLAIN_SINGLELINE;
		}
		else
		{
			contentType = pContentType;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDirectCellEditor()
	{
		return false;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the password echo char which is used by
	 * {@link #maskPassword(String)}.
	 * 
	 * @return the password echo char which is used by
	 *         {@link #maskPassword(String)}.
	 * @see #setEchoChar(char)
	 * @see #maskPassword(String)
	 */
	protected char getEchoChar()
	{
		return echoChar;
	}
	
	/**
	 * Masks the given string with the set echo char.
	 * 
	 * @param pPassword the string to mask.
	 * @return the masked string. An empty string if the given password is
	 *         {@code null} or empty.
	 * @see #getEchoChar()
	 * @see #setEchoChar(char)
	 */
	protected String maskPassword(String pPassword)
	{
		if (pPassword == null || pPassword.length() == 0)
		{
			return "";
		}
		
		while (passwordMaskBuffer.length() < pPassword.length())
		{
			passwordMaskBuffer.append(echoChar);
		}
		
		return passwordMaskBuffer.substring(0, pPassword.length());
	}
	
	/**
	 * Sets the password echo char which is used by
	 * {@link #maskPassword(String)}.
	 * 
	 * @param pEchoChar the password mask which is used by
	 *            {@link #maskPassword(String)}.
	 * @see #getEchoChar()
	 * @see #maskPassword(String)
	 */
	protected void setEchoChar(char pEchoChar)
	{
		echoChar = pEchoChar;
		
		passwordMaskBuffer.delete(0, passwordMaskBuffer.length());
	}
	
}	// AbstractTextCellEditor
