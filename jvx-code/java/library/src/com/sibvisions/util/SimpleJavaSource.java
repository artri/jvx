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

	/** Parsed Tags Cache. */
	protected List<Object> tempTags = new ArrayList<Object>();
	
	/** All known instances. */
	protected Map<String, Object> instances = new HashMap<String, Object>();

	/** The Name for any instance. */
	protected Map<Object, String> instanceNames = new IdentityHashMap<Object, String>();

	/** The classloader for class loading. */
	protected ClassLoader classLoader = null;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
			tempTags.clear();
			
			parseTags(pSource, tempTags);
			
			return parseParameterWithOperators(tempTags);
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
	 * @param pTags the tags.
	 * @param pCallOrField true, if a call or field follows.
	 * @return the class.
	 */
	private Class<?> getClassByName(String pClassName, List<Object> pTags, boolean pCallOrField)
	{
		boolean simpleClassName = true;
		boolean subClass = false;
		
		tempBuilder.setLength(0);
		tempBuilder.append(pClassName);

		int offset = pCallOrField ? 2 : 0;
		
		while (pTags.size() > offset && ".".equals(pTags.get(offset)))
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

			if (pCallOrField && pTags.size() > 1)
			{
                if ("class".equals(pTags.get(1)))
                {
                    return getClassByName(tempBuilder.toString());
                }
                else 
                {
                    try
                    {
                        Class fieldCheckClass = getClassByName(tempBuilder.toString());
                        fieldCheckClass.getField((String)pTags.get(1));
                        
                        return fieldCheckClass;
                    }
                    catch (Throwable ex)
                    {
                        // Ignore
                    }
                }
			}

			pTags.remove(0);
			tempBuilder.append(subClass ? '$' : '.');
			tempBuilder.append(pTags.remove(0));
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
					current.set(current.size() - 1, (String)current.get(current.size() - 1) + ch);
					pos = i + 1;
				}
				else
				{
					if (i > pos)
					{
						current.add(pSource.substring(pos, i));
						
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
							current.add(pSource.substring(pos, pos + 1));
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
            current.add(pSource.substring(pos, length));
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
	 * Parses on Parameter.
	 * 
	 * @param pTags the tags to parse.
	 * @return the result.
	 */
	private Object parseParameter(List<Object> pTags)
	{
		Object result;
		
		Object tagOrSubTags = pTags.remove(0);
		
		if (tagOrSubTags instanceof List<?>)
		{
			List<Object> subTags = (List<Object>)tagOrSubTags;
			
			try
			{
				List<Object> testClassTags = new ArrayList<Object>(subTags);
				getClassByName((String)testClassTags.remove(0), testClassTags, false);
				
				parseGenerics(pTags);
				
				result = parseParameter(pTags);
				
				//result = parseCallsOrFields(result, pTags);
			}
			catch (Exception ex)
			{
				result = parseParameterWithOperators(subTags);

				result = parseCallsOrFields(result, pTags);
			}
		}
		else
		{
			String tag = (String)tagOrSubTags;
			
			if ("new".equals(tag)) // Constructor
			{
				Class<?> clazz = getClassByName((String)pTags.remove(0), pTags, false);
			
				parseGenerics(pTags);
				
				if ("[".equals(pTags.get(0)))
				{
					int[] dimensions = parseDimensions(pTags);
					
					if (dimensions[0] < 0)
					{
						result = parseArrayData((List<Object>)pTags.remove(0), dimensions.length, clazz);
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
						
						result = Array.newInstance(getComponentType(clazz, dimensions.length - fixedDimension), fixed);
					}
				}
				else
				{
					checkMethodAllowed(clazz, CONSTRUCTOR);

					Object[] parameters = (Object[])parseParameters((List<Object>)pTags.remove(0), Object.class);
					if (parameters.length == 1 && parameters[0] == null) // Reflective maps Object[] {null} to first Parameter is null.
					{
						parameters = null;
					}

					try
					{
						result = Reflective.construct(clazz, parameters);
					}
					catch (Throwable pThrowable)
					{
						throw new IllegalArgumentException("Creating a new instance of \"" + clazz.getSimpleName() + "\" failed!", pThrowable);
					}
				
					result = parseCallsOrFields(result, pTags);
				}
			}
			else if ("null".equals(tag))
			{
				result = null;
			}
			else if ("true".equals(tag))
			{
				result = Boolean.TRUE;
			}
			else if ("false".equals(tag))
			{
				result = Boolean.FALSE;
			}
			else if (tag.startsWith("\""))
			{
				result = parseString(tag.substring(1, tag.length() - 1));
			}
			else if (tag.startsWith("\'"))
			{
				result = parseString(tag.substring(1, tag.length() - 1));
			}
			else if ("0123456789.".indexOf(tag.charAt(0)) >= 0)
			{
				boolean isDoubleOrFLoat = ".".equals(tag);
				if (isDoubleOrFLoat)
				{
					if (pTags.size() > 0)
					{
						tag = tag + pTags.remove(0);
					}
				}
				else
				{
					isDoubleOrFLoat = pTags.size() > 0 && ".".equals(pTags.get(0));
					if (isDoubleOrFLoat)
					{
						tag = tag + pTags.remove(0);
						if (pTags.size() > 0)
						{
							String possibleTag = (String)pTags.get(0);
							if (Character.isDigit(possibleTag.charAt(0)) || possibleTag.startsWith("E") || possibleTag.startsWith("E")
									|| "fFdD".contains(possibleTag))
							{
								tag = tag + possibleTag;
								pTags.remove(0);
							}
						}
					}
				}

				if (tag.charAt(0) == '0' && tag.length() > 1 && !isDoubleOrFLoat)
				{
					char ch = Character.toLowerCase(tag.charAt(1));
					if (ch == 'x')
					{
						if (tag.endsWith("l") || tag.endsWith("L"))
						{
							result = Long.valueOf(tag.substring(2, tag.length() - 1).replace("_", ""), 16);
						}
						else
						{
							result = Integer.valueOf(Long.valueOf(tag.substring(2).replace("_", ""), 16).intValue());
						}
					}
					else if (ch == 'b')
					{
						if (tag.endsWith("l") || tag.endsWith("L"))
						{
							result = Long.valueOf(tag.substring(2, tag.length() - 1).replace("_", ""), 2);
						}
						else
						{
							result = Integer.valueOf(tag.substring(2).replace("_", ""), 2);
						}
					}
					else
					{
						if (tag.endsWith("l") || tag.endsWith("L"))
						{
							result = Long.valueOf(tag.substring(1, tag.length() - 1).replace("_", ""), 8);
						}
						else
						{
							result = Integer.valueOf(tag.substring(1).replace("_", ""), 8);
						}
					}
				}
				else if (tag.endsWith("f") || tag.endsWith("F"))
				{
					result = Float.valueOf(tag.replace("_", ""));
				}
				else if (tag.endsWith("d") || tag.endsWith("D") || isDoubleOrFLoat || tag.contains("e") || tag.contains("E"))
				{
					result = Double.valueOf(tag.replace("_", ""));
				}
				else if (tag.endsWith("l") || tag.endsWith("L"))
				{
					result = Long.valueOf(tag.substring(0, tag.length() - 1).replace("_", ""));
				}
				else
				{
					result = Integer.valueOf(tag.replace("_", ""));
				}
			}
			else if ("-".equals(tag))
			{
				Object value = parseParameter(pTags);
				
				if (value instanceof Integer)
				{
					result = Integer.valueOf(-((Number)value).intValue());
				}
				else if (value instanceof Long)
				{
					result = Long.valueOf(-((Number)value).longValue());
				}
				else if (value instanceof Float)
				{
					result = Float.valueOf(-((Number)value).floatValue());
				}
				else if (value instanceof Double)
				{
					result = Double.valueOf(-((Number)value).doubleValue());
				}
				else if (value instanceof Byte)
				{
					result = Byte.valueOf((byte)-((Number)value).intValue());
				}
				else if (value instanceof Short)
				{
					result = Short.valueOf((short)-((Number)value).intValue());
				}
				else
				{
					throw new IllegalArgumentException("Negative Sign can only be used on Numbers");
				}
			}
			else if ("+".equals(tag))
			{
				result = parseParameter(pTags);
			}
			else if (pTags.size() > 0 && pTags.get(0) instanceof List<?>) // function call to this
			{
				pTags.add(0, tag);
				pTags.add(0, ".");
				
				result = parseCallsOrFields(instances.get("this"), pTags);
			}
			else if (instances.containsKey(tag)) // existing field.
			{
				Object instance = instances.get(tag);
				
				if (pTags.size() > 0 && "[".equals(pTags.get(0)))
				{
					int[] dimensions = parseDimensions(pTags);
					
					for (int i = 0; i < dimensions.length; i++)
					{
						instance = Array.get(instance, dimensions[i]);
					}
				}
				
				result = parseCallsOrFields(instance, pTags);
			}
			else
			{
				Class<?> clazz = getClassByName(tag, pTags, true);
				
				result = parseCallOrField(null, clazz, pTags);
				
				result = parseCallsOrFields(result, pTags);
			}
		}

		//TODO [HM] Operator parsing, ? command parsing, loop parsing, ...
		
		return result;
	}

	/**
	 * Parses on Parameter including operators.
	 * 
	 * @param pTags the tags to parse.
	 * @return the result.
	 */
	private Object parseParameterWithOperators(List<Object> pTags)
	{
		List<Object> operands = new ArrayList<Object>();
		List<String> operators = new ArrayList<String>();
		
		operands.add(parseParameter(pTags));
		
		while (pTags.size() > 0 && !",".equals(pTags.get(0)))
		{
			Object tagOrSubTags =  pTags.remove(0);
			
			if (tagOrSubTags instanceof String)
			{
				operators.add((String)tagOrSubTags);
				
				operands.add(parseParameter(pTags));
			}
			else
			{
				throw new IllegalArgumentException("Expected operator not object!");
			}
		}
		
		for (int i = 0; i < operators.size(); i++)
		{
			String operator = operators.get(i);
			if ("*".equals(operator) || "/".equals(operator) || "%".equals(operator))
			{
				operands.set(i, calculate(operands.get(i), operands.remove(i + 1), operators.remove(i)));
				i--;
			}
		}
		for (int i = 0; i < operators.size(); i++)
		{
			String operator = operators.get(i);
			if (!"|".equals(operator) && !"||".equals(operator))
			{
				operands.set(i, calculate(operands.get(i), operands.remove(i + 1), operators.remove(i)));
				i--;
			}
		}
		for (int i = 0; i < operators.size(); i++)
		{
			operands.set(i, calculate(operands.get(i), operands.remove(i + 1), operators.remove(i)));
			i--;
		}
		
		
		return operands.get(0);
	}
	
	/**
	 * Calculates 2 operands.
	 * @param pOperand1 operand 1
	 * @param pOperand2 operand 2
	 * @param operator operator
	 * @return the result
	 */
	private Object calculate(Object pOperand1, Object pOperand2, String operator)
	{
		if (!(pOperand1 instanceof Number && pOperand2 instanceof Number))
		{
			if ("==".equals(operator))
			{
				return Boolean.valueOf(pOperand1 == pOperand2);
			}
			else if ("!=".equals(operator))
			{
				return Boolean.valueOf(pOperand1 != pOperand2);
			}
		}
		if (pOperand1 instanceof String || pOperand2 instanceof String)
		{
			if ("+".equals(operator))
			{
				return String.valueOf(pOperand1) + String.valueOf(pOperand2);
			}
		}
		else if (pOperand1 instanceof Number && pOperand2 instanceof Number)
		{
			if (pOperand1 instanceof Double || pOperand1 instanceof Float || pOperand2 instanceof Double || pOperand2 instanceof Float)
			{
				double o1 = ((Number)pOperand1).doubleValue();
				double o2 = ((Number)pOperand2).doubleValue();
				double result;
				if ("*".equals(operator))
				{
					result = o1 * o2;
				}
				else if ("/".equals(operator))
				{
					result = o1 / o2;
				}
				else if ("%".equals(operator))
				{
					result = o1 % o2;
				}
				else if ("+".equals(operator))
				{
					result = o1 + o2;
				}
				else if ("-".equals(operator))
				{
					result = o1 - o2;
				}
				else if ("==".equals(operator))
				{
					return Boolean.valueOf(o1 == o2);
				}
				else if ("!=".equals(operator))
				{
					return Boolean.valueOf(o1 != o2);
				}
				else if (">".equals(operator))
				{
					return Boolean.valueOf(o1 > o2);
				}
				else if ("<".equals(operator))
				{
					return Boolean.valueOf(o1 < o2);
				}
				else if (">=".equals(operator))
				{
					return Boolean.valueOf(o1 >= o2);
				}
				else if ("<=".equals(operator))
				{
					return Boolean.valueOf(o1 <= o2);
				}
				else
				{
					throw new IllegalArgumentException("Operator " + operator + " is not supported for float or double!");
				}

				if (pOperand1 instanceof Double || pOperand2 instanceof Double)
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
				if ("*".equals(operator))
				{
					result = o1 * o2;
				}
				else if ("/".equals(operator))
				{
					result = o1 / o2;
				}
				else if ("%".equals(operator))
				{
					result = o1 % o2;
				}
				else if ("+".equals(operator))
				{
					result = o1 + o2;
				}
				else if ("-".equals(operator))
				{
					result = o1 - o2;
				}
				else if ("&".equals(operator))
				{
					result = o1 & o2;
				}
				else if ("|".equals(operator))
				{
					result = o1 | o2;
				}
				else if ("^".equals(operator))
				{
					result = o1 ^ o2;
				}
				else if ("==".equals(operator))
				{
					return Boolean.valueOf(o1 == o2);
				}
				else if ("!=".equals(operator))
				{
					return Boolean.valueOf(o1 != o2);
				}
				else if (">".equals(operator))
				{
					return Boolean.valueOf(o1 > o2);
				}
				else if ("<".equals(operator))
				{
					return Boolean.valueOf(o1 < o2);
				}
				else if (">=".equals(operator))
				{
					return Boolean.valueOf(o1 >= o2);
				}
				else if ("<=".equals(operator))
				{
					return Boolean.valueOf(o1 <= o2);
				}
				else
				{
					throw new IllegalArgumentException("Operator " + operator + " is not supported for long, int, short or byte!");
				}

				if (pOperand1 instanceof Long || pOperand2 instanceof Long)
				{
					return Long.valueOf(result);
				}
				else if (pOperand1 instanceof Integer || pOperand2 instanceof Integer)
				{
					return Integer.valueOf((int)result);
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
		else if (pOperand1 instanceof Boolean && pOperand2 instanceof Boolean)
		{
			boolean o1 = ((Boolean)pOperand1).booleanValue();
			boolean o2 = ((Boolean)pOperand2).booleanValue();
			if ("&&".equals(operator))
			{
				return  Boolean.valueOf(o1 && o2);
			}
			else if ("||".equals(operator))
			{
				return  Boolean.valueOf(o1 || o2);
			}
			else if ("^".equals(operator))
			{
				return  Boolean.valueOf(o1 ^ o2);
			}
			else
			{
				throw new IllegalArgumentException("Operator " + operator + " is not supported for boolean!");
			}
		}

		throw new IllegalArgumentException("Operator " + operator + " is not supported for " + pOperand1 + " and " + pOperand2);
	}
	
	/**
	 * Parses a parameter list.
	 * 
	 * @param pTags the tags to parse.
	 * @return the parameter list.
	 */
	private int[] parseDimensions(List<Object> pTags)
	{
		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		
		do
		{
			pTags.remove(0); // [
			
			Object tag = pTags.get(0); // ] or value
			
			if ("]".equals(tag))
			{
				dimensions.add(Integer.valueOf(-1));
			}
			else
			{
				dimensions.add((Integer)parseParameter(pTags));
			}
			if (pTags.size() == 0 || !"]".equals(pTags.get(0)))
			{
	            throw new IllegalArgumentException("Missing bracket close ']' for bracket open '['!");
			}
			pTags.remove(0); // ]
		}
		while (pTags.size() > 0 && "[".equals(pTags.get(0)));

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
	 * @param pTags the tags.
	 * @param pDimension the dimension.
	 * @param pComponentType the component type.
	 * @return the array definition.
	 */
	private Object parseArrayData(List<Object> pTags, int pDimension, Class<?> pComponentType)
	{
		pDimension--;
		if (pDimension <= 0)
		{
			return parseParameters(pTags, pComponentType);
		}
		else
		{
			List<Object> result = new ArrayList<Object>();
			
			if (pTags.size() > 0)
			{
				result.add(parseArrayData((List<Object>)pTags.remove(0), pDimension, pComponentType));
				
				while (pTags.size() > 0)
				{
					pTags.remove(0); // ,
					
					result.add(parseArrayData((List<Object>)pTags.remove(0), pDimension, pComponentType));
				}
			}

			return result.toArray((Object[])Array.newInstance(getComponentType(pComponentType, pDimension), result.size()));
		}
	}
	
	/**
	 * Parses a parameter list.
	 * 
	 * @param pTags the tags to parse.
	 * @param pComponentType the ComponentType of the Array.
	 * @return the parameter list.
	 */
	private Object parseParameters(List<Object> pTags, Class<?> pComponentType)
	{
		List<Object> result = new ArrayList<Object>();
		
		if (pTags.size() > 0)
		{
			result.add(parseParameterWithOperators(pTags));
			
			while (pTags.size() > 0 && ",".equals(pTags.get(0)))
			{
				pTags.remove(0);
				
				result.add(parseParameterWithOperators(pTags));
			}
		}
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
	 * @param pTags the tags to parse.
	 * @return the result.
	 */
	private Object parseCallsOrFields(Object pInstance, List<Object> pTags)
	{
		while (pTags.size() > 0 && ".".equals(pTags.get(0)))
		{
			pInstance = parseCallOrField(pInstance, pInstance.getClass(), pTags);
		}

		return pInstance;
	}

	/**
	 * Parses one call or field, static or non static.
	 * 
	 * @param pInstance the instance if non static.
	 * @param pClass the class.
	 * @param pTags the tags to parse.
	 * @return the result.
	 */
	private Object parseCallOrField(Object pInstance, Class<?> pClass, List<Object> pTags)
	{
		pTags.remove(0);
		
		String callOrField = (String)pTags.remove(0);
		
		if (pTags.size() > 0 && pTags.get(0) instanceof List<?>)
		{
			checkMethodAllowed(pClass, callOrField);

			Object[] parameters = (Object[])parseParameters((List<Object>)pTags.remove(0), Object.class);
			if (parameters.length == 1 && parameters[0] == null)  // Reflective maps Object[] {null} to first Parameter is null.
			{
				parameters = null;
			}

			try
			{
				pInstance = Reflective.call(pInstance, pClass, false, callOrField, parameters);
			}
			catch (Throwable pThrowable)
			{
				throw new IllegalArgumentException("Method call " + callOrField + "(...) failed!", pThrowable);
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
	 * @param pTags the tags to parse.
	 */
	private void parseGenerics(List<Object> pTags)
	{
		if ("<".equals(pTags.get(0)))
		{
			pTags.remove(0);
			
			int count = 1;
			
			while (count > 0 && pTags.size() > 0)
			{
				Object tag = pTags.remove(0);
				
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
	
}	// SimpleJavaSource
