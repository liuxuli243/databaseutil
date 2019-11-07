/**
 * ExportExcel.java
 * com.linyun.airline.admin.customneeds.util
 * Copyright (c) 2018, 北京同方股份有限公司版权所有.
*/

package com.thtf.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出Excel
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   刘旭利
 * @Date	 2016年12月1日 	 
 */
public class ExcelExport {

	//显示的导出表的标题  
	private String title;
	//导出表的列名  
	private List<String> rowName = new ArrayList<String>();
	//内容
	private List<List<String>> dataList = new ArrayList<List<String>>();
	
	private String fileName;

	private HttpServletResponse response;


	/**
	 * 
	 * @param title excel表格标题
	 * @param rowName 列标题
	 * @param dataList	数据（从第三行开始）
	 * @param fileName	导出的文件名
	 * @param response
	 */
	public ExcelExport(String title, List<String> rowName,
			List<List<String>> dataList, String fileName,
			HttpServletResponse response) {
		super();
		this.title = title;
		this.rowName = rowName;
		this.dataList = dataList;
		this.fileName = fileName;
		this.response = response;
	}

	/* 
	 * 导出数据 
	 * */
	public void export() throws Exception {
		try {
			SXSSFWorkbook workbook = new SXSSFWorkbook(); // 创建工作簿对象  
			Sheet sheet = workbook.createSheet(title); // 创建工作表  

			// 产生表格标题行  
			Row rowm = sheet.createRow(0);
			Cell cellTiltle = rowm.createCell(0);

			//sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
			//CellStyle columnTopStyle = this.getStyle(workbook);//获取列头样式对象  
			CellStyle style = this.getStyle(workbook); //单元格样式对象  

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.size() - 1)));
			cellTiltle.setCellStyle(getTitleStyle(workbook));
			cellTiltle.setCellValue(title);

			// 定义所需列数  
			int columnNum = rowName.size();
			Row rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)  
			//HSSFRow rowRowName = sheet.createRow(0); // 在索引2的位置创建行(最顶端的行开始的第二行)  

			// 将列头设置到sheet的单元格中  
			for (int n = 0; n < columnNum; n++) {
				Cell cellRowName = rowRowName.createCell(n); //创建列头对应个数的单元格  
				cellRowName.setCellType(Cell.CELL_TYPE_STRING); //设置列头单元格的数据类型  
				RichTextString text = new HSSFRichTextString(rowName.get(n));
				cellRowName.setCellValue(text); //设置列头单元格的值  
				cellRowName.setCellStyle(getColumnStyle(workbook)); //设置列头单元格样式  
			}

			//将查询出的数据设置到sheet对应的单元格中  
			for (int i = 0; i < dataList.size(); i++) {

				List<String> obj = dataList.get(i);//遍历每个对象  
				Row row = sheet.createRow(i + 3);//创建所需的行数  
//				HSSFRow row = sheet.createRow(i + 1);//创建所需的行数  

				for (int j = 0; j < obj.size(); j++) {
					Cell cell = null; //设置单元格的数据类型  
					//注释掉的是第一列为序号
					/*if (j == 0) {
						cell = row.createCell(j, Cell.CELL_TYPE_NUMERIC);
						cell.setCellValue(i + 1);
					} else {*/
					cell = row.createCell(j, Cell.CELL_TYPE_STRING);
					if (!"".equals(obj.get(j)) && obj.get(j) != null) {
						cell.setCellValue(obj.get(j).toString()); //设置单元格的值  
					}
					//}
					cell.setCellStyle(style); //设置单元格样式  
				}
			}
			//让列宽随着导出的列长自动适应  
			sheet.setDefaultColumnWidth(17);
			//widthAdaptive(sheet, columnNum);

			if (workbook != null) {
				OutputStream out = null;
				try {
					response.reset();
					//String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
					//String headStr = "attachment; filename=\"" + fileName + "\"";
					response.setContentType("APPLICATION/OCTET-STREAM");
					//response.setHeader("Content-Disposition", headStr);
					String nfileName=URLEncoder.encode(fileName,"UTF-8");
			        String cd="attachment; filename="+nfileName+";filename*=UTF-8''"+nfileName;
					response.addHeader("Content-Disposition",cd);
					out = response.getOutputStream();
					workbook.write(out);
				} catch (IOException e) {

				} finally {
					out.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 设置Excel表格列宽度为自适应
	 * <p>
	 * 设置Excel表格列宽度为自适应
	 *
	 * @param sheet ：对应的Excel表格对象
	 * @param columnNum 列数
	 */
	public void widthAdaptive(Sheet sheet, int columnNum) {
		//让列宽随着导出的列长自动适应  
		for (int colNum = 0; colNum < columnNum; colNum++) {
			int columnWidth = sheet.getColumnWidth(colNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				Row currentRow;
				//当前行未被使用过  
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				} else {
					currentRow = sheet.getRow(rowNum);
				}
				if (currentRow.getCell(colNum) != null) {
					Cell currentCell = currentRow.getCell(colNum);
					if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
						int length = currentCell.getStringCellValue().getBytes().length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			if (colNum == 0) {
				sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
			} else {
				sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
			}
		}
	}

	/*   
	 * 列数据信息单元格样式 
	 */
	public CellStyle getStyle(SXSSFWorkbook workbook) {
		// 设置字体  
		//HSSFFont font = workbook.createFont();
		//设置字体大小  
		//font.setFontHeightInPoints((short)10);  
		//字体加粗  
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
		//设置字体名字   
		//font.setFontName("Courier New");
		//设置样式;   
		CellStyle style = workbook.createCellStyle();
		/*//设置底边框;   
		style.setBorderBottom(CellStyle.BORDER_THIN);
		//设置底边框颜色;    
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		//设置左边框;     
		style.setBorderLeft(CellStyle.BORDER_THIN);
		//设置左边框颜色;   
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		//设置右边框;   
		style.setBorderRight(CellStyle.BORDER_THIN);
		//设置右边框颜色;   
		style.setRightBorderColor(HSSFColor.BLACK.index);
		//设置顶边框;   
		style.setBorderTop(CellStyle.BORDER_THIN);
		//设置顶边框颜色;    
		style.setTopBorderColor(HSSFColor.BLACK.index);
		//在样式用应用设置的字体;    
		style.setFont(font);
		//设置自动换行;   
		style.setWrapText(false);*/
		//设置水平对齐的样式为居中对齐;    
		style.setAlignment(CellStyle.ALIGN_CENTER);
		//设置垂直对齐的样式为居中对齐;   
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

		return style;
	}
	
	/**
	 * 设置列头单元格的背景色
	 */
	public CellStyle getColumnStyle(SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = workbook.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		return style;
	}
	/**
	 * 设置标题样式
	 */
	public CellStyle getTitleStyle(SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = workbook.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		return style;
	}
}
