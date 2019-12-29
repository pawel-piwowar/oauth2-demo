## Oauth2 Demo

### Scenario
- "Resource Application" ( oauth2-demo-resource-app,  localhost:8080) has protected resource: http://localhost:8080/api/accounts/default (account data) which can only be accessed by authorized users. 
- "Authorization Server" (oauth2-demo-auth-server, localhost:8082) is configured for authorization of "Resource Application". 
Single user "demo" is defined with role "USER". 
- Anonymous user is visiting "Client Application" (oauth2-demo-client-app, http://localhost:8081). "Client Application" gives him possibility to retrieve his account data from "Resource Application".   
- After user confirmation, redirect is made to "Authorization Server"
```
http://localhost:8082/oauth/authorize?client_id=demo-client-app&response_type=code&scope=read_account
```
- User has to login (user: "demo", pass:"123456").   
![login](./docs/auth-server-login.png)
- Than he may accept (or reject) giving access to his account data to "Client Application"
![approval](./docs/auth-server-approval.png)
- After user acceptance, redirect is made back to "Client Application" with temporary access code   
```
http://localhost:8081/api/oauth2/account?code=[code]
```
- In order to get access token, "Client Application" makes call to "Authorization Server" using separate HTTP connection (acting as HTTP client).
 "Client Application" is authenticating itself in "Authorization Server" using login "demo-client-app" and pass: "123456"
```
    POST http://localhost:8082/oauth/token  
    Headers:
    Content-Type: application/x-www-form-urlencoded
    Authorization: Basic ZGVtby1jbGllbnQtYXBwOjEyMzQ1Ng==  
    Parameters:
    grant_type=authorization_code  
    code=[code from redirect]  
    redirect_uri=http://localhost:8081/api/oauth2/account  
```
response with access token value: 
```json
{       "access_token": "44aa81f8-fe2f-4b08-bde2-4cd7e86fe189",
        "token_type": "bearer",
        "refresh_token": "[another_access_token_value]",
        "expires_in": 4815,
        "scope": "read_account" }
```
- Now "Client Application" (still using separate HTTP connection) makes call to "Resource Application" for REST resource "api/accounts/default",
token value is sent for authorization as header parameter . 
```    
    GET http://localhost:8080/api/accounts/default  
    Headers:  
    authorization:Bearer 44aa81f8-fe2f-4b08-bde2-4cd7e86fe189
```  
On this stage "Resource Application" makes call to "Authorization Server" for token validation,
address: "http://localhost:8082/oauth/check_token" (this call is made by spring-security-oauth2 without user browser redirects), 
after successful token validation, response is returned to "Client Application":
```json
{ "accountNumber":"3435656777565677",
 "accountName":"Saving account",
 "balance":45.67 }
```
- This response containing account data JSON is sent to client browser (it is response for previous redirect: http://localhost:8081/api/oauth2/account?code=[code] ).
![approval](./docs/account-data.png)
Please note, that token value is never sent using client Internet browser. Separate connection is used instead,
where oauth2-demo-client-app application acts as http client. In this demo WebClient from Spring Webflux is used.  
Class : [com.pp.oauth2.demo.client.app.connector.Oauth2Connector](./oauth2-demo-client-app/src/main/java/com/pp/oauth2/demo/client/app/connector/Oauth2Connector.java)  
(user account data is also transferred through this separate connection, it could be sent back to client browser or not)  
Alternatively client application could be created using spring-security-oauth2-client.

### Running the demo

1. Install java 1.8 or higher, maven 3.3
2. Run "mvn clean install" from root directory of the project
3. Start applications by typing : "mvn spring-boot:run" in oauth2-demo-resource-app, oauth2-demo-client-app and oauth2-demo-auth-server directories.
This will start three applications on ports 8080, 8081 and 8082 respectively  
4. Type http://localhost:8081 in web browser

Points 2-4 could be executed using "run.bat" script from root directory of the project.

### Resources 

- https://github.com/endymuhardin/belajar-springoauth2
- https://howtodoinjava.com/spring-boot2/oauth2-auth-server
- https://pattern-match.com/blog/2018/10/17/springboot2-with-oauth2-integration
- https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/htmlsingle
- https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html

### Comments

In case of any problems/questions fell free to create issue here: https://github.com/pawel-piwowar/oauth2-demo/issues
