package com.schedule.generator.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.schedule.generator.dto.ScheduleSet;
import com.schedule.generator.entity.ScheduledTrain;
import com.schedule.generator.util.StyleUtil;

public class ExcelWriter {

	HSSFWorkbook wb = null;	
	HSSFSheet sheet = null;	
	FileOutputStream fileOut = null;
	
	public ExcelWriter() throws FileNotFoundException {
		 wb = new HSSFWorkbook();
		 sheet = wb.createSheet();
		 fileOut = new FileOutputStream(Constants.OUTPUT_FILE);		
	}
	
	public void close() throws IOException {
		wb.close();
	}
	
	public String writeSets(List<List<ScheduleSet>> clubbedSetList) {
		String outputMsg = "";
		int rowNum = 2;
		
		StyleUtil style = new StyleUtil(wb);
		HSSFCellStyle boldStyle = style.getBoldStyle();
		HSSFCellStyle boldULStyle = style.getBoldUnderLineStyle();
		HSSFCellStyle smallFontStyle = style.getSmallFontStyle();
		HSSFCellStyle leftBorderStyle = style.getLeftBorderStyle();
		HSSFCellStyle rightBorderStyle = style.getRightBorderStyle();
		HSSFCellStyle topBorderStyle = style.getTopBorderStyle();
		HSSFCellStyle leftTopCornerBorderStyle = style.getLeftTopCornerBorderStyle();
		HSSFCellStyle leftBottomCornerBorderStyle = style.getLeftBottomCornerBorderStyle();
		HSSFCellStyle topRightCornerBorderStyle = style.getTopRightCornerBorderStyle();
		HSSFCellStyle bottomBorderStyle = style.getBottomBorderStyle();
		HSSFCellStyle rightBottomCornerBorderStyle = style.getRightBottomCornerBorderStyle();
		HSSFCellStyle remarkStyle = style.getRemarkCellStyle();
		
		sheet.setDefaultColumnStyle(1, boldStyle);
		sheet.setDefaultColumnStyle(2, boldStyle);
		sheet.setDefaultColumnStyle(3, boldStyle);
		sheet.setDefaultColumnStyle(4, boldStyle);
		sheet.setDefaultColumnStyle(5, boldStyle);
		sheet.setDefaultColumnStyle(6, boldStyle);
		sheet.setDefaultColumnStyle(7, boldStyle);
		
		DecimalFormat df = new DecimalFormat();
	    df.setMinimumFractionDigits(2);
	    
		for (List<ScheduleSet> setPair : clubbedSetList) {
			for(ScheduleSet set : setPair) {
				ArrayList<ScheduledTrain> tList = set.getTrainList();
				
				if (!set.toString().equalsIgnoreCase("empty")) {					
					HSSFRow row1 = sheet.createRow(rowNum++);
					row1.createCell(1);
					row1.getCell(1).setCellStyle(leftTopCornerBorderStyle);
					
					row1.createCell(2);
					row1.getCell(2).setCellStyle(topBorderStyle);
					
					row1.createCell(4);
					row1.getCell(4).setCellStyle(topBorderStyle);
					
					row1.createCell(5);
					row1.getCell(5).setCellStyle(topBorderStyle);
					
					row1.createCell(5);
					row1.getCell(5).setCellStyle(topRightCornerBorderStyle);
					
					HSSFCell cell = row1.createCell(3); 				
					
					cell.setCellStyle(boldULStyle);
					cell.setCellValue("SET NO. " + set.getSetNo());	
					
					HSSFRow row2 = sheet.createRow(rowNum++);
					row2.createCell(1).setCellValue("ON DUTY");	
					row2.getCell(1).setCellStyle(leftBorderStyle);	
					row2.createCell(2).setCellValue(set.getOnDutyTime());
					row2.createCell(3).setCellValue(set.getOrigen());					
					row2.createCell(4).setCellValue("KMS:");
					row2.createCell(5).setCellValue(set.getDistance());
					row2.getCell(5).setCellStyle(rightBorderStyle);	
					
					if (set.getSunDayOnDutyVals() != null) {
						HSSFRow row2NOS = sheet.createRow(rowNum++);						
						row2NOS.createCell(1).setCellValue("SUN:");	
						row2NOS.getCell(1).setCellStyle(remarkStyle);
						row2NOS.createCell(2).setCellValue(set.getSunDayOnDutyVals()[1]);
						row2NOS.getCell(2).setCellStyle(style.getRemarkCellStyleNoBorder());
						row2NOS.createCell(3).setCellValue(set.getSunDayOnDutyVals()[0]);	
						row2NOS.getCell(3).setCellStyle(style.getRemarkCellStyleNoBorder());
						row2NOS.createCell(5);
						row2NOS.getCell(5).setCellStyle(rightBorderStyle);
					}
					
					HSSFRow row3 = sheet.createRow(rowNum++);
					row3.createCell(1).setCellValue("OFF DUTY");	
					row3.getCell(1).setCellStyle(leftBorderStyle);
					row3.createCell(2).setCellValue(set.getOffDutyTime());	
					row3.createCell(3).setCellValue(set.getDestination());
					row3.createCell(4).setCellValue("HRS:");
					row3.createCell(5).setCellValue(set.getDuration());
					row3.getCell(5).setCellStyle(rightBorderStyle);	
					
					/*if (set.getSunDayOffDutyTrain() != null) {
						HSSFRow row3NOS = sheet.createRow(rowNum++);
						row3NOS.createCell(1).setCellValue("SUN:");	
						row3NOS.getCell(1).setCellStyle(leftBorderStyle);
						row3NOS.getCell(1).setCellStyle(remarkStyle);
						row3NOS.createCell(2).setCellValue(TimeUtil.timeToString(tList.get(tList.size()-2).getArrivalTime()));
						row3NOS.getCell(2).setCellStyle(remarkStyle);
						row3NOS.createCell(3).setCellValue(tList.get(tList.size()-2).getDestination());		
						row3NOS.getCell(3).setCellStyle(remarkStyle);
						row3NOS.createCell(4).setCellValue("SUN:");
						row3NOS.getCell(4).setCellStyle(remarkStyle);
						float distanceOnSunday = Float.valueOf(set.getDistance()) - tList.get(tList.size()-2).getDistance();
						row3NOS.createCell(5).setCellValue(distanceOnSunday);
						row3NOS.getCell(5).setCellStyle(rightBorderStyle);
						row3NOS.getCell(5).setCellStyle(remarkStyle);
					}*/
					
					//Blank Row
					HSSFRow row4 = addRow(rowNum++, 10.00);
					row4.getCell(1).setCellStyle(leftBorderStyle);					
					row4.getCell(5).setCellStyle(rightBorderStyle);
					
					int bigRow = 0;
					int smallRow = 0;					
					
					for (int trainNum = 0; trainNum <tList.size(); trainNum++) {
						ScheduledTrain train = tList.get(trainNum);
					//for (ScheduledTrain train : tList) {
						
						if (train.getRORemark() != null) {
							if (trainNum == 0) {
								row4.getCell(1).setCellValue(train.getRORemark());
								row4.getCell(1).setCellStyle(remarkStyle);
							}
							//row4.getCell(1).setCellValue(train.getRORemark());
							//row4.getCell(1).setCellStyle(remarkStyle);
						}
						if (train.getETYRemark() != null) {
							row4.getCell(1).setCellValue(train.getETYRemark());
							row4.getCell(1).setCellStyle(remarkStyle);
						}
						
						HSSFRow row5 = sheet.createRow(rowNum++);
						bigRow++;
						row5.createCell(1).setCellValue(train.getTrainNo());	
						row5.getCell(1).setCellStyle(leftBorderStyle);	
						row5.createCell(2).setCellValue(train.getOrigin() + "-" + train.getDestination());
						row5.createCell(3).setCellValue(train.getModeCode());
						row5.createCell(4).setCellValue(TimeUtil.timeToString(train.getDepartureTime()));
						row5.createCell(5).setCellValue(TimeUtil.timeToString(train.getArrivalTime()));
						row5.getCell(5).setCellStyle(rightBorderStyle);	
						row5.createCell(6).setCellValue(df.format(train.getDistance()));
						
						if(train.getMode() != null || train.getNumCars() != null){
							HSSFRow row6 = sheet.createRow(rowNum++);
							bigRow++;
							row6.createCell(1).setCellValue(train.getNumCars());
							row6.getCell(1).setCellStyle(leftBorderStyle);
							
							row6.createCell(2).setCellValue(train.getMode());
							row6.getCell(2).setCellStyle(smallFontStyle);						
						
							row6.createCell(5);
							row6.getCell(5).setCellStyle(rightBorderStyle);
						}
						
						//Blank Row
						HSSFRow row7 = addRow(rowNum++, 10.00);
						smallRow++;
						row7.getCell(1).setCellValue(train.getPRTRemark());
						row7.getCell(1).setCellStyle(remarkStyle);
						row7.getCell(5).setCellStyle(rightBorderStyle);
						
						if (row7.getCell(1).getStringCellValue().equals("")) {
							row7.getCell(1).setCellValue(train.getTAPRemark());
						} else {
							HSSFRow row8 = addRow(rowNum++, 10.00);
							smallRow++;
							row8.getCell(1).setCellValue(train.getTAPRemark());
							row8.getCell(1).setCellStyle(remarkStyle);
							row8.getCell(5).setCellStyle(rightBorderStyle);
						}
					}
					
					while(bigRow <8) {
						HSSFRow spaceBigRow = addRow(rowNum++);
						bigRow++;
						spaceBigRow.getCell(1).setCellStyle(leftBorderStyle);
						spaceBigRow.getCell(5).setCellStyle(rightBorderStyle);
					}
					while (smallRow < 4) {
						HSSFRow spaceSmallRow = addRow(rowNum++, 10.00);
						smallRow++;
						spaceSmallRow.getCell(1).setCellStyle(leftBorderStyle);
						spaceSmallRow.getCell(5).setCellStyle(rightBorderStyle);
					}
					
					HSSFRow row9 = sheet.createRow(rowNum++);
					row9.createCell(1);
					row9.getCell(1).setCellStyle(leftBottomCornerBorderStyle);					
					row9.createCell(2).setCellValue("REST HRS:");
					row9.getCell(2).setCellStyle(bottomBorderStyle);
					row9.createCell(3).setCellValue(set.getRestHours());
					row9.getCell(3).setCellStyle(bottomBorderStyle);
					row9.createCell(4);
					row9.getCell(4).setCellStyle(bottomBorderStyle);
					row9.createCell(5);
					row9.getCell(5).setCellStyle(bottomBorderStyle);				
					row9.createCell(5);
					row9.getCell(5).setCellStyle(rightBottomCornerBorderStyle);	
				} else {
					HSSFRow row1 = addRow(rowNum++);
					row1.getCell(1).setCellStyle(leftTopCornerBorderStyle);					
					row1.getCell(2).setCellStyle(topBorderStyle);
					row1.getCell(3).setCellStyle(boldULStyle);
					row1.getCell(4).setCellStyle(topBorderStyle);					
					row1.getCell(5).setCellStyle(topBorderStyle);					
					row1.getCell(5).setCellStyle(topRightCornerBorderStyle);
					
					int bigRow = 0;
					int smallRow = 0;
					
					while(bigRow < 10) {
						HSSFRow spaceBigRow = addRow(rowNum++);
						bigRow++;
						spaceBigRow.getCell(1).setCellStyle(leftBorderStyle);
						spaceBigRow.getCell(5).setCellStyle(rightBorderStyle);
					}							
					while (smallRow < 5) {
						HSSFRow spaceSmallRow = addRow(rowNum++);
						smallRow++;
						spaceSmallRow.getCell(1).setCellStyle(leftBorderStyle);
						spaceSmallRow.getCell(5).setCellStyle(rightBorderStyle);							
					}
					HSSFRow row2 = addRow(rowNum++);
					row2.getCell(1).setCellStyle(leftBottomCornerBorderStyle);					
					row2.getCell(2).setCellStyle(bottomBorderStyle);
					row2.getCell(3).setCellStyle(bottomBorderStyle);
					row2.getCell(4).setCellStyle(bottomBorderStyle);			
					row2.getCell(5).setCellStyle(rightBottomCornerBorderStyle);	
				}
			}
			for(int i = 1; i <= 4; i++) {
				//Blank Big Row for separating sets
				sheet.createRow(rowNum++);					
			}
		}
				
		sheet.setColumnHidden(6, true);
		try {
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			outputMsg = e.getMessage() + "\n";
			e.printStackTrace();
		}
		System.out.println("\nOutput file generated successfully!!!");
		outputMsg += "<html>Output file generated successfully";
		
		return outputMsg;
	}	
	
	private HSSFRow addRow(int rowNum, double... height) {
		HSSFRow row = sheet.createRow(rowNum++);
		if(height.length == 1) {
			row.setHeightInPoints((float)height[0]);			
		}
		row.createCell(1);					
		row.createCell(2);
		row.createCell(3);
		row.createCell(4);			
		row.createCell(5);	
		return row;
	}

}
