# authN
Authentication service that leverages a custom login method using Spring Security and JWT

Languages and tools:
- Maven
- Java 8
- Spring Boot 2.0 (Spring Security and Spring Web)
- MySql Database for user credentials storage

Exposed endpoints:
- POST /v1/user/login
  
    Request sample:
  curl --request POST \
  --url http://localhost:8090/v1/user/login \
  --header 'Content-Type: application/json' \
  --data '{
  "username": "user",
  "password": "12345"
  }'
  

- GET /hello-world

  Request sample: curl --request GET \
--url http://localhost:8090/hello-world \
--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoiSU5HIFRlY2giLCJleHAiOjE2MjQ5MTM2NDUsImlhdCI6MTYyNDkxMjc0NSwiYXV0aG9yaXRpZXMiOlsiUkVBRCJdfQ.minuZ9PJtCKmGmwMcXJ_JdsMdSnxn8gCqXoecpSOo8rmGjhdz1qpLa-Skkoo5-HidDOJF-MwosId32wHA3zXzw'
  
Run with mvnw clean install