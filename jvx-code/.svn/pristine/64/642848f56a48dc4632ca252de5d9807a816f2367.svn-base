-- Copyright 2009 SIB Visions GmbH
--
-- Licensed under the Apache License, Version 2.0 (the "License"); you may not
-- use this file except in compliance with the License. You may obtain a copy of
-- the License at
-- 
-- http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
-- WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
-- License for the specific language governing permissions and limitations under
-- the License.

-------------------------------------------------------------------------------
-- Tables
-------------------------------------------------------------------------------

/*
 List of valid users authenticated with the DBSecurityManager
*/
CREATE TABLE USERS
(
 ID INTEGER IDENTITY, 
 USERNAME VARCHAR(255) NOT NULL,
 PASSWORD VARCHAR(255) NOT NULL,
 CHANGE_PASSWORD CHAR(1) DEFAULT 'N',
 VALID_FROM TIMESTAMP,
 VALID_TO TIMESTAMP,
 ACTIVE CHAR(1) DEFAULT 'Y',
 FIRST_NAME VARCHAR(20),
 LAST_NAME VARCHAR(20),
 EMAIL VARCHAR(255),
 PHONE VARCHAR(20),
 CONSTRAINT USR_NAME_UK UNIQUE(USERNAME)
)

CREATE TABLE AUTOLOGIN
(
  ID      INTEGER NOT NULL IDENTITY,
  USER_ID INTEGER NOT NULL,
  LOGINKEY VARCHAR(50) not null,
  CONSTRAINT AULI_UK UNIQUE (LOGINKEY),
  CONSTRAINT AULI_USER_ID_FK FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
)

CREATE TABLE ACCESS
(
 ID INTEGER IDENTITY,
 LIFECYCLENAME VARCHAR(255) NOT NULL,
 CONSTRAINT ACC_LIFECYCLENAME_UK UNIQUE(LIFECYCLENAME)
)

CREATE VIEW V_ACCESSRULES AS
SELECT u.USERNAME
      ,a.LIFECYCLENAME
  FROM USERS u,
       ACCESS a
 WHERE u.ACTIVE = 'Y'
   AND u.USERNAME != 'no_access'

-------------------------------------------------------------------------------
-- Configuration
-------------------------------------------------------------------------------

-- Test users
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) VALUES(1, 'rene', 'rene', 'René', 'Jahn');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) VALUES(2, 'martin', 'martin', 'Martin', 'Handsteiner');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) VALUES(3, 'roland', 'roland', 'Roland', 'Hörmann');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, CHANGE_PASSWORD) VALUES(4, 'change_pwd', 'change_pwd', 'Change', 'Password', 'Y');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, VALID_FROM) VALUES(5, 'valid_from', 'valid_from', 'Valid', 'From', '9999-11-01 12:00:00');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, VALID_FROM, VALID_TO) VALUES(6, 'valid_from_to', 'valid_from_to', 'Valid', 'From_To', '2008-11-01 12:00:00', '2008-11-01 13:00:00');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVE) VALUES(7, 'inactive', 'inactive', 'Inactive', 'User', 'N');
INSERT INTO USERS (ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVE) VALUES(8, 'no_access', 'no_access', 'NO', 'Access', 'Y');

INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.User');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('java.util.Vector');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.StorageDataBookTest');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.Company');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.Address');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.Bug136');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.Bug525');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.Bug659');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.QuickConnect');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.SessionIsolated');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.ScreenWithSessionIsolation');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.ScreenIsolation');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.SessionWithCallHandler');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.ScreenWithCallHandler');
INSERT INTO ACCESS (LIFECYCLENAME) VALUES ('demo.special.ScreenSessionWithCallHandler');