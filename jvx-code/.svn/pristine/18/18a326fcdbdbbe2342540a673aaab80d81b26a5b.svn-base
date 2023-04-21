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
 * 18.03.2014 - [TK] - creation
 * 18.09.2015 - [JR] - #1470: add column name to cell style
 */
package com.sibvisions.rad.ui.vaadin.ext.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.sibvisions.rad.ui.vaadin.ext.ui.ExtendedTable;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.VScrollTable.HeaderCell;
import com.vaadin.v7.client.ui.VScrollTable.VScrollTableBody.VScrollTableRow;

/**
 * The <code>TableConnector</code> extends the {@link com.vaadin.v7.client.ui.table.TableConnector} and adds
 * support for row height configuration and column infos like nullable and read only.
 * 
 * @author Thomas Krautinger
 */
@Connect(ExtendedTable.class)
public class TableConnector extends com.vaadin.v7.client.ui.table.TableConnector
                            implements FocusHandler, BlurHandler
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the row height attribute name. */
	public static final String ATTRIBUTE_ROW_HEIGHT = "rowheight";
	
	/** the column infos (read only, nullable). */
	public static final String ATTRIBUTE_COLUMN_INFO = "columninfo";
	
	/** the row/cell styles. */
	public static final String ATTRIBUTE_STYLES = "styles";
	
	/** the row/cell style mappings. */
	public static final String ATTRIBUTE_STYLE_MAPPINGS = "styleMappings";

	/** the row/cell class mappings. */
    public static final String ATTRIBUTE_CLASS_MAPPINGS = "classMappings";
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
//	native static void consoleLog(String message) /*-{
//	    try {
//	        console.log(message);
//	    } catch (e) {
//	    }
//	}-*/;    

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void updateFromUIDL(UIDL pUidl, ApplicationConnection pClient)
	{ 
//		consoleLog("TableConnector: updateFromUIDL");
		
		getWidget().rendering = true;
		
		Map<String, Boolean> readonlyCells = new HashMap<String, Boolean>();
		Map<String, Boolean> nullableCells = new HashMap<String, Boolean>();
		Map<String, String> namedCells = new HashMap<String, String>();
		Map<String, StyleProperty> cellStyleMappings = new HashMap<String, StyleProperty>();
        Map<String, String[]> cellClassMappings = new HashMap<String, String[]>();
		Map<String, Map<String, List<String>>> rowStyles = new HashMap<String, Map<String, List<String>>>();
		Map<String, Map<String, String>> rowClasses = new HashMap<String, Map<String, String>>();
		
		int rowHeight = -1;
		
		// Parse (default) row height
		if (pUidl.hasAttribute(ATTRIBUTE_ROW_HEIGHT))
		{
		    rowHeight = pUidl.getIntAttribute(ATTRIBUTE_ROW_HEIGHT);
		}
		
		// Parse column infos (read only, nullable)
		// [
		//    "columninfo",
		//    {},
		//    ["column",{"cid":"2","readonly":false,"nullable":true}],
		//    ["column",{"cid":"1","readonly":true,"nullable":false}],
		//    ...
		// ]
		UIDL uidlColumnMetadata = pUidl.getChildByTagName(ATTRIBUTE_COLUMN_INFO);
		
		if (uidlColumnMetadata != null)
		{
			Iterator<?> iteratorColumnMetadata = uidlColumnMetadata.getChildIterator();
    		
    		while (iteratorColumnMetadata.hasNext())
    		{
                UIDL uidlColumn = (UIDL) iteratorColumnMetadata.next();
                
                String sCID = uidlColumn.getStringAttribute("cid");
                
                readonlyCells.put(sCID, Boolean.valueOf(uidlColumn.getBooleanAttribute("readonly")));
                nullableCells.put(sCID, Boolean.valueOf(uidlColumn.getBooleanAttribute("nullable")));
                
                String sName = uidlColumn.getStringAttribute("name");
                
                if (sName != null)
                {
                    namedCells.put(sCID, sName);
                }
    		}
		}
		
		// Parse row/cell style mappings
		//
		// [
		//    "styleMappings",
		//    {},
		//    ["style", {"sid":"0", "k":"color", "v":"rgb( 0, 0, 255)"}],
		//    ["style", {"sid":"1", "k":"background-color", "v":"rgb( 255, 0, 0)"}],
		//	  ...
		// ]
		UIDL uidlStyleMappings = pUidl.getChildByTagName(ATTRIBUTE_STYLE_MAPPINGS);
		
		if (uidlStyleMappings != null)
		{
			Iterator<?> iteratorStyleMapping = uidlStyleMappings.getChildIterator();
			
			while (iteratorStyleMapping.hasNext())
			{
				UIDL uidStyle = (UIDL) iteratorStyleMapping.next();
				
				if (uidStyle != null)
				{
					cellStyleMappings.put(uidStyle.getStringAttribute("sid"),
										  new StyleProperty(uidStyle.getStringAttribute("k"), uidStyle.getStringAttribute("v")));
				}
			}
		}

        // Parse row/cell class mappings
        //
        // [
        //    "classMappings",
        //    {},
        //    ["class", {"cid":"0", "v":"customstyle1"}],
        //    ["class", {"cid":"1", "v":"reddisabled,customstyle2"}],
        //    ...
        // ]
        UIDL uidlClassMappings = pUidl.getChildByTagName(ATTRIBUTE_CLASS_MAPPINGS);
        
        if (uidlClassMappings != null)
        {
            Iterator<?> iteratorClassMapping = uidlClassMappings.getChildIterator();
            
            while (iteratorClassMapping.hasNext())
            {
                UIDL uidStyle = (UIDL)iteratorClassMapping.next();
                
                if (uidStyle != null)
                {
                    cellClassMappings.put(uidStyle.getStringAttribute("cid"), uidStyle.getStringArrayAttribute("v"));
                }
            }
        }
		
		// Parse row/cell styles and classes
		//
		// [
		//    "styles",
		//	  {},
		//	  ["tr",{"key":"0"},["styles",{"cid":"1"},"0"]],
		//	  ["tr",{"key":"1"},["styles",{"cid":"2"},"1"]],
		//	  ...
		// ]
		UIDL uidlStyles = pUidl.getChildByTagName(ATTRIBUTE_STYLES);
		
		if (uidlStyles != null)
		{
			Iterator<?> iteratorStyle = uidlStyles.getChildIterator();
			
			while (iteratorStyle.hasNext())
			{
				UIDL uidlRowStyle = (UIDL) iteratorStyle.next();
				
				String rowKey = uidlRowStyle.getStringAttribute("key");
				
				Iterator<?> iteratorCellStyles = uidlRowStyle.getChildIterator();
				
				while (iteratorCellStyles.hasNext())
				{
					UIDL uidlCell =  (UIDL)iteratorCellStyles.next();

                    String columnId = uidlCell.getStringAttribute("cid");

                    UIDL uidlCellStyles = uidlCell.getChildByTagName("styles");

					Iterator<?> iteratorCellStyleMappings = uidlCellStyles.getChildIterator();
					
					while (iteratorCellStyleMappings.hasNext())
					{
						Object cellStyleMapping = iteratorCellStyleMappings.next();
						
						if (cellStyleMapping != null
							&& cellStyleMapping instanceof String
							&& cellStyleMappings.containsKey((String)cellStyleMapping))
						{
							Map<String, List<String>> rowStyle = rowStyles.get(rowKey);
							
							if (rowStyle == null)
							{
								rowStyle = new HashMap<String, List<String>>();
								rowStyles.put(rowKey, rowStyle);
							}
							
							List<String> cellStyle = rowStyle.get(columnId);
							
							if (cellStyle == null)
							{
								cellStyle = new ArrayList<String>();
								rowStyle.put(columnId, cellStyle);
							}
							
							if (!cellStyle.contains(cellStyleMapping))
							{
								cellStyle.add((String)cellStyleMapping);
							}
						}
					}
					
					UIDL uidlCellClasses = uidlCell.getChildByTagName("classes");
					
                    Iterator<?> iteratorCellClassMappings = uidlCellClasses.getChildIterator();
                    
                    if (iteratorCellClassMappings.hasNext())
                    {
                        Object cellClassMapping = iteratorCellClassMappings.next();
                        
                        if (cellClassMapping != null
                            && cellClassMapping instanceof String
                            && cellClassMappings.containsKey((String)cellClassMapping))
                        {
                            Map<String, String> rowClass = rowClasses.get(rowKey);
                            
                            if (rowClass == null)
                            {
                                rowClass = new HashMap<String, String>();
                                rowClasses.put(rowKey, rowClass);
                            }
                            
                            rowClass.put(columnId, (String)cellClassMapping);
                        }
                    }
				}
			}
		}
		
		super.updateFromUIDL(pUidl, pClient);
		getWidget().rendering = true; // it was set to false in super class

		// There are still use cases, where postLayout is not called, but columns are updated...
		// So we have to set this property anyway to true, otherwise, the column header is not scrolling...
		if (!getWidget().initializedAndAttached && !getWidget().sizeNeedsInit) // getWidget().updateColumnProperties(uidl); sets this maybe to false. 
		{                                                                      // In case there is not layout path, we have to set it
			getWidget().initializedAndAttached = true;						   // true again!
		}
		
		int cellCount = getWidget().tHead.getVisibleCellCount();
		
		Iterator<Widget> iteratorRows =  getWidget().scrollBody.iterator();
		
	    while (iteratorRows.hasNext())
	    {
	    	VScrollTableRow row = (VScrollTableRow) iteratorRows.next();
	    	
	    	Map<String, List<String>> rowStyle = rowStyles.get(row.getKey());
	    	Map<String, String> rowClass = rowClasses.get(row.getKey());
	    			
	    	for (int i = 0; i < cellCount; i++)
	    	{
	    		HeaderCell headerCell = getWidget().tHead.getHeaderCell(i);
	    		Element cell = DOM.getChild(row.getElement(), i);
    		
	    		String sColKey = headerCell.getColKey();
	    		
	    		if (readonlyCells.containsKey(sColKey))
	    		{
	    			if (readonlyCells.get(sColKey).booleanValue())
	    			{
	    				cell.addClassName("v-table-cell-readonly");
	    			}
	    			else
	    			{
	    				cell.addClassName("v-table-cell-not-readonly");
	    			}
	    		}
	    		
	    		if (nullableCells.containsKey(sColKey))
	    		{
	    			if (nullableCells.get(sColKey).booleanValue())
	    			{
	    				cell.addClassName("v-table-cell-nullable");
	    			}
	    			else
	    			{
	    				cell.addClassName("v-table-cell-not-nullable");
	    			}
	    		}
	    		
	    		if (namedCells.containsKey(sColKey))
	    		{
	    		    cell.addClassName("c-" + namedCells.get(sColKey).toLowerCase());
	    		}
	    		
	    		if (rowHeight >= 0)
	    		{
	    		    Element el = cell.getFirstChildElement();
	    		    
	    			if (el.getFirstChildElement() == null)
		    		{
	    			    Style style = el.getStyle();
	    			    
	    				style.setPropertyPx("height", rowHeight);
		    			style.setPropertyPx("lineHeight", rowHeight);
		    		}
	    		}
	    		
	    		if (rowStyle != null
	    		    && rowStyle.containsKey(sColKey))
	    		{
	    			List<String> cellStyles = rowStyle.get(sColKey);
	    			
	    			Style style = cell.getStyle();
	    			
	    			for (int j = 0, jc = cellStyles.size(); j < jc; j++)
	    			{
	    				StyleProperty cellStyleProperty = cellStyleMappings.get(cellStyles.get(j));
	    				
	    				if (cellStyleProperty != null)
	    				{
	    					style.setProperty(cellStyleProperty.getName(), cellStyleProperty.getValue());
	    				}
	    			}
	    		}
	    		
	    		if (rowClass != null
	    		    && rowClass.containsKey(sColKey))
	    		{
	    		    String[] saClasses = cellClassMappings.get(rowClass.get(sColKey));
	    		    
	    		    if (saClasses != null && saClasses.length > 0)
	    		    {
	    		        for (String className : saClasses)
	    		        {
	                        cell.addClassName(className);
	    		        }
	    		    }
	    		}
	    	}
	    }

		getWidget().rendering = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postLayout()
	{
//		consoleLog("TableConnector: postLayout");

	    // This function causes an deferred layout path! it calls per scheduler needsMeasure and layoutNow
	    // There should be no need to do this, so lets see.
//		super.postLayout();  
	    
        if (getWidget().sizeNeedsInit) 
        {
//            consoleLog("TableConnector: sizeInit");
            getWidget().sizeInit();
            
//            getWidget().triggerLazyColumnAdjustment(true); // Adjust columns immediatelly, to prevent toggling scrollbars
        }
		
//		getWidget().onScroll(null); // On layout changes post layout is called, but the scroll event is sometimes missing, so the table inside is not repainted.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init()
	{
		super.init();
		
		getWidget().scrollBodyPanel.addBlurHandler(this);
		getWidget().scrollBodyPanel.addFocusHandler(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBlur(BlurEvent pEvent)
	{
		getFocusAndBlurServerRpc().blur();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFocus(FocusEvent pEvent)
	{
		getFocusAndBlurServerRpc().focus();
	}

	/**
	 * Gets the focus and blur server rpc.
	 * @return the focus and blur server rpc.
	 */
    @SuppressWarnings("deprecation")
	private FocusAndBlurServerRpc getFocusAndBlurServerRpc() 
    {
        return getRpcProxy(FocusAndBlurServerRpc.class);
    }

	
    //****************************************************************
    // Subclass definition
    //****************************************************************	

	/**
	 * The <code>StyleProperty</code> contains the name and value of an css property.
	 * 
	 * @author Thomas Krautinger
	 */
	private final class StyleProperty
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The style name. */
		private String name;
		
		/** The style value. */
		private String value;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of <code>StyleProperty</code>.
		 * 
		 * @param pName the style name
		 * @param pValue the style value
		 */
		public StyleProperty(String pName, String pValue)
		{
			name = pName;
			value = pValue;
		}

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // User-defined methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the style name.
		 * 
		 * @return the style name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Gets the style value.
		 * 
		 * @return the style value
		 */
		public String getValue()
		{
			return value;
		}
		
	} // StyleProperty
	
}   // TableConnector
