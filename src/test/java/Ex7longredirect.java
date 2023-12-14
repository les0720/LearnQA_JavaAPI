import io.restassured.RestAssured;
import io.restassured.filter.log.UrlDecoder;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
public class Ex7longredirect {
    @Test
    public void testRestAssured() {
        String Url = "https://playground.learnqa.ru/api/long_redirect";
        int count = 0;
        String finalUrl;
        do {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(Url)
                    .andReturn();
            int statusCode = response.getStatusCode();
            String locationHeader = response.getHeader("Location");
            finalUrl = Url;

            if (locationHeader != null) {
                Url = locationHeader;
                count = count + 1;
            } else {
                if (statusCode == 200) {
                    break;
                }
            }
        }
            while (true) ;
            System.out.println("finalUrl: " + finalUrl);


    }
}
