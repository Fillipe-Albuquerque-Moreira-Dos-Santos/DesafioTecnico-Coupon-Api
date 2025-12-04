# Aplicação de Gestão de Cupons

Este projeto é uma **aplicação web para gerenciamento de cupons de desconto**, construída com **Spring Boot**, **Thymeleaf** e **Bootstrap 5**.  
Permite criar, listar, editar e excluir cupons, além de marcar se estão publicados ou não.

---

## Funcionalidades

http://localhost:8080/cupons
http://localhost:8080/cupons/novo

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

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA**
- **Thymeleaf**
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
