import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Junit {
    @Test
public void testRestAssured () {
  Response response = RestAssured
          .get("https://playground.learnqa.ru/api/map2")
          .andReturn();
  assertTrue(response.statusCode() ==200, "Unexpected status code");

    }
}