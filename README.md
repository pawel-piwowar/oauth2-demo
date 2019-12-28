## Oauth2 Demo

### Sequence
- Application B ( oauth2-demo-resource-app  localhost:8080) has protected resource: http://localhost:8080/api/accounts/default (account data) which can only be accessed by authorized users. 
 Single user is defined : login "demo", password: "123456"
- User "demo" is visiting application A (oauth2-demo-client-app, http://localhost:8081). Application A gives him possibility to retrieve his account data from application B.   
- Redirect is made to application B
```
http://localhost:8080/oauth/authorize?client_id=demo-client-app&response_type=code&scope=read_account
```
- The user has to login in application B (user: "demo", pass:"123456"). Than he may accept (or reject) giving access to his account data for application A  
- After that, redirect is made back to application A with temporary access code   
```
http://localhost:8081/api/oauth2/account?code=[code]
```
- Before returning response to web browser, application A makes call to application B using separate HTTP connection (acting as HTTP client)   
 Application A is authenticated in application B using login "demo-client-app" and pass: "123456"
```
    POST http://localhost:8080/oauth/token  
    Headers:
    Content-Type: application/x-www-form-urlencoded  
    Parameters:
    grant_type=authorization_code  
    code=[code from redirect]  
    redirect_uri=http://localhost:8081/api/oauth2/account  
```
It will return following response: 
```json
{       "access_token": "[uniqueaccess_tokeb_value]",
        "token_type": "bearer",
        "refresh_token": "[uniqueaccess_tokeb_value]",
        "expires_in": 4815,
        "scope": "read_account"
    }
```
- Second call (still made using separate HTTP connection) is for the protected REST resource, token is send as header parameter. Response containing account data is sent back as response to client browser.
```    
    GET http://localhost:8080/api/accounts/default  
    Headers:  
    authorization:Bearer [token value from previous response]
```  

Please note, that token value is never sent using client internet browser. Separate connection is used instead,
where oauth2-demo-client-app application acts as http client. In this case  WebClient from Spring Webflux is used,  
class for getting token: [com.pp.oauth2.demo.client.app.connector.Oauth2Connector](./oauth2-demo-client-app\src\main\java\com\pp\oauth2\demo\client\app\connector\Oauth2Connector.java) and  
class for getting requeted account data: [com.pp.oauth2.demo.client.app.connector.AccountsConnector](./oauth2-demo-client-app\src\main\java\com\pp\oauth2\demo\client\app\connector\AccountsConnector.java) 

### Usage

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
