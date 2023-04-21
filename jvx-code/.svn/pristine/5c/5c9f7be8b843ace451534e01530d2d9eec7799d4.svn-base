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
 * 08.04.2009 - [JR] - creation
 * 03.09.2009 - [JR] - getName implemented
 * 04.08.2009 - [JR] - getContent with InputStreamReader implemented
 * 27.10.2010 - [JR] - delete, zip implemented
 * 10.11.2010 - [JR] - getContent(String), getContent(File) implemented
 * 02.12.2010 - [JR] - deleteEmpty: boolean parameter added
 * 06.12.2010 - [JR] - copyFile: support directory target
 * 10.12.2010 - [JR] - move implemented
 * 18.02.2011 - [JR] - unzip implemented
 * 10.03.2011 - [JR] - replace implemented
 * 06.04.2011 - [JR] - listZipEntries implemented
 * 12.04.2011 - [JR] - getDirectory implemented
 * 02.06.2011 - [JR] - used BufferedOutputStream where FileInputStream is used
 * 04.06.2011 - [JR] - save creates directories
 * 19.06.2011 - [JR] - save with InputStream as parameter
 * 17.11.2011 - [JR] - moveDirectory: 
 *                     * delete directory
 *                     * keep works now recursive
 * 18.03.2013 - [JR] - zip now creates folder entries    
 * 25.05.2013 - [JR] - zip: support files in root directory
 * 13.08.2013 - [JR] - zip: fixed problem with folders (crc or size needed)
 * 30.08.2013 - [JR] - copyDirectory: create empty source directory   
 * 14.12.2013 - [JR] - deleteSub implemented   
 * 01.12.2014 - [JR] - zip file list        
 * 15.05.2015 - [JR] - #1388: support ZipInputStream   
 */
package com.sibvisions.util.type;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.FileSearch;
import com.sibvisions.util.log.LoggerFactory;

/**
 * The <code>FileUtil</code> contains file and filename dependent 
 * utility methods.
 * 
 * @author René Jahn
 */
