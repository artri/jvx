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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;

import research.syntax.JavaTokenMarker;
import research.syntax.SyntaxDocument;
 

/**
 * Test implementation.
 * 
 * @author Martin Handsteiner
 */
public class SwingTest extends JFrame
{
	
	/**
	 * Highlights syntax in a DefaultStyledDocument.  Allows any number of keywords to 
	 * be formatted in any number of user-defined styles.
	 *
	 * @author camickr (primary author; java sun forums user)
	 * @author David Underhill
	 */
	static class MultiSyntaxDocument extends DefaultStyledDocument
	{
	    
	    //<editor-fold defaultstate="collapsed" desc="        Defaults         ">
	    
	    public static final String DEFAULT_FONT_FAMILY = "Courier New";
	    public static final int    DEFAULT_FONT_SIZE   = 11;
	           
	    public static final SimpleAttributeSet DEFAULT_NORMAL;
	    public static final SimpleAttributeSet DEFAULT_COMMENT;
	    public static final SimpleAttributeSet DEFAULT_STRING;
	    public static final SimpleAttributeSet DEFAULT_KEYWORD;
	    
	    static {
	        DEFAULT_NORMAL = new SimpleAttributeSet();
			StyleConstants.setForeground( DEFAULT_NORMAL, Color.BLACK);
	        StyleConstants.setFontFamily( DEFAULT_NORMAL, DEFAULT_FONT_FAMILY );
	        StyleConstants.setFontSize(   DEFAULT_NORMAL, DEFAULT_FONT_SIZE );
	        
			DEFAULT_COMMENT = new SimpleAttributeSet();
			StyleConstants.setForeground(DEFAULT_COMMENT, new java.awt.Color( 51, 102, 0 ) ); //dark green
			StyleConstants.setFontFamily( DEFAULT_COMMENT, DEFAULT_FONT_FAMILY );
	        StyleConstants.setFontSize(   DEFAULT_COMMENT, DEFAULT_FONT_SIZE );
	        
			DEFAULT_STRING = new SimpleAttributeSet();
			StyleConstants.setForeground( DEFAULT_STRING, new java.awt.Color( 153, 0, 107 ) ); //dark pink
	        StyleConstants.setFontFamily( DEFAULT_STRING, DEFAULT_FONT_FAMILY );
	        StyleConstants.setFontSize(   DEFAULT_STRING, DEFAULT_FONT_SIZE );
	        
	        //default style for new keyword types
	        DEFAULT_KEYWORD = new SimpleAttributeSet();
			StyleConstants.setForeground( DEFAULT_KEYWORD, new java.awt.Color( 0, 0, 153 ) ); //dark blue
	        StyleConstants.setBold(       DEFAULT_KEYWORD, true );
	        StyleConstants.setFontFamily( DEFAULT_KEYWORD, DEFAULT_FONT_FAMILY );
	        StyleConstants.setFontSize(   DEFAULT_KEYWORD, DEFAULT_FONT_SIZE );
	    }
	    
	    //</editor-fold>
	    
	    
	    //<editor-fold defaultstate="collapsed" desc="         Fields          ">
	    
		private DefaultStyledDocument doc;
		private Element rootElement;
	 
		private boolean multiLineComment;
		private MutableAttributeSet normal  = DEFAULT_NORMAL;
		private MutableAttributeSet comment = DEFAULT_COMMENT;
		private MutableAttributeSet quote   = DEFAULT_STRING;
	 
		private HashMap<String, MutableAttributeSet> keywords;
	    
	    private int fontSize    = DEFAULT_FONT_SIZE;
	    private String fontName = DEFAULT_FONT_FAMILY;
	    
	    //</editor-fold>
	 
	    
	    //<editor-fold defaultstate="collapsed" desc="      Constructors       ">
	    
		public MultiSyntaxDocument( final HashMap<String, MutableAttributeSet> keywords ) {
			doc = this;
			rootElement = doc.getDefaultRootElement();
			putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
	        
			this.keywords = keywords;
		}
	    
	    //</editor-fold>
	 
	    
	    //<editor-fold defaultstate="collapsed" desc="  Highlighting Setters  ">
	    
	    public enum ATTR_TYPE { Normal, Comment, Quote; }
	        
