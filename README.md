<img src="src/main/resources/static/img/logo.png" width="200px" align="center" alt="Knowie logo" />
# Knowie
This is an online school exam project created to PWEB class at Federal Institute of Alagoas.

## Configuration
You should have Docker compose, Maven and JDK 11 installed on your machine.

* At first, you should create a docker network. Run `docker network create pwebnetwork` on your CMD.
* On root folder, run `docker build -t mysql-pweb -f ./docker-config/db/Dockerfile .` to build the database image.
* In sequel, on root folder, run `docker build -t springboot-pweb -f ./docker-config/app/Dockerfile .` to build the application image.
* After all, run `docker run -p 10000:3306 --network pwebnetwork --name mysql mysql-pweb` and than `docker run -p 9000:8080 --network pwebnetwork --name springboot --env-file .env springboot-pweb`

## API
* [Application url](http://localhost:2364/api/v1)
* [Mysql](http://localhost:3306)

## Contributors
* [Gabriel Syphan](https://github.com/gabrielsyphan)
