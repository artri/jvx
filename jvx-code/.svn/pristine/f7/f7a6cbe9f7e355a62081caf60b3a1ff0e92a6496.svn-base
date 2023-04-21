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
 * 14.11.2009 - [JR] - creation
 * 20.12.2009 - [JR] - #55: username != password (notequaluser) implemented
 */
package com.sibvisions.rad.server.security.validation;

import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;

/**
 * The <code>DefaultPasswordValidator</code> validates a password and uses following options:
 * <ul>
 *   <li>Minimum length (default: 5)</li>
 *   <li>Must contain at least one number (default: false)</li>
 *   <li>Must contain at least one letter (default: false)</li>
 *   <li>Must contain at least one special character (space, dot, ...) (default: false)</li>
 *   <li>Must contain upper and lower case letters (default: false)</li>
 *   <li>Password not equal to the username (defult: false)</li>
 * </ul>
 * to check the strength of a password.
 * If you want to use the <code>DefaultPasswordValidator</code> then you should specify it in
 * your configuration as follows:
 * <pre>
 * &lt;securitymanager&gt;
 *   &lt;class&gt;...&lt;/class&gt;
 *   &lt;passwordvalidator&gt;
 *     &lt;class&gt;com.sibvisions.rad.server.security.validation.DefaultPasswordValidator&lt;/class&gt;
 *     &lt;minlength&gt;n&lt;/minlength&gt;
 *     &lt;digit&gt;true | false&lt;/digit&gt;
 *     &lt;letter&gt;true | false&lt;/letter&gt;
 *     &lt;specialchar&gt;true | false&lt;/specialchar&gt;
 *     &lt;mixedcase&gt;true | false&lt;/mixedcase&gt;
 *     &lt;notequaluser&gt;true | false&lt;/notequaluser&gt;
 *   &lt;/passwordvalidator&gt;
 * &lt;/securitymanager&gt;
 * </pre>
 *
 * @author René Jahn
 * @see IPasswordValidator
 */
public class DefaultPasswordValidator implements IPasswordValidator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void checkPassword(ISession pSession, String pPassword)
	{
		IConfiguration cfg = pSession.getConfig();
		
		int iMinLength;
		
		boolean bSpecialChar;
		boolean bDigit;
		boolean bLetter;
		boolean bMixedCase;
		boolean bNotEqual;
		
		//------------------------------------------------------------
		// get default/user-defined validation properties
		//------------------------------------------------------------
		
		try
		{
			iMinLength = Integer.parseInt(cfg.getProperty("/application/securitymanager/passwordvalidator/minlength", "5"));	
		}
		catch (Exception ex)
		{
			iMinLength = 5;
		}

		bSpecialChar = Boolean.valueOf(cfg.getProperty("/application/securitymanager/passwordvalidator/specialchar", "false")).booleanValue();
		bDigit       = Boolean.valueOf(cfg.getProperty("/application/securitymanager/passwordvalidator/digit", "false")).booleanValue();
		bLetter      = Boolean.valueOf(cfg.getProperty("/application/securitymanager/passwordvalidator/letter", "false")).booleanValue();
		bMixedCase   = Boolean.valueOf(cfg.getProperty("/application/securitymanager/passwordvalidator/mixedcase", "false")).booleanValue();
		bNotEqual    = Boolean.valueOf(cfg.getProperty("/application/securitymanager/passwordvalidator/notequaluser", "false")).booleanValue();
		
		//------------------------------------------------------------
		// validation
		//------------------------------------------------------------

		String sNewPassword;
		
		if (pPassword == null)
		{
			sNewPassword = "";
		}
		else
		{
			sNewPassword = pPassword;
		}
		
		boolean bError = false;

		if (bNotEqual && sNewPassword.equals(pSession.getUserName()))
		{
			bError = true;
		}
		
		if (sNewPassword.length() < iMinLength)
		{
			bError = true;
		}
		
		if (!bError && (bSpecialChar || bDigit || bLetter))
		{
			int iDigits = 0;
			int iLetters = 0;
			int iSpecial = 0;
			int iLower = 0;
			int iUpper = 0;
			
			char ch;
			
			for (int i = 0; i < sNewPassword.length(); i++)
			{
				ch = sNewPassword.charAt(i);
				
				if (ch >= 'A' && ch <= 'Z')
				{
					iLetters++;
					iUpper++;
				}
				else if (ch >= 'a' && ch <= 'z')
				{
					iLetters++;
					iLower++;
				}
				else if (ch >= '0' && ch <= '9')
				{
					iDigits++;
				}
				else 
				{
					iSpecial++;
				}
			}
			
			if (bDigit && iDigits == 0)
			{
				bError = true;
			}
			
			if (bLetter)
			{
				if (iLetters == 0)
				{
					bError = true;
				}
				
				if (bMixedCase && (iUpper == 0 || iLower == 0))
				{
					bError = true;
				}
			}
			
			if (bSpecialChar && iSpecial == 0)
			{
				bError = true;
			}
		}
		
		if (bError)
		{
			StringBuffer sbfMessage = new StringBuffer("The new password should comply with the following rules: ");

			if (iMinLength > 0)
			{
				sbfMessage.append("Minimum length = ");
				sbfMessage.append(iMinLength);
				sbfMessage.append(", ");
			}
			
			if (bDigit)
			{
				sbfMessage.append("at least one digit, ");
			}
			
			if (bLetter)
			{
				if (bMixedCase)
				{
					sbfMessage.append("at least two letters (mixed case), ");
				}
				else
				{
					sbfMessage.append("at least one letter, ");
				}
			}
			
			if (bSpecialChar)
			{
				sbfMessage.append("at least one special character, ");
			}

			if (bNotEqual)
			{
				sbfMessage.append("Password is not equal to the Username");
			}
			
			if (',' == sbfMessage.charAt(sbfMessage.length() - 2))
			{
				throw new SecurityException(sbfMessage.substring(0, sbfMessage.length() - 2));
			}
			else
			{
				throw new SecurityException(sbfMessage.toString());
			}
		}
	}

}	// DefaultPasswordValidator
