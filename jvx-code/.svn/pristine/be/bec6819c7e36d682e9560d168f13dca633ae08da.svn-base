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
 * 15.06.2009 - [JR] - setPattern: *.* if null or empty
 * 27.10.2010 - [JR] - exclude pattern: first char == !
 * 28.10.2010 - [JR] - used * instead of *.*
 *                   - String... to support multiple patterns
 *                   - unified path
 * 06.12.2010 - [JR] - searchFirstFile implemented  
 *                   - searchIntern: don't add start directory and start file to the search results
 *                   - getHierarchicalDirectoryList: used found directories instead of Hashtable,
 *                                                   because the Hashtable does not include empty dirs
 * 26.05.2011 - [JR] - #365: clear creates new instances (re-use bugfix)
 * 17.02.2014 - [JR] - #948: fixed indexOf check in searchIntern 
 * 20.06.2022 - [JR] - case sensitive search option                                                                   
 */
package com.sibvisions.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>FileSearch</code> is a utility class to search files and directories.
 *
 * @author René Jahn
 */
public class FileSearch
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** contains all found directories. */
    private ArrayUtil<String> auFoundDirectories = new ArrayUtil<String>();
    
    /** contains all found files. */
    private ArrayUtil<String> auFoundFiles = new ArrayUtil<String>();

    /** contains all files per directory. */
    private Hashtable<String, List<String>> htDirList = new Hashtable<String, List<String>>();
    
    /** the filename pattern filter. */
    private NamePatternFilter filter = null;
    
    /** the search start. */
    private File fiStartSearch = null;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>FileSearch</code>.
     */
    public FileSearch()
    {
    	filter = new NamePatternFilter();
    }
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Searches files and directories. The result will be appended to previous 
     * search results.
     * 
     * @param pAnalyse the start directory/file
     * @param pDeepSearch <code>true</code> to search files and directories recursive 
     *                  (include subdirectories)
     * @param pPathPattern a list of path patterns (with wildcard <code>*</code>) or <code>null</code> if all files are valid.
     *                     If the first character is <code>!</code> then the pattern is an exclude pattern (all except...). 
     *                     Directories end with a <code>/</code>, e.g. a directory pattern <code>*</code><code>/</code>.
     * @see #clear()
     */
    public void search(String pAnalyse, boolean pDeepSearch, String... pPathPattern)
    {
    	if (pAnalyse == null)
    	{
    		return;
    	}
    	
        //use the desired pattern
        filter.setPattern(pPathPattern);
        filter.setBasePath(pAnalyse);

        fiStartSearch = new File(pAnalyse);
       
        searchIntern(fiStartSearch, pDeepSearch);
        
        fiStartSearch = null;
    }

    /**
     * Searches files and directories. The result will be appended to previous 
     * search results.
     * 
     * @param pAnalyse the start directory/file
     * @param pDeepSearch <code>true</code> to search files and directories recursive 
     *                  (include subdirectories)
     * @param pPathPattern a list of path patterns (with wildcard <code>*</code>) or <code>null</code> if all files are valid.
     *                     If the first character is <code>!</code> then the pattern is an exclude pattern (all except...).
     *                     Directories end with a <code>/</code>, e.g. a directory pattern <code>*</code><code>/</code>.
     * @see #clear()
     */
    public void search(File pAnalyse, boolean pDeepSearch, String... pPathPattern)
    {
    	if (pAnalyse == null)
    	{
    		return;
    	}
    	
        //use the desired pattern
        filter.setPattern(pPathPattern);
        filter.setBasePath(pAnalyse);
    	
        fiStartSearch = pAnalyse;
        
    	searchIntern(pAnalyse, pDeepSearch);
    	
    	fiStartSearch = null;
    }
    
    /**
     * Searches files and directories.
     * 
     * @param pAnalyse the start directory/file
     * @param pDeepSearch <code>true</code> to search files and directories recursive 
     *                  (include subdirectories)
     */
    private void searchIntern(File pAnalyse, boolean pDeepSearch)
    {
        File[] fFound;
        ArrayUtil<String> auFiles = new ArrayUtil<String>();


        //can't do further search with files -> stop here
        if (pAnalyse.isFile())
        {
            return;
        }

        fFound = pAnalyse.listFiles(filter);

        if (fFound != null)
        {
        	boolean bDirChecked = false;
        	
            for (int i = 0, anz = fFound.length; i < anz; i++)
            {
                if (fFound[i].isDirectory())
                {
                	//add to search result, only if allowed
                	if (filter.isAllowed(fFound[i]))
                	{
                		auFoundDirectories.add(fFound[i].getAbsolutePath());
                	}

                	if (pDeepSearch)
                    {
                        searchIntern(fFound[i], true);
                    }
                }
                else
                {
                	//ignore the search-start directory
                	if (pAnalyse != fiStartSearch && !bDirChecked)
                	{
	                	//if the directory was not included through the patterns -> add the directory
	                	if (auFoundDirectories.indexOf(pAnalyse.getAbsolutePath()) < 0)
	                	{
	                		bDirChecked = true;
	                		auFoundDirectories.add(pAnalyse.getAbsolutePath());
	                	}
                	}
                	
                    auFoundFiles.add(fFound[i].getAbsolutePath());

                    //add the file to the directory mapping
                    auFiles.add(fFound[i].getName());
                }
            }
        }
        
        if (fFound == null || fFound.length == 0)
        {
        	if (pAnalyse != fiStartSearch 
	    		&& filter.isAllowed(pAnalyse)
	    		&& auFoundDirectories.indexOf(pAnalyse.getAbsolutePath()) < 0)
	    	{
	        	//if the directory was not included with through the patterns -> add the directory
        		auFoundDirectories.add(pAnalyse.getAbsolutePath());
	    	}
        }        

        //no files -> ignore dir
        if (!auFiles.isEmpty())
        {
            //map the files per directory
            htDirList.put(pAnalyse.getAbsolutePath(), auFiles);
        }
    }

    /**
     * Clear previous search results.
     */
    public void clear()
    {
    	//don't clear because getter return references!
    	auFoundFiles = new ArrayUtil<String>();
    	auFoundDirectories = new ArrayUtil<String>();
    	
    	htDirList = new Hashtable<String, List<String>>();
    }
    
    /**
     * Gets the found files of a previous search.
     * 
     * @return the absolute path for all found files
     */
    public List<String> getFoundFiles()
    {
        return auFoundFiles;
    }

    /**
     * Gets the found directories of a previous search.
     * <p>
     * @return the absolute path for all found directories
     */
    public List<String> getFoundDirectories()
    {
        return auFoundDirectories;
    }

    /**
     * Gets a list of found files per found directory.
     * <p>
     * @return the absolute path for all files per directory (also as absolute path)
     */
    public Hashtable<String, List<String>> getFilesPerDirectory()
    {
        return htDirList;
    }

    /**
     * Gets the hierarchial directory tree as flat list:
     * <pre>
     * directory-1
     * |- sub-1.1
     * |- sub-1.2
     * |- sub-1.3
     * |  |- sub 1.3.1
     * |  |- sub 1.3.2
     * |- sub 1.4
     * 
     * will return a list with following elements:
     * 
     * directory-1
     * sub-1.1
     * sub-1.2
     * sub-1.3
     * sub-1.3.1
     * sub-1.3.2
     * sub-1.4
     * </pre>.
     * 
     * @return a flat list of a hierarchical directory tree
     */
    public Set<String> getHierarchicalDirectoryList()
    {
        TreeSet<String> ts = new TreeSet<String>
        (
            new Comparator<String>()
            {
                public int compare(String s1, String s2)
                {
                    int iResult = s1.length() - s2.length();

                    //alphabetical sort, if the directories are in the same hierarchy
                    if (iResult == 0)
                    {
                        return s1.compareTo(s2);
                    }

                    return iResult;
                }
            }
        );

        ts.addAll(auFoundDirectories);


        return ts;
    }
    
    /**
     * Sets whether search is case sensitive.
     * 
     * @param pCaseSensitive <code>true</code> to search case sensitive, <code>false</code> otherwise
     */
    public void setCaseSensitive(boolean pCaseSensitive)
    {
    	filter.setCaseSensitive(pCaseSensitive);
    }
    
    /**
     * Gets whether search is case sensitive.
     * 
     * @return <code>true</code> if search is case sensitive, <code>false</code> otherwise
     */
    public boolean isCaseSensitive()
    {
    	return filter.isCaseSensitive();
    }
    
    /**
     * Searches the first file which fits the given pattern(s).
     *  
     * @param pSource the start directory
     * @param pPattern the file pattern(s). If a pattern starts with <code>!</code> then the pattern
     *                 is an exclude pattern
     * @return the found file or <code>null</code> if no file was found
     */
    public static File searchFirstFile(File pSource, String... pPattern)
    {
    	return searchFirstFile(pSource, true, pPattern);
    }
    
    /**
     * Searches the first file which fits the given pattern(s).
     *  
     * @param pSource the start directory
     * @param pPattern the file pattern(s). If a pattern starts with <code>!</code> then the pattern
     *                 is an exclude pattern
     * @param pCaseSensitive whether to search case sensitive or case insensitive                 
     * @return the found file or <code>null</code> if no file was found
     */
    public static File searchFirstFile(File pSource, boolean pCaseSensitive, String... pPattern)
    {
    	NamePatternFilter filter = new NamePatternFilter();
    	filter.setCaseSensitive(pCaseSensitive);
    	filter.setBasePath(pSource);
    	filter.setPattern(pPattern);
    	
    	return searchFirstFileIntern(pSource, filter);
    }
    
    /**
     * Searches the first file which fulfills the given filter.
     *  
     * @param pSource the start directory
     * @param pFilter the file filter
     * @return the found file or <code>null</code> if no file was found
     */
    private static File searchFirstFileIntern(File pSource, FileFilter pFilter)
    {
    	if (pSource.isFile())
    	{
    		return pSource;
    	}
    	
    	File[] fiList = pSource.listFiles(pFilter);
    	
    	if (fiList != null)
    	{
    		for (File fiFound : fiList)
    		{
    			if (fiFound.isFile())
    			{
    				return fiFound;
    			}
    		}
    		
    		for (File fiFound : fiList)
    		{
    			if (fiFound.isDirectory())
    			{
    				fiFound = searchFirstFileIntern(fiFound, pFilter);
    				
    				if (fiFound != null)
    				{
    					return fiFound;
    				}
    			}
    		}
    	}
    	
    	return null;
    }

	//****************************************************************
	// Subclass definition
	//****************************************************************
    
    /**
     * The <code>NamePatternFilter</code> is a filename filter which accept
     * only files when the filename matches one of the set filters.
     */
    private static final class NamePatternFilter implements FileFilter
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** the base path which is not relevant for matching. */
    	private String sBasePath = null;
    	
    	/** the include patterns. */
        private String[] saInclude = null;
    	/** the include patterns (lower case). */
        private String[] saIncludeLower = null;
        
        /** the exclude patterns. */
        private String[] saExclude = null;
        /** the exclude patterns (lower case). */
        private String[] saExcludeLower = null;
        
        /** cached base path length (for fast access). */
        private int iBasePathLength;
        
        /** whether to search case sensitive. */
        private boolean bCaseSensitive = true;

    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Interface implementation
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * {@inheritDoc}
         */
        public boolean accept(File pPath)
        {
        	//special handling for directories, because it is important that we go through all sub directories which
        	//are not excluded, otherwise we won't find files in "deeper" directories
        	//
        	//the directory exclusion is checked in search(...) with isAllowed()
            if (pPath.isDirectory())
        	{
                if (saExclude != null)
                {
                	String sName = pPath.getAbsolutePath().substring(iBasePathLength).replace('\\', '/');
                	
                	for (String sPattern : bCaseSensitive ? saExclude : saExcludeLower)
                	{
                		if (StringUtil.like(bCaseSensitive ? sName : sName.toLowerCase(), sPattern))
                		{
                			return false;
                		}
                	}
                }
        		
        		return true;
        	}
            else
            {
            	return isAllowed(pPath);
            }
        }
        
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// User-defined methods
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /**
         * Sets the pattern for the filename search.
         * 
         * @param pPattern the search pattern
         */
        public void setPattern(String... pPattern)
        {
        	ArrayUtil<String> auInclude = new ArrayUtil<String>();
        	ArrayUtil<String> auExclude = new ArrayUtil<String>();
        	
        	if (pPattern == null || pPattern.length == 0)
        	{
        		saInclude = new String[] {"*"};
        		saIncludeLower = new String[] {"*"};
        		
        		return;
        	}
        	
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
        		saIncludeLower = new String[] {"*"};
        	}
        	else
        	{
	        	saInclude = new String[auInclude.size()];
	        	auInclude.toArray(saInclude);
	        	
	        	saIncludeLower = new String[auInclude.size()];
	        	
	        	for (int i = 0; i < saInclude.length; i++)
	        	{
	        		saIncludeLower[i] = saInclude[i].toLowerCase();
	        	}
        	}
        	
        	if (auExclude.size() == 0)
        	{
        		saExclude = null;
        		saExcludeLower = null;
        	}
        	else
        	{
	        	saExclude = new String[auExclude.size()];
	        	auExclude.toArray(saExclude);
	        	
	        	saExcludeLower = new String[auExclude.size()];
	        	
	        	for (int i = 0; i < saExclude.length; i++)
	        	{
	        		saExcludeLower[i] = saExclude[i].toLowerCase();
	        	}
        	}
        }
        
        /**
         * Sets the base path which is excluded from filtering.
         * 
         * @param pPath the excluded path
         */
        public void setBasePath(String pPath)
        {
        	sBasePath = pPath;
        	
        	iBasePathLength = sBasePath.length();
        }
        
        /**
         * Sets the base path which is excluded from filtering.
         * 
         * @param pPath the excluded path
         */
        public void setBasePath(File pPath)
        {
        	sBasePath = pPath.getAbsolutePath();
        	
        	iBasePathLength = sBasePath.length();
        }
        
        /**
         * Sets whether search is case sensitive.
         * 
         * @param pCaseSensitive <code>true</code> to search case sensitive, <code>false</code> otherwise
         */
        public void setCaseSensitive(boolean pCaseSensitive)
        {
        	bCaseSensitive = pCaseSensitive;
        }
        
        /**
         * Gets whether search is case sensitive.
         * 
         * @return <code>true</code> if search is case sensitive, <code>false</code> otherwise
         */
        public boolean isCaseSensitive()
        {
        	return bCaseSensitive;
        }
        
        /**
         * Returns whether a file is valid with consideration of the allow and deny rules.
         * 
         * @param pFile the file
         * @return <code>true</code> if the file is allowed or <code>false</code> if it is not allowed
         */
        public boolean isAllowed(File pFile)
        {
        	String sName = pFile.getAbsolutePath().substring(iBasePathLength).replace('\\', '/');
        	
        	//mark a directory
        	if (pFile.isDirectory())
        	{
        		sName = sName + "/";
        	}
        	
        	if (saExclude != null)
            {
            	for (String sPattern : bCaseSensitive ? saExclude : saExcludeLower)
            	{
            		if (StringUtil.like(bCaseSensitive ? sName : sName.toLowerCase(), sPattern))
            		{
            			return false;
            		}
            	}
            }
        
            if (saInclude != null)
            {
            	for (String sPattern : bCaseSensitive ? saInclude : saIncludeLower)
            	{
            		if (StringUtil.like(bCaseSensitive ? sName : sName.toLowerCase(), sPattern))
            		{
            			return true;
            		}
            	}
            }
            
            return false;
        }

    }   // NamePatternFilter

}   // FileSearch

