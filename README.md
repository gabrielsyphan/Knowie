<p align="center">
  <img src="src/main/resources/static/img/logo.png" width="200px" alt="Knowie logo" />
</p>

# Knowie
This is an online school exam project created to PWEB class at Federal Institute of Alagoas.

## Configuration
You should have Docker compose, Maven and JDK 17 installed on your machine.

* At first, you should create a docker network. Run `docker network create knowienetwork` on your CMD.
* On root folder, run `docker build -t mysql-knowie -f ./docker-config/db/Dockerfile .` to build the database image.
* In sequel, on root folder, run `docker build -t springboot-knowie -f ./docker-config/app/Dockerfile .` to build the application image.
* After all, run `docker run -p 10000:3306 --network knowienetwork --name mysql mysql-knowie` and than `docker run -p 9000:8080 --network knowienetwork --name springboot --env-file .env springboot-knowie`
* You can make login as ADMIN using the follow credentials: email: admin, password: admin

## API
* [Application url](http://localhost:2364/api/v1)
* [Mysql](http://localhost:3306)

## Contributors
* [Gabriel Syphan](https://github.com/gabrielsyphan)
* [Knowie Website](http://knowie.site)
