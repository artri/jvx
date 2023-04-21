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
 * 14.11.2014 - [JR] - creation
 */
package com.sibvisions.rad.server.protocol;

/**
 * The <code>ProtocolFactory</code> is responsible for an {@link IProtocolWriter}. It defines the
 * creation of the writer instance, via {@link #createWriter()}. 
 * The factory itself doesn't create a writer, because it's abstract and delegates the creation to 
 * a concrete implementation, because creating protocols may depend on OS or runtime environment.
 * 
 * @author René Jahn
 */
public abstract class ProtocolFactory
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the protocol factory implementation. */
    private static ProtocolFactory factory = null;

    /** the protocol writer. */
    private static ThreadLocal<IProtocolWriter> thlWriter = null;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor, because the <code>ProtocolFactory</code> is a utility class.
     */
    protected ProtocolFactory()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of {@link IProtocolWriter}.
     * 
     * @return the writer 
     */
    public abstract IProtocolWriter createWriter();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates and sets a new instance of {@link ProtocolFactory}. If the <code>pClassName</code> was not
     * found then the factory won't be initialized.
     * 
     * @param pClassName the protocol factory implementation
     */
    public static synchronized void init(String pClassName)
    {
        //don't allow different factories
        if (factory != null) 
        {
            String sClassName;
            
            if (pClassName == null)
            {
                throw new RuntimeException("The protocol factory can't be null!");
            }
            else
            {
                sClassName = pClassName;
            }

            if (!factory.getClass().getName().equals(sClassName))
            {
                throw new RuntimeException("The protocol factory was already initialized!");
            }
            
            //use the current factory!
        }
        else
        {
            try
            {
                factory = (ProtocolFactory)Class.forName(pClassName).newInstance();
            }
            catch (Exception e)
            {
                //factory was not found
            }
        }
    }
    
    /**
     * Gets the current factory instance.
     * 
     * @return the current factory instnance or <code>null</code> if no instance was initialized
     * @see #init(String)
     */
    public static synchronized ProtocolFactory getInstance()
    {
        return factory;
    }
    
    /**
     * Construct (if necessary) and return an {@link IProtocolWriter} instance, using the factory's
     * current set of configuration attributes.
     * 
     * @return the writer or <code>null</code> if factory was not initialized
     * @see #init(String)
     */
    public static synchronized IProtocolWriter getWriter()
    {
        if (factory == null)
        {
            return null;
        }
        
        if (thlWriter == null)
        {
            IProtocolWriter writer = factory.createWriter(); 
            
            thlWriter = new ThreadLocal<IProtocolWriter>();
            thlWriter.set(writer);
            
            return writer;
        }
        else
        {
            IProtocolWriter writer = thlWriter.get();
            
            if (writer == null)
            {
                writer = factory.createWriter();
                thlWriter.set(writer);
            }
            
            return writer;
        }
    }    

    /**
     * Opens a protocol record via {@link #getWriter()}.
     * 
     * @param pCommand the command
     * @param pParameter additional parameter
     * @return the record
     */
    public static Record openRecord(String pCommand, Object... pParameter)
    {
        return openRecord(ICategoryConstants.UNDEFINED, pCommand, pParameter);
    }    
    
    /**
     * Opens a protocol record via {@link #getWriter()}.
     * 
     * @param pCategory the protocol category
     * @param pCommand the command
     * @param pParameter additional parameter
     * @return the record
     */
    public static Record openRecord(String pCategory, String pCommand, Object... pParameter)
    {
        IProtocolWriter writer = getWriter();
        
        if (writer == null)
        {
            return null;
        }
        
        Record record = writer.openRecord(pCategory, pCommand);
        
        if (record != null)
        {
            record.setParameter(pParameter);
        }
        
        return record;
    }
    
}   // ProtocolFactory
