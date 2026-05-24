package com.integrador.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.integrador.restcontroller.estafetav2.TokenController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegadorServiceApplication.class)
public class TokenControllerTest {

	@Autowired
	private com.integrador.restcontroller.estafetav2.TokenController tokenController;

	@Test
	public void testTokenEndpoint() {
		System.out.println("====== STARTING TOKEN CONTROLLER TEST ======");
		try {
			ResponseEntity<String> response = tokenController.token();
			System.out.println("Response Status: " + response.getStatusCode());
			System.out.println("Response Body: " + response.getBody());
		} catch (Exception e) {
			System.out.println("Exception caught during test execution: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("====== ENDING TOKEN CONTROLLER TEST ======");
	}
}
