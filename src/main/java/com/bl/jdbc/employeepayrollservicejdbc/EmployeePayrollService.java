package com.bl.jdbc.employeepayrollservicejdbc;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private List<EmployeePayrollData> empPayrollList;

	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	// Parameterized constructor
	public EmployeePayrollService(List<EmployeePayrollData> empPayrollList) {
		this.empPayrollList = empPayrollList;
	}

	public List<EmployeePayrollData> readEmployeePayrollDataDB(IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			this.empPayrollList = employeePayrollDBService.readData();
		}
		return this.empPayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData empPayrollData = this.getEmployeePayrollData(name);
		if (empPayrollData != null)
			empPayrollData.setEmpSalary(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.empPayrollList.stream().filter(empPayrollDataItem -> empPayrollDataItem.getEmpName().equals(name))
				.findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> empPayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return empPayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	public List<EmployeePayrollData> getEmpByDateRange(String startDate, String endDate) {
		this.empPayrollList = employeePayrollDBService.getEmployeePayrollDataWithStartDateInGivenRange(startDate,
				endDate);
		return this.empPayrollList;
	}

	public double getEmpDataAvgByGender(String gender) {
		double avgSalary = employeePayrollDBService.getEmployeePayrollDataAvgSalary(gender);
		return avgSalary;
	}

	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate, String companyName,
			String departmentName) throws EmployeePayrollException {
		empPayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, gender, salary, startDate, companyName,
				departmentName));

	}

}
