package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a Post-request")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-request Registration")
    public Response makePostRegistrationRequest(String url, Map<String, String> userData) {
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT-request Edit user with Auth")
    public Response makePutEditWithAuthRequest(String url, Map<String, String> editData, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request Edit user")
    public Response makePutEditWithOutAuthRequest(String url, Map<String, String> userData) {
        return given()
                .filter(new AllureRestAssured())
                .body(userData)
                .put(url)
                .andReturn();
    }

    @Step("Make a POST-request to edit user")
    public Response makePostRequestEditUser(String url, String token, String cookie, Map<String, String> editData, String userId) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", token)
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url + userId)
                .andReturn();
    }

    @Step("Make a DELETE-request")
    public Response makeDeleteUserWithAuth(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }

    @Step("Make a GET-request without auth")
    public Response makeGetRequestWithOutAuth(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request to get data user")
    public JsonPath makePostRequestUser(String url, Map<String, String> userData) {
        JsonPath responseCreateAuth =
                given()
                        .filter(new AllureRestAssured())
                        .body(userData)
                        .post(url)
                        .jsonPath();
        return responseCreateAuth;
    }

    @Step("Make a POST-request to delete user only userId")
    public Response makePostRequestDeleteUser(String url,String token, String cookie, String userId) {
        return  given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url + userId)
                .andReturn();
    }

    @Step("Make a Get-request with token, auth cookie, Id")
    public Response makeGetRequestWithId(String url, String userId) {
        return given()
                .filter(new AllureRestAssured())
                .get(url+userId)
                .andReturn();
    }

}

