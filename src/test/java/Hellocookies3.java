import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
public class Hellocookies3 {
    @Test
    public void testRestAssured() {
        Map<String, String> data = new HashMap<>();
        data.put("login2", "secret_login");
        data.put("password", "secret_pass2");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = response.getCookie ("auth_cookie");
        System.out.println(responseCookie);

    }
}

