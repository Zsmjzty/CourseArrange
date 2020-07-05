package tools;

import java.util.ArrayList;

public class GetUsable {
	/*
	 * 从该班级课表中得到哪些时间片还未被分配
	 */
	public static ArrayList<Byte> getUsableTime(ArrayList<byte[]> solution){
		ArrayList<Byte> time=new ArrayList<Byte>();
		byte[] temp=new byte[40];
		int col1;
		int col2;
		int term;
		for(int i=0;i<solution.size();i++) {
			col1=solution.get(i)[ExcelReader.WEEKTIME1_COL];
			col2=solution.get(i)[ExcelReader.WEEKTIME2_COL];
			term=solution.get(i)[ExcelReader.TERM_COL];
			if(term==0) {
				if(col1!=-1) {
					temp[col1-1]=1;
				}
				if(col2!=-1) {
					temp[col2-1]=1;
				}
			}else if(term==1){
				if(col1!=-1) {
					temp[col1-1+20]=1;
				}
				if(col2!=-1) {
					temp[col2-1+20]=1;
				}
			}else if(term==2) {
				if(col1!=-1) {
					temp[col1-1]=1;
					temp[col1-1+20]=1;
				}
				if(col2!=-1) {
					temp[col2-1]=1;
					temp[col2-1+20]=1;
				}
				
			}
		}
		for(byte i=0;i<40;i++) {
			if(temp[i]==0) {
				time.add((byte)(i+1));
			}
		}
		for(int i=0;i<time.size();i++) {
			System.out.print(time.get(i)+"  ");
		}
		System.out.println();
		return time;
	}
	/*
	 * 	根据初始化的课程，查看该时间段 还有哪些所需要类型的教室可用
	 */
	public static  ArrayList<Byte> getUsableClassroom(int time,int terms,String type){
		ArrayList<Byte> result=new ArrayList<Byte>();
		int allClassroom=ExcelReader.classroomHashMap.size();
		byte[] used=new byte[allClassroom];
			for(byte i=1;i<=allClassroom;i++) {
				/*
				 * 如果类型 对应，查看教室是否冲突
				 */
				if(ExcelReader.classroomHashMap.get(i).equals(type)) {
					if(clashDetect.DetectClassroom(time, terms, i)) {
						used[i-1]=1;
					}
				}else {
					used[i-1]=1;
				}
		}
		for(byte i=0;i<allClassroom;i++) {
			if(used[i]==0) {
				result.add((byte) (i+1));
			}
		}
		return result;
		
	}
}
