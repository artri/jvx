The JVx' first app is a simple application with JVx.

It's a fully configured Eclipse project. Simply checkout the repository and import the project into Eclipse.
The project source is available in src.client and src.server folders. We use separate source folders for 
separation of client and server code. This is very helpful for building the application.

To start the application, simply launch: JVxFirstApp or JVxFirstApp.launch


The repository/project contains an ANT build file (build.xml). 

  * The task "start.complete" creates all relevant files for the deployment of the application.
  * The task "start.updatelibs" is a task for updating application libs but it's an internal task and
    requires JVx repository checked out in a specific directory (for internal use only)
    
The build creates a war file for Java application servers. The war archive contains the server side of the
application and the client. The client is currently an Applet (only supported from Internet Explorer) or a
JNLP application. The JNLP application needs a valid code signing certificate.

The pre-built war archive already contains a signed application. The official SIB Visions certificate was
used.

If you want to build the first app on your own, simply change the properties:

  * sign.alias
  * sign.storepass
  * sign.keystore
  
in the build.xml. Without this properties, the build won't add JNLP start files. The JNLP start files are
available in the deployment directory:

  * applet.jnlp
  * application.jnlp
  
and are pre-configured for http://localhost:8080/jvxfirstapp. If you deploy to a different URL, simply change
the JNLP files.

Start URLs:

Applet: http://localhost:8080/jvxfirstapp/
JNLP: http://localhost:8080/jvxfirstapp/application.jnlp

Login: admin / admin
(see users.xml)


Requirements

* Java 6 or later
* Java 7 or later (if you sign the application)
* start the database (see db/startHsqlDB.bat)

Optional

* Tomcat 7 or 8


Details:
http://www.sibvisions.com/en/jvxmdocumentation/86-jvxfirstappl