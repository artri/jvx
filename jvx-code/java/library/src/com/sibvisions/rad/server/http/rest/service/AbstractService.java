/*
 * Copyright 2014 SIB Visions GmbH
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
 * 18.09.2014 - [JR] - creation
 * 04.09.2018 - [JR] - #1945: use prefix and postfix for code parameter
 * 05.03.2021 - [JR] - #2605: set content-disposition
 */
package com.sibvisions.rad.server.http.rest.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.rad.io.FileHandle;
import javax.rad.io.IFileHandle;
import javax.rad.persist.DataSourceException;
import javax.rad.server.InvalidPasswordException;
import javax.rad.server.NotFoundException;
import javax.rad.server.UnknownObjectException;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.IBean;
import javax.rad.type.bean.IBeanType;

import org.restlet.data.Disposition;
import org.restlet.data.MediaType;
import org.restlet.data.Metadata;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.util.StringUtils;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibvisions.rad.persist.bean.BeanConverter;
import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.HttpContext;
import com.sibvisions.rad.server.http.rest.JSONUtil;
import com.sibvisions.rad.server.http.rest.LifeCycleConnector;
import com.sibvisions.rad.server.http.rest.RESTServerContextImpl;
import com.sibvisions.rad.server.http.rest.service.mixin.BeanConverterMixin;
import com.sibvisions.rad.server.http.rest.service.mixin.DBAccessMixin;
import com.sibvisions.rad.server.security.SecurityContext;
import com.sibvisions.util.SimpleJavaSource;
import com.sibvisions.util.log.ILogger;
import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>AbstractService</code> is the base class for REST services.
 * 
 * @author René Jahn
 */
