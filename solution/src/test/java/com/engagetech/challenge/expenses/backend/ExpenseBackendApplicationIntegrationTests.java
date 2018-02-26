package com.engagetech.challenge.expenses.backend;

import com.engagetech.challenge.expenses.backend.model.ModelTestFactory;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseForm;
import com.engagetech.challenge.expenses.backend.model.view.ExpenseListItem;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/ExpenseBackendApplicationIntegrationTests.sql")
public class ExpenseBackendApplicationIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void saveExpense() {
		ResponseEntity<ExpenseListItem[]> listOfExpensesResponse = restTemplate.getForEntity("/app/expenses", ExpenseListItem[].class);
		assertEquals(HttpStatus.OK, listOfExpensesResponse.getStatusCode());
		assertEquals(0, listOfExpensesResponse.getBody().length);

		ExpenseForm expenseForm = ModelTestFactory.newExpenseForm("2018-02-09", "14.65", "Test reason");
		ResponseEntity<Void> saveExpenseResponse = restTemplate.postForEntity("/app/expenses", expenseForm, Void.class);
		assertEquals(HttpStatus.OK, saveExpenseResponse.getStatusCode());

		ExpenseListItem expenseListItem = ModelTestFactory.newExpenseListItem("2018-02-09", 14.65, 2.44, "Test reason");
		listOfExpensesResponse = restTemplate.getForEntity("/app/expenses", ExpenseListItem[].class);
		assertEquals(HttpStatus.OK, listOfExpensesResponse.getStatusCode());
		assertEquals(1, listOfExpensesResponse.getBody().length);
		assertEquals(expenseListItem, listOfExpensesResponse.getBody()[0]);
	}

	@Test
	public void saveExpenseWithNoDateValidationError() {
		ResponseEntity<ExpenseListItem[]> listOfExpensesResponse = restTemplate.getForEntity("/app/expenses", ExpenseListItem[].class);
		assertEquals(HttpStatus.OK, listOfExpensesResponse.getStatusCode());
		assertEquals(0, listOfExpensesResponse.getBody().length);

		ExpenseForm expenseForm = ModelTestFactory.newExpenseForm("14.65", "Test reason");
		ResponseEntity<Map> saveExpenseResponse = restTemplate.postForEntity("/app/expenses", expenseForm, Map.class);
		assertEquals(HttpStatus.BAD_REQUEST, saveExpenseResponse.getStatusCode());

		DocumentContext jsonPath = JsonPath.parse(saveExpenseResponse.getBody());
		assertEquals(Integer.valueOf(400), jsonPath.read("$.status"));
		assertEquals("Bad Request", jsonPath.read("$.error"));
		assertEquals("/app/expenses", jsonPath.read("$.path"));
		assertEquals(Integer.valueOf(1), jsonPath.read("$.errors.length()"));
		assertEquals("expenseForm", jsonPath.read("$.errors[0].objectName"));
		assertEquals("date", jsonPath.read("$.errors[0].field"));
		assertEquals("NotNull", jsonPath.read("$.errors[0].code"));
		assertEquals("Date must be not null", jsonPath.read("$.errors[0].defaultMessage"));

		listOfExpensesResponse = restTemplate.getForEntity("/app/expenses", ExpenseListItem[].class);
		assertEquals(HttpStatus.OK, listOfExpensesResponse.getStatusCode());
		assertEquals(0, listOfExpensesResponse.getBody().length);
	}

}
