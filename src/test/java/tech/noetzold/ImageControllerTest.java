package tech.noetzold;


import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import tech.noetzold.controller.ImageController;

@QuarkusTest
@TestHTTPEndpoint(ImageController.class)
public class ImageControllerTest {
}
