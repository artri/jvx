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
 * 01.10.2008 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.plaf.smart.painter;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.DefaultTableCellRenderer;

import com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel;

/**
 * The <code>SmartTableHeaderRenderer</code> is the default header renderer for 
 * Tables with the Smart/LF.
 * 
 * @author René Jahn
 */
public class SmartTableHeaderRenderer extends DefaultTableCellRenderer
                                      implements UIResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the empty insets around the header cell. */
	private Insets insEmpty = new Insets(0, 0, 0, 0);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
    public String getName()
    {
    	return "TableHeader.renderer";
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public Component getTableCellRendererComponent
    (
    	JTable pTable, 
    	Object pObject, 
    	boolean pIsSelected, 
    	boolean pHasFocus, 
    	int pRow, 
    	int pColumn
    )
    {
        if (pTable != null && !pHasFocus)
        {
        	//Style für den Header aus der Konfiguration holen
            SynthStyle synthstyle = SmartLookAndFeel.getStyle(this, Region.TABLE_HEADER);
            SynthContext synthcontext = new SynthContext(pTable.getTableHeader(), Region.TABLE_HEADER, synthstyle, 0);
            Insets insStyle = synthstyle.getInsets(synthcontext, null);

            if (insStyle.equals(insEmpty))
            {
                setBorder(noFocusBorder);
            }
            else
            {
                setBorder(new EmptyBorder(insStyle));
            }
        }
        
        setValue(pObject);
        
        return this;
    }
	
}	// SmartTableHeaderRenderer
