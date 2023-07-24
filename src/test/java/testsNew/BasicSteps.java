package testsNew;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import utils.CreateToken;
import utils.ResponseToken;

@Getter
@Setter
public class BasicSteps {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    public static String getToken() {
        CreateToken tokenCreate = new CreateToken().builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();
        Response tokenRquest = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(tokenCreate)
                .post("/auth");
        String token = tokenRquest.as(ResponseToken.class)
                .getToken();
        return token;
    }
}
