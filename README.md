# individual-monitoring-Belit
Individual monitoring dashboard app, Belit version with Hibernate ORM and support for assessments. 

Deployment on Glassfish instructions:

1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Glassfish 4.1.1
3.	In GLASSFISH_HOME\glassfish\domains\domain1\config\domain.xml replace 'resources' and 'servers' nodes with following ones:
  <resources>
    <jdbc-connection-pool is-isolation-level-guaranteed="false" datasource-classname="org.postgresql.ds.PGSimpleDataSource" name="c4aPool" res-type="javax.sql.DataSource">
      <property name="User" value="city4age_srv"></property>
      <property name="serverName" value="109.111.225.84"></property>
      <property name="PortNumber" value="5432"></property>
      <property name="URL" value="jdbc:postgresql://109.111.225.84:5432/city4age?characterEncoding=utf8&amp;useUnicode=true&amp;currentSchema=city4age_sr"></property>
      <property name="Password" value="cities4Ages"></property>
    </jdbc-connection-pool>
    <connector-connection-pool max-pool-size="250" resource-adapter-name="jmsra" steady-pool-size="1" name="jms/__defaultConnectionFactory-Connection-Pool" connection-definition-name="javax.jms.ConnectionFactory"></connector-connection-pool>
    <connector-resource pool-name="jms/__defaultConnectionFactory-Connection-Pool" object-type="system-all-req" jndi-name="jms/__defaultConnectionFactory"></connector-resource>
    <context-service object-type="system-all" jndi-name="concurrent/__defaultContextService"></context-service>
    <managed-executor-service object-type="system-all" jndi-name="concurrent/__defaultManagedExecutorService"></managed-executor-service>
    <managed-scheduled-executor-service object-type="system-all" jndi-name="concurrent/__defaultManagedScheduledExecutorService"></managed-scheduled-executor-service>
    <managed-thread-factory object-type="system-all" jndi-name="concurrent/__defaultManagedThreadFactory"></managed-thread-factory>
    <jdbc-resource pool-name="c4aPool" jndi-name="jdbc/c4aDB"></jdbc-resource>
  </resources>
  <servers>
    <server config-ref="server-config" name="server">
      <application-ref ref="__admingui" virtual-servers="__asadmin"></application-ref>
      <application-ref ref="C4A-dashboard" virtual-servers="server"></application-ref>
      <resource-ref ref="jms/__defaultConnectionFactory"></resource-ref>
      <resource-ref ref="concurrent/__defaultContextService"></resource-ref>
      <resource-ref ref="concurrent/__defaultManagedExecutorService"></resource-ref>
      <resource-ref ref="concurrent/__defaultManagedScheduledExecutorService"></resource-ref>
      <resource-ref ref="concurrent/__defaultManagedThreadFactory"></resource-ref>
      <resource-ref ref="jdbc/c4aDB"></resource-ref>
    </server>
  </servers>
4.	Start Glassfish 4.1.1:
asadmin start-domain
5. If you don't have, install now Apache Maven 3.3.9
6.	Rebuild frontend module (run from project's 'frontend' dir): 
mvn clean install
7. For first deploy only (run from project's root dir):
mvn clean install
8.	Deploy application (run from project's root dir):
mvn clean install --pl backend glassfish:deploy
or, in case old version of app is already deployed on Glassfish 4.1.1:
mvn clean install --pl backend glassfish:redeploy
9.	Open in browser:
http://localhost:8080/C4A-dashboard/

Running unit tests from Eclipse IDE:

1. If you don't have, install now Java jdk1.8.0_112
2. If you don't have, install now Eclipse Neon.1a Release (4.6.1)
3. Import project into workspace from file system using Import > Existing projects into workspace
4. Run unit tests from src/test/java packages