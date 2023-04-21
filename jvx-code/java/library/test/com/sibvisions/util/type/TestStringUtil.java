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
 * 07.06.2011 - [JR] - #383: test cases
 */
package com.sibvisions.util.type;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.util.type.StringUtil.CaseSensitiveType;
import com.sibvisions.util.type.StringUtil.CharacterType;
import com.sibvisions.util.type.StringUtil.TextType;

/**
 * Tests {@link StringUtil} methods.
 * 
 * @author René Jahn
 * @see StringUtil
 */
public class TestStringUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests camelcase - constantname conversion.
	 */
	@Test
	public void testCamelCaseAndConstantName()
	{
		Assert.assertEquals("vSmag01MaschineErpData", StringUtil.convertToMemberName("V_SMAG01_MASCHINE_ERP_DATA"));
		
		Assert.assertEquals("V_SMAG01_MASCHINE_ERP_DATA", StringUtil.convertMemberNameToText("vSmag01MaschineErpData").replace(' ', '_').toUpperCase());
	}
	
	/**
	 * Tests the {@link StringUtil#concat(String, String...)} method.
	 */
	@Test
	public void testConcat()
	{
		Assert.assertEquals("null", StringUtil.concat("", (String)null));
		Assert.assertEquals("null", StringUtil.concat("", (Object)null));
		Assert.assertEquals("", StringUtil.concat("", (List<Object>)null));
		Assert.assertEquals("", StringUtil.concat("", (Object[])null));
		Assert.assertEquals("", StringUtil.concat("", (String[])null));
		
		Assert.assertEquals("", StringUtil.concat("", new String[] {}));
		Assert.assertEquals("", StringUtil.concat("", new Object[] {}));
		Assert.assertEquals("", StringUtil.concat("", new ArrayList<String>()));
		Assert.assertEquals("", StringUtil.concat("", ""));
		
		Assert.assertEquals("a", StringUtil.concat("", "a"));
		Assert.assertEquals("ab", StringUtil.concat("", "a", "b"));
		Assert.assertEquals("abc", StringUtil.concat("", "a", "b", "c"));
		Assert.assertEquals("null;b;null", StringUtil.concat(";", null, "b", null));
		
		Assert.assertEquals("test 5 after", StringUtil.concat(" ", "test", Integer.valueOf(5), new StringBuilder("after")));
	}
	
	/**
	 * Tests the {@link StringUtil#containsWhitespace(String)} method.
	 */
	@Test
	public void testContainsWhitespace()
	{
		Assert.assertFalse(StringUtil.containsWhitespace(null));
		Assert.assertFalse(StringUtil.containsWhitespace(""));
		Assert.assertFalse(StringUtil.containsWhitespace("aaaaaaabbbccc"));
		
		Assert.assertTrue(StringUtil.containsWhitespace(" "));
		Assert.assertTrue(StringUtil.containsWhitespace("\n"));
		Assert.assertTrue(StringUtil.containsWhitespace("\r"));
		Assert.assertTrue(StringUtil.containsWhitespace("\t"));
		
		Assert.assertTrue(StringUtil.containsWhitespace("Some text"));
		Assert.assertTrue(StringUtil.containsWhitespace("Some text"));
	}
	
	/**
	 * Tests the {@link StringUtil#convertMemberNameToText(String)} method.
	 */
	@Test
	public void testConvertMemberNameToText()
	{
		Assert.assertNull(StringUtil.convertMemberNameToText(null));
		Assert.assertEquals("", StringUtil.convertMemberNameToText(""));
		
		Assert.assertEquals("Abc", StringUtil.convertMemberNameToText("abc"));
		Assert.assertEquals("Ab C", StringUtil.convertMemberNameToText("abC"));
		Assert.assertEquals("A B C", StringUtil.convertMemberNameToText("ABC"));
		Assert.assertEquals("J V Ta", StringUtil.convertMemberNameToText("JVTa"));
		Assert.assertEquals("Contactslist", StringUtil.convertMemberNameToText("contactslist"));
		Assert.assertEquals("Contacts List", StringUtil.convertMemberNameToText("contactsList"));
		Assert.assertEquals(" Contacts List", StringUtil.convertMemberNameToText(" contactsList"));
		Assert.assertEquals(" \t  Contacts List", StringUtil.convertMemberNameToText(" \t  contactsList"));
		Assert.assertEquals("\tContacts List", StringUtil.convertMemberNameToText("\tcontactsList"));
		Assert.assertEquals("Contactslist", StringUtil.convertMemberNameToText("contactslist"));
		Assert.assertEquals("Header and Footer", StringUtil.convertMemberNameToText("Header_andFooter"));
		Assert.assertEquals("Header and footer", StringUtil.convertMemberNameToText("Header_and_footer"));
		Assert.assertEquals("Header And Footer", StringUtil.convertMemberNameToText("HeaderAndFooter"));
		Assert.assertEquals("Header and footer", StringUtil.convertMemberNameToText("Header_and_footer"));
		Assert.assertEquals("Header and Footer", StringUtil.convertMemberNameToText("Header_and_Footer"));
		Assert.assertEquals("Header-and-Footer", StringUtil.convertMemberNameToText("Header-and-Footer"));
		Assert.assertEquals("Header-and-Footer ", StringUtil.convertMemberNameToText("Header-and-Footer "));
		Assert.assertEquals("Header    and Footer", StringUtil.convertMemberNameToText("Header    and Footer"));
		
		Assert.assertEquals("Hüter Und Schäfchen", StringUtil.convertMemberNameToText("hueterUndSchaefchen", true));
	}
	
	/**
	 * Tests the {@link StringUtil#convertMethodNameToText(String)} method.
	 */
	@Test
	public void testConvertMethodNameToText()
	{
		Assert.assertNull(StringUtil.convertMethodNameToText(null));
		Assert.assertEquals("", StringUtil.convertMethodNameToText(""));
		
		Assert.assertEquals("Abc", StringUtil.convertMethodNameToText("abc"));
		Assert.assertEquals("C", StringUtil.convertMethodNameToText("abC"));
		Assert.assertEquals("ABC", StringUtil.convertMethodNameToText("ABC"));
		Assert.assertEquals("JVTa", StringUtil.convertMethodNameToText("JVTa"));
		Assert.assertEquals("Something", StringUtil.convertMethodNameToText("something"));
		Assert.assertEquals("Contacts List", StringUtil.convertMethodNameToText("createContactsList"));
		Assert.assertEquals("Contacts List123", StringUtil.convertMethodNameToText("ContactsList123"));
		Assert.assertEquals("Leibesübung", StringUtil.convertMethodNameToText("getLeibesuebung", true));
		Assert.assertEquals("Leibesuebung", StringUtil.convertMethodNameToText("getLeibesuebung"));
		Assert.assertEquals("Überfahrt", StringUtil.convertMethodNameToText("getUeberfahrt", true));
		Assert.assertEquals("Ueberfahrt", StringUtil.convertMethodNameToText("getUeberfahrt"));
		Assert.assertEquals("Über Das Mehr Überfahren", StringUtil.convertMethodNameToText("getUeberDasMehrUeberfahren", true));
		Assert.assertEquals("Ueber Das Mehr Ueberfahren", StringUtil.convertMethodNameToText("getUeberDasMehrUeberfahren"));
		Assert.assertEquals("Header and Footer", StringUtil.convertMethodNameToText("getHeader_and_Footer"));
		Assert.assertEquals("Header and Footer", StringUtil.convertMethodNameToText("getHeader_andFooter"));
		Assert.assertEquals("Header and footer", StringUtil.convertMethodNameToText("getHeader_and_footer"));
		Assert.assertEquals("Header And Footer", StringUtil.convertMethodNameToText("getHeaderAndFooter"));
		Assert.assertEquals("Header And Footer", StringUtil.convertMethodNameToText("get_HeaderAndFooter"));
		Assert.assertEquals("header And Footer", StringUtil.convertMethodNameToText("get_headerAndFooter"));
		Assert.assertEquals("OK", StringUtil.convertMethodNameToText("getOK"));
		Assert.assertEquals("SIB Co ", StringUtil.convertMethodNameToText("SIB_Co_"));
		Assert.assertEquals("SIB Co   ", StringUtil.convertMethodNameToText("SIB_Co___"));
		Assert.assertEquals("SIB Cä ", StringUtil.convertMethodNameToText("SIB_Cae_", true));
		Assert.assertEquals("SIB Cä", StringUtil.convertMethodNameToText("SIB_Cae", true));
		Assert.assertEquals("SIB Co", StringUtil.convertMethodNameToText("_SIB_Co", true));
	}
	
	/**
	 * Tests the {@link StringUtil#convertToMemberName(String)} method.
	 */
	@Test
	public void testConvertToMemberName()
	{
		Assert.assertNull(StringUtil.convertToMemberName(null));
		Assert.assertEquals("", StringUtil.convertToMemberName(""));
		
		Assert.assertEquals("mynameIsjava", StringUtil.convertToMemberName("myName_isJava"));
		Assert.assertEquals("ab", StringUtil.convertToMemberName("___1233ab"));
		Assert.assertEquals("oenlyMember", StringUtil.convertToMemberName("önly member"));
		Assert.assertEquals("thisissmyMethod", StringUtil.convertToMemberName("ThisIßMy method"));
		Assert.assertEquals("a", StringUtil.convertToMemberName("á"));
		Assert.assertEquals("gHelloOeaeue", StringUtil.convertToMemberName((char)0x0120 + "_hello öäü"));
		Assert.assertEquals("tableTable", StringUtil.convertToMemberName("TABLE_TABLE"));
		Assert.assertEquals("table1Table", StringUtil.convertToMemberName("TABLE1_TABLE"));
		Assert.assertEquals("firstname", StringUtil.convertToMemberName("Firstname"));
		Assert.assertEquals("firstname", StringUtil.convertToMemberName("FirstName"));
		Assert.assertEquals("firstName", StringUtil.convertToMemberName("First_Name"));
		Assert.assertEquals("firstName", StringUtil.convertToMemberName("First_name"));
		Assert.assertEquals("firstName", StringUtil.convertToMemberName("First_name_"));
	}
	
    /**
     * Tests the {@link StringUtil#convertMethodNameToFieldName(String)} method.
     */
	@Test
	public void testConvertMethodNameToFieldName()
	{
        Assert.assertEquals("firstName", StringUtil.convertMethodNameToFieldName("getFirstName"));
        Assert.assertEquals("validname", StringUtil.convertMethodNameToFieldName("isValidname"));
        Assert.assertEquals("isvalidname", StringUtil.convertMethodNameToFieldName("isvalidname"));
	}
	
	/**
	 * Tests the {@link StringUtil#convertToMethodName(String)} method.
	 */
	@Test
	public void testConvertToMethodName()
	{
		Assert.assertNull(StringUtil.convertToMethodName(null, null));
		Assert.assertEquals("", StringUtil.convertToMethodName(null, ""));
		Assert.assertNull(StringUtil.convertToMethodName("", null));
		Assert.assertEquals("", StringUtil.convertToMethodName("", ""));
		
		Assert.assertEquals("Modern", StringUtil.convertToMethodName("", "modern"));
		Assert.assertEquals("Myname", StringUtil.convertToMethodName("", "myName"));
		Assert.assertEquals("getMyname", StringUtil.convertToMethodName("get", "myName"));
		Assert.assertEquals("getMyName", StringUtil.convertToMethodName("get", "my_Name"));
		Assert.assertEquals("getMyName", StringUtil.convertToMethodName("get", "my_Name_"));
		Assert.assertEquals("getMyName", StringUtil.convertToMethodName("get", "my_name"));
		Assert.assertEquals("getMyName", StringUtil.convertToMethodName("get", "my name"));
		Assert.assertEquals("isMyNaeme", StringUtil.convertToMethodName("is", "my Näme"));
		Assert.assertEquals("isMynaeme", StringUtil.convertToMethodName("is", "my.Näme"));
	}
	
	/**
	 * Tests the {@link StringUtil#convertToName(String)} method.
	 */
	@Test
	public void testConvertToName()
	{
		Assert.assertNull(StringUtil.convertToName(null));
		Assert.assertEquals("", StringUtil.convertToName(""));
		
		Assert.assertEquals("oeueaeand_noname", StringUtil.convertToName("öüä\tand_noname"));
		Assert.assertEquals("oeueaeand_noname", StringUtil.convertToName("öüä and_noname"));
		Assert.assertEquals("this_ss_is_oe_name", StringUtil.convertToName("this_ß_is_ö_name"));
		Assert.assertEquals("MyColumnName", StringUtil.convertToName("_MyColumnName"));
		Assert.assertEquals("Column", StringUtil.convertToName("Column"));
		Assert.assertEquals("MyColumnName", StringUtil.convertToName("_MyColumn.Name"));
		Assert.assertEquals("ID1", StringUtil.convertToName("ID.1"));
		Assert.assertEquals("AEnderung", StringUtil.convertToName("Änderung"));
		Assert.assertEquals("UEber", StringUtil.convertToName("Über"));
		Assert.assertEquals("OEd", StringUtil.convertToName("Öd"));
		Assert.assertEquals("_OEd#13", StringUtil.convertToName("_Öd#13", "_$", "_$#"));
		Assert.assertEquals("$OEd#13$1", StringUtil.convertToName("$Öd#13$1", "_$", "_$#"));
	}
	
	/**
	 * Tests the {@link StringUtil#countCharacter(String, char)} method.
	 */
	@Test
	public void testCountCharacter()
	{
		Assert.assertEquals(0, StringUtil.countCharacter(null, 'a'));
		Assert.assertEquals(0, StringUtil.countCharacter("", 'a'));
		
		Assert.assertEquals(1, StringUtil.countCharacter("a", 'a'));
		Assert.assertEquals(3, StringUtil.countCharacter("aaa", 'a'));
		Assert.assertEquals(0, StringUtil.countCharacter("bbb", 'a'));
		Assert.assertEquals(0, StringUtil.countCharacter("    ", 'a'));
		Assert.assertEquals(4, StringUtil.countCharacter("    ", ' '));
		Assert.assertEquals(2, StringUtil.countCharacter("abcced", 'c'));
	}
	
	/**
	 * Tests the {@link StringUtil#firstCharLower(String)} method.
	 */
	@Test
	public void testFirstCharLower()
	{
		Assert.assertNull(StringUtil.firstCharUpper(null));
		Assert.assertEquals("", StringUtil.firstCharUpper(""));
		
		Assert.assertEquals("   ", StringUtil.firstCharUpper("   "));
		
		Assert.assertEquals("a", StringUtil.firstCharLower("a"));
		Assert.assertEquals("a", StringUtil.firstCharLower("A"));
		Assert.assertEquals("welcome", StringUtil.firstCharLower("Welcome"));
		Assert.assertEquals("welCome", StringUtil.firstCharLower("WelCome"));
	}
	
	/**
	 * Tests the {@link StringUtil#firstCharUpper(String)} method.
	 */
	@Test
	public void testFirstCharUpper()
	{
		Assert.assertNull(StringUtil.firstCharUpper(null));
		Assert.assertEquals("", StringUtil.firstCharUpper(""));
		
		Assert.assertEquals("   ", StringUtil.firstCharUpper("   "));
		
		Assert.assertEquals("A", StringUtil.firstCharUpper("a"));
		Assert.assertEquals("A", StringUtil.firstCharUpper("A"));
		Assert.assertEquals("Welcome", StringUtil.firstCharUpper("welcome"));
		Assert.assertEquals("WelCome", StringUtil.firstCharUpper("welCome"));
	}
	
	/**
	 * Tests the {@link StringUtil#formatInitCap(String)} method.
	 */
	@Test
	public void testFormatInitCap()
	{
		Assert.assertNull(StringUtil.formatInitCap(null));
		Assert.assertEquals("", StringUtil.formatInitCap(""));
		Assert.assertEquals("", StringUtil.formatInitCap("    "));
		
		Assert.assertEquals("Test Of Init Cap", StringUtil.formatInitCap("test of init cap"));
		Assert.assertEquals("Test", StringUtil.formatInitCap("test"));
		Assert.assertEquals("Test", StringUtil.formatInitCap("TEST"));
		Assert.assertEquals("Test 123 Asdf", StringUtil.formatInitCap("TEST 123 asdF"));
		Assert.assertEquals("Test 123 Asdf", StringUtil.formatInitCap("TEST_123_asdF"));
		
		Assert.assertEquals("Test123Asdf", StringUtil.formatInitCap("TEST 123 asdF", true));
		Assert.assertEquals("Test123Asdf", StringUtil.formatInitCap("TEST123_asdF", true));
		Assert.assertEquals("Test123asdf", StringUtil.formatInitCap("TEST123asdF", true));
		Assert.assertEquals("", StringUtil.formatInitCap("_", true));
		Assert.assertEquals(" ", StringUtil.formatInitCap("_", false));
		
		Assert.assertEquals("PreFormatted", StringUtil.formatInitCap("pre_Formatted", true));
		Assert.assertEquals("Preformatted", StringUtil.formatInitCap("preFormatted", true));
		
		Assert.assertEquals("NewApplication", StringUtil.formatInitCap("New application", true));
		
		Assert.assertEquals("This Is My Name", StringUtil.formatInitCap("  This is my name  ", false));
		
		Assert.assertEquals("A", StringUtil.formatInitCap("A_"));
	}
	
	/**
	 * Tests the {@link StringUtil#formatInitCap(String)} method.
	 */
	@Test
	public void testFormatInitCapBug1475()
	{
		String originalName = "J V Tablename";
		
		Assert.assertEquals(originalName, StringUtil.convertMemberNameToText(StringUtil.convertToMemberName(originalName)));
		
		originalName = "J_V_AG_TYP";
		
		Assert.assertEquals(StringUtil.formatInitCap(originalName), StringUtil.convertMemberNameToText(StringUtil.convertToMemberName(originalName)));
	}
	
	/**
	 * Tests the {@link StringUtil#formatMethodName(String, String)} method.
	 */
	@Test
	public void testFormatMethodName()
	{
		Assert.assertEquals("getMail_from_host", StringUtil.formatMethodName("get", "mail_from_host"));
		Assert.assertEquals("setX", StringUtil.formatMethodName("set", "x"));
		
		Assert.assertEquals("x", StringUtil.formatMethodName(null, "x"));
		Assert.assertNull(StringUtil.formatMethodName("set", null));
		Assert.assertNull(StringUtil.formatMethodName(" ", " "));
	}
	
	/**
	 * Tests the {@link StringUtil#getCaseSensitiveType(String)} method.
	 */
	@Test
	public void testGetCaseSensitiveType()
	{
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType(null));
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType(""));
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType("   "));
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType(" \t\n\r \t"));
		
		//letters
		Assert.assertSame(CaseSensitiveType.UpperCase, StringUtil.getCaseSensitiveType("ABC"));
		Assert.assertSame(CaseSensitiveType.LowerCase, StringUtil.getCaseSensitiveType("abc"));
		Assert.assertSame(CaseSensitiveType.MixedCase, StringUtil.getCaseSensitiveType("AabD"));
		
		Assert.assertSame(CaseSensitiveType.LowerCase, StringUtil.getCaseSensitiveType("123öäüß_"));
		Assert.assertSame(CaseSensitiveType.UpperCase, StringUtil.getCaseSensitiveType("123ÖAS_"));
		
		//special characters
		Assert.assertSame(CaseSensitiveType.UpperCase, StringUtil.getCaseSensitiveType("ÄÜÖ"));
		Assert.assertSame(CaseSensitiveType.LowerCase, StringUtil.getCaseSensitiveType("äüö"));
		Assert.assertSame(CaseSensitiveType.LowerCase, StringUtil.getCaseSensitiveType("ß"));
		
		//no letters
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType("123"));
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType(".,_"));
		Assert.assertSame(CaseSensitiveType.NoLetter, StringUtil.getCaseSensitiveType("+#*<>"));
	}
	
	/**
	 * Tests the {@link StringUtil#getCharacterType(String)} method.
	 */
	@Test
	public void testGetCharacterType()
	{
		Assert.assertSame(CharacterType.None, StringUtil.getCharacterType(null));
		Assert.assertSame(CharacterType.None, StringUtil.getCharacterType(""));
		
		Assert.assertSame(CharacterType.LettersDigitsSpace, StringUtil.getCharacterType("a b c 1"));
		Assert.assertSame(CharacterType.LettersDigitsWhitespace, StringUtil.getCharacterType("a123cdß kll\t"));
		
		Assert.assertSame(CharacterType.OnlyWhitespace, StringUtil.getCharacterType("\t\n\r "));
		Assert.assertSame(CharacterType.OnlyWhitespace, StringUtil.getCharacterType(" "));
		Assert.assertSame(CharacterType.OnlySpecial, StringUtil.getCharacterType("\".-#+"));
		Assert.assertSame(CharacterType.OnlySpecial, StringUtil.getCharacterType("\t\n\r -"));
		
		Assert.assertSame(CharacterType.Letters, StringUtil.getCharacterType("ABC"));
		Assert.assertSame(CharacterType.Digits, StringUtil.getCharacterType("123"));
		Assert.assertSame(CharacterType.LettersDigits, StringUtil.getCharacterType("a123cdß"));
		
		Assert.assertSame(CharacterType.LettersSpecial, StringUtil.getCharacterType("abAB\t"));
		Assert.assertSame(CharacterType.LettersSpecial, StringUtil.getCharacterType("abAB.-"));
		Assert.assertSame(CharacterType.LettersSpecial, StringUtil.getCharacterType("abAB \".-\t"));
		Assert.assertSame(CharacterType.DigitsSpecial, StringUtil.getCharacterType("123\t"));
		Assert.assertSame(CharacterType.DigitsSpecial, StringUtil.getCharacterType("123.-"));
		Assert.assertSame(CharacterType.DigitsSpecial, StringUtil.getCharacterType("\".-\t123"));
		
		Assert.assertSame(CharacterType.All, StringUtil.getCharacterType("abAB \".-123"));
		Assert.assertSame(CharacterType.All, StringUtil.getCharacterType("abAB \t-123"));
		Assert.assertSame(CharacterType.All, StringUtil.getCharacterType("abAB \".-\t123"));
	}
	
	/**
	 * Tests the {@link StringUtil#getFirstWord(String)} method.
	 */
	@Test
	public void testGetFirstWord()
	{
		Assert.assertNull(StringUtil.getFirstWord(null));
		Assert.assertEquals("", StringUtil.getFirstWord(""));
		
		Assert.assertEquals("Welcome", StringUtil.getFirstWord("WelcomeToTheShow"));
		Assert.assertEquals("welcome", StringUtil.getFirstWord("welcomeToTheShow"));
		Assert.assertEquals("Welcome", StringUtil.getFirstWord("Welcome"));
		Assert.assertEquals("welcome", StringUtil.getFirstWord("welcome"));
		
		Assert.assertEquals("w", StringUtil.getFirstWord("w"));
	}
	
	/**
	 * Checks if a found text is part of a quoted string.
	 */
	@Test
	public void testGetQuotedBoundaries()
	{
		Assert.assertArrayEquals(new int[] {}, StringUtil.getQuotedBoundaries(null));
		Assert.assertArrayEquals(new int[] {}, StringUtil.getQuotedBoundaries(""));
		Assert.assertArrayEquals(new int[] {}, StringUtil.getQuotedBoundaries("   "));
		
		Assert.assertArrayEquals(new int[] { 0, 6 }, StringUtil.getQuotedBoundaries("\"12345\""));
		Assert.assertArrayEquals(new int[] { 4, 10 }, StringUtil.getQuotedBoundaries("get(\"12345\")"));
		Assert.assertArrayEquals(new int[] { 4, 10 }, StringUtil.getQuotedBoundaries("get(\"12345\"), \"Hallo"));
		Assert.assertArrayEquals(new int[] { 4, 10, 14, 20 }, StringUtil.getQuotedBoundaries("get(\"12345\"), \"Hallo\""));
		Assert.assertArrayEquals(new int[] { 4, 10, 14, 20 }, StringUtil.getQuotedBoundaries("get(\"12\\\"5\"), \"Hallo\""));
	}
	
	/**
	 * Tests the {@link StringUtil#getShortenedWords(String, int)} method.
	 */
	@Test
	public void testGetShortenedWords()
	{
		Assert.assertNull(StringUtil.getShortenedWords(null, 3));
		Assert.assertEquals("", StringUtil.getShortenedWords("", 3));
		
		Assert.assertEquals("", StringUtil.getShortenedWords("Super", 0));
		
		Assert.assertEquals("UPPERCASETEST", StringUtil.getShortenedWords("UPPERCASETEST", 2));
		
		Assert.assertEquals("Sup", StringUtil.getShortenedWords("Super", 3));
		Assert.assertEquals("SupSim", StringUtil.getShortenedWords("SuperSimpel", 3));
		Assert.assertEquals("Con", StringUtil.getShortenedWords("Contracts", 3));
		Assert.assertEquals("ConEdu", StringUtil.getShortenedWords("ContractsEducation", 3));
		Assert.assertEquals("SupKurNamVerDesMar", StringUtil.getShortenedWords("SuperKurzNamenVerfahrenDesMartins", 3));
		Assert.assertEquals("SSon", StringUtil.getShortenedWords("SSonderfall", 3));
		Assert.assertEquals("SupK", StringUtil.getShortenedWords("SuperK", 3));
		
		Assert.assertEquals("SupeKurzNameVerfDesMart", StringUtil.getShortenedWords("SuperKurzNamenVerfahrenDesMartins", 4));
		
		Assert.assertEquals("com.sib.ful.SomClaWitANam", StringUtil.getShortenedWords("com.sibvisions.fullclassnametest.SomeClassWithAName", 3));
		
		// The examples from the JavaDoc
		Assert.assertEquals("SomDatWorScr", StringUtil.getShortenedWords("SomeDataWorkScreen", 3));
		Assert.assertEquals("ConEdu", StringUtil.getShortenedWords("ContractsEducation", 3));
		Assert.assertEquals("ContrEduca", StringUtil.getShortenedWords("ContractsEducation", 5));
		Assert.assertEquals("com.sib.tes.ClaNam", StringUtil.getShortenedWords("com.sibvisions.test.ClassName", 3));
		
		Assert.assertEquals("dba", StringUtil.getShortenedWords(DBAccess.class.getSimpleName(), 1).toLowerCase());
		Assert.assertEquals("dbs", StringUtil.getShortenedWords(DBStorage.class.getSimpleName(), 1).toLowerCase());
		Assert.assertEquals("cqs", StringUtil.getShortenedWords("CustomQueryStorage", 1).toLowerCase());
		
		//whitespace
		Assert.assertEquals("cu qu st", StringUtil.getShortenedWords("Custom Query Storage", 2).toLowerCase());
		Assert.assertEquals("ha to st", StringUtil.getShortenedWords("hans tom steve", 2).toLowerCase());
		
		Assert.assertEquals("hatost", StringUtil.getShortenedWords("hans tom steve", 2, true).toLowerCase());
	}
	
	/**
	 * Tests the {@link StringUtil#getWhitespaceSeparatedShortenedWords(String, int, boolean) method.
	 */
	@Test
	public void testGetWhitespaceSeparatedShortenedWords()
	{
		Assert.assertEquals("hatost", StringUtil.getWhitespaceSeparatedShortenedWords("hans tom steve", 2, false).toLowerCase());
		Assert.assertEquals("h2to1", StringUtil.getWhitespaceSeparatedShortenedWords("h2 to1 1", 2, false).toLowerCase());
		
		Assert.assertEquals("hto", StringUtil.getWhitespaceSeparatedShortenedWords("h2 to1 1", 2, true).toLowerCase());
		
		Assert.assertEquals("neap", StringUtil.getWhitespaceSeparatedShortenedWords("Neue applikation 2", 2, true).toLowerCase());
		
		Assert.assertEquals("Ne", StringUtil.getWhitespaceSeparatedShortenedWords("NeueApplikation 2", 2, true));
		Assert.assertEquals("ab", StringUtil.getWhitespaceSeparatedShortenedWords("a.b.c", 2, true).toLowerCase());
		
		Assert.assertEquals("NeAp", StringUtil.getWhitespaceSeparatedShortenedWords("NeueApplikation 2", 2, true, true));
		
	}
	
	/**
	 * Tests the {@link StringUtil#getText(String, char...)} method.
	 */
	@Test
	public void testGetTextByCharacters()
	{
		Assert.assertNull(StringUtil.getText(null));
		Assert.assertNull(StringUtil.getText(null, new char[] { 'a', 'b' }));
		Assert.assertNull(StringUtil.getText(null, new char[] {}));
		Assert.assertEquals("", StringUtil.getText(""));
		Assert.assertEquals("", StringUtil.getText("", new char[] { 'a', 'b' }));
		Assert.assertEquals("", StringUtil.getText("", new char[] {}));
		
		Assert.assertEquals("aabbbF", StringUtil.getText("aaAbbbBcCdDeeeeEfF", new char[] { 'a', 'b', 'F' }));
	}
	
	/**
	 * Tests the
	 * {@link StringUtil#getText(String, com.sibvisions.util.type.StringUtil.TextType)}
	 * method.
	 */
	@Test
	public void testGetTextByType()
	{
		Assert.assertEquals("ABCdef", StringUtil.getText("ABCdef123 \t\n.-.,", TextType.Letters));
		Assert.assertEquals("123", StringUtil.getText("ABCdef123 \t\n.-.,", TextType.Digits));
		Assert.assertEquals("ABCdef123", StringUtil.getText("ABCdef123 \t\n.-.,", TextType.LettersDigits));
		
		Assert.assertEquals("ABCdef123 ", StringUtil.getText("ABCdef123 \t\n.-.,", TextType.LettersDigitsSpace));
		Assert.assertEquals("ABCdef123 \t\n", StringUtil.getText("ABCdef123 \t\n.-.,", TextType.LettersDigitsWhitespace));
		
		Assert.assertEquals("abclok", StringUtil.getText("abc123ß.loköäü", TextType.AZLetters));
		Assert.assertEquals("abc123lok", StringUtil.getText("abc123ß.loköäü", TextType.AZLettersDigits));
		Assert.assertEquals("abCDef...", StringUtil.getText("1234abCDef...", TextType.FromFirstLetter));
		Assert.assertEquals("abCDef...", StringUtil.getText("1234_abCDef...", TextType.FromFirstLetter));
		Assert.assertEquals("abCDef", StringUtil.getText("_abCDef", TextType.FromFirstLetter));
		
		Assert.assertEquals("abCDef", StringUtil.getText("1234abCDef", TextType.WithoutLeadingDigits));
		Assert.assertEquals("_abCDef", StringUtil.getText("_abCDef", TextType.WithoutLeadingDigits));
		
		Assert.assertEquals("", StringUtil.getText("", TextType.UpperCase));
		Assert.assertEquals("TIAT", StringUtil.getText("ThisIsATest", TextType.UpperCase));
		Assert.assertEquals("ANSE", StringUtil.getText("A kinda Normal SentEnce.", TextType.UpperCase));
		
		Assert.assertEquals("hissest", StringUtil.getText("ThisIsATest", TextType.LowerCase));
		Assert.assertEquals("kindaormalentnce", StringUtil.getText("A kinda Normal SentEnce.", TextType.LowerCase));
		
		Assert.assertEquals("f.l@d.c", StringUtil.getText("first.last@mydomain.com", 'f', '.', 'l', '@', 'd', 'c'));
		Assert.assertEquals("SuperSmith", StringUtil.getText("  \nSuper\t@..asdSmith-\r", "Supermith".toCharArray()));
	}
	
	/**
	 * Tests the {@link StringUtil#levenshteinDistance(String, String)} method.
	 */
	@Test
	public void testLevenshteinDistance()
	{
		Assert.assertEquals(0, StringUtil.levenshteinDistance("", ""));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("A", ""));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("", "A"));
		Assert.assertEquals(0, StringUtil.levenshteinDistance("ABCDE", "ABCDE"));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("demo", "emo"));
		Assert.assertEquals(2, StringUtil.levenshteinDistance("Australia", "Austria"));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("a", "A"));
		Assert.assertEquals(7, StringUtil.levenshteinDistance("Levenshtein", "Lever"));
		
		Assert.assertEquals(1, StringUtil.levenshteinDistance("ABCDE", "ABDE"));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("ABDE", "ABCDE"));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("ABCDE", "ABEDE"));
		Assert.assertEquals(1, StringUtil.levenshteinDistance("ABDCE", "ABCDE"));
	}
	
	/**
	 * Tests the {@link StringUtil#like(String, String)} method.
	 */
	@Test
	public void testLike()
	{
		Assert.assertTrue(StringUtil.like("/home/user/app/demo", "/home/*/demo"));
		Assert.assertTrue(StringUtil.like("/home/user/app/demo.txt", "/home/*/d*.txt"));
		Assert.assertTrue(StringUtil.like("My Name is very important!", "*!"));
		Assert.assertTrue(StringUtil.like("My Name is not important!", "My*"));
		Assert.assertTrue(StringUtil.like("On the wall On the wall", "On*the*wall"));
		Assert.assertTrue(StringUtil.like("ABCD", "ABCD"));
		Assert.assertTrue(StringUtil.like("ABCDEFG ABCDEFG", "A*A*"));
		Assert.assertTrue(StringUtil.like("ABCDEFG ABCDEFG", "*F*A*"));
		Assert.assertTrue(StringUtil.like("ABCDEFG ABCDEFG", "*F*G"));
		Assert.assertTrue(StringUtil.like("ABCDEFG ABCDEFG", "*G"));
		Assert.assertTrue(StringUtil.like("ABCDEFG ABCDEFG", "*FG"));
		Assert.assertTrue(StringUtil.like("AB#CDEF.G ABCDEFG", "*.G A*"));
		Assert.assertTrue(StringUtil.like("[DEMO] Objectname", "[*] O*"));
		Assert.assertTrue(StringUtil.like("[DEMO]: Objectname", "*: O*"));
		Assert.assertTrue(StringUtil.like("[DEMO]^ Objectname", "*^*"));
		Assert.assertTrue(StringUtil.like("[DEMO]$ Objectname", "*$*"));
		
		Assert.assertFalse(StringUtil.like("ABCDEFG ABCDEFG", "A*F"));
		Assert.assertFalse(StringUtil.like("ABCDEFG ABCDEFG", "A"));
		
		Assert.assertTrue(StringUtil.like("http://abc.domain.com", "http://abc.domain.com"));

		Assert.assertTrue(StringUtil.like("a", "a**"));
	}
	
	/**
	 * Tests the {@link StringUtil#lpad(String, int)} method.
	 */
	@Test
	public void testLpad()
	{
		Assert.assertNull(StringUtil.lpad(null, 3));
		Assert.assertEquals("   ", StringUtil.lpad("", 3));
		Assert.assertEquals("   ", StringUtil.lpad("  ", 3));
		
		Assert.assertEquals("List", StringUtil.lpad("List", 3));
		Assert.assertEquals("      List", StringUtil.lpad("List", 10));
	}
	
	/**
	 * Tests the {@link StringUtil#ltrim(String)} method.
	 */
	@Test
	public void testLtrim()
	{
		Assert.assertNull(StringUtil.ltrim(null));
		Assert.assertEquals("", StringUtil.ltrim(""));
		Assert.assertEquals("", StringUtil.ltrim("    "));
		Assert.assertEquals("", StringUtil.ltrim("\n\t  \t \r \n"));
		
		Assert.assertEquals("A", StringUtil.ltrim("A"));
		Assert.assertEquals("AB", StringUtil.ltrim("AB"));
		Assert.assertEquals("ABC", StringUtil.ltrim("ABC"));
		
		Assert.assertEquals("abc", StringUtil.ltrim("abc"));
		Assert.assertEquals("abc", StringUtil.ltrim("   abc"));
		Assert.assertEquals("abc   ", StringUtil.ltrim("   abc   "));
	}
	
	/**
	 * Tests the {@link StringUtil#parseColor(String)} method.
	 */
	@Test
	public void testParseColor()
	{
		Assert.assertNull(StringUtil.parseColor(null));
		Assert.assertNull(StringUtil.parseColor(""));
		
		Assert.assertNull(StringUtil.parseColor("#TooLongForANumber"));
		Assert.assertNull(StringUtil.parseColor("#Short"));
		
		Assert.assertNull(StringUtil.parseColor("0xTooLongForANumber"));
		Assert.assertNull(StringUtil.parseColor("0xShort"));
		
		Assert.assertArrayEquals(null, StringUtil.parseColor("notlimegreen"));
		
		Assert.assertArrayEquals(new int[] { 50, 205, 50 }, StringUtil.parseColor("limegreen"));
		
		Assert.assertArrayEquals(new int[] { 0xAA, 0xBB, 0xCC }, StringUtil.parseColor("#ABC"));
		Assert.assertArrayEquals(new int[] { 0xAB, 0xCD, 0xEF }, StringUtil.parseColor("#ABCDEF"));
		
		Assert.assertArrayEquals(new int[] { 0xAB, 0xCD, 0xEF }, StringUtil.parseColor("0xABCDEF"));
		Assert.assertArrayEquals(new int[] { 0xAB, 0xCD, 0xEF }, StringUtil.parseColor("0XABCDEF"));
		
		Assert.assertArrayEquals(new int[] { 128, 129, 130 }, StringUtil.parseColor("128,129,130"));
		Assert.assertArrayEquals(new int[] { 128, 129, 130, 131 }, StringUtil.parseColor("128,129,130,131"));
		
		Assert.assertArrayEquals(new int[] { 0xAB, 0xCD, 0xEF, 0xFF }, StringUtil.parseColor("0xFFABCDEF"));
		Assert.assertArrayEquals(new int[] { 0xAB, 0xCD, 0xEF, 0xFF }, StringUtil.parseColor("#ABCDEFFF"));
	}
	
	/**
	 * Tests the {@link StringUtil#parseInteger(String, String)} method.
	 */
	@Test
	public void testParseInteger()
	{
		Assert.assertNull(StringUtil.parseInteger(null, null));
		Assert.assertNull(StringUtil.parseInteger("", null));
		Assert.assertNull(StringUtil.parseInteger(null, ""));
		Assert.assertNull(StringUtil.parseInteger("", ""));
		
		Assert.assertArrayEquals(new int[] { 128 }, StringUtil.parseInteger("128", null));
		Assert.assertArrayEquals(new int[] { 128 }, StringUtil.parseInteger("128", ""));
		Assert.assertArrayEquals(new int[] {}, StringUtil.parseInteger("aaaa", ""));
		
		Assert.assertArrayEquals(new int[] { 128, 129, 130, 131 }, StringUtil.parseInteger("128,129,130,131", ","));
		Assert.assertArrayEquals(new int[] { -1, 129, -1, 131 }, StringUtil.parseInteger("ccc,129,aaa,131", ","));
	}
	
	/**
	 * Tests the {@link StringUtil#quote(String, char)} method.
	 */
	@Test
	public void testQuote()
	{
		Assert.assertNull(StringUtil.quote(null, '"'));
		
		Assert.assertEquals("\"\"", StringUtil.quote("", '"'));
		Assert.assertEquals("\"   \"", StringUtil.quote("   ", '"'));
		
		Assert.assertEquals("\"abcde\"", StringUtil.quote("abcde", '"'));
		Assert.assertEquals("#abcde#", StringUtil.quote("abcde", '#'));
		Assert.assertEquals("#a##b####cd######e#", StringUtil.quote("a#b##cd###e", '#'));
	}
	
	/**
	 * Tests the {@link StringUtil#removeCharacters(String, char[])} method.
	 */
	@Test
	public void testRemoveCharacters()
	{
		Assert.assertNull(StringUtil.removeCharacters(null));
		Assert.assertEquals("", StringUtil.removeCharacters(""));
		Assert.assertEquals("abcde", StringUtil.removeCharacters("abcde"));
		Assert.assertEquals("abcde", StringUtil.removeCharacters("abcde", new char[] {}));
		
		Assert.assertEquals("bcbdee", StringUtil.removeCharacters("abacbdaee", new char[] { 'a' }));
		Assert.assertEquals("cd", StringUtil.removeCharacters("abacbdaee", new char[] { 'a', 'b', 'e' }));
	}
	
	/**
	 * Tests the {@link StringUtil#removeQuotes(String, String, String)}.
	 */
	@Test
	public void testRemoveQuotes()
	{
		Assert.assertNull(StringUtil.removeQuotes(null, null, null));
		Assert.assertEquals("", StringUtil.removeQuotes("", null, null));
		Assert.assertEquals("abbba", StringUtil.removeQuotes("abbba", null, null));
		Assert.assertEquals("bbba", StringUtil.removeQuotes("abbba", "a", null));
		Assert.assertEquals("abbb", StringUtil.removeQuotes("abbba", null, "a"));
		Assert.assertEquals("bbb", StringUtil.removeQuotes("abbba", "a", "a"));
		
		Assert.assertEquals("bbb", StringUtil.removeQuotes("cccabbbaeee", "a", "a"));
		
		Assert.assertEquals("cccabbb", StringUtil.removeQuotes("cccabbb", "a", "a"));
		
		Assert.assertEquals("hhhh", StringUtil.removeQuotes("cccahhhhbeeee", "a", "b"));
		
		Assert.assertEquals("cccbhhhhaeeee", StringUtil.removeQuotes("cccbhhhhaeeee", "a", "b"));
	}
	
	/**
	 * Tests the {@link StringUtil#removeWhitespaces(String)} method.
	 */
	@Test
	public void testRemoveWhitespaces()
	{
		Assert.assertEquals(null, StringUtil.removeWhitespaces(null));
		Assert.assertEquals("", StringUtil.removeWhitespaces(""));
		
		Assert.assertEquals("", StringUtil.removeWhitespaces("     "));
		Assert.assertEquals("", StringUtil.removeWhitespaces("\t   \n  \r\n"));
		
		Assert.assertEquals("ThisisatestReallyanewline.", StringUtil.removeWhitespaces(" This is a test\nReally a new line."));
	}
	
	/**
	 * Tests camelcase - constantname conversion.
	 */
	@Test
	public void testReplace()
	{
		Assert.assertNull(null, StringUtil.replace(null, null, null));
		Assert.assertEquals("", StringUtil.replace("", null, null));
		Assert.assertEquals("Hello there!", StringUtil.replace("Hello there!", null, null));
		
		Assert.assertEquals("Hllo thr!", StringUtil.replace("Hello there!", "e", null));
		
		String testString = "Hallo Martin wie gehts?";
		
		Assert.assertEquals(System.identityHashCode(testString), System.identityHashCode(StringUtil.replace(testString, "Hugo", "Topfen")));
		
		Assert.assertEquals("Hallo  Martin  wie  gehts?", StringUtil.replace(testString, " ", "  "));
		
		Assert.assertEquals(testString, StringUtil.replace(testString, "", " "));
	}
	
	/**
	 * Tests {@link StringUtil#replacePlaceholder(String)}.
	 */
	@Test
	public void testReplacePlaceholders()
	{
		Assert.assertEquals("A_${key1}_value", StringUtil.replacePlaceholder("A_${key1}_value"));
		
		System.setProperty("key1", "K1");
		
		Assert.assertEquals("A_K1_value", StringUtil.replacePlaceholder("A_${key1}_value"));
		Assert.assertEquals("A_K1_value", StringUtil.replacePlaceholder("A_${sys:key1}_value"));
		
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0)
		{
			String sUserProfile = System.getenv("USERPROFILE");
			
			Assert.assertEquals("ENV_" + sUserProfile, StringUtil.replacePlaceholder("ENV_${env:USERPROFILE}"));
		}
		
		Assert.assertEquals("A_${key2", StringUtil.replacePlaceholder("A_${key2"));
		Assert.assertEquals("A_${}", StringUtil.replacePlaceholder("A_${}"));
	}
	
	/**
	 * Tests the {@link StringUtil#rpad(String, int)} method.
	 */
	@Test
	public void testRpad()
	{
		Assert.assertNull(StringUtil.rpad(null, 3));
		Assert.assertEquals("   ", StringUtil.rpad("", 3));
		Assert.assertEquals("   ", StringUtil.rpad("  ", 3));
		
		Assert.assertEquals("List", StringUtil.rpad("List", 3));
		Assert.assertEquals("List      ", StringUtil.rpad("List", 10));
	}
	
	/**
	 * Tests the {@link StringUtil#rtrim(String)} method.
	 */
	@Test
	public void testRtrim()
	{
		Assert.assertNull(StringUtil.rtrim(null));
		Assert.assertEquals("", StringUtil.rtrim(""));
		Assert.assertEquals("", StringUtil.rtrim("    "));
		Assert.assertEquals("", StringUtil.rtrim("\n\t  \t \r \n"));
		
		Assert.assertEquals("A", StringUtil.rtrim("A"));
		Assert.assertEquals("AB", StringUtil.rtrim("AB"));
		Assert.assertEquals("ABC", StringUtil.rtrim("ABC"));
		
		Assert.assertEquals("abc", StringUtil.rtrim("abc"));
		Assert.assertEquals("abc", StringUtil.rtrim("abc   "));
		Assert.assertEquals("   abc", StringUtil.rtrim("   abc   "));
	}
	
	/**
	 * Tests the {@link StringUtil#sanitizeId(String)} method.
	 */
	@Test
	public void testSanitizeId()
	{
		// Should not fail.
		Assert.assertNull(StringUtil.sanitizeId(null));
		Assert.assertEquals("", StringUtil.sanitizeId(""));
		Assert.assertEquals("", StringUtil.sanitizeId("     "));
		
		// Allowed characters
		Assert.assertEquals("qwertyuiopasdfghjklzxcvbnm", StringUtil.sanitizeId("qwertyuiopasdfghjklzxcvbnm"));
		Assert.assertEquals("QWERTYUIOPASDFGHJKLZXCVBNM", StringUtil.sanitizeId("QWERTYUIOPASDFGHJKLZXCVBNM"));
		Assert.assertEquals("A1234567890", StringUtil.sanitizeId("A1234567890"));
		Assert.assertEquals("A:-_.", StringUtil.sanitizeId("A:-_."));
		
		// Not allowed characters
		Assert.assertEquals("", StringUtil.sanitizeId("`~!@#$%^&*()+="));
		Assert.assertEquals("", StringUtil.sanitizeId("[]\\}{|;'\"<>?/,"));
		Assert.assertEquals("", StringUtil.sanitizeId(" "));
		
		// Must begin with a letter
		Assert.assertEquals("", StringUtil.sanitizeId("1234567890"));
		Assert.assertEquals("B67890", StringUtil.sanitizeId("12345B67890"));
		Assert.assertEquals("b67890", StringUtil.sanitizeId("123-45:b67890"));
		
		// "Real" tests
		Assert.assertEquals("JVxVaadinWeb", StringUtil.sanitizeId("JVx Vaadin, Web"));
	}
	
	/**
	 * Tests the {@link StringUtil#separate(String, String, String, boolean)}.
	 */
	@Test
	public void testSeparate()
	{
		assertCollection(new String[] {}, StringUtil.separate(null, null, null, false));
		assertCollection(new String[] {}, StringUtil.separate("", null, null, false));
		assertCollection(new String[] { "something" }, StringUtil.separate("something", null, null, false));
		
		assertCollection(new String[] { "some[th]ing" }, StringUtil.separate("some[th]ing", "[", null, false));
		assertCollection(new String[] { "some[th]ing" }, StringUtil.separate("some[th]ing", null, "]", false));
		assertCollection(new String[] { "th" }, StringUtil.separate("some[th]ing", "[", "]", false));
		assertCollection(new String[] { "me", "th", "ng" }, StringUtil.separate("so[me][th]i[ng]", "[", "]", false));
		assertCollection(new String[] { "[me]", "[th]", "[ng]" }, StringUtil.separate("so[me][th]i[ng]", "[", "]", true));
		
		assertCollection(new String[] { "me", "th", "ng" }, StringUtil.separate("so[me][th]i[ng", "[", "]", false));
		assertCollection(new String[] { "[me]", "[th]", "[ng]" }, StringUtil.separate("so[me][th]i[ng", "[", "]", true));
		assertCollection(new String[] { "[[tag1]", "[tag2]" }, StringUtil.separate("[[tag1][tag2]", "[", "]", true));
		assertCollection(new String[] { "[tag1", "tag2" }, StringUtil.separate("[[tag1][tag2]", "[", "]", false));
	}
	
	/**
	 * Tests the {@link StringUtil#separateList(String, String, boolean)}
	 * method.
	 */
	@Test
	public void testSeparateList()
	{
		assertCollection(new String[] {}, StringUtil.separateList(null, null, false));
		assertCollection(new String[] {}, StringUtil.separateList(null, ";", true));

		assertCollection(new String[] { "" }, StringUtil.separateList("", null, false));
		assertCollection(new String[] { "abcd" }, StringUtil.separateList("abcd", null, false));
		assertCollection(new String[] { "abcd" }, StringUtil.separateList("abcd", "", false));
		
		assertCollection(new String[] { "  abcd  " }, StringUtil.separateList("  abcd  ", "", false));
		assertCollection(new String[] { "abcd" }, StringUtil.separateList("  abcd  ", "", true));
		
		assertCollection(new String[] { "" }, StringUtil.separateList("a", "a", false));
		assertCollection(new String[] { "" }, StringUtil.separateList("a", "a", true));
		
		assertCollection(new String[] { "  ", "  " }, StringUtil.separateList("  a  ", "a", false));
		assertCollection(new String[] { "", "" }, StringUtil.separateList("  a  ", "a", true));
		
		assertCollection(new String[] { "", "a", "b", "c", "d" }, StringUtil.separateList(".a.b.c.d.", ".", false));
		assertCollection(new String[] { "", "a", "b", "c", "d" }, StringUtil.separateList(".a.b.c.d.", ".", true));
		
		assertCollection(new String[] { "  a  ", "  b", "c  ", " ", " d  " }, StringUtil.separateList("  a  .  b.c  . . d  ", ".", false));
		assertCollection(new String[] { "a", "b", "c", "", "d" }, StringUtil.separateList("  a  .  b.c  . . d  ", ".", true));
		
		assertCollection(new String[] { "192", "168", "1", "100" }, StringUtil.separateList("192.168.1.100", ".", false));
		
		assertCollection(new String[] { "FontAwesome.adn", "color=orange" }, StringUtil.separateList("FontAwesome.adn;color=orange;", ";", true));
		
		assertCollection(new String[] { "" }, StringUtil.separateList("", ";", false));
		assertCollection(new String[] { "" }, StringUtil.separateList("", ";", true));
		
		assertCollection(new String[] { "/com/sibvisions/imageName.png" }, StringUtil.separateList("/com/sibvisions/imageName.png", ";", true));
	}
	
	/**
	 * Tests the {@link StringUtil#stripTags(String)} method.
	 */
	@Test
	public void testStripTags()
	{
		Assert.assertNull(StringUtil.stripTags(null));
		Assert.assertEquals("", StringUtil.stripTags(""));
		Assert.assertEquals("   	   ", StringUtil.stripTags("   	   "));
		
		Assert.assertEquals("This is a non HTML text.", StringUtil.stripTags("This is a non HTML text."));
		Assert.assertEquals("The following symbol > shouldn't be changed at all.", StringUtil.stripTags("The following symbol > shouldn't be changed at all."));
		
		Assert.assertEquals("Something", StringUtil.stripTags("<html>Something</html>"));
		Assert.assertEquals("Complicated: The Link", StringUtil.stripTags("<html><b>Complicated</b>: <a href=\"http://somewhere.com\">The Link</a></html>"));
		Assert.assertEquals("Some > thing", StringUtil.stripTags("<html>Some > thing</html>"));
		Assert.assertEquals("Some < thing", StringUtil.stripTags("<html>Some < thing</html>"));
	}
	
	/**
	 * Tests the {@link StringUtil#convertSpaceSeparated(String)} method.
	 */
	@Test
	public void testConvertSpaceSeparated()
	{
		Assert.assertEquals("REST Service", StringUtil.convertSpaceSeparated("RESTService"));
		Assert.assertEquals("a Bc De F", StringUtil.convertSpaceSeparated("aBcDeF"));
		Assert.assertEquals("Best", StringUtil.convertSpaceSeparated("Best"));
		Assert.assertEquals("rest", StringUtil.convertSpaceSeparated("rest"));
		Assert.assertEquals("AAABBB", StringUtil.convertSpaceSeparated("AAABBB"));
		Assert.assertEquals("Abra Kadabra", StringUtil.convertSpaceSeparated("Abra Kadabra"));
		Assert.assertEquals("B Be", StringUtil.convertSpaceSeparated("BBe"));
		Assert.assertEquals("e B", StringUtil.convertSpaceSeparated("eB"));
		Assert.assertEquals("B", StringUtil.convertSpaceSeparated("B"));
		Assert.assertEquals("aa", StringUtil.convertSpaceSeparated("aa"));
		Assert.assertNull(StringUtil.convertSpaceSeparated(null));
		Assert.assertEquals("   ", StringUtil.convertSpaceSeparated("   "));
		Assert.assertEquals("   A", StringUtil.convertSpaceSeparated("   A"));
		Assert.assertEquals("   Ae", StringUtil.convertSpaceSeparated("   Ae"));
		Assert.assertEquals("   e A", StringUtil.convertSpaceSeparated("   eA"));
		Assert.assertEquals("IoT", StringUtil.convertSpaceSeparated("IoT", 4));
		Assert.assertEquals("Io T", StringUtil.convertSpaceSeparated("IoT", 0));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Asserts that the given list is what is expected.
	 * 
	 * @param pExpected the expected list.
	 * @param pActual the actual list.
	 */
	private void assertCollection(String[] pExpected, List<String> pActual)
	{
		if (pExpected.length != pActual.size())
		{
			Assert.fail("Different length, expected <" + Integer.toString(pExpected.length) + "> but was <" + pActual.size() + ">.");
		}
		
		for (int index = 0; index < pExpected.length; index++)
		{
			Assert.assertEquals(pExpected[index], pActual.get(index));
		}
	}
	
}	// TestStringUtil
