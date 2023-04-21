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
 * 30.03.2009 - [JR] - creation
 * 28.05.2009 - [JR] - updateUI overwritten to update the toolbars UI [BUGFIX]
 * 04.02.2011 - [JR] - remove JVxToolbar if empty after removing sub toolbars
 * 07.02.2011 - [JR] - getArea: return TOP instead of null [BUGFIX]
 * 09.02.2011 - [JR] - setArea: don't add toolbar if not already added, but cache constraint for later add call
 * 25.10.2011 - [JR] - #491: re-use the toolbar area and update the UI after add/remove
 * 01.11.2011 - [JR] - removeToolBar: remove separator if needed
 * 18.10.2014 - [JR] - hide special separators if previous toolbar is "empty" 
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;

/**
 * The <code>JVxToolBarPanel</code> is a special {@link JPanel} extension
 * with a content pane and toolbar areas around it.
 * 
 * @author René Jahn
 */
public class JVxToolBarPanel extends JPanel
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the possible toolbar areas. */
	public enum ToolBarArea 
	{ 
		/** the possible areas. */
		TOP, LEFT, BOTTOM, RIGHT, NONE 
	};
	
	/** the content panel. */
	private JPanel panContent;
	
	/** the top toolbar. */
	private JVxToolBar toolbar = new JVxToolBar();
	
	/** temporary constraint before adding toolbar. */
	private String sTempConstraint;
	
	/** the current area constraint. */
	private String sAreaConstraint = JVxBorderLayout.NORTH;
	
	/** whether separator visibility should be checked. */
	private boolean bCheckSeparatorVisibility = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>JVxToolBarPanel</code>.
	 */
	public JVxToolBarPanel()
	{
		super.setLayout(new JVxBorderLayout());
		
		panContent = new JPanel();
		panContent.setName("toolBarPanel.contentPane");
		panContent.setLayout(new BorderLayout());
		
		panContent.setBackground(null);
		panContent.setForeground(null);
		panContent.setFont(null);
		panContent.setCursor(null);
		
		super.addImpl(panContent, JVxBorderLayout.CENTER, 0);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUI()
	{
		super.updateUI();
		
		//update the toolbar UI to set the L&F
		if (toolbar != null)
		{
			toolbar.updateUI();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayout(LayoutManager pLayout)
	{
		if (panContent != null)
		{
			panContent.setLayout(pLayout);
		}
		else
		{
			super.setLayout(pLayout);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOpaque(boolean pOpaque)
	{
		if (panContent != null)
		{
			panContent.setOpaque(pOpaque);
		}
		
		if (toolbar != null)
		{
			toolbar.setOpaque(pOpaque);
		}
		
		super.setOpaque(pOpaque);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addImpl(Component pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent == toolbar)
		{
			super.addImpl(pComponent, pConstraints, pIndex);
		}
		else
		{
			panContent.add(pComponent, pConstraints, pIndex);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Component pComponent)
	{
		if (pComponent == toolbar)
		{
			for (int i = 0, anz = getComponentCount(); i < anz; i++)
			{
				if (getComponent(i) == toolbar)
				{
					super.remove(i);
					
					validate();
					repaint();
					
					return;
				}
			}
			
			super.remove(pComponent);
		}
		else
		{
			panContent.remove(pComponent);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int pIndex)
	{
		panContent.remove(pIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAll()
	{
		panContent.removeAll();
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public void addNotify()
	{
        checkSeparatorVisibility();
	    
	    super.addNotify();
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	public void paintComponent(Graphics pGraphics)
	{
        checkSeparatorVisibility();
        
        super.paintComponent(pGraphics);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds a toolbar to this panel as last component.
	 * 
	 * @param pToolBar the toolbar to be added
	 */
	public void addToolBar(JToolBar pToolBar)
	{
		addToolBar(pToolBar, -1);
	}
	
	/**
	 * Adds a toolbar to this panel at a specified index.
	 * 
	 * @param pToolBar the toolbar to be added
	 * @param pIndex the index for the toolbar              
	 */
	public void addToolBar(JToolBar pToolBar, int pIndex)
	{
		int iPos = pIndex;
		
		int iCount = toolbar.getComponentCount();
		
		if (iPos < 0)
		{
			iPos = iCount - 1;
		}
		
		if (iCount > 0)
		{
			if (iPos < iCount - 1)
			{
				//check if the next component is a container and if the first component is not a separator -> add a separator
				Component comp = toolbar.getComponent(iPos);
				
				if (comp instanceof Container && !(((Container)comp).getComponent(0) instanceof JToolBar.Separator))
				{
					((Container)comp).add(new JToolBar.Separator(), null, 0);
				}
			}
			else
			{
				pToolBar.add(new JToolBar.Separator(), null, 0);
			}
		}
		
		toolbar.add(pToolBar, pIndex);
		
		if (toolbar.getParent() == null)
		{
			super.addImpl(toolbar, sTempConstraint != null ? sTempConstraint : sAreaConstraint, 0);

			sTempConstraint = null;
		}
		
		if (iCount == 0)
		{
			toolbar.validate();
		}

		bCheckSeparatorVisibility = true;
		
		validate();
		repaint();
	}
	
	/**
	 * Removes a toolbar from this panel.
	 * 
	 * @param pToolBar the toolbar
	 */
	public void removeToolBar(JToolBar pToolBar)
	{
		Container parent = pToolBar.getParent();
		
		//Removes the toolbar only if the toolbar was added to this panel
		if (parent == toolbar)
		{
			if (pToolBar.getComponentCount() > 0 && pToolBar.getComponent(0) instanceof JToolBar.Separator)
			{
				pToolBar.remove(0);
			}

			int iPos = toolbar.getComponentIndex(pToolBar);
			
			//remove the separator of the "next" toolbar if needed (only if first toolbar is removed)
			if (iPos == 0 
				&& iPos + 1 < toolbar.getComponentCount())
			{
				Component comp = toolbar.getComponent(iPos + 1);
				
				if (comp instanceof Container && (((Container)comp).getComponent(0) instanceof JToolBar.Separator))
				{
					((Container)comp).remove(0);
				}
			}
			
			toolbar.remove(pToolBar);
			
			toolbar.repaint();
			
			if (toolbar.getComponentCount() == 0)
			{
				//cache the current area, because it is possible that the user changed the area via drag
				//and drop
				String sConstraint = (String)((JVxBorderLayout)getLayout()).getConstraints(toolbar);
				
				if (sConstraint != null)
				{
					sAreaConstraint = sConstraint;
				}

				//check the parent because it is possible that the parent is a window
				Container conParent = toolbar.getParent();

				if (conParent != null)
				{
					conParent.remove(toolbar);
					
					if (conParent != this)
					{
						Container con = conParent;
						
						while (con != null)
						{
							if (con instanceof JDialog)
							{
								((JDialog)con).dispose();
							}
							
							con = con.getParent();
						}
					}
					
					conParent.validate();
					conParent.repaint();
				}
			}
			
			bCheckSeparatorVisibility = true;
		}
	}
	
	/**
	 * Gets the number of <code>JToolBar</code>s in this panel.
	 * 
	 * @return the number of toolbars
	 */
	public int getToolBarCount()
	{
		int iCount = toolbar.getComponentCount();
		
		//ignore the separators
		return iCount - (iCount > 1 ? iCount - 1 : 0); 
	}
	
	/**
	 * Gets the {@link JToolBar} at a specific index.
	 *  
	 * @param pIndex the index
	 * @return the toolbar at <code>pIndex</code> from <code>pArea</code>
	 */
	public JToolBar getToolBar(int pIndex)
	{
		if (pIndex < 0 || pIndex >= toolbar.getComponentCount() - 1)
		{
			throw new ArrayIndexOutOfBoundsException("No such child: " + pIndex);
		}
		else
		{
			int iSize = toolbar.getComponentCount();
			
			return (JToolBar)toolbar.getComponent(pIndex + (iSize > 1 ? iSize - 1 : 0));
		}
		
	}

	/**
	 * Sets the area in which the toolbars will be added.
	 * 
	 * @param pArea the {@link ToolBarArea}
	 */
	public void setArea(ToolBarArea pArea)
	{
		String sConstraint = (String)((JVxBorderLayout)getLayout()).getConstraints(toolbar);
		
		String sNewConstraint;
		
		int iOrientation;
		
		switch (pArea)
		{
			case TOP:
				sNewConstraint = JVxBorderLayout.NORTH;
				iOrientation = JToolBar.HORIZONTAL;
				break;
			case LEFT:
				sNewConstraint = JVxBorderLayout.WEST;
				iOrientation = JToolBar.VERTICAL;
				break;
			case BOTTOM:
				sNewConstraint = JVxBorderLayout.SOUTH;
				iOrientation = JToolBar.HORIZONTAL;
				break;
			case RIGHT:
				sNewConstraint = JVxBorderLayout.EAST;
				iOrientation = JToolBar.VERTICAL;
				break;
			default:
				sNewConstraint = JVxBorderLayout.NORTH;
				iOrientation = JToolBar.HORIZONTAL;
		}
		
		if (!sNewConstraint.equals(sConstraint))
		{
			if (toolbar.getParent() != null)
			{
				sTempConstraint = null;
				super.addImpl(toolbar, sNewConstraint, 0);
			}
			else
			{
				sTempConstraint = sNewConstraint; 			
			}

			toolbar.setOrientation(iOrientation);
		}
		
		sAreaConstraint = sNewConstraint;
		
		bCheckSeparatorVisibility = true;
	}
	
	/**
	 * Gets the current area of the toolbar.
	 * 
	 * @return {@link ToolBarArea}
	 */
	public ToolBarArea getArea()
	{
		//Use the current constraint from the component because it is possible that the user
		//has dragged it to another place without setting the area programatically
		String sConstraint = (String)((JVxBorderLayout)getLayout()).getConstraints(toolbar);
		
		if (sConstraint == null)
		{
			sConstraint = sAreaConstraint;
		}
		
		if (JVxBorderLayout.NORTH.equals(sConstraint))
		{
			return ToolBarArea.TOP;
		}
		else if (JVxBorderLayout.WEST.equals(sConstraint))
		{
			return ToolBarArea.LEFT;
		}
		else if (JVxBorderLayout.EAST.equals(sConstraint))
		{
			return ToolBarArea.RIGHT;
		}
		else if (JVxBorderLayout.SOUTH.equals(sConstraint))
		{
			return ToolBarArea.BOTTOM;
		}
		
		return ToolBarArea.NONE;		
	}

	/**
	 * Gets the internal toolbar.
	 * 
	 * @return the toolbar
	 */
	protected JToolBar getToolBar()
	{
		return toolbar;
	}
	
	/**
	 * Checks if additional separator should be visible. It's not necessary to show a separator
	 * if previous component is not visible or a Container doesn't contain visible components.
	 */
	private void checkSeparatorVisibility()
	{
        if (bCheckSeparatorVisibility)
        {
            int cntPrevious = 0;
            int cntSub = 0;
            
            Component comp;
            Component compSub;
            
            for (int i = 0, cnti = toolbar.getComponentCount(); i < cnti; i++)
            {
                comp = toolbar.getComponent(i);
                
                if (comp instanceof Container)
                {
                    //previous container has more than one visible components -> show separator, otherwise hide it
                    if (i > 0 && ((Container)comp).getComponentCount() > 0)
                    {
                        compSub = ((Container)comp).getComponent(0);
            
                        if (compSub instanceof JToolBar.Separator)
                        {
                            compSub.setVisible(cntPrevious > 0);
                        }
                    }
                    
                    cntSub = 0;
    
                    if (comp.isVisible())
                    {
                        //search the number of visible components in the current container
                        for (int j = 0, cntj = ((Container)comp).getComponentCount(); j < cntj && cntSub == 0; j++)
                        {
                            compSub = ((Container)comp).getComponent(j);
                            
                            if (compSub.isVisible())
                            {
                                cntSub++;
                            }
                        }
                    }
                    
                    if (cntSub != 0)
                    {
                        cntPrevious = cntSub;
                    }
                }
            }
            
            bCheckSeparatorVisibility = false;
        }
	}
	
}	// JVxToolBarPanel