abstract class AbstractService extends ServerResource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /** the logger. */
    private static ILogger logger;
    
	
	/** the java code prefix. */
    protected static final String JCODE_START = "{j:";
	/** the java code suffix. */
	protected static final String JCODE_END = "}";
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public Representation handle()
    {
    	if (HttpContext.getCurrentInstance() != null)
    	{
    		return super.handle();
    	}
    	else
    	{
    		@Deprecated
	        HttpContext ctxt = new HttpContext(ServletUtils.getRequest(getRequest()), ServletUtils.getResponse(getResponse())); 
	        
	        try
	        {
	        	SecurityContext sctxt = new SecurityContext();
	        	sctxt.setHidePackages(false);
	
	    		try
	    		{
	        		RESTServerContextImpl srvctxt = new RESTServerContextImpl();
	
	        		try
		        	{
		        		return super.handle();
		        	}
		        	finally
		        	{
		        		srvctxt.release();
		        	}
	    		}
	    		finally
	    		{
	    			sctxt.release();
	    		}	        	
	        }
	        finally
	        {
	            ctxt.release();
	        }
    	}
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Handles an exception. This method logs the excception as debug message and sets
     * the status dependent of the exception.
     * 
     * @param pException the exception
     * @return <code>null</code>
     */
    protected Representation handleException(Throwable pException)
    {
        debug(pException);

        String sMessage = pException != null ? pException.getMessage() : "";
        
        if (pException instanceof DataSourceException)
        {
        	RestServiceException rse = ExceptionUtil.getThrowable(pException, RestServiceException.class);
        	
        	if (rse != null)
        	{
        		setStatus(new Status(rse.getStatus()), sMessage);
        		
        		return createExceptionResponse(rse, pException);
        	}
        }
        else if (pException instanceof RestServiceException)
        {
        	setStatus(new Status(((RestServiceException)pException).getStatus()), sMessage);
        	
        	return createExceptionResponse((RestServiceException)pException, null);
        }
        else if (pException instanceof SecurityException)
        {
    		if (getStatus().equals(Status.SUCCESS_OK))
    		{	
    			if (pException instanceof InvalidPasswordException)
            	{
            		setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
            	}
            	else
            	{
        			setStatus(Status.CLIENT_ERROR_FORBIDDEN);
            	}
    		}
        	
            return null;
        }
        else if (pException instanceof UnknownObjectException)
        {
            setStatus(Status.CLIENT_ERROR_NOT_FOUND, sMessage);
            return null;
        }
        
		if (getStatus().equals(Status.SUCCESS_OK))
		{	        
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST, sMessage);
		}

        return null;
    }
    
    /**
     * Creates a general representation for a given service exception.
     * 
     * @param pException the service exception
     * @param pCause the exception cause
     * @return the representation or <code>null</code> if no general representation could be created
     */
    protected Representation createExceptionResponse(RestServiceException pException, Throwable pCause)
    {
    	if (pException != null && pException.getDetails() != null)
    	{
    		List<Preference<MediaType>> mediaTypes = getRequest().getClientInfo().getAcceptedMediaTypes();
    	
    		//if the exception has details -> send details to the client, otherwise the response code is goog enough
    		IBean bean = new Bean();
    		bean.put("statusCode", Integer.valueOf(pException.getStatus()));
    		bean.put("details", pException.getDetails());
    		    
    		//if we have a custom cause, we use this one
    		if (pCause != null)
    		{
    			IBean bnCause = new Bean();
    			bnCause.put("class", pCause.getClass().getName());
    			bnCause.put("message", pCause.getMessage());
    			bnCause.put("stack", ExceptionUtil.dump(pCause, true));
    			
    			bean.put("cause", bnCause);
    		}
    		else if (pException.getCause() != null)
    		{
    			Throwable cause = pException.getCause();
    			
    			IBean bnCause = new Bean();
    			bnCause.put("class", cause.getClass().getName());
    			bnCause.put("message", cause.getMessage());
    			bnCause.put("stack", ExceptionUtil.dump(cause, true));
    			
    			bean.put("cause", bnCause);    	    			
    		}

    		for (Preference mediaType : mediaTypes) 
    		{
    			Metadata md = mediaType.getMetadata();
    			
    			if (MediaType.APPLICATION_JSON.equals(md)) 
    			{
    	        	return toInternalRepresentation(bean);
    			}
    			else if (MediaType.TEXT_HTML.equals(md))
				{
    				return toHtmlErrorRepresentation(bean);
				}
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Gets whether the request is a dry-run test. That means, no data manipulation should be done.
     * 
     * @return <code>true</code> if the request is a dry-run request (Request-Header: X-DRYRUN is set to "true"),
     *         <code>false</code> otherwise
     */
    protected boolean isDryRun()
    {
        Series series = (Series)getRequest().getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
        
        return Boolean.parseBoolean(series.getFirstValue("x-dryrun"));
    }
    
    /**
     * Configures a Jackson representation.
     * 
     * @param pObject the object to transfer
     * @return the configured representation
     */
    protected Representation toInternalRepresentation(final Object pObject)
    {
        if (pObject instanceof IFileHandle)
        {
            final IFileHandle ifh = ((IFileHandle)pObject);

            OutputRepresentation orep = new OutputRepresentation(MediaType.APPLICATION_OCTET_STREAM)
            {
                @Override
                public void write(OutputStream pStream) throws IOException
                {
                    FileUtil.copy(ifh.getInputStream(), pStream);
                }
            };              
            
            String sName = ifh.getFileName();
            
            if (!StringUtil.isEmpty(sName))
            {
                //#2605
	            Disposition disposition = new Disposition(Disposition.TYPE_ATTACHMENT);
	            disposition.setFilename(sName);
	            
	            orep.setDisposition(disposition);
	            
	            if (ifh instanceof FileHandle)
	            {
	            	FileHandle fh = (FileHandle)ifh;
	            	
	            	File fi = fh.getFile();
	            	
	            	if (fi != null)
	            	{
	            		orep.setModificationDate(new Date(fi.lastModified()));
	            		orep.setSize(fi.length());
	            	}
	            }
            }
            
            return orep;
        }
        else if (pObject instanceof InputStream)
        {
            return new OutputRepresentation(MediaType.APPLICATION_OCTET_STREAM)
            {
                @Override
                public void write(OutputStream pStream) throws IOException
                {
                    FileUtil.copy((InputStream)pObject, pStream);
                }
            };              
        }
        else if (pObject instanceof byte[])
        {
            return new OutputRepresentation(MediaType.APPLICATION_OCTET_STREAM)
            {
                @Override
                public void write(OutputStream pStream) throws IOException
                {
                	pStream.write((byte[])pObject);
                	pStream.flush();
                }
            };              
        }
        else
        {
	        JacksonRepresentation rep = new JacksonRepresentation(pObject);
	        
	        ObjectMapper mapper = rep.getObjectMapper();
	        
	        JSONUtil.configureObjectMapper(mapper);
	        
	        mapper.addMixInAnnotations(DBAccess.class, DBAccessMixin.class);
	        mapper.addMixInAnnotations(BeanConverter.class, BeanConverterMixin.class);
	        
	        return rep;
        }
    }
    
    /**
     * Converts an error bean to a html response.
     * 
     * @param pBean the error bean
     * @return the response/representation
     */
    protected Representation toHtmlErrorRepresentation(IBean pBean)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("   <title>Error page</title>\n");
		sb.append("</head>\n");
		sb.append("<body style=\"font-family: sans-serif;\">\n");
		sb.append("<p style=\"font-size: 1.2em;font-weight: bold;margin: 1em 0px;\">");
		sb.append("Status Code: ");
		sb.append(StringUtils.htmlEscape(pBean.get("statusCode").toString()));
		sb.append("</p>\n");

		dumpAsHtml(sb, pBean, "details");

		if (pBean.get("cause") != null)
		{
			sb.append("<span style='display: none; visibility: hidden;'>\n");
			dumpAsHtml(sb, pBean, "cause");
			sb.append("</span>\n");
			
		}
		
		sb.append("</body>\n");
		sb.append("</html>\n");
		
		return new StringRepresentation(sb.toString(), MediaType.TEXT_HTML);
    }
    
    /**
     * Dumps a bean property as html.
     * 
     * @param pOut the output buffer
     * @param pBean the bean
     * @param pPropertyName the name of the property from the bean
     */
    private void dumpAsHtml(StringBuilder pOut, IBean pBean, String pPropertyName)
    {
    	if (pBean != null)
    	{
    		IBean bean = (IBean)pBean.get(pPropertyName);
    		
    		if (bean != null)
    		{
	    		pOut.append("<p>\n");
	    		pOut.append("<p style='font-weight: bold;margin: 1em 0px;'>");
	    		pOut.append(StringUtil.formatInitCap(pPropertyName));
	    		pOut.append("</p>\n");
	    		
	    		IBeanType type = bean.getBeanType();
	    		
	    		if (type.getPropertyCount() > 0)
	    		{
	    			pOut.append("<table border='0' cellpading='0' cellspacing='0'>\n");
	    			pOut.append("<tr><th style='text-align: left; padding-left: 0; font-size: 0.95em;'>Property</th>" +
	    			                "<th style='text-align: left; padding-left: 10px; font-size: 0.95em;'>Value</th></tr>\n");
	    			
		    		for (String prop : type.getPropertyNames())
		    		{
		    			pOut.append("<tr><td style='padding-top: 5px; padding-left: 0;'>");
		    			pOut.append(StringUtils.htmlEscape(prop));
		    			pOut.append("</td><td style='padding-left: 10px; padding-top: 5px;>");
		    			pOut.append(StringUtils.htmlEscape(bean.get(prop) != null ? bean.get(prop).toString() : ""));
		    			pOut.append("</td></tr>\n");
		    		}
		    		
		    		pOut.append("</table>\n");
	    		}
	    		
	    		pOut.append("</p>\n");
    		}
    	}
    }
    
    /**
     * Logs debug information.
     * 
     * @param pInfo the debug information
     */
    public void debug(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }
        
        logger.debug(pInfo);
    }

    /**
     * Logs information.
     * 
     * @param pInfo the information
     */
    public void info(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }
        
        logger.info(pInfo);
    }

    
    /**
     * Logs error information.
     * 
     * @param pInfo the error information
     */
    public void error(Object... pInfo)
    {
        if (logger == null)
        {
            logger = LoggerFactory.getInstance(getClass());
        }

        logger.error(pInfo);
    }
    
    /**
     * Converts a string parameter to a Java object, if possible.
     * 
     * @param pParams the input parameter
     * @return a new object array which contains the converted parameter
     */
    protected Object[] convertParameter(Object... pParams)
    {
        if (pParams == null)
        {
            return null;
        }
        
        Object[] oResult = pParams.clone();

        SimpleJavaSource ssj = new SimpleJavaSource();
        ssj.addImport("javax.rad.model.condition.*");
        ssj.addImport("javax.rad.model.*");
        ssj.addImport("javax.rad.model.dataType.*");
        ssj.addImport("javax.rad.model.reference.*");
        ssj.addImport("javax.rad.type.*");
        ssj.addImport("javax.rad.type.bean.*");
        ssj.addImport("javax.rad.util.Parameter");
        ssj.addImport("java.math.*");
        
        ssj.addAllowedMethod("ICondition.*");
        ssj.addAllowedDerivedConstructors("ICondition");
        ssj.addAllowedMethod("SortDefinition.*");
        ssj.addAllowedDerivedConstructors("SortDefinition");
        ssj.addAllowedMethod("java.math.BigDecimal.*");
        ssj.addAllowedMethod("java.math.BigInteger.*");
        ssj.addAllowedMethod("java.util.Date.*");
        ssj.addAllowedMethod("java.sql.Timestamp.*");
        ssj.addAllowedMethod("java.lang.Number.*");
        ssj.addAllowedDerivedConstructors("java.lang.Number");
        ssj.addAllowedMethod("java.lang.String.*");
        ssj.addAllowedMethod("java.lang.Long.*");
        ssj.addAllowedMethod("java.lang.Boolean.*");
        
        for (int i = 0; i < pParams.length; i++)
        {
            if (pParams[i] instanceof String)
            {
            	String sParam = (String)pParams[i];
            	
            	if (sParam.startsWith(JCODE_START) && sParam.endsWith(JCODE_END))
            	{
	                try
	                {
	                    oResult[i] = ssj.execute(((String)pParams[i]).substring(JCODE_START.length(), ((String)pParams[i]).length() - JCODE_END.length()));
	                }
	                catch (Exception e)
	                {	                	
	                    debug(e);
	                }
            	}
            }           
        }
        
        return oResult;
    }
    
	/**
	 * Gets the session for the current user.
	 * 
	 * @return the session
	 */
	protected DirectServerSession getSession()
	{
		LifeCycleConnector con = ((LifeCycleConnector)getRequest().getClientInfo().getUser());
		
		if (con == null)
		{
			throw new NotFoundException("Lifecycle connector was not found!");
		}
		
	    return con.getSession();
	}
    
}   // AbstractService
