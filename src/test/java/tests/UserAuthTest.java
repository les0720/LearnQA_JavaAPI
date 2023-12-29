import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;
import lib.ApiCoreRequests;

import java.util.HashMap;
import java.util.Map;

import static  org.junit.jupiter.api.Assertions.assertTrue;
import static  org.junit.jupiter.api.Assertions.assertEquals;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

//allure
@Epic("Authorization cases")
@Feature("Authorization")

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description ("This test successfully authorize user by email and password")//описание теста
    @DisplayName("Test positive auth user")//название теста в отчете
    public void testAuthUser() {
       Response responseCheckAuth = apiCoreRequests
               .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                       this.header,
                       this.cookie
               );
       Assertions.asserJsonByName(responseCheckAuth,"user_id", this.userIdOnAuth);

    }

    @Description ("This test check authorization status w/o sending auth cookie or token")//описание для отчета Allure
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition) {
        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.asserJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.asserJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}