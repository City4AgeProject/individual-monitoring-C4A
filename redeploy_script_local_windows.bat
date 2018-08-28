cd "C:\java_ee_sdk-8\glassfish5\glassfish\bin"
call asadmin stop-domain

del /q "C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\applications\*"
for /d %%x in ("C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\applications\*") do @rd /s /q "%%x"

del /q "C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\generated\*"
for /d %%x in ("C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\generated\*") do @rd /s /q "%%x"

del /q "C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\osgi-cache\*"
for /d %%x in ("C:\java_ee_sdk-8\glassfish5\glassfish\domains\domain1\osgi-cache\*") do @rd /s /q "%%x"

call asadmin start-domain

cd "C:\Users\marina.andric\Documents\GitHub\anagraph"
call mvn clean install --pl build glassfish:redeploy

pause