# Server

Sample Spring web scraping application.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Ubuntu 16.04
- OpenJDK 1.8
- Gradle 2.10
- Docker 1.12

### Installing

1. Clone the repository.
3. Update `Downloader.java` accordingly to the actual domains used.
2. Build the project via `./gradlew build`.
3. Download postgres into the docker via `docker pull postgres`.
4. Run postgres in teh docker container via `docker run --name some-postgres -v /home/user/database:/database -e PGDATA=/database -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres`.
5. Run the project via `./gradlew bootRun`.

## Running the tests

Run `./gradlew test`.

