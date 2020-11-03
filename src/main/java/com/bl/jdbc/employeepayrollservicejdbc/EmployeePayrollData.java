package com.bl.jdbc.employeepayrollservicejdbc;

import java.time.LocalDate;
import java.util.Date;

public class EmployeePayrollData {
	private int empId;
	private String empName;
	private double empSalary;
	private LocalDate startDate;
	public String gender;

	public EmployeePayrollData(int id, String name, double salary) {
		this.empId = id;
		this.empName = name;
		this.empSalary = salary;
	}

	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate2) {
		this(id, name, salary);
		this.startDate = startDate;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate) {
		this(id, name, salary, startDate);
		this.gender = gender;
	}

	public String toString() {
		return "id=" + empId + ", name=" + empName + ", salary=" + empSalary;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public double getEmpSalary() {
		return empSalary;
	}

	public void setEmpSalary(double empSalary) {
		this.empSalary = empSalary;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmployeePayrollData that = (EmployeePayrollData) o;
		return empId == that.empId && Double.compare(that.empSalary, empSalary) == 0 && empName.equals(that.empName);
	}
}
