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
 * 18.06.2015 - [JR] - creation
 */
package com.sibvisions.rad.server.http.rest.service.jackson;

import java.io.IOException;
import java.io.InputStream;

import javax.rad.io.IFileHandle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * The <code>FileHandleSerializer</code> writes {@link IFileHandle} as byte[].
 * 
 * @author René Jahn
 */
public class FileHandleSerializer extends JsonSerializer<IFileHandle>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(IFileHandle pFileHandle, JsonGenerator pGenerator, SerializerProvider pProvider) throws IOException,  
                                                                                                                  JsonProcessingException
    {
    	InputStream is = pFileHandle.getInputStream();
    	
    	if (is != null)
    	{
    		pGenerator.writeBinary(is, (int)pFileHandle.getLength());
    	}
    	else
    	{
    		pGenerator.writeNull();
    	}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public Class<IFileHandle> handledType()
    {
        return IFileHandle.class;
    }    
    
}   // FileHandleSerializer
