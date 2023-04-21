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
 * 06.12.2008 - [HM] - creation
 */
package javax.rad.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the <code>TranslationMap</code>.
 * 
 * @author Martin Handsteiner
 * @see TranslationMap
 */
public class TestTranslationMap
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests addTranslation method.
	 */
	@Test
	public void testAddTranslation()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("Hallo", "Helo");
		
		Assert.assertTrue(map.size() == 1);
	}
	
	/**
	 * Tests translate method.
	 */
	@Test
	public void testTranslate()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("Hallo", "Helo");
		map.put("Martin", "Marteu");
		map.put("Hallo *, wie geht es Dir?", "Helo *, how are you?");
		map.put("Application.About.info.application*", "Application information*");
		map.put("ORA-20000: *\nORA*", "*");
		map.put("Hallo * wie geht es Dir Herr *?", "Helo *1, how are you Sir *0?");
		map.put("A * B * C", "a *1 b * c");
		map.put("D * E * F", "d *2 e *1 f *0");
		
		Assert.assertEquals("Marteu", map.translate("Martin"));
		Assert.assertEquals("Liese", map.translate("Liese"));

		Assert.assertEquals("Helo Liese, how are you?", map.translate("Hallo Liese, wie geht es Dir?"));
		Assert.assertEquals("Helo Marteu, how are you?", map.translate("Hallo Martin, wie geht es Dir?"));
		
		Assert.assertEquals("Application information (ApplicationName)", map.translate("Application.About.info.application (ApplicationName)"));
		Assert.assertEquals("So darf das nicht sein!", map.translate("ORA-20000: So darf das nicht sein!\nORA-000364 Hallo wie gehts"));
		
		Assert.assertEquals("Helo Handsteiner, how are you Sir Marteu?", map.translate("Hallo Martin wie geht es Dir Herr Handsteiner?"));

		Assert.assertEquals("a Y b Y c", map.translate("A X B Y C"));

		Assert.assertEquals("d  e Y f X", map.translate("D X E Y F"));
	}

	/**
	 * Tests bug 4711.
	 */
	@Test
	public void testBug4711()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("ORA-00001: Unique Constraint (RFID.*) verletzt*", "UK Verletzt * !");
		
		Assert.assertEquals("UK Verletzt Hallo.Col !", map.translate("ORA-00001: Unique Constraint (RFID.Hallo.Col) verletzt\nin der 47. zeile!"));
		
	}
	
	/**
	 * Tests bug 0815.
	 */
	@Test
	public void testBug0815()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("*Test", "*Martin");
		
		Assert.assertEquals("Mein Martin", map.translate("Mein Test"));
		
	}
	
	/**
	 * Tests bug 0816.
	 */
	@Test
	public void testBug0816()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("Invalid password for '*' and application '*'", "Ungültiges Passwort für Benutzer '*0'");
		map.put("Invali'*' and application '*'", "Ungültiges Passwort für Benutzer '*0'");
		map.put("User '*' was not found for application '*'", "Der Benutzer '*0' ist unbekannt!");
		
		Assert.assertEquals("Ungültiges Passwort für Benutzer 'llc'", map.translate("Invalid password for 'llc' and application 'llc'"));
		Assert.assertEquals("Ungültiges Passwort für Benutzer 'llc'", map.translate("Invali'llc' and application 'llc'"));
		Assert.assertEquals("Der Benutzer 'llc1' ist unbekannt!", map.translate("User 'llc1' was not found for application 'lsmf'"));
		
	}
	
	/**
	 * Tests feature 0411.
	 */
	@Test
	public void testFeature0411()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("DanubeSteamShippingCaption", "DonauDampfschifffahrtskapität");

		map.put("Translate * with and * without subtranslation!", "Übersetze * mit und ~* ohne erneuter Übersetzung!");
		
		Assert.assertEquals("Übersetze DonauDampfschifffahrtskapität mit und DanubeSteamShippingCaption ohne erneuter Übersetzung!", 
				map.translate("Translate DanubeSteamShippingCaption with and DanubeSteamShippingCaption without subtranslation!"));
	}
	
	/**
	 * Tests feature 0411.
	 */
	@Test
	public void testPreferTranslationsWithLessWildcards()
	{
		TranslationMap map = new TranslationMap();
		
		map.put("*.setFilter(a)", "A");
		map.put("*.setFilter(*)", "B");

		Assert.assertEquals("A", map.translate("filter.setFilter(a)"));
	}
	
	
}	// TestTranslationMap
