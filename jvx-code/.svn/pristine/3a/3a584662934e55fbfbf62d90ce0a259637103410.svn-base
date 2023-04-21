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
 * 04.11.2008 - [HM] - creation
 * 06.08.2012 - [JR] - #595: mouse event handling changed (modifiers instead of getButton())
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * ScrollPane implementation that get the right minimum size, preferred size and maximum size.
 * 
 * @author Martin Handsteiner
 * @see javax.swing.JScrollPane 
 */
public class JVxScrollPane extends JScrollPane 
                           implements MouseListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a <code>JVxScrollPane</code> that displays the view
     * component in a viewport
     * whose view position can be controlled with a pair of scrollbars.
     * The scrollbar policies specify when the scrollbars are displayed, 
     * For example, if <code>vsbPolicy</code> is
     * <code>VERTICAL_SCROLLBAR_AS_NEEDED</code>
     * then the vertical scrollbar only appears if the view doesn't fit
     * vertically. The available policy settings are listed at 
     * {@link #setVerticalScrollBarPolicy} and
     * {@link #setHorizontalScrollBarPolicy}.
     * 
     * @see #setViewportView
     * 
     * @param pView the component to display in the scrollpanes viewport
     * @param pVerticalScrollBarPolicy an integer that specifies the vertical
     *		scrollbar policy
     * @param pHorizontalScrollBarPolicy an integer that specifies the horizontal
     *		scrollbar policy
     */
    public JVxScrollPane(Component pView, int pVerticalScrollBarPolicy, int pHorizontalScrollBarPolicy) 
    {
    	super(pView, pVerticalScrollBarPolicy, pHorizontalScrollBarPolicy);
    }

    /**
     * Creates a <code>JVxScrollPane</code> that displays the
     * contents of the specified
     * component, where both horizontal and vertical scrollbars appear
     * whenever the component's contents are larger than the view.
     * 
     * @see #setViewportView
     * @param pView the component to display in the scrollpane's viewport
     */
    public JVxScrollPane(Component pView) 
    {
        super(pView, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Creates an empty (no viewport view) <code>JVxScrollPane</code>
     * with specified 
     * scrollbar policies. The available policy settings are listed at 
     * {@link #setVerticalScrollBarPolicy} and
     * {@link #setHorizontalScrollBarPolicy}.
     * 
     * @see #setViewportView
     * 
     * @param pVerticalScrollBarPolicy an integer that specifies the vertical
     *		scrollbar policy
     * @param pHorizontalScrollBarPolicy an integer that specifies the horizontal
     *		scrollbar policy
     */
    public JVxScrollPane(int pVerticalScrollBarPolicy, int pHorizontalScrollBarPolicy) 
    {
        super(null, pVerticalScrollBarPolicy, pHorizontalScrollBarPolicy);
    }

    /**
     * Creates an empty (no viewport view) <code>JVxScrollPane</code>
     * where both horizontal and vertical scrollbars appear when needed.
     */
    public JVxScrollPane() 
    {
    	super(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * {@inheritDoc}
     */
	public void mouseClicked(MouseEvent pEvent)
	{
	}

    /**
     * {@inheritDoc}
     */
	public void mouseEntered(MouseEvent pEvent)
	{
	}

    /**
     * {@inheritDoc}
     */
	public void mouseExited(MouseEvent pEvent)
	{
	}

    /**
     * {@inheritDoc}
     */
	public void mousePressed(MouseEvent pEvent)
	{
		Component source = pEvent.getComponent();
		Component comp = getViewportView();

		if (source != comp)
		{
			if (comp != null && !comp.hasFocus())
			{
				comp.requestFocus();
			}
		}
	}

    /**
     * {@inheritDoc}
     */
	public void mouseReleased(MouseEvent pEvent)
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
	public void doLayout() 
	{
	    boolean oldVisible = horizontalScrollBar.isVisible();
	    
	    super.doLayout();
	    
	    if (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED && oldVisible != horizontalScrollBar.isVisible() && getClass() == JVxScrollPane.class)
	    {
	        SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    JVxScrollPane.this.invalidate();
                    JVxUtil.revalidateAll(JVxScrollPane.this);
                }
            });
	    }
    }

	
    /**
     * Returns the preferred size in a bit different way as JScrollPane.
     * In the case of SCROLLBARS_AS_NEEDED, the space for them is reserved if the width or height
     * is not tracked. This gives fixes the problem, layouts will resize everything do to changing
     * the visibility of the scrollbars. 
     * 
     * @return the preferred size.
     */
    @Override
    public Dimension getPreferredSize()
    {
    	if (isPreferredSizeSet())
    	{
    		return super.getPreferredSize();
    	}
    	Insets ins = getInsets();
    	int prefWidth = ins.left + ins.right;
    	int prefHeight = ins.top + ins.bottom;

    	if (viewport !=  null) 
    	{
    		Dimension prefSize;
    		
    		Component view = viewport.getView();
    		if (view == null)
    		{
    			prefSize = viewport.getPreferredSize();
    		}
    		else
    		{
    			prefSize = getPreferredComponentSize(view);
    		}

    		prefWidth += prefSize.width;
    	    prefHeight += prefSize.height;
    	}

    	Border viewportBorder = getViewportBorder();
    	if (viewportBorder != null) 
    	{
    	    Insets vpbInsets = viewportBorder.getBorderInsets(this);
    	    prefWidth += vpbInsets.left + vpbInsets.right;
    	    prefHeight += vpbInsets.top + vpbInsets.bottom;
    	}

    	if (rowHeader != null && rowHeader.isVisible()) 
    	{
    		Component rowHeaderView = rowHeader.getView();
    		Dimension prefSize = null;
    		if (rowHeaderView != null)
    		{
    			prefSize = rowHeaderView.getPreferredSize();
    			prefWidth += prefSize.width;
    		}
    	}

    	if (columnHeader != null && columnHeader.isVisible()) 
    	{
    		Component columnHeaderView = columnHeader.getView();
    		Dimension prefSize = null;
    		if (columnHeaderView != null)
    		{
    			prefSize = columnHeaderView.getPreferredSize();
    			prefHeight += prefSize.height;
    		}
    	}

    	// Calculate width with Scrollbars anyway, to prevent toggling, and cutting text.
    	if (verticalScrollBarPolicy != VERTICAL_SCROLLBAR_NEVER) 
    	{
    		prefWidth += verticalScrollBar.getPreferredSize().width;
    	}

    	// Calculate height of Scrollbars on demand. Vertical is actually no toggling problem. 
    	if (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_ALWAYS
    	        || (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED 
    	            && horizontalScrollBar.isVisible() && getWidth() > 0 && getHeight() > 0)) // && getClass() == JVxScrollPane.class)) 
    	{
    		prefHeight += horizontalScrollBar.getPreferredSize().height;
    	}

    	return new Dimension(prefWidth, prefHeight);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Installs mouse listener for gain focus on mouse pressed.
     */
    public void installMouseListenerToGainFocus() 
    {
        getViewport().addMouseListener(this);
    }
    
    /**
     * The calculation of PreferredSize is in here to prevent wrapping each component.
     * TextArea and TextField uses preferred size of set columns and rows. 
     * If columns or rows are set to 0 the default preferred size is used.
     * 
     * @param pComponent the component we want to know the preferred size.
     * @return the preferred component size.
     */
    protected Dimension getPreferredComponentSize(Component pComponent)
    {
        if (pComponent instanceof JTextArea)
        {
            JTextArea textArea = (JTextArea)pComponent;
            FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
            Insets ins = textArea.getInsets();
            Dimension pref = null;

            int columns = textArea.getColumns();
            int width;
            if (columns == 0)
            {
                if (pref == null)
                {
                    Dimension s = textArea.getSize();
                    if (s.width == 0 && s.height == 0 && textArea.getLineWrap())
                    {
                        textArea.setLineWrap(false);
                        pref = textArea.getPreferredSize();
                        textArea.setLineWrap(true);
                    }
                    else
                    {
                        pref = textArea.getPreferredSize();
                    }
                }
                width = pref.width;
            }
            else
            {
                width = columns * metrics.charWidth('m') + ins.left + ins.right;
            }
            int rows = textArea.getRows();
            int height;
            if (rows == 0)
            {
                if (pref == null)
                {
                    Dimension s = textArea.getSize();
                    if (s.width == 0 && s.height == 0 && textArea.getLineWrap())
                    {
                        textArea.setLineWrap(false);
                        pref = textArea.getPreferredSize();
                        textArea.setLineWrap(true);
                    }
                    else
                    {
                        pref = textArea.getPreferredSize();
                    }
                }
                height = pref.height;
            }
            else
            {
                height = rows * metrics.getHeight() + ins.top + ins.bottom;
            }
            
            return new Dimension(width, height);
        }
        else if (pComponent instanceof JTextField)
        {
            JTextField textField = (JTextField)pComponent;
            Insets ins = textField.getInsets();
            FontMetrics metrics = textField.getFontMetrics(textField.getFont());
            int columns = textField.getColumns();
            int width;
            if (columns == 0)
            {
                width = textField.getPreferredSize().width;
            }
            else
            {
                width = columns * metrics.charWidth('m') + ins.left + ins.right;
            }
            return new Dimension(width, 
                                 metrics.getHeight() + ins.top + ins.bottom);
        }
        else
        {
            return pComponent.getPreferredSize();
        }
    }
    
    /**
     * Gets the view port view.
     * @return the view port view.
     */
    public Component getViewportView()
    {
    	if (getViewport() == null)
    	{
    		return null;
    	}
    	else
    	{
    		return getViewport().getView();
    	}
    }

}	// JVxScrollPane
