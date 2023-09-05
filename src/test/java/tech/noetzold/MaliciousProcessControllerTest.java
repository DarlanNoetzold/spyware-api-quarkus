package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import tech.noetzold.controller.MaliciousProcessController;

@QuarkusTest
@TestHTTPEndpoint(MaliciousProcessController.class)
public class MaliciousProcessControllerTest {
}
