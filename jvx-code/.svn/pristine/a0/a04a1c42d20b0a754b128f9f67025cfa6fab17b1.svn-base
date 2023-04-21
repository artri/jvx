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
 * 14.06.2021 - [JR] - creation
 */
package com.sibvisions.rad.server.util;

import javax.rad.persist.DataSourceException;
import javax.rad.server.IConfiguration;

import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.persist.jdbc.IDBAccess;
import com.sibvisions.rad.server.config.Configuration;
import com.sibvisions.util.Mail;
import com.sibvisions.util.Mail.Attachment;
import com.sibvisions.util.type.MailUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>ObjectUtil</code> is a server-side utility for server-side objects. 
 * 
 * @author René Jahn
 */
public class ObjectUtil 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>ObjectUtil</code> is a utility
	 * class.
	 */
	protected ObjectUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends an email. The server configuration will be used from the current session' config.
	 * 
	 * @param pMail the pre-configured mail object
	 * @param pFrom the from/sender
	 * @param pTo the to/recipient
	 * @param pCc the CC
	 * @param pBcc the BCC
	 * @param pSubject the subject
	 * @param pText the text/body
	 * @param pFileName the attachment filename
	 * @param pContent the content filename
	 * @throws Exception if sending email failed
	 */
	public static void sendMail(Mail pMail, String pFrom, String pTo, String pCc, String pBcc, String pSubject, String pText, String pFileName, Object pContent) throws Exception
	{
		sendMail(pMail, pFrom, pTo, pCc, pBcc, pSubject, pText, new Attachment[] {new Attachment(pFileName, pContent)});
	}
	
	/**
	 * Sends an email. The server configuration will be used from the current session' config.
	 * 
	 * @param pMail the pre-configured mail object
	 * @param pFrom the from/sender
	 * @param pTo the to/recipient
	 * @param pCc the CC
	 * @param pBcc the BCC
	 * @param pSubject the subject
	 * @param pText the text/body
	 * @param pAttachment optional attachment
	 * @throws Exception if sending email failed
	 * @see #createMail(IConfiguration)
	 * @see #createMail(String)
	 */
	public static void sendMail(Mail pMail, String pFrom, String pTo, String pCc, String pBcc, String pSubject, String pText, Attachment... pAttachment) throws Exception
	{
		String sFrom = pFrom;
		
		if (StringUtil.isEmpty(pFrom))
		{
			sFrom = pMail.getDefaultFrom();
		}
		
        if (!MailUtil.isValidEmail(sFrom))
        {
        	throw new IllegalArgumentException("Sending email failed because value of 'From' field is not a valid email address!");
        }
        
        if (!MailUtil.isValidEmailList(pTo))
        {
        	throw new IllegalArgumentException("Sending email failed because value of 'To' field is not a valid email address!");
        }
        
        if (!StringUtil.isEmpty(pCc) && !MailUtil.isValidEmailList(pCc))
        {
        	throw new IllegalArgumentException("Sending email failed because value of 'CC' field is not a valid email address!");
        }

        if (!StringUtil.isEmpty(pBcc) && !MailUtil.isValidEmailList(pBcc))
        {
        	throw new IllegalArgumentException("Sending email failed because value of 'BCC' field is not a valid email address!");
        }
		
		String sBody = pText;
		
        if (sBody != null && sBody.toLowerCase().startsWith("<html>"))
        {
        	pMail.setContentType(Mail.CONTENT_HTML);
        }
        
        pMail.send(pFrom, pTo, pCc, pBcc, pSubject, pText, pAttachment);
	}

	/**
	 * Creaes a new pre-configured insance of <code>Mail</code>. The configuration is read from 
	 * the application zone.
	 *
	 * @param pApplicationName the name of the application
	 * @return the mail instance
	 * @throws Exception if application is invalid
	 * @see #createMail(IConfiguration)
	 */
	public static Mail createMail(String pApplicationName) throws Exception
	{
		return createMail(Configuration.getApplicationZone(pApplicationName).getConfig());
	}
	
	/**
	 * Creates a new pre-configured instance of <code>Mail</code>. 
	 * Use following to configure sending emails:
	 * 
	 * <pre>
	 * &lt;application&gt;
	 *   ...
	 *   &lt;mail&gt;
	 *     &lt;smtp&gt;
	 *       &lt;host&gt;smtp.yourserver.com&lt;/host&gt;
	 *       &lt;port&gt;587&lt;/port&gt;
	 *       &lt;username&gt;username&lt;/username&gt;
	 *       &lt;password&gt;password&lt;/password&gt;
	 *       &lt;tlsEnabled&gt;true&lt;/tlsEnabled&gt;
	 *       &lt;defaultFrom&gt;Noreply &lt;noreply@yourserver.com&gt;&lt;/defaultFrom&gt;
	 *       &lt;defaultHtml&gt;true&lt;/defaultHtml&gt;
	 *     &lt;/smtp&gt;
	 *   &lt;/mail&gt;
	 * &lt;/application&gt;
	 * </pre> 
	 * 
	 * @param pConfig the configuration
	 * @return the mail instance
	 */
	public static Mail createMail(IConfiguration pConfig)
	{
		XmlNode ndSmtp = pConfig.getNode("/application/mail/smtp");
		
		if (ndSmtp == null)
		{
			throw new IllegalArgumentException("The SMTP server settings were not found in the application configuration!");
		}
		
		String sHost = ndSmtp.getNodeValue("host");
		String sPort = ndSmtp.getNodeValue("port");
		String sUsername = ndSmtp.getNodeValue("username");
		String sPassword = ndSmtp.getNodeValue("password");
		String sDefaultFrom = ndSmtp.getNodeValue("defaultFrom");
		
		if (sDefaultFrom == null)
		{
			//backwards compatibility
			sDefaultFrom = ndSmtp.getNodeValue("defaultsender");
		}

		String sTlsEnabled = ndSmtp.getNodeValue("tlsEnabled");
		
		if (sTlsEnabled == null)
		{
			//backwards compatibility
			sTlsEnabled = ndSmtp.getNodeValue("tlsenabled");
		}
		
		String sDefaultHtml = ndSmtp.getNodeValue("defaultHtml");
		
		boolean bTlsEnabled = Boolean.parseBoolean(sTlsEnabled) || "Y".equalsIgnoreCase(sTlsEnabled);
		boolean bHtmlEnabled = Boolean.parseBoolean(sDefaultHtml) || "Y".equalsIgnoreCase(sDefaultHtml);

		Mail mail = new Mail(sHost, sPort, sUsername, sPassword);
		mail.setTLSEnabled(bTlsEnabled);
		mail.setDefaultFrom(sDefaultFrom);
		
		if (bHtmlEnabled)
		{
			mail.setContentType(Mail.CONTENT_HTML);
		}
		
		return mail;
	}

	/**
	 * Gets whether email server is configured. The configuration is read from 
	 * the application zone.
	 * 
	 * @param pApplicationName the name of the application
	 * @return <code>true</code> if at least server and port are configured, <code>false</code> otherwise
	 * @throws Exception if application is invalid
	 */
	public static boolean isEmailConfigured(String pApplicationName) throws Exception
	{
		return isEmailConfigured(Configuration.getApplicationZone(pApplicationName).getConfig());
	}
	
	/**
	 * Gets whether email server is configured.
	 * 
	 * @param pConfig the configuration
	 * @return <code>true</code> if at least server and port are configured, <code>false</code> otherwise
	 * @see #createMail(IConfiguration)
	 */
	public static boolean isEmailConfigured(IConfiguration pConfig)
	{
		XmlNode ndSmtp = pConfig.getNode("/application/mail/smtp");
		
		if (ndSmtp != null
			&& !StringUtil.isEmpty(ndSmtp.getNodeValue("host"))
			&& !StringUtil.isEmpty(ndSmtp.getNodeValue("port")))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Creates a new instance of {@link DBStorage} for write-back operations. The created storage won't detect
	 * default or allowed values and won't join foreign key references as autolink columns.
	 * 
	 * @param pDBAccess the database access/connection
	 * @param pTable the table name which should be used
	 * @return the newly created storage
	 * @throws DataSourceException if opening storage failed
	 */
	public static DBStorage createWriteBackStorage(IDBAccess pDBAccess, String pTable) throws DataSourceException
	{
	    DBStorage dbs = new DBStorage();
	    dbs.setAutoLinkReference(false);
	    dbs.setAllowedValues(false);
	    dbs.setDefaultValue(false);
	    dbs.setDBAccess(pDBAccess);
	    dbs.setWritebackTable(pTable);
	    dbs.open();
	    
	    return dbs;
	}	
	
}	// ObjectUtil
