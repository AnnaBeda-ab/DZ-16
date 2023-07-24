package testsNew;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.sql.Date.valueOf;
import static utils.BookingDates.generateDate;

public class RestTestNew {
    private static int bookingid;
    RequestSpecification specWithAutho;
    RequestSpecification specWithAuthoToken;

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        specWithAutho = new RequestSpecBuilder()
                .addHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .build();
        specWithAuthoToken = new RequestSpecBuilder()
                .addHeader("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                .addHeader("Cookie", "token=" + BasicSteps.getToken())
                .build();
    }

    @Test(description = "This test checks creating new booking. Task 1 (POST)", priority = 1)
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
                .log()
                .all()
                .body(body)
                .post("/booking");
        response.then().statusCode(200);
        response.as(ResponseBooking.class);
        response.as(ResponseBooking.class).getBookingid();
        response.prettyPrint();
        bookingid = response.jsonPath().get("bookingid");
    }

    @Test(description = "This test checks getting all of the bookings' id. Task 2 (GET)")
    public void getIdAllBooksForBookingTest() {
        Response response = RestAssured.get("/booking");
        response.then().statusCode(200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("jsonAllBookingIdSchema.json"));
    }

   @Test(description = "This test checks changing the price of the stated booking's id. Task 3 (PATCH)", priority = 2)
    public void changeBookingPriceTest() {
        ChangePrice body = new ChangePrice().builder()
                .totalprice(400)
                .build();
        Response response = RestAssured
                .given()
                .spec(specWithAuthoToken)
                .log()
                .all()
                .body(body)
                .patch("/booking/{id}", bookingid);
        response.then().statusCode(200);
        response.as(CreateBookingBody.class);
    }

    @Test(description = "This test checks changing the name and additionalneeds by the stated booking's id. Task 4 (PUT)", priority = 3)
    public void changeBookingNameUpdateAllTest() {
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
                .spec(specWithAuthoToken)
                .log()
                .all()
                .body(body)
                .put("/booking/{id}", bookingid);
        response.then().statusCode(200);
        response.as(CreateBookingBody.class);
    }

    @Test(description = "This test checks deleting the booking by id. Task 5 (DELETE)", priority = 4)
    public void deleteBookingTest() {
        Response delBooking = RestAssured.given()
                .spec(specWithAuthoToken)
                .log()
                .all()
                .delete("/booking/{id}",bookingid);
        delBooking.then().statusCode(201);
    }
}


