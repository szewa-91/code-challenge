# Code challenge app

This application is a simple implementation of Twitter-like system, exposing a REST API.

## Running application

The application is based on Spring Boot. It starts its own Tomcat server. To run it, just use Maven goal (using built-in
Maven wrapper):

``./mvnw spring-boot:run``

or on Windows

``mvnw spring-boot:run``

To run tests, execute:

``./mvnw test``

## API

Swagger UI was added for easier interacting with the application. After running the application, you can find it
under URL: 

http://localhost:8080/swagger-ui.html

### Specifying active user

For each request, you can specify the user on behalf of who you execute it. Just add a query param `activeUser`. 
For example the request:

``POST http://localhost:8080/post?activeUser=user1``

Means that you want to create a new message as `user1`. For some requests it's mandatory.

### Endpoints

```http
POST /post
```

This endpoint will create a new message. The author of a message will be a current user specified by the query param.
Body of request should be a content of message (maximum length is 140 characters, cannot be empty)

Returns ``201 CREATED`` for success or ``400 BAD_REQUEST`` if a content is too long or in bad format. 

This endpoint expects that you specify an active user (Refer to **Specifying active user** section). The active user will be
created if doesn't exist.

```http
POST /users/{name of user to follow}/follow
```

This endpoint attempt to add a user specified in URL to list of followed authors of an active user. 
The following user will be a current user specified by the query param.

Returns ``200 OK`` for success or ``404 NOT_FOUND`` if a user you try to follow doesn't exist. 

This endpoint expects that you specify an active user (Refer to **Specifying active user** section). The active user will be
created if doesn't exist.

```http
GET /users/{name of user}/wall
```

This endpoint displays a list of posts that they've posted in reversed chronological order.

Returns ``200 OK`` for success or ``404 NOT_FOUND`` if a user doesn't exist. 

```http
GET /users/{name of user}/timeline
```

This endpoint displays a list of posts created by all the authors that are followed by a specified user.

Returns ``200 OK`` for success or ``404 NOT_FOUND`` if a user doesn't exist. 