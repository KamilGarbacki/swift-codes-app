# SWIFT CODE APP
## Project Description  
The SWIFT Code Application is designed to parse, store, and retrieve SWIFT (BIC) codes efficiently. It transforms SWIFT data from a spreadsheet into a structured database format, enabling quick and accurate access for applications.  

## Documentation
>http://localhost:8080/swagger-ui/index.html#/


## Build With
- Java 21
- Spring Boot Framework
- PostgreSQL

## Docker Image
The project is avilable as a Docker Repository which can be accessed with
```sh
docker pull kamilgarbacki/swift-code-app
```

## Project Setup
### 1. Clone the Repository
```sh
git clone https://github.com/KamilGarbacki/swift-codes-app
cd swift-code-app
```

### 2. Start the PostgreSQL Database with Docker
Ensure Docker is running, then start the PostgreSQL container:

```sh
docker-compose up -d
```

This will spin up a PostgreSQL instance as defined in the `docker-compose.yml` file.

### 3. Build the Project
Run the following command to build the project:
```sh
mvn clean install
```

## Running the Application
After the database is up and the project is built, start the Spring Boot application:
```sh
java -jar .\target\swift-codes-app-1.0-SNAPSHOT.jar
```

## Testing The Application
### 1. Unit Tests
To run unit tests:
```sh
mvn test
```

### 2. Integration Tests
To run unit and integration tests:
```sh
mvn verify
```

## Troubleshooting

- If the database connection fails, ensure the container is running:
  ```sh
  docker ps
  ```
  If not running, restart it:
  ```sh
  docker-compose up -d
  ```

- If port conflicts occur, modify `docker-compose.yml` to use a different port.
