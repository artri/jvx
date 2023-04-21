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
 * 08.04.2009 - [JR] - creation
 * 28.04.2009 - [JR] - merge method removed
 * 29.07.2009 - [JR] - formatLabel copied from ColumnDefinition
 * 06.10.2009 - [JR] - getAlphaNumeric implemented
 * 26.11.2009 - [JR] - toString(Object) implemented
 * 13.02.2010 - [JR] - formatInitCap implemented
 * 14.02.2010 - [JR] - formatInitCap with remove spaces implemented
 * 26.03.2010 - [JR] - #92: removeQuotes implemented
 * 09.10.2010 - [JR] - #114: added removeCharacters
 * 29.11.2010 - [JR] - replace implemented
 * 04.03.2011 - [JR] - getCaseSensitiveType and getCharacterType implemented
 * 10.03.2011 - [JR] - getAlphaNumeric renamed to getText and used TextType
 *                   - added LettersDigitsWhitespace and LettersDigitsSpace
 * 07.06.2011 - [JR] - #383
 *                     * removed formatHumanReadable
 *                     * removed keep format parameter from formatInitCap
 *                     * introduced convertToMethodName, convertToMemberName
 * 21.06.2011 - [JR] - convertToName implemented  
 * 06.07.2011 - [JR] - #416: formatInitCap trim implemented      
 * 05.08.2011 - [JR] - � mapping was A not AE [BUGFIX]     
 * 10.08.2011 - [JR] - formatInitCap: '_' replacement is included in whitespaces
 * 30.10.2011 - [JR] - convertMethodNameToText implemented
 * 20.11.2011 - [JR] - convertMethodNameToText with replace parameter implemented 
 * 26.01.2013 - [JR] - convertMemberNameToText implemented
 * 21.02.2013 - [JR] - isEmpty implemented    
 * 12.04.2013 - [RH] - padLeft/padRight methods implemented
 * 18.04.2013 - [JR] - containsWhitespace implemented
 * 09.05.2013 - [JR] - countCharacter implemented
 * 14.05.2013 - [JR] - separate implemented
 * 18.01.2014 - [JR] - convertMethodNameToText, convertMemberNameToText now replaces '_' with ' '
 * 13.07.2014 - [JR] - firstCharLower implemented
 * 26.10.2014 - [JR] - getFirstWord implemented
 * 29.11.2014 - [JR] - toString with max array length support
 * 17.06.2015 - [JR] - deepToString now with IdentityHashMap instead of HashSet, to avoid
 *                     recursion with hashCode if same object were found 
 * 30.07.2015 - [JR] - getText with char... support   
 * 30.09.2015 - [JR] - parseColor implemented           
 * 01.10.2015 - [JR] - color constants added   
 * 12.01.2018 - [JR] - #1874: checked trailing special character in convertNameToText    
 */
package com.sibvisions.util.type;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sibvisions.util.ArrayUtil;

/**
 * The {@link StringUtil} contains {@link String} dependent utility methods.
 * 
 * @author Ren� Jahn
 */
