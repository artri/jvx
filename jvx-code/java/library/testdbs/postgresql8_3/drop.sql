-- Copyright 2014 SIB Visions GmbH
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

drop type mood;
drop type yesno;

drop table TEST;
drop table DETAIL;
drop table ADRESSEN;
drop table TEST_DEFAULTS
drop table currencies
drop table countries
drop table users;
drop table person;

drop trigger test_trigger on test;
drop function test_trigger();
drop trigger detail_trigger on detail;
drop function detail_trigger();
drop trigger adressen_trigger on adressen;
drop function adressen_trigger();
drop function getVIP();
drop function testprocedure();

