package tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
public class ExcelReader {
	public static byte NAME_COL=0;
	public static byte VOLUME_COL=1;
	public static byte TYPE_COL=2;
	public static byte CLASSROOMTYPE_COL=3;
	public static byte LEARNTIME_COL=4;
	public static byte WEEKTIME1_COL=5;
	public static byte WEEKTIME2_COL=6;
	public static byte CLASSROOM_COL=7;
	public static byte TERM_COL=8;
	public static int classNum;
	public static HashMap<Byte,String> courseNameHashMap=new HashMap<Byte,String>();
	public static HashMap<Byte,String> courseTypeHashMap=new HashMap<Byte,String>();
	public static HashMap<String,Byte> typeCourseHashMap=new HashMap<String,Byte>();
	public static HashMap<String,Float> coursePointHashMap=new HashMap<String,Float>();
	public static HashMap<String,Float> courseArrangeHashMap=new HashMap<String,Float>();
	public static HashMap<Byte,String> classroomHashMap=new HashMap<Byte,String>();
	public static ArrayList<byte[]> allCourse=new ArrayList<byte[]>();
	private static String TeacherInfo;
	private static String ClassroomInfo;
	private static String CourseInfo;
	private static String ArrangeInfo;
	public static void init() {
		   Properties params=new Properties();
		   String configFile="config.properties";
		   InputStream is =ExcelReader.class.getClassLoader().getResourceAsStream(configFile);
		   try {
			   params.load(is);
		   }catch(IOException e) {
			   e.printStackTrace();
		   }
		   TeacherInfo=params.getProperty("TeacherInfo");
		   ClassroomInfo=params.getProperty("ClassroomInfo");
		   CourseInfo=params.getProperty("CourseInfo");
		   ArrangeInfo=params.getProperty("ArrangeInfo");
		   classNum=Integer.parseInt(params.getProperty("classNum"));
	   }
	static {
		init();
	}
	/*将excel表格中的课程信息导入到本地并用7位byte 按顺序是课程名，剩余课容量，类型，所需要的教室类型,学时，周时间以及前后八周*/
	public static void loadCourse() throws EncryptedDocumentException, IOException {
		File xlsFile = new File(CourseInfo);
		Workbook workbook = WorkbookFactory.create(xlsFile);
		Sheet sheet = workbook.getSheetAt(0);
		int rowNumbers = sheet.getLastRowNum();
		Row temp = sheet.getRow(0);
		int cells = temp.getPhysicalNumberOfCells();
		for (byte row = 1; row <= rowNumbers; row++) {
			byte courseNums=0;
			byte[] b1=new byte[9];
			Row r = sheet.getRow(row);
			for (int col = 0; col < cells; col++) {
				switch(col) {
				/*
				 * 建立数字与课程名的哈希映射 从1开始
				 */
				case 0:
					String courseName=r.getCell(col).toString();
					courseNameHashMap.put(row, courseName);
					b1[col]=row;
					break;
				/*
				 * 得到课程容量 200人的为127
				 */
				case 1:
					String volume=r.getCell(col).toString();
					int volumeNums=(int)Float.parseFloat(volume);
					if(volumeNums>127) {
						b1[col]=127;
					}else {
						b1[col]=(byte)volumeNums;
					}
					break;
				/*
				 * 得到课程类型，写在基因中
				 */
				case 2:
					String type=r.getCell(col).toString();
					byte b=typeCourseHashMap.get(type);
					b1[col]=b;
					break;
				/*
				 * 得到备注信息，写在基因中
				 */
				case 3:
					if(r.getCell(col)!=null) {
					String extra=r.getCell(col).toString();
					if(extra.equals("机房")) {
						b1[col]=1;
					}
					
					}
					break;
				/*
				 * 得到学时，存储在基因中
				 */
				case 4:
					String courseTime=r.getCell(col).toString();
					byte courseTimeNums=(byte)Float.parseFloat(courseTime);
					b1[col]=courseTimeNums;
					break;				
				/*
				 * 得到学分，不用写在基因中，利用hashmap存储
				 */
				case 5:
					String point=r.getCell(col).toString();
					Float pointNums=Float.parseFloat(point);
					String name=ExcelReader.courseNameHashMap.get(row);
					coursePointHashMap.put(name, pointNums);
					break;
				/*
				* 得到学期信息,暂时不用
				*/
				case 6:
					String terms=r.getCell(col).toString();
					break;
				/*
				 * 开设门数，不用写在基因中，
				 */
				case 7:
					String course=r.getCell(col).toString();
					courseNums=(byte)Float.parseFloat(course);
					break;
				default:
					break;
			}
			/*
			 * 第五位存储前半周时间 第六位后半周时间,第七位位表示教室，第八位表示是否是整学期的;
			 */
				b1[5]=-1;
				b1[6]=-1;
				b1[7]=-1;
				b1[8]=-1;
			for(int i=0;i<courseNums;i++) {
				byte[] a=new byte[9];
				for(int k=0;k<9;k++) {
					a[k]=b1[k];
				}
				allCourse.add(a);
			}
			}
		}
	
	}
	/*
	 * 将excel表格中的数据读取到本地，需要知道类型，编号，容量 类型1比特 1机房 0普通，//五位编号32个教室以内，八位容量，256位
	以内，一共十四位，二维数组 保存
	*/
	public static void loadClassroom() throws EncryptedDocumentException, IOException {
		File file=new File(ClassroomInfo);
		Workbook workbook=WorkbookFactory.create(file);
		Sheet sheet=workbook.getSheetAt(0);
		int rowNums=sheet.getLastRowNum();
		Row temp = sheet.getRow(0);
		int cells = temp.getPhysicalNumberOfCells();
		for(int row=1;row<=rowNums;row++) {
			Row rowContent=sheet.getRow(row);
			byte index=0;
			String type=null;
			for(int col=0;col<cells;col++) {
				if(col==0) {
					String volume=rowContent.getCell(col).toString();
					index=(byte)Float.parseFloat(volume);
				}else if(col==1){
					type=rowContent.getCell(col).toString();
				}
			}
			classroomHashMap.put(index,type);
		}
	}
	
	public static void loadArrangeInfo() throws EncryptedDocumentException, IOException{
		File file=new File(ArrangeInfo);
		Workbook workbook=WorkbookFactory.create(file);
		Sheet sheet=workbook.getSheetAt(0);
		int rowNum=sheet.getLastRowNum();
		Row row=sheet.getRow(0);
		String courseName=null;
		float point=0;
		int cellsNum=row.getPhysicalNumberOfCells();
		for(byte rowIndex=1;rowIndex<=rowNum;rowIndex++) {
			row=sheet.getRow(rowIndex);
			for(int col=0;col<cellsNum;col++) {
				switch(col) {
				case 0:
					courseName=row.getCell(col).toString();
					break;
				case 1:
					point=Float.parseFloat(row.getCell(col).toString());
					break;
				default:
					break;
				}
			}
			courseTypeHashMap.put(rowIndex,courseName);
			typeCourseHashMap.put(courseName,rowIndex);
			courseArrangeHashMap.put(courseName,point);
		}
	}
}
