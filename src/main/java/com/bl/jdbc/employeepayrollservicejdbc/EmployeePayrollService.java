package com.bl.jdbc.employeepayrollservicejdbc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) {
		empPayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, gender, salary, startDate));
	}

	public void addEmployeeToPayroll(List<EmployeePayrollData> empList) {
		empList.forEach(empPayrollData -> {
			System.out.println("Employee being added: " + empPayrollData.getEmpName());
			this.addEmployeeToPayroll(empPayrollData.getEmpName(), empPayrollData.getGender(),
					empPayrollData.getEmpSalary(), LocalDate.now());
			System.out.println("Employee Added: " + empPayrollData.getEmpName());
		});
		System.out.println(this.empPayrollList);
	}

	public void addEmployeesToPayrollWithThreads(List<EmployeePayrollData> empDataList) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		empDataList.forEach(empPayrollData -> {
			Runnable task = () -> {
				employeeAdditionStatus.put(empPayrollData.hashCode(), false);
				System.out.println("Employee being added: " + Thread.currentThread().getName());
				this.addEmployeeToPayroll(empPayrollData.getEmpName(), empPayrollData.getGender(),
						empPayrollData.getEmpSalary(), LocalDate.now());
				employeeAdditionStatus.put(empPayrollData.hashCode(), true);
				System.out.println("Employee added: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, empPayrollData.getEmpName());
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public long countEntries() {
		return empPayrollList.size();
	}

	public void updateEmployeeSalary(Map<String, Double> nameToSalaryMap) {
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		nameToSalaryMap.forEach((name, salary) ->{
			Runnable task = () -> {
				employeeAdditionStatus.put(employeeAdditionStatus.hashCode(), false);
				System.out.println("Salary being updated: " + Thread.currentThread().getName());
				this.updateEmployeeSalary(name, salary);
				employeeAdditionStatus.put(employeeAdditionStatus.hashCode(), true);
				System.out.println("Salary updated: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, name);
			thread.start();
		});
		while (employeeAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
