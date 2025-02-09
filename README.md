# OSINT Web App

**OSINT Web App** is a web application for scanning domains using AMASS tool. The application provides an API for
managing scan processes, monitoring statuses, and retrieving detailed scan information.

## Requirements

1. **Java 17**
2. **Docker**
3. **PostgreSQL** (or use Docker to deploy the database)

## Installation and Setup

### 1. Clone the repository

Clone the repository:

```bash
git https://github.com/BolgovID/osint-scanner.git
cd osint-scanner
```

### 2. Create env file

Create a file named `.env.dev` in the root directory of the project and add the following environment variables:

```dotenv
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_USER=tomas_the_locomotive
POSTGRES_PASSWORD=never_guess
POSTGRES_DB=osint-db
FRONTEND_URL=http://localhost:4200
```

If you plan to use your own PostgreSQL instance, set your credentials here.

### 3. Database Configuration

Ensure that the database instance is running before you start the application.

#### Option 1: Use Docker for PostgreSQL

If you want to use Docker for the PostgreSQL database, I’ve prepared a docker-compose.yml file.

Execute the following command in your terminal from the root directory:

```bash
docker compose up -d
```

This will start the PostgreSQL database in a Docker container in the background.

Option 2: Use Your Own PostgreSQL Instance

If you prefer using your own PostgreSQL instance, ensure it is running and the credentials in the .env.dev file match
your configuration.

### 4. Build project

Use Gradle to build the project:

```bash
./gradlew build
```

This will compile the project and prepare the necessary dependencies.

### 5. Make jar

To create a JAR file, run the following command:docker run -d --rm --env-file .env.dev osint-web

```bash
./gradlew bootJar
```

### 6. Run

Once the JAR file is created, you can run the application using:

```bash
java -jar build/libs/*.jar
```

## Run with Docker container

Create a file named `.env.docker` in the root directory of the project and add the following environment variables:

```dotenv
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_USER=tomas_the_locomotive
POSTGRES_PASSWORD=never_guess
POSTGRES_DB=osint-db
FRONTEND_URL=http://localhost:4200
```

### 1. Build image

To create docker image type the following:

```bash
docker build -t osint-web .
```

We specify the name of image with flag -t, so our image will be called osint-web

### 2. Run image

To run created image write next:

```bash
docker run --rm -d \
  --env-file .env.docker \
  --network osint-web-app_osint-network \
  -v /var/run/docker.sock:/var/run/docker.sock \
  osint-web
```

We should specify network here if we have a postgres as a container. Ensure that dockerized app works on the same
network with database

## Endpoints

There are some endpoints with request examples

### Initiate scanning

Endpoint that start domain scanning process

```bash
curl -X POST "http://localhost:8080/api/scans" \
     -H "Content-Type: application/json" \
     -d '{
           "domain": "google.com"
         }'
```

### Get all scans

Return the list of all scans from newest to oldest

```bash
curl -X GET "http://localhost:8080/api/scans"
```

### Get scan detail

Show the scan details

```bash
curl -X GET "http://localhost:8080/api/scans/1"
```

### Stop scan

```bash
curl -X POST "http://localhost:8080/api/scans/1/stop"
```