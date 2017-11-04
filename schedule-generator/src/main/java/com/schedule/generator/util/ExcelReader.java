package com.schedule.generator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.schedule.generator.entity.ScheduledTrain;
import com.schedule.generator.exception.ExcelException;
import com.schedule.generator.exception.PerformaFormatException;

public class ExcelReader {

	private Workbook  wb = null;
	private boolean isNumericSet = true;
	private InputStream inputStream;
	
	public ExcelReader(InputStream is, boolean isNumericSet) throws EncryptedDocumentException, InvalidFormatException, IOException {		
			this.isNumericSet = isNumericSet;
			this.inputStream = is;
			wb = WorkbookFactory.create(inputStream);			
	}
	
	@SuppressWarnings("deprecation")
	public List<ScheduledTrain> getTrainList() throws IOException, ExcelException {
		List<ScheduledTrain> trainList = new ArrayList<ScheduledTrain>();
		
		Sheet sheet = wb.getSheet(Constants.PERFORMA_SHEET);
		int rows;
  	    try {
  	    	rows = sheet.getPhysicalNumberOfRows();	
		} catch (NullPointerException e) {
			throw new PerformaFormatException("Malformed performa selected, sheet \"" + 
					Constants.PERFORMA_SHEET + "\" not found.");
		}
		
		System.out.println("\nTotal rows present in PROFARMA:\t" + rows);
		
		Sheet distanceSheet = wb.getSheet(Constants.KM_SHEET); 
		Map<String, Object> distanceMap;
		try {
			distanceMap = getMap(distanceSheet);
		} catch (NullPointerException e) {
			throw new PerformaFormatException("Malformed performa selected, sheet \"" + 
					Constants.KM_SHEET + "\" not found.");
		}
  	   	
		Sheet setCodeSheet = wb.getSheet(Constants.SET_CODES_SHEET); 
  	    Map<String, Object> setCodeMap;
  	    try {
  	    	setCodeMap = getMap(setCodeSheet);
  	    } catch (NullPointerException e) {
  	    	throw new PerformaFormatException("Malformed performa selected, sheet \"" + 
				Constants.SET_CODES_SHEET + "\" not found.");
  	    }
  	    
		for (int rowNumber = 1; rowNumber <= rows; rowNumber++) {				
			Row row = sheet.getRow(rowNumber);
           ScheduledTrain train = new ScheduledTrain();
           
           if (row == null) {
               continue;
           } else {  
        		   row.getCell(0).setCellType(1);        		  
        		   train.setTrainNo(row.getCell(0).getStringCellValue());
        		   
        		   String direction = row.getCell(9).getStringCellValue();		
        		   
        		   String destCode = row.getCell(2).getStringCellValue().trim();
        		   String [] oridinDest = destCode.split("\\s+");
        		  
        		   if(direction.equals("UP")) {
	        		   if (oridinDest.length == 1) {
	        			   train.setOrigen(destCode);
	        			   train.setDestination("CCG");
	        		   } else  if (oridinDest.length == 2) {
	        			   train.setOrigen(oridinDest[0]);
	        			   train.setDestination(oridinDest[1]);
	        		   } else {
	        			   throw new ExcelException("Origin/Destination in excel is not correct, " +
	        					   "please check train number \"" + train.getTrainNo() + 
	        					   "\" and try again with correct data in excel.");
	        		   }
	        		   
	        	   } else if(direction.equals("DOWN")) {
	        		   if (oridinDest.length == 1) {
	        			   train.setDestination(row.getCell(2).getStringCellValue());
	        			   train.setDestination(destCode);
	        			   train.setOrigen("CCG");
	        		   } else  if (oridinDest.length == 2) {
	        			   train.setOrigen(oridinDest[0]);
	        			   train.setDestination(oridinDest[1]);
	        		   }  else {
	        			   throw new ExcelException("Origin/Destination in excel is not correct, " +
	        					   "please check train number \"" + train.getTrainNo() + 
	        					   "\" and try again with correct data in excel.");
	        		   }
	        	   }
        		   
        		   String oriDest = train.getOrigin() + "-" + train.getDestination();        		           	   
	        	   float distance = 0;	        	   
	        	   Set<String> keySet = distanceMap.keySet();
	        	   for (String key: keySet) {
	        		   if (key.equalsIgnoreCase(oriDest)) {
	        			   distance = Float.parseFloat(distanceMap.get(key).toString());
	        		   }
	        	   }
	        	   train.setDistance(distance);
	        	   
	        	   if (row.getCell(3).getCellType() != 3) {
	        		   row.getCell(3).setCellType(1);
	        		   train.setMode(row.getCell(3).getStringCellValue());
	        		   train.setModeCode("(F)");
	        	   } 
	        	   
	        	   if (row.getCell(4) != null && row.getCell(4).getCellType() != 3) {
	        		   Cell cell = row.getCell(4);
	        		   cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        		   train.setDepartureTime(cell.getDateCellValue());
	        	   }
	        	   
	        	   if (row.getCell(5) != null && row.getCell(5).getCellType() != 3) {
	        		   Cell cell = row.getCell(5);
        		   	   cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	        		   train.setArrivalTime(cell.getDateCellValue());
	        	   }
	        	   
	        	   row.getCell(6).setCellType(1);
	        	   if (!row.getCell(6).getStringCellValue().trim().equals("")) {
	        		   //train.setNextTrainNo(row.getCell(6).getStringCellValue().trim());
	        		   String tempStr = row.getCell(6).getStringCellValue().trim();
	        		   String tempStrArr[] = tempStr.split("[\\s]+");
 	        		   train.setNextTrainNo(tempStrArr[0]);
	        	   }        		   
	        	   row.getCell(7).setCellType(1);
	        	   String setNo = row.getCell(7).getStringCellValue().trim().replaceAll(" ", "");	
	        	   	       	  
	        	   Integer setNoInt = 0;	        	   
	        	   Set<String> setCodes = setCodeMap.keySet();
	        	   for (String key: setCodes) {	        		  
	        		   if (key.equalsIgnoreCase(setNo)) {
	        			   setNoInt = Integer.parseInt(setCodeMap.get(key).toString());
	        		   }
	        	   }
	        	  
	        	   if (isNumericSet) {
	        		   train.setSetNo(setNoInt.toString());
	        	   } else {
	        		   train.setSetNo(setNo);
	        	   }
	        	   
	        	   try {
	        		   if(row.getCell(8) != null && row.getCell(8).getCellType() != 3) {
		        		   String [] numCarStr = row.getCell(8).getStringCellValue().split("\\s+");
			        	   if (numCarStr[0].equals("15"))
			        		   train.setNumCars(numCarStr[0] + " CAR");
		        	   }
	        		   
	        		   if (row.getCell(10) != null && row.getCell(10).getCellType() != 3) {
		        		   row.getCell(10).setCellType(1);	
		        		   String prefix = train.getModeCode() == null ? "" : train.getModeCode() + " ";
		        		   train.setModeCode(prefix + row.getCell(10).getStringCellValue());
		        		   train.setNOSRemark(row.getCell(10).getStringCellValue());
		        	   }
	        		   
	        	   } catch (NullPointerException npe) {
	        		  npe.printStackTrace();
	        	   }	        	   
           }
           trainList.add(train);
		}
				
		return trainList;
	}
	
	@SuppressWarnings("deprecation")
	private Map<String, Object> getMap(Sheet sheet) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		int rows = sheet.getPhysicalNumberOfRows();			
		for (int rowNumber = 1; rowNumber <= rows; rowNumber++) {				
		   Row row = sheet.getRow(rowNumber);
           
           if (row == null) {
               continue;
           } else {    
        	  
        	   try {
        		   row.getCell(1).setCellType(1);
        		   row.getCell(0).setCellType(1);
        		   String mapKey = row.getCell(0).getStringCellValue().trim().replaceAll(" ", "");
        		   map.put(mapKey, row.getCell(1).getStringCellValue());
        	   } catch (NullPointerException e) {
        		  System.out.println("\nWARNING>>>Ignoring row#" + (rowNumber+1) + " in " + sheet.getSheetName() + " sheet (POSSIBLE REASON: missing cell content)" );        		   
        	   } catch (Exception e) {
         		  throw new PerformaFormatException("Error in " + sheet.getSheetName() + " sheet on row number " + (rowNumber+1));        		   
         	   }
           }           
		}
		return map;		
	}
	
	public void close () throws IOException {
		inputStream.close();
		wb.close();
	}
}
