/*
 * Copyright 2016 SIB Visions GmbH
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
 * 23.06.2016 - [JR] - creation
 */
package com.sibvisions.rad.remote;

import javax.rad.remote.event.CallBackResultEvent;
import javax.rad.remote.event.ICallBackResultListener;

import com.sibvisions.util.ArrayUtil;

/**
 * The <code>CallBackResultCollectionListener</code> is a simple {@link ICallBackResultListener} that
 * collects all callback result objects with instruction <code>INT_VALUE</code>.
 * 
 * @author René Jahn
 */
public class CallBackResultCollectionListener implements ICallBackResultListener
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the value list. */
    private ArrayUtil<Integer> list = new ArrayUtil<Integer>();

    /** the instruction which should be collected. */
    private String sInstruction;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a new instance of <code>CallBackResultCollectionListener</code>.
     * 
     * @param pInstruction the instruction
     */
    public CallBackResultCollectionListener(String pInstruction)
    {
        sInstruction = pInstruction;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void callBackResult(CallBackResultEvent pEvent) throws Throwable
    {
        if (sInstruction.equals(pEvent.getInstruction()))
        {
            list.add((Integer)pEvent.getObject());
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the value list.
     * 
     * @return the list
     */
    public ArrayUtil<Integer> getList()
    {
        return list;
    }
    
}  // CallBackResultCollectionListener
