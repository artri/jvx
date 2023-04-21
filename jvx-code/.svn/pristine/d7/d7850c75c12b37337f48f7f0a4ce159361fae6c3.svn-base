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
 * 01.10.2008 - [RH] - creation
 * 02.11.2008 - [RH] - conversion of object to storage and back removed; compare use == for equals ->Internalize
 * 13.11.2008 - [RH] - clone moved to DataType() 
 * 13.03.2010 - [JR] - #88: getTypeIdentifier implemented
 */
package javax.rad.model.datatype;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;

import javax.rad.io.IFileHandle;
import javax.rad.io.RemoteFileHandle;
import javax.rad.model.ModelException;
import javax.rad.model.ui.ICellEditor;

import com.sibvisions.util.type.CodecUtil;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.FileUtil;

/**
 * A <code>BinaryDataType</code> is the data type class of a 
 * binary <code>ColumnDefinition</code>.
 *  
 * @author Roland Hörmann
 */
// TODO [RH] hashcode is wrong.
public class BinaryDataType extends DataType
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the type identifier. */
	public static final int TYPE_IDENTIFIER = Types.BINARY;

	/** The encoding. */
	private String encoding = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Constructs a <code>BinaryDataType</code> with the maximum <code>Integer</code> size.
	 */
	public BinaryDataType()
	{
	}

	/**
	 * Constructs a <code>BinaryDataType</code> with the given <code>Integer</code> size.
	 * 
	 * @param pSize the size
	 */
	public BinaryDataType(int pSize)
	{
		setSize(pSize);
	}
	
	/**
	 * Constructs a <code>BinaryDataType</code> with the maximum <code>Integer</code> size and the given cell editor.
	 * 
	 * @param pCellEditor the cell editor
	 */
	public BinaryDataType(ICellEditor pCellEditor)
	{
		setCellEditor(pCellEditor);
	}
	
	/**
	 * Constructs a <code>BinaryDataType</code> with the given <code>Integer</code> size and the given cell editor.
	 * 
	 * @param pSize the size
	 * @param pCellEditor the cell editor
	 */
	public BinaryDataType(int pSize, ICellEditor pCellEditor)
	{
		setSize(pSize);
		setCellEditor(pCellEditor);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public int getTypeIdentifier()
	{
		return TYPE_IDENTIFIER;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getTypeClass()
	{
		if (getCellEditor() != null || getCellRenderer() != null)
		{
            return byte[].class;
		}
		else
		{
            return Object.class;
		}
	}
	
    /**
     * {@inheritDoc}
     */
    public Object convertAndCheckToTypeClass(Object pObject) throws ModelException
	{
		Object baObject = convertToTypeClass(pObject);
    	
		if (baObject == null)
		{
			return null;
		}
		
		//otherwise, e.g. the filehandle will retrieve it's size, to check that it's "unlimited"
		if (getSize() == Integer.MAX_VALUE)
		{
			return baObject;
		}

		int length;
		if (baObject instanceof byte[])
		{
			length = ((byte[])baObject).length;
		}
		else if (baObject instanceof IFileHandle)
		{
			try
			{
				length = (int)((IFileHandle)baObject).getLength();
			}
			catch (Exception ex)
			{
				length = 0;
				// Silent, if we don't know the size, we suggest, its ok.
			}
		}
		else
		{
			throw new ModelException("Object type is not supported!");
		}

		if (length > getSize())
		{
			throw new ModelException("Binary too large! - length from " +
					length + " to " + getSize());
		}
		
    	return baObject;
	}
    
	/**
	 * {@inheritDoc}
	 */
	public String convertToString(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof byte[])
		{
			byte[] bytes = (byte[])pObject;
			if (encoding != null)
			{
				try
				{
					return new String(bytes, encoding);
				}
				catch (UnsupportedEncodingException e)
				{
					// Fall back to no encoding
				}
			}
			return new String(bytes);
		}
		else if (pObject instanceof IFileHandle)
		{
			try
			{
				byte[] bytes = FileUtil.getContent(((IFileHandle)pObject).getInputStream());
				if (encoding != null)
				{
					try
					{
						return new String(bytes, encoding);
					}
					catch (UnsupportedEncodingException e)
					{
						// Fall back to no encoding
					}
				}
				return new String(bytes);
			}
			catch (Exception pEx)
			{
				// Silent, we don't know the String
			}
		}
		
		return pObject.toString();
	}    

	/**
	 * {@inheritDoc}
	 */
	public String convertToUnifiedString(Object pObject)
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof byte[])
		{
			return CodecUtil.encodeBase64((byte[])pObject);
		}
		else if (pObject instanceof IFileHandle)
		{
			try
			{
				return CodecUtil.encodeBase64(FileUtil.getContent(((IFileHandle)pObject).getInputStream()));
			}
			catch (Exception pEx)
			{
				// Silent, we don't know the String
			}
		}
		
		return pObject.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object convertToTypeClass(Object pObject) throws ModelException
	{
		if (pObject == null)
		{
			return null;
		}
		else if (pObject instanceof byte[])
		{
			byte[] baObject = (byte[])pObject;
			if (baObject.length == 0)
			{
				return null;
			}
			return baObject;
		}
		else if (pObject instanceof String)
		{
			String sObject = (String)pObject;
			
			if (sObject.length() == 0)
			{
				return null;
			}
			
			try
			{
				return CodecUtil.decodeBase64(sObject);
			}
			catch (Exception pEx)
			{
				// Silent, we don't know the String
			}

			return sObject.getBytes();
		}
		else if (pObject instanceof IFileHandle)
		{
			// Skip over it, the RemoteFileHandle is used for lazy fetch.
			return pObject;
		}
		else if (pObject instanceof InputStream)
		{
		    byte[] baObject = FileUtil.getContent((InputStream)pObject);
		    
		    if (baObject.length == 0)
		    {
		        return null;
		    }
		    
		    return baObject;
		}
		else if (pObject instanceof InputStreamReader)
		{
            byte[] baObject = FileUtil.getContent((InputStreamReader)pObject);
            
            if (baObject.length == 0)
            {
                return null;
            }
            
            return baObject;
		}
		
		throw new ModelException("Conversion failed! Type not supported ! from " +  
				pObject.getClass().getName() + " to " + getTypeClass().getName());
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Object pObject1, Object pObject2)
	{
		// possible because of Internalize.intern()
		if (pObject1 == pObject2)
		{
			return 0;
		}		
		try
		{
			IFileHandle rf1 = null;
			IFileHandle rf2 = null;
			
			if (pObject1 instanceof IFileHandle)
			{
				rf1 = (IFileHandle)pObject1;
			}
			if (pObject2 instanceof IFileHandle)
			{
				rf2 = (IFileHandle)pObject2;
			}
			if (rf1 != null && rf2 != null 
					&& rf1 instanceof RemoteFileHandle && rf2 instanceof RemoteFileHandle
					&& CommonUtil.equals(((RemoteFileHandle)rf1).getObjectCacheKey(), ((RemoteFileHandle)rf2).getObjectCacheKey()))
			{
				return 0;
			}
			if (pObject1 != null && pObject2 != null)
			{
				byte[] ba1;
				byte[] ba2;
				
				int la1;
				if (rf1 == null)
				{
					ba1 = (byte[])pObject1;
					la1 = ba1.length;
				}
				else
				{
					ba1 = null;
					la1 = (int)rf1.getLength();
				}
				int la2;
				if (rf2 == null)
				{
					ba2 = (byte[])convertToTypeClass(pObject2);
					la2 = ba2.length;
				}
				else
				{
					ba2 = null;
					la2 = (int)rf2.getLength();
				}
				
				if (la1 < la2)
				{
					return -1;
				}
				else if (la1 > la2)
				{
					return 1;
				} 
				
				if (rf1 != null)
				{
					ba1 = FileUtil.getContent(rf1.getInputStream());
				}
				if (rf2 != null)
				{
					ba2 = FileUtil.getContent(rf2.getInputStream());
				}
				
				for (int i = 0; i < ba1.length; i++)
				{
					if (ba1[i] < ba2[i])
					{
						return -1;
					} 
					else if (ba1[i] > ba2[i])
					{
						return 1;
					} 
				}
				return 0;
			}
		}
		catch (Exception modelException)
		{
			return 1;
		}
		
		return super.compareTo(pObject1, pObject2);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public BinaryDataType clone()
	{
		return (BinaryDataType)super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object prepareValue(Object pObject) throws ModelException
	{	
		if (pObject instanceof IFileHandle && (getCellEditor() != null || getCellRenderer() != null))
		{
			// RemoteFileHandle is used for lazy fetch of values.
			// So we only need to get the content of the RemoteFileHandle.
			IFileHandle handle = (IFileHandle) pObject;
			try
			{
				return FileUtil.getContent(handle.getInputStream());
			}
			catch (IOException e)
			{
				throw new ModelException(e);
			}
		}
		
		return pObject;
	}
	
	/**
	 * Get's the content to the given object.
	 * @param pBinaryObject the object
	 * @return the byte[]
	 * @throws IOException if it fails.
	 */
	public static final byte[] getContent(Object pBinaryObject) throws IOException
	{
		if (pBinaryObject == null)
		{
			return null;
		}
		else if (pBinaryObject instanceof byte[])
		{
			return (byte[])pBinaryObject;
		}
		else if (pBinaryObject instanceof IFileHandle)
		{
		    IFileHandle fileHandle = (IFileHandle)pBinaryObject;
            long size = fileHandle.getLength();
            if (size > Integer.MAX_VALUE)
            {
                throw new IOException("File is too large (>" + Integer.MAX_VALUE + " bytes)!");
            }
		    
		    return FileUtil.getContent(fileHandle.getInputStream(), (int)size);
		}
		else if (pBinaryObject instanceof File)
		{
			return FileUtil.getContent((File)pBinaryObject);
		}
		else if (pBinaryObject instanceof InputStream)
		{
			return FileUtil.getContent((InputStream)pBinaryObject);
		}
		else if (pBinaryObject instanceof InputStreamReader)
		{
			return FileUtil.getContent((InputStreamReader)pBinaryObject);
		}
        else if (pBinaryObject instanceof Blob)
        {
            try
            {
                return FileUtil.getContent(((Blob)pBinaryObject).getBinaryStream());
            }
            catch (SQLException ex)
            {
                throw new IOException("Problem when reading a blob", ex);
            }
        }
		
		throw new IOException(pBinaryObject.getClass() + " is a not supported binary object!");
	}
	
	/**
	 * Gets the encoding, that is used for converting to String.
	 * null means default encoding.
	 * @return the used encoding
	 */
	public String getEncoding()
	{
		return encoding;
	}
	
	/**
	 * Sets the encoding, that is used for converting to String.
	 * null means default encoding.
	 * @param pEncoding the used encoding
	 */
	public void setEncoding(String pEncoding)
	{
		encoding = pEncoding;
	}
	
} 	// BinaryDataType
