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
 * 01.10.2008 - [HM] - creation
 */
package research;

import java.util.Enumeration;

import javax.rad.type.bean.Bean;

class DataSource
{
	
	public Enumeration select(String pSelect, Object... pParams)
	{
		return null;
	}
	
}

/**
 * Tests, how Typesave Cursors can be made.
 * 
 * @author Martin Handsteiner
 */
public class TypeSaveCursorTest
{
	

	private static DataSource dataSource;

	
	public static void main(String[] pArgs)
	{
		
		@SuppressWarnings("unused")
		class MyRecord 
		{
			private String name;
		
			public MyRecord(String pName)
			{
				name = pName;
			}
			
			public String getName()
			{
				return name;
			}
			public void setName(String pName)
			{
				name = pName;
			}
			public boolean isValid()
			{
				return true;
			}
		}
		try
		{
		  System.out.println(java.beans.Introspector.getBeanInfo(MyRecord.class));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		
		System.out.println(new Object[0][0].getClass().getName());
		System.out.println(new Object[0][0].getClass().getCanonicalName());
		System.out.println(new Object[0][0].getClass().getSimpleName());
		System.out.println(TypeSaveCursorTest.class.getSimpleName());
		
/*		MyRecord myRec = new MyRecord("Martin");
		
		PropertyDescriptor[] props = myRec.getPropertyDescriptors();
		
		for (int i = 0; i < props.length; i++)
		{
			String propertyName = props[i].getName();
			System.out.println(propertyName + "  " + myRec.get(propertyName) + "  " + props[i].getPropertyType());
		}
		
	*/	
		
		System.exit(0);
		
		Enumeration<MyRecord> cur1 = dataSource.select(
				"select * from" +
				"  from tabelle" +
				" where a = ?" +
				"   and b = ?", 
						  	"A", 
						  	Integer.valueOf(2));
	
		while (cur1.hasMoreElements())
		{
			MyRecord rec = cur1.nextElement();
		
			System.out.println(rec.getName());
		
		}
		
		Enumeration<Bean> cur2 = dataSource.select(
				"select * from" +
				"  from tabelle" +
				" where a = ?" +
				"   and b = ?", 
						  	"A", 
						  	Integer.valueOf(2));
	
		while (cur2.hasMoreElements())
		{
			Bean rec = cur2.nextElement();
		
			System.out.println(rec.get("NAME"));
		
		}
		
		
	}
	
	
}
