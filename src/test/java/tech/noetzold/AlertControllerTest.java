package tech.noetzold;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import tech.noetzold.controller.AlertController;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(AlertController.class)
public class AlertControllerTest {

    private String accessToken;

    @BeforeEach
    public void obtainAccessToken() {
        final String username = "admin";
        final String password = "admin";

        final String tokenEndpoint = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";

        final Map<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        requestData.put("grant_type", "password");

        final Response response = given()
                .auth().preemptive().basic("backend-service", "secret")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParams(requestData)
                .when()
                .post(tokenEndpoint);

        response.then().statusCode(200);

        this.accessToken = response.jsonPath().getString("access_token");
    }


    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAllAlerts() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:9000/alert?page=1&size=10&sortBy=id")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAlertById() {
        // Use the accessToken in your request headers
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:9000/alert/51")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAlertByPcId() {
        // Use the accessToken in your request headers
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("http://localhost:9000/alert/pcId/asdasdasdasd")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveAlert() {
        JsonObject json = Json.createObjectBuilder()
                .add("pcId", "somePcId")
                .add("image", "teste")
                .build();

        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:9000/alert")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testRemoveAlert() {
        given()
                .header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete("http://localhost:9000/alert/1")
                .then()
                .statusCode(202);
    }
}
