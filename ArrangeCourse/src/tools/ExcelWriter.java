package tools;

import java.io.File;
import java.io.FileOutputStream;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import GA.ClassSolution;

public class ExcelWriter {
	public static void outputSolution(ClassSolution cs) {
		ClassSolution head=cs;
		int classNo=1;
		HSSFWorkbook wb=new HSSFWorkbook();
		do {
			HSSFSheet sheet =wb.createSheet("班级"+String.valueOf(classNo));
			sheet.setDefaultColumnWidth(200*256);
			sheet.setDefaultRowHeightInPoints(200*20);
			HSSFCellStyle cellStyle=wb.createCellStyle();       
			cellStyle.setWrapText(true);       
			cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
			/*
			 * 第一行，显示班级
			 */
			HSSFRow row0=sheet.createRow(0);
			HSSFCell cell0=row0.createCell(0);
			cell0.setCellValue("班级"+String.valueOf(classNo)+"的前八周推荐课表");
			cell0.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
			cell0.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			
			HSSFCell cell2=row0.createCell(10);
			cell2.setCellValue("班级"+String.valueOf(classNo)+"的后八周推荐课表");
			cell2.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
			cell2.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
			sheet.addMergedRegion(new CellRangeAddress(0,0,10,15));
			/*
			 * 第二行，显示星期
			 */
			HSSFRow row1=sheet.createRow(1);
			HSSFCell cell10=row1.createCell(0);
			HSSFCell cell110=row1.createCell(10);
			cell10.setCellValue("时间  星期");
			cell110.setCellValue("时间  星期");
			cell10.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
			cell10.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			cell110.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
			cell110.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			for(int colIndex=1;colIndex<=5;colIndex++) {
				HSSFCell c=row1.createCell(colIndex);
				c.setCellValue("星期"+String.valueOf(colIndex));
				c.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
				c.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			}
			for(int colIndex=11;colIndex<16;colIndex++) {
				HSSFCell c=row1.createCell(colIndex);
				c.setCellValue("星期"+String.valueOf(colIndex-10));
				c.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
				c.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
			}
			 HSSFPatriarch patriarch = sheet.createDrawingPatriarch();  
		     HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 1023, 255, (short)0, 1, (short)0, 1);  
		     HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);  
		     shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);   
		     shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID) ; 
		     
		     HSSFClientAnchor b = new HSSFClientAnchor(0, 0, 1023, 255, (short)10, 1, (short)10, 1);  
		     shape1 = patriarch.createSimpleShape(b);  
		     shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);   
		     shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID) ; 
		     

			for(int rowNum=2;rowNum<=5;rowNum++) {
				HSSFRow row=sheet.createRow(rowNum);
				HSSFCell c0=row.createCell(0);
				HSSFCell c10=row.createCell(10);
				c0.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
				c0.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
				c10.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
				c10.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
				switch(rowNum) {
				case 2:
					c0.setCellValue("8:00~9:30");
					c10.setCellValue("8:00~9:30");
					break;
				case 3:
					c0.setCellValue("9:50~11:25");
					c10.setCellValue("9:50~11:25");
					break;
				case 4:
					c0.setCellValue("13:30~15:05");
					c10.setCellValue("13:30~15:05");
					break;
				case 5:
					c0.setCellValue("15:10~16:45");
					c10.setCellValue("15:10~16:45");
					break;
				default:
					break;
				}
				String courseName;
				String teacherName;
				int classroomNo;
				for(int colIndex=1;colIndex<6;colIndex++) {
					HSSFCell cell=row.createCell(colIndex);
					cell.setCellStyle(cellStyle);
						for(int i=0;i<head.solution.size();i++) {
							int[] temp=head.solution.get(i);
							if(temp[ExcelReader.TERM_COL]==0) {
								if(temp[ExcelReader.WEEKTIME1_COL]==((colIndex-1)*4+rowNum-1)||temp[ExcelReader.WEEKTIME2_COL]==((colIndex-1)*4+rowNum-1)) {
								courseName=ExcelReader.courseNameHashMap.get(temp[ExcelReader.NAME_COL]);
								teacherName=ExcelReader.teacherNameHashMap.get(temp[ExcelReader.TEACHER_COL]);
								classroomNo=temp[ExcelReader.CLASSROOM_COL]+1;
								cell.setCellValue(new HSSFRichTextString(courseName+"\r\n"+teacherName+"\r\n"+String.valueOf(classroomNo)));
								break;
								}
							}else if(temp[ExcelReader.TERM_COL]==2){
								if(temp[ExcelReader.WEEKTIME1_COL]==((colIndex-1)*4+rowNum-1)||temp[ExcelReader.WEEKTIME2_COL]==((colIndex-1)*4+rowNum-1)){
									courseName=ExcelReader.courseNameHashMap.get(temp[ExcelReader.NAME_COL]);
									teacherName=ExcelReader.teacherNameHashMap.get(temp[ExcelReader.TEACHER_COL]);
									classroomNo=temp[ExcelReader.CLASSROOM_COL]+1;
									cell.setCellValue(new HSSFRichTextString(courseName+"\r\n"+teacherName+"\r\n"+String.valueOf(classroomNo)));
									break;
								}
							}
					}
				}
				
				for(int colIndex=11;colIndex<16;colIndex++) {
					HSSFCell cell=row.createCell(colIndex);
					cell.setCellStyle(cellStyle);
						for(int i=0;i<head.solution.size();i++) {
							int[] temp=head.solution.get(i);
							if(temp[ExcelReader.TERM_COL]==1) {
								if(temp[ExcelReader.WEEKTIME1_COL]==((colIndex-11)*4+rowNum-1)||temp[ExcelReader.WEEKTIME2_COL]==((colIndex-11)*4+rowNum-1)) {
								courseName=ExcelReader.courseNameHashMap.get(temp[ExcelReader.NAME_COL]);
								teacherName=ExcelReader.teacherNameHashMap.get(temp[ExcelReader.TEACHER_COL]);
								classroomNo=temp[ExcelReader.CLASSROOM_COL]+1;
								cell.setCellValue(new HSSFRichTextString(courseName+"\r\n"+teacherName+"\r\n"+String.valueOf(classroomNo)));
								break;
								}
							}else if(temp[ExcelReader.TERM_COL]==2){
								if(temp[ExcelReader.WEEKTIME1_COL]==((colIndex-11)*4+rowNum-1)||temp[ExcelReader.WEEKTIME2_COL]==((colIndex-11)*4+rowNum-1)){
									courseName=ExcelReader.courseNameHashMap.get(temp[ExcelReader.NAME_COL]);
									teacherName=ExcelReader.teacherNameHashMap.get(temp[ExcelReader.TEACHER_COL]);
									classroomNo=temp[ExcelReader.CLASSROOM_COL]+1;
									cell.setCellValue(new HSSFRichTextString(courseName+"\r\n"+teacherName+"\r\n"+String.valueOf(classroomNo)));
									break;
								}
							}
					}
				}
			}
		
			head=head.next;
			classNo++;
		}while(head!=null);
		
		try {
			FileOutputStream fos=new FileOutputStream(new File(ExcelReader.savePath)); 
			wb.write(fos);
			wb.close();
			fos.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("生成Excel文档失败");
		}
	}
	
}
