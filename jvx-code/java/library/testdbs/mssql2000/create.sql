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


-- Table: "TEST"
-- DROP TABLE "TEST";

CREATE TABLE TEST
(
  ID numeric(18) NOT NULL IDENTITY,
  NAME varchar(100),
  CONSTRAINT TEST_PK PRIMARY KEY (ID)
);

-- Table: "TEST_SORT"
-- DROP TABLE "TEST_SORT";

CREATE TABLE TEST_SORT
(
  ID numeric(18) NOT NULL IDENTITY,
  NAME varchar(100),
  SORT numeric(18),
  CONSTRAINT TEST_SORT_PK PRIMARY KEY (ID)
);

-- Table: DETAIL

-- DROP TABLE TEST_DEFAULTS;

CREATE TABLE test_defaults
(
  active char(1) DEFAULT ('N'),
  numberval numeric(18) default ((1234)),
  dateval datetime DEFAULT ('2001-01-01'),
  datetimeval datetime DEFAULT ('2010-01-01 12:00:00'),
  text varchar(50) DEFAULT ('TEXT Test'),
  checkme char(1) default ('N')
);

ALTER TABLE test_defaults ADD CONSTRAINT TDEF_ACTIVE_CHECK CHECK (active = 'Y' or active = 'N')
ALTER TABLE test_defaults ADD CONSTRAINT TDEF_CHECKME_CHECK CHECK(checkme = 'J' and text = 'N')

-- Table: DETAIL

-- DROP TABLE DETAIL;

CREATE TABLE DETAIL
(
  ID numeric(18) NOT NULL IDENTITY,
  NAME varchar(100),
  TEST_ID numeric(18),
  CONSTRAINT DETAIL_PK PRIMARY KEY (ID),
  CONSTRAINT TEST_FK FOREIGN KEY (TEST_ID)
      REFERENCES TEST (ID) 
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: ADRESSEN

-- DROP TABLE ADRESSEN;

CREATE TABLE ADRESSEN
(
  ID numeric(18) NOT NULL IDENTITY,
  POST_ID numeric(18) NOT NULL,
  STRA_ID numeric(18) NOT NULL,
  HAUSNUMMER numeric(18) NOT NULL,
  TUERNUMMER numeric(18),
  STIEGE numeric(18),
  CONSTRAINT ADRESSEN_PK PRIMARY KEY (ID)
);
