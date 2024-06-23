# spyware-api-quarkus
## Development:
* Java 17 was used as the base language;
* It was developed with Quarkus;
* The database used was PostgreSQL;
* Authentication and authorization were done with Keycloak;
* The documentation was built with OpenAPI;
* Use of Redis (noSQL database) to maintain cache data;
* Messenger built with RabbitMQ;
* Use of Docker to configure and deploy the project, in addition to auxiliary containers;
* Tests with JUnit5 and Rest Assured.

## Project:
* Proof of concept project for the development of malware so that we can learn how to avoid and recognize them;
* This API is part of a larger project called Remote-Analyser, which is a system developed by me, for collecting suspicious data on business and/or institutional computers. Thus serving as a more efficient monitoring of the assets of these entities;
* This API Gateway is hosted on Heroku and was developed with Spring Boot using a database (PostgreSQL) to store the collected data. Additionally, it has a login endpoint that is encrypted for better reliability and data security. For better usability of the API, one of its endpoints has documentation for an example of the use of each endpoint;
* The application contains cache managed by SpringBoot and saved in a noSQL database called Redis;
* Security based on JWT Tokens also managed by Keycloak;
* The data is saved in a PostgreSql database and consumed by queues managed by the RabbitMQ messaging service in a scalable way.


## How to use:
* The application can be run by docker-compose and run the quarkus application using mvn quarkus:dev.


## Spyware Script:
* Repository on GitHub:
<br>Link: https://github.com/DarlanNoetzold/spyware

## Results:

https://docs.google.com/spreadsheets/d/1qMTwImwpNWSVuj_5WLhxky-Kq7nu6FhEKcGyL-_v-zA/edit?usp=sharing

---
⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
