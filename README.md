# Coupon API

API REST de cupons de desconto em **Spring Boot 4 / Java 17**, com regras de negócio encapsuladas em um objeto de **domínio** (`Cupom`) separado da **entidade JPA** (`CupomEntity`).

## Como rodar

```bash
./mvnw spring-boot:run
# ou
docker compose up --build
```

- API: http://localhost:8080/coupon
- Swagger: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (JDBC `jdbc:h2:mem:cupomdb`, user `sa`, sem senha)

## Endpoints

| Método | Rota              | Descrição                                |
|--------|-------------------|------------------------------------------|
| POST   | `/coupon`         | Cria um cupom (pode nascer publicado)    |
| DELETE | `/coupon/{id}`    | Soft delete                              |

## Regras (todas em `Cupom.java`)

- Campos obrigatórios: `code`, `description`, `discountValue`, `expirationDate`.
- `code` alfanumérico de 6 caracteres; especiais são removidos antes de salvar.
- `discountValue` mínimo `0.5` (sem máximo, valor absoluto).
- `expirationDate` não pode estar no passado.
- Cupom pode nascer publicado (`published = true`).
- Soft delete; cupom já deletado não pode ser deletado de novo.

## Testes

```bash
./mvnw test
```

## Estrutura

```
br.com.coupon.api.cupom
├── CupomApplication
├── domain/                  regras puras, sem framework
│   ├── Cupom                domínio + factory + deletar()
│   ├── CupomException
│   └── CupomNaoEncontradoException
├── infrastructure/          JPA
│   ├── CupomEntity          @Entity + toDomain()/from(Cupom)
│   └── CupomRepository      Spring Data JpaRepository
├── application/             caso de uso
│   └── CupomService
└── web/                     HTTP
    ├── CupomController
    ├── CupomRequest         entrada (Bean Validation)
    ├── CupomResponse        saída (sem expor flag deleted)
    └── GlobalExceptionHandler
```
