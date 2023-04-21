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
 * 10.02.2009 - [JR] - createFileChooser defined
 * 17.03.2009 - [JR] - added invokeLater, invokeAndWait
 * 02.07.2009 - [JR] - getImage from byte[]
 * 11.05.2011 - [JR] - createScrollPanel implemented
 * 21.03.2018 - [JR] - createPopupMenuButton implemented
 */
package javax.rad.ui;

import javax.rad.model.ui.ICellEditor;
import javax.rad.ui.celleditor.ICheckBoxCellEditor;
import javax.rad.ui.celleditor.IChoiceCellEditor;
import javax.rad.ui.celleditor.IDateCellEditor;
import javax.rad.ui.celleditor.IImageViewer;
import javax.rad.ui.celleditor.ILinkedCellEditor;
import javax.rad.ui.celleditor.INumberCellEditor;
import javax.rad.ui.celleditor.ITextCellEditor;
import javax.rad.ui.component.IButton;
import javax.rad.ui.component.ICheckBox;
import javax.rad.ui.component.IIcon;
import javax.rad.ui.component.ILabel;
import javax.rad.ui.component.IMap;
import javax.rad.ui.component.IPasswordField;
import javax.rad.ui.component.IPopupMenuButton;
import javax.rad.ui.component.IRadioButton;
import javax.rad.ui.component.ITextArea;
import javax.rad.ui.component.ITextField;
import javax.rad.ui.component.IToggleButton;
import javax.rad.ui.container.IDesktopPanel;
import javax.rad.ui.container.IFrame;
import javax.rad.ui.container.IGroupPanel;
import javax.rad.ui.container.IInternalFrame;
import javax.rad.ui.container.IPanel;
import javax.rad.ui.container.IScrollPanel;
import javax.rad.ui.container.ISplitPanel;
import javax.rad.ui.container.ITabsetPanel;
import javax.rad.ui.container.IToolBar;
import javax.rad.ui.container.IToolBarPanel;
import javax.rad.ui.container.IWindow;
import javax.rad.ui.control.ICellFormat;
import javax.rad.ui.control.IChart;
import javax.rad.ui.control.IEditor;
import javax.rad.ui.control.IGauge;
import javax.rad.ui.control.ITable;
import javax.rad.ui.control.ITree;
import javax.rad.ui.layout.IBorderLayout;
import javax.rad.ui.layout.IFlowLayout;
import javax.rad.ui.layout.IFormLayout;
import javax.rad.ui.layout.IGridLayout;
import javax.rad.ui.menu.ICheckBoxMenuItem;
import javax.rad.ui.menu.IMenu;
import javax.rad.ui.menu.IMenuBar;
import javax.rad.ui.menu.IMenuItem;
import javax.rad.ui.menu.IPopupMenu;
import javax.rad.ui.menu.ISeparator;

/**
 * IFactory Interface to create Platform and technology independent Components.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF,... .
 * <p>
 * The directive for implementation is:
 * <ul>
 * <li>createXXX Methods should create new instances</li>
 * <li>getXXX Methods should return one and the same instance for the given
 * parameter</li>
 * </ul>
 * 
 * @author Martin Handsteiner
 */
