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
package remote.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sibvisions.rad.remote.ISerializer;

/**
 * Simple <code>ISerializer</code> implementation which does the serialization
 * with {@link ObjectInputStream} and {@link ObjectOutputStream}.
 * 
 * @author René Jahn
 * @see ISerializer
 */
public class ObjectSerializer implements ISerializer
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public Object read(DataInputStream in) throws Exception
	{
		ObjectInputStream ois = new ObjectInputStream(in);
		
		return ois.readObject();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void write(DataOutputStream out, Object object) throws Exception
	{
		ObjectOutputStream oos = new ObjectOutputStream(out);
		
		oos.writeObject(object);
	}

}	// ObjectSerializer
