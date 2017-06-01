package com.mumbaimetro.megablockmanager.dto;

import java.io.Serializable;

public class Train implements Serializable {
	private static final long serialVersionUID = -7033701694935313931L;
	private String trainNumber;
	private boolean isNos;
	
	public Train(){
		
	}
	
	public Train (String trainNumber, boolean isNos) {
		this.trainNumber = trainNumber;
		this.isNos = isNos;
	}
	
	public String getTrainNumber() {
		return trainNumber;
	}
	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}
	public boolean isNos() {
		return isNos;
	}
	public void setNos(boolean isNos) {
		this.isNos = isNos;
	}
}
