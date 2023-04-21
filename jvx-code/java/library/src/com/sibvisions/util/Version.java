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
 * 03.06.2014 - [JR] - creation
 * 08.03.2017 - [JR] - toString(boolean) with support for 2.0.0.0 -> 2.0
 */
package com.sibvisions.util;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>Version</code> class is a simple representation for version numbers. It supports major, minor,
 * build and revision sequences, e.g. 2.0.0.1. The class supports parsing version strings.
 * 
 * @author René Jahn
 */
public class Version implements Comparable<Version>
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** the supported version levels. */
    public enum Level
    {
        /** Major. */
        Major,
        /** Minor. */
        Minor,
        /** Build. */
        Build,
        /** Revision. */
        Revision;
    }
    
    /** the major number. */
    private int iMajor;
    
    /** the minor number. */
    private int iMinor;
    
    /** the build number. */
    private int iBuild;
    
    /** the revision number. */
    private int iRevision;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of <code>Version</code> with given major number. A negative number
     * means ignore.
     * 
     * @param pMajor the major number
     */
    public Version(int pMajor)
    {
        this(pMajor, -1, -1, -1);
    }

    /**
     * Creates a new instance of <code>Version</code> with given major and minor
     * numbers.A negative number means ignore.
     * 
     * @param pMajor the major number
     * @param pMinor the minor number
     */
    public Version(int pMajor, int pMinor)
    {
        this(pMajor, pMinor, -1, -1);
    }

    /**
     * Creates a new instance of <code>Version</code> with given numbers. A negative number
     * means ignore.
     * 
     * @param pMajor the major number
     * @param pMinor the minor number
     * @param pBuild the build number
     */
    public Version(int pMajor, int pMinor, int pBuild)
    {
        this(pMajor, pMinor, pBuild, -1);
    }
    
    /**
     * Creates a new instance of <code>Version</code> with given numbers. A negative number
     * means ignore.
     * 
     * @param pMajor the major number
     * @param pMinor the minor number
     * @param pBuild the build number
     * @param pRevision the revision number
     */
    public Version(int pMajor, int pMinor, int pBuild, int pRevision)
    {
        iMajor = pMajor;
        iMinor = pMinor;
        iBuild = pBuild;
        iRevision = pRevision;
        
        if (pMajor < 0 && pMinor < 0 && pBuild < 0 && pRevision < 0)
        {
            throw new IllegalArgumentException("No valid version information!");
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the version number as full string wih all sequences (major, minor, build, revision).
     * 
     * @return the version string
     */
    @Override
    public String toString()
    {
        return toString(Level.Revision, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        
        int result = prime + iBuild;
        result = prime * result + iMajor;
        result = prime * result + iMinor;
        result = prime * result + iRevision;
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pObject)
    {
        if (this == pObject)
        {
            return true;
        }
        
        if (pObject == null)
        {
            return false;
        }
        
        if (getClass() != pObject.getClass())
        {
            return false;
        }
        
        Version other = (Version)pObject;
        
        if (iBuild != other.iBuild 
            || iMajor != other.iMajor
            || iMinor != other.iMinor
            || iRevision != other.iRevision)
        {
            return false;
        }

        return true;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Interface implementation
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    public int compareTo(Version pVersion)
    {
        return compareTo(pVersion, Level.Revision);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Parses a version number. Only number parts are allowed, e.g. 2.0.0.1. If the version
     * contains more than 4 parts, all other parts will be ignored. If a part isn't a number,
     * it will be ignored (set to -1).
     * 
     * @param pVersion the version string
     * @return the version
     */
    public static Version parse(String pVersion)
    {
        if (pVersion == null)
        {
            //throws an Exception
            return new Version(-1);
        }
        
        String[] sParts = pVersion.split("\\.");
        
        int[] iNr = new int[] {-1, -1, -1, -1};
        
        for (int i = 0; i < sParts.length; i++)
        {
            try
            {
                iNr[i] = Integer.parseInt(sParts[i]);
            }
            catch (NumberFormatException nfe)
            {
                //not a valid part
            }
        }
        
        return new Version(iNr[0], iNr[1], iNr[2], iNr[3]);
    }
    
    /**
     * Parses a version number. Each part is represented by 4 digits.
     * 
     * @param pVersion the version representation
     * @return the version
     */
    public static Version parse(long pVersion)
    {
        String sVersion = "" + pVersion;
        
        int iLen = sVersion.length();
        
        return new Version(Integer.parseInt(sVersion.substring(0, iLen - 12)),
                           Integer.parseInt(sVersion.substring(iLen - 12, iLen - 8)),
                           Integer.parseInt(sVersion.substring(iLen - 8, iLen - 4)), 
                           Integer.parseInt(sVersion.substring(iLen - 4)));
    }
    
    /**
     * Gets a version string with all sequences up-to the given level.
     * 
     * @param pLevel the version level
     * @param pShort <code>true</code> to stop after the last sequence that contains a valid number (&gt; 0).
     * @return the version string
     */
    public String toString(Level pLevel, boolean pShort)
    {
        if (pLevel == null)
        {
            throw new IllegalArgumentException("Level can't be null");
        }
        
        StringBuilder sb = new StringBuilder();

        int[] iNr = new int[] {iMajor, iMinor, iBuild, iRevision};
        
        int iValue;
        
        boolean bMoreDigits;
        
        for (int i = 0, cnt = pLevel.ordinal(); i <= cnt; i++)
        {
            iValue = iNr[i];

            if (pShort)
            {
                bMoreDigits = true;

                //short means: don't show sequences without value (= < 0) 
                
                //fill "gaps" -> 2, -1, 0, -1 = 2.0.0
                if (iValue < 0)
                {
                    for (int j = i + 1; j <= cnt && iValue < 0; j++)
                    {
                        if (iNr[j] >= 0)
                        {
                            iValue = 0;
                        }
                    }
                    
                    if (iValue < 0)
                    {
                        return sb.toString();
                    }
                }
                else if (iValue == 0)
                {
                    bMoreDigits = false;
                    
                    for (int j = i + 1; j <= cnt && !bMoreDigits; j++)
                    {
                        bMoreDigits = iNr[j] > 0;
                    }
                }
                
                if (!bMoreDigits && i > 1)
                {
                    //show at least minor version
                    return sb.toString();
                }

                if (sb.length() > 0)
                {
                    sb.append(".");
                }
                
                sb.append(iValue);
                
                if (!bMoreDigits)
                {
                    return sb.toString();
                }
            }
            else
            {
                //full representation: show all sequences
                if (i > 0)
                {
                    sb.append(".");
                }
                
                if (iValue < 0)
                {
                    sb.append(0);
                }
                else
                {
                    sb.append(iValue);
                }
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Gets a version for given level. All sequences will be used even if they are invalid. No
     * sequences will be ignored.
     * 
     * @param pLevel the level
     * @return the version string
     */
    public String toString(Level pLevel)
    {
        return toString(pLevel, false);
    }

    /**
     * Gets a version for given level.
     * 
     * @param pShort <code>true</code> to stop after the last sequence that contains a valid number (&gt; 0).
     * @return the version string
     */
    public String toString(boolean pShort)
    {
        return toString(Level.Revision, pShort);
    }
    
    /**
     * Gets the level of the last significant sequence, e.g. 2.0.1 will return {@link Level#Build}.
     * 
     * @return the level
     */
    public Level getLevel()
    {
        int[] iNr = new int[] {iMajor, iMinor, iBuild, iRevision};
        Level[] levels = new Level[] {Level.Major, Level.Minor, Level.Build, Level.Revision};
        
        Level level = null;
        
        for (int i = 0; i < 4; i++)
        {
            if (iNr[i] >= 0)
            {
                level = levels[i];
            }
        }
        
        return level;
    }
    
    /**
     * Gets whether the version is undefined. A version is undefined if all version parts
     * contain negative numbers.
     * 
     * @return <code>true</code> if version is undefined, <code>false</code> otherwise
     */
    public boolean isUndefined()
    {
        return iMajor < 0 && iMinor < 0 && iBuild < 0 && iRevision < 0;
    }
    
    /**
     * Gets the full version number.
     * 
     * @return the version number
     */
    public long toNumber()
    {
        return toNumber(Level.Revision);
    }
    
    /**
     * Gets the number representation of the version.
     * 
     * @param pLevel the expected depth/level
     * @return the number e.g. 2.1.0.2 = 2000100000002
     */
    public long toNumber(Level pLevel)
    {
        if (pLevel == null)
        {
            throw new IllegalArgumentException("Level can't be null");
        }
        
        int[] iNr = new int[] {iMajor, iMinor, iBuild, iRevision};
        
        int iValue;
        
        StringBuilder sbNumber = new StringBuilder();
        
        for (int i = 0, cnt = pLevel.ordinal(); i <= cnt; i++)
        {
            iValue = iNr[i];
        
            if (iValue < 0)
            {
                sbNumber.append("0000");
            }
            else
            {
                sbNumber.append(StringUtil.lpad("" + iValue, 4, '0'));
            }
        }        

        return Long.valueOf(sbNumber.toString()).longValue();
    }
    
    /**
     * Compares this version with another version.
     * 
     * @param pVersion the to compare
     * @param pLevel the check depth/level
     * @return -1 if this version is lower than the other version, 0 if both versions are equal, 1 if
     *         this version is larger than the other version
     */
    public int compareTo(Version pVersion, Level pLevel)
    {
        if (pLevel == null)
        {
            throw new IllegalArgumentException("Level can't be null");
        }
        
        long lNr = toNumber(pLevel);
        long lNr2;
        
        if (pVersion == null)
        {
            lNr2 = -1;
        }
        else
        {
            lNr2 = pVersion.toNumber(pLevel);
        }
        
        if (lNr < lNr2)
        {
            return -1;
        }
        else if (lNr > lNr2)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * Gets the major number (1.x.x.x).
     * 
     * @return the number (e.g. 1)
     */
    public int getMajor()
    {
        return iMajor;
    }
    
    /**
     * Gets the minor number (x.2.x.x).
     * 
     * @return the minor number (e.g. 2)
     */
    public int getMinor()
    {
        return iMinor;
    }
    
    /**
     * Gets the build number (x.x.3.x).
     * 
     * @return the build number (e.g. 3)
     */
    public int getBuild()
    {
        return iBuild;
    }
    
    /**
     * Gets the revision number (x.x.x.4).
     * 
     * @return the revision number (e.g. 4)
     */
    public int getRevision()
    {
        return iRevision;
    }
    
    /**
     * Gets whether the current (this) version is smaller than the given version.
     * 
     * @param pVersion the "greater" version
     * @return <code>true</code> if this version is smaller than the given, <code>false</code> otherwise
     */
    public boolean isSmaller(Version pVersion)
    {
        return compareTo(pVersion) < 0;
    }

    /**
     * Gets whether the current (this) version is smaller or equal than the given version.
     * 
     * @param pVersion the smaller or equal version
     * @return <code>true</code> if this version is smaller than the given, <code>false</code> otherwise
     */
    public boolean isSmallerOrEqual(Version pVersion)
    {
        return compareTo(pVersion) <= 0;
    }
    
    /**
     * Gets whether the current (this) version is greater than the given version.
     * 
     * @param pVersion the "smaller" version
     * @return <code>true</code> if this version is greater than the given, <code>false</code> otherwise
     */
    public boolean isGreater(Version pVersion)
    {
        return compareTo(pVersion) > 0;
    }

    /**
     * Gets whether the current (this) version is greater or equal than the given version.
     * 
     * @param pVersion the greater or equal version
     * @return <code>true</code> if this version is greater than the given, <code>false</code> otherwise
     */
    public boolean isGreaterOrEqual(Version pVersion)
    {
        return compareTo(pVersion) >= 0;
    }
    
    /**
     * Gets whether this version number is a valid version number. A version number is valid if at least
     * one version information is greater or equals 0.
     * 
     * @return <code>true</code> if version number is valid, <code>false</code> otherwise
     */
    public boolean isValid()
    {
        return iMajor >= 0 || iMinor >= 0 || iBuild >= 0 || iRevision >= 0; 
    }
    
}   // Version
