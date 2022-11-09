package flights.api_tests;

import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@SpringBootTest
public class BookerAPITests {
    
    enum Request {
        GET,
        POST
    }

    private static Map<String, Object> input = new HashMap<>();
    private static Map<String, String> header = new HashMap<>();
    static String body = "";

    public static void clearInputProperties() {
        input.clear();
        header.clear();
        body = "";
    }

    public static Response makeRequest(Map<String, ?> params, String path, Request type, String body,
            Map<String, String> header) {// Add (String body) if you want to receive the body
        Response temp2 = null;
        String response = "Error";
        header.put("content-type", "application/json");
        RequestSpecification temp = given()
                .headers(header)
                .queryParams(params)
                .body(body)
                .when();

        switch (type) {
            case GET:
                temp2 = temp.get(path);
                break;
            case POST:
                temp2 = temp.post(path);
                break;
        }
        return temp2;
    }

    private static Stream<Arguments> validFlightArgs() {
        return Stream.of(
                // Valid names, card numbers, and expiry dates.
                Arguments.of("{\"name\" : \"Eric\", \"cardNumber\" : \"4024007198903550\",\"expiryDate\" : \"01/26\"}"),
                Arguments.of("{\"name\" : \"Dick\", \"cardNumber\" : \"4929514510231509\",\"expiryDate\" : \"02/27\"}"),
                Arguments.of("{\"name\" : \"Fernando\", \"cardNumber\" : \"5514085136430112\",\"expiryDate\" : \"03/28\"}"));
    }

    private static Stream<Arguments> invalidFlightArgs() {
        return Stream.of(
                // Illegal name
                Arguments.of("{\"name\" : \"as%$d%f%\", \"cardNumber\" : \"4024007198903550\",\"expiryDate\" : \"01/26\"}"),
                // Invalid card number
                Arguments.of("{\"name\" : \"Dick\", \"cardNumber\" : \"402400198550\",\"expiryDate\" : \"01/26\"}"),
                // Expired card
                Arguments.of("{\"name\" : \"Fernando\", \"cardNumber\" : \"4532952632815200981\",\"expiryDate\" : \"01/14\"}"));
    }

    @ParameterizedTest
    @MethodSource("validFlightArgs")
	void testConfirmBookingValidRequest(String bodyString) {
        RestAssured.baseURI = "http://localhost:8081";
        body = bodyString;
        Response response = makeRequest(input, "/book", Request.POST, body, header);
        clearInputProperties();
        assertEquals(201,response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("invalidFlightArgs")
	void testConfirmBookingInvalidRequest(String bodyString) {
        RestAssured.baseURI = "http://localhost:8081";
        body = bodyString;
        Response response = makeRequest(input, "/book", Request.POST, body, header);
        clearInputProperties();
        assertEquals(400,response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource( strings = "{\"name\" : \"as%$d%f%\", \"cardNumber\" : \"4024007198903550\",\"expiryDate\" : \"01/26\"}")
	void testConfirmBookingInvalidRequestName(String bodyString) {
        RestAssured.baseURI = "http://localhost:8081";
        body = bodyString;
        Response response = makeRequest(input, "/book", Request.POST, body, header);
        clearInputProperties();
        String expectedIssue = "Illegal characters in name";
        assertEquals(expectedIssue, response.getBody().jsonPath().get("message"));
    }

    @ParameterizedTest
    @ValueSource( strings = "{\"name\" : \"Eric\", \"cardNumber\" : \"402400\",\"expiryDate\" : \"01/26\"}")
	void testConfirmBookingInvalidRequestCardNumber(String bodyString) {
        RestAssured.baseURI = "http://localhost:8081";
        body = bodyString;
        Response response = makeRequest(input, "/book", Request.POST, body, header);
        clearInputProperties();
        String expectedIssue = "Invalid card number";
        assertEquals(expectedIssue, response.getBody().jsonPath().get("message"));
    }

    @ParameterizedTest
    @ValueSource( strings = "{\"name\" : \"Fernando\", \"cardNumber\" : \"4024007198903550\",\"expiryDate\" : \"01/20\"}")
	void testConfirmBookingInvalidRequestCardDate(String bodyString) {
        RestAssured.baseURI = "http://localhost:8081";
        body = bodyString;
        Response response = makeRequest(input, "/book", Request.POST, body, header);
        clearInputProperties();
        String expectedIssue = "Card has already expired";
        assertEquals(expectedIssue, response.getBody().jsonPath().get("message"));
    }
}
