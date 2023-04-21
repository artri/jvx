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
 * 11.11.2015 - [JR] - creation
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.numberfield;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests functionality of {@link VNumberField}.
 * 
 * @author René Jahn
 */
public class TestVNumberField
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** The decimal seperator. */
    private char decimalSeperator = ',';
    
    /** The grouping seperator. */
    private char groupingSeperator = '.';
    
    /** The exponent seperator. */
    private String exponentSeperator = "E";

    /** The minus sign. */
    private char minusSign = '-';
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests {@link #isNumericValue(String, int, int)}.
     */
    @Test
    public void testIsNumeric()
    {
        Assert.assertFalse(isNumericValue("m", 2, 0));
        Assert.assertTrue(isNumericValue("12", 3, 0));
        Assert.assertTrue(isNumericValue("12.0", 4, 1));
        Assert.assertTrue(isNumericValue("0.55", 4, 2));
    }
    
    /**
     * This method contains a copy of isNumericValue of {@link VNumberField}.
     * 
     * @param pText the test
     * @param pPrecision the max precision
     * @param pScale the max scale
     * @return <code>true</code> if text is numeric, <code>false</code> otherwise
     */
    private boolean isNumericValue(String pText, int pPrecision, int pScale)
    {
        if (pText != null && pText.length() > 0)
        {
            int exponentSeperatorIndex; 
            
            if (exponentSeperator != null)
            {
                exponentSeperatorIndex = pText.indexOf(exponentSeperator);
                
                if (exponentSeperatorIndex >= 0)
                {
                    //number can't start or end with exponent separator
                    if (exponentSeperatorIndex == 0 
                        || exponentSeperatorIndex == pText.length() - exponentSeperator.length() - 1)
                    {
                        return false;
                    }
                    
                    //only one exponent is allowed
                    if (pText.indexOf(exponentSeperator, exponentSeperatorIndex + exponentSeperator.length()) >= 0)
                    {
                        return false;
                    }
                }
            }
            else
            {
                exponentSeperatorIndex = -1;
            }
            
            int lastDecimalSeperatorIndex = -1;
            
            int iFirstMinusAfterIndex = -1;
            
            int iMinusBefore = 0;
            int iMinusAfter = 0;
            
            char ch;
            
            //check if number only contains valid characters and remember decimal separator position
            //also count minus signs and check grouping separator position
            for (int i = 0, cnt = pText.length(); i < cnt; i++)
            {
                ch = pText.charAt(i);
                
                if (ch == decimalSeperator && (exponentSeperatorIndex == -1 || i < exponentSeperatorIndex))
                {
                    //only one decimal seperator is allowed
                    if (lastDecimalSeperatorIndex >= 0)
                    {
                        return false;
                    }
                    
                    lastDecimalSeperatorIndex = i;
                }
                
                if ((ch < '0' || ch > '9') && ch != minusSign && ch != groupingSeperator && ch != decimalSeperator)
                {
                    //"invalid" character must be one of the exponent characters
                    if (exponentSeperatorIndex == -1 
                        || (i < exponentSeperatorIndex && i >= exponentSeperatorIndex + exponentSeperator.length()))
                    {
                        return false;
                    }
                }

                //count minus signs before and after exponent
                if (ch == minusSign)
                {
                    if (exponentSeperatorIndex == -1 || i < exponentSeperatorIndex)
                    {
                        iMinusBefore++;
                    }
                    else
                    {
                        iMinusAfter++;
                        
                        if (iFirstMinusAfterIndex == -1)
                        {
                            iFirstMinusAfterIndex = i;
                        }
                    }
                }
                
                //no grouping after decimal or exponent
                if (ch == groupingSeperator)
                {
                    if ((lastDecimalSeperatorIndex >= 0 && i >= lastDecimalSeperatorIndex)
                         || (exponentSeperatorIndex >= 0 && i >= exponentSeperatorIndex + exponentSeperator.length()))
                    {
                        return false;
                    }
                }
            }

            //minus validation
            if (iMinusBefore > 1 || iMinusAfter > 1)
            {
                return false;
            }
            
            //minus after exponent: must be first character
            if (exponentSeperatorIndex >= 0 
                && iFirstMinusAfterIndex != -1 && iFirstMinusAfterIndex > exponentSeperatorIndex + exponentSeperator.length())
            {
                return false;
            }

            //unlimited input check
            
            //#1336
            //no precision/scale set
            if (pPrecision <= 0 && pScale < 0)
            {
                return true;
            }
            
            //count digits
            
            int iFoundPrecision = 0;
            int iFoundScale = 0;
    
            for (int i = 0, cnt = pText.length(); i < cnt; i++)
            {
                ch = pText.charAt(i);
                
                if (ch >= '0' && ch <= '9')
                {
                    if (lastDecimalSeperatorIndex == -1 || i < lastDecimalSeperatorIndex)
                    {
                        iFoundPrecision++;
                    }
                    else if (i > lastDecimalSeperatorIndex && (exponentSeperatorIndex == -1 || i < exponentSeperatorIndex))
                    {
                        iFoundScale++;
                    }
                }           
            }
            
            if (exponentSeperatorIndex > 0)
            {
                try
                {
                    int iDigits = Integer.parseInt(pText.substring(exponentSeperatorIndex + exponentSeperator.length()));
                    
                    if (iFoundScale > 0)
                    {
                        iFoundPrecision += iDigits;

                        if (iFoundScale >= iDigits)
                        {
                            iFoundScale -= iDigits;
                        }
                        else
                        {
                            iFoundScale = 0;
                        }
                    }
                }
                catch (Exception e)
                {
                    //ignore
                }
            }
            
            if (iFoundPrecision + iFoundScale <= pPrecision 
                && iFoundPrecision <= pPrecision && iFoundScale <= pScale)
            {
                return true;
            }
            
            return false;
        }
        
        return true;
    }
    
}   // TestVNumberField
