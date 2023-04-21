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
 * 21.12.2009 - [HM] - creation
 */
package javax.rad.type;

/**
 * The <code>Type</code> is a platform independent definition of standard types.
 * 
 * @param <T> the type.
 * 
 * @author Martin Handsteiner
 */
public interface IType<T>
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** The type is unknown. */
	public static final UnknownType UNKNOWN_TYPE = new UnknownType();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns the class used by this type.
	 * 
	 * @return the class used by this type.
	 */
	public abstract Class<T> getTypeClass();
	
	/**
	 * Converts the object to an <code>Object</code> which is an instance of type class.
	 * 
	 * @param pObject the <code>Object</code> to convert
	 * @return the <code>Object</code> which is an instance of type class.
	 */
	public abstract T valueOf(Object pObject);

	/**
	 * Converts the object to an <code>Object</code> which is an instance of type class.
	 * 
	 * @param pObject the <code>Object</code> to convert
	 * @return the <code>Object</code> which is an instance of type class.
	 */
	public abstract T validatedValueOf(Object pObject);

	/**
	 * Returns the hash code of the given object.
	 * 
	 * @param pObject the object.
	 * @return the hash code.
	 */
	public abstract int hashCode(T pObject);
	
	/**
	 * Returns true, if the two objects are equal.
	 * 
	 * @param pObject1 the first object.
	 * @param pObject2 the second object.
	 * @return true, if the two objects are equal.
	 */
	public abstract boolean equals(T pObject1, Object pObject2);
	
	/**
	 * Compares two objects. 
	 * If the first object is smaller than the second object a value &lt; 0 is returned.
	 * If the two objects are equal than 0 is returned.
	 * If the first object is greater than the second object a value &gt; 0 is returned.
	 * 
	 * @param pObject1 the first object.
	 * @param pObject2 the second object.
	 * @return &lt; 0 if pObject1 is smaller, 0 if equal and &gt; 0 if it is greater then pObject2
	 */
	public abstract int compareTo(T pObject1, Object pObject2);
	
	/**
	 * Converts the object to a String.
	 * 
	 * @param pObject the object.
	 * @return the <code>String</code> representation of the <code>Object</code>.
	 */
	public abstract String toString(T pObject);
	
}	// IType
