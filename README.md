# individual-monitoring-Belit
Individual monitoring dashboard app, Belit version with Hibernate ORM and support for assessments. 

Deployment on Glassfish instructions - AUTOMATIC (Using script - under Windows OS):
------------------------------------------------------------------------------------
1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Glassfish 4.1.1 (glassfish4 folder must be located in C:\glassfish-4.1.1\)
3. Run "setup_glassfish_windows_os" script for automatic setting up Glassfish server for C4A application.
4. Start Glassfish 4.1.1:
	asadmin start-domain
5. If you don't have, install now Apache Maven 3.3.9
6. Deploy application (run from project's root dir - "anagraph\"):
	mvn clean install --pl build glassfish:deploy
or, in case old version of app is already deployed on Glassfish 4.1.1:
	mvn clean install --pl build glassfish:redeploy
7. Open in browser:
http://localhost:8080/C4A-dashboard/

--------------------------------------------
Running unit tests from Eclipse IDE:

1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Eclipse Neon.1a Release (4.6.1)
3. Import project into workspace from file system using Import > Existing projects into workspace
4. Run unit tests from src/test/java packages

Note: Check jboss-logging and guava jar versions used. Glassfish load older versions by default
