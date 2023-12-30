package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User Delete tests")
@Feature("UserDelete")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    String cookie;
    String header;
    int userIdOnAuth;

    @Test //  Удалить пользователя по ID 2
    @Description("Delete a user by ID 2")
    @DisplayName("Test delete a user by ID 2")
    public void loginUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

        Response responseDeleteUser2 = apiCoreRequests
                .makeDeleteUserWithAuth("https://playground.learnqa.ru/api/user/" +userIdOnAuth, this.header, this.cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser2, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser2, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test // Создать пользователя, авторизоваться,удалить, получить его данные по ID
    @Description("test of delete user with auth,delete, get his data by ID")
    @DisplayName("Test positive delete user with auth")
    public void testDeleteNewUser(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        this.userIdOnAuth = this.getIntFromJson(responseCreateAuth,"id");

        //Login USER
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //Dellete User
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteUserWithAuth("https://playground.learnqa.ru/api/user/" +userIdOnAuth, header, cookie);
        //Get Deleted user info
        Response responseGetDeletedUser = apiCoreRequests
                .makeGetRequestWithOutAuth("https://playground.learnqa.ru/api/user/" +userIdOnAuth);

        Assertions.assertResponseCodeEquals(responseGetDeletedUser, 404);
        Assertions.assertResponseTextEquals(responseGetDeletedUser, "User not found");
    }

    @Test // удалить пользователя, авторизованным другим пользователем
    @Description("Delete a user authorized by another user")
    @DisplayName("Test negative delete user authorized by another user")
    public void testDeleteAnotherUser(){
        //GENERATE USER1
        Map<String, String> user1 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestUser(
                "https://playground.learnqa.ru/api/user/",
                user1);

        //LOGIN USER1
        Map<String, String> authData = new HashMap<>();
        authData.put("email", user1.get("email"));
        authData.put("password", user1.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login"
                        , authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");

        //GENERATE USER 2
        Map<String, String> user2 = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuthUserTwo = apiCoreRequests.makePostRequestUser(
                "https://playground.learnqa.ru/api/user/",
                user2);
        String user2Id = responseCreateAuthUserTwo.getString("id");

        //DELETE
        Response responseDeleteUser = apiCoreRequests.makePostRequestDeleteUser(
                "https://playground.learnqa.ru/api/user/",
                this.header,
                this.cookie,
                user2Id);

        //GET
        Response responseUserData2 = apiCoreRequests.makeGetRequestWithId(
                "https://playground.learnqa.ru/api/user/",
                user2Id);

        Assertions.assertJsonHasField(responseUserData2, "username");
    }
}
