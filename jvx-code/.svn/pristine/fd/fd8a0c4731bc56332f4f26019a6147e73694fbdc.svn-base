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
 * 06.03.2014 - [JR] - creation
 */
package com.sibvisions.util;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.Version.Level;


/**
 * Tests the {@link Version} methods.
 * 
 * @author René Jahn
 * @see Version
 */
public class TestVersion
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Tests {@link Version#Version(int, int, int, int)}.
     */
    @Test
    public void testConstructor()
    {
        try
        {
            new Version(-1, -2, -3, -1);
            
            Assert.fail("Invalid version created!");
        }
        catch (IllegalArgumentException iae)
        {
            Assert.assertEquals("No valid version information!", iae.getMessage());
        }
    }
    
    /**
     * Tests {@link Version#toString()}.
     */
    @Test
    public void testToString()
    {
        Version version = new Version(2, 1, 0, 1);
        
        Assert.assertEquals("2.1.0.1", version.toString());
        Assert.assertEquals("2.1.0", version.toString(Level.Build));
        Assert.assertEquals("2", version.toString(Level.Major));
        
        Assert.assertEquals("2.1.0.0", new Version(2, 1, -1, -1).toString());
        Assert.assertEquals("2.1", new Version(2, 1, -1, -1).toString(Level.Revision, true));
        Assert.assertEquals("2.1.0.5", new Version(2, 1, -1, 5).toString(Level.Revision, true));
        Assert.assertEquals("2.1", new Version(2, 1, -1, -1).toString(true));
        Assert.assertEquals("2.1", Version.parse("2.1").toString(true));
        Assert.assertEquals("2.1", Version.parse("2.1..").toString(true));
        Assert.assertEquals("2.1.0.0", Version.parse("2.1").toString(false));
        
        Assert.assertEquals("2.1", Version.parse("2.1.0.0").toString(true));
        Assert.assertEquals("2.0", Version.parse("2.0.0.0").toString(true));
        Assert.assertEquals("2.0.1", Version.parse("2.0.1.0").toString(true));
    }
    
    /**
     * Tests {@link Version#toNumber()}.
     */
    @Test
    public void testToNumber()
    {
        Version version = new Version(0, 1, 2, 2);
        
        Assert.assertEquals(100020002, version.toNumber());
        Assert.assertEquals(10002, version.toNumber(Level.Build));
        Assert.assertEquals(0, version.toNumber(Level.Major));
        
        Assert.assertEquals(20002, new Version(0, 0, 2, 2).toNumber());
        Assert.assertEquals(1000100020000L, new Version(1, 1, 2, 0).toNumber());
        Assert.assertEquals(200020002, new Version(0, 2, 2, 2).toNumber());
        Assert.assertEquals(100020002, new Version(0, 1, 2, 2).toNumber());

        Assert.assertEquals(100020000, new Version(0, 1, 2, -1).toNumber());
        Assert.assertEquals(1, new Version(0, -1, -1, 1).toNumber());
    }
    
    /**
     * Tests {@link Version#getLevel()}.
     */
    @Test
    public void testGetLevel()
    {
        Assert.assertEquals(Level.Minor, new Version(0, 1).getLevel());
        Assert.assertEquals(Level.Build, new Version(0, -1, 2, -1).getLevel());
        Assert.assertEquals(Level.Minor, new Version(1, 0, -1).getLevel());
        Assert.assertEquals(Level.Revision, new Version(0, 1, 2, 2).getLevel());
        Assert.assertEquals(Level.Major, new Version(1).getLevel());
        Assert.assertEquals(Level.Minor, new Version(-1, 1).getLevel());
        
        try
        {
            Assert.assertNull(new Version(-1).getLevel());
            
            Assert.fail("Invalid version created!");
        }
        catch (IllegalArgumentException iae)
        {
            Assert.assertEquals("No valid version information!", iae.getMessage());
        }
    }
    
    /**
     * Tests {@link Version#equals()}.
     */
    @Test
    public void testEquals()
    {
        Assert.assertNotEquals(new Version(1), new Version(1, 0, 2, 1));
        Assert.assertNotEquals(new Version(2), new Version(0, 0, 0, 2));
        Assert.assertNotEquals(new Version(2), new Version(-1, -1, -1, 2));
        Assert.assertNotEquals(new Version(2, 5, 2, 1), new Version(1, 2, 5, 2));
        Assert.assertNotEquals(new Version(1, 2, 5, 1), new Version(1, 2, 5, 2));

        Assert.assertEquals(new Version(1), new Version(1));
        Assert.assertEquals(new Version(1), new Version(1, -1, -1));
    }
    
    /**
     * Tests {@link Version#getLevel()}.
     */
    @Test
    public void testCompareTo()
    {
        Assert.assertEquals(-1, new Version(1).compareTo(new Version(2)));
        Assert.assertEquals(-1, new Version(0, 5, 5, 0).compareTo(new Version(0, 5, 5, 5)));
        Assert.assertEquals(-1, new Version(0, 1, 2, 0).compareTo(new Version(1, 3)));
        Assert.assertEquals(-1, new Version(1, 0).compareTo(new Version(1, 2, 1), Level.Build));

        Assert.assertEquals(0, new Version(2).compareTo(new Version(2, 0, 0, 0)));
        Assert.assertEquals(0, new Version(2).compareTo(new Version(2, -1, -1, 0)));
        Assert.assertEquals(0, new Version(2).compareTo(new Version(2, -1, -1, -1)));

        Assert.assertEquals(0, new Version(1, 0).compareTo(new Version(1, 0, 1), Level.Minor));
        Assert.assertEquals(0, new Version(1, 0).compareTo(new Version(1, 2, 1), Level.Major));
        
        Assert.assertEquals(1, new Version(1).compareTo(new Version(0, 5)));
        Assert.assertEquals(1, new Version(0, 5, 5, 5).compareTo(new Version(0, 5, 5, 0)));
        Assert.assertEquals(1, new Version(0, 1, 1, 2).compareTo(new Version(0, 0, 0, 5)));
        Assert.assertEquals(1, new Version(0, 3, -1, -1).compareTo(new Version(0, 2, 0, -1)));
        Assert.assertEquals(1, new Version(2, 0).compareTo(new Version(1, 9)));
        
        //2.0 is not compatible with 3.0
        Assert.assertEquals(-1, Version.parse("2.0.0").compareTo(Version.parse("3.0.0"), Level.Major));
        //different exact match comparison
        Assert.assertEquals(0, Version.parse("3.0").compareTo(Version.parse("3.0.0"), Level.Major));
        Assert.assertEquals(0, Version.parse("3").compareTo(Version.parse("3.0.0"), Level.Major));
        Assert.assertEquals(0, Version.parse("3.0").compareTo(Version.parse("3"), Level.Major));
        Assert.assertEquals(0, Version.parse("3.0.0").compareTo(Version.parse("3.0"), Level.Major));
        Assert.assertEquals(1, Version.parse("3.0.0").compareTo(Version.parse("2.0"), Level.Major));
        
        //2.0.* is compatible with 2.1.0
        Assert.assertEquals(-1, Version.parse("2.0.0").compareTo(Version.parse("2.1.0"), Level.Minor));
        Assert.assertEquals(-1, Version.parse("2.0.1").compareTo(Version.parse("2.1.0"), Level.Minor));
        Assert.assertEquals(0, Version.parse("2.1.0").compareTo(Version.parse("2.1.0"), Level.Minor));
        
        Assert.assertEquals(1, Version.parse("2.1.0").compareTo(Version.parse("2.0.0"), Level.Minor));
        
        //2.1.1 is compatible with 2.1.0
        Assert.assertEquals(0, Version.parse("2.1.1").compareTo(Version.parse("2.1.0"), Level.Minor));
        //2.2.0 is newer than 2.1.0
        Assert.assertEquals(1, Version.parse("2.2.0").compareTo(Version.parse("2.1.0"), Level.Minor));
        Assert.assertEquals(-1, Version.parse("2.0.0").compareTo(Version.parse("3.0.0"), Level.Minor));
    }
    
    /**
     * Tests {@link Version#parse(String)}.
     */
    @Test
    public void testParse()
    {
        Assert.assertEquals(2000000000000L, Version.parse("2.").toNumber());
        Assert.assertEquals(2000000000001L, Version.parse("2.0.0.1").toNumber());
        Assert.assertEquals(2000000000000L, Version.parse("2.0.0.1a").toNumber());
        Assert.assertEquals(1, Version.parse("0.0.0.1").toNumber());
        Assert.assertEquals(2000100000000L, Version.parse("2.1").toNumber());
        
        Assert.assertEquals(2, Version.parse("2.1").getMajor());
        Assert.assertEquals(1, Version.parse("2.1").getMinor());
        Assert.assertEquals(-1, Version.parse("2.1").getBuild());
        Assert.assertEquals(-1, Version.parse("2.1").getRevision());

        Assert.assertEquals(4, Version.parse("2.1.3.4").getRevision());
        
        try
        {
            Version.parse(null);
            
            Assert.fail("Null version parsed!");
        }
        catch (IllegalArgumentException iae)
        {
            Assert.assertEquals("No valid version information!", iae.getMessage());
        }
        
        long lNumber = new Version(2, 5, 200, 1).toNumber();
        
        Version ver = Version.parse(lNumber);
        
        Assert.assertEquals(ver.getMajor(), 2);
        Assert.assertEquals(ver.getMinor(), 5);
        Assert.assertEquals(ver.getBuild(), 200);
        Assert.assertEquals(ver.getRevision(), 1);
    }

    /**
     * Tests {@link Version#isSmaller(Version)}.
     */
    @Test
    public void testIsSmaller()
    {
        Version ver = new Version(2, 1, 1);
        
        Assert.assertTrue(ver.isSmaller(Version.parse("2.2")));
        Assert.assertFalse(ver.isSmaller(Version.parse("2.1")));
        Assert.assertTrue(ver.isSmaller(Version.parse("3")));
    }

    /**
     * Tests {@link Version#isSmallerOrEqual(Version)}.
     */
    @Test
    public void testIsSmallerOrEquals()
    {
        Version ver = new Version(2, 1, 1);
        
        Assert.assertTrue(ver.isSmallerOrEqual(Version.parse("2.1.1")));
        Assert.assertFalse(ver.isSmallerOrEqual(Version.parse("2.1.0")));
    }

    /**
     * Tests {@link Version#isGreater(Version)}.
     */
    @Test
    public void testIsGreater()
    {
        Version ver = new Version(3, 1, 1);
        
        Assert.assertTrue(ver.isGreater(Version.parse("2.2")));
        Assert.assertFalse(ver.isGreater(Version.parse("3.1.1.1")));
        Assert.assertTrue(ver.isGreater(Version.parse("3")));
    }

    /**
     * Tests {@link Version#isGreaterOrEqual(Version)}.
     */
    @Test
    public void testIsGreaterOrEquals()
    {
        Version ver = new Version(3, 1, 1);
        
        Assert.assertTrue(ver.isGreaterOrEqual(Version.parse("3.1.1")));
        Assert.assertFalse(ver.isGreaterOrEqual(Version.parse("3.2.0")));
    }
    
}	// TestVersion

