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
 * 01.10.2008 - [JR] - creation
 * 02.11.2008 - [JR] - getNodes implemented
 * 12.12.2008 - [JR] - renamed: load -> update
 * 18.11.2010 - [JR] - #206: update en/disable support, clone support, static isValid implemented
 * 21.01.2011 - [JR] - #262: call update in clone
 * 16.06.2013 - [JR] - #673: setContent implemented
 * 02.10.2014 - [JR] - #1126: support virtual configurations
 * 27.11.2014 - [JR] - clone nodes (optional), don't clone per default
 * 25.03.2015 - [TK] - #1339: property files support
 * 24.06.2016 - [JR] - JNDI support
 */
package com.sibvisions.rad.server.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.FileWatchdog;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * The <code>UpToDateConfigFile</code> encapsulates the access to a xml configuration
 * file. It's guaranteed that the access to the properties of the file is up-to-date.
 * That means that changes will be detected automatically.
 * 
 * @author René Jahn
 */
public class UpToDateConfigFile implements Cloneable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the configuration file. */
	private File fiConfig;
	
	/** the content stream. */
	private InputStream stream;
	
	/** the current configuration. */
	private XmlNode xmnConfig = null;
	
    /** modified date of the configuration file. */
    private long lLastModified = -1;

    /** set to <code>true</code> ignores the update of the configuration. */
	private boolean bUpdateEnabled = true;
	
	/** set to <code>true</code> saves immediate after each property change. */
	private boolean bSaveImmediate = true;

	/** set t <code>true</code> if a property was changed via {@link UpToDateConfigFile#setProperty(String, String)}. */
	private boolean bChanged = false;
	
	/** marks the virtual file mode. */
	private boolean bVirtual = false;
	
	/** whether the content was read from an external stream. */
	private boolean bStream = false;
	
	/** whether xml nodes should be cloned before returning. */
	private boolean bCloneNodes = false;
	
	/** whether the content was modified by includes. */
	private boolean bModifiedByInclude = false;
	
	/** whether file was valid before disable updates. */
	private boolean bWasValid = false;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>UpToDateConfigFile</code> for a
	 * xml configuration file. The configuration will read immediately.
	 * 
	 * @param pDirectory the directory where to find the configuration file
	 * @param pConfig the configuration file name
	 * @throws Exception if it is not possible to read the configuration
	 *                   from the filesystem or the xml content is not valid
	 */
	public UpToDateConfigFile(File pDirectory, String pConfig) throws Exception
	{
		if (pDirectory != null && pConfig != null)
		{
			fiConfig = new File(pDirectory, pConfig);
			
			FileWatchdog.add(fiConfig);
		}
		
		update();
	}
	
	/**
	 * Creates a new instance of <code>UpToDateConfigFile</code> for a
	 * xml configuration file. The configuration will read immediately.
	 * 
	 * @param pConfig the configuration file
	 * @throws Exception if it is not possible to read the configuration
	 *                   from the filesystem or the xml content is not valid
	 */
	public UpToDateConfigFile(File pConfig) throws Exception
	{
		fiConfig = pConfig;
		
		FileWatchdog.add(fiConfig);
		
		update();
	}
	
	/**
	 * Creates a new instance of <code>UpToDateConfigFile</code> as virtual
	 * configuration without file reference.
	 */
	UpToDateConfigFile()
	{
	    //Virtual mode
	    bVirtual = true;
	}
	
	/**
	 * Creates a new instance of <code>UpToDateConfigFile</code> as virtual
	 * configuration without file reference but with content from a stream.
	 * 
	 * @param pStream the stream
	 * @throws Exception if reading stream fails
	 */
	UpToDateConfigFile(InputStream pStream) throws Exception
	{
	    stream = pStream;
	    
	    bVirtual = true;
	    bStream = true;
	    
	    update();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		try
		{
			update();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		UpToDateConfigFile udcf = (UpToDateConfigFile)super.clone();
		
		udcf.xmnConfig = (XmlNode)xmnConfig.clone();
		
		return udcf;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Gets the configuration file.
	 * 
	 * @return the configuration file
	 */
	public File getFile()
	{
	    if (bVirtual)
	    {
	        return null;
	    }
	    else
	    {
	        return fiConfig;
	    }
	}

	/**
	 * Checks if the configuration file exists.
	 * 
	 * @return <code>true</code> if the configuration file exists otherwise <code>false</code> 
	 */
	public boolean isValid()
	{
	    if (bVirtual)
	    {
	        return true;
	    }
	    else 
	    {
	        return FileWatchdog.exists(fiConfig);
	    }
	}
	
	/**
	 * Checks if the configuration file exists.
	 * 
	 * @param pFile the configuration file
	 * @return <code>true</code> if the configuration file exists otherwise <code>false</code> 
	 */
	static boolean isValid(File pFile)
	{
		return pFile != null && pFile.exists();
	}
	
	/**
	 * Loads the configuration from the filesystem.
	 * 
	 * @throws Exception if it is not possible to read the configuration 
	 *                   from the filesystem or the xml content is not valid
	 */
	protected void update() throws Exception
	{
		//only return without checks if file was valid and update is disabled
		if (!isUpdateEnabled() && bWasValid)
		{
			return;
		}
		
	    if (isValid())
		{
			if (isUpdateEnabled())
			{
			    if (lLastModified < 0 || (fiConfig != null && FileWatchdog.lastModified(fiConfig) != lLastModified))
			    {
                    reload();
			    }
			}
		}
		else
		{
			throw new Exception("Invalid configuration file: " + (fiConfig != null ? fiConfig.getAbsolutePath() : null));
		}
	}
	
	/**
	 * Sets the value of a property to the application configuration file.
	 * 
	 * @param pName the property name (e.g. /application/securitymanager/class)
	 * @param pValue the value
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized void setProperty(String pName, String pValue) throws Exception
	{
		update();

		if (pValue == null)
		{
			xmnConfig.removeNode(pName);
		}
		else
		{
			xmnConfig.setNode(pName, pValue);
		}
		
		bChanged = true;
		
		if (bSaveImmediate)
		{
			save();
		}
	}
	
	/**
	 * Sets a specific node to the application configuration file.
	 * 
	 * @param pName the property name
	 * @param pNode the new node or <code>null</code> to remove the property
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized void setNode(String pName, XmlNode pNode) throws Exception
	{
		update();
		
		if (pNode == null)
		{
			xmnConfig.removeNode(pName);
		}
		else
		{
			xmnConfig.setNode(pName, pNode);
		}
		
		bChanged = true;
		
		if (bSaveImmediate)
		{
			save();
		}
	}
	
	/**
	 * Gets the value of a property from the application configuration file.
	 *
	 * @param pName the property name (e.g /application/securitymanager/class)
	 * @return the value for the property or null if the property is not available
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized String getProperty(String pName) throws Exception
	{
		return getProperty(pName, null);
	}
	
	/**
	 * Gets the value of a property from the application configuration file.
	 *
	 * @param pName the property name (e.g /application/securitymanager/class)
	 * @param pDefault the default value if the property is not available
	 * @return the value for the property or <code>pDefault</code> if the property is not available
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized String getProperty(String pName, String pDefault) throws Exception
	{
		update();
		
		XmlNode xmn = xmnConfig.getNode(pName);
				
		if (xmn != null)
		{
			return (String)CommonUtil.nvl(xmn.getValue(), "");
		}
		else
		{
			return pDefault;
		}
	}	
	
	/**
	 * Gets a list of values for a property which exists more than once.
	 * 
	 * @param pName the property name
	 * @return the list of values or <code>null</code> if the property is not available
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized List<String> getProperties(String pName) throws Exception
	{
		update();
		
		List<XmlNode> liNodes = xmnConfig.getNodes(pName);
		
		if (liNodes == null)
		{
			return null;
		}
		
		ArrayUtil<String> auValues = new ArrayUtil<String>();
		
		for (XmlNode node : liNodes)
		{
			auValues.add((String)CommonUtil.nvl(node.getValue(), ""));
		}
		
		return auValues;
	}
	
	/**
	 * Gets the value for a property as xml representation.
	 * 
	 * @param pName the property name
	 * @return the available xml node(s) or <code>null</code> if the property is not available
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized List<XmlNode> getNodes(String pName) throws Exception
	{
		update();
		
		List<XmlNode> liNodes = xmnConfig.getNodes(pName);
		
		if (bCloneNodes && liNodes != null)
		{
			List<XmlNode> liClone = new ArrayUtil<XmlNode>(liNodes.size());
			
			for (XmlNode node : liNodes)
			{
				liClone.add((XmlNode)node.clone());
			}
			
			return liClone;
		}
		else
		{
			return liNodes;
		}
	}
	
	/**
	 * Gets the value for a property as xml representation.
	 * 
	 * @param pName the property name
	 * @return the available xml node or <code>null</code> is the property is not available
	 * @throws Exception if the configuration is invalid
	 */
	public synchronized XmlNode getNode(String pName) throws Exception
	{
		update();
		
		XmlNode node = xmnConfig.getNode(pName); 
		
		if (node != null)
		{
		    if (bCloneNodes)
		    {
		        return (XmlNode)node.clone();
		    }
		    else
		    {
		        return node;
		    }
		}
		
		return null;
	}
	
	/**
	 * Sets the up-to-date option of the configuration.
	 * 
	 * @param pEnabled <code>true</code> doesn't update the configuration when
	 *                 next accessing; <code>false</code> always keeps the configuration
	 *                 up-to-date
	 */
	public void setUpdateEnabled(boolean pEnabled)
	{
		bUpdateEnabled = pEnabled;
		
		if (pEnabled)
		{
			bWasValid = false;
		}
		else
		{
			bWasValid = isValid();
		}
	}

	/**
	 * Gets whether the up-to-date option is enabled.
	 * 
	 * @return <code>true</code> if the current configuration will be read before
	 *         accessing a property, <code>false</code> if the configuration won't
	 *         be read again 
	 */
	public boolean isUpdateEnabled()
	{
		return bUpdateEnabled;
	}
	
	/**
	 * Sets the save immediate option. If save immediate is enabled, then changes
	 * will be written automatically.
	 * 
	 * @param pEnabled <code>true</code> to enable immediate save changes, <code>false</code> otherwise 
	 */
	public synchronized void setSaveImmediate(boolean pEnabled)
	{
		bSaveImmediate = pEnabled;
	}
	
	/**
	 * Gets it immediate save of changes is enabled.
	 * 
	 * @return <code>true</code> if changes will saved immediate, <code>false</code> otherwise
	 */
	public boolean isSaveImmediate()
	{
		return bSaveImmediate;
	}
	
	/**
	 * Saves the changes to the file.
	 * 
	 * @throws IOException if the file access failed or the content was modified by &lt;include&gt; declarations
	 */
	public synchronized void save() throws IOException
	{
	    if (bVirtual)
	    {
	        bChanged = false;
	    }
	    else if (bChanged)
		{
	    	if (bModifiedByInclude)
	    	{
	    		throw new IOException("Couldn't save configuration file! The content was modified by <include> declarations.");
	    	}
	    	else
	    	{
				FileOutputStream fos = null;
				
				try
				{
					fos = new FileOutputStream(fiConfig);
					
					XmlWorker xmwConfig = createXmlWorker();
					xmwConfig.write(fos, xmnConfig);
					
					lLastModified = fiConfig.lastModified();
					bChanged = false;
				}
				finally
				{
				    CommonUtil.close(fos);
				}
	    	}
		}
	}

	/**
	 * Discards all changes and loads the configuration file.
	 * 
	 * @throws Exception if an exception occurs during loading
	 */
	public synchronized void reload() throws Exception
	{
		bChanged = false;

		if (bVirtual)
		{
		    if (stream != null)
		    {
                XmlWorker xmwConfig = createXmlWorker();
                
                try
                {
                	XmlNode xmnNode = xmwConfig.read(stream);
                	
                	prepareIncludeNodes(xmnNode);
                    
                	setContent(xmnNode);
                }
                finally
                {
                    CommonUtil.close(stream);
                }
		    }
		    else
		    {
		        setContent(XmlNode.createXmlDeclaration());
		    }
		    
		    lLastModified = System.currentTimeMillis();
		}
		else
		{
    		FileInputStream fisConfig;
    		
    		try
    		{
    			fisConfig = new FileInputStream(fiConfig);
    		}
    		catch (IOException ioe)
    		{
    			throw new Exception("Invalid configuration file: " + fiConfig.getAbsolutePath(), ioe);
    		}
    		
    		try
    		{
    			XmlWorker xmwConfig = createXmlWorker();
    			XmlNode xmnNode = xmwConfig.read(fisConfig);

    			prepareIncludeNodes(xmnNode);
    			
    			setContent(xmnNode);
    			
    			lLastModified = fiConfig.lastModified();
    		}
    		finally
    		{
    		    CommonUtil.close(fisConfig);
    		}
		}
	}

	/**
	 * Creates the default {@link XmlWorker} for reading the file.
	 * 
	 * @return the xml worker
	 */
	protected XmlWorker createXmlWorker()
	{
		return new XmlWorker();		
	}
	
	/**
	 * Sets the internal data node.
	 * 
	 * @param pNode the data node
	 */
	protected void setContent(XmlNode pNode)
	{
		xmnConfig = pNode;
	}

	/**
	 * Gets whether the config is a physical file.
	 * 
	 * @return <code>true</code> if config is a physical file
	 */
	public final boolean isFile()
	{
	    return fiConfig != null;
	}
	
	/**
	 * Gets whether the config is virtual.
	 * 
	 * @return <code>true</code> if config is virtual
	 */
	public final boolean isVirtual()
	{
	    return bVirtual;
	}
	
	/**
	 * Gets whether the config was read from an external stream.
	 * 
	 * @return <code>true</code> if config was read from stream
	 */
	public final boolean isStream()
	{
	    return bStream;
	}
	
	/**
	 * Sets whether xml nodes should be cloned before returning.
	 * 
	 * @param pClone <code>true</code> to clone nodes, <code>false</code> otherwise
	 */
	public void setCloneNodes(boolean pClone)
	{
	    bCloneNodes = pClone;
	}
	
	/**
	 * Gets whether xml nodes should be cloned before returning.
	 * 
	 * @return <code>true</code> if nodes will be cloned, <code>false</code> otherwise
	 */
	public boolean isCloneNodes()
	{
	    return bCloneNodes;
	}
	
	/**
	 * Prepares the include nodes <code>&lt;include&gt;...&lt;/include&gt;</code>.
	 * 
	 * The value of the include node is used as file path to load the resource
	 * over the classpath or filesystem if it is available.
	 * 
	 * All loaded properties are inserted under the first founded root node.
	 * In addition, all property place holders <code>${x.y.z}</code> are replaced with
	 * the loaded values of the properties.
	 * 
	 * @param pNode the node to prepare
	 * @throws IOException if an error occurred when reading from the property file input stream.
	 */
	protected void prepareIncludeNodes(XmlNode pNode) throws IOException
	{
		if (pNode != null)
		{
			XmlNode xmnRootNode = pNode.getFirstTextNode();
			
			if (xmnRootNode != null)
			{
				List<XmlNode> liNodes = xmnRootNode.getNodes("include");

				if (liNodes != null)
				{
					// create joined property set
					Properties propIncludes = new Properties();
					
					Properties propSystem = System.getProperties();
					Map<String, String> mapEnv = System.getenv();
					
					for (int i = 0, ic = liNodes.size(); i < ic; i++)
					{
						XmlNode xmnNode = liNodes.get(i);
						
						String sResource = replacePlaceholder(xmnNode.getValue(), propIncludes, propSystem, mapEnv);
						
						InputStream isResource = ResourceUtil.getResourceAsStream(sResource);
						
						//JNDI check
						if (isResource == null)
						{
						    try
						    {
    			                InitialContext ctxt = new InitialContext();
    			                
    			                try
    			                {
    			                    Object objInstance = ctxt.lookup(sResource);
    
    			                    if (objInstance instanceof String)
    			                    {
    			                        InputStream istream = ResourceUtil.getResourceAsStream((String)objInstance);
    			                        
    			                        if (istream == null)
    			                        {
    			                            ByteArrayInputStream bais = new ByteArrayInputStream(((String)objInstance).getBytes("UTF-8"));
    			                            
    			                            //maybe XML (no special node checks)
    			                            XmlWorker.readNode(bais);
    			                            
    			                            bais.reset();
    			                            
    			                            isResource = bais;
    			                        }
    			                        else
    			                        {
    			                            isResource = istream;
    			                        }
    			                    }
    			                    else if (objInstance instanceof InputStream)
    			                    {
    			                        isResource = (InputStream)objInstance;
    			                    }
    			                }
    			                finally
    			                {
    			                    ctxt.close();
    			                }
						    }
						    catch (Exception e)
						    {
						        LoggerFactory.getInstance(UpToDateConfigFile.class).debug(e);
						        
						        isResource = null;
						    }
						}
						
						try
						{
    						if (isResource != null)
    						{
    							propIncludes.load(isResource);
    							
    							xmnRootNode.remove(xmnNode);
    							bModifiedByInclude = true;
    						}
						}
						finally
						{
						    CommonUtil.close(isResource);
						}
					}
					
					// replace all property placeholder
					bModifiedByInclude |= replacePlaceholder(pNode, propIncludes, propSystem, mapEnv);
					
					// insert all imported properties to the root node
					Enumeration<Object> eKeys = propIncludes.keys();
					
					ArrayList<String[]> liError = new ArrayList<String[]>();
					
					while (eKeys.hasMoreElements())
					{
						String sKey = (String)eKeys.nextElement();
						String sNodePath = sKey.replaceAll("\\.", "/");
						
						XmlNode xmnNode = xmnRootNode.getNode(sNodePath);
						
						if (xmnNode != null)
						{
							xmnNode.setValue(propIncludes.getProperty(sKey));
						}
						else
						{
							String sNodeValue = propIncludes.getProperty(sKey);

							try
							{
								xmnRootNode.insertNode(sNodePath, sNodeValue);
							}
							catch (IndexOutOfBoundsException iobe)
							{
								liError.add(new String[] {sNodePath, sNodeValue});
							}
						}
					}
					
					int initial;
					
					String[] sElement;

					do
					{
						initial = liError.size() - 1;

						for (int i = initial; i >= 0; i--)
						{
							sElement = liError.get(i);
							
							try
							{
								xmnRootNode.insertNode(sElement[0], sElement[1]);
								
								liError.remove(i);
							}
							catch (IndexOutOfBoundsException e)
							{
								//next try
							}
						}
					}
					while (initial != liError.size() - 1);
				}
			}
		}
	}
	
	/**
	 * Replaces all property placeholder <code>${x.y.z}</code> with
	 * the corresponding value in the properties, recursively.
	 *  
	 * @param pNode the node to replace the property place holders
	 * @param pImportedProperties the import properties
	 * @param pSystemProperties the system properties
	 * @param pEnvironmentProperties the environment properties
	 * @return <code>true</code> if one node value was modified
	 */
	protected boolean replacePlaceholder(XmlNode pNode,
						                 Properties pImportedProperties,
										 Properties pSystemProperties,
										 Map<String, String> pEnvironmentProperties)
	{
		boolean bModified = false;
		
		if (pNode != null)
		{
			for (XmlNode xmlSubNode : pNode.getNodes())
			{
				if (xmlSubNode.getType() != XmlNode.TYPE_COMMENT)
				{
					String sValue = xmlSubNode.getValue();
					
					if (sValue != null)
					{
					    String sNewValue = replacePlaceholder(sValue, pImportedProperties, pSystemProperties, pEnvironmentProperties);
					    
					    if (!sNewValue.equals(sValue))
					    {
					        xmlSubNode.setValue(sNewValue);
					        
					        bModified |= true;
					    }
					}
					
					bModified |= replacePlaceholder(xmlSubNode, pImportedProperties, pSystemProperties, pEnvironmentProperties);
				}
			}
		}
		
		return bModified;
	}
	
	/**
	 * Replaces all placeholder with the desired properties.
	 * 
	 * @param pValue the value of an node
	 * @param pImportedProperties the import properties
	 * @param pSystemProperties the system properties
	 * @param pEnvironmentProperties the environment properties
	 * @return the value with replaced placeholder(s)
	 */
	protected String replacePlaceholder(final String pValue,
										Properties pImportedProperties,
										Properties pSystemProperties,
										Map<String, String> pEnvironmentProperties)
	{
		if (pValue != null)
		{
            int iParamStart = pValue.indexOf("${");

            if (iParamStart < 0)
            {
                return pValue;
            }

            StringBuilder sbNewValue = new StringBuilder(pValue);
            
            String sReplaceValue;
			String sReplaceParam;
			String sReplaceParamPrefix;
			String sReplaceParamPostfix;
			
			int iParamPrefixStart;
			int iParamEnd = 0;
					
			while (iParamStart >= 0
				   && iParamEnd != -1)
			{
				iParamEnd = sbNewValue.indexOf("}", iParamStart + 2);
				
				if (iParamEnd > iParamStart)
				{				    
					sReplaceParam = sbNewValue.substring(iParamStart + 2, iParamEnd);

                    if (sReplaceParam.length() > 0)
                    {
                        sReplaceParamPrefix = null;
    					sReplaceParamPostfix = null;
                        sReplaceValue = null;
    					
    					iParamPrefixStart = sReplaceParam.indexOf(':');
    					
    					if (iParamPrefixStart >= 0)
    					{
    						sReplaceParamPrefix = sReplaceParam.substring(0, iParamPrefixStart);
    						sReplaceParamPostfix = sReplaceParam.substring(iParamPrefixStart + 1);
    					}
    					
    					if ("imp".equalsIgnoreCase(sReplaceParamPrefix))
    					{
    						if (pImportedProperties != null)
    						{
    							sReplaceValue = pImportedProperties.getProperty(sReplaceParamPostfix);
    						}
    					}
    					else if ("sys".equalsIgnoreCase(sReplaceParamPrefix))
    					{
    						if (pSystemProperties != null)
    						{
    							sReplaceValue = pSystemProperties.getProperty(sReplaceParamPostfix);
    						}
    					}
    					else if ("env".equalsIgnoreCase(sReplaceParamPrefix))
    					{
    						if (pEnvironmentProperties != null)
    						{
    							sReplaceValue = pEnvironmentProperties.get(sReplaceParamPostfix);
    						}
    					}
    					else
    					{
    						if (pImportedProperties != null)
    						{
    							sReplaceValue = pImportedProperties.getProperty(sReplaceParam);
    						}
    						
    						if (sReplaceValue == null)
    						{
    							if (pSystemProperties != null)
    							{
    								sReplaceValue = pSystemProperties.getProperty(sReplaceParam);
    							}
    							
    							if (sReplaceValue == null)
    							{
    								if (pEnvironmentProperties != null)
    								{
    									sReplaceValue = pEnvironmentProperties.get(sReplaceParam);
    								}
    							}
    						}
    					}
    					
                        if (sReplaceValue != null)
                        {
                            sbNewValue = sbNewValue.replace(iParamStart, iParamEnd + 1, sReplaceValue);
                            
                            iParamEnd = iParamStart + sReplaceValue.length() - 1;
                        }
                    }
				}
				
				iParamStart = sbNewValue.indexOf("${", iParamEnd + 1);
			}
			
			return sbNewValue.toString();
		}
		
		return pValue;
	}

}	// UpToDateConfigFile