	    /**
	     * Sets the font of the specified attribute
	     * @param attr   the attribute to apply this font to (normal, comment, string)
	     * @param style  font style (Font.BOLD, Font.ITALIC, Font.PLAIN)
	     */
	    public void setAttributeFont( ATTR_TYPE attr, int style ) {
	        Font f = new Font( fontName, style, fontSize );
	        
	        if(      attr == ATTR_TYPE.Comment ) setAttributeFont( comment, f );
	        else if( attr == ATTR_TYPE.Quote   ) setAttributeFont( quote,   f );
	        else                                 setAttributeFont( normal,  f );
	    }
	    
	    /**
	     * Sets the font of the specified attribute
	     * @param attr  attribute to apply this font to
	     * @param f     the font to use 
	     */
	    public static void setAttributeFont( MutableAttributeSet attr, Font f ) {
	        StyleConstants.setBold(         attr, f.isBold()                      );
	        StyleConstants.setItalic(       attr, f.isItalic()                    );
	        StyleConstants.setFontFamily(   attr, f.getFamily()                   );
	        StyleConstants.setFontSize(     attr, f.getSize()                     );
	    }
	    
	    /**
	     * Sets the foreground (font) color of the specified attribute
	     * @param attr  the attribute to apply this font to (normal, comment, string)
	     * @param c     the color to use 
	     */
	    public void setAttributeColor( ATTR_TYPE attr, Color c ) {
	        if(      attr == ATTR_TYPE.Comment ) setAttributeColor( comment, c );
	        else if( attr == ATTR_TYPE.Quote   ) setAttributeColor( quote,   c );
	        else                                 setAttributeColor( normal,  c );
	    }
	    
	    /**
	     * Sets the foreground (font) color of the specified attribute
	     * @param attr  attribute to apply this color to
	     * @param c  the color to use 
	     */
	    public static void setAttributeColor( MutableAttributeSet attr, Color c ) {
			StyleConstants.setForeground( attr, c );
	    }
	    
	    //</editor-fold>
	    
	    
	    //<editor-fold defaultstate="collapsed" desc="  Keyword Specification ">
	    
	    /**
	     * Associates a keyword with a particular formatting style
	     *
	     * @param keyword  the token or word to format
	     * @param attr     how to format keyword
	     */
	    public void addKeyword( String keyword, MutableAttributeSet attr ) {
	        keywords.put( keyword, attr );
	    }
	    
	    /**
	     * Gets the formatting for a keyword
	     *
	     * @param keyword  the token or word to stop formatting
	     *
	     * @return how keyword is formatted, or null if no formatting is applied to it
	     */
	    public MutableAttributeSet getKeywordFormatting( String keyword ) {
	        return keywords.get( keyword );
	    }
	    
	    /**
	     * Removes an association between a keyword with a particular formatting style
	     *
	     * @param keyword  the token or word to stop formatting
	     */
	    public void removeKeyword( String keyword ) {
	        keywords.remove( keyword );
	    }
	    
	    //</editor-fold>
	    
	    
	    //<editor-fold defaultstate="collapsed" desc="     Tab Size Setup     ">
	    
	    /** sets the number of characters per tab */
	    @SuppressWarnings("deprecation")
		public void setTabs( int charactersPerTab) {
	        Font f = new Font( fontName, Font.PLAIN, fontSize );
	        
			FontMetrics fm = java.awt.Toolkit.getDefaultToolkit().getFontMetrics( f );
			int charWidth = fm.charWidth( 'w' );
			int tabWidth = charWidth * charactersPerTab;
	 
			TabStop[] tabs = new TabStop[35];
	 
			for (int j = 0; j < tabs.length; j++)
			{
				int tab = j + 1;
				tabs[j] = new TabStop( tab * tabWidth );
			}
	 
			TabSet tabSet = new TabSet(tabs);
			SimpleAttributeSet attributes = new SimpleAttributeSet();
			StyleConstants.setTabSet(attributes, tabSet);
			int length = this.getLength();
			this.setParagraphAttributes(0, length, attributes, false);
		}
	    
	    //</editor-fold>
	    
	    
	    //<editor-fold defaultstate="collapsed" desc=" Syntax Highlighting   ">
	    
		/*
		 *  Override to apply syntax highlighting after the document has been updated
		 */
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
		{
			if (str.equals("{"))
				str = addMatchingBrace(offset);
	 
			super.insertString(offset, str, a);
	        processChangedLines(offset, str.length());
		}
	 
		/*
		 *  Override to apply syntax highlighting after the document has been updated
		 */
		public void remove(int offset, int length) throws BadLocationException
		{
			super.remove(offset, length);
			processChangedLines(offset, 0);
		}
	 
