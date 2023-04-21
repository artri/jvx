/*
 * Copyright 2011 SIB Visions GmbH
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
 * 11.12.2011 - [JR] - creation
 * 18.12.2011 - [JR] - #525: convertValue implemented
 * 21.06.2018 - [JR] - recording implemented
 *                   - #1934: set SessionContext
 * 04.10.2018 - [JR] - sort definition support for fetch operation   
 * 05.03.2021 - [JR] - #2606: ISO8601 support for parameters              
 */
package com.sibvisions.rad.server.http.rest.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import javax.rad.model.ModelException;
import javax.rad.model.SortDefinition;
import javax.rad.model.condition.Equals;
import javax.rad.model.condition.ICondition;
import javax.rad.model.datatype.BinaryDataType;
import javax.rad.model.datatype.IDataType;
import javax.rad.model.datatype.TimestampDataType;
import javax.rad.persist.ColumnMetaData;
import javax.rad.persist.DataSourceException;
import javax.rad.persist.IStorage;
import javax.rad.persist.MetaData;
import javax.rad.server.ISession;
import javax.rad.server.ServerContext;
import javax.rad.server.SessionContext;
import javax.rad.type.bean.Bean;
import javax.rad.type.bean.BeanType;
import javax.rad.type.bean.IBean;
import javax.rad.type.bean.IBeanType;
import javax.rad.type.bean.PropertyDefinition;
import javax.rad.util.SilentAbortException;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.sibvisions.rad.persist.AbstractStorage;
import com.sibvisions.rad.persist.jdbc.DBAccess;
import com.sibvisions.rad.persist.jdbc.DBStorage;
import com.sibvisions.rad.server.DirectServerSession;
import com.sibvisions.rad.server.http.rest.JSONUtil;
import com.sibvisions.rad.server.http.rest.RESTAdapter;
import com.sibvisions.rad.server.http.rest.RESTServerContextImpl;
import com.sibvisions.rad.server.http.rest.service.config.StorageServiceConfig;
import com.sibvisions.rad.server.http.rest.service.config.StorageServiceConfig.Option;
import com.sibvisions.rad.server.protocol.ICategoryConstants;
import com.sibvisions.rad.server.protocol.ICommandConstants;
import com.sibvisions.rad.server.protocol.ProtocolFactory;
import com.sibvisions.rad.server.protocol.Record;
import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>StorageService</code> lists all available records of an {@link AbstractStorage}.
 * 
 * @author René Jahn
 */
