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

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

//import org.testng.Assert;

public class GeneratorAPITests {

    enum Request {
        GET,
        POST
    }

    private static Map<String, Object> input = new HashMap<>();
    private static Map<String, String> header = new HashMap<>();
    static String body = "";

    public static void clearInputMaps() {
        input.clear();
        header.clear();
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

    public static void main(String[] args) {

    }

    private static Stream<Arguments> getFlightsArgs() {
        return Stream.of(
                Arguments.of("{\"date\" : \"2023-01-10\", \"origin\" : \"Madrid\",\"destination\" : \"Sevilla\"}"),
                Arguments.of("{\"date\" : \"2023-02-20\", \"origin\" : \"Dublin\",\"destination\" : \"Lisbon\"}"),
                Arguments.of("{\"date\" : \"2023-03-30\", \"origin\" : \"Rome\",\"destination\" : \"Sao Paulo\"}"));
    }

    @ParameterizedTest
    @MethodSource("getFlightsArgs")
    void testGetFlightsOneway(String bodyString) {
        RestAssured.baseURI = "http://localhost:8080/destination";
        body = bodyString;
        Response response = makeRequest(input, "/day", Request.POST, body, header);
        assertEquals(201,response.getStatusCode());
        clearInputMaps();
    }

    @Test
    void testGetFlightsRoundtrip() {
        //
    }

    @Test
    void testGetDestination(String bodyString) {
    }

    @Test
    void testGetMapping() {
    }

    @Test
    void testApplyFilters() {
    }

}
