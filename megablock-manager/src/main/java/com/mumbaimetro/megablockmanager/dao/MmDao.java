package com.mumbaimetro.megablockmanager.dao;

import java.util.List;

import com.mumbaimetro.megablockmanager.dto.Train;
import com.mumbaimetro.megablockmanager.dto.TrainSetMap;

public interface MmDao {
	public void writeToDb(List<TrainSetMap> trainSets) ;
	public int updateCancellationStatus(List<String> cancelledTrains);
	public List<String> getCancelledSetNumbers();
	public List<TrainSetMap> getCancelledTrainsBySetNumber(String setNumber);	
	public List<Train> getNonCancelledTrainsBySetNumber(String setNumber);
}
