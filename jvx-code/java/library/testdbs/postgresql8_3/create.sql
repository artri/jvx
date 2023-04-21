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

create language plpgsql;

-- Table: "TEST"

CREATE TABLE TEST
(
  "ID" numeric(18) NOT NULL,
  "NAME" varchar(100),
  CONSTRAINT TEST_PK PRIMARY KEY ("ID")
);

-- Table: "TEST"

CREATE TABLE TEST_SORT
(
  "ID" numeric(18) NOT NULL,
  "NAME" varchar(100),
  "SORT" numeric(18),
  CONSTRAINT TEST_SORT_PK PRIMARY KEY ("ID")
);

-- Table: DETAIL

CREATE TABLE DETAIL
(
  ID numeric(18) NOT NULL,
  NAME varchar(100),
  TEST_ID numeric(18),
  CONSTRAINT DETAIL_PK PRIMARY KEY (ID),
  CONSTRAINT TEST_FK FOREIGN KEY (TEST_ID)
      REFERENCES TEST ("ID") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: ADRESSEN

CREATE TABLE ADRESSEN
(
  ID numeric(18) NOT NULL,
  POST_ID numeric(18) NOT NULL,
  STRA_ID numeric(18) NOT NULL,
  HAUSNUMMER numeric(18) NOT NULL,
  TUERNUMMER numeric(18),
  STIEGE numeric(18),
  CONSTRAINT ADRESSEN_PK PRIMARY KEY (ID)
);

-- Table: TEST_DEFAULTS

CREATE TABLE TEST_DEFAULTS
(
  ACTIVE CHAR(1) NOT NULL DEFAULT 'N' , 
  DATETIMEVAL timestamp NOT NULL DEFAULT '2010-01-01 12:00:00' , 
  NUMBERVAL DECIMAL(11,0) NOT NULL DEFAULT 1234 , 
  TEXT VARCHAR(50) NOT NULL DEFAULT 'TEXT Test' , 
  DATEVAL DATE NOT NULL DEFAULT '2001-01-01' 
); 

-- Table: currencies

CREATE TABLE currencies (
   id SERIAL NOT NULL,
   name VARCHAR(50),
   PRIMARY KEY (id)
);

-- Table: countries

CREATE TABLE countries (
   id SERIAL NOT NULL,
   country VARCHAR(50),
   currency_id INTEGER,
   PRIMARY KEY (id),
   FOREIGN KEY(currency_id) REFERENCES currencies (id)
);

-- Table: users

CREATE TABLE users (
   id SERIAL NOT NULL,
   firstname VARCHAR(50),
   lastname VARCHAR(50),
   company VARCHAR(100),
   email VARCHAR(50),
   loginname VARCHAR (30),
   password VARCHAR(30),
   street VARCHAR(50),
   postalcode VARCHAR(5),
   city VARCHAR(30),
   country_id INTEGER,
   PRIMARY KEY (id),
   FOREIGN KEY(country_id) REFERENCES countries (id)
);

-- Table: person

CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');
CREATE TYPE yesno AS ENUM ('Y', 'N');

CREATE TABLE person (
   name text,
   current_mood mood,
   active yesno
);

INSERT INTO person VALUES ('Moe', 'happy', 'N');

CREATE SEQUENCE ADRESSEN_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
  CYCLE;
  
CREATE SEQUENCE DETAIL_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1
  CYCLE;
  
CREATE SEQUENCE TEST_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3
  CACHE 1
  CYCLE;

CREATE SEQUENCE TEST_SORT_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
  CYCLE;

CREATE FUNCTION test_trigger() RETURNS trigger AS $test_trigger$
BEGIN
if (NEW."ID" is null) then	
  NEW."ID" := nextval('TEST_SEQ');
end if;
RETURN NEW;
END;
$test_trigger$ LANGUAGE plpgsql;

CREATE TRIGGER test_trigger BEFORE INSERT ON TEST
FOR EACH ROW EXECUTE PROCEDURE test_trigger();

CREATE FUNCTION test_sort_trigger() RETURNS trigger AS $test_sort_trigger$
BEGIN
if (NEW."ID" is null) then	
  NEW."ID" := nextval('TEST_SORT_SEQ');
end if;
RETURN NEW;
END;
$test_sort_trigger$ LANGUAGE plpgsql;

CREATE TRIGGER test_sort_trigger BEFORE INSERT ON TEST_SORT
FOR EACH ROW EXECUTE PROCEDURE test_sort_trigger();

CREATE FUNCTION detail_trigger() RETURNS trigger AS $detail_trigger$
BEGIN
if (NEW.ID is null) then	
  NEW.ID := nextval('DETAIL_SEQ');
end if;
RETURN NEW;
END;
$detail_trigger$ LANGUAGE plpgsql;

CREATE TRIGGER detail_trigger BEFORE INSERT ON DETAIL
FOR EACH ROW EXECUTE PROCEDURE detail_trigger();

CREATE FUNCTION adressen_trigger() RETURNS trigger AS $adressen_trigger$
BEGIN
if (NEW.ID is null) then	
  NEW.ID := nextval('ADRESSEN_SEQ');
end if;
RETURN NEW;
END;
$adressen_trigger$ LANGUAGE plpgsql;

CREATE TRIGGER adressen_trigger BEFORE INSERT ON ADRESSEN
FOR EACH ROW EXECUTE PROCEDURE adressen_trigger();

-- Table function test

create or replace function getVIP(pFrom numeric, pTo numeric) 
  returns table (res_name varchar(200), res_salary integer) 
  as $$
declare

   arrPerson varchar[] := array['1H', '2H', '3H', '1T', '3T', '2T'];
   arrSalary integer[] := array[100, 200, 300, 1000, 3000, 2000];

   i integer;
begin

  for i in 1..array_upper(arrPerson, 1)
  loop
    if ((pFrom <= 0 or arrSalary[i] >= pFrom) and (pTo <= 0 or arrSalary[i] <= pTo)) then
      res_name := arrPerson[i];
      res_salary := arrSalary[i];
      
      return next;
    end if;
  end loop;
  
end; $$
language 'plpgsql';

CREATE OR REPLACE FUNCTION testprocedure (
  a numeric,
  b numeric,
  out c numeric,
  out d numeric,
  inout e numeric
)
RETURNS record AS
$body$
DECLARE
BEGIN
  e := a + b + e;
  c := e + 1;
  d := e + 2;
END;
$body$
LANGUAGE 'plpgsql';

-- maybe not successful, if 'locale -a' doesn't contain the locale
ALTER DATABASE testdb SET lc_messages=en_us;

