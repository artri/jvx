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
package javax.rad.ui.event;

import javax.rad.ui.IComponent;

/**
 * Platform and technology independent key event definition.
 * It is designed for use with AWT, Swing, SWT, JSP, JSF, ... .
 * 
 * @author Martin Handsteiner
 * @see    java.awt.event.KeyEvent
 */
public class UIKeyEvent extends UIEvent 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The first number in the range of ids used for mouse events. */
    public static final int KEY_FIRST 		= 400;

    /**
     * The "key typed" event.  This event is generated when a character is
     * entered.  In the simplest case, it is produced by a single key press.  
     * Often, however, characters are produced by series of key presses, and 
     * the mapping from key pressed events to key typed events may be 
     * many-to-one or many-to-many.  
     */
    public static final int KEY_TYPED 		= KEY_FIRST;

    /**
     * The "key pressed" event. This event is generated when a key
     * is pushed down.
     */
    public static final int KEY_PRESSED 	= KEY_FIRST + 1;

    /**
     * The "key released" event. This event is generated when a key
     * is let up.
     */
    public static final int KEY_RELEASED 	= KEY_FIRST + 2;

    /** The last number in the range of ids used for mouse events. */
    public static final int KEY_LAST      	= KEY_RELEASED;

    
    /** Virtual key codes. */
    public static final int VK_ENTER          = '\n';
    /** Virtual key codes. */
    public static final int VK_BACK_SPACE     = '\b';
    /** Virtual key codes. */
    public static final int VK_TAB            = '\t';
    /** Virtual key codes. */
    public static final int VK_CANCEL         = 0x03;
    /** Virtual key codes. */
    public static final int VK_CLEAR          = 0x0C;
    /** Virtual key codes. */
    public static final int VK_SHIFT          = 0x10;
    /** Virtual key codes. */
    public static final int VK_CONTROL        = 0x11;
    /** Virtual key codes. */
    public static final int VK_ALT            = 0x12;
    /** Virtual key codes. */
    public static final int VK_PAUSE          = 0x13;
    /** Virtual key codes. */
    public static final int VK_CAPS_LOCK      = 0x14;
    /** Virtual key codes. */
    public static final int VK_ESCAPE         = 0x1B;
    /** Virtual key codes. */
    public static final int VK_SPACE          = 0x20;
    /** Virtual key codes. */
    public static final int VK_PAGE_UP        = 0x21;
    /** Virtual key codes. */
    public static final int VK_PAGE_DOWN      = 0x22;
    /** Virtual key codes. */
    public static final int VK_END            = 0x23;
    /** Virtual key codes. */
    public static final int VK_HOME           = 0x24;

    /** Constant for the non-numpad <b>left</b> arrow key. */
    public static final int VK_LEFT           = 0x25;
    /** Constant for the non-numpad <b>up</b> arrow key. */
    public static final int VK_UP             = 0x26;
    /** Constant for the non-numpad <b>right</b> arrow key. */
    public static final int VK_RIGHT          = 0x27;
    /** Constant for the non-numpad <b>down</b> arrow key. */
    public static final int VK_DOWN           = 0x28;
    /** Constant for the comma key, ",". */
    public static final int VK_COMMA          = 0x2C;
    /** Constant for the minus key, "-". */
    public static final int VK_MINUS          = 0x2D;
    /** Constant for the period key, ".". */
    public static final int VK_PERIOD         = 0x2E;
    /** Constant for the forward slash key, "/". */
    public static final int VK_SLASH          = 0x2F;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_0              = 0x30;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_1              = 0x31;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_2              = 0x32;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_3              = 0x33;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_4              = 0x34;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_5              = 0x35;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_6              = 0x36;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_7              = 0x37;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_8              = 0x38;
    /** VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39). */
    public static final int VK_9              = 0x39;
    /** Constant for the semicolon key, ";". */
    public static final int VK_SEMICOLON      = 0x3B;
    /** Constant for the equals key, "=". */
    public static final int VK_EQUALS         = 0x3D;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_A              = 0x41;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_B              = 0x42;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_C              = 0x43;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_D              = 0x44;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_E              = 0x45;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_F              = 0x46;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_G              = 0x47;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_H              = 0x48;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_I              = 0x49;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_J              = 0x4A;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_K              = 0x4B;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_L              = 0x4C;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_M              = 0x4D;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_N              = 0x4E;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_O              = 0x4F;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_P              = 0x50;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_Q              = 0x51;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_R              = 0x52;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_S              = 0x53;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_T              = 0x54;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_U              = 0x55;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_V              = 0x56;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_W              = 0x57;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_X              = 0x58;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_Y              = 0x59;
    /** VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A). */
    public static final int VK_Z              = 0x5A;
    /** Constant for the open bracket key, "[". */
    public static final int VK_OPEN_BRACKET   = 0x5B;
    /** Constant for the back slash key, "\". */
    public static final int VK_BACK_SLASH     = 0x5C;
    /** Constant for the close bracket key, "]". */
    public static final int VK_CLOSE_BRACKET  = 0x5D;

    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD0        = 0x60;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD1        = 0x61;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD2        = 0x62;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD3        = 0x63;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD4        = 0x64;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD5        = 0x65;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD6        = 0x66;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD7        = 0x67;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD8        = 0x68;
    /** Constant for the Numpad key. */
    public static final int VK_NUMPAD9        = 0x69;
    /** Constant for the Numpad key. */
    public static final int VK_MULTIPLY       = 0x6A;
    /** Constant for the Numpad key. */
    public static final int VK_ADD            = 0x6B;
    /** Constant for the Numpad key. */
    public static final int VK_SEPARATOR      = 0x6C;
    /** Constant for the Numpad key. */
    public static final int VK_SUBTRACT       = 0x6D;
    /** Constant for the Numpad key. */
    public static final int VK_DECIMAL        = 0x6E;
    /** Constant for the Numpad key. */
    public static final int VK_DIVIDE         = 0x6F;
    /** Constant for the Numpad key. */
    public static final int VK_DELETE         = 0x7F; /* ASCII DEL */
    /** Constant for the Numpad key. */
    public static final int VK_NUM_LOCK       = 0x90;
    /** Constant for the Numpad key. */
    public static final int VK_SCROLL_LOCK    = 0x91;

    /** Constant for the function key. */
    public static final int VK_F1             = 0x70;
    /** Constant for the function key. */
    public static final int VK_F2             = 0x71;
    /** Constant for the function key. */
    public static final int VK_F3             = 0x72;
    /** Constant for the function key. */
    public static final int VK_F4             = 0x73;
    /** Constant for the function key. */
    public static final int VK_F5             = 0x74;
    /** Constant for the function key. */
    public static final int VK_F6             = 0x75;
    /** Constant for the function key. */
    public static final int VK_F7             = 0x76;
    /** Constant for the function key. */
    public static final int VK_F8             = 0x77;
    /** Constant for the function key. */
    public static final int VK_F9             = 0x78;
    /** Constant for the function key. */
    public static final int VK_F10            = 0x79;
    /** Constant for the function key. */
    public static final int VK_F11            = 0x7A;
    /** Constant for the function key. */
    public static final int VK_F12            = 0x7B;

    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F13            = 0xF000;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F14            = 0xF001;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F15            = 0xF002;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F16            = 0xF003;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F17            = 0xF004;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F18            = 0xF005;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F19            = 0xF006;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F20            = 0xF007;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F21            = 0xF008;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F22            = 0xF009;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F23            = 0xF00A;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_F24            = 0xF00B;
 
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_PRINTSCREEN    = 0x9A;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_INSERT         = 0x9B;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_HELP           = 0x9C;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_META           = 0x9D;

    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_BACK_QUOTE     = 0xC0;
    /** F13 - F24 are used on IBM 3270 keyboard; use random range for constants. */
    public static final int VK_QUOTE          = 0xDE;

    /** Constant for the numeric keypad <b>up</b> arrow key. */
    public static final int VK_KP_UP          = 0xE0;
    /** Constant for the numeric keypad <b>down</b> arrow key. */
    public static final int VK_KP_DOWN        = 0xE1;
    /** Constant for the numeric keypad <b>left</b> arrow key. */
    public static final int VK_KP_LEFT        = 0xE2;
    /** Constant for the numeric keypad <b>right</b> arrow key. */
    public static final int VK_KP_RIGHT       = 0xE3;
    
    /** For European keyboards. */
    public static final int VK_DEAD_GRAVE               = 0x80;
    /** For European keyboards. */
    public static final int VK_DEAD_ACUTE               = 0x81;
    /** For European keyboards. */
    public static final int VK_DEAD_CIRCUMFLEX          = 0x82;
    /** For European keyboards. */
    public static final int VK_DEAD_TILDE               = 0x83;
    /** For European keyboards. */
    public static final int VK_DEAD_MACRON              = 0x84;
    /** For European keyboards. */
    public static final int VK_DEAD_BREVE               = 0x85;
    /** For European keyboards. */
    public static final int VK_DEAD_ABOVEDOT            = 0x86;
    /** For European keyboards. */
    public static final int VK_DEAD_DIAERESIS           = 0x87;
    /** For European keyboards. */
    public static final int VK_DEAD_ABOVERING           = 0x88;
    /** For European keyboards. */
    public static final int VK_DEAD_DOUBLEACUTE         = 0x89;
    /** For European keyboards. */
    public static final int VK_DEAD_CARON               = 0x8a;
    /** For European keyboards. */
    public static final int VK_DEAD_CEDILLA             = 0x8b;
    /** For European keyboards. */
    public static final int VK_DEAD_OGONEK              = 0x8c;
    /** For European keyboards. */
    public static final int VK_DEAD_IOTA                = 0x8d;
    /** For European keyboards. */
    public static final int VK_DEAD_VOICED_SOUND        = 0x8e;
    /** For European keyboards. */
    public static final int VK_DEAD_SEMIVOICED_SOUND    = 0x8f;

    /** For European keyboards. */
    public static final int VK_AMPERSAND                = 0x96;
    /** For European keyboards. */
    public static final int VK_ASTERISK                 = 0x97;
    /** For European keyboards. */
    public static final int VK_QUOTEDBL                 = 0x98;
    /** For European keyboards. */
    public static final int VK_LESS                     = 0x99;

    /** For European keyboards. */
    public static final int VK_GREATER                  = 0xa0;
    /** For European keyboards. */
    public static final int VK_BRACELEFT                = 0xa1;
    /** For European keyboards. */
    public static final int VK_BRACERIGHT               = 0xa2;

    /** Constant for the "@" key. */
    public static final int VK_AT                       = 0x0200;
    /** Constant for the ":" key. */
    public static final int VK_COLON                    = 0x0201;
    /** Constant for the "^" key. */
    public static final int VK_CIRCUMFLEX               = 0x0202;
    /** Constant for the "$" key. */
    public static final int VK_DOLLAR                   = 0x0203;
    /** Constant for the Euro currency sign key. */
    public static final int VK_EURO_SIGN                = 0x0204;
    /** Constant for the "!" key. */
    public static final int VK_EXCLAMATION_MARK         = 0x0205;
    /** Constant for the inverted exclamation mark key. */
    public static final int VK_INVERTED_EXCLAMATION_MARK = 0x0206;
    /** Constant for the "(" key. */
    public static final int VK_LEFT_PARENTHESIS         = 0x0207;
    /** Constant for the "#" key. */
    public static final int VK_NUMBER_SIGN              = 0x0208;
    /** Constant for the "+" key. */
    public static final int VK_PLUS                     = 0x0209;
    /** Constant for the ")" key. */
    public static final int VK_RIGHT_PARENTHESIS        = 0x020A;
    /** Constant for the "_" key. */
    public static final int VK_UNDERSCORE               = 0x020B;
    
    /**
     * Constant for the Microsoft Windows "Windows" key.
     * It is used for both the left and right version of the key.  
     */
    public static final int VK_WINDOWS                  = 0x020C;
    /** Constant for the Microsoft Windows Context Menu key. */
    public static final int VK_CONTEXT_MENU             = 0x020D;
 
    /* for input method support on Asian Keyboards */

    /** not clear what this means - listed in Microsoft Windows API. */
    public static final int VK_FINAL                    = 0x0018;
    /** 
     * Constant for the Convert function key. 
     * Japanese PC 106 keyboard, Japanese Solaris keyboard: henkan
     */
    public static final int VK_CONVERT                  = 0x001C;
    /** 
     * Constant for the Don't Convert function key. 
     * Japanese PC 106 keyboard: muhenkan
     */
    public static final int VK_NONCONVERT               = 0x001D;
    /** 
     * Constant for the Accept or Commit function key. 
     * Japanese Solaris keyboard: kakutei
     */
    public static final int VK_ACCEPT                   = 0x001E;
    /** not clear what this means - listed in Microsoft Windows API. */
    public static final int VK_MODECHANGE               = 0x001F;
    /**
     * replaced by VK_KANA_LOCK for Microsoft Windows and Solaris; 
     * might still be used on other platforms. 
     */
    public static final int VK_KANA                     = 0x0015;
    /**
     * replaced by VK_INPUT_METHOD_ON_OFF for Microsoft Windows and Solaris;
     * might still be used for other platforms.
     */
    public static final int VK_KANJI                    = 0x0019;

    /** 
     * Constant for the Alphanumeric function key. 
     * Japanese PC 106 keyboard: eisuu
     */
    public static final int VK_ALPHANUMERIC             = 0x00F0;
    /** 
     * Constant for the Katakana function key. 
     * Japanese PC 106 keyboard: katakana
     */
    public static final int VK_KATAKANA                 = 0x00F1;
    /** 
     * Constant for the Hiragana function key. 
     * Japanese PC 106 keyboard: hiragana
     */
    public static final int VK_HIRAGANA                 = 0x00F2;
    /** 
     * Constant for the Full-Width Characters function key. 
     * Japanese PC 106 keyboard: zenkaku
     */
    public static final int VK_FULL_WIDTH               = 0x00F3;
    /** 
     * Constant for the Half-Width Characters function key. 
     * Japanese PC 106 keyboard: hankaku
     */
    public static final int VK_HALF_WIDTH               = 0x00F4;
    /** 
     * Constant for the Roman Characters function key. 
     * Japanese PC 106 keyboard: roumaji
     */
    public static final int VK_ROMAN_CHARACTERS         = 0x00F5;
    /** 
     * Constant for the All Candidates function key. 
     * Japanese PC 106 keyboard - VK_CONVERT + ALT: zenkouho
     */
    public static final int VK_ALL_CANDIDATES           = 0x0100;
    /** 
     * Constant for the Previous Candidate function key.
     * Japanese PC 106 keyboard - VK_CONVERT + SHIFT: maekouho
     */
    public static final int VK_PREVIOUS_CANDIDATE       = 0x0101;
    /** 
     * Constant for the Code Input function key. 
     * Japanese PC 106 keyboard - VK_ALPHANUMERIC + ALT: kanji bangou
     */
    public static final int VK_CODE_INPUT               = 0x0102;
    /**
     * Constant for the Japanese-Katakana function key.
     * This key switches to a Japanese input method and selects its Katakana input mode.
     * Japanese Macintosh keyboard - VK_JAPANESE_HIRAGANA + SHIFT
     */
    public static final int VK_JAPANESE_KATAKANA        = 0x0103;
    /**
     * Constant for the Japanese-Hiragana function key.
     * This key switches to a Japanese input method and selects its Hiragana input mode.
     * Japanese Macintosh keyboard
     */
    public static final int VK_JAPANESE_HIRAGANA        = 0x0104;
    /**
     * Constant for the Japanese-Roman function key.
     * This key switches to a Japanese input method and selects its Roman-Direct input mode.
     * Japanese Macintosh keyboard
     */
    public static final int VK_JAPANESE_ROMAN           = 0x0105;
    /**
     * Constant for the locking Kana function key. This key locks the keyboard into a Kana layout.
     * Japanese PC 106 keyboard with special Windows driver - eisuu + Control; Japanese Solaris keyboard: kana
     */
    public static final int VK_KANA_LOCK                = 0x0106;
    /** 
     * Constant for the input method on/off key. 
     * Japanese PC 106 keyboard: kanji. Japanese Solaris keyboard: nihongo 
     */
    public static final int VK_INPUT_METHOD_ON_OFF      = 0x0107;

    /** for Sun keyboards. */
    public static final int VK_CUT                      = 0xFFD1;
    /** for Sun keyboards. */
    public static final int VK_COPY                     = 0xFFCD;
    /** for Sun keyboards. */
    public static final int VK_PASTE                    = 0xFFCF;
    /** for Sun keyboards. */
    public static final int VK_UNDO                     = 0xFFCB;
    /** for Sun keyboards. */
    public static final int VK_AGAIN                    = 0xFFC9;
    /** for Sun keyboards. */
    public static final int VK_FIND                     = 0xFFD0;
    /** for Sun keyboards. */
    public static final int VK_PROPS                    = 0xFFCA;
    /** for Sun keyboards. */
    public static final int VK_STOP                     = 0xFFC8;
    
    /** Constant for the Compose function key. */
    public static final int VK_COMPOSE                  = 0xFF20;
    /** Constant for the AltGraph function key. */
    public static final int VK_ALT_GRAPH                = 0xFF7E;

    /** Constant for the Begin key. */
    public static final int VK_BEGIN                    = 0xFF58;

    /**
     * This value is used to indicate that the keyCode is unknown.
     * KEY_TYPED events do not have a keyCode value; this value 
     * is used instead.  
     */
    public static final int VK_UNDEFINED      = 0x0;

    /**
     * KEY_PRESSED and KEY_RELEASED events which do not map to a
     * valid Unicode character use this for the keyChar value.
     */
    public static final char CHAR_UNDEFINED   = 0xFFFF;
    
    /**
     * The unique value assigned to each of the keys on the
     * keyboard.  There is a common set of key codes that
     * can be fired by most keyboards.
     * The symbolic name for a key code should be used rather
     * than the code value itself.
     */
    private int  keyCode;

    /**
     * <code>keyChar</code> is a valid unicode character
     * that is fired by a key or a key combination on
     * a keyboard.
     */
    private char keyChar;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UIKeyEvent</code>.
	 * 
	 * @param pSource the Source of this UIKeyEvent.
	 * @param pId     the Id of this UIKeyEvent.
     * @param pWhen   the time the event occurred
     * @param pModifiers represents the modifier keys and mouse buttons down while the event occurred
     * @param pKeyCode the integer code for an actual key, or VK_UNDEFINED 
     *                 (for a key-typed event)
     * @param pKeyChar the Unicode character generated by this event, or 
     *                 CHAR_UNDEFINED (for key-pressed and key-released
     *                 events which do not map to a valid Unicode character)
	 */
	public UIKeyEvent(IComponent pSource, int pId, long pWhen, int pModifiers, int pKeyCode, char pKeyChar)
	{
		super(pSource, pId, pWhen, pModifiers);
		
		keyCode = pKeyCode;
		keyChar = pKeyChar;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkId(int pId)
	{
		if (pId < KEY_FIRST || pId > KEY_LAST)
		{
			super.checkId(pId);
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the integer keyCode associated with the key in this event.
     * 
     * @return the integer code for an actual key on the keyboard. 
     *         (For <code>KEY_TYPED</code> events, the keyCode is 
     *         <code>VK_UNDEFINED</code>.)
     */
    public int getKeyCode() 
    {
        return keyCode;
    }

    /**
     * Returns the character associated with the key in this event.
     * For example, the <code>KEY_TYPED</code> event for shift + "a" 
     * returns the value for "A".
     * <p>
     * <code>KEY_PRESSED</code> and <code>KEY_RELEASED</code> events 
     * are not intended for reporting of character input.  Therefore, 
     * the values returned by this method are guaranteed to be 
     * meaningful only for <code>KEY_TYPED</code> events.  
     *
     * @return the Unicode character defined for this key event.
     *         If no valid Unicode character exists for this key event, 
     *         <code>CHAR_UNDEFINED</code> is returned.
     */
    public char getKeyChar() 
    {
        return keyChar;
    }
    
}	// UIKeyEvent
