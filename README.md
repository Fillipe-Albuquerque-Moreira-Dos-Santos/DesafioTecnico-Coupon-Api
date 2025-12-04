# Aplicação de Gestão de Cupons

Este projeto é uma **aplicação web para gerenciamento de cupons de desconto**, construída com **Spring Boot**, **Thymeleaf** e **Bootstrap 5**.  
Permite criar, listar, editar e excluir cupons, além de marcar se estão publicados ou não.

---

## Links em LocalHost

- Listar todos os cupons cadastrados: [http://localhost:8080/cupons](http://localhost:8080/cupons)  
- Criar um novo cupom: [http://localhost:8080/cupons/novo](http://localhost:8080/cupons/novo)
- Link swagger: http://localhost:8080/swagger-ui/index.html#/
- Banco de dados H2: java -cp ~/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar org.h2.tools.Shell  / jdbc:h2:file:/home/fillipe/dev/cupom/data/cupomdb;AUTO_SERVER=TRUE
  
## Funcionalidades

- Listar todos os cupons cadastrados
- Criar um novo cupom:
  - Código alfanumérico (até 6 caracteres)
  - Descrição
  - Valor do desconto
  - Data de expiração
  - Publicação (ativo/inativo)
- Editar um cupom existente
- Deletar um cupom
- Validação de campos com mensagens de erro
- Feedback para operações (sucesso/erro)

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4**
- **Spring Data JPA**
- **Thymeleaf**
- - **Docker**
- **Bootstrap 5**
- **H2 Database** (memória, para teste)
- **JUnit 5 / Mockito** (testes unitários)
- **Lombok** 
- **SpringDoc OpenAPI** (documentação da API)


## Estrutura do Projeto

```text
br.com.coupon.api.cupom
├── CupomApplication.java         
├── controller/
├── dto/
├── entity/
├── exception/                  
├── mapper/                      
├── repository/
├── service/
└── validator/                    
