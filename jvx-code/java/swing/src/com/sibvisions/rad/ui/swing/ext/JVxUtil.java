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
 * 14.11.2008 - [JR] - creation
 * 04.11.2009 - [JR] - moved image scaling to ImageUtil
 * 28.02.2011 - [JR] - setImageMapping: clear image cache
 * 13.05.2011 - [JR] - setGlobalCursor: check if change is necessary 
 * 25.08.2011 - [JR] - #465: clipboard actions via JNLP services
 * 31.08.2012 - [JR] - fixed NPEs in static {} when LaF does not support used colors
 * 24.09.2013 - [JR] - getIcon(String, byte[]) -> fallback to getIcon(String)
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.AWTPermission;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.AllPermission;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.WeakHashMap;

import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.IColor;
import javax.rad.ui.InvokeLaterThread;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.LauncherUtil;
import com.sibvisions.rad.ui.Webstart;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxDateCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxNumberCellEditor;
import com.sibvisions.rad.ui.swing.ext.celleditor.JVxTextCellEditor;
import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.rad.ui.swing.impl.component.SwingTextComponent;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.imageio.MetaDataImage;

/**
 * The <code>JVxUtil</code> is a utility class with often used
 * functionality encapsulated in useful methods.
 * 
 * @author Ren� Jahn
 */
