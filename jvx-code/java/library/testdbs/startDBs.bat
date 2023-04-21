@ECHO OFF
rem Copyright 2009 SIB Visions GmbH
rem
rem Licensed under the Apache License, Version 2.0 (the "License"); you may not
rem use this file except in compliance with the License. You may obtain a copy of
rem the License at
rem 
rem http://www.apache.org/licenses/LICENSE-2.0
rem 
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
rem WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
rem License for the specific language governing permissions and limitations under
rem the License.

java -Xmx512M -cp ../lib/hsqldb.jar org.hsqldb.Server -database.0 file:test/testdb -dbname.0 testdb -database.1 file:demo/demodb -dbname.1 demodb -database.2 file:persons/personsdb -dbname.2 personsdb