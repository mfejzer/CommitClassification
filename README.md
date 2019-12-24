review_bug_detection
====================
This repository contains implementation of experiments from "Supporting code review by automatic detection of potentially buggy changes" paper. Java 8 and Apache Maven are required to compile the project.

This application is "source code changes classifier" working with Git repositories either as standalone application or with Gerrit code review system.
The classifier is used to detect potential programming errors, based on those that occurred earlier during the project development. 
A history of existing errors is retrieved from the source code repository of the project.
The idea of a classifier was proposed in "Classifying software changes: Clean or buggy?" by Sunghun Kim, E James Whitehead, and Yi Zhang.
This project differs from Kim et al. proposal using only subset of features directly available to reviewer, and utilization of instance weight scheme to handle disproportion between classes.

# How to complie:
$ mvn clean install 

# How to run replication
$ java -jar target/scmparser-replication.jar --help

# Minimal setup for Gerrit integration:
* Working Gerrit (tested on 2.6-rc0) instance with imported projects
* Gerrit user with:
  * ssh key without password
  * permissions to review code
  * permissions to list available projects
  * permissions to receive Gerrit events via "ssh -p <port> <host> gerrit stream-events"
* Project history should contain commits with metadata about fixed bugs (see BugFixDetectionList.scala)

# How to run Gerrit integration:
$ java -jar scmparser-gerrit.jar --help
