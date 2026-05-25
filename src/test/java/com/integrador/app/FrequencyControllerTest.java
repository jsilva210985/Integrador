package com.integrador.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.integrador.restcontroller.estafetav2.FrequencyController;
import org.json.JSONObject;
import org.json.JSONArray;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegadorServiceApplication.class)
public class FrequencyControllerTest {

	@Autowired
	private FrequencyController frequencyController;

	@Test
	public void testFrequencyEndpointValidation() {
		System.out.println("====== STARTING FREQUENCY ENDPOINT VALIDATION TEST ======");
		try {
			// Sending invalid request to verify validation checks
			String content = "{\"cliente\":\"invalid\",\"password\":\"invalid\",\"token\":\"invalid\"}";
			ResponseEntity<String> response = frequencyController.frequency(null, content);
			System.out.println("Response Status: " + response.getStatusCode());
			System.out.println("Response Body: " + response.getBody());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		System.out.println("====== ENDING FREQUENCY ENDPOINT VALIDATION TEST ======");
	}

	@Test
	public void testFrequencyResponseTransformation() {
		System.out.println("====== STARTING FREQUENCY TRANSFORMATION TEST ======");
		try {
			String v2Response = "{\n" +
					"    \"frequencies\": [\n" +
					"        {\n" +
					"            \"origin\": [\n" +
					"                {\n" +
					"                    \"address\": {\n" +
					"                        \"postalCode\": \"44158\",\n" +
					"                        \"settlementName\": [\n" +
					"                            {\n" +
					"                                \"name\": \"DEITZ\"\n" +
					"                            }\n" +
					"                        ],\n" +
					"                        \"townshipName\": \"GUADALAJARA\",\n" +
					"                        \"stateCode\": \"Jalisco\"\n" +
					"                    },\n" +
					"                    \"warehouseCode\": \"GDC\",\n" +
					"                    \"result\": {\n" +
					"                        \"code\": 0,\n" +
					"                        \"description\": \"OK\"\n" +
					"                    }\n" +
					"                }\n" +
					"            ],\n" +
					"            \"destinations\": [\n" +
					"                {\n" +
					"                    \"result\": {\n" +
					"                        \"code\": 0,\n" +
					"                        \"description\": \"OK\"\n" +
					"                    },\n" +
					"                    \"address\": {\n" +
					"                        \"postalCode\": \"45640\",\n" +
					"                        \"settlementName\": [\n" +
					"                            {\n" +
					"                                \"name\": \"ALTIPLANO\"\n" +
					"                            },\n" +
					"                            {\n" +
					"                                \"name\": \"BANUS\"\n" +
					"                            }\n" +
					"                        ],\n" +
					"                        \"townshipName\": \"TLAJOMULCO DE ZUNIGA\",\n" +
					"                        \"stateCode\": \"Jalisco\"\n" +
					"                    },\n" +
					"                    \"service\": [\n" +
					"                        {\n" +
					"                            \"name\": \"Dia Sig.\",\n" +
					"                            \"estimatedDeliveryDate\": \"2026-05-26\"\n" +
					"                        },\n" +
					"                        {\n" +
					"                            \"name\": \"Metropoli\",\n" +
					"                            \"estimatedDeliveryDate\": \"2026-05-26\"\n" +
					"                        }\n" +
					"                    ],\n" +
					"                    \"warehouseCode\": \"GDC\",\n" +
					"                    \"zoneCode\": \"1\",\n" +
					"                    \"periodicityName\": \"Diaria\",\n" +
					"                    \"isMonday\": true,\n" +
					"                    \"isTuesday\": true,\n" +
					"                    \"isWednesday\": true,\n" +
					"                    \"isThursday\": true,\n" +
					"                    \"isFriday\": true,\n" +
					"                    \"isSaturday\": true,\n" +
					"                    \"isSunday\": false,\n" +
					"                    \"isReexpedition\": false,\n" +
					"                    \"isOcurre\": false,\n" +
					"                    \"restriction\": false,\n" +
					"                    \"restrictionDescription\": \"\"\n" +
					"                }\n" +
					"            ]\n" +
					"        }\n" +
					"    ]\n" +
					"}";

			String requestJsonStr = "{\n" +
					"    \"cliente\": \"test_user\",\n" +
					"    \"password\": \"test_pass\",\n" +
					"    \"token\": \"test_token\",\n" +
					"    \"esFrecuencia\": \"true\",\n" +
					"    \"esLista\": \"true\",\n" +
					"    \"tipoEnvio\": {\n" +
					"        \"EsPaquete\": \"false\",\n" +
					"        \"Peso\": \"10.5\",\n" +
					"        \"Alto\": \"0\",\n" +
					"        \"Largo\": \"0\",\n" +
					"        \"Ancho\": \"0\"\n" +
					"    },\n" +
					"    \"datosOrigen\": {\n" +
					"        \"string\": [\n" +
					"            \"44158\"\n" +
					"        ]\n" +
					"    },\n" +
					"    \"datosDestino\": {\n" +
					"        \"string\": [\n" +
					"            \"45640\"\n" +
					"        ]\n" +
					"    }\n" +
					"}";

			JSONObject requestJson = new JSONObject(requestJsonStr);
			String v1ResponseStr = frequencyController.transformV2ResponseToV1(v2Response, requestJson);

			System.out.println("Transformed response:\n" + v1ResponseStr);

			JSONObject v1Json = new JSONObject(v1ResponseStr);

			// Assert basic fields
			assertEquals(44158, v1Json.getInt("CodigoPosOri"));
			assertEquals("Si", v1Json.getString("ExistenteSiglaOri"));
			assertEquals("Si", v1Json.getString("ExistenteSiglaDes"));
			assertEquals("000", v1Json.getString("Error"));
			assertEquals("", v1Json.getString("MensajeError"));

			// Assert Colonias
			JSONArray colonias = v1Json.getJSONArray("Colonias");
			assertEquals(2, colonias.length());
			assertEquals("ALTIPLANO", colonias.getJSONObject(0).getString("nombre"));
			assertEquals("BANUS", colonias.getJSONObject(1).getString("nombre"));

			// Assert Destino
			JSONObject destino = v1Json.getJSONObject("Destino");
			assertEquals("TLAJOMULCO DE ZUNIGA", destino.getString("Municipio"));
			assertEquals(45640, destino.getInt("CpDestino"));
			assertEquals("Guadalajara", destino.getString("Plaza1")); // mapped from GDC
			assertEquals("Jalisco", destino.getString("Estado"));

			// Assert Origen
			JSONObject origen = v1Json.getJSONObject("Origen");
			assertEquals("GUADALAJARA", origen.getString("MunicipioOri"));
			assertEquals(44158, origen.getInt("CodigoPosOri"));
			assertEquals("GUADALAJARA CENTRO", origen.getString("PlazaOri")); // mapped from GDC
			assertEquals("Jalisco", origen.getString("EstadoOri"));

			// Assert TipoEnvio (numbers parsed safely)
			JSONObject tipoEnvio = v1Json.getJSONObject("TipoEnvio");
			assertFalse(tipoEnvio.getBoolean("EsPaquete"));
			assertEquals(10.5, tipoEnvio.getDouble("Peso"), 0.001);
			assertEquals(0, tipoEnvio.getInt("Alto"));

			// Assert DiasEntrega
			JSONObject diasEntrega = v1Json.getJSONObject("DiasEntrega");
			assertEquals("X", diasEntrega.getString("Lunes"));
			assertEquals("X", diasEntrega.getString("Sabado"));
			assertEquals("", diasEntrega.getString("Domingo"));

			// Assert CostoReexpedicion & ModalidadEntrega
			assertEquals("No", v1Json.getString("CostoReexpedicion"));
			JSONObject modalidad = v1Json.getJSONObject("ModalidadEntrega");
			assertEquals("Diaria", modalidad.getString("Frecuencia"));
			assertEquals("No", modalidad.getString("OcurreForzoso"));

			// Assert TipoServicio
			JSONObject tipoServicioObj = v1Json.getJSONObject("TipoServicio");
			JSONArray servicios = tipoServicioObj.getJSONArray("TipoServicio");
			assertEquals(2, servicios.length());
			assertEquals("Dia Sig.", servicios.getJSONObject(0).getString("DescripcionServicio"));
			assertEquals("Metropoli", servicios.getJSONObject(1).getString("DescripcionServicio"));

		} catch (Exception e) {
			fail("Transformation threw an exception: " + e.getMessage());
		}
		System.out.println("====== ENDING FREQUENCY TRANSFORMATION TEST ======");
	}
}
