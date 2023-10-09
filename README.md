# spyware-api-quarkus
## Desenvolvimento:
* Foi usado Java 17 como linguagem base;
* Foi desenvolvido com Quarkus;
* O Banco utilizado foi o PostgreSQL;
* A autenticação e autorização foram feitas com Keycloak;
* A documentação foi construida com OpenAPI;
* Uso de Redis (banco noSQL) para manter dados de cache;
* Mensageiria construída com RabbitMQ;
* Uso do docker para configuração e implantação do projeto, além dos conteiners auxiliares;
* Testes com JUnit5 e Rest Assured.

## Projeto:
* Projeto de Prova de conceito para o desenvolvimento de malware's para que assim possamos aprender como evitá-los e reconhece-los;
* Esta API faz parte de um projeto maior chamado Remote-Analyser, o qual é um sistema desenvolvido por mim, para coleta de dados suspeitos em computadores empresarias e/ou institucionais. Servindo assim, como um monitoramento mais eficiente do patrimônio destas entidades;
* Este API Gateway está hospedado na Heroku e foi desenvolvido com Spring Boot usando um banco de dados (PostgreSQL) para armazenar os dados coletados. Além disso, ele tem um endpoint para login sendo encriptado para melhor confiabilidade e segurança dos dados. Para a melhor usabilidade da API, um dos endpoints do mesmo tem uma documentação para uma exemplificação de uso de cada endpoint;
* A aplicação contém cache gerenciado pelo SpringBoot e salvo em um banco noSQL chamado Redis;
* Segurança baseada em Tokens JWT também gerenciada pelo Keycloak;
* Os dados são salvos em um banco PostgreSql são consumidos por filas gerenciadas pelo serviço de mensageiria RabbitMQ de maneira escalável.


## Como utilizar:
* A aplicação pode ser executada pelo docker-compose e executar a aplicação quarkus usando mvn quarkus:dev.


## Script do Spyware:
* Repositório no GitHub:
<br>Link: https://github.com/DarlanNoetzold/spyware

---
⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
