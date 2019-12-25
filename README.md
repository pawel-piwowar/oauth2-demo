# Oauth2 Demo

- Application B ( oauth2-demo-resource-app  localhost:8080) has protected resource http://localhost:8080/api/accounts/default which can only be accessed by authorized users  
- Application A ( oauth2-demo-client-app  localhost:8081) wants to access this resource  
- Redirect is made to application B, the user has to login in application B (user: demo, pass:123456) and confirm giving access for application A  
- After that, redirect is made back to application A with temporary access code  
- Then application A requests token using separate HTTP connection and when the token is granted makes call to application B REST resource

### Usage

1. Install java 1.8 or higher, maven 3.3
2. Run "mvn clean install" from root directory of the project
3. Start applications by typing : "mvn spring-boot:run" in  oauth2-demo-resource-app and oauth2-demo-client-app directories.
This will start two applications on port 8080 and 8081 respectively  
4. Type "http://localhost:8081" in web browser