		/*
		 *  Determine how many lines have been changed,
		 *  then apply highlighting to each line
		 */
		public void processChangedLines(int offset, int length)
			throws BadLocationException
		{
			String content = doc.getText(0, doc.getLength());
	 
			//  The lines affected by the latest document update
	 
			int startLine = rootElement.getElementIndex( offset );
			int endLine = rootElement.getElementIndex( offset + length );
	 
			//  Make sure all comment lines prior to the start line are commented
			//  and determine if the start line is still in a multi line comment
	 
			setMultiLineComment( commentLinesBefore( content, startLine ) );
	 
			//  Do the actual highlighting
	 
			for (int i = startLine; i <= endLine; i++)
			{
				applyHighlighting(content, i);
			}
	 
			//  Resolve highlighting to the next end multi line delimiter
	 
			if (isMultiLineComment())
				commentLinesAfter(content, endLine);
			else
				highlightLinesAfter(content, endLine);
		}
	 
		/*
		 *  Highlight lines when a multi line comment is still 'open'
		 *  (ie. matching end delimiter has not yet been encountered)
		 */
		private boolean commentLinesBefore(String content, int line)
		{
			int offset = rootElement.getElement( line ).getStartOffset();
	 
			//  Start of comment not found, nothing to do
	 
			int startDelimiter = lastIndexOf( content, getStartDelimiter(), offset - 2 );
	 
			if (startDelimiter < 0)
				return false;
	 
			//  Matching start/end of comment found, nothing to do
	 
			int endDelimiter = indexOf( content, getEndDelimiter(), startDelimiter );
	 
			if (endDelimiter < offset & endDelimiter != -1)
				return false;
	 
			//  End of comment not found, highlight the lines
	 
			doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
			return true;
		}
	 
		/*
		 *  Highlight comment lines to matching end delimiter
		 */
		private void commentLinesAfter(String content, int line)
		{
			int offset = rootElement.getElement( line ).getEndOffset();
	 
			//  End of comment not found, nothing to do
	 
			int endDelimiter = indexOf( content, getEndDelimiter(), offset );
	 
			if (endDelimiter < 0)
				return;
	 
			//  Matching start/end of comment found, comment the lines
	 
			int startDelimiter = lastIndexOf( content, getStartDelimiter(), endDelimiter );
	 
			if (startDelimiter < 0 || startDelimiter <= offset)
			{
				doc.setCharacterAttributes(offset, endDelimiter - offset + 1, comment, false);
			}
		}
	 
		/*
		 *  Highlight lines to start or end delimiter
		 */
		private void highlightLinesAfter(String content, int line)
			throws BadLocationException
		{
			int offset = rootElement.getElement( line ).getEndOffset();
	 
			//  Start/End delimiter not found, nothing to do
	 
			int startDelimiter = indexOf( content, getStartDelimiter(), offset );
			int endDelimiter = indexOf( content, getEndDelimiter(), offset );
	 
			if (startDelimiter < 0)
				startDelimiter = content.length();
	 
			if (endDelimiter < 0)
				endDelimiter = content.length();
	 
			int delimiter = Math.min(startDelimiter, endDelimiter);
	 
			if (delimiter < offset)
				return;
	 
			//	Start/End delimiter found, reapply highlighting
	 
			int endLine = rootElement.getElementIndex( delimiter );
	 
			for (int i = line + 1; i < endLine; i++)
			{
				Element branch = rootElement.getElement( i );
				Element leaf = doc.getCharacterElement( branch.getStartOffset() );
				AttributeSet as = leaf.getAttributes();
	 
				if ( as.isEqual(comment) )
					applyHighlighting(content, i);
			}
		}
	 
		/*
		 *  Parse the line to determine the appropriate highlighting
		 */
		private void applyHighlighting(String content, int line)
			throws BadLocationException
		{
			int startOffset = rootElement.getElement( line ).getStartOffset();
			int endOffset = rootElement.getElement( line ).getEndOffset() - 1;
	 
			int lineLength = endOffset - startOffset;
			int contentLength = content.length();
	 
			if (endOffset >= contentLength)
				endOffset = contentLength - 1;
	 
			//  check for multi line comments
			//  (always set the comment attribute for the entire line)
	 
			if (endingMultiLineComment(content, startOffset, endOffset)
			||  isMultiLineComment()
			||  startingMultiLineComment(content, startOffset, endOffset) )
			{
				doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
				return;
			}
	 
			//  set normal attributes for the line
	 
			doc.setCharacterAttributes(startOffset, lineLength, normal, true);
	 
			//  check for single line comment
	 
			int index = content.indexOf(getSingleLineDelimiter(), startOffset);
	 
			if ( (index > -1) && (index < endOffset) )
			{
				doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
				endOffset = index - 1;
			}
	 
			//  check for tokens
	 
			checkForTokens(content, startOffset, endOffset);
		}
	 
