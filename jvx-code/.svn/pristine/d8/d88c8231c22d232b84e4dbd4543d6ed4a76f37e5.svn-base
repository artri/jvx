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
import java.io.InputStream;

/**
 * The <code>IDownloadExecutor</code> is an executor for downloads of {@link RemoteFileHandle} from remote
 * servers.
 *  
 * @author René Jahn
 */
public interface IDownloadExecutor
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Method definitions
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Reads the content from the remote server as stream.
     * 
     * @param pFileHandle the file handle
     * @return the content stream
     * @throws IOException if creating content stream failed
     */
    public InputStream readContent(RemoteFileHandle pFileHandle) throws IOException;
    
    /**
     * Gets the expected content length.
     * 
     * @param pFileHandle the file handle
     * @return the content length, <code>-1</code> if unknown
     * @throws IOException if getting content length failed
     */
    public long getContentLength(RemoteFileHandle pFileHandle) throws IOException;
    
}   // IDownloadExecutor
