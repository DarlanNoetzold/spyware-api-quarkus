package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tech.noetzold.controller.AlertController;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@QuarkusTest
@TestHTTPEndpoint(AlertController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlertControllerTest {

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAllAlerts() {
        when()
                .get("/alert?page=1&size=10&sortBy=id")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("alert"));
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAlertById() {
        when()
                .get("/alert/1")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("alert"));
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAlertByPcId() {
        when()
                .get("/alert/pcId/somePcId")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("alert"));
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveAlert() {
        JsonObject json = Json.createObjectBuilder()
                .add("id", 1)
                .add("message", "Test Alert")
                .add("image", Json.createObjectBuilder()
                        .add("id", 1)
                        .build())
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("/alert")
                .then()
                .statusCode(201)
                .body(CoreMatchers.containsString("Test Alert"));
    }

    @Test
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testRemoveAlert() {
        when()
                .delete("/alert/1")
                .then()
                .statusCode(202);
    }
}
