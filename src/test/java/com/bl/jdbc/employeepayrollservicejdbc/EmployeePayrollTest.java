package com.bl.jdbc.employeepayrollservicejdbc;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollTest {

	@Test
	public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmpCount() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		Assert.assertEquals(9, empPayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_whenUpdated_shouldSyncWithDB() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> empPayrollData = empPayrollService
				.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		empPayrollService.updateEmployeeSalary("Terisa", 3000000.0);
		boolean result = empPayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
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
		Assert.assertEquals(1, empPayrollDataList.size());
	}

//	UC6
	@Test
	public void givenEmployeeDataInDBByGender_shouldMatchAvgSalary() {
		EmployeePayrollService empPayrollService = new EmployeePayrollService();
		String gender = "F";
		double avgSalary = empPayrollService.getEmpDataAvgByGender(gender);
		System.out.println("avg -" + avgSalary);
		boolean result = (3600000.0 == avgSalary);
		Assert.assertTrue(result);
	}

//	UC 9-11 -- implement ERD
	@Test
	public void givenNewEmployee_whenAdded_shouldSyncWithDB() throws EmployeePayrollException {
		EmployeePayrollService empService = new EmployeePayrollService();
		empService.readEmployeePayrollDataDB(EmployeePayrollService.IOService.DB_IO);
		empService.addEmployeeToPayroll("Mark", "M", 4000000.0, LocalDate.now(), "Reliance", "IT");
		boolean result = empService.checkEmployeePayrollInSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
	
	// multi-threading
	
	
}
