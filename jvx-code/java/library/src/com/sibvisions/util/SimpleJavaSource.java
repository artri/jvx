/*
 * Copyright 2017 SIB Visions GmbH
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
 * 26.10.2017 - [JR] - creation [VisionX backport]
 */
package com.sibvisions.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sibvisions.util.type.StringUtil;

/**
 * The <code>SimpleJavaSource</code> interprets Java Source.
 * 
 * @author Martin Handsteiner
 */
public class SimpleJavaSource
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** Constant for constructor. */
	public static final String CONSTRUCTOR = "<init>";
	/** Constant for constructor of derived classes. */
	public static final String DERIVED_CONSTRUCTORS = "<init-derived>";

    /** Constant for constructor of derived classes. */
    private static final Set<String> IGNORED_TAGS = new HashSet<String>();
    /** The map with default values. */ 
    private static final HashMap<String, Object> DEFAULT_VALUES = new HashMap<String, Object>();
    /** The operator indexes. */
    private static final List<Set<String>> CALCULATE_OPERATOR_PRIORITY = new ArrayList<Set<String>>();
    
	/** The configured allowed methods. */
	private List<String> allowedMethods = null;
	/** The configured denied methods. */
	private List<String> deniedMethods = null;
	
	/** Internal parsed allowed methods. */
	private Map<Class, Set<String>> internAllowedMethods = null;
	/** Internal parsed denied methods. */
	private Map<Class, Set<String>> internDeniedMethods = null;
	
	/** Cached allowed methods for each class. */
	private Map<Class, Set<String>> cachedAllowedMethods = null;
	/** Cached denied methods for each class. */
	private Map<Class, Set<String>> cachedDeniedMethods = null;
	
	/** Static temporary string builder, to avoid heap consumption. */
	protected StringBuilder tempBuilder = new StringBuilder();

	/** The Name for constants. */
	protected List<Class<?>> classesWithConstants = new ArrayList<Class<?>>();

	/** The class name of this instance. 
	 *  The default package of this class name is used as implicit import. */
	protected String className = null;
	/** The super class. */
	protected String superClassName = null;
	/** The interfaces. */
	protected String interfaceNames = null;
	
	/** Imports to find Java Classes. */
	protected ArrayList<String> imports = new ArrayList<String>();
	/** Real Imports without default package. */
	protected List<String> cachedRealImports = null;

	/** Class forName. */
	protected Map<Class<?>, List<String>> ignoreProperties = new HashMap<Class<?>, List<String>>();

	/** Imports to find Java Classes. */
	protected Map<String, String> archiveClassListMap = new HashMap<String, String>();

	/** Class forName. */
	protected Map<String, Class<?>> simpleClassNameClasses = new HashMap<String, Class<?>>();

	/** All known instances. */
	protected Map<String, Object> instances = new HashMap<String, Object>();

	/** The Name for any instance. */
	protected Map<Object, String> instanceNames = new IdentityHashMap<Object, String>();

	/** The classloader for class loading. */
	protected ClassLoader classLoader = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        IGNORED_TAGS.add("abstract");
        IGNORED_TAGS.add("final");
        IGNORED_TAGS.add("native");
        IGNORED_TAGS.add("private");
        IGNORED_TAGS.add("protected");
        IGNORED_TAGS.add("public");
        IGNORED_TAGS.add("static");
        IGNORED_TAGS.add("volatile");
        IGNORED_TAGS.add("transient");
        IGNORED_TAGS.add("synchronized");

        DEFAULT_VALUES.put("byte",    Byte.valueOf((byte)0));
        DEFAULT_VALUES.put("short",   Short.valueOf((short)0));
        DEFAULT_VALUES.put("int",     Integer.valueOf(0));
        DEFAULT_VALUES.put("long",    Long.valueOf(0));
        DEFAULT_VALUES.put("float",   Float.valueOf(0));
        DEFAULT_VALUES.put("double",  Double.valueOf(0));
        DEFAULT_VALUES.put("char",    Character.valueOf((char)0));
        DEFAULT_VALUES.put("boolean", Boolean.FALSE);
        
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("*", "/", "%")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("+", "-")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("<<", ">>", ">>>")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("<", ">", "<=", ">=", "instanceof")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("==", "!=")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("&")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("^")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("|")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("&&")));
        CALCULATE_OPERATOR_PRIORITY.add(new HashSet<String>(Arrays.asList("||")));
    }
    
	/**
	 * Constructs a new <code>SimpleJavaSource</code>.
	 */
	public SimpleJavaSource()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Gets the class loader.
	 * 
	 * @return the class loader.
	 */
	public ClassLoader getClassLoader()
	{
		return classLoader;
	}
	
	/**
	 * Sets the class loader.
	 * 
	 * @param pClassLoader the class loader.
	 */
	public void setClassLoader(ClassLoader pClassLoader)
	{
		if (classLoader != pClassLoader)
		{
			classLoader = pClassLoader;
			
			simpleClassNameClasses.clear();
			cachedRealImports = null;
			if (cachedAllowedMethods != null)
			{
				cachedAllowedMethods.clear();
			}
			if (cachedDeniedMethods != null)
			{
				cachedDeniedMethods.clear();
			}
		}
	}
	
	/**
	 * Gets the class name.
	 * 
	 * @return the class name.
	 */
	public String getClassName()
	{
		return className;
	}
	
	/**
	 * Sets the class name.
	 * 
	 * @param pClassName the class name.
	 */
	public void setClassName(String pClassName)
	{
		className = pClassName;
		
		addImport(getDefaultPackage());
	}
	
	/**
	 * Gets the default package of this class name.
	 * @return the default package of this class name.
	 */
	public String getDefaultPackage()
	{
		if (className != null)
		{
			int index = className.lastIndexOf('.');
			if (index >= 0)
			{
				return className.substring(0, index) + ".*";
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the super class name.
	 * 
	 * @return the super class name.
	 */
	public String getSuperClassName()
	{
		return superClassName;
	}
	
	/**
	 * Sets the super class name.
	 * 
	 * @param pSuperClassName the super class name.
	 */
	public void setSuperClassName(String pSuperClassName)
	{
		superClassName = pSuperClassName;
		
		try
		{
			Class<?> superClass = Class.forName(pSuperClassName, true, getClassLoader());
			
			while (superClass != null)
			{
				addImport(superClass.getName() + ".*");
				
				for (Class<?> interf : superClass.getInterfaces())
				{
					addImport(interf.getName() + ".*");
				}
				superClass = superClass.getSuperclass();
			}
		}
		catch (Exception ex)
		{
			addImport(pSuperClassName + ".*");
		}
	}
	
	/**
	 * Gets the interface names.
	 * 
	 * @return the interface names.
	 */
	public String getInterfaceNames()
	{
		return interfaceNames;
	}
	
	/**
	 * Sets the interface names.
	 * 
	 * @param pInterfaceNames the interface names.
	 */
	public void setInterfaceNames(String pInterfaceNames)
	{
		interfaceNames = pInterfaceNames;
		
		List<String> interfaces = StringUtil.separateList(pInterfaceNames, ",", true);
		
		for (String interf : interfaces)
		{
			addImport(interf + ".*");
		}
	}
	
	/**
	 * Gets the archive class list to find classes with simple class name without class loader.
	 * 
	 * @return the archive class list.
	 */
	public List<String> getArchiveClassList()
	{
		return new ArrayList(archiveClassListMap.values());
	}
	
	/**
	 * Sets the archive class list to find classes with simple class name without class loader.
	 * 
	 * @param pArchiveClassList the archive class list.
	 */
	public void setArchiveClassList(List<String> pArchiveClassList)
	{
		archiveClassListMap.clear();
		simpleClassNameClasses.clear();
		if (cachedAllowedMethods != null)
		{
			cachedAllowedMethods.clear();
		}
		if (cachedDeniedMethods != null)
		{
			cachedDeniedMethods.clear();
		}

		if (pArchiveClassList != null)
		{
			for (String clName : pArchiveClassList)
			{
				archiveClassListMap.put(clName.replace('$', '.'), clName);
			}
		}
	}
	
	/**
	 * Adds class to constant classes.
	 * 
	 * @param pClass the class to add.
	 */
	public void addClassWithConstants(Class<?> pClass)
	{
		if (!classesWithConstants.contains(pClass))
		{
			classesWithConstants.add(pClass);
		}
	}
	
	/**
	 * Removes class from constant classes.
	 * 
	 * @param pClass the class to remove.
	 */
	public void removeClassWithConstants(Class<?> pClass)
	{
		classesWithConstants.remove(pClass);
	}
	
	/**
	 * Gets all classes from constant classes.
	 * 
	 * @return pClass the class to remove.
	 */
	public Class<?>[] getClassesWithConstants()
	{
		return classesWithConstants.toArray(new Class[classesWithConstants.size()]);
	}
	
	//----------------------------------------------------------------
	// Import and Field Management
	//----------------------------------------------------------------
	
	/**
	 * Adds an import statement to find simple class names.
	 * 
	 * @param pImport the import.
	 */
	public void addImport(String pImport)
	{
		if (StringUtil.isEmpty(pImport))
		{
			return;
		}

		pImport = pImport.replace('$', '.');
		
        if (pImport.startsWith("java.lang.") && pImport.indexOf('.', 10) < 0) // avoid import of java.lang Classes
        {
            return;
        }
        else if (pImport.startsWith("[")) // avoid import of array classes
        {
            return;
        }
            
		for (int i = imports.size() - 1; i >= 0; i--)
		{
			String imp = imports.get(i);

			if (imp.endsWith(".*"))
			{
				String importPackage = imp.substring(0, imp.length() - 1);
				
				if (pImport.startsWith(importPackage) && pImport.indexOf('.', importPackage.length()) < 0)
				{
					return;
				}
			}
			else if (imp.equals(pImport))
			{
				return;
			}
		}

		if (pImport.endsWith(".*"))
		{
			String importPackage = pImport.substring(0, pImport.length() - 1);
			
			for (int i = imports.size() - 1; i >= 0; i--)
			{
				String imp = imports.get(i);

				if (imp.startsWith(importPackage) && imp.indexOf('.', importPackage.length()) < 0)
				{
					imports.remove(i);
				}
			}
		}
			
		simpleClassNameClasses.clear();
		cachedRealImports = null;
		
		imports.add(pImport);
	}
	
	/**
	 * Removes an import statement.
	 * 
	 * @param pImport the class.
	 */
	public void removeImport(String pImport)
	{
		if (StringUtil.isEmpty(pImport))
		{
			return;
		}
		
		if (pImport.endsWith(".*"))
		{
			String importPackage = pImport.substring(0, pImport.length() - 2);
			
			for (int i = imports.size() - 1; i >= 0; i--)
			{
				String imp = imports.get(i);
				
				if (imp.startsWith(importPackage) && imp.indexOf('.', importPackage.length()) < 0)
				{
					simpleClassNameClasses.clear();
					
					imports.remove(i);
				}
			}
		}
		
		simpleClassNameClasses.clear();
		cachedRealImports = null;
		
		imports.remove(pImport);
	}

	/**
	 * Removes all import statement.
	 */
	public void removeAllImports()
	{
		simpleClassNameClasses.clear();

		imports.clear();
		cachedRealImports = null;
	}

	/**
	 * Gets all import statements.
	 * 
	 * @return all import statements.
	 */
	public List<String> getImports()
	{
		if (cachedRealImports == null)
		{
			cachedRealImports = (List<String>)imports.clone();
			
			cachedRealImports.remove(getDefaultPackage());
			
			List<String> interfaces = StringUtil.separateList(interfaceNames, ",", true);
			
			for (String interf : interfaces)
			{
				cachedRealImports.remove(interf + ".*");
			}
	
			try
			{
				Class<?> superClass = Class.forName(superClassName, true, getClassLoader());
				while (superClass != null)
				{
					cachedRealImports.remove(superClass.getName() + ".*");
					
					for (Class<?> interf : superClass.getInterfaces())
					{
						cachedRealImports.remove(interf.getName() + ".*");
					}
					superClass = superClass.getSuperclass();
				}
			}
			catch (Exception ex)
			{
				cachedRealImports.remove(superClassName + ".*");
			}
		}

		return cachedRealImports;
	}

	/**
	 * Gets the properties to ignore.
	 * @param pSuperClass the super class
	 * @return the properties to ignore.
	 */
	public List<String> getIgnorePropertyDefinition(Class<?> pSuperClass)
	{
		return ignoreProperties.get(pSuperClass);
	}
	
	/**
	 * Gets the properties to ignore.
	 * @param pSuperClass the super class
	 * @param pIgnoreProperties the properties to ignore.
	 */
	public void setIgnorePropertyDefinition(Class<?> pSuperClass, List<String> pIgnoreProperties)
	{
		ignoreProperties.put(pSuperClass, pIgnoreProperties);
	}
	
	/**
	 * True, if the given property has to be ignored.
	 * @param pInstance the instance.
	 * @param pPropertyName the property.
	 * @return True, if the given property has to be ignored.
	 */
	public boolean isPropertyIgnored(Object pInstance, String pPropertyName)
	{
		for (Map.Entry<Class<?>, List<String>> propertyDefinition : ignoreProperties.entrySet())
		{
			if (propertyDefinition.getKey().isInstance(pInstance))
			{
				if (propertyDefinition.getValue().contains(pPropertyName))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * True, if the given property is detected as set with isXxxSet.
	 * @param pInstance the instance.
	 * @param pPropertyName the property.
	 * @return True, if the given property is detected as set with isXxxSet.
	 */
	public boolean isPropertySet(Object pInstance, String pPropertyName)
	{
		try
		{
			if (!((Boolean)pInstance.getClass().getMethod(StringUtil.formatMethodName("is", pPropertyName + "Set")).
					invoke(pInstance)).booleanValue())
			{
				return false;
			}
		}
		catch (Exception ex)
		{
			// Ignore, as it is not an isXxxSet property
		}
		return true;
	}
	
	/**
	 * Gets the instance for a specific field.
	 * 
	 * @param pFieldName the field name.
	 * @return the instance.
	 */
	public Object getFieldValue(String pFieldName)
	{
		return instances.get(pFieldName);
	}
	
	/**
	 * Sets the instance for a specific field.
	 * 
	 * @param pFieldName the field name.
	 * @param pInstance the instance.
	 */
	public void setFieldValue(String pFieldName, Object pInstance)
	{
	    Object oldInstance = instances.put(pFieldName, pInstance);
		
        if (oldInstance != null && pFieldName.equals(instanceNames.get(oldInstance)))
        {
            instanceNames.remove(oldInstance);
        }
		if (pInstance != null && !instanceNames.containsKey(pInstance))
		{
		    instanceNames.put(pInstance, pFieldName);
		}
	}
	
	/**
	 * Gets the name for a instance.
	 * 
	 * @param pInstance the field name.
	 * @return the instance.
	 */
	public String getFieldName(Object pInstance)
	{
		return instanceNames.get(pInstance);
	}
	
	/**
	 * Removes the specific field.
	 * 
	 * @param pFieldName the field name.
	 */
	public void removeField(String pFieldName)
	{
		instanceNames.remove(instances.remove(pFieldName));
	}
	
	/**
	 * Gets all fields.
	 * 
	 * @return all fields.
	 */
	public String[] getFieldNames()
	{
		return instances.keySet().toArray(new String[instances.size()]);
	}
	
	//----------------------------------------------------------------
	// Source Interpreter/Parser
	//----------------------------------------------------------------
		
	/**
	 * Executes any java statement.
	 * 
	 * @param pSource the source.
	 * @return the result.
	 */
	public Object execute(String pSource)
	{
		if (pSource == null || pSource.length() == 0)
		{
			return null;
		}
		else
		{
			List<Object> tempTags = new ArrayList<Object>();
			
			parseTags(pSource, tempTags);
			
			ParseInfo info = new ParseInfo(tempTags);

			try
			{
			    return parseStatementWithOperators(info);
            }
            catch (Exception ex)
            {
                ParseInfo inf = info;
                
                while (inf.curSub != null)
                {
                    inf = inf.curSub;
                }

                throw new ExecuteException(pSource, inf.curTag.tag, inf.curTag.pos, ex);
            }
		}
	}

    /**
     * Executes any java statement.
     * 
     * @param pSource the source.
     * @return the result.
     */
    public Object executeScript(String pSource)
    {
        if (pSource == null || pSource.length() == 0)
        {
            return null;
        }
        else
        {
            List<Object> tempTags = new ArrayList<Object>();
            
            parseTags(pSource, tempTags);
            
            ParseInfo info = new ParseInfo(tempTags);
            
            try
            {
                return parseAllStatementsWithImports(info);
            }
            catch (Exception ex)
            {
                ParseInfo inf = info;
                while (inf.curSub != null)
                {
                    inf = inf.curSub;
                }
                throw new ExecuteException(pSource, inf.curTag.tag, inf.curTag.pos, ex);
            }
        }
    }

	/**
	 * Gets the class from a simple class name.
	 * 
	 * @param pCanonicalClassName the simple class name.
	 * @return the full qualified class name, if found in archive class list.
	 */
	private String getArchiveClassListName(String pCanonicalClassName)
	{
		return archiveClassListMap.get(pCanonicalClassName);
	}

	/**
	 * Gets the class from a simple class name.
	 * 
	 * @param pSimpleClassName the simple class name.
	 * @return the full qualified class name, if found in archive class list.
	 */
	private String findClassInArchiveClassList(String pSimpleClassName)
	{
		for (int i = 0, size = imports.size(); i < size; i++)
		{
			String imp = imports.get(i);
			
			if (imp.equals(pSimpleClassName))
			{
				return pSimpleClassName;
			}
			else if (imp.length() > pSimpleClassName.length()
					&& imp.charAt(imp.length() - pSimpleClassName.length() - 1) == '.'
					&& imp.endsWith(pSimpleClassName))
			{
				String clName = getArchiveClassListName(imp);
				if (clName == null)
				{
					return imp;
				}
				else
				{
					return clName;
				}
			}
		}
		
		String tempName = "java.lang." + pSimpleClassName;
		if (archiveClassListMap.containsKey(tempName))
		{
			return tempName;
		}
		
		for (int i = 0, size = imports.size(); i < size; i++)
		{
			String imp = imports.get(i);
			
			if (imp.endsWith("*"))
			{
				tempName = imp.substring(0, imp.length() - 1) + pSimpleClassName;
				String clName = getArchiveClassListName(tempName);
				if (clName != null)
				{
					return clName;
				}
			}
		}
		
		return pSimpleClassName;
	}

	/**
	 * Gets the class from a simple class name.
	 * 
	 * @param pClassName the simple class name.
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @param pCallOrField true, if a call or field follows.
	 * @return the class.
	 */
	private Class<?> getClassByName(String pClassName, ParseInfo pInfo, boolean pCallOrField)
	{
		boolean simpleClassName = true;
		boolean subClass = false;
		
		tempBuilder.setLength(0);
		tempBuilder.append(pClassName);

		int offset = pCallOrField ? 2 : 0;
		
		while (pInfo.size() > offset && ".".equals(pInfo.getTag(offset)))
		{
			if (!subClass)
			{
				try
				{
					tempBuilder.replace(0, tempBuilder.length(), getClassByName(tempBuilder.toString(), simpleClassName).getName());
					subClass = true;
				}
				catch (Exception ex)
				{
					// Do Nothing
				}
			}
			simpleClassName = false;

			if (pCallOrField && pInfo.size() > 1)
			{
                if ("class".equals(pInfo.getTag(1)))
                {
                    return getClassByName(tempBuilder.toString());
                }
                else 
                {
                    try
                    {
                        Class fieldCheckClass = getClassByName(tempBuilder.toString());
                        fieldCheckClass.getField(pInfo.getTag(1));
                        
                        return fieldCheckClass;
                    }
                    catch (Throwable ex)
                    {
                        // Ignore
                    }
                }
			}

			pInfo.skip();
			tempBuilder.append(subClass ? '$' : '.');
			tempBuilder.append(pInfo.removeTag());
		}
		
		if (!simpleClassName)
		{
			pClassName = tempBuilder.toString();
		}
		
		return getClassByName(pClassName, simpleClassName);
	}
    
	/**
	 * Gets the class for the given class name.
	 * 
	 * @param pClassName the class name
	 * @param pSimpleClassName <code>true</code> if the name is a simple class name, <code>false</code> otherwise
	 * @return the class or <code>null</code> if no class with given name was found
	 */
    public Class<?> getClassByName(String pClassName, boolean pSimpleClassName)
	{
        Class<?> clazz = simpleClassNameClasses.get(pClassName);
        
        if (clazz == null)
        {
            ClassLoader clLoader;
            
            if (classLoader == null)
            {
                clLoader = getClass().getClassLoader();
            }
            else
            {
                clLoader = classLoader;
            }
            String tempClassName;
            if (pSimpleClassName)
            {
                tempClassName = findClassInArchiveClassList(pClassName);
            }
            else
            {
                tempClassName = getArchiveClassListName(pClassName);
                if (tempClassName == null)
                {
                    tempClassName = pClassName;
                }
            }
            try
            {
                clazz = Class.forName(tempClassName, true, clLoader);
            }
            catch (Throwable th1)
            {
                if (pSimpleClassName)
                {
                    tempClassName = "java.lang." + pClassName;
                    try
                    {
                        clazz = Class.forName(tempClassName, true, clLoader);
                        
                        archiveClassListMap.put(tempClassName.replace('$',  '.'), tempClassName);
                    }
                    catch (Throwable th2)
                    {
                        for (int i = 0, size = imports.size(); i < size && clazz == null; i++)
                        {
                            String imp = imports.get(i);
                            
                            if (imp.endsWith("*"))
                            {
	                            try
	                            {
	                            	String clName = imp.substring(0, imp.length() - 2);
                                    tempClassName = getArchiveClassListName(clName);
                                    if (tempClassName == null)
                                    {
                                        tempClassName = clName;
                                    }
	                            	char classSeparator;
	                            	try
	                            	{
	                            		Class.forName(tempClassName, true, clLoader);
	                            		classSeparator = '$';
		                            }
		                            catch (Throwable th3)
		                            {
		                            	classSeparator = '.';
		                            }
	                            	
	                            	tempClassName = tempClassName + classSeparator + pClassName;
                                    
                                    clazz = Class.forName(tempClassName, true, clLoader);
                                    
                                    archiveClassListMap.put(tempClassName.replace('$',  '.'), tempClassName);
	                            }
	                            catch (Throwable th3)
	                            {
	                                // Class not found in this package.
	                            }
                            }
                        }
                    }
                }
            }
            if (clazz == null)
            {
                if (Integer.TYPE.getName().equals(pClassName))
                {
                    clazz = Integer.TYPE;
                }
                else if (Boolean.TYPE.getName().equals(pClassName))
                {
                    clazz = Boolean.TYPE;
                }
                else if (Long.TYPE.getName().equals(pClassName))
                {
                    clazz = Long.TYPE;
                }
                else if (Double.TYPE.getName().equals(pClassName))
                {
                    clazz = Double.TYPE;
                }
                else if (Float.TYPE.getName().equals(pClassName))
                {
                    clazz = Float.TYPE;
                }
                else if (Character.TYPE.getName().equals(pClassName))
                {
                    clazz = Character.TYPE;
                }
                else if (Byte.TYPE.getName().equals(pClassName))
                {
                    clazz = Byte.TYPE;
                }
                else
                {
                    throw new IllegalArgumentException("Class or field " + pClassName + " not found!");
                }
            }

            simpleClassNameClasses.put(pClassName, clazz);
        }
        
        return clazz;
	}
    
    /**
     * Gets the class for the given name.
     * 
     * @param pClassName the full qualified class name
     * @return the class or <code>null</code> if no class with given name was found
     */
    public Class<?> getClassByName(String pClassName)
    {
        return getClassByName(pClassName, false);
    }
	
	/**
	 * Gets the array class with the given dimension.
	 * 
	 * @param pComponentType the simple class name.
	 * @param pDimension the array dimension.
	 * @return the class.
	 */
	public Class<?> getComponentType(Class<?> pComponentType, int pDimension)
	{
		if (pDimension <= 0)
		{
			return pComponentType;
		}
		else
		{
			return Array.newInstance(pComponentType, new int[pDimension]).getClass();
		}
	}
	
	/**
     * Adds the tag to the list, if it is not an ignored tag.
     * 
     * @param pTags the list
     * @param pTag the tag
     */
    private void addToList(List<Object> pTags, Tag pTag)
    {
        if (!IGNORED_TAGS.contains(pTag.tag))
        {
            pTags.add(pTag);
        }
    }
    
    /**
	 * Parses all tags.
	 * 
	 * @param pSource source
	 * @param pTags tags
	 */
	private void parseTags(String pSource, List<Object> pTags)
	{
		List<List<Object>> hierachy = new ArrayList<List<Object>>();
		List<Integer>      hierachyReason = new ArrayList<Integer>();
		
		List<Object> current = pTags;

		int length = pSource.length();
		
		char ch = 0;
		char oldCh;
		
		boolean string = false;
		boolean character = false;
		boolean comment = false;
		boolean lineComment = false;
		int pos = 0;
			
		for (int i = 0; i < length; i++)
		{
			oldCh = ch;
			ch = pSource.charAt(i);
			
			if (comment)
			{
				if (oldCh == '*' && ch == '/')
				{
					comment = false;

					pos = i + 1;
				}
			}
			else if (lineComment)
			{
				if (ch == '\n')
				{
					comment = false;

					pos = i + 1;
				}
			}
			else if (string)
			{
				if (ch == '"' && oldCh != '\\')
				{
					string = false;
				}
			}
			else if (character)
			{
				if (ch == '\'' && oldCh != '\\')
				{
					character = false;
				}
			}
			else if (!Character.isJavaIdentifierPart(ch))
			{
				if (oldCh == '/' && ch == '*')
				{
					comment = true;

					current.remove(current.size() - 1);
				}
				else if (oldCh == '/' && ch == '/')
				{
					comment = true;

					current.remove(current.size() - 1);
				}
				else if ("-+<>=&|".indexOf(ch) >= 0 && "!=<>+-*/%&^|~".indexOf(oldCh) >= 0)
				{
				    Tag.appendChar(current, ch);

					pos = i + 1;
				}
				else
				{
					if (i > pos)
					{
                        addToList(current, new Tag(pSource, pos, i));
						
						pos = i;
					}
					
					if (ch == '"')
					{
						string = true;
					}
					else if (ch == '\'')
					{
						character = true;
					}
					else
					{
						if (ch == '(' || ch == '{')
						{
							List<Object> newCurrent = new ArrayList<Object>();
							
							current.add(newCurrent);
							
							hierachy.add(current);
							hierachyReason.add(Integer.valueOf(i));
							
							current = newCurrent;
						}
						else if (ch == ')' || ch == '}')
						{
						    int hierachySize = hierachy.size(); 
							if (hierachySize == 0)
							{
								throw new IllegalArgumentException("Found bracket close '" + ch + "' (pos=" + i + ") without corresponding bracket open!");
							}
							else
							{
							    hierachySize--;
							    int reasonIndex = hierachyReason.remove(hierachySize).intValue();
							    char reasonCh = pSource.charAt(reasonIndex);
							    if ((reasonCh == '(' && ch != ')') || (reasonCh == '{' && ch != '}'))
							    {
	                                throw new IllegalArgumentException("Found bracket close '" + ch + "' (pos=" + i + 
	                                        ") not corresponding with bracket open '" + reasonCh + "' (pos=" + reasonIndex + ")!");
							    }
								current = hierachy.remove(hierachySize);
							}
						}
						else if (!Character.isWhitespace(ch))
						{
                            addToList(current, new Tag(pSource, pos, pos + 1));
						}
						pos++;
					}
				}
			}
		}
		int hierachySize = hierachy.size();
		if (hierachy.size() > 0)
		{
		    hierachySize--;
            int reasonIndex = hierachyReason.remove(hierachySize).intValue();
            char reasonCh = pSource.charAt(reasonIndex);

            throw new IllegalArgumentException("Missing bracket close for bracket open '" + reasonCh + "' (pos=" + reasonIndex + ")!");
		}
		else if (string)
		{
            throw new IllegalArgumentException("Missing string close for string open '\"' (pos=" + pos + ")!");
		}
        else if (character)
        {
            throw new IllegalArgumentException("Missing character close for character open '\'' (pos=" + pos + ")!");
        }
		// comment close will not be fired
//        else if (comment)
//        {
//            throw new IllegalArgumentException("Missing comment close for comment open '/*' (pos=" + pos + ")!");
//        }
//        else if (lineComment)
//        {
//            throw new IllegalArgumentException("Missing line comment close for line comment open '//' (pos=" + pos + ")!");
//        }
        if (length > pos)
        {
            addToList(current, new Tag(pSource, pos, length));
        }
	}

	/**
	 * Parses the string (\', \", \\, \f, \n, \r, \t, \u0000 .
	 * @param pString the source string.
	 * @return the parsed string.
	 */
	public String parseString(String pString)
	{
		int index = pString.indexOf('\\');
		if (index >= 0)
		{
			StringBuilder result = new StringBuilder(pString.length());

			int last = 0;
			int len = pString.length();

			while (index >= 0)
			{
				result.append(pString.substring(last, index));

				last = index + 1;
				if (last < len)
				{
					char ch = pString.charAt(last);
					
					int chIndex = "\'\"\\bfnrt".indexOf(ch);
					if (chIndex >= 0)
					{
						result.append("\'\"\\\b\f\n\r\t".charAt(chIndex));
						last++;
					}
					else if (ch == 'u')
					{
						int ui = last + 1;
						while (ui < len && pString.charAt(ui) == 'u')
						{
							ui++;
						}
						
						if (ui + 3 < len)
						{
							result.append((char)Integer.parseInt(pString.substring(ui, ui + 4), 16));
							
							last = ui + 4;
						}
						else
						{
							result.append('\\');
						}
					}
					else
					{
						result.append('\\');
					}
				}
				else
				{
					result.append('\\');
				}
				index = pString.indexOf('\\', last);
			}

			result.append(pString.substring(last));
			
			pString = result.toString();
		}
		
		return pString;
	}
	
	/**
	 * Parses one statement part.
	 * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @return the result.
	 */
	private Object parseStatement(ParseInfo pInfo)
	{
		Object tagOrSubTags = pInfo.removeTagOrSubTags();
		
		if (tagOrSubTags.getClass() != Tag.class)
		{
			List<Object> subTags = (List<Object>)tagOrSubTags;
			
			String tag = subTags.size() > 0 ? Tag.getTagString(subTags.get(0)) : null;
            boolean isPossibleCast = tag != null;
            if (isPossibleCast)
            {
                isPossibleCast = tag.length() > 0 && Character.isJavaIdentifierStart(tag.charAt(0));

                if (isPossibleCast)
                {
					try
					{
						ParseInfo testClassTags = new ParseInfo(pInfo, subTags);
						getClassByName(testClassTags.removeTag(), testClassTags, false);
				
						parseGenerics(pInfo);
				
                        return parseStatement(pInfo);
					}
					catch (Exception ex)
					{
                        // not a class
                    }
                }
			}

            Object result = parseStatementWithOperators(new ParseInfo(pInfo, subTags));

            return parseCallsOrFields(result, pInfo);
		}
		else
		{
		    pInfo.curTag = (Tag)tagOrSubTags;
			String tag = pInfo.curTag.tag;
			
            switch (tag)
			{
                case "new":
    				Class<?> clazz = getClassByName(pInfo.removeTag(), pInfo, false);
    			
    				parseGenerics(pInfo);
    				
    				if ("[".equals(pInfo.getTag()))
    				{
    					int[] dimensions = parseDimensions(pInfo);
    					
    					if (dimensions[0] < 0)
    					{
    					    return parseArrayData(new ParseInfo(pInfo, pInfo.removeSubTags()), dimensions.length, clazz);
    					}
    					else
    					{
    						int fixedDimension = 1;
    						
    						while (fixedDimension < dimensions.length && dimensions[fixedDimension] >= 0)
    						{
    							fixedDimension++;
    						}
    						
    						int[] fixed = new int[fixedDimension];
    						
    						System.arraycopy(dimensions, 0, fixed, 0, fixedDimension);
    						
    						return Array.newInstance(getComponentType(clazz, dimensions.length - fixedDimension), fixed);
    					}
    				}
    				else
    				{
    					checkMethodAllowed(clazz, CONSTRUCTOR);
    
    					Object[] parameters = (Object[])parseParameters(new ParseInfo(pInfo, pInfo.removeSubTags()), Object.class);
    					if (parameters.length == 1 && parameters[0] == null) // Reflective maps Object[] {null} to first Parameter is null.
    					{
    						parameters = null;
    					}
    
    					Object result;
    					try
    					{
    						result = Reflective.construct(clazz, parameters);
    					}
    		            catch (NoSuchMethodException pEx)
    		            {
    		                throw new IllegalArgumentException("Constructor " + clazz.getSimpleName() + "(...) does not exist with the given parameters!");
    		            }
    					catch (Throwable pThrowable)
    					{
    						throw new IllegalStateException("Creating a new instance of \"" + clazz.getSimpleName() + "\" failed!", pThrowable);
    					}
    				
    					return parseCallsOrFields(result, pInfo);
    				}
                case "null":
                    return null;
                case "true":
                    return Boolean.TRUE;
                case "false":
                    return Boolean.FALSE;
                case "-":
                    Object value = parseStatement(pInfo);
                    
                    if (value instanceof Integer)
                    {
                        return Integer.valueOf(-((Number)value).intValue());
                    }
                    else if (value instanceof Long)
                    {
                        return Long.valueOf(-((Number)value).longValue());
                    }
                    else if (value instanceof Float)
                    {
                        return Float.valueOf(-((Number)value).floatValue());
                    }
                    else if (value instanceof Double)
                    {
                        return Double.valueOf(-((Number)value).doubleValue());
                    }
                    else if (value instanceof Byte)
                    {
                        return Byte.valueOf((byte)-((Number)value).intValue());
                    }
                    else if (value instanceof Short)
                    {
                        return Short.valueOf((short)-((Number)value).intValue());
                    }
                    else
                    {
                        throw new IllegalArgumentException("Negative Sign can only be used on Numbers");
                    }
                case "+":
                    return parseStatement(pInfo);
                default:
                    if (tag.startsWith("\""))
                    {
                        return parseString(tag.substring(1, tag.length() - 1));
                    }
                    else if (tag.startsWith("\'"))
                    {
                        return parseString(tag.substring(1, tag.length() - 1));
                    }
                    else if ("0123456789.".indexOf(tag.charAt(0)) >= 0)
                    {
                        boolean isDoubleOrFloat = ".".equals(tag);
                        if (isDoubleOrFloat)
                        {
                            if (pInfo.hasTag())
                            {
                                tag = tag + pInfo.removeTag();
                            }
                        }
                        else
                        {
                            isDoubleOrFloat = pInfo.hasTag() && ".".equals(pInfo.getTag());
                            if (isDoubleOrFloat)
                            {
                                tag = tag + pInfo.removeTag();
                                if (pInfo.hasTag())
                                {
                                    String possibleTag = pInfo.getTag();
                                    if (Character.isDigit(possibleTag.charAt(0)) || possibleTag.startsWith("E") || possibleTag.startsWith("E")
                                            || "fFdD".contains(possibleTag))
                                    {
                                        tag = tag + possibleTag;
                                        pInfo.skip();
                                    }
                                }
                            }
                        }

                        if (tag.charAt(0) == '0' && tag.length() > 1 && !isDoubleOrFloat)
                        {
                            char ch = Character.toLowerCase(tag.charAt(1));
                            if (ch == 'x')
                            {
                                if (tag.endsWith("l") || tag.endsWith("L"))
                                {
                                    return Long.valueOf(tag.substring(2, tag.length() - 1).replace("_", ""), 16);
                                }
                                else
                                {
                                    return Integer.valueOf(Long.valueOf(tag.substring(2).replace("_", ""), 16).intValue());
                                }
                            }
                            else if (ch == 'b')
                            {
                                if (tag.endsWith("l") || tag.endsWith("L"))
                                {
                                    return Long.valueOf(tag.substring(2, tag.length() - 1).replace("_", ""), 2);
                                }
                                else
                                {
                                    return Integer.valueOf(tag.substring(2).replace("_", ""), 2);
                                }
                            }
                            else
                            {
                                if (tag.endsWith("l") || tag.endsWith("L"))
                                {
                                    return Long.valueOf(tag.substring(1, tag.length() - 1).replace("_", ""), 8);
                                }
                                else
                                {
                                    return Integer.valueOf(tag.substring(1).replace("_", ""), 8);
                                }
                            }
                        }
                        else if (tag.endsWith("f") || tag.endsWith("F"))
                        {
                            return Float.valueOf(tag.replace("_", ""));
                        }
                        else if (tag.endsWith("d") || tag.endsWith("D") || isDoubleOrFloat || tag.contains("e") || tag.contains("E"))
                        {
                            return Double.valueOf(tag.replace("_", ""));
                        }
                        else if (tag.endsWith("l") || tag.endsWith("L"))
                        {
                            return Long.valueOf(tag.substring(0, tag.length() - 1).replace("_", ""));
                        }
                        else
                        {
                            return Integer.valueOf(tag.replace("_", ""));
                        }
                    }
                    else if (pInfo.hasTag() && pInfo.getTagOrSubTags().getClass() != Tag.class) // function call to this
                    {
                        pInfo.back(); // consider tag
                        
                        Object instance = instances.get("this");
                        Object result = parseCallOrField(instance, instance.getClass(), pInfo);
                        
                        return parseCallsOrFields(result, pInfo);
                    }
                    else if (instances.containsKey(tag)) // existing field.
                    {
                        Object instance = instances.get(tag);
				
                        if (pInfo.hasTag() && "[".equals(pInfo.getTag()))
                        {
                            int[] dimensions = parseDimensions(pInfo);
					
                            for (int i = 0; i < dimensions.length; i++)
                            {
                                instance = Array.get(instance, dimensions[i]);
                            }
                        }
				
                        return parseCallsOrFields(instance, pInfo);
                    }
                    else if (!Character.isJavaIdentifierStart(tag.charAt(0)))
                    {
                        throw new IllegalArgumentException("Found " + tag + " where class, field or literal was expected!");
                    }
                    else
                    {
                        clazz = getClassByName(tag, pInfo, true);
                        
                        boolean hasTag = pInfo.hasTag();
                        String curTag = hasTag ? pInfo.getTag() : null;
                        if (hasTag && ".".equals(curTag))				
                        {
                            pInfo.skip(); // .
                            
                            Object result = parseCallOrField(null, clazz, pInfo);
	                
                            return parseCallsOrFields(result, pInfo);
                        }
                        else if (!hasTag)
                        {
                            return clazz;
                        }
                        else if (pInfo.size() == 1 && curTag != null)
                        {
                            setFieldValue(pInfo.removeTag(), DEFAULT_VALUES.get(tag));
                            
                            return null;
                        }
                        else
                        {
                            return parseSingleStatement(pInfo);
                        }
                    }
			}
		}
	}

	/**
	 * Parses one statement including operators.
	 * 
	 * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @return the result.
	 */
	private Object parseStatementWithOperators(ParseInfo pInfo)
	{
		List<Object> operands = new ArrayList<Object>();
		List<Tag> operators = new ArrayList<Tag>();
		
		operands.add(parseStatement(pInfo));
		
		while (pInfo.hasTag() && !",;?:".contains(pInfo.getTag()))
		{
			Object tag = pInfo.removeTagOrSubTags();
			
			if (tag.getClass() == Tag.class)
			{
				operators.add((Tag)tag);

				try
				{
				    operands.add(parseStatement(pInfo));
				}
				catch (RuntimeException ex)
				{
				    String tagStr = ((Tag)tag).tag;
				
				    for (Set<String> calculateOperator : CALCULATE_OPERATOR_PRIORITY)
			        {
				        if (calculateOperator.contains(tagStr))
		                {
				            throw ex;
		                }
			        }
				    
				    pInfo.curTag = operators.get(0);
		            throw new IllegalArgumentException("Operator " + operators.get(0).tag + " is not supported!");
				}
			}
			else
			{
				throw new IllegalArgumentException("Expected operator not object!");
			}
		}
		
        for (Set<String> calculateOperator : CALCULATE_OPERATOR_PRIORITY)
		{
			for (int i = 0; i < operators.size(); i++)
			{
                if (calculateOperator.contains(operators.get(i).tag))
				{
					operands.set(i, calculate(operands.get(i), operands.remove(i + 1), operators.remove(i).tag));
					i--;
				}
			}
	    }
        
        if (operators.size() > 0)
        {
            pInfo.curTag = operators.get(0);
            throw new IllegalArgumentException("Operator " + operators.get(0).tag + " is not supported!");
	    }
		
		Object result = operands.get(0);
		
		if (pInfo.hasTag() && "?".equals(pInfo.getTag())) 
		{
		    // Maybe check that result has to be a boolean
		    pInfo.skip(); // ?

		    if (((Boolean)result).booleanValue()) // try a kind of shortcut
		    {
		        result = parseStatementWithOperators(pInfo);
		        
	            if (pInfo.hasTag() && ":".equals(pInfo.getTag()))
	            {
	                pInfo.skip(); // :

	                while (pInfo.hasTag() && !",;".contains(pInfo.getTag()))
	                {
	                    pInfo.skip();
	                }
	            }
	            else
	            {
	                throw new IllegalArgumentException("Operator : is missing after operator ?");
	            }
		    }
		    else
		    {
                while (pInfo.hasTag() && !":,;".contains(pInfo.getTag()))
                {
                    pInfo.skip();
                }
                if (pInfo.hasTag() && ":".equals(pInfo.getTag()))
                {
                    pInfo.skip(); // :
                    
                    result = parseStatementWithOperators(pInfo);
                }
                else
                {
                    throw new IllegalArgumentException("Operator : is missing after operator ?");
                }
		    }
		}
		
		return result;
	}
	
    /**
     * Skips a single statement and returns the skipped tags.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the skipped tags.
     */
    private ParseInfo skipSingleStatement(ParseInfo pInfo)
    {
        ParseInfo result = new ParseInfo(pInfo, false);

        String tag = pInfo.getTag();
        if (tag == null)
        {
            pInfo.skip();
        }
        else if ("if".equals(tag))
        {
            pInfo.skip(); // if
            pInfo.skip(); // condition

            skipSingleStatement(pInfo);
            
            if (pInfo.hasTag() && "else".equals(pInfo.getTag()))
            {
                pInfo.skip(); // else
                
                skipSingleStatement(pInfo);
            } 
        }
        else if ("while".equals(tag))
        {
            pInfo.skip(); // while
            pInfo.skip(); // condition
            
            skipSingleStatement(pInfo);
        }
        else if ("do".equals(tag))
        {
            pInfo.skip(); // do

            skipSingleStatement(pInfo);
            
            if (!pInfo.hasTag() || !"while".equals(pInfo.getTag()))
            {
                throw new IllegalArgumentException("Operator while is missing after operator do");
            }
            
            pInfo.skip(); // while
            pInfo.skip(); // condition
        }
        else if ("for".equals(tag))
        {
            pInfo.skip(); // for
            pInfo.skip(); // condition
            
            skipSingleStatement(pInfo);
        }
        else
        {
            while (pInfo.hasTag() && !";".equals(pInfo.getTag()))
            {
                pInfo.skip();
            }
        }
        
        result.size = pInfo.pos;

        if (pInfo.hasTag() && ";".equals(pInfo.getTag()))
        {
            pInfo.skip();
        }

        return result;
    }
    
    /**
     * Skips a single statement and returns the skipped tags.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the skipped tags.
     */
    private ParseInfo skipSimpleStatement(ParseInfo pInfo)
    {
        ParseInfo result = new ParseInfo(pInfo, false);

        while (pInfo.hasTag() && !";".equals(pInfo.getTag()))
        {
            pInfo.skip();
        }

        result.size = pInfo.pos;
        
        if (pInfo.hasTag() && ";".equals(pInfo.getTag()))
        {
            pInfo.skip();
        }

        return result;
    }

    /**
     * Parses a single statement.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the result.
     */
    private Object parseSingleStatement(ParseInfo pInfo)
    {
        Object tagOrSubTags = pInfo.getTagOrSubTags();
        if (tagOrSubTags.getClass() != Tag.class)
        {
            pInfo.skip();
            return parseAllStatements(new ParseInfo(pInfo, (List<Object>)tagOrSubTags));
        }
        else
        {
            pInfo.curTag = (Tag)tagOrSubTags;
            String tag = pInfo.curTag.tag;
            Object result = null;
            
            switch (tag)
            {
                case "if":
                    pInfo.skip(); // if
            
                    Boolean condition = (Boolean)parseStatementWithOperators(new ParseInfo(pInfo, pInfo.removeSubTags()));

                    ParseInfo ifTags = skipSingleStatement(pInfo);
                    
                    if (condition.booleanValue())
                    {
                        result = parseSingleStatement(ifTags);
                    }

                    if (pInfo.hasTag() && "else".equals(pInfo.getTag()))
                    {
                        pInfo.skip(); // else
                        
                        ParseInfo elseTags = skipSingleStatement(pInfo);

                        if (!condition.booleanValue())
                        {
                            result = parseSingleStatement(elseTags);
                        }
                    } 
                    break;
                case "while":
                    pInfo.skip(); // while
                    
                    ParseInfo conditionTags = new ParseInfo(pInfo, pInfo.removeSubTags());
                    ParseInfo whileTags = skipSingleStatement(pInfo);
                    
                    while (((Boolean)parseStatementWithOperators(new ParseInfo(conditionTags, true))).booleanValue())
                    {
                        result = parseSingleStatement(new ParseInfo(whileTags, true));
                    }
                    break;
                case "do":
                    pInfo.skip(); // do
                    
                    ParseInfo doTags = skipSingleStatement(pInfo);
                    
                    if (!pInfo.hasTag() || !"while".equals(pInfo.getTag()))
                    {
                        throw new IllegalArgumentException("Operator while is missing after operator do");
                    }
                    
                    pInfo.skip(); // while
                    
                    conditionTags = new ParseInfo(pInfo, pInfo.removeSubTags());
        
                    do
                    {
                        result = parseSingleStatement(new ParseInfo(doTags, true));
                    }
                    while (((Boolean)parseStatementWithOperators(new ParseInfo(conditionTags, true))).booleanValue());
                    break;
                case "for":
                    pInfo.skip(); // for
                    
                    ParseInfo incrTags = new ParseInfo(pInfo, pInfo.removeSubTags());
                    ParseInfo initTags = skipSimpleStatement(incrTags);
                    ParseInfo condTags = skipSimpleStatement(incrTags);
                    
                    ParseInfo forTags = skipSingleStatement(pInfo);
                    
                    pInfo.curSub = initTags;
                    incrTags.parent = pInfo;
                    condTags.parent = pInfo;
                    
                    for (parseSingleStatement(initTags); 
                            ((Boolean)parseStatementWithOperators(new ParseInfo(condTags, true))).booleanValue(); 
                            parseSingleStatement(new ParseInfo(incrTags, true)))
                    {
                        result = parseSingleStatement(new ParseInfo(forTags, true));
                    }
                    break;
                default:
                    ParseInfo statTags = skipSimpleStatement(pInfo);
                    
                    int assignIndex = indexOfAssignment(statTags); // =, +=, -=, *=, /=, %=, &=, ^=, |=, <<=, >>=, >>>=
                            
                    if (assignIndex >= 0)
                    {
                        while (assignIndex > 0)
                        {
                            statTags.skip(assignIndex - 1);

                            String fieldName = statTags.removeTag();
                            String assignOperator = statTags.removeTag();
                            int len = assignOperator.length();
                            if (len > 1)
                            {
                                setFieldValue(fieldName, calculate(getFieldValue(fieldName), parseStatementWithOperators(statTags), assignOperator.substring(0, len - 1)));
                                
                                return null;
                            }
                            else
                            {
                                setFieldValue(fieldName, parseStatementWithOperators(statTags));
                                
                                assignIndex = indexOf(statTags, "=");
                            }
                        }
                        
                        return null;
                    }
                    else
                    {
                        if (statTags.size() == 2) // ++i, --i, i--, i++
                        {
                            int indexIncDec = indexOf(statTags, "++");
                            boolean dec = indexIncDec < 0;
                            if (dec)
                            {
                                indexIncDec = indexOf(statTags, "--");
                                
                            }
                            if (indexIncDec >= 0)
                            {
                                String fieldName = statTags.getTag(1 - indexIncDec);
                                
                                Object operand = getFieldValue(fieldName);
                                boolean isDouble = operand instanceof Double;
                                if (isDouble || operand instanceof Float)
                                {
                                    double op = ((Number)operand).doubleValue();
                                    if (dec)
                                    {
                                        op--;
                                    }
                                    else
                                    {
                                        op++;
                                    }
                                    if (isDouble)
                                    {
                                        operand = Double.valueOf(op);
                                    }
                                    else
                                    {
                                        operand = Float.valueOf((float)op);
                                    }
                                }
                                else
                                {
                                    long op = ((Number)operand).longValue();
                                    if (dec)
                                    {
                                        op--;
                                    }
                                    else
                                    {
                                        op++;
                                    }
                                    if (operand instanceof Integer)
                                    {
                                        operand = Integer.valueOf((int)op);
                                    }
                                    else if (operand instanceof Long)
                                    {
                                        operand = Long.valueOf(op);
                                    }
                                    else if (operand instanceof Short)
                                    {
                                        operand = Short.valueOf((short)op);
                                    }
                                    else if (operand instanceof Byte)
                                    {
                                        operand = Byte.valueOf((byte)op);
                                    }
                                    else
                                    {
                                        throw new IllegalArgumentException("Operator " + statTags.getTag(indexIncDec) + " is not supported for " + operand);
                                    }
                                }

                                setFieldValue(fieldName, operand);
                                
                                return null;
                            }
                        }
                        
                        return parseStatementWithOperators(statTags);
                    }
            }

            return result;
        }
    }
    
    /**
     * Index of assignment operator or -1, if none exists.
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the index
     */
    private int indexOfAssignment(ParseInfo pInfo)
    {
        for (int i = 0, size = pInfo.size(); i < size; i++)
        {
            String tag = pInfo.getTag(i);
            if (tag != null && tag.endsWith("="))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Index of object or -1, if none exists.
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @param pObject the object
     * @return the index
     */
    private int indexOf(ParseInfo pInfo, Object pObject)
    {
        for (int i = 0, size = pInfo.size(); i < size; i++)
        {
            if (pObject.equals(pInfo.getTag(i)))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Parses all statements separated with ;.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the result.
     */
    private Object parseAllStatements(ParseInfo pInfo)
    {
        Object result = null;
        
        while (pInfo.hasTag())
        {
            result = parseSingleStatement(pInfo);

            while (pInfo.hasTag() && ";".equals(pInfo.getTag()))
            {
                pInfo.skip();
            }
        }
        
        return result;
    }

    /**
     * Parses all imports.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     */
    private void parseImports(ParseInfo pInfo)
    {
        while (pInfo.hasTag() && "import".equals(pInfo.getTag()))
        {
            pInfo.skip();
            
            Class<?> clazz = getClassByName(pInfo.removeTag(), pInfo, false);
            addImport(clazz.getName());
            
            if (pInfo.hasTag() && ";".equals(pInfo.getTag()))
            {
                pInfo.skip();
            }
        }
    }
    
    /**
     * Parses all statements separated with ;.
     * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
     * @return the result.
     */
    private Object parseAllStatementsWithImports(ParseInfo pInfo)
    {
        parseImports(pInfo);
        
        return parseAllStatements(pInfo);
    }
        
	/**
	 * Calculates 2 operands.
	 * @param pOperand1 operand 1
	 * @param pOperand2 operand 2
     * @param pOperator operator
	 * @return the result
	 */
    private Object calculate(Object pOperand1, Object pOperand2, String pOperator)
	{
        if (pOperand1 instanceof Number && pOperand2 instanceof Number)
		{
            boolean isDouble = pOperand1 instanceof Double || pOperand2 instanceof Double;
            
            if (isDouble || pOperand1 instanceof Float || pOperand2 instanceof Float)
			{
				double o1 = ((Number)pOperand1).doubleValue();
				double o2 = ((Number)pOperand2).doubleValue();
				double result;
                switch (pOperator)
				{
                    case "*": result = o1 * o2; break;
                    case "/": result = o1 / o2; break;
                    case "%": result = o1 % o2; break;
                    case "+": result = o1 + o2; break;
                    case "-": result = o1 - o2; break;
                    case "==": return Boolean.valueOf(o1 == o2);
                    case "!=": return Boolean.valueOf(o1 != o2);
                    case ">": return Boolean.valueOf(o1 > o2);
                    case "<": return Boolean.valueOf(o1 < o2);
                    case ">=": return Boolean.valueOf(o1 >= o2);
                    case "<=": return Boolean.valueOf(o1 <= o2);
                    default:
                        throw new IllegalArgumentException("Operator " + pOperator + " is not supported for float or double!");
				}

                if (isDouble)
				{
					return Double.valueOf(result);
				}
				else
				{
					return Float.valueOf((float)result);
				}
			}
			else
			{
				long o1 = ((Number)pOperand1).longValue();
				long o2 = ((Number)pOperand2).longValue();
				long result;
                switch (pOperator)
				{
                    case "*": result = o1 * o2; break;
                    case "/": result = o1 / o2; break;
                    case "%": result = o1 % o2; break;
                    case "+": result = o1 + o2; break;
                    case "-": result = o1 - o2; break;
                    case "<<": result = o1 << o2; break;
                    case ">>": result = o1 >> o2; break;
                    case ">>>": result = o1 >>> o2; break;
                    case "&": result = o1 & o2; break;
                    case "^": result = o1 ^ o2; break;
                    case "|": result = o1 | o2; break;
                    case "==": return Boolean.valueOf(o1 == o2);
                    case "!=": return Boolean.valueOf(o1 != o2);
                    case ">": return Boolean.valueOf(o1 > o2);
                    case "<": return Boolean.valueOf(o1 < o2);
                    case ">=": return Boolean.valueOf(o1 >= o2);
                    case "<=": return Boolean.valueOf(o1 <= o2);
                    default:
                        throw new IllegalArgumentException("Operator " + pOperator + " is not supported for long, int, short or byte!");
				}

                if (pOperand1 instanceof Integer || pOperand2 instanceof Integer)
                {
                    return Integer.valueOf((int)result);
                }
                else if (pOperand1 instanceof Long || pOperand2 instanceof Long)
				{
					return Long.valueOf(result);
				}
				else if (pOperand1 instanceof Short || pOperand2 instanceof Short)
				{
					return Short.valueOf((short)result);
				}
				else if (pOperand1 instanceof Byte || pOperand2 instanceof Byte)
				{
					return Byte.valueOf((byte)result);
				}
			}
		}
        else
		{
            switch (pOperator)
			{
                case "==":
                    return Boolean.valueOf(pOperand1 == pOperand2);
                case "!=":
                    return Boolean.valueOf(pOperand1 != pOperand2);
                case "instanceof":
                    if (pOperand2 instanceof Class)
                    {
                        return Boolean.valueOf(((Class)pOperand2).isInstance(pOperand1));
                    }
                    break;
                case "+":
                    if (pOperand1 instanceof String || pOperand2 instanceof String)
                    {
                        return String.valueOf(pOperand1) + String.valueOf(pOperand2);
                    }
                    break;
                case "&&":
                    if (pOperand1 instanceof Boolean && pOperand2 instanceof Boolean)
                    {
                        return Boolean.valueOf(((Boolean)pOperand1).booleanValue() && ((Boolean)pOperand2).booleanValue());
                    }
                    break;
                case "||":
                    if (pOperand1 instanceof Boolean && pOperand2 instanceof Boolean)
                    {
                        return Boolean.valueOf(((Boolean)pOperand1).booleanValue() && ((Boolean)pOperand2).booleanValue());
                    }
                    break;
                case "^":
                    if (pOperand1 instanceof Boolean && pOperand2 instanceof Boolean)
                    {
                        return Boolean.valueOf(((Boolean)pOperand1).booleanValue() && ((Boolean)pOperand2).booleanValue());
                    }
                    break;
                default:
            }
        }

        throw new IllegalArgumentException("Operator " + pOperator + " is not supported for " + pOperand1 + " and " + pOperand2);
	}
	
	/**
	 * Parses a parameter list.
	 * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @return the parameter list.
	 */
	private int[] parseDimensions(ParseInfo pInfo)
	{
		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		
		do
		{
		    pInfo.skip(); // [
			
			if ("]".equals(pInfo.getTag())) // ] or value
			{
				dimensions.add(Integer.valueOf(-1));
			}
			else
			{
				dimensions.add((Integer)parseStatement(pInfo));
			}
			if (!pInfo.hasTag() || !"]".equals(pInfo.getTag()))
			{
	            throw new IllegalArgumentException("Missing bracket close ']' for bracket open '['!");
			}
			pInfo.skip(); // ]
		}
		while (pInfo.hasTag() && "[".equals(pInfo.getTag()));

		int[] result = new int[dimensions.size()];
		
		for (int i = 0; i < result.length; i++)
		{
			result[i] = dimensions.get(i).intValue();
		}
		
		return result;
	}
	
	/**
	 * Parses the constant array definition.
	 * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @param pDimension the dimension.
	 * @param pComponentType the component type.
	 * @return the array definition.
	 */
	private Object parseArrayData(ParseInfo pInfo, int pDimension, Class<?> pComponentType)
	{
		pDimension--;
		if (pDimension <= 0)
		{
			return parseParameters(pInfo, pComponentType);
		}
		else
		{
			List<Object> result = new ArrayList<Object>();
			
			if (pInfo.hasTag())
			{
				result.add(parseArrayData(new ParseInfo(pInfo, pInfo.removeSubTags()), pDimension, pComponentType));
				
				while (pInfo.hasTag())
				{
				    String tag = pInfo.removeTag();
				    if (!",".equals(tag))
				    {
				        throw new IllegalArgumentException("Found " + tag + " where , was expected!");
				    }
					
					result.add(parseArrayData(new ParseInfo(pInfo, pInfo.removeSubTags()), pDimension, pComponentType));
				}
			}

	        pInfo.close();
	        
			return result.toArray((Object[])Array.newInstance(getComponentType(pComponentType, pDimension), result.size()));
		}
	}
	
	/**
	 * Parses a parameter list.
	 * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @param pComponentType the ComponentType of the Array.
	 * @return the parameter list.
	 */
	private Object parseParameters(ParseInfo pInfo, Class<?> pComponentType)
	{
		List<Object> result = new ArrayList<Object>();
		
		if (pInfo.hasTag())
		{
			result.add(parseStatementWithOperators(pInfo));
			
			while (pInfo.hasTag() && ",".equals(pInfo.getTag()))
			{
			    pInfo.skip();
				
				result.add(parseStatementWithOperators(pInfo));
			}
		}
		
		pInfo.close();
		
		if (pComponentType.isPrimitive())
		{
			Object array = Array.newInstance(pComponentType, result.size());
			for (int i = 0, size = result.size(); i < size; i++)
			{
				Array.set(array, i, result.get(i));
			}
			
			return array;
		}
		else
		{
			return result.toArray((Object[])Array.newInstance(pComponentType, result.size()));
		}
	}
	
	/**
	 * Parses calls or fields.
	 * 
	 * @param pInstance the instance.
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @return the result.
	 */
	private Object parseCallsOrFields(Object pInstance, ParseInfo pInfo)
	{
		while (pInfo.hasTag() && ".".equals(pInfo.getTag()))
		{
	        pInfo.skip(); // .
	        
			pInstance = parseCallOrField(pInstance, pInstance.getClass(), pInfo);
		}

		return pInstance;
	}

	/**
	 * Parses one call or field, static or non static.
	 * 
	 * @param pInstance the instance if non static.
	 * @param pClass the class.
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 * @return the result.
	 */
	private Object parseCallOrField(Object pInstance, Class<?> pClass, ParseInfo pInfo)
	{
		String callOrField = pInfo.removeTag();
		
		if (pInfo.hasTag() && pInfo.getSubTags() != null)
		{
			checkMethodAllowed(pClass, callOrField);

			Object[] parameters = (Object[])parseParameters(new ParseInfo(pInfo, pInfo.removeSubTags()), Object.class);
			if (parameters.length == 1 && parameters[0] == null)  // Reflective maps Object[] {null} to first Parameter is null.
			{
				parameters = null;
			}

			try
			{
				pInstance = Reflective.call(pInstance, pClass, false, callOrField, parameters);
			}
			catch (NoSuchMethodException pEx)
			{
			    throw new IllegalArgumentException("Method " + callOrField + "(...) does not exist with the given parameters!");
			}
			catch (Throwable pThrowable)
			{
				throw new IllegalStateException("Method call " + callOrField + "(...) failed!", pThrowable);
			}
		}
		else
		{
			try
			{
				if ("class".equals(callOrField) && pInstance == null)
				{
					pInstance = pClass;
				}
				else
				{
					Field field = pClass.getField(callOrField);
					
					boolean isStaticField = pInstance == null;
					
					pInstance = field.get(pInstance);
					
					if (isStaticField && pInstance != null) // static member
					{
						addClassWithConstants(pClass);
					}
				}
			}
			catch (Throwable pException)
			{
				throw new IllegalArgumentException("Field " + callOrField + " not found!");
			}
		}

		return pInstance;
	}
	
	/**
	 * Parses the generics stuff.
	 * 
     * @param pInfo the <code>ParseInfo</code> for parsing.
	 */
	private void parseGenerics(ParseInfo pInfo)
	{
		if (pInfo.hasTag() && "<".equals(pInfo.getTag()))
		{
		    pInfo.skip();
			
			int count = 1;
			
			while (count > 0 && pInfo.hasTag())
			{
				Object tag = pInfo.removeTag();
				
				if ("<".equals(tag))
				{
					count++;
				}
				else if (">".equals(tag))
				{
					count--;
				}
			}
		}
	}

	/**
	 * Gets all allowed Methods, or an empty list, if everything allowed.
	 * 
	 * @return all allowed Methods, or an empty list, if everything allowed.
	 */
	public String[] getAllowedMethods()
	{
		if (allowedMethods == null)
		{
			return new String[0];
		}
		else
		{
			return allowedMethods.toArray(new String[allowedMethods.size()]);
		}
	}
	
	/**
	 * Sets all allowed Methods.
	 * if an empty list is set, everything is allowed.
	 * 
	 * @param pMethodDefinitions the method definitions
	 */
	public void setAllowedMethods(String... pMethodDefinitions)
	{
		if (allowedMethods != null)
		{
			removeAllAllowedMethods();
		}
		if (pMethodDefinitions != null)
		{
			for (String methodDefinition : pMethodDefinitions)
			{
				addAllowedMethod(methodDefinition);
			}
		}
	}
	
	/**
	 * Gets all denied Methods, or an empty list, if nothing is denied.
	 * 
	 * @return all denied Methods, or an empty list, if nothing is denied.
	 */
	public String[] getDeniedMethods()
	{
		if (deniedMethods == null)
		{
			return new String[0];
		}
		else
		{
			return deniedMethods.toArray(new String[deniedMethods.size()]);
		}
	}
	
	/**
	 * Sets all denied Methods.
	 * if an empty list is set, nothing is denied.
	 * 
	 * @param pMethodDefinitions the method definitions
	 */
	public void setDeniedMethods(String... pMethodDefinitions)
	{
		if (deniedMethods != null)
		{
			removeAllDeniedMethods();
		}
		if (pMethodDefinitions != null)
		{
			for (String methodDefinition : pMethodDefinitions)
			{
				addDeniedMethod(methodDefinition);
			}
		}
	}
	
	/**
	 * Adds the given allowed methods. 
	 * Wild card * is allowed for all methods of the given class. This includes the constructor
	 * 
	 * Example:
	 *   java.lang.System.currentTimeMillis
	 *   javax.rad.model.condition.ICondition.*
	 * 
	 * If classes are defined with imports the simple class name is also allowed.
	 * 
	 * Example:
	 *   System.currentTimeMillis
	 *   ICondition.*
	 * 
	 * @param pMethodDefinition the method definition
	 */
	public void addAllowedMethod(String pMethodDefinition)
	{
		if (allowedMethods == null)
		{
			allowedMethods = new ArrayList<String>();
			internAllowedMethods = new HashMap<Class, Set<String>>();
			cachedAllowedMethods = new HashMap<Class, Set<String>>();
		}		
		addMethod(allowedMethods, internAllowedMethods, cachedAllowedMethods, pMethodDefinition);
	}
		
	/**
	 * Adds the given denied methods. 
	 * Wild card * is denied for all methods of the given class. This includes the constructor
	 * 
	 * Example:
	 *   java.lang.System.exit
	 *   java.io.File.*
	 * 
	 * If classes are defined with imports the simple class name is also allowed.
	 * 
	 * Example:
	 *   System.exit
	 *   File.*
	 * 
	 * @param pMethodDefinition the method definition
	 */
	public void addDeniedMethod(String pMethodDefinition)
	{
		if (deniedMethods == null)
		{
			deniedMethods = new ArrayList<String>();
			internDeniedMethods = new HashMap<Class, Set<String>>();
			cachedDeniedMethods = new HashMap<Class, Set<String>>();
		}		
		addMethod(deniedMethods, internDeniedMethods, cachedDeniedMethods, pMethodDefinition);
	}
		
	/**
	 * Adds the constructor to the allowed methods of the given class.
	 * The constructor is defined by the constant <code>CONSTRUCTOR</code>. 
	 * 
	 * @param pClassName the class name to allow the constructor
	 */
	public void addAllowedConstructor(String pClassName)
	{
		addAllowedMethod(pClassName + "." + CONSTRUCTOR);
	}
		
	/**
	 * Adds the constructor to the allowed methods of the given class and all its derived classes.
	 * The derived constructors are defined by the constant <code>DERIVED_CONSTRUCTORS</code>. 
	 * Example:
	 * if the derived constructurs of ICondition are allowed, 
	 * the constructors of Equals, Not, Or, And, Like, ... are allowed to be called.
	 * 
	 * @param pClassName the class name to allow the constructor for itself and all derived classes
	 */
	public void addAllowedDerivedConstructors(String pClassName)
	{
		addAllowedMethod(pClassName + "." + DERIVED_CONSTRUCTORS);
	}
		
	/**
	 * Adds the constructor to the denied methods of the given class.
	 * The constructor is defined by the constant <code>CONSTRUCTOR</code>. 
	 * 
	 * @param pClassName the class name to deny the constructor 
	 */
	public void addDeniedConstructor(String pClassName)
	{
		addDeniedMethod(pClassName + "." + CONSTRUCTOR);
	}
		
	/**
	 * Adds the constructor to the denied methods of the given class and all its derived classes.
	 * The derived constructors are defined by the constant <code>DERIVED_CONSTRUCTORS</code>. 
	 * Example:
	 * if the derived constructurs of ICondition are denied, 
	 * the constructors of Equals, Not, Or, And, Like, ... are not allowed to be called.
	 * 
	 * @param pClassName the class name to deny the constructor for itself and all derived classes
	 */
	public void addDeniedDerivedConstructors(String pClassName)
	{
		addDeniedMethod(pClassName + "." + DERIVED_CONSTRUCTORS);
	}
		
	/**
	 * Internal method to parse and add the method definition.
	 * 
	 * @param pMethods the stored method definitions.
	 * @param pInternMethods the internal parsed classes and methods.
	 * @param pCachedMethods the cached methods for all used classes.
	 * @param pMethodDefinition the method definition.
	 */
	private void addMethod(List<String> pMethods, Map<Class, Set<String>> pInternMethods, Map<Class, Set<String>> pCachedMethods, String pMethodDefinition)
	{
		int mthdIdx = pMethodDefinition.lastIndexOf('.');
		
		String clsName = pMethodDefinition.substring(0, mthdIdx);
		String mthdName = pMethodDefinition.substring(mthdIdx + 1);
		
		Class clazz = getClassByName(clsName, clsName.indexOf('.') < 0);
		
		Set<String> methods = pInternMethods.get(clazz);
		if (methods == null)
		{
			methods = new HashSet<String>();
			pInternMethods.put(clazz, methods);
		}
		
		if ("*".equals(mthdName))
		{
			pMethodDefinition = clazz.getName() + ".*"; // use full qualified class name to store 
					
			Method[] meth = clazz.getMethods();
			
			for (Method m : meth)
			{
				methods.add(m.getName());
			}
			
			methods.add(CONSTRUCTOR); // Constructor is included with *, derived constructors not!
		}
		else
		{
			pMethodDefinition = clazz.getName() + "." + mthdName; // use full qualified class name to store
			
			methods.add(mthdName);
		}

		pMethods.add(pMethodDefinition);
		pCachedMethods.clear();
	}
	
	/**
	 * Removes the given allowed methods. 
	 * Wild card * is allowed for all methods of the given class. This includes the constructor
	 * 
	 * Example:
	 *   java.lang.System.currentTimeMillis
	 *   javax.rad.model.condition.ICondition.*
	 * 
	 * If classes are defined with imports the simple class name is also allowed.
	 * 
	 * Example:
	 *   System.currentTimeMillis
	 *   ICondition.*
	 * 
	 * @param pMethodDefinition the method definition
	 */
	public void removeAllowedMethod(String pMethodDefinition)
	{
		if (allowedMethods != null)
		{
			removeMethod(allowedMethods, internAllowedMethods, cachedAllowedMethods, pMethodDefinition);
		}
	}
		
	/**
	 * Removes the given denied methods. 
	 * Wild card * is denied for all methods of the given class. This includes the constructor
	 * 
	 * Example:
	 *   java.lang.System.exit
	 *   java.io.File.*
	 * 
	 * If classes are defined with imports the simple class name is also allowed.
	 * 
	 * Example:
	 *   System.exit
	 *   File.*
	 * 
	 * @param pMethodDefinition the method definition
	 */
	public void removeDeniedMethod(String pMethodDefinition)
	{
		if (deniedMethods != null)
		{
			removeMethod(deniedMethods, internDeniedMethods, cachedDeniedMethods, pMethodDefinition);
		}
	}

	/**
	 * Removes all allowed methods.
	 */
	public void removeAllAllowedMethods()
	{
		if (allowedMethods != null)
		{
			allowedMethods.clear();
			internAllowedMethods.clear();
			cachedAllowedMethods.clear();
		}
	}
	
	/**
	 * Removes all denied methods.
	 */
	public void removeAllDeniedMethods()
	{
		if (deniedMethods != null)
		{
			deniedMethods.clear();
			internDeniedMethods.clear();
			cachedDeniedMethods.clear();
		}
	}
	
	/**
	 * Adds the constructor to the allowed methods of the given class.
	 * The constructor is defined by the constant <code>CONSTRUCTOR</code>. 
	 * 
	 * @param pClassName the class name to allow the constructor
	 */
	public void removeAllowedConstructor(String pClassName)
	{
		removeAllowedMethod(pClassName + "." + CONSTRUCTOR);
	}
		
	/**
	 * Removes the constructor to the allowed methods of the given class and all its derived classes.
	 * The derived constructors are defined by the constant <code>DERIVED_CONSTRUCTORS</code>. 
	 * Example:
	 * if the derived constructurs of ICondition are allowed, 
	 * the constructors of Equals, Not, Or, And, Like, ... are allowed to be called.
	 * 
	 * @param pClassName the class name to allow the constructor for itself and all derived classes
	 */
	public void removeAllowedDerivedConstructors(String pClassName)
	{
		removeAllowedMethod(pClassName + "." + DERIVED_CONSTRUCTORS);
	}
		
	/**
	 * Removes the constructor to the denied methods of the given class.
	 * The constructor is defined by the constant <code>CONSTRUCTOR</code>. 
	 * 
	 * @param pClassName the class name to deny the constructor 
	 */
	public void removeDeniedConstructor(String pClassName)
	{
		removeDeniedMethod(pClassName + "." + CONSTRUCTOR);
	}
		
	/**
	 * Removes the constructor to the denied methods of the given class and all its derived classes.
	 * The derived constructors are defined by the constant <code>DERIVED_CONSTRUCTORS</code>. 
	 * Example:
	 * if the derived constructurs of ICondition are denied, 
	 * the constructors of Equals, Not, Or, And, Like, ... are not allowed to be called.
	 * 
	 * @param pClassName the class name to deny the constructor for itself and all derived classes
	 */
	public void removeDeniedDerivedConstructors(String pClassName)
	{
		removeDeniedMethod(pClassName + "." + DERIVED_CONSTRUCTORS);
	}
		
	/**
	 * Internal method to parse and remove the method definition.
	 * 
	 * @param pMethods the stored method definitions.
	 * @param pInternMethods the internal parsed classes and methods.
	 * @param pCachedMethods the cached methods for all used classes.
	 * @param pMethodDefinition the method definition.
	 */
	private void removeMethod(List<String> pMethods, Map<Class, Set<String>> pInternMethods, Map<Class, Set<String>> pCachedMethods, String pMethodDefinition)
	{
		int mthdIdx = pMethodDefinition.lastIndexOf('.');
		
		String clsName = pMethodDefinition.substring(0, mthdIdx);
		String mthdName = pMethodDefinition.substring(mthdIdx + 1);
		
		Class clazz = getClassByName(clsName, clsName.indexOf('.') < 0);
		
		Set<String> methods = pInternMethods.get(clazz);
		if (methods != null)
		{
			if ("*".equals(mthdName))
			{
				pMethodDefinition = clazz.getName() + ".*"; // use full qualified class name to remove 

				methods.clear();
			}
			else
			{
				pMethodDefinition = clazz.getName() + "." + mthdName; // use full qualified class name to store

				methods.remove(mthdName);
			}
			
			if (methods.isEmpty())
			{
				pInternMethods.remove(clazz);
			}
		}

		pMethods.remove(pMethodDefinition);
		pCachedMethods.clear();
	}
	
	/**
	 * Gets the methods for the given class.
	 * This analyzes all super classes and interfaces, also the configuration of derived constructors.
	 * 
	 * @param pInternMethods the internal parsed allowed or denied methods
	 * @param pCachedMethods the cached analyzed methods
	 * @param pClass the class to analyse
	 * @return the methods for the given class.
	 */
	private Set<String> getMethods(Map<Class, Set<String>> pInternMethods, Map<Class, Set<String>> pCachedMethods, Class pClass)
	{
		Set<String> methods = pCachedMethods.get(pClass);
		
		if (methods == null)
		{
			methods = new HashSet<String>();
			pCachedMethods.put(pClass, methods);

			Set<String> internMethods = pInternMethods.get(pClass);
			if (internMethods != null)
			{
				methods.addAll(internMethods);
			}

			if (methods.contains(DERIVED_CONSTRUCTORS))
			{
				methods.add(CONSTRUCTOR);
			}
			boolean hasConstructor = methods.contains(CONSTRUCTOR);

			Class[] interfaces = pClass.getInterfaces();
			if (interfaces != null)
			{
				for (Class cl : pClass.getInterfaces())
				{
					methods.addAll(getMethods(pInternMethods, pCachedMethods, cl));
				}
			}
			
			Class superClass = pClass.getSuperclass();
			if (superClass != null)
			{
				methods.addAll(getMethods(pInternMethods, pCachedMethods, pClass.getSuperclass()));
			}
			
			if (!hasConstructor && !methods.contains(DERIVED_CONSTRUCTORS))
			{
				methods.remove(CONSTRUCTOR);
			}
		}

		return methods;
	}
	
	/**
	 * Checks, if the method is allowed for the class. 
	 * The method has to be in the list of allowed methods, or no allowed method is configured.
	 * After that the method may not be in the list of denied methods, or no denied method is configured.
	 * 
	 * @param pClass the class to analyze
	 * @param pMethod the method
	 */
	private void checkMethodAllowed(Class pClass, String pMethod)
	{
		if (allowedMethods == null || allowedMethods.isEmpty() || getMethods(internAllowedMethods, cachedAllowedMethods, pClass).contains(pMethod))
		{
			if (deniedMethods == null || deniedMethods.isEmpty() || !getMethods(internDeniedMethods, cachedDeniedMethods, pClass).contains(pMethod))
			{
				return;
			}
		}
		
		throw new SecurityException("Method '" + pMethod + "' in class '" + pClass.getName() + "' is forbidden!");
	}
	
	/**
	 * Storing tag information.
	 * 
	 * @author Martin Handsteiner
	 */
	private static final class Tag
	{
	    /** The tag as string. */
	    private String tag;
	    /** The position in source. */
	    private int    pos;

	    /**
	     * Constructs a new Tag.
	     * @param pSource the source
	     * @param pStart the start position of the tag
	     * @param pEnd the end position of the tag
	     */
	    public Tag(String pSource, int pStart, int pEnd)
	    {
            tag = pSource.substring(pStart, pEnd);
            pos = pStart;
	    }
	    
	    /**
	     * Gets the tag as string, or null, if it is not a tag.
	     * 
	     * @param pTag the possible tag
	     * @return the tag as string, or null, if it is not a tag.
	     */
	    public static final String getTagString(Object pTag)
	    {
	        return pTag.getClass() == Tag.class ? ((Tag)pTag).tag : null;
	    }
	    
	    /**
	     * Appends a character to the existing tag string of the last tag.
	     * 
	     * @param pTags the tag list
	     * @param pChar the character
	     */
	    public static final void appendChar(List<Object> pTags, char pChar)
	    {
	        ((Tag)pTags.get(pTags.size() - 1)).tag += pChar;
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
        @Override
        public String toString()
        {
            return tag + "(pos:" + pos + ")";
        }
	}
	
	/**
	 * The <code>ParseInfo</code> stores the current position of parsing the tags,
	 * and additional informations for debugging like parent and current sub and current tag.
	 * 
	 * @author Martin Handsteiner
	 */
    private static final class ParseInfo
    {
        /** The list of tags to analyze. */
        private List<Object> tags;
        /** The current position for analyzing. */
        private int pos;
        /** The size for current analyzing. */
        private int size;
        /** The parent. */
        private ParseInfo parent;
        /** The current sub. */
        private ParseInfo curSub;
        /** The current tag. */
        private Tag curTag;
        
        /**
         * Constructs a root <code>ParseInfo</code> the the given tag list.
         * 
         * @param pTags the tag list.
         */
        private ParseInfo(List<Object> pTags)
        {
            tags = pTags;
            pos  = 0;
            size = pTags.size();
        }

        /**
         * Constructs a sub <code>ParseInfo</code> the the given tag list.
         * 
         * @param pInfo the parent.
         * @param pTags the tag list.
         */
        private ParseInfo(ParseInfo pInfo, List<Object> pTags)
        {
            tags = pTags;
            pos  = 0;
            size = pTags.size();
            parent = pInfo;
            parent.curSub = this;
        }

        /**
         * Constructs a sub <code>ParseInfo</code> the the given tag list.
         * 
         * @param pInfo the parent.
         * @param pClone true, if it is a clone.
         */
        private ParseInfo(ParseInfo pInfo, boolean pClone)
        {
            tags = pInfo.tags;
            pos  = pInfo.pos;
            size = pInfo.size;
            if (pClone)
            {
                parent = pInfo.parent;
                parent.curSub = this;
            }
            else
            {
                parent = pInfo;
                parent.curSub = this;
            }
        }

        /**
         * Informs the parent, that parsing this sub is done.
         */
        public final void close()
        {
            parent.curSub = null;
        }
        
        /**
         * Gets, if there is still a tag available.
         *   
         * @return true, if there is still a tag available.
         */
        public final boolean hasTag()
        {
            return pos < size;
        }
        
        /**
         * Gets the next item, either a tag or a list of tags.
         * 
         * @return the next item, either a tag or a list of tags.
         */
        public final Object getTagOrSubTags()
        {
            return tags.get(pos);
        }

        /**
         * Gets and removes the next item, either a tag or a list of tags.
         * 
         * @return the next item, either a tag or a list of tags.
         */
        public final Object removeTagOrSubTags()
        {
                return tags.get(pos++);
        }

        /**
         * Gets the next tag string or null, if it is not a tag.
         * 
         * @return the next tag.
         */
        public final String getTag()
        {
            Object tagOrSubTags = tags.get(pos);
            
            if (tagOrSubTags.getClass() == Tag.class)
            {
                curTag = (Tag)tagOrSubTags;
                
                return curTag.tag;
            }
            else
            {
                return null;
            }
        }

        /**
         * Gets the tag string at the given index or null, if it is not a tag.
         * 
         * @param pIndex the index.
         * @return the next tag.
         */
        public final String getTag(int pIndex)
        {
            Object tagOrSubTags = tags.get(pos + pIndex);
            
            return tagOrSubTags.getClass() == Tag.class ? ((Tag)tagOrSubTags).tag : null;
        }

        /**
         * Gets and removes the next tag string or null, if it is not a tag.
         * 
         * @return the next tag.
         */
        public final String removeTag()
        {
            curTag = (Tag)tags.get(pos++);
                
            return curTag.tag;                
        }

        /**
         * Gets the next list of tags or null, if it is not a list.
         * 
         * @return the next list of tags.
         */
        public final List<Object> getSubTags()
        {
            Object tagOrSubTags = tags.get(pos);
            
            return tagOrSubTags.getClass() != Tag.class ? (List<Object>)tagOrSubTags : null;
        }

        /**
         * Gets the next list of tags or null, if it is not a list.
         * 
         * @return the next list of tags.
         */
        public final List<Object> removeSubTags()
        {
            Object tagOrSubTags = tags.get(pos++);
            
            if (tagOrSubTags.getClass() != Tag.class)
            {
                return (List<Object>)tagOrSubTags;
            }
            else
            {
                curTag = (Tag)tagOrSubTags;
                
                throw new IllegalArgumentException("Found " + curTag.tag + " where a list of tags was expected!");
            }
        }

        /**
         * Go one step back.
         */
        public final void back()
        {
            pos--;
        }

        /**
         * Skip current tag.
         */
        public final void skip()
        {
            pos++;
        }

        /**
         * Skip an amount of tags.
         * 
         * @param pAmount
         */
        public final void skip(int pAmount)
        {
            pos += pAmount;
        }

        /**
         * The size.
         * 
         * @return the size. 
         */
        public final int size()
        {
            return size - pos;
        }
        
        /**
         * Gets the list of tags to analyze.
         * 
         * @return the list of tags to analyze.
         */
        public final List<Object> toList()
        {
            if (pos > size)
            {
                return tags.subList(size, size);
            }
            else
            {
                return tags.subList(pos, size);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return toList().toString();
        }
    }
    
}	// SimpleJavaSource
