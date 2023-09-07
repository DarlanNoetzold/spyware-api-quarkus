package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import tech.noetzold.controller.MaliciousWebsiteController;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(MaliciousWebsiteController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MaliciousWebsiteControllerTest {

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
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAllMaliciousWebsites() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .get("http://localhost:9000/website?page=1&size=10&sortBy=id")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetMaliciousWebsiteById() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .get("http://localhost:9000/website/1")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveMaliciousWebsite() {
        final JsonObject json = Json.createObjectBuilder()
                .add("url", "http://example.com")
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:9000/website")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testRemoveMaliciousWebsite() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .delete("http://localhost:9000/website/1")
                .then()
                .statusCode(202);
    }
}