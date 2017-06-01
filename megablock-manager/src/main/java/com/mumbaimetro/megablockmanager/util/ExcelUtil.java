package com.mumbaimetro.megablockmanager.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	private static ExcelUtil excelUtil;
	
	private XSSFCellStyle headerStyle;
	private XSSFCellStyle oddRowStyle;
	private XSSFCellStyle evenRowStyle;
	private XSSFCellStyle nosOddRowStyle;
	private XSSFCellStyle nosEvenRowStyle;
	private XSSFWorkbook workbook;
	
	private ExcelUtil (XSSFWorkbook workbook) {
		this.workbook = workbook;
		Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(HSSFColor.WHITE.index);
        
        headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0,132,209)));
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setFont(headerFont);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		oddRowStyle = workbook.createCellStyle();
		XSSFColor white =new XSSFColor(new java.awt.Color(255,255,255));
		oddRowStyle.setFillForegroundColor(white);
        oddRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        oddRowStyle.setBorderLeft(BorderStyle.THIN);
        oddRowStyle.setBorderTop(BorderStyle.THIN);
        oddRowStyle.setBorderRight(BorderStyle.THIN);
        oddRowStyle.setBorderBottom(BorderStyle.THIN);
        
		evenRowStyle = workbook.createCellStyle();
		XSSFColor grey =new XSSFColor(new java.awt.Color(224,224,224));
        evenRowStyle.setFillForegroundColor(grey);
        evenRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        evenRowStyle.setBorderLeft(BorderStyle.THIN);
        evenRowStyle.setBorderTop(BorderStyle.THIN);
        evenRowStyle.setBorderRight(BorderStyle.THIN);
        evenRowStyle.setBorderBottom(BorderStyle.THIN);
        
        Font nosFont = workbook.createFont();
		nosFont.setColor(HSSFColor.RED.index);
		
		nosOddRowStyle = workbook.createCellStyle();
		nosOddRowStyle.setFillForegroundColor(white);
		nosOddRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		nosOddRowStyle.setBorderLeft(BorderStyle.THIN);
		nosOddRowStyle.setBorderTop(BorderStyle.THIN);
		nosOddRowStyle.setBorderRight(BorderStyle.THIN);
		nosOddRowStyle.setBorderBottom(BorderStyle.THIN);
		nosOddRowStyle.setFont(nosFont);
        
		nosEvenRowStyle = workbook.createCellStyle();
		nosEvenRowStyle.setFillForegroundColor(grey);
		nosEvenRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		nosEvenRowStyle.setBorderLeft(BorderStyle.THIN);
		nosEvenRowStyle.setBorderTop(BorderStyle.THIN);
		nosEvenRowStyle.setBorderRight(BorderStyle.THIN);
		nosEvenRowStyle.setBorderBottom(BorderStyle.THIN);
		nosEvenRowStyle.setFont(nosFont);
	}
	
	public static ExcelUtil getExcelUtil(XSSFWorkbook workbook) {
		if (excelUtil == null) {
			return new ExcelUtil(workbook);
		} else {
			return excelUtil;
		}
	}
	
	public void setHeaderStyle() {
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		sheet.getRow(0).setHeight((short) 500);		
        
        sheet.getRow(0).getCell(0).setCellStyle(headerStyle);
        sheet.getRow(0).getCell(1).setCellStyle(headerStyle);
        sheet.getRow(0).getCell(2).setCellStyle(headerStyle);
        sheet.getRow(0).getCell(3).setCellStyle(headerStyle);
	}
	
	public void setContentStyles(int rowNum, int setIndex) {
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		        
        if (setIndex % 2 == 0) {
        	sheet.getRow(rowNum).getCell(0).setCellStyle(evenRowStyle);
        	sheet.getRow(rowNum).getCell(1).setCellStyle(evenRowStyle);
        	sheet.getRow(rowNum).getCell(2).setCellStyle(evenRowStyle);
        	sheet.getRow(rowNum).getCell(3).setCellStyle(evenRowStyle);
        } else {
        	sheet.getRow(rowNum).getCell(0).setCellStyle(oddRowStyle);
        	sheet.getRow(rowNum).getCell(1).setCellStyle(oddRowStyle);
        	sheet.getRow(rowNum).getCell(2).setCellStyle(oddRowStyle);
        	sheet.getRow(rowNum).getCell(3).setCellStyle(oddRowStyle);
        }
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
	}
	
	public void setNosStyle(Cell nosCell, int setIndex) {
		
		if (setIndex % 2 == 0) {
			nosCell.setCellStyle(nosEvenRowStyle);
		} else {
			nosCell.setCellStyle(nosOddRowStyle);
		}
	}
}
