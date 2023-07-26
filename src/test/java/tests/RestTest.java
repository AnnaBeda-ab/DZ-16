package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.sql.Date.valueOf;
import static utils.BookingDates.generateDate;

public class RestTest {
    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test(description = "This test checks creating new booking. Task 1 (POST)")
    public void createBookingTest() {
        CreateBookingBody body = new CreateBookingBody().builder()
                .firstname("Bob")
                .lastname("Martin")
                .totalprice(200)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(generateDate("2023-02-20"))
                        .checkout(generateDate("2023-02-25"))
                        .build())
                .additionalneeds("breakfast")
                .build();
        Response response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(body)
                .post("/booking");
        response.as(ResponseBooking.class);
    }

    @Test(description = "This test checks getting all of the bookings' id. Task 2 (GET)")
    public void getIdAllBooksForBookingTest() {
        Response response = RestAssured.get("/booking");
        response.prettyPrint();
        response.then().statusCode(200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("jsonAllBookingIdSchema.json"));
    }

    @Test(description = "This test checks changing the price of the stated booking's id. Task 3 (PATCH)")
    public void changeBookingPriceTest() {
        ChangePrice body = new ChangePrice().builder()
                .totalprice(400)
                .build();
        Response response = RestAssured
                .given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(body)
                .patch("/booking/{id}", 2159);
        response.then().statusCode(200);
        response.as(CreateBookingBody.class);
    }

    @Test(description = "This test checks changing the name and additionalneeds by the stated booking's id. Task 4 (PUT)")
    public void changeBookingNameUpdateAllTest() {
        CreateToken tokenRequest = new CreateToken().builder()
                .username("admin")
                .password("password123")
                .build();
        Response responseToken = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(tokenRequest)
                .post("/auth");
        //responseToken.prettyPrint();
        String token = responseToken.as(ResponseToken.class)
                .getToken();
        CreateBookingBody body = new CreateBookingBody().builder()
                .firstname("Alex")
                .lastname("Martin")
                .totalprice(200)
                .depositpaid(true)
                .bookingdates(BookingDates.builder()
                        .checkin(generateDate("2023-03-10"))
                        .checkout(generateDate("2023-03-20"))
                        .build())
                .additionalneeds("all inclusive")
                .build();
        Response response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .body(body)
                .put("/booking/{id}", 2159);
        response.then().statusCode(200);
        response.as(CreateBookingBody.class);
    }

    @Test(description = "This test checks deleting the booking by id. Task 5 (DELETE)")
    public void deleteBookingTest() {
        CreateToken requestToken = new CreateToken().builder()
                .username("admin")
                .password("password123")
                .build();
        Response token = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestToken)
                .post("/auth");
        Response delBooking = RestAssured.given()
            //    .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
            //    .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .delete("/booking/1");
        delBooking.prettyPrint();
        delBooking.then().statusCode(201);
    }
}


