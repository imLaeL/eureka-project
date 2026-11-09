# Sistema de empréstimo de equipamentos

Implementação dos três microsserviços de domínio solicitados no exercício:

- `msusuario` (porta 8081): cadastro e consulta de usuários.
- `msequipamentos` (porta 8082): cadastro, consulta, listagem de disponíveis e reserva/liberação atômica.
- `msemprestimo` (porta 8083): empréstimos, devoluções e comunicação síncrona via OpenFeign.

`discovery-server` e `api-gateway` não fazem parte deste pacote, conforme solicitado. Os clientes Eureka ficam desabilitados por padrão para permitir execução local direta. Para usar Eureka em uma infraestrutura externa, defina `EUREKA_ENABLED=true` e `EUREKA_URL`; nesse cenário, os nomes registrados são `msusuario`, `msequipamentos` e `msemprestimo`.

## Requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

Cada aplicação usa um banco/schema independente (`msusuario`, `msequipamentos` e `msemprestimo`) e executa sua própria migration Flyway na inicialização. Não existem chaves estrangeiras entre serviços.

## Execução

Em três terminais, execute:

```bash
cd msusuario && mvn spring-boot:run
cd msequipamentos && mvn spring-boot:run
cd msemprestimo && mvn spring-boot:run
```

O padrão de conexão é `root/root`. Para alterar, use `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.

O `msemprestimo` chama `msusuario` e `msequipamentos` por descoberta de serviço quando Eureka está habilitado. Com Eureka desabilitado (padrão), o Spring Cloud LoadBalancer usa as instâncias locais `localhost:8081` e `localhost:8082` configuradas no `application.yml` do `msemprestimo`.

## Fluxo principal

1. Criar usuário em `POST http://localhost:8081/api/v1/usuarios`.
2. Criar equipamento em `POST http://localhost:8082/api/v1/equipamentos`.
3. Solicitar empréstimo em `POST http://localhost:8083/api/v1/emprestimos`.
4. Devolver em `PATCH http://localhost:8083/api/v1/emprestimos/{id}/devolucao`.

Os exemplos completos estão em `postman/emprestimo.postman_collection.json`.
