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
 * 24.04.2009 - [JR] - creation
 * 11.05.2009 - [JR] - added spaces for multiline messages (show message beside level not under it)
 * 26.11.2009 - [JR] - used StringUtil.toString instead of Arrays.deepToString
 * 20.02.2023 - [JR] - #3145: custom log format and milliseconds for standard format
 */
package com.sibvisions.util.log.jdk;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JdkLineFormatter</code> is a {@link Formatter} implementation. The default
 * format is: 
 * <pre>
 * dd.mm.yyyy HH:mi:ss [ &lt;Level&gt; ] &lt;Text&gt; &lt;caller class&gt; &lt;caller method&gt;
 * </pre>
 * If the text contains more than 100 characters the format is:
 * <pre>
 * dd.mm.yyyy HH:mi:ss [ &lt;Level&gt; ] &lt;caller class&gt; &lt;caller method&gt;
 *                     &lt;Text&gt;
 * </pre>
 * 
 * @author René Jahn
 */
public class JdkLineFormatter extends Formatter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** Leading space for multiline message. */
	private static final String SPACE = "                                        ";
	
	/** format for the timestamp of a log message. */
	private MessageFormat msgf = new MessageFormat("{0,date} {0,time,HH:mm:ss.SSS}");
	
	/** timestamp of the a message. */
	private Date dateLog = new Date();

	/** an optional custom log format. */
	private String sLogFormat = null;
	
	/** whether this logger is already initialized. */
	private boolean bInit = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public synchronized String format(LogRecord pRecord) 
    {
		configureFormat(pRecord);
		
		try
		{
	    	dateLog.setTime(pRecord.getMillis());

	    	String sSource;
			
	    	if (pRecord.getSourceClassName() != null) 
	    	{	
	    		sSource = pRecord.getSourceClassName();
	    	} 
	    	else 
	    	{
	    		sSource = pRecord.getLoggerName();
	    	}
	    	
	    	boolean bMultiLine = pRecord.getMessage() != null 
	    	                     && (pRecord.getMessage().length() > 100 || pRecord.getMessage().indexOf('\n') >= 0 || pRecord.getThrown() != null);
			
			if (sLogFormat != null)
			{

	            return String.format(sLogFormat,
	                                 dateLog,
				                     sSource,
				                     pRecord.getLoggerName(),
				                     pRecord.getLevel().getName(),
				                     bMultiLine ? formatMultilineMessage(formatMessage(pRecord)) : formatMessage(pRecord),
				                     formatMultilineMessage(ExceptionUtil.dump(pRecord.getThrown(), true)));
			}
			else
			{
		    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    	PrintStream ps = new PrintStream(bos);
		    	
		    	StringBuffer sbLog = new StringBuffer();
		    	
		    	
		    	msgf.format(new Object[] {dateLog}, sbLog, null);
		    	
		    	if (bMultiLine)
		    	{
			    	ps.printf(" [ %-11s ] ", pRecord.getLevel().getName());
			    	ps.close();
		
			    	sbLog.append(bos.toString());
		    	}
		    	else
		    	{
			    	ps.printf(" [ %-11s ] %-100s ", pRecord.getLevel().getName(), formatMessage(pRecord));
			    	ps.close();
		
			    	sbLog.append(bos.toString());
		    	}
		    	
		    	if (pRecord.getSourceClassName() != null) 
		    	{	
		    		sbLog.append(pRecord.getSourceClassName());
		    	} 
		    	else 
		    	{
		    		sbLog.append(pRecord.getLoggerName());
		    	}
		    	
		    	if (pRecord.getSourceMethodName() != null) 
		    	{	
		    		sbLog.append(" ");
		    		sbLog.append(pRecord.getSourceMethodName());
		    	}
		    	
		    	sbLog.append("\n");
		    	
		    	if (bMultiLine)
		    	{
		    		sbLog.append(formatMultilineMessage(formatMessage(pRecord)));
		    		
		    		if (pRecord.getThrown() != null && !pRecord.getMessage().endsWith("\n"))
		    		{
		    			sbLog.append("\n");
		    		}
		    	}
		
		    	if (pRecord.getThrown() != null) 
		    	{
		    		sbLog.append(formatMultilineMessage(ExceptionUtil.dump(pRecord.getThrown(), true)));
		    	}	    	
		    	
		    	return sbLog.toString();
			}
		}
		catch (Throwable th)
		{
			th.printStackTrace();
			
			return formatMessage(pRecord);
		}
	}
	
	/**
	 * Configures the format.
	 * 
	 * @param pRecord the log record
	 */
	private void configureFormat(LogRecord pRecord)
	{
		if (!bInit)
		{
			LogManager lman = LogManager.getLogManager();
			
			//root handlers
			
			Handler[] handlers = lman.getLogger("").getHandlers();
			
			if (handlers != null)
			{
				for (int i = 0; i < handlers.length; i++)
				{
					if (handlers[i].getFormatter() == this)
					{
						bInit = true;
						
						String sFormat = lman.getProperty(handlers[i].getClass().getName() + ".format");
						
						if (!StringUtil.isEmpty(sFormat))
						{
							sLogFormat = sFormat;
						}
					}
				}
			}
	
			if (!bInit)
			{
				//We also check logger handlers
				
				handlers = lman.getLogger(pRecord.getLoggerName()).getHandlers();
				
				for (int i = 0; i < handlers.length; i++)
				{
					if (handlers[i].getFormatter() == this)
					{
						bInit = true;
						
						String sFormat = lman.getProperty(handlers[i].getClass().getName() + ".format");
						
						if (!StringUtil.isEmpty(sFormat))
						{
							sLogFormat = sFormat;
						}
					}
				}
			}
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Adds leading spaces, after every line break, to the log message.
	 * 
	 * @param pMessage message with line breaks
	 * @return message with leading spaces after each line break
	 */
	private StringBuilder formatMultilineMessage(String pMessage)
	{
		List<String> liLines = StringUtil.separateList(pMessage, "\n", false);
		
		StringBuilder sbMessage = new StringBuilder();
		
		
		for (String sLine : liLines)
		{
			sbMessage.append(SPACE);
			sbMessage.append(sLine);
			sbMessage.append("\n");
		}
		
		return sbMessage;
	}
	
	/**
	 * Concatenates any objects to a string. It handles Object[] and Throwable
	 * information as special objects. 
	 * 
	 * @param pInfo any objects
	 * @return the log text
	 */
	public static String concat(Object... pInfo)
	{
		//prepare the log text
		StringBuffer sbfText = new StringBuffer();
		
		if (pInfo != null)
		{
			for (int i = 0, anz = pInfo.length; i < anz; i++)
			{
				if (pInfo[i] == null)
				{
					sbfText.append("null");
				}
				else if (pInfo[i] instanceof Throwable)
				{
					if (i > 0)
					{
						sbfText.append("\n");
					}
					
					sbfText.append(ExceptionUtil.dump((Throwable)pInfo[i], true));
				}
				else
				{
					sbfText.append(StringUtil.toString(pInfo[i]));
				}
			}
		}
		
		return sbfText.toString();
	}

}	// JdkLineFormatter
