# individual-monitoring-Belit
Individual monitoring dashboard app, Belit version with Hibernate ORM and support for assessments. 

Deployment instructions:

1.	Install Glassfish 4.1.1.
2.	IMPORTANT: jboss-logging.jar under GLASSFISH_HOME/glassfish/modules must be replaced to version 3.3.0 from public maven repository.
3.	Start Glassfish 4.1.1:
asadmin start-domain
4.	Create jdbc connecton pool and jdbc resource under Glassfish 4.1.1:
<jdbc-resource pool-name="c4aPool" object-type="system-admin" jndi-name="jdbc/__c4aDB"></jdbc-resource>
<jdbc-connection-pool is-isolation-level-guaranteed="false" datasource-classname="org.postgresql.ds.PGSimpleDataSource" name="c4aPool" res-type="javax.sql.DataSource"/>
5.	Change spring.datasource.url in application.yml located in src/main/resources of backend application to reflect your database connection.
6.	Deploy application:
mvn clean install --pl backend glassfish:deploy
or, in case old version of app is already deployed on Glassfish 4.1.1:
mvn clean install --pl backend glassfish:redeploy
7.	IMPORTANT: If there is Derby database connection conflict during application deployment remove all other jdbc connection pools and jdbc resources other then the ones created manually in step.4
8.	Open in browser:
http://localhost:8080/C4A-dashboard/
