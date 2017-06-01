package com.mumbaimetro.megablockmanager.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mumbaimetro.megablockmanager.dao.MmDao;
import com.mumbaimetro.megablockmanager.dto.CancelledSet;
import com.mumbaimetro.megablockmanager.dto.Train;
import com.mumbaimetro.megablockmanager.dto.TrainSetMap;
import com.mumbaimetro.megablockmanager.util.ExcelUtil;

@Service
public class MmServiceImpl implements MmService{

	@Autowired
	private MmDao mmDao;
	
	@Value("${trainWiseSetFilePath}")
	private String trainWiseSetFilePath;
	
	@Value("${cancelledTrainFilePath}")
	private String cancelledTrainFilePath;	
	
	@Value("${reportFilePath}")
	private String reportFilePath;	
	
	@SuppressWarnings("deprecation")
	@Transactional
	public void writeExcelToDb() throws IOException {		
		File file = new File(trainWiseSetFilePath);
		System.out.println("\n\n\nReading excel...\n" + file.getAbsolutePath());	
		List<TrainSetMap> trainSets = new ArrayList<TrainSetMap>();
		
		FileInputStream excelFile = new FileInputStream(file);
        
		Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        while (iterator.hasNext()) {
        	Row currentRow = iterator.next();

        	if (currentRow.getRowNum()==0) continue;
        	
            TrainSetMap dto = new TrainSetMap();
           
            Cell cell0 = currentRow.getCell(0);            
            if (cell0.getCellTypeEnum() == CellType.STRING) {
            	dto.setTrainNumber(cell0.getStringCellValue());               
            } else if (cell0.getCellTypeEnum() == CellType.NUMERIC) {           	 	
           	 	dto.setTrainNumber(new Long((long)cell0.getNumericCellValue()).toString());
            }
            
            Cell cell1 = currentRow.getCell(1);
            String setNumber = "";
            if (cell1.getCellTypeEnum() == CellType.STRING) {
            	setNumber = cell1.getStringCellValue();
            	//dto.setSetNumber(cell1.getStringCellValue());               
            } else if (cell1.getCellTypeEnum() == CellType.NUMERIC) {
            	setNumber = new Long((long)cell1.getNumericCellValue()).toString();
           	 	//dto.setSetNumber(new Long((long)cell1.getNumericCellValue()).toString());
            } 
            
            Cell cell2 = currentRow.getCell(2);
            if (cell2 != null && 
            		cell2.getStringCellValue().equalsIgnoreCase("YES")) {
            	dto.setNos(Boolean.TRUE); 
            	dto.setCancelled(Boolean.TRUE);
            } 
            
            if (setNumber.contains("/")) {
            	String [] setNumbers = setNumber.split("/");
            	dto.setSetNumber(setNumbers[1]);
            	
            	TrainSetMap nextDto = new TrainSetMap();
            	nextDto.setTrainNumber(dto.getTrainNumber());
            	nextDto.setSetNumber(setNumbers[0]);
            	nextDto.setNos(dto.isNos());
            	nextDto.setCancelled(dto.isCancelled());
            	trainSets.add(nextDto);
            } else {
            	dto.setSetNumber(setNumber);
            }
            
            trainSets.add(dto);

         }
        workbook.close();
        
        mmDao.writeToDb(trainSets);
	}
	
	@Transactional
	public void writeResultToExcel() throws IOException {
		List<String> cancelledTrains = getCancelledTrainList();	
		mmDao.updateCancellationStatus(cancelledTrains);
		List<String> cancelledSetNumbers = mmDao.getCancelledSetNumbers();
		
		List<CancelledSet> cancelledSets = new ArrayList<CancelledSet>();
		for (String setNumber : cancelledSetNumbers) {
			List<TrainSetMap> trainSetMappings = mmDao.getCancelledTrainsBySetNumber(setNumber);
			CancelledSet cancelledSet = new CancelledSet();
			cancelledSet.setSetNumber(setNumber);
			
			List<Train> trains = new ArrayList<Train>();			
			for (TrainSetMap ts : trainSetMappings) {
				trains.add(new Train(ts.getTrainNumber(), ts.isNos()));				
			}
			
			cancelledSet.setCancelledTrains(trains);
			
			cancelledSets.add(cancelledSet);
		}		
		
		for (CancelledSet cancelledSet : cancelledSets) {
			List<Train> nonCancelledTrains = mmDao.getNonCancelledTrainsBySetNumber(cancelledSet.getSetNumber());
			cancelledSet.setNonCancelledTrains(nonCancelledTrains);
			if (cancelledSet.getNonCancelledTrains().size() == 0) {
				cancelledSet.setVacant(Boolean.TRUE);
			}
		}
		
		Collections.sort(cancelledSets);
		generateExcel(cancelledSets);	
		
		System.out.println("\n\n\nThank You!!!");	
		System.out.println("Exiting in next 30 seconds......");	
	}
	
