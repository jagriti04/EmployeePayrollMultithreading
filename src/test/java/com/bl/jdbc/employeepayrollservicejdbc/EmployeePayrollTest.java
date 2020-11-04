package com.bl.jdbc.employeepayrollservicejdbc;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollTest {

	@Test
	public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmpCount() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Assert.assertEquals(8, empPayrollData.size());
	}

	@Test
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

//	UC6
	@Test
	public void givenEmployeeDataInDBByGender_shouldMatchAvgSalary() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		String gender = "F";
		double avgSalary = empPayrollService.getEmpDataAvgByGender(gender);
		System.out.println("avg -" + avgSalary);
		boolean result = (2333333.0 == avgSalary);
		Assert.assertTrue(result);
	}

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
	public void given6Employee_WhenAddedDatabase_ShouldMatchEntries() throws EmployeePayrollException {
		EmployeePayrollData[] arrayEmps = { new EmployeePayrollData(11, "Sam", "F", 100000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Harry",  "F", 600000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Roohi", "F", 700000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Nandini", "F", 800000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Ram", "M", 900000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Shyam", "M", 500000.0, LocalDate.now()),

		};
		EmployeePayrollService employeePayroll = new EmployeePayrollService();
		employeePayroll.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Instant start = Instant.now();
		employeePayroll.addEmployeeToPayroll(Arrays.asList(arrayEmps));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		Assert.assertEquals(7, employeePayroll.countEntries());
	}
}