public final class StringUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Map} which maps characters to their replacements. */
	private static HashMap<Character, String> characterReplacements = new HashMap<Character, String>();
	
	/** The {@link Map} which maps names to colors. */
	private static HashMap<String, int[]> namesToColors = new HashMap<String, int[]>();
	
	/**
	 * The {@link Map} which maps characters to their replacements (reversed
	 * mode).
	 */
	private static HashMap<String, Character> reverseCharacterReplacements = new HashMap<String, Character>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initializes the replacement mapping for name conversions.
	 */
	static
	{
		reverseCharacterReplacements.put("ue", Character.valueOf((char)0x00FC));
		reverseCharacterReplacements.put("Ue", Character.valueOf((char)0x00DC));
		reverseCharacterReplacements.put("UE", Character.valueOf((char)0x00DC));
		reverseCharacterReplacements.put("ae", Character.valueOf((char)0x00E4));
		reverseCharacterReplacements.put("Ae", Character.valueOf((char)0x00C4));
		reverseCharacterReplacements.put("AE", Character.valueOf((char)0x00C4));
		reverseCharacterReplacements.put("oe", Character.valueOf((char)0x00F6));
		reverseCharacterReplacements.put("Oe", Character.valueOf((char)0x00D6));
		reverseCharacterReplacements.put("OE", Character.valueOf((char)0x00D6));
		reverseCharacterReplacements.put("ss", Character.valueOf((char)0x00DF));
		
		// http://jrgraphix.net/r/Unicode/
		
		// Latin-1 Supplement
		characterReplacements.put(Character.valueOf((char)0x00A2), "c");
		characterReplacements.put(Character.valueOf((char)0x00A5), "Y");
		characterReplacements.put(Character.valueOf((char)0x00A9), "c");
		characterReplacements.put(Character.valueOf((char)0x00AA), "a");
		characterReplacements.put(Character.valueOf((char)0x00AE), "R");
		characterReplacements.put(Character.valueOf((char)0x00C0), "A");
		characterReplacements.put(Character.valueOf((char)0x00C1), "A");
		characterReplacements.put(Character.valueOf((char)0x00C2), "A");
		characterReplacements.put(Character.valueOf((char)0x00C3), "A");
		characterReplacements.put(Character.valueOf((char)0x00C4), "AE");
		characterReplacements.put(Character.valueOf((char)0x00C5), "A");
		characterReplacements.put(Character.valueOf((char)0x00C6), "AE");
		characterReplacements.put(Character.valueOf((char)0x00C7), "C");
		characterReplacements.put(Character.valueOf((char)0x00C8), "E");
		characterReplacements.put(Character.valueOf((char)0x00C9), "E");
		characterReplacements.put(Character.valueOf((char)0x00CA), "E");
		characterReplacements.put(Character.valueOf((char)0x00CB), "E");
		characterReplacements.put(Character.valueOf((char)0x00CC), "I");
		characterReplacements.put(Character.valueOf((char)0x00CD), "I");
		characterReplacements.put(Character.valueOf((char)0x00CE), "I");
		characterReplacements.put(Character.valueOf((char)0x00CF), "I");
		characterReplacements.put(Character.valueOf((char)0x00D0), "D");
		characterReplacements.put(Character.valueOf((char)0x00D1), "N");
		characterReplacements.put(Character.valueOf((char)0x00D2), "O");
		characterReplacements.put(Character.valueOf((char)0x00D3), "O");
		characterReplacements.put(Character.valueOf((char)0x00D4), "O");
		characterReplacements.put(Character.valueOf((char)0x00D5), "O");
		characterReplacements.put(Character.valueOf((char)0x00D6), "OE");
		characterReplacements.put(Character.valueOf((char)0x00D7), "x");
		characterReplacements.put(Character.valueOf((char)0x00D8), "O");
		characterReplacements.put(Character.valueOf((char)0x00D9), "U");
		characterReplacements.put(Character.valueOf((char)0x00DA), "U");
		characterReplacements.put(Character.valueOf((char)0x00DB), "U");
		characterReplacements.put(Character.valueOf((char)0x00DC), "UE");
		characterReplacements.put(Character.valueOf((char)0x00DD), "Y");
		characterReplacements.put(Character.valueOf((char)0x00DF), "ss");
		characterReplacements.put(Character.valueOf((char)0x00E0), "a");
		characterReplacements.put(Character.valueOf((char)0x00E1), "a");
		characterReplacements.put(Character.valueOf((char)0x00E2), "a");
		characterReplacements.put(Character.valueOf((char)0x00E3), "a");
		characterReplacements.put(Character.valueOf((char)0x00E4), "ae");
		characterReplacements.put(Character.valueOf((char)0x00E5), "a");
		characterReplacements.put(Character.valueOf((char)0x00E6), "ae");
		characterReplacements.put(Character.valueOf((char)0x00E7), "c");
		characterReplacements.put(Character.valueOf((char)0x00E8), "e");
		characterReplacements.put(Character.valueOf((char)0x00E9), "e");
		characterReplacements.put(Character.valueOf((char)0x00EA), "e");
		characterReplacements.put(Character.valueOf((char)0x00EB), "ae");
		characterReplacements.put(Character.valueOf((char)0x00EC), "i");
		characterReplacements.put(Character.valueOf((char)0x00ED), "i");
		characterReplacements.put(Character.valueOf((char)0x00EE), "i");
		characterReplacements.put(Character.valueOf((char)0x00EF), "i");
		characterReplacements.put(Character.valueOf((char)0x00F0), "o");
		characterReplacements.put(Character.valueOf((char)0x00F1), "n");
		characterReplacements.put(Character.valueOf((char)0x00F2), "o");
		characterReplacements.put(Character.valueOf((char)0x00F3), "o");
		characterReplacements.put(Character.valueOf((char)0x00F4), "o");
		characterReplacements.put(Character.valueOf((char)0x00F5), "o");
		characterReplacements.put(Character.valueOf((char)0x00F6), "oe");
		characterReplacements.put(Character.valueOf((char)0x00F8), "o");
		characterReplacements.put(Character.valueOf((char)0x00F9), "u");
		characterReplacements.put(Character.valueOf((char)0x00FA), "u");
		characterReplacements.put(Character.valueOf((char)0x00FB), "u");
		characterReplacements.put(Character.valueOf((char)0x00FC), "ue");
		characterReplacements.put(Character.valueOf((char)0x00FD), "y");
		characterReplacements.put(Character.valueOf((char)0x00FE), "b");
		characterReplacements.put(Character.valueOf((char)0x00FF), "y");
		
		// Latin Extended-A
		characterReplacements.put(Character.valueOf((char)0x0100), "A");
		characterReplacements.put(Character.valueOf((char)0x0101), "a");
		characterReplacements.put(Character.valueOf((char)0x0102), "A");
		characterReplacements.put(Character.valueOf((char)0x0103), "a");
		characterReplacements.put(Character.valueOf((char)0x0104), "A");
		characterReplacements.put(Character.valueOf((char)0x0105), "a");
		characterReplacements.put(Character.valueOf((char)0x0106), "C");
		characterReplacements.put(Character.valueOf((char)0x0107), "c");
		characterReplacements.put(Character.valueOf((char)0x0108), "C");
		characterReplacements.put(Character.valueOf((char)0x0109), "c");
		characterReplacements.put(Character.valueOf((char)0x010A), "C");
		characterReplacements.put(Character.valueOf((char)0x010B), "c");
		characterReplacements.put(Character.valueOf((char)0x010C), "C");
		characterReplacements.put(Character.valueOf((char)0x010D), "c");
		characterReplacements.put(Character.valueOf((char)0x010E), "D");
		characterReplacements.put(Character.valueOf((char)0x010F), "d");
		characterReplacements.put(Character.valueOf((char)0x0110), "D");
		characterReplacements.put(Character.valueOf((char)0x0111), "d");
		characterReplacements.put(Character.valueOf((char)0x0112), "E");
		characterReplacements.put(Character.valueOf((char)0x0113), "e");
		characterReplacements.put(Character.valueOf((char)0x0114), "E");
		characterReplacements.put(Character.valueOf((char)0x0115), "e");
		characterReplacements.put(Character.valueOf((char)0x0116), "E");
		characterReplacements.put(Character.valueOf((char)0x0117), "e");
		characterReplacements.put(Character.valueOf((char)0x0118), "E");
		characterReplacements.put(Character.valueOf((char)0x0119), "e");
		characterReplacements.put(Character.valueOf((char)0x011A), "E");
		characterReplacements.put(Character.valueOf((char)0x011B), "e");
		characterReplacements.put(Character.valueOf((char)0x011C), "G");
		characterReplacements.put(Character.valueOf((char)0x011D), "g");
		characterReplacements.put(Character.valueOf((char)0x011E), "G");
		characterReplacements.put(Character.valueOf((char)0x011F), "g");
		characterReplacements.put(Character.valueOf((char)0x0120), "G");
		characterReplacements.put(Character.valueOf((char)0x0121), "g");
		characterReplacements.put(Character.valueOf((char)0x0122), "G");
		characterReplacements.put(Character.valueOf((char)0x0123), "g");
		characterReplacements.put(Character.valueOf((char)0x0124), "H");
		characterReplacements.put(Character.valueOf((char)0x0125), "h");
		characterReplacements.put(Character.valueOf((char)0x0126), "H");
		characterReplacements.put(Character.valueOf((char)0x0127), "h");
		characterReplacements.put(Character.valueOf((char)0x0128), "I");
		characterReplacements.put(Character.valueOf((char)0x0129), "i");
		characterReplacements.put(Character.valueOf((char)0x012A), "I");
		characterReplacements.put(Character.valueOf((char)0x012B), "i");
		characterReplacements.put(Character.valueOf((char)0x012C), "I");
		characterReplacements.put(Character.valueOf((char)0x012D), "i");
		characterReplacements.put(Character.valueOf((char)0x012E), "I");
		characterReplacements.put(Character.valueOf((char)0x012F), "i");
		characterReplacements.put(Character.valueOf((char)0x0130), "i");
		characterReplacements.put(Character.valueOf((char)0x0131), "I");
		characterReplacements.put(Character.valueOf((char)0x0132), "IJ");
		characterReplacements.put(Character.valueOf((char)0x0133), "ij");
		characterReplacements.put(Character.valueOf((char)0x0134), "J");
		characterReplacements.put(Character.valueOf((char)0x0135), "j");
		characterReplacements.put(Character.valueOf((char)0x0136), "K");
		characterReplacements.put(Character.valueOf((char)0x0137), "k");
		characterReplacements.put(Character.valueOf((char)0x0138), "K");
		characterReplacements.put(Character.valueOf((char)0x0139), "L");
		characterReplacements.put(Character.valueOf((char)0x013A), "I");
		characterReplacements.put(Character.valueOf((char)0x013B), "L");
		characterReplacements.put(Character.valueOf((char)0x013C), "I");
		characterReplacements.put(Character.valueOf((char)0x013D), "L");
		characterReplacements.put(Character.valueOf((char)0x013E), "I");
		characterReplacements.put(Character.valueOf((char)0x013F), "L");
		characterReplacements.put(Character.valueOf((char)0x0140), "I");
		characterReplacements.put(Character.valueOf((char)0x0141), "L");
		characterReplacements.put(Character.valueOf((char)0x0142), "I");
		characterReplacements.put(Character.valueOf((char)0x0143), "N");
		characterReplacements.put(Character.valueOf((char)0x0144), "n");
		characterReplacements.put(Character.valueOf((char)0x0145), "N");
		characterReplacements.put(Character.valueOf((char)0x0146), "n");
		characterReplacements.put(Character.valueOf((char)0x0147), "N");
		characterReplacements.put(Character.valueOf((char)0x0148), "n");
		characterReplacements.put(Character.valueOf((char)0x0149), "n");
		characterReplacements.put(Character.valueOf((char)0x014A), "n");
		characterReplacements.put(Character.valueOf((char)0x014B), "n");
		characterReplacements.put(Character.valueOf((char)0x014C), "O");
		characterReplacements.put(Character.valueOf((char)0x014D), "o");
		characterReplacements.put(Character.valueOf((char)0x014E), "O");
		characterReplacements.put(Character.valueOf((char)0x014F), "o");
		characterReplacements.put(Character.valueOf((char)0x0150), "OE");
		characterReplacements.put(Character.valueOf((char)0x0151), "oe");
		characterReplacements.put(Character.valueOf((char)0x0152), "OE");
		characterReplacements.put(Character.valueOf((char)0x0153), "oe");
		characterReplacements.put(Character.valueOf((char)0x0154), "R");
		characterReplacements.put(Character.valueOf((char)0x0155), "r");
		characterReplacements.put(Character.valueOf((char)0x0156), "R");
		characterReplacements.put(Character.valueOf((char)0x0157), "r");
		characterReplacements.put(Character.valueOf((char)0x0158), "R");
		characterReplacements.put(Character.valueOf((char)0x0159), "r");
		characterReplacements.put(Character.valueOf((char)0x015A), "S");
		characterReplacements.put(Character.valueOf((char)0x015B), "s");
		characterReplacements.put(Character.valueOf((char)0x015C), "S");
		characterReplacements.put(Character.valueOf((char)0x015D), "s");
		characterReplacements.put(Character.valueOf((char)0x015E), "S");
		characterReplacements.put(Character.valueOf((char)0x015F), "s");
		characterReplacements.put(Character.valueOf((char)0x0160), "S");
		characterReplacements.put(Character.valueOf((char)0x0161), "s");
		characterReplacements.put(Character.valueOf((char)0x0162), "T");
		characterReplacements.put(Character.valueOf((char)0x0163), "t");
		characterReplacements.put(Character.valueOf((char)0x0164), "T");
		characterReplacements.put(Character.valueOf((char)0x0165), "t");
		characterReplacements.put(Character.valueOf((char)0x0166), "F");
		characterReplacements.put(Character.valueOf((char)0x0167), "f");
		characterReplacements.put(Character.valueOf((char)0x0168), "U");
		characterReplacements.put(Character.valueOf((char)0x0169), "u");
		characterReplacements.put(Character.valueOf((char)0x016A), "U");
		characterReplacements.put(Character.valueOf((char)0x016B), "u");
		characterReplacements.put(Character.valueOf((char)0x016C), "U");
		characterReplacements.put(Character.valueOf((char)0x016D), "u");
		characterReplacements.put(Character.valueOf((char)0x016E), "U");
		characterReplacements.put(Character.valueOf((char)0x016F), "u");
		characterReplacements.put(Character.valueOf((char)0x0170), "UE");
		characterReplacements.put(Character.valueOf((char)0x0171), "ue");
		characterReplacements.put(Character.valueOf((char)0x0172), "U");
		characterReplacements.put(Character.valueOf((char)0x0173), "u");
		characterReplacements.put(Character.valueOf((char)0x0174), "W");
		characterReplacements.put(Character.valueOf((char)0x0175), "w");
		characterReplacements.put(Character.valueOf((char)0x0176), "Y");
		characterReplacements.put(Character.valueOf((char)0x0177), "y");
		characterReplacements.put(Character.valueOf((char)0x0178), "Y");
		characterReplacements.put(Character.valueOf((char)0x0179), "Z");
		characterReplacements.put(Character.valueOf((char)0x017A), "z");
		characterReplacements.put(Character.valueOf((char)0x017B), "Z");
		characterReplacements.put(Character.valueOf((char)0x017C), "z");
		characterReplacements.put(Character.valueOf((char)0x017D), "Z");
		characterReplacements.put(Character.valueOf((char)0x017E), "z");
		
		// Latin Extended-B
		characterReplacements.put(Character.valueOf((char)0x0180), "b");
		characterReplacements.put(Character.valueOf((char)0x0181), "B");
		characterReplacements.put(Character.valueOf((char)0x0182), "b");
		characterReplacements.put(Character.valueOf((char)0x0183), "b");
		characterReplacements.put(Character.valueOf((char)0x0184), "b");
		characterReplacements.put(Character.valueOf((char)0x0185), "b");
		characterReplacements.put(Character.valueOf((char)0x0186), "C");
		characterReplacements.put(Character.valueOf((char)0x0187), "C");
		characterReplacements.put(Character.valueOf((char)0x0188), "c");
		characterReplacements.put(Character.valueOf((char)0x0189), "D");
		characterReplacements.put(Character.valueOf((char)0x018A), "D");
		characterReplacements.put(Character.valueOf((char)0x018E), "E");
		characterReplacements.put(Character.valueOf((char)0x018F), "e");
		characterReplacements.put(Character.valueOf((char)0x0191), "F");
		characterReplacements.put(Character.valueOf((char)0x0192), "f");
		characterReplacements.put(Character.valueOf((char)0x0193), "G");
		characterReplacements.put(Character.valueOf((char)0x0194), "Y");
		characterReplacements.put(Character.valueOf((char)0x0196), "I");
		characterReplacements.put(Character.valueOf((char)0x0197), "I");
		characterReplacements.put(Character.valueOf((char)0x0198), "K");
		characterReplacements.put(Character.valueOf((char)0x0199), "k");
		characterReplacements.put(Character.valueOf((char)0x019A), "I");
		characterReplacements.put(Character.valueOf((char)0x019D), "N");
		characterReplacements.put(Character.valueOf((char)0x019E), "n");
		characterReplacements.put(Character.valueOf((char)0x01A0), "O");
		characterReplacements.put(Character.valueOf((char)0x01A1), "o");
		characterReplacements.put(Character.valueOf((char)0x01A4), "P");
		characterReplacements.put(Character.valueOf((char)0x01A6), "R");
		characterReplacements.put(Character.valueOf((char)0x01A7), "S");
		characterReplacements.put(Character.valueOf((char)0x01A8), "s");
		characterReplacements.put(Character.valueOf((char)0x01AA), "l");
		characterReplacements.put(Character.valueOf((char)0x01AB), "t");
		characterReplacements.put(Character.valueOf((char)0x01AC), "T");
		characterReplacements.put(Character.valueOf((char)0x01AD), "f");
		characterReplacements.put(Character.valueOf((char)0x01AE), "T");
		characterReplacements.put(Character.valueOf((char)0x01AF), "U");
		characterReplacements.put(Character.valueOf((char)0x01B0), "u");
		characterReplacements.put(Character.valueOf((char)0x01B2), "U");
		characterReplacements.put(Character.valueOf((char)0x01B3), "Y");
		characterReplacements.put(Character.valueOf((char)0x01B4), "y");
		characterReplacements.put(Character.valueOf((char)0x01B5), "Z");
		characterReplacements.put(Character.valueOf((char)0x01B6), "z");
		characterReplacements.put(Character.valueOf((char)0x01BC), "5");
		characterReplacements.put(Character.valueOf((char)0x01BF), "P");
		characterReplacements.put(Character.valueOf((char)0x01C0), "I");
		characterReplacements.put(Character.valueOf((char)0x01C4), "DZ");
		characterReplacements.put(Character.valueOf((char)0x01C5), "Dz");
		characterReplacements.put(Character.valueOf((char)0x01C6), "dz");
		characterReplacements.put(Character.valueOf((char)0x01C7), "LJ");
		characterReplacements.put(Character.valueOf((char)0x01C8), "Lj");
		characterReplacements.put(Character.valueOf((char)0x01C9), "lj");
		characterReplacements.put(Character.valueOf((char)0x01CA), "NJ");
		characterReplacements.put(Character.valueOf((char)0x01CB), "Nj");
		characterReplacements.put(Character.valueOf((char)0x01CC), "nj");
		characterReplacements.put(Character.valueOf((char)0x01CD), "A");
		characterReplacements.put(Character.valueOf((char)0x01CE), "a");
		characterReplacements.put(Character.valueOf((char)0x01CF), "I");
		characterReplacements.put(Character.valueOf((char)0x01D0), "I");
		characterReplacements.put(Character.valueOf((char)0x01D1), "O");
		characterReplacements.put(Character.valueOf((char)0x01D2), "o");
		characterReplacements.put(Character.valueOf((char)0x01D3), "U");
		characterReplacements.put(Character.valueOf((char)0x01D4), "u");
		characterReplacements.put(Character.valueOf((char)0x01D5), "U");
		characterReplacements.put(Character.valueOf((char)0x01D6), "u");
		characterReplacements.put(Character.valueOf((char)0x01D7), "U");
		characterReplacements.put(Character.valueOf((char)0x01D8), "u");
		characterReplacements.put(Character.valueOf((char)0x01D9), "U");
		characterReplacements.put(Character.valueOf((char)0x01DA), "u");
		characterReplacements.put(Character.valueOf((char)0x01DB), "U");
		characterReplacements.put(Character.valueOf((char)0x01DC), "u");
		characterReplacements.put(Character.valueOf((char)0x01DD), "e");
		characterReplacements.put(Character.valueOf((char)0x01DE), "A");
		characterReplacements.put(Character.valueOf((char)0x01DF), "a");
		characterReplacements.put(Character.valueOf((char)0x01E0), "A");
		characterReplacements.put(Character.valueOf((char)0x01E1), "a");
		characterReplacements.put(Character.valueOf((char)0x01E3), "AE");
		characterReplacements.put(Character.valueOf((char)0x01E4), "ae");
		characterReplacements.put(Character.valueOf((char)0x01E4), "G");
		characterReplacements.put(Character.valueOf((char)0x01E5), "g");
		characterReplacements.put(Character.valueOf((char)0x01E6), "G");
		characterReplacements.put(Character.valueOf((char)0x01E7), "g");
		characterReplacements.put(Character.valueOf((char)0x01E8), "K");
		characterReplacements.put(Character.valueOf((char)0x01E9), "k");
		characterReplacements.put(Character.valueOf((char)0x01EA), "O");
		characterReplacements.put(Character.valueOf((char)0x01EB), "o");
		characterReplacements.put(Character.valueOf((char)0x01EC), "O");
		characterReplacements.put(Character.valueOf((char)0x01ED), "o");
		characterReplacements.put(Character.valueOf((char)0x01F0), "J");
		characterReplacements.put(Character.valueOf((char)0x01F1), "DZ");
		characterReplacements.put(Character.valueOf((char)0x01F2), "Dz");
		characterReplacements.put(Character.valueOf((char)0x01F3), "dz");
		characterReplacements.put(Character.valueOf((char)0x01F4), "G");
		characterReplacements.put(Character.valueOf((char)0x01F5), "g");
		characterReplacements.put(Character.valueOf((char)0x01F6), "Hu");
		characterReplacements.put(Character.valueOf((char)0x01F7), "D");
		characterReplacements.put(Character.valueOf((char)0x01F8), "N");
		characterReplacements.put(Character.valueOf((char)0x01F9), "n");
		characterReplacements.put(Character.valueOf((char)0x01FA), "A");
		characterReplacements.put(Character.valueOf((char)0x01FB), "a");
		characterReplacements.put(Character.valueOf((char)0x01FC), "AE");
		characterReplacements.put(Character.valueOf((char)0x01FD), "ae");
		characterReplacements.put(Character.valueOf((char)0x01FE), "O");
		characterReplacements.put(Character.valueOf((char)0x01FF), "o");
		characterReplacements.put(Character.valueOf((char)0x01F8), "N");
		characterReplacements.put(Character.valueOf((char)0x0200), "A");
		characterReplacements.put(Character.valueOf((char)0x0201), "a");
		characterReplacements.put(Character.valueOf((char)0x0202), "A");
		characterReplacements.put(Character.valueOf((char)0x0203), "a");
		characterReplacements.put(Character.valueOf((char)0x0204), "E");
		characterReplacements.put(Character.valueOf((char)0x0205), "e");
		characterReplacements.put(Character.valueOf((char)0x0206), "E");
		characterReplacements.put(Character.valueOf((char)0x0207), "e");
		characterReplacements.put(Character.valueOf((char)0x0208), "I");
		characterReplacements.put(Character.valueOf((char)0x0209), "i");
		characterReplacements.put(Character.valueOf((char)0x020A), "I");
		characterReplacements.put(Character.valueOf((char)0x020B), "i");
		characterReplacements.put(Character.valueOf((char)0x020C), "OE");
		characterReplacements.put(Character.valueOf((char)0x020D), "oe");
		characterReplacements.put(Character.valueOf((char)0x020E), "O");
		characterReplacements.put(Character.valueOf((char)0x0210), "R");
		characterReplacements.put(Character.valueOf((char)0x0211), "r");
		characterReplacements.put(Character.valueOf((char)0x0212), "R");
		characterReplacements.put(Character.valueOf((char)0x0213), "r");
		characterReplacements.put(Character.valueOf((char)0x0214), "UE");
		characterReplacements.put(Character.valueOf((char)0x0215), "ue");
		characterReplacements.put(Character.valueOf((char)0x0216), "U");
		characterReplacements.put(Character.valueOf((char)0x0217), "u");
		characterReplacements.put(Character.valueOf((char)0x0218), "S");
		characterReplacements.put(Character.valueOf((char)0x0219), "s");
		characterReplacements.put(Character.valueOf((char)0x021A), "T");
		characterReplacements.put(Character.valueOf((char)0x021E), "H");
		characterReplacements.put(Character.valueOf((char)0x021F), "h");
		characterReplacements.put(Character.valueOf((char)0x0220), "n");
		characterReplacements.put(Character.valueOf((char)0x0221), "d");
		characterReplacements.put(Character.valueOf((char)0x0224), "Z");
		characterReplacements.put(Character.valueOf((char)0x0225), "z");
		characterReplacements.put(Character.valueOf((char)0x0226), "A");
		characterReplacements.put(Character.valueOf((char)0x0227), "a");
		characterReplacements.put(Character.valueOf((char)0x0228), "E");
		characterReplacements.put(Character.valueOf((char)0x0229), "e");
		characterReplacements.put(Character.valueOf((char)0x022A), "O");
		characterReplacements.put(Character.valueOf((char)0x022B), "o");
		characterReplacements.put(Character.valueOf((char)0x022C), "O");
		characterReplacements.put(Character.valueOf((char)0x022D), "o");
		characterReplacements.put(Character.valueOf((char)0x022E), "O");
		characterReplacements.put(Character.valueOf((char)0x022F), "o");
		characterReplacements.put(Character.valueOf((char)0x0230), "O");
		characterReplacements.put(Character.valueOf((char)0x0231), "o");
		characterReplacements.put(Character.valueOf((char)0x0232), "Y");
		characterReplacements.put(Character.valueOf((char)0x0233), "y");
		characterReplacements.put(Character.valueOf((char)0x0234), "I");
		characterReplacements.put(Character.valueOf((char)0x0235), "n");
		characterReplacements.put(Character.valueOf((char)0x0236), "t");
		characterReplacements.put(Character.valueOf((char)0x0237), "J");
		characterReplacements.put(Character.valueOf((char)0x023A), "A");
		characterReplacements.put(Character.valueOf((char)0x023B), "C");
		characterReplacements.put(Character.valueOf((char)0x023C), "c");
		characterReplacements.put(Character.valueOf((char)0x023D), "t");
		characterReplacements.put(Character.valueOf((char)0x023E), "T");
		characterReplacements.put(Character.valueOf((char)0x023F), "s");
		characterReplacements.put(Character.valueOf((char)0x0240), "Z");
		characterReplacements.put(Character.valueOf((char)0x0243), "B");
		characterReplacements.put(Character.valueOf((char)0x0244), "U");
		characterReplacements.put(Character.valueOf((char)0x0246), "E");
		characterReplacements.put(Character.valueOf((char)0x0247), "e");
		characterReplacements.put(Character.valueOf((char)0x0248), "J");
		characterReplacements.put(Character.valueOf((char)0x0249), "j");
		characterReplacements.put(Character.valueOf((char)0x024A), "q");
		characterReplacements.put(Character.valueOf((char)0x024B), "q");
		characterReplacements.put(Character.valueOf((char)0x024C), "R");
		characterReplacements.put(Character.valueOf((char)0x024D), "r");
		characterReplacements.put(Character.valueOf((char)0x024E), "Y");
		characterReplacements.put(Character.valueOf((char)0x024F), "y");
		
		// Colors (see http://www.w3schools.com/cssref/css_colornames.asp)
		
		namesToColors.put("aliceblue", new int[] { 240, 248, 255 });
		namesToColors.put("antiquewhite", new int[] { 250, 235, 215 });
		namesToColors.put("aqua", new int[] { 0, 255, 255 });
		namesToColors.put("aquamarine", new int[] { 127, 255, 212 });
		namesToColors.put("azure", new int[] { 240, 255, 255 });
		namesToColors.put("beige", new int[] { 245, 245, 220 });
		namesToColors.put("bisque", new int[] { 255, 228, 196 });
		namesToColors.put("black", new int[] { 0, 0, 0 });
		namesToColors.put("blanchedalmond", new int[] { 255, 235, 205 });
		namesToColors.put("blue", new int[] { 0, 0, 255 });
		namesToColors.put("blueviolet", new int[] { 138, 43, 226 });
		namesToColors.put("brown", new int[] { 165, 42, 42 });
		namesToColors.put("burlywood", new int[] { 222, 184, 135 });
		namesToColors.put("cadetblue", new int[] { 95, 158, 160 });
		namesToColors.put("chartreuse", new int[] { 127, 255, 0 });
		namesToColors.put("chocolate", new int[] { 210, 105, 30 });
		namesToColors.put("coral", new int[] { 255, 127, 80 });
		namesToColors.put("cornflowerblue", new int[] { 100, 149, 237 });
		namesToColors.put("cornsilk", new int[] { 255, 248, 220 });
		namesToColors.put("crimson", new int[] { 220, 20, 60 });
		namesToColors.put("cyan", new int[] { 0, 255, 255 });
		namesToColors.put("darkblue", new int[] { 0, 0, 139 });
		namesToColors.put("darkcyan", new int[] { 0, 139, 139 });
		namesToColors.put("darkgoldenrod", new int[] { 184, 134, 11 });
		namesToColors.put("darkgray", new int[] { 169, 169, 169 });
		namesToColors.put("darkgreen", new int[] { 0, 100, 0 });
		namesToColors.put("darkkhaki", new int[] { 189, 183, 107 });
		namesToColors.put("darkmagenta", new int[] { 139, 0, 139 });
		namesToColors.put("darkolivegreen", new int[] { 85, 107, 47 });
		namesToColors.put("darkorange", new int[] { 255, 140, 0 });
		namesToColors.put("darkorchid", new int[] { 153, 50, 204 });
		namesToColors.put("darkred", new int[] { 139, 0, 0 });
		namesToColors.put("darksalmon", new int[] { 233, 150, 122 });
		namesToColors.put("darkseagreen", new int[] { 143, 188, 143 });
		namesToColors.put("darkslateblue", new int[] { 72, 61, 139 });
		namesToColors.put("darkslategray", new int[] { 47, 79, 79 });
		namesToColors.put("darkturquoise", new int[] { 0, 206, 209 });
		namesToColors.put("darkviolet", new int[] { 148, 0, 211 });
		namesToColors.put("deeppink", new int[] { 255, 20, 147 });
		namesToColors.put("deepskyblue", new int[] { 0, 191, 255 });
		namesToColors.put("dimgray", new int[] { 105, 105, 105 });
		namesToColors.put("dodgerblue", new int[] { 30, 144, 255 });
		namesToColors.put("firebrick", new int[] { 178, 34, 34 });
		namesToColors.put("floralwhite", new int[] { 255, 250, 240 });
		namesToColors.put("forestgreen", new int[] { 34, 139, 34 });
		namesToColors.put("fuchsia", new int[] { 255, 0, 255 });
		namesToColors.put("gainsboro", new int[] { 220, 220, 220 });
		namesToColors.put("ghostwhite", new int[] { 248, 248, 255 });
		namesToColors.put("gold", new int[] { 255, 215, 0 });
		namesToColors.put("goldenrod", new int[] { 218, 165, 32 });
		namesToColors.put("gray", new int[] { 128, 128, 128 });
		namesToColors.put("green", new int[] { 0, 128, 0 });
		namesToColors.put("greenyellow", new int[] { 173, 255, 47 });
		namesToColors.put("honeydew", new int[] { 240, 255, 240 });
		namesToColors.put("hotpink", new int[] { 255, 105, 180 });
		namesToColors.put("indianred", new int[] { 205, 92, 92 });
		namesToColors.put("indigo", new int[] { 75, 0, 130 });
		namesToColors.put("ivory", new int[] { 255, 255, 240 });
		namesToColors.put("khaki", new int[] { 240, 230, 140 });
		namesToColors.put("lavender", new int[] { 230, 230, 250 });
		namesToColors.put("lavenderblush", new int[] { 255, 240, 245 });
		namesToColors.put("lawngreen", new int[] { 124, 252, 0 });
		namesToColors.put("lemonchiffon", new int[] { 255, 250, 205 });
		namesToColors.put("lightblue", new int[] { 173, 216, 230 });
		namesToColors.put("lightcoral", new int[] { 240, 128, 128 });
		namesToColors.put("lightcyan", new int[] { 224, 255, 255 });
		namesToColors.put("lightgoldenrodyellow", new int[] { 250, 250, 210 });
		namesToColors.put("lightgray", new int[] { 211, 211, 211 });
		namesToColors.put("lightgreen", new int[] { 144, 238, 144 });
		namesToColors.put("lightpink", new int[] { 255, 182, 193 });
		namesToColors.put("lightsalmon", new int[] { 255, 160, 122 });
		namesToColors.put("lightseagreen", new int[] { 32, 178, 170 });
		namesToColors.put("lightskyblue", new int[] { 135, 206, 250 });
		namesToColors.put("lightslategray", new int[] { 119, 136, 153 });
		namesToColors.put("lightsteelblue", new int[] { 176, 196, 222 });
		namesToColors.put("lightyellow", new int[] { 255, 255, 224 });
		namesToColors.put("lime", new int[] { 0, 255, 0 });
		namesToColors.put("limegreen", new int[] { 50, 205, 50 });
		namesToColors.put("linen", new int[] { 250, 240, 230 });
		namesToColors.put("magenta", new int[] { 255, 0, 255 });
		namesToColors.put("maroon", new int[] { 128, 0, 0 });
		namesToColors.put("mediumaquamarine", new int[] { 102, 205, 170 });
		namesToColors.put("mediumblue", new int[] { 0, 0, 205 });
		namesToColors.put("mediumorchid", new int[] { 186, 85, 211 });
		namesToColors.put("mediumpurple", new int[] { 147, 112, 219 });
		namesToColors.put("mediumseagreen", new int[] { 60, 179, 113 });
		namesToColors.put("mediumslateblue", new int[] { 123, 104, 238 });
		namesToColors.put("mediumspringgreen", new int[] { 0, 250, 154 });
		namesToColors.put("mediumturquoise", new int[] { 72, 209, 204 });
		namesToColors.put("mediumvioletred", new int[] { 199, 21, 133 });
		namesToColors.put("midnightblue", new int[] { 25, 25, 112 });
		namesToColors.put("mintcream", new int[] { 245, 255, 250 });
		namesToColors.put("mistyrose", new int[] { 255, 228, 225 });
		namesToColors.put("moccasin", new int[] { 255, 228, 181 });
		namesToColors.put("navajowhite", new int[] { 255, 222, 173 });
		namesToColors.put("navy", new int[] { 0, 0, 128 });
		namesToColors.put("oldlace", new int[] { 253, 245, 230 });
		namesToColors.put("olive", new int[] { 128, 128, 0 });
		namesToColors.put("olivedrab", new int[] { 107, 142, 35 });
		namesToColors.put("orange", new int[] { 255, 165, 0 });
		namesToColors.put("orangered", new int[] { 255, 69, 0 });
		namesToColors.put("orchid", new int[] { 218, 112, 214 });
		namesToColors.put("palegoldenrod", new int[] { 238, 232, 170 });
		namesToColors.put("palegreen", new int[] { 152, 251, 152 });
		namesToColors.put("paleturquoise", new int[] { 175, 238, 238 });
		namesToColors.put("palevioletred", new int[] { 219, 112, 147 });
		namesToColors.put("papayawhip", new int[] { 255, 239, 213 });
		namesToColors.put("peachpuff", new int[] { 255, 218, 185 });
		namesToColors.put("peru", new int[] { 205, 133, 63 });
		namesToColors.put("pink", new int[] { 255, 192, 203 });
		namesToColors.put("plum", new int[] { 221, 160, 221 });
		namesToColors.put("powderblue", new int[] { 176, 224, 230 });
		namesToColors.put("purple", new int[] { 128, 0, 128 });
		namesToColors.put("rebeccapurple", new int[] { 102, 51, 153 });
		namesToColors.put("red", new int[] { 255, 0, 0 });
		namesToColors.put("rosybrown", new int[] { 188, 143, 143 });
		namesToColors.put("royalblue", new int[] { 65, 105, 225 });
		namesToColors.put("saddlebrown", new int[] { 139, 69, 19 });
		namesToColors.put("salmon", new int[] { 250, 128, 114 });
		namesToColors.put("sandybrown", new int[] { 244, 164, 96 });
		namesToColors.put("seagreen", new int[] { 46, 139, 87 });
		namesToColors.put("seashell", new int[] { 255, 245, 238 });
		namesToColors.put("sienna", new int[] { 160, 82, 45 });
		namesToColors.put("silver", new int[] { 192, 192, 192 });
		namesToColors.put("skyblue", new int[] { 135, 206, 235 });
		namesToColors.put("slateblue", new int[] { 106, 90, 205 });
		namesToColors.put("slategray", new int[] { 112, 128, 144 });
		namesToColors.put("snow", new int[] { 255, 250, 250 });
		namesToColors.put("springgreen", new int[] { 0, 255, 127 });
		namesToColors.put("steelblue", new int[] { 70, 130, 180 });
		namesToColors.put("tan", new int[] { 210, 180, 140 });
		namesToColors.put("teal", new int[] { 0, 128, 128 });
		namesToColors.put("thistle", new int[] { 216, 191, 216 });
		namesToColors.put("tomato", new int[] { 255, 99, 71 });
		namesToColors.put("turquoise", new int[] { 64, 224, 208 });
		namesToColors.put("violet", new int[] { 238, 130, 238 });
		namesToColors.put("wheat", new int[] { 245, 222, 179 });
		namesToColors.put("white", new int[] { 255, 255, 255 });
		namesToColors.put("whitesmoke", new int[] { 245, 245, 245 });
		namesToColors.put("yellow", new int[] { 255, 255, 0 });
		namesToColors.put("yellowgreen", new int[] { 154, 205, 50 });
	}
	
	/**
	 * Invisible constructor because <code>StringUtil</code> is a utility class.
	 */
	private StringUtil()
	{
		// No instance needed.
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Concatenates the given {@link List} of {@link Object}s with the given
	 * {@link String delimiter}. {@link Object#toString()} is used to convert
	 * the {@link Object} to a {@link String}, if the {@link Object} is
	 * {@code null}, the {@link String} {@code "null"} will be appended.
	 * 
	 * @param pDelimiter the {@link String delimiter} to use.
	 * @param pObjects the {@link Object}s to concatenate.
	 * @return the concatenated {@link String}. An empty {@link String} if the
	 *         {@link List} is either {@code null} or empty.
	 * @see #concat(String, Object...)
	 * @see #concat(String, String...)
	 * @see Object#toString()
	 */
	public static String concat(String pDelimiter, List<? extends Object> pObjects)
	{
		if (pObjects == null || pObjects.isEmpty())
		{
			return "";
		}
		
		StringBuilder result = new StringBuilder(16 * pObjects.size());
		
		for (Object object : pObjects)
		{
			result.append(object);
			result.append(pDelimiter);
		}
		
		// Remove the last delimiter.
		result.delete(result.length() - pDelimiter.length(), result.length());
		
		return result.toString();
	}
	
	/**
	 * Concatenates the {@link Object}s with the given {@link String delimiter}.
	 * {@link Object#toString()} is used to convert the the {@link Object} to a
	 * {@link String}, if the {@link Object} is {@code null}, the {@link String}
	 * {@code "null"} will be appended.
	 * 
	 * @param pDelimiter the {@link String delimiter} to use.
	 * @param pObjects the {@link Object}s to concatenate.
	 * @return the concatenated {@link String}. An empty {@link String} if the
	 *         {@link List} is either {@code null} or empty.
	 * @see #concat(String, List)
	 * @see #concat(String, String...)
	 * @see Object#toString()
	 */
	public static String concat(String pDelimiter, Object... pObjects)
	{
		// Early return necessary because Arrays.asList() will throw
		// a NullPointerException if the array is null.
		if (pObjects == null || pObjects.length == 0)
		{
			return "";
		}
		
		return concat(pDelimiter, Arrays.asList(pObjects));
	}
	
	/**
	 * Concatenates the elements with pDelimiter as separator.
	 * 
	 * @param pDelimiter the delimiter.
	 * @param pElements the elements to concatenate.
	 * @return the concatenated string.
	 * @see #concat(String, List)
	 * @see #concat(String, Object...)
	 */
	public static String concat(String pDelimiter, String... pElements)
	{
		// Early return necessary because Arrays.asList() will throw
		// a NullPointerException if the array is null.
		if (pElements == null || pElements.length == 0)
		{
			return "";
		}
		
		// I did some performance testing and neither the creation of the List
		// nor that toString() is called on a String (StringBuilder.append)
		// had a mentionable performance hit.
		return concat(pDelimiter, Arrays.asList(pElements));
	}
	
	/**
	 * Gets whether a text contains at least one
	 * {@link Character#isWhitespace(char) whitespace character}.
	 * 
	 * @param pText the text to check.
	 * @return {@code true} if at least one whitespace character was found,
	 *         {@code false} otherwise.
	 * @see Character#isWhitespace(char)
	 */
	public static boolean containsWhitespace(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return false;
		}
		
		for (char currentChar : pText.toCharArray())
		{
			if (Character.isWhitespace(currentChar))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Converts a member name in a human readable format e.g.
	 * {@code myMethodName} is converted to {@code My Method Name}. No special
	 * character sequences are replaced e.g. {@code ue} is not replaced with
	 * {@code �} (german umlaut).
	 * 
	 * @param pMemberName the member name.
	 * @return the human readable name.
	 * @see #convertMethodNameToText(String, boolean)
	 */
	public static String convertMemberNameToText(String pMemberName)
	{
		return convertMemberNameToText(pMemberName, false);
	}
	
	/**
	 * Converts a member name in a human readable format e.g.
	 * {@code myMethodName} is converted to {@code My Method Name}.
	 * 
	 * @param pMemberName the member name.
	 * @param pReplaceSpecialCharacterSequences {@code true} to replace e.g.
	 *            {@code ue} to {@code �} (german umlaut).
	 * @return the human readable name.
	 */
	public static String convertMemberNameToText(String pMemberName, boolean pReplaceSpecialCharacterSequences)
	{
		if (pMemberName == null || pMemberName.length() == 0)
		{
			return pMemberName;
		}
		
		return convertNameToText(pMemberName, pReplaceSpecialCharacterSequences, false);
	}
	
	/**
	 * Converts a method name in a human readable format. The prefix is removed
	 * and spaces are inserted where the character case is changed e.g.
	 * {@code getMyMethodName} is converted to {@code My Method Name}. No
	 * special character sequences are replaced e.g. {@code ue} is not replaced
	 * with {@code �} (german umlaut).
	 * 
	 * @param pMethodName the method name.
	 * @return the human readable name.
	 * @see #convertMethodNameToText(String, boolean)
	 */
	public static String convertMethodNameToText(String pMethodName)
	{
		return convertMethodNameToText(pMethodName, false);
	}
	
	/**
	 * Converts a method name in a human readable format. The prefix is removed
	 * and spaces are inserted where the character case is changed e.g.
	 * {@code getMyMethodName} is converted to {@code My Method Name}.
	 * 
	 * @param pMethodName the method name.
	 * @param pReplaceSpecialCharacterSequences {@code true} to replace e.g.
	 *            {code ue} to {@code �} (german umlaut).
	 * @return the human readable name.
	 */
	public static String convertMethodNameToText(String pMethodName, boolean pReplaceSpecialCharacterSequences)
	{
		return convertNameToText(removeMethodPrefix(pMethodName), pReplaceSpecialCharacterSequences, true);
	}
	
	/**
	 * Converts a method name to a name, without prefix and first character lowercase.
	 * 
	 * @param pMethodName the method name
	 * @return the name
	 */
    public static String convertMethodNameToFieldName(String pMethodName)
    {
        return firstCharLower(removeMethodPrefix(pMethodName));
    }

    /**
     * Removes the prefix from the given method. The prefix is everything before the first uppercase character.
     * 
     * @param pMethodName the method name e.g. getValue
     * @return the name without prefix e.g. Value
     */
    private static String removeMethodPrefix(String pMethodName)
	{
        if (pMethodName == null || pMethodName.length() == 0)
        {
            return pMethodName;
        }

        // We will look for the first uppercase letter or underscore and remove
        // everything before it. This is to cut a leading get/set and similar.
        
        String sMethodName = pMethodName;
        
        int firstUppercaseCharacterIndex = -1;
        
        for (int index = 0; index < sMethodName.length() && firstUppercaseCharacterIndex == -1; index++)
        {
            char currentChar = sMethodName.charAt(index);
            
            if (Character.isUpperCase(currentChar)
                    || currentChar == '_')
            {
                firstUppercaseCharacterIndex = index;
            }
        }
        
        if (firstUppercaseCharacterIndex >= 0)
        {
            sMethodName = sMethodName.substring(firstUppercaseCharacterIndex);
        }
        
        return sMethodName;
	}
	
	/**
	 * Converts any text to a member name. If the text contains whitespaces or
	 * {@code _} they will be removed. If the text contains special characters
	 * like German umlauts, the characters will be replaced by appropriate ASCII
	 * characters.
	 * 
	 * @param pName the text.
	 * @return the converted member name.
	 */
	public static String convertToMemberName(String pName)
	{
		return convertToMethodName(null, pName);
	}
	
	/**
	 * Converts any text to a method name. If the text contains whitespaces or
	 * {@code _} they will be removed. If the text contains special characters
	 * like German umlauts, the characters will be replaced by appropriate ASCII
	 * characters. If the name contains no text, null will be returned.
	 * 
	 * @param pPrefix the method prefix e.g. get, set, is, has, ...
	 * @param pName the text.
	 * @return the converted member name.
	 */
	public static String convertToMethodName(String pPrefix, String pName)
	{
		if (pName == null || pName.length() == 0)
		{
			return pName;
		}
		
		int prefixLength = 0;
		
		if (pPrefix != null && pPrefix.length() > 0)
		{
			prefixLength = pPrefix.length();
		}
		
		StringBuilder humanReadableName = new StringBuilder(prefixLength + pName.length() + 16);
		
		boolean firstCharacterFound = false;
		
		if (pPrefix != null)
		{
			humanReadableName.append(pPrefix);
			firstCharacterFound = true;
		}
		
		for (int index = 0; index < pName.length(); index++)
		{
			char currentChar = pName.charAt(index);
			
			// Replace the character (as necessary) before we do anything else.
			String replaceString = characterReplacements.get(Character.valueOf(currentChar));
			
			if (replaceString != null)
			{
				currentChar = replaceString.charAt(0);
			}
			
			if (Character.isLetter(currentChar))
			{
				currentChar = Character.toLowerCase(currentChar);
				
				if (index > 0)
				{
					char previousChar = pName.charAt(index - 1);
					
					if (Character.isWhitespace(previousChar)
							|| previousChar == '_')
					{
						currentChar = Character.toUpperCase(currentChar);
					}
				}
				else if (firstCharacterFound)
				{
					currentChar = Character.toUpperCase(currentChar);
				}
				
				humanReadableName.append(currentChar);
				
				if (replaceString != null && replaceString.length() > 1)
				{
					humanReadableName.append(replaceString.substring(1));
				}
				
				firstCharacterFound = true;
			}
			else if (firstCharacterFound
					&& (Character.isLetter(currentChar) || Character.isDigit(currentChar)))
			{
				humanReadableName.append(currentChar);
			}
		}
		
		return humanReadableName.toString();
	}
	
	/**
	 * Converts text to a valid name. All whitespaces will be removed and non
	 * ASCII characters will be replaced with ASCII characters, if this is
	 * possible.
	 * 
	 * @param pText any text.
	 * @return a valid name.
	 */
	public static String convertToName(String pText)
	{
		return convertToName(pText, null, null);
	}
	
	/**
	 * Converts text to a valid name. All whitespaces will be removed and non
	 * ASCII characters will be replaced with ASCII characters, if this is
	 * possible.
	 * 
	 * @param pText any text.
	 * @param pAdditionalStartCharacters additional start characters.
	 * @param pAdditionalCharacters additional characters.
	 * @return a valid name.
	 */
	public static String convertToName(String pText, String pAdditionalStartCharacters, String pAdditionalCharacters)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		StringBuilder name = new StringBuilder(pText.length() + 16);
		
		boolean firstCharacterFound = false;
		
		for (int index = 0; index < pText.length(); index++)
		{
			char currentChar = pText.charAt(index);
			
			// Replace the character (as necessary) before we do anything else.
			String replaceString = characterReplacements.get(Character.valueOf(currentChar));
			
			if (replaceString != null)
			{
				name.append(replaceString);
			}
			else if (!firstCharacterFound
					&& (Character.isLetter(currentChar)
							|| (pAdditionalStartCharacters != null && pAdditionalStartCharacters.indexOf(currentChar) >= 0)))
			{
				name.append(currentChar);
				firstCharacterFound = true;
			}
			else if (firstCharacterFound
					&& (Character.isLetter(currentChar)
							|| Character.isDigit(currentChar)
							|| (pAdditionalCharacters != null && pAdditionalCharacters.indexOf(currentChar) >= 0)
							|| currentChar == '_'))
			{
				name.append(currentChar);
			}
		}
		
		return name.toString();
	}
	
	/**
	 * Counts the number of times the given character appears in the given text.
	 * 
	 * @param pText the text.
	 * @param pCharToCount the character to search.
	 * @return the number of occurrences of {@code pChar} in {@code pText}.
	 */
	public static int countCharacter(String pText, char pCharToCount)
	{
		if (pText == null || pText.length() == 0)
		{
			return 0;
		}
		
		int count = 0;
		
		for (char currentChar : pText.toCharArray())
		{
			if (currentChar == pCharToCount)
			{
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Converts the first char to lowercase.
	 * 
	 * @param pText the text.
	 * @return the text with the first character converter to lower case.
	 */
	public static String firstCharLower(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		return Character.toLowerCase(pText.charAt(0)) + pText.substring(1);
	}
	
	/**
	 * Converts the first char to uppercase.
	 * 
	 * @param pText the text.
	 * @return the text with the first character converter to uppercase.
	 */
	public static String firstCharUpper(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		return Character.toUpperCase(pText.charAt(0)) + pText.substring(1);
	}
	
	/**
	 * Sets the first character in each word to uppercase and the rest to
	 * lowercase.
	 * 
	 * @param pName the unformatted text.
	 * @return the formatted text.
	 */
	public static String formatInitCap(String pName)
	{
		return formatInitCap(pName, false);
	}
	
	/**
	 * Sets the first character in each word to uppercase and the rest to
	 * lowercase.
	 * 
	 * @param pName the unformatted or formatted text.
	 * @param pRemoveSpaces {@code true} to remove whitespace characters from
	 *            the result.
	 * @return the formatted text.
	 */
	public static String formatInitCap(String pName, boolean pRemoveSpaces)
	{
		if (pName == null || pName.length() == 0)
		{
			return pName;
		}
		
		String trimmedName = pName.trim();
		
		StringBuilder formattedName = new StringBuilder(trimmedName.length() + 16);
		
		boolean nextIsUppercase = true;
		
		for (char currentChar : trimmedName.toCharArray())
		{
			if (Character.isWhitespace(currentChar)
					|| currentChar == '_')
			{
				nextIsUppercase = true;
				
				if (!pRemoveSpaces)
				{
					formattedName.append(" ");
				}
			}
			else if (nextIsUppercase)
			{
				formattedName.append(Character.toUpperCase(currentChar));
				nextIsUppercase = false;
			}
			else
			{
				formattedName.append(Character.toLowerCase(currentChar));
			}
		}
		
		String trimmedFormattedName = formattedName.toString().trim();
		
		if (trimmedFormattedName.length() == 0)
		{
			return formattedName.toString();
		}
		else
		{
			return trimmedFormattedName;
		}
	}
	
	/**
	 * Formats a member name. A member starts always with a lower case letter.
	 * 
	 * @param pName the member name.
	 * @return the formatted member name.
	 */
	public static String formatMemberName(String pName)
	{
		return formatMethodName(null, pName);
	}
	
	/**
	 * Formats a method name with a given property name and the method prefix.
	 * 
	 * @param pPrefix the method prefix, an empty string or {@code null} if the
	 *            method name has no prefix.
	 * @param pName the unformatted method name.
	 * @return the formatted method name. The first character is upper case when
	 *         a prefix is used, and the first character is lower case when no
	 *         prefix is used. The other characters are unchanged.
	 */
	public static String formatMethodName(String pPrefix, String pName)
	{
		if (pName != null && pName.trim().length() > 0)
		{
			if (pPrefix == null)
			{
				return Character.toLowerCase(pName.charAt(0)) + pName.substring(1);
			}
			else
			{
				return pPrefix + Character.toUpperCase(pName.charAt(0)) + pName.substring(1);
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the case sensitive type of a text.
	 * 
	 * @param pText any text or {@code null}.
	 * @return {@link CaseSensitiveType#NoLetter} if the text contains no letter
	 *         or the text is {@code null}. {@link CaseSensitiveType#LowerCase}
	 *         if the text contains at least one letter and all available
	 *         letters are lower case. {@link CaseSensitiveType#UpperCase} if
	 *         the text contains at least one letter and all available letters
	 *         are upper case. {@link CaseSensitiveType#MixedCase} if the text
	 *         contains at least two letters and the text contains lower and
	 *         upper case letters.
	 */
	public static CaseSensitiveType getCaseSensitiveType(String pText)
	{
		if (pText == null || pText.trim().length() == 0)
		{
			return CaseSensitiveType.NoLetter;
		}
		
		CaseSensitiveType type = CaseSensitiveType.NoLetter;
		
		for (char currentChar : pText.toCharArray())
		{
			if (Character.isLetter(currentChar))
			{
				if (Character.isUpperCase(currentChar))
				{
					if (type == CaseSensitiveType.LowerCase)
					{
						return CaseSensitiveType.MixedCase;
					}
					
					type = CaseSensitiveType.UpperCase;
				}
				else if (Character.isLowerCase(currentChar))
				{
					if (type == CaseSensitiveType.UpperCase)
					{
						return CaseSensitiveType.MixedCase;
					}
					
					type = CaseSensitiveType.LowerCase;
				}
			}
		}
		
		return type;
	}
	
	/**
	 * Gets the character type of a text.
	 * 
	 * @param pText any text or {@code null}.
	 * @return {@link CharacterType#None} if the text is empty or {@code null}.
	 *         {@link CharacterType#Letters} if the text only contains letters.
	 *         {@link CharacterType#Digits} if the text only contains digits.
	 *         {@link CharacterType#LettersDigits} if the text only contains
	 *         letters and digits. {@link CharacterType#LettersSpecial} if the
	 *         text contains letters and other characters but no digits.
	 *         {@link CharacterType#DigitsSpecial} if the text contains digits
	 *         and other characters but no letters.
	 *         {@link CharacterType#LettersDigitsWhitespace} if the text only
	 *         contains letters, digits and whitespaces.
	 *         {@link CharacterType#LettersDigitsSpace} if the text only
	 *         contains letters, digits and spaces.
	 *         {@link CharacterType#OnlyWhitespace} if the text only contains
	 *         whitespaces. {@link CharacterType#OnlySpecial} if the text
	 *         contains no letters and no digits. {@link CharacterType#All} if
	 *         the text contains letters, digits and other characters.
	 */
	public static CharacterType getCharacterType(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return CharacterType.None;
		}
		
		char ch;
		
		boolean bLetter = false;
		boolean bDigit = false;
		boolean bWhite = false;
		boolean bSpace = false;
		boolean bNotSpace = false;
		boolean bSpecial = false;
		boolean bOther = false;
		
		for (int i = 0, length = pText.length(); i < length; i++)
		{
			ch = pText.charAt(i);
			
			if (Character.isLetter(ch))
			{
				bLetter = true;
			}
			else if (Character.isDigit(ch))
			{
				bDigit = true;
			}
			else if (Character.isWhitespace(ch))
			{
				bWhite = true;
				bSpecial = true;
				
				if (Character.isSpaceChar(ch))
				{
					bSpace = true;
				}
				else
				{
					bNotSpace = true;
				}
			}
			else
			{
				bSpecial = true;
				bOther = true;
			}
			
			if (bLetter && bDigit && bWhite && bOther)
			{
				return CharacterType.All;
			}
		}
		
		if (bLetter && bDigit && bWhite && !bOther)
		{
			if (bSpace && !bNotSpace)
			{
				return CharacterType.LettersDigitsSpace;
			}
			
			return CharacterType.LettersDigitsWhitespace;
		}
		
		if (bLetter && bDigit && !bWhite && !bOther)
		{
			return CharacterType.LettersDigits;
		}
		
		if (bLetter && !bDigit && !bWhite && !bOther)
		{
			return CharacterType.Letters;
		}
		
		if (bLetter && !bDigit && (bWhite || bOther))
		{
			return CharacterType.LettersSpecial;
		}
		
		if (bDigit && !bLetter && !bWhite && !bOther)
		{
			return CharacterType.Digits;
		}
		
		if (bDigit && !bLetter && (bWhite || bOther))
		{
			return CharacterType.DigitsSpecial;
		}
		
		if (bWhite && !bOther)
		{
			return CharacterType.OnlyWhitespace;
		}
		
		if (bSpecial)
		{
			return CharacterType.OnlySpecial;
		}
		
		return CharacterType.None;
	}
	
	/**
	 * Gets the first word of a text. The end of the first word is defined as
	 * end of text or if a uppercase letter follows.
	 * 
	 * @param pText the text.
	 * @return the first word.
	 */
	public static String getFirstWord(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		StringBuilder firstWord = new StringBuilder();
		
		char ch;
		
		for (int i = 0, len = pText.length(); i < len; i++)
		{
			ch = pText.charAt(i);
			
			if (i > 0 && Character.isUpperCase(ch))
			{
				return firstWord.toString();
			}
			else
			{
				firstWord.append(ch);
			}
		}
		
		return firstWord.toString();
	}
	
	/**
	 * Returns the boundaries of quoted strings. Examples.:
	 * 
	 * <pre>
	 * unitTest("Test", Application.class, " Application.class");
	 * unitTest("Test\", Application.class", Application.class");
	 * unitTest("Test", Application.class", Application.class);
	 * </pre>
	 * 
	 * @param pText the text with our without quotes.
	 * @return the boundaries as array [start, stop, start, stop] or an empty
	 *         array.
	 */
	public static int[] getQuotedBoundaries(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return new int[0];
		}
		
		int[] boundaries = new int[0];
		
		int iStart = -1;
		
		boolean bStart = false;
		
		for (int i = 0, length = pText.length(); i < length; i++)
		{
			if (pText.charAt(i) == '\"' && (i == 0 || pText.charAt(i - 1) != '\\'))
			{
				bStart = !bStart;
				
				if (bStart)
				{
					iStart = i;
				}
				else
				{
					boundaries = ArrayUtil.add(boundaries, iStart);
					boundaries = ArrayUtil.add(boundaries, i);
				}
			}
		}
		
		return boundaries;
	}
	
	/**
	 * Gets all words from the given text with the given maximum length. The words must be separated by whitespace
	 * to detect a single word. If the given text is {@code null} or empty the given text is returned.
	 * 
	 * Examples:
	 * 
	 * <pre>
	 * Some Data, 2		-&gt; SoDa
	 * ContEduc, 3		-&gt; con
	 * ContrEduca, 1	-&gt; C 
	 * c.o.m, 2			-&gt; co
	 * </pre>
	 * 
	 * @param pText the text to process
	 * @param pMaxWordLength the maximum length of the words
	 * @param pOnlyLetters {@code true} to include only letters in the text, {@code false} to use letters and digits 
	 * @return all words from the given text with the given maximum length.
	 *         {@code null} if the given text was null, an empty string if the
	 *         given text was empty or the maximum word length was equal or less
	 *         than zero.
	 */
	public static String getWhitespaceSeparatedShortenedWords(String pText, int pMaxWordLength, boolean pOnlyLetters)
	{
		return getWhitespaceSeparatedShortenedWords(pText, pMaxWordLength, pOnlyLetters, false);
	}
		
	/**
	 * Gets all words from the given text with the given maximum length. The words must be separated by whitespace
	 * to detect a single word or if case-mix is enabled, an upper-case letter. If the given text is {@code null} 
	 * or empty the given text is returned.
	 * 
	 * Examples:
	 * 
	 * <pre>
	 * Some Data, 2		-&gt; SoDa
	 * ContEduc, 3		-&gt; Con or ConEdu (if case-mix) 
	 * ContrEduca, 1	-&gt; C 
	 * c.o.m, 2			-&gt; co
	 * </pre>
	 * 
	 * @param pText the text to process
	 * @param pMaxWordLength the maximum length of the words
	 * @param pOnlyLetters {@code true} to include only letters in the text, {@code false} to use letters and digits 
	 * @param pCaseMix {@code true} to start a new word in case of an upper-case letter, {@code false} otherwise
	 * @return all words from the given text with the given maximum length.
	 *         {@code null} if the given text was null, an empty string if the
	 *         given text was empty or the maximum word length was equal or less
	 *         than zero.
	 */
	public static String getWhitespaceSeparatedShortenedWords(String pText, int pMaxWordLength, boolean pOnlyLetters, boolean pCaseMix)
	{
		if (isEmpty(pText))
		{
			return pText;
		}
		
		if (pMaxWordLength <= 0)
		{
			return "";
		}
		
		StringBuilder words = new StringBuilder();
		
		int currentWordLength = 0;
		
		for (char currentChar : pText.toCharArray())
		{
			if (pCaseMix && Character.isUpperCase(currentChar))
			{
				currentWordLength = 0;
			}

			if ((pOnlyLetters && Character.isLetter(currentChar)) 
				|| (!pOnlyLetters && Character.isLetterOrDigit(currentChar)))
			{
				if (currentWordLength < pMaxWordLength)
				{
					words.append(currentChar);
				}
				
				currentWordLength++;
			}
			else if (Character.isWhitespace(currentChar))
			{
				// This is the start of a new word, but this character does
				// not count towards the length of the word.
				currentWordLength = 0;
			}
		}
		
		return words.toString();	
	}
	
	/**
	 * Gets all words from the given text with the given maximum length. If the
	 * given text is {@code null} or empty the given text is returned.
	 * 
	 * Anything that is not a letter is returned verbatim and assumed to be a
	 * word separator.
	 * 
	 * Examples:
	 * 
	 * <pre>
	 * SomeDataWorkScreen, 3		-&gt; SomDatWorScr
	 * ContractsEducation, 3		-&gt; ConEdu
	 * ContractsEducation, 5		-&gt; ContrEduca
	 * com.sibvisions.test.ClassName, 3	-&gt; com.sib.tes.ClaNam
	 * </pre>
	 * 
	 * @param pText the text to process
	 * @param pMaxWordLength the maximum length of the words
	 * @return all words from the given text with the given maximum length.
	 *         {@code null} if the given text was null, an empty string if the
	 *         given text was empty or the maximum word length was equal or less
	 *         than zero.
	 */
	public static String getShortenedWords(String pText, int pMaxWordLength)
	{
		return getShortenedWords(pText, pMaxWordLength, false);
	}
	
	/**
	 * Gets all words from the given text with the given maximum length. The words must be separated by whitespace
	 * to detect a single word. If the given text is {@code null} or empty the given text is returned.
	 * 
	 * Examples:
	 * 
	 * <pre>
	 * SomeDataWorkScreen, 3		-&gt; SomDatWorScr
	 * ContractsEducation, 3		-&gt; ConEdu
	 * ContractsEducation, 5		-&gt; ContrEduca
	 * com.sibvisions.test.ClassName, 3	-&gt; com.sib.tes.ClaNam
	 * </pre>
	 * 
	 * @param pText the text to process
	 * @param pMaxWordLength the maximum length of the words
	 * @param pIgnoreWhitespace {@code true} to ignore whitespace in returned text
	 * @return all words from the given text with the given maximum length.
	 *         {@code null} if the given text was null, an empty string if the
	 *         given text was empty or the maximum word length was equal or less
	 *         than zero.
	 */
	public static String getShortenedWords(String pText, int pMaxWordLength, boolean pIgnoreWhitespace)
	{
		if (isEmpty(pText))
		{
			return pText;
		}
		
		if (pMaxWordLength <= 0)
		{
			return "";
		}
		
		StringBuilder words = new StringBuilder();
		
		int currentWordLength = 0;
		
		for (char currentChar : pText.toCharArray())
		{
			if (Character.isUpperCase(currentChar))
			{
				words.append(currentChar);
				currentWordLength = 1;
			}
			else if (!Character.isLetter(currentChar))
			{
				if (!pIgnoreWhitespace || !Character.isWhitespace(currentChar))
				{
					words.append(currentChar);
				}
				
				// This is the start of a new word, but this character does
				// not count towards the length of the word.
				currentWordLength = 0;
			}
			else if (currentWordLength < pMaxWordLength)
			{
				words.append(currentChar);
				currentWordLength++;
			}
		}
		
		return words.toString();
	}
	
	/**
	 * Gets only specific characters from a string.
	 * 
	 * @param pText any text.
	 * @param pAllowed the allowed characters.
	 * @return the alpha numeric characters.
	 */
	public static String getText(String pText, char... pAllowed)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		if (pAllowed == null || pAllowed.length == 0)
		{
			return "";
		}
		
		StringBuilder specificText = new StringBuilder();
		
		for (char currentChar : pText.toCharArray())
		{
			if (ArrayUtil.contains(pAllowed, currentChar))
			{
				specificText.append(currentChar);
			}
		}
		
		return specificText.toString();
	}
	
	/**
	 * Gets only specific characters from a string.
	 * 
	 * @param pText any text.
	 * @param pType the character return type.
	 * @return the alpha numeric characters.
	 */
	public static String getText(String pText, TextType pType)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		StringBuilder specificText = new StringBuilder();
		
		boolean startCharacterFound = false;
		
		for (char currentChar : pText.toCharArray())
		{
			if (!startCharacterFound)
			{
				startCharacterFound = pType.isStartCharacter(currentChar);
			}
			
			if (startCharacterFound && pType.acceptCharacter(currentChar))
			{
				specificText.append(currentChar);
			}
		}
		
		return specificText.toString();
	}
	
	/**
	 * Gets whether the given text contains no characters or consists only of
	 * whitespace.
	 * 
	 * @param pString the string to test.
	 * @return {@code true} if {@code pText} is {@code null} or has 0 characters
	 *         (whitespaces will be ignored).
	 * @see String#trim()
	 */
	public static boolean isEmpty(String pString)
	{
		return pString == null || pString.trim().length() == 0;
	}
	
	/**
	 * Calculates the Damerau-Levenshtein distance between two strings.
	 * 
	 * The return value is basically the number of operations needed to turn one
	 * string into another. An operation in this context is a insertion,
	 * deletion, substitution or transposition.
	 * 
	 * @param pStringA the first string. Needs to be not {@code null}.
	 * @param pStringB the second string. Needs to be not {@code null}.
	 * @return the Damerau-Levenshtein distance.
	 * @throws IllegalArgumentException if any of the parameters is {@code null}
	 *             .
	 * @see <a href=
	 *      "http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance">
	 *      http://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
	 *      </a>
	 */
	public static int levenshteinDistance(String pStringA, String pStringB)
	{
		if (pStringA == null)
		{
			throw new IllegalArgumentException("The first string can not be null!");
		}
		
		if (pStringB == null)
		{
			throw new IllegalArgumentException("The second string can not be null!");
		}
		
		int[][] distanceTable = new int[pStringA.length() + 1][pStringB.length() + 1];
		
		for (int indexA = 0; indexA <= pStringA.length(); indexA++)
		{
			distanceTable[indexA][0] = indexA;
		}
		for (int indexB = 0; indexB <= pStringB.length(); indexB++)
		{
			distanceTable[0][indexB] = indexB;
		}
		
		for (int indexA = 1; indexA <= pStringA.length(); indexA++)
		{
			for (int indexB = 1; indexB <= pStringB.length(); indexB++)
			{
				int cost = 0;
				
				if (pStringA.charAt(indexA - 1) != pStringB.charAt(indexB - 1))
				{
					cost = 1;
				}
				
				int deletion = distanceTable[indexA - 1][indexB] + 1;
				int insertion = distanceTable[indexA][indexB - 1] + 1;
				int substitution = distanceTable[indexA - 1][indexB - 1] + cost;
				
				distanceTable[indexA][indexB] = Math.min(Math.min(deletion, insertion), substitution);
				
				if (indexA > 1 && indexB > 1)
				{
					if (pStringA.charAt(indexA - 1) == pStringB.charAt(indexB - 2) && pStringA.charAt(indexA - 2) == pStringB.charAt(indexB - 1))
					{
						int transposition = distanceTable[indexA - 2][indexB - 2] + cost;
						distanceTable[indexA][indexB] = Math.min(distanceTable[indexA][indexB], transposition);
					}
				}
			}
		}
		
		return distanceTable[pStringA.length()][pStringB.length()];
	}
	
	/**
	 * Fast like search in Strings with wildcard (* and ?) support.
	 * 
	 * @param pSource any string.
	 * @param pSearch search pattern with or without wildcards.
	 * @return {@code true} if, and only if, the string matches the pattern.
	 */
	public static boolean like(String pSource, String pSearch)
	{
		if (pSource == null && pSearch == null)
		{
			return true;
		}
		else if (pSource == null || pSearch == null)
		{
			return false;
		}
		else
		{
			return like(pSource, 0, pSource.length(), pSearch, 0, pSearch.length());
		}
	}
	
    /**
     * Pads the given string on the left side with spaces until it has the
     * specified length.
     * 
     * @param pText the text to format.
     * @param pLength the length in characters to pad left.
     * @return the left padded String.
     * @since 2.5
     */
    public static String lpad(String pText, int pLength)
    {
        return lpad(pText, pLength, ' ');
    }
    
    /**
	 * Pads the given string on the left side with given character until it has the
	 * specified length.
	 * 
	 * @param pText the text to format.
	 * @param pLength the length in characters to pad left.
	 * @param pChar the fill character.
	 * @return the left padded String.
	 * @since 2.6
	 */
	public static String lpad(String pText, int pLength, char pChar)
	{
		if (pText == null || pText.length() >= pLength)
		{
			return pText;
		}
		
		StringBuilder paddedText = new StringBuilder(pLength);
		
		int paddingLength = pLength - pText.length();
		
		for (int counter = 0; counter < paddingLength; counter++)
		{
			paddedText.append(pChar);
		}
		
		paddedText.append(pText);
		
		return paddedText.toString();
	}
	
	/**
	 * Removes all whitespaces before the first non whitespace character.
	 * 
	 * @param pText the text.
	 * @return the left trimmed text.
	 */
	public static String ltrim(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		for (int index = 0; index < pText.length(); index++)
		{
			if (!Character.isWhitespace(pText.charAt(index)))
			{
				return pText.substring(index);
			}
		}
		
		return "";
	}
	
	/**
	 * Pads the given string on the left side with spaces until it has the
	 * specified length.
	 * 
	 * @param pText the text to format.
	 * @param pLength the length in characters to pad left.
	 * @return the left padded String.
	 * @deprecated since 2.5, use {@link StringUtil#lpad(String, int)} instead.
	 */
	@Deprecated
	public static String padLeft(Object pText, int pLength)
	{
		return lpad(StringUtil.toString(pText), pLength);
	}
	
	/**
	 * It formats a string with right Padding over pWidth characters.
	 * 
	 * @param pText the text to format.
	 * @param pLength the length in characters to pad right.
	 * @return the right padded String.
	 * @deprecated since 2.5, use {@link StringUtil#rpad(String, int)} instead.
	 */
	@Deprecated
	public static String padRight(Object pText, int pLength)
	{
		return rpad(StringUtil.toString(pText), pLength);
	}
	
	/**
	 * Parses color codes from given string. The implementation supports
	 * following formats:
	 * 
	 * <pre>
	 * #9988AA (web style)
	 * #98A (web style)  
	 * 0x9988AA
	 * 127, 127, 127 (without alpha value) 
	 * 127, 127, 127, 0 (with alpha value)
	 * </pre>
	 * 
	 * @param pValue the color definition.
	 * @return the found RGB or RGBA values or {@code null} if format isn't
	 *         supported.
	 */
	public static int[] parseColor(String pValue)
	{
		if (pValue == null)
		{
			return null;
		}
		else if (namesToColors.containsKey(pValue.toLowerCase()))
		{
			return namesToColors.get(pValue.toLowerCase());
		}
		else if (pValue.startsWith("#"))
		{
			if (pValue.length() == 9)
			{
				return new int[] {
						Integer.valueOf(pValue.substring(1, 3), 16).intValue(),
						Integer.valueOf(pValue.substring(3, 5), 16).intValue(),
						Integer.valueOf(pValue.substring(5, 7), 16).intValue(),
						Integer.valueOf(pValue.substring(7, 9), 16).intValue()};
				
			}
			else if (pValue.length() == 7)
			{
				return new int[] {
						Integer.valueOf(pValue.substring(1, 3), 16).intValue(),
						Integer.valueOf(pValue.substring(3, 5), 16).intValue(),
						Integer.valueOf(pValue.substring(5, 7), 16).intValue() };
				
			}
			else if (pValue.length() == 4)
			{
				String sR = pValue.substring(1, 2);
				sR += sR;
				String sG = pValue.substring(2, 3);
				sG += sG;
				String sB = pValue.substring(3, 4);
				sB += sB;
				
				return new int[] { Integer.valueOf(sR, 16).intValue(),
						Integer.valueOf(sG, 16).intValue(),
						Integer.valueOf(sB, 16).intValue() };
			}
			else
			{
				return null;
			}
		}
		else if (pValue.startsWith("0x") || pValue.startsWith("0X"))
		{
			if (pValue.length() == 10)
			{
				return new int[] {
						Integer.valueOf(pValue.substring(4, 6), 16).intValue(),
						Integer.valueOf(pValue.substring(6, 8), 16).intValue(),
						Integer.valueOf(pValue.substring(8, 10), 16).intValue(),
				        Integer.valueOf(pValue.substring(2, 4), 16).intValue()}; // Alpha-Values are the first 2 digits in the 0x format. See: #2846
				
			}
			else if (pValue.length() == 8)
			{
				return new int[] {
						Integer.valueOf(pValue.substring(2, 4), 16).intValue(),
						Integer.valueOf(pValue.substring(4, 6), 16).intValue(),
						Integer.valueOf(pValue.substring(6, 8), 16).intValue() };
				
			}
			else
			{
				return null;
			}
		}
		
		int[] iValues = StringUtil.parseInteger(pValue, ",");
		
		if (iValues == null)
		{
			return null;
		}
		else
		{
			if (iValues.length == 3)
			{
				return new int[] { iValues[0], iValues[1], iValues[2] };
			}
			else if (iValues.length == 4)
			{
				return new int[] { iValues[0], iValues[1], iValues[2], iValues[3] };
			}
			
			return null;
		}
	}
	
	/**
	 * Gets the int values from a string with delimiters.
	 * 
	 * @param pValues the string with numbers and delimiters.
	 * @param pDelimiter the delimiter.
	 * @return the int values or {@code null} if the values are {@code null} or
	 *         empty.
	 */
	public static int[] parseInteger(String pValues, String pDelimiter)
	{
		if (pValues == null || pValues.trim().length() == 0)
		{
			return null;
		}
		
		if (pDelimiter == null || pDelimiter.length() == 0)
		{
			try
			{
				return new int[] { Integer.parseInt(pValues) };
			}
			catch (NumberFormatException nfe)
			{
				return new int[] {};
			}
		}
		
		List<String> valuesAsList = separateList(pValues, pDelimiter, true);
		
		int[] values = new int[valuesAsList.size()];
		
		for (int index = 0, length = values.length; index < length; index++)
		{
			try
			{
				values[index] = Integer.parseInt(valuesAsList.get(index));
			}
			catch (NumberFormatException nfe)
			{
				values[index] = -1;
			}
		}
		
		return values;
	}
	
	/**
	 * Adds a quote character to the begin and end of text. If the text contains
	 * the quote character then an additional quote character will be added e.g.
	 * "value is ""0"""
	 * 
	 * @param pText the text to protect.
	 * @param pQuoteChar the quote character.
	 * @return the protected text.
	 */
	public static String quote(String pText, char pQuoteChar)
	{
		if (pText == null)
		{
			return pText;
		}
		
		if (pText.length() == 0)
		{
			return String.valueOf(pQuoteChar) + String.valueOf(pQuoteChar);
		}
		
		StringBuilder quotedText = new StringBuilder(pText.length() + 16);
		
		quotedText.append(pQuoteChar);
		
		for (char currentChar : pText.toCharArray())
		{
			quotedText.append(currentChar);
			
			if (currentChar == pQuoteChar)
			{
				quotedText.append(currentChar);
			}
		}
		
		quotedText.append(pQuoteChar);
		
		return quotedText.toString();
	}
	
	/**
	 * Removes specific characters from the given string.
	 * 
	 * @param pText a string.
	 * @param pCharsToRemove the characters which should be removed.
	 * @return the string without specified characters.
	 */
	public static String removeCharacters(String pText, char... pCharsToRemove)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		if (pCharsToRemove == null || pCharsToRemove.length == 0)
		{
			return pText;
		}
		
		StringBuilder cleanedText = new StringBuilder(pText.length());
		
		for (char currentChar : pText.toCharArray())
		{
			if (!ArrayUtil.contains(pCharsToRemove, currentChar))
			{
				cleanedText.append(currentChar);
			}
		}
		
		return cleanedText.toString();
	}
	
	/**
	 * Removes the begin and end quote of strings, e.g. {@code 'text'} will be
	 * translated to {@code text}
	 * 
	 * @param pText the quoted text.
	 * @param pQuote the quote character e.g. {@code '} or {@code "}.
	 * @return the {@code pText} without begin and end quote.
	 */
	public static String removeQuotes(String pText, String pQuote)
	{
		return removeQuotes(pText, pQuote, pQuote);
	}
	
	/**
	 * Removes the begin and end quote of strings, e.g. {@code 'text'} will be
	 * translated to {@code text}
	 * 
	 * @param pText the quoted text.
	 * @param pStartQuote the start quote character e.g. {@code '} or {@code "}
	 *            or {@code (}.
	 * @param pEndQuote the end quote character e.g. {@code '} or {@code "} or
	 *            {@code )}.
	 * @return the {@code pText} without begin and end quote.
	 */
	public static String removeQuotes(String pText, String pStartQuote, String pEndQuote)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		int firstQuoteIndex = 0;
		int lastQuoteIndex = pText.length();
		
		if (pStartQuote != null && pStartQuote.length() > 0)
		{
			firstQuoteIndex = pText.indexOf(pStartQuote) + pStartQuote.length();
		}
		
		if (pEndQuote != null && pEndQuote.length() > 0)
		{
			lastQuoteIndex = pText.lastIndexOf(pEndQuote);
		}
		
		if (firstQuoteIndex >= 0 && lastQuoteIndex > firstQuoteIndex)
		{
			return pText.substring(firstQuoteIndex, lastQuoteIndex);
		}
		
		return pText;
	}
	
	/**
	 * Removes all the {@link Character#isWhitespace(char) whitespace
	 * characters} from the given string.
	 * 
	 * @param pString the string to clean.
	 * @return the cleaned string.
	 * @see Character#isWhitespace(char)
	 */
	public static String removeWhitespaces(String pString)
	{
		if (pString == null || pString.length() == 0)
		{
			return pString;
		}
		
		StringBuilder cleanedString = new StringBuilder(pString.length());
		
		for (char currentChar : pString.toCharArray())
		{
			if (!Character.isWhitespace(currentChar))
			{
				cleanedString.append(currentChar);
			}
		}
		
		return cleanedString.toString();
	}
	
	/**
	 * Gets a String without any white space. If the text is null an empty
	 * string is returned.
	 * 
	 * @param pText the text.
	 * @return the text without white spaces.
	 * @deprecated since 2.5, use {@link StringUtil#removeWhitespaces(String)}
	 *             instead.
	 */
	@Deprecated
	public static String removeWhiteSpaces(String pText)
	{
		return removeWhitespaces(pText);
	}
	
	/**
	 * Returns a new string resulting from replacing all occurrences of
	 * {@code pOld} in this string with {@code pNew}.
	 * 
	 * @param pText the original text.
	 * @param pOld the text to replace.
	 * @param pNew the replacement.
	 * @return the resulting string.
	 */
	public static String replace(String pText, String pOld, String pNew)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		if (pOld == null || pOld.length() == 0)
		{
			return pText;
		}
		
		if (pNew == null)
		{
			pNew = "";
		}
		
		int index = pText.indexOf(pOld);
		
		if (index < 0)
		{
			return pText; // Nothing to replace, so return original String
		}
		
		int oldLength = pOld.length();
		
		StringBuilder replacedText = new StringBuilder(pText.length() + Math.max(0, pNew.length() - oldLength) * 10);
		
		int start = 0;
		
		do
		{
			replacedText.append(pText.substring(start, index));
			replacedText.append(pNew);
			
			start = index + oldLength;
			index = pText.indexOf(pOld, start);
		}
		while (index >= 0);
		
		replacedText.append(pText.substring(start));
		
		return replacedText.toString();
	}
	
	/**
	 * Replaces all placeholders with the values from the defined property. Use
	 * following syntax: ${sys:propertyname} (replacement with the system
	 * property), ${env:parametername} (replacement with the environment
	 * parameter) or ${name} (replacement with the system property or the
	 * environment parameter if system property wasn't found).
	 * 
	 * @param pValue the value with or without placeholders
	 * @return the value with replaced placeholders
	 */
	public static String replacePlaceholder(String pValue)
	{
		if (pValue != null)
		{
			int iStart = pValue.indexOf("${");
			
			if (iStart < 0)
			{
				return pValue;
			}
			
			StringBuilder sbNewValue = new StringBuilder(pValue);
			
			String sFoundValue;
			String sFoundParam;
			String sFoundParamPrefix;
			String sFoundParamPostfix;
			
			int iPrefixStart;
			int iEnd = 0;
			
			while (iStart >= 0
					&& iEnd != -1)
			{
				iEnd = sbNewValue.indexOf("}", iStart + 2);
				
				if (iEnd > iStart)
				{
					sFoundParam = sbNewValue.substring(iStart + 2, iEnd);
					
					if (sFoundParam.length() > 0)
					{
						sFoundParamPrefix = null;
						sFoundParamPostfix = null;
						sFoundValue = null;
						
						iPrefixStart = sFoundParam.indexOf(':');
						
						if (iPrefixStart >= 0)
						{
							sFoundParamPrefix = sFoundParam.substring(0, iPrefixStart);
							sFoundParamPostfix = sFoundParam.substring(iPrefixStart + 1);
						}
						
						if ("sys".equalsIgnoreCase(sFoundParamPrefix))
						{
							sFoundValue = System.getProperty(sFoundParamPostfix);
						}
						else if ("env".equalsIgnoreCase(sFoundParamPrefix))
						{
							sFoundValue = System.getenv(sFoundParamPostfix);
						}
						else
						{
							sFoundValue = System.getProperty(sFoundParam);
							
							if (sFoundValue == null)
							{
								sFoundValue = System.getenv(sFoundParam);
							}
						}
						
						if (sFoundValue != null)
						{
							sbNewValue = sbNewValue.replace(iStart, iEnd + 1, sFoundValue);
							
							iEnd = iStart + sFoundValue.length() - 1;
						}
					}
				}
				
				iStart = sbNewValue.indexOf("${", iEnd + 1);
			}
			
			return sbNewValue.toString();
		}
		
		return pValue;
	}
	
    /**
     * Pads the given string on the right side with spaces until it has the
     * specified length.
     * 
     * @param pText the text to format.
     * @param pLength the length in characters to pad right.
     * @return the right padded String.
     * @since 2.5
     */
    public static String rpad(String pText, int pLength)
    {
        return rpad(pText, pLength, ' ');
    }
	
    /**
     * Pads the given string on the right side with given character until it has the
     * specified length.
     * 
     * @param pText the text to format.
     * @param pLength the length in characters to pad right.
     * @param pChar the fill character.
     * @return the right padded String.
     * @since 2.6
     */
	public static String rpad(String pText, int pLength, char pChar)
	{
		if (pText == null || pText.length() >= pLength)
		{
			return pText;
		}
		
		StringBuilder paddedText = new StringBuilder(pLength);
		
		paddedText.append(pText);
		
		int paddingLength = pLength - pText.length();
		
		for (int counter = 0; counter < paddingLength; counter++)
		{
			paddedText.append(pChar);
		}
		
		return paddedText.toString();
	}
	
	/**
	 * Removes all whitespaces after the last non whitespace character.
	 * 
	 * @param pText the text.
	 * @return the trimmed text.
	 */
	public static String rtrim(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		for (int index = pText.length() - 1; index >= 0; index--)
		{
			if (!Character.isWhitespace(pText.charAt(index)))
			{
				return pText.substring(0, index + 1);
			}
		}
		
		return "";
	}
	
	/**
	 * Sanitizes the given input so that it can be used as ID (for example in a
	 * HTML document).
	 * 
	 * The definition of "sanitize" in this case is taken from the HTML 4 spec:
	 * <blockquote> ID and NAME tokens must begin with a letter ([A-Za-z]) and
	 * may be followed by any number of letters, digits ([0-9]), hyphens ("-"),
	 * underscores ("_"), colons (":"), and periods ("."). </blockquote>
	 * 
	 * @param pId the id to sanitize.
	 * @return the sanitized id. {@code null} if the given id was null.
	 */
	public static String sanitizeId(String pId)
	{
		// Not using StringUtil.isEmty(String) here because we do want
		// to process strings that only contain whitespaces.
		if (pId == null || pId.length() == 0)
		{
			return pId;
		}
		
		StringBuilder sanitizedId = new StringBuilder();
		
		boolean startsWithLetter = false;
		
		for (char currentChar : pId.toCharArray())
		{
			// RegEx representation: [a-zA-Z0-9\-:._]
			if ((currentChar >= 'A' && currentChar <= 'Z')
					|| (currentChar >= 'a' && currentChar <= 'z')
					|| ((Character.isDigit(currentChar)
							|| currentChar == '-'
							|| currentChar == ':'
							|| currentChar == '.'
							|| currentChar == '_') && startsWithLetter))
			{
				startsWithLetter = true;
				sanitizedId.append(currentChar);
			}
		}
		
		return sanitizedId.toString();
	}
	
	/**
	 * Separates the given text in parts.
	 * 
	 * @param pText the text.
	 * @param pStartDelimiter the start delimiter.
	 * @param pEndDelimiter the end delimiter.
	 * @param pIncludeDelimiter {@code true} to include the delimiters in the
	 *            result for every found part.
	 * @return all found parts.
	 */
	public static ArrayUtil<String> separate(String pText, String pStartDelimiter, String pEndDelimiter, boolean pIncludeDelimiter)
	{
		if (pText == null || pText.length() == 0)
		{
			return new ArrayUtil<String>();
		}
		
		if (pStartDelimiter == null || pStartDelimiter.length() == 0
				|| pEndDelimiter == null || pEndDelimiter.length() == 0)
		{
			return new ArrayUtil<String>(pText);
		}
		
		ArrayUtil<String> values = new ArrayUtil<String>();
		
		int startIndex = pText.indexOf(pStartDelimiter, 0);
		int endIndex = 0;
		
		while (startIndex >= 0 && endIndex >= 0)
		{
			endIndex = pText.indexOf(pEndDelimiter, startIndex);
			
			if (endIndex >= 0)
			{
				if (pIncludeDelimiter)
				{
					values.add(pText.substring(startIndex, endIndex + 1));
				}
				else
				{
					values.add(pText.substring(startIndex + 1, endIndex));
				}
				
				startIndex = endIndex + pEndDelimiter.length();
			}
			
			startIndex = pText.indexOf(pStartDelimiter, startIndex);
		}
		
		// If the last delimiter is missing, we'll simply add the rest as tag.
		if (startIndex >= 0 && endIndex < 0)
		{
			if (pIncludeDelimiter)
			{
				values.add(pText.substring(startIndex) + pEndDelimiter);
			}
			else
			{
				values.add(pText.substring(startIndex + 1));
			}
		}
		
		return values;
	}
	
	/**
	 * Separates a string of values with a configurable delimiter.
	 * 
	 * @param pList string with values.
	 * @param pDelimiter delimiter to separate.
	 * @param pTrim true to trim the separated values.
	 * @return list of separated values.
	 */
	public static ArrayUtil<String> separateList(String pList, String pDelimiter, boolean pTrim)
	{
		if (pList == null)
		{
			return new ArrayUtil<String>();
		}
		
		if (pList.length() == 0)
		{
			return new ArrayUtil<String>(pList);
		}
		
		if (pDelimiter == null || pDelimiter.length() == 0)
		{
			if (pTrim)
			{
				return new ArrayUtil<String>(pList.trim());
			}
			else
			{
				return new ArrayUtil<String>(pList);
			}
		}
		
		ArrayUtil<String> values = new ArrayUtil<String>();
		
		int startIndex = 0;
		int endIndex = pList.indexOf(pDelimiter, startIndex);
		
		while (endIndex >= 0)
		{
			String part = pList.substring(startIndex, endIndex);
			
			if (pTrim)
			{
				part = part.trim();
			}
			
			values.add(part);
			
			startIndex = endIndex + pDelimiter.length();
			endIndex = pList.indexOf(pDelimiter, startIndex);
		}
		
		// If the last delimiter is missing, we'll simply add the rest as tag.
		if (startIndex >= 0 && startIndex < pList.length() && endIndex < 0)
		{
			String part = pList.substring(startIndex);
			
			if (pTrim)
			{
				part = part.trim();
			}
			
			values.add(part);
		}
		
		return values;
	}
	
	/**
	 * Strips any tags from the given text.
	 * 
	 * This does only remove the tags from the string, it does not perform any
	 * sort of parsing or similar, so it does not check for any sort of
	 * malformed syntax and simply removes everything that is surrounded by
	 * &lt;&gt;.
	 * 
	 * Examples:
	 * 
	 * <pre>
	 * &lt;html&gt;Something&lt;/html&gt;					Something
	 * &lt;b&gt;Some&lt;/b&gt;thing&lt;/b&gt;					Something
	 * &lt;html&gt;This is&lt;br&gt;a&lt;br&gt;&lt;br&gt;new line test.&lt;/html&gt;		This isanew line test.
	 * </pre>
	 * 
	 * @param pText the test from which to strip all HTML tags.
	 * @return the given text without HTML tags.
	 */
	public static String stripTags(String pText)
	{
		if (pText == null || pText.length() == 0)
		{
			return pText;
		}
		
		StringBuilder strippedText = new StringBuilder(pText.length());
		
		boolean inTag = false;
		int lastTagStartIndex = 0;
		
		for (int index = 0, length = pText.length(); index < length; index++)
		{
			char ch = pText.charAt(index);
			
			if (ch == '<')
			{
				if (!inTag)
				{
					inTag = true;
				}
				else
				{
					index = lastTagStartIndex;
					strippedText.append('<');
					inTag = false;
				}
				
				lastTagStartIndex = index;
			}
			else if (ch == '>' && inTag)
			{
				inTag = false;
			}
			else if (!inTag)
			{
				strippedText.append(ch);
			}
		}
		
		return strippedText.toString();
	}
	
	/**
	 * Returns a string representation of the "deep contents" of the specified
	 * object. If the object contains other objects as elements, the string
	 * representation contains their contents and so on. This method is designed
	 * for converting multidimensional arrays, Collections and Maps to strings.
	 * <p>
	 * The string representation consists of a list of the object's elements,
	 * enclosed in brackets (<code>"[]"</code> or <code>"{}"</code>). Adjacent elements
	 * are separated by the characters <code>", "</code> (a comma followed by a
	 * space).
	 * <p>
	 * To avoid infinite recursion, if the specified object contains itself as
	 * an element, or contains an indirect reference to itself through one or
	 * more levels of arrays, the self-reference is converted to the string
	 * <code>"#REF#"</code>. For example, an array containing only a reference to
	 * itself would be rendered as <code>"[#REF#]"</code>.
	 * <p>
	 * This method returns <code>"null"</code> if the specified object is
	 * <code>null</code>.
	 *
	 * @param pObject the object whose string representation to return
	 * @return a string representation of <code>pObject</code>
	 */
	public static String toString(Object pObject)
	{
		return toString(pObject, -1);
	}
	
	/**
	 * Returns a string representation of the "deep contents" of the specified
	 * object. If the object contains other objects as elements, the string
	 * representation contains their contents and so on. This method is designed
	 * for converting multidimensional arrays, Collections and Maps to strings.
	 * <p>
	 * The string representation consists of a list of the object's elements,
	 * enclosed in brackets (<code>"[]"</code> or <code>"{}"</code>). Adjacent elements
	 * are separated by the characters <code>", "</code> (a comma followed by a
	 * space).
	 * <p>
	 * To avoid infinite recursion, if the specified object contains itself as
	 * an element, or contains an indirect reference to itself through one or
	 * more levels of arrays, the self-reference is converted to the string
	 * <code>"#REF#"</code>. For example, an array containing only a reference to
	 * itself would be rendered as <code>"[#REF#]"</code>.
	 * <p>
	 * This method returns <code>"null"</code> if the specified object is
	 * <code>null</code>.
	 *
	 * @param pObject the object whose string representation to return
	 * @param pMaxArrayLength the maximum length of arrays. If the length of an
	 *            array is exceeded, the elements won't be printed. Instead a
	 *            placeholder like byte[n] will be printed.
	 * @return a string representation of <code>pObject</code>
	 */
	public static String toString(Object pObject, int pMaxArrayLength)
	{
		if (pObject == null)
		{
			return "null";
		}
		
		StringBuilder string = new StringBuilder();
		
		deepToString(pObject, string, new IdentityHashMap<Object, Object>(), pMaxArrayLength);
		
		return string.toString();
	}
	
	/**
	 * Converts the given name to a human readable text.
	 * 
	 * @param pName the name to convert.
	 * @param pReplaceSpecialCharacterSequences if special character sequences
	 *            should be replaced.
	 * @param pGroupUppercaseLetters if upper case letters should be group
	 *            together.
	 * @return the human readble name.
	 */
	private static String convertNameToText(String pName, boolean pReplaceSpecialCharacterSequences, boolean pGroupUppercaseLetters)
	{
		if (pName == null || pName.length() == 0)
		{
			return pName;
		}
		
		StringBuilder humanReadableName = new StringBuilder(pName.length() + 16);
		
		boolean bFirstCharacterFound = false;
		boolean bAppendCharacter;
		
		for (int index = 0, cnt = pName.length(); index < cnt; index++)
		{
			int originalIndex = index;
			
			char currentChar = pName.charAt(index);
			
			bAppendCharacter = true;
			
			// Replace the character (as necessary) before we do anything else.
			if (pReplaceSpecialCharacterSequences)
			{
				Character replaceCharacter = reverseCharacterReplacements.get(String.valueOf(currentChar));
				
				if (replaceCharacter != null)
				{
					currentChar = replaceCharacter.charValue();
				}
				else if (index < pName.length() - 1)
				{
					replaceCharacter = reverseCharacterReplacements.get(pName.substring(index, index + 2));
					
					if (replaceCharacter != null)
					{
						currentChar = replaceCharacter.charValue();
						index++;
					}
				}
			}
			
			if (!bFirstCharacterFound && Character.isLetter(currentChar))
			{
				// Make the first character uppercase.
				currentChar = Character.toUpperCase(currentChar);
				bFirstCharacterFound = true;
			}
			else if (Character.isUpperCase(currentChar) && index > 0)
			{
				// Check if we should include a space before the uppercase
				// letter.
				char previousChar = pName.charAt(originalIndex - 1);
				
				if ((Character.isUpperCase(previousChar) && !pGroupUppercaseLetters)
						|| Character.isLowerCase(previousChar)
						|| Character.isDigit(previousChar))
				{
					humanReadableName.append(" ");
				}
			}
			else if (currentChar == '_')
			{
			    bAppendCharacter = false;

                if (bFirstCharacterFound)
                {
                    // Only append a space if we already have found the first
                    // character.
                    humanReadableName.append(" ");
                }
                else
                {
                    if (index + 1 < cnt)
                    {
                        //we don't uppercase the character, if a trailing '_' sequence was before the first character 
                        
                        // Forward to the next character.
                        currentChar = pName.charAt(index + 1);

                        // Set if we have found the first character. This is the
                        // logic which handles a leading underscore.
                        bFirstCharacterFound = Character.isLetter(currentChar);
                    }
                }
			}
			
			if (bAppendCharacter)
			{
			    humanReadableName.append(currentChar);
			}
		}
		
		return humanReadableName.toString();
	}
	
	/**
	 * Returns a string representation of the "deep contents" of the specified
	 * object. If the object contains other objects as elements, the string
	 * representation contains their contents and so on. This method is designed
	 * for converting multidimensional arrays, Collections and Maps to strings.
	 * <p>
	 * The string representation consists of a list of the object's elements,
	 * enclosed in brackets (<code>"[]"</code> or <code>"{}"</code>). Adjacent elements
	 * are separated by the characters <code>", "</code> (a comma followed by a
	 * space).
	 * <p>
	 * To avoid infinite recursion, if the specified object contains itself as
	 * an element, or contains an indirect reference to itself through one or
	 * more levels of arrays, the self-reference is converted to the string
	 * <code>"#REF#"</code>. For example, an array containing only a reference to
	 * itself would be rendered as <code>"[#REF#]"</code>.
	 * <p>
	 * This method returns <code>"null"</code> if the specified object is
	 * <code>null</code>.
	 *
	 * @param pObject the object whose string representation to return
	 * @param pBuffer the output buffer for recursive calls instead of a return
	 *            value
	 * @param pSelfCache the object cache to avoid recursion
	 * @param pMaxArrayLength the maximum length of arrays. If the length of an
	 *            array is exceeded, the elements won't be printed. Instead a
	 *            placeholder like #byte[n]# will be printed.
	 */
	private static void deepToString(Object pObject, StringBuilder pBuffer, IdentityHashMap<Object, Object> pSelfCache, int pMaxArrayLength)
	{
		if (pObject == null)
		{
			pBuffer.append("null");
			return;
		}
		
		if (pSelfCache.containsKey(pObject))
		{
			pBuffer.append("#REF#");
			return;
		}
		
		pSelfCache.put(pObject, Boolean.TRUE);
		
		if (pObject instanceof Iterator<?>)
		{
			Iterator<?> iterator = (Iterator<?>)pObject;
			
			pBuffer.append('[');
			
			if (iterator.hasNext())
			{
				while (iterator.hasNext())
				{
					deepToString(iterator.next(), pBuffer, pSelfCache, pMaxArrayLength);
					pBuffer.append(", ");
				}
				
				pBuffer.delete(pBuffer.length() - 2, pBuffer.length());
			}
			
			pBuffer.append(']');
		}
		else if (pObject instanceof Enumeration<?>)
		{
			Enumeration<?> enumeration = (Enumeration<?>)pObject;
			
			pBuffer.append('[');
			
			if (enumeration.hasMoreElements())
			{
				while (enumeration.hasMoreElements())
				{
					deepToString(enumeration.nextElement(), pBuffer, pSelfCache, pMaxArrayLength);
					pBuffer.append(", ");
				}
				
				pBuffer.delete(pBuffer.length() - 2, pBuffer.length());
			}
			
			pBuffer.append(']');
		}
		else if (pObject instanceof Iterable<?>)
		{
			pBuffer.append('[');
			
			boolean hadItem = false;
			
			for (Object object : (Iterable<?>)pObject)
			{
				hadItem = true;
				
				deepToString(object, pBuffer, pSelfCache, pMaxArrayLength);
				pBuffer.append(", ");
			}
			
			if (hadItem)
			{
				pBuffer.delete(pBuffer.length() - 2, pBuffer.length());
			}
			
			pBuffer.append(']');
		}
		else if (pObject instanceof Map<?, ?>)
		{
			Map<?, ?> map = (Map<?, ?>)pObject;
			
			pBuffer.append('{');
			
			int i = 0;
			
			for (Map.Entry<?, ?> entry : map.entrySet())
			{
				if (i != 0)
				{
					pBuffer.append(", ");
				}
				
				pBuffer.append('[');
				deepToString(entry.getKey(), pBuffer, pSelfCache, pMaxArrayLength);
				pBuffer.append(", ");
				deepToString(entry.getValue(), pBuffer, pSelfCache, pMaxArrayLength);
				pBuffer.append(']');
				
				i++;
			}
			
			pBuffer.append('}');
		}
		else if (pObject instanceof Dictionary<?, ?>)
		{
			Dictionary<?, ?> dict = (Dictionary<?, ?>)pObject;
			
			pBuffer.append('{');
			
			int i = 0;
			
			Object oKey;
			
			for (Enumeration<?> enKeys = dict.keys(); enKeys.hasMoreElements();)
			{
				oKey = enKeys.nextElement();
				
				if (i != 0)
				{
					pBuffer.append(", ");
				}
				
				pBuffer.append('[');
				deepToString(oKey, pBuffer, pSelfCache, pMaxArrayLength);
				pBuffer.append(", ");
				deepToString(dict.get(oKey), pBuffer, pSelfCache, pMaxArrayLength);
				pBuffer.append(']');
				
				i++;
			}
			
			pBuffer.append('}');
		}
		else
		{
			Class eClass = pObject.getClass();
			
			if (eClass.isArray())
			{
				pBuffer.append('[');
				
				if (eClass == byte[].class)
				{
					if (pMaxArrayLength >= 0 && ((byte[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#byte[");
						pBuffer.append(((byte[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((byte[])pObject));
					}
				}
				else if (eClass == short[].class)
				{
					if (pMaxArrayLength >= 0 && ((short[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#short[");
						pBuffer.append(((short[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((short[])pObject));
					}
				}
				else if (eClass == int[].class)
				{
					if (pMaxArrayLength >= 0 && ((int[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#int[");
						pBuffer.append(((int[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((int[])pObject));
					}
				}
				else if (eClass == long[].class)
				{
					if (pMaxArrayLength >= 0 && ((long[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#long[");
						pBuffer.append(((long[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((long[])pObject));
					}
				}
				else if (eClass == char[].class)
				{
					if (pMaxArrayLength >= 0 && ((char[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#char[");
						pBuffer.append(((char[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((char[])pObject));
					}
				}
				else if (eClass == float[].class)
				{
					if (pMaxArrayLength >= 0 && ((float[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#float[");
						pBuffer.append(((float[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((float[])pObject));
					}
				}
				else if (eClass == double[].class)
				{
					if (pMaxArrayLength >= 0 && ((short[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#double[");
						pBuffer.append(((double[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((double[])pObject));
					}
				}
				else if (eClass == boolean[].class)
				{
					if (pMaxArrayLength >= 0 && ((boolean[])pObject).length > pMaxArrayLength)
					{
						pBuffer.append("#boolean[");
						pBuffer.append(((boolean[])pObject).length);
						pBuffer.append("]#");
					}
					else
					{
						pBuffer.append(Arrays.toString((boolean[])pObject));
					}
				}
				else
				{
					// element is an array of object references
					Object[] array = (Object[])pObject;
					
					if (pMaxArrayLength >= 0 && array.length > pMaxArrayLength)
					{
						pBuffer.append("#Object[");
						pBuffer.append(array.length);
						pBuffer.append("]#");
					}
					else
					{
						for (int i = 0; i < array.length; i++)
						{
							if (i != 0)
							{
								pBuffer.append(", ");
							}
							
							deepToString(array[i], pBuffer, pSelfCache, pMaxArrayLength);
						}
					}
				}
				
				pBuffer.append(']');
			}
			else
			{
				pBuffer.append(pObject.toString());
			}
		}
		
		pSelfCache.remove(pObject);
	}
	
	/**
	 * Sub function of like. For performance reasons, the original Strings are
	 * not modified, The start and end defines the region to search.
	 * 
	 * @param pSource the source string.
	 * @param pSrcStart the start index of the source region.
	 * @param pSrcEnd the end index of the source region.
	 * @param pSearch the search string.
	 * @param pStart the start index of the search region.
	 * @param pEnd the end index of the search region.
	 * @return true, if the like matches.
	 */
	private static boolean like(String pSource, int pSrcStart, int pSrcEnd, String pSearch, int pStart, int pEnd)
	{
		int pos = pSrcStart;
		for (int i = pStart; i < pEnd; i++)
		{
			char ch = pSearch.charAt(i);
			if (ch == '*')
			{
				if (i == pEnd - 1)
				{
					return true;
				}
				int nStart = i + 1;
				while (pos <= pSrcEnd)
				{
					if (like(pSource, pos, pSrcEnd, pSearch, nStart, pEnd))
					{
						return true;
					}
					pos++;
				}
				return false;
			}
			else if (pos == pSrcEnd)
			{
				return false;
			}
			else
			{
				if (ch != '?' && ch != pSource.charAt(pos))
				{
					return false;
				}
				pos++;
			}
		}
		return pos == pSrcEnd;
	}
	
	/**
	 * Converts a simple text to a text with space separated "words". A word will be detected
	 * if a letter is upper-case and the next letter is lower-case.
	 * 
	 * @param pText the text to check
	 * @return the space separated words
	 */
	public static String convertSpaceSeparated(String pText)
	{
		return convertSpaceSeparated(pText, 0);
	}
	
	/**
	 * Converts a simple text to a text with space separated "words". A word will be detected
	 * if a letter is upper-case and the next letter is lower-case.
	 * 
	 * @param pText the text to check
	 * @param pMinLength the minimal text length to start separation. If text is shorter than this threshold,
	 *                   the text will remain unchanged
	 * @return the space separated words
	 */
	public static String convertSpaceSeparated(String pText, int pMinLength)
	{
		if (pText == null)
		{
			return null;
		}
		
		if (pMinLength >= 0 && pText.length() < pMinLength)
		{
			return pText;
		}
		
		StringBuilder sbText = new StringBuilder(pText.length() + 10);
		
		char ch;
		char chPrevious = 0;
		
		boolean bUpperCase;
		
		for (int i = 0, cnt = pText.length(); i < cnt; i++)
		{
			ch = pText.charAt(i);
			
			bUpperCase = Character.isUpperCase(ch);

			//upperCase detected and not first character and not already added a space
			//(if space was previous character, don't add another one)
			if (bUpperCase && i > 0 && chPrevious != ' ')
			{
				//default check: 'BBe' -> 'B Be'
				if (i < cnt - 1 && Character.isLowerCase(pText.charAt(i + 1)))
				{
					sbText.append(' ');
				}
				//last character check: 'eB' -> 'e B'
				else if (i == cnt - 1 && Character.isLowerCase(pText.charAt(i - 1)))
				{
					sbText.append(' ');
				}
			}
			
			// Replace the character (as necessary)
			String replaceString = characterReplacements.get(Character.valueOf(ch));
			
			if (replaceString != null)
			{
				sbText.append(replaceString);
				
				chPrevious = replaceString.charAt(replaceString.length() - 1); 
			}
			else
			{
				sbText.append(ch);

				chPrevious = ch;
			}
		}
		
		return sbText.toString();
	}
	
	/**
	 * Gets an alternative value for an <code>empty</code> string.
	 * 
	 * @param pValue desired value
	 * @param pEValue alternative value 
	 * @return <code>pValue</code> or <code>pEValue</code> if <code>isEmpty(pValue)</code>
	 */
	public static String evl(String pValue, String pEValue)
	{
		if (isEmpty((String)pValue))
		{
			return pEValue;
		}
		
		return pValue;
	}	
	
	/**
	 * Creates a "random" text.
	 * 
	 * @param pAllowedChars the allowed code characters
	 * @param pLength the text length
	 * @param pMaxOptional if <code>&gt; 0</code> a random number of max characters will be added to the length
	 * @return a new "random" text
	 */
	public static String createRandomText(String pAllowedChars, int pLength, int pMaxOptional)
	{
		SecureRandom rand = new SecureRandom();
		
		StringBuilder sbPassword = new StringBuilder();
		
		int iChars = pAllowedChars.length();
		int iLength = pLength;
		
		if (pMaxOptional > 0)
		{
			iLength += rand.nextInt(pMaxOptional);
		}

		for (int i = 0; i < iLength; i++)
		{
			sbPassword.append(pAllowedChars.charAt(rand.nextInt(iChars)));
		}
		
		return sbPassword.toString();
	}
	
	/**
	 * Converts all of the characters in given String to lower case using the rules of the default locale. 
	 * 
	 * @param pText the string
	 * @return the converted string
	 */
	public static String lowerCase(String pText)
	{
		return pText != null ? pText.toLowerCase() : null;
	}
	
	/**
	 * Converts all of the characters in given String to upper case using the rules of the default locale. 
	 * 
	 * @param pText the string
	 * @return the converted string
	 */
	public static String upperCase(String pText)
	{
		return pText != null ? pText.toUpperCase() : null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/** case senstive types. */
	public static enum CaseSensitiveType
	{
		/** all letters are lower case. */
		LowerCase,
		/** letters are upper and lower case. */
		MixedCase,
		/** no letter available. */
		NoLetter,
		/** all letters are upper case. */
		UpperCase
	}
	
	/** the character types. */
	public static enum CharacterType
	{
		/** all kind of characters. */
		All,
		/** only digits. */
		Digits,
		/** digits and no letters. */
		DigitsSpecial,
		/** only letters. */
		Letters,
		/** letters and digits. */
		LettersDigits,
		/** letters and digits and space. */
		LettersDigitsSpace,
		/** letters and digits and whitespace. */
		LettersDigitsWhitespace,
		/** letters and no digits. */
		LettersSpecial,
		/** null or empty. */
		None,
		/** no letters and no digits. */
		OnlySpecial,
		/** only whitespaces. */
		OnlyWhitespace
	}
	
	/** the text types. */
	public static enum TextType
	{
		/** only letters from A(a) to Z(z). */
		AZLetters
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return (pChar >= 'A' && pChar <= 'Z')
						|| (pChar >= 'a' && pChar <= 'z');
			}
		},
		
		/** letters from A(a) to Z(z) and digits. */
		AZLettersDigits
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return (pChar >= 'A' && pChar <= 'Z')
						|| (pChar >= 'a' && pChar <= 'z')
						|| Character.isDigit(pChar);
			}
		},
		
		/** only digits. */
		Digits
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isDigit(pChar);
			}
		},
		
		/** all characters but start from first letter. */
		FromFirstLetter
		{
			@Override
			public boolean isStartCharacter(char pChar)
			{
				return Character.isLetter(pChar);
			}
		},
		
		/** only letters. */
		Letters
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isLetter(pChar);
			}
		},
		
		/** letters and digits. */
		LettersDigits
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isLetter(pChar)
						|| Character.isDigit(pChar);
			}
		},
		
		/** letters and digits and space. */
		LettersDigitsSpace
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isLetter(pChar)
						|| Character.isDigit(pChar)
						|| Character.isSpaceChar(pChar);
			}
		},
		
		/** letters and digits and whitespace. */
		LettersDigitsWhitespace
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isLetter(pChar)
						|| Character.isDigit(pChar)
						|| Character.isWhitespace(pChar);
			}
		},
		
		/** all characters that are lowercase. */
		LowerCase
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isLowerCase(pChar);
			}
		},
		
		/** all characters that are uppercase. */
		UpperCase
		{
			@Override
			public boolean acceptCharacter(char pChar)
			{
				return Character.isUpperCase(pChar);
			}
		},
		
		/** all characters but without leading digits. */
		WithoutLeadingDigits
		{
			@Override
			public boolean isStartCharacter(char pChar)
			{
				return !Character.isDigit(pChar);
			}
		};
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Tests if this type does accept the given char.
		 * <p>
		 * This function is called on every char in a string.
		 * 
		 * @param pChar the char to test.
		 * @return {@code true} if this type does accept the given char.
		 */
		public boolean acceptCharacter(char pChar)
		{
			return true;
		}
		
		/**
		 * Tests if the given char is a valid start position for the type.
		 * <p>
		 * This function is only called as long as it does not return true. It
		 * is used to determine whether a valid start position for the type has
		 * been reached or not. As long as this function returns {@code false}
		 * {@link #acceptCharacter(char)} will not be called. If it returns
		 * {@code true} {@link #acceptCharacter(char)} will be called for every
		 * char but no longer this function.
		 * 
		 * @param pChar the char to test.
		 * @return {@code true} if the given char is a valid start position.
		 */
		public boolean isStartCharacter(char pChar)
		{
			return true;
		}
		
	}	// TextType
	
}	// StringUtil
