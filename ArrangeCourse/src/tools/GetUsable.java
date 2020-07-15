package tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GetUsable {
	/*
	 * 从该班级课表中得到哪些时间片还未被分配
	 */
	public static ArrayList<Integer> getUsableTime(ArrayList<int[]> solution){
		ArrayList<Integer> time=new ArrayList<Integer>();
		int[] temp=new int[40];
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
		for(int i=0;i<40;i++) {
			if(temp[i]==0) {
				time.add(i+1);
			}
		}
		/*
		 * for(int i=0;i<time.size();i++) {
			System.out.print(time.get(i)+"  ");
		}
		System.out.println();
		*/
		return time;
	}
	/*
	 * 	根据初始化的课程，查看该时间段 还有哪些所需要类型的教室可用
	 */
	public static  ArrayList<Integer> getUsableClassroom(ArrayList<int[]> s,int time,int terms,String type){
		ArrayList<Integer> result=new ArrayList<Integer>();
		int allClassroom=ExcelReader.classroomHashMap.size();
		int[] used=new int[allClassroom];
			for(int i=1;i<=allClassroom;i++) {
				/*
				 * 如果类型 对应，查看教室是否冲突
				 */
				if(ExcelReader.classroomHashMap.get(i).equals(type)) {
					if(clashDetect.DetectClassroom(s,time, terms, i)) {
						used[i-1]=1;
					}
				}else {
					used[i-1]=1;
				}
		}
		for(int i=0;i<allClassroom;i++) {
			if(used[i]==0) {
				result.add(i+1);
			}
		}
		return result;
	}
	
	public static ArrayList<double[]> refreshPoint(ArrayList<double[]> newPoint) {
		double sum=0;
		for(int i=0;i<newPoint.size();i++) {
			sum+=newPoint.get(i)[1];
		}
		for(int i=0;i<newPoint.size();i++) {
			newPoint.get(i)[1]/=sum;
		}
		
		ArrayList<double[]> answer=new ArrayList<double[]>();
		for(int i=0;i<newPoint.size();i++) {
			double[] temp=new double[2];
			temp[0]=newPoint.get(i)[0];
			if(i==0) {
				temp[1]=newPoint.get(i)[1];
				answer.add(temp);
				continue;
			}else {
				temp[1]=answer.get(i-1)[1]+newPoint.get(i)[1];
				answer.add(temp);
			}
		}
		return answer;
	}
	
	public static int getUsableTeacher(ArrayList<int[]> teacher,int time1,int time2,int term,String type){
		Random r=new Random();
		ArrayList<Integer> correctPro=new ArrayList<Integer>();
		for(Iterator<Integer> proIter = ExcelReader.teacherProHashMap.keySet().iterator() ; proIter.hasNext();){
			int teacherNum  = proIter.next();
			if(ExcelReader.teacherProHashMap.get(teacherNum).equals(type)) {
				correctPro.add(teacherNum);
			}
		}
		
		if(correctPro.size()==0) {
			return -1;
		}
		int temp=0;
		if(term==1||term==0) {
			do {
				int i=r.nextInt(correctPro.size());
				int teacherIndex=correctPro.get(i);
				if(teacher.get(teacherIndex-1)[time1-1+term*20]==0) {
					if(time2!=-1) {
						if(teacher.get(teacherIndex-1)[time2-1+term*20]==0) {
							return teacherIndex;
						}
					}else {
						return teacherIndex;
					}
				}
				temp++;
			}while(temp<=40);
		}else if(term==2) {
			do {
				int i=r.nextInt(correctPro.size());
				
				int teacherIndex=correctPro.get(i);
				if(teacher.get(teacherIndex-1)[time1-1+20]==0&&teacher.get(teacherIndex-1)[time2-1+20]==0) {
						if(teacher.get(teacherIndex-1)[time1-1]==0&&teacher.get(teacherIndex-1)[time2-1]==0) {
							return teacherIndex;
						}
				}
				temp++;
			}while(temp<=40);
		}
		return -1;
	}
}
