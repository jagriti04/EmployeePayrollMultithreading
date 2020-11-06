package com.bl.jdbc.employeepayrollservicejdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestTest {

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;

	}

	private EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		EmployeePayrollData[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmps;
	}

	public Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}

	// uc4
	@Test
	public void givenEmployeeDataInJSONServer_whenRetrieved_shouldReturnCorrectEntryCount() {
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(new ArrayList<>(Arrays.asList(arrayOfEmps)));
		long entries = employeePayrollService.countEntries();
		Assert.assertEquals(4, entries);
	}

	// UC1
	@Test
	public void givenNewEmployee_whenAdded_shouldMatch201ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(new ArrayList<>(Arrays.asList(arrayOfEmps)));
		EmployeePayrollData empData = new EmployeePayrollData(0, "Mark2", 3000000.0);
		Response response = addEmployeeToJsonServer(empData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);

		empData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.addEmployeeToPayroll(empData, EmployeePayrollService.IOService.REST_IO);

		long entries = employeePayrollService.countEntries();
		Assert.assertEquals(5, entries);
	}

	// UC2
	@Test
	public void givenListOfNewEmployee_whenAdded_shouldMatch201ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(new ArrayList<>(Arrays.asList(arrayOfEmps)));

		EmployeePayrollData[] arrayOfEmpPayrolls = { new EmployeePayrollData(0, "Sam", 100000.0),
				new EmployeePayrollData(0, "Harry", 600000.0), new EmployeePayrollData(0, "Roohi", 700000.0) };

		for (EmployeePayrollData employeePayrollData : arrayOfEmpPayrolls) {
			Response response = addEmployeeToJsonServer(employeePayrollData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);

			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
			employeePayrollService.addEmployeeToPayroll(employeePayrollData, EmployeePayrollService.IOService.REST_IO);
		}
		long entries = employeePayrollService.countEntries();
		Assert.assertEquals(7, entries);
	}

	// UC3
	@Test
	public void givenNewSalaryForEmployee_whenUpdated_shouldMatch200Response() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(new ArrayList<>(Arrays.asList(arrayOfEmps)));

		employeePayrollService.updateEmployeeSalary("Anil", 300000.0, EmployeePayrollService.IOService.REST_IO);
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		Response response = request.put("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

	// UC5
	@Test
	public void givenEmployeeToDelete_whenDeleted_shouldMatch200ResponseAndCount() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmps = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(new ArrayList<>(Arrays.asList(arrayOfEmps)));

		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Jeff");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		
		employeePayrollService.deleteEmployeePayroll(employeePayrollData.getEmpName(), EmployeePayrollService.IOService.REST_IO);
		long entries = employeePayrollService.countEntries();
		Assert.assertEquals(4, entries);
	}
}