public final class JVxUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Clipboard Permission. */
	private static Permission permClipboard;

	/** All Permission. */
	private static Permission permAll;
	
    /** the default number cell editor. */
    private static final ICellEditor NUMBER_CELL_EDITOR = new JVxNumberCellEditor(); 
    
    /** the default date cell editor. */
    private static final ICellEditor DATE_CELL_EDITOR = new JVxDateCellEditor(); 
    
    /** the default text cell editor. */
    private static final ICellEditor TEXT_CELL_EDITOR = new JVxTextCellEditor();
    
    /** The default cell editors. */
    private static Hashtable<Class<?>, ICellEditor> defaultCellEditors = new Hashtable<Class<?>, ICellEditor>();

    /** the cache for created images (saves memory). */
    private static Hashtable<String, ImageIcon> htImageCache = new Hashtable<String, ImageIcon>();
    
    /** the cache for created images (increases speed). */
    private static WeakHashMap<byte[], WeakReference<ImageIcon>> htCreateImageCache = new WeakHashMap<byte[], WeakReference<ImageIcon>>();
    
    /** the mapping names for images. */
    private static Hashtable<String, String> htImageMapping = new Hashtable<String, String>();
    
    /** the mapping names for images. */
    private static WeakReference<RootPaneContainer> rootPaneContainer = null;

    /** the system color mapping. */
    private static Hashtable<String, Color> systemColors = new Hashtable<String, Color>();
    
    /** The components to validate. */ 
    private static ArrayList<Component> componentsToValidate = new ArrayList<Component>();
    
    /** true, if it is a delayed validate. */
    private static boolean delayedRevalidate = false;
    
    /** The window that will be active soon. */
    private static Window windowWillBeActiveSoon = null;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static 
    {
        defaultCellEditors.put(Number.class, NUMBER_CELL_EDITOR);
        defaultCellEditors.put(Date.class, DATE_CELL_EDITOR);

        Color colSelBack = (Color)UIManager.getDefaults().get("Table.selectionBackground");
        
        if (colSelBack == null)
        {
            colSelBack = new Color(0, 0, 255);
        }
        
        Color colSelFore = (Color)UIManager.getDefaults().get("Table.selectionForeground");
        
        if (colSelFore == null)
        {
            colSelFore = new Color(255, 255, 255);
        }
        
        systemColors.put(IColor.CONTROL_BACKGROUND,                     CommonUtil.nvl((Color)UIManager.getDefaults().get("Table.background"), new Color(255, 255, 255)));
        systemColors.put(IColor.CONTROL_ALTERNATE_BACKGROUND,           new Color(208, 208, 208));
        systemColors.put(IColor.CONTROL_FOREGROUND,                     CommonUtil.nvl((Color)UIManager.getDefaults().get("Table.foreground"), new Color(0, 0, 0)));
        systemColors.put(IColor.CONTROL_ACTIVE_SELECTION_BACKGROUND,    colSelBack);
        systemColors.put(IColor.CONTROL_ACTIVE_SELECTION_FOREGROUND,    colSelFore);
        systemColors.put(IColor.CONTROL_INACTIVE_SELECTION_BACKGROUND,  colSelBack);
        systemColors.put(IColor.CONTROL_INACTIVE_SELECTION_FOREGROUND,  colSelFore);
        systemColors.put(IColor.CONTROL_MANDATORY_BACKGROUND,           new Color(255, 244, 210));
        systemColors.put(IColor.CONTROL_READ_ONLY_BACKGROUND,           CommonUtil.nvl((Color)UIManager.getDefaults().get("Panel.background"), new Color(204, 204, 204)));
        systemColors.put(IColor.INVALID_EDITOR_BACKGROUND,              new Color(209, 51, 51));
        
        try
        {
        	permClipboard = new AWTPermission("accessClipboard");
        }
        catch (Throwable th)
        {
        	permClipboard = null;
        }
        
        try
        {
        	permAll = new AllPermission();
        }
        catch (Throwable th)
        {
        	permAll = null;
        }
    }
    
    /**
     * Invisible constructor, because the <code>JVxUtil</code> class is a utility class.
     */
    protected JVxUtil()
    {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Gets the preferred size of a component. The size is between the
     * minimum and maximum size.
     * 
     * @param pComponent the component
     * @return the preferred size dependent of the minimum and maximum size
     */
    public static Dimension getPreferredSize(Component pComponent)
    {
        Dimension dimMin  = pComponent.getMinimumSize();
        Dimension dimMax  = pComponent.getMaximumSize();
        Dimension dimPref = pComponent.getPreferredSize();
        if (pComponent instanceof JLabel)
        {
            JLabel label = (JLabel)pComponent;
            Container parent = label.getParent();
            Font font = label.getFont();
            String text = label.getText();
            if (text == null)
            {
                text = "";
            }
            
            int key = text.hashCode();
            if (font != null)
            {
                key += font.hashCode() * 7;
            }
            
            Integer lastKey = (Integer)label.getClientProperty("lastPreferredCalculationKey");
            
            if (parent != null)
            {
                LayoutManager layout = parent.getLayout();
                
                key += parent.hashCode() * 13;
                if (layout != null)
                {
                    key += layout.hashCode() * 31;
                }
                
                if (text.startsWith("<html>"))
                {
                    int oldWidth = label.getWidth();
                    int oldHeight = label.getHeight();
                    
                    int width = oldWidth;
                    if (lastKey == null || key != lastKey.intValue())
                    {
                        width = 0;
                        if (layout != null)
                        {
                            oldWidth = 0;
                        }
                    }
                    if (width <= 0)
                    {
                        width = Short.MAX_VALUE;
                    }
                    Graphics gr = label.getGraphics();
                    if (gr != null)
                    {
                        boolean isFontSet = label.isFontSet();
                        label.setFont(font); // It is necessary to directly set the font, otherwise, the preferred size is calculated with wrong font.
                        label.setSize(width, oldHeight);
                        // setFont seams to make this code unnecessary
//                        gr = gr.create(0,  0, width, 1);
//                        gr.setFont(font);
//                        label.getUI().paint(gr, label);
//                        gr.dispose();
                        dimPref = label.getPreferredSize();
                        if (width < dimPref.width)
                        {
                            dimPref.width = width;
                        }
                        if (!isFontSet)
                        {
                            label.setFont(null);
                        }
                        label.setSize(oldWidth, oldHeight);
                    }
                }
            }
            if (lastKey == null || key != lastKey.intValue())
            {
                label.putClientProperty("lastPreferredCalculationKey", Integer.valueOf(key));
            }
        }
        
        int iWidth = dimPref.width;
        int iHeight = dimPref.height;
        
        if (pComponent.isMinimumSizeSet())
        {
            if (dimMin.width > iWidth)
            {
                iWidth = dimMin.width;
            }
            
            if (dimMin.height > iHeight)
            {
                iHeight = dimMin.height;
            }
        }
        
        if (pComponent.isMaximumSizeSet())
        {
            if (dimMax.width < iWidth)
            {
                iWidth = dimMax.width;
            }
            
            if (dimMax.height < iHeight)
            {
                iHeight = dimMax.height;
            }
        }
        
        return new Dimension(iWidth, iHeight);
    }
    
    /**
     * Gets the minimum size of a component. It analyses BorderLayout in a correct way.
     * 
     * @param pComponent the component
     * @return the minimum size
     */
    public static Dimension getMinimumSize(Component pComponent)
    {
        Dimension minimumSize;
        
        if (pComponent.isMinimumSizeSet())
        {
            minimumSize = pComponent.getMinimumSize();
        }
        else if (pComponent instanceof JScrollPane
                || pComponent instanceof JTabbedPane
                || pComponent instanceof JSplitPane)
        {
            minimumSize = ((JComponent)pComponent).getMinimumSize();
        }
        else if (pComponent instanceof JRootPane)
        {
            minimumSize = getMinimumSize(((JRootPane)pComponent).getContentPane());
        }
        else if (pComponent instanceof JPanel)
        {
            LayoutManager layout = ((JPanel)pComponent).getLayout();
            
            if (layout instanceof JVxFormLayout)
            {
                minimumSize = ((JVxFormLayout)layout).minimumLayoutSize((JPanel)pComponent);
            }
            else if (layout instanceof JVxBorderLayout)
            {
                minimumSize = ((JVxBorderLayout)layout).minimumLayoutSize((JPanel)pComponent);
            }
            else
            {
                minimumSize = getPreferredSize(pComponent);
            }
        }
        else
        {
            minimumSize = getPreferredSize(pComponent);
            if (minimumSize.width > 50  && pComponent instanceof JLabel)
            {
                String text = ((JLabel)pComponent).getText();
                if (text != null && text.startsWith("<html>"))
                {
                    minimumSize.width = 50;
                }
            }
        }
        if (pComponent.isMaximumSizeSet())
        {
            Dimension dimMax  = pComponent.getMaximumSize();
            if (dimMax.width < minimumSize.width)
            {
                minimumSize.width = dimMax.width;
            }
            
            if (dimMax.height < minimumSize.height)
            {
                minimumSize.height = dimMax.height;
            }
        }
        
        return minimumSize;
    }

    /**
     * set the window, that will get the focus soon.
     * @param pWindow the window.
     */
    public static void setWindowWillBeActiveSoon(Window pWindow)
    {
    	boolean firstWindow = windowWillBeActiveSoon == null;
    	
    	windowWillBeActiveSoon = pWindow;
    	
    	if (firstWindow)
    	{
    		SwingUtilities.invokeLater(new Runnable() 
    		{
				public void run() 
				{
					windowWillBeActiveSoon = null;
				}
			});
    	}
    }
    
    /**
     * Get the window, that will get the focus soon.
     * @return pWindow the window.
     */
    public static Window getWindowWillBeActiveSoon()
    {
    	return windowWillBeActiveSoon;
    }
    
    /**
     * Searches the parent tree until a parent without layout was found. The "first" parent
     * with a layout will be validated. Through the parent validate, all sub components of the parent
     * will be validated. All components will be validated via {@link SwingUtilities#invokeLater(Runnable)}.
     *  
     * @param pComponent the start component
     */
    public static void revalidateAllDelayed(Component pComponent)
    {
        revalidateAll(pComponent, false);
    }
    
    /**
     * Searches the parent tree until a parent without layout was found. The "first" parent
     * with a layout will be validated. Through the parent validate, all sub components of the parent
     * will be validated. All components will be validated immediately.
     *  
     * @param pComponent the start component
     */
    public static void revalidateAll(Component pComponent)
    {
        revalidateAll(pComponent, true);
    }
    
    /**
     * Is true, if it is a delayed validate. 
     * 
     * @return true, if it is a delayed validate.
     */
    public static boolean isDelayedRevalidate()
    {
        return delayedRevalidate;
    }
    
    /**
     * Searches the parent tree until a parent without layout was found. The "first" parent
     * with a layout will be validated. Through the parent validation, all sub components of the parent
     * will be validated.
     *  
     * @param pComponent the start component
     * @param pImmediate <code>true</code> to validate immediate or <code>false</code> to use 
     *                   {@link SwingUtilities#invokeLater(Runnable)} for validation
     */
    private static void revalidateAll(Component pComponent, boolean pImmediate)
    {
        Component comp = pComponent;
        
        while (comp != null && comp.getParent() != null && comp.getParent().getLayout() != null)
        {
            comp = comp.getParent();
        }
        
        if (comp != null && comp != pComponent)
        {
            if (pImmediate)
            {
                comp.validate();
            }
            else
            {
                boolean startValidation = componentsToValidate.size() == 0;
                
                if (!componentsToValidate.contains(comp))
                {
                    componentsToValidate.add(comp);
                }
                if (startValidation)
                {
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            delayedRevalidate = true;
                                    
                            try
                            {
                                while (componentsToValidate.size() > 0)
                                {
                                    componentsToValidate.remove(0).validate();
                                }
                            }
                            finally
                            {
                                delayedRevalidate = false;
                            }
                        }
                    });
                }
            }
        }
    }
    
    /**
     * Creates a new {@link ImageIcon} from the given name.
     * 
     * @param pImageName the name of the image to get.
     * @return the {@link ImageIcon} for the specified name, {@code null} if it
     *         could not be created,
     */
    public static ImageIcon createIcon(String pImageName)
    {
        if (pImageName == null)
        {
            return null;
        }
        
        try
        {
            ImageIcon icon = null;
            
            if (pImageName.startsWith("FontAwesome."))
            {
                icon = new JVxFontAwesomeIcon(pImageName.substring(12));
            }
            else if (pImageName.startsWith("JIconFont."))
            {
            	icon = JVxFontIcon.createIcon(pImageName.substring(10));
            }
            else
            {
            	HashMap<String, String> hmpProp = LauncherUtil.splitImageProperties(pImageName);
                
                InputStream isImage = ResourceUtil.getResourceAsStream(hmpProp.remove("name"));
                
                if (isImage != null)
                {
                	byte[] byData = FileUtil.getContent(isImage);
                	
                    icon = new MetaDataImageIcon(pImageName, byData, Toolkit.getDefaultToolkit().createImage(byData));
                }
            }
            
            return icon;
        }
        catch (Exception e)
        {
        	LoggerFactory.getInstance(JVxUtil.class).error(e);
        	
            return null;
        }
    }
    
    /**
     * Creates an {@link ImageIcon} from the given {@link Icon}.
     * 
     * @param pIcon the icon
     * @return the image icon
     */
    public static ImageIcon iconToImage(Icon pIcon) 
    {
		if (pIcon instanceof ImageIcon) 
		{
			return (ImageIcon) pIcon;
		} 
		else 
		{
			int w = pIcon.getIconWidth();
			int h = pIcon.getIconHeight();

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h);

			Graphics2D g = image.createGraphics();
			pIcon.paintIcon(null, g, 0, 0);

			g.dispose();

			return new ImageIcon(image);
		}
    }        
    
    /**
     * Gets the default <code>ICellEditor</code> for the given class.
     * This function should always return an editor.
     * It should look for best matching editor with Class.isAssignableFrom.
     *
     * @param pClass the class type to be edited.
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static ICellEditor getDefaultCellEditor(Class<?> pClass)
    {
        if (pClass == null)
        {
            return TEXT_CELL_EDITOR;
        }
        else
        {
            ICellEditor cellEditor = defaultCellEditors.get(pClass);
            
            if (cellEditor == null)
            {
                return getDefaultCellEditor(pClass.getSuperclass());
            }
            else
            {
                return cellEditor;
            }
        }
    }
    
    /**
     * Sets the default <code>ICellEditor</code> for the given class.
     * This function should always return an editor.
     * It should look for best matching editor with Class.isAssignableFrom.
     * If the given ICellEditor is null, it is removed as editor for the given class.
     *
     * @param pClass the class type to be edited.
     * @param pCellEditor the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public static void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor)
    {
        if (pCellEditor == null)
        {
            defaultCellEditors.remove(pClass);
        }
        else
        {
            defaultCellEditors.put(pClass, pCellEditor);
        }
    }
    
    /**
     * Gets the system color.
     *
     * @param pType the color type
     * @return the system color
     */
    public static Color getSystemColor(String pType)
    {
        return systemColors.get(pType);
    }
    
    /**
     * Sets the system color.
     *
     * @param pType the color type
     * @param pSystemColor the system color
     */
    public static void setSystemColor(String pType, Color pSystemColor)
    {
        if (pSystemColor == null)
        {
            systemColors.remove(pType);
        }
        else
        {
            systemColors.put(pType, pSystemColor);
        }
    }
    
    /**
     * Gets the average color from pColor1 and pColor2.
     * 
     * @param pColor1 the first Color.
     * @param pColor2 the second Color.
     * @return the average color from pColor1 and pColor2.
     */
    public static Color getAverageColor(Color pColor1, Color pColor2)
    {
        if (pColor1 == null)
        {
            return pColor2;
        }
        else if (pColor2 == null)
        {
            return pColor1;
        }
        else
        {
            int r = (pColor1.getRed() + pColor2.getRed()) / 2;
            int g = (pColor1.getGreen() + pColor2.getGreen()) / 2;
            int b = (pColor1.getBlue() + pColor2.getBlue()) / 2;
            return new Color(r, g, b);
        }
    }
    
    /**
     * Gets an image from the resource bundle.
     * 
     * @param pImageName the icon resource name
     * @return the {@link Image} or <code>null</code> if the resource is not available 
     */
    public static Image getImage(String pImageName)
    {
        ImageIcon ico = getIcon(pImageName);
        
        if (ico == null)
        {
            return null;
        }
        else
        {
            return ico.getImage();
        }
    }   

    /**
     * Gets an ImageIcon from the resource bundle.
     * 
     * @param pImageName the icon resource name
     * @return the {@link java.awt.Image} or <code>null</code> if the resource is not available 
     */
    public static ImageIcon getIcon(String pImageName)
    {
        // don't load null resources
        if (pImageName == null)
        {
            return null;
        }

        pImageName = getImageMapping(pImageName);
        
        ImageIcon icon = htImageCache.get(pImageName);

        if (icon == null)
        {
            icon = createIcon(pImageName);
            
            if (icon != null)
            {
                htImageCache.put(pImageName, icon);
            }
        }
        
        return icon;
    }

    /**
     * Gets an ImageIcon from the resource bundle.
     * 
     * @param pImageName the image name (used for cache mechanism) or <code>null</code> if
     *              the image should not be cached
     * @param pData the image byte data
     * @return the {@link java.awt.Image} or <code>null</code> if the resource is not available 
     */
    public static Image getImage(String pImageName, byte[] pData)
    {
        ImageIcon ico = getIcon(pImageName, pData);
        
        if (ico == null)
        {
            return null;
        }
        else
        {
            return ico.getImage();
        }
    }
    
    /**
     * Gets an ImageIcon from the resource bundle.
     * 
     * @param pImageName the image name (used for cache mechanism) or <code>null</code> if
     *              the image should not be cached
     * @param pData the image byte data
     * @return the {@link java.awt.Image} or <code>null</code> if the resource is not available 
     */
    public static ImageIcon getIcon(String pImageName, byte[] pData)
    {
        if (pData == null)
        {
            //maybe the image name is mapped
            return getIcon(pImageName);
        }
        
        WeakReference<ImageIcon> wrIcon = htCreateImageCache.get(pData);
        ImageIcon icon = null;
        if (wrIcon != null)
        {
            icon = wrIcon.get();
        }
        
        if (icon == null)
        {
            icon = new MetaDataImageIcon(pImageName, pData, Toolkit.getDefaultToolkit().createImage(pData));
            
            htCreateImageCache.put(pData, new WeakReference(icon));
        }
        
        if (pImageName != null)
        {
            htImageCache.put(pImageName, icon);
        }
        
        return icon;
    }
    
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @return the image name.
     */
    public static String getImageMapping(String pMappingName)
    {
    	if (pMappingName == null)
    	{
    		return null;
    	}
    	
        String imageName = htImageMapping.get(pMappingName);
        
        if (imageName == null)
        {
            return pMappingName;
        }
        else
        {
            return imageName;
        }
    }
    
    /**
     * Sets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @param pImageName the image name.
     */
    public static void setImageMapping(String pMappingName, String pImageName)
    {
        //clear cache because it is possible that the image has changed

        if (pMappingName == null)
        {
            if (pImageName != null)
            {
                htImageCache.remove(pImageName);
            }
        }
        else
        {
            if (pImageName == null)
            {
                htImageMapping.remove(pMappingName);
            }
            else
            {
                htImageMapping.put(pMappingName, pImageName);
            }
        }
    }
    
    /**
     * Gets all used mapping names.
     * 
     * @return the mapping names.
     */
    public static String[] getImageMappingNames()
    {
        return htImageMapping.keySet().toArray(new String[htImageMapping.size()]);
    }
    
    /**
     * Gets the application global cursor.
     * 
     * @param pComponent the component.
     * @return the cursor.
     */
    public static Cursor getGlobalCursor(Component pComponent)
    {
        RootPaneContainer rootPane = null;
        
        while (pComponent != null)
        {
            if (pComponent instanceof RootPaneContainer)
            {
                rootPane = (RootPaneContainer)pComponent;
            }
            pComponent = pComponent.getParent();
        }
        
        if (rootPaneContainer != null)
        {
            if (rootPane == null)
            {
                rootPane = rootPaneContainer.get();
            }
        }
        if (rootPane != null)
        {
            Component comp = rootPane.getGlassPane();
            
            return comp.getCursor();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Sets an application global cursor.
     * 
     * @param pComponent the component.
     * @param pCursor the cursor.
     */
    public static void setGlobalCursor(Component pComponent, Cursor pCursor)
    {
        RootPaneContainer rootPane = null;
        
        while (pComponent != null)
        {
            if (pComponent instanceof RootPaneContainer)
            {
                rootPane = (RootPaneContainer)pComponent;
            }
            pComponent = pComponent.getParent();
        }
        
        if (rootPaneContainer != null)
        {
            if (rootPane == null)
            {
                rootPane = rootPaneContainer.get();
            }
            else
            {
                RootPaneContainer oldRootPane = rootPaneContainer.get();
                if (oldRootPane != null)
                {
                    Component comp = oldRootPane.getGlassPane();
                    comp.setVisible(false);
                    comp.setCursor(null);
                }
                rootPaneContainer = null;
            }
                
        }
        if (rootPane != null)
        {
            Component comp = rootPane.getGlassPane();
            
            if (pCursor == null || pCursor.getType() == Cursor.DEFAULT_CURSOR)
            {
                if (comp.isVisible())
                {
                    comp.setVisible(false);
                }
                
                Cursor cur = comp.getCursor();
                if (cur != null && cur.getType() != Cursor.DEFAULT_CURSOR)
                {
                    comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
            else
            {
                if (!comp.isVisible())
                {
                    comp.setVisible(true);
                }
                
                Cursor cur = comp.getCursor();
                
                if (cur == null || cur.getType() != pCursor.getType())
                {
                    comp.setCursor(pCursor);
                }
            }
            
            rootPaneContainer = new WeakReference(rootPane);
        }
    }

    /**
     * Uses InvokerLaterThread, to invoke guarantee even in another Thread, that it will be invoked later.
     * @param pRunnable the Runnable
     */
    public static void invokeLater(Runnable pRunnable)
    {
        if (InvokeLaterThread.isInvokeLaterQueueEnabled())
        {
    		((InvokeLaterThread)Thread.currentThread()).invokeLater(pRunnable);
        }
        else
        {
            SwingUtilities.invokeLater(pRunnable);
        }
    }
    
    /**
     * Installs copy/paste/cut actions for the given text component. Since 1.6.0 u24 it is not
     * allowed to access the clipboard with standard swing controls
     * 
     * @param pComponent the text component
     */
    public static void installActions(final JTextComponent pComponent)
    {
        if (Webstart.isJnlp())
        {
            boolean bAllowed;
            
            try
            {
            	Permission perm = permClipboard;
            	
                if (perm == null) 
                {
                    perm = permAll;
                }
                
                System.getSecurityManager().checkPermission(perm);            	
                
                bAllowed = true;
            }
            catch (Exception se)
            {
                bAllowed = false;
            }
            
            if (!bAllowed)
            {
                //JRE versions <= 1.6.0_24 are allowed to copy to the clipboard
                String sVersion = System.getProperty("java.version");
                
                if (sVersion.compareTo("1.6.0_24") >= 0)
                {
                    ActionMap map = pComponent.getActionMap();
                    
                    Action acCopy = map.get(DefaultEditorKit.copyAction);
                    
                    if (!(acCopy instanceof ForwardAction))
                    {
                        map.put(DefaultEditorKit.copyAction, new ForwardAction(acCopy)
                        {
                            public void actionPerformed(ActionEvent pEvent) 
                            {
                                super.actionPerformed(pEvent);
                
                                if (!(pComponent instanceof JPasswordField))
                                {
                                    try
                                    {
                                        Webstart.setClipboard(pComponent.getSelectedText());
                                    }
                                    catch (Exception e)
                                    {
                                        LoggerFactory.getInstance(SwingTextComponent.class).error(e);
                                    }
                                }
                            }           
                        });
                    }
                    
                    Action acPaste = map.get(DefaultEditorKit.pasteAction);
            
                    if (!(acPaste instanceof ForwardAction))
                    {
                        map.put(DefaultEditorKit.pasteAction, new ForwardAction(acPaste)
                        {
                            public void actionPerformed(ActionEvent pEvent) 
                            {
                                int iStart = pComponent.getSelectionStart();
                                int iEnd   = pComponent.getSelectionEnd();
                                
                                int iPos = pComponent.getCaretPosition();
                                
                                try
                                {
                                    String sClipboardText = Webstart.getClipboard();
                                    
                                    if (sClipboardText != null)
                                    {
                                        if (iStart >= 0 && iEnd > iStart)
                                        {
                                            pComponent.getDocument().remove(iStart, iEnd - iStart);
                                            pComponent.getDocument().insertString(iStart, sClipboardText, null);
    
                                            pComponent.requestFocus();
                                            pComponent.setCaretPosition(iStart + sClipboardText.length());
                                        }
                                        else
                                        {
                                            pComponent.getDocument().insertString(iPos, sClipboardText, null);
                                            
                                            pComponent.requestFocus();
                                            pComponent.setCaretPosition(iStart + sClipboardText.length());
                                        }
                                    }
                                    else
                                    {
                                        super.actionPerformed(pEvent);
                                    }
                                }
                                catch (Exception e)
                                {
                                    LoggerFactory.getInstance(SwingTextComponent.class).error(e);
                                    
                                    super.actionPerformed(pEvent);
                                }
                            }           
                        });
                    }
                    
                    Action acCut = map.get(DefaultEditorKit.cutAction);
                    
                    if (!(acCut instanceof ForwardAction))
                    {
                        map.put(DefaultEditorKit.cutAction, new ForwardAction(acCut)
                        {
                            public void actionPerformed(ActionEvent pEvent) 
                            {
                                try
                                {
                                    if (!(pComponent instanceof JPasswordField))
                                    {
                                        int iStart = pComponent.getSelectionStart();
                                        int iEnd   = pComponent.getSelectionEnd();
                            
                                        if (iStart >= 0 && iEnd > iStart)
                                        {
                                            Webstart.setClipboard(pComponent.getSelectedText());
                                            
                                            pComponent.getDocument().remove(iStart, iEnd - iStart);
                                            
                                            pComponent.requestFocus();
                                            pComponent.setCaretPosition(iStart);
                                        }
                                        else
                                        {
                                            super.actionPerformed(pEvent);
                                        }
                                    }
                                    else
                                    {
                                        super.actionPerformed(pEvent);
                                    }
                                }
                                catch (Exception e)
                                {
                                    LoggerFactory.getInstance(SwingTextComponent.class).error(e);
                                    
                                    super.actionPerformed(pEvent);
                                }
                            }           
                        });
                    }
                }
            }
        }
    }

    //****************************************************************
    // Subclass definition
    //****************************************************************
    
    /**
     * The <code>ForwardAction</code> takes an {@link Action} and forward the action.
     * 
     * @author Ren� Jahn
     */
    static class ForwardAction extends AbstractAction
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** the original action. */
        private Action action;
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>ForwardAction</code>.
         * 
         * @param pAction the action
         */
        ForwardAction(Action pAction)
        {
            action = pAction;
        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Interface implementation
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent pEvent) 
        {
            action.actionPerformed(pEvent);
        }       
        
    }   // ForwardAction
    
    /**
     * The <code>MetaDataImageIcon</code> is an {@link ImageIcon} and supports orientation
     * correction.
     * 
     * @author Ren� Jahn
     */
	public static class MetaDataImageIcon extends ImageIcon
	{
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>MetaDataImageIcon</code>.
		 * 
		 * @param pImageName the image name
		 * @param pData the image data
		 * @param pImage the image
		 */
		public MetaDataImageIcon(String pImageName, byte[] pData, Image pImage)
		{
		    super(pImage); // Waits until the image is loaded
			setImage(new MetaDataImage(pImageName, pData, pImage).getImage());
		}
		
	}	// MetaDataImageIcon
    
}   // JVxUtil