		/*
		 *  Does this line contain the start delimiter
		 */
		private boolean startingMultiLineComment(String content, int startOffset, int endOffset)
			throws BadLocationException
		{
			int index = indexOf( content, getStartDelimiter(), startOffset );
	 
			if ( (index < 0) || (index > endOffset) )
				return false;
			else
			{
				setMultiLineComment( true );
				return true;
			}
		}
	 
		/*
		 *  Does this line contain the end delimiter
		 */
		private boolean endingMultiLineComment(String content, int startOffset, int endOffset)
			throws BadLocationException
		{
			int index = indexOf( content, getEndDelimiter(), startOffset );
	 
			if ( (index < 0) || (index > endOffset) )
				return false;
			else
			{
				setMultiLineComment( false );
				return true;
			}
		}
	 
		/*
		 *  We have found a start delimiter
		 *  and are still searching for the end delimiter
		 */
		private boolean isMultiLineComment()
		{
			return multiLineComment;
		}
	 
		private void setMultiLineComment(boolean value)
		{
			multiLineComment = value;
		}
	 
		/*
		 *	Parse the line for tokens to highlight
		 */
		private void checkForTokens(String content, int startOffset, int endOffset)
		{
			while (startOffset <= endOffset)
			{
				//  skip the delimiters to find the start of a new token
	 
				while ( isDelimiter( content.substring(startOffset, startOffset + 1) ) )
				{
					if (startOffset < endOffset)
						startOffset++;
					else
						return;
				}
	 
				//  Extract and process the entire token
	 
				if ( isQuoteDelimiter( content.substring(startOffset, startOffset + 1) ) )
					startOffset = getQuoteToken(content, startOffset, endOffset);
				else
					startOffset = getOtherToken(content, startOffset, endOffset);
			}
		}
	 
		/*
		 *
		 */
		private int getQuoteToken(String content, int startOffset, int endOffset)
		{
			String quoteDelimiter = content.substring(startOffset, startOffset + 1);
			String escapeString = getEscapeString(quoteDelimiter);
	 
			int index;
			int endOfQuote = startOffset;
	 
			//  skip over the escape quotes in this quote
	 
			index = content.indexOf(escapeString, endOfQuote + 1);
	 
			while ( (index > -1) && (index < endOffset) )
			{
				endOfQuote = index + 1;
				index = content.indexOf(escapeString, endOfQuote);
			}
	 
			// now find the matching delimiter
	 
			index = content.indexOf(quoteDelimiter, endOfQuote + 1);
	 
			if ( (index < 0) || (index > endOffset) )
				endOfQuote = endOffset;
			else
				endOfQuote = index;
	 
			doc.setCharacterAttributes(startOffset, endOfQuote - startOffset + 1, quote, false);
	 
			return endOfQuote + 1;
		}
	 
		/*
		 *
		 */
		private int getOtherToken(String content, int startOffset, int endOffset)
		{
			int endOfToken = startOffset + 1;
	 
			while ( endOfToken <= endOffset )
			{
				if ( isDelimiter( content.substring(endOfToken, endOfToken + 1) ) )
					break;
	 
				endOfToken++;
			}
	 
			String token = content.substring(startOffset, endOfToken);
	 
	        //see if this token has a highlighting format associated with it
	        MutableAttributeSet attr = keywords.get( token );
			if( attr != null )
			{
				doc.setCharacterAttributes(startOffset, endOfToken - startOffset, attr, false);
			}
	 
			return endOfToken + 1;
		}
	 
		/*
		 *  Assume the needle will the found at the start/end of the line
		 */
		private int indexOf(String content, String needle, int offset)
		{
			int index;
	 
			while ( (index = content.indexOf(needle, offset)) != -1 )
			{
				String text = getLine( content, index ).trim();
	 
				if (text.startsWith(needle) || text.endsWith(needle))
					break;
				else
					offset = index + 1;
			}
	 
			return index;
		}
	 
