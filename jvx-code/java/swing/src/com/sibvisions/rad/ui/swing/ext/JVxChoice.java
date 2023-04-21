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
 * 08.10.2008 - [HM] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.awt.impl.AwtFactory;

/**
 * A Choice editor can display and switch several values as Image.
 * This is more flexible than JRadioButton or JCheckBox, because it can
 * be configured which items should be returned and which images should be
 * displayed instead.
 *  
 * @author Martin Handsteiner
 */
public class JVxChoice extends JTextField 
                       implements JVxConstants, 
                                  ItemSelectable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** A null document filter. */
	private static final NullDocumentFilter NULL_DOCUMENT_FILTER = new NullDocumentFilter();
	
	/** A null cursor for hiding the caret. */
	private static final NullCaret NULL_CARET = new NullCaret();
	
	/** A empty object array. */
	private static final Object[] EMPTY_OBJECTARRAY = new Object[] {};

	/** A empty image array. */
	private static final Image[] EMPTY_IMAGEARRAY = new Image[] {};
	
	/** The list of allowed values. */
	private Object[] allowedValues; 
	
	/** The list of allowed values. */
	private Image[] images;
	
	/** The cursor. */
	private Cursor cursor;
	
	/** The default image shown when selectedIndex is -1. */
	private Image defaultImage = null;
	
	/** The selected index. */
	private Color innerBackground;
	
	/** The selected index. */
	private Border innerBorder;
	
	/** The selected index. */
	private boolean imageBorderVisible = false;
	
	/** The selected index. */
	private Dimension paintSize;
	
	/** The max width of the images. */ 
	private int imageWidth;
	/** The max height of the images. */ 
	private int imageHeight;
	
	/** The size the images should have. */
	private Dimension imageSize = null;
	
	/** The horizontal alignment. */ 
	private int horizontalAlignment = CENTER;
	/** The vertical alignment. */ 
	private int verticalAlignment = CENTER;
	
	/** The selected index. */
	private int selectedIndex = -1;
	
	/** The action command. */
	private String actionCommand = null;
	
	/** The current painted image, to stop animation thread for not painted images. */
	private Image currentPaintedImage = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Constructs a new JVxChoice component.
	 */
	public JVxChoice()
	{
		this(null, null);
	}
	
	/**
	 * Constructs a new JVxChoice with the given allowed values and image names.
	 * @param pAllowedValues the allowed values.
	 * @param pImages the images.
	 */
	public JVxChoice(Object[] pAllowedValues, Image[] pImages)
	{
		enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		((AbstractDocument)getDocument()).setDocumentFilter(NULL_DOCUMENT_FILTER);
		setCaret(NULL_CARET);
		setAllowedValues(pAllowedValues);
		setImages(pImages);
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUI(TextUI pTextUI)
	{
		Color background = getBackground();
		Border border = getBorder();
		setBackground(innerBackground);
		setBorder(innerBorder);
		super.setUI(pTextUI);
		innerBorder = getBorder();
		innerBackground = getBackground();
		setBackground(background);
		setBorder(border);
		setOpaque(false);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) 
    {
        // Stop animation thread, if component is not showing anymore.
        return isShowing() && img == currentPaintedImage && super.imageUpdate(img, infoflags, x, y, w, h);
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCursor(Cursor pCursor)
	{
		super.setCursor(pCursor);
		cursor = pCursor;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEditable(boolean pEditable)
	{
		Color background = getBackground();
		Border border = getBorder();
		setBackground(innerBackground);
		setBorder(innerBorder);
		super.setEditable(pEditable);
		innerBorder = getBorder();
		innerBackground = getBackground();
		setBackground(background);
		setBorder(border);
		if (pEditable)
		{
			super.setCursor(cursor);
		}
		else
		{
			super.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Color getBackground()
	{
		if (paintSize == null || innerBackground == null)
		{
			return super.getBackground();
		}
		else
		{
			return innerBackground;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth()
	{
		if (paintSize == null)
		{
			return super.getWidth();
		}
		else
		{
			return paintSize.width;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight()
	{
		if (paintSize == null)
		{
			return super.getHeight();
		}
		else
		{
			return paintSize.height;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getSize()
	{
		if (paintSize == null)
		{
			return super.getSize();
		}
		else
		{
			return new Dimension(paintSize.width, paintSize.height);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getBounds()
	{
		if (paintSize == null)
		{
			return super.getBounds();
		}
		else
		{
			return new Rectangle(getX(), getY(), paintSize.width, paintSize.height);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics pGraphics)
	{
		Dimension size = getSize();

		Insets iIns = getInnerInsets();
		Rectangle iBounds = getInnerBounds();
		
		if (isBackgroundSet())
		{
			pGraphics.setColor(getBackground());
			pGraphics.fillRect(0, 0, size.width, size.height);
		}
		
		paintSize = new Dimension(iBounds.width, iBounds.height);

		Graphics graphics = pGraphics.create(iBounds.x, iBounds.y, iBounds.width, iBounds.height);
		
		if (imageBorderVisible)
		{
			ui.update(graphics, this);
		}
		
		paintSize = null;
		
        Image image;
		if (selectedIndex < 0)
		{
			image = defaultImage;
		}
		else if (selectedIndex < images.length)
		{
			image = images[selectedIndex];
		}
		else
		{
			image = null;
		}
		if (image != null)
		{
			int imgW = image.getWidth(null);
			int imgH = image.getHeight(null);
			Dimension iSize = getImageSize();
			
			if (horizontalAlignment != STRETCH)
			{
				if (imgW < iSize.width)
				{
					iIns.left += (iSize.width - imgW) / 2;
					iSize.width = imgW;
				}
			}
			if (verticalAlignment != STRETCH)
			{
				if (imgH < iSize.height)
				{
					iIns.top += (iSize.height - imgH) / 2;
					iSize.height = imgH;
				}
			}
			((Graphics2D)pGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			graphics.drawImage(image, iIns.left, iIns.top, iSize.width, iSize.height, this);
			
		}
		
		graphics.dispose();
        currentPaintedImage = image;

		if (imageBorderVisible && innerBorder != null) 
        {
			innerBorder.paintBorder(this, pGraphics, iBounds.x, iBounds.y, iBounds.width, iBounds.height);
        }
	}
	
	/**
	 * Gets the preferred size of the icon dependent of the configured
	 * alignments. If an alignment is {@link #STRETCH} then the
	 * size of the component will be used. Otherwise the size
	 * of the image will be used.
	 * 
	 * @return the preferred size of the image
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if (isPreferredSizeSet())
		{
			return super.getPreferredSize();
		}
		else
		{
			Insets ins = getInsets(); 
			Insets iIns = getInnerInsets();
			Dimension iSize = getImageSize();
			return new Dimension(iSize.width + ins.left + ins.right + iIns.left + iIns.right, iSize.height + ins.top + ins.bottom + iIns.top + iIns.bottom);
		}
	}
	
	/**
	 * Gets the horizontal alignment of the icon.
	 * 
	 * @return the alignment
	 * @see JVxConstants
	 */
	@Override
	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	/**
	 * Sets the horizontal alignment of the icon.
	 * 
	 * @param pHorizontalAlignment the alignment
	 * @see JVxConstants
	 */
	@Override
	public void setHorizontalAlignment(int pHorizontalAlignment)
	{
		if (pHorizontalAlignment == LEFT || pHorizontalAlignment == CENTER || pHorizontalAlignment == RIGHT || pHorizontalAlignment == STRETCH)
		{
			if (pHorizontalAlignment != horizontalAlignment)
			{
				horizontalAlignment = pHorizontalAlignment;
				invalidate();
				repaint();
			}
		}
		else
		{
			throw new IllegalArgumentException("horizontalAlignment");
		}
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void processMouseEvent(MouseEvent pMouseEvent)
    {
    	if (!pMouseEvent.isConsumed() && pMouseEvent.getID() == MouseEvent.MOUSE_PRESSED && getInnerBounds().contains(pMouseEvent.getX(), pMouseEvent.getY()))
    	{
    		requestFocus();
    		selectNextIndex();
    	}
    	super.processMouseEvent(pMouseEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processKeyEvent(KeyEvent pKeyEvent)
    {
    	if (!pKeyEvent.isConsumed() && pKeyEvent.getID() == KeyEvent.KEY_PRESSED &&	pKeyEvent.getKeyCode() == KeyEvent.VK_SPACE)
    	{
    		pKeyEvent.consume();
    		selectNextIndex();
    	}
    	super.processKeyEvent(pKeyEvent);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object[] getSelectedObjects()
	{
		if (selectedIndex < 0)
		{
			return new Object[] {};
		}
		else
		{
			return new Object[] {allowedValues[selectedIndex]};
		}
	}

	/**
	 * {@inheritDoc}
	 */
    public void addItemListener(ItemListener pItemListener) 
    {
        listenerList.add(ItemListener.class, pItemListener);
    }

	/**
	 * {@inheritDoc}
	 */
    public void removeItemListener(ItemListener pItemListener) 
    {
        listenerList.remove(ItemListener.class, pItemListener);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the inner background.
	 * 
	 * @return the inner background.
	 */
	public Color getInnerBackground()
	{
		return innerBackground;
	}
	
	/**
	 * sets the inner background.
	 * 
	 * @param pInnerBackground the inner background.
	 */
	public void setInnerBackground(Color pInnerBackground)
	{
		innerBackground = pInnerBackground;
		invalidate();
	}
	
	/**
	 * Gets the inner border.
	 * 
	 * @return the inner border.
	 */
	public Border getInnerBorder()
	{
		return innerBorder;
	}
	
	/**
	 * sets the inner border.
	 * 
	 * @param pInnerBorder the inner border.
	 */
	public void setInnerBorder(Border pInnerBorder)
	{
		innerBorder = pInnerBorder;
		invalidate();
	}
	
	/**
	 * Gets the visibility of the image border.
	 * 
	 * @return the visibility of the image border.
	 */
	public boolean isImageBorderVisible()
	{
		return imageBorderVisible;
	}
	
	/**
	 * sets the visibility of the image border.
	 * 
	 * @param pImageBorderVisible the visibility of the image border.
	 */
	public void setImageBorderVisible(boolean pImageBorderVisible)
	{
		imageBorderVisible = pImageBorderVisible;
		invalidate();
	}

	/**
	 * Gets all allowed values.
	 * @return all allowed values.
	 */
	public Object[] getAllowedValues()
	{
		return allowedValues;
	}

	/**
	 * Sets all allowed values.
	 * @param pAllowedValues all allowed values.
	 */
	public void setAllowedValues(Object[] pAllowedValues)
	{
		Object item = getSelectedItem();
		if (pAllowedValues == null)
		{
			allowedValues = EMPTY_OBJECTARRAY;
		}
		else
		{
			allowedValues = pAllowedValues;
		}
		setSelectedItem(item);
	}

	/**
	 * Gets the images that are used for displaying the values.
	 * @return the images.
	 */
	public Image[] getImages()
	{
		return images;
	}

	/**
	 * Sets the images that are used for displaying the values.
	 * 
	 * @param pImages the images.
	 */
	public void setImages(Image[] pImages)
	{
		Image[] newImages;
		if (pImages == null || pImages.length == 0)
		{
			newImages = EMPTY_IMAGEARRAY;
		}
		else
		{
			newImages = pImages;
		}
		if (images != newImages)
		{
			images = newImages;
			calculateImageSize();
		}
	}
	
    /**
     * Returns the image size.
     * The original size of the image.
     *
     * @return the size the images should have.
     */
    public Dimension getImageSize()
    {
    	if (imageSize == null)
    	{
    		return new Dimension(imageWidth, imageHeight);
    	}
    	else
    	{
    		return (Dimension)imageSize.clone();
    	}
    }
    

    /**
     * Returns the image size.
     * The original size of the image.
     *
     * @param pImageSize the size the images should have.
     */
    public void setImageSize(Dimension pImageSize)
    {
    	imageSize = pImageSize;
    }

	/**
	 * Calculates the maximum image size.
	 */
	private void calculateImageSize()
	{
	    imageWidth = 14;
	    imageHeight = 14;
	    if (defaultImage != null)
	    {
    		imageWidth = Math.max(imageWidth, defaultImage.getWidth(null));
    		imageHeight = Math.max(imageHeight, defaultImage.getHeight(null));
	    }
	    for (int i = 0; i < images.length; i++) 
	    {
	    	if (images[i] != null) 
	    	{
	    		imageWidth = Math.max(imageWidth, images[i].getWidth(null));
	    		imageHeight = Math.max(imageHeight, images[i].getHeight(null));
	    	}
	    }
	}

    /**
     * Returns the inner insets.
     *
     * @return the inner insets.
     */
	public Insets getInnerInsets()
	{
		if (imageBorderVisible && innerBorder != null)
		{
			return innerBorder.getBorderInsets(this);
		}
		else
		{
			return new Insets(0, 0, 0, 0);
		}
		
	}
	
    /**
     * Returns the inner bounds of the choice component.
     * Mouse events will only effect inside the inner bounds.
     *
     * @return the inner bounds.
     */
	public Rectangle getInnerBounds()
	{
		Insets ins = getInsets();
		Dimension size = getSize();
		size.width -= ins.left + ins.right;
		size.height -= ins.top + ins.bottom;
		
		Insets iIns = getInnerInsets();
		Dimension iSize = getImageSize();
		iSize.width += iIns.left + iIns.right;
		iSize.height += iIns.top + iIns.bottom;

		Rectangle result = new Rectangle();
		
		switch (horizontalAlignment)
		{
			case LEFT:
				result.x = ins.left;
				result.width = iSize.width;
				break;
			case CENTER:
				result.x = ins.left + (size.width - iSize.width) / 2;
				result.width = iSize.width;
				break;
			case RIGHT:
				result.x = ins.left + size.width - iSize.width;
				result.width = iSize.width;
				break;
			default:
				result.x = 0;
				result.width = size.width;
		}
		switch (verticalAlignment)
		{
			case TOP:
				result.y = ins.top;
				result.height = iSize.height;
				break;
			case CENTER:
				result.y = ins.top + (size.height - iSize.height) / 2;
				result.height = iSize.height;
				break;
			case BOTTOM:
				result.y = ins.top + size.height - iSize.height;
				result.height = iSize.height;
				break;
			default:
				result.y = ins.top;
				result.height = size.height;
		}
		return result;
	}

	/**
	 * Gets the default image that is shown if selectedIndex is -1.
	 * @return the default image.
	 */
	public Image getDefaultImage()
	{
		return defaultImage;
	}

	/**
	 * Sets the default image that is shown if selectedIndex is -1.
	 * 
	 * @param pDefaultImage the default image.
	 */
	public void setDefaultImage(Image pDefaultImage)
	{
		if (defaultImage != pDefaultImage)
		{
			defaultImage = pDefaultImage;
			calculateImageSize();
		}
	}

	/**
	 * Gets the vertical alignment of the icon.
	 * 
	 * @return the alignment
	 * @see JVxConstants
	 */
	public int getVerticalAlignment()
	{
		return verticalAlignment;
	}
	
	/**
	 * Sets the vertical alignment of the icon.
	 * 
	 * @param pVerticalAlignment the alignment
	 * @see JVxConstants
	 */
	public void setVerticalAlignment(int pVerticalAlignment)
	{
		if (pVerticalAlignment == TOP || pVerticalAlignment == CENTER || pVerticalAlignment == BOTTOM || pVerticalAlignment == STRETCH)
		{
			if (pVerticalAlignment != verticalAlignment)
			{
				verticalAlignment = pVerticalAlignment;
				invalidate();
				repaint();
			}
		}
		else
		{
			throw new IllegalArgumentException("verticalAlignment");
		}
	}

	/**
	 * Gets the selected index, or -1 if no value is selected.
	 * @return the selected index.
	 */
	public int getSelectedIndex()
	{
		return selectedIndex;
	}

	/**
	 * Sets the selected index. If the index does not exist, it is set to -1 (deselected). 
	 * @param pSelectedIndex the index to select.
	 */
	public void setSelectedIndex(int pSelectedIndex)
	{
		if (pSelectedIndex < 0 || pSelectedIndex >= allowedValues.length)
		{
			if (selectedIndex >= 0)
			{
			    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, allowedValues[selectedIndex], ItemEvent.DESELECTED));
				selectedIndex = -1;
				invalidate();
				repaint();
			    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, null, ItemEvent.SELECTED));
			}
		}
		else if (selectedIndex != pSelectedIndex)
		{
			if (selectedIndex < 0)
			{
			    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, null, ItemEvent.DESELECTED));
			}
			else
			{
			    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, allowedValues[selectedIndex], ItemEvent.DESELECTED));
			}
			selectedIndex = pSelectedIndex;
			invalidate();
			repaint();
		    fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, allowedValues[selectedIndex], ItemEvent.SELECTED));
		}
	    fireActionEvent();
	}

	/**
	 * Selects the next index. This functionality is round robin. 
	 */
	public void selectNextIndex()
	{
		if (isEnabled() && isEditable())
		{
			if (selectedIndex + 1 < allowedValues.length)
			{
				setSelectedIndex(selectedIndex + 1);
			}
			else
			{
				setSelectedIndex(0);
			}
		}
	}

	/**
	 * Gets the current selected value, or null, if no one is selected.
	 * Be careful, null can also be a allowed value. 
	 * @return the selected value.
	 */
	public Object getSelectedItem()
	{
		if (selectedIndex < 0 || allowedValues == null || selectedIndex >= allowedValues.length)
		{
			return null;
		}
		else
		{
			return allowedValues[selectedIndex];
		}
	}

	/**
	 * Sets the value that should be selected. If the value i not included in the 
	 * allowed values list, the selected index is set to -1.
	 * Be careful, null can also be a allowed value. 
	 * @param pSelectedItem the value to select.
	 */
	public void setSelectedItem(Object pSelectedItem)
	{
		for (int i = 0; i < allowedValues.length; i++)
		{
			if (pSelectedItem == allowedValues[i] || (pSelectedItem != null && pSelectedItem.equals(allowedValues[i])))
			{
				setSelectedIndex(i);
				return;
			}
		}
		setSelectedIndex(-1);
	}
	
    /** 
     * Returns the action command that is included in the event sent to
     * action listeners.
     *
     * @return  the string containing the "command" that is sent
     *          to action listeners.
     */
    public String getActionCommand() 
    {
        return actionCommand;
    }

    /** 
     * Sets the action command that should be included in the event
     * sent to action listeners.
     *
     * @param pActionCommand  a string containing the "command" that is sent
     *                  to action listeners; the same listener can then
     *                  do different things depending on the command it
     *                  receives
     */
    public void setActionCommand(String pActionCommand) 
    {
        actionCommand = pActionCommand;
    }
	
    /**
     * Returns an array of all the <code>ItemListener</code>s added
     * to this JVxChoice with addItemListener().
     *
     * @return all of the <code>ItemListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ItemListener[] getItemListeners() 
    {
        return (ItemListener[])listenerList.getListeners(ItemListener.class);
    }

    /** 
     * Adds an <code>ActionListener</code>. 
     * <p>
     * The <code>ActionListener</code> will receive an <code>ActionEvent</code>
     * when a selection has been made. 
     *
     * @param pActionListener  the <code>ActionListener</code> that is to be notified
     * @see #setSelectedItem
     */
    public void addActionListener(ActionListener pActionListener) 
    {
        listenerList.add(ActionListener.class, pActionListener);
    }

    /** Removes an <code>ActionListener</code>.
     *
     * @param pActionListener the <code>ActionListener</code> to remove
     */
    public void removeActionListener(ActionListener pActionListener) 
    {
	    listenerList.remove(ActionListener.class, pActionListener);
    }

    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this JComboBox with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     */
    public ActionListener[] getActionListeners() 
    {
        return (ActionListener[])listenerList.getListeners(ActionListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     *  
     * @param pItemEvent the event of interest
     * @see ItemListener
     */
    protected void fireItemStateChanged(ItemEvent pItemEvent) 
    {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) 
        {
            if (listeners[i] == ItemListener.class) 
            {
                ((ItemListener)listeners[i + 1]).itemStateChanged(pItemEvent);
            }
        }
    }   

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     *
     * @see ActionListener
     */
    protected void fireActionEvent() 
    {
    	ActionEvent actionEvent = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) 
        {
            if (listeners[i] == ActionListener.class) 
            {
            	if (actionEvent == null)
            	{
            		actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(),
                            					  AwtFactory.getMostRecentEventTime(), AwtFactory.getCurrentModifiers());
            	}
                ((ActionListener)listeners[i + 1]).actionPerformed(actionEvent);
            }          
        }
    }
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * Null Caret is a invisible non functional Caret to
     * prevent null pointer exceptions in swing.
     * 
     * @author Martin Handsteiner
     */
    public static class NullCaret implements Caret
	{
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * Creates a new instance of <code>NullCaret</code>.
    	 */
    	public NullCaret()
    	{
    	}
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * {@inheritDoc}
    	 */
    	public void addChangeListener(ChangeListener pListener)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void removeChangeListener(ChangeListener pListener)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void install(JTextComponent pTextComponent)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void deinstall(JTextComponent pTextComponent)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void setBlinkRate(int pRate)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public int getBlinkRate()
		{
			return 0;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void setDot(int pDot)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public int getDot()
		{
			return 0;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void moveDot(int dot)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void setMagicCaretPosition(Point pPoint)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public Point getMagicCaretPosition()
		{
			return null;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public int getMark()
		{
			return 0;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void setSelectionVisible(boolean pSelectionVisible)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public boolean isSelectionVisible()
		{
			return false;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void setVisible(boolean pVisible)
		{
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public boolean isVisible()
		{
			return false;
		}

    	/**
    	 * {@inheritDoc}
    	 */
    	public void paint(Graphics pGraphics)
		{
		}

	}	// NullCaret
    
    /**
     * Null Document Filter.
     * 
     * @author Martin Handsteiner
     */
    public static class NullDocumentFilter extends DocumentFilter 
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Overwritten methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public void insertString(FilterBypass pFb, int pOffset, String pText, AttributeSet pAttr) throws BadLocationException 
    	{
    		// throw new BadLocationException(pFb.toString(), pOffset);
    		// Prevent insert;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public void replace(DocumentFilter.FilterBypass pFb, int pOffset, int pLength, String pText, AttributeSet pAttr) throws BadLocationException 
    	{
    		// throw new BadLocationException(pFb.toString(), pOffset);
    	    pFb.replace(pOffset, pLength, "", pAttr);
    	}
    	
    }	// NullDocumentFilter
    
}	// JVxChoice
