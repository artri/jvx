/*
 * Copyright 2019 SIB Visions GmbH
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
 * 22.05.2019 - [HM] - creation
 */
package com.sibvisions.util.type;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.Internalize;

/**
 * The <code>TimeZoneUtil</code> is a utility for changing timezone different from local default.
 *  
 * @author Martin Handsteiner
 */
public final class TimeZoneUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The country timezones. */
	private static final String[][] COUNTRYCODE_TIMEZONEIDS = new String[][]
	   {{"AU", "ACT"}, {"AU", "AET"}, {"AR", "AGT"}, {"EG", "ART"}, 
		{"US", "AST"}, {"CI", "Africa/Abidjan"}, {"GH", "Africa/Accra"}, {"ET", "Africa/Addis_Ababa"}, 
		{"DZ", "Africa/Algiers"}, {"ER", "Africa/Asmara"}, {"ER", "Africa/Asmera"}, {"ML", "Africa/Bamako"}, 
		{"CF", "Africa/Bangui"}, {"GM", "Africa/Banjul"}, {"GW", "Africa/Bissau"}, {"MW", "Africa/Blantyre"}, 
		{"CG", "Africa/Brazzaville"}, {"BI", "Africa/Bujumbura"}, {"EG", "Africa/Cairo"}, {"MA", "Africa/Casablanca"}, 
		{"ES", "Africa/Ceuta"}, {"GN", "Africa/Conakry"}, {"SN", "Africa/Dakar"}, {"TZ", "Africa/Dar_es_Salaam"}, 
		{"DJ", "Africa/Djibouti"}, {"CM", "Africa/Douala"}, {"EH", "Africa/El_Aaiun"}, {"SL", "Africa/Freetown"}, 
		{"BW", "Africa/Gaborone"}, {"ZW", "Africa/Harare"}, {"ZA", "Africa/Johannesburg"}, {"SS", "Africa/Juba"}, 
		{"UG", "Africa/Kampala"}, {"SD", "Africa/Khartoum"}, {"RW", "Africa/Kigali"}, {"CD", "Africa/Kinshasa"}, 
		{"NG", "Africa/Lagos"}, {"GA", "Africa/Libreville"}, {"TG", "Africa/Lome"}, {"AO", "Africa/Luanda"}, 
		{"CD", "Africa/Lubumbashi"}, {"ZM", "Africa/Lusaka"}, {"GQ", "Africa/Malabo"}, {"MZ", "Africa/Maputo"}, 
		{"LS", "Africa/Maseru"}, {"SZ", "Africa/Mbabane"}, {"SO", "Africa/Mogadishu"}, {"LR", "Africa/Monrovia"}, 
		{"KE", "Africa/Nairobi"}, {"TD", "Africa/Ndjamena"}, {"NE", "Africa/Niamey"}, {"MR", "Africa/Nouakchott"}, 
		{"BF", "Africa/Ouagadougou"}, {"BJ", "Africa/Porto-Novo"}, {"ST", "Africa/Sao_Tome"}, {"CI", "Africa/Timbuktu"}, 
		{"LY", "Africa/Tripoli"}, {"TN", "Africa/Tunis"}, {"NA", "Africa/Windhoek"}, {"US", "America/Adak"}, 
		{"US", "America/Anchorage"}, {"AI", "America/Anguilla"}, {"AG", "America/Antigua"}, {"BR", "America/Araguaina"}, 
		{"AR", "America/Argentina/Buenos_Aires"}, {"AR", "America/Argentina/Catamarca"}, {"AR", "America/Argentina/ComodRivadavia"}, {"AR", "America/Argentina/Cordoba"}, 
		{"AR", "America/Argentina/Jujuy"}, {"AR", "America/Argentina/La_Rioja"}, {"AR", "America/Argentina/Mendoza"}, {"AR", "America/Argentina/Rio_Gallegos"}, 
		{"AR", "America/Argentina/Salta"}, {"AR", "America/Argentina/San_Juan"}, {"AR", "America/Argentina/San_Luis"}, {"AR", "America/Argentina/Tucuman"}, 
		{"AR", "America/Argentina/Ushuaia"}, {"AW", "America/Aruba"}, {"PY", "America/Asuncion"}, {"CA", "America/Atikokan"}, 
		{"US", "America/Atka"}, {"BR", "America/Bahia"}, {"MX", "America/Bahia_Banderas"}, {"BB", "America/Barbados"}, 
		{"BR", "America/Belem"}, {"BZ", "America/Belize"}, {"CA", "America/Blanc-Sablon"}, {"BR", "America/Boa_Vista"}, 
		{"CO", "America/Bogota"}, {"US", "America/Boise"}, {"AR", "America/Buenos_Aires"}, {"CA", "America/Cambridge_Bay"}, 
		{"BR", "America/Campo_Grande"}, {"MX", "America/Cancun"}, {"VE", "America/Caracas"}, {"AR", "America/Catamarca"}, 
		{"GF", "America/Cayenne"}, {"KY", "America/Cayman"}, {"US", "America/Chicago"}, {"MX", "America/Chihuahua"}, 
		{"CA", "America/Coral_Harbour"}, {"AR", "America/Cordoba"}, {"CR", "America/Costa_Rica"}, {"CA", "America/Creston"}, 
		{"BR", "America/Cuiaba"}, {"CW", "America/Curacao"}, {"GL", "America/Danmarkshavn"}, {"CA", "America/Dawson"}, 
		{"CA", "America/Dawson_Creek"}, {"US", "America/Denver"}, {"US", "America/Detroit"}, {"DM", "America/Dominica"}, 
		{"CA", "America/Edmonton"}, {"BR", "America/Eirunepe"}, {"SV", "America/El_Salvador"}, {"MX", "America/Ensenada"}, 
		{"CA", "America/Fort_Nelson"}, {"US", "America/Fort_Wayne"}, {"BR", "America/Fortaleza"}, {"CA", "America/Glace_Bay"}, 
		{"GL", "America/Godthab"}, {"CA", "America/Goose_Bay"}, {"TC", "America/Grand_Turk"}, {"GD", "America/Grenada"}, 
		{"GP", "America/Guadeloupe"}, {"GT", "America/Guatemala"}, {"EC", "America/Guayaquil"}, {"GY", "America/Guyana"}, 
		{"CA", "America/Halifax"}, {"CU", "America/Havana"}, {"MX", "America/Hermosillo"}, {"US", "America/Indiana/Indianapolis"}, 
		{"US", "America/Indiana/Knox"}, {"US", "America/Indiana/Marengo"}, {"US", "America/Indiana/Petersburg"}, {"US", "America/Indiana/Tell_City"}, 
		{"US", "America/Indiana/Vevay"}, {"US", "America/Indiana/Vincennes"}, {"US", "America/Indiana/Winamac"}, {"US", "America/Indianapolis"}, 
		{"CA", "America/Inuvik"}, {"CA", "America/Iqaluit"}, {"JM", "America/Jamaica"}, {"AR", "America/Jujuy"}, 
		{"US", "America/Juneau"}, {"US", "America/Kentucky/Louisville"}, {"US", "America/Kentucky/Monticello"}, {"US", "America/Knox_IN"}, 
		{"BQ", "America/Kralendijk"}, {"BO", "America/La_Paz"}, {"PE", "America/Lima"}, {"US", "America/Los_Angeles"}, 
		{"US", "America/Louisville"}, {"SX", "America/Lower_Princes"}, {"BR", "America/Maceio"}, {"NI", "America/Managua"}, 
		{"BR", "America/Manaus"}, {"MF", "America/Marigot"}, {"MQ", "America/Martinique"}, {"MX", "America/Matamoros"}, 
		{"MX", "America/Mazatlan"}, {"AR", "America/Mendoza"}, {"US", "America/Menominee"}, {"MX", "America/Merida"}, 
		{"US", "America/Metlakatla"}, {"MX", "America/Mexico_City"}, {"PM", "America/Miquelon"}, {"CA", "America/Moncton"}, 
		{"MX", "America/Monterrey"}, {"UY", "America/Montevideo"}, {"CA", "America/Montreal"}, {"MS", "America/Montserrat"}, 
		{"BS", "America/Nassau"}, {"US", "America/New_York"}, {"CA", "America/Nipigon"}, {"US", "America/Nome"}, 
		{"BR", "America/Noronha"}, {"US", "America/North_Dakota/Beulah"}, {"US", "America/North_Dakota/Center"}, {"US", "America/North_Dakota/New_Salem"}, 
		{"MX", "America/Ojinaga"}, {"PA", "America/Panama"}, {"CA", "America/Pangnirtung"}, {"SR", "America/Paramaribo"}, 
		{"US", "America/Phoenix"}, {"HT", "America/Port-au-Prince"}, {"TT", "America/Port_of_Spain"}, {"BR", "America/Porto_Acre"}, 
		{"BR", "America/Porto_Velho"}, {"PR", "America/Puerto_Rico"}, {"CL", "America/Punta_Arenas"}, {"CA", "America/Rainy_River"}, 
		{"CA", "America/Rankin_Inlet"}, {"BR", "America/Recife"}, {"CA", "America/Regina"}, {"CA", "America/Resolute"}, 
		{"BR", "America/Rio_Branco"}, {"AR", "America/Rosario"}, {"MX", "America/Santa_Isabel"}, {"BR", "America/Santarem"}, 
		{"CL", "America/Santiago"}, {"DO", "America/Santo_Domingo"}, {"BR", "America/Sao_Paulo"}, {"GL", "America/Scoresbysund"}, 
		{"US", "America/Shiprock"}, {"US", "America/Sitka"}, {"BL", "America/St_Barthelemy"}, {"CA", "America/St_Johns"}, 
		{"KN", "America/St_Kitts"}, {"LC", "America/St_Lucia"}, {"VI", "America/St_Thomas"}, {"VC", "America/St_Vincent"}, 
		{"CA", "America/Swift_Current"}, {"HN", "America/Tegucigalpa"}, {"GL", "America/Thule"}, {"CA", "America/Thunder_Bay"}, 
		{"MX", "America/Tijuana"}, {"CA", "America/Toronto"}, {"VG", "America/Tortola"}, {"CA", "America/Vancouver"}, 
		{"TT", "America/Virgin"}, {"CA", "America/Whitehorse"}, {"CA", "America/Winnipeg"}, {"US", "America/Yakutat"}, 
		{"CA", "America/Yellowknife"}, {"AQ", "Antarctica/Casey"}, {"AQ", "Antarctica/Davis"}, {"AQ", "Antarctica/DumontDUrville"}, 
		{"AU", "Antarctica/Macquarie"}, {"AQ", "Antarctica/Mawson"}, {"AQ", "Antarctica/McMurdo"}, {"AQ", "Antarctica/Palmer"}, 
		{"AQ", "Antarctica/Rothera"}, {"NZ", "Antarctica/South_Pole"}, {"AQ", "Antarctica/Syowa"}, {"AQ", "Antarctica/Troll"}, 
		{"AQ", "Antarctica/Vostok"}, {"SJ", "Arctic/Longyearbyen"}, {"YE", "Asia/Aden"}, {"KZ", "Asia/Almaty"}, 
		{"JO", "Asia/Amman"}, {"RU", "Asia/Anadyr"}, {"KZ", "Asia/Aqtau"}, {"KZ", "Asia/Aqtobe"}, 
		{"TM", "Asia/Ashgabat"}, {"TM", "Asia/Ashkhabad"}, {"KZ", "Asia/Atyrau"}, {"IQ", "Asia/Baghdad"}, 
		{"BH", "Asia/Bahrain"}, {"AZ", "Asia/Baku"}, {"TH", "Asia/Bangkok"}, {"RU", "Asia/Barnaul"}, 
		{"LB", "Asia/Beirut"}, {"KG", "Asia/Bishkek"}, {"BN", "Asia/Brunei"}, {"IN", "Asia/Calcutta"}, 
		{"RU", "Asia/Chita"}, {"MN", "Asia/Choibalsan"}, {"CN", "Asia/Chongqing"}, {"CN", "Asia/Chungking"}, 
		{"LK", "Asia/Colombo"}, {"BD", "Asia/Dacca"}, {"SY", "Asia/Damascus"}, {"BD", "Asia/Dhaka"}, 
		{"TL", "Asia/Dili"}, {"AE", "Asia/Dubai"}, {"TJ", "Asia/Dushanbe"}, {"CY", "Asia/Famagusta"}, 
		{"PS", "Asia/Gaza"}, {"CN", "Asia/Harbin"}, {"PS", "Asia/Hebron"}, {"VN", "Asia/Ho_Chi_Minh"}, 
		{"HK", "Asia/Hong_Kong"}, {"MN", "Asia/Hovd"}, {"RU", "Asia/Irkutsk"}, {"TR", "Asia/Istanbul"}, 
		{"ID", "Asia/Jakarta"}, {"ID", "Asia/Jayapura"}, {"IL", "Asia/Jerusalem"}, {"AF", "Asia/Kabul"}, 
		{"RU", "Asia/Kamchatka"}, {"PK", "Asia/Karachi"}, {"CN", "Asia/Kashgar"}, {"NP", "Asia/Kathmandu"}, 
		{"NP", "Asia/Katmandu"}, {"RU", "Asia/Khandyga"}, {"IN", "Asia/Kolkata"}, {"RU", "Asia/Krasnoyarsk"}, 
		{"MY", "Asia/Kuala_Lumpur"}, {"MY", "Asia/Kuching"}, {"KW", "Asia/Kuwait"}, {"MO", "Asia/Macao"}, 
		{"MO", "Asia/Macau"}, {"RU", "Asia/Magadan"}, {"ID", "Asia/Makassar"}, {"PH", "Asia/Manila"}, 
		{"OM", "Asia/Muscat"}, {"CY", "Asia/Nicosia"}, {"RU", "Asia/Novokuznetsk"}, {"RU", "Asia/Novosibirsk"}, 
		{"RU", "Asia/Omsk"}, {"KZ", "Asia/Oral"}, {"KH", "Asia/Phnom_Penh"}, {"ID", "Asia/Pontianak"}, 
		{"KP", "Asia/Pyongyang"}, {"QA", "Asia/Qatar"}, {"KZ", "Asia/Qostanay"}, {"KZ", "Asia/Qyzylorda"}, 
		{"MM", "Asia/Rangoon"}, {"SA", "Asia/Riyadh"}, {"VN", "Asia/Saigon"}, {"RU", "Asia/Sakhalin"}, 
		{"UZ", "Asia/Samarkand"}, {"KR", "Asia/Seoul"}, {"CN", "Asia/Shanghai"}, {"SG", "Asia/Singapore"}, 
		{"RU", "Asia/Srednekolymsk"}, {"TW", "Asia/Taipei"}, {"UZ", "Asia/Tashkent"}, {"GE", "Asia/Tbilisi"}, 
		{"IR", "Asia/Tehran"}, {"IL", "Asia/Tel_Aviv"}, {"BT", "Asia/Thimbu"}, {"BT", "Asia/Thimphu"}, 
		{"JP", "Asia/Tokyo"}, {"RU", "Asia/Tomsk"}, {"ID", "Asia/Ujung_Pandang"}, {"MN", "Asia/Ulaanbaatar"}, 
		{"MN", "Asia/Ulan_Bator"}, {"CN", "Asia/Urumqi"}, {"RU", "Asia/Ust-Nera"}, {"LA", "Asia/Vientiane"}, 
		{"RU", "Asia/Vladivostok"}, {"RU", "Asia/Yakutsk"}, {"MM", "Asia/Yangon"}, {"RU", "Asia/Yekaterinburg"}, 
		{"AM", "Asia/Yerevan"}, {"PT", "Atlantic/Azores"}, {"BM", "Atlantic/Bermuda"}, {"ES", "Atlantic/Canary"}, 
		{"CV", "Atlantic/Cape_Verde"}, {"FO", "Atlantic/Faeroe"}, {"FO", "Atlantic/Faroe"}, {"NO", "Atlantic/Jan_Mayen"}, 
		{"PT", "Atlantic/Madeira"}, {"IS", "Atlantic/Reykjavik"}, {"GS", "Atlantic/South_Georgia"}, {"SH", "Atlantic/St_Helena"}, 
		{"FK", "Atlantic/Stanley"}, {"AU", "Australia/ACT"}, {"AU", "Australia/Adelaide"}, {"AU", "Australia/Brisbane"}, 
		{"AU", "Australia/Broken_Hill"}, {"AU", "Australia/Canberra"}, {"AU", "Australia/Currie"}, {"AU", "Australia/Darwin"}, 
		{"AU", "Australia/Eucla"}, {"AU", "Australia/Hobart"}, {"AU", "Australia/LHI"}, {"AU", "Australia/Lindeman"}, 
		{"AU", "Australia/Lord_Howe"}, {"AU", "Australia/Melbourne"}, {"AU", "Australia/NSW"}, {"AU", "Australia/North"}, 
		{"AU", "Australia/Perth"}, {"AU", "Australia/Queensland"}, {"AU", "Australia/South"}, {"AU", "Australia/Sydney"}, 
		{"AU", "Australia/Tasmania"}, {"AU", "Australia/Victoria"}, {"AU", "Australia/West"}, {"AU", "Australia/Yancowinna"}, 
		{"BR", "BET"}, {"BD", "BST"}, {"BR", "Brazil/Acre"}, {"BR", "Brazil/DeNoronha"}, 
		{"BR", "Brazil/East"}, {"BR", "Brazil/West"}, {"MZ", "CAT"}, {"CA", "CNT"}, 
		{"US", "CST"}, {"CN", "CTT"}, {"CA", "Canada/Atlantic"}, {"CA", "Canada/Central"}, 
		{"CA", "Canada/East-Saskatchewan"}, {"CA", "Canada/Eastern"}, {"CA", "Canada/Mountain"}, {"CA", "Canada/Newfoundland"}, 
		{"CA", "Canada/Pacific"}, {"CA", "Canada/Saskatchewan"}, {"CA", "Canada/Yukon"}, {"CL", "Chile/Continental"}, 
		{"CL", "Chile/EasterIsland"}, {"CU", "Cuba"}, {"KE", "EAT"}, {"FR", "ECT"}, 
		{"EG", "Egypt"}, {"IE", "Eire"}, {"NL", "Europe/Amsterdam"}, {"AD", "Europe/Andorra"}, 
		{"RU", "Europe/Astrakhan"}, {"GR", "Europe/Athens"}, {"GB", "Europe/Belfast"}, {"RS", "Europe/Belgrade"}, 
		{"DE", "Europe/Berlin"}, {"SK", "Europe/Bratislava"}, {"BE", "Europe/Brussels"}, {"RO", "Europe/Bucharest"}, 
		{"HU", "Europe/Budapest"}, {"DE", "Europe/Busingen"}, {"MD", "Europe/Chisinau"}, {"DK", "Europe/Copenhagen"}, 
		{"IE", "Europe/Dublin"}, {"GI", "Europe/Gibraltar"}, {"GG", "Europe/Guernsey"}, {"FI", "Europe/Helsinki"}, 
		{"IM", "Europe/Isle_of_Man"}, {"TR", "Europe/Istanbul"}, {"JE", "Europe/Jersey"}, {"RU", "Europe/Kaliningrad"}, 
		{"UA", "Europe/Kiev"}, {"RU", "Europe/Kirov"}, {"PT", "Europe/Lisbon"}, {"SI", "Europe/Ljubljana"}, 
		{"GB", "Europe/London"}, {"LU", "Europe/Luxembourg"}, {"ES", "Europe/Madrid"}, {"MT", "Europe/Malta"}, 
		{"AX", "Europe/Mariehamn"}, {"BY", "Europe/Minsk"}, {"MC", "Europe/Monaco"}, {"RU", "Europe/Moscow"}, 
		{"CY", "Europe/Nicosia"}, {"NO", "Europe/Oslo"}, {"FR", "Europe/Paris"}, {"ME", "Europe/Podgorica"}, 
		{"CZ", "Europe/Prague"}, {"LV", "Europe/Riga"}, {"IT", "Europe/Rome"}, {"RU", "Europe/Samara"}, 
		{"SM", "Europe/San_Marino"}, {"BA", "Europe/Sarajevo"}, {"RU", "Europe/Saratov"}, {"UA", "Europe/Simferopol"}, 
		{"MK", "Europe/Skopje"}, {"BG", "Europe/Sofia"}, {"SE", "Europe/Stockholm"}, {"EE", "Europe/Tallinn"}, 
		{"AL", "Europe/Tirane"}, {"MD", "Europe/Tiraspol"}, {"RU", "Europe/Ulyanovsk"}, {"UA", "Europe/Uzhgorod"}, 
		{"LI", "Europe/Vaduz"}, {"VA", "Europe/Vatican"}, {"AT", "Europe/Vienna"}, {"LT", "Europe/Vilnius"}, 
		{"RU", "Europe/Volgograd"}, {"PL", "Europe/Warsaw"}, {"HR", "Europe/Zagreb"}, {"UA", "Europe/Zaporozhye"}, 
		{"CH", "Europe/Zurich"}, {"GB", "GB"}, {"GB", "GB-Eire"}, {"HK", "Hongkong"}, 
		{"US", "IET"}, {"IN", "IST"}, {"IS", "Iceland"}, {"MG", "Indian/Antananarivo"}, 
		{"IO", "Indian/Chagos"}, {"CX", "Indian/Christmas"}, {"CC", "Indian/Cocos"}, {"KM", "Indian/Comoro"}, 
		{"TF", "Indian/Kerguelen"}, {"SC", "Indian/Mahe"}, {"MV", "Indian/Maldives"}, {"MU", "Indian/Mauritius"}, 
		{"YT", "Indian/Mayotte"}, {"RE", "Indian/Reunion"}, {"IR", "Iran"}, {"IL", "Israel"}, 
		{"JP", "JST"}, {"JM", "Jamaica"}, {"JP", "Japan"}, {"MH", "Kwajalein"}, 
		{"LY", "Libya"}, {"WS", "MIT"}, {"MX", "Mexico/BajaNorte"}, {"MX", "Mexico/BajaSur"}, 
		{"MX", "Mexico/General"}, {"AM", "NET"}, {"NZ", "NST"}, {"NZ", "NZ"}, 
		{"NZ", "NZ-CHAT"}, {"US", "Navajo"}, {"PK", "PLT"}, {"US", "PNT"}, 
		{"CN", "PRC"}, {"PR", "PRT"}, {"US", "PST"}, {"WS", "Pacific/Apia"}, 
		{"NZ", "Pacific/Auckland"}, {"PG", "Pacific/Bougainville"}, {"NZ", "Pacific/Chatham"}, {"FM", "Pacific/Chuuk"}, 
		{"CL", "Pacific/Easter"}, {"VU", "Pacific/Efate"}, {"KI", "Pacific/Enderbury"}, {"TK", "Pacific/Fakaofo"}, 
		{"FJ", "Pacific/Fiji"}, {"TV", "Pacific/Funafuti"}, {"EC", "Pacific/Galapagos"}, {"PF", "Pacific/Gambier"}, 
		{"SB", "Pacific/Guadalcanal"}, {"GU", "Pacific/Guam"}, {"US", "Pacific/Honolulu"}, {"UM", "Pacific/Johnston"}, 
		{"KI", "Pacific/Kiritimati"}, {"FM", "Pacific/Kosrae"}, {"MH", "Pacific/Kwajalein"}, {"MH", "Pacific/Majuro"}, 
		{"PF", "Pacific/Marquesas"}, {"UM", "Pacific/Midway"}, {"NR", "Pacific/Nauru"}, {"NU", "Pacific/Niue"}, 
		{"NF", "Pacific/Norfolk"}, {"NC", "Pacific/Noumea"}, {"AS", "Pacific/Pago_Pago"}, {"PW", "Pacific/Palau"}, 
		{"PN", "Pacific/Pitcairn"}, {"FM", "Pacific/Pohnpei"}, {"FM", "Pacific/Ponape"}, {"PG", "Pacific/Port_Moresby"}, 
		{"CK", "Pacific/Rarotonga"}, {"MP", "Pacific/Saipan"}, {"AS", "Pacific/Samoa"}, {"PF", "Pacific/Tahiti"}, 
		{"KI", "Pacific/Tarawa"}, {"TO", "Pacific/Tongatapu"}, {"FM", "Pacific/Truk"}, {"UM", "Pacific/Wake"}, 
		{"WF", "Pacific/Wallis"}, {"FM", "Pacific/Yap"}, {"PL", "Poland"}, {"PT", "Portugal"}, 
		{"TW", "ROC"}, {"KR", "ROK"}, {"SB", "SST"}, {"SG", "Singapore"}, 
		{"TR", "Turkey"}, {"US", "US/Alaska"}, {"US", "US/Aleutian"}, {"US", "US/Arizona"}, 
		{"US", "US/Central"}, {"US", "US/East-Indiana"}, {"US", "US/Eastern"}, {"US", "US/Hawaii"}, 
		{"US", "US/Indiana-Starke"}, {"US", "US/Michigan"}, {"US", "US/Mountain"}, {"US", "US/Pacific"}, 
		{"US", "US/Pacific-New"}, {"AS", "US/Samoa"}, {"VN", "VST"}, {"RU", "W-SU"}};
	
	/** The time zone list for a country code. */
	private static Map<String, Set<String>> countryCodeTimeZoneIds = new HashMap<String, Set<String>>(); 
		
	/** The country codes for a time zone. */
	private static Map<String, Set<String>> timeZoneIdCountryCodes = new HashMap<String, Set<String>>(); 
		
	/** The default time zone. */
	private static TimeZone defaultTimeZone;
    /** The default system time zone. */
    private static TimeZone cachedSystemDefaultTimeZone;

	/** The default thread time zone. */
	private static ThreadLocal<TimeZone> threadDefaultTimeZone = new ThreadLocal<TimeZone>();
	
	/** The default thread calendar. */
	private static ThreadLocal<HashMap<Locale, HashMap<TimeZone, Calendar>>> threadCalendar = new ThreadLocal<HashMap<Locale, HashMap<TimeZone, Calendar>>>();
	
	/** Reduce internalize. */
	private static boolean shouldInternalize = true;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static
	{
		resetCountryTimeZones();
	}
	
	/**
	 * Creating a TimeZoneUtil is not allowed.
	 */
	private TimeZoneUtil()
	{
		// Utility Class
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Resets the country time zones to default.
	 */
	public static void resetCountryTimeZones()
	{
		countryCodeTimeZoneIds.clear();
		timeZoneIdCountryCodes.clear();
		
		for (String[] countryCodeTimeZoneId : COUNTRYCODE_TIMEZONEIDS)
		{
			String countryCode = countryCodeTimeZoneId[0];
			String timeZoneId = countryCodeTimeZoneId[1];
			
			addCountryTimeZone(countryCode, timeZoneId);
		}
	}

	/**
	 * Adds a time zone to the given country.
	 * 
	 * @param pCountryCode the country code.
	 * @param pTimeZoneId the time zone id.
	 */
	public static void addCountryTimeZone(String pCountryCode, String pTimeZoneId)
	{
		Set<String> timeZoneIds = countryCodeTimeZoneIds.get(pCountryCode);
		if (timeZoneIds == null)
		{
			timeZoneIds = new HashSet<String>();
			countryCodeTimeZoneIds.put(pCountryCode, timeZoneIds);
		}
		timeZoneIds.add(pTimeZoneId);
		
		Set<String> countryCodes = timeZoneIdCountryCodes.get(pTimeZoneId);
		if (countryCodes == null)
		{
			countryCodes = new HashSet<String>();
			timeZoneIdCountryCodes.put(pTimeZoneId, countryCodes);
		}
		countryCodes.add(pCountryCode);
	}
	
	/**
	 * Removes a time zone from the given country.
	 * 
	 * @param pCountryCode the country code.
	 * @param pTimeZoneId the time zone id.
	 */
	public static void removeCountryTimeZone(String pCountryCode, String pTimeZoneId)
	{
		Set<String> timeZoneIds = countryCodeTimeZoneIds.get(pCountryCode);
		if (timeZoneIds != null)
		{
			timeZoneIds.remove(pTimeZoneId);
			if (timeZoneIds.size() == 0)
			{
				countryCodeTimeZoneIds.remove(pCountryCode);
			}
		}
		Set<String> countryCodes = timeZoneIdCountryCodes.get(pTimeZoneId);
		if (countryCodes != null)
		{
			countryCodes.remove(pCountryCode);
			if (countryCodes.size() == 0)
			{
				timeZoneIdCountryCodes.remove(pTimeZoneId);
			}
		}
	}
	
	/**
	 * Gets the time zone ids for the given country code.
	 * 
	 * @param pCountryCode the country code.
	 * @return the time zone ids.
	 */
	public static String[] getTimeZoneIdsForCountryCode(String pCountryCode)
	{
		Set<String> timeZoneIds = countryCodeTimeZoneIds.get(pCountryCode);

		if (timeZoneIds == null)
		{
			return new String[0];
		}
		else
		{
			return timeZoneIds.toArray(new String[timeZoneIds.size()]);
		}
	}

	/**
	 * Gets the time zone ids for the given language code.
	 * 
	 * @param pLanguageCode the language code.
	 * @return the time zone ids.
	 */
	public static String[] getTimeZoneIdsForLanguageCode(String pLanguageCode)
	{
		Set<String> timeZoneIds = new HashSet<String>();
		
		for (String countryCode : LocaleUtil.getCountryCodesForLanguageCode(pLanguageCode))
		{
			Set<String> tzIds = countryCodeTimeZoneIds.get(countryCode);
			if (tzIds != null)
			{
				timeZoneIds.addAll(tzIds);
			}
		}
		
		return timeZoneIds.toArray(new String[timeZoneIds.size()]);
	}
	
	/**
	 * Gets the country codes for the given time zone id.
	 * 
	 * @param pTimeZoneId the timezone id. 
	 * @return the country codes.
	 */
	public static String[] getCountryCodesForTimeZoneId(String pTimeZoneId)
	{
		Set<String> countryCodes = timeZoneIdCountryCodes.get(pTimeZoneId);
		
		if (countryCodes == null)
		{
			return new String[0];
		}
		else
		{
			return countryCodes.toArray(new String[countryCodes.size()]);
		}
	}
	
	/**
	 * Formats the offset in millis as String in format +HH:mm or -HH:mm.
	 * 
	 * @param pOffsetInMillis the offset in millis.
	 * @return the String in format +HH:mm or -HH:mm.
	 */
	public static String formatOffsetInMillis(int pOffsetInMillis)
	{
		return (pOffsetInMillis >= 0 ? "+" : "-") + 
		        String.valueOf(Math.abs(pOffsetInMillis) / 3600000 + 100).substring(1) +
		        String.valueOf((Math.abs(pOffsetInMillis) / 60000) % 60 + 100).substring(1);
	}

	/**
	 * Gets the offset in millis from the given formatted offset.
	 * @param pFormattedOffset the formatted offset
	 * @return the offset in millis
	 */
	public static int getOffsetInMillis(String pFormattedOffset)
	{
		if (pFormattedOffset.startsWith("GMT"))
		{
			return TimeZone.getTimeZone(pFormattedOffset).getRawOffset();
		}
		else
		{
			return TimeZone.getTimeZone("GMT" + pFormattedOffset).getRawOffset();
		}
	}
	
	/**
	 * Gets all matching time zone ids for the given locale and rawoffset in millis.
	 * 
	 * @param pRawOffsetInMillis the rawoffset in millis
	 * @param pLocale the locale
	 * @return all matching time zone ids
	 */
	public static String[] getTimeZoneIds(int pRawOffsetInMillis, Locale pLocale)
	{
		String country = pLocale.getCountry();
		String language = pLocale.getLanguage();
		
		String[] availableLocaleIds;
		if ("".equals(country))
		{
			availableLocaleIds = getTimeZoneIdsForLanguageCode(language);
		}
		else
		{
			availableLocaleIds = getTimeZoneIdsForCountryCode(country);
		}
		
		String[] availableOffsetIds = TimeZone.getAvailableIDs(pRawOffsetInMillis);
		
		return ArrayUtil.intersect(availableOffsetIds, availableLocaleIds);
	}

	/**
	 * Gets the best match time zone id.
	 * If there are more available time zone ids the first of the country is used.
	 * If there is no matching time zone id, a time zone id for the current offset in millis is created with GMT+HH:mm.
	 * 
	 * @param pRawOffsetInMillis the raw offset in millis.
	 * @param pLocale the locale.
	 * @param pCurrentOffsetInMillis the current offset in millis.
	 * @return the best match time zone.
	 */
	public static String getBestMatchTimeZoneId(int pRawOffsetInMillis, Locale pLocale, int pCurrentOffsetInMillis)
	{
		String[] availableIds = getTimeZoneIds(pRawOffsetInMillis, pLocale);
		
		if (availableIds.length > 0)
		{
			return availableIds[0];
		}
		else
		{
		    long now = System.currentTimeMillis();
		    
		    TimeZone defTimeZone = TimeZone.getDefault();
		    String defID = defTimeZone.getID(); 
		    if (defTimeZone.getRawOffset() == pRawOffsetInMillis
		            && defTimeZone.getOffset(now) == pCurrentOffsetInMillis)
		    {
		        return defID;
		    }
		    
		    int regionIndex = defID.indexOf('/');
		    String region = regionIndex < 0 ? null : defID.substring(0, regionIndex);
		    
		    String anyMatchingTimeZone = null;
		    String matchingTimeZoneWithoutRegion = null;
		    String matchingTimeZoneWithRegion = null;
		    
	        String[] availableOffsetIds = TimeZone.getAvailableIDs(pRawOffsetInMillis);
	        for (String id : availableOffsetIds)
	        {
	            TimeZone zone = TimeZone.getTimeZone(id);
	            if (zone.getRawOffset() == pRawOffsetInMillis
	                    && zone.getOffset(now) == pCurrentOffsetInMillis)
	            {
	                if (anyMatchingTimeZone == null)
	                {
	                    anyMatchingTimeZone = id;
	                }
	                int regIndex = id.indexOf('/');
	                if (regIndex >= 0)
	                {
	                    if (id.substring(0, regIndex).equals(region))
	                    {
	                        matchingTimeZoneWithRegion = id;
	                        if (matchingTimeZoneWithoutRegion != null)
	                        {
	                            break;
	                        }
	                    }
	                }
	                else if (matchingTimeZoneWithoutRegion == null)
	                {
	                    matchingTimeZoneWithoutRegion = id;
                        if (matchingTimeZoneWithRegion != null)
                        {
                            break;
                        }
	                }
	            }
	        }

	        if (matchingTimeZoneWithRegion != null)
	        {
	            return matchingTimeZoneWithRegion;
	        }
	        if (matchingTimeZoneWithoutRegion != null)
	        {
	            return matchingTimeZoneWithoutRegion;
	        }
	        if (anyMatchingTimeZone != null)
	        {
	            return anyMatchingTimeZone;
	        }
	        
			return "GMT" + formatOffsetInMillis(pCurrentOffsetInMillis);
		}
	}
	
	/**
	 * Get a time zone parsing the time zone tag.
	 * 
	 * @param pTimeZoneTag the language tag.
	 * @return the TimeZone.
	 */
	@SuppressWarnings("unused")
	public static TimeZone forTimeZoneId(String pTimeZoneTag)
	{
		if (pTimeZoneTag == null || pTimeZoneTag.length() == 0)
		{
			return getDefault();
		}
		else
		{
	        getDefault(); // Default time zone should be base of intern functionality.
			return Internalize.intern(TimeZone.getTimeZone(pTimeZoneTag));
		}
	}
	
    /**
     * Get a time zone for the zone id.
     * 
     * @param pZoneId the zone id.
     * @return the TimeZone.
     */
    @SuppressWarnings("unused")
    public static TimeZone forTimeZoneId(ZoneId pZoneId)
    {
        if (pZoneId == null)
        {
            return getDefault();
        }
        else
        {
            getDefault(); // Default time zone should be base of intern functionality.
            return Internalize.intern(TimeZone.getTimeZone(pZoneId));
        }
    }
    
	/**
	 * Gets the default time zone. If no locale is set, the default time zone is returned.
	 * 
	 * @return the default time zone.
	 */
	public static TimeZone getDefault()
	{
		TimeZone timeZone = threadDefaultTimeZone.get();
		if (timeZone != null)
		{
		    if (shouldInternalize)
		    {
                shouldInternalize = false;
		        Internalize.intern(timeZone);
		    }
			return timeZone;
		}
		if (defaultTimeZone != null)
		{
            if (shouldInternalize)
            {
                shouldInternalize = false;
                Internalize.intern(defaultTimeZone);
            }
			return defaultTimeZone; 
		}
		
		timeZone = TimeZone.getDefault();
		if (!timeZone.equals(cachedSystemDefaultTimeZone))
		{
		    Internalize.intern(timeZone);

		    cachedSystemDefaultTimeZone = timeZone;
		}
		
		return cachedSystemDefaultTimeZone;
	}
	
    /**
     * Gets the calendar for the given time zone and local.
     * 
     * @param pTimeZone the time zone
     * @param pLocale the locale
     * @return the calendar for the given time zone and local.
     */
    public static Calendar getCalendar(TimeZone pTimeZone, Locale pLocale)
    {
        if (pLocale == null)
        {
            pLocale = LocaleUtil.getDefault();
        }
        if (pTimeZone == null)
        {
            pTimeZone = getDefault();
        }

        HashMap<Locale, HashMap<TimeZone, Calendar>> calendars = threadCalendar.get();
        if (calendars == null)
        {
            calendars = new HashMap<Locale, HashMap<TimeZone, Calendar>>();
            
            threadCalendar.set(calendars);
        }
        
        HashMap<TimeZone, Calendar> localeCalendars = calendars.get(pLocale);
        if (localeCalendars == null)
        {
            localeCalendars = new HashMap<TimeZone, Calendar>();
            
            calendars.put(pLocale, localeCalendars);
        }
        
        Calendar calendar = localeCalendars.get(pTimeZone);
        if (calendar == null)
        {
            calendar = Calendar.getInstance(pTimeZone, pLocale);
            
            localeCalendars.put(pTimeZone, calendar);
        }
        
        calendar.setTimeInMillis(System.currentTimeMillis());

        return calendar;
    }

    /**
     * Gets the calendar for the given time zone and local.
     * 
     * @param pTimeZone the time zone
     * @return the calendar for the given time zone and local.
     */
    public static Calendar getCalendar(TimeZone pTimeZone)
    {
        return getCalendar(pTimeZone, null);
    }
    
	/**
	 * Gets the default calendar for the default time zone.
	 * 
	 * @return the default calendar
	 */
	public static Calendar getDefaultCalendar()
	{
	    return getCalendar(null, null);
	}

	/**
	 * Gets the default time zone. If no locale is set, the default time zone is returned.
	 * 
	 * @param pTimeZone the default time zone.
	 */
	public static void setDefault(TimeZone pTimeZone)
	{
		defaultTimeZone = pTimeZone;
		shouldInternalize = true;
	}
	
	/**
	 * Gets the default time zone. If no locale is set, the default time zone is returned.
	 * 
	 * @return the default time zone.
	 */
	public static TimeZone getThreadDefault()
	{
		return threadDefaultTimeZone.get();
	}
	
	/**
	 * Gets the default time zone. If no locale is set, the default time zone is returned.
	 * 
	 * @param pTimeZone the default time zone.
	 */
	public static void setThreadDefault(TimeZone pTimeZone)
	{
		threadDefaultTimeZone.set(pTimeZone);
		shouldInternalize = true;
	}

//	/**
//	 * Gets a <code>LocalDateTime</code> which matches exact the <code>Date</code> or <code>Timestamp</code> including time
//	 * in the default time zone of TimeZoneUtil.
//	 * 
//	 * @param pDate <code>Date</code> or <code>Timestamp</code> instance
//	 * @return a <code>LocalDateTime</code>
//	 */
//	public static LocalDateTime getLocalDateTime(Date pDate)
//	{
////	    LocalDateTime.ofInstant(pDate.toInstant(), getDefaultZoneId())); // Calculates wrong <1894 and >2038, Nanos have to be set extra
//	    
//		Calendar cal = getDefaultCalendar();
//		cal.setTime(pDate);
//		
//		return LocalDateTime.of(cal.get(Calendar.YEAR),
//				cal.get(Calendar.MONTH) + 1,
//				cal.get(Calendar.DAY_OF_MONTH),
//				cal.get(Calendar.HOUR_OF_DAY),
//				cal.get(Calendar.MINUTE),
//				cal.get(Calendar.SECOND),
//				pDate instanceof Timestamp ? ((Timestamp)pDate).getNanos() : cal.get(Calendar.MILLISECOND) * 1000000);
//	}
//
//	/**
//	 * Gets a <code>Timestamp</code> which matches exact the <code>LocalDateTime</code> including time
//	 * in the default time zone of TimeZoneUtil.
//	 * 
//	 * @param pLocalDateTime the <code>LocalDateTime</code> 
//	 * @return a <code>Timestamp</code>
//	 */
//	public static Timestamp getTimestamp(LocalDateTime pLocalDateTime)
//	{
////      new Timestamp(pLocalDateTime.atZone(getDefaultZoneId()).toInstant().toEpochMilli()); // Calculates wrong <1894 and >2038, Nanos have to be set extra
//
//        Calendar cal = getDefaultCalendar();
//		cal.set(pLocalDateTime.getYear(), 
//				pLocalDateTime.getMonthValue() - 1, 
//				pLocalDateTime.getDayOfMonth(), 
//				pLocalDateTime.getHour(), 
//				pLocalDateTime.getMinute(), 
//				pLocalDateTime.getSecond());
//		
//		Timestamp result = new Timestamp(cal.getTimeInMillis());
//		result.setNanos(pLocalDateTime.getNano());
//		
//		return result;
//	}

}	// TimeZoneUtil