		/*
		 *  Assume the needle will the found at the start/end of the line
		 */
		private int lastIndexOf(String content, String needle, int offset)
		{
			int index;
	 
			while ( (index = content.lastIndexOf(needle, offset)) != -1 )
			{
				String text = getLine( content, index ).trim();
	 
				if (text.startsWith(needle) || text.endsWith(needle))
					break;
				else
					offset = index - 1;
			}
	 
			return index;
		}
	 
		private String getLine(String content, int offset)
		{
			int line = rootElement.getElementIndex( offset );
			Element lineElement = rootElement.getElement( line );
			int start = lineElement.getStartOffset();
			int end = lineElement.getEndOffset();
			return content.substring(start, end - 1);
		}
	 
		/*
		 *  Override for other languages
		 */
		protected boolean isDelimiter(String character)
		{
			String operands = ";:{}()[]+-/%<=>!&|^~*";
	 
			if (Character.isWhitespace( character.charAt(0) ) ||
				operands.indexOf(character) != -1 )
				return true;
			else
				return false;
		}
	 
		/*
		 *  Override for other languages
		 */
		protected boolean isQuoteDelimiter(String character)
		{
			String quoteDelimiters = "\"'";
	 
			if (quoteDelimiters.indexOf(character) < 0)
				return false;
			else
				return true;
		}
	 
		/*
		 *  Override for other languages
		 */
		protected String getStartDelimiter()
		{
			return "/*";
		}
	 
		/*
		 *  Override for other languages
		 */
		protected String getEndDelimiter()
		{
			return "*/";
		}
	 
		/*
		 *  Override for other languages
		 */
		protected String getSingleLineDelimiter()
		{
			return "//";
		}
	 
		/*
		 *  Override for other languages
		 */
		protected String getEscapeString(String quoteDelimiter)
		{
			return "\\" + quoteDelimiter;
		}
	 
		/*
		 *
		 */
		protected String addMatchingBrace(int offset) throws BadLocationException
		{
			StringBuffer whiteSpace = new StringBuffer();
			int line = rootElement.getElementIndex( offset );
			int i = rootElement.getElement(line).getStartOffset();
	 
			while (true)
			{
				String temp = doc.getText(i, 1);
	 
				if (temp.equals(" ") || temp.equals("\t"))
				{
					whiteSpace.append(temp);
					i++;
				}
				else
					break;
			}
	 
			return "{\n" + whiteSpace.toString() + "\t\n" + whiteSpace.toString() + "}";
		}
	 
	    //</editor-fold>
	 
	    
	    //<editor-fold defaultstate="collapsed" desc="  Accessors/Mutators   ">
	    
	    /** gets the current font size */
	    public int getFontSize() { return fontSize; }
	 
	    /** sets the current font size (affects all built-in styles) */
	    public void setFontSize(int fontSize) {
	        this.fontSize = fontSize;
	        StyleConstants.setFontSize(   normal,  fontSize );
	        StyleConstants.setFontSize(   quote,   fontSize );
	        StyleConstants.setFontSize(   comment, fontSize );
	    }
	 
	    /** gets the current font family */
	    public String getFontName() { return fontName; }
	 
