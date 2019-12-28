## Oauth2 Demo

### Scenario
- "Resource Application" ( oauth2-demo-resource-app,  localhost:8080) has protected resource: http://localhost:8080/api/accounts/default (account data) which can only be accessed by authorized users. 
 Single user "demo" is defined. 
- Anonymous user is visiting "Client Application" (oauth2-demo-client-app, http://localhost:8081). "Client Application" gives him possibility to retrieve his account data from "Resource Application".   
- After user confirmation, redirect is made to "Resource Application"
```
http://localhost:8080/oauth/authorize?client_id=demo-client-app&response_type=code&scope=read_account
```
- The user has to login in "Resource Application" (user: "demo", pass:"123456"). Than he may accept (or reject) giving access to his account data for "Client Application"  
- After user acceptance, redirect is made back to "Client Application" with temporary access code   
```
http://localhost:8081/api/oauth2/account?code=[code]
```
- Before returning response to web browser, "Client Application" makes call to "Resource Application" using separate HTTP connection (acting as HTTP client)   
 "Client Application" is authenticated in "Resource Application" using login "demo-client-app" and pass: "123456"
```
    POST http://localhost:8080/oauth/token  
    Headers:
    Content-Type: application/x-www-form-urlencoded  
    Parameters:
    grant_type=authorization_code  
    code=[code from redirect]  
    redirect_uri=http://localhost:8081/api/oauth2/account  
```
response: 
```json
{       "access_token": "[uniqueaccess_tokeb_value]",
        "token_type": "bearer",
        "refresh_token": "[uniqueaccess_tokeb_value]",
        "expires_in": 4815,
        "scope": "read_account" }
```
- Second call (still made using separate HTTP connection) is for the protected REST resource itself, token value is sent as header parameter. 
```    
    GET http://localhost:8080/api/accounts/default  
    Headers:  
    authorization:Bearer [token value from previous response]
```  
and response is received:
```json
{ "accountNumber":"3435656777565677",
 "accountName":"Saving account",
 "balance":45.67 }
```
- Response containing account data JSON is sent to client browser (it is response for http://localhost:8081/api/oauth2/account?code=[code] redirect).

Please note, that token value is never sent using client Internet browser. Separate connection is used instead,
where oauth2-demo-client-app application acts as http client. In this case  WebClient from Spring Webflux is used (https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html).  
Class for getting token: [com.pp.oauth2.demo.client.app.connector.Oauth2Connector](./oauth2-demo-client-app/src/main/java/com/pp/oauth2/demo/client/app/connector/Oauth2Connector.java)
Class for getting requeted account data: [com.pp.oauth2.demo.client.app.connector.AccountsConnector](./oauth2-demo-client-app/src/main/java/com/pp/oauth2/demo/client/app/connector/AccountsConnector.java) 

### Running the demo

1. Install java 1.8 or higher, maven 3.3
2. Run "mvn clean install" from root directory of the project
3. Start applications by typing : "mvn spring-boot:run" in  oauth2-demo-resource-app and oauth2-demo-client-app directories.
This will start two applications on port 8080 and 8081 respectively  
4. Type http://localhost:8081 in web browser

Points 2-4 could be executed using "run.bat" script from root directory of the project.

### Resources 

This project was created using following articles:
 
- https://howtodoinjava.com/spring-boot2/oauth2-auth-server/
- https://pattern-match.com/blog/2018/10/17/springboot2-with-oauth2-integration/

### Comments

In case of any problems/questions fell free to create issue here: https://github.com/pawel-piwowar/oauth2-demo/issues
