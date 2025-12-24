# Accessing MongoDB Data with REST (Demo)

This Spring Boot application exposes `Person` objects via Spring Data REST backed by MongoDB.

## Prerequisites
- Java 17+ (Java 22 was used during development)
- Git (optional)
- Docker (optional) or a local MongoDB server running at `mongodb://localhost:27017`
- Maven wrapper is included (`mvnw.cmd`) so you don't need a local Maven installation.

## Configuration
Edit `src/main/resources/application.properties` if you need to change the MongoDB URI:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/demo
```

## Build
From the project root run:

```powershell
.\mvnw.cmd -DskipTests package
```

## Run

Run with the Spring Boot Maven plugin (development):

```powershell
.\mvnw.cmd spring-boot:run
```

Or run the packaged jar (preferred for detached/run-in-background):

```powershell
# Build first
.\mvnw.cmd -DskipTests package
# Start in foreground (logs stream to terminal)
java -jar target\gs-accessing-gemfire-data-rest-0.1.0.jar
# (Or start detached on Windows PowerShell)
Start-Process -FilePath 'java' -ArgumentList '-jar','target\\gs-accessing-gemfire-data-rest-0.1.0.jar' -WindowStyle Hidden
```

The application listens on port `8080` by default.

## API Endpoints

- GET  `/people` — list people (HAL JSON)
- POST `/people` — create a new person
- GET  `/people/{id}` — retrieve a person by id

Example JSON for POST:

```json
{
  "firstName": "John",
  "lastName": "Doe"
}
```

## Example requests

PowerShell (recommended on Windows):

```powershell
# Create a person
Invoke-RestMethod -Uri http://localhost:8080/people -Method Post -Body (@{firstName='Alice'; lastName='Smith'} | ConvertTo-Json) -ContentType 'application/json'

# List people (pretty printed)
(Invoke-RestMethod -Uri http://localhost:8080/people) | ConvertTo-Json -Depth 5
```

curl (cross-platform):

```bash
# Create a person
curl -X POST -H "Content-Type: application/json" -d '{"firstName":"Alice","lastName":"Smith"}' http://localhost:8080/people

# List people
curl http://localhost:8080/people
```

## Stopping the app
- If started in foreground: press `Ctrl+C` in the terminal.
- If started detached with `Start-Process`, find the java process and stop it (Task Manager or PowerShell `Stop-Process`).

## Notes
- Make sure MongoDB is running and reachable at the URI in `application.properties` before starting the app.
- Spring Data REST exposes the repository automatically. You can also navigate to `http://localhost:8080/profile/people` to see the HAL profile for the resource.

---

If you want, I can add a short integration test that uses an embedded Mongo (or Testcontainers) and exercises the `PersonRepository` endpoints.