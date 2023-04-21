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
 * 01.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

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
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;
import com.sibvisions.rad.server.util.ObjectUtil;
import com.sibvisions.util.Mail;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.LocaleUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>DefaultTextInputNotificationHandler</code> is a simple {@link IPayloadNotificationHandler} and 
 * sends MFA authentication confirmation code per email.
 * 
 * @author René Jahn
 */
public class DefaultTextInputNotificationHandler implements IPayloadNotificationHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DefaultTextInputNotificationHandler</code>.
	 */
	public DefaultTextInputNotificationHandler()
	{
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendNotification(ISession pSession, UserInfo pUser, AccessToken pToken, String pCode) throws Exception
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
		
		String sPath = cfg.getProperty("/application/securitymanager/mfa/authenticator/searchpath");
		
		if (StringUtil.isEmpty(sPath))
		{
			//default search path
			sPath = "/com/sibvisions/rad/server/security/mfa/auth/";
		}
		else if (!sPath.endsWith("/") && !sPath.endsWith("\\"))
		{
			sPath += "/";
		}
		
		TranslationMap tmap = new TranslationMap();

		//language specific translation
        InputStream isTranslation = ResourceUtil.getResourceAsStream(sPath + "translation_mfa_" + sLang + ".xml");

        try
        {
	        //default translation
	        if (isTranslation == null)
	        {
	        	isTranslation = ResourceUtil.getResourceAsStream(sPath + "translation_mfa.xml");
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
		InputStream isEmail = ResourceUtil.getResourceAsStream(sPath + "confirmationcode_" + sLang + ".html");

		if (isEmail == null)
		{
			//use default file
			isEmail = ResourceUtil.getResourceAsStream(sPath + "confirmationcode.html");
		}
		
		String sText;
		
		int iSeconds = Math.round(pToken.getTimeout() / 60000f);
		
		if (isEmail != null)
		{
			ByteArrayOutputStream osEmail = new ByteArrayOutputStream();
			
			Hashtable<String, String> htMapping = new Hashtable<String, String>();
			htMapping.put("[CONFIRMATION_CODE]", pCode);
			htMapping.put("[TIMEOUT]", "" + iSeconds);
			
			FileUtil.replace(isEmail, osEmail, htMapping, "UTF-8", "UTF-8");
			
			sText = osEmail.toString("UTF-8");
		}
		else
		{
			sText = "<html><head></head><body>" + tmap.translate("Confirmation code:") + " " + pCode + tmap.translate("is valid for") + 
					" " + iSeconds + " " + tmap.translate("seconds") + "</body></html>"; 
		}
		
		ObjectUtil.sendMail(mail, null, pUser.getEmailAddress(), null, null, tmap.translate("Your confirmation code"), sText);
	}
	
}	// DefaultTextInputNotificationHandler
