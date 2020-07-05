package GA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


import tools.ExcelReader;
import tools.GetUsable;
import tools.clashDetect;


/*
 * 个体类，表示的是一个班级的选课方案
 */
public class ClassSolution {
	public ArrayList<byte[]> solution=new ArrayList<byte[]>();
	public ArrayList<byte[]> solution2=new ArrayList<byte[]>();
	HashMap<String,Float> need=new HashMap<String,Float>(); 
	ArrayList<Byte> usableTime=new ArrayList<Byte>();
	float solutionPoint;
	/*
	 * 利用随机数，读取选课方案中的要求，随机产生一张课表
	 */
	public void init() {
		/*
		 * 读取指导性教学计划的要求
		 */
        for(Iterator<String> it = ExcelReader.courseArrangeHashMap.keySet().iterator() ; it.hasNext();){
        	
        	String key = it.next().toString();
            need.put(key, ExcelReader.courseArrangeHashMap.get(key));
            
        }
        /*
         *	 读取未满足要求的大类，并从该种大类中选择随机选择一门，并且课程名不能相同，注意更新need哈希
         *	随机安排时间，不能冲突，随机安排教室，不能冲突
         */
        for(Iterator<String> it =need.keySet().iterator() ; it.hasNext();){
        	
            String key = it.next().toString();
            float value=need.get(key);
            while(value>0) {
            	System.out.println(key+"还需要"+value+"学分");
            	Random r=new Random();
            	byte courseType=ExcelReader.typeCourseHashMap.get(key);
            	
            	ArrayList<byte[]> toChoose=new ArrayList<byte[]>();
            	
            	for(int i=0;i<ExcelReader.allCourse.size();i++) {
            		
            		byte[] a=ExcelReader.allCourse.get(i);
            		/*
            		 * 找到类型相同且未被初始化的课程，并且未选过的课程对其进行初始化
            		 */
            		if(a[ExcelReader.TYPE_COL]==courseType) {
            			if(a[ExcelReader.CLASSROOM_COL]==-1&&!clashDetect.Detect(solution, a)) {
            				toChoose.add(a);
            			}
            		}
            	}
            	/*
            	 * 若是没有课程被初始化，则暂时不选择该课程
            	 */
            	
            	if(toChoose.size()==0) {
            		break;
            	}else {
            		/*
            		for(int i=0;i<toChoose.size();i++) {
            			for(int j=0;j<toChoose.get(i).length;j++) {
            				System.out.print(toChoose.get(i)[j]+"  ");
            			}
            			System.out.println();
            		}*/
            		
            		usableTime=GetUsable.getUsableTime(solution);
            		/*for(int i=0;i<usableTime.size();i++) {
            				System.out.print(usableTime.get(i)+"  ");
            		}*/
            		
            		int temp=r.nextInt(toChoose.size());
                	byte[] tempCourse=toChoose.get(temp);
                	System.out.println("随机到的课程数字为"+tempCourse[ExcelReader.NAME_COL]);
                	/*
                	 * 更新该课程的课余量
                	 */
                	System.out.println("该课程课余量为"+tempCourse[ExcelReader.VOLUME_COL]);
                	tempCourse[ExcelReader.VOLUME_COL]=(byte) (tempCourse[ExcelReader.VOLUME_COL]-60);
                	
                	byte a=tempCourse[ExcelReader.NAME_COL];
                	
                	String name=ExcelReader.courseNameHashMap.get(a);
                	System.out.println("随机到的课程名为"+name);
                	
                	float newKey=ExcelReader.coursePointHashMap.get(name);
                	System.out.println("所选择的课程学分为"+newKey);
                	/*
                	 * 更新选课计划所需要的学分
                	 */
                	value=value-newKey;
                	need.put(key, value);
                	value=need.get(key);
                	System.out.println("更新之后"+key+"需要 "+value+"学分");
                	
                	int usableNum=usableTime.size();
                	/*
                	 * 得到前后八周分解线
                	 */
                	byte half=0;
                	for(;half<usableNum;half++) {
                		if(usableTime.get(half)>20)
                			break;
                	}
                	System.out.println("前后八周的分界线为"+half);
                	/*
                	 * 得到教室类型
                	 */
            		String type;
            		
            		if(tempCourse[ExcelReader.CLASSROOMTYPE_COL]==1) {
            			type="机房";
            		}else {
            			type="普通";
            		}
            		/*
            		 * 根据学时分配时间,分为12，24，48三种
            		 */
                	byte learntime=tempCourse[ExcelReader.LEARNTIME_COL];
            		System.out.println("该课程学时为"+learntime);
            		switch(learntime) {
                	case 12:
                		ArrayList<Byte> usableClassroom12=new ArrayList<Byte>();
                		do {
                		int weektimeIndex=r.nextInt(usableNum);
                		System.out.println("可用时间有"+usableNum+"个");
                		int weektime=usableTime.get(weektimeIndex);
                		System.out.println("随机到的时间为"+weektime);
                		byte terms;
                		if(weektime>20) {
                			tempCourse[ExcelReader.WEEKTIME1_COL]=(byte)(weektime-20);
                			terms=1;
                		}else {
                			tempCourse[ExcelReader.WEEKTIME1_COL]=(byte)(weektime);
                    		terms=0;
                		}
                		tempCourse[ExcelReader.TERM_COL]=terms;
                		
                		/*
                		 * 分配教室，异常情况为该时间没有可用的教室
                		 */
                		usableClassroom12=GetUsable.getUsableClassroom(weektime, terms, type);
                		int classroomNum=r.nextInt(usableClassroom12.size());
                		byte num12=usableClassroom12.get(classroomNum);
                		tempCourse[ExcelReader.CLASSROOM_COL]=num12;
                		}while(usableClassroom12.size()==0);
                		break;
                	case 24:
                		int ok=0;
                		int noAnswer=0;
                  		int judge=r.nextInt(2);
                  		System.out.println("随机到"+judge+"八周");
                  		/*
            			 * 极限情况可能会出错，前八周或者后八周都没时间了
            			 * @报错
            			 */
                		do {
                			if(judge==0) {
                				if(half==0||half==1) {
                					judge=1;
                					if(noAnswer==1) {
                						return;
                					}
                					noAnswer=1;
                					continue;
                				}
                				ArrayList<Byte> usableClassroomtemp=new ArrayList<Byte>();
                			/*
                			 * 先分配时间，再找该时间段可用的教室，如果没有，则重新分配
                			 */
                				do {
                					int weektimeIndex1=r.nextInt(half);
                					int weektimeIndex2=(weektimeIndex1+4)%half;
                					int weektime1=usableTime.get(weektimeIndex1);
                					int weektime2=usableTime.get(weektimeIndex2);
                					System.out.println("前半周两节课的时间索引随机到"+weektimeIndex1+weektimeIndex2);
                					tempCourse[ExcelReader.WEEKTIME1_COL]=(byte)(weektime1);
                					tempCourse[ExcelReader.WEEKTIME2_COL]=(byte)(weektime2);
                					tempCourse[ExcelReader.TERM_COL]=0;
                					System.out.println("前半周两节课的时间分别为"+weektime1+weektime2);
                					ArrayList<Byte> usableClassroom24=GetUsable.getUsableClassroom(weektime1, judge, type);
                					ArrayList<Byte> usableClassroom241=GetUsable.getUsableClassroom(weektime2, judge, type);
                					/*
                					 * 找到哪些教室两个时间段都可以上课
                					 */
                					for(int i=0;i<usableClassroom24.size();i++) {
                						for(byte j=0;j<usableClassroom241.size();j++) {
                							if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                								usableClassroomtemp.add(j);
                							}
                						}
                					}
                					
                				}while(usableClassroomtemp.size()==0);
                				
                				int classroomNum24=r.nextInt(usableClassroomtemp.size());
                				byte num24=usableClassroomtemp.get(classroomNum24);
                				tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                				ok=1;
                			}else {
                				if(half==usableNum-1) {
                					judge=0;
                					if(noAnswer==1) {
                						return;
                					}
                					noAnswer=1;
                					continue;
                				}
                				
                			ArrayList<Byte> usableClassroomtemp=new ArrayList<Byte>();
                			do {
                			int weektimeIndex1=r.nextInt(usableNum-half)+half;
                    		int weektimeIndex2=(weektimeIndex1+4)%(usableNum-half)+half;
                    		int weektime1=usableTime.get(weektimeIndex1);
                    		int weektime2=usableTime.get(weektimeIndex2);
                    		tempCourse[ExcelReader.WEEKTIME1_COL]=(byte)(weektime1-20);
                    		tempCourse[ExcelReader.WEEKTIME2_COL]=(byte)(weektime2-20);
                    		tempCourse[ExcelReader.TERM_COL]=1;
                    		System.out.println("后半周两节课的时间分别为"+weektime1+weektime2);
                    		
                    		ArrayList<Byte> usableClassroom24=GetUsable.getUsableClassroom(weektime1, judge, type);
                    		ArrayList<Byte> usableClassroom241=GetUsable.getUsableClassroom(weektime2, judge, type);
                    		
                    		for(int i=0;i<usableClassroom24.size();i++) {
                    			for(byte j=0;j<usableClassroom241.size();j++) {
                    				if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                    					usableClassroomtemp.add(j);
                    				}
                    			}
                    		}
                			}while(usableClassroomtemp.size()==0);
                			
                			
                    		int classroomNum24=r.nextInt(usableClassroomtemp.size());
                    		byte num24=usableClassroomtemp.get(classroomNum24);
                    		tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                    		ok=1;
                		}
                	}while(ok==0);
                		break;
                	case 48:
                		/*
                		 * 对可用时间再次过滤，找到哪些时间前后八周都可以
                		 */
                		ArrayList<Byte> usable2=new ArrayList<Byte>(); 
                		for(int i=0;i<half;i++) {
                			//System.out.println(usableTime.get(i));
                			if(usableTime.indexOf((byte)(usableTime.get(i)+20))!=-1) {
                				usable2.add(usableTime.get(i));
                			}
                		}
                		//System.out.println("48可用时间为"+usable2.size());
                		if(usable2.size()<2) {
                			return;
                		}else {
                			ArrayList<Byte> usableClassroomtemp=new ArrayList<Byte>();
                			do {
                			int weektimeIndex3=r.nextInt(usable2.size());
                			int weektimeIndex4=(weektimeIndex3+4)%usable2.size();
                			int weektime3=usableTime.get(weektimeIndex3);
                			int weektime4=usableTime.get(weektimeIndex4);
                			tempCourse[ExcelReader.WEEKTIME1_COL]=(byte)(weektime3);
                    		tempCourse[ExcelReader.WEEKTIME2_COL]=(byte)(weektime4);
                    		tempCourse[ExcelReader.TERM_COL]=2;
                    		
                    		ArrayList<Byte> usableClassroom24=GetUsable.getUsableClassroom(weektime3, 2, type);
                    		ArrayList<Byte> usableClassroom241=GetUsable.getUsableClassroom(weektime4, 2, type);
                    		
                    		for(int i=0;i<usableClassroom24.size();i++) {
                    			for(byte j=0;j<usableClassroom241.size();j++) {
                    				if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                    					usableClassroomtemp.add(j);
                    				}
                    			}
                    		}
                			}while(usableClassroomtemp.size()==0);
                    		int classroomNum24=r.nextInt(usableClassroomtemp.size());
                    		byte num24=usableClassroomtemp.get(classroomNum24);
                    		tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                		}
                		default:
                		break;
            		}	
            		/*System.out.println("初始化的课程信息为");
            		for(int i=0;i<tempCourse.length;i++) {
            			System.out.print(tempCourse[i]+" ");
            		}
            		System.out.println();
            		*/
            		solution.add(tempCourse);
            }
            }
        }
            		
       
        
	}
}
	
