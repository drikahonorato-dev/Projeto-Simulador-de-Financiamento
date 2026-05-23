meu GitHub https://github.com/drikahonorato-dev/Projeto-Simulador-de-Financiamento.git

# Simulador de Financiamentos — API REST

API backend para simulação de financiamentos com cálculo de juros compostos, memória de cálculo detalhada e persistência em banco H2 embutido.

---

## Stack

| Item | Tecnologia |
|---|---|
| Linguagem | Java 25 |
| Framework | Quarkus 3.35.3 |
| Banco de Dados | H2 (embutido, arquivo local) |
| Testes | JUnit 5 + Mockito + RestAssured |
| Cobertura | JaCoCo (threshold minimo: 80%) |
| Documentacao | OpenAPI / Swagger UI (SmallRye) |

---

## Pre-requisitos

- Java 25 instalado
- Maven (ou usar o wrapper mvnw incluido no projeto)
- Sem necessidade de Docker ou scripts SQL manuais

---

## Como compilar

```bash
./mvnw clean package -DskipTests
```

No Windows:
```powershell
.\mvnw clean package -DskipTests
```

---

## Como executar a aplicacao

```bash
./mvnw quarkus:dev
```

No Windows:
```powershell
.\mvnw quarkus:dev
```

A aplicacao sobe automaticamente em http://localhost:8080 e cria o schema do banco H2 sozinha.

---

## Como rodar os testes e validar a cobertura

### Comando exato:

```bash
./mvnw clean test
```

No Windows:
```powershell
.\mvnw clean test
```

Este comando:
1. Compila o projeto
2. Executa os 37 testes (unitarios e integracao)
3. Gera o relatorio de cobertura JaCoCo
4. Falha o build se a cobertura for inferior a 80%

### Ver relatorio de cobertura:

Apos rodar os testes, abra no navegador:

No Windows:
```powershell
Start-Process "target\site\jacoco\index.html"
```

Cobertura atual: 99%

---

## Endpoints da API

### POST /simulacoes - Criar simulacao

Request:
```json
{
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12
}
```

Response 201 Created:
```json
{
  "id": 1,
  "valorInicial": 1000.00,
  "taxaJurosMensal": 1.5,
  "prazoMeses": 12,
  "valorTotalFinal": 1195.62,
  "valorTotalJuros": 195.62,
  "memoriaCalculo": [
    {
      "mes": 1,
      "saldoInicial": 1000.00,
      "juro": 15.00,
      "saldoFinal": 1015.00
    }
  ]
}
```

| Codigo | Situacao |
|---|---|
| 201 | Simulacao criada com sucesso |
| 400 | Payload invalido |
| 404 | ID nao encontrado |

---

### GET /simulacoes/{id} - Consultar simulacao

Response 200 OK: objeto completo da simulacao com memoria de calculo.

---

## Documentacao interativa (Swagger UI)

Com a aplicacao rodando, acesse:
http://localhost:8080/swagger-ui

---

## Estrutura do projeto
---

## Suite de testes

| Classe | Tipo | Testes |
|---|---|---|
| SimulacaoServiceTest | Unitario (Mockito) | 12 |
| SimulacaoResourceTest | Integracao (RestAssured) | 4 |
| SimulacaoRequestDTOTest | Unitario (Bean Validation) | 13 |
| NotFoundExceptionMapperTest | Unitario | 4 |
| ValidationExceptionMapperTest | Unitario (Mockito) | 4 |
| Total | | 37 |