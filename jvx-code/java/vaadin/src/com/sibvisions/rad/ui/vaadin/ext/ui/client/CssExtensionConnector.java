/*
 * Copyright 2013 SIB Visions GmbH
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
 * 08.01.2013 - [SW] - creation
 * 11.04.2019 - [DJ] - #1694: method clearPropertyFromStyle fixed, pStyle.replace caused bug
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.ArrayList;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.extension.CssExtension;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * The <code>CssExtensionConnector</code> class is the connector to the 
 * client component.
 * 
 * @author Stefan Wurm
 */
@Connect(CssExtension.class)
public class CssExtensionConnector extends AbstractExtensionConnector
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the connected widget. **/
	private Widget widget;
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Abstract methods implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    protected void extend(ServerConnector pTarget) 
    {
        widget = ((ComponentConnector) pTarget).getWidget();
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    public CssExtensionState getState() 
    {
        return (CssExtensionState)super.getState();
    } 
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void onStateChanged(StateChangeEvent pStateChangeEvent) 
    {
        super.onStateChanged(pStateChangeEvent);

        removeAttributes();
        addAttributes();        
    }    
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    
    /**
     * Adds the Attributes to the DOM. 
     */
    public void addAttributes()
    {
    	com.google.gwt.dom.client.Element element = widget.getElement();
    	ArrayList<com.google.gwt.dom.client.Element> liFoundElements = null;

    	for (CssExtensionAttribute styleAttribute : getState().getAttributes()) 
        {
        	int iDir = styleAttribute.getSearchDirection(); 
        	
        	RegExp regexp = createRegExp(styleAttribute);
        	
        	liFoundElements = null;

        	if (iDir == CssExtensionAttribute.SEARCH_DOWN)
			{
        		liFoundElements = searchElementDown(element, regexp, styleAttribute.isMultipleMatch(), true);
			}
			else if (iDir == CssExtensionAttribute.SEARCH_UP)
			{
				com.google.gwt.dom.client.Element elFound = searchElementUp(element, regexp);
				
				if (elFound != null)
				{
					liFoundElements = new ArrayList<com.google.gwt.dom.client.Element>();
					liFoundElements.add(elFound);
				}
			}
			else if (iDir == CssExtensionAttribute.SEARCH_IN_PARENT)
			{
				liFoundElements = searchInParent(element, regexp, styleAttribute.isMultipleMatch());
			}
			else if (iDir == CssExtensionAttribute.SEARCH_IN_PARENT_PARENT)
			{
			    if (element != null)
			    {
		            com.google.gwt.dom.client.Element parent = element.getParentElement();

		            liFoundElements = searchInParent(parent, regexp, styleAttribute.isMultipleMatch());
			    }
			}
			else if (iDir == CssExtensionAttribute.SELF)
			{
				liFoundElements = new ArrayList<com.google.gwt.dom.client.Element>();
				liFoundElements.add(element);
			}
        	
        	if (liFoundElements != null)
        	{
        		com.google.gwt.dom.client.Element el;
        		
        		for (int i = 0, cnt = liFoundElements.size(); i < cnt; i++)
        		{
        			el = liFoundElements.get(i);
        			
		        	if (styleAttribute.isStyleAttribute())
		        	{	
		        		String domStyle = el.getAttribute("style");
		        		
		        		if (domStyle != null)
		        		{
		        			domStyle = removePropertyFromStyle(domStyle, styleAttribute.getAttribute());
		        			
		        			if (!domStyle.endsWith(";")) // IE 8 has no ; at the end.
		        			{
		        				domStyle += ";";
		        			}
		        		}
		
		        		if (domStyle != null)
		        		{
		        			domStyle += " " + styleAttribute.getAttribute() + ": " + styleAttribute.getValue();
		        		}
		        		else
		        		{
		        			domStyle = styleAttribute.getAttribute() + ": " + styleAttribute.getValue();
		        		}
		        		
		        		if (styleAttribute.isStyleAttributeImportant())
		        		{
		        			domStyle += " !important";
		        		}
		        		
		        		domStyle += ";";
		
		        		el.setAttribute("style", minify(domStyle));
		        	}
		        	else if (styleAttribute.isClassAttribute())
		        	{
		        	    el.addClassName(styleAttribute.getValue());
		        	}
		        	else
		        	{
		        		el.setAttribute(styleAttribute.getAttribute(), styleAttribute.getValue());
		        	}
        		}
        	}
        }      	
    }
    
    /**
     * Searches the element with the class name from the position of the parent element down the dom tree.
     * 
     * @param pElement the start element
     * @param pRegExp the regular expression
     * @param pMultiMatch whether more than one element is possible
     * @param pRootLevel the first level to search
     * @return the element
     */
    public ArrayList<com.google.gwt.dom.client.Element> searchElementDown(com.google.gwt.dom.client.Element pElement, RegExp pRegExp, boolean pMultiMatch, boolean pRootLevel)
    {
    	if (pRegExp != null && pElement != null)
    	{
    		com.google.gwt.dom.client.Element elCurrent;
    		
            ArrayList<com.google.gwt.dom.client.Element> liFound = null;

            for (int i = 0, cnt = pElement.getChildCount(); i < cnt; i++)
			{
				elCurrent = (com.google.gwt.dom.client.Element)pElement.getChild(i);

				if (elCurrent.getClassName() != null 
				    && pRegExp.exec(elCurrent.getClassName()) != null)
				{
					if (liFound == null)
					{
						liFound = new ArrayList<com.google.gwt.dom.client.Element>();
					}
					liFound.add(elCurrent);
					
                	if (!pMultiMatch)
                	{
                		return liFound;
                	}
				}
				else if (liFound == null || !pRootLevel)
				{
				    ArrayList<com.google.gwt.dom.client.Element> liSubFound = searchElementDown(elCurrent, pRegExp, pMultiMatch, false);
				    
				    if (liSubFound != null)
				    {
				        if (liFound == null)
				        {
				            liFound = liSubFound;
				        }
                        else
                        {
                            liFound.addAll(liSubFound);
                        }
				    }
				}
			}
            
            return liFound;
    	}  

    	return null;
    }

    /**
     * Searches the element with the class name from the position of the child element up the dom tree.
     * 
     * @param pElement the start element
     * @param pRegExp the reg exp
     * @return the element
     */
    public com.google.gwt.dom.client.Element searchElementUp(com.google.gwt.dom.client.Element pElement, RegExp pRegExp)
    {
    	if (pRegExp != null && pElement != null)
    	{
    		com.google.gwt.dom.client.Element parent = pElement.getParentElement();
    		
			if (parent != null)
			{
				if (parent.getClassName() != null && pRegExp.exec(parent.getClassName()) != null)
				{
					return parent;
				}

				com.google.gwt.dom.client.Element elCurrent;

				for (int i = 0, cnt = parent.getChildCount(); i < cnt; i++)
				{
					elCurrent = (com.google.gwt.dom.client.Element)parent.getChild(i);
					
					if (elCurrent != pElement 
						&& elCurrent.getClassName() != null 
						&& pRegExp.exec(elCurrent.getClassName()) != null)
					{	
						return elCurrent;
					}
				}
				
				return searchElementUp(parent, pRegExp);
			}
    	}  
    	
    	return null;
    }    
    
    /**
     * Searches the element with the class name in the same parent but not recursive.
     * 
     * @param pElement the start element
     * @param pRegExp the regular expression
     * @param pMultiMatch whether more than one element is possible
     * @return the element
     */
    public ArrayList<com.google.gwt.dom.client.Element> searchInParent(com.google.gwt.dom.client.Element pElement, RegExp pRegExp, boolean pMultiMatch)
    {
        if (pRegExp != null && pElement != null)
        {
            com.google.gwt.dom.client.Element parent = pElement.getParentElement();
            
            if (parent != null)
            {
                com.google.gwt.dom.client.Element elCurrent;
    
                ArrayList<com.google.gwt.dom.client.Element> liFound = null;
                
                for (int i = 0, cnt = parent.getChildCount(); i < cnt; i++)
                {
                    elCurrent = (com.google.gwt.dom.client.Element)parent.getChild(i);
    
                    if (elCurrent != pElement
                        && elCurrent.getClassName() != null 
                        && pRegExp.exec(elCurrent.getClassName()) != null)
                    {
                    	if (liFound == null)
                    	{
                    		liFound = new ArrayList<com.google.gwt.dom.client.Element>();
                    	}
                    	
                    	liFound.add(elCurrent);
                    	
                    	if (!pMultiMatch)
                    	{
                    		return liFound;
                    	}
                    }
                }
                
                if (liFound != null)
                {
                	return liFound;
                }
            }
        }  

        return null;
    }    
 
    /**
     * Removes the attributes from the DOM.
     */
    public void removeAttributes()
    {
    	com.google.gwt.dom.client.Element element = widget.getElement();
    	ArrayList<com.google.gwt.dom.client.Element> liFoundElements = null;
    	
        for (CssExtensionAttribute removedAttribute : getState().getAttributesRemoved()) 
        {
        	liFoundElements = null;
        	
        	int iDir = removedAttribute.getSearchDirection();
        	
        	RegExp regexp = createRegExp(removedAttribute);
        	
			if (iDir == CssExtensionAttribute.SEARCH_DOWN)
			{
				liFoundElements = searchElementDown(element, regexp, removedAttribute.isMultipleMatch(), true);
			}
			else if (iDir == CssExtensionAttribute.SEARCH_UP)
			{
				com.google.gwt.dom.client.Element elFound = searchElementUp(element, regexp);
				
				if (elFound != null)
				{
					liFoundElements = new ArrayList<com.google.gwt.dom.client.Element>();
					liFoundElements.add(elFound);
				}
			}
            else if (iDir == CssExtensionAttribute.SEARCH_IN_PARENT)
            {
                liFoundElements = searchInParent(element, regexp, removedAttribute.isMultipleMatch());
            }
            else if (iDir == CssExtensionAttribute.SEARCH_IN_PARENT_PARENT)
            {
                if (element != null)
                {
                    com.google.gwt.dom.client.Element parent = element.getParentElement();

                    liFoundElements = searchInParent(parent, regexp, removedAttribute.isMultipleMatch());
                }
            }
			else if (iDir == CssExtensionAttribute.SELF)
			{
				liFoundElements = new ArrayList<com.google.gwt.dom.client.Element>();
				liFoundElements.add(element);
			}

        	if (liFoundElements != null)
        	{
        		com.google.gwt.dom.client.Element el;
        		
        		for (int i = 0, cnt = liFoundElements.size(); i < cnt; i++)
        		{
        			el = liFoundElements.get(i);
        			
		        	if (removedAttribute.isStyleAttribute())
		        	{		
		        		String domStyle = el.getAttribute("style");
		
		        		if (domStyle != null)
		        		{
		        			domStyle = removePropertyFromStyle(domStyle, removedAttribute.getAttribute());
	
		        			el.setAttribute("style", minify(domStyle));
		        		}
		        	}
		        	else if (removedAttribute.isClassAttribute())
		        	{
		        	    el.removeClassName(removedAttribute.getValue());
		        	}
		        	else
		        	{
		        		el.removeAttribute(removedAttribute.getAttribute());
		        	}
        		}
			}
        }      	
    }
    
    /**
     * Removes the given property from the given style string.
     * 
     * style = background-color: green; border: 3px; color: blue;
     * propery = border
     * 
     * return value = background-color: green; color: blue;
     * 
     * @param pStyle the style string.
     * @param pProperty the property.
     * @return the style string
     */
    public String removePropertyFromStyle(String pStyle, String pProperty)
    {
    	if (pStyle != null)
    	{
	    	StringBuilder sbStyle = new StringBuilder();
	        
	        String[] values;
	        
	        for (String sEntry : pStyle.split(";"))
	        {
	            values = sEntry.split(":");
	            
	            if (!values[0].trim().equals(pProperty))
	            {
	                sbStyle.append(sEntry).append(';');
	            }
	        }
	        
	        return sbStyle.toString();
    	}
    	else
    	{
    		return  "";
    	}
    }
    
    /**
     * Removes unnecessary semicolons from the style definition.
     * 
     * @param pStyle the style definition
     * @return the style with beautified semicolons
     */
    private String minify(String pStyle)
    {
    	if (pStyle == null)
    	{
    		return null;
    	}

    	if (pStyle.length() < 3 || pStyle.trim().length() < 3)
    	{
    		return "";
    	}
    	
    	String style = pStyle;

    	if (style.startsWith(";"))
    	{
    		style = style.substring(1);
    	}
    	else if (style.startsWith(" ;"))
    	{
    		style = style.substring(2);
    	}

    	return style.replace("; ;", ";").replace(";;", ";").trim();
    }
    
    /**
     * Creates the css regular expression.
     * 
     * @param pAttribute the attribute
     * @return the reg exp or null, if attribute doesn't contain a specific class
     */
    private RegExp createRegExp(CssExtensionAttribute pAttribute)
    {
    	String sClass = pAttribute.getElementClassName();
    	
    	if (sClass != null)
    	{
        	if (pAttribute.isExactMatch())
        	{
        		return RegExp.compile("\\b" + sClass + "\\b", "i");
        	}
        	else
        	{
        		return RegExp.compile(sClass, "i");
        	}
    	}
    	
    	return null;
    }
 
} 	// CssExtensionConnector
