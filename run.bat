call mvn clean install
cd oauth2-demo-client-app
start mvn spring-boot:run
cd ..\oauth2-demo-resource-app
start mvn spring-boot:run
cd ..\oauth2-demo-auth-server
start mvn spring-boot:run
cd ..
start "" "http://localhost:8081"