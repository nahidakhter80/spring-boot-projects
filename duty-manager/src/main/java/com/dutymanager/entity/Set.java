package com.dutymanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="SET")
public class Set implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name="SET_NUMBER")
	private Long setNumber;	
}
