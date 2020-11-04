package com.bl.jdbc.employeepayrollservicejdbc;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollTest {

	@Test
	public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmpCount() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Assert.assertEquals(14, empPayrollData.size());
	}

//
//	@Test
	public void givenNewSalaryForEmployee_whenUpdated_shouldSyncWithDB() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		empPayrollService.updateEmployeeSalary("Bella", 2000000.0);

		boolean result = empPayrollService.checkEmployeePayrollInSyncWithDB("Bella");
		Assert.assertTrue(result);
	}

//	UC5
	@Test
	public void givenDateRange_shouldMatchWithEmployeeJoinedInDateRange() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		String start = "2019-01-01";
		String end = "2020-01-05";
		List<EmployeePayrollData> empPayrollDataList = empPayrollService.getEmpByDateRange(start, end);
		Assert.assertEquals(0, empPayrollDataList.size());
	}

////	UC6
	@Test
	public void givenEmployeeDataInDBByGender_shouldMatchAvgSalary() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		String gender = "F";
		double avgSalary = empPayrollService.getEmpDataAvgByGender(gender);
		System.out.println("avg -" + avgSalary);
		boolean result = (1028571.0 == avgSalary);
		Assert.assertTrue(result);
	}

//
//	UC 9-11 -- implement ERD
	@Test
	public void givenNewEmployee_whenAdded_shouldSyncWithDB() throws EmployeePayrollException {
		EmployeePayrollService empService = new EmployeePayrollService();
		empService.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		empService.addEmployeeToPayroll("Bill", "M", 4000000.0, LocalDate.now(), "Reliance", "IT");
		boolean result = empService.checkEmployeePayrollInSyncWithDB("Bill");
		Assert.assertTrue(result);
	}

	// multi-threading

	@Test
	public void givenSixEmployee_whenAddedDatabase_shouldMatchEntries() throws EmployeePayrollException {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(11, "Sam", "F", 100000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Harry", "M", 600000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Roohi", "F", 700000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Nandini", "F", 800000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Ram", "M", 900000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Shyam", "M", 500000.0, LocalDate.now()),

		};
		EmployeePayrollService employeePayroll = new EmployeePayrollService();
		employeePayroll.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Instant start = Instant.now();
		employeePayroll.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayroll.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(14, employeePayroll.countEntries());
	}

	@Test
	public void givenNewSalaryForMultipleEmployees_whenUpdated_shouldSyncWithDB() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Map<String, Double> nameToSalaryMap = new HashMap<>();
		nameToSalaryMap.put("Harry", 400000.0);
		nameToSalaryMap.put("Shyam", 900000.0);
		nameToSalaryMap.put("Ram", 500000.0);
		Instant threadStart = Instant.now();
		empPayrollService.updateEmployeeSalary(nameToSalaryMap);
		boolean result = empPayrollService.checkEmployeePayrollInSyncWithDB("Harry")
				&& empPayrollService.checkEmployeePayrollInSyncWithDB("Shyam")
				&& empPayrollService.checkEmployeePayrollInSyncWithDB("Ram");
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: " + Duration.between(threadStart, threadEnd));
		Assert.assertTrue(result);
	}
}
