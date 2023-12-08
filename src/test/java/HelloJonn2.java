import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloJonn2 {
    @Test
    public void testRestAssured(){
        Map<String,String> params = new HashMap<>();
        params.put("name","Jonn");
        Response response = RestAssured
                .given()
                .queryParams(params)
                .get( "https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }
}
