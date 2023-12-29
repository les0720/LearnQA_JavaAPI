package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Junit2 {
    @Test
    public void testRestAssured () {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map2")
                .andReturn();
        assertEquals(200, response.statusCode(), "Unexpected status code");

    }
}
