package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import tech.noetzold.controller.BadLanguageController;

@QuarkusTest
@TestHTTPEndpoint(BadLanguageController.class)
public class BadLanguageControllerTest {
}
