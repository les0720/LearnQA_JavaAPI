import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex12 {

    @Test
    public void testHomeworkHeader() {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Headers header = response.getHeaders();
        String valueHeader = response.getHeader("X-Secret-Homework-Header");

        assertTrue(header.hasHeaderWithName("x-Secret-Homework-Header"), "Response doesn't have 'X-Secret-Homework-Header' header");
        assertEquals("Some secret value", valueHeader, "Response doesn't have header value 'Some secret value'");
    }
}
