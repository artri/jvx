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
package research;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Test class for a JDesktopPane with a special content panel.
 * 
 * @author René Jahn
 */
public class ContentDesktopPaneTest
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts the application.
	 * 
	 * @param pArgs arguments
	 */
	public static void main(String[] pArgs)
	{
		new ContentDesktopPaneTest();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>ContentDesktopPane</code>.
	 */
	protected ContentDesktopPaneTest()
	{
		JFrame frame = new JFrame("Content DesktopPane");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JInternalFrame ifrTest = new JInternalFrame("Test internal frame");
		ifrTest.setBounds(10, 10, 300, 200);
		
		JPanel panContent = new JPanel(new FlowLayout());
		
		panContent.add(new JButton("OK"));
		
		ContentDesktopPane desktop = new ContentDesktopPane();
		
		desktop.add(ifrTest);
		desktop.add(panContent);
		
		frame.add(desktop);

		frame.setSize(800, 600);
		frame.setVisible(true);
		
		ifrTest.setVisible(true);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
}	// ContentDesktopPaneTest

/**
 * Test class for a {@link JDesktopPane} with a special content area.
 * 
 * @author rene
 */
class ContentDesktopPane extends JDesktopPane
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the internal content panel. */
	private JPanel panContent = null;
	
	/** the layout manager for the desktop pane. */
	private ContentLayout layout = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Sets the layout manager for the content pane.
	 * 
	 * @param pLayout the layout manager
	 */
	@Override
	public void setLayout(LayoutManager pLayout)
	{
		attachContentPane();
		
		panContent.setLayout(pLayout);
		
		if (layout == null)
		{
			layout = new ContentLayout();
			
			super.setLayout(layout);
		}
	}
	
	/**
	 * Gets the layout manager for the content pane.
	 * 
	 * @return the layout manager
	 */
	@Override
	public LayoutManager getLayout()
	{
		attachContentPane();
		
		return panContent.getLayout();
	}

	/**
	 * Adds a {@link Component} to the desktop. If the component is an internal frame,
	 * then it will be added to the destops {@link JLayeredPane} otherwise it will
	 * be added to the content pane of the desktop.
	 * 
	 * @param pComponent the component to be added
	 * @param pConstraints constraints an object expressing layout constraints 
	 *                     for this component
	 * @param pIndex index the position in the container's list at which to
	 *               insert the component, where <code>-1</code> means append 
	 *               to the end
     * @exception IllegalArgumentException if <code>index</code> is invalid
     * @exception IllegalArgumentException if adding the container's parent
     *			  to itself
     * @throws IllegalArgumentException if <code>comp</code> has been added
     *         to the <code>Container</code> more than once
     * @exception IllegalArgumentException if adding a window to a container
     * @see       #add(Component)       
     * @see       #add(Component, int)       
     * @see       #add(Component, java.lang.Object)       
     * @see       LayoutManager
     * @see       LayoutManager2
	 */
	@Override
	protected void addImpl(Component pComponent, Object pConstraints, int pIndex)
	{
		if (pComponent instanceof JInternalFrame)
		{
			super.addImpl(pComponent, pConstraints, pIndex);
		}
		else
		{
			attachContentPane();
			
			panContent.add(pComponent, pConstraints, pIndex);
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds the content pane to the desktops FRAME_CONTENT_LAYER, if it's not
	 * already done.
	 */
	private void attachContentPane()
	{
		if (panContent == null)
		{
			panContent = new JPanel(new BorderLayout(0, 0));
			super.addImpl(panContent, JLayeredPane.FRAME_CONTENT_LAYER, 0);
		}
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The <code>ContentLayout</code> layouts the content pane of the desktop.
	 * All other components will be ignored.
	 * 
	 * @author René Jahn
	 */
	private class ContentLayout implements LayoutManager
	{
		/**
		 * {@inheritDoc}
		 */
		public void addLayoutComponent(String pName, Component pComponent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public void removeLayoutComponent(Component pComponent)
		{
		}

		/**
		 * {@inheritDoc}
		 */
		public Dimension minimumLayoutSize(Container pParent)
		{
			return preferredLayoutSize(pParent);
		}

		/**
		 * {@inheritDoc}
		 */
		public Dimension preferredLayoutSize(Container pParent)
		{
			return pParent.getPreferredSize();
		}

		/**
		 * {@inheritDoc}
		 */
		public void layoutContainer(Container pParent)
		{
			if (panContent != null)
			{
				panContent.setBounds(pParent.getBounds());
			}
		}
		
	}	// ContentLayout
	
}	// ContentDesktopPane
