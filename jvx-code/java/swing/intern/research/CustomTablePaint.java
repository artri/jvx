package research;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class CustomTablePaint extends JFrame
{
	public static void main(String[] args) throws Exception
	{
//		UIManager.setLookAndFeel(new WindowsLookAndFeel());
		
		new CustomTablePaint();
	}
	boolean bAllow = false;
	public CustomTablePaint()
	{
		setLayout(new BorderLayout());
		
		add(new Table());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		
		setVisible(true);
	}
	
	private JTextField[] header = new JTextField[4];
	private JTextField[] footer = new JTextField[4];

	public JTable tbl;
	
	public CustomScrollPane scpTable;
	
	JPanel panHeader = new JPanel(new CustomNullLayout());
	JPanel panFooter = new JPanel(new CustomNullLayout());

	private class Table extends JPanel
	                    implements PropertyChangeListener,
	                               TableColumnModelListener,
	                               MouseListener
	{
		private Component comFocus;		
		
		private Table()
		{
			tbl = new JTable(100, 4)
			{
				public void addNotify()
				{
					super.addNotify();

					JPanel panTest = new JPanel(new BorderLayout());
					panTest.add(tbl.getTableHeader(), BorderLayout.SOUTH);
					panTest.add(panHeader);
	
					scpTable.setColumnHeaderView(panTest);
					scpTable.setColumnFooterView(panFooter);
				}
				
				public void setTableHeader(JTableHeader header)
				{
					super.setTableHeader(header);
					
					header.addMouseListener(Table.this);
				}
			};
			
			tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			scpTable = new CustomScrollPane();
			scpTable.setViewportView(tbl);
			scpTable.setLayout(new ScrolLayout());
			
			setLayout(new BorderLayout());
			
			header[0] = new JTextField();
			header[1] = new JTextField();
			header[2] = new JTextField();
			header[3] = new JTextField();
			
			footer[0] = new JTextField();
			footer[1] = new JTextField();
			footer[2] = new JTextField();
			footer[3] = new JTextField();

			header[0].setText("A");
			footer[1].setText("FOOTER-B");
			
			panHeader.add(header[0]);
			panHeader.add(header[1]);
			panHeader.add(header[2]);
			panHeader.add(header[3]);
			
			panFooter.add(footer[0]);
			panFooter.add(footer[1]);
			panFooter.add(footer[2]);
			panFooter.add(footer[3]);

			tbl.getTableHeader().getColumnModel().getColumn(0).addPropertyChangeListener(this);
			tbl.getTableHeader().getColumnModel().getColumn(1).addPropertyChangeListener(this);
			tbl.getTableHeader().getColumnModel().getColumn(2).addPropertyChangeListener(this);
			tbl.getTableHeader().getColumnModel().getColumn(3).addPropertyChangeListener(this);

			tbl.getColumnModel().addColumnModelListener(this);

			add(scpTable);
			
			updateColumns();
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
			if ("width".equals(evt.getPropertyName()))
			{
				updateColumns();
			}
		}
		
		private void updateColumns()
		{
			JTableHeader head = tbl.getTableHeader();
			
			TableColumn col = head.getDraggedColumn();
			
			int iAdd = head.getDraggedDistance();
			int iOverlap;
			
			Component comp;
			Rectangle rect;
			
			for (int i = 0, anz = head.getColumnModel().getColumnCount(); i < anz; i++)
			{
				rect = head.getHeaderRect(i);
				
				iOverlap = 1;

				if (col == head.getColumnModel().getColumn(i))
				{
					comp = header[head.getColumnModel().getColumn(i).getModelIndex()];
					comp.setBounds(rect.x + iAdd - iOverlap, rect.y, rect.width + iOverlap, comp.getPreferredSize().height);
					
					panHeader.setComponentZOrder(comp, 0);
	
					
					comp = footer[head.getColumnModel().getColumn(i).getModelIndex()]; 
					comp.setBounds(rect.x + iAdd - iOverlap, rect.y, rect.width + iOverlap, comp.getPreferredSize().height);
					
					panFooter.setComponentZOrder(comp, 0);
				}
				else
				{
					comp = header[head.getColumnModel().getColumn(i).getModelIndex()];
					comp.setBounds(rect.x - iOverlap, rect.y, rect.width + iOverlap, comp.getPreferredSize().height);
	
					comp = footer[head.getColumnModel().getColumn(i).getModelIndex()]; 
					comp.setBounds(rect.x - iOverlap, rect.y, rect.width + iOverlap, comp.getPreferredSize().height);
				}
			}
		}

		public void columnAdded(TableColumnModelEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void columnMarginChanged(ChangeEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void columnMoved(TableColumnModelEvent e)
		{
			updateColumns();
		}

		public void columnRemoved(TableColumnModelEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void columnSelectionChanged(ListSelectionEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void mouseClicked(MouseEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e)
		{
			comFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			
			requestFocus();
		}

		public void mouseReleased(MouseEvent e)
		{
			if (comFocus != null)
			{
				comFocus.requestFocus();
			}
		}
	}
	
	class ScrolLayout extends ScrollPaneLayout
	{
		JViewport vpColFooter;

		public void addLayoutComponent(String name, Component comp)
		{
			if (CustomScrollPane.COLUMN_FOOTER.equals(name))
			{
				vpColFooter = (JViewport)addSingletonComponent(vpColFooter, comp);
			}
			else
			{
				super.addLayoutComponent(name, comp);
			}
		}
		
		public void removeLayoutComponent(Component comp)
		{
			if (comp == vpColFooter)
			{
				vpColFooter = null;
			}
			else
			{
				super.removeLayoutComponent(comp);
			}
		}
		
		public void layoutContainer(Container parent) 
	    {
			super.layoutContainer(parent);
			
			if ((vpColFooter != null) && (vpColFooter.isVisible())) 
			{
				Rectangle recViewport = viewport.getBounds();
				
				recViewport.height -= vpColFooter.getPreferredSize().height;
				
				viewport.setBounds(recViewport);
				
				Rectangle rectFooter = new Rectangle(recViewport.x, recViewport.y + recViewport.height, recViewport.width, vpColFooter.getPreferredSize().height);
				
				vpColFooter.setBounds(rectFooter);
				
				if (hsb != null)
				{
//					//ScrollBar nach unten schieben
//					Rectangle recHsb = hsb.getBounds();
//					
//					recHsb.y += vpColFooter.getPreferredSize().height;
//					
//					hsb.setBounds(recHsb);
				}
			}
			
			Rectangle bounds = vsb.getBounds();
			if (upperRight == null)
			{
				int iDiff = bounds.y - colHead.getBounds().y;
				
				bounds.y -= iDiff;
				bounds.height += iDiff;
				
				vsb.setBounds(bounds);
			}
	    }
		
		public Dimension preferredLayoutSize(Container parent)
		{
			Dimension dim = super.preferredLayoutSize(parent);
			

			if (vpColFooter != null && vpColFooter.isVisible())
			{
				dim.height += vpColFooter.getPreferredSize().height;
			}
			
			return dim;
		}
		
		public Dimension minimumLayoutSize(Container parent)
		{
			Dimension dim = super.minimumLayoutSize(parent);
			
			if ((vpColFooter != null) && vpColFooter.isVisible()) 
			{
			    Dimension size = vpColFooter.getMinimumSize();
			    dim.width = Math.max(dim.width, size.width);
			    dim.height += size.height;
			}			
			
			return dim;
		}
		
		public void syncWithScrollPane(JScrollPane sp) 
		{
			super.syncWithScrollPane(sp);
			
			if (sp instanceof CustomScrollPane)
			{
				vpColFooter = ((CustomScrollPane)sp).getColumnFooter();
			}
		}
		
		public JViewport getColumnFooter()
		{
			return vpColFooter;
		}
	}

	class CustomNullLayout implements LayoutManager
	{
		public void addLayoutComponent(String name, Component comp)
		{
		}

		public void removeLayoutComponent(Component comp)
		{
			// TODO Auto-generated method stub
			
		}

		public void layoutContainer(Container parent)
		{
//			Insets ins = parent.getInsets();
//			
//			for (int i = 0, anz = parent.getComponentCount(); i < anz; i++)
//			{
//				
//			}
		}

		public Dimension minimumLayoutSize(Container parent)
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Dimension preferredLayoutSize(Container parent)
		{
			Insets ins = parent.getInsets();
			
			Dimension dim = new Dimension();
			
			Rectangle recBounds;
			
			for (int i = 0, anz = parent.getComponentCount(); i < anz; i++)
			{
				recBounds = parent.getComponent(i).getBounds();
				
				dim.height = Math.max(dim.height, recBounds.height);
				dim.width += recBounds.width;
			}
			
			dim.width  += ins.left + ins.right;
			dim.height += ins.top + ins.bottom;
			
			return dim;
		}
	}
	
	public class CustomScrollPane extends JScrollPane
	                              implements ChangeListener
	{
	    public final static String COLUMN_FOOTER = "COLUMN_FOOTER";

	    private JViewport columnFooter;

	    
	    /**
	     * Returns the column footer.
	     * 
	     * @return the <code>columnFooter</code> property
	     * @see #setColumnFooter
	     */
	    public JViewport getColumnFooter() 
	    {
	        return columnFooter;
	    }

	    /**
	     * Removes the old columnFooter, if it exists.  If the new columnFooter
	     * isn't <code>null</code>, sync the x coordinate of the its viewPosition 
	     * with the viewport (if there is one) and then add it to the scrollpane.
	     * <p>
	     * Most applications will find it more convenient to use 
	     * <code>setColumnFooterView</code>
	     * to add a column footer component and its viewport to the scrollpane.
	     * 
	     * @see #getColumnFooter
	     * @see #setColumnFooterView
	     */
	    public void setColumnFooter(JViewport pColumnFooter) 
	    {
			JViewport old = getColumnHeader();
			
			this.columnFooter = pColumnFooter;
			
			if (pColumnFooter != null) 
			{
			    add(columnFooter, COLUMN_FOOTER);
			}
			else if (old != null) 
			{
			    remove(old);
			}

			viewport.addChangeListener(this);
			
			firePropertyChange("columnFooter", old, columnFooter);
	
			revalidate();
			repaint();
	    }

	    /**
	     * Creates a column-footer viewport if necessary, sets
	     * its view, and then adds the column-footer viewport
	     * to the scrollpane.  For example:
	     * <pre>
	     * CustomScrollPane scrollpane = new CustomScrollPane();
	     * scrollpane.setViewportView(myBigComponentToScroll);
	     * scrollpane.setColumnFooterView(myBigComponentsColumnFooter);
	     * </pre>
	     * 
	     * @see #setColumnFooter
	     * @see JViewport#setView
	     * 
	     * @param view the component to display as the column footer
	     */
	    public void setColumnFooterView(Component view) 
	    {
	        if (getColumnFooter() == null) 
	        {
	            setColumnFooter(createViewport());
	        }
	        
	        getColumnFooter().setView(view);
	    }

		public void stateChanged(ChangeEvent e)
		{
			if (columnFooter != null)
			{
				Point pos = viewport.getViewPosition();
				
				//don't scroll vertical!
				pos.y = 0;
				
				columnFooter.setViewPosition(pos);
			}
			
		}
	    
	}	// CustomTablePaint
	
}	// CustomTablePaint
