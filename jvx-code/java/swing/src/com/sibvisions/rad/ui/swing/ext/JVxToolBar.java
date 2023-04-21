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
 * 17.11.2008 - [JR] - InternalToolBarLayout used, if possible
 * 05.12.2008 - [JR] - setLayoutOrientation: fixed alignments
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

import com.sibvisions.rad.ui.swing.ext.layout.JVxSequenceLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;

/**
 * The <code>JVxToolBar</code> is an extended <code>JToolBar</code>. It
 * forwards relevant properties to all sub toolbars.
 * 
 * @author René Jahn
 */
public class JVxToolBar extends JToolBar
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the toolbar layout if no user-defined/look and feel layout is set. */
	private InternalToolBarLayout layout;
	
	/** flag that identifies that a special layout is in use. */
	private boolean bSynthUI = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>JToolBar</code> with {@link javax.swing.SwingConstants#HORIZONTAL}
	 * orientation.
	 */
	public JVxToolBar()
	{
		this(null, HORIZONTAL);
	}
	
	/**
	 * Creates a new instance of <code>JToolBar</code> with an user-defined
	 * orientation.
	 * 
	 * @param pOrientation the orientation
	 */
	public JVxToolBar(int pOrientation)
	{
		this(null, pOrientation);
	}
	
	/**
	 * Creates a new instance of <code>JToolBar</code> with an user-defined
	 * toolbar name and {@link javax.swing.SwingConstants#HORIZONTAL} orientation.
	 * 
	 * @param pName the name
	 */
	public JVxToolBar(String pName)
	{
		this(pName, HORIZONTAL);
	}
	
	/**
	 * Creates a new instance of <code>JToolBar</code> with an user-defined toolbar name
	 * and orientation.
	 * 
	 * @param pName the name
	 * @param pOrientation the orientation
	 */
	public JVxToolBar(String pName, int pOrientation)
	{
		super(pName, pOrientation);
		
		LayoutManager lmInit = getLayout();
		
		Class<?> clsDeclaring = (lmInit == null ? null : getLayout().getClass().getDeclaringClass());
		
		
		//only use internal layout when possible!
		if (lmInit == null
            || clsDeclaring == JToolBar.class
            || "javax.swing.plaf.synth.SynthToolBarUI".equals(clsDeclaring.getName()))
		{
			layout = new InternalToolBarLayout();
			layout.setHorizontalGap(1);
			layout.setVerticalGap(1);
			
			setLayout(layout);
			
			bSynthUI = "javax.swing.plaf.synth.SynthToolBarUI".equals(clsDeclaring.getName());
			
			setLayoutOrientation(pOrientation);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrientation(int pOrientation)
	{
		super.setOrientation(pOrientation);
	
		setLayoutOrientation(pOrientation);

		//sub toolbars always use the orientation of the parent Toolbar
		//otherwise the layout looks bad
		//this is important because it's possible to have different
		//orientations since toolbars are floatable
		Component comp;
		
		for (int i = 0, anz = getComponentCount(); i < anz; i++)
		{
			comp = super.getComponent(i);
			
			if (comp instanceof JToolBar)
			{
				((JToolBar)comp).setOrientation(pOrientation);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFloatable(boolean pFloatable)
	{
		super.setFloatable(pFloatable);
		
		updateMargins();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addImpl(Component pComponent, Object pConstraints, int pIndex)
	{
		//sub toolbars always uses the parents orientation!
		if (pComponent instanceof JToolBar)
		{
			((JToolBar)pComponent).setOrientation(getOrientation());
		}

		super.addImpl(pComponent, pConstraints, pIndex);
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the orientation of the internal layout, if set.
	 * 
	 * @param pOrientation the desired toolbar orientation
	 */
	private void setLayoutOrientation(int pOrientation)
	{
		if (layout != null)
		{
			if (pOrientation == HORIZONTAL)
			{
				layout.setOrientation(JVxSequenceLayout.HORIZONTAL);
				layout.setHorizontalAlignment(JVxSequenceLayout.LEFT);
				layout.setVerticalAlignment(JVxSequenceLayout.STRETCH);
				layout.setHorizontalComponentAlignment(JVxSequenceLayout.CENTER);
				layout.setVerticalComponentAlignment(JVxSequenceLayout.STRETCH);
			}
			else
			{
				layout.setOrientation(JVxSequenceLayout.VERTICAL);
				layout.setHorizontalAlignment(JVxSequenceLayout.STRETCH);
				layout.setVerticalAlignment(JVxSequenceLayout.TOP);
				layout.setHorizontalComponentAlignment(JVxSequenceLayout.STRETCH);
				layout.setVerticalComponentAlignment(JVxSequenceLayout.CENTER);
			}
			
			updateMargins();
		}
	}
	
	/**
	 * Updates the margins for special layout configurations.
	 */
	private void updateMargins()
	{
		if (layout != null)
		{
			//synth paints the float-handle and has no margins for it!
			//10px are ok
			if (bSynthUI)
			{
				if (!isFloatable())
				{
					layout.setMargins(new Insets(0, 0, 0, 0));
				}
				else
				{
					if (getOrientation() == HORIZONTAL)
					{
						layout.setMargins(new Insets(0, 10, 0, 0));
					}
					else
					{
						layout.setMargins(new Insets(10, 0, 0, 0));
					}
				}
			}
			
			//make the toolbar a little bit smaller
			if (SwingFactory.isMacLaF())
			{
				Border bor = (Border)getClientProperty("#border.orig"); 
				
				if (bor == null)
				{
					bor = getBorder();
					
					putClientProperty("#border.orig", bor);
				}
				
				WrappedInsetsBorder borNew;
				
				if (getOrientation() == HORIZONTAL)
				{
					borNew = new WrappedInsetsBorder(bor, new Insets(2, bor.getBorderInsets(this).left, 2, 2));
				}
				else
				{
					borNew = new WrappedInsetsBorder(bor, new Insets(bor.getBorderInsets(this).top, 2, 2, 2));
				}
				
				borNew.setPaintInsets(new Insets(0, 0, 0, 0));
				
				setBorder(borNew);
			}
		}
	}

	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * A simple layout wrapper to implement {@link UIResource}. That's the
	 * only way for look and feels to switch the layout.
	 * 
	 * @author René Jahn
	 */
	private static final class InternalToolBarLayout extends JVxSequenceLayout
	                                                 implements UIResource
	{
	}
	
}	// JVxToolBar