public interface IFactory 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
     * Returns an array containing the names of all font families in this
     * <code>GraphicsEnvironment</code> localized for the default locale,
     * as returned by <code>Locale.getDefault()</code>.
     * <p>
     * Typical usage would be for presentation to a user for selection of
     * a particular family name. An application can then specify this name
     * when creating a font, in conjunction with a style, such as bold or
     * italic, giving the font system flexibility in choosing its own best
     * match among multiple fonts in the same font family.
     *
     * @return an array of <code>String</code> containing font family names
     *         localized for the default locale, or a suitable alternative
     *         name if no name exists for this locale.
     * @see IFont
     * @see IFont#getFamily
     */
    public String[] getAvailableFontFamilyNames();

    /**
     * Creates a new instance of <code>IFont</code> from the specified name, 
     * style and point size.
     * <p>
     * The font name can be a font face name or a font family name.
     * It is used together with the style to find an appropriate font face.
     * When a font family name is specified, the style argument is used to
     * select the most appropriate face from the family. When a font face
     * name is specified, the face's style and the style argument are
     * merged to locate the best matching font from the same family.
     * For example if face name "Arial Bold" is specified with style
     * <code>IFont.ITALIC</code>, the font system looks for a face in the
     * "Arial" family that is bold and italic, and may associate the font
     * instance with the physical font face "Arial Bold Italic".
     * The style argument is merged with the specified face's style, not
     * added or subtracted.
     * This means, specifying a bold face and a bold style does not
     * double-embolden the font, and specifying a bold face and a plain
     * style does not lighten the font.
     * <p>
     * If no face for the requested style can be found, the font system
     * may apply algorithmic styling to achieve the desired style.
     * For example, if <code>ITALIC</code> is requested, but no italic
     * face is available, glyphs from the plain face may be algorithmically
     * obliqued (slanted).
     * <p>
     * Font name lookup is case insensitive, using the case folding
     * rules of the US locale.
     * <p>
     * If the <code>name</code> parameter represents something other than a
     * logical font, i.e. is interpreted as a physical font face or family, and
     * this cannot be mapped by the implementation to a physical font or a
     * compatible alternative, then the font system will map the Font
     * to a standard font.
     * <p>
     *
     * @param pName the font name. This can be a font face name or a font
     *        family name, and may represent either a logical font or a physical
     *        font found in this <code>GraphicsEnvironment</code>.
     * @param pStyle the style constant for the <code>IFont</code>
     *        The style argument is an integer bitmask that may
     *        be PLAIN, or a bitwise union of BOLD and/or ITALIC
     *        (for example, ITALIC or BOLD|ITALIC).
     *        If the style argument does not conform to one of the expected
     *        integer bitmasks then the style is set to PLAIN.
     * @param pSize the point size of the <code>IFont</code>
     * @return the <code>IFont</code>
     * @see IFont
     * @see #getAvailableFontFamilyNames
     */
    public IFont createFont(String pName, int pStyle, int pSize);

    /**
     * Creates an sRGB color with the specified combined RGBA value consisting
     * of the alpha component in bits 24-31, the red component in bits 16-23,
     * the green component in bits 8-15, and the blue component in bits 0-7.
     * If the <code>pHasalpha</code> argument is <code>false</code>, alpha
     * is defaulted to 255.
     *
     * @param pRGBA the combined RGBA components
     * @return the <code>IColor</code>
     * @see IColor
     * @see IColor#getRed()
     * @see IColor#getGreen()
     * @see IColor#getBlue()
     * @see IColor#getAlpha()
     * @see IColor#getRGBA()
     */
    public IColor createColor(int pRGBA);

    /**
     * This encapsulate symbolic colors representing the color of
     * native GUI objects on a system.  For systems which support the dynamic
     * update of the system colors (when the user changes the colors)
     * the actual RGB values of these symbolic colors will also change
     * dynamically.  In order to compare the "current" RGB value of a
     * <code>SystemColor</code> object with a non-symbolic Color object,
     * <code>getRGB</code> should be used rather than <code>equals</code>.
     * <p>
     * Note that the way in which these system colors are applied to GUI objects
     * may vary slightly from platform to platform since GUI objects may be
     * rendered differently on each platform.
     *
     * @param pType the color type.
     * @return the <code>IColor</code>
     * @see IColor
     */
    public IColor getSystemColor(String pType);

    /**
     * Sets the given <code>IColor</code> as SystemColor.
     * If <code>pSystemColor</code> is <code>null</code> the 
     * original <code>SystemColor</code> is restored.
     *
     * @param pType the color type.
     * @param pSystemColor the <code>IColor</code>
     * @see IColor
     */
    public void setSystemColor(String pType, IColor pSystemColor);

    /**
     * Gets a <code>ICursor</code> object with the specified type.
     * 
     * @param pType the type of cursor
     * @return the <code>ICursor</code>
     * @see ICursor
     */
    public ICursor getPredefinedCursor(int pType);
    
    /**
     * Returns a system-specific custom <code>ICursor</code> object matching the 
     * specified name.  Cursor names are, for example: "Invalid.16x16"
     * 
     * @param pCursorName a string describing the desired system-specific custom cursor 
     * @return the system specific custom cursor named
     * @see ICursor
     */
    public ICursor getSystemCustomCursor(String pCursorName);
    
    /**
     * Searches the image if it is available as:
     * <ul>
     *  <li>File</li> 
     *  <li>ClassLoader</li>
     * </ul> 
     *  resource.
     *  
     * @param pImageName the name of a resource containing pixel data
     *        in a recognized file format.
     * @return an <code>IImage</code> which gets its pixel data from
     *         the specified file.
     * @see IImage
     */
    public IImage getImage(String pImageName);
   
    /**
     * Creates an image from byte data.
     * 
     * @param pImageName the name for the image. The name will be used 
     *                   for cache mechanism. If the name is set to <code>null</code>
     *                   then the cache will ignore the image.
     * @param pData the image data
     * @return the image created from the byte data
     */
    public IImage getImage(String pImageName, byte[] pData);
    
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @return the image name.
     */
    public String getImageMapping(String pMappingName);
    
    /**
     * Gets the image name for the given mapping name.
     * 
     * @param pMappingName the mapping name.
     * @param pImageName the image name.
     */
    public void setImageMapping(String pMappingName, String pImageName);
    
    /**
     * Gets all used mapping names.
     * 
     * @return the mapping names.
     */
    public String[] getImageMappingNames();
    
    /**
     * Creates a new instance of <code>IPoint</code> with the given x and y.
     *
     * @param pX the x value
     * @param pY the y value
     * @return the <code>IPoint</code>
     * @see IPoint
     */
    public IPoint createPoint(int pX, int pY);

    /**
     * Creates a new instance of <code>IDimension</code> with the given with and height.
     *
     * @param pWidth the width 
     * @param pHeight the height 
     * @return the <code>IDimension</code>
     * @see IDimension
     */
    public IDimension createDimension(int pWidth, int pHeight);
    
    /**
     * Creates a new instance of <code>IRectangle</code> with the given x, y, width and height.
     *
     * @param pX the x value
     * @param pY the y value
     * @param pWidth the width 
     * @param pHeight the height 
     * @return the <code>IRectangle</code>
     * @see IRectangle
     */
    public IRectangle createRectangle(int pX, int pY, int pWidth, int pHeight);
    
    /**
     * Creates a new instance of <code>IInsets</code>.
     *
     * @param pTop the top 
     * @param pLeft the left 
     * @param pBottom the bottom 
     * @param pRight the right 
     * @return the <code>IInsets</code>
     * @see IInsets
     */
    public IInsets createInsets(int pTop, int pLeft, int pBottom, int pRight);
    
    /**
     * Gets the component base class for the factory implementation.
     * This is needed for detecting the implementations for this interface.
     * 
     * @param <C> the component interface for generating the technology dependent component.
     * @return the component base class for the factory implementation.
     */
    public <C extends IComponent> Class<C> getComponentBaseClass();
    
    /**
     * Registers a component implementation for a component base class.
     * 
     * @param <C> the component interface for generating the technology dependent component.
     * @param <D> the component implementation class for generating the technology dependent component.
     * @param pComponentInterface the component interface
     * @param pComponentImplementation the technology dependent component class
     */
    public <C extends IComponent, D extends C> void registerComponent(Class<C> pComponentInterface, Class<D> pComponentImplementation);
    
    /**
     * Creates the corresponding technology dependent component implementation for the given component interface.
     * 
     * @param <C> the component interface for generating the technology dependent component.
     * @param pComponentInterface the component class
     * @return the technology dependent component implementation.
     */
    public <C extends IComponent> C createComponent(Class<C> pComponentInterface);
    
    /**
     * Creates a new instance of <code>ILabel</code>.
     *
     * @return the <code>ILabel</code>
     * @see ILabel
     */
    public ILabel createLabel();
   
    /**
     * Creates a new instance of <code>ITextField</code>.
     *
     * @return the <code>ITextField</code>
     * @see ITextField
     */
    public ITextField createTextField();
   
    /**
     * Creates a new instance of <code>IPasswordField</code>.
     *
     * @return the <code>IPasswordField</code>
     * @see IPasswordField
     */
    public IPasswordField createPasswordField();
   
    /**
     * Creates a new instance of <code>ITextArea</code>.
     *
     * @return the <code>ITextArea</code>
     * @see ITextArea
     */
    public ITextArea createTextArea();
   
    /**
     * Creates a new instance of <code>IIcon</code>.
     *
     * @return the <code>IIcon</code>
     * @see IIcon
     */
    public IIcon createIcon();
   
    /**
     * Creates a new instance of <code>IButton</code>.
     *
     * @return the <code>IButton</code>
     * @see IButton
     */
    public IButton createButton();
    
    /**
     * Creates a new instance of <code>IToggleButton</code>.
     *
     * @return the <code>IToggleButton</code>
     * @see IToggleButton
     */
    public IToggleButton createToggleButton();
   
    /**
     * Creates a new instance of <code>IPopupMenuButton</code>.
     * 
     * @return the <code>IPopupMenuButton</code>
     * @see IPopupMenuButton
     */
    public IPopupMenuButton createPopupMenuButton();
    
    /**
     * Creates a new instance of <code>ICheckBox</code>.
     *
     * @return the <code>ICheckBox</code>
     * @see ICheckBox
     */
    public ICheckBox createCheckBox();
   
    /**
     * Creates a new instance of <code>IRadioButton</code>.
     *
     * @return the <code>IRadioButton</code>
     * @see IRadioButton
     */
    public IRadioButton createRadioButton();
   
    /**
     * Creates a new instance of <code>IMenuItem</code>.
     *
     * @return the <code>IMenuItem</code>
     * @see IMenuItem
     */
    public IMenuItem createMenuItem();

    /**
     * Creates a new instance of <code>ISeparator</code>.
     *
     * @return the <code>ISeparator</code>
     * @see ISeparator
     */
    public ISeparator createSeparator();
    
    /**
     * Creates a new instance of <code>ICheckBoxMenuItem</code>.
     *
     * @return the <code>ICheckBoxMenuItem</code>
     * @see ICheckBoxMenuItem
     */
    public ICheckBoxMenuItem createCheckBoxMenuItem();
   
    /**
     * Creates a new instance of <code>IMenu</code>.
     *
     * @return the <code>IMenu</code>
     * @see IMenu
     */
    public IMenu createMenu();
   
    /**
     * Creates a new instance of <code>IMenuBar</code>.
     *
     * @return the <code>IMenuBar</code>
     * @see IMenuBar
     */
    public IMenuBar createMenuBar();
   
    /**
     * Creates a new instance of <code>IPopupMenu</code>.
     *
     * @return the <code>IPopupMenu</code>
     * @see IPopupMenu
     */
    public IPopupMenu createPopupMenu();
   
    /**
     * Creates a new instance of <code>IEditor</code>.
     *
     * @return the <code>IEditor</code>
     * @see IEditor
     */
    public IEditor createEditor();
   
    /**
     * Creates a new instance of <code>ITable</code>.
     *
     * @return the <code>ITable</code>
     * @see ITable
     */
    public ITable createTable();
   
    /**
     * Creates a new instance of <code>ITree</code>.
     *
     * @return the <code>ITree</code>
     * @see ITree
     */
    public ITree createTree();
   
    /**
     * Creates a new instance of <code>IChart</code>.
     *
     * @return the <code>IChart</code>
     * @see IChart
     */
    public IChart createChart();
   
    /**
     * Creates a new instance of <code>IGauge</code>.
     *
     * @return the <code>IGauge</code>
     * @see IGauge
     */
    public IGauge createGauge();
   
    /**
     * Creates a new Instance of <code>IMap</code>.
     * 
     * @return the <code>IMap</code>
     * @see IMap
     */
    public IMap createMap();
   
    /**
     * Creates a new instance of <code>ICellFormat</code>.
     *
     * @param pBackground the background of the Cell.
     * @param pForeground the foreground of the Cell.
     * @param pFont  the font of the Cell.
     * @param pImage the image of the Cell.
     * @param pStyle the style definition of the Cell.
     * @param pLeftIndent the left indent.
     * @return the <code>ICellFormat</code>
     * @see ICellFormat
     */
    public ICellFormat createCellFormat(IColor pBackground, IColor pForeground, IFont pFont, IImage pImage, Style pStyle, int pLeftIndent);
   
    /**
     * Gets the default <code>ICellEditor</code> for the given class.
     * This function should always return an editor.
     * It should look for best matching editor with Class.isAssignableFrom.
     *
     * @param pClass the class type to be edited.
     * @return the <code>ICellEditor</code>
     * @see ICellEditor
     */
    public ICellEditor getDefaultCellEditor(Class<?> pClass);
   
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
    public void setDefaultCellEditor(Class<?> pClass, ICellEditor pCellEditor);
   
    /**
     * Creates a new instance of <code>IImageViewer</code>.
     *
     * @return the <code>IImageViewer</code>
     * @see IImageViewer
     */
    public IImageViewer createImageViewer();
   
    /**
     * Creates a new instance of <code>IChoiceCellEditor</code>.
     *
     * @return the <code>IChoiceCellEditor</code>
     * @see IChoiceCellEditor
     */
    public IChoiceCellEditor createChoiceCellEditor();
   
    /**
     * Creates a new instance of <code>ICheckBoxCellEditor</code>.
     *
     * @return the <code>ICheckBoxCellEditor</code>
     * @see ICheckBoxCellEditor
     */
    public ICheckBoxCellEditor createCheckBoxCellEditor();
   
    /**
     * Creates a new instance of <code>IDateCellEditor</code>.
     *
     * @return the <code>IDateCellEditor</code>
     * @see IDateCellEditor
     */
    public IDateCellEditor createDateCellEditor();
   
    /**
     * Creates a new instance of <code>ILinkedCellEditor</code>.
     *
     * @return the <code>ILinkedCellEditor</code>
     * @see ILinkedCellEditor
     */
    public ILinkedCellEditor createLinkedCellEditor();
   
    /**
     * Creates a new instance of <code>INumberCellEditor</code>.
     *
     * @return the <code>INumberCellEditor</code>
     * @see INumberCellEditor
     */
    public INumberCellEditor createNumberCellEditor();
   
    /**
     * Creates a new instance of <code>ITextCellEditor</code>.
     *
     * @return the <code>ITextCellEditor</code>
     * @see ITextCellEditor
     */
    public ITextCellEditor createTextCellEditor();
   
    /**
     * Creates a new instance of <code>IPanel</code>.
     *
     * @return the <code>IPanel</code>
     * @see IPanel
     */
    public IPanel createPanel();
   
    /**
     * Creates a new instance of <code>IToolBarPanel</code>.
     * 
     * @return the <code>IToolBarPanel</code>
     * @see IToolBarPanel
     */
    public IToolBarPanel createToolBarPanel();
    
    /**
     * Creates a new instance of <code>IGroupPanel</code>.
     *
     * @return the <code>IGroupPanel</code>
     * @see IGroupPanel
     */
    public IGroupPanel createGroupPanel();
   
    /**
     * Creates a new instance of <code>IScrollPanel</code>.
     * 
     * @return the <code>IScrollPanel</code>
     * @see IScrollPanel
     */
    public IScrollPanel createScrollPanel();
    
    /**
     * Creates a new instance of <code>ISplitPanel</code>.
     *
     * @return the <code>ISplitPanel</code>
     * @see ISplitPanel
     */
    public ISplitPanel createSplitPanel();
   
    /**
     * Creates a new instance of <code>ITabsetPanel</code>.
     *
     * @return the <code>ITabsetPanel</code>
     * @see ITabsetPanel
     */
    public ITabsetPanel createTabsetPanel();
   
    /**
     * Creates a new instance of <code>IToolBar</code>.
     *
     * @return the <code>IToolBar</code>
     * @see IToolBar
     */
    public IToolBar createToolBar();
   
    /**
     * Creates a new instance of <code>IDesktopPanel</code>.
     *
     * @return the <code>IDesktopPanel</code>
     * @see IDesktopPanel
     */
    public IDesktopPanel createDesktopPanel();
   
    /**
     * Creates a new instance of <code>IInternalFrame</code>.
     *
     * @param pDesktop the associated desktop for the internal frame
     * @return the <code>IInternalFrame</code>
     * @see IInternalFrame
     */
    public IInternalFrame createInternalFrame(IDesktopPanel pDesktop);
   
    /**
     * Creates a new instance of <code>IWindow</code>.
     * 
     * @return the <code>IWindow</code>
     * @see IWindow
     */
    public IWindow createWindow();
    
    /**
     * Creates a new instance of <code>IFrame</code>.
     * 
     * @return the <code>IFrame</code>
     * @see IFrame
     */
    public IFrame createFrame();

    /**
     * Creates a new Instance of IComponent that contains any Custom Component.
     * @param pCustomComponent the custom Component.
     * @return the <code>IComponent</code> custom component.
     */
    public IComponent createCustomComponent(Object pCustomComponent);
    
    /**
     * Creates a new Instance of IContainer that contains any Custom Container.
     * @param pCustomContainer the custom Container.
     * @return the <code>IContainer</code> custom container.
     */
    public IContainer createCustomContainer(Object pCustomContainer);
    
    /**
     * Creates a new instance of <code>IBorderLayout</code>.
     * 
     * @return the <code>IBorderLayout</code>
     * @see IBorderLayout
     */
    public IBorderLayout createBorderLayout();

    /**
     * Creates a new instance of <code>IFlowLayout</code>.
     * 
     * @return the <code>IFlowLayout</code>
     * @see IFlowLayout
     */
    public IFlowLayout createFlowLayout();

    /**
     * Creates a new instance of <code>IFormLayout</code>.
     * 
     * @return the <code>IFormLayout</code>
     * @see IFormLayout
     */
    public IFormLayout createFormLayout();
    
    /**
     * Creates a new instance of <code>IGridLayout</code>.
     * 
     * @return the <code>IGridLayout</code>
     * @param columns the column count
     * @param rows the row count
     * @see IGridLayout
     */
	public IGridLayout createGridLayout(int columns, int rows);
	
    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously on the event dispatching thread. 
     * This will happen after all pending events have been processed. This method 
     * should be used when an application thread needs to update the GUI.
     *  
     * @param pRunnable the asynchronous call
     * @see #invokeAndWait(Runnable)
     */
    public void invokeLater(Runnable pRunnable);
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed synchronously on the event dispatching thread. This call blocks 
     * until all pending events have been processed and (then) <code>pRunnable.run()</code> returns. 
     * This method should be used when an application thread needs to update the GUI.
     * 
     * @param pRunnable the call
     * @throws Exception if the call causes an exception
     */
    public void invokeAndWait(Runnable pRunnable) throws Exception;
    
    /**
     * Causes <code>pRunnable.run()</code> to be executed asynchronously in a new thread. 
     * Action calls and UI Calls in the thread should be synchronized with the event dispatching thread
     * by using invokeLater or invokeAndWait.
     * To guarantee functionality of invokeLater in Threads, IFactory implementations should use
     * InvokeLaterThread to create threads. The InvokeLaterThread calls all invokeLater notifications
     * after the Thread. In thread loops <code>InvokeLaterThread.executeInvokeLater</code> can
     * be called to invoke thread safe all invokeLater runnables immediate. 
     * This gives the IFactory implementation a chance to decide how and when to run the threads.
     * 
     * @param pRunnable the call
     * @return the {@link Thread} in which the {@link Runnable} is being executed, the {@link Thread} is already running.
     * @see #invokeAndWait(Runnable)
     * @see #invokeLater(Runnable)
     */
    public Thread invokeInThread(Runnable pRunnable);
    
    /**
     * Gets the value of the property with the given name.
     * 
     * @param pName the name of the property.
     * @return the value of the property with the given name.
     */
    public Object getProperty(String pName);
    
    /**
     * Sets the given value with the given name as property.
     * 
     * @param pName the name of the property.
     * @param pValue the value of the property.
     */
    public void setProperty(String pName, Object pValue);
    
}	// IFactory
