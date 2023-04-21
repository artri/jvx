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
 * 25.05.2009 - [JR] - creation
 * 04.10.2009 - [JR] - OLDPASSWORD constant
 * 23.02.2010 - [JR] - #18: PROPERTY_CLASSES defined
 * 08.03.2012 - [JR] - #556: PREFIX_ENVPROP defined
 * 15.10.2013 - [JR] - SESSIONTIMEOUT_AS_SECONDS defined
 * 08.04.2014 - [JR] - ISOLATION defined
 * 07.10.2014 - [JR] - TIME constants defined
 */
package javax.rad.remote;

import java.util.Date;

/**
 * The <code>IConnectionConstants</code> defines constants for the
 * connection handling between client and server.
 *  
 * @author René Jahn
 */
public interface IConnectionConstants
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** allowed transferable connection property classes. */
	public static final Class<?>[] PROPERTY_CLASSES = {String.class, Character.class, Boolean.class,
          											   Byte.class, Long.class, Integer.class,
          											   Short.class, Double.class, Float.class,
          											   Date.class};
	
	/** the prefix for client properties. */
	public static final String PREFIX_CLIENT                = "client.";

	/** the prefix for server properties. */
	public static final String PREFIX_SERVER                = "server.";

	/** the prefix for session properties. */
	public static final String PREFIX_SESSION               = "session.";

	/** the prefix for system properties. */
	public static final String PREFIX_SYSPROP               = "sysprop.";

	/** the prefix for environment properties. */
	public static final String PREFIX_ENVPROP               = "envprop.";
	        
	/** the prefix for request properties. */
	public static final String PREFIX_REQUEST               = "request.";

	/** the prefix for factory properties. */
	public static final String PREFIX_FACTORY               = "factory:";

	
	/** the key for the application name. */
	public static final String APPLICATION 		            = PREFIX_CLIENT + "application";

	/** the key for the user name. */
	public static final String USERNAME 		            = PREFIX_CLIENT + "username";
	
	/** the key for the password. */ 
	public static final String PASSWORD  		            = PREFIX_CLIENT + "password";
	
	/** the key for the old password. */
	public static final String OLDPASSWORD 		            = PREFIX_CLIENT + "oldpassword";

	/** the key for the new password. */
	public static final String NEWPASSWORD 		            = PREFIX_CLIENT + "newpassword";

	/** the key for reset the password. */
	public static final String RESETPASSWORD 		        = PREFIX_CLIENT + "resetpassword";

	/** the key for the authentication key. */
	public static final String AUTHKEY 			            = PREFIX_CLIENT + "authkey";
	
	/** the key for the life-cycle object name. */
	public static final String LIFECYCLENAME 	            = PREFIX_CLIENT + "lifecyclename";
	
	/** the key for the compression. */
	public static final String COMPRESSION		            = PREFIX_CLIENT + PREFIX_SESSION + "compression";
	
	/** the key for the alive interval. */
	public static final String ALIVEINTERVAL	            = PREFIX_CLIENT + "alive.interval";
	
	/** the key for the alive factor. */
	public static final String ALIVEFACTOR	            	= PREFIX_CLIENT + "alive.factor";

	/** the key for the connection class name. */
    public static final String CONNECTION_CLASS             = PREFIX_CLIENT + "connectionClass";

    /** the key for client-side metadata caching. */
	public static final String METADATA_CACHEROLE           = PREFIX_CLIENT + "metadata_cacherole";
	
	/** the key for the client connection creation time. */
    public static final String CREATIONTIME_CLIENT          = PREFIX_CLIENT + "creationTime";

    /** the key for the client connection creation time. */
    public static final String CLIENT_LOCALE_LANGUAGE       = PREFIX_CLIENT + "locale.language";
    
    /** the key for the client connection creation time. */
    public static final String CLIENT_LOCALE_COUNTRY        = PREFIX_CLIENT + "locale.country";
    
    /** the key for the client connection creation time. */
    public static final String CLIENT_LOCALE_VARIANT        = PREFIX_CLIENT + "locale.variant";

    /** the key for the client connection creation time. */
    public static final String CLIENT_TIMEZONE_RAWOFFSET    = PREFIX_CLIENT + "timezone.rawoffset";

    /** the key for the client connection creation time. */
    public static final String CLIENT_TIMEZONE_OFFSET       = PREFIX_CLIENT + "timezone.offset";

    /** the key for the client connection creation time. */
    public static final String CLIENT_TIMEZONE		        = PREFIX_CLIENT + "timezone";

    /** the key for the client connection creation time. */
    public static final String CLIENT_FILE_ENCODING         = PREFIX_CLIENT + "file.encoding";

    
    /** the key for the session timeout (minutes). */
    public static final String SESSIONTIMEOUT               = PREFIX_SERVER + PREFIX_SESSION + "timeout";
    
    /** the key for the session timeout (seconds). */
    public static final String SESSIONTIMEOUT_IN_SECONDS    = PREFIX_SERVER + PREFIX_SESSION + "timeoutSeconds";

    /** the key for session controlled metadata cache option (server-side) (no PREFIX_SESSION to suppress direct calls). */
	public static final String METADATA_CACHEOPTION         = PREFIX_SERVER + PREFIX_CLIENT + "metadatacache_option";

    /** the key for the server session creation time. */
    public static final String CREATIONTIME_SERVER          = PREFIX_SERVER + PREFIX_SESSION + "creationTime";
    
    /** the key for the security environment. */
    public static final String SECURITY_ENVIRONMENT	        = PREFIX_SERVER + PREFIX_SESSION + "securityEnvironment";
    
    /** the multi-factor authentication prefix. */
    public static final String PREFIX_MFA   				= PREFIX_CLIENT + "mfa.";
    /** the multi-factor authentication property prefix. */
    public static final String PREFIX_MFA_PROPERTY			= PREFIX_MFA + "property.";
    /** the multi-factor authentication token. */
    public static final String MFA_TOKEN					= PREFIX_MFA + "token";
    /** the multi-factor authentication payload. */
    public static final String MFA_PAYLOAD					= PREFIX_MFA + "payload";
    
}	// IConnectionConstants
