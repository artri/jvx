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
 * 01.05.2011 - [JR] - creation
 * 19.05.2011 - [JR] - used ThreadHandler instead of simple Thread
 * 24.05.2011 - [JR] - configurable error/output forwarder
 *                   - async execution supported
 *                   - working directory support
 * 16.09.2011 - [JR] - made it possible to access stream results after destroy
 *                   - check if process is running
 *                   - listener support   
 * 19.09.2011 - [JR] - getCommand(): quote program [BUGFIX]
 * 14.10.2011 - [JR] - removed quoting    
 * 04.11.2011 - [JR] - setIgnoreLogging implemented     
 * 03.12.2012 - [JR] - buffer character count implemented     
 * 24.12.2013 - [JR] - support for environment parameters               
 */
package com.sibvisions.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sibvisions.util.event.IExecuteListener;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;

/**
 * The <code>Execute</code> class executes external programs.
 * 
 * @author René Jahn
 */
public class Execute
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** program to launch. */
	private String sProgram = null;
	
	/** the working directory. */
	private File fiWorkDir = null;
	
	/** program parameters. */
	private ArrayList<String> alParams = null;

	/** environment parameters. */
	private HashMap<String, String> hmpEnvParams = null;
	
	/** the running process. */
	private Process proc;
	
	/** Content of output stream. */
	private ProcessStreamReader psrOutput = null;
	
	/** Content of error stream. */
	private ProcessStreamReader psrError = null;
	
	/** the output forwarder. */
	private PrintStream psOutForward = null;

	/** the error forwarder. */
	private PrintStream psErrForward = null;
	
	/** message logger. */
	private ILogger log = null;
	
	/** the listeners. */
	private List<IExecuteListener> listener = null;
	
	/** whether params should be logged. */
	private boolean bParamsLog = true;
	
	/** whether to use system environment parameters. */
	private boolean bUseSystemEnv = false;
	
	/** whether send is disabled. */
	private boolean bSendDisabled = false;
	
	/** the output buffer character count. */
	private int iBufferCharCount = 4000;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the program to launch.
	 * 
	 * @param pProgram program path
	 */
	public void setProgram(String pProgram)
	{
		this.sProgram = pProgram;
	}
	
	/**
	 * Gets the program to launch.
	 * 
	 * @return program path
	 */
	public String getProgram()
	{
		return sProgram;
	}
	
	/**
	 * Adds a launch parameter.
	 * 
	 * @param pParam parameter
	 */
	public void addParameter(String pParam)
	{
		if (alParams == null)
		{
			alParams = new ArrayList<String>();
		}
		
		alParams.add(pParam);
	}
	
	/**
	 * Removes a launch parameter.
	 * 
	 * @param pParam parameter
	 */
	public void removeParameter(String pParam)
	{
		if (alParams == null)
		{
			alParams = new ArrayList<String>();
		}
		
		alParams.remove(pParam);
	}
	
	/**
	 * Clears all parameters.
	 */
	public void clearParameters()
	{
		alParams = null;
	}
	
	/**
	 * Adds an environment parameter.
	 * 
	 * @param pName the parameter name
	 * @param pValue the value
	 */
	public void addEnvironmentParameter(String pName, String pValue)
	{
		if (hmpEnvParams == null)
		{
			hmpEnvParams = new HashMap<String, String>();
		}
		
		hmpEnvParams.put(pName, pValue);
	}	
	
	/**
	 * Sets whether the default environment parameters should be used.
	 * 
	 * @param pUseSystemEnv <code>true</code> to use default environment parameters, <code>false</code> to
	 *                      use custom parameters (if set)
	 */
	public void setUseSystemEnvironmentParameter(boolean pUseSystemEnv)
	{
	    bUseSystemEnv = pUseSystemEnv;
	}
	
	/**
	 * Gets whether the default environment parameters should be used.
	 * 
	 * @return <code>true</code> if default environment parameters will be merged with custom parameters (if set),
	 *         <code>false</code> if only custom parameters will be used (if set).
	 */
	public boolean isUseSystemEnvironmentParameter()
	{
	    return bUseSystemEnv;
	}
	
	/**
	 * Gets the environment parameters.
	 * 
	 * @return the list of environment parameters
	 */
	private String[] getEnvironmentParameters()
	{
	    if (hmpEnvParams == null)
	    {
	        return null;
	    }
	    
	    Map<String, String> hmpParams;
	    
	    if (bUseSystemEnv)
	    {
	        ProcessBuilder pb = new ProcessBuilder("command");

	        hmpParams = pb.environment();
	        
	        //merge user-defined with default environment parameters
	        for (Entry<String, String> entry : hmpEnvParams.entrySet())
	        {
	            hmpParams.put(entry.getKey(), entry.getValue());
	        }
	    }
	    else
	    {
	        hmpParams = hmpEnvParams;
	    }
	    
	    //create parameter list
	    
	    String[] saParams = new String[hmpParams.size()];

	    int i = 0;
	    
	    for (Entry<String, String> entry : hmpParams.entrySet())
	    {
	        saParams[i++] = entry.getKey() + "=" + CommonUtil.nvl(entry.getValue(), "");
	    }
		
		return saParams;
	}
	
	/**
	 * Get the complete launch command.
	 * 
	 * @return program with parameters
	 */
	private String[] getCommand()
	{
		ArrayUtil<String> liCommand = new ArrayUtil<String>();

		String sProg = getProgram();
		
		liCommand.add(sProg);
		
		//concat parameter
		if (alParams != null)
		{
			for (String sParam : alParams)
			{
				if (sParam == null)
				{
					liCommand.add(" ");
				}
				else
				{
					liCommand.add(sParam);
				}
			}
		}
		
		return liCommand.toArray(new String[liCommand.size()]);
	}

	/**
	 * Launches the given command in a new process.
	 * 
	 * @param pWait <code>true</code> to wait until process is finished, <code>false</code> to continue
	 * @return if waiting is enabled, returns <code>true</code> if the exit code is <code>0</code>.
	 *         If waiting is disabled, returns <code>true</code> if the process was started. 
	 * @throws Exception if it's not possible to launch the given command
	 */
	public boolean execute(boolean pWait) throws Exception
	{
		destroy();
		
		String[] saCommand = getCommand();
		
		//sometimes very important e.g. if the command contains username and password
		if (log == null)
		{
			log = LoggerFactory.getInstance(getClass());
		}
		
		Runtime run = Runtime.getRuntime();
		proc = null;
		
		try
		{
			if (bParamsLog)
			{
				log.info((Object)saCommand);
			}
			else
			{
				log.info(getProgram());
			}
			
			if (fiWorkDir == null)
			{
				proc = run.exec(saCommand, getEnvironmentParameters());
			}
			else
			{
				proc = run.exec(saCommand, getEnvironmentParameters(), fiWorkDir);
			}
			
			if (bSendDisabled)
			{
			    proc.getOutputStream().close();
			}
			
			psrOutput = new ProcessStreamReader(proc.getInputStream(), psOutForward);
			psrOutput.iCharCount = iBufferCharCount;
			
			psrError = new ProcessStreamReader(proc.getErrorStream(), psErrForward);
			psrError.iCharCount = iBufferCharCount;
			
			if (pWait)
			{
				int iResult = proc.waitFor();
				
				if (bParamsLog)
				{
					log.debug(saCommand, 
							  "\n:: OUTPUT ::\n~~~~~~~~~~~~\n",
							  getOutput(),
							  "\n:: TYPE_ERROR  ::\n~~~~~~~~~~~~\n", 
							  getError());
				}
				else
				{
					log.debug(getProgram(), 
							  "\n:: OUTPUT ::\n~~~~~~~~~~~~\n",
							  getOutput(),
							  "\n:: TYPE_ERROR  ::\n~~~~~~~~~~~~\n", 
							  getError());
				}
				
				return iResult == 0;
			}
			else
			{
				ThreadHandler.start(new Runnable()
				{
					public void run()
					{
						try
						{
							proc.waitFor();
						}
						catch (InterruptedException ie)
						{
							//nothing to be done
						}
						finally
						{
							destroy();
						}
					}
				});
				
				return true;
			}
		}
		finally
		{
			if (pWait)
			{
				destroy();
			}
		}
	}
	
	/**
	 * Destroyes the current process.
	 */
	public void destroy()
	{
		//DON'T set stream readers to null because it is not possible to get the results out
		if (psrOutput != null)
		{
			psrOutput.terminate();
		}
		
		if (psrError != null)
		{
			psrError.terminate();
		}
		
		if (proc != null)
		{
			proc.destroy();
			proc = null;
		}
		
		if (listener != null)
		{
			for (IExecuteListener lis : listener)
			{
			    try
			    {
			        lis.destroyed(this);
			    }
			    catch (Throwable th)
			    {
			        log.debug(th);
			    }
			}
		}
	}
	
	/**
	 * Gets the data of error stream from last exection.
	 * 
	 * @return null if {@link #execute(boolean)} method was not called, otherwise the data of
	 *         the error stream
	 */
	public String getError()
	{
		if (psrError != null)
		{
			return psrError.getResult();
		}
		
		return null;
	}
	
	/**
	 * Gets the data of output stream from last execution.
	 * 
	 * @return null if {@link #execute(boolean)} method was not called, otherwise the data of
	 *         the o stream
	 */
	public String getOutput()
	{
		if (psrOutput != null)
		{
			return psrOutput.getResult();
		}
		
		return null;
	}
	
    /**
     * Gets the data of output stream from last exectiona and clears the output afterwards.
     * 
     * @return null if {@link #execute(boolean)} method was not called, otherwise the data of
     *         the o stream
     */
	public String getAndClearOutput()
	{
        if (psrOutput != null)
        {
            return psrOutput.getAndClearResult();
        }
        
        return null;
	}
	
	/**
	 * Clears the output.
	 */
	public void clearOutput()
	{
	    if (psrOutput != null)
	    {
	        psrOutput.clear();
	    }
	}
	
	/**
	 * Sets the working directory.
	 * 
	 * @param pWorkDir the directory
	 */
	public void setWorkingDirectory(File pWorkDir)
	{
		fiWorkDir = pWorkDir;
	}
	
	/**
	 * Gets the working directory, if set.
	 * 
	 * @return the working directory or <code>null</code> if no directory is set
	 */
	public File getWorkingDirectory()
	{
		return fiWorkDir;
	}
	
	/**
	 * Sets the forwarder for standard output.
	 * 
	 * @param pOutput the stream
	 */
	public void setOutputForwarder(PrintStream pOutput)
	{
		psOutForward = pOutput;
	}

	/**
	 * Gets the forwarder for standard output.
	 * 
	 * @return the stream or <code>null</code> if no stream is set
	 */
	public PrintStream getOutputForwarder()
	{
		return psOutForward;
	}
	
	/**
	 * Sets the forwarder for error messages.
	 *  
	 * @param pError the stream
	 */
	public void setErrorForwarder(PrintStream pError)
	{
		psErrForward = pError;
	}
	
	/**
	 * Gets the forwarder for error messages.
	 * 
	 * @return the stream or <code>null</code> if no stream is set
	 */
	public PrintStream getErrorForwarder()
	{
		return psErrForward;
	}

	/**
	 * Adds an execution listener.
	 * 
	 * @param pListener the listener
	 */
	public void addListener(IExecuteListener pListener)
	{
		if (listener == null)
		{
			listener = new ArrayUtil<IExecuteListener>();
		}
		
		listener.add(pListener);
	}
	
	/**
	 * Removes an execution listener.
	 * 
	 * @param pListener the listener
	 */
	public void removeListener(IExecuteListener pListener)
	{
		if (listener != null)
		{
			listener.remove(pListener);
			
			if (listener.size() == 0)
			{
				listener = null;
			}
		}
	}
	
	/**
	 * Sends the given data to the executed program.
	 * 
	 * @param pData the data
	 * @throws IOException if program is not executed or a transmission error occurs
	 */
	public void send(byte[] pData) throws IOException
	{
	    if (bSendDisabled)
	    {
	        throw new IOException("Can't send data because noOutput option was set!");
	    }
	    
	    if (proc != null)
	    {
			OutputStream out = proc.getOutputStream();
	
			out.write(pData);
			out.flush();
	    }
	}
	
	/**
	 * Gets whether the program is still running.
	 * 
	 * @return <code>true</code> if the program is executed and still running, <code>false</code> otherwise
	 */
	public boolean isRunning()
	{
		try
		{
			if (proc != null)
			{
				proc.exitValue();
			}
			
			return false;
		}
		catch (IllegalThreadStateException itse)
		{
			return true;
		}
	}
	
	/**
	 * Sets that parameter logging should be en- or disabled. This is useful if logging is
	 * generally enabled, but you want to hide the parameters e.g. if the confidental data is
	 * included.
	 * 
	 * @param pLog <code>true</code> to turn off logging, <code>false</code> to use logging
	 */
	public void setParamsLogEnabled(boolean pLog)
	{
		bParamsLog = pLog;
	}
	
	/**
	 * Gets whether parameter logging is enabled.
	 * 
	 * @return <code>true</code> if parameter logging is enabled, <code>false</code> if parameter logging is disabled 
	 */
	public boolean isParamsLogEnabled()
	{
		return bParamsLog;
	}
	
	/**
	 * Sets the output buffer character count.
	 * 
	 * @param pCharCount the number of characters or <code>-1</code> for unlimited characters
	 * @see #getBufferCharacterCount()
	 */
	public void setBufferCharacterCount(int pCharCount)
	{
		iBufferCharCount = pCharCount;
	}
	
	/**
	 * Gets the output buffer character count (default: 4000).
	 * 
	 * @return the number of characters or <code>-1</code> for unlimited characters
	 */
	public int getBufferCharacterCount()
	{
		return iBufferCharCount;
	}
	
	/**
	 * Disables send feature. This call will close the output stream of the process,
	 * if already executing.
	 */
	public void disableSend()
	{
	    bSendDisabled = true;
	    
	    if (proc != null)
	    {
	        try
	        {
	            proc.getOutputStream().close();
	        }
	        catch (Throwable th)
	        {
	            //ignore
	        }
	    }
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * Reads the content from an <code>InputStream</code>.
	 */
	static class ProcessStreamReader implements Runnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
		/** <code>InputStream</code> readable data. */
		private InputStream isIn = null;
		
		/** Thread to read the InputStream. */
		private Thread thRead = null;
		
		/** Read result. */
		private StringBuffer sbResult;
		
		/** the forwader stream. */
		private PrintStream psForwarder;

		/** the buffer character count. */
		private int iCharCount = -1;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>ProcessStreamReader</code>.
		 * 
		 * @param pIn any <code>InputStream</code>
		 * @param pForwarder a forwarder for stream output
		 */
		ProcessStreamReader(InputStream pIn, PrintStream pForwarder)
		{
			this.isIn = pIn;

			thRead = ThreadHandler.start(this);
			
			psForwarder = pForwarder;
			
			sbResult = new StringBuffer();
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			byte[] byData = new byte[4096];
			
			int iLen;
			
			try
			{
				String sData;
				
				while ((iLen = isIn.read(byData)) >= 0)
				{
					sData = new String(byData, 0, iLen);
					
					if (psForwarder != null)
					{
						psForwarder.append(sData);
					}

					if (iCharCount > 0 && sbResult.length() > iCharCount)
					{
						sbResult = new StringBuffer();
					}
					
					sbResult.append(sData);
				}
			}
			catch (Exception e)
			{
				//egal
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Stop reading from <code>InputStream</code>.
		 */
		void terminate()
		{
			thRead = ThreadHandler.stop(thRead);
		}
	
		/**
		 * Gets the read <code>InputStream</code> data.
		 *  
		 * @return data or <code>null</code> if a forwarder is used
		 */
		public String getResult()
		{
			return sbResult.toString();
		}
		
		/**
		 * Gets the read <code>InputStream</code> data and clears the buffer.
		 * 
		 * @return data or <code>null</code> if a forwarder is used
		 */
		public String getAndClearResult()
		{
		    synchronized (sbResult)
		    {
    		    String sResult = sbResult.toString();
    		    
    		    sbResult.setLength(0);
    		    
    		    return sResult;
		    }
		}
		
		/**
		 * Cleas the buffer.
		 */
		public void clear()
		{
		    sbResult.setLength(0);
		}
		
	}	// ProcessStreamReader
	
}	// Execute

