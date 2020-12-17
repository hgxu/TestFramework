import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUploadTest {
    static String userDirectory;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        userDirectory = System.getProperty("user.dir");
    }

    @Test
    public void uploadFileTest() {
        String file = "/Users/gracexu/consumer.pdf";
        File testUploadFile = new File(file);

        String endpoint = "/process";
        Response response = given()
                .multiPart(testUploadFile)
                .when().
                        post(endpoint);

        System.out.println(response.getBody().prettyPrint());

        assertEquals(200, response.getStatusCode());

        String fileName = response.jsonPath().get("fileName");
        String fileDownloadUri = response.jsonPath().get("fileDownloadUri");
        String fileType = response.jsonPath().get("fileType");
        int size = response.jsonPath().get("size");


        assertEquals("consumer.pdf", fileName);
        assertEquals("http://localhost:8080/downloadFile/" + fileName, fileDownloadUri);
        assertEquals("application/octet-stream", fileType);
        assertEquals(36315, size);

        // We can implement GetFiel API so call get file and assert
    }

    @Test
    public void healthCheckTest(){
        String endpoint = "/health";
        Response response = given().when().get(endpoint);

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void uploadFileIncorrecEndpointTest() {
        String file = "/Users/gracexu/consumer.pdf";
        File testUploadFile = new File( file);

        String endpoint = "/api/process";
        Response response = given()
                .multiPart(testUploadFile)
                .when().
                        post(endpoint);

        assertEquals(404, response.getStatusCode());

    }

    @Test
    public void helpTest(){
        String endpoint = "/help";
        Response response = given().when().get(endpoint);

        assertEquals(200, response.getStatusCode());
    }
}
