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
class PricesAPITests {

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

	private static Stream<Arguments> validPriceArgs() {
		return Stream.of(
				// Valid names, card numbers, and expiry dates.
				Arguments.of(
						"{\"totalPassengers\" : \"5\", \"luggage\" : \"3\",\"kids\" : \"1\",\"babies\" : \"1\",\"basePrice\" : \"350\"}"),
				Arguments.of(
						"{\"totalPassengers\" : \"9\", \"luggage\" : \"6\",\"kids\" : \"2\",\"babies\" : \"2\",\"basePrice\" : \"450\"}"),
				Arguments.of(
						"{\"totalPassengers\" : \"15\", \"luggage\" : \"10\",\"kids\" : \"4\",\"babies\" : \"3\",\"basePrice\" : \"550\"}"));
	}

	private static Stream<Arguments> invalidFlightArgs() {
		return Stream.of(
				// Illegal name
				Arguments.of(
						"{\"totalPassengers\" : \"as%$d%f%\", \"cardNumber\" : \"4024007198903550\",\"expiryDate\" : \"01/26\"}"),
				// Invalid card number
				Arguments.of("{\"name\" : \"Dick\", \"cardNumber\" : \"402400198550\",\"expiryDate\" : \"01/26\"}"),
				// Expired card
				Arguments.of(
						"{\"name\" : \"Fernando\", \"cardNumber\" : \"4532952632815200981\",\"expiryDate\" : \"01/14\"}"));
	}

	@ParameterizedTest
	@MethodSource("validPriceArgs")
	void testPricesValidRequest(String bodyString) {
		RestAssured.baseURI = "http://localhost:8082";
		body = bodyString;
		Response response = makeRequest(input, "/price", Request.POST, body, header);
		clearInputProperties();
		assertEquals(201, response.getStatusCode());
	}

	@ParameterizedTest
	@ValueSource(strings = { "{\"totalPassengers\" : \"1\"}", "{\"totalPassengers\" : \"15\"}",
			"{\"totalPassengers\" : \"45\"}", "{\"totalPassengers\" : \"999\"}", })
	void testPricesInvalidRequestName(String bodyString) {
		RestAssured.baseURI = "http://localhost:8082";
		body = bodyString;
		Response response = makeRequest(input, "/price", Request.POST, body, header);
		clearInputProperties();
		assertEquals(201, response.getStatusCode());
	}

}