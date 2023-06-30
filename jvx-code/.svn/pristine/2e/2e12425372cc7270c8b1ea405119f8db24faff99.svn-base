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
 * 01.06.2023 - [HM] - creation
 */
package com.sibvisions.util;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>ExecuteException</code> is thrown in case of any exception during execute or executeScript.
 * 
 * @author Martin Handsteiner
 * @see SimpleJavaSource
 */
public class ExecuteException extends IllegalArgumentException
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The source. */
	private String source;
	/** The error tag. */
	private String errorTag;
	/** The error position. */
	private int errorPosition;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new <code>ExecuteException</code>.
	 * 
	 * @param pSource the source
	 * @param pErrorTag the error tag
	 * @param pErrorPosition the error position
     * @param pCause the cause
	 */
	public ExecuteException(String pSource, String pErrorTag, int pErrorPosition, Throwable pCause)
	{
	    super(pCause.getMessage() + " (tag=\"" + pErrorTag + "\", pos=" + pErrorPosition + ")\n" + 
	            getErrorCodeSnippet(pSource, pErrorTag, pErrorPosition), pCause);
	    
	    source = pSource;
	    errorTag = pErrorTag;
	    errorPosition = pErrorPosition;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the executed source.
	 * 
	 * @return the executed source.
	 */
	public String getSource()
	{
	    return source;
	}
	
	/**
	 * Gets the tag causing the error.
	 * 
	 * @return the tag causing the error.
	 */
	public String getErrorTag()
	{
	    return errorTag;
	}
    
	/**
	 * Gets the position of the error tag.
	 * 
	 * @return the position of the error tag.
	 */
    public int getErrorPosition()
    {
        return errorPosition;
    }
    
    /**
     * Gets the line of the error position starting with 0.
     * 
     * @return the line of the error position starting with 0.
     */
    public int getErrorLine()
    {
        char[] chars = source.toCharArray();
        
        int line = 0;
        for (int i = 0; i < errorPosition; i++)
        {
            if (chars[i] == '\n')
            {
                line++;
            }
        }

        return line;
    }
    
    /**
     * Gets the column of the error position starting with 0.
     * 
     * @return the column of the error position starting with 0.
     */
    public int getErrorColumn()
    {
        int lastLine = source.lastIndexOf('\n', errorPosition);
        
        return lastLine < 0 ? errorPosition : errorPosition - lastLine - 1;
    }
    
    /**
     * Gets the root exception.
     * 
     * @return the root exception.
     */
    public Throwable getRootCause()
    {
        Throwable rootCause = this;
        
        while (rootCause.getCause() != null)
        {
            rootCause = rootCause.getCause();
        }
        
        return rootCause;
    }
    
    /**
     * Gets the error code snippet for the error message.
     * 
     * @param pSource the source
     * @param pErrorTag the error tag
     * @param pErrorPosition the error position
     * @return the code snippet 
     */
    private static String getErrorCodeSnippet(String pSource, String pErrorTag, int pErrorPosition)
    {
        int lastLine = pSource.lastIndexOf('\n', pErrorPosition);
        int nextLine = pSource.indexOf('\n', pErrorPosition + pErrorTag.length());
        int errorColumn = lastLine < 0 ? pErrorPosition + 1 : pErrorPosition - lastLine;

        return pSource.substring(lastLine < 0 ? 0 : lastLine + 1, nextLine < 0 ? pSource.length() : nextLine) +
                '\n' +
                StringUtil.lpad("^", errorColumn);
        
    }
    
}	// ExecuteException
