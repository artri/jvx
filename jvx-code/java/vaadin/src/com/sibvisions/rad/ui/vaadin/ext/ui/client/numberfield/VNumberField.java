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
 * 08.04.2014 - [TK] - creation
 * 02.03.2015 - [JR] - precision, scale support
 * 24.03.2015 - [JR] - #1336: fixed input if "unlimited"
 * 11.11.2015 - [JR] - #1506: check invalid number on key press
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client.numberfield;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VTextField;

/**
 * The client-side (GWT) class that will render the widget {@link com.sibvisions.rad.ui.vaadin.ext.ui.NumberField} in the browser.
 * 
 * @author Thomas Krautinger
 */
public class VNumberField extends VTextField implements KeyPressHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The CSS-Classname. **/
	public static final String CLASSNAME = "v-numberfield";
	
	/** The decimal seperator. */
	private char decimalSeperator = ',';
	
	/** The grouping seperator. */
	private char groupingSeperator = '.';
	
	/** The exponent seperator. */
	private String exponentSeperator = "E";
	
    /** The minus sign. */
    private char minusSign = '-';
    
    /** The last valid value. */
    private String textBeforeEdit = null;

	/** The cursor postion before edit. */
	private int cursorPosBeforeEdit = 0;
	
	/** The selection length before edit. */
	private int selectionLengthBeforeEdit = 0;
	
	/** The scale. */
	private int precision;
	
	/** The precision. */
	private int scale;
	
    /** <code>True</code> if the input of an grouping seperator is allowed, otherwise <code>false</code>. */
    private boolean isGroupingSeperatorAllowed = true;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>VNumberField</code>.
	 */
	public VNumberField()
	{
		super();
		
		setStyleName(CLASSNAME);
		addStyleName(VTextField.CLASSNAME);
		setAlignment(TextAlignment.RIGHT);
		
		addKeyPressHandler(this);
		
		sinkEvents(Event.ONPASTE);
	}
	
