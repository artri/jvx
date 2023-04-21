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
 * 01.10.2008 - [HM] - creation
 */
package research;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import com.sibvisions.rad.ui.swing.ext.JVxButton;
import com.sibvisions.rad.ui.swing.ext.JVxCalendarPane;
import com.sibvisions.rad.ui.swing.ext.JVxDateCombo;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;

/**
 * Test implementation.
 * 
 * @author Martin Handsteiner
 */
public class FormLayoutTest extends JFrame
{

	/**
	 * Tests the FormLayout.
	 * @param args The arguments.
	 * @throws Exception exception.
	 */
	public static void main(String[] args) throws Exception
	{
		
    	javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		System.setProperty("sun.awt.noerasebackground", "true");

//		javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//    	javax.swing.UIManager.setLookAndFeel(com.sibvisions.rad.ui.swing.ext.plaf.smart.SmartLookAndFeel.class.getName());

		JDesignPanel designPane = new JDesignPanel();
		
		JVxFormLayout layout = new JVxFormLayout();
		designPane.setLayout(layout);
		
		JToggleButton bOk = new JToggleButton("Ok");
		JVxButton.setBorderOnMouseEntered(bOk, true);
		JVxButton.setAccelerator(bOk, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		
		JVxButton bCancel = new JVxButton("Cancel");
		bCancel.setBorderOnMouseEntered(true);
		bCancel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		
		JButton bCenter1 = new JButton("C  1");
		JButton bCenter2 = new JButton("C  2");
		
		JVxCalendarPane bCenter3 = new JVxCalendarPane();
//		bCenter3.setLocale(Locale.FRENCH);
		
//		DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT));
		//DateFormatter dateFormatter = new DateFormatter(new SimpleDateFormat("dd.MMMM.yyyy HH:mm"));
		DefaultFormatterFactory dateFactory = new DefaultFormatterFactory();
		JFormattedTextField field = new JFormattedTextField();
		field.setFormatterFactory(dateFactory);
		
		JVxDateCombo bCombo = new JVxDateCombo();
//		bCombo.setEditorComponent(field);
//		bCombo.setPopupComponent(new JVxCalendarPane());

		JComboBox box = new JComboBox(new Object[] {"Hallo", "Herb!"});
		box.setEditable(true);
	
		
		MaskFormatter formatter = new MaskFormatter("###-####");
		formatter.setPlaceholderCharacter('_');

		//DefaultFormatterFactory numberFactory = new DefaultFormatterFactory(formatter);		
		
		JFormattedTextField editor = new JFormattedTextField();
		//editor.setFormatterFactory(numberFactory);
		
		designPane.add(bOk, null);
		designPane.add(bCancel, null);
		designPane.add(bCenter1, null);
		designPane.add(bCenter2, null);
		designPane.add(bCenter3, null);
		designPane.add(bCombo, JVxFormLayout.NEWLINE);
		designPane.add(box, JVxFormLayout.NEWLINE);
		designPane.add(editor, JVxFormLayout.NEWLINE);
		
		JVxFormLayout.Constraint cOk = layout.getConstraint(bOk);
		JVxFormLayout.Constraint cCancel = layout.getConstraint(bCancel);
		JVxFormLayout.Constraint cCenter1 = layout.getConstraint(bCenter1);
		JVxFormLayout.Constraint cCenter2 = layout.getConstraint(bCenter2);
		JVxFormLayout.Constraint cCenter3 = layout.getConstraint(bCenter3);

		JVxFormLayout.Constraint cCombo = layout.getConstraint(bCombo);
		cCombo.setRightAnchor(null);
		
		// Cancel Button should be aligned at right Border
		cCancel.setRightAnchor(layout.getRightMarginAnchor());
		cCancel.setLeftAnchor(null);
		
		// Center1 Button should be aligned centered below Ok Button
		cCenter1.setLeftAnchor(new JVxFormLayout.Anchor(cOk.getLeftAnchor()));
		cCenter1.setRightAnchor(new JVxFormLayout.Anchor(cOk.getRightAnchor()));
		// Center2 Button should be aligned centered below Cancel Button
		cCenter2.setLeftAnchor(new JVxFormLayout.Anchor(cCancel.getLeftAnchor()));
		cCenter2.setRightAnchor(new JVxFormLayout.Anchor(cCancel.getRightAnchor()));
		// Center3 Button should be aligned centered in Screen
//		cCenter3.setLeftAnchor(new JVxFormLayout.Anchor(cCancel.getLeftAnchor()));
//		cCenter3.setRightAnchor(new JVxFormLayout.Anchor(cCancel.getRightAnchor()));
		cCenter3.setLeftAnchor(layout.getLeftMarginAnchor());
		cCenter3.setRightAnchor(layout.getRightMarginAnchor());
		
		FormLayoutTest frame = new FormLayoutTest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(designPane, BorderLayout.CENTER);
		
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	/**
	 * DesignPanel.
	 */
	public static class JDesignPanel extends JPanel
	{
		private static final Cursor DEFAULT = null;
		private static final Cursor LEFT_RIGHT = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
		private static final Cursor TOP_BOTTOM = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
		
		private JVxFormLayout.Anchor[] hAnchors = new JVxFormLayout.Anchor[0];
		private JVxFormLayout.Anchor[] vAnchors = new JVxFormLayout.Anchor[0];
		
		private JVxFormLayout.Anchor dragAnchor = null;
		private int dragDifference;
		
		private int y;
		
		public JDesignPanel()
		{
			enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
		}
		
		public void drawXArrow(Graphics pGraphics, int pX1, int pX2, boolean pAutoSize, boolean pRelative)
		{
			if (pAutoSize)
			{
				if (pRelative)
				{
					pGraphics.setColor(Color.red);
				}
				else
				{
					pGraphics.setColor(Color.magenta);
				}
			}
			else
			{
				if (pRelative)
				{
					pGraphics.setColor(Color.cyan);
				}
				else
				{
					pGraphics.setColor(Color.green);
				}
			}
			pGraphics.drawLine(pX1, y, pX2, y);
			pGraphics.drawLine(pX2 - 1, y - 1, pX2 + 1, y - 1);
			pGraphics.drawLine(pX2 - 1, y, pX2 + 1, y);
			pGraphics.drawLine(pX2 - 1, y + 1, pX2 + 1, y + 1);
			y += 4;
		}
		
		public void paintComponent(Graphics pGraphics)
		{
			super.paintComponent(pGraphics);
			
			LayoutManager layout = getLayout();
			if (layout instanceof JVxFormLayout)
			{
				JVxFormLayout formLayout = (JVxFormLayout)layout;
				
				Insets ins = getInsets();
				Dimension size = getSize();
				
				y = 5;
				
				hAnchors = formLayout.getHorizontalAnchors();
				pGraphics.setColor(Color.blue);
				for (int i = 0; i < hAnchors.length; i++)
				{
					if (hAnchors[i].isRelative())
					{
						pGraphics.setColor(Color.red);
					}
					else
					{
						pGraphics.setColor(Color.blue);
					}
					int pos = hAnchors[i].getAbsolutePosition();
					pGraphics.drawLine(pos, ins.top, pos, size.height - ins.top);
					if (hAnchors[i].getRelatedAnchor() != null)
					{
						drawXArrow(pGraphics, pos, hAnchors[i].getRelatedAnchor().getAbsolutePosition(), hAnchors[i].isAutoSize(), hAnchors[i].isRelative());
					}
				}
				
				vAnchors = formLayout.getVerticalAnchors();
				pGraphics.setColor(Color.blue);
				for (int i = 0; i < vAnchors.length; i++)
				{
					if (vAnchors[i].isRelative())
					{
						pGraphics.setColor(Color.red);
					}
					else
					{
						pGraphics.setColor(Color.blue);
					}
					int pos = vAnchors[i].getAbsolutePosition();
					pGraphics.drawLine(ins.left, pos, size.width - ins.right, pos);
				}
				
			}
		}

		private int findAnchor(JVxFormLayout.Anchor[] pAnchors, int pPosition)
		{
			for (int i = 0; i < pAnchors.length; i++)
			{
				int pos = pAnchors[i].getAbsolutePosition();
				
				if (pPosition >= pos - 1 && pPosition <= pos + 1)
				{
					return i;
				}
			}
			return -1;
		}
		
		public void processMouseEvent(MouseEvent pEvent)
		{
			if (pEvent.getID() == MouseEvent.MOUSE_PRESSED)
			{
				int index = findAnchor(hAnchors, pEvent.getX());
				if (index < 0)
				{
					index = findAnchor(vAnchors, pEvent.getY());
					if (index < 0)
					{
						dragAnchor = null;
					}
					else
					{
						dragAnchor = vAnchors[index];
						if (dragAnchor.isRelative())
						{
							dragAnchor = null;
						}
						else
						{
							dragDifference = dragAnchor.getPosition() - pEvent.getY();
						}
					}
				}
				else
				{
					dragAnchor = hAnchors[index];
					if (dragAnchor.isRelative())
					{
						dragAnchor = null;
					}
					else
					{
						dragDifference = dragAnchor.getPosition() - pEvent.getX();
					}
				}
			}
			else if (pEvent.getID() == MouseEvent.MOUSE_RELEASED)
			{
				if (dragAnchor != null)
				{
					if (dragAnchor.getOrientation() == JVxFormLayout.Anchor.HORIZONTAL)
					{
						dragAnchor.setPosition(pEvent.getX() + dragDifference);
						dragAnchor.setAutoSize(false);
					}
					else
					{
						dragAnchor.setPosition(pEvent.getY() + dragDifference);
						dragAnchor.setAutoSize(false);
					}
					dragAnchor = null;
					repaint();
					invalidate();
					validate();
				}
			}
			else if (pEvent.getID() == MouseEvent.MOUSE_CLICKED)
			{
				if (pEvent.getClickCount() == 2)
				{
					int index = findAnchor(hAnchors, pEvent.getX());
					if (index < 0)
					{
						index = findAnchor(vAnchors, pEvent.getY());
						if (index >= 0)
						{
							vAnchors[index].setAutoSize(true);
						}
					}
					else
					{
						hAnchors[index].setAutoSize(true);
					}
					repaint();
					invalidate();
					validate();
				}
			}
		}
		
		public void processMouseMotionEvent(MouseEvent pEvent)
		{
			if (pEvent.getID() == MouseEvent.MOUSE_MOVED)
			{
				
				int index = findAnchor(hAnchors, pEvent.getX());
				if (index < 0 || hAnchors[index].isRelative())
				{
					index = findAnchor(vAnchors, pEvent.getY());
					if (index < 0 || vAnchors[index].isRelative())
					{
						setCursor(DEFAULT);
					}
					else
					{
						setCursor(TOP_BOTTOM);
					}
				}
				else
				{
					setCursor(LEFT_RIGHT);
				}
			}
			else if (pEvent.getID() == MouseEvent.MOUSE_DRAGGED)
			{
				if (dragAnchor != null)
				{
					if (dragAnchor.getOrientation() == JVxFormLayout.Anchor.HORIZONTAL)
					{
						dragAnchor.setPosition(pEvent.getX() + dragDifference);
						dragAnchor.setAutoSize(false);
					}
					else
					{
						dragAnchor.setPosition(pEvent.getY() + dragDifference);
						dragAnchor.setAutoSize(false);
					}
					repaint();
					invalidate();
					validate();
				}
			}
			super.processMouseEvent(pEvent);
			
			
		}
		
	}

}	// FormLayoutTest
