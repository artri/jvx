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
 */
package com.sibvisions.util;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sibvisions.util.type.FileUtil;

/**
 * Tests for {@link FileSearch}.
 * 
 * @author René Jahn
 */
public class TestFileSearch
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the root search directory. */
	private static File fiDirRoot;
	
	/** the sub search directory. */
	private static File fiDirSub;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates the directory structure for the search.
	 */
	@BeforeClass
	public static void beforeClass()
	{
		fiDirRoot = new File(System.getProperty("java.io.tmpdir"), "search");
		fiDirRoot.mkdir();
		
		fiDirSub = new File(fiDirRoot, "sub");
		fiDirSub.mkdir();
		
		try
		{
			new File(fiDirRoot, "1.txt").createNewFile();
			new File(fiDirRoot, "2.exe").createNewFile();
			
			new File(fiDirSub, "sub_1.txt").createNewFile();
			new File(fiDirSub, "sub_2.txt").createNewFile();
			new File(fiDirSub, "sub_3.exe").createNewFile();
			
			new File(fiDirSub, "sub_sub").mkdir();
		}
		catch (Exception e)
		{
			//egal
		}
	}
	
	/**
	 * Deletes the directory structure.
	 */
	@AfterClass
	public static void afterClass()
	{
		File[] fiSub = fiDirSub.listFiles();
		
		for (int i = 0, anz = fiSub.length; i < anz; i++)
		{
			fiSub[i].delete();
		}

		fiSub = fiDirRoot.listFiles();

		for (int i = 0, anz = fiSub.length; i < anz; i++)
		{
			fiSub[i].delete();
		}
		
		fiDirRoot.delete();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the search method.
	 */
	@Test
	public void testSearch()
	{
		FileSearch fs = new FileSearch();
		
		//DEEP directory search
		fs.search(fiDirRoot, true, "*/*.txt", "*/");
		
		Assert.assertEquals(3, fs.getFoundFiles().size());
		Assert.assertEquals(2, fs.getFoundDirectories().size());
		Assert.assertEquals(2, fs.getFilesPerDirectory().get(new File(fiDirRoot, "sub").getAbsolutePath()).size());
		
		String[] sHierDir = new String[fs.getFoundDirectories().size()];
		fs.getHierarchicalDirectoryList().toArray(sHierDir);
		
		Assert.assertEquals("sub", new File(sHierDir[0]).getName());
		Assert.assertEquals("sub_sub", new File(sHierDir[1]).getName());
		
		
		//FLAT directory search
		fs.clear();
		fs.search(fiDirRoot, false, "*");
		
		Assert.assertEquals(2, fs.getFoundFiles().size());

		
		//simple FILE search
		fs.clear();
		fs.search(new File(fiDirRoot, "1.txt").getAbsolutePath(), false);
		
		Assert.assertEquals(0, fs.getFoundFiles().size());
		
		
		//simpoe NOT search
		fs.clear();
		fs.search(fiDirRoot, false, "!*/1.txt");
		
		Assert.assertEquals(1, fs.getFoundFiles().size());
		Assert.assertEquals("2.exe", fs.getFoundFiles().get(0).substring(fs.getFoundFiles().get(0).length() - 5));
	}

	/**
	 * Tests the search method, but only directories which contains valid files should be found.
	 */
	@Test
	public void testSearchOnlyDirsWithFilePattern()
	{
		FileSearch fs = new FileSearch();
		
		//DEEP directory search
		fs.search(fiDirRoot, true, "*/*.txt");
		
		Assert.assertEquals(3, fs.getFoundFiles().size());
		Assert.assertEquals(1, fs.getFoundDirectories().size());
	}
	
	/**
	 * Search only directories.
	 */
	@Test
	public void testSearchOnlyDirs()
	{
		FileSearch fs = new FileSearch();
		
		//DEEP directory search
		fs.search(fiDirRoot, true, "*/");
		
		Assert.assertEquals(0, fs.getFoundFiles().size());
		Assert.assertEquals(2, fs.getFoundDirectories().size());
	}	

	/**
	 * Tests directory search with one empty directory.
	 */
	@Test
	public void test948()
	{
        File fiSearch = new File(fiDirRoot, "bug948");
        fiSearch.mkdirs();
        
        try
        {
            //must be empty!
            new File(fiSearch, ".settings").mkdir();
    	    
    	    FileSearch fs = new FileSearch();
            fs.search(fiSearch, true);
            
            Assert.assertEquals(0, fs.getFoundFiles().size());
            Assert.assertEquals(1, fs.getFoundDirectories().size());
        }
        finally
        {
            FileUtil.delete(fiSearch);
        }
	}
	
}	// TestFileSearch