	@SuppressWarnings("deprecation")
	public List<String> getCancelledTrainList() throws IOException {
		File file = new File(cancelledTrainFilePath);
		System.out.println("\n\n\nReading excel...\n" + file.getAbsolutePath());	
		List<String> trainList = new ArrayList<String>();
		
		FileInputStream excelFile = new FileInputStream(file);
        
		Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        while (iterator.hasNext()) {
        	Row currentRow = iterator.next();
        	
        	String trainNumber = "";
            Cell cell = currentRow.getCell(0); 
            
            if (cell == null) {
            	continue;
            }
            
            if (cell.getCellTypeEnum() == CellType.STRING) {
            	trainNumber = cell.getStringCellValue();               
            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {           	 	
            	trainNumber = new Long((long)cell.getNumericCellValue()).toString();
            }             
            trainList.add(trainNumber);
         }
        
        workbook.close();
		return trainList;
	}		
	
	private void generateExcel(List<CancelledSet> cancelledSets) {		
		File file = new File(reportFilePath);
		System.out.println("\n\n\nWriting excel...\n" + file.getAbsolutePath());
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        
        Cell headerCell0 = headerRow.createCell(0);
        headerCell0.setCellValue("Set");
        
        Cell headerCell3 = headerRow.createCell(1);
        headerCell3.setCellValue("Vacant");
        
        Cell headerCell1 = headerRow.createCell(2);
        headerCell1.setCellValue("Canceled Trains");
        
        Cell headerCell2 = headerRow.createCell(3);
        headerCell2.setCellValue("Non Canceled Trains");        
        
        ExcelUtil excelUtil = ExcelUtil.getExcelUtil(workbook);
        excelUtil.setHeaderStyle();
        
        int setIndex = 0;
        for (CancelledSet cancelledSet : cancelledSets) {
        	setIndex++;
        	String setNumber = cancelledSet.getSetNumber();
        	List<Train> cancelledTrains = cancelledSet.getCancelledTrains();
        	List<Train> nonCancelledTrains = cancelledSet.getNonCancelledTrains();
        	String vacant = "";
        	if (nonCancelledTrains.size() == 0) vacant = "Vacant";
        	
        	int rowsInSet = cancelledTrains.size() > nonCancelledTrains.size() ? cancelledTrains.size() : nonCancelledTrains.size();
        	
			for (int i = 0; i < rowsInSet; i++) {
				
				Row row = sheet.createRow(rowNum++);
				Cell setNumberCell = row.createCell(0);
				Cell vacantCell = row.createCell(1);
				Cell cancelledTrainsCell = row.createCell(2);
				Cell nonCancelledTrainsCell = row.createCell(3);
				excelUtil.setContentStyles((rowNum-1), setIndex);
				setNumberCell.setCellValue(setNumber);
				setNumber = "";		
				
				vacantCell.setCellValue(vacant);
				vacant = "";	
				
				try{
					if (cancelledTrains.get(i).isNos()) {
						cancelledTrainsCell.setCellValue(cancelledTrains.get(i).getTrainNumber() + " (NOS)");
						excelUtil.setNosStyle(cancelledTrainsCell, setIndex);
					} else {
						cancelledTrainsCell.setCellValue(cancelledTrains.get(i).getTrainNumber());
					}
					
				} catch (IndexOutOfBoundsException iobe) {
					cancelledTrainsCell.setCellValue("");
				}
				
				try{
					if (nonCancelledTrains.get(i).isNos()) {
						nonCancelledTrainsCell.setCellValue(nonCancelledTrains.get(i).getTrainNumber() + " (NOS)");
						excelUtil.setNosStyle(nonCancelledTrainsCell, setIndex);
					} else {
						nonCancelledTrainsCell.setCellValue(nonCancelledTrains.get(i).getTrainNumber());
					}
					
				} catch (IndexOutOfBoundsException iobe) {
					nonCancelledTrainsCell.setCellValue("");
				}
			}      
			
			if (rowsInSet > 1) {
				sheet.addMergedRegion(new CellRangeAddress((rowNum-rowsInSet),(rowNum-1),0,0));
				sheet.addMergedRegion(new CellRangeAddress((rowNum-rowsInSet),(rowNum-1),1,1));
			}
						
        }
        
        
        FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
		    workbook.close();
		    System.out.println("\n\n\nSuccessfully generated excel\n" + reportFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}	
	
	/*private void generateExcel(List<CancelledSet> cancelledSets) {		
		System.out.println("\n\n\nWriting excel...\n" + reportFilePath);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        
        int rowNum = 0;
        
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        
        Row row1 = sheet.createRow(rowNum++);
        Cell headerCell0 = row1.createCell(0);
        headerCell0.setCellValue("Set");
        headerCell0.setCellStyle(headerStyle);
        
        Cell headerCell1 = row1.createCell(1);
        headerCell1.setCellValue("Canceled Trains");
        headerCell1.setCellStyle(headerStyle);
        
        Cell headerCell2 = row1.createCell(2);
        headerCell2.setCellValue("Non Canceled Trains");
        headerCell2.setCellStyle(headerStyle);
        
        Cell headerCell3 = row1.createCell(3);
        headerCell3.setCellValue("Vacant");
        headerCell3.setCellStyle(headerStyle);
        
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setWrapText(true);
        
        for (CancelledSet cancelledSet : cancelledSets) {
        	Row row = sheet.createRow(rowNum++);
        	int colNum = 0;
        	
        	Cell setNumberCell = row.createCell(colNum++);
        	setNumberCell.setCellValue(cancelledSet.getSetNumber());
        	
        	Cell cancelledTrainsCell = row.createCell(colNum++);
        	String cancelledTrainsStr = "";
        	
        	for (int i = 0; i < cancelledSet.getCancelledTrains().size(); i++) {
        		cancelledTrainsStr += cancelledSet.getCancelledTrains().get(i);
        		if ((i+1) != cancelledSet.getCancelledTrains().size()) {
        			cancelledTrainsStr += "\n";
        		}        		
        	}        	
        	cancelledTrainsCell.setCellValue(cancelledTrainsStr);
        	
        	
        	Cell nonCancelledTrainsCell = row.createCell(colNum++);
        	String nonCancelledTrainsStr = "";
        	
        	for (int i = 0; i < cancelledSet.getNonCancelledTrains().size(); i++) {
        		nonCancelledTrainsStr += cancelledSet.getNonCancelledTrains().get(i);
        		if ((i+1) != cancelledSet.getNonCancelledTrains().size()) {
        			nonCancelledTrainsStr += "\n";
        		}
        		
        	}
        	nonCancelledTrainsCell.setCellValue(nonCancelledTrainsStr);
        	
        	if (cancelledSet.getCancelledTrains().size() >cancelledSet.getNonCancelledTrains().size())  {
        		row.setHeightInPoints((cancelledSet.getCancelledTrains().size()*
        				sheet.getDefaultRowHeightInPoints()));
        		sheet.autoSizeColumn(1,false);
        	} else {
        		row.setHeightInPoints((cancelledSet.getNonCancelledTrains().size()*
        				sheet.getDefaultRowHeightInPoints()));
        		sheet.autoSizeColumn(2,false);
        	}
        	
        	Cell vacantCell = row.createCell(colNum++);
        	if (cancelledSet.isVacant()) {
        		vacantCell.setCellValue("Vacant");
        	} else {
        		vacantCell.setCellValue("");
        	}        	
        }
        
        FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(reportFilePath);
			workbook.write(outputStream);
		    workbook.close();
		    System.out.println("\n\n\nSuccessfully generated excel\n" + reportFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}	*/
}
