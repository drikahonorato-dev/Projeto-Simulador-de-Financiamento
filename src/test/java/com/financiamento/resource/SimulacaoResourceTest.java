package com.financiamento.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class SimulacaoResourceTest {

    @Test
    void deveCriarSimulacaoERetornar201() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "valorInicial": 1000.00,
                    "taxaJurosMensal": 1.5,
                    "prazoMeses": 12
                }
                """)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("valorTotalFinal", notNullValue())
                .body("memoriaCalculo", hasSize(12));
    }

    @Test
    void deveRetornar400ParaPayloadInvalido() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400);
    }

    @Test
    void deveRetornar404ParaIdInexistente() {
        given()
                .when()
                .get("/simulacoes/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void deveBuscarSimulacaoCriada() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "valorInicial": 2000.00,
                    "taxaJurosMensal": 2.0,
                    "prazoMeses": 6
                }
                """)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/simulacoes/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("prazoMeses", equalTo(6))
                .body("memoriaCalculo", hasSize(6));
    }
}
