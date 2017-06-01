package com.schedule.generator.util;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

public class StyleUtil {	
	private HSSFFont boldFont;
	private HSSFFont remarkFont;
	private HSSFWorkbook wb;
	private HSSFCellStyle boldStyle;
	private HSSFCellStyle smallFontStyle;	
	private HSSFCellStyle boldULStyle;
	private HSSFCellStyle leftBorderStyle;
	private HSSFCellStyle rightBorderStyle;
	private HSSFCellStyle topBorderStyle;
	private HSSFCellStyle leftTopCornerBorderStyle;
	private HSSFCellStyle leftBottomCornerBorderStyle;
	private HSSFCellStyle topRightCornerBorderStyle;
	private HSSFCellStyle bottomBorderStyle;
	private HSSFCellStyle rightBottomCornerBorderStyle;
	private HSSFCellStyle remarkCellStyle;
	private HSSFCellStyle redStyle;
	
	private HSSFCellStyle remarkCellStyleNoBorder;
	
	@SuppressWarnings("deprecation")
	public StyleUtil(HSSFWorkbook wb) {
		this.wb = wb;
		boldFont = wb.createFont();
		boldFont.setFontName("Arial");
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		remarkFont = wb.createFont();
		remarkFont.setFontHeightInPoints((short) 8);
		
		boldStyle = wb.createCellStyle();
		boldStyle.setFont(boldFont);
		
		smallFontStyle = wb.createCellStyle();
		HSSFFont smallFont = wb.createFont();
		smallFont.setFontName("Arial");
		smallFont.setColor(HSSFFont.COLOR_RED);
		smallFont.setFontHeightInPoints((short) 9);
		smallFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		smallFontStyle.setFont(smallFont);
		
		boldULStyle = wb.createCellStyle();
		HSSFFont boldULFont = wb.createFont();
		boldULFont.setFontName("Arial");
		boldULFont.setFontHeightInPoints((short) 14);
		boldULFont.setUnderline(HSSFFont.U_SINGLE);
		boldULFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldULStyle.setFont(boldULFont);
		boldULStyle.setBorderTop(CellStyle.BORDER_THICK);
		boldULStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		
		leftBorderStyle = wb.createCellStyle();
		leftBorderStyle.setBorderLeft(CellStyle.BORDER_THICK);
		leftBorderStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		leftBorderStyle.setFont(boldFont);
		
		rightBorderStyle = wb.createCellStyle();
		rightBorderStyle.setBorderRight(CellStyle.BORDER_THICK);
		rightBorderStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		rightBorderStyle.setFont(boldFont);
		
		topBorderStyle = wb.createCellStyle();
		topBorderStyle.setBorderTop(CellStyle.BORDER_THICK);
		topBorderStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		topBorderStyle.setFont(boldFont);
		
		leftTopCornerBorderStyle = wb.createCellStyle();
		leftTopCornerBorderStyle.setBorderLeft(CellStyle.BORDER_THICK);
		leftTopCornerBorderStyle.setBorderLeft(CellStyle.BORDER_THICK);
		leftTopCornerBorderStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		leftTopCornerBorderStyle.setBorderTop(CellStyle.BORDER_THICK);
		leftTopCornerBorderStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		leftTopCornerBorderStyle.setFont(boldFont);
		
		leftBottomCornerBorderStyle = wb.createCellStyle();
		leftBottomCornerBorderStyle.setBorderLeft(CellStyle.BORDER_THICK);
		leftBottomCornerBorderStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		leftBottomCornerBorderStyle.setBorderBottom(CellStyle.BORDER_THICK);
		leftBottomCornerBorderStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		leftBottomCornerBorderStyle.setFont(boldFont);
		
		topRightCornerBorderStyle = wb.createCellStyle();
		topRightCornerBorderStyle.setBorderRight(CellStyle.BORDER_THICK);
		topRightCornerBorderStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		topRightCornerBorderStyle.setBorderTop(CellStyle.BORDER_THICK);
		topRightCornerBorderStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		topRightCornerBorderStyle.setFont(boldFont);
		
		bottomBorderStyle = wb.createCellStyle();
		bottomBorderStyle.setBorderBottom(CellStyle.BORDER_THICK);
		bottomBorderStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		bottomBorderStyle.setFont(boldFont);
		
		rightBottomCornerBorderStyle = wb.createCellStyle();
		rightBottomCornerBorderStyle.setBorderRight(CellStyle.BORDER_THICK);
		rightBottomCornerBorderStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		rightBottomCornerBorderStyle.setBorderBottom(CellStyle.BORDER_THICK);
		rightBottomCornerBorderStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		rightBottomCornerBorderStyle.setFont(boldFont);
		
		remarkCellStyle = wb.createCellStyle();
		remarkCellStyle.setBorderLeft(CellStyle.BORDER_THICK);
		remarkCellStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		remarkCellStyle.setFont(remarkFont);
		
		redStyle = wb.createCellStyle();
		HSSFFont boldFont = wb.createFont();
		boldFont.setFontName("Arial");
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldFont.setColor(HSSFColor.GREEN.index);
		redStyle.setFont(boldFont);
		redStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
		
		remarkCellStyleNoBorder = wb.createCellStyle();
		remarkCellStyleNoBorder.setFont(remarkFont);
		
	}
		
	public HSSFCellStyle getBoldStyle(){
		return boldStyle;
	}
	
	public HSSFCellStyle getSmallFontStyle(){		
		return smallFontStyle;
	}	
	
	public HSSFCellStyle getBoldUnderLineStyle(){		
		return boldULStyle;
	}
	public HSSFCellStyle getLeftBorderStyle(){
		return leftBorderStyle;
	}	
	
	public HSSFCellStyle getRightBorderStyle(){
		return rightBorderStyle;
	}
	
	public HSSFCellStyle getTopBorderStyle(){
		return topBorderStyle;
	}
	
	public HSSFCellStyle getLeftTopCornerBorderStyle(){
		return leftTopCornerBorderStyle;
	}
	
	public HSSFCellStyle getLeftBottomCornerBorderStyle(){
		return leftBottomCornerBorderStyle;
	}
	
	public HSSFCellStyle getTopRightCornerBorderStyle(){
		return topRightCornerBorderStyle;
	}
	
	public HSSFCellStyle getBottomBorderStyle(){
		return bottomBorderStyle;
	}
	
	public HSSFCellStyle getRightBottomCornerBorderStyle(){
		return rightBottomCornerBorderStyle;
	}
	
	public HSSFCellStyle getRemarkCellStyle(){
		return remarkCellStyle;
	}
	
	public HSSFCellStyle getGreenCellStyle(){
		return redStyle;
	}

	public HSSFCellStyle getRemarkCellStyleNoBorder() {
		return remarkCellStyleNoBorder;
	}

}
