/*
 * Copyright 2014 SIB Visions GmbH
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
 * 27.10.2014 - [JR] - creation
 * 28.07.2016 - [JR] - escapeHtml implemented
 * 21.12.2016 - [JR] - custom request properties supported
 * 14.11.2017 - [JR] - timeout parameter introduced
 */
package com.sibvisions.util.type;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

/**
 * The <code>HttpUtil</code> is a utility class for http communication.
 * 
 * @author René Jahn
 */
public final class HttpUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** character replace map. */
    private static HashMap<Character, String> hmpReplaceHtml4 = new HashMap<Character, String>();
    
    /** The greenwich mean time timezone. */
    private static TimeZone tzGmt = TimeZone.getTimeZone("GMT");
    
    /** the date format for the http modified date. */
    private static SimpleDateFormat sdfModified;

    
    static
    {
        sdfModified = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        sdfModified.setTimeZone(tzGmt);
        
        //original source: Apache commons lang3 (https://github.com/apache/commons-lang)
        //https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/text/translate/EntityArrays.java
        
        //BASIC
        hmpReplaceHtml4.put(Character.valueOf((char)0x0022), "&quot;");      // " - double-quote
        hmpReplaceHtml4.put(Character.valueOf((char)0x0026), "&amp;");       // & - ampersand
        hmpReplaceHtml4.put(Character.valueOf((char)0x003C), "&lt;");        // < - less-than
        hmpReplaceHtml4.put(Character.valueOf((char)0x003E), "&gt;");        // > - greater-than
        
        //ISO8859-1

        hmpReplaceHtml4.put(Character.valueOf((char)0x00A0), "&nbsp;");      // &
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A1), "&iexcl;");     // inverted exclamation mark
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A2), "&cent;");      // cent sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A3), "&pound;");     // pound sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A4), "&curren;");    // currency sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A5), "&yen;");       // yen sign = yuan sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A6), "&brvbar;");    // broken bar = broken vertical bar
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A7), "&sect;");      // section sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A8), "&uml;");       // diaeresis = spacing diaeresis
        hmpReplaceHtml4.put(Character.valueOf((char)0x00A9), "&copy;");      // © - copyright sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AA), "&ordf;");      // feminine ordinal indicator
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AB), "&laquo;");     // left-pointing double angle quotation mark = left pointing guillemet
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AC), "&not;");       // not sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AD), "&shy;");       // soft hyphen = discretionary hyphen
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AE), "&reg;");       // ® - registered trademark sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00AF), "&macr;");      // macron = spacing macron = overline = APL overbar
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B0), "&deg;");       // degree sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B1), "&plusmn;");    // plus-minus sign = plus-or-minus sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B2), "&sup2;");      // superscript two = superscript digit two = squared
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B3), "&sup3;");      // superscript three = superscript digit three = cubed
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B4), "&acute;");     // acute accent = spacing acute
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B5), "&micro;");     // micro sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B6), "&para;");      // pilcrow sign = paragraph sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B7), "&middot;");    // middle dot = Georgian comma = Greek middle dot
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B8), "&cedil;");     // cedilla = spacing cedilla
        hmpReplaceHtml4.put(Character.valueOf((char)0x00B9), "&sup1;");      // superscript one = superscript digit one
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BA), "&ordm;");      // masculine ordinal indicator
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BB), "&raquo;");     // right-pointing double angle quotation mark = right pointing guillemet
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BC), "&frac14;");    // vulgar fraction one quarter = fraction one quarter
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BD), "&frac12;");    // vulgar fraction one half = fraction one half
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BE), "&frac34;");    // vulgar fraction three quarters = fraction three quarters
        hmpReplaceHtml4.put(Character.valueOf((char)0x00BF), "&iquest;");    // inverted question mark = turned question mark
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C0), "&Agrave;");    // À - uppercase A, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C1), "&Aacute;");    // Á - uppercase A, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C2), "&Acirc;");     // Â - uppercase A, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C3), "&Atilde;");    // Ã - uppercase A, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C4), "&Auml;");      // Ä - uppercase A, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C5), "&Aring;");     // Å - uppercase A, ring
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C6), "&AElig;");     // Æ - uppercase AE
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C7), "&Ccedil;");    // Ç - uppercase C, cedilla
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C8), "&Egrave;");    // È - uppercase E, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00C9), "&Eacute;");    // É - uppercase E, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CA), "&Ecirc;");     // Ê - uppercase E, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CB), "&Euml;");      // Ë - uppercase E, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CC), "&Igrave;");    // Ì - uppercase I, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CD), "&Iacute;");    // Í - uppercase I, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CE), "&Icirc;");     // Î - uppercase I, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00CF), "&Iuml;");      // Ï - uppercase I, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D0), "&ETH;");       // Ð - uppercase Eth, Icelandic
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D1), "&Ntilde;");    // Ñ - uppercase N, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D2), "&Ograve;");    // Ò - uppercase O, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D3), "&Oacute;");    // Ó - uppercase O, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D4), "&Ocirc;");     // Ô - uppercase O, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D5), "&Otilde;");    // Õ - uppercase O, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D6), "&Ouml;");      // Ö - uppercase O, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D7), "&times;");     // multiplication sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D8), "&Oslash;");    // Ø - uppercase O, slash
        hmpReplaceHtml4.put(Character.valueOf((char)0x00D9), "&Ugrave;");    // Ù - uppercase U, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DA), "&Uacute;");    // Ú - uppercase U, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DB), "&Ucirc;");     // Û - uppercase U, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DC), "&Uuml;");      // Ü - uppercase U, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DD), "&Yacute;");    // Ý - uppercase Y, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DE), "&THORN;");     // Þ - uppercase THORN, Icelandic
        hmpReplaceHtml4.put(Character.valueOf((char)0x00DF), "&szlig;");     // ß - lowercase sharps, German
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E0), "&agrave;");    // à - lowercase a, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E1), "&aacute;");    // á - lowercase a, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E2), "&acirc;");     // â - lowercase a, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E3), "&atilde;");    // ã - lowercase a, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E4), "&auml;");      // ä - lowercase a, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E5), "&aring;");     // å - lowercase a, ring
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E6), "&aelig;");     // æ - lowercase ae
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E7), "&ccedil;");    // ç - lowercase c, cedilla
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E8), "&egrave;");    // è - lowercase e, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00E9), "&eacute;");    // é - lowercase e, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00EA), "&ecirc;");     // ê - lowercase e, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00EB), "&euml;");      // ë - lowercase e, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00EC), "&igrave;");    // ì - lowercase i, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00ED), "&iacute;");    // í - lowercase i, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00EE), "&icirc;");     // î - lowercase i, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00EF), "&iuml;");      // ï - lowercase i, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F0), "&eth;");       // ð - lowercase eth, Icelandic
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F1), "&ntilde;");    // ñ - lowercase n, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F2), "&ograve;");    // ò - lowercase o, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F3), "&oacute;");    // ó - lowercase o, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F4), "&ocirc;");     // ô - lowercase o, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F5), "&otilde;");    // õ - lowercase o, tilde
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F6), "&ouml;");      // ö - lowercase o, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F7), "&divide;");    // division sign
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F8), "&oslash;");    // ø - lowercase o, slash
        hmpReplaceHtml4.put(Character.valueOf((char)0x00F9), "&ugrave;");    // ù - lowercase u, grave accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FA), "&uacute;");    // ú - lowercase u, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FB), "&ucirc;");     // û - lowercase u, circumflex accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FC), "&uuml;");      // ü - lowercase u, umlaut
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FD), "&yacute;");    // ý - lowercase y, acute accent
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FE), "&thorn;");     // þ - lowercase thorn, Icelandic
        hmpReplaceHtml4.put(Character.valueOf((char)0x00FF), "&yuml;");      // ÿ - lowercase y, umlaut
        
        //HTML4
        
        // <!-- Latin Extended-B -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0192), "&fnof;");  // latin small f with hook = function= florin, U+0192 ISOtech -->
        // <!-- Greek -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0391), "&Alpha;"); // greek capital letter alpha, U+0391 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0392), "&Beta;");  // greek capital letter beta, U+0392 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0393), "&Gamma;"); // greek capital letter gamma,U+0393 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0394), "&Delta;"); // greek capital letter delta,U+0394 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0395), "&Epsilon;");   // greek capital letter epsilon, U+0395 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0396), "&Zeta;");  // greek capital letter zeta, U+0396 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0397), "&Eta;");   // greek capital letter eta, U+0397 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0398), "&Theta;"); // greek capital letter theta,U+0398 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0399), "&Iota;");  // greek capital letter iota, U+0399 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039A), "&Kappa;"); // greek capital letter kappa, U+039A -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039B), "&Lambda;");    // greek capital letter lambda,U+039B ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039C), "&Mu;");    // greek capital letter mu, U+039C -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039D), "&Nu;");    // greek capital letter nu, U+039D -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039E), "&Xi;");    // greek capital letter xi, U+039E ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x039F), "&Omicron;");   // greek capital letter omicron, U+039F -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A0), "&Pi;");    // greek capital letter pi, U+03A0 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A1), "&Rho;");   // greek capital letter rho, U+03A1 -->
        // <!-- there is no Sigmaf, and no U+03A2 character either -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A3), "&Sigma;"); // greek capital letter sigma,U+03A3 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A4), "&Tau;");   // greek capital letter tau, U+03A4 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A5), "&Upsilon;");   // greek capital letter upsilon,U+03A5 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A6), "&Phi;");   // greek capital letter phi,U+03A6 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A7), "&Chi;");   // greek capital letter chi, U+03A7 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A8), "&Psi;");   // greek capital letter psi,U+03A8 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03A9), "&Omega;"); // greek capital letter omega,U+03A9 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B1), "&alpha;"); // greek small letter alpha,U+03B1 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B2), "&beta;");  // greek small letter beta, U+03B2 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B3), "&gamma;"); // greek small letter gamma,U+03B3 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B4), "&delta;"); // greek small letter delta,U+03B4 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B5), "&epsilon;");   // greek small letter epsilon,U+03B5 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B6), "&zeta;");  // greek small letter zeta, U+03B6 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B7), "&eta;");   // greek small letter eta, U+03B7 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B8), "&theta;"); // greek small letter theta,U+03B8 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03B9), "&iota;");  // greek small letter iota, U+03B9 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BA), "&kappa;"); // greek small letter kappa,U+03BA ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BB), "&lambda;");    // greek small letter lambda,U+03BB ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BC), "&mu;");    // greek small letter mu, U+03BC ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BD), "&nu;");    // greek small letter nu, U+03BD ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BE), "&xi;");    // greek small letter xi, U+03BE ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03BF), "&omicron;");   // greek small letter omicron, U+03BF NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C0), "&pi;");    // greek small letter pi, U+03C0 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C1), "&rho;");   // greek small letter rho, U+03C1 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C2), "&sigmaf;");    // greek small letter final sigma,U+03C2 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C3), "&sigma;"); // greek small letter sigma,U+03C3 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C4), "&tau;");   // greek small letter tau, U+03C4 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C5), "&upsilon;");   // greek small letter upsilon,U+03C5 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C6), "&phi;");   // greek small letter phi, U+03C6 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C7), "&chi;");   // greek small letter chi, U+03C7 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C8), "&psi;");   // greek small letter psi, U+03C8 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03C9), "&omega;"); // greek small letter omega,U+03C9 ISOgrk3 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03D1), "&thetasym;");  // greek small letter theta symbol,U+03D1 NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03D2), "&upsih;"); // greek upsilon with hook symbol,U+03D2 NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x03D6), "&piv;");   // greek pi symbol, U+03D6 ISOgrk3 -->
        // <!-- General Punctuation -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2022), "&bull;");  // bullet = black small circle,U+2022 ISOpub -->
        // <!-- bullet is NOT the same as bullet operator, U+2219 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2026), "&hellip;");    // horizontal ellipsis = three dot leader,U+2026 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2032), "&prime;"); // prime = minutes = feet, U+2032 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2033), "&Prime;"); // double prime = seconds = inches,U+2033 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x203E), "&oline;"); // overline = spacing overscore,U+203E NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2044), "&frasl;"); // fraction slash, U+2044 NEW -->
        // <!-- Letterlike Symbols -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2118), "&weierp;");    // script capital P = power set= Weierstrass p, U+2118 ISOamso -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2111), "&image;"); // blackletter capital I = imaginary part,U+2111 ISOamso -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x211C), "&real;");  // blackletter capital R = real part symbol,U+211C ISOamso -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2122), "&trade;"); // trade mark sign, U+2122 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2135), "&alefsym;");   // alef symbol = first transfinite cardinal,U+2135 NEW -->
        // <!-- alef symbol is NOT the same as hebrew letter alef,U+05D0 although the
        // same glyph could be used to depict both characters -->
        // <!-- Arrows -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2190), "&larr;");  // leftwards arrow, U+2190 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2191), "&uarr;");  // upwards arrow, U+2191 ISOnum-->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2192), "&rarr;");  // rightwards arrow, U+2192 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2193), "&darr;");  // downwards arrow, U+2193 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2194), "&harr;");  // left right arrow, U+2194 ISOamsa -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21B5), "&crarr;"); // downwards arrow with corner leftwards= carriage return, U+21B5 NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21D0), "&lArr;");  // leftwards double arrow, U+21D0 ISOtech -->
        // <!-- ISO 10646 does not say that lArr is the same as the 'is implied by'
        // arrow but also does not have any other character for that function.
        // So ? lArr canbe used for 'is implied by' as ISOtech suggests -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21D1), "&uArr;");  // upwards double arrow, U+21D1 ISOamsa -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21D2), "&rArr;");  // rightwards double arrow,U+21D2 ISOtech -->
        // <!-- ISO 10646 does not say this is the 'implies' character but does not
        // have another character with this function so ?rArr can be used for
        // 'implies' as ISOtech suggests -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21D3), "&dArr;");  // downwards double arrow, U+21D3 ISOamsa -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x21D4), "&hArr;");  // left right double arrow,U+21D4 ISOamsa -->
        // <!-- Mathematical Operators -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2200), "&forall;");    // for all, U+2200 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2202), "&part;");  // partial differential, U+2202 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2203), "&exist;"); // there exists, U+2203 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2205), "&empty;"); // empty set = null set = diameter,U+2205 ISOamso -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2207), "&nabla;"); // nabla = backward difference,U+2207 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2208), "&isin;");  // element of, U+2208 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2209), "&notin;"); // not an element of, U+2209 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x220B), "&ni;");    // contains as member, U+220B ISOtech -->
        // <!-- should there be a more memorable name than 'ni'? -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x220F), "&prod;");  // n-ary product = product sign,U+220F ISOamsb -->
        // <!-- prod is NOT the same character as U+03A0 'greek capital letter pi'
        // though the same glyph might be used for both -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2211), "&sum;");   // n-ary summation, U+2211 ISOamsb -->
        // <!-- sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2212), "&minus;"); // minus sign, U+2212 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2217), "&lowast;");    // asterisk operator, U+2217 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x221A), "&radic;"); // square root = radical sign,U+221A ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x221D), "&prop;");  // proportional to, U+221D ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x221E), "&infin;"); // infinity, U+221E ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2220), "&ang;");   // angle, U+2220 ISOamso -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2227), "&and;");   // logical and = wedge, U+2227 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2228), "&or;");    // logical or = vee, U+2228 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2229), "&cap;");   // intersection = cap, U+2229 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x222A), "&cup;");   // union = cup, U+222A ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x222B), "&int;");   // integral, U+222B ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2234), "&there4;");    // therefore, U+2234 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x223C), "&sim;");   // tilde operator = varies with = similar to,U+223C ISOtech -->
        // <!-- tilde operator is NOT the same character as the tilde, U+007E,although
        // the same glyph might be used to represent both -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2245), "&cong;");  // approximately equal to, U+2245 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2248), "&asymp;"); // almost equal to = asymptotic to,U+2248 ISOamsr -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2260), "&ne;");    // not equal to, U+2260 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2261), "&equiv;"); // identical to, U+2261 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2264), "&le;");    // less-than or equal to, U+2264 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2265), "&ge;");    // greater-than or equal to,U+2265 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2282), "&sub;");   // subset of, U+2282 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2283), "&sup;");   // superset of, U+2283 ISOtech -->
        // <!-- note that nsup, 'not a superset of, U+2283' is not covered by the
        // Symbol font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn -->,
        hmpReplaceHtml4.put(Character.valueOf((char)0x2284), "&nsub;");  // not a subset of, U+2284 ISOamsn -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2286), "&sube;");  // subset of or equal to, U+2286 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2287), "&supe;");  // superset of or equal to,U+2287 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2295), "&oplus;"); // circled plus = direct sum,U+2295 ISOamsb -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2297), "&otimes;");    // circled times = vector product,U+2297 ISOamsb -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x22A5), "&perp;");  // up tack = orthogonal to = perpendicular,U+22A5 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x22C5), "&sdot;");  // dot operator, U+22C5 ISOamsb -->
        // <!-- dot operator is NOT the same character as U+00B7 middle dot -->
        // <!-- Miscellaneous Technical -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2308), "&lceil;"); // left ceiling = apl upstile,U+2308 ISOamsc -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2309), "&rceil;"); // right ceiling, U+2309 ISOamsc -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x230A), "&lfloor;");    // left floor = apl downstile,U+230A ISOamsc -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x230B), "&rfloor;");    // right floor, U+230B ISOamsc -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2329), "&lang;");  // left-pointing angle bracket = bra,U+2329 ISOtech -->
        // <!-- lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation
        // mark' -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x232A), "&rang;");  // right-pointing angle bracket = ket,U+232A ISOtech -->
        // <!-- rang is NOT the same character as U+003E 'greater than' or U+203A
        // 'single right-pointing angle quotation mark' -->
        // <!-- Geometric Shapes -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x25CA), "&loz;");   // lozenge, U+25CA ISOpub -->
        // <!-- Miscellaneous Symbols -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2660), "&spades;");    // black spade suit, U+2660 ISOpub -->
        // <!-- black here seems to mean filled as opposed to hollow -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2663), "&clubs;"); // black club suit = shamrock,U+2663 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2665), "&hearts;");    // black heart suit = valentine,U+2665 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2666), "&diams;"); // black diamond suit, U+2666 ISOpub -->

        // <!-- Latin Extended-A -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0152), "&OElig;"); // -- latin capital ligature OE,U+0152 ISOlat2 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0153), "&oelig;"); // -- latin small ligature oe, U+0153 ISOlat2 -->
        // <!-- ligature is a misnomer, this is a separate character in some languages -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0160), "&Scaron;");    // -- latin capital letter S with caron,U+0160 ISOlat2 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0161), "&scaron;");    // -- latin small letter s with caron,U+0161 ISOlat2 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x0178), "&Yuml;");  // -- latin capital letter Y with diaeresis,U+0178 ISOlat2 -->
        // <!-- Spacing Modifier Letters -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x02C6), "&circ;");  // -- modifier letter circumflex accent,U+02C6 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x02DC), "&tilde;"); // small tilde, U+02DC ISOdia -->
        // <!-- General Punctuation -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2002), "&ensp;");  // en space, U+2002 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2003), "&emsp;");  // em space, U+2003 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2009), "&thinsp;");    // thin space, U+2009 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x200C), "&zwnj;");  // zero width non-joiner,U+200C NEW RFC 2070 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x200D), "&zwj;");   // zero width joiner, U+200D NEW RFC 2070 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x200E), "&lrm;");   // left-to-right mark, U+200E NEW RFC 2070 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x200F), "&rlm;");   // right-to-left mark, U+200F NEW RFC 2070 -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2013), "&ndash;"); // en dash, U+2013 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2014), "&mdash;"); // em dash, U+2014 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2018), "&lsquo;"); // left single quotation mark,U+2018 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2019), "&rsquo;"); // right single quotation mark,U+2019 ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x201A), "&sbquo;"); // single low-9 quotation mark, U+201A NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x201C), "&ldquo;"); // left double quotation mark,U+201C ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x201D), "&rdquo;"); // right double quotation mark,U+201D ISOnum -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x201E), "&bdquo;"); // double low-9 quotation mark, U+201E NEW -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2020), "&dagger;");    // dagger, U+2020 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2021), "&Dagger;");    // double dagger, U+2021 ISOpub -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2030), "&permil;");    // per mille sign, U+2030 ISOtech -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x2039), "&lsaquo;");    // single left-pointing angle quotation mark,U+2039 ISO proposed -->
        // <!-- lsaquo is proposed but not yet ISO standardized -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x203A), "&rsaquo;");    // single right-pointing angle quotation mark,U+203A ISO proposed -->
        // <!-- rsaquo is proposed but not yet ISO standardized -->
        hmpReplaceHtml4.put(Character.valueOf((char)0x20AC), "&euro;");  // -- euro sign, U+20AC NEW -->        
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Invisible constructor because <code>HttpUtil</code> is a utility
     * class.
     */
    private HttpUtil()
    {
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Sends an empty post request.
     * 
     * @param pURL the URL
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL) throws Exception
    {
        return post(pURL, null, null, null, -1);
    }
    
    /**
     * Sends a post request with given data.
     * 
     * @param pURL the URL
     * @param pStream the content to send
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL, InputStream pStream) throws Exception
    {
        return post(pURL, pStream, null, null, -1);
    }
    
    /**
     * Sends a post request with given file.
     * 
     * @param pURL the URL
     * @param pFile the file
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL, File pFile) throws Exception
    {
        FileInputStream fis = new FileInputStream(pFile);
        
        try
        {
            return post(pURL, fis, pFile.getName(), null, -1);
        }
        finally
        {
            CommonUtil.close(fis);
        }
    }

    /**
     * Sends a post request with given content.
     * 
     * @param pURL the URL
     * @param pStream the content to send
     * @param pFileName the file name
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL, InputStream pStream, String pFileName) throws Exception
    {
        return post(pURL, pStream, pFileName, null, -1);
    }
    
    /**
     * Sends a post request with given content.
     * 
     * @param pURL the URL
     * @param pStream the content to send
     * @param pFileName the file name
     * @param pProperties additional request properties
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL, InputStream pStream, String pFileName, Map<String, String> pProperties) throws Exception
    {
        return post(pURL, pStream, pFileName, pProperties, -1);
    }
    
    /**
     * Sends a post request with given content.
     * 
     * @param pURL the URL
     * @param pStream the content to send
     * @param pFileName the file name
     * @param pProperties additional request properties
     * @param pTimeout the connect and read timeout
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream post(String pURL, InputStream pStream, String pFileName, Map<String, String> pProperties, int pTimeout) throws Exception
    {
        URL url = new URL(pURL);
        
        URLConnection ucon = url.openConnection(); 
        ucon.setDoOutput(pStream != null);
        ucon.setDoInput(true);
        ucon.setUseCaches(false);
        
        if (pTimeout > 0)
        {
            ucon.setConnectTimeout(pTimeout);
            ucon.setReadTimeout(pTimeout);
        }

        if (pProperties != null)
        {
            for (Entry<String, String> entry : pProperties.entrySet())
            {
                ucon.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
        if (pStream != null)
        {
            ucon.setRequestProperty("Content-Type", "application/octet-stream");
            
            if (pFileName != null)
            {
                ucon.setRequestProperty("Content-Disposition", "attachment; filename=\"" + pFileName + "\";");
            }

            FileUtil.copy(pStream, ucon.getOutputStream());
        }

        return ucon.getInputStream(); 
    }
    
    /**
     * Sends a get request.
     * 
     * @param pURL the URL
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream get(String pURL) throws Exception
    {
        return get(pURL, null, null, -1);
    }

    /**
     * Sends a get request with given file.
     * 
     * @param pURL the URL
     * @param pFile the file
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream get(String pURL, File pFile) throws Exception
    {
        FileInputStream fis = new FileInputStream(pFile);
        
        try
        {
            return get(pURL, fis, null, -1);
        }
        finally
        {
            CommonUtil.close(fis);
        }
    }

    /**
     * Sends a get request with content.
     * 
     * @param pURL the URL
     * @param pStream the content stream
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream get(String pURL, InputStream pStream) throws Exception
    {
        return get(pURL, pStream, null, -1);
    }    

    /**
     * Sends a get request with content.
     * 
     * @param pURL the URL
     * @param pStream the content stream
     * @param pProperties additional request properties
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream get(String pURL, InputStream pStream, Map<String, String> pProperties) throws Exception
    {
        return get(pURL, pStream, pProperties, -1);
    }    
    
    /**
     * Sends a get request with content.
     * 
     * @param pURL the URL
     * @param pStream the content stream
     * @param pProperties additional request properties
     * @param pTimeout the connect and read timeout
     * @return the response stream
     * @throws Exception if communication failed
     */
    public static InputStream get(String pURL, InputStream pStream, Map<String, String> pProperties, int pTimeout) throws Exception
    {
        URL url = new URL(pURL);
        
        URLConnection ucon = url.openConnection(); 
        ucon.setDoOutput(pStream != null);
        ucon.setDoInput(true);
        ucon.setUseCaches(false);
        
        if (pTimeout > 0)
        {
            ucon.setConnectTimeout(pTimeout);
            ucon.setReadTimeout(pTimeout);
        }        
        
        if (pProperties != null)
        {
            for (Entry<String, String> entry : pProperties.entrySet())
            {
                ucon.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if (pStream != null)
        {
            ucon.setRequestProperty("Content-Type", "application/octet-stream");
            
            FileUtil.copy(pStream, ucon.getOutputStream());
        }        

        return ucon.getInputStream(); 
    }
    
    /**
     * Replaces special characters with escape characters, valid for html.
     * 
     * @param pText the text
     * @return the escaped text
     */
    public static String escapeHtml(String pText)
    {
        if (pText == null)
        {
            return null;
        }
        
        StringBuilder sbText = new StringBuilder(pText.length() + (int)(pText.length() * 0.1));
        
        char ch;
        
        for (int i = 0; i < pText.length(); i++)
        {
            ch = pText.charAt(i);

            if (ch == '\n')
            {
                sbText.append("<br/>");
            }
            else
            {
                String sReplace = hmpReplaceHtml4.get(Character.valueOf(ch));
                
                if (sReplace == null)
                {
                    sbText.append(ch);
                }
                else
                {
                    sbText.append(sReplace);
                }
            }
        }
        
        return sbText.toString();
    }
    
    /**
     * Converts formatted html to one-line html without whitespaces after start tags and before end tags, e.g.
     * <pre>&lt;html&gt;  
     *  welcome
     *  &lt;div&gt;  hello 
     *  &lt;/div&gt;  
     *&lt;/html&gt;</pre>  
     * converted to
     * <pre>&lt;html&gt;welcome&lt;div&gt;hello&lt;/div&gt;&lt;/html&gt;</pre>
     * 
     * @param pHtml the formatted html
     * @return the on-line html
     */
    public static String oneLine(String pHtml)
    {
    	if (pHtml == null)
    	{
    		return null;
    	}
    	
		StringBuilder sbOneLine = new StringBuilder(pHtml);
		
		int start = 0;
		int end;
		int iPos = sbOneLine.indexOf(">", start);
		
		while (iPos >= 0)
		{
			end = iPos + 1;
			
			while (end < sbOneLine.length() && Character.isWhitespace(sbOneLine.charAt(end)))
			{
				end++;
			}
			
			if (end > iPos + 1)
			{
				sbOneLine.replace(iPos + 1, end, "");
			}
			
			start = iPos + 1;
			iPos = sbOneLine.indexOf(">", start);
		}	
		
		start = 1;
		iPos = sbOneLine.indexOf("<", start);
		
		while (iPos > 0)
		{
			end = iPos - 1;
			
			while (end > 0 && Character.isWhitespace(sbOneLine.charAt(end)))
			{
				end--;
			}
			
			if (end < iPos - 1)
			{
				sbOneLine.replace(end + 1, iPos, "");
			}
			
			start = end + 2;
			iPos = sbOneLine.indexOf("<", start);
		}
		
		return sbOneLine.toString();    	
    }
    
    /**
     * Gets the ETag http header value for the last modified of a file.
     * 
     * @param pFile the requested file
     * @return the ETag value with the last modified of the file
     */
    public static String getETag(File pFile)
    {
        return "W/\"" + pFile.length() + "-" + pFile.lastModified() + "\"";
    }

    /**
     * Gets the Last-Modified http header value for the last modified of a file.
     * 
     * @param pFile the requested file
     * @return the Last-Modified value with the last modified of the file
     */
    public static String getLastModified(File pFile)
    {
        return sdfModified.format(new Date(pFile.lastModified()));
    }    
    
}   // HttpUtil
