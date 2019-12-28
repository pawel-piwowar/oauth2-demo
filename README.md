# Oauth2 Demo

- Application B ( oauth2-demo-resource-app  localhost:8080) has protected resource: http://localhost:8080/api/accounts/default (account data) which can only be accessed by authorized users. 
 Single user is defined : login "demo", password: "123456"
- User "demo" is visiting application A (oauth2-demo-client-app, http://localhost:8081). Application A gives him possibility to retrieve his account data from application B.   
- Redirect is made to application B, the user has to login in application B (user: "demo", pass:"123456"). Than he may accept (or reject) giving access to his account data for application A  
- After that, redirect is made back to application A with temporary access code   
- Before returning response to web browser, application A makes two calls to application B using separate HTTP connection (acting as HTTP client). 
First one is for authorization token, second for the protected REST resource, token is send as header parameter. Response containing account data is sent back as response to client browser.

### Usage

1. Install java 1.8 or higher, maven 3.3
2. Run "mvn clean install" from root directory of the project
3. Start applications by typing : "mvn spring-boot:run" in  oauth2-demo-resource-app and oauth2-demo-client-app directories.
This will start two applications on port 8080 and 8081 respectively  
4. Type http://localhost:8081 in web browser

### Resources 

This project was created using following articles:
 
- https://howtodoinjava.com/spring-boot2/oauth2-auth-server/
- https://pattern-match.com/blog/2018/10/17/springboot2-with-oauth2-integration/

### Comments

In case of any problems/questions fell free to create issue here: https://github.com/pawel-piwowar/oauth2-demo/issues
