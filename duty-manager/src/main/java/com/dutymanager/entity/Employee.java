package com.dutymanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries ({
	@NamedQuery (name="q1", query = "from Employee e where e.name = :name"),
})
@NamedNativeQueries ({

	@NamedNativeQuery(name="q1", query = "SELECT * FROM EMPLOYEE WHERE NAME=:name")
})

@Entity
@Table (name="EMPLOYEE")
public class Employee implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name="EMP_CODE")
	private Long employeeCode;
	
	@Column (name="EMP_NAME")
	private String employeeName;

	public Long getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(Long employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}

