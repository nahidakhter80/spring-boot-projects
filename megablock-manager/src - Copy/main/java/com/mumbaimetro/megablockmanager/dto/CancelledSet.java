package com.mumbaimetro.megablockmanager.dto;

import java.io.Serializable;
import java.util.List;

public class CancelledSet implements Serializable, Comparable<CancelledSet> {

	private static final long serialVersionUID = 479541518484143186L;

	private String setNumber;
	private List<Train> cancelledTrains;
	private List<Train> nonCancelledTrains;
	private boolean isVacant;
	
	public String getSetNumber() {
		return setNumber;
	}
	public void setSetNumber(String setNumber) {
		this.setNumber = setNumber;
	}
	public List<Train> getCancelledTrains() {
		return cancelledTrains;
	}
	public void setCancelledTrains(List<Train> cancelledTrains) {
		this.cancelledTrains = cancelledTrains;
	}
	public List<Train> getNonCancelledTrains() {
		return nonCancelledTrains;
	}
	public void setNonCancelledTrains(List<Train> nonCancelledTrains) {
		this.nonCancelledTrains = nonCancelledTrains;
	}
	public boolean isVacant() {
		return isVacant;
	}
	public void setVacant(boolean isVacant) {
		this.isVacant = isVacant;
	}
	@Override
	public int compareTo(CancelledSet o) {
		return new Integer(this.getSetNumber()).compareTo(new Integer(o.getSetNumber()));
	}
}
