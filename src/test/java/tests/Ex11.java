package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11 {

    @Test
    public void testHomeworkCookie() {

        String Url = "https://playground.learnqa.ru/api/homework_cookie";

            Response response = RestAssured
                    .given()
                    .get(Url)
                    .andReturn();

            Map<String, String> cookies = response.getCookies();
        assertEquals("hw_value", cookies.get("HomeWork"));
    }

}
