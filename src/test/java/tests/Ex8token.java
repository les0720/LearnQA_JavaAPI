package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;
public class Ex8token {
    @Test
    public void testRestAssured() {

        String Url = "https://playground.learnqa.ru/ajax/api/longtime_job";

            JsonPath responseTask = RestAssured
                    .get(Url)
                    .jsonPath();

            String token=responseTask.get("token");
            int seconds=responseTask.get("seconds");

        JsonPath responsewithtoken = RestAssured
                .given()
                .queryParam("token",token)
                .get(Url)
                .jsonPath();

        String status=responsewithtoken.get("status");

        if ("Job is NOT ready".equals(status)) { // проверка статуса
            System.out.println("Job status ok");
        }
        else {
            System.out.println("Job status not ok");
        }

        try {
            Thread.sleep(seconds * 1000); // Ожидание
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JsonPath responseWithTokenAfterDelay = RestAssured
                .given()
                .queryParam("token",token)
                .get(Url)
                .jsonPath();

        status=responseWithTokenAfterDelay.get("status");
        String result=responseWithTokenAfterDelay.get("result");

        if ("Job is ready".equals(status)) { // проверка статуса
            System.out.println("Job status: " +status);
        }
        else {
            System.out.println("Job status not ok");
        }

        if (result != null) { // проверка наличия результата
            System.out.println("Result: " +result);
        }
        else {
            System.out.println("result is null");
        }




    }
}
