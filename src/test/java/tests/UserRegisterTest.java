package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Register")

public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test //тест на создание юзера с существующим email
    public void testCreateUserWithExistingEmail() {
        String email = "vincotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        Assertions.assertResponseTextEquals(responseCreateAuth,"Users with email '" + email + "' already exists");
    }

    @Test //успешное создание пользователя
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
    }

    @Test //с некорректным email - без символа @
    @Description("Testing of registration with an invalid email")
    @DisplayName("Negative Test creating users with an invalid email")
    public void testCreateUserWrongEmail(){
            String email = "vinkotov.ru";
            Map<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData = DataGenerator.getRegistrationData(userData);

            Response responseCreateAuth = apiCoreRequests
                    .makePostRegistrationRequest(
                            "https://playground.learnqa.ru/api/user/",
                            userData);

            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @ParameterizedTest //Создание пользователя без указания одного из полей
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @Description("Creating a user without specifying one of the fields")
    @DisplayName("Negative Test creating users without required fields")
    public void testCreateUserWithoutField(String field){
        String email = DataGenerator.getRandomEmail();
        Map<String,String> userData = DataGenerator.getRegistrationData();
        userData.remove(field);

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test // Создание пользователя с очень коротким именем в один символ
    @Description("Creating a user with a very short name in one character")
    @DisplayName("The test of positive registration of a user with a name of 1 character")
    public void testCreateUserWithShortName(){
        String firstName = "a";
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationRequest(
                        "https://playground.learnqa.ru/api/user/",
                        userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'firstName' field is too short");
    }

    @Test //Создание пользователя с очень длинным именем - длиннее 250 символов
    @Description("Testing registration with userName length more than 250 character")
    @DisplayName("Test registration with userName length more than 250 character")
    public void testCreateUserWithLongName(){
        String email = DataGenerator.getRandomEmail();
        Map<String,String> userData = DataGenerator.getRegistrationData();
        String str = RandomStringUtils.randomAlphabetic(252);  //сгенерировать имя
        userData.replace("firstName", str); // замена

        Response responseCreateAuth = apiCoreRequests
                .makePostRegistrationRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    }


}
