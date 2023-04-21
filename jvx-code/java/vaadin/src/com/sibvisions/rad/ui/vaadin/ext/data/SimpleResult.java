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
 * 17.10.2017 - [RZ] - Creation
 */
package com.sibvisions.rad.ui.vaadin.ext.data;

import java.util.Optional;

import com.vaadin.data.Result;
import com.vaadin.server.SerializableConsumer;
import com.vaadin.server.SerializableFunction;

/**
 * The {@link SimpleResult} is an immutable {@link Result} implementation which
 * provides the basic functioanlity required.
 * 
 * @author Robert Zenz
 * @param <R> the type of the result.
 */
public class SimpleResult<R> implements Result<R>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The error message. */
	private Optional<String> errorMessage = null;
	
	/** The result value. */
	private R value = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link SimpleResult}.
	 *
	 * @param pValue the value.
	 */
	public SimpleResult(R pValue)
	{
		super();
		
		value = pValue;
		errorMessage = Optional.empty();
	}
	
	/**
	 * Creates a new instance of {@link SimpleResult}.
	 *
	 * @param pErrorMessage the error message.
	 */
	public SimpleResult(String pErrorMessage)
	{
		super();
		
		errorMessage = Optional.of(pErrorMessage);
	}
	
	/**
	 * Creates a new instance of {@link SimpleResult}.
	 *
	 * @param pException the {@link Throwable exception} from which to get the
	 *            error message.
	 */
	public SimpleResult(Throwable pException)
	{
		this(pException.getMessage());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <S> Result<S> flatMap(SerializableFunction<R, Result<S>> pMapper)
	{
		if (!isError())
		{
			return pMapper.apply(value);
		}
		else
		{
			return new SimpleResult<>(errorMessage.get());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getMessage()
	{
		return errorMessage;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X extends Throwable> R getOrThrow(SerializableFunction<String, ? extends X> pExceptionProvider) throws X
	{
		if (!isError())
		{
			return value;
		}
		else
		{
			throw pExceptionProvider.apply(errorMessage.get());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(SerializableConsumer<R> pIfOk, SerializableConsumer<String> pIfError)
	{
		if (!isError())
		{
			pIfOk.accept(value);
		}
		else
		{
			pIfError.accept(errorMessage.get());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isError()
	{
		return errorMessage.isPresent();
	}
	
}	// SimpleResult
