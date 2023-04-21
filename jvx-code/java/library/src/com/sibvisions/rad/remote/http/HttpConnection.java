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
 * 04.08.2009 - [JR] - initSsl: short exception log
 * 10.08.2009 - [JR] - used logger
 * 20.01.2010 - [JR] - Properties constructor forwarded to parent constructor
 * 15.10.2010 - [JR] - #185: SSL handling now gets the servlet URL
 * 31.05.2016 - [JR] - #29: prepareException introduced
 * 22.11.2022 - [JR] - #3090: disable self-signed certificate support
 */
package com.sibvisions.rad.remote.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.rad.io.IDownloadExecutor;
import javax.rad.io.IFileHandle;
import javax.rad.io.IUploadExecutor;
import javax.rad.io.RemoteFileHandle;
import javax.rad.io.TransferContext;
import javax.rad.remote.ConnectionException;
import javax.rad.remote.ConnectionInfo;
import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.UnauthorizedException;

import com.sibvisions.rad.remote.AbstractSerializedConnection;
import com.sibvisions.rad.remote.ISerializer;
import com.sibvisions.util.Reflective;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>HttpConnection</code> communicates with the remote server via http
 * protocol.
 * 
 * @author René Jahn
 */
public class HttpConnection extends AbstractSerializedConnection
                            implements IDownloadExecutor,
                                       IUploadExecutor
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the prefix for request properties. */
	public static final String PREFIX_HTTP = IConnectionConstants.PREFIX_CLIENT + "http.";
	
	/** the property name for the servlet url. */
	public static final String PROP_SERVICE = "service";

    /** the property name for the download url. */
    public static final String PROP_DOWNLOAD = "download";

    /** the property name for the upload url. */
    public static final String PROP_UPLOAD = "upload";
    
    /** the connection timeout property. */
    public static final String PROP_CONNECTION_TIMEOUT = "connectionTimeout";
    
    /** the self-signed certificate support property. */
    public static final String PROP_SELF_SIGNED = "selfSigned";

	
	/** the SSL provider. */
	private static Provider provider = null;
	
	/** the TrustManager for SSL connections. */
    private static TrustManager[] tmSsl = null;
    
    /** the HostnameVerifier for SSL connections. */
    private static HostnameVerifier hvSsl = null;

    /** URL to connect to the remote server. */
	private URL urlServlet = null;

    /** URL of the remote servlet. */
	private String sServletURL = null;
	
    /** URL of the download servlet. */
    private String sDownloadURL = null;
    
    /** URL of the upload servlet. */
    private String sUploadURL = null;

    /** Current connection to the server. */
	private URLConnection ucServer = null;
	
	/** the logger. */
	private ILogger log = LoggerFactory.getInstance(getClass());

	/** the connection timeout. */
	private int iConTimeout = -1;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of <code>HttpConnection</code> with 
	 * properties instead of many parameters. The supported property
	 * keys are:
	 * <ul>
	 *   <li>HttpConnection.PROP_SERIALIZER</li>
	 *   <li>HttpConnection.PROP_SERVICE</li>
	 * </ul>
	 * 
	 * @param pProperties the properties for the connection
	 * @throws MalformedURLException if the servlet URL is not valid
	 * @throws NoSuchAlgorithmException if the specified protocol is not 
	 *                                  available in the default provider package 
	 *                                  or any of the other provider packages that 
	 *                                  were searched.
	 * @throws ClassNotFoundException if the serializer is defined and could not be created                                  
	 * @throws KeyManagementException if ssl operation fails
	 */
	public HttpConnection(Properties pProperties) throws MalformedURLException,
	 											  		 NoSuchAlgorithmException,
	 											  		 KeyManagementException,
	 											  		 ClassNotFoundException
	{
		this(createSerializer(pProperties.getProperty(PROP_SERIALIZER)), 
			 pProperties.getProperty(PROP_SERVICE), 
		     Boolean.parseBoolean(pProperties.getProperty(PROP_SELF_SIGNED)));
		
		setDownloadURL(pProperties.getProperty(PROP_DOWNLOAD));
		setUploadURL(pProperties.getProperty(PROP_UPLOAD));
		
		setConnectionTimeout(pProperties.getProperty(PROP_CONNECTION_TIMEOUT));
	}
	
	/**
	 * Creates a new instance of <code>HttpConnection</code> with the default
	 * serializer.
	 * 
	 * @param pServletURL URL to the remote server
	 * @throws MalformedURLException if the servlet URL is not valid
	 * @throws NoSuchAlgorithmException if the specified protocol is not 
	 *                                  available in the default provider package 
	 *                                  or any of the other provider packages that 
	 *                                  were searched.
	 * @throws KeyManagementException if ssl operation fails
	 * @see AbstractSerializedConnection#AbstractSerializedConnection(ISerializer)
	 */
	public HttpConnection(String pServletURL) throws MalformedURLException,
													 NoSuchAlgorithmException,
													 KeyManagementException
	{
		this(null, pServletURL, true);
	}
	
	/**
	 * Creates a new instance of <code>HttpConnection</code>.
	 * 
	 * @param pSerializer the serializer for the communication between client and server
	 * @param pServletURL URL to the remote server
	 * @throws MalformedURLException if the servlet URL is not valid
	 * @throws NoSuchAlgorithmException if the specified protocol is not 
	 *                                  available in the default provider package 
	 *                                  or any of the other provider packages that 
	 *                                  were searched.
	 * @throws KeyManagementException if ssl operation fails
	 * @see AbstractSerializedConnection#AbstractSerializedConnection(ISerializer)
	 */
	public HttpConnection(ISerializer pSerializer, String pServletURL) throws MalformedURLException,
																			  NoSuchAlgorithmException,
																			  KeyManagementException
	{
		this(pSerializer, pServletURL, true);
	}
	
	/**
	 * Creates a new instance of <code>HttpConnection</code>.
	 * 
	 * @param pSerializer the serializer for the communication between client and server
	 * @param pServletURL URL to the remote server
	 * @param pAllowSelfSigned <code>true</code> to support self-signed certificates, <code>false</code> otherwise
	 * @throws MalformedURLException if the servlet URL is not valid
	 * @throws NoSuchAlgorithmException if the specified protocol is not 
	 *                                  available in the default provider package 
	 *                                  or any of the other provider packages that 
	 *                                  were searched.
	 * @throws KeyManagementException if ssl operation fails
	 * @see AbstractSerializedConnection#AbstractSerializedConnection(ISerializer)
	 */
	public HttpConnection(ISerializer pSerializer, String pServletURL, boolean pAllowSelfSigned) throws MalformedURLException,
																										NoSuchAlgorithmException,
																										KeyManagementException
    {
		super(pSerializer);
		
		this.sServletURL = pServletURL;
	
		urlServlet = new URL(pServletURL);
		
		log.info("Server: ", pServletURL, ", Serializer: ", pSerializer != null ? pSerializer.getClass() : null);

		if (pAllowSelfSigned)
		{
			supportSelfSignedCertificates();
		}
    }
	
	/**
	 * Initializes the ssl context to support self-signed certificates.
	 * 
	 * @throws KeyManagementException if ssl operation fails
	 * @throws NoSuchAlgorithmException if the specified protocol is not 
	 *                                  available in the default provider package 
	 *                                  or any of the other provider packages that 
	 *                                  were searched.
	 */
	protected void supportSelfSignedCertificates() throws NoSuchAlgorithmException,
	  					   	                              KeyManagementException
	{
		if ("https".equals(urlServlet.getProtocol().toLowerCase()))
		{
			if (provider == null)
			{
				try
				{
			        try
			        {
			        	provider = Security.getProvider("SunJSSE");
			        }
			        catch (Exception e)
			        {
			        	log.error(e);

			        	try
			        	{
			        		provider = (Provider)Class.forName("com.sun.net.ssl.internal.ssl.Provider").newInstance();
			        	}
			        	catch (Exception ex)
			        	{
			        		log.error(ex);

			        		//maybe android
				        	try
				        	{
				        		provider = (Provider)Class.forName("org.apache.harmony.xnet.provider.jsse.JSSEProvider").newInstance();
				        	}
				        	catch (Exception exc)
				        	{
				        		log.error(exc);
				        	}
			        	}
			        }
			        
			        if (provider != null)
			        {
				        //Trust manager for certicifate validation
			    		tmSsl = new TrustManager[] { new HttpsTrustManager() };
				        hvSsl = new HttpsHostnameVerifier(); 
				
				    	Security.addProvider(provider);
				
				        //Install Trust Manager
				    	SSLContext sc = SSLContext.getInstance("SSL");
				        
				        sc.init(null, tmSsl, new SecureRandom());
				        
				        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				    	HttpsURLConnection.setDefaultHostnameVerifier(hvSsl);
			        }
			        else
			        {
			        	log.error("SSL Security provider not found!");
			        }
				}
				catch (SecurityException se)
				{
					//not allowed in applets
					log.error(se);
					
					tmSsl = null;
					hvSsl = null;
					provider = null;
				}
			}
			
			//add the server URL as trusted
			if (tmSsl != null && hvSsl != null)
			{
				((HttpsTrustManager)tmSsl[0]).addUrl(urlServlet);
				((HttpsHostnameVerifier)hvSsl).addUrl(urlServlet);
			}
		}
	}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
    public InputStream readContent(RemoteFileHandle pFileHandle) throws IOException
    {
        if (sDownloadURL != null)
        {
            try
            {
                return new URL(createDownloadURL(pFileHandle)).openStream();
            }
            catch (MalformedURLException mfe)
            {
                throw new RuntimeException(mfe);
            }
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public long getContentLength(RemoteFileHandle pFileHandle) throws IOException
    {
        if (sDownloadURL != null)
        {            
            try
            {
                return new URL(createDownloadURL(pFileHandle)).openConnection().getContentLength();
            }
            catch (MalformedURLException mfe)
            {
                throw new RuntimeException(mfe);
            }
        }
        
        //no URL, no content
        return 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public RemoteFileHandle writeContent(IFileHandle pFileHandle) throws IOException
    {
        if (sUploadURL != null)
        {
            RemoteFileHandle rfh = new RemoteFileHandle(pFileHandle.getFileName(), RemoteFileHandle.createObjectCacheKey());
            
            URL url = new URL(createUploadURL(rfh));
            
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
            urlConnection.setRequestProperty("Content-Disposition", "attachment; filename=\"" + pFileHandle.getFileName() + "\"");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            try
            {
                Reflective.call(urlConnection, "setFixedLengthStreamingMode", Long.valueOf(pFileHandle.getLength()));
            }
            catch (Throwable ex)
            {
                urlConnection.setFixedLengthStreamingMode((int)pFileHandle.getLength());
            }
            
            FileUtil.copy(pFileHandle.getInputStream(), true, urlConnection.getOutputStream(), true);
            
            urlConnection.getInputStream().close();
            
            return rfh;
        }
        
        return null;
    }
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream(ConnectionInfo pConnectionInfo) throws IOException
	{
		ucServer = urlServlet.openConnection();
		
		ucServer.setUseCaches(false);
		ucServer.setRequestProperty("Content-Type", "application/octet-stream");
		
		if (iConTimeout >= 0)
		{
			ucServer.setConnectTimeout(iConTimeout);
			ucServer.setReadTimeout(iConTimeout);
		}
		else
		{
		    //defaults
            ucServer.setConnectTimeout(5000);
            ucServer.setReadTimeout(5000);
		}
		
	    ucServer.setDoInput(true);
	    ucServer.setDoOutput(true);
	    
	    //put http properties to the request
	    Hashtable<String, Object> htProps = pConnectionInfo.getProperties();
	    Object oValue;
	    
	    String sKey;
	    
	    for (Map.Entry<String, Object> entry : htProps.entrySet())
	    {
	    	sKey = entry.getKey();
	    	
	    	if (sKey.startsWith(PREFIX_HTTP))
	    	{
	    		oValue = entry.getValue();
	    		
	    		if (oValue instanceof String)
	    		{
	    			ucServer.setRequestProperty(sKey.substring(PREFIX_HTTP.length()), (String)oValue);
	    		}
	    	}
	    }

	    return ucServer.getOutputStream();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream(ConnectionInfo pConnectionInfo) throws IOException
	{
		if (ucServer != null)
		{
            try
            {
                return ucServer.getInputStream();
            }
            catch (IOException ioe)
            {
                if (ucServer instanceof HttpURLConnection
                    || ucServer instanceof HttpsURLConnection)
                {
                    int iResponseCode = getResponseCode(ioe);
                    
                    if (iResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED
                        || iResponseCode ==  HttpURLConnection.HTTP_FORBIDDEN)
                    {
                        throw new UnauthorizedException();
                    }
                }
                
                throw ioe;
            }
		}
		else
		{
			throw new IOException("The connection is not open!");
		}
	}
	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object initCall(ConnectionInfo pConnectionInfo)
	{
	    return new TransferContext(pConnectionInfo, sDownloadURL != null ? this : null, sUploadURL != null ? this : null);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
	protected void releaseCall(ConnectionInfo pConnectionInfo, Object pInit)
	{
	    ((TransferContext)pInit).release();
	}
    
    /**
     * {@inheritDoc}
     * 
     * @return <code>false</code> because it's not needed for http connections
     */
    @Override
    protected boolean isReadingMagicByteEnabled()
    {
        //backwards compatibility 
        //(no problem with http connections because every request has a new stream)
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Throwable prepareException(Throwable pThrowable)
    {
        return fillInStackTrace(pThrowable);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Throwable prepareAfterRetryException(Throwable pThrowable)
    {
    	int iResponseCode = getResponseCode(pThrowable);

    	// Also check 500, in case getResponseCode() makes a new request, because reflective is not possible.
    	// In this case -1 or 500 are possible, 500 when network came back in the meantime.
    	if (iResponseCode == 500 || iResponseCode == -1) 
    	{
	    	if (pThrowable instanceof ConnectionException)
	    	{
	    		Throwable thCause = pThrowable.getCause();
	    		
	    		if (thCause instanceof NoRouteToHostException)
				{
	    			return prepareException(new NoRouteToHostException("Server is not available!"));
				}
	    		else if (thCause instanceof SocketException)
	    		{
	    			String sMessage = StringUtil.lowerCase(thCause.getMessage());
	    			
	    			if (sMessage != null && sMessage.indexOf("unreachable") >= 0)
	    			{
	    				return prepareException(new NoRouteToHostException("Server is not available!"));
	    			}
	    		}
	    		
	    		if (thCause instanceof SocketTimeoutException)
	    		{
	    			String sMessage = StringUtil.lowerCase(thCause.getMessage());
	    			
    				//java.net.SocketTimeoutException: connect timed out
    				//java.net.SocketTimeoutException: Connect timed out
    				//java.net.SocketTimeoutException: Connection timed out
    				//java.net.SocketTimeoutException: Read timed out
	    			if (sMessage != null && sMessage.indexOf("connect") >= 0)
	    			{
    					return prepareException(new NoRouteToHostException("Server is not available!"));
	    			}
	    			
	    			iResponseCode = -2;
	    		}
	    	}
    	}
    	
        if (iResponseCode == 500 || iResponseCode == -2)
        {
            return prepareException(new TimeoutException("The statement execution took too long!"));
        }
    
        return super.prepareAfterRetryException(pThrowable);
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Gets the response code from the url connection, if possible.
     * 
     * @param pCause the original exception of a request
     * @return the response code or <code>-1</code> if not possible to get the code
     */
    private int getResponseCode(Throwable pCause)
    {
        if (ucServer instanceof HttpURLConnection
            || ucServer instanceof HttpsURLConnection)
        {
            try // Try to get ResponseCode reflective, otherwise in case of -1 HttpURLConnection again tries to connect to server   
            {
                return ((Integer)Reflective.getValue(ucServer, HttpURLConnection.class.getDeclaredField("responseCode"))).intValue();
            }
            catch (Throwable th) // Fall back, if reflective is not possible. Risk, that response code cannot be read.
            {
    	        try
    	        {
    	            return (((HttpURLConnection)ucServer).getResponseCode());
    	        }
    	        catch (Exception e)
    	        {
    	            if (pCause != null)
    	            {
        	            String sMessage = pCause.getMessage();
        	            
        	            if (sMessage != null)
        	            {
            	            int iPos = sMessage.toLowerCase().indexOf("response code: ");
            	            
            	            if (iPos > 0)
            	            {
            	                try
            	                {
            	                    return Integer.parseInt(sMessage.substring(iPos + 15, sMessage.indexOf(" ", iPos + 15)).trim());
            	                }
            	                catch (Exception ex)
            	                {
            	                    //ignore
            	                }
            	            }
        	            }
    	            }
    	        }
            }
        }
        
        return -1;
    }
    
	/**
	 * Gets the servlet URL to access the remote server.
	 * 
	 * @return URL to the remote server
	 */
	public String getServletURL()
	{
		return sServletURL;
	}

	/**
	 * Sets the download URL.
	 * 
	 * @param pURL the URL to the download service
	 * @throws MalformedURLException if URL isn't valid
	 */
    public void setDownloadURL(String pURL) throws MalformedURLException
    {
        checkURL(pURL);
        
        sDownloadURL = pURL;
    }

    /**
     * Gets the download URL.
     * 
     * @return the URL to the download service
     */
    public String getDownloadURL()
	{
	    return sDownloadURL;
	}

    /**
     * Sets the upload URL.
     * 
     * @param pURL the URL to the upload service
     * @throws MalformedURLException if URL isn't valid
     */
    public void setUploadURL(String pURL) throws MalformedURLException
    {
        checkURL(pURL);
        
        sUploadURL = pURL;
    }

    /**
     * Gets the upload URL.
     * 
     * @return the URL to the upload service
     */
    public String getUploadURL()
    {
        return sUploadURL;
    }
    
    /**
     * Sets the connection timeout as string.
     * 
     * @param pTimeout the timeout as string
     */
    private void setConnectionTimeout(String pTimeout)
    {
    	if (!StringUtil.isEmpty(pTimeout))
    	{
    		try
    		{
    			setConnectionTimeout(Integer.parseInt(pTimeout));
    		}
    		catch (Exception e)
    		{
    			log.error(e);
    		}
    	}
    }
    
	/**
	 * Sets the timeout for the connection establishment.
	 * 
	 * @param pTimeout the timeout in millis or -1 for the default timeout
	 */
	public void setConnectionTimeout(int pTimeout)
	{
		iConTimeout = pTimeout;
	}

	/**
	 * Gets the timeout for the connection establishment.
	 * 
	 * @return pTimeout the timeout in millis or -1 for the default timeout
	 */
	public int getConnectionTimeout()
	{
		return iConTimeout;
	}

	/**
	 * Checks if the given URL is valid. It's valid if it can be parsed via {@link URL}.
	 * 
	 * @param pURL the URL to check
	 * @return the parsed URL
	 * @throws MalformedURLException if URL isn't valid
	 */
	private URL checkURL(String pURL) throws MalformedURLException
	{
        if (pURL != null)
        {
            new URL(pURL);
        }
        
        return null;
	}

	/**
	 * Creates a download URL for the given file handle.
	 * 
	 * @param pFileHandle the file handle
	 * @return the download URL
	 */
	private String createDownloadURL(RemoteFileHandle pFileHandle)
	{
        if (sDownloadURL.indexOf("?") > 0)
        {
            return sDownloadURL + "&KEY=" + pFileHandle.getObjectCacheKey();
        }
        else
        {
            return sDownloadURL + "?KEY=" + pFileHandle.getObjectCacheKey();
        }
	}
	
    /**
     * Creates an upload URL for the given file handle.
     * 
     * @param pFileHandle the file handle
     * @return the upload URL
     */
	private String createUploadURL(RemoteFileHandle pFileHandle)
	{
	    StringBuilder sbURL = new StringBuilder(sUploadURL);
	    
	    
        if (sUploadURL.indexOf("?") > 0)
        {
            sbURL.append("&");
        }
        else
        {
            sbURL.append("?");
        }
        
        sbURL.append("KEY=");
        sbURL.append(pFileHandle.getObjectCacheKey());
        
        ConnectionInfo coninf = TransferContext.getCurrentConnectionInfo();
        
        if (coninf != null)
        {
            sbURL.append("&CONNECTION_ID=");

            try
            {
                sbURL.append(CodecUtil.encodeHex(coninf.getConnectionId().toString()));
            }
            catch (Exception e)
            {
                //ignore
            }
        }

        return sbURL.toString();
	}
	
	/**
	 * Creates a new "empty" remote file handle prepared for downloading content.
	 * 
	 * @return the file handle
	 */
	public RemoteFileHandle createRemoteFileHandle()
	{
	    TransferContext ctxt = new TransferContext(null, sDownloadURL != null ? this : null, sUploadURL != null ? this : null);	    
	    
	    try
	    {
	        RemoteFileHandle rfh = new RemoteFileHandle();
	        rfh.setObjectCacheKey(RemoteFileHandle.createObjectCacheKey());
	        
	        return rfh;
	    }
	    finally
	    {
	        ctxt.release();
	    }
	}
	
}	// HttpConnection