//    native static void consoleLog(String message) /*-{
//      try {
//        console.log(message);
//      } catch (e) {
//      }
//    }-*/;    

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void onBrowserEvent(Event pEvent)
	{
		if (pEvent.getTypeInt() == Event.ONPASTE)
		{	
		    textBeforeEdit = getText();
			cursorPosBeforeEdit = getCursorPos();
			selectionLengthBeforeEdit = getSelectionLength();
	
			Scheduler.get().scheduleDeferred(new ScheduledCommand()
			{
                @Override
                public void execute() 
                {
                    if (!isNumericValue(getText()))
                    {
                        setText(textBeforeEdit);
                        setSelectionRange(cursorPosBeforeEdit, selectionLengthBeforeEdit);
                    }
                }
            });
		}
		else
		{
			super.onBrowserEvent(pEvent);
		}
	}
	
    /**
	 * {@inheritDoc}
	 */
	@Override
	public void onKeyPress(KeyPressEvent pEvent) 
	{
		boolean consumeEvent = false;

		if (isReadOnly()
			|| !isEnabled())
		{
			consumeEvent = true;
		}
		else if (!isSpecialKeyPress(pEvent)
				 && !pEvent.isAltKeyDown()
				 && !pEvent.isControlKeyDown())
		{
		    
		    String text = getText();
			int cursorIndex = getCursorPos();
			int selectionLength = getSelectionLength();
			
			char ch = pEvent.getCharCode();
			
			if (ch == '-' && !text.substring(0, cursorIndex).endsWith(exponentSeperator))
			{
			    if (text.length() > 0 && text.charAt(0) == minusSign)
			    {
			        text = text.substring(1);
			    }
			    else
			    {
			        text = minusSign + text;
			    }
			    setText(text);

			    consumeEvent = true;
			}
			else
			{
    			if (selectionLength > 0)
    			{
    				text = text.substring(0, cursorIndex) + ch + text.substring(cursorIndex + selectionLength, text.length());
    			}
    			else
    			{
    				text = text.substring(0, cursorIndex) + ch + text.substring(cursorIndex, text.length());
    			}
                
                consumeEvent = !isNumericValue(text);
			}
		}
		
		if (consumeEvent)
		{			
			pEvent.preventDefault();
			pEvent.stopPropagation();
			cancelKey();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the decimal seperator.
	 * 
	 * @return the decimal seperator.
	 */
	public char getDecimalSeperator()
	{
		return decimalSeperator;
	}

	/**
	 * Sets the decimal seperator. 
	 * 
	 * @param pDecimalSeperator the decimal seperator.
	 */
	public void setDecimalSeperator(char pDecimalSeperator)
	{
		decimalSeperator = pDecimalSeperator;
	}

	/**
	 * Gets the grouping seperator.
	 * 
	 * @return the grouping seperator.
	 */
	public char getGroupingSeperator()
	{
		return groupingSeperator;
	}

	/**
	 * Sets the grouping seperator.
	 * 
	 * @param pGroupingSeperator the grouping seperator.
	 */
	public void setGroupingSeperator(char pGroupingSeperator)
	{
		groupingSeperator = pGroupingSeperator;
	}

	/**
	 * Gets the exponent seperator.
	 * 
	 * @return the exponent seperator.
	 */
	public String getExponentSeperator()
	{
		return exponentSeperator;
	}

	/**
	 * Sets the exponent seperator.
	 * 
	 * @param pExponentSeperator the exponent seperator.
	 */
	public void setExponentSeperator(String pExponentSeperator)
	{
		exponentSeperator = pExponentSeperator;
	}

	/**
	 * Gets the minus sign.
	 * 
	 * @return the minus sign.
	 */
	public char getMinusSign()
	{
		return minusSign;
	}

	/**
	 * Sets the minus sign.
	 * 
	 * @param pMinusSign the minus sign.
	 */
	public void setMinusSign(char pMinusSign)
	{
		minusSign = pMinusSign;
	}

	/**
	 * Gets whether, the input of the grouping seperator is allowed. 
	 * 
	 * @return <code>True</code> if the input of the grouping seperator is allowed, otherwise <code>false</code>
	 */
	public boolean isGroupingSeperatorAllowed()
	{
		return isGroupingSeperatorAllowed;
	}

	/**
	 * Sets whether, the input of the grouping seperator is allowed. 
	 * 
	 * @param pIsGroupingSeperatorAllowed <code>True</code> if the input of the grouping seperator is allowed, otherwise <code>false</code>
	 */
	public void setGroupingSeperatorAllowed(boolean pIsGroupingSeperatorAllowed)
	{
		isGroupingSeperatorAllowed = pIsGroupingSeperatorAllowed;
	}
	
	/**
	 * Validates whether the pressed key was a special key.
	 * 
	 * @param pKeyEvent the key event.
	 * 
	 * @return <code>True</code> if an special key was pressed, otherwise <code>false</code>.
	 */
	private boolean isSpecialKeyPress(KeyEvent pKeyEvent)
	{
		int keyCode = pKeyEvent.getNativeEvent().getKeyCode();
		
		if (BrowserInfo.get().isGecko()
			&& keyCode > 0)
		{
			return (keyCode == KeyCodes.KEY_LEFT
					|| keyCode == KeyCodes.KEY_RIGHT
					|| keyCode == KeyCodes.KEY_UP
					|| keyCode == KeyCodes.KEY_DOWN
					|| keyCode == KeyCodes.KEY_HOME
					|| keyCode == KeyCodes.KEY_END
					|| keyCode == KeyCodes.KEY_PAGEUP
					|| keyCode == KeyCodes.KEY_PAGEDOWN
					|| keyCode == KeyCodes.KEY_ESCAPE
					|| keyCode == KeyCodes.KEY_BACKSPACE
					|| keyCode == KeyCodes.KEY_DELETE
					|| keyCode == KeyCodes.KEY_ENTER
					|| keyCode == KeyCodes.KEY_TAB);
		}
		
		return false;
	}
	
	/**
	 * Check if <code>pText</code> contains a valid number.
	 *
	 * @param pText the text to validate.
	 * 
	 * @return <code>True</code> if the text is valid, otherwise <code>false</code>.
	 */
	private boolean isNumericValue(String pText)
	{
		if (pText != null && pText.length() > 0)
		{
		    pText = pText.replace(" ", ""); // remove white space.
		    
		    boolean hasPercent = pText.endsWith("%"); 
		    if (hasPercent)
		    {
		        pText = pText.substring(0, pText.length() - 1);
		    }
		    
		    int exponentSeperatorIndex; 
			
			if (exponentSeperator != null)
			{
			    exponentSeperatorIndex = pText.indexOf(exponentSeperator);
			    
			    if (exponentSeperatorIndex >= 0)
			    {
    			    //number can't start with exponent separator
    	            if (exponentSeperatorIndex == 0)
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
			    
			    if (ch == decimalSeperator)
			    {
			        //only one decimal seperator is allowed
			        if (lastDecimalSeperatorIndex >= 0)
			        {
			            return false;
			        }
			        // no comma after exponent
			        else if (exponentSeperatorIndex >= 0 && i > exponentSeperatorIndex)
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

			// Fixes only one special case
//			//#1336
//			//no precision/scale set
//			if (precision <= 0 && scale < 0)
//			{
//			    return true;
//			}
//			
			//count digits
			
            int iFoundPrecision = 0;
            int iFoundScale = 0;
    
            for (int i = 0, cnt = pText.length(); i < cnt; i++)
            {
                ch = pText.charAt(i);
                
                if (ch >= '0' && ch <= '9')
                {
                    if ((lastDecimalSeperatorIndex == -1 || i < lastDecimalSeperatorIndex)
                            && (exponentSeperatorIndex == -1 || i < exponentSeperatorIndex))
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
			        
			        iFoundPrecision = Math.max(0,  iFoundPrecision + iDigits);
			        iFoundScale = Math.max(0, iFoundScale - iDigits);
			    }
			    catch (Exception e)
			    {
			        //ignore
			    }
			}
			
			if (hasPercent)
			{
                iFoundPrecision = Math.max(0,  iFoundPrecision - 2);
                iFoundScale = Math.max(0, iFoundScale + 2);
			}
			
			if ((precision <= 0 || iFoundPrecision <= precision - Math.max(iFoundScale, scale)) // identical check to Swing Editor
		             && (scale < 0 || iFoundScale <= scale))
//			if (iFoundPrecision + iFoundScale <= precision // old check
//			    && iFoundPrecision <= precision && iFoundScale <= scale)
			{
			    return true;
			}
			
			return false;
		}
		
		return true;
	}
	
    /**
     * Gets the precision.
     * 
     * @return the precision
     */
    public int getPrecision()
    {
        return precision;
    }
    
	/**
	 * Sets the precision.
	 * 
	 * @param pPrecision the precision
	 */
	public void setPrecision(int pPrecision)
	{
	    precision = pPrecision;
	}
	
    /**
     * Gets the scale.
     * 
     * @return the scale
     */
    public int getScale()
    {
        return scale;
    }
    
	/**
	 * Sets the scale.
	 * 
	 * @param pScale the scale
	 */
	public void setScale(int pScale)
	{
	    scale = pScale;
	}

}   // VNumberField

