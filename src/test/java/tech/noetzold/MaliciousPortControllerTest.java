package tech.noetzold;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import tech.noetzold.controller.MaliciousPortController;

@QuarkusTest
@TestHTTPEndpoint(MaliciousPortController.class)
public class MaliciousPortControllerTest {
}
