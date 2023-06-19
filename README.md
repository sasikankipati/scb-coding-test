USER SERVICE API

Tech Stack:

Java 17
Spring Boot 3.1.0
Spring Security 6.1.0
Spring Data JPA 3.1.0
Swagger (Open API) 2.0
JJWT 0.9.1

Authentication API:

Base path - /auth

    /register - Register User for authentication

    /authenticate - Authenticate the User and generate JWT

User API: (All APIs for User is protected and requires valid JWT in Authorization header)

    /getUser - Accepts userId and returns the related User if userId is not null else all the Users

        Required Role - ROLE_USER or ROLE_ADMIN

    /createUser - Accepts userId, name & city. Adds new User.

        Required Role - ROLE_ADMIN

    /updateUser - Accepts userId, name & city. Updates User's Name or City.

        Required Role - ROLE_ADMIN

    /deleteUser - Accepts userId and delete the associated User.

        Required Role - ROLE_ADMIN

Database: H2 (in memory)

Console : /h2-console (enabled for testing purpose only. please update the property spring.h2.console.enabled as false for higher environments)

Tables:

AUTH_USER (To store users for authentication)
ROLES (To store standard roles like 'ROLE_USER')
AUTH_USER_ROLES (To maintain the relationships between AUTH_USER and ROLES)

USER (To store the User details)

API Documentation - /swagger-ui/index.html

Run steps:

Import the project to IDE and resolve all the required dependencies

Start the application it will be exposed on port 8090 (change if required in application.properties)

1. Call the /auth/register endpoint with sample payload like below to register the user for authentication,  

{
"userName":"test_user",
"password":"user",
"roles":["ROLE_USER"]
}

2. Call the /auth/authenticate endpoint with sample payload like below to get the JWT,

{
"userName":"test_user",
"password":"user"
}

Note: For testing purpose below users are added automatically on application startup,

    Name    Password    Role
    user    user        ROLE_USER
    admin   admin       ROLE_ADMIN

Use the above credentials directly to /auth/authenticate endpoint instead of creating new users through /auth/register

3. Call any user endpoint /getUser or /createUser or /updateUser or /deleteUser with the JWT received on above step 2. Please note that JWT should be added to the request header with name 'Authorization'  

Logs: 

Stored in /logs/user-service.log

JWT properties: (Change if required)

app.jwtSecret=exampleSecretKey
app.jwtExpiry=1800000



