# individual-monitoring 
Individual Monitoring Dashboard app.
Main C4A version with main characteristics:
* Hibernate ORM 
* Support for assessments
* JWT Authentication
* Multilingual

Deployment on Glassfish instructions - AUTOMATIC (Using script - under Windows OS):
------------------------------------------------------------------------------------
1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Glassfish 5.0 (glassfish5 folder must be located in C:\java_ee_sdk-8\)
3. Run "setup_glassfish_windows_os" script for automatic setting up Glassfish server for C4A application.
4. Start Glassfish 5.0:
	asadmin start-domain
5. If you don't have, install now Apache Maven 3.3.9
6. Deploy application (run from project's root dir - "anagraph\"):
	mvn clean install --pl build glassfish:deploy
or, in case old version of app is already deployed on Glassfish 5.0:
	mvn clean install --pl build glassfish:redeploy
7. Open in browser:
http://localhost:8080/C4A-dashboard/

--------------------------------------------
Running unit tests from Eclipse IDE:

1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Oxygen.1 Release (4.7.1)
3. Import project into workspace from file system using Import > Existing projects into workspace
4. Run unit tests from src/test/java packages

Note: Check jboss-logging and guava jar versions used. Glassfish loads older versions by default.
