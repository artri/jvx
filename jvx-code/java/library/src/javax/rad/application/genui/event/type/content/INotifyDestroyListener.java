/*
 * Copyright 2020 SIB Visions GmbH
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
 * 21.07.2020 - [JR] - creation
 */
package javax.rad.application.genui.event.type.content;

import javax.rad.application.IContent;

/**
 * The <code>INotifyVisibleListener</code> receives event about {@link IContent#notifyDestroy()}.
 * 
 * @author René Jahn
 */
public interface INotifyDestroyListener 
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Method definitions
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Invoked when a content is destroyed.
	 * 
	 * @param pContent the content
	 */	
	public void notifyContentDestroy(IContent pContent);

}	// INotifyDestroyListener
