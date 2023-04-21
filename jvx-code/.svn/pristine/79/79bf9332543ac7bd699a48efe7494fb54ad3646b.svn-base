/*
 * Copyright 2015 SIB Visions GmbH
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
 * 01.02.2015 - [JR] - creation
 */
package javax.rad.io;

import java.io.IOException;

/**
 * The <code>IUploadExecutor</code> is an executor for uploads of {@link IFileHandle}s to remote
 * servers.
 * 
 * @author René Jahn
 */
public interface IUploadExecutor
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Writes the given file handle to a remote server.
     *  
     * @param pFileHandle the file handle
     * @return the {@link RemoteFileHandle}
     * @throws IOException if upload failed
     */
    public RemoteFileHandle writeContent(IFileHandle pFileHandle) throws IOException;
    
}   // IUploadExecutor