public class StorageService extends AbstractService
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the standard date format. */
	private static final StdDateFormat DATE_FORMAT = StdDateFormat.instance;
	
	/** ISO8601 format. */
	private static final SimpleDateFormat DATE_FORMAT_STR_ISO8601 	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	/** ISO8601 format with timezone string. */
	private static final SimpleDateFormat DATE_FORMAT_STR_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	/** RFC1123 format. */
	private static final SimpleDateFormat DATE_FORMAT_STR_RFC1123 	= new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Representation handleException(Throwable pException)
    {
    	if (pException instanceof OptionDisabledException)
    	{
            debug(pException);

            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
    	}
    	else
    	{
    		return super.handleException(pException);
    	}
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Fetches records from the storage. It is possible to filter the result if query parameters are used.
	 * 
	 * @return the records as JSON representation
	 */
	@SuppressWarnings("deprecation")
	@Get
	public Representation executeFetch()
	{	
		info("FETCH-URL: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_GET);
	
		try
		{
			DirectServerSession session = getSession();
			
			ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();			
			
			String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
			
			SessionContext ctxt = createSessionContext(session, sObjectName, "get");
			
			Record recFetch = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_EXEC_FETCH);
			
			try
			{		
				AbstractStorage storage = (AbstractStorage)session.get(sObjectName);
				
				if (storage instanceof DBStorage)
				{
					((DBStorage)storage).setAllowedValues(false);
					((DBStorage)storage).setDefaultValue(false);
					
					ServerContext sctxt = ServerContext.getCurrentInstance();
					
					if (!(sctxt instanceof RESTServerContextImpl) 
						|| !((RESTServerContextImpl)sctxt).isManaged())
					{
						//fetch immediate - no IFileHandles
						((DBStorage)storage).setLazyFetchEnabled(false);
					}
				}
				
				checkOption(storage, Option.Fetch);
				
				Form query = getQuery();

		        //support paging
		        String sFirstRow = query.getFirstValue("_firstRow"); 
		        String sMaxRows = query.getFirstValue("_maxRows");
		        
		        int iStart = -1;
		        int iMaxRows = -1;
		        
		        if (!StringUtil.isEmpty(sFirstRow))
		        {
		        	try
		        	{
		        		iStart = Integer.parseInt(sFirstRow);
		        	}
		        	catch (Exception e)
		        	{
		        		//nothing to be done
		        	}
		        }
		        
		        if (!StringUtil.isEmpty(sMaxRows))
		        {
		        	try
		        	{
		        		iMaxRows = Integer.parseInt(sMaxRows);
		        	}
		        	catch (Exception e)
		        	{
		        		//nothing to be done
		        	}
		        }
		
		        List<IBean> liBeans;
		        
		        int iOldMaxTime = -1;
		        
		        try
		        {
		        	//available as request Parameter?
			        if (iStart >= 0 || iMaxRows >= 0)
			        {
				        if (storage instanceof DBStorage)
				        {
				        	iOldMaxTime = ((DBAccess)((DBStorage)storage).getDBAccess()).getMaxTime();
				        	
				        	((DBAccess)((DBStorage)storage).getDBAccess()).setMaxTime(-1);
				        }
			        }
			        	
			        if (iStart < 0)
			        {
			        	iStart = 0;
			        }

			        if (iMaxRows < 0)
			        {
			        	iMaxRows = -1;
			        }
			        
					liBeans = storage.fetchBean(getCondition(storage), getSortDefinition(), iStart, iMaxRows);
		        }
		        finally
		        {
		        	//reset
		        	if (iOldMaxTime >= 0)
		        	{
		        		((DBAccess)((DBStorage)storage).getDBAccess()).setMaxTime(iOldMaxTime);
		        	}
		        }
	
				//only show visible columns!
				
				MetaData md = storage.getMetaData();
				
				String[] sVisCols = md.getVisibleColumnNames();
				
				if (sVisCols != null && sVisCols.length > 0)
				{
					String[] sRemoveCols = ArrayUtil.removeAll(md.getColumnNames(), sVisCols);
					
					if (sRemoveCols != null && sRemoveCols.length > 0)
					{
						IBean bean;

						IBeanType btStorage = storage.getBeanConverter().getBeanType();						
						BeanType btNew = new BeanType();
						
						PropertyDefinition pdef;
						
						for (int i = 0, cnt = btStorage.getPropertyCount(); i < cnt; i++)
						{
							pdef = btStorage.getPropertyDefinition(i);
							
							if (ArrayUtil.contains(sVisCols, pdef.getName()))
							{
								btNew.addPropertyDefinition(pdef);
							}
						}
						
						//replace beans with cleaned properties
						for (int i = 0, cnt = liBeans.size(); i < cnt; i++)
						{
							bean = liBeans.get(i);

							Bean bnNew = new Bean(btNew);
							
							for (int j = 0; j < sVisCols.length; j++)
							{
								bnNew.put(sVisCols[j], bean.get(sVisCols[j]));								
							}
							
							liBeans.set(i, bnNew);
						}
					}
				}
		        
				Representation rep = toInternalRepresentation(liBeans);
				
	            setStatus(Status.SUCCESS_OK);
	
	            return rep;
			}
			finally
			{
				try
				{
					if (ctxt != null)
					{
						ctxt.release();
					}
				}
				finally
				{
					CommonUtil.close(recFetch);
				}
			}
		}
		catch (Throwable th)
		{
			if (record != null)
			{
				record.setException(th);
			}
			
			return handleException(th);
		}
		finally
		{
			CommonUtil.close(record);
		}
	}
	
	/**
	 * Inserts a new record.
	 * 
	 * @param pRepresentation the new record
	 * @return the inserted record
	 */
	@Post
	public Representation executeInsert(Representation pRepresentation)
	{
		info("INSERT-URL: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_POST);
		
		try
		{
			if (pRepresentation == null)
			{
				if (record != null)
				{
					record.setException(new SilentAbortException(Status.CLIENT_ERROR_BAD_REQUEST.toString()));
				}
				
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return null;
			}
			
			try
			{
				DirectServerSession session = getSession();
				
				ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();			
				
				String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
				
				SessionContext ctxt = createSessionContext(session, sObjectName, "post");
				
				Record recInsert = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_EXEC_INSERT);
				
				try
				{			
					AbstractStorage storage = (AbstractStorage)session.get(sObjectName);
					
					checkOption(storage, Option.Insert);
					
					HashMap<String, Object> hmpObject = JSONUtil.getObject(pRepresentation, HashMap.class);
					
					if (hmpObject == null)
					{
						if (recInsert != null)
						{
							recInsert.setException(new SilentAbortException(Status.CLIENT_ERROR_BAD_REQUEST.toString()));
						}						
						
						setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						return null;
					}
					
					IBean bean = storage.createEmptyBean();
					
					copyValues(storage, bean, hmpObject);
					
					bean = storage.insert(bean);
					
					return toInternalRepresentation(removeInvisibleValues(storage, bean));
				}
				finally
				{
					try
					{
						if (ctxt != null)
						{
							ctxt.release();
						}
					}
					finally
					{
						CommonUtil.close(recInsert);
					}
				}
			}
			catch (Throwable th)
			{
				if (record != null)
				{
					record.setException(th);
				}
				
				return handleException(th);
			}
		}
		finally
		{
			CommonUtil.close(record);
		}
	}
	
	/**
	 * Updates a record. It is not possible to change columns that are primary key columns.
	 * 
	 * @param pRepresentation the new record
	 * @return the updated record
	 */
	@Put
	public Representation executeUpdate(Representation pRepresentation)
	{
		info("UPDATE-URL: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_PUT);
		
		try
		{		
			if (pRepresentation == null)
			{
				if (record != null)
				{
					record.setException(new SilentAbortException(Status.CLIENT_ERROR_BAD_REQUEST.toString()));
				}
				
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return null;
			}
			
			try
			{
				DirectServerSession session = getSession();
	
				ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();
				
				String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
				
				SessionContext ctxt = createSessionContext(session, sObjectName, "put");
				
				Record recUpdate = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_EXEC_UPDATE);
				
				try
				{
					AbstractStorage storage = (AbstractStorage)session.get(sObjectName);
			
					checkOption(storage, Option.Update);
					
					List<IBean> liBeans = storage.fetchBean(getCondition(storage), null, 0, 2);
					
					if (liBeans.size() == 0)
					{
						if (recUpdate != null)
						{
							recUpdate.setException(new SilentAbortException(Status.CLIENT_ERROR_NOT_FOUND.toString()));
						}
						
						setStatus(Status.CLIENT_ERROR_NOT_FOUND);
						return null;
					}
					
					if (liBeans.size() > 1)
					{
						if (recUpdate != null)
						{
							recUpdate.setException(new SilentAbortException(Status.CLIENT_ERROR_CONFLICT.toString()));
						}
						
						setStatus(Status.CLIENT_ERROR_CONFLICT);
						return null;
					}
					
					HashMap<String, Object> hmpObject = JSONUtil.getObject(pRepresentation, HashMap.class);
					
					if (hmpObject == null)
					{
						if (recUpdate != null)
						{
							recUpdate.setException(new SilentAbortException(Status.CLIENT_ERROR_BAD_REQUEST.toString()));
						}						
						
						setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						return null;
					}
					
					IBean bean = liBeans.get(0);
					
					//we need the old bean for the update, so we create a "copy" and set new values in this new object
					IBean beanNew = bean.clone();
					
					copyValues(storage, beanNew, hmpObject);
					
					beanNew = storage.update(bean, beanNew);
					
					return toInternalRepresentation(removeInvisibleValues(storage, beanNew));
				}
				finally
				{
					try
					{
						if (ctxt != null)
						{
							ctxt.release();
						}
					}
					finally
					{
						CommonUtil.close(recUpdate);
					}
				}
			}
			catch (Throwable th)
			{
				if (record != null)
				{
					record.setException(th);
				}
				
				return handleException(th);
			}
		}
		finally
		{
			CommonUtil.close(record);
		}
	}
	
	/**
	 * Deletes records from the storage. It is possible to delete more than one row if query parameters are used. If no
	 * record is found, the NOT_FOUND status is set. If PK is used and more than one records were found, the CONFLICT status 
	 * is set. 
	 * 
	 * @return the number of deleted records
	 */
	@Delete
	public Representation executeDelete()
	{
		info("DELETE-URL: ", getRequest().getResourceRef());
		
		Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_DELETE);
		
		try
		{
			DirectServerSession session = getSession();
			
			ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();
			
			String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
			
			SessionContext ctxt = createSessionContext(session, sObjectName, "delete");
			
			Record recDelete = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_EXEC_DELETE);
			
			try
			{
				AbstractStorage storage = (AbstractStorage)session.get(sObjectName);
				
				checkOption(storage, Option.Delete);
				
				List<IBean> liBeans = storage.fetchBean(getCondition(storage), null, 0, 2);
				
				if (liBeans.size() == 0)
				{
					if (recDelete != null)
					{
						recDelete.setException(new SilentAbortException(Status.CLIENT_ERROR_NOT_FOUND.toString()));
					}
					
					setStatus(Status.CLIENT_ERROR_NOT_FOUND);
					return null;
				}
				
				//PK -> exactly one record
				if (cmpAttrib.get(RESTAdapter.PARAM_PK) != null)
				{
					if (liBeans.size() > 1)
					{
						if (recDelete != null)
						{
							recDelete.setException(new SilentAbortException(Status.CLIENT_ERROR_CONFLICT.toString()));
						}
						
						setStatus(Status.CLIENT_ERROR_CONFLICT);
						return null;
					}
				}
				
				info("Number of records to delete: ", Integer.valueOf(liBeans.size()));
	
				if (!isDryRun())
				{
					for (IBean bean : liBeans)
					{
						storage.delete(bean);
					}
				}
				
				return toInternalRepresentation(Integer.valueOf(liBeans.size()));
			}
			finally
			{
				try
				{
					if (ctxt != null)
					{
						ctxt.release();
					}
				}
				finally
				{
					CommonUtil.close(recDelete);
				}
			}
		}
		catch (Throwable th)
		{
			if (record != null)
			{
				record.setException(th);
			}
			
			return handleException(th);
		}
		finally
		{
			CommonUtil.close(record);
		}
	}
	
	/**
	 * Gets the metadata for the storage.
	 * 
	 * @return the metadata
	 */
	@Options
	public Representation executeGetMetaData()
	{
		info("METADATA-URL: ", getRequest().getResourceRef());
		
		if (((RESTAdapter)getApplication()).isServiceEnabled("storageMetaData"))
		{
			Record record = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_OPTIONS);
			
			try
			{
				DirectServerSession session = getSession();
				
				ConcurrentMap<String, Object> cmpAttrib = getRequest().getAttributes();
	
				String sObjectName = (String)cmpAttrib.get(RESTAdapter.PARAM_OBJECT_NAME);
				
				SessionContext ctxt = createSessionContext(session, sObjectName, "options");
				
				Record recMetadata = ProtocolFactory.openRecord(ICategoryConstants.REST, ICommandConstants.REST_EXEC_METADATA);
				
				try
				{
					AbstractStorage storage = (AbstractStorage)session.get(sObjectName);

					checkOption(storage, Option.MetaData);
					
					MetaData md = storage.getMetaData();
					
					String[] sVisCols = md.getVisibleColumnNames();
					
					if (sVisCols == null || sVisCols.length == 0)
					{
						return toInternalRepresentation(md);	
					}
					
					String[] sRemoveCols = ArrayUtil.removeAll(md.getColumnNames(), sVisCols);
					
					if (sRemoveCols != null && sRemoveCols.length > 0)
					{
						//hard copy
	
						MetaData mdNew = new MetaData();
						
						mdNew.setPrimaryKeyColumnNames(ArrayUtil.removeAll(md.getPrimaryKeyColumnNames(), sRemoveCols));
						mdNew.setAutoIncrementColumnNames(ArrayUtil.removeAll(md.getAutoIncrementColumnNames(), sRemoveCols));
						mdNew.setRepresentationColumnNames(ArrayUtil.removeAll(md.getRepresentationColumnNames(), sRemoveCols));
						
						ColumnMetaData[] cmd = md.getColumnMetaData();
						
						ArrayUtil<ColumnMetaData> auCmdNew = new ArrayUtil<ColumnMetaData>();
						
						for (int i = 0; i < cmd.length; i++)
						{
							if (!ArrayUtil.contains(sRemoveCols, cmd[i].getName()))
							{
								auCmdNew.add(cmd[i]);
							}
						}
						
						mdNew.setColumnMetaData(auCmdNew.toArray(new ColumnMetaData[auCmdNew.size()]));
	
						return toInternalRepresentation(mdNew);
					}
					else
					{
						return toInternalRepresentation(md);
						
					}
				}
				finally
				{
					try
					{
						if (ctxt != null)
						{
							ctxt.release();
						}
					}
					finally
					{
						CommonUtil.close(recMetadata);
					}
				}
			}
			catch (Throwable th)
			{
				if (record != null)
				{
					record.setException(th);
				}
				
				return handleException(th);
			}
			finally
			{
				CommonUtil.close(record);
			}
		}
		else
		{
			debug("Service 'storageMetaData' is disabled!");

			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return null;
		}
	}
	
	/**
	 * Gets the condition based on the URL and query parameters.
	 * 
	 * @param pStorage the storage
	 * @return the condition or <code>null</code> if no condition is used
	 * @throws DataSourceException if metadata detection fails
	 */
	private ICondition getCondition(IStorage pStorage) throws DataSourceException
	{
        ICondition cond = null;
		
		MetaData mdat = pStorage.getMetaData();

		String[] sPKColumns = mdat.getPrimaryKeyColumnNames();
		
		Object oPK = getRequest().getAttributes().get(RESTAdapter.PARAM_PK); 

		
		//URL with a PK -> assume that the storage has exactly one PK column
		if (oPK != null)
		{
			if (sPKColumns == null || sPKColumns.length > 1)
			{
				setStatus(Status.CLIENT_ERROR_CONFLICT);
				return null;
			}
			
			cond = new Equals(sPKColumns[0], convertValue(mdat, sPKColumns[0], oPK));
		}

        Form query = getQuery();

        //support custom "complex" conditions
        String sCondition = query.getFirstValue("_condition"); 
        
        if (!StringUtil.isEmpty(sCondition))
        {
        	sCondition = toJavaCode(sCondition);
        	
            ICondition condParam;
            
            try
            {
                condParam = (ICondition)convertParameter(sCondition)[0];
                
                if (cond == null)
                {
                    cond = condParam;
                }
                else
                {
                    cond = cond.and(condParam);
                }
                
            }
            catch (Exception ex)
            {
                info(ex);
            }
        }
            
		//It is possible to add additional conditions via query parameter (AND)
		String[] sValidColumnNames = mdat.getColumnNames();
		String[] sVisCols = mdat.getVisibleColumnNames();
		
		if (sVisCols != null && sVisCols.length > 0)
		{
			sVisCols = ArrayUtil.intersect(sValidColumnNames, mdat.getVisibleColumnNames());
		}
		
		String sColName;
		String sValue;
		
		Parameter param;
		
		for (int i = 0, anz = query.size(); i < anz; i++)
		{
			param = query.get(i);

			sColName = param.getName().toUpperCase();
		
			sValue = param.getValue();
			
			//empty means null
			if (sValue != null && sValue.length() == 0)
			{
				sValue = null;
			}
			
			if (ArrayUtil.contains(sValidColumnNames, sColName))
			{
				if (cond == null)
				{
					cond = new Equals(sColName, convertValue(mdat, sColName, sValue));
				}
				else
				{
					cond = cond.and(new Equals(sColName, convertValue(mdat, sColName, sValue)));
				}
			}
		}
        
    	return cond;
	}
	
	/**
	 * Gets the sort definition based on the URL and query parameters.
	 * 
	 * @return the sort definition or <code>null</code> if no sort definition is used
	 */
	private SortDefinition getSortDefinition()
	{
        //support custom "complex" sort definition
        String sSortDefinition = getQuery().getFirstValue("_sort"); 
        
        if (!StringUtil.isEmpty(sSortDefinition))
        {
        	sSortDefinition = toJavaCode(sSortDefinition);
        	
            try
            {            	
                return (SortDefinition)convertParameter(sSortDefinition)[0];
            }
            catch (Exception ex)
            {
                info(ex);
            }
        }
        
		return null;
	}
	
	/**
	 * Converts pseudo java code to a java code string. If the pseudo code isn't already marked as
	 * java code, it will be marked automatically.
	 * 
	 * @param pCode the pseudo java code (without imports, ...)
	 * @return the pseudo code with java code prefix and suffix
	 */
	private String toJavaCode(String pCode)
	{
		String sCode = pCode;
		
		if (!sCode.startsWith(JCODE_START))
		{
			sCode = JCODE_START + sCode;
		}
		
		if (!sCode.endsWith(JCODE_END))
		{
			sCode = sCode + JCODE_END;
		}
		
		return sCode;
	}
	
	/**
	 * Copies the values from a request to a predefined bean object.
	 * 
	 * @param pStorage the storage to use
	 * @param pBean the predefined bean
	 * @param pData the request data
	 * @throws Exception if copy fails because of metadata access problems or type conversion errors
	 */
	private void copyValues(AbstractStorage pStorage, IBean pBean, HashMap<String, Object> pData) throws Exception
	{
		MetaData mdat = pStorage.getMetaData();
		
		String[] sValidColumnNames = mdat.getColumnNames();
		String[] sVisCols = mdat.getVisibleColumnNames();
		
		if (sVisCols != null && sVisCols.length > 0)
		{
			sValidColumnNames = ArrayUtil.intersect(sValidColumnNames, sVisCols);
		}
		
		String sColumnName;
		Object oValue;
		
		for (Entry<String, Object> entry : pData.entrySet())
		{
			sColumnName = entry.getKey().toUpperCase();
			
			if (ArrayUtil.contains(sValidColumnNames, sColumnName))
			{
				oValue = entry.getValue();

				if (oValue != null)
				{
					int iType = mdat.getColumnMetaData(sColumnName).getTypeIdentifier();
					
					if (iType == TimestampDataType.TYPE_IDENTIFIER)
					{
						//try to convert
						oValue = DATE_FORMAT.parse(oValue.toString());
					}
					else if (iType == BinaryDataType.TYPE_IDENTIFIER)
					{
						if (oValue instanceof String)
						{
							oValue = CodecUtil.decodeBase64((String)oValue);
						}
					}
	
					//We need BigDecimal
					if (oValue instanceof Number && !(oValue instanceof BigDecimal))
					{
						if (oValue instanceof BigInteger)
						{
							oValue = new BigDecimal((BigInteger)oValue); 
						}
						else
						{
							oValue = new BigDecimal(((Number)oValue).toString());
						}
					}
				}
				
				pBean.put(sColumnName, oValue);
			}
		}
	}
	
	/**
	 * Removes all invisible columns from the given bean.
	 * 
	 * @param pStorage the storage
	 * @param pBean the original bean
	 * @return the bean without invisible columns
	 * @throws DataSourceException if accessing metadata fails
	 */
	private IBean removeInvisibleValues(AbstractStorage pStorage, IBean pBean) throws DataSourceException
	{
		MetaData md = pStorage.getMetaData();
		
		String[] sVisCols = md.getVisibleColumnNames();
		
		if (sVisCols != null && sVisCols.length > 0)
		{
			String[] sRemoveCols = ArrayUtil.removeAll(md.getColumnNames(), sVisCols);
			
			if (sRemoveCols != null && sRemoveCols.length > 0)
			{	
				IBeanType btBean = pBean.getBeanType();						
				BeanType btNew = new BeanType();
				
				PropertyDefinition pdef;
				
				for (int i = 0, cnt = btBean.getPropertyCount(); i < cnt; i++)
				{
					pdef = btBean.getPropertyDefinition(i);
					
					if (ArrayUtil.contains(sVisCols, pdef.getName()))
					{
						btNew.addPropertyDefinition(pdef);
					}
				}
				
				Bean bnNew = new Bean(btNew);
				
				for (int j = 0; j < sVisCols.length; j++)
				{
					bnNew.put(sVisCols[j], pBean.get(sVisCols[j]));								
				}
				
				return bnNew;
			}
		}

		return pBean;
	}
	
	/**
	 * Converts a value to an object type of the column metadata type.
	 * 
	 * @param pMetaData the meta data
	 * @param pColumnName the column name
	 * @param pValue the input value
	 * @return the converted output value
	 */
	private Object convertValue(MetaData pMetaData, String pColumnName, Object pValue)
	{
		try
		{
			IDataType type = pMetaData.getColumnMetaData(pColumnName).getDataType();
			
			if (type.getTypeIdentifier() == TimestampDataType.TYPE_IDENTIFIER)
			{
				//#2606
				if (pValue instanceof String)
				{
					String sValue = (String)pValue;
					
					try
					{
						//faster to search starting at the end
						if (sValue.lastIndexOf("+") >= 0)
						{
							if (sValue.indexOf(",") >= 0)
							{
								return DATE_FORMAT_STR_RFC1123.parse(sValue);
							}
							else
							{
								return DATE_FORMAT_STR_ISO8601.parse(sValue);
							}
						}
						else if (sValue.endsWith("Z"))
						{
							return DATE_FORMAT_STR_ISO8601_Z.parse(sValue);
						}
					}
					catch (Exception ex)
					{
						debug(ex);
					}
				}
			}
			
			return type.convertToTypeClass(pValue);
			
		}
		catch (ModelException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a session context.
	 * 
	 * @param pSession the session
	 * @param pObjectName the object name
	 * @param pMethodName the method name
	 * @return the session context
	 */
	protected SessionContext createSessionContext(ISession pSession, String pObjectName, String pMethodName)
	{
		RESTSessionContextImpl context = new RESTSessionContextImpl(pSession);
		
		context.setObjectName(pObjectName);
		context.setMethodName(pMethodName);
		
		return context;
	}
	
	/**
	 * Checks if a specific option is disabled.
	 * 
	 * @param pStorage the storage
	 * @param pOption the option to check
	 */
	protected void checkOption(AbstractStorage pStorage, Option pOption)
	{
		StorageServiceConfig cfg = (StorageServiceConfig)pStorage.getObject(StorageServiceConfig.class.getName());
		
		if (cfg != null)
		{
			if (!cfg.isOptionEnabled(pOption))
			{
				throw new OptionDisabledException();
			}
		}
	}
	
    //****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>OptionDisabledException</code> is a marker Exception to define that an Option is disabled.
	 * 
	 * @author René Jahn
	 */
	private static final class OptionDisabledException extends RuntimeException
	{
	}	// OptionDisabledException
	
}	// StorageService
