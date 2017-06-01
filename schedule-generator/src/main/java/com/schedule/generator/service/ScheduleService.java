package com.schedule.generator.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schedule.generator.entity.ScheduledTrain;
import com.schedule.generator.dao.ScheduleDao;
import com.schedule.generator.dto.ScheduleSet;
import com.schedule.generator.util.AlphanumericComparator;
import com.schedule.generator.util.Constants;
import com.schedule.generator.util.ExcelReader;
import com.schedule.generator.util.ExcelWriter;
import com.schedule.generator.util.NumericStringComparator;
import com.schedule.generator.util.TimeUtil;

@Service
public class ScheduleService {
	
	private Boolean isNumericSet = Boolean.TRUE;
	
	@Autowired
	private ScheduleDao dao;
	
	@Transactional	
	public List<ScheduledTrain> processProfarma(InputStream is, Boolean isNumericSet) throws IOException, EncryptedDocumentException, InvalidFormatException {
		this.isNumericSet = isNumericSet;
		ExcelReader excelReader = new ExcelReader(is, isNumericSet);
		System.out.println("\n\nReading selected excel file...");
		List<ScheduledTrain> trainList = excelReader.getTrainList();
		excelReader.close();
		
		dao.insertScheduledTrains(trainList);
		
		List<ScheduledTrain> stbList = dao.getStbList();
		
		return stbList;
	}
	
	@Transactional	
	public void updateEtyRemark(List<ScheduledTrain> list) {
		System.out.println("\n\nUpdating entered ETY remarks...");
		dao.updateEtyRemark(list);	
	}
	
	@Transactional	
	public void generateExcel() throws Exception {
		System.out.println("\n\nWriting output to excel file...");
		
		List<ScheduleSet> setList = getSetList(dao.getTrainList());
		
		setDestintions(setList);
		
		setList = getSetListWithProtection(setList);
		
		setList = getSetListWithRORemark(setList);
		setList = getSetListWithTAPRemark(setList);
		setList = getSetListWithETYRemark(setList);	
		setList = getSetListWithROAfterProtection(setList);
		//setNOSRemark();
		
		List<List<ScheduleSet>> clubbedSets = getClubbedSets(setList);
				 
		ExcelWriter ew = new ExcelWriter();		
		ew.writeSets(clubbedSets);
		ew.close();
	}
	
	private List<ScheduleSet> getSetList(List<ScheduledTrain> trainList){
		DecimalFormat df = new DecimalFormat();
	    df.setMinimumFractionDigits(2);
		
		List<ScheduleSet> setList = new ArrayList<ScheduleSet>();
		
		Set <String> setNoList ;
		if (isNumericSet) {
			setNoList = new TreeSet<String>(new NumericStringComparator());
		} else {
			setNoList = new TreeSet<String>(new AlphanumericComparator(Locale.ENGLISH));
		}
		
		for(ScheduledTrain train : trainList) {
			setNoList.add(train.getSetNo());			
		}
		
		for(String setNo : setNoList) {	      	
      	   	if (isNumericSet) {
      	   		if (setNo.equals("0")) continue; 
      	   	} else {
      	   		if (((String)setNo).equals("")) continue;
      	   	}
      	   	
			ScheduleSet set = new ScheduleSet();
			set.setSetNo(setNo);
			
			ArrayList<ScheduledTrain> tList = new ArrayList<ScheduledTrain>(); 
			double distance = 0.0;
			
			for(ScheduledTrain train : trainList) {				
				if (setNo.equals(train.getSetNo())) {						
					tList.add(train);
					distance += train.getDistance();
				}
			}

			Collections.sort(tList);
			sortTrainList(tList);
			set.setTrainList(tList);
			
			String[] origenDestination = getOrigenDestination(tList);
			
			String origin = origenDestination[0];
			if(origin.equalsIgnoreCase("MDD")){
				origin = "KCS";	
			} else if(origin.equalsIgnoreCase("NSP")){
				origin = "VR C/S";	
			} else if(origin.equalsIgnoreCase("MX") || origin.equalsIgnoreCase("BCL")){
				origin = "C/S";	
			} 			
			set.setOrigen(origin);
			
			String destination = origenDestination[1];
			
			if(destination.equalsIgnoreCase("MDD")){
				destination = "KCS";				
			} else if(destination.equalsIgnoreCase("NSP")){
				destination = "VR C/S";				
			} else if(destination.equalsIgnoreCase("MX") || destination.equalsIgnoreCase("BCL")){
				destination = "C/S";				
			} 
			
			set.setDestination(destination);	
			
			String onDutyTime = calculatetOnDutyTime(tList, origin);
			set.setOnDutyTime(onDutyTime);
			
			String returnVal[] =  fetchSunDayOnDutyVals(tList);
			if (returnVal != null) {
				set.setSunDayOnDutyVals(returnVal);
			}	
			
			String offDutyTime = calculatetOffDutyTime(tList, destination);
			set.setOffDutyTime(offDutyTime);
			
			String duration = TimeUtil.getDuration(TimeUtil.stringToTime(onDutyTime), TimeUtil.stringToTime(offDutyTime));
			set.setDuration(duration);
			
			set.setDistance(df.format(distance));	
			
			setList.add(set);
		}
		
		getRestHours(setList);
		
		return setList;
	}	
	
