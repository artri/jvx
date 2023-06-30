/*
 * Copyright 2012 SIB Visions GmbH
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
 * 15.07.2012 - [JR] - creation
 */
package com.sibvisions.util.security;

import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SSLProvider</code> is a utility, that ensures full SSL security support by default,
 * but enables the possibility to add host name and port to an exception list, that will not be checked for 
 * a trusted SSL certificate or matching host name. 
 * eg:
 * <pre><code>
 * SSLProvider.init();
 * SSLProvider.addHostToException("*.badssl.com");      // will add an exception for all sub domains of badssl.com for port 443 
 * SSLProvider.addHostToException("*.badssl.com:8443"); // will add an exception for all sub domains of badssl.com for port 8443
 * SSLProvider.addHostToException("*.badssl.com:*");    // will add an exception for all sub domains of badssl.com for port all ports
 * </code></pre>
 * 
 * @author Martin Handsteiner
 */
public final class SSLProvider
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the host:port exception list with wild cards. */
    private static Set<String> whiteList = new LinkedHashSet<String>();

    /** the original socket factory for restore. */
    private static SSLSocketFactory originalSocketFactory = null;
    
    /** the original host name verifier for restore. */
    private static HostnameVerifier originalHostnameVerifier = null;
    
    /** The logger. */
    private static ILogger logger = LoggerFactory.getInstance(SSLProvider.class);
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Utility class.
	 */
	private SSLProvider()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Installs own TrustManager and HostnameVerifier, that ensures full SSL security support by default,
     * but enables the possibility to add host name and port to an exception list, that will not be checked for 
     * a trusted SSL certificate or matching host name.
	 */
	public static void init()
	{
	    if (originalSocketFactory == null &&  originalHostnameVerifier == null)
	    {
	        try
	        {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[] {new SSLTrustManager()}, new SecureRandom());
         
                originalSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                originalHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new SSLHostnameVerifier());
	        }
	        catch (Exception ex)
	        {
	            logger.error(ex);
	        }
	    }
	}
	
    /**
     * Restores the original TrustManager and HostnameVerifier.
     */
	public static void restore()
	{
	    if (originalSocketFactory != null || originalHostnameVerifier != null)
        {
            HttpsURLConnection.setDefaultSSLSocketFactory(originalSocketFactory);
            HttpsURLConnection.setDefaultHostnameVerifier(originalHostnameVerifier);
            
            originalSocketFactory = null;
            originalHostnameVerifier = null;
        }
	}

	/**
	 * Adds a host and port definition to the white list.
	 * The host name and the port can contain wild cards.
	 * eg:
	 * <pre><code>
	 * SSLProvider.init();
	 * SSLProvider.addHostToException("*.badssl.com");      // will add an exception for all sub domains of badssl.com for port 443 
	 * SSLProvider.addHostToException("*.badssl.com:8443"); // will add an exception for all sub domains of badssl.com for port 8443
	 * SSLProvider.addHostToException("*.badssl.com:*");    // will add an exception for all sub domains of badssl.com for port all ports
	 * </code></pre>
	 * 
	 * @param pHostAndPortWithWildCard the host:port definition that can contain wild cards.
	 */
	public static void addHostToWhiteList(String pHostAndPortWithWildCard)
	{
	    whiteList.add(pHostAndPortWithWildCard);
	}
	
    /**
     * Removes a host and port definition from the white list.
     * 
     * @param pHostAndPortWithWildCard the host:port definition that can contain wild cards.
     */
    public static void removeHostFromWhiteList(String pHostAndPortWithWildCard)
    {
        whiteList.remove(pHostAndPortWithWildCard);
    }
    
    /**
     * Removes all host and port definitions from the white list.
     */
    public static void removeAllHostsFromWhiteList()
    {
        whiteList.clear();
    }
    
    /**
     * Gets all host and port definitions from the white list.
     * 
     * @return all host and port definitions from the white list.
     */
    public static String[] getHostsFromWhiteList()
    {
        return whiteList.toArray(new String[whiteList.size()]);
    }
    
	/**
	 * Gets, whether the given host name and port is contained in the white list, 
	 * considering the wild cards.
	 * 
	 * @param pHostName the host name
	 * @param pPort the port, -1 means, that it should not be checked.
	 * @return whether the given host name and port is contained in the white list, considering the wild cards.
	 */
    public static boolean isHostInWhiteList(String pHostName, int pPort)
    {
        String portToCheck = pPort < 0 ? "" : Integer.toString(pPort);
        
        for (String exception : whiteList)
        {
            int index = exception.indexOf(':');
            
            String host;
            String port;
            if (index < 0)
            {
                host = exception;
                port = "443";
            }
            else
            {
                host = exception.substring(0, index);
                port = exception.substring(index + 1);
            }
            
            if (StringUtil.like(pHostName, host) && (pPort < 0 || StringUtil.like(portToCheck, port)))
            {
                return true;
            }
        }
        
        return false;
    }
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>SSLTrustManager</code> ensures full SSL security support by default,
     * but enables the possibility to add host name and port to an exception list, that will not be checked for 
     * a trusted SSL certificate.
	 *  
     * @author Martin Handsteiner
	 */
    public static class SSLTrustManager extends X509ExtendedTrustManager
    {
        /** The original trust manager. */
        private X509ExtendedTrustManager originalTrustManager;
        
        /**
         * Constructs a new <code>SSLTrustManager</code>.
         * 
         * @throws Exception if it fails.
         */
        public SSLTrustManager() throws Exception
        {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore)null);
            originalTrustManager = (X509ExtendedTrustManager)tmf.getTrustManagers()[0];
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(X509Certificate[] pChain, String pAuthType) throws CertificateException 
        {
            originalTrustManager.checkClientTrusted(pChain, pAuthType);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(X509Certificate[] pChain, String pAuthType) throws CertificateException 
        {
            originalTrustManager.checkServerTrusted(pChain, pAuthType);
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public X509Certificate[] getAcceptedIssuers() 
        {
            return originalTrustManager.getAcceptedIssuers();
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(X509Certificate[] pChain, String pAuthType, Socket pSocket) throws CertificateException
        {
            if (isHostInWhiteList(pSocket.getInetAddress().getHostName(), pSocket.getPort()))
            {
                logger.debug("Check client trusted host '", pSocket.getInetAddress().getHostName(), ":", Integer.valueOf(pSocket.getPort()), "' is white listed!");
            }
            else
            {
                logger.debug("Check client trusted host '", pSocket.getInetAddress().getHostName(), ":", Integer.valueOf(pSocket.getPort()), "' is not white listed!");
                originalTrustManager.checkClientTrusted(pChain, pAuthType, pSocket);
            }
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(X509Certificate[] pChain, String pAuthType, Socket pSocket) throws CertificateException
        {
            if (isHostInWhiteList(pSocket.getInetAddress().getHostName(), pSocket.getPort()))
            {
                logger.debug("Check server trusted host '", pSocket.getInetAddress().getHostName(), ":", Integer.valueOf(pSocket.getPort()), "' is white listed!");
            }
            else
            {
                logger.debug("Check server trusted host '", pSocket.getInetAddress().getHostName(), ":", Integer.valueOf(pSocket.getPort()), "' is not white listed!");
                originalTrustManager.checkServerTrusted(pChain, pAuthType, pSocket);
            }
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(X509Certificate[] pChain, String pAuthType, SSLEngine pSSLEngine) throws CertificateException
        {
            if (isHostInWhiteList(pSSLEngine.getPeerHost(), pSSLEngine.getPeerPort()))
            {
                logger.debug("Check client trusted host '", pSSLEngine.getPeerHost(), ":", Integer.valueOf(pSSLEngine.getPeerPort()), "' is white listed!");
            }
            else
            {
                logger.debug("Check client trusted host '", pSSLEngine.getPeerHost(), ":", Integer.valueOf(pSSLEngine.getPeerPort()), "' is not white listed!");
                originalTrustManager.checkClientTrusted(pChain, pAuthType, pSSLEngine);
            }
        }
    
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(X509Certificate[] pChain, String pAuthType, SSLEngine pSSLEngine) throws CertificateException
        {
            if (isHostInWhiteList(pSSLEngine.getPeerHost(), pSSLEngine.getPeerPort()))
            {
                logger.debug("Check server trusted host '", pSSLEngine.getPeerHost(), ":", Integer.valueOf(pSSLEngine.getPeerPort()), "' is white listed!");
            }
            else
            {
                logger.debug("Check server trusted host '", pSSLEngine.getPeerHost(), ":", Integer.valueOf(pSSLEngine.getPeerPort()), "' is not white listed!");
                originalTrustManager.checkServerTrusted(pChain, pAuthType, pSSLEngine);
            }
        } 
    
    }

    /**
     * Installs own TrustManager and HostnameVerifier, that ensures full SSL security support by default,
     * but enables the possibility to add host name and port to an exception list, that will not be checked for 
     * a trusted SSL certificate or matching host name.
     */
    /**
     * The <code>SSLTrustManager</code> ensures full SSL security support by default,
     * but enables the possibility to add host name and port to an exception list, that will not be checked for 
     * a matching host name.
     *  
     * @author Martin Handsteiner
     */
    public static class SSLHostnameVerifier implements HostnameVerifier
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean verify(String pHostName, SSLSession pSession)
        {
            boolean isHostOk = isHostInWhiteList(pHostName, pSession.getPeerPort());
            
            logger.debug("Verify host '", pHostName, ":", Integer.valueOf(pSession.getPeerPort()), "' is ", isHostOk ? "white listed!" : "not white listed!");
            
            return isHostOk;
        }
    }; 
    
}	// SSLProvider
