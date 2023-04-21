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
 * 13.11.2015 - [RZ] - creation
 */
package com.sibvisions.rad.ui.celleditor;

import javax.rad.model.IDataRow;
import javax.rad.model.ui.ICellEditorHandler;
import javax.rad.model.ui.ICellEditorListener;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality provided by {@link AbstractTextCellEditor}.
 * 
 * @author Robert Zenz
 */
public class TestAbstractTextCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests getting and setting of the content type.
	 */
	@Test
	public void testContentType()
	{
		TestingTextCellEditor cellEditor = new TestingTextCellEditor();
		
		Assert.assertEquals(AbstractTextCellEditor.TEXT_PLAIN_SINGLELINE, cellEditor.getContentType());
		
		cellEditor.setContentType(AbstractTextCellEditor.TEXT_PLAIN_PASSWORD);
		Assert.assertEquals(AbstractTextCellEditor.TEXT_PLAIN_PASSWORD, cellEditor.getContentType());
		
		cellEditor.setContentType(null);
		Assert.assertEquals(AbstractTextCellEditor.TEXT_PLAIN_SINGLELINE, cellEditor.getContentType());
		
		cellEditor.setContentType("");
		Assert.assertEquals("", cellEditor.getContentType());
		
		TestingTextCellEditor cellEditorConstructorTest = new TestingTextCellEditor(AbstractTextCellEditor.TEXT_PLAIN_PASSWORD);
		Assert.assertEquals(AbstractTextCellEditor.TEXT_PLAIN_PASSWORD, cellEditorConstructorTest.getContentType());
		
		TestingTextCellEditor cellEditorConstructorTestNull = new TestingTextCellEditor(null);
		Assert.assertEquals(AbstractTextCellEditor.TEXT_PLAIN_SINGLELINE, cellEditorConstructorTestNull.getContentType());
	}
	
	/**
	 * Tests the password masking functionality with the default echo char.
	 */
	@Test
	public void testPasswordMaskingDefaultEchoChar()
	{
		TestingTextCellEditor cellEditor = new TestingTextCellEditor();
		
		Assert.assertEquals("********", cellEditor.maskPassword("12345678"));
	}
	
	/**
	 * Tests the password masking functionality by swapping the echo char after
	 * the internal buffer has already been filled.
	 */
	@Test
	public void testPasswordMaskingEchoCharSwap()
	{
		TestingTextCellEditor cellEditor = new TestingTextCellEditor();
		
		Assert.assertEquals("*****************************************", cellEditor.maskPassword("This is a really long long long sentence."));
		cellEditor.setEchoChar('$');
		Assert.assertEquals("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", cellEditor.maskPassword("This is a really long long long sentence."));
	}
	
	/**
	 * Tests the password masking functionality by testing the behavior for
	 * different parameters.
	 */
	@Test
	public void testPasswordMaskingParameters()
	{
		TestingTextCellEditor cellEditor = new TestingTextCellEditor();
		
		Assert.assertEquals("", cellEditor.maskPassword(null));
		Assert.assertEquals("", cellEditor.maskPassword(""));
	}
	
	/**
	 * Tests the password masking functionality by setting a different echo
	 * char.
	 */
	@Test
	public void testPasswordMaskingSetEchoChar()
	{
		TestingTextCellEditor cellEditor = new TestingTextCellEditor();
		
		cellEditor.setEchoChar('$');
		Assert.assertEquals('$', cellEditor.getEchoChar());
		
		Assert.assertEquals("$$$$$$$$", cellEditor.maskPassword("12345678"));
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link AbstractTextCellEditor} extension that is used for testing. It
	 * simply makes the protected methods public.
	 * 
	 * @author Robert Zenz
	 */
	private static final class TestingTextCellEditor extends AbstractTextCellEditor
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link TestingTextCellEditor}.
		 */
		public TestingTextCellEditor()
		{
			super();
		}
		
		/**
		 * Creates a new instance of {@link TestingTextCellEditor}.
		 *
		 * @param pInitialContentType the {@link String initial content type}.
		 */
		public TestingTextCellEditor(String pInitialContentType)
		{
			super(pInitialContentType);
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public ICellEditorHandler createCellEditorHandler(ICellEditorListener pCellEditorListener, IDataRow pDataRow, String pColumnName)
		{
			return null;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public char getEchoChar()
		{
			return super.getEchoChar();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String maskPassword(String pPassword)
		{
			return super.maskPassword(pPassword);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setEchoChar(char pEchoChar)
		{
			super.setEchoChar(pEchoChar);
		}
		
	}	// TestingTextCellEditor
	
}	// TestAbstractTextCellEditor
