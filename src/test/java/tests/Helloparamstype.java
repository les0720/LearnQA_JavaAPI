package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
public class Helloparamstype {
    @Test
    public void testRestAssured() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.prettyPrint();
    }
}
