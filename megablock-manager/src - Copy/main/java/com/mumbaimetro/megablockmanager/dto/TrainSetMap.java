package com.mumbaimetro.megablockmanager.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "TRAIN_SET")
public class TrainSetMap implements Serializable {

	private static final long serialVersionUID = 4426242430388427094L;
	
	@Column (name="TRAIN_NUMBER")
	@Id
	private String trainNumber;
	
	@Column (name = "SET_NUMBER")
	private String setNumber;
	
	@Column (name = "NUMBER_OF_TRAINS_IN_SET")
	private Integer numberOfTrainsInSet;
	
	@Column (name = "NUMBER_OF_CANCELLED_TRAINS_IN_SET")
	private Integer numberOfCancelledTrainsInSet;
	
	@Column (name = "IS_CANCELLED")
	private Boolean isCancelled = Boolean.FALSE;
	
	@Column (name = "IS_NOS")
	private Boolean isNos = Boolean.FALSE;
	
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
	public Integer getNumberOfTrainsInSet() {
		return numberOfTrainsInSet;
	}
	public void setNumberOfTrainsInSet(Integer numberOfTrainsInSet) {
		this.numberOfTrainsInSet = numberOfTrainsInSet;
	}
	public Integer getNumberOfCancelledTrainsInSet() {
		return numberOfCancelledTrainsInSet;
	}
	public void setNumberOfCancelledTrainsInSet(Integer numberOfCancelledTrainsInSet) {
		this.numberOfCancelledTrainsInSet = numberOfCancelledTrainsInSet;
	}
	public Boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	public Boolean isNos() {
		return isNos;
	}
	public void setNos(Boolean isNos) {
		this.isNos = isNos;
	}
	
}
