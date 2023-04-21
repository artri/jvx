/*
 * Copyright 2016 SIB Visions GmbH
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
 * 28.01.2016 - [JR] - creation
 */
package com.sibvisions.rad.server.protocol;

/**
 * The <code>ICommandConstants</code> defines the standard protocol commands.
 * 
 * @author René Jahn
 */
public interface ICommandConstants
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constants
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //----------------------------------------------------------------
    // Database
    //----------------------------------------------------------------

    /** the insert command. */
    public static final String DB_INSERT                  = "INSERT";

    /** the fetch command. */
    public static final String DB_FETCH                   = "FETCH";

    /** the update command. */
    public static final String DB_UPDATE                  = "UPDATE";

    /** the delete command. */
    public static final String DB_DELETE                  = "DELETE";

    /** the get unique keys command. */
    public static final String DB_GET_UKS                 = "GET_UKS";

    /** the get primary key command. */
    public static final String DB_GET_PK                  = "GET_PK";

    /** the get foreign keys command. */
    public static final String DB_GET_FKS                 = "GET_FKS";

    /** the get default values command. */
    public static final String DB_GET_DEFAULTVALUES       = "GET_DEFAULTVALUES";

    /** the get allowed values command. */
    public static final String DB_GET_ALLOWEDVALUES       = "GET_ALLOWEDVALUES";

    /** the get column metadata command. */
    public static final String DB_GET_COLUMNMETADATA      = "GET_COLUMNMETADATA";

    /** the get tablesynonym command. */
    public static final String DB_GET_TABLESYNONYM        = "GET_TABLESYNONYM";

    /** the get table info command. */
    public static final String DB_GET_TABLEINFO           = "GET_TABLEINFO";

    /** the execute procedure command. */
    public static final String DB_EXEC_PROCEDURE          = "EXECUTE_PROCEDURE";

    /** the execute function command. */
    public static final String DB_EXEC_FUNCTION           = "EXECUTE_FUNCTION";

    /** the execute statement command. */
    public static final String DB_EXEC_STATEMENT          = "EXECUTE_STATEMENT";

    /** the execute query command. */
    public static final String DB_EXEC_QUERY              = "EXECUTE_QUERY";

    /** the execute sql command. */
    public static final String DB_EXEC_SQL                = "EXECUTE_SQL";

    
    //----------------------------------------------------------------
    // Storage
    //----------------------------------------------------------------

    /** the execute fetch command. */
    public static final String STORAGE_EXEC_FETCH         = "EXECUTE_FETCH";

    /** the execute refetch command. */
    public static final String STORAGE_EXEC_REFETCH       = "EXECUTE_REFETCH";

    /** the execute insert command. */
    public static final String STORAGE_EXEC_INSERT        = "EXECUTE_INSERT";

    /** the execute update command. */
    public static final String STORAGE_EXEC_UPDATE        = "EXECUTE_UPDATE";

    /** the execute delete command. */
    public static final String STORAGE_EXEC_DELETE        = "EXECUTE_DELETE";

    /** the storage open command. */
    public static final String STORAGE_OPEN               = "OPEN_STORAGE";

    /** the create automatic link command. */
    public static final String STORAGE_CREATE_AUTOLINK    = "CREATE_AUTOLINK";

    
    //----------------------------------------------------------------
    // Session Manager
    //----------------------------------------------------------------

    /** the master session command. */
    public static final String SESSMAN_MASTER             = "MASTER";

    /** the sub session command. */
    public static final String SESSMAN_SUB                = "SUB";

    /** the init properties command. */
    public static final String SESSMAN_INIT_PROPERTIES    = "INIT_PROPERTIES";

    /** the post create session command. */
    public static final String SESSMAN_POST_CREATE        = "POST_CREATE";

    /** the session destroy command. */
    public static final String SESSMAN_DESTROY            = "DESTROY";

    /** the validate sessions command. */
    public static final String SESSMAN_VALIDATE           = "VALIDATESESSIONS";

    
    //----------------------------------------------------------------
    // Session
    //----------------------------------------------------------------

    /** the init session command. */
    public static final String SESSION_INIT               = "INIT_SESSION";

    /** the configuration command. */
    public static final String SESSION_CONFIGURATION      = "CONFIGURATION";

    /** the authenticate command. */
    public static final String SESSION_AUTHENTICATE       = "AUTHENTICATE";

    /** the reset password command. */
    public static final String SESSION_RESETPASSWORD      = "RESETPASSWORD";

    /** the check access command. */
    public static final String SESSION_CHECK_ACCESS       = "CHECK_ACCESS";

    /** the call command. */
    public static final String SESSION_CALL               = "CALL";

    /** the call action command. */
    public static final String SESSION_CALL_ACTION        = "CALL_ACTION";

    /** the callback command. */
    public static final String SESSION_CALLBACK           = "CALLBACK";

    
    //----------------------------------------------------------------
    // Server
    //----------------------------------------------------------------

    /** the create session command. */
    public static final String SERVER_CREATE_SESSION      = "createSession";

    /** the create subsession command. */
    public static final String SERVER_CREATE_SUBSESSION   = "createSubSession";

    /** the destroy session command. */
    public static final String SERVER_DESTROY_SESSION     = "destroySession";

    /** the process command. */
    public static final String SERVER_PROCESS             = "process";

    /** the execute command. */
    public static final String SERVER_EXECUTE             = "execute";

    /** the execute callback command. */
    public static final String SERVER_EXEC_CALLBACK       = "executeCallBack";

    /** the execute action command. */
    public static final String SERVER_EXEC_ACTION         = "executeAction";

    /** the execute action callback command. */
    public static final String SERVER_EXEC_ACTIONCALLBACK = "executeActionCallBack";

    /** the set new password command. */
    public static final String SERVER_SET_NEWPASSWORD     = "setNewPassword";
    
    //----------------------------------------------------------------
    // REST
    //----------------------------------------------------------------
    
    /** the get command. */
    public static final String REST_GET    				  = "GET";
    
    /** the put command. */
    public static final String REST_PUT    				  = "PUT";
    
    /** the post command. */
    public static final String REST_POST    			  = "POST";

    /** the delete command. */
    public static final String REST_DELETE    			  = "DELETE";

    /** the options command. */
    public static final String REST_OPTIONS    			  = "OPTIONS";
    
    /** the fetch command. */
    public static final String REST_EXEC_FETCH    		  = "EXECUTE_FETCH";    

    /** the insert command. */
    public static final String REST_EXEC_INSERT    		  = "EXECUZTE_INSERT";   
    
    /** the update command. */
    public static final String REST_EXEC_UPDATE    		  = "EXECUTE_UPDATE";   
    
    /** the insert command. */
    public static final String REST_EXEC_DELETE    		  = "EXECUTE_DELETE";       

    /** the delete command. */
    public static final String REST_EXEC_METADATA  		  = "EXECUTE_METADATA";
    
}   // ICommandConstants
