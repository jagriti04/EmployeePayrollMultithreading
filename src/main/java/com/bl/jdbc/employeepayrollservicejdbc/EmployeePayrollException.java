package com.bl.jdbc.employeepayrollservicejdbc;

public class EmployeePayrollException extends Exception {
	public enum ExceptionType {
		QUERY_EXECUTION_ERROR,
		CONNECTION_CREATE_ERROR, CONNECTION_CLOSING_ERROR
    }

    public ExceptionType type;

    public EmployeePayrollException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public EmployeePayrollException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
