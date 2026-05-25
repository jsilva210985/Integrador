package com.integrador.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.integrador.restcontroller.estafetav2.TrackingController;
import org.json.JSONObject;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegadorServiceApplication.class)
public class TrackingControllerTest {

	@Autowired
	private TrackingController trackingController;

	@Test
	public void testTrackingEndpointValidation() {
		System.out.println("====== STARTING PULLTRACKING ENDPOINT VALIDATION TEST ======");
		try {
			// Sending invalid request to verify validation checks
			String content = "{\"cliente\":\"invalid\",\"password\":\"invalid\",\"token\":\"invalid\"}";
			ResponseEntity<String> response = trackingController.tracking(null, content);
			System.out.println("Response Status: " + response.getStatusCode());
			System.out.println("Response Body: " + response.getBody());
			assertEquals(400, response.getStatusCodeValue());
			assertTrue(response.getBody().contains("Usuario o Contraseña invalido"));
		} catch (Exception e) {
			fail("Validation test failed: " + e.getMessage());
		}
		System.out.println("====== ENDING PULLTRACKING ENDPOINT VALIDATION TEST ======");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExtractWaybillsFormats() {
		System.out.println("====== STARTING PULLTRACKING EXTRACT WAYBILLS TEST ======");
		try {
			Method extractMethod = TrackingController.class.getDeclaredMethod("extractWaybills", JSONObject.class);
			extractMethod.setAccessible(true);

			// Format 1: Direct waybill parameter
			JSONObject json1 = new JSONObject("{\"waybill\":\"0010000000112300016901\"}");
			List<String> list1 = (List<String>) extractMethod.invoke(trackingController, json1);
			assertEquals(1, list1.size());
			assertEquals("0010000000112300016901", list1.get(0));

			// Format 2: Direct waybills array or comma-separated
			JSONObject json2a = new JSONObject("{\"waybills\":[\"WB1\", \"WB2\"]}");
			List<String> list2a = (List<String>) extractMethod.invoke(trackingController, json2a);
			assertEquals(2, list2a.size());
			assertEquals("WB1", list2a.get(0));
			assertEquals("WB2", list2a.get(1));

			JSONObject json2b = new JSONObject("{\"waybills\":\"WB3, WB4\"}");
			List<String> list2b = (List<String>) extractMethod.invoke(trackingController, json2b);
			assertEquals(2, list2b.size());
			assertEquals("WB3", list2b.get(0));
			assertEquals("WB4", list2b.get(1));

			// Format 3: ConsultaEnviosV2 nested trackings
			JSONObject json3 = new JSONObject("{\n" +
					"    \"searchType\": {\n" +
					"        \"waybillList\": {\n" +
					"            \"waybills\": {\n" +
					"                \"trackings\": [\"5055909704781720574655\"]\n" +
					"            }\n" +
					"        }\n" +
					"    }\n" +
					"}");
			List<String> list3 = (List<String>) extractMethod.invoke(trackingController, json3);
			assertEquals(1, list3.size());
			assertEquals("5055909704781720574655", list3.get(0));

			// Format 4: ConsultaEnvios V1 nested string array
			JSONObject json4 = new JSONObject("{\n" +
					"    \"searchType\": {\n" +
					"        \"waybillList\": {\n" +
					"            \"waybills\": {\n" +
					"                \"string\": [\"WB7\"]\n" +
					"            }\n" +
					"        }\n" +
					"    }\n" +
					"}");
			List<String> list4 = (List<String>) extractMethod.invoke(trackingController, json4);
			assertEquals(1, list4.size());
			assertEquals("WB7", list4.get(0));

		} catch (Exception e) {
			fail("Extract waybills test failed: " + e.getMessage());
		}
		System.out.println("====== ENDING PULLTRACKING EXTRACT WAYBILLS TEST ======");
	}

	@Test
	public void testShouldIncludeHistoryConfig() {
		System.out.println("====== STARTING PULLTRACKING SHOULD INCLUDE HISTORY TEST ======");
		try {
			Method shouldIncludeHistoryMethod = TrackingController.class.getDeclaredMethod("shouldIncludeHistory", JSONObject.class);
			shouldIncludeHistoryMethod.setAccessible(true);

			// Check user payload format with includeHistory: false
			JSONObject jsonPayload = new JSONObject("{\n" +
					"    \"cliente\" : \"UsuarioGenerador\",\n" +
					"    \"password\" : \"2021UsuarioGenerador.\",\n" +
					"    \"token\": \"abc\",\n" +
					"    \"searchType\": {\n" +
					"        \"type\": \"L\",\n" +
					"        \"waybillList\": {\n" +
					"            \"waybillType\": \"G\",\n" +
					"            \"waybills\": {\n" +
					"                \"trackings\": [\"5055909704781720574655\"]\n" +
					"            }\n" +
					"        }\n" +
					"    },\n" +
					"    \"searchConfiguration\": {\n" +
					"        \"historyConfiguration\": {\n" +
					"            \"historyType\": \"LAST\",\n" +
					"            \"includeHistory\": false\n" +
					"        }\n" +
					"    }\n" +
					"}");

			boolean include1 = (Boolean) shouldIncludeHistoryMethod.invoke(trackingController, jsonPayload);
			assertFalse(include1);

			// Check payload format with includeHistory: true
			JSONObject jsonPayloadTrue = new JSONObject("{\n" +
					"    \"searchConfiguration\": {\n" +
					"        \"historyConfiguration\": {\n" +
					"            \"includeHistory\": true\n" +
					"        }\n" +
					"    }\n" +
					"}");
			boolean include2 = (Boolean) shouldIncludeHistoryMethod.invoke(trackingController, jsonPayloadTrue);
			assertTrue(include2);

			// Check empty / missing config defaults to true
			JSONObject emptyJson = new JSONObject("{}");
			boolean include3 = (Boolean) shouldIncludeHistoryMethod.invoke(trackingController, emptyJson);
			assertTrue(include3);

		} catch (Exception e) {
			fail("ShouldIncludeHistory test failed: " + e.getMessage());
		}
		System.out.println("====== ENDING PULLTRACKING SHOULD INCLUDE HISTORY TEST ======");
	}
}