	    /** sets the current font family (affects all built-in styles) */
	    public void setFontName(String fontName) {
	        this.fontName = fontName;
	        StyleConstants.setFontFamily( normal,  fontName );
	        StyleConstants.setFontFamily( quote,   fontName );
	        StyleConstants.setFontFamily( comment, fontName );
	    }
	    
	    
	    //</editor-fold>
	    
	}

	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		EditorKit editorKit = new StyledEditorKit()
		{
			public Document createDefaultDocument()
			{
                MutableAttributeSet attr = MultiSyntaxDocument.DEFAULT_KEYWORD;
                HashMap<String, MutableAttributeSet> keywords = new HashMap<String, MutableAttributeSet>();
                keywords.put( "abstract",   attr );
                keywords.put( "boolean",    attr );
                keywords.put( "break",      attr );
                keywords.put( "byte",       attr );
                keywords.put( "byvalue",    attr );
                keywords.put( "case",       attr );
                keywords.put( "cast",       attr );
                keywords.put( "catch",      attr );
                keywords.put( "char",       attr );
                keywords.put( "class",      attr );
                keywords.put( "const",      attr );
                keywords.put( "continue",   attr );
                keywords.put( "default",    attr );
                keywords.put( "do",         attr );
                keywords.put( "double",     attr );
                keywords.put( "else",       attr );
                keywords.put( "extends",    attr );
                keywords.put( "false",      attr );
                keywords.put( "final",      attr );
                keywords.put( "finally",    attr );
                keywords.put( "float",      attr );
                keywords.put( "for",        attr );
                keywords.put( "future",     attr );
                keywords.put( "generic",    attr );
                keywords.put( "goto",       attr );
                keywords.put( "if",         attr );
                keywords.put( "implements", attr );
                keywords.put( "import",     attr );
                keywords.put( "inner",      attr );
                keywords.put( "instanceof", attr );
                keywords.put( "int",        attr );
                keywords.put( "interface",  attr );
                keywords.put( "long",       attr );
                keywords.put( "native",     attr );
                keywords.put( "new",        attr );
                keywords.put( "null",       attr );
                keywords.put( "operator",   attr );
                keywords.put( "outer",      attr );
                keywords.put( "package",    attr );
                keywords.put( "private",    attr );
                keywords.put( "protected",  attr );
                keywords.put( "public",     attr );
                keywords.put( "rest",       attr );
                keywords.put( "return",     attr );
                keywords.put( "short",      attr );
                keywords.put( "static",     attr );
                keywords.put( "super",      attr );
                keywords.put( "switch",     attr );
                keywords.put( "synchronized", attr );
                keywords.put( "this",       attr );
                keywords.put( "throw",      attr );
                keywords.put( "throws",     attr );
                keywords.put( "transient",  attr );
                keywords.put( "true",       attr );
                keywords.put( "try",        attr );
                keywords.put( "var",        attr );
                keywords.put( "void",       attr );
                keywords.put( "volatile",   attr );
                keywords.put( "while",      attr );
                
                SimpleAttributeSet operatorTest = new SimpleAttributeSet();
                MultiSyntaxDocument.setAttributeColor( operatorTest, new Color( 255, 0, 0 ) );
                MultiSyntaxDocument.setAttributeFont( operatorTest, new Font( "Courier New", Font.BOLD, 11 ) );
                keywords.put( "dound",         operatorTest );
                
                
				MultiSyntaxDocument doc = new MultiSyntaxDocument( keywords );
                
                doc.setTabs( 4 );
                
                return doc;
			}
		};

		final JEditorPane edit = new JEditorPane();
		edit.setEditorKitForContentType("text/java", editorKit);
		
		try {
		  edit.read(new FileInputStream("intern/research/SwingTest.java"), null);
		}catch (Exception ex) {
			ex.printStackTrace();
		}


	    JScrollPane scrollEdit = new JScrollPane();
	    scrollEdit.setViewportView(edit);
		
