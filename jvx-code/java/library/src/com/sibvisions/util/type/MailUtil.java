/*
 * Copyright 2022 SIB Visions GmbH
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
 * 15.06.2022 - [JR] - creation
 */
package com.sibvisions.util.type;

import java.util.regex.Pattern;

/**
 * The <code>Mail</code> class is a utility class for sending mails.
 * 
 * @author René Jahn
 */
public final class MailUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Email Pattern. */
	private static final String EMAIL_PATTERN_REGEXP = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-+]+)*@" // First part
		+ "([A-Za-z0-9]+[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})" // "Normal" address
		+ "|\\[?.*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\]?)$"; // IP

	/** Pattern instance. */
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_REGEXP);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Invisible constructor because <code>MailUtil</code> is a utility class.
     */
    private MailUtil()
    {
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
		
	/**
	 * Check valid Email Address.
	 * 
	 * @param pEmail the email address.
	 * @return true, if the email address is valid.
	 */
	public static boolean isValidEmail(String pEmail) 
	{
		String sEmail = pEmail;
		
		if (sEmail != null)
		{
			String sRelevantPart = sEmail;
				
			sEmail = sEmail.trim();
			
			if (sEmail.endsWith(">"))
			{	
				int iPos = sEmail.lastIndexOf("<");
				
				sRelevantPart = sEmail.substring(iPos + 1, sEmail.length() - 1);
			}
			
			return EMAIL_PATTERN.matcher(sRelevantPart).matches();
		}

		return false;
	}
	
    /**
     * Validates if a list of emails is correct formatted.
     * 
     * @param pEmails contains email(s).
     * @return true, if all emails valid, false if no all emails valid.
     */
    public static boolean isValidEmailList(String pEmails)
    {
        if (pEmails.contains(";") || pEmails.contains(","))
        {
            String[] saEmails = pEmails.split("\\s*[;,]\\s*");
            
            for (String sEmail : saEmails)
            {
                if (!isValidEmail(sEmail))
                {
                    return false;
                }
            }
        }
        else
        {
            if (!isValidEmail(pEmails))
            {
                return false;
            }
        }
        
        return true;
    }	
	
}	// MailUtil
