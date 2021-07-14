package io.quarkus.sample;

import java.util.stream.Stream;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoResourceTest {

    @Test @Order(1)
    public void testGetAll() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api")
        .then()
            .statusCode(200)
            .body(is(ALL));
    }
    
    @Test @Order(2)
    public void testGet() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/api/1")
        .then()
            .statusCode(200)
            .body(is(ONE));
    }

    @Test @Order(3)
    public void testCreateNew() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .body(CREATE_NEW)
            .post("/api")
        .then()
            .statusCode(201)
            .body(is(NEW_CREATED));
    }

    @Test @Order(4)
    public void testUpdate() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .body(UPDATE)
            .patch("/api/5")
        .then()
            .statusCode(200)
            .body(is(UPDATED));
    }

    @ParameterizedTest @Order(5)
    @MethodSource("todoItemsToDelete")
    public void testDelete(int id, int expectedStatus) {
        given()
                .pathParam("id", id)
        .when()
                .delete("/api/{id}")
        .then()
                .statusCode(expectedStatus);
    }

    private static Stream<Arguments> todoItemsToDelete() {
        return Stream.of(
                Arguments.of(5, 204),
                Arguments.of(15, 404)
        );
    }

    private static final String ALL = "[{\"id\":1,\"completed\":true,\"order\":0,\"title\":\"Introduction to Quarkus\"},{\"id\":2,\"completed\":false,\"order\":1,\"title\":\"Hibernate with Panache\"},{\"id\":3,\"completed\":false,\"order\":2,\"title\":\"Visit Quarkus web site\",\"url\":\"https://quarkus.io\"},{\"id\":4,\"completed\":false,\"order\":3,\"title\":\"Star Quarkus project\",\"url\":\"https://github.com/quarkusio/quarkus/\"}]";

    private static final String ONE = "{\"id\":1,\"completed\":true,\"order\":0,\"title\":\"Introduction to Quarkus\"}";

    private static final String CREATE_NEW = "{\"completed\":false,\"order\":0,\"title\":\"Use the REST Endpoint\"}";

    private static final String NEW_CREATED = "{\"id\":5,\"completed\":false,\"order\":0,\"title\":\"Use the REST Endpoint\"}";

    private static final String UPDATE = "{\"id\":5,\"completed\":false,\"order\":0,\"title\":\"Use the GraphQL Endpoint\"}";

    private static final String UPDATED = "{\"id\":5,\"completed\":false,\"order\":0,\"title\":\"Use the GraphQL Endpoint\"}";
}