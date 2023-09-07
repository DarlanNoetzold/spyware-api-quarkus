package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import tech.noetzold.controller.BadLanguageController;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(BadLanguageController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BadLanguageControllerTest {

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
    public void testGetAllBadLanguages() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .get("http://localhost:9000/language?page=1&size=10&sortBy=id")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetBadLanguageById() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .get("http://localhost:9000/language/1")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveBadLanguage() {
        final JsonObject json = Json.createObjectBuilder()
                .add("word", "Test Word")
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json.toString())
                .when()
                .post("http://localhost:9000/language")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testRemoveBadLanguage() {
        given()
                .auth().oauth2(accessToken)
                .when()
                .delete("http://localhost:9000/language/1")
                .then()
                .statusCode(202);
    }
}
