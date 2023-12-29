package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
public class Ex5parsing {
    @Test
    public void testRestAssured() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        ArrayList message = response.get("messages");
        System.out.println (message.get(1));

    }
}