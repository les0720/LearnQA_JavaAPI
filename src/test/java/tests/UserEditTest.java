package tests;

import io.qameta.allure.*;

import io.restassured.RestAssured;
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

@Epic("Edit user cases")
@Feature("Edit user")

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userID = responseCreateAuth.getString("id");

        //Login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EditUser
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userID)
                .andReturn();

        //Get
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userID)
                .andReturn();

        Assertions.asserJsonByName(responseUserData, "firstName", newName);

    }

    @Test //Пытаемся изменить данные пользователя, будучи неавторизованными
    @Description("Edit data user without authorization")
    @DisplayName("Test negative edit data user without authorization")
    @Owner("les0720")
    public void testEditWithOutAuth(){

        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userID = responseCreateAuth.jsonPath().getString("id");

        //Edit user without auth
        String newName = "Any name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = apiCoreRequests
                .makePutEditWithOutAuthRequest("https://playground.learnqa.ru/api/user/"+ userID, userData);
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

        //Login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        //Get data
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+ userID, header, cookie);
        Assertions.asserJsonByName(responseUserData, "firstName", "learnqa");
    }

    @Test// Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем
    @Description("Edit user's data while being authorized by another user")
    @DisplayName("Test edit another user's data")
    @Flaky
    @Issue("FGY-1111")
    public void testEditAnotherUser(){
        //GENERATE USER 1
        Map<String,String> userData1 = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData1);
        String userID = responseCreateAuth.jsonPath().getString("id");

        //GENERATE USER 2
        Map<String,String> userData2 = DataGenerator.getRegistrationData();
        Response responseCreateAuth2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData2);
        //String userID = responseCreateAuth.jsonPath().getString("id");

        //Login USER 2
        Map<String,String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));
        Response responseGetAuth2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);
        String cookie2 = this.getCookie(responseGetAuth2, "auth_sid");
        String header2 = this.getHeader(responseGetAuth2, "x-csrf-token");

        //Edit User1 Auth with User 2
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = apiCoreRequests
                .makePutEditWithAuthRequest("https://playground.learnqa.ru/api/user/" + userID, editData, header2, cookie2);
        Assertions.assertResponseCodeEquals(responseEditUser, 200);

        //Login USER 1
        Map<String,String> authData1 = new HashMap<>();
        authData1.put("email", userData1.get("email"));
        authData1.put("password", userData1.get("password"));
        Response responseGetAuth1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);
        String cookie1 = this.getCookie(responseGetAuth1, "auth_sid");
        String header1 = this.getHeader(responseGetAuth1, "x-csrf-token");

        //Get User 1 data
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+ userID, header1, cookie1);
        Assertions.asserJsonByName(responseUserData, "firstName", "learnqa");
    }

    @Test//Изменить email пользователя, авторизованными тем же пользователем, на email без символа @
    @Description("Edit email user authorized by the same user to an email without a symbol @")
    @DisplayName("Test edit user - email without a symbol @")
    public void testEditUserWrongEmail(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userID = responseCreateAuth.jsonPath().getString("id");

        //Login USER
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        //Edit
        String mail = "vinkotov.ru";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", mail);

        Response responseEditUser = apiCoreRequests.makePostRequestEditUser(
                "https://playground.learnqa.ru/api/user/",
                header, cookie, editData, userID);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);


        //Get User data
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+ userID, header, cookie);

        Assertions.asserJsonByName(responseUserData, "email", userData.get("email"));
    }


    @Test//Изменить firstName пользователя, авторизованными тем же пользователем, на значение в 1 символ
    @Description("Edit firstName to a short value of one character")
    @DisplayName("Test edit firstName to a very short value")
    public void testEditUserShortFirstName(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userID = responseCreateAuth.jsonPath().getString("id");

        //Login USER
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");

        //Edit User Auth short firstName
        String firstName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", firstName);
        Response responseEditUser = apiCoreRequests
                .makePutEditWithAuthRequest("https://playground.learnqa.ru/api/user/" + userID, editData, header, cookie);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.asserJsonByName(responseEditUser,"error","Too short value for field firstName");

        //Get User  data
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+ userID, header, cookie);
        Assertions.asserJsonByName(responseUserData, "firstName", "learnqa");

    }
}