	/*
	 * To set destinations of the trains 
	 * (only if they are last train of the set) 
	 * for which ETY option selected from UI
	 */	
	private void setDestintions(List<ScheduleSet> setList) {
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			String destination = null;
			int lastTrainIndex = trainList.size()-1;			
			ScheduledTrain train = trainList.get(lastTrainIndex);
			if(train.getPRTRemark() != null && train.getNextTrainNo().equalsIgnoreCase("STB")) {
				if (train.getPRTRemark().equalsIgnoreCase(Constants.ETY_TO_KCS)) {
					destination = "KCS";
				} else if (train.getPRTRemark().equalsIgnoreCase(Constants.ETY_TO_VRCS)) { 
					destination = "VRCS";
				} else if (train.getPRTRemark().equalsIgnoreCase(Constants.ETY_TO_SCRAP_YARD)) { 
					destination = "SCR YD";
				}
				set.setDestination(destination);
			}			
		}		
	}
	
	private List<ScheduleSet> getSetListWithProtection(List<ScheduleSet> setList){
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			
			for (ScheduledTrain train : trainList) {
				String nextTrainNo = train.getNextTrainNo();
				
				// to handle non numeric train numbers, eg. STB
				try{
					new Long(nextTrainNo);
				} catch (NumberFormatException e) {
					continue;
				}
				
				
				String remark = "";
				
				if (!nextTrainNo.equals("") && !train.getDestination().equalsIgnoreCase("CCG")) {					
					Object returnedVals[] = getOtherSetNo(nextTrainNo, setList);
					String returnedSetNo = returnedVals[0].toString().trim();
					Date returnedDepartureTime = (Date)returnedVals[1];
					String returnedDestination = "";
					Date arrivalTime = train.getArrivalTime();
					
					int difference = TimeUtil.getMinuteDiffetence(arrivalTime, returnedDepartureTime);
					
					if (!returnedSetNo.equalsIgnoreCase(set.getSetNo().toString().trim())) {
						if(train.getTrainNo().equalsIgnoreCase(nextTrainNo)) {
							returnedDestination = returnedVals[2].toString().trim();
							remark = "PRT SET NO. " + returnedSetNo + " (" + train.getOrigin() + "-" + returnedDestination + " LOCAL)";
						} else {
							/*if (difference > 30) {								
								remark = "ETY TO C/S";
							} else {
								remark = "PRT T.NO. " + nextTrainNo + " OF SET NO. " + returnedSetNo;
							}*/
							
							if (difference <= 30) {	
								remark = "PRT T.NO. " + nextTrainNo + " OF SET NO. " + returnedSetNo;
							}
						}
					}	
					
					if(train.getDestination().equalsIgnoreCase("MDD")){
						remark = "ETY TO KCS";				
					} else if(train.getDestination().equalsIgnoreCase("NSP")){
						remark = "ETY TO VR C/S";				
					} else if(train.getDestination().equalsIgnoreCase("MX") || train.getDestination().equalsIgnoreCase("BCL")){
						remark = "ETY TO C/S";				
					} 
					train.setPRTRemark(remark);
				}
			}
		}
		return setList;
	}
	
	private List<ScheduleSet> getSetListWithRORemark(List<ScheduleSet> setList){
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			for (ScheduledTrain train : trainList) {
				train.setRORemark(getRORemark(train, setList));
				break;
			}
		}
		return setList;
	}
	
	private List<ScheduleSet> getSetListWithTAPRemark(List<ScheduleSet> setList){
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			for (int i = 0; i < trainList.size()-1; i++) {
				if (!trainList.get(i).getDestination().equalsIgnoreCase(trainList.get(i+1).getOrigin())){
					if (trainList.get(i+1).getOrigin().equals("PL")) {
						trainList.get(i).setTAPRemark("TAP TO C/S" + 
								" BY TN NO. (" + trainList.get(i).getDestination() +" DEP)");
					} else {
						trainList.get(i).setTAPRemark("TAP TO " + trainList.get(i+1).getOrigin() + 
								" BY TN NO. (" + trainList.get(i).getDestination() +" DEP)");
					}					
				}
			}		
		}
		return setList;
	}
	
	private List<ScheduleSet> getSetListWithETYRemark(List<ScheduleSet> setList){
		for (ScheduleSet set : setList) {
			if (set.getTrainList().get(0).getOrigin().equalsIgnoreCase("MDD")) {
				set.getTrainList().get(0).setETYRemark("ETY TO MDD");	
			} else if (set.getTrainList().get(0).getOrigin().equalsIgnoreCase("NSP")) {
				set.getTrainList().get(0).setETYRemark("ETY TO NSP");	
			} else if (set.getTrainList().get(0).getOrigin().equalsIgnoreCase("MX")) {
				set.getTrainList().get(0).setETYRemark("ETY TO MX");	
			} else if (set.getTrainList().get(0).getOrigin().equalsIgnoreCase("BCL")) {
				set.getTrainList().get(0).setETYRemark("ETY TO BCL");	
			}
		}
		return setList;
	}
	
	/*private void setNOSRemark(){
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();			
			
			if (!trainList.get(0).getNOSRemark().equals("")) {
				set.setOnDutyNOSRemark("NOS");
				System.out.println(set.getSetNo());
			}
			
			if (!trainList.get(trainList.size()-1).getNOSRemark().equals("")) {
				set.setOffDutyNOSRemark("NOS");
				System.out.println(set.getSetNo());
			}
		}
	}*/
	
	private List<List<ScheduleSet>> getClubbedSets(List<ScheduleSet> setList){
		int setNumber = 1;
		
		int singleSet1 = 11;
		int singleSet2 = 20;
		
		List<List<ScheduleSet>> clubbedSets = new ArrayList<List<ScheduleSet>>();
		List<ScheduleSet> setPair = new ArrayList<ScheduleSet>();
		
		for (ScheduleSet set : setList) {
			if (setNumber == singleSet1) {
				setPair.add(set);	
				setPair.add(new ScheduleSet("empty"));
				singleSet1 += 20;
			} else if (setNumber == singleSet2) {					
				setPair.add(set);	
				setPair.add(new ScheduleSet("empty"));
				singleSet2 += 20;
			} else {
				setPair.add(set);	
			}
			
			if(setPair != null && setPair.size() == 2) {
				clubbedSets.add(setPair);
				setPair = new ArrayList<ScheduleSet>();
			}
			setNumber++;
		}		
		
		return clubbedSets;
	}	
	
	private String calculatetOnDutyTime(ArrayList<ScheduledTrain> trainList, String origin) {
		String onDutyTime = "";
		
		if (trainList != null) {
			String firstTrainDepartureTime = "00:00";
			int numTrains = trainList.size();
			if (numTrains > 0) {
				firstTrainDepartureTime = TimeUtil.timeToString(trainList.get(0).getDepartureTime());
			}
			
			if (origin.equals("KCS")) {
				onDutyTime =  TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(firstTrainDepartureTime, 45));
			} else if (origin.equals("BSR")) {
				onDutyTime =  TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(firstTrainDepartureTime, 30));
			} else if (origin.equals("C/S")) {
				onDutyTime =  TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(firstTrainDepartureTime, 40));
			} else if (origin.equals("VR C/S")) {
				onDutyTime =  TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(firstTrainDepartureTime, 85));
			} else {
				onDutyTime =  TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(firstTrainDepartureTime, 20));
			}			
		}
		
		return onDutyTime;
	}
	
	private String calculatetOffDutyTime(ArrayList<ScheduledTrain> trainList, String destination) {
		String offDutyTime = "";
		
		if (trainList != null) {
			String lastTrainArrivalTime = "00:00";
			int numTrains = trainList.size();
			if (numTrains > 0) {
				lastTrainArrivalTime = TimeUtil.timeToString(trainList.get(numTrains-1).getArrivalTime());
			}	
			
			if (destination.equals("KCS")) {
				offDutyTime =  TimeUtil.roundTimeToIncrease((TimeUtil.addMinutes(lastTrainArrivalTime, 35)));
			} else if (destination.equals("VR C/S")) {
				offDutyTime =  TimeUtil.roundTimeToIncrease((TimeUtil.addMinutes(lastTrainArrivalTime, 45)));
			} else if (destination.equals("C/S")) {
				offDutyTime =  TimeUtil.roundTimeToIncrease((TimeUtil.addMinutes(lastTrainArrivalTime, 30)));
			} else {
				offDutyTime =  TimeUtil.roundTimeToIncrease((TimeUtil.addMinutes(lastTrainArrivalTime, 20)));
			}
		}
		
		
		return offDutyTime;
	}
	
	private String[] getOrigenDestination(ArrayList<ScheduledTrain> trainList) {
		String[] origenDestination = {"", ""};
		
		if (trainList != null) {
			int numTrains = trainList.size();
			if (numTrains > 0) {
				origenDestination[0] = trainList.get(0).getOrigin();
				origenDestination[1] = trainList.get(numTrains-1).getDestination();
			}
		}		
		return origenDestination;
	}
	
	private void sortTrainList(List<ScheduledTrain> trainList) {
		int i = 0;
		int j = 0;
		for(i=0; i<(trainList.size()); i++) {		
			for (j = 0; j < i; j++) {
				Date dateI = trainList.get(i).getDepartureTime();
				Date dateJ = trainList.get(j).getDepartureTime();
				
				if ((dateI.compareTo(dateJ) > 0) && (TimeUtil.getHoursDifference(dateJ, dateI) > 10)) {
					ScheduledTrain temptrain;					
					temptrain = trainList.get(i);
					
					trainList.remove(i);
					trainList.add(i, trainList.get(j));
					
					trainList.remove(j);
					trainList.add(j, temptrain);
				} 
				
			}
		}
	}
	
	private void getRestHours(List<ScheduleSet> sets) {
		String restHours = "00:00";
		String offDutyTime = "00:00";
		String onDutyTime = "00:00";
		
		for (int i = 0; i < sets.size(); i++) {
			if ((i+1) < sets.size()) {
				offDutyTime = sets.get(i).getOffDutyTime();
				onDutyTime = sets.get(i+1).getOnDutyTime();
				restHours = TimeUtil.getDuration(TimeUtil.stringToTime(offDutyTime), TimeUtil.stringToTime(onDutyTime));				
				
			} else {
				offDutyTime = sets.get(i).getOffDutyTime();
				onDutyTime = sets.get(0).getOnDutyTime();
				restHours = TimeUtil.getDuration(TimeUtil.stringToTime(offDutyTime), TimeUtil.stringToTime(onDutyTime));
			}
			
			Date odt = TimeUtil.stringToTime(offDutyTime);
			Date startTime = TimeUtil.stringToTime("04:00");
			Date endTime = TimeUtil.stringToTime("07:00");
			if(odt.compareTo(startTime) >= 0 && odt.compareTo(endTime) <= 0) {
				long diff = TimeUtil.stringToTime(restHours).getTime() - TimeUtil.stringToTime("00:00").getTime() + 24*60*60*1000;
				int timeInSeconds = (int)diff / 1000;
		        int hours, minutes;
		        hours = timeInSeconds / 3600;
		        timeInSeconds = timeInSeconds - (hours * 3600);
		        minutes = timeInSeconds / 60;
		        timeInSeconds = timeInSeconds - (minutes * 60);
		        restHours = (hours<10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
			}
			
			/*String time[] = restHours.split(":");
			int newHours =Integer.parseInt(time[0]);
			String newMins =time[1];
			if (newHours < 12) {
				newHours = newHours + 24;
				restHours = newHours + ":" + newMins;
			}*/
			
			if (restHours.equals("00:00")) {
				restHours = "24:00";
			}
			
			sets.get(i).setRestHours(restHours);
		}		
	}
	
	private Object[] getOtherSetNo(String nextTrainNo, List<ScheduleSet> setList) {
		Object returnVals[] = {new Object(), new Date(), new String("")};
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			for (ScheduledTrain train : trainList) {
				if(train.getTrainNo().equalsIgnoreCase(nextTrainNo)) {
					returnVals[0] = set.getSetNo();
					returnVals[1] = train.getDepartureTime();
					returnVals[2]= train.getDestination();					
					return returnVals;
				}
			}
		}
		return returnVals;
	}
	
	private String getRORemark(ScheduledTrain t, List<ScheduleSet> setList) {
		Object setNo = t.getSetNo();
		String trainNo = t.getTrainNo();
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			for (ScheduledTrain train : trainList) {
				int mins = TimeUtil.getMinuteDiffetence(train.getArrivalTime(), t.getDepartureTime());
				
				if(train.getNextTrainNo().equalsIgnoreCase(trainNo) && !t.getOrigin().equalsIgnoreCase("CCG") &&
						!train.getSetNo().toString().equalsIgnoreCase(setNo.toString()) && mins <=20) {
					return "RO SET NO. " + train.getSetNo();					
				}
			}
		}
		return "";
	}
	
	private String[] fetchSunDayOnDutyVals (List<ScheduledTrain> tList) {
		 String[] returnVals = {"", ""};
		if (tList.get(0).getNOSRemark() !=null 
				&& !tList.get(0).getNOSRemark().equals("")) {
			for (int i = 1; i < tList.size(); i++) {
				ScheduledTrain train = tList.get(i);
				if (train.getNOSRemark() == null) {
					returnVals[0] = train.getOrigin();
					returnVals[1] = TimeUtil.roundTimeToReduce(TimeUtil.minusMinutes(TimeUtil.timeToString(train.getDepartureTime()), 20));
					return returnVals;
				}
			}
		}
		return null;
	}
	
	/*private ScheduledTrain fetchSunDayOffDutyTrain (List<ScheduledTrain> tList) {
		if (!tList.get(tList.size()-1).getNOSRemark().equals("")) {
			for (int i = (tList.size()-2); i >= 0 ; i--) {
				if (tList.get(i).getNOSRemark().equals("")) {
					return tList.get(i);
				}
			}
		}
		return null;
	}	*/
	
	private List<ScheduleSet> getSetListWithROAfterProtection(List<ScheduleSet> setList){
		for (ScheduleSet set : setList) {
			List<ScheduledTrain> trainList = set.getTrainList();
			
			for(int i =0; i<trainList.size(); i++) {
				ScheduledTrain currentTrain = trainList.get(i);
				if (i+1 < trainList.size()) {
					if(currentTrain.getPRTRemark() != null) {
						ScheduledTrain nextTrain = trainList.get(i+1);
						String nextTrainNo = nextTrain.getTrainNo();
						String roRemark = getROSetNo(nextTrainNo, setList);
						nextTrain.setRORemark(roRemark);
					}
				}				
			}
		}
		return setList;
	}
	
	private String getROSetNo(String trainNo, List<ScheduleSet> setList) {
		for (ScheduleSet set : setList) {
			for (ScheduledTrain train : set.getTrainList()) {
				if (train.getNextTrainNo().equalsIgnoreCase(trainNo)) {
					return "R/O SET NO. " + train.getSetNo();
				}
			}
		}
		return "";
	}
}


