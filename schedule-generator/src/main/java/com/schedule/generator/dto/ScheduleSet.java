package com.schedule.generator.dto;

import java.io.Serializable;
import java.util.ArrayList;

import com.schedule.generator.entity.ScheduledTrain;

public class ScheduleSet implements Serializable{	
	private static final long serialVersionUID = 5073036006136797969L;
	
	private String setNo = "";
	private String onDutyTime = "";
	private String offDutyTime = "";
	private String onDutyNOSRemark = "";
	private String offDutyNOSRemark = "";	
	private String origen = "";
	private String destination = "";
	private String distance = "0.00";
	private String duration = "";
	private String restHours = "00:00";
	private ArrayList<ScheduledTrain> trainList = new ArrayList<ScheduledTrain>();
	private String sunDayOnDutyVals[] = null;	
	//private ScheduledTrain sunDayOffDutyTrain = null;

	public String str = "";

	public ScheduleSet() {
	}
	
	public ScheduleSet(String s) {
		str = "empty";
	}
	
	public String getSetNo() {
		return setNo;
	}
	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}
	
	public String getOnDutyTime() {
		return onDutyTime;
	}
	public void setOnDutyTime(String onDutyTime) {
		this.onDutyTime = onDutyTime;
	}
	public String getOffDutyTime() {
		return offDutyTime;
	}
	public void setOffDutyTime(String offDutyTime) {
		this.offDutyTime = offDutyTime;
	}
	public String getOnDutyNOSRemark() {
		return onDutyNOSRemark;
	}
	public void setOnDutyNOSRemark(String onDutyNOSRemark) {
		this.onDutyNOSRemark = onDutyNOSRemark;
	}
	public String getOffDutyNOSRemark() {
		return offDutyNOSRemark;
	}
	public void setOffDutyNOSRemark(String offDutyNOSRemark) {
		this.offDutyNOSRemark = offDutyNOSRemark;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRestHours() {
		return restHours;
	}
	public void setRestHours(String restHours) {
		this.restHours = restHours;
	}
	
	public ArrayList<ScheduledTrain> getTrainList() {
		return trainList;
	}

	public void setTrainList(ArrayList<ScheduledTrain> tainList) {
		this.trainList = tainList;
	}
	
	/*public ScheduledTrain getSunDayOnDutyTrain() {
		return sunDayOnDutyTrain;
	}

	public void setSunDayOnDutyTrain(ScheduledTrain sunDayOnDutyTrain) {
		this.sunDayOnDutyTrain = sunDayOnDutyTrain;
	}

	public ScheduledTrain getSunDayOffDutyTrain() {
		return sunDayOffDutyTrain;
	}

	public void setSunDayOffDutyTrain(ScheduledTrain sunDayOffDutyTrain) {
		this.sunDayOffDutyTrain = sunDayOffDutyTrain;
	}*/
	
	public String[] getSunDayOnDutyVals() {
		return sunDayOnDutyVals;
	}

	public void setSunDayOnDutyVals(String[] sunDayOnDutyVals) {
		this.sunDayOnDutyVals = sunDayOnDutyVals;
	}
	
	public String toString(){
		if(!str.equalsIgnoreCase("empty")) {
			try {
				str = "setNo: " + setNo + 
						"\norigen: " + origen +
						"\ndestination: " + destination +
						"\ndistance: " + distance + 
						"\nrestHours: " + restHours + 						
						"\nonDutyTime: " + onDutyTime +
						"\noffDutyTime: " + offDutyTime +
						"\nduration: " + duration + 
						"\nnumTrains: " + trainList.size() ;
				for(ScheduledTrain st : trainList) {
					str = str + "\n\t" + st.getDepartureTime() + " ----- " +st.getArrivalTime();
				}
				str = str + "\n\n\n";
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}		
		return str;
	}
}
