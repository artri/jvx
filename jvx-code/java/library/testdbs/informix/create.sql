-- Copyright 2021 SIB Visions GmbH
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

-- Create table
create table USERS
(
  ID               DECIMAL(16) primary key constraint USER_PK not null,
  USERNAME         VARCHAR(200) unique constraint USER_UK not null,
  PASSWORD         VARCHAR(200),
  CHANGE_PASSWORD  CHAR(1) default 'N' not null,
  CHANGE_PASSWORD2 CHAR(1) default 'N' not null,
  ACTIVE           CHAR(1) default 'Y' not null,
  VALID_FROM       DATE,
  VALID_TO         DATE,
  CREATED_BY       VARCHAR(200) not null,
  CREATED_ON       DATE not null,
  CHANGED_BY       VARCHAR(200) not null,
  CHANGED_ON       DATE not null,
  TITLE            VARCHAR(64),
  FIRST_NAME       VARCHAR(200),
  LAST_NAME        VARCHAR(200),
  EMAIL            VARCHAR(200),
  PHONE            VARCHAR(200),
  MOBIL            VARCHAR(200),
  FAX              VARCHAR(200),
  ABTEILUNG        VARCHAR(64),
  STRASSE          VARCHAR(64),
  PLZ              VARCHAR(6),
  ORT              VARCHAR(64)
);

-- Create/Recreate check constraints 
alter table USERS add constraint check (active IN ('Y', 'N')) constraint USER_ACTIVE_CHECK;

alter table USERS add constraint check (CHANGE_PASSWORD IN ('Y', 'N')) constraint USER_CHANGE_PASSWORD_CHECK;

alter table USERS add constraint check (CHANGE_PASSWORD2 = 'Y' OR CHANGE_PASSWORD2 = 'N') constraint USER_CHANGE_PASSWORD2_CHECK;


CREATE TABLE TEST
(
  ID decimal(18) NOT null PRIMARY key CONSTRAINT TEST_PK,
  NAME varchar(100)
);

CREATE TABLE TEST_SORT
(
  ID decimal(18) NOT null PRIMARY key CONSTRAINT TEST_SORT_PK,
  NAME varchar(100),
  SORT decimal(18)
);

CREATE TABLE DETAIL
(
  ID decimal(18) NOT null PRIMARY key CONSTRAINT DETAIL_PK,
  NAME varchar(100),
  TEST_ID decimal(18)
);

alter table DETAIL add constraint foreign key (TEST_ID) references TEST(ID) CONSTRAINT TEST_FK;

CREATE TABLE ADRESSEN
(
  ID decimal(18) NOT null PRIMARY key CONSTRAINT ADRESSEN_PK,
  POST_ID decimal(18) NOT NULL,
  STRA_ID decimal(18) NOT NULL,
  HAUSNUMMER decimal(18) NOT NULL,
  TUERNUMMER decimal(18),
  STIEGE decimal(18)
);

CREATE TABLE TEST_DEFAULTS
(
  ACTIVE CHAR(1) DEFAULT 'N' NOT NULL, 
  datetimeval DATETIME YEAR TO SECOND DEFAULT DATETIME (2021-05-05 15:35:00) YEAR TO SECOND, 
  NUMBERVAL DECIMAL(11,0) DEFAULT 1234 NOT NULL, 
  TEXT VARCHAR(50) DEFAULT 'TEXT Test' NOT NULL, 
  dateval date DEFAULT '2001-01-01'
); 

drop sequence SEQ_TEST_ID;

-- Create sequence 
create sequence SEQ_TEST_ID
minvalue 0
maxvalue 999999999999999999
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