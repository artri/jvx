package research;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import com.sibvisions.rad.ui.swing.ext.JVxButton;

/**
 * An implementation of a "split" button.The left side acts like a normal button, right side has a jPopupMenu attached. <br />
 * This class raises two events.<br />
 * <ol>
 * <li>buttonClicked(e); //when the button is clicked</li>
 * <li>splitButtonClicked(e; //when the split part of the button is clicked) </li>
 * </ol>
 * You need to subscribe to SplitButtonActionListener to handle these events.<br /><br />
 * 
 * Use as you wish, but an acknowlegement would be appreciated, ;) <br /><br />
 * <b>Known Issue:</b><br />
 * The 'button part' of the splitbutton is being drawn without the border??? and this is only happening in CDE/Motif and Metal Look and Feels.
 * GTK+ and nimbus works perfect. No Idea why? if anybody could point out the mistake that'd be nice.My email naveedmurtuza[at]gmail.com<br /><br />
 * P.S. The fireXXX methods has been directly plagarized from JDK source code, and yes even the javadocs..;)<br /><br />
 * The border bug in metal L&F is now fixed. Thanks to Hervé Guillaume.
 * @author Naveed Quadri
 */
public class SimpleSplitButton extends JVxButton 
                               implements MouseMotionListener, 
                                          MouseListener 
{

    /** the margins without split area. */
    private Insets insOrig;
    
    /** the buffered arrow image. */
    private Image imgArrow;

    /** the split separator.. */
    private JSeparator separator;

    /** the menu. */
    private JPopupMenu menu;
    
    /** the split area with the arrow. */
    private Rectangle rectSplitArea;
    
    /** the default action if not listener is set. */
    private JMenuItem miDefault;
    
    private int iSplitWidth = 20;
    
    private int iArrowWidth = 8;
    
    /** whether the mouse cursor is in/over the split area. */
    private boolean bIsOverSplitArea = false;
    
    
    public SimpleSplitButton(String text) 
    {
        this(text, null);
    }
    
    public SimpleSplitButton(String text, Icon icon) 
    {
        super(text, icon);

        insOrig = getMargin();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin(Insets pInsets)
    {
        insOrig = pInsets;

        if (menu != null)
        {
            super.setMargin(new Insets(insOrig.top, insOrig.left, insOrig.bottom, insOrig.right + 20));
        }
        else
        {
            super.setMargin(pInsets);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        if (menu != null)
        {
            Dimension dimSize = getSize();
            
            rectSplitArea = new Rectangle(dimSize.width - iSplitWidth, 0, iSplitWidth, dimSize.height);
            
            //Separator
            
            BufferedImage img = new BufferedImage(4, dimSize.width, BufferedImage.TYPE_INT_ARGB);
            
            Graphics gr = img.getGraphics();
            
            if (separator == null)
            {
                separator = new JSeparator(JSeparator.VERTICAL);
                separator.setEnabled(isEnabled());
            }
            
            separator.setBounds(4, 4, 4, getSize().height - 8);
            separator.paint(gr);

            gr.dispose();
    
            g.drawImage(img, dimSize.width - iSplitWidth, 4, this);
            
            // Arrow
            
            if (imgArrow == null)
            {
                img = new BufferedImage(iArrowWidth, iArrowWidth, BufferedImage.TYPE_INT_ARGB);
                
                gr = img.getGraphics();
                
                if (isEnabled())
                {
                    gr.setColor(Color.black);
                }
                else
                {
                    gr.setColor(new Color(153, 153, 153));
                }
                
                gr.fillPolygon(new int[]{0, iArrowWidth, iArrowWidth / 2}, 
                               new int[] {0, 0, (iArrowWidth / 2)}, 
                               3);
                gr.dispose();
                
                imgArrow = img;
            }
            
            g.drawImage(imgArrow, dimSize.width - iSplitWidth - 1 + ((iSplitWidth - iArrowWidth) / 2), 
                        (dimSize.height - iArrowWidth / 2) / 2 + 1, this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean pEnabled)
    {
        super.setEnabled(pEnabled);
        
        if (separator != null)
        {
            separator.setEnabled(pEnabled);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireActionPerformed(ActionEvent pEvent) 
    {
        if (menu != null)
        {
            if (bIsOverSplitArea)
            {
                setMenuVisible(true);
                
                fireMenuActionPerformed(pEvent);
            }
            else
            {
                if (getActionListeners().length == 0)
                {
                    if (miDefault != null)
                    {
                        miDefault.doClick(0);
                    }
                }
                else
                {
                    super.fireActionPerformed(pEvent);
                }
            }
        }
        else
        {
            super.fireActionPerformed(pEvent);
        }
    }
    
    protected void fireMenuActionPerformed(ActionEvent pEvent)
    {
        Object[] listeners = listenerList.getListenerList();
        
        ActionEvent aev = null;
        
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == IMenuActionListener.class)
            {
                if (aev == null)
                {
                    String actionCommand = pEvent.getActionCommand();
                    
                    if (actionCommand == null)
                    {
                        actionCommand = getActionCommand();
                    }
                    
                    aev = new ActionEvent(SimpleSplitButton.this, ActionEvent.ACTION_PERFORMED, actionCommand, pEvent.getWhen(), pEvent.getModifiers());
                }
                
                ((IMenuActionListener)listeners[i + 1]).actionPerformed(aev);
            }
        }
    }
    
    protected void setMenuVisible(boolean pVisible)
    {
        if (menu != null)
        {
            if (pVisible)
            {
                Dimension dimPref = menu.getPreferredSize();
                
                int iWidth = getWidth();
                
                if (iWidth < dimPref.width)
                {
                    iWidth = dimPref.width;
                }
                
                menu.setPopupSize(iWidth, dimPref.height);
                
                menu.show(this, 0, getHeight());
            }
            else
            {
                menu.setVisible(false);            
            }
        }
    }
    
    public void setPopupMenu(JPopupMenu pMenu)
    {
        menu = pMenu;
        
        if (menu == null)
        {
            removeMouseMotionListener(this);
            removeMouseListener(this);

            rectSplitArea = null;
        }
        else
        {
            addMouseMotionListener(this);
            addMouseListener(this);
        }
        
        setMargin(insOrig);
    }
    
    public void setDefaultAction(JMenuItem pMenuItem)
    {
        miDefault = pMenuItem;
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
        if (rectSplitArea != null)
        {
            bIsOverSplitArea = false;
            
            repaint(rectSplitArea);
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        if (rectSplitArea != null)
        {
            bIsOverSplitArea = rectSplitArea.contains(e.getPoint());
            
            repaint(rectSplitArea);
        }
    }
    
    public void mouseDragged(MouseEvent e)
    {
    }
    
    public void addMenuActionListener(IMenuActionListener pListener)
    {
        listenerList.add(IMenuActionListener.class, pListener);
    }
    
    public void removeMenuActionListener(IMenuActionListener pListener)
    {
        listenerList.remove(IMenuActionListener.class, pListener);
    }
    
}
