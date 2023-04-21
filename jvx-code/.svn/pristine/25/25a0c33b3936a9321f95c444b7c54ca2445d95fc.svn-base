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
 * 09.06.2021 - [TH] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.rad.genui.IFontAwesome;
import javax.rad.model.ui.ITranslatable;
import javax.rad.util.ITranslator;
import javax.rad.util.TranslationMap;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;

import com.sibvisions.rad.ui.swing.ext.layout.JVxBorderLayout;
import com.sibvisions.rad.ui.swing.ext.layout.JVxSequenceLayout;
import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>JVxHtmlEditor</code> provides the generation of the physical text editor component, 
 * handles all events, and gives standard access to edited values.
 * 
 * @author Toni Heiss
 */
public class JVxHtmlEditor extends JPanel 
                           implements ActionListener, 
                                      KeyListener, 
                                      DocumentListener, 
                                      CaretListener, 
                                      ITranslatable, 
                                      ITranslator
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Actions

    /** The action to make the selection bold. */
    private static final BoldAction                    ACTION_BOLD           = new StyledEditorKit.BoldAction();

    /** The action to make the selection italic. */
    private static final ItalicAction                  ACTION_ITALIC         = new StyledEditorKit.ItalicAction();

    /** The action to make the selection underlined. */
    private static final UnderlineAction               ACTION_UNDERLINE      = new StyledEditorKit.UnderlineAction();

    // Commands

    /** The action command to switch the view. */
    public static final String                         CMD_SWITCHVIEW        = "SWITCHVIEW";

    /** The action command to make the selection bold. */
    public static final String                         CMD_BOLD              = "BOLD";

    /** The action command to make the selection italic. */
    public static final String                         CMD_ITALIC            = "ITALIC";

    /** The action command to underline the selection. */
    public static final String                         CMD_UNDERLINE         = "UNDERLINE";

    /** The action command to format the selection in subscript. */
    public static final String                         CMD_SUBSCRIPT         = "SUBSCRIPT";

    /** The action command to format the selection in superscript. */
    public static final String                         CMD_SUPERSCRIPT       = "SUPERSCRIPT";

    /** The action command to align the selection left. */
    public static final String                         CMD_ALIGNLEFT         = "ALIGNLEFT";

    /** The action command to align the selection in the center. */
    public static final String                         CMD_ALIGNCENTER       = "ALIGNCENTER";

    /** The action command to align the selection right. */
    public static final String                         CMD_ALIGNRIGHT        = "ALIGNRIGHT";

    /** The action command to strike through the selection . */
    public static final String                         CMD_STRIKETHROUGH     = "STRIKETHROUGH";

    /** The action command to insert a horizontal rule. */
    public static final String                         CMD_HORIZONTALRULE    = "HORIZONTALRULE";

    /** The action command to make the selection to an ordered list. */
    public static final String                         CMD_ORDEREDLIST       = "ORDEREDLIST";

    /** The action command to make the selection to an unordered list. */
    public static final String                         CMD_UNORDEREDLIST     = "UNORDEREDLIST";

    /** The action command to remove a link. */
    public static final String                         CMD_DEBUG             = "DEBUG";

    /** The action command to remove the formatting of the selection. */
    public static final String                         CMD_REMOVEFORMATTING  = "REMOVEFORMATTING ";

    // Rest of statics

    /**
     * The magic string which no one should ever use in their html,
     * because some functions insert this string to find the real indexes in the
     * html raw text or document text.
     */
    private static final String                        MAGIC_STRING          = "-M-A-G-I-C-S-T-R-I-N-G-1-";

    /**
     * The second magic string which no one should ever use in their html,
     * because some functions insert this string to find the real indexes in the
     * html raw text or document text.
     */
    private static final String                        MAGIC_STRING_2        = "-M-A-G-I-C-S-T-R-I-N-G-2-";

    /** All font names. */
    public static final LinkedHashMap<String, Integer> FONT_SIZES;

    /** All font types. */
    public static final LinkedHashMap<String, String>  FONT_TYPES;

    /** All foreground and background colors. */
    public static final LinkedHashMap<String, Color>   COLORS;

    /** Prefix for the foreground color action. */
    private static final String                        PREFIX_FOREGROUND     = "FOREGROUND_";

    /** Prefix for the background color action . */
    private static final String                        PREFIX_BACKGROUND     = "BACKGROUND_";

    /** Text for the button {@link #butFontType}. */
    private static final String                        TEXT_FONT_TYPE        = "Font";

    /** Text for the button {@link #butFontSize}. */
    private static final String                        TEXT_FONT_SIZE        = "Font size";

    /** Text for the button {@link #butTextColor}. */
    private static final String                        TEXT_TEXT_COLOR       = "Text color";

    /** Text for the button {@link #butBackgroundColor}. */
    private static final String                        TEXT_BACKGROUND_COLOR = "Background color";
    
    /** the icon cache. */
    private static HashMap<Color, ImageIcon>           hmpIconCache          = new HashMap<Color, ImageIcon>();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Document & Kit
    /** The HTML editor kit for the text pane. */
    private HTMLEditorKit                              htmlKit              = new HTMLEditorKit();

    /** The HTML document of the editor kit. */
    private HTMLDocument                               htmlDoc              = (HTMLDocument)htmlKit.createDefaultDocument();

    // Base Layout
    /** The border layout of this panel. */
    private JVxBorderLayout                            blThis        		= new JVxBorderLayout();

    /** The scroll pane for the rich text pane. */
    private JVxScrollPane                              scpRichText          = new JVxScrollPane();

    /** The scroll pane for the syntax text area. */
    private JVxScrollPane                              scpRawHtml           = new JVxScrollPane();

    /** The text pane showing the text as rich text. */
    private JEditorPane                                editRichText;

    /** The text area showing the text as plain text. */
    private JTextArea                                  editRawHtml          = new JTextArea();

    // Toolbars
    /** The border layout for the panel of the toolbars. */
    private JVxBorderLayout                            blPanelToolbar       = new JVxBorderLayout();

    /** The panel for the toolbars. */
    private JPanel                                     panelToolbar         = new JPanel();

    // First toolbar
    /** The tool bar for the editor. */
    private JPanel                                     panelToolbarNorth    = new JPanel();

    /** Layout for the north toolbar. */
    private JVxSequenceLayout                          slToolbarNorth       = new JVxSequenceLayout();

    /** The button to switch the view. */
    private JVxToggleButton                            butSwitchView        = new JVxToggleButton();

    /** The button to make the selection bold. */
    private JVxToggleButton                            butBold              = new JVxToggleButton();

    /** The button to make the selection italic. */
    private JVxToggleButton                            butItalic            = new JVxToggleButton();

    /** The button to underline the selection. */
    private JVxToggleButton                            butUnderline         = new JVxToggleButton();

    /** The button to format the selection in subscript. */
    private JVxToggleButton                            butSubscript         = new JVxToggleButton();

    /** The button to format the selection in superscript. */
    private JVxToggleButton                            butSuperscript       = new JVxToggleButton();

    /** The button to align the selection left. */
    private JVxButton                                  butAlignLeft         = new JVxButton();

    /** The button to align the selection in the center. */
    private JVxButton                                  butAlignCenter       = new JVxButton();

    /** The button to align the selection right. */
    private JVxButton                                  butAlignRight        = new JVxButton();

    /** The button to strike through the selection . */
    private JVxButton                                  butStrikethrough     = new JVxButton();

    /** The button to insert a horizontal rule. */
    private JVxButton                                  butHorizontalRule    = new JVxButton();

    /** The button to make the selection to an ordered list. */
    private JVxButton                                  butOrderedList       = new JVxButton();

    /** The button to make the selection to an unordered list. */
    private JVxButton                                  butUnorderedList     = new JVxButton();

    /** The button to remove a link. */
    private JVxToggleButton                            butDebug             = new JVxToggleButton();

    /** The button to remove the formatting of the selection. */
    private JVxButton                                  butRemoveFormatting  = new JVxButton();

    // Second toolbar
    /** South toolbar with font changing actions. */
    private JPanel                                     panelToolbarSouth    = new JPanel();

    /** Layout for the south toolbar with font changing actions. */
    private JVxSequenceLayout                          slToolbarSouth       = new JVxSequenceLayout();

    /** The button for the font sizes. */
    private JVxButton                                  butFontSize;

    /** The pop-up menu of the font sizes button. */
    private JPopupMenu                                 popFontSizes;

    /** The button for the font types. */
    private JVxButton                                  butFontType;

    /** The pop-up menu of the font types button. */
    private JPopupMenu                                 popFontTypes;

    /** The button for the font colors. */
    private JVxButton                                  butTextColor;

    /** The pop-up menu of the font colors button. */
    private JPopupMenu                                 popTextColors;

    /** The button for the font background colors. */
    private JVxButton                                  butBackgroundColor;

    /** The pop-up menu of the font background colors button. */
    private JPopupMenu                                 popBackgroundColors;

    /** The standard paragraph created. */
    private HTML.Tag                                   standardParagraphTag = HTML.Tag.DIV;

    /** The translation map. */
    private TranslationMap                             mapTranslation;

    // Ignore doc listener
    /** If the document listener should be ignored. */
    private boolean                                    bIgnoreDocListener   = false;

    /** If the caret listener should be ignored. */
    private boolean                                    bIgnoreCaretListener = false;

    /** Whether the translation is active. */
    private boolean                                    bTranslate           = true;
    
    /** Whether the design mode is active. */
    private boolean                                    bDesignMode          = Beans.isDesignTime();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        FONT_SIZES = new LinkedHashMap<String, Integer>();
        FONT_SIZES.put("Small", Integer.valueOf(12));
        FONT_SIZES.put("Small (XS)", Integer.valueOf(10));
        FONT_SIZES.put("Small (XXS)", Integer.valueOf(8));
        FONT_SIZES.put("Medium", Integer.valueOf(14));
        FONT_SIZES.put("Large", Integer.valueOf(16));
        FONT_SIZES.put("Large (XL)", Integer.valueOf(24));
        FONT_SIZES.put("Large (XXL)", Integer.valueOf(36));

        FONT_TYPES = new LinkedHashMap<String, String>();
        FONT_TYPES.put("Default", "Default");
        FONT_TYPES.put("Serif", "Serif");
        FONT_TYPES.put("Sans-Serif", "Sans-Serif");
        FONT_TYPES.put("Monospaced", "Monospaced");

        COLORS = new LinkedHashMap<String, Color>();
        COLORS.put("White", Color.WHITE);
        COLORS.put("Light gray", Color.LIGHT_GRAY);
        COLORS.put("Gray", Color.GRAY);
        COLORS.put("Dark gray", Color.DARK_GRAY);
        COLORS.put("Black", Color.BLACK);
        COLORS.put("Red", Color.RED);
        COLORS.put("Blue", Color.BLUE);
        COLORS.put("Yellow", Color.YELLOW);
        COLORS.put("Green", Color.GREEN);
        COLORS.put("Cyan", Color.CYAN);
        COLORS.put("Pink", Color.PINK);
        COLORS.put("Orange", Color.ORANGE);
        COLORS.put("Magenta", Color.MAGENTA);
    }

    /**
     * Creates a new instance of <code>JVxHtmlEditor</code>.
     */
    public JVxHtmlEditor()
    {
        initFirstToolbar();
        initSecondToolbar();

        panelToolbar.setLayout(blPanelToolbar);
        blPanelToolbar.setMargins(new Insets(0, 0, 5, 0));
        
        panelToolbar.add(panelToolbarNorth, JVxBorderLayout.NORTH);
        panelToolbar.add(panelToolbarSouth, JVxBorderLayout.SOUTH);

        // Text areas
        editRawHtml.getDocument().addDocumentListener(this);
        scpRawHtml.setViewportView(editRawHtml);

        editRichText = new JEditorPane()
        {
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // Overwritten methods
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            /**
             * {@inheritDoc}
             */
            @Override
            public void setCaretPosition(int position)
            {
                //We never want to select the head element. Never. Only if it throws an error we try again with 0.
                //Solves bugs associated with CTRL+A selection
                
                if (position == 0)
                {
                    boolean bHasHead = false;
                    
                    for (Element element =  htmlDoc.getParagraphElement(0); element != null && !bHasHead; element = element.getParentElement())
                    {
                        bHasHead = "head".equalsIgnoreCase(element.getName());
                    }
                    
                    if (bHasHead)
                    {
                        try
                        {
                            super.setCaretPosition(1);
                        }
                        catch (Exception e)
                        {
                            super.setCaretPosition(0);
                        }                        
                    }
                    else
                    {
                        super.setCaretPosition(0);
                    }
                }
                else
                {
                    super.setCaretPosition(position);
                }
            }
              
            /**
             * {@inheritDoc}
             */
            @Override
            public void setText(String pText)
            {
                super.setText(pText);
                
                if (!bIgnoreDocListener)
                {
                    bIgnoreDocListener = true;
                    
                    try
                    {
                        editRawHtml.setText(JVxHtmlEditor.this.getText());
                    }
                    finally
                    {
                        bIgnoreDocListener = false;
                    }                                                                                      
                }
            }
              
            /**
             * {@inheritDoc}
             */
            @Override
            public void setEditable(boolean pEditable)
            {
                super.setEditable(pEditable);
                  
                if (editRawHtml != null)
                {
                    editRawHtml.setEditable(pEditable);
                }
                  
                if (editRichText != null)
                {
                    updateButtonStates();
                }
            }
        };
        editRichText.addCaretListener(this);
        editRichText.addKeyListener(this);
        editRichText.setEditorKit(htmlKit);
        editRichText.setDocument(htmlDoc);
        editRichText.setContentType("text/html");
        
        //apply font, because content type sets a different font!
        Font font = new JTextField().getFont();
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
        htmlDoc.getStyleSheet().addRule(bodyRule);
        
        
        scpRichText.setViewportView(editRichText);
        
        if (SwingFactory.isMacLaF())
        {
            WrappedInsetsBorder border = new WrappedInsetsBorder(new JTextField().getBorder());
            border.setPaintInsets(new Insets(0, 0, 0, 0));
            
            editRichText.setBorder(border);
            scpRichText.setBorder(null);
            
            editRawHtml.setBorder(border);
            scpRawHtml.setBorder(null);
        }        
        
        //removes mysterious p-margin tag
        editRichText.setText("<html></html>");

        // Layout
        setLayout(blThis);
        
        add(panelToolbar, JVxBorderLayout.NORTH);
        add(scpRichText, JVxBorderLayout.CENTER);
    }
    
    /**
     * Initializes the first toolbar.
     */
    private void initFirstToolbar()
    {
        configureToolBarButton(butBold, CMD_BOLD, "bold.png");
        configureToolBarButton(butItalic, CMD_ITALIC, "italic.png");
        configureToolBarButton(butUnderline, CMD_UNDERLINE, "underline.png");
        
        configureToolBarButton(butSubscript, CMD_SUBSCRIPT, "subscript.png");
        configureToolBarButton(butSuperscript, CMD_SUPERSCRIPT, "superscript.png");

        configureToolBarButton(butAlignLeft, CMD_ALIGNLEFT, "align_left.png");
        configureToolBarButton(butAlignCenter, CMD_ALIGNCENTER, "align_center.png");
        configureToolBarButton(butAlignRight, CMD_ALIGNRIGHT, "align_right.png");

        configureToolBarButton(butStrikethrough, CMD_STRIKETHROUGH, "strikethrough.png");
        configureToolBarButton(butHorizontalRule, CMD_HORIZONTALRULE, "horizontal_rule.png");
        
        configureToolBarButton(butOrderedList, CMD_ORDEREDLIST, "ordered_list.png");
        configureToolBarButton(butUnorderedList, CMD_UNORDEREDLIST, "unordered_list.png");
        
        configureToolBarButton(butDebug, CMD_DEBUG, "remove_link.png");
        
        configureToolBarButton(butRemoveFormatting, CMD_REMOVEFORMATTING, null);
        butRemoveFormatting.setIcon(JVxUtil.getIcon(IFontAwesome.ERASER_SMALL));
        
        configureToolBarButton(butSwitchView, CMD_SWITCHVIEW, null);
        butSwitchView.setIcon(JVxUtil.getIcon(IFontAwesome.FILE_TEXT_O_SMALL));
        
        panelToolbarNorth.setLayout(slToolbarNorth);
        
        slToolbarNorth.setMargins(new Insets(0, 0, 0, 0));
        slToolbarNorth.setHorizontalAlignment(JVxSequenceLayout.LEFT);
        slToolbarNorth.setVerticalComponentAlignment(JVxSequenceLayout.STRETCH);
        slToolbarNorth.setHorizontalGap(0);
        
        // First toolbar adding
        panelToolbarNorth.add(butBold);
        panelToolbarNorth.add(butItalic);
        panelToolbarNorth.add(butUnderline);
        panelToolbarNorth.add(butSubscript);
        panelToolbarNorth.add(butSuperscript);
        panelToolbarNorth.add(butAlignLeft);
        panelToolbarNorth.add(butAlignCenter);
        panelToolbarNorth.add(butAlignRight);
        panelToolbarNorth.add(butStrikethrough);
        panelToolbarNorth.add(butHorizontalRule);
        panelToolbarNorth.add(butOrderedList);
        panelToolbarNorth.add(butUnorderedList);
        
        //debug only
        //panelToolbarNorth.add(butDebug);
        
        panelToolbarNorth.add(butRemoveFormatting);
        panelToolbarNorth.add(butSwitchView);
    }
    
    /**
     * Initializes the second toolbar.
     */
    private void initSecondToolbar()
    {
        popFontSizes = createMenu(FONT_SIZES);
        popFontTypes = createMenu(FONT_TYPES);
        
        butFontSize = createMenuButton(TEXT_FONT_SIZE, popFontSizes);
        butFontType = createMenuButton(TEXT_FONT_TYPE, popFontTypes);
        
        popTextColors = createColorMenu(COLORS, PREFIX_FOREGROUND);
        popBackgroundColors = createColorMenu(COLORS, PREFIX_BACKGROUND);
        
        butTextColor = createMenuButton(TEXT_TEXT_COLOR, popTextColors);
        butBackgroundColor = createMenuButton(TEXT_BACKGROUND_COLOR, popBackgroundColors);

        panelToolbarSouth.setLayout(slToolbarSouth);
        
        slToolbarSouth.setMargins(new Insets(0, 0, 0, 0));
        slToolbarSouth.setHorizontalAlignment(JVxSequenceLayout.LEFT);
        slToolbarSouth.setVerticalComponentAlignment(JVxSequenceLayout.STRETCH);
        slToolbarSouth.setHorizontalGap(0);
        
        // Second toolbar adding
        panelToolbarSouth.add(butFontType);
        panelToolbarSouth.add(butFontSize);
        panelToolbarSouth.add(butTextColor);
        panelToolbarSouth.add(butBackgroundColor);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public void setTranslation(TranslationMap pTranslation)
    {
        mapTranslation = pTranslation;
        
        updateTranslation();
    }

    /**
     * {@inheritDoc}
     */
    public TranslationMap getTranslation()
    {
        return mapTranslation;
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslationEnabled(boolean pEnabled)
    {
        bTranslate = pEnabled;
        
        updateTranslation();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTranslationEnabled()
    {
        return bTranslate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String translate(String pText)
    {
        if (bTranslate && mapTranslation != null)
        {
            return mapTranslation.translate(pText);
        }
        else
        {
            return pText;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent pActionEvent)
    {
        try
        {
            String sActionCommand = pActionEvent.getActionCommand();

            if (CMD_SWITCHVIEW.equals(sActionCommand))
            {
                switchView();
            }
            else if (CMD_HORIZONTALRULE.equals(sActionCommand))
            {
                doHorizontalRule();
            }
            else if (CMD_REMOVEFORMATTING.equals(sActionCommand))
            {
                doRemoveFormatting();
            }
            else if (CMD_DEBUG.equals(sActionCommand))
            {           
                doDebug();
            }
            else
            {
                bIgnoreCaretListener = true;

                try
                {
                    ensureNonImpliedParagraphs();                    
                }
                finally
                {
                    bIgnoreCaretListener = false;
                }
                
                if (CMD_BOLD.equals(sActionCommand))
                {
                    ACTION_BOLD.actionPerformed(pActionEvent);
                }
                else if (CMD_ITALIC.equals(sActionCommand))
                {
                    ACTION_ITALIC.actionPerformed(pActionEvent);
                }
                else if (CMD_UNDERLINE.equals(sActionCommand))
                {
                    ACTION_UNDERLINE.actionPerformed(pActionEvent);
                }
                else if (CMD_SUBSCRIPT.equals(sActionCommand))
                {
                    doSubscript();
                }
                else if (CMD_SUPERSCRIPT.equals(sActionCommand))
                {
                    doSuperscript();
                }
                else if (CMD_ALIGNLEFT.equals(sActionCommand))
                {
                    doAlignLeft();
                }
                else if (CMD_ALIGNCENTER.equals(sActionCommand))
                {
                    doAlignCenter();
                }
                else if (CMD_ALIGNRIGHT.equals(sActionCommand))
                {
                    doAlignRight();
                }
                else if (CMD_STRIKETHROUGH.equals(sActionCommand))
                {
                    doStrikethrough();
                }
                else if (CMD_ORDEREDLIST.equals(sActionCommand))
                {
                    doList(HTML.Tag.OL);
                }
                else if (CMD_UNORDEREDLIST.equals(sActionCommand))
                {
                    doList(HTML.Tag.UL);
                }
                else if (FONT_SIZES.containsKey(sActionCommand))
                {
                    new StyledEditorKit.FontSizeAction(sActionCommand, FONT_SIZES.get(sActionCommand).intValue()).actionPerformed(pActionEvent);
                }
                else if (FONT_TYPES.containsKey(sActionCommand))
                {
                    doFontType(sActionCommand);
                }
                else if (sActionCommand.startsWith(PREFIX_FOREGROUND))
                {
                    String sColorKey = sActionCommand.substring(PREFIX_FOREGROUND.length(), sActionCommand.length());
                    
                    new StyledEditorKit.ForegroundAction(sActionCommand, COLORS.get(sColorKey)).actionPerformed(pActionEvent);
                }
                else if (sActionCommand.startsWith(PREFIX_BACKGROUND))
                {
                    String sColorKey = sActionCommand.substring(PREFIX_BACKGROUND.length(), sActionCommand.length());
                    
                    new BackgroundAction(sActionCommand, COLORS.get(sColorKey)).actionPerformed(pActionEvent);
                }
            }
        }
        catch (Throwable th)
        {
            if (th instanceof RuntimeException)
            {
                throw (RuntimeException)th;
            }
            else
            {
                throw new RuntimeException(th);
            }
        }
        finally
        {
            updateToggleButtonStates();
        }
    }

    /**
     * Debug functionality.
     */
    private void doDebug()
    {
        //debugElementInfo(htmlDoc.getParagraphElement(0), 0);
        
        System.out.println("CARET:" + Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd()));
        
        Element charElement = htmlDoc.getCharacterElement(Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd()));
        
        int i = 1;
        
        debugElementInfo(charElement, i++);

        for (Element elementParent = charElement.getParentElement(); elementParent != null; elementParent = elementParent.getParentElement())
        {
            debugElementInfo(elementParent, i++);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void insertUpdate(DocumentEvent e)
    {
        if (!bIgnoreDocListener)
        {
            bIgnoreDocListener = true;
            
            try
            {
                editRichText.setText(editRawHtml.getText());                
            }
            finally
            {
                bIgnoreDocListener = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUpdate(DocumentEvent e)
    {
        if (!bIgnoreDocListener)
        {
            bIgnoreDocListener = true;
            
            try
            {
                editRichText.setText(editRawHtml.getText());                
            }
            finally
            {
                bIgnoreDocListener = false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void changedUpdate(DocumentEvent e)
    {
        // Not used by plain text areas.
    }

    /**
     * {@inheritDoc}
     */
    public void caretUpdate(CaretEvent e)
    {
        if (!bIgnoreCaretListener && e.getSource() == editRichText)
        {
            updateToggleButtonStates();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void keyPressed(KeyEvent pKey)
    {
        if (pKey.getKeyChar() == KeyEvent.VK_ENTER && !pKey.isControlDown())
        {
            pKey.consume();                
        }
    }

    /**
     * {@inheritDoc}
     */
    public void keyReleased(KeyEvent pKey)
    {
        if (pKey.getKeyChar() == KeyEvent.VK_ENTER && !pKey.isControlDown())
        {
            pKey.consume();   
        }
    }

    /**
     * {@inheritDoc}
     */
    public void keyTyped(KeyEvent pKey)
    {
        try
        {
            if (pKey.getKeyChar() == KeyEvent.VK_ENTER && !pKey.isControlDown())
            {
                doBreak(pKey);
                
                pKey.consume();
            }
        }
        catch (Throwable thr)
        {
            thr.printStackTrace();
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Creates a popup menu for specific items.
     * 
     * @param pItems the items
     * @return the popup menu
     */
    private JPopupMenu createMenu(LinkedHashMap<String, ?> pItems)
    {
        JPopupMenu popup = new JPopupMenu();
        popup.setFocusable(false);
        
        for (String key : pItems.keySet())
        {
            JMenuItem menuSizeEntry = new JMenuItem(key);
            menuSizeEntry.setActionCommand(key);
            menuSizeEntry.addActionListener(this);
            
            popup.add(menuSizeEntry);
        }

        return popup;
    }
    
    /**
     * Creates a popup menu with specific colors.
     * 
     * @param pColors the colors
     * @param pActionCommandPrefix the action command prefix
     * @return the popup menu
     */
    private JPopupMenu createColorMenu(LinkedHashMap<String, Color> pColors, String pActionCommandPrefix)
    {
        JPopupMenu popup = new JPopupMenu();
        popup.setFocusable(false);

        String sName;
        
        for (Entry<String, Color> entry : pColors.entrySet())
        {
            sName = entry.getKey();
                
            ImageIcon icon = getImageIcon(entry.getValue());

            JMenuItem item = new JMenuItem(sName);
            item.setActionCommand(pActionCommandPrefix + sName);
            item.addActionListener(this);
            item.setIcon(icon);

            popup.add(item);
        } 
        
        return popup;
    }

    /**
     * Creates a new button with a popup menu.
     * 
     * @param pText the button text
     * @param pMenu the popup menu
     * @return the button
     */
    private JVxButton createMenuButton(String pText, JPopupMenu pMenu)
    {
        JVxButton button = new JVxButton(pText);
        
        if (SwingFactory.isMacLaF())
        {
            //before setting the icon!!! -> icon will change the font
            Font ft = button.getFont();

            //see SwingAbstractFormatableButton
            //very strange, but Mac LaF doesn't recognize the margins unless there is an icon!
            button.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/1x1.png"));
            button.setIconTextGap(0);
            
            button.setFont(new Font(ft.getName(), ft.getStyle(), ft.getSize()));
        }
        
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setPopupMenu(pMenu);
        button.setFocusable(false);
        button.setBorderOnMouseEntered(true);
        
        Insets insetFontSizes = button.getMargin();
        
        if (insetFontSizes != null)
        {
            button.setMargin(new Insets(insetFontSizes.top, 5, insetFontSizes.bottom, 5));            
        }
        else
        {            
            button.setMargin(new Insets(5, 5, 5, 5));            
        }

        return button;
    }
    
    /**
     * Configures the given button as toolbar button.
     * 
     * @param pButton the button
     * @param pActionCommand the action command
     * @param pImage the image or <code>null</code> to leave image unset
     */
    private void configureToolBarButton(AbstractButton pButton, String pActionCommand, String pImage)
    {
        pButton.addActionListener(this);
        pButton.setActionCommand(pActionCommand);
        pButton.setFocusable(false);
        
        pButton.setMargin(new Insets(0, 0, 0, 0));

        if (SwingFactory.isMacLaF())
        {
            pButton.setPreferredSize(new Dimension(27, 27));
        }
        else
        {
            pButton.setPreferredSize(new Dimension(25, 25));
        }
        
        if (pImage != null)
        {
            pButton.setIcon(JVxUtil.getIcon("/com/sibvisions/rad/ui/swing/ext/images/htmleditor/" + pImage));
        }

        if (pButton instanceof JVxButton)
        {
            ((JVxButton)pButton).setBorderOnMouseEntered(true);
        }
        else if (pButton instanceof JVxToggleButton)
        {
            ((JVxToggleButton)pButton).setBorderOnMouseEntered(true);
        }
    }
    
    /**
     * Gets the rich text component.
     * 
     * @return The rich text component.
     */
    public JTextComponent getRichTextPane()
    {
        return editRichText;
    }
    
    /**
    * Gets the syntax text component.
    * 
    * @return The syntax text component.
    */
   public JTextComponent getSyntaxTextPane()
   {
       return editRawHtml;
   }

    /**
     * Returns the HTML text inside the body tag.
     * 
     * @param pText The text to strip.
     * @return The text.
     */
    public String getBodyText(String pText)
    {
        if (StringUtil.isEmpty(pText))
        {
            return "";
        }
        
        String sText = pText;
        String sStartTag = "<body>";

        int iStartIndex = sText.indexOf(sStartTag);

        if (iStartIndex < 0)
        {
            sStartTag = "</head>";
            iStartIndex = sText.indexOf(sStartTag);

            if (iStartIndex < 0)
            {
                sStartTag = "<html>";
                iStartIndex = sText.indexOf(sStartTag);
            }
        }

        if (iStartIndex >= 0)
        {
            iStartIndex += sStartTag.length();
        }
        else
        {
            iStartIndex = 0;
        }

        int iEndIndex = sText.lastIndexOf("</body>");

        if (iEndIndex < 0)
        {
            iEndIndex = sText.lastIndexOf("</html>");

            if (iEndIndex < 0)
            {
                iEndIndex = sText.length();
            }
        }

        return sText.substring(iStartIndex, iEndIndex);
    }

    /**
     * Returns the text of the HTML Editor.
     * 
     * @return The HTML text.
     */
    public String getText() 
    {
        String sWYSText;
        try
        {
            sWYSText = htmlDoc.getText(0, htmlDoc.getLength());            
        }
        catch (BadLocationException ble)
        {
            sWYSText = null;
        }
        
        if (StringUtil.isEmpty(sWYSText))
        {
            return "";
        }
        else
        {
            return "<html>" + getBodyText(editRichText.getText()) + "</html>";            
        }
    }
    
    /**
     * Sets the text.
     * 
     * @param pText The text.
     */
    public void setText(String pText)
    {
        editRichText.setText("<html><body>" + getBodyText(pText) + "</body></html>");            
     }

    /**
     * Shows debug info of an element in the console.
     * 
     * @param pElement The element.
     * @param pIntendation The amount of intendation to produce for better visuality.
     */
    private void debugElementInfo(Element pElement, int pIntendation)
    {
        String sIndentation = new String(new char[pIntendation]).replace("\0", "-");
        
        System.out.println(sIndentation + " ---------- ELEMENT-START ----------");
        if (pElement != null)
        {
            System.out.println(sIndentation + " ELEMENT at " + pElement.getStartOffset() + "/" + pElement.getEndOffset());
            System.out.println(sIndentation + " Element name " + pElement.getName());
            System.out.println(sIndentation + " Element class " + pElement.getClass().getName());
            
            AttributeSet set = pElement.getAttributes();
            
            System.out.println(sIndentation + " ELEMENT ATTRIBUTES");
            Enumeration a = set.getAttributeNames();
            while (a.hasMoreElements())
            {
                Object next = a.nextElement();
                System.out.println(sIndentation + " -----Attribute-----");
                System.out.println(sIndentation + " -- Class: " + next.getClass().getName());
                System.out.println(sIndentation + " -- Name : " + next.toString());
                System.out.println(sIndentation + " -- Value Class: " + set.getAttribute(next).getClass().getName());
                System.out.println(sIndentation + " -- Value Name : " + set.getAttribute(next).toString());
            } 
        }
        else
        {
            System.out.println(sIndentation + " ---------- ELEMENT NULL ----------");
        }
        System.out.println(sIndentation + " ---------- ELEMENT-END ----------");
    }
    
    /**
     * Applies styles in a set to the current selection.
     * 
     * @param pSet A set of attributes.
     */
    private void applyStyle(AttributeSet pSet)
    {
        int iLength = 0;
        if (editRichText.getSelectedText() != null)
        {
            iLength = editRichText.getSelectedText().length();
        }
        if (iLength > 0)
        {
            htmlDoc.setCharacterAttributes(Math.min(editRichText.getCaret().getDot(), editRichText.getCaret().getMark()), iLength, pSet, false);
        }

        MutableAttributeSet inputAttributes = htmlKit.getInputAttributes();
        inputAttributes.addAttributes(pSet);
    }

    /**
     * What the standard paragraph tag should be if a p-implied paragraph gets switched.
     * 
     * @param pDiv If <code>true</code> uses<code>HTML.Tag.DIV</code>, else <code>HTML.Tag.P</code>.
     */
    public void setDivTagAsParagraph(boolean pDiv)
    {
        if (pDiv)
        {
            standardParagraphTag = HTML.Tag.DIV;
        }
        else
        {            
            standardParagraphTag = HTML.Tag.P;
        }
    }
    
    /**
     * Updates the selection status of various toggle buttons.
     */
    private void updateToggleButtonStates()
    {
        if (!bDesignMode)
        {            
            MutableAttributeSet setAttributes = htmlKit.getInputAttributes();
            butBold.setSelected(StyleConstants.isBold(setAttributes));
            butItalic.setSelected(StyleConstants.isItalic(setAttributes));
            butUnderline.setSelected(StyleConstants.isUnderline(setAttributes));
            butSubscript.setSelected(StyleConstants.isSubscript(setAttributes));
            butSuperscript.setSelected(StyleConstants.isSuperscript(setAttributes));
            butStrikethrough.setSelected(StyleConstants.isStrikeThrough(setAttributes));
        }
    }

    /**
     * Inserts an absolute position string around the selection.
     */
    private void preservePosition()
    {   
        int iStart = Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
        int iEnd = Math.max(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
               
        
        editRichText.setCaretPosition(iStart);
        MutableAttributeSet setStart = new SimpleAttributeSet(htmlKit.getInputAttributes());
        editRichText.setCaretPosition(iEnd);
        MutableAttributeSet setEnd = new SimpleAttributeSet(htmlKit.getInputAttributes());
        
        editRichText.setSelectionStart(iStart);
        editRichText.setSelectionEnd(iEnd);

        try
        {
            htmlDoc.insertString(iStart, MAGIC_STRING, setStart);
            htmlDoc.insertString(iEnd + MAGIC_STRING.length(), MAGIC_STRING, setEnd);
        }
        catch (BadLocationException e2)
        {
            e2.printStackTrace();
        }
    }
    
    /**
     * Removes the absolute position string.
     * 
     * @throws BadLocationException If it fails.
     */
    private void removePosition() throws BadLocationException
    {
        String sWYSText = htmlDoc.getText(0, htmlDoc.getLength());
        int iSelectionStart = sWYSText.indexOf(MAGIC_STRING);
        int iSelectionEnd = sWYSText.indexOf(MAGIC_STRING, iSelectionStart + 1);
        
        String sHtml = editRichText.getText();
        
        int iIndex = sHtml.indexOf(MAGIC_STRING);
        sHtml = sHtml.substring(0, iIndex) + sHtml.substring(iIndex + MAGIC_STRING.length());
        
        iIndex = sHtml.indexOf(MAGIC_STRING);
        sHtml = sHtml.substring(0, iIndex) + sHtml.substring(iIndex + MAGIC_STRING.length());
        
        editRichText.setText(sHtml); //.replace(MAGIC_STRING, ""));
        
        editRichText.setSelectionStart(iSelectionStart);
        editRichText.setSelectionEnd(iSelectionEnd - MAGIC_STRING.length());
    }
    
    /**
     * Gets the selected paragraphs.
     * 
     * @return A list of {@link Element}.
     */
    private List<Element> getSelectedParagraphs()
    {
        int iSelectionStart = Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
        int iSelectionEnd = Math.max(editRichText.getSelectionStart(), editRichText.getSelectionEnd());

        return getParagraphs(iSelectionStart, iSelectionEnd);
    }

    /**
     * Gets all paragraphs in the range specified.
     * 
     * @param pStart The index to start.
     * @param pEnd The index to end.
     * @return A list of {@link Element}
     */
    private List<Element> getParagraphs(int pStart, int pEnd)
    {
        List<Element> listElements = new ArrayList<Element>();
        
        // Get the first and paragraph
        Element firstParapraph = htmlDoc.getParagraphElement(pStart);
        Element lastParagraph = htmlDoc.getParagraphElement(pEnd);
        listElements.add(firstParapraph);
        
        // Get every paragraph selected.
        while (firstParapraph.getEndOffset() < lastParagraph.getEndOffset())
        {
            firstParapraph = htmlDoc.getParagraphElement(firstParapraph.getEndOffset() + 1);
            listElements.add(firstParapraph);
        }
        
        return listElements;
    }
    
    /**
     * This method transforms every selected line in p-implied paragraphs are converted to 
     * {@link #standardParagraphTag} paragraphs.
     * 
     * @throws BadLocationException If it fails.
     */
    private void ensureNonImpliedParagraphs() throws BadLocationException
    {    
        List<Element> listParagraphs = getSelectedParagraphs();
        
        List<Element> listPImpliedParagraphs = new ArrayList<Element>();
        
        for (Element element : listParagraphs)
        {
            if (CommonUtil.equals(HTML.Tag.IMPLIED.toString(), element.getName()))
            {   
                Element elementParent = element.getParentElement();
                 
                if (elementParent != null)
                {
                    HTML.Tag tagParent = HTML.getTag(elementParent.getName());        
                    
                    if (tagParent == null 
                        || tagParent.equals(HTML.Tag.BODY) 
                        || !(tagParent.equals(HTML.Tag.DIV) || tagParent.equals(HTML.Tag.LI) 
                        || tagParent.equals(HTML.Tag.P)))
                    {
                        listPImpliedParagraphs.add(element);
                    }
                }
                else
                {
                    listPImpliedParagraphs.add(element);                    
                }
            }
        }
        
        if (listPImpliedParagraphs.isEmpty())
        {
            return;
        }
        
        //Get selection range
        int selectionStart = Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
        int selectionEnd = Math.max(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
        
        preservePosition();
        try 
        {        
            selectionEnd += MAGIC_STRING.length();
            
            //Last to first
            Collections.reverse(listPImpliedParagraphs);
            
            for (Element element : listPImpliedParagraphs)
            {
                int elementSelectionStart = Math.max(element.getStartOffset(), selectionStart);
                int elementSelectionEnd = Math.min(element.getEndOffset(), selectionEnd);
                
                //Find first linebreak
                int firstLinebreakPos = element.getStartOffset(); 
                for (int i = elementSelectionStart; i > element.getStartOffset(); i--)
                {
                    if (isBreakElement(htmlDoc.getCharacterElement(i)))
                    {
                        firstLinebreakPos = i;
                        break;
                    }
                }
                
                //Find last linebreak
                int lastLineBreakPost = element.getEndOffset();
                for (int i = elementSelectionEnd; i < element.getEndOffset(); i++)
                {
                    if (isBreakElement(htmlDoc.getCharacterElement(i)))
                    {
                        lastLineBreakPost = i;
                        break;
                    }
                }
                
                List<Integer> listLineBreakPositions = new ArrayList<Integer>();
                
                //Get all linebreaks
                for (int i = elementSelectionStart + 1; i < elementSelectionEnd; i++)
                {
                    if (isBreakElement(htmlDoc.getCharacterElement(i)))
                    {
                        listLineBreakPositions.add(Integer.valueOf(i));
                    }
                }
                
                //Lowest to highest -> highest to lowest.
                Collections.reverse(listLineBreakPositions);
                
                if (lastLineBreakPost == element.getEndOffset())
                {
                    htmlDoc.insertString(lastLineBreakPost - 1, MAGIC_STRING_2, htmlDoc.getCharacterElement(lastLineBreakPost - 1).getAttributes());                
                }
                else
                {
                    htmlDoc.replace(lastLineBreakPost, 1, MAGIC_STRING_2, htmlDoc.getCharacterElement(lastLineBreakPost - 1).getAttributes());    
                }
                
                for (Integer i : listLineBreakPositions)
                {
                    htmlDoc.replace(i.intValue(), 1, MAGIC_STRING_2 + MAGIC_STRING_2, htmlDoc.getCharacterElement(i.intValue() - 1).getAttributes());
                }
                
                if (firstLinebreakPos == element.getStartOffset())
                {
                    htmlDoc.insertString(firstLinebreakPos, MAGIC_STRING_2, htmlDoc.getCharacterElement(firstLinebreakPos + 1).getAttributes());                
                }
                else
                {
                    htmlDoc.replace(firstLinebreakPos, 1, MAGIC_STRING_2, htmlDoc.getCharacterElement(firstLinebreakPos + 1).getAttributes());    
                }
            }
            
            String sHtml = editRichText.getText();
            
            boolean bOpen = true;
            int index;
            while ((index = sHtml.indexOf(MAGIC_STRING_2)) != -1)
            {
                if (bOpen)
                {
                    sHtml = sHtml.substring(0, index) + "<" + standardParagraphTag.toString() + ">" + sHtml.substring(index + MAGIC_STRING_2.length());                
                }
                else
                {
                    sHtml = sHtml.substring(0, index) + "</" + standardParagraphTag.toString() + ">" + sHtml.substring(index + MAGIC_STRING_2.length());                
                }
                
                bOpen = !bOpen;
            }
            
            editRichText.setText(sHtml);
        }
        finally
        {
            removePosition();            
        }
    }
    
    /**
     * Returns whether or not the specified character is a break character.
     * 
     * @param pCharElement The char element to check.
     * @return True or False
     */
    private boolean isBreakElement(Element pCharElement)
    {
        Boolean isBreak = (Boolean)pCharElement.getAttributes().getAttribute(HTML.Tag.BR);
        
        return (CommonUtil.equals(HTML.Tag.BR.toString(), pCharElement.getName()) || isBreak != null && isBreak.booleanValue());
    }
    
    /**
     * Sets a paragraph attribute on all selected paragraphs.
     * 
     * @param pAttribute The attribute object.
     * @param pValue The value of the attribute. If <code>null</code>, removes the attribute.
     * @throws BadLocationException If setting the outer html of an element fails.
     * @throws IOException If setting the outer html of an element fails.
     */
    private void setParagraphAttribute(Object pAttribute, Object pValue) throws BadLocationException, IOException
    {   
        preservePosition();
        
        try
        {
            List<Element> listElements = getSelectedParagraphs();

            for (Element element : listElements)
            {
                //Implied could be in a list, or p-implied under body.
                //Can only align real paragraphs automatically.
                if (CommonUtil.equals(HTML.Tag.IMPLIED.toString(), element.getName()))
                {
                    Element elementParent = element.getParentElement();
                    HTML.Tag tagParent = HTML.getTag(elementParent.getName());

                    if (tagParent != null)
                    {
                        SimpleAttributeSet simpleSet = new SimpleAttributeSet(element.getAttributes());
                        simpleSet.removeAttribute(pAttribute);
                        
                        if (pValue != null)
                        {
                            simpleSet.addAttribute(pAttribute, pValue);                        
                        }

                        HTML.Tag tagParagraph = tagParent;
                        
                        if (HTML.Tag.BODY.equals(tagParent))
                        {
                            tagParagraph = standardParagraphTag;
                        }
                        
                        String sTagOpen = "<" + tagParagraph.toString();
                        String sTagClose = "</" + tagParagraph.toString() + ">";
                        
                        Enumeration<?> attributes = simpleSet.getAttributeNames();
                        
                        while (attributes.hasMoreElements())
                        {
                            Object attribute = attributes.nextElement();
                            if (!"name".equals(attribute.toString()))
                            {
                                sTagOpen += " " + attribute.toString() + "=\"" + simpleSet.getAttribute(attribute) + "\"";
                            }
                        }
                        
                        sTagOpen += ">";
                        
                        //If the paragraph is not the original, replace the parent element but keep all other sub elements intakt.
                        if (tagParagraph.equals(tagParent))
                        {
                            StringBuilder sb = new StringBuilder();
                            
                            for (int iIndex = 0; iIndex < elementParent.getElementCount(); iIndex++)
                            {
                                Element elementChild = elementParent.getElement(iIndex);
                                if (elementChild != element)
                                {
                                    sb.append(elementToHtml(elementChild, true));
                                }
                                else
                                {
                                    sb.append(sTagOpen);
                                    sb.append(elementToHtml(element, false));
                                    sb.append(sTagClose);
                                }
                            }
                            
                            htmlDoc.setOuterHTML(elementParent, sb.toString());     
                        }
                        else
                        {
                            htmlDoc.setOuterHTML(element, sTagOpen + elementToHtml(element, false) + sTagClose);                        
                        }
                    }
                }
                else
                {
                    SimpleAttributeSet simpleSet = new SimpleAttributeSet(element.getAttributes());
                    simpleSet.removeAttribute(pAttribute);
                    
                    if (pValue != null)
                    {
                        simpleSet.addAttribute(pAttribute, pValue);                        
                    }
                    htmlDoc.setParagraphAttributes(element.getStartOffset(), element.getEndOffset() - (element.getStartOffset() + 1), simpleSet, true);
                }
            } 
        }
        finally
        {
            removePosition();            
        }
        
    }

    /**
     * Converts an element to string.
     * 
     * @param pElement The element to convert.
     * @param pIncludeTag If the tag of the element should be included.
     * @return A string.
     */
    private String elementToHtml(Element pElement, boolean pIncludeTag)
    {
        final Boolean includeTag = Boolean.valueOf(pIncludeTag);
        final Element highestHierarchy = pElement;

        StringWriter stringWriter = new StringWriter();
        
        HTMLWriter htmlWriter = new HTMLWriter(stringWriter, htmlDoc, pElement.getStartOffset(), 
                                               pElement.getEndOffset() - pElement.getStartOffset())
        {
            /**
             * Checks if the element is synthesized and will not
             * be included in the html string output.
             */
            @Override
            protected boolean synthesizedElement(Element pElement)
            {
                return pElement.getStartOffset() < getStartOffset() || pElement.getStartOffset() > getEndOffset() || !inHierarchy(pElement) 
                        || (pElement == highestHierarchy && !includeTag.booleanValue()) || super.synthesizedElement(pElement);
            }

            /**
             * Checks if given element is in hierarchy.
             * 
             * @param pElement the element
             * @return <code>true</code> if in hierarchy, <code>false</code> otherwise
             */
            private boolean inHierarchy(Element pElement)
            {
                if (pElement != null)
                {
                    if (pElement == highestHierarchy)
                    {
                        return true;
                    }
                    else
                    {
                        return inHierarchy(pElement.getParentElement());
                    }
                }
                
                return false;
            }
        };            
        
        try
        {
            htmlWriter.write();
        }
        catch (Throwable pThrow)
        {
            //Ignore
        }
        
        return stringWriter.toString();
    }
    
    /**
     * Gets an image icon if already cached or creates a new cached instance if not
     * available.
     * 
     * @param pColor The color.
     * @return The image icon.
     */
    private ImageIcon getImageIcon(Color pColor) 
    {
        ImageIcon icon = hmpIconCache.get(pColor);
        
        if (icon == null)
        {
            BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setPaint(pColor);
            graphics.fillRect(1, 1, 14, 14);
            graphics.dispose();
            
            icon = new ImageIcon(image);
            
            hmpIconCache.put(pColor, icon);
        }

        return icon;
    }
    
    /**
     * Updates the buttonts to their correct state.
     */
    private void updateButtonStates()
    {
        boolean bEnabled = !butSwitchView.isSelected() && editRichText.isEditable() && editRichText.isEnabled();
        
        butBold.setEnabled(bEnabled);
        butItalic.setEnabled(bEnabled);
        butUnderline.setEnabled(bEnabled);
        butSubscript.setEnabled(bEnabled);
        butSuperscript.setEnabled(bEnabled);
        butAlignLeft.setEnabled(bEnabled);
        butAlignCenter.setEnabled(bEnabled);
        butAlignRight.setEnabled(bEnabled);
        butStrikethrough.setEnabled(bEnabled);
        butHorizontalRule.setEnabled(bEnabled);
        butOrderedList.setEnabled(bEnabled);
        butUnorderedList.setEnabled(bEnabled);
        butDebug.setEnabled(bEnabled);
        butRemoveFormatting.setEnabled(bEnabled);
        
        butBackgroundColor.setEnabled(bEnabled);
        butTextColor.setEnabled(bEnabled);
        butFontSize.setEnabled(bEnabled);
        butFontType.setEnabled(bEnabled);
        
        butBold.setSelected(false);
        butItalic.setSelected(false);
        butUnderline.setSelected(false);
        butSubscript.setSelected(false);
        butSuperscript.setSelected(false);
        butStrikethrough.setSelected(false);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Actions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Switches between the rich text and the html syntax.
     */
    public void switchView()
    {
        
        // if (getComponent(1).equals(scpRichText))
        if (butSwitchView.isSelected())
        {
            remove(scpRichText);
            editRawHtml.setText(getText());
            add(scpRawHtml, JVxBorderLayout.CENTER);
        }
        else
        {
            remove(scpRawHtml);
            add(scpRichText, JVxBorderLayout.CENTER);
            editRichText.setCaretPosition(0);
        }

        updateButtonStates();

        validate();
        repaint();
    }
    
    /**
     * Makes the selection subscript.
     */
    public void doSubscript()
    {
        SimpleAttributeSet simpleSet = new SimpleAttributeSet();
        StyleConstants.setSubscript(simpleSet, butSubscript.isSelected());
        applyStyle(simpleSet);
    }

    /**
     * Makes the selection superscript.
     */
    public void doSuperscript()
    {
        SimpleAttributeSet simpleSet = new SimpleAttributeSet();
        StyleConstants.setSuperscript(simpleSet, butSuperscript.isSelected());
        applyStyle(simpleSet);
    }

    /**
     * Makes the selection align left.
     * 
     * @throws BadLocationException If setting the outer html of an element fails.
     * @throws IOException If setting the outer html of an element fails.
     */
    public void doAlignLeft() throws BadLocationException, IOException
    {
        setParagraphAttribute(HTML.Attribute.ALIGN, null);
    }

    /**
     * Makes the selection align center.
     * 
     * @throws BadLocationException If setting the outer html of an element fails.
     * @throws IOException If setting the outer html of an element fails.
     */
    public void doAlignCenter() throws BadLocationException, IOException
    {
        setParagraphAttribute(HTML.Attribute.ALIGN, "center");
    }

    /**
     * Makes the selection align right.
     * 
     * @throws BadLocationException If setting the outer html of an element fails.
     * @throws IOException If setting the outer html of an element fails.
     */
    public void doAlignRight() throws BadLocationException, IOException
    {
        setParagraphAttribute(HTML.Attribute.ALIGN, "right");
    }
    
    /**
     * Inserts a break. <code>HTML.Tag.BR</code>
     * 
     * @param pKey The key event.
     * @throws IOException If it fails
     * @throws BadLocationException If it fails.
     */
    public void doBreak(KeyEvent pKey) throws BadLocationException, IOException
    {
        if (editRichText.getSelectedText() != null)
        {
            editRichText.replaceSelection("");
        }

        int iCaretPosition = Math.min(editRichText.getSelectionStart(), editRichText.getSelectionEnd());
        
        //Get the tag to break
        Element element = htmlDoc.getParagraphElement(iCaretPosition);
        
//        debugElementInfo(element, 0);
        
        boolean bInserted = false;
        while (element != null && !bInserted)
        {
            if (CommonUtil.equals(element.getName(), HTML.Tag.LI.toString())
                    || CommonUtil.equals(element.getName(), HTML.Tag.DIV.toString())
                    || CommonUtil.equals(element.getName(), HTML.Tag.P.toString()))
            {
                preservePosition();
                
                String sHtml = elementToHtml(element, true);
                String sTag = sHtml.substring(0, sHtml.indexOf(">") + 1);
                
                String sNewHtml = sHtml.substring(0, sHtml.indexOf(MAGIC_STRING));
                if (pKey.isShiftDown())
                {
                    sNewHtml = sNewHtml + "<br>";
                }
                else
                {
                    sNewHtml = sNewHtml + "</" + element.getName() + ">" + sTag;
                }
                
                sNewHtml = sNewHtml + sHtml.substring(sHtml.lastIndexOf(MAGIC_STRING) + MAGIC_STRING.length());
                
                htmlDoc.setOuterHTML(element, sNewHtml);
                bInserted = true;
            }
            else if (CommonUtil.equals(element.getName(), HTML.Tag.BODY.toString()))
            {
                htmlKit.insertHTML(htmlDoc, iCaretPosition, "<br>", 0, 0, HTML.Tag.BR);
                bInserted = true;
            }
            
            element = element.getParentElement();                
        }
        
        if (bInserted)
        {
            try
            {
                editRichText.setCaretPosition(iCaretPosition + 1);                
            }
            catch (Exception e)
            {
                //Ignore
            }
        }
    }
    
    /**
     * Inserts a horizontal rule. <code>HTML.Tag.HR</code>
     * 
     * @throws BadLocationException If it fails.
     * @throws IOException If it fails.
     */
    public void doHorizontalRule() throws BadLocationException, IOException
    {
        if (editRichText.getSelectedText() != null)
        {
            editRichText.replaceSelection("");
        }

        int iCaretPosition = editRichText.getCaretPosition();
        
        htmlKit.insertHTML(htmlDoc, iCaretPosition, "<hr>", 0, 0, HTML.Tag.HR);
        editRichText.setText(editRichText.getText());
        editRichText.setCaretPosition(iCaretPosition + 1);
    }

    /**
     * Makes the selection strike through.
     */
    public void doStrikethrough()
    {
        SimpleAttributeSet simpleSet = new SimpleAttributeSet();
        
        StyleConstants.setStrikeThrough(simpleSet, butStrikethrough.isSelected());
        
        applyStyle(simpleSet);
    }

    /**
     * Creates an ordered or unordered list.
     * 
     * @param pTag The <code>HTML.Tag</code> of the List. <code>HTML.Tag.OL</code> or <code>HTML.Tag.UL</code>
     * @throws IOException If it fails.
     * @throws BadLocationException If it fails.
     */
    public void doList(HTML.Tag pTag) throws BadLocationException, IOException
    {
        if (!CommonUtil.equals(HTML.Tag.OL, pTag) && !CommonUtil.equals(HTML.Tag.UL, pTag))
        {
            return;
        }
        
        preservePosition();
        
        try
        {
            //All should either be P, DIV or already in an LI as ensureNonImpliedParagraphs was called in actionPerformed first.
            List<Element> listSelectedParagraphs = getSelectedParagraphs();
            
            if (listSelectedParagraphs.isEmpty())
            {
                return;
            }
            
            Element elementHighestHierarchy = null;
            Element elementFirstChildSelected = null;
            Element elementLastChildSelected = null;
            
            Element elementFirstSelected = listSelectedParagraphs.get(0);
            Element elementLastSelected = listSelectedParagraphs.get(listSelectedParagraphs.size() - 1);
            
            //Get same parent
            List<Element> listHierarchyFirst = new ArrayList<Element>();
            List<Element> listHierarchySecond = new ArrayList<Element>();
            
            for (Element e = elementFirstSelected; e != null; e = e.getParentElement())
            {
                listHierarchyFirst.add(0, e);
            }
            
            for (Element e = elementLastSelected; e != null; e = e.getParentElement())
            {
                listHierarchySecond.add(0, e);
            }
            
            for (int i = 0; i < Math.min(listHierarchyFirst.size(), listHierarchySecond.size()); i++)
            {
                if (elementHighestHierarchy == null)
                {
                    if (!listHierarchyFirst.get(i).equals(listHierarchySecond.get(i)))
                    {
                        elementHighestHierarchy = listHierarchyFirst.get(i - 1);
                        elementFirstChildSelected = listHierarchyFirst.get(i);
                        elementLastChildSelected = listHierarchySecond.get(i);
                    }
                }
            }
            
            if (listSelectedParagraphs.size() == 1 
                || CommonUtil.equals(elementHighestHierarchy.getName(), HTML.Tag.UL.toString())
                || CommonUtil.equals(elementHighestHierarchy.getName(), HTML.Tag.OL.toString()))
            {
                //Get first p, div, or OL/UL element
                
                Element elementToReplace = null;
                Element elementToCheck = listSelectedParagraphs.get(0);
                
                boolean bIsList = false;
                
                while (elementToReplace == null && elementToCheck != null)
                {
                    if (CommonUtil.equals(elementToCheck.getName(), HTML.Tag.UL.toString())
                        || CommonUtil.equals(elementToCheck.getName(), HTML.Tag.OL.toString())
                        || CommonUtil.equals(elementToCheck.getName(), HTML.Tag.DIV.toString())
                        || CommonUtil.equals(elementToCheck.getName(), HTML.Tag.P.toString()))
                    {
                        elementToReplace = elementToCheck;
                        bIsList = CommonUtil.equals(elementToCheck.getName(), HTML.Tag.UL.toString())
                                  || CommonUtil.equals(elementToCheck.getName(), HTML.Tag.OL.toString());
                    }
                    
                    elementToCheck = elementToCheck.getParentElement();
                }
                
                if (bIsList)
                {
                    //Are you the same? Delete list
                    if (CommonUtil.equals(pTag.toString(), elementToReplace.getName()))
                    {
                        StringBuilder sb = new StringBuilder();
                        
                        for (Element subElements : getParagraphs(elementToReplace.getStartOffset(), elementToReplace.getEndOffset()))
                        {
                            sb.append("<" + standardParagraphTag.toString() + ">" + elementToHtml(subElements, false) + "</" + standardParagraphTag.toString() + ">");
                        }
                        
                        htmlDoc.setOuterHTML(elementToReplace, sb.toString());
                    }
                    else
                    {
                        htmlDoc.setOuterHTML(elementToReplace, "<" + pTag.toString() + ">" + elementToHtml(elementToReplace, false) + "</" + pTag.toString() + ">");
                    }
                }
                else
                {
                    htmlDoc.setOuterHTML(elementToReplace, "<" + pTag.toString() + "><li>" + elementToHtml(elementToReplace, false) + "</li></" + pTag.toString() + ">");
                }
                
            }
            else
            {
                StringBuilder sbPrefix = new StringBuilder();
                StringBuilder sbSuffix = new StringBuilder();
                StringBuilder sbConversion = new StringBuilder();
                
                List<Element> listChildren = new ArrayList<Element>();
                
                boolean beginFound = false;
                boolean endFound = false;
                
                for (int i = 0; i < elementHighestHierarchy.getElementCount(); i++)
                {
                    if (endFound)
                    {
                        sbSuffix.append(elementToHtml(elementHighestHierarchy.getElement(i), true));
                    }
                    else if (beginFound)
                    {
                        if (elementHighestHierarchy.getElement(i).equals(elementLastChildSelected))
                        {
                            endFound = true;
                        }
                        listChildren.add(elementHighestHierarchy.getElement(i));
                    }
                    else
                    {
                        if (elementHighestHierarchy.getElement(i).equals(elementFirstChildSelected))
                        {
                            listChildren.add(elementHighestHierarchy.getElement(i));
                            beginFound = true;
                        }
                        else
                        {
                            sbPrefix.append(elementToHtml(elementHighestHierarchy.getElement(i), true));
                        }
                    }
                }
                
                sbConversion.append("<" + pTag.toString() + ">");
                for (Element elementToConvert : listChildren)
                {
                    if (CommonUtil.equals(elementToConvert.getName(), HTML.Tag.UL.toString())
                            || CommonUtil.equals(elementToConvert.getName(), HTML.Tag.OL.toString()))
                    {
                        sbConversion.append(elementToHtml(elementToConvert, false));
                    }
                    else if (CommonUtil.equals(elementToConvert.getName(), HTML.Tag.LI.toString()))
                    {
                        sbConversion.append(elementToHtml(elementToConvert, true));
                    }
                    else
                    {
                        sbConversion.append("<li>" + elementToHtml(elementToConvert, false) + "</li>");
                    }
                }
                sbConversion.append("</" + pTag.toString() + ">");
                
                htmlDoc.setOuterHTML(elementHighestHierarchy, sbPrefix.toString() + sbConversion.toString() + sbSuffix.toString());
            }
        }
        finally
        {
            removePosition();            
        }     
    }

    /**
     * Removes formatting of the selection.
     * 
     * @throws BadLocationException If it fails.
     */
    public void doRemoveFormatting() throws BadLocationException
    {
        preservePosition();
        
        try
        {
            String sHtml = editRichText.getText();

            int iHtmlSelectionStart = sHtml.indexOf(MAGIC_STRING);
            int iHtmlSelectionEnd = sHtml.indexOf(MAGIC_STRING, iHtmlSelectionStart + 1);
            
            String sSubHtml = sHtml.substring(iHtmlSelectionStart, iHtmlSelectionEnd);
            
            sSubHtml = sSubHtml.replace("<br>", "<br>" + MAGIC_STRING_2);
            
            sHtml = sHtml.substring(0, iHtmlSelectionStart) + sSubHtml + sHtml.substring(iHtmlSelectionEnd);
            
            editRichText.setText(sHtml);
            
            String sWYSText = htmlDoc.getText(0, htmlDoc.getLength());
            int iSelectionStart = sWYSText.indexOf(MAGIC_STRING);
            
            int iSelectionEnd = sWYSText.indexOf(MAGIC_STRING, iSelectionStart + 1);

            SimpleAttributeSet simpleSet = new SimpleAttributeSet();
            simpleSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            
            htmlDoc.setCharacterAttributes(iSelectionStart, iSelectionEnd - iSelectionStart, simpleSet, true);

            editRichText.setText(editRichText.getText().replace(MAGIC_STRING_2, "<br>"));
        }
        finally
        {
            removePosition();            
        }
    }

    /**
     * Changes the font of the selection.
     * 
     * @param pFont The font to change to.
     */
    public void doFontType(String pFont)
    {
        if (!StringUtil.isEmpty(pFont))
        {
            String sFont = pFont.toLowerCase();
            
            MutableAttributeSet tagAttrs = new SimpleAttributeSet();
            
            if (CommonUtil.equals("Default", pFont))
            {
                tagAttrs.addAttribute(StyleConstants.FontFamily, "");                
            }
            else
            {
                tagAttrs.addAttribute(StyleConstants.FontFamily, sFont);                                
            }
            
            applyStyle(tagAttrs);
        }
    }
    
    /**
     * Updates the translation.
     */
    private void updateTranslation()
    {
        butFontSize.setText(translate(TEXT_FONT_SIZE));
        
        for (Component menuItem : popFontSizes.getComponents())
        {
            ((JMenuItem)menuItem).setText(translate(((JMenuItem)menuItem).getActionCommand()));
        }
        
        butFontType.setText(translate(TEXT_FONT_TYPE));
        
        butTextColor.setText(translate(TEXT_TEXT_COLOR));
        
        for (Component menuItem : popTextColors.getComponents())
        {
            ((JMenuItem)menuItem).setText(translate(((JMenuItem)menuItem).getActionCommand().substring(PREFIX_FOREGROUND.length())));
        }
        
        butBackgroundColor.setText(translate(TEXT_BACKGROUND_COLOR));
        
        for (Component menuItem : popBackgroundColors.getComponents())
        {
            ((JMenuItem)menuItem).setText(translate(((JMenuItem)menuItem).getActionCommand().substring(PREFIX_BACKGROUND.length())));
        }
    }
    
    //****************************************************************
    // Subclass definition
    //****************************************************************    
    
    /**
     * The <code>BackgroundAction</code> defines an action for background color handling.
     * 
     * @author Toni Heiss
     */
    public static class BackgroundAction extends StyledTextAction
    {
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Class members
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** The color to set. */
        private Color color;

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Initialization
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Creates a new instance of <code>BackgroundAction</code>.
         *
         * @param pActionName the action name
         * @param pColor the background color
         */
        public BackgroundAction(String pActionName, Color pColor)
        {
            super(pActionName);
            
            color = pColor;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Overwritten methods
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        /**
         * Sets the foreground color.
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JEditorPane editor = getEditor(e);
            
            if (editor != null)
            {
                HTMLDocument document = (HTMLDocument)editor.getDocument();
                
                int idx = Math.min(editor.getSelectionStart(), editor.getSelectionEnd());
                int idxEnd = Math.max(editor.getSelectionStart(), editor.getSelectionEnd());
                
                SimpleAttributeSet colourSet = new SimpleAttributeSet();
                colourSet.addAttribute("style", "background-color:#" + Integer.toHexString(color.getRGB()).substring(2));
                
                SimpleAttributeSet set = new SimpleAttributeSet();
                set.addAttribute(HTML.Tag.SPAN, colourSet);
                document.setCharacterAttributes(idx, idxEnd - idx, set, false);
                
                editor.setText(editor.getText());
                editor.setSelectionStart(idx);
                editor.setSelectionEnd(idxEnd);
            }
        }
        
    }   // BackgroundAction
    
}   // JVxHtmlEditor
