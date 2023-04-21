# Copyright 2017 SIB Visions GmbH
# 
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
# 
# http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.


Provided war file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The precreated DemoERP.war already contains a signed JNLP application. The official 
SIB Visions certificate was used.

The database configuration is available in WEB-INF/rad/apps/DemoERO/config.xml and
is set to:

<url>jdbc:mysql://localhost:3306/demoerp</url>      
<username>demoerp</username>
<password>demoerp</password> 



Manual setup
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Configuration
~~~~~~~~~~~~~

The application needs a database. A script for mysql is included in the file db/db.sql. Simply
execute the command.

The database is configured in config.xml.


Deployment
~~~~~~~~~~

Use ANT with build.xml

Start target: start.webcontent

Afterwards, reload "WebContent" folder and start the application server in Eclipse.


The target: start.complete (= default) will create a war file for manual deployment.
But check the database configuration in WEB-INF/rad/apps/DemoERP/config.xml.


If you have a valid code signing certificate, set sign.* properties in build.xml to apply the
certificate. Otherwise the application won't be signed.


Start URLs
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

WebUI:
http://localhost:8080/DemoERP/web/ui

JNLP Application: 
http://localhost:8080/DemoERP/application.jnlp

Applet (only Internet Explorer):           
http://localhost:8080/DemoERP/


Login: 
admin / admin
manager / manager
sales / sales
mobil / mobil