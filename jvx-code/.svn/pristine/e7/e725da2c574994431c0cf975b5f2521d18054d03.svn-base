/*
 * Copyright 2021 SIB Visions GmbH
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
 * 15.06.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.security.reset;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import javax.rad.remote.IConnectionConstants;
import javax.rad.server.IConfiguration;
import javax.rad.server.ISession;
import javax.rad.util.TranslationMap;

import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.util.ObjectUtil;
import com.sibvisions.util.Mail;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DefaultOneTimePasswordHandler</code> is a simple {@link IOneTimePasswordHandler} and 
 * sends new passwords per email.
 * 
 * @author René Jahn
 */
public class DefaultOneTimePasswordHandler implements IOneTimePasswordHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DefaultOneTimePasswordHandler</code>.
	 */
	public DefaultOneTimePasswordHandler()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendOneTimePassword(ISession pSession, UserInfo pUser, String pPassword) throws Exception
	{
		Mail mail = ObjectUtil.createMail(pSession.getConfig());
		//always html
		mail.setContentType(Mail.CONTENT_HTML);

		
		String sLang;
		
		sLang = LocaleUtil.getLanguage((String)pSession.getProperty(IConnectionConstants.PREFIX_SERVER + IConnectionConstants.PREFIX_SESSION + "language"));
		
		if (sLang == null)
		{
			//NOT LocaleUtil because it's a UI class
			sLang = Locale.getDefault().getLanguage();
		}
		
		IConfiguration cfg = pSession.getConfig();
		
		String sPath = cfg.getProperty("/application/securitymanager/otphandler/searchpath");
		
		if (StringUtil.isEmpty(sPath))
		{
			//default search path
			sPath = "/com/sibvisions/rad/server/security/reset/";
		}
		else if (!sPath.endsWith("/") && !sPath.endsWith("\\"))
		{
			sPath += "/";
		}
		
		TranslationMap tmap = new TranslationMap();

		//language specific translation
        InputStream isTranslation = ResourceUtil.getResourceAsStream(sPath + "translation_otp_" + sLang + ".xml");

        try
        {
	        //default translation
	        if (isTranslation == null)
	        {
	        	isTranslation = ResourceUtil.getResourceAsStream(sPath + "translation_otp.xml");
	            tmap.setLanguage(sLang);
	        }
	
			if (isTranslation != null)
	        {
	            Properties properties = new Properties();
	
	            //don't use the isTranslation because we have problems with some browser plugin versions
	            //(stream closed exceptions)
	            properties.loadFromXML(new BufferedInputStream(isTranslation));
	            
	            tmap.setAsProperties(properties);
	        }
        }
        finally
        {
        	CommonUtil.close(isTranslation);
        }
		
        //E-Mail template
		InputStream isEmail = ResourceUtil.getResourceAsStream(sPath + "resetpassword_" + sLang + ".html");

		if (isEmail == null)
		{
			//use default file
			isEmail = ResourceUtil.getResourceAsStream(sPath + "resetpassword.html");
		}
		
		String sText;
		
		if (isEmail != null)
		{
			ByteArrayOutputStream osEmail = new ByteArrayOutputStream();
			
			Hashtable<String, String> htMapping = new Hashtable<String, String>();
			htMapping.put("[OTPASSWORD]", pPassword);
			
			FileUtil.replace(isEmail, osEmail, htMapping, "UTF-8", "UTF-8");
			
			sText = osEmail.toString("UTF-8");
		}
		else
		{
			sText = "<html><head></head><body>" + tmap.translate("One-time password:") + " " + pPassword + "</body></html>"; 
		}
		
		ObjectUtil.sendMail(mail, null, pUser.getEmailAddress(), null, null, tmap.translate("Your new password"), sText);
	}
	
}	// DefaultOneTimePasswordHandler
