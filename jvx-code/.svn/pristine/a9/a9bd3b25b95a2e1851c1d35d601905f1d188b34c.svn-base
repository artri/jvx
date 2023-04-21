-- Copyright 2017 SIB Visions GmbH
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

create user test identified by test;

grant connect to test;
grant resource to test;
grant create any view to test;
grant create synonym to test;
grant create type to test;
grant create procedure to test;

-- Create table
create table USERS
(
  ID               NUMBER(16) not null,
  USERNAME         VARCHAR2(200) not null,
  PASSWORD         VARCHAR2(200),
  CHANGE_PASSWORD  CHAR(1) default 'N' not null,
  CHANGE_PASSWORD2 CHAR(1) default 'N' not null,
  ACTIVE           CHAR(1) default 'Y' not null,
  VALID_FROM       DATE,
  VALID_TO         DATE,
  CREATED_BY       VARCHAR2(200) not null,
  CREATED_ON       DATE not null,
  CHANGED_BY       VARCHAR2(200) not null,
  CHANGED_ON       DATE not null,
  TITLE            VARCHAR2(64),
  FIRST_NAME       VARCHAR2(200),
  LAST_NAME        VARCHAR2(200),
  EMAIL            VARCHAR2(200),
  PHONE            VARCHAR2(200),
  MOBIL            VARCHAR2(200),
  FAX              VARCHAR2(200),
  ABTEILUNG        VARCHAR2(64),
  STRASSE          VARCHAR2(64),
  PLZ              VARCHAR2(6),
  ORT              VARCHAR2(64)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table USERS
  add constraint USER_PK primary key (ID);
  
alter table USERS
  add constraint USER_UK unique (USERNAME);
-- Create/Recreate check constraints 
alter table USERS
  add constraint USER_ACTIVE_CHECK
  check (active IN ('Y', 'N'));
alter table USERS
  add constraint USER_CHANGE_PASSWORD_CHECK
  check (CHANGE_PASSWORD IN ('Y', 'N'));
alter table USERS
  add constraint USER_CHANGE_PASSWORD2_CHECK
  check (CHANGE_PASSWORD2 = 'Y' OR CHANGE_PASSWORD2 = 'N');


CREATE TABLE TEST
(
  ID numeric(18) NOT NULL,
  NAME varchar(100),
  CONSTRAINT TEST_PK PRIMARY KEY (ID)
);

CREATE TABLE TEST_SORT
(
  ID numeric(18) NOT NULL,
  NAME varchar(100),
  SORT numeric(18),
  CONSTRAINT TEST_SORT_PK PRIMARY KEY (ID)
);

CREATE TABLE DETAIL
(
  ID decimal(18) NOT NULL,
  NAME varchar(100),
  TEST_ID decimal(18),
  CONSTRAINT DETAIL_PK PRIMARY KEY (ID),
  CONSTRAINT TEST_FK FOREIGN KEY (TEST_ID)
      REFERENCES TEST (ID)
);

CREATE TABLE ADRESSEN
(
  ID decimal(18) NOT NULL,
  POST_ID decimal(18) NOT NULL,
  STRA_ID decimal(18) NOT NULL,
  HAUSNUMMER decimal(18) NOT NULL,
  TUERNUMMER decimal(18),
  STIEGE decimal(18),
  CONSTRAINT ADRESSEN_PK PRIMARY KEY (ID)
);

CREATE TABLE TEST_DEFAULTS
(
  ACTIVE CHAR(1) DEFAULT 'N' NOT NULL, 
  DATETIMEVAL date DEFAULT to_date('2010-01-01 12:00:00', 'yyyy-dd-MM HH:mi:ss') NOT NULL, 
  NUMBERVAL DECIMAL(11,0) DEFAULT 1234 NOT NULL, 
  TEXT VARCHAR(50) DEFAULT 'TEXT Test' NOT NULL, 
  DATEVAL DATE DEFAULT to_date('2001-01-01', 'yyyy-dd-MM') NOT NULL 
); 


-- Create sequence 
create sequence SEQ_TEST_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 0
increment by 1
nocache
order;

create or replace trigger TR_TEST_BR_I
  before insert on test  
  for each row

begin

  if (:new.id is null) then
    select seq_test_id.nextval into :new.id from dual;
  end if;
  
end;

-- Create sequence 
create sequence SEQ_TEST_SORT_ID
minvalue 0
maxvalue 999999999999999999999999999
start with 0
increment by 1
nocache
order;

create or replace trigger TR_TEST_SORT_BR_I
  before insert on test_sort
  for each row

begin

  if (:new.id is null) then
    select seq_test_sort_id.nextval into :new.id from dual;
  end if;
  
end; 

create or replace function execFunction(pNumber in out number, pInText in varchar2, pOutText out varchar2) return varchar2 is
  res varchar2(200);
  nr number := pNumber;
begin
  pOutText := 'Out: '|| pOutText ||' In: '|| pInText;

  pNumber := pNumber + pNumber;

  return 'IN-Param Nr: '|| nr;
end;

create or replace procedure execProcedure(pNumber in out number, pInText in varchar2, pOutText out varchar2) is
  nr number := pNumber;
begin
  pOutText := 'Out: '|| pOutText ||' In: '|| pInText;

  pNumber := pNumber + pNumber; 
end;

create or replace synonym SYN_TEST for TEST.DETAIL;