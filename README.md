🎟️
todosimple-api (Spring Boot)
=========

🗣️
Sobre
----------
 Este conteúdo é relativo à criação de uma API RESTful utilizando o framework Spring Boot (Java), MySQL e Docker-Compose.

 A API corresponde a uma lista de tarefas que é atrelada ao seu usuário dono.

 Em resumo, com relação aos usuários, a aplicação possui rotas para a autenticação (login) e cadastro de novos usuários.
 Com o usuário criado, é possível cadastrar as suas tarefas, as quais poderão ser visualizadas através das rotas atreladas às tarefas.

 Além disso, foi criada uma simples interface web em HTML, CSS e JavaScript (utilizando também a biblioteca Booststrap) que consome esta API.  

👨‍💻 
Tecnologias utilizadas
----------

* **Frontend**
  * [HTML 5](https://www.w3schools.com/html/)
  * [CSS 3](https://www.w3schools.com/css/)
  * [JavaScript](https://www.w3schools.com/js/)

* **Backend**
  * [Java](https://www.java.com)
  * [Maven](https://maven.apache.org/)


* **Banco de Dados**
  * [MySQL](https://www.mysql.com/)
 
* **Virtualização**
  * [Docker](https://www.docker.com/)


🎯
Instruções de uso
----------

As seguintes instruções vão te levar a uma cópia do projeto executando em sua máquina local para propósitos de testes, desenvolvimento e estudos.

Pré-requisitos:

* Ter instalado todas as seguintes ferramentas:
  * Java
  * Maven
  * MySQL
  * Docker (Docker-Compose)


* Passo 1: Fazer o clone do repositório:
```
$ git clone https://github.com/morcelicaio/todosimple-api.git
```

* Passo 2: Configurar e iniciar a API Spring Boot (iniciar seu backend)
  * Passo 2.1: Entrar no arquivo application.properties:
    ```
    $ vi todosimple-api\src\main\resources\application-dev.properties
    ```
  * Passo 2.2: Configurar as suas credenciais de banco de dados de acordo com sua instalação do MySQL Server:
    ```
    # Database config
    spring.datasource.url=jdbc:mysql://localhost:3306/todosimple?createDatabaseIfNotExist=true
    spring.datasource.username=nomedoseuUsuario
    spring.datasource.password=senhadoUsuarioBD
    ```
