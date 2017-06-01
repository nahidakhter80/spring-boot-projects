package com.schedule.generator.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SCHEDULED_TRAIN")
public class ScheduledTrain implements Comparable<ScheduledTrain>, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column (name="TRAIN_NUMBER")
	private String 	trainNo;
	
	@Id
	@Column (name="ORIGIN")
	private String 	origin;
	
	@Id
	@Column (name="DESTINATION")
	private String 	destination;
	
	@Column (name="ARRIVAL_TIME")
	private Date 	arrivalTime;
	
	@Column (name="DEPARTURE_TIME")
	private Date 	departureTime;
	
	@Column (name="MODE")
	private String	mode;
	
	@Column (name="CARS")
	private String 	numCars;
	
	@Column (name="DISTANCE")
	private Float 	distance;
	
	@Column (name="PRT_REMARK")
	private String 	prtRemark;
	
	@Column (name="RO_REMARK")
	private String 	roRemark;
	
	@Column (name="TAP_REMARK")
	private String 	tapRemark;
	
	@Column (name="ETY_REMARK")
	private String 	etyRemark;
	
	@Column (name="NOS_REMARK")
	private String 	nosRemark;
	
	@Column (name="NEXT_TRAIN_NUMBER")
	private String 	nextTrainNo;
	
	@Column (name="SET_NUMBER")
	private String 	setNo;
	
	@Column (name="MODE_CODE")
	private String 	modeCode;
	
	public ScheduledTrain() {
	}
	
	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigen(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getNumCars() {
		return numCars;
	}

	public void setNumCars(String numCars) {
		this.numCars = numCars;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public String getPRTRemark() {
		return prtRemark;
	}
	
	public void setPRTRemark(String prtRemark) {
		this.prtRemark = prtRemark;
	}
	
	public String getRORemark() {
		return roRemark;
	}

	public void setRORemark(String roRemark) {
		this.roRemark = roRemark;
	}
	
	public String getTAPRemark() {
		return tapRemark;
	}

	public void setTAPRemark(String tapRemark) {
		this.tapRemark = tapRemark;
	}
	
	public String getETYRemark() {
		return etyRemark;
	}

	public void setETYRemark(String mtRemark) {
		this.etyRemark = mtRemark;
	}
	
	public String getNOSRemark() {
		return nosRemark;
	}

	public void setNOSRemark(String nosRemark) {
		this.nosRemark = nosRemark;
	}
		
	public String getNextTrainNo() {
		return nextTrainNo;
	}

	public void setNextTrainNo(String nextTrainNo) {
		this.nextTrainNo = nextTrainNo;
	}
	
	public String getSetNo() {
		return setNo;
	}

	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}
	public String getModeCode() {
		return modeCode;
	}

	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}	
	
	/*public String toString(){
		String str = "";
		try {
			str = "trainNo: " + trainNo +
					"\norigen: " + origin +
					"\ndestination: " + destination +
					"\ndepartureTime: " + new SimpleDateFormat("HH:mm").format(departureTime) + 
					"\narrivalTime: " + new SimpleDateFormat("HH:mm").format(arrivalTime) + 
					"\nmode: " + mode +
					"\nmodeCode: " + modeCode +
					"\nnumCars: " + numCars + 
					"\nsetNo: " + setNo + 
					"\ndistance: " + distance + 
					"\n\n\n";
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
		return str;
	}*/

	@Override
	public int compareTo(ScheduledTrain train) {		
		Date d1 = this.getDepartureTime();
		Date d2 = train.getDepartureTime();		
		return d1.compareTo(d2);
	}
}
