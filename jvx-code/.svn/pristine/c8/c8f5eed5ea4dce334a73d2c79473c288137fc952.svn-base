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
 * 20.01.2010 - [HM] - creation
 * 29.04.2010 - [JR] - #119: init implemented
 * 01.05.2010 - [JR] - #119: TypeCache used instead of reset()
 */
package com.sibvisions.rad.remote.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.sibvisions.rad.remote.UniversalSerializer;

/**
 * The <code>ITypeSerializer</code> interface defines standard methods for serialization
 * and deserialization of a specific type.
 * 
 * @param <T> the type
 *  
 * @author Martin Handsteiner
 */
public interface ITypeSerializer<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the type class that can be serialized by this serializer.
	 * 
	 * @return the type class.
	 */
	public Class<T> getTypeClass();
	
	/**
	 * Gets the minimal value used to detect this type in stream.
	 * 
	 * @return the minimal value used to detect this type in stream.
	 */
	public int getMinValue();
	
	/**
	 * Gets the maximal value used to detect this type in stream.
	 * 
	 * @return the maximal value used to detect this type in stream.
	 */
	public int getMaxValue();
	
	/**
	 * Reads in a serialized object from a stream.
	 * 
	 * @param pSerializer the universal serializer.
	 * @param pIn stream with serialized content
	 * @param pTypeValue type value.
	 * @param pCache the cache for {@link javax.rad.type.bean.BeanType} definitions
	 * @return deserialized object
	 * @throws Exception if deserialization fails
	 */
	public T read(UniversalSerializer pSerializer, DataInputStream pIn, int pTypeValue, TypeCache pCache) throws Exception;
	
	/**
	 * Writes a serialized object to a stream.
	 * 
	 * @param pSerializer the universal serializer.
	 * @param pOut output stream for the object
	 * @param pObject serializable object
	 * @param pCache the cache for {@link javax.rad.type.bean.BeanType} definitions
	 * @throws Exception if serialization fails
	 */
	public void write(UniversalSerializer pSerializer, DataOutputStream pOut, T pObject, TypeCache pCache) throws Exception;
	
}	// ITypeSerializer
