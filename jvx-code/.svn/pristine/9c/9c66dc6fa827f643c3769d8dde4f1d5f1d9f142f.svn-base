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
 * 01.12.2010 - [JR] - creation
 * 07.07.2015 - [JR] - #1433: replace placeholders
 */
package com.sibvisions.rad.server.security;

import javax.rad.server.IConfiguration;

import com.sibvisions.rad.persist.jdbc.DBCredentials;
import com.sibvisions.rad.server.config.ApplicationZone;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>DataSourceHandler</code> allows access to credentials configured in the application configuration.
 * It offers quick-access methods to the specific datasources.
 * 
 * @author René Jahn
 */
public final class DataSourceHandler
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor, because the <code>DataSourceHandler</code> class is a utility class.
	 */
	private DataSourceHandler()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>DBCredentials</code> with information provided
     * in a given application.
     * 
     * @param pZone the application zone which contains the configuration
     * @param pName the name of the datasource in the configuration. The name must exist
     *              in the following format:
     *              <pre>
     *              &lt;application&gt;
     *                &lt;datasource&gt;
     *                  &lt;db name="dbname"&gt;
     *                    &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
     *                    &lt;url&gt;connection url&lt;/url&gt;
     *                    &lt;username&gt;username&lt;/username&gt;
     *                    &lt;password&gt;password&lt;/password&gt;
     *                  &lt;/db&gt;
     *                &lt;/datasource&gt;
     *              &lt;/application&gt;
     *              </pre>
     * @return the credentials or <code>null</code> if there are no credentials with the given name
     */
    public static DBCredentials createDBCredentials(ApplicationZone pZone, String pName)
    {
        return createDBCredentials(pZone, pName, null);
    }

    /**
	 * Creates a new instance of <code>DBCredentials</code> with information provided
	 * in a given application.
	 * 
	 * @param pZone the application zone which contains the configuration
	 * @param pName the name of the datasource in the configuration. The name must exist
	 *              in the following format:
	 *              <pre>
	 *              &lt;application&gt;
	 *                &lt;datasource&gt;
	 *                  &lt;db name="dbname"&gt;
	 *                    &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
	 *                    &lt;url&gt;connection url&lt;/url&gt;
	 *                    &lt;username&gt;username&lt;/username&gt;
	 *                    &lt;password&gt;password&lt;/password&gt;
	 *                  &lt;/db&gt;
	 *                &lt;/datasource&gt;
	 *              &lt;/application&gt;
	 *              </pre>
	 * @param pEnvironment the environment name for credentials detection. The environment name will be used for
	 *                     accessing tag names, as suffix.
	 * @return the credentials or <code>null</code> if there are no credentials with the given name
	 */
	public static DBCredentials createDBCredentials(ApplicationZone pZone, String pName, String pEnvironment)
	{
		if (pZone != null && pName != null)
		{
			return createDBCredentials(pZone.getConfig(), pName, pEnvironment);
		}
		
		return null;
	}

    /**
     * Creates a new instance of <code>DBCredentials</code> with information provided
     * in a given configuration.
     * 
     * @param pConfig the configuration
     * @param pName the name of the datasource in the configuration. The name must exist
     *              in the following format:
     *              <pre>
     *              &lt;application&gt;
     *                &lt;datasource&gt;
     *                  &lt;db name="dbname"&gt;
     *                    &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
     *                    &lt;url&gt;connection url&lt;/url&gt;
     *                    &lt;username&gt;username&lt;/username&gt;
     *                    &lt;password&gt;password&lt;/password&gt;
     *                  &lt;/db&gt;
     *                &lt;/datasource&gt;
     *              &lt;/application&gt;
     *              </pre>
     * @return the credentials or <code>null</code> if there are no credentials with the given name
     */
    public static DBCredentials createDBCredentials(IConfiguration pConfig, String pName)
    {
        return createDBCredentials(pConfig, pName, null);
    }	
	
	/**
	 * Creates a new instance of <code>DBCredentials</code> with information provided
	 * in a given configuration.
	 * 
	 * @param pConfig the configuration
	 * @param pName the name of the datasource in the configuration. The name must exist
	 *              in the following format:
	 *              <pre>
	 *              &lt;application&gt;
	 *                &lt;datasource&gt;
	 *                  &lt;db name="dbname"&gt;
	 *                    &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
	 *                    &lt;url&gt;connection url&lt;/url&gt;
	 *                    &lt;username&gt;username&lt;/username&gt;
	 *                    &lt;password&gt;password&lt;/password&gt;
	 *                  &lt;/db&gt;
	 *                &lt;/datasource&gt;
	 *              &lt;/application&gt;
	 *              </pre>
     * @param pEnvironment the environment name for credentials detection. The environment name will be used for
     *                     accessing tag names, as suffix.
	 * @return the credentials or <code>null</code> if there are no credentials with the given name
	 */
	public static DBCredentials createDBCredentials(IConfiguration pConfig, String pName, String pEnvironment)
	{
		if (pConfig != null && pName != null)
		{
			XmlNode xmnDs = pConfig.getNode("/application/datasource");
			
			if (xmnDs != null)
			{
				int iPos = xmnDs.indexOf("/db/name", pName);
				
				if (iPos >= 0)
				{
					return createDBCredentials(xmnDs.getNode("/db(" + iPos + ")"), pEnvironment);
				}
			}
		}
		
		return null;
	}
	
    /**
     * Creates a new instance of <code>DBCredentials</code> with information provided
     * in xml format.
     * 
     * @param pNode the information as xml node. The node should contain at least
     *              <pre>
     *              &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
     *              &lt;url&gt;connection url&lt;/url&gt;
     *              &lt;username&gt;username&lt;/username&gt;
     *              &lt;password&gt;password&lt;/password&gt;
     *              </pre>
     * @return the credentials or <code>null</code> if there are no credentials with the given name
     */
    public static DBCredentials createDBCredentials(XmlNode pNode)
    {
        return createDBCredentials(pNode, null);
    }
    
	/**
	 * Creates a new instance of <code>DBCredentials</code> with information provided
	 * in xml format.
	 * 
	 * @param pNode the information as xml node. The node should contain at least
	 *              <pre>
	 *              &lt;driver&gt;JDBC driver class&lt;/driver&gt; <b>(optional tag)</b>
	 *              &lt;url&gt;connection url&lt;/url&gt;
	 *              &lt;username&gt;username&lt;/username&gt;
	 *              &lt;password&gt;password&lt;/password&gt;
	 *              </pre>
     * @param pEnvironment the environment name for credentials detection. The environment name will be used for
     *                     accessing tag names, as suffix.
	 * @return the credentials or <code>null</code> if there are no credentials with the given name
	 */
	public static DBCredentials createDBCredentials(XmlNode pNode, String pEnvironment)
	{
		if (pNode != null)
		{
		    String sDriver = null;
		    String sUserName = null;
		    String sPassword = null;
		    
		    XmlNode xmnURL = null;
		    
		    boolean bCheckEnv = !StringUtil.isEmpty(pEnvironment); 
		    
		    if (bCheckEnv)
		    {
		        xmnURL = pNode.getNode("/url_" + pEnvironment);
		    }
		    
		    if (xmnURL == null)
		    {
		        xmnURL = pNode.getNode("/url");
		    }
			
			if (xmnURL != null)
			{
	            if (bCheckEnv)
	            {
                    sDriver   = getValue(pNode.getNode("/driver_" + pEnvironment));
                    sUserName = getValue(pNode.getNode("/username_" + pEnvironment));
                    sPassword = getValue(pNode.getNode("/password_" + pEnvironment));
	            }
	            
	            if (sDriver == null)
	            {
	                sDriver = getValue(pNode.getNode("/driver"));
	            }
	            
	            if (sUserName == null)
	            {
	                sUserName = getValue(pNode.getNode("/username"));
	            }

                if (sPassword == null)
                {
                    sPassword = getValue(pNode.getNode("/password"));
                }
	            
                //#1431
                return new DBCredentials(StringUtil.replacePlaceholder(sDriver), 
                                         StringUtil.replacePlaceholder(getValue(xmnURL)), 
                                         StringUtil.replacePlaceholder(sUserName), 
                                         StringUtil.replacePlaceholder(sPassword));
			}
		}
		
		return null;
	}

	/**
	 * Gets the checked value from an {@link XmlNode}.
	 * 
	 * @param pNode the node
	 * @return <code>null</code> if <code>pNode == null</code> and an empty string if the value
	 *         of the node is <code>null</code>, otherwise the string value of the node
	 */
	private static String getValue(XmlNode pNode)
	{
		if (pNode != null)
		{
			return CommonUtil.nvl(pNode.getValue(), "");
		}
		else
		{
			return null;
		}
	}
	
}	// DataSourceHandler
