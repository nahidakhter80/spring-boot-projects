package com.mumbaimetro.megablockmanager.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

public class TrainSetMapId implements Serializable{

	private static final long serialVersionUID = 2332652774157782498L;

	@Column (name="TRAIN_NUMBER")
	@Id
	private String trainNumber;
	
	@Column (name = "SET_NUMBER")
	@Id
	private String setNumber;

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(String setNumber) {
		this.setNumber = setNumber;
	}
	
}
