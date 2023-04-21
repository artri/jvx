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
 * 14.10.2009 - [JR] - creation
 * 27.10.2009 - [JR] - send: parameter validation [BUGFIX]
 * 02.11.2012 - [JR] - set charset=UTF-8 for html mails
 * 21.12.2016 - [JR] - #1690: properties and TLS support
 * 07.06.2019 - [JR] - support multiple attachments
 */
package com.sibvisions.util;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.rad.io.IFileHandle;

import com.sibvisions.util.type.MailUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Mail</code> class is a utility class for sending mails.
 * 
 * @author René Jahn
 */
public final class Mail extends Authenticator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the plain text content. */
	public static final String CONTENT_TEXT = "text/plain; charset=UTF-8";
	
	/** the html content. */
	public static final String CONTENT_HTML = "text/html; charset=UTF-8";
	
	
	/** the hostname/ip of the mailserver. */
	private String sHost;
	
	/** the smtp port of the mailserver. */
	private String sPort;
	
	/** the username for smtp authentication. */
	private String sUserName;
	
	/** the password for smtp authentication. */
	private String sPassword;
	
	/** the default from if not set. */
	private String sDefaultFrom;

	/** the content type. */
	private String sContentType = CONTENT_TEXT;
	
    /** the additional mail properties. */
    private Properties props;

    /** whether TLS is enabled. */
    private boolean bTLSEnabled = false;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Mail</code> with a specific mailserver and smtp port.
	 * 
	 * @param pHost the hostname or ip address of the mailserver
	 * @param pSmtpPort the smtp port of the mailserver
	 */
	public Mail(String pHost, String pSmtpPort)
	{
		this(pHost, pSmtpPort, null, null);
	}
	
	/**
	 * Creates a new instance of <code>Mail</code> with a specific mailserver, smtp port and
	 * authentication credentials.
	 * 
	 * @param pHost the hostname or ip address of the mailserver
	 * @param pSmtpPort the smtp port of the mailserver
	 * @param pUserName the username for smtp authentication
	 * @param pPassword the password for smtp authentication
	 */
	public Mail(String pHost, String pSmtpPort, String pUserName, String pPassword)
	{
		sHost = pHost;
		sPort = pSmtpPort;
		
		sUserName = pUserName;
		sPassword = pPassword;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	protected PasswordAuthentication getPasswordAuthentication() 
	{
        return new PasswordAuthentication(sUserName, sPassword);
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost()
	{
		return sHost;
	}
	
	/**
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public String getPort()
	{
		return sPort;
	}
	
	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUserName()
	{
		return sUserName;
	}
	
	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword()
	{
		return sPassword;
	}
	
	/**
	 * Sets the content type.
	 * 
	 * @param pContentType the content type
	 */
	public void setContentType(String pContentType)
	{
		sContentType = pContentType;
	}
	
	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public String getContentType()
	{
		return sContentType;
	}
	
	/**
	 * Sets or removes a property.
	 * 
	 * @param pName the name
	 * @param pValue the value or <code>null</code> to remove the property
	 */
	public void setProperty(String pName, String pValue)
	{
	    if (pValue != null)
	    {
    	    if (props == null)
    	    {
    	        props = new Properties();
    	    }
    	    
    	    props.put(pName, pValue);
	    }
	    else if (props != null)
	    {
	        props.remove(pName);
	        
	        if (props.isEmpty())
	        {
	            props = null;
	        }
	    }
	}
	
	/**
	 * Gets the value of a property.
	 * 
	 * @param pName the name
	 * @return the value
	 */
	public Object getProperty(String pName)
	{
	    if (props != null)
	    {
	        return props.get(pName);
	    }
	    else
	    {
	        return null;
	    }
	}
	
	/**
	 * Sets whether TLS is enabled.
	 * 
	 * @param pEnable <code>true</code> to enable, <code>false</code> otherwise
	 */
	public void setTLSEnabled(boolean pEnable)
	{
	    bTLSEnabled = pEnable;
	}
	
	/**
	 * Gets whether TLS is enabled.
	 * 
	 * @return <code>true</code> if enabled, <code>false</code> otherwise
	 */
	public boolean isTLSEnabled()
	{
	    return bTLSEnabled;
	}

	/**
     * Sends a plain text mail.
     * 
     * @param pFrom the sender
     * @param pTo the recipient(s) comma separated
     * @param pSubject the subject of the message
     * @param pText the text of the message
     * @throws Exception if the send operation failed
     */
    public void send(String pFrom, String pTo, String pSubject, String pText) throws Exception
    {
        send(pFrom, pTo, null, pSubject, pText);
    }
	
	/**
	 * Sends a plain text mail.
	 * 
	 * @param pFrom the sender
	 * @param pTo the recipient(s) comma separated
	 * @param pCc the carbon copy recipient(s) comma separated
	 * @param pSubject the subject of the message
	 * @param pText the text of the message
	 * @throws Exception if the send operation failed
	 */
	public void send(String pFrom, String pTo, String pCc, String pSubject, String pText) throws Exception
	{
		send(pFrom, pTo, pCc, null, pSubject, pText);
	}
	
	/**
	 * Sends a plain text mail.
	 * 
	 * @param pFrom the sender
	 * @param pTo the recipient(s) comma separated
	 * @param pCc the carbon copy recipient(s) comma separated
	 * @param pSubject the subject of the message
	 * @param pText the text of the message
	 * @param pFileName the file name
	 * @param pContent the content
	 * @throws Exception if the send operation failed
	 * 
	 * @deprecated since 2.8.5, use {@link #send(String, String, String, String, String, String, Attachment...)} instead.
	 */
	@Deprecated
	public void send(String pFrom, String pTo, String pCc, String pSubject, String pText, String pFileName, Object pContent) throws Exception
	{
		if (isAttachment(pFileName, pContent))
		{
			send(pFrom, pTo, pCc, null, pSubject, pText, new Attachment[] {new Attachment(pFileName, pContent)});
		}
		else
		{
			send(pFrom, pTo, pCc, null, pSubject, pText);
		}
	}
	
	/**
	 * Sends an e-mail.
	 * 
	 * @param pFrom the sender
	 * @param pTo the recipient(s) comma separated
	 * @param pCc the carbon copy recipient(s) comma separated
	 * @param pBcc the carbon copy recipient(s) comma separated
	 * @param pSubject the subject of the message
	 * @param pText the text of the message
	 * @param pFileName the file name
	 * @param pContent the content
	 * @throws Exception if the send operation failed
	 * 
	 * @deprecated since 2.8.5, use {@link #send(String, String, String, String, String, String, Attachment...)} instead.
	 */
	@Deprecated
	public void send(String pFrom, String pTo, String pCc, String pBcc, String pSubject, String pText, String pFileName, Object pContent) throws Exception
	{
		if (isAttachment(pFileName, pContent))
		{
			send(pFrom, pTo, pCc, pBcc, pSubject, pText, new Attachment[] {new Attachment(pFileName, pContent)});
		}
		else
		{
			send(pFrom, pTo, pCc, pBcc, pSubject, pText);
		}
	}
		
	/**
	 * Sends an e-mail.
	 * 
	 * @param pFrom the sender
	 * @param pTo the recipient(s) comma separated
	 * @param pCc the carbon copy recipient(s) comma separated
	 * @param pBcc the carbon copy recipient(s) comma separated
	 * @param pSubject the subject of the message
	 * @param pText the text of the message
	 * @param pAttachment the list of attachments
	 * @throws Exception if the send operation failed
	 */
	public void send(String pFrom, String pTo, String pCc, String pBcc, String pSubject, String pText, Attachment... pAttachment) throws Exception
	{
		//Validation
		if (StringUtil.isEmpty(sHost))
		{
			throw new IllegalArgumentException("Sending email failed because 'Server' is empty!");
		}

		if (StringUtil.isEmpty(sPort))
		{
			throw new IllegalArgumentException("Sending email failed because 'Port' is empty!");
		}
		
		String sFrom = StringUtil.evl(pFrom, sDefaultFrom);
		
		if (StringUtil.isEmpty(sFrom))
		{
			throw new IllegalArgumentException("Sending email failed because value of 'From' field is empty!");
		}
		
		if (StringUtil.isEmpty(pTo))
		{
			throw new IllegalArgumentException("Sending email failed because value of 'To' field is empty!");
		}
		
		Properties prop = new Properties();
		
    	prop.put("mail.smtp.host", sHost);
        prop.put("mail.smtp.port", sPort);
        
        Authenticator auth = null;
        
        if (sUserName != null)
        {
            auth = this;
            
            prop.put("mail.smtp.auth", "true");
        }
        else
        {
        	prop.put("mail.smtp.auth", "false");
        }
        
        if (bTLSEnabled)
        {
            prop.put("mail.smtp.starttls.enable", "true");
        }

        //custom properties - allows overriding
        if (props != null)
        {
            prop.putAll(props);
        }

        //comma separated!
        String sTo = pTo.replace(";", ",");
        
        Session sess = Session.getInstance(prop, auth);

        InternetAddress iaTo = new InternetAddress(sFrom, false);
        iaTo.setPersonal(iaTo.getPersonal(), "UTF-8");
        
        MimeMessage mess = new MimeMessage(sess);
  		mess.setFrom(iaTo);
  		mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sTo, false));

  		if (pCc != null)
  		{
  			String sCc = pCc.replace(";", ",");
  			
      		mess.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sCc, false));
  		}
  		
  		if (pBcc != null)
  		{
  			String sBcc = pBcc.replace(";", ",");
  			
      		mess.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(sBcc, false));
  		}
  		
  		mess.setSubject(MimeUtility.encodeText(pSubject, "UTF-8", "Q"));
  		
  		boolean bValidAttach = false;
  		
  		if (pAttachment != null)
  		{
	  		for (int i = 0; i < pAttachment.length && !bValidAttach; i++)
	  		{
	  			bValidAttach = pAttachment[i].isValid();
	  		}
  		}
  		
  		if (!bValidAttach)
  		{
  		    mess.setContent(pText, sContentType);
  		}
  		else
  		{
	  		BodyPart messageBodyPart = new MimeBodyPart();
		  	messageBodyPart.setContent(pText, sContentType);
		
		  	Multipart multipart = new MimeMultipart();
		  	multipart.addBodyPart(messageBodyPart);
		  	
		  	if (pAttachment != null)
		  	{
		  		for (int i = 0; i < pAttachment.length; i++)
		  		{
		  			if (pAttachment[i].isValid())
		  			{
				  		String sFileName = pAttachment[i].fileName;
				  		Object content = pAttachment[i].content;
			  		
				  		DataSource source = null;
			  		
				  		if (content instanceof DataSource)
				  		{
				  			source = (DataSource)content;
				  			
				  			if (StringUtil.isEmpty(sFileName))
				  			{
				  				sFileName = source.getName();
				  			}
				  		}
				  		else if (content instanceof IFileHandle)
				  		{
				  			if (StringUtil.isEmpty(sFileName))
				  			{
				  				sFileName = ((IFileHandle)content).getFileName();
				  			}
				  			
				  			source = new ByteArrayDataSource(sFileName, ((IFileHandle)content).getInputStream());
				  		}
				  		else if (content instanceof File)
				  		{
				  			if (StringUtil.isEmpty(sFileName))
				  			{
				  				sFileName = ((File)content).getName();
				  			}
				  			
				  			source = new FileDataSource(((File)content));
				  		}
				  		else if (!StringUtil.isEmpty(sFileName))
				  		{
				  			if (content instanceof InputStream)
					  		{
				  				source = new ByteArrayDataSource(sFileName, (InputStream)content);
					  		}
					  		else if (content instanceof byte[])
					  		{
					  			source = new ByteArrayDataSource(sFileName, (byte[])content);
					  		}
					  		else if (content instanceof Blob)
					  		{
					  		    source = new ByteArrayDataSource(sFileName, ((Blob)content).getBinaryStream());
					  		}			  		
				  		}
					
					  	messageBodyPart = new MimeBodyPart();
					  	messageBodyPart.setDataHandler(new DataHandler(source));
					  	messageBodyPart.setFileName(sFileName);
				  	
					  	multipart.addBodyPart(messageBodyPart);
		  			}
		  		}
		  	}
		  	
		  	mess.setContent(multipart);
  		}
  		
  		mess.setHeader("X-Mailer", Mail.class.getName());
  		mess.setSentDate(new Date());

		Transport.send(mess);
	}
	
	/**
	 * Gets whether the given information defines a valid attachment.
	 *  
	 * @param pFileName the file/attachment name
	 * @param pContent the content
	 * @return <code>true</code> if given information defines a valid attachment, <code>false</code> otherwise
	 * @throws Exception if content can't be read
	 */
	static boolean isAttachment(String pFileName, Object pContent) throws Exception
	{
  		if (pContent instanceof DataSource)
  		{
  			DataSource source = (DataSource)pContent;
  			
  			if (StringUtil.isEmpty(pFileName))
  			{
  				return source.getName() != null;
  			}
  			else
  			{
  				return true;
  			}
  		}
  		else if (pContent instanceof IFileHandle)
  		{
  			return ((IFileHandle)pContent).getFileName() != null;
  		}
  		else if (pContent instanceof File)
  		{
  			return ((File)pContent).exists();
  		}
  		else if (!StringUtil.isEmpty(pFileName))
  		{
  			if (pContent instanceof InputStream)
	  		{
  				return true;
	  		}
	  		else if (pContent instanceof byte[])
	  		{
	  			return true;
	  		}
	  		else if (pContent instanceof Blob)
	  		{
	  			return true;
	  		}
  		}
  		
  		return false;
	}
	
	/**
	 * Sets default from/sender.
	 * 
	 * @param pFrom the default from/sender
	 */
	public void setDefaultFrom(String pFrom)
	{
		sDefaultFrom = pFrom;
	}
	
	/**
	 * Gets the default from/sender.
	 * 
	 * @return the default from/sender
	 */
	public String getDefaultFrom()
	{
		return sDefaultFrom;
	}
	
	/**
	 * Check valid Email Address.
	 * 
	 * @param pEmail the email address.
	 * @return true, if the email address is valid.
	 * @deprecated use {@link MailUtil#isValidEmail(String)}
	 */
	@Deprecated
	public static boolean isValidEmail(String pEmail) 
	{
		return MailUtil.isValidEmail(pEmail);
	}
	
    /**
     * Validates if a list of emails is correct formatted.
     * 
     * @param pEmails contains email(s).
     * @return true, if all emails valid, false if no all emails valid.
     * @deprecated use {@link MailUtil#isValidEmailList(String)}
     */
	@Deprecated	
    public static boolean isValidEmailList(String pEmails)
    {
		return MailUtil.isValidEmailList(pEmails);
    }	
    
    /**
     * Creates attachments.
     * 
     * @param pFileNamePattern If not all file names specified, this pattern will be used for generating the file names.
     * @param pFileNames attachment file names
     * @param pContents attachment files
     * @return array of attachments
     */
    public static Attachment[] createAttachments(String pFileNamePattern, Object[] pFileNames, Object[] pContents)
    {
        int iAttachmentCount = 0;
        
        Attachment[] aaAttachments;
        
        if (pContents != null && pContents.length > 0)
        {
            boolean bGenerateFileNames = false;
            
            if (pFileNames.length == 0 
            	&& !StringUtil.isEmpty(pFileNamePattern)
                && (pFileNamePattern.lastIndexOf(".") > 0 
                    && pFileNamePattern.lastIndexOf(".") < pFileNamePattern.length()))
            {
                bGenerateFileNames = true;
            }
            
            if (pFileNames.length == 0 && StringUtil.isEmpty(pFileNamePattern))
            {
                throw new IllegalArgumentException("Sending email failed because attachment name is missing!");
            }
            
            if (pFileNames.length > 0 && (pFileNames.length != pContents.length))
            {
                throw new IllegalArgumentException("Sending email failed because number of attachment names is different than the number of attachments!");
            }
            
            if (pFileNames.length == 0 && !bGenerateFileNames)
            {
                throw new IllegalArgumentException("Sending email failed because attachment name is missing!");
            }
            
            aaAttachments = new Attachment[pContents.length];
            
            Attachment attachment;

            for (int i = 0; i < pContents.length; i++)
            {
                if (pContents[i] == null || (!bGenerateFileNames && pFileNames[i] == null))
                {
                    continue;
                }
                
                if (bGenerateFileNames)
                {
                    String sFileName;
                    
                    if (iAttachmentCount > 0)
                    {
                        int iDotPosition = pFileNamePattern.lastIndexOf(".");
                        
                        sFileName = pFileNamePattern.substring(0, iDotPosition) + iAttachmentCount + pFileNamePattern.substring(iDotPosition);
                    }
                    else
                    {
                        sFileName = pFileNamePattern;
                    }
                    
                    attachment = new Attachment(sFileName, pContents[i]);
                }
                else
                {
                    attachment = new Attachment((String)pFileNames[i], pContents[i]);
                }
                
                aaAttachments[iAttachmentCount++] = attachment;
            }
        }
        else
        {
            return null;
        }
        
        return ArrayUtil.truncate(aaAttachments, iAttachmentCount);
    }	    
	
	//****************************************************************
	// Subclass definition
	//****************************************************************

	/**
	 * The <code>Attachment</code> is a simple container for an email attachment definition.
	 * 
	 * @author René Jahn
	 */
	public static final class Attachment
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/** the filename. */
		private String fileName;
		/** the content. */
		private Object content;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Attachment</code>.
		 * 
		 * @param pFileName the file/attachment name
		 * @param pContent the content
		 */
		public Attachment(String pFileName, Object pContent)
		{
			fileName = pFileName;
			content  = pContent;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets the file/attachment name.
		 * 
		 * @return the name
		 */
		public String getFileName()
		{
			return fileName;
		}
		
		/**
		 * Gets the content.
		 * 
		 * @return the content
		 */
		public Object getContent()
		{
			return content;
		}
		
		/**
		 * Gets whether this attachment is a valid one.
		 * 
		 * @return <code>true</code> if attachment is valid, <code>false</code> otherwise
		 */
		public boolean isValid()
		{
			try
			{
				return Mail.isAttachment(fileName, content);
			}
			catch (Exception e)
			{
				return false;
			}
		}
		
	}	// Attachment
	
}	// Mail


