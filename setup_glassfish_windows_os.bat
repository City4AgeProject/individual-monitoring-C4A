echo setup glassfish
cd build
copy guava-18.0.jar C:\glassfish-4.1.1\glassfish4\glassfish\modules
copy postgresql-42.0.0.jre6.jar C:\glassfish-4.1.1\glassfish4\glassfish\domains\domain1\lib
copy glassfish-resources.xml C:\glassfish-4.1.1\glassfish4\bin
cd C:\glassfish-4.1.1\glassfish4\bin
del asadmin
call asadmin start-domain
call asadmin add-resources glassfish-resources.xml
call asadmin stop-domain
