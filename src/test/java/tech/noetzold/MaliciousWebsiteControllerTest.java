package tech.noetzold;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import tech.noetzold.controller.MaliciousWebsiteController;

@QuarkusTest
@TestHTTPEndpoint(MaliciousWebsiteController.class)
public class MaliciousWebsiteControllerTest {
}
