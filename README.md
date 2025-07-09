# 🚀 PicPay Simplificado - Desafio Back-End

[![Made with Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)

## 🧠 Sobre

Esse projeto foi desenvolvido como parte de um desafio backend, inspirado no sistema do **PicPay**. A ideia é simular de forma simples e funcional uma **API de transferência entre usuários**, com as regras de negócio requisitadas, autorização e notificação.

Foi um ótimo exercício para aplicar na prática conceitos como serviços, validações, consumo de API externa, testes automatizados, mocks de dependências e boas práticas com o Spring.

## ✅ O que esse projeto faz

-   Cadastro de usuários (comuns e lojistas)
-   Listagem de todos os usuários cadastrados
-   Transferência de dinheiro entre usuários
-   Validações importantes como:
    -   O usuário precisa ter saldo suficiente
    -   Lojistas **não podem transferir dinheiro**, apenas receber
    -   O usuário **não pode transferir para si mesmo**
-   Consulta a um serviço externo mockado que autoriza a transação
-   Envio de notificação por e-mail após a transferência (também mockado)

## 🧪 Tecnologias e Ferramentas

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database (memória)**
- **Lombok**
- **JUnit 5 + Mockito**
- **Maven**

## ⚙️ Como rodar o projeto

### Pré-requisitos

- Java 17+ instalado
- Maven instalado

### Passos

```bash
# Clonar o repositório
git clone https://github.com/lucaslp25/picpay-simplificado.git

# Entrar na pasta do projeto
cd picpay-simplificado

# Rodar a aplicação
./mvnw spring-boot:run

``` 
## Acessando a API
Assim que a aplicação estiver rodando, você pode acessar a documentação Swagger no navegador:

### 📌 Swagger UI: http://localhost:8080/swagger-ui/index.html

Lá você pode ver todos os endpoints, testar requisições e entender o comportamento da API de forma mais interativa.

## Testando com o Postman
Se preferir testar com o Postman:

Abra o Postman

Crie uma nova requisição

Use os endpoints abaixo:


POST /users
```json
{
  "firstName": "Fulano",
  "lastName": "da silva",
  "cpf": "123.456.789-00",
  "email": "fulano@email.com",
  "password": "1234",
  "type": "COMMON"
}
```

GET | http://localhost:8080/users

POST /transactions
```json
{
  "id_sender": 1,
  "id_receiver": 2,
  "amount": 25.0
}