public final class FileUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class Members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** File or directory copy mode. */
	public enum CopyMode
	{
		/** Backup existing files. */
		Backup,
		/** Overwrite existing files. */
		Overwrite,
		/** Keep existing files and copy only new files. */
		Keep
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Invisible constructor because <code>FileUtil</code> is a utility
	 * class.
	 */
	private FileUtil()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Checks, if 2 files are equal.
	 * 
	 * @param pFile1 file 1
	 * @param pFile2 file 2
	 * @return true, if the 2 files are equal.
	 */
	public static boolean equals(File pFile1, File pFile2)
	{
		if (pFile1 != null && pFile2 != null && pFile1.exists() && pFile2.exists() && pFile1.length() == pFile2.length())
		{
			if (!pFile1.equals(pFile2))
			{
				InputStream str1 = null;
				InputStream str2 = null;
				
				try
				{
					str1 = new FileInputStream(pFile1);
					str2 = new FileInputStream(pFile2);

					byte[] content1 = new byte[4096];
					byte[] content2 = new byte[4096];
					
					int len;
					while ((len = str1.read(content1)) >= 0)
					{
						str2.read(content2);
						
				        for (int i = 0; i < len; i++)
				        {
				            if (content1[i] != content2[i])
				            {
				                return false;
				            }
				        }
					}
				}
				catch (IOException ex)
				{
					return false;
				}
				finally
				{
					CommonUtil.close(str1, str2);
				}
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * Copies the content from a stream into a file.
	 * 
	 * @param pIn the input stream
	 * @param pTarget the output file
	 * @return the number of written bytes
	 * @throws IOException if it's not possible to read from the <code>InputStream</code>
	 *                     or write to the <code>File</code>
	 */
	public static long copy(InputStream pIn, File pTarget) throws IOException
	{
		BufferedOutputStream bos = null;
		
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(pTarget));
			
			long length = 0;
			int iLen;
			
			byte[] byContent = new byte[4096];
			
			while ((iLen = pIn.read(byContent)) >= 0)
			{
				bos.write(byContent, 0, iLen);
				length += iLen;
			}

			bos.flush();
			
			return length;
		}
		finally
		{
		    CommonUtil.close(bos);
		}
	}
	
	/**
	 * Copies the content of an <code>InputStream</code> to the desired <code>OutputStream</code>.
	 * 
	 * @param pIn input stream with content
	 * @param pOut output stream
	 * @return the length of the InputStream.
	 * @throws IOException if it's not possible to read from the <code>InputStream</code>
	 *                     or write to the <code>OutputStream</code> 
	 */
	public static long copy(InputStream pIn, OutputStream pOut) throws IOException
	{
		return copy(pIn, false, pOut, false);
	}

	/**
	 * Copies the content of an <code>InputStream</code> to the desired <code>OutputStream</code>.
	 * 
	 * @param pIn input stream with content
	 * @param pCloseIn true, if the input stream
	 * @param pOut output stream
	 * @param pCloseOut true, if the output stream
	 * @return the length of the InputStream.
	 * @throws IOException if it's not possible to read from the <code>InputStream</code>
	 *                     or write to the <code>OutputStream</code> 
	 */
	public static long copy(InputStream pIn, boolean pCloseIn, OutputStream pOut, boolean pCloseOut) throws IOException
	{
		long length = 0;
		int iLen;
		
		byte[] byContent = new byte[4096];
		
		while ((iLen = pIn.read(byContent)) >= 0)
		{
			pOut.write(byContent, 0, iLen);
			length += iLen;
		}

		pOut.flush();

		if (pCloseIn)
		{
		    CommonUtil.close(pIn);
		}
		if (pCloseOut)
		{
		    CommonUtil.close(pOut);
		}
		return length;
	}

	/**
	 * Copy a file or directory to another file or directory.
	 * 
	 * @param pSource the source file
	 * @param pTarget the target file
	 * @param pPatterns the include/exclude patterns. This parameter is only used for directory copy.
	 * @throws IOException if the copy process failed
	 */
	public static void copy(File pSource, File pTarget, String... pPatterns) throws IOException
	{
		copy(pSource, pTarget, CopyMode.Overwrite, pPatterns);
	}
	
	/**
	 * Copies one file into another file. If the source file is a directory, then all files in
	 * the directory will be copied to the target. 
	 * 
	 * @param pSource source file
	 * @param pTarget target file
	 * @param pMode the copy mode
	 * @param pPatterns include/exclude file pattern(s)
	 * @throws IOException if it's not possible to copy the file or directory
	 */
	public static void copy(File pSource, File pTarget, CopyMode pMode, String... pPatterns) throws IOException
	{
		if (pSource.isFile())
		{
			copyFile(pSource, pTarget, pMode);
		}
		else if (pSource.isDirectory())
		{
			copyDirectory(pSource, pTarget, pMode, pPatterns);
		}
		else
		{
			throw new IOException("File or directory is required: '" + pSource.getAbsolutePath() + "'!");
		}
	}
	
	/**
	 * Copies one file into another file using <code>java.nio</code> Channels.
	 * 
	 * @param pSource source file
	 * @param pTarget target file
	 * @param pMode the copy mode. If the target file should be backuped, then a timestamp 
	 *              will be added to the filename.
	 * @throws IOException if it's not possible to copy the file
	 */
	private static void copyFile(File pSource, File pTarget, CopyMode pMode) throws IOException
	{
		if (pTarget.exists())
		{
			if (pMode == CopyMode.Keep)
			{
				return;
			}
			else if (pMode == CopyMode.Backup)
			{
				String sDate = DateUtil.format(new Date(), "dd_MM_yyyy_HH_mm_ss");
				
				String sExt = getExtension(pTarget.getName());
				
				File fiNew = new File(pTarget.getCanonicalFile().getParent(), removeExtension(pTarget.getName()) + "_" + sDate + (sExt != null ? "." + sExt : ""));
				
				if (!pTarget.renameTo(fiNew))
				{
					throw new IOException("Can't rename file '" + pTarget.getAbsolutePath() + "' to '" + fiNew.getAbsolutePath() + "'!");
				}
			}
		}
		
		FileInputStream fisSource = null;
		FileOutputStream fosTarget = null;
		
		FileChannel fcSource = null;	
		FileChannel fcTarget = null;
		
		try
		{
			if (!pTarget.exists())
			{
				File fiParent = pTarget.getCanonicalFile().getParentFile();
				
				if (fiParent != null)
				{
					fiParent.mkdirs();
				}
			}
			else if (pTarget.isDirectory())
			{
				pTarget = new File(pTarget, pSource.getName());
			}
			
	    	fisSource = new FileInputStream(pSource);
	    	fosTarget = new FileOutputStream(pTarget);
			
			fcSource = fisSource.getChannel();	
	    	fcTarget = fosTarget.getChannel();
	    	
	    	long length = fcSource.size();
			
	    	fcSource.transferTo(0, length, fcTarget);
		}
		finally
		{
		    CommonUtil.close(fcSource, fisSource, fcTarget, fosTarget);
		}
	}
	
	/**
	 * Copie the files and sub directories from one directory to another directory.
	 * 
	 * @param pSource the source directory
	 * @param pTarget the target directory
	 * @param pMode the copy mode
	 * @param pPatterns the include/exclude patterns
	 * @throws IOException if the copy process failed
	 */
	private static void copyDirectory(File pSource, File pTarget, CopyMode pMode, String... pPatterns) throws IOException
	{
		FileSearch fsearch = new FileSearch();
		fsearch.search(pSource, true, pPatterns);
		
		File fiOldDir;
		File fiNewDir;

        int iLen = pSource.getAbsolutePath().length();
		
		for (Entry<String, List<String>> entry : fsearch.getFilesPerDirectory().entrySet())
		{
			fiOldDir = new File(entry.getKey());
			fiNewDir = new File(pTarget, entry.getKey().substring(iLen));

			if (!fiNewDir.exists() && !fiNewDir.mkdirs())
			{
				throw new IOException("Can't create directory '" + fiNewDir.getAbsolutePath() + "'!");
			}
			
			for (String file : entry.getValue())
			{
				copyFile(new File(fiOldDir, file), new File(fiNewDir, file), pMode);
			}
		}
		
		//create empty directories

		if (pSource.exists() 
			&& fsearch.getFoundFiles().isEmpty() 
			&& fsearch.getFoundDirectories().isEmpty())
		{
			pTarget.mkdirs();
		}
		
		for (String sDir : fsearch.getFoundDirectories())
		{
			fiNewDir = new File(pTarget, sDir.substring(iLen));
			
			if (!fiNewDir.exists())
			{
				fiNewDir.mkdirs();
			}
		}
	}

	/**
	 * Moves the given source file to another location. If the target file exists, it will be overwritten.
	 * 
	 * @param pSource the source file
	 * @param pTarget the target location
	 * @throws IOException if an error occurs during moving
	 */
	public static void move(File pSource, File pTarget) throws IOException
	{
		move(pSource, pTarget, CopyMode.Overwrite);
	}
	
	/**
	 * Moves the given source file to another location. The given copy mode defines the operation if the 
	 * target file already exists.
	 * 
	 * @param pSource the source file
	 * @param pTarget the target location
	 * @param pMode the copy mode
	 * @throws IOException if an error occurs during moving
	 */
	public static void move(File pSource, File pTarget, CopyMode pMode) throws IOException
	{
		if (pSource.equals(pTarget))
		{
			return;
		}
		
		if (pSource.isFile())
		{
			moveFile(pSource, pTarget, pMode);
		}
		else if (pSource.isDirectory())
		{
			moveDirectory(pSource, pTarget, pMode);
		}
		else
		{
			throw new IOException("File or directory is required: '" + pSource.getAbsolutePath() + "'!");
		}
	}
	
	/**
	 * Moves the given source file to another location.
	 * 
	 * @param pSource the source file
	 * @param pTarget the target location
	 * @param pMode the copy mode
	 * @throws IOException if the move operation fails
	 */
	private static void moveFile(File pSource, File pTarget, CopyMode pMode) throws IOException
	{
		if (pTarget.isDirectory())
		{
			pTarget = new File(pTarget, pSource.getName());
		}

		if (pTarget.exists())
		{
			//It is not possible to move a file to a directory
			if (pTarget.isDirectory())
			{
				throw new IOException("Can't move '" + pSource.getAbsolutePath() + "' to directory '" + pTarget.getAbsolutePath() + "'!");
			}

			if (pMode == null || pMode == CopyMode.Overwrite)
			{
				if (!pTarget.delete())
				{
					throw new IOException("Can't overwrite '" + pTarget.getAbsolutePath() + "'!");
				}
			}
			else if (pMode == CopyMode.Keep)
			{
				return;
			}
			else
			{
				String sDate = DateUtil.format(new Date(), "dd_MM_yyyy_HH_mm_ss");
				
				String sExt = getExtension(pTarget.getName());
				
				File fiNew = new File(pTarget.getCanonicalFile().getParent(), removeExtension(pTarget.getName()) + "_" + sDate + (sExt != null ? "." + sExt : ""));
				
				if (!pTarget.renameTo(fiNew))
				{
					throw new IOException("Can't rename file '" + pTarget.getAbsolutePath() + "' to '" + fiNew.getAbsolutePath() + "'!");
				}
			}
		}
		else
		{
			File fiParent = pTarget.getCanonicalFile().getParentFile();
			
			if (fiParent != null)
			{
				fiParent.mkdirs();
			}
		}
		
		if (!pSource.renameTo(pTarget))
		{
			throw new IOException("Can't move '" + pSource.getAbsolutePath() + "' to '" + pTarget.getAbsolutePath() + "'!");
		}
	}
	
	/**
	 * Moves the given source director to another location.
	 * 
	 * @param pSource the source directory
	 * @param pTarget the target directory
	 * @param pMode the copy mode
	 * @throws IOException if the move operation fails
	 */
	private static void moveDirectory(File pSource, File pTarget, CopyMode pMode) throws IOException
	{
		if (pTarget.exists())
		{
			if (pTarget.isFile())
			{
				throw new IOException("Can't move '" + pSource.getAbsolutePath() + "' to file '" + pTarget.getAbsolutePath() + "'!");
			}
			
			if (pMode == null || pMode == CopyMode.Overwrite || pMode == CopyMode.Keep)
			{
				//don't delete existing directory and copy new files and overwrite existing files
				FileUtil.copyDirectory(pSource, pTarget, pMode);
				
				FileUtil.delete(pSource);
				
				return;
			}	
			else
			{
				String sDate = DateUtil.format(new Date(), "dd_MM_yyyy_HH_mm_ss");
				
				File fiNew = new File(pTarget.getAbsolutePath() + "_" + sDate);
				
				if (!pTarget.renameTo(fiNew))
				{
					throw new IOException("Can't rename file '" + pTarget.getAbsolutePath() + "' to '" + fiNew.getAbsolutePath() + "'!");
				}
			}
		}
		else
		{
			pTarget.getCanonicalFile().getParentFile().mkdirs();
		}
		
		if (!pSource.renameTo(pTarget))
		{
			//try copy mode!
			
			if (!pTarget.mkdirs())
			{
				throw new IOException("Can't move '" + pSource.getAbsolutePath() + "' to '" + pTarget.getAbsolutePath() + "' because create target directory failed!");
			}
			
			copyDirectory(pSource, pTarget, CopyMode.Overwrite);
			
			if (pSource.exists() && !pSource.delete())
			{
				pSource.deleteOnExit();
			}
		}
	}

	/**
	 * Reads the content of an <code>InputStream</code> into a byte array and closes the stream after reading.
	 * <p>
	 * @param pStream the input stream
	 * @return the content of the stream or null if an error occurs
	 */
	public static byte[] getContent(InputStream pStream)
	{
		return getContent(pStream, true);
	}
	
    /**
     * Reads the content of an <code>InputStream</code> into a byte array.
     * <p>
     * @param pStream the input stream
     * @param pAutoClose whether the input stream should be closed after reading
     * @return the content of the stream or null if an error occurs
     */
    public static byte[] getContent(InputStream pStream, boolean pAutoClose)
    {
        if (pStream != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
            byte[] byTmp = new byte[8192];
            
            try
            {
                int iLen = pStream.read(byTmp);
                
                while (iLen >= 0)
                {
                    baos.write(byTmp, 0, iLen);
                    
                    iLen = pStream.read(byTmp);
                }
        
                baos.close();
                
                return baos.toByteArray();
            }
            catch (IOException ioe)
            {
                return null;
            }
            finally
            {
                if (pAutoClose)
                {
                    CommonUtil.close(pStream);
                }
            }
        }
    
        return null;
    }
    
    /**
     * Reads the content of an <code>InputStream</code> into a byte array and the exact size is already known.
     * The stream will be closed after reading.
     * <p>
     * @param pStream the input stream
     * @param pSize the input stream
     * @return the content of the stream or null if an error occurs
     */
    public static byte[] getContent(InputStream pStream, int pSize)
    {
        return getContent(pStream, pSize, true);
    }
    
	/**
	 * Reads the content of an <code>InputStream</code> into a byte array and the exact size is already known.
	 * <p>
	 * @param pStream the input stream
     * @param pSize the input stream
	 * @param pAutoClose whether the input stream should be closed after reading
	 * @return the content of the stream or null if an error occurs
	 */
	public static byte[] getContent(InputStream pStream, int pSize, boolean pAutoClose)
	{
	    if (pStream != null)
	    {
		    try
	        {
		        byte[] byResult = new byte[pSize];
		        
		        int pos = 0;
		        int len = pStream.read(byResult, pos, pSize);
                int sizeLeft = pSize - len;
		        
		        while (len >= 0 && sizeLeft > 0)
		        {
		            pos += len;
		            
		            len = pStream.read(byResult, pos, sizeLeft);
                    sizeLeft -= len;
		        }
		        
		        return byResult;
	        }
	        catch (IOException ioe)
	        {
	        	return null;
	        }
	        finally
	        {
	        	if (pAutoClose)
	        	{
	        	    CommonUtil.close(pStream);
	        	}
	        }
	    }
	
	    return null;
	}
	
	/**
	 * Gets the content from a file.
	 * 
	 * @param pFile the file
	 * @return the content of the file or null if an error occurs 
	 * @throws IOException if the file does not exist
	 */
	public static byte[] getContent(File pFile) throws IOException
	{
		if (pFile == null)
		{
			throw new IOException("File not found: null");
		}
		long size = pFile.length();
		if (size > Integer.MAX_VALUE)
		{
            throw new IOException("File is too large (>" + Integer.MAX_VALUE + " bytes)!");
		}
		
		return getContent(new FileInputStream(pFile), (int)size);
	}
	
	/**
	 * Gets the content from a file.
	 * 
	 * @param pFileName the path to the file
	 * @return the content of the file or null if an error occurs 
	 * @throws IOException if the file does not exist
	 */
	public static byte[] getContent(String pFileName) throws IOException
	{
		if (pFileName == null)
		{
			throw new IOException("File not found: null");
		}

		return getContent(new File(pFileName));
	}

	/**
	 * Reads the content of an <code>InputStreamReader</code> into a byte array and closes the stream after reading.
	 * <p>
	 * @param pReader the input stream reader
	 * @return the content of the reader or null if an error occurs
	 */
	public static byte[] getContent(InputStreamReader pReader)
	{
		return getContent(pReader, true);
	}
	
	/**
	 * Reads the content of an <code>InputStreamReader</code> into a byte array.
	 * <p>
	 * @param pReader the input stream reader
	 * @param pAutoClose whether the input stream should be closed after reading
	 * @return the content of the reader or null if an error occurs
	 */
	public static byte[] getContent(InputStreamReader pReader, boolean pAutoClose)
	{
	    if (pReader != null)
	    {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter osw;
			
			try
			{
				osw = new OutputStreamWriter(baos, pReader.getEncoding());
			}
			catch (UnsupportedEncodingException use)
			{
				use.printStackTrace();
				
				return null;
			}
			
		    int iLen;

	        char[] ch = new char[8192];

	        
	        try
	        {
		        while ((iLen = pReader.read(ch)) >= 0)
		        {
		        	osw.write(ch, 0, iLen);
		        }
	        	
		        osw.flush();
	        	osw.close();

	        	baos.close();
	        	
		        return baos.toByteArray();
	        }
	        catch (IOException ioe)
	        {
	        	return null;
	        }
	        finally
	        {
		        if (pAutoClose)
		        {
		            CommonUtil.close(pReader);
		        }
	        }
	    }
	
	    return null;
	}

	/**
	 * Removes the file extension from a file name.
	 * 
	 * @param pPath the filename
	 * @return the filename without extension
	 */
	public static String removeExtension(String pPath)
	{
		if (pPath != null)
		{
			int iPos = pPath.lastIndexOf('.');
			
			if (iPos > 0)
			{
				return pPath.substring(0, iPos);
			}
		}
		
		return pPath;
	}

	/**
	 * Gets the extension of an absolute file path.
	 * 
	 * @param pPath the absolute path of a file
	 * @return the extension (text behind the last '.') or <code>null</code> if the path is <code>null</code>
	 *         or has no extension
	 */
	public static String getExtension(String pPath)
	{
		if (pPath == null)
		{
			return null;
		}
		
		int iPos = pPath.lastIndexOf('.');
		
		if (iPos >= 0)
		{
			return pPath.substring(iPos + 1);
		}
		
		return null;
	}

    /**
     * Gets a file that does not exist.
     * If the given file already exists, the file name built in the way "nameWithoutExtension[number].extension".
     * 
     * @param pFile the desired file name
     * @return a file that does not exist.
     */
    public static File getNotExistingFile(File pFile)
    {
        return getNotExistingFile(pFile, "[", "]");
    }	
	
	/**
	 * Gets a file that does not exist.
	 * If the given file already exists, the file name built in the way "nameWithoutExtension&lt;bracketstart&gt;number&lt;bracketend&gt;.extension".
	 * 
	 * @param pFile the desired file name
	 * @param pOpenBracket the start bracket
	 * @param pCloseBracket the close bracket
	 * @return a file that does not exist.
	 */
	public static File getNotExistingFile(File pFile, String pOpenBracket, String pCloseBracket)
	{
		File file = pFile;
		
		try
		{
			file = pFile.getCanonicalFile();
		}
		catch (IOException ioe)
		{
			//try to use given file
			
			LoggerFactory.getInstance(FileUtil.class).error(ioe);
		}

		String extension = getExtension(file.getName());
		String nameWithoutExtension = removeExtension(file.getName());
		
		int i = 1;
		
		while (file.exists())
		{
			file = new File(file.getParent(), nameWithoutExtension + pOpenBracket + i + pCloseBracket + "." + extension);
			
			i++;
		}
		
		return file;
	}

	/**
	 * Gets the name of a file from an absolute path.
	 * 
	 * @param pAbsolutePath the absolute path for a file
	 * @return the name of the file without path
	 */
	public static String getName(String pAbsolutePath)
	{
		if (pAbsolutePath == null)
		{
			return null;
		}
		
		int iPos = pAbsolutePath.lastIndexOf('/');
		
		if (iPos < 0)
		{
			iPos = pAbsolutePath.lastIndexOf('\\');
		}
		
		if (iPos >= 0)
		{
			return pAbsolutePath.substring(iPos + 1);
		}
		
		return pAbsolutePath;
	}
	
	/**
	 * Gets the directory for a path. It searchs the last path separator and cuts off all characters
	 * behind, e.g. /home/a/b/c returns /home/a/b
	 * 
	 * @param pAbsolutePath a path
	 * @return the parent directory path or <code>null</code> if no path separator was found
	 */
	public static String getDirectory(String pAbsolutePath)
	{
		if (pAbsolutePath == null)
		{
			return null;
		}
		
		int iPos = pAbsolutePath.lastIndexOf('/');
		
		if (iPos < 0)
		{
			iPos = pAbsolutePath.lastIndexOf('\\');
		}

		if (iPos > 0)
		{
			return pAbsolutePath.substring(0, iPos);
		}
		
		return null;
	}
	
	/**
	 * Formats the given path as directory. That means a trailing slash will be added if the path
	 * is not already a directory.
	 *  
	 * @param pPath the path
	 * @return the directory path
	 */
	public static String formatAsDirectory(String pPath)
	{
		if (pPath == null)
		{
			return null;
		}
		
		char ch = pPath.charAt(pPath.length() - 1);
		
		if (ch == '\\' || ch == '/')
		{
			return pPath;
		}
		else
		{
			return pPath + "/";
		}
	}

    /**
     * Delete all files and directories in the given directory. This method will stop after first
     * error.
     * 
     * @param pSource the file or directory
     * @param pPattern the patterns
     * @return true if the directory was deleted, <code>false</code> otherwise
     */
    public static boolean deleteSub(File pSource, String... pPattern)
    {
        return deleteSub(pSource, true, pPattern);
    }	
	
    /**
     * Delete all files and directories in the given directory.
     * 
     * @param pSource the file or directory
     * @param pStopAfterError <code>true</code> to stop after delete error, <code>false</code> to 
     *                        continue with next file
     * @param pPattern the patterns
     * @return true if the directory was deleted, <code>false</code> otherwise
     */
    public static boolean deleteSub(File pSource, boolean pStopAfterError, String... pPattern)
    {
        boolean bSuccess = true;
        
        FileSearch fsearch = new FileSearch();
        
        fsearch.search(pSource, true, pPattern);

        //delete files
        for (String file : fsearch.getFoundFiles())
        {
            if (!new File(file).delete())
            {
                if (pStopAfterError)
                {
                    return false;
                }
                else
                {
                    bSuccess = false;
                }
            }
        }

        //delete directories (from bottom to top)
        List<String> liDir = fsearch.getFoundDirectories();
        
        for (int i = liDir.size() - 1; i >= 0; i--)
        {
            if (!new File(liDir.get(i)).delete())
        	{
                if (pStopAfterError)
                {
                    return false;
                }
                else
                {
                    bSuccess = false;
                }
        	}
        }
        
        return bSuccess;
    }	

    /**
     * Deletes a file or directory. This method will stop after first
     * error.
     * 
     * @param pSource the file or directory
     * @param pPattern the patterns
     * @return true if the directory was deleted, <code>false</code> otherwise
     */
    public static boolean delete(File pSource, String... pPattern)
    {
        return delete(pSource, true, pPattern);
    }    
    
    /**
     * Deletes a file or directory.
     * 
     * @param pSource the file or directory
     * @param pStopAfterError <code>true</code> to stop after delete error, <code>false</code> to 
     *                        continue with next file
     * @param pPattern the patterns
     * @return true if the directory was deleted, <code>false</code> otherwise
     */
    public static boolean delete(File pSource, boolean pStopAfterError, String... pPattern)
    {
    	if (deleteSub(pSource, pStopAfterError, pPattern))
		{
	        if ((pPattern == null || pPattern.length == 0) 
	            && pSource.exists() && !pSource.delete())
	        {
	        	return false;
	        }

	        return true;
		}
    	else
    	{
    		return false;
    	}
    }	
    
    /**
     * Delete empty directories, recursive.
     * 
     * @param pDirectory the start directories
     * @param pPattern the include/exclude pattern
     * @return <code>true</code> if all empty directories were deleted, <code>false</code>
     *         otherwise
     */
    public static boolean deleteEmpty(File pDirectory, String... pPattern)
    {
        FileSearch fsearch = new FileSearch();
    	fsearch.search(pDirectory, true, pPattern);

        //delete directories (from bottom to top)
        List<String> liDir = fsearch.getFoundDirectories();

        File fiDir;
        
        String[] sPaths;
        
        for (int i = liDir.size() - 1; i >= 0; i--)
        {
        	fiDir = new File(liDir.get(i));
        	
        	sPaths = fiDir.list();
        	
        	if (sPaths != null && sPaths.length == 0 && !fiDir.delete())
        	{
        		return false;
        	}
        }
        
        return true;	
    }
    
    /**
     * Deletes directories if they are empty.
     * 
     * @param pDirectory a list of directories
     * @return <code>true</code> if all empty directories were deleted, <code>false</code> if an empty
     *         directory can not be deleted
     */
    public static boolean deleteIfEmpty(File... pDirectory)
    {
        String[] sPaths;

        for (File dir : pDirectory)
        {
        	sPaths = dir.list();
        	
        	if (sPaths != null && sPaths.length == 0 && !dir.delete())
        	{
        		return false;
        	}
        }
        
        return true;
    }

    /**
     * Creates a zip package for given directories. 
     * <p>
     * @param pOut the output stream
     * @param pMode {@link ZipOutputStream#STORED} or {@link ZipOutputStream#DEFLATED}
     * @param pSearchPath all search path
     * @param pExtension all allowed extensions
     * @throws IOException if a problem occurs during zip creation
     */
    public static void zip(OutputStream pOut, int pMode, String[] pSearchPath, String... pExtension) throws IOException
    {
    	if (pSearchPath == null || pSearchPath.length == 0)
    	{
    		return;
    	}
    	
    	//Optimize search path
    	ArrayUtil<String> auSearchPath = new ArrayUtil<String>();

    	for (String sSearchPath : pSearchPath)
    	{
    		sSearchPath = sSearchPath.replace("\\", "/");
    		
    		if (sSearchPath.endsWith("/"))
    		{
    			sSearchPath = sSearchPath.substring(0, sSearchPath.length() - 1);
    		}

    		auSearchPath.add(sSearchPath);
    	}

		Collections.sort(auSearchPath);

		//don't search more than once in the same hierarchy
		
		String sPath;
		
		for (int i = auSearchPath.size() - 1; i >= 0; i--)
		{
			sPath = auSearchPath.get(i);
			
			for (int j = i - 1; j >= 0; j--)
			{
				if (sPath.startsWith(auSearchPath.get(j)))
				{
					auSearchPath.remove(i);
					j = -1;
				}
			}
		}
    	
        FileInputStream in = null;

        ZipOutputStream zos = new ZipOutputStream(pOut);
        BufferedOutputStream bos = new BufferedOutputStream(zos);

        CRC32 crc = null;

        ZipEntry ze;

        FileSearch fsearch = new FileSearch();

        File fCurrent;

        byte[] by = new byte[4096];

        int iLength;
        int iCutOff;


        zos.setMethod(pMode);
        
        if (pMode == ZipOutputStream.DEFLATED)
        {
            zos.setLevel(9);
        }

        try
        {
        	//Search all files
        	for (String sSearch : auSearchPath)
        	{
        		if (!fsearch.getFoundDirectories().contains(sSearch))
        		{
        			fsearch.search(sSearch, true, pExtension);
        		}
        	}
        	
        	//be sure that all directories are included (e.g. root dir)
        	List<String> liFoundDirectories = fsearch.getFoundDirectories();
        	
        	for (Entry<String, List<String>> entry : fsearch.getFilesPerDirectory().entrySet())
			{
        		if (!liFoundDirectories.contains(entry.getKey()))
        		{
        			liFoundDirectories.add(entry.getKey());
        		}
			}
        	
        	Collections.sort(liFoundDirectories);
        	
        	ArrayUtil<String> auDirectories = new ArrayUtil<String>();
        	List<String> liFileList;
        	
        	String sDirName;
        	
        	StringBuilder sbDirName = new StringBuilder();
        	
	        for (String sDirectory : liFoundDirectories)
	        {
		        sDirName = sDirectory.replace("\\", "/");

		        iCutOff = -1;
	        	
	        	for (int i = 0, anz = auSearchPath.size(); i < anz && iCutOff == -1; i++)
	        	{
	        		if (sDirName.startsWith(auSearchPath.get(i)))
	        		{
	        			iCutOff = auSearchPath.get(i).length() + 1;	        			
	        		}
	        	}
		        
		        liFileList = fsearch.getFilesPerDirectory().get(sDirectory);
	        	
		        if (liFileList != null)
		        {
			        //create directory structure
			        
					sbDirName.setLength(0);
					
					if (iCutOff < sDirName.length())
					{
						for (String sPart : StringUtil.separateList(sDirName.substring(iCutOff), "/", false))
						{
							sbDirName.append(sPart);
							sbDirName.append("/");
							
							sDirName = sbDirName.toString();
							
							if (auDirectories.indexOf(sDirName) < 0)
							{
								ze = new ZipEntry(sDirName);

					            if (pMode == ZipOutputStream.STORED)
					            {
					            	ze.setSize(0);
					            	ze.setCrc(0);
					            }
								
								zos.putNextEntry(ze);
								
								auDirectories.add(sDirName);
							}
						}		        
					}
					
		        	for (String sFile : liFileList)
		        	{
			            fCurrent = new File(sDirectory, sFile);
			
			            if (pMode == ZipOutputStream.STORED)
			            {
			                crc = new CRC32();
			                
			                try
			                {
				                in = new FileInputStream(fCurrent);
				
				                while ((iLength = in.read(by)) >= 0)
				                {
				                    crc.update(by, 0, iLength);
				                }
			                }
				            finally
				            {
				                CommonUtil.close(in);
				            }
			            }
			
			            //configure zip entry
			            ze = new ZipEntry(fCurrent.getAbsolutePath().substring(iCutOff).replace('\\', '/'));
			
			            if (pMode == ZipOutputStream.STORED)
			            {
			                ze.setSize(fCurrent.length());
			                ze.setTime(fCurrent.lastModified());
			                ze.setCrc(crc.getValue());
			            }
			
			            zos.putNextEntry(ze);
			
			            try
			            {
				            in = new FileInputStream(fCurrent);
				
				            while ((iLength = in.read(by)) >= 0)
				            {
				                bos.write(by, 0, iLength);
				            }
			            }
			            finally
			            {
			                CommonUtil.close(in);
			            }
			
			            bos.flush();
			            zos.closeEntry();
			        }
		        }
        	}
        }
        finally
        {
            CommonUtil.close(bos);
        }

        //cleanup
        by  = null;
        zos = null;
        bos = null;
        in  = null;
        fsearch = null;
    }

    /**
     * Creates a zip package for given files. 
     * <p>
     * @param pOut the output stream
     * @param pMode {@link ZipOutputStream#STORED} or {@link ZipOutputStream#DEFLATED}
     * @param pFiles all search path
     * @throws IOException if a problem occurs during zip creation
     */
    public static void zip(OutputStream pOut, int pMode, File... pFiles) throws IOException
    {
        if (pFiles == null || pFiles.length == 0)
        {
            return;
        }
        
        FileInputStream in = null;

        ZipOutputStream zos = new ZipOutputStream(pOut);
        BufferedOutputStream bos = new BufferedOutputStream(zos);

        CRC32 crc = null;

        ZipEntry ze;

        byte[] by = new byte[4096];

        int iLength;

        zos.setMethod(pMode);
        
        if (pMode == ZipOutputStream.DEFLATED)
        {
            zos.setLevel(9);
        }

        try
        {
            for (int i = 0; i < pFiles.length; i++)
            {
                if (pFiles[i].isFile())
                {
                    if (pMode == ZipOutputStream.STORED)
                    {
                        crc = new CRC32();
                        
                        try
                        {
                            in = new FileInputStream(pFiles[i]);
            
                            while ((iLength = in.read(by)) >= 0)
                            {
                                crc.update(by, 0, iLength);
                            }
                        }
                        finally
                        {
                            CommonUtil.close(in);
                        }
                    }
        
                    //configure zip entry
                    ze = new ZipEntry(pFiles[i].getName());
        
                    if (pMode == ZipOutputStream.STORED)
                    {
                        ze.setSize(pFiles[i].length());
                        ze.setTime(pFiles[i].lastModified());
                        ze.setCrc(crc.getValue());
                    }
        
                    zos.putNextEntry(ze);
        
                    try
                    {
                        in = new FileInputStream(pFiles[i]);
            
                        while ((iLength = in.read(by)) >= 0)
                        {
                            bos.write(by, 0, iLength);
                        }
                    }
                    finally
                    {
                        CommonUtil.close(in);
                    }
        
                    bos.flush();
                    zos.closeEntry();
                }
            }
        }
        finally
        {
            CommonUtil.close(bos);
        }

        //cleanup
        by  = null;
        zos = null;
        bos = null;
        in  = null;
    }
    
    /**
     * Unzips the given file.
     * 
     * @param pFile the path to the zip archive
     * @param pTarget the target directory
     * @param pPattern the include/exclude pattern
     * @throws IOException if unzipping fails
     */
    public static void unzip(String pFile, String pTarget, String... pPattern) throws IOException
    {
    	unzip(new File(pFile), pTarget, pPattern);
    }
    
    /**
     * Unzips the given file.
     * 
     * @param pFile the zip archive
     * @param pTarget the target directory
     * @param pPattern the include/exclude pattern
     * @throws IOException if unzipping fails
     */
    public static void unzip(File pFile, String pTarget, String... pPattern) throws IOException
    {
    	FileInputStream fis = new FileInputStream(pFile);
    	
    	try
    	{
    		unzip(fis, pTarget, pPattern);
    	}
    	finally
    	{
    	    CommonUtil.close(fis);
    	}
    }
    
	/**
	 * Extracts the content of a zip archive.
	 * 
	 * @param pFile the zip archive
	 * @param pTarget where the content should go
	 * @param pPattern the include/exclude pattern
	 * @throws IOException if an error occurs during extraction
	 * @see #unzip(InputStream, File, String...)
	 */
    public static void unzip(File pFile, File pTarget, String... pPattern) throws Exception
    {
    	FileInputStream fis = new FileInputStream(pFile);
    	
    	try
    	{
    		unzip(fis, pTarget, pPattern);
    	}
    	finally
    	{
    	    CommonUtil.close(fis);
    	}
    }
    
	/**
	 * Extracts the content of a zip archive.
	 * 
	 * @param pArchive the zip archive
	 * @param pTarget where the content should go
	 * @param pPattern the include/exclude pattern
	 * @throws IOException if an error occurs during extraction
	 * @see #unzip(InputStream, File, String...)
	 */
    public static void unzip(InputStream pArchive, String pTarget, String... pPattern) throws IOException
    {
    	unzip(pArchive, new File(pTarget), pPattern);
    }
    
	/**
	 * Extracts the content of a zip archive.
	 * 
	 * @param pArchive the zip archive
	 * @param pTarget where the content should go
	 * @param pPattern the include/exclude pattern
	 * @throws IOException if an error occurs during extraction
	 */
	public static void unzip(InputStream pArchive, File pTarget, String... pPattern) throws IOException
	{
		File fiCurrent;
		
		int iLen;
		
		byte[] byData = new byte[4096];
		
		
		fiCurrent = pTarget;
		
		if (fiCurrent.exists() || fiCurrent.mkdirs())
		{
			BufferedOutputStream bosCurrent = null;

			ZipInputStream zis = null;
			
			try
			{
			    if (pArchive instanceof ZipInputStream)
			    {
			        zis = (ZipInputStream)pArchive;
			    }
			    else
			    {
			        zis = new ZipInputStream(pArchive);
			    }
	
				ZipEntry zeCurrent;
				
				
	        	ArrayUtil<String> auInclude = new ArrayUtil<String>();
	        	ArrayUtil<String> auExclude = new ArrayUtil<String>();
	        	
	            String[] saInclude = null;
	            String[] saExclude = null;
	        	
	        	if (pPattern == null || pPattern.length == 0)
	        	{
	        		saInclude = new String[] {"*"};
	        		saExclude = null;
	        	}
	        	else
	        	{
		        	for (String sNode : pPattern)
		        	{
		        		if (sNode == null || sNode.length() == 0)
		        		{
		        			auInclude.add("*");
		        		}
		        		else if (sNode.charAt(0) == '!')
		        		{
		        			auExclude.add(sNode.substring(1).replace('\\', '/'));
		        		}
		        		else
		        		{
		        			auInclude.add(sNode.replace('\\', '/'));
		        		}
		        	}
		        	
		        	if (auInclude.size() == 0)
		        	{
		            	//Allow all
		        		saInclude = new String[] {"*"};
		        	}
		        	else
		        	{
			        	saInclude = new String[auInclude.size()];
			        	auInclude.toArray(saInclude);
		        	}
		        	
		        	if (auExclude.size() == 0)
		        	{
		        		saExclude = null;
		        	}
		        	else
		        	{
			        	saExclude = new String[auExclude.size()];
			        	auExclude.toArray(saExclude);
		        	}
	        	}
				
				String sName;
				String sEntryName;
				
				boolean bAllowed;
				
				while ((zeCurrent = zis.getNextEntry()) != null)
	    		{
					sEntryName = zeCurrent.getName();

		        	bAllowed = true;

		        	sName = sEntryName.replace('\\', '/');
		        	
		        	//mark a directory
		        	if (zeCurrent.isDirectory())
		        	{
		        		sName = sName + "/";
		        	}
		        	
		        	if (saExclude != null)
		            {
		        		for (int i = 0; i < saExclude.length; i++)
		            	{
		            		if (StringUtil.like(sName, saExclude[i]))
		            		{
		            			bAllowed = false;
		            		}
		            	}
		            }
		        
		            if (bAllowed && saInclude != null)
		            {
		            	for (int i = 0; i < saInclude.length; i++)
		            	{
		            		if (StringUtil.like(sName, saInclude[i]))
		            		{
		            			bAllowed = true;
		            		}
		            	}
		            }
					
		            if (bAllowed)
		            {
						fiCurrent = getValidTarget(pTarget, sEntryName);
						
		    			//create directories if needed
		    			if (zeCurrent.isDirectory())
		    			{
		    				fiCurrent.mkdirs();
		    			}
		    			else
		    			{
		    				//it is possible that we don't get the directory
		    				String sPath = FileUtil.getDirectory(fiCurrent.getAbsolutePath());
		    				
		    				if (sPath != null)
		    				{
		    					new File(sPath).mkdirs();
		    				}
		    				
		    				try
		    				{
			        			//extract
			        			bosCurrent = new BufferedOutputStream(new FileOutputStream(fiCurrent));
			        			
			        			while ((iLen = zis.read(byData)) != -1)
			        			{
			        				bosCurrent.write(byData, 0, iLen);
			        			}
		    				}
		    				finally
		    				{
		    				    bosCurrent = CommonUtil.close(bosCurrent);
		    				}
		    			}
		            }
	    		}
			}
			finally
			{
			    CommonUtil.close(zis);
			}
		}
		else
		{
			throw new IOException("Can not create target directory: " + pTarget.getAbsolutePath());
		}
	}
	
	/**
	 * Gets a valid zip target output file.
	 * 
	 * @param pBase the base directory
	 * @param pName the target name
	 * @return the target file
	 * @throws IOException if target file is outside the base directory or file name detection fails
	 */
	private static File getValidTarget(File pBase, String pName) throws IOException
	{
		String canBase = pBase.getCanonicalPath();

		File fiTarget = new File(pBase, pName);
		String canTarget = fiTarget.getCanonicalPath();
		   
		if (canTarget.startsWith(canBase)) 
		{
			return fiTarget;
		} 
		else 
		{
			throw new IllegalStateException("File is outside extraction target directory.");
		}	
	}

	/**
	 * Gets a list of all entries from a zip archive.
	 * 
	 * @param pArchive the stream for the zip archive
	 * @return the list of entries
	 * @throws IOException if the archive does not contain zip content or a read error occurs
	 */
	@SuppressWarnings("resource")
	public static List<String> listZipEntries(InputStream pArchive) throws IOException
    {
		ZipInputStream zis = null;
		
		try
		{
		    if (pArchive instanceof ZipInputStream)
		    {
		        zis = (ZipInputStream)pArchive;
		    }
		    else
		    {
		        zis = new ZipInputStream(pArchive);
		    }

			List<String> liEntries = new ArrayUtil<String>();

			ZipEntry zeCurrent;
			
			while ((zeCurrent = zis.getNextEntry()) != null)
			{
				liEntries.add(zeCurrent.getName());
			}

            return liEntries;
		}
		finally
		{
		    CommonUtil.close(zis);
		}
    }
	
    /**
     * Saves the given content into a file.
     * 
     * @param pFile the output file
     * @param byContent the data
     * @throws IOException if an error occurs during output
     */
    public static void save(File pFile, byte[] byContent) throws IOException
    {
    	ByteArrayInputStream bais = new ByteArrayInputStream(byContent); 
    	
    	try
    	{
    		save(pFile, bais);
    	}
    	finally
    	{
    	    CommonUtil.close(bais);
    	}
    }
    
    /**
     * Saves the given stream into a file.
     * 
     * @param pFile the output file
     * @param pInput the input stream
     * @throws IOException if an error occurs during output
     */
    public static void save(File pFile, InputStream pInput) throws IOException
    {
    	BufferedOutputStream bos = null;
    	
    	try
    	{
    		File fiParent = pFile.getCanonicalFile().getParentFile();
    		
    		if (fiParent != null && !fiParent.exists())
    		{
    			fiParent.mkdirs();
    		}
    		
    		bos = new BufferedOutputStream(new FileOutputStream(pFile));
    		
    		int iLen;
    		
    		byte[] byData = new byte[4096];
    		
    		while ((iLen = pInput.read(byData)) >= 0)
    		{
	    		bos.write(byData, 0, iLen);
    		}
    		
    		bos.flush();
    		bos.close();
    		
    		bos = null;
    	}
    	finally
    	{
    	    CommonUtil.close(bos);
    	}
    }

    /**
     * Replaces key/value mappings read from the given input file and writes the result to the given
     * output file. This method requires ASCII files. If the given input and output file are the same,
     * then this methods caches the data in a temporary file or in memory before writing the result to
     * the output file. An input file with a filesize smaller than 1M is cached in memory.
     * 
     * @param pInput the input file
     * @param pOutput the output file
     * @param pMapping the key/value mapping e.g. ${PARAM1} = JVx
     * @param pEncoding the encoding for the input/output files
     * @throws IOException if a read or write error occurs
     */
    public static void replace(File pInput, File pOutput, Hashtable<String, String> pMapping, String pEncoding) throws IOException
    {
    	replace(pInput, pOutput, pMapping, pEncoding, pEncoding);
    }   
    
    /**
     * Replaces key/value mappings read from the given input file and writes the result to the given
     * output file. This method requires ASCII files. If the given input and output file are the same,
     * then this methods caches the data in a temporary file or in memory before writing the result to
     * the output file. An input file with a filesize smaller than 1M is cached in memory.
     * 
     * @param pInput the input file
     * @param pOutput the output file
     * @param pMapping the key/value mapping e.g. ${PARAM1} = JVx
     * @param pInEncoding the encoding of the input file
     * @param pOutEncoding the encoding for the output file
     * @throws IOException if a read or write error occurs
     */
    public static void replace(File pInput, File pOutput, Hashtable<String, String> pMapping, String pInEncoding, String pOutEncoding) throws IOException
    {
		FileInputStream fis = null;
		OutputStream    os = null;
		
		File fiTemp = null;
		
		try
		{
			fis = new FileInputStream(pInput);

			boolean bTempWrite;

			if (pInput == pOutput || pInput.equals(pOutput))
			{
				bTempWrite = true;
				
				if (pInput.length() <= 1024000L)
				{
					os = new ByteArrayOutputStream();
				}
				else
				{
					fiTemp = File.createTempFile(FileUtil.class.getName(), "jvx");
					
					os = new FileOutputStream(fiTemp);
				}
			}
			else
			{
				bTempWrite = false;
				os = new FileOutputStream(pOutput);
			}
			
			replace(fis, os, pMapping, pInEncoding, pOutEncoding);
			
			fis.close();
			fis = null;
			
			if (bTempWrite)
			{
				//try to rename the file -> fast variant
				if (fiTemp != null)
				{
					if (pInput.delete())
					{
						if (fiTemp.renameTo(pInput))
						{
							bTempWrite = false;
						}
					}
				}
				
				if (bTempWrite)
				{
					if (os instanceof ByteArrayOutputStream)
					{
						BufferedOutputStream bos = null;
						
						try
						{
							bos = new BufferedOutputStream(new FileOutputStream(pOutput));
							bos.write(((ByteArrayOutputStream)os).toByteArray());
						}
						finally
						{
						    CommonUtil.close(bos);
						}
					}
					else
					{
						copy(fiTemp, pOutput);
					}
				}
			}
		}
		finally
		{
		    CommonUtil.close(fis, os);
			
			if (fiTemp != null)
			{
				if (!fiTemp.delete())
				{
					fiTemp.deleteOnExit();
				}
			}
		}
    }
    
    /**
     * Replaces key/value mappings read from the given input stream and writes the result to the given
     * output stream. This method requires ASCII streams.
     * 
     * @param pInput the input stream
     * @param pOutput the output stream
     * @param pMapping the key/value mapping e.g. ${PARAM1} = JVx
     * @param pInEncoding the encoding of the input stream
     * @param pOutEncoding the encoding for the output stream
     * @throws IOException if a read or write error occurs
     */
    public static void replace(InputStream pInput, OutputStream pOutput, Hashtable<String, String> pMapping, String pInEncoding, String pOutEncoding) throws IOException
    {
    	BufferedReader brInput = null;
    	BufferedWriter bwOutput = null;
    	
    	try
    	{
			//Input
			InputStreamReader isr;
			
			if (pInEncoding != null)
			{
				isr = new InputStreamReader(pInput, pInEncoding);
			}
			else
			{
				isr = new InputStreamReader(pInput);
			}
			
			brInput = new BufferedReader(isr);
	
			//Output
			
			OutputStreamWriter oswTemplate;
			
			if (pOutEncoding != null)
			{
				oswTemplate = new OutputStreamWriter(pOutput, pOutEncoding);
			}
			else
			{
				oswTemplate = new OutputStreamWriter(pOutput);
			}
			
			
			bwOutput = new BufferedWriter(oswTemplate);
			
			
			String sLineSeparator = System.getProperty("line.separator");
			
	        String sLine = brInput.readLine();
	        while (sLine != null)
	        {
        		for (Map.Entry<String, String> entry : pMapping.entrySet())
        		{
        			sLine = StringUtil.replace(sLine, entry.getKey(), entry.getValue());
        		}
	        	
	        	bwOutput.write(sLine);
	        	
	        	sLine = brInput.readLine();

	        	//last line!
	        	if (sLine != null)
	        	{
		        	bwOutput.write(sLineSeparator);
	        	}
	        }
	        
	    	bwOutput.write(sLineSeparator);
    	}
    	finally
    	{
    	    CommonUtil.close(brInput, bwOutput);
    	}
    }
    
    /**
     * Detects whether the content has an GZIP Header.
     * @param pContent the content.
     * @return true, if the content has an GZIP Header.
     */
    public static boolean hasGZIPHeader(byte[] pContent)
    {
    	return pContent != null
    			&& pContent.length >= 10
    			&& pContent[0] == 31
    			&& pContent[1] == -117
    			&& pContent[2] == 8;
    }
    
    /**
     * Detects whether the content has an GZIP Header.
     * @param pContent the content.
     * @return true, if the content has an GZIP Header.
     * @throws IOException if it fails.
     */
    public static boolean hasGZIPHeader(InputStream pContent) throws IOException
    {
    	if (pContent == null)
    	{
    		return false;
    	}
    	
    	if (!pContent.markSupported())
    	{
    		throw new IOException("The InputStream has no mark support!");
    	}
    	
    	pContent.mark(10);
    	
    	try
    	{
	    	byte[] header = new byte[10];
	    	
    		return pContent.read(header) >= 10 && hasGZIPHeader(header);
    	}
    	finally
    	{
    		pContent.reset();
    	}
    }
    
	/**
	 * Fast like search in path with wildcard(**, * and ?) support.
	 *  
	 * @param pSource any string
	 * @param pSearch search pattern with or without wildcards.
	 * @return <code>true</code> if, and only if, the string matches the pattern
	 */
	public static boolean like(String pSource, String pSearch) 
	{
	    if (pSource == null && pSearch == null) 
	    {
	    	return true;
	    }
	    else if (pSource == null || pSearch == null) 
	    {
	    	return false;
	    }
	    else 
	    {
	    	return like(pSource, 0, pSource.length(), pSearch, 0, pSearch.length());
	    }
	}
    
	/**
	 * Sub function of like. For performance reasons, the original Strings are not modified,
	 * The start and end defines the region to search.
	 * 
	 * @param pSource	the source string.
	 * @param pSrcStart	the start index of the source region.
	 * @param pSrcEnd	the end index of the source region.
	 * @param pSearch	the search string.
	 * @param pStart  	the start index of the search region.
	 * @param pEnd		the end index of the search region.
	 * @return true, if the like matches.
	 */
	private static boolean like(String pSource, int pSrcStart, int pSrcEnd, 
			                    String pSearch, int pStart,    int pEnd)
	{
		int pos = pSrcStart;
	  	for (int i = pStart; i < pEnd; i++) 
	  	{
	  		char ch = pSearch.charAt(i);
	  		if (ch == '*') 
	  		{
	  			if (i + 1 < pEnd && pSearch.charAt(i + 1) == '*')
	  			{	  				
		  			if (i + 1 == pEnd - 1)
	  				{
	  					return true;
	  				}
	  				
		  			int nStart = i + 2;
		  			
		  			while (pos < pSrcEnd) 
		  			{ 
		  				if (like(pSource, pos, pSrcEnd, pSearch, nStart, pEnd))
		  				{
		  					return true;
		  				}
		  				pos++;
		  			}
		  			return false;
	  			}
	  			else
	  			{
		  			int nStart = i + 1;
		  			
		  			int iSrcEnd = pSource.indexOf('/', pos);
		  			
		  			if (iSrcEnd < 0)
		  			{
			  			iSrcEnd = pSource.indexOf('\\', pos);
		  			}
		  			
		  			if (iSrcEnd < 0)
		  			{
			  			if (i == pEnd - 1) 
			  			{
			  				return true;
			  			}
		  				
		  				iSrcEnd = pSrcEnd;
		  			}
		  			else
		  			{
		  				iSrcEnd++;
		  			}
		  			
		  			int iEnd = pSearch.indexOf('/', nStart);
		  			
		  			if (iEnd < 0)
		  			{
		  				iEnd = pSearch.indexOf('\\', nStart);
		  			}
		  			
		  			if (iEnd < 0)
		  			{
		  				iEnd = pEnd;
		  			}
		  			else
		  			{
		  				iEnd++;
		  			}
		  			
		  			while (pos < iSrcEnd) 
		  			{ 
		  				if (like(pSource, pos, iSrcEnd, pSearch, nStart, iEnd))
		  				{
		  					if (like(pSource, iSrcEnd, pSrcEnd, pSearch, iEnd, pEnd))
		  					{
		  						return true;
		  					}
		  				}
		  				pos++;
		  			}
		  			return false;
	  			}
	  		}
	  		else if (pos == pSrcEnd) 
	  		{
	  			return false;
	  		}
	  		else 
	  		{
	  			if (ch != '?' && ch != pSource.charAt(pos)) 
	  			{
	  				return false;
	  			}
	  			pos++;
	  		}
	  	}
	  	return pos == pSrcEnd;
	}    
	
}	// FileUtil