		JToggleButton buttonHighlight = new JToggleButton("Enable SyntaxHighlight");
		buttonHighlight.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent pEv) {
				if (pEv.getStateChange() ==  ItemEvent.SELECTED) {
					edit.setContentType("text/java");
				}else {
					edit.setEditorKit(null);
				}
				try {
				  edit.read(new FileInputStream("intern/research/SwingTest.java"), null);
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JDesktopPane rootPane = new JDesktopPane();

		SwingTest frame = new SwingTest();
		frame.setLayeredPane(rootPane);//setContentPane(rootPane);
		frame.setSize(1024, 768);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JInternalFrame f1 = new JInternalFrame("Own internal Frame 1", true, true, true, true);
		rootPane.add(f1);
		f1.getContentPane().setLayout(new BorderLayout());
//		f1.getContentPane().add(new JTextArea());
		f1.getContentPane().add(scrollEdit, BorderLayout.CENTER);
		f1.getContentPane().add(buttonHighlight, BorderLayout.SOUTH);
//		f1.pack();
		f1.setBounds(0,0,800,600);
		f1.setVisible(true);


		final JEditorPane browser = new JEditorPane();
		browser.setEditable(false);
		browser.setContentType("text/html");

		try {
			browser.read(new FileInputStream("src/com/sibvisions/rad/package.html"), null);
		}catch (Exception ex) {
			ex.printStackTrace();
		}

	    JScrollPane scrollBrowser = new JScrollPane();
	    scrollBrowser.setViewportView(browser);
		
		JToggleButton buttonSource = new JToggleButton("View Source");
		buttonSource.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent pEv) {
				if (pEv.getStateChange() ==  ItemEvent.SELECTED) {
					browser.setEditorKit(null);
				}else {
					browser.setContentType("text/html");
				}
				try {
					browser.read(new FileInputStream("src/com/sibvisions/rad/package.html"), null);
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JInternalFrame fBrowser = new JInternalFrame("Own internal Browser", true, true, true, true);
		rootPane.add(fBrowser);
		fBrowser.getContentPane().setLayout(new BorderLayout());
//		f1.getContentPane().add(new JTextArea());
		fBrowser.getContentPane().add(scrollBrowser, BorderLayout.CENTER);
		fBrowser.getContentPane().add(buttonSource, BorderLayout.SOUTH);
//		f1.pack();
		fBrowser.setBounds(20,20,800,600);
		fBrowser.setVisible(true);


		JButton testButton = new JButton();
		testButton.setIcon(new ImageIcon("test.png"));
		testButton.setText("Test Button");
		
		JToggleButton testToggleButton = new JToggleButton();
		testToggleButton.setIcon(new ImageIcon("test.png"));
		testToggleButton.setText("Test ToggleButton");
		
		JCheckBox testCheckBox = new JCheckBox();
		//testCheckBox.setIcon(new ImageIcon("test.png"));
		System.out.println(UIManager.getIcon("CheckBox.icon"));
		testCheckBox.setText("Test CheckBox");
		
		JRadioButton testRadioButton = new JRadioButton();
//		testRadioButton.setIcon(new ImageIcon("test.png"));
		testRadioButton.setText("Test RadioButton");
		
		JInternalFrame fJButtons = new JInternalFrame("Own internal Browser", true, true, true, true);
		rootPane.add(fJButtons);
		fJButtons.getContentPane().setLayout(new FlowLayout());
		fJButtons.getContentPane().add(testButton, null);
		fJButtons.getContentPane().add(testToggleButton, null);
		fJButtons.getContentPane().add(testCheckBox, null);
		fJButtons.getContentPane().add(testRadioButton, null);
		fJButtons.setBounds(60,60,800,600);
		fJButtons.setVisible(true);
		System.out.println(testCheckBox.getUI());

		
		
		
		
	    JTable table = new JTable(5000,20);
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    //TODO [HM] nicht in jdk 1.5 enthalten
	    //table.setAutoCreateRowSorter(true);

//	    table.setRowHeight(table.getRowHeight()*2-6);

	    TableModel model = table.getModel();
	    for (int i = 0; i < model.getRowCount(); i++) 
	    {
	    	for (int j = 0; j < model.getColumnCount(); j++) 
	    	{
//	    		model.setValueAt("<html>Row: "+i+"<br>Col: "+j+"</html>", i, j);
	    		model.setValueAt("Row: "+i+"\nCol: "+j, i, j);
	    	}
	    }
		
		
	    JScrollPane scroll = new JScrollPane();
	    scroll.setViewportView(table);
	    
		JInternalFrame f2 = new JInternalFrame("Own internal Frame 2", true, true, true, true);
		rootPane.add(f2);
		f2.getContentPane().setLayout(new GridLayout());
		f2.getContentPane().add(scroll);
		f2.pack();
		f2.setLocation(40, 40);
		f2.setVisible(true);
		
		JInternalFrame f3 = new JInternalFrame("Own internal Frame 2", true, true, true, true);
		rootPane.add(f3);
		
		JVxFormLayout layout = new JVxFormLayout();
		layout.setHorizontalAlignment(JVxFormLayout.RIGHT);
		JVxFormLayout.Anchor right = new JVxFormLayout.Anchor(layout.getRightAnchor(), -10);
		JVxFormLayout.Anchor bottom = new JVxFormLayout.Anchor(layout.getBottomAnchor(), -10);
		
		f3.getContentPane().setLayout(layout);
		f3.getContentPane().add(new JButton("Hallo1"));
//		f3.getContentPane().add(new JButton("Hallo Martin"));
//		f3.getContentPane().add(new JButton("Hallo3"));
		JButton bx = new JButton("Hallo sd4");
		f3.getContentPane().add(bx);
		
		layout.getConstraint(bx).setRightAnchor(right);
		layout.getConstraint(bx).setLeftAnchor(null);
//		layout.getConstraint(bx).setBottomAnchor(bottom);
		
				
		f3.pack();
		f3.setLocation(40, 40);
		f3.setVisible(true);
		
		System.out.println("ContentPane: " + f3.getContentPane().getSize() + "  " + f3.getContentPane().getPreferredSize());
		System.out.println("IFrame: " + f3.getSize() + "  " + f3.getPreferredSize());
		System.out.println("IFrame: " + f3.getMinimumSize() + "  " + f3.getMaximumSize());

		JInternalFrame f4 = new JInternalFrame("Syntax Highlighting", true, true, true, true);
		rootPane.add(f4);
		
		f4.setLayout(new BorderLayout());
		
		JEditorPane testEdit = new JEditorPane();
		
		SyntaxDocument document = new SyntaxDocument();
		document.setTokenMarker(new JavaTokenMarker());

		DefaultEditorKit kit = new DefaultEditorKit()
		{
			public Document createDefaultDocument() 
			{
				SyntaxDocument document = new SyntaxDocument();
				document.setTokenMarker(new JavaTokenMarker());
				return document;
		    }
		};
		
		testEdit.setEditorKitForContentType("text/java", kit);
		testEdit.setContentType("text/java");

//		testEdit.setDocument(document);
		
	    try
	    {
	    	testEdit.read(new FileInputStream("intern/research/SwingTest.java"), null);
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
	    }
	    JScrollPane pane = new JScrollPane(testEdit);
	    
	    f4.add(pane, BorderLayout.CENTER);
	    
	    f4.pack();
	    f4.setVisible(true);
		
        // Verschiedene Condition Varianten:
		// Vorläufig außer Acht gelassen, dass GROUP BY und distinct mit Suche innerhalb nicht abgedeckt ist.
		// außer Acht gelassen, Suche id in DataBook
		
		// row hat Spalten id, name, vondatum, bisdatum = 5, 'Hallo', '01.03.2008', null
		
		// ICondition hat Funktion boolean compute(DataRow pRow)
		// ICondition cond1 = new RowCondition(row, Condition.EQUAL, new String[] {"id"}, Condition.AND, new String[] {"id"},                   true);
		//                                           Default=EQUAL,  Default=null(alle),  Default=AND,   Default=null(Spaltenname der DataRow), Default null Werte nicht suchen
        // ICondition cond2 = new RowCondition(row, Condition.LIKE, new String[] {"name"});
		// ICondition filter = new Condition(cond1, Condition.OR, cond2);
		// Vorteil: eine ganze Row mit einem Schlag im Filter (Simplestversion: new RowCondition(row))
		// Nachteil: Definition der RowCondition doch behebig; 
		
		// Nett wäre: 
		// new Condition("id ?= :id or name ?like :name) and datum >= :vondatum and datum ?< :bisdatum", row);
		//                   ? heißt nur wenn Wert vorhanden, dann suchen.
		// Binding der :Parameter mit row Columns
		// Vorteil: Cool für Programmierer
		// Nachteil: auch Standardsuche muss so irgendwie definiert werden.
		//           Könnte man lösen, mit Condition.createDefault(row);
		//           Nur eine Row wird angebunden
		// 
		
		// Wenn man das Parsen ersparen will, wirds schon komplizierter:
		// ICondition cond1 = new CompareCondition("id", Condition.EQUALS, row, "id",                true)
		//                                                                      Default: Columnname, Default null Werte nicht suchen
		// ICondition cond2 = new CompareCondition("name", Condition.LIKE, row);
		// ICondition cond3 = new CompareCondition("datum", Condition.GREATEROREQUAL, row, "vondatum");
		// ICondition cond4 = new CompareCondition("datum", Condition.SMALLER, row, "bisdatum");
		// ICondition cond5 = new Condition(cond1, Condition.OR, cond2);
		// ICondition cond6 = new Condition(cond3, Condition.AND, cond4);
		// ICondition filter = new Condition(cond5, Condition.AND, cond6);
		
		// Überhaupt am schönsten:
		// ICondition cond1 = new EqualCondition("id", row, "id",                true)
		//                                                  Default: Columnname, Default null Werte nicht suchen
		// ICondition cond2 = new LikeCondition("name", row);
		// ICondition cond3 = new GreaterOrEqualCondition("datum", row, "vondatum");
		// ICondition cond4 = new SmallerCondition("datum", row, "bisdatum");
		// ICondition cond5 = new OrCondition(cond1, cond2);
		// ICondition cond6 = new AndCondition(cond3, cond4);
		// ICondition filter = new AndCondition(cond5, cond6);
		
		
		

	}

}	// SwingTest
