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
 * 04.11.2008 - [HM] - creation
 */
package javax.rad.ui.celleditor;

/**
 * Platform and technology independent text editor definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 */
public interface ITextCellEditor extends IInplaceCellEditor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Content type for using a single line editor. */
	public static final String TEXT_PLAIN_SINGLELINE 	= "text/plain;singleline";
	
	/** Content type for using a multi line line editor. */
	public static final String TEXT_PLAIN_MULTILINE 	= "text/plain;multiline";
	
	/** Content type for using a multi line line editor. */
	public static final String TEXT_PLAIN_WRAPPEDMULTILINE 	= "text/plain;wrappedmultiline";
	
	/** Content type for using a multi line line editor. */
	public static final String TEXT_PLAIN_PASSWORD 	= "text/plain;password";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the content type used for editing the text.
	 * 
	 * @return the content type
	 */
	public String getContentType();

	/**
	 * Sets the content type used for editing the text.
	 * 
	 * @param pContentType the content type
	 */
	public void setContentType(String pContentType);

}	// ITextCellEditor
