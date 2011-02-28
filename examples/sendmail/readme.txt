
                                 seam-mail-examples-sendmail

 Source archetype: jboss-javaee6-webapp

 What is it?
 ===========

 This is your project! It's a sample, deployable Maven 2 project to help you
 get your foot in the door developing with Java EE 6. This project is setup to
 allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0,
 EJB 3.1 and JPA 2.0) that can run on a certified application server (Complete
 or Web Profile). It includes a persistence unit and some sample persistence
 and transaction code to help you get your feet wet with database access in
 enterprise Java. 

 System requirements
 ===================

 All you need to run this project is Java 5.0 (Java SDK 1.5) or greator and
 Maven 2.0.10 or greater. This application is setup to be run on a Java EE 6
 application server. We've tested it on GlassFish 3 and JBoss AS 6.0.

 Please note that Maven 2 project needs to use the JBoss Nexus Maven repository
 because there are certain Java EE API JARs that are not yet publised to the
 Maven Central Repository (see https://jira.jboss.org/jira/browse/WELD-222)
 The testing framework used by the project, Arquillian, is also only available
 in the JBoss repository.

 Deploying the application
 =========================

 To deploy the application to JBoss AS (standalone), first make sure that the
 JBOSS_HOME environment variable points to a JBoss AS 6.0 installation.

 Alternatively, you can set the location of JBoss AS using the following
 profile defintion in the .m2/settings.xml file in your home directory:

<?xml version="1.0" encoding="UTF-8"?>
<settings
   xmlns="http://maven.apache.org/POM/4.0.0"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

   <profiles>
      <profile>
         <id>environment</id>
         <activation>
            <activeByDefault>true</activeByDefault>
         </activation>
         <properties>
            <jboss.home>/path/to/jboss-as-6.0.0.M3</jboss.home>
         </properties>
      </profile>
   </profiles>
   
</settings>

 Once you've set either the JBOSS_HOME environment variable or the jboss.home
 Maven property, you can deploy to JBoss AS by executing the following command:

  mvn package jboss:hard-deploy

 This will deploy two artifacts, target/seam-mail-examples-sendmail.war and
 default-ds.xml. The latter installs a data source named jdbc/__default.

 You can also set jboss.home on the commandline:

  mvn package jboss:hard-deploy -Djboss.home=/path/to/jboss-as-6.0.0.M3

 Start JBoss AS. The application will be running at the following URL:

  http://localhost:8080/seam-mail-examples-sendmail/home.jsf

 Note: If you are using JBoss AS 6.0.0.M3, you may encounter the exception
 described in https://jira.jboss.org/browse/WELD-448 if you attempt to access
 to root context path (i.e., /).
  
 To undeploy from JBoss AS, run this command:

  mvn jboss:hard-undeploy

 If you want to deploy to GlassFish, first produce the archive to deploy:

  mvn package
 
 There are several ways to deploy the archive to GlassFish. The recommended
 approach is to open the project in NetBeans 6.8, right-click on the project
 and select "Run" from the context menu. That starts JavaDB, GlassFish and
 deploys the application.

 You can also start GlassFish from the commandline. Change to the glassfish/bin
 directory in the GlassFish install root and run these three commands:

  asadmin start-database
  asadmin start-domain domain1

 Now you can either deploy the target/seam-mail-examples-sendmail.war through the web-based
 GlassFish admininstration console, or you can again use asadmin:

  asadmin /path/to/project/target/seam-mail-examples-sendmail.war

 Importing the project into an IDE
 =================================

 If you created the project using the Maven 2 archetype wizard in your IDE
 (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
 already have an IDE project.

 If you created the project from the commandline using archetype:generate, then
 you need to bring the project into your IDE. If you are using NetBeans 6.8 or
 IntelliJ IDEA 9, then all you have to do is open the project as an existing
 project. Both of these IDEs recognize Maven 2 projects natively.

 To import into Eclipse, you first need to install the m2eclipse plugin. To get
 started, add the m2eclipse update site (http://m2eclipse.sonatype.org/update/)
 to Eclipse and install the m2eclipse plugin and required dependencies. Once
 that is installed, you'll be ready to import the project into Eclipse.

 Select File > Import... and select "Import... > Maven Projects" and select
 your project directory. m2eclipse should take it from there.

 Once in the IDE, you can execute the Maven commands through the IDE controls
 to deploy the application to a container.

 Downloading the sources and Javadocs
 ====================================

 If you want to be able to debug into the source code or look at the Javadocs
 of any library in the project, you can run either of the following two
 commands to pull them into your local repository. The IDE should then detect
 them.

  mvn dependency:sources
  mvn dependency:resolve -Dclassifier=javadoc

 Resources
 =========

 Weld archetypes:
 -  Quickstart:        http://seamframework.org/Documentation/WeldQuickstartForMavenUsers
 -  Issue tracker:     https://jira.jboss.org/jira/browse/WELDRAD
 -  Source code:       http://anonsvn.jboss.org/repos/weld/archetypes
 -  Forums:            http://seamframework.org/Community/WeldUsers
 JSR-299 overview:     http://seamframework.org/Weld
 JSF community site:   http://www.javaserverfaces.org

Testing
=============
mvn test -Pweld-ee-embedded-1.1 -- Currently Broken
mvn test -Pjbossas-remote-6
