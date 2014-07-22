package com.mtools.core.plugin.helper;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class ExcelTool {
	protected  final static Log log = LogFactory.getLog(ExcelTool.class);
	
	@SuppressWarnings("null")
//	public static List<DetecRecord> getDeteResult(InputStream in) throws IOException, ParseException{
//		POIFSFileSystem fs = new POIFSFileSystem(in);
//		HSSFWorkbook book = new HSSFWorkbook(fs);
//		HSSFSheet sheet = book.getSheetAt(0);
//		int rowNum = sheet.getLastRowNum();
//		DetecRecord record=null;
//		List<DetecRecord> records=new ArrayList<DetecRecord>();
//		String[] line = new String[5];
//		 
//		//从第1行开始
//		for(int t=1;t<=rowNum;t++){
//			
//			 record=new DetecRecord();
//			
//			HSSFRow row_sub = sheet.getRow(t);
//			if(row_sub==null){
//				log.error("第" + t + "行数据不能为空!");
//				break;
//			}
//		 /**
//		  *  卡号，阴阳值（0阴，1阳），结果描述，建议，检测日期
//		  */
//            for (int j = 0 ; j<=4 ;j++){
//					
//					
//					line[j] = getCellValue(row_sub.getCell(j)).trim()==null?"":getCellValue(row_sub.getCell(j)).trim();
//					line[j] =getRightStr(line[j]);
//            }
//            record.setCardnum(line[0]);
//            record.setDetecresult(line[1]);
//            record.setDetecdesc(line[2]);
//            record.setReserved(line[3]);
//            record.setDetectime(FuncUtil.paresTime(line[4]));
//            records.add(record);
//		}
//		return records;
//	}
	
	
	public static String getCellValue(HSSFCell cell) {
		String value = "";
		if (cell==null) 
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			double tp=Double.valueOf(cell.getNumericCellValue());
			value=String.format("%.2f", tp);
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}	
	
	public static String getCellValue2(HSSFCell cell) {
		String value = "";
		if (cell==null) 
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			double tp=Double.valueOf(cell.getNumericCellValue());
			value=String.format("%.0f", tp);
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}
	public static String getRightStr(String str){
		if(str==null){
			str="";
		}
		if(str.indexOf(".00")>0){
			return str.substring(0, str.indexOf(".00"));
		}
		return str;
	}
}
