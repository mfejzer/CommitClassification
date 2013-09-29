CommitClassification
====================
This application is "source code changes classifier" working with Gerrit.
The idea of a classifier was proposed in "Classifying software changes: Clean or buggy?" by Sunghun Kim, E James Whitehead, and Yi Zhang. 
The classifier is used to detect potential programming errors, based on those that occurred earlier during the project development. 
A history of existing errors is retrieved from the source code repository of the project.
Application works on Git repositories that are under the control of code review system - Gerrit. 

Minimal setup:
1) Working Gerrit (tested on 2.6-rc0) instance with imported project
2) Gerrit user with:
2.1) ssh key without password
2.2) permissions to review code
2.3) permissions to list available projects
2.4) permissions to receive Gerrit events via "ssh -p <port> <host> gerrit stream-events"
3) Project history should contain commits with metadata about fixed bugs (see BugFixDetectionList.scala)
4) Installed Java and Maven (at least 3.0.3)

How to complie (in folder scmparser):
$ mvn clean install 

How to run (in folder scmparser) and receive message describing parameters:
$ java -jar target/scmparser-1.0-SNAPSHOT.jar --help
<<<<<<< HEAD
=======

What parameters mean:
  -d, --directory  <arg>                Directory to keep cloned projects
  -h, --hostname  <arg>                 Gerrit hostname
  -p, --port  <arg>                     Gerrit port
  -r, --repeatLearnAfterHours  <arg>    Directory refresh interval
  -u, --user  <arg>                     Gerrit user
>>>>>>> Create README.md
