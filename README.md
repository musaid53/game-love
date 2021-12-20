# Game-Love


## Building

fabric8io/docker-maven-plugin used in this project. You can check the plugin from  [fabric8io/docker-maven-plugin](https://dmp.fabric8.io/)

this will create "game-love" image on your docker. also to start docker image you 
can use docker-compose file to create container and run

if you don't have docker on your pc, or you don't want to use docker
just use this; 
```shell script
./mvnw clean install 
```

if you want to use docker, use(default docker.skip is true);
```shell script
./mvnw clean install '-Ddocker.skip=false'
```


disable tests;
```shell script
./mvnw clean install '-DskipTests'
```
## Running Local
### Disabling Security - local

if you want to disable security with jwt token you can do;
changing application.yml -> "securityEnabled" value to false or,

```bash
./mvnw spring-boot:run '-Dspring-boot.run.arguments="--SECURITY_ENABLED=false"'
```
or just use with defaults
```bash
./mvnw spring-boot:run
```
## Running on docker
> **_NOTE:_** you must package with -Ddocker.skip=false for running docker image (it builds image)
> and also dont skip tests (build must be after tests)

Runnig the docker image

```shell script
docker run -i --rm -p 8080:8080 --name game-love game-love
```
Or use compose:
```shell script
docker compose up
```

Using the app:
For swagger documentation; 
[http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

If you enabled security; you must login first;
```shell script
curl --location --request POST 'http://localhost:8080/auth/login' --header 'Content-Type: application/json' --data-raw '{"username": "admin","password": "admin"}'
```
And add this accessToken to header as "Authorization: <accessToken>" or "Authorization: Bearer <accessToken>"
example request;
```shell script
curl --location --request PUT 'localhost:8080/game/create' --header 'Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWlkIiwicm9sZXMiOiJBRE1JTixVU0VSIiwiaWF0IjoxNjM5OTQ3NDYzLCJleHAiOjE2Mzk5NTEwNjN9.AjINiESnOG9FMuwiwN19WA6ncli_X52X6LpDP8edZYo' --header 'Content-Type: text/plain' --data-raw 'Mario'
```
For using easily, I have added a postman collection: game-love-postman_collection.json





