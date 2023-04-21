/*
 * Copyright 2014 SIB Visions GmbH
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
 * 19.11.2014 - [JR] - creation
 * 26.01.2015 - [JR] - #1238: Exception property added
 * 08.11.2017 - [JR] - toString implemented  
 */
package com.sibvisions.rad.server.protocol;

import java.util.concurrent.atomic.AtomicInteger;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.ICloseable;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Record</code> contains information for writing via {@link IProtocolWriter}.
 * 
 * @author René Jahn
 */
public class Record implements ICloseable
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the ID prefix. */
    private static final String PREFIX_ID = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
    
    /** the record sequence. */
    private static AtomicInteger aiSequence = new AtomicInteger(0);
    
    /** the protocol writer. */
    private IProtocolWriter writer;
    
    /** the UUID. */
    protected String sUUID;
    
    /** the category. */
    protected String sCategory;
    
    /** the command. */
    protected String sCommand;

    /** the identifier. */
    protected Object[] oIdentifier;
    
    /** the parameter. */
    protected Object[] oParameter;
    
    /** an exception. */
    protected Throwable exception;
    
    /** the creation time in nanos. */
    protected long lCreationNano;
    
    /** the creation time in milliseconds. */
    protected long lCreation;
    
    /** the end time in nanos (default: -1, means undefined). */
    protected long lDuration = -1;
    
    /** the initial memory. */
    protected long lMemoryInitial;
    
    /** the memory consumption. */
    protected long lMemoryConsumption = -1;

    /** the count. */
    protected int iCount;
    
    /** whether we are closing. */
    private boolean bClosing;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>Record</code> for given command.
     * 
     * @param pCategory the protocol category
     * @param pCommand the assigned command
     */
    public Record(String pCategory, String pCommand)
    {
        this(null, pCategory, pCommand);
    }

    /**
     * Creates a new instance of <code>Record</code> for given writer and command.
     * 
     * @param pWriter the {@link IProtocolWriter}
     * @param pCategory the protocol category
     * @param pCommand the assigned command
     */
    public Record(IProtocolWriter pWriter, String pCategory, String pCommand)
    {
        writer = pWriter;
        sCategory = pCategory;
        sCommand = pCommand;
        
        lCreationNano = System.nanoTime();
        
        lCreation = System.currentTimeMillis();
        
        Runtime rt = Runtime.getRuntime();
        
        lMemoryInitial = rt.totalMemory() - rt.freeMemory(); 
        
        sUUID = PREFIX_ID + "-" + aiSequence.incrementAndGet();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("UUID = ");
        sb.append(sUUID);
        sb.append(", Category = \"");
        sb.append(sCategory);
        sb.append("\", Command = \"");
        sb.append(sCommand);
        sb.append("\", Identifier = ");
        sb.append(StringUtil.toString(oIdentifier));
        sb.append(", Parameter = ");
        sb.append(StringUtil.toString(oParameter));
        sb.append(", Exception = \"");
        sb.append(ExceptionUtil.dump(exception, true));
        sb.append("\", Creation = ");
        sb.append(lCreation);
        sb.append(", Count = ");
        sb.append(iCount);
        sb.append("}");
        
        return sb.toString();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the Universal Unique Identifier.
     * 
     * @return the UUID
     */
    public String getUUID()
    {
        return sUUID;
    }
    
    /**
     * Gets the protocol category.
     * 
     * @return the category
     */
    public String getCategory()
    {
        return sCategory;
    }
    
    /**
     * Gets the command.
     * 
     * @return the command
     */
    public String getCommand()
    {
        return sCommand;
    }
    
    /**
     * Gets the creation time.
     * 
     * @return the time in milliseconds.
     */
    public long getCreationTime()
    {
        return lCreation;
    }
    
    /**
     * Gets the duration.
     * 
     * @return the duration in nanos
     */
    public long getDuration()
    {
        return lDuration;
    }
    
    /**
     * Sets the parameter.
     * 
     * @param pParameter the parameter
     */
    public void setParameter(Object... pParameter)
    {
        oParameter = pParameter;
    }
    
    /**
     * Gets the parameter.
     * 
     * @return the parameter
     */
    public Object[] getParameter()
    {
        return oParameter;
    }
    
    /**
     * Adds an identifier at the end.
     * 
     * @param pIdentifier the identifier
     */
    public void addIdentifier(Object pIdentifier)
    {
        oIdentifier = ArrayUtil.add(oIdentifier, pIdentifier);
    }
    
    /**
     * Adds an identifier at the given position.
     * 
     * @param pIdentifier the identifier
     * @param pIndex the index/position
     */
    public void addIdentifier(Object pIdentifier, int pIndex)
    {
        oIdentifier = ArrayUtil.add(oIdentifier, pIndex, pIdentifier);
    }
    
    /**
     * Gets the identifier.
     * 
     * @return the identifier
     */
    public Object[] getIdentifier()
    {
        return oIdentifier;
    }
    
    /**
     * Sets the count. The count can be a call or modified objects count.
     * 
     * @param pCount the count
     */
    public void setCount(int pCount)
    {
        iCount = pCount;
    }
    
    /**
     * Gets the count.
     * 
     * @return the count
     */
    public int getCount()
    {
        return iCount;
    }
    
    /**
     * Gets the initial memory.
     * 
     * @return the bytes
     */
    public long getMemoryInitial()
    {
        return lMemoryInitial;
    }
    
    /**
     * Gets the memory consumption.
     * 
     * @return the bytes
     */
    public long getMemoryConsumption()
    {
        return lMemoryConsumption;
    }
    
    /**
     * Sets the exception (if occured).
     * 
     * @param pException the exception
     */
    public void setException(Throwable pException)
    {
        exception = pException;
    }
    
    /**
     * Gets the exception (if occured).
     * 
     * @return the exception
     */
    public Throwable getException()
    {
        return exception;
    }
    
    /**
     * Closes the record.
     */
    public void close()
    {
        //avoid recusion, if close will be called from the writer
        if (!bClosing)
        {
            bClosing = true;
         
            lDuration = System.nanoTime() - lCreationNano;

            Runtime rt = Runtime.getRuntime();
            lMemoryConsumption = rt.totalMemory() - rt.freeMemory() - lMemoryInitial; 
            
            try
            {
                if (writer != null)
                {
                    writer.closeRecord(this);
                }
                else
                {
                    ProtocolFactory.getWriter().closeRecord(this);
                }
            }
            catch (Throwable th)
            {
                LoggerFactory.getInstance(Record.class).error(th);
            }
            finally
            {
                bClosing = false;
            }
        }
    }
    
}   // Record
