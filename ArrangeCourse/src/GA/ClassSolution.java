package GA;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class ClassSolution implements Serializable{
	public ArrayList<Integer> freeTeacher;
	public ArrayList<int[]> usableTeacher;
	public ArrayList<int[]> allCourse;
	public ArrayList<int[]> solution=new ArrayList<int[]>();
	HashMap<String,Float> need=new HashMap<String,Float>(); 
	ArrayList<Integer> usableTime=GetUsable.getUsableTime(solution);
	public ClassSolution next;
	public double point;
	/*
	 * 利用随机数，读取选课方案中的要求，随机产生一张课表
	 */
	public ClassSolution(ArrayList<int[]> allCourse,ArrayList<int[]> usableTeacher,ArrayList<Integer> freeTeacher){
		this.allCourse=allCourse;
		this.usableTeacher=usableTeacher;
		this.freeTeacher=freeTeacher;

	}

	public String init() {
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
            	int courseType=ExcelReader.typeCourseHashMap.get(key);
            	
            	ArrayList<int[]> toChoose=new ArrayList<int[]>();
            	
            	for(int i=0;i<allCourse.size();i++) {
            		
            		int[] a=allCourse.get(i);
            		/*
            		 * 找到类型相同,时间不冲突，没有选择过的，课容量充足的课程，添加入待随机名单中
            		 */
            		if(a[ExcelReader.TYPE_COL]==courseType) {
            			if(a[ExcelReader.VOLUME_COL]>=ExcelReader.studentsNum&&!clashDetect.Detect(solution, a)&&!clashDetect.hasChoosed(solution, a)) {
            				toChoose.add(a);
            			}
            		}
            	}
            	/*
            	 * 若是没有课程可供选择，则说明该方案课程设计不充足，显示哪一门课程设置出现了问题
            	 */
            	
            	if(toChoose.size()==0) {
            		System.out.println(key+"类课程总课容量设置过少");
            		return "失败";
            	}else {
            		usableTime=GetUsable.getUsableTime(solution);
            		/*
            		for(int i=0;i<toChoose.size();i++) {
            			for(int j=0;j<toChoose.get(i).length;j++) {
            				System.out.print(toChoose.get(i)[j]+"  ");
            			}
            			System.out.println();
            		}*/
            		
            		/*
            		 * 得到可用时间
            		 */
            		
            		
            		/*for(int i=0;i<usableTime.size();i++) {
            				System.out.print(usableTime.get(i)+"  ");
            		}*/
            		
            		int temp=r.nextInt(toChoose.size());
                	int[] tempCourse=toChoose.get(temp);
                	if(tempCourse[ExcelReader.CLASSROOM_COL]!=-1) {}
                	System.out.println("随机到的课程数字为"+tempCourse[ExcelReader.NAME_COL]);
                	/*
                	 * 更新该课程的课余量
                	 */
                	System.out.println("该课程课余量为"+tempCourse[ExcelReader.VOLUME_COL]);
                	tempCourse[ExcelReader.VOLUME_COL]=tempCourse[ExcelReader.VOLUME_COL]-60;
                	
                	int a=tempCourse[ExcelReader.NAME_COL];
                	
                	String name=ExcelReader.courseNameHashMap.get(a);
                	System.out.println("随机到的课程名为"+name);
                	float newKey=ExcelReader.coursePointHashMap.get(name);
                	System.out.println("所选择的课程学分为"+newKey);
                	value=value-newKey;
                	need.put(key, value);
                	value=need.get(key);
                	/*
                	 * 更新选课计划所需要的学分
                	 */
                	
                	System.out.println("更新之后"+key+"需要 "+value+"学分");
                	if(tempCourse[ExcelReader.CLASSROOM_COL]!=-1) {
             
                		solution.add(tempCourse);
                		continue;
                	}
                	int usableNum=usableTime.size();
                	if(usableNum==0) {
                		return "失败";
                	}
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
                	int learntime=tempCourse[ExcelReader.LEARNTIME_COL];
            		System.out.println("该课程学时为"+learntime);
            		switch(learntime) {
                	case 12:
                		ArrayList<Integer> usableClassroom12=new ArrayList<Integer>();
                		int weektime;
                		do {
                		int weektimeIndex=r.nextInt(usableNum);
                		System.out.println("可用时间有"+usableNum+"个");
                		weektime=usableTime.get(weektimeIndex);
                		System.out.println("随机到的时间为"+weektime);
                		byte terms;
                		if(weektime>20) {
                			tempCourse[ExcelReader.WEEKTIME1_COL]=(weektime-20);
                			terms=1;
                		}else {
                			tempCourse[ExcelReader.WEEKTIME1_COL]=(weektime);
                    		terms=0;
                		}
                		tempCourse[ExcelReader.TERM_COL]=terms;
                		
                		
                		/*
                		 * 分配教室，异常情况为该时间没有可用的教室
                		 */
                		usableClassroom12=GetUsable.getUsableClassroom(allCourse,weektime, terms, type);
                		System.out.println(weektime+" "+terms+"学期可用的"+type+"的教室有");
                		for(int i=0;i<usableClassroom12.size();i++) {
                			System.out.print(usableClassroom12.get(i)+" ");
                		}
                		System.out.println();
                		if(usableClassroom12.size()<1) {
                			continue;
                		}
                		int classroomNum=r.nextInt(usableClassroom12.size());
                		
                		int num12=usableClassroom12.get(classroomNum);
                		tempCourse[ExcelReader.CLASSROOM_COL]=num12;
                		System.out.println("选择的教室为"+num12);
                		}while(usableClassroom12.size()==0);
                 		/*
                		 * 分配教师
                		 */
            			int ok12=0;
                		if(freeTeacher.size()!=0) {
                			for(int i=0;i<freeTeacher.size();i++) {
                				int freeTeacherIndex=freeTeacher.get(i); 
                				//System.out.println("该教师专业为"+ExcelReader.teacherProHashMap.get(freeTeacherIndex));
                				if(ExcelReader.teacherProHashMap.get(freeTeacherIndex).equals(key)) {
                					System.out.println("12课时的freeTeacherIndex"+freeTeacherIndex);
                					tempCourse[ExcelReader.TEACHER_COL]=freeTeacherIndex;
                        			usableTeacher.get(freeTeacherIndex-1)[weektime-1]=1;
                        			freeTeacher.remove(i);
                        			ok12=1;
                        			//System.out.println("有未被初始化的教师");
                        			break;
                				}
                			}         			
                		}	
                		if(ok12==0) {
                			ArrayList<Integer> correctPro=new ArrayList<Integer>();
                			for(Iterator<Integer> proIter = ExcelReader.teacherProHashMap.keySet().iterator() ; proIter.hasNext();){
                				int teacherNum  = proIter.next();
                				if(ExcelReader.teacherProHashMap.get(teacherNum).equals(key)) {
                					correctPro.add(teacherNum);
                				}
                			}
                			while(ok12==0){
                				System.out.println("该专业没有未被初始化的教师");
                				int i=r.nextInt(correctPro.size());
                				int teacherIndex=correctPro.get(i);
                				if(usableTeacher.get(teacherIndex-1)[weektime-1]==0) {
                					System.out.println("12课时的TeacherIndex"+teacherIndex);
                					usableTeacher.get(teacherIndex-1)[weektime-1]=1;
                					tempCourse[ExcelReader.TEACHER_COL]=teacherIndex;
                					ok12=1;
                				}
                			}
                		}
                		break;
                	case 24:
                		/*
                		 * ok表示总的分配是否完成
                		 * noAnswer表示无解
                		 * judge表示前后八周
                		 */
                		int ok=0;
                		int noAnswer=0;
                  		int judge=r.nextInt(2);
                  		int weektime1 = 0;
                  		int weektime2 = 0;
                  		/*
            			 * 极限情况可能会出错，前八周或者后八周都没时间了
            			 * @报错
            			 */
                		do {
                      		System.out.println("随机到"+judge+"八周");
                			if(judge==0) {
                				if(half==0||half==1) {
                					judge=1;
                					if(noAnswer==1) {
                						return "失败";
                					}
                					noAnswer=1;
                					continue;
                				}
                				ArrayList<Integer> usableClassroomtemp=new ArrayList<Integer>();
                			/*
                			 * 先分配时间，再找该时间段可用的教室，如果没有，则重新分配
                			 */
                				do {
                					int weektimeIndex1=r.nextInt(half);
                					int weektimeIndex2=r.nextInt(half);
                					weektime1=usableTime.get(weektimeIndex1);
                					weektime2=usableTime.get(weektimeIndex2);
                					//System.out.println("前半周两节课的时间索引随机到"+weektimeIndex1+weektimeIndex2);
                					tempCourse[ExcelReader.WEEKTIME1_COL]=weektime1;
                					tempCourse[ExcelReader.WEEKTIME2_COL]=weektime2;
                					tempCourse[ExcelReader.TERM_COL]=0;
                					System.out.println("前半周两节课的时间分别为"+weektime1+weektime2);
                					ArrayList<Integer> usableClassroom24=GetUsable.getUsableClassroom(allCourse,weektime1, judge, type);
                					ArrayList<Integer> usableClassroom241=GetUsable.getUsableClassroom(allCourse,weektime2, judge, type);
                					System.out.println(weektime1+" "+judge+"学期可用的"+type+"的教室有");
                            		for(int i=0;i<usableClassroom24.size();i++) {
                            			System.out.print(usableClassroom24.get(i)+" ");
                            		}
                            		System.out.println();
                            		System.out.println(weektime2+" "+judge+"学期可用的"+type+"的教室有");
                            		for(int i=0;i<usableClassroom241.size();i++) {
                            			System.out.print(usableClassroom241.get(i)+" ");
                            		}
                            		System.out.println();
                					/*
                					 * 找到哪些教室两个时间段都可以上课
                					 */
                					for(int i=0;i<usableClassroom24.size();i++) {
                						for(int j=0;j<usableClassroom241.size();j++) {
                							if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                								usableClassroomtemp.add(usableClassroom24.get(i));
                								break;
                							}
                						}
                					}
                				}while(usableClassroomtemp.size()==0||weektime1==weektime2);
                				
                				int classroomNum24=r.nextInt(usableClassroomtemp.size());
                				int num24=usableClassroomtemp.get(classroomNum24);
                				tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                				System.out.println("选择的教室为"+num24);
                				ok=1;
                			}else {
                				if(usableNum-half<2) {
                					judge=0;
                					if(noAnswer==1) {
                						return "失败";
                					}
                					noAnswer=1;
                					continue;
                				}
                				
                				ArrayList<Integer> usableClassroomtemp=new ArrayList<Integer>();
                				do {
                					System.out.println(usableNum+" "+half);
                					int weektimeIndex1=r.nextInt(usableNum-half)+half;
                					int weektimeIndex2=r.nextInt(usableNum-half)+half;
                					weektime1=usableTime.get(weektimeIndex1)-20;
                					weektime2=usableTime.get(weektimeIndex2)-20;
                					tempCourse[ExcelReader.WEEKTIME1_COL]=weektime1;
                					tempCourse[ExcelReader.WEEKTIME2_COL]=weektime2;
                					tempCourse[ExcelReader.TERM_COL]=1;
                					System.out.println("后半周两节课的时间分别为"+weektime1+" "+weektime2);
                    		
                					ArrayList<Integer> usableClassroom24=GetUsable.getUsableClassroom(allCourse,weektime1, judge, type);
                					ArrayList<Integer> usableClassroom241=GetUsable.getUsableClassroom(allCourse,weektime2, judge, type);
                					
                					System.out.println(weektime1+" "+judge+"学期可用的"+type+"的教室有");
                            		for(int i=0;i<usableClassroom24.size();i++) {
                            			System.out.print(usableClassroom24.get(i)+" ");
                            		}
                            		System.out.println();
                            		System.out.println(weektime2+" "+judge+"学期可用的"+type+"的教室有");
                            		for(int i=0;i<usableClassroom241.size();i++) {
                            			System.out.print(usableClassroom241.get(i)+" ");
                            		}
                            		System.out.println();
                					for(int i=0;i<usableClassroom24.size();i++) {
                						for(int j=0;j<usableClassroom241.size();j++) {
                							if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                								usableClassroomtemp.add(usableClassroom24.get(i));
                								break;
                							}
                						}
                					}
                				}while(usableClassroomtemp.size()==0||weektime1==weektime2);
                			
                			
                				int classroomNum24=r.nextInt(usableClassroomtemp.size());
                				int num24=usableClassroomtemp.get(classroomNum24);
                				tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                				System.out.println("选择的教室为"+num24);
                				ok=1;
                			}
                		}while(ok==0);
                		int k=0;
        				if(freeTeacher.size()!=0) {
                			for(int i=0;i<freeTeacher.size();i++) {
                				int freeTeacherIndex=freeTeacher.get(i); 
                				//System.out.println("该教师专业为"+ExcelReader.teacherProHashMap.get(freeTeacherIndex));
                				if(ExcelReader.teacherProHashMap.get(freeTeacherIndex).equals(key)) {
                					tempCourse[ExcelReader.TEACHER_COL]=freeTeacherIndex;
                					System.out.println("24课时的teacherIndex"+freeTeacherIndex);
                        			usableTeacher.get(freeTeacherIndex-1)[weektime1-1+judge*20]=1;
                        			usableTeacher.get(freeTeacherIndex-1)[weektime2-1+judge*20]=1;
                        			freeTeacher.remove(i);
                        			k=1;
                        			System.out.println("有未被初始化的教师");
                        			break;
                				}
                			}         			
                		}
                		
                		if(k==0) {
                			ArrayList<Integer> correctPro=new ArrayList<Integer>();
                			for(Iterator<Integer> proIter = ExcelReader.teacherProHashMap.keySet().iterator() ; proIter.hasNext();){
                				int teacherNum  = proIter.next();
                				//System.out.println("教师编号为"+teacherNum);
                				if(ExcelReader.teacherProHashMap.get(teacherNum).equals(key)) {
                					correctPro.add(teacherNum);
                				}
                			}
                				while(k==0){
                        			int i=r.nextInt(correctPro.size());
                        			int teacherIndex=correctPro.get(i);
                        			System.out.println("24课时的eacherIndex"+teacherIndex);
                        			if(usableTeacher.get(teacherIndex-1)[weektime1-1+judge*20]==0&&usableTeacher.get(teacherIndex-1)[weektime2-1+judge*20]==0) {
                        				usableTeacher.get(teacherIndex-1)[weektime1-1+judge*20]=1;
                        				usableTeacher.get(teacherIndex-1)[weektime2-1+judge*20]=1;
                        				tempCourse[ExcelReader.TEACHER_COL]=teacherIndex;
                        				k=1;
                        			}
                       			}
             		   }
                	
                		
        			break;
                	case 48:
                		/*
                		 * 对可用时间再次过滤，找到哪些时间前后八周都可以
                		 */
                		ArrayList<Integer> usable2=new ArrayList<Integer>(); 
                		for(int i=0;i<half;i++) {
                			//System.out.println(usableTime.get(i));
                			if(usableTime.indexOf((usableTime.get(i))+20)!=-1) {
                				usable2.add(usableTime.get(i));
                			}
                		}
                		//System.out.println("48可用时间为"+usable2.size());
                		if(usable2.size()<2) {
                			return "失败";
                		}else {
                			int weektime3;
                			int weektime4;
                			ArrayList<Integer> usableClassroomtemp=new ArrayList<Integer>();
                			do {	
                			int weektimeIndex3=r.nextInt(usable2.size());
                			int weektimeIndex4=r.nextInt(usable2.size());
                			weektime3=usable2.get(weektimeIndex3);
                			weektime4=usable2.get(weektimeIndex4);
                			System.out.println("48课时随机选择"+weektime3+" "+weektime4);
                			tempCourse[ExcelReader.WEEKTIME1_COL]=weektime3;
                    		tempCourse[ExcelReader.WEEKTIME2_COL]=weektime4;
                    		tempCourse[ExcelReader.TERM_COL]=2;
                    		
                    		ArrayList<Integer> usableClassroom24=GetUsable.getUsableClassroom(allCourse,weektime3, 2, type);
                    		ArrayList<Integer> usableClassroom241=GetUsable.getUsableClassroom(allCourse,weektime4, 2, type);
                    		
        					System.out.println(weektime3+" "+2+"学期可用的"+type+"的教室有");
                    		for(int i=0;i<usableClassroom24.size();i++) {
                    			System.out.print(usableClassroom24.get(i)+" ");
                    		}
                    		System.out.println();
                    		System.out.println(weektime4+" "+2+"学期可用的"+type+"的教室有");
                    		for(int i=0;i<usableClassroom241.size();i++) {
                    			System.out.print(usableClassroom241.get(i)+" ");
                    		}
                    		System.out.println();
                    		
                    		
                    		for(int i=0;i<usableClassroom24.size();i++) {
                    			for(int j=0;j<usableClassroom241.size();j++) {
                    				if(usableClassroom24.get(i)==usableClassroom241.get(j)) {
                    					usableClassroomtemp.add(usableClassroom24.get(i));
                    					break;
                    				}
                    			}
                    		}
                			}while(usableClassroomtemp.size()==0||weektime3==weektime4);
                    		int classroomNum24=r.nextInt(usableClassroomtemp.size());
                    		int num24=usableClassroomtemp.get(classroomNum24);
                    		tempCourse[ExcelReader.CLASSROOM_COL]=num24;
                    		System.out.println("选择的教室为"+num24);
                    		int ok48=0;
                    		if(freeTeacher.size()!=0) {
                    			for(int i=0;i<freeTeacher.size();i++) {
                    				int freeTeacherIndex=freeTeacher.get(i); 
                    				//System.out.println("该教师专业为"+ExcelReader.teacherProHashMap.get(freeTeacherIndex));
                    				if(ExcelReader.teacherProHashMap.get(freeTeacherIndex).equals(key)) {
                    					System.out.println("48课时的freeTeacherIndex"+freeTeacherIndex);
                    					tempCourse[ExcelReader.TEACHER_COL]=freeTeacherIndex;
                            			usableTeacher.get(freeTeacherIndex-1)[weektime3-1]=1;
                            			usableTeacher.get(freeTeacherIndex-1)[weektime4-1]=1;
                            			usableTeacher.get(freeTeacherIndex-1)[weektime3+19]=1;
                            			usableTeacher.get(freeTeacherIndex-1)[weektime4+19]=1;
                            			freeTeacher.remove(i);
                            			ok48=1;
                            			//System.out.println("该专业有未被初始化的教师");
                            			break;
                    				}
                    			}         			
                    		}
                    		if(ok48==0) {
                    			ArrayList<Integer> correctPro=new ArrayList<Integer>();
                    			for(Iterator<Integer> proIter = ExcelReader.teacherProHashMap.keySet().iterator() ; proIter.hasNext();){
                    				int teacherNum  = proIter.next();
                    				if(ExcelReader.teacherProHashMap.get(teacherNum).equals(key)) {
                    					correctPro.add(teacherNum);
                  	        		}
                    			}
                    			while(ok48==0){
                    				System.out.println("该专业没有未被初始化的教师");
                    				int i=r.nextInt(correctPro.size());
                    				int teacherIndex=correctPro.get(i);
                    				if(usableTeacher.get(teacherIndex-1)[weektime3-1]==0&&usableTeacher.get(teacherIndex-1)[weektime4-1]==0) {
                    					if(usableTeacher.get(teacherIndex-1)[weektime3+19]==0&&usableTeacher.get(teacherIndex-1)[weektime4+19]==0) {
                    						System.out.println("48课时的teacherIndex"+teacherIndex);
                    						usableTeacher.get(teacherIndex-1)[weektime3-1]=1;
                        					usableTeacher.get(teacherIndex-1)[weektime4-1]=1;
                        					usableTeacher.get(teacherIndex-1)[weektime3+19]=1;
                        					usableTeacher.get(teacherIndex-1)[weektime4+19]=1;
                        					tempCourse[ExcelReader.TEACHER_COL]=teacherIndex;
                        					ok48=1;
                    					}
                    				}
                    			}
                    		}
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
            		usableTime=GetUsable.getUsableTime(solution);
            }
            }
        }
        System.out.println("选课全部结束");
		return null;
	}
	/*
	 * 对当前方案进行评分，评分主要考虑学习效率,前后八周课程是否均匀;
	 * 效率控制在0.5-0.9
	 * 
	 */
	public double getPoint() {
		double effectivity=0;
		for(int i=0;i<solution.size();i++) {
			int time1=0;
			int time2=0;
			int[] temp=solution.get(i);
			time1=temp[ExcelReader.WEEKTIME1_COL];
			int courseNum=temp[ExcelReader.NAME_COL];
			String courseName=ExcelReader.courseNameHashMap.get(courseNum);
			float coursePoint=ExcelReader.coursePointHashMap.get(courseName);
			
			if(temp[ExcelReader.TERM_COL]==0||temp[ExcelReader.TERM_COL]==1) {
				double a=Math.abs((time1%4)-2.5);
				
				switch((int)a) {
					case 1:
						effectivity+=(double)1.2f*coursePoint;
						break;
					case 0:
						effectivity+=(double)0.4f*coursePoint;
						break;
					default:
						break;
				}
				
					if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
						time2=temp[ExcelReader.WEEKTIME2_COL];
					
						a=Math.abs((time2)%4-2.5);
					
						switch((int)a) {
							case 1:
								effectivity+=(double)1.2f*coursePoint;
								break;
							case 0:
								effectivity+=(double)0.4f*coursePoint;
								break;
							default:
								break;
						}
					}
				
			}else {
				double a=Math.abs((time1%4)-2.5);
				
				switch((int)a) {
				case 1:
					effectivity+=(double)1.2f*coursePoint*2;
					break;
				case 0:
					effectivity+=(double)0.4f*coursePoint*2;
					break;
				default:
					break;
				}
				
				time2=temp[ExcelReader.WEEKTIME2_COL];
				
				a=Math.abs((time2%4)-2.5);
				
				switch((int)a) {
				case 1:
					effectivity+=(double)1.2f*coursePoint*2;
					break;
				case 0:
					effectivity+=(double)0.4f*coursePoint*2;
					break;
				default:
					break;
				}
				
			}
		}
		
		double choosedNum=40-usableTime.size();
		effectivity/=choosedNum;
		if(effectivity-1<=0)
			return 0;
		else
		return effectivity-1;
	}
	/*
	 * 前后八周调节，差值比率，尽量控制在一个额度之内,可以为正可以为负
	 */
	public double getHalfTerm() {
		int lastTerm=0;
		int nextTerm=0;
		for(int i=0;i<solution.size();i++) {
			if(solution.get(i)[ExcelReader.TERM_COL]==2) {
				lastTerm+=2;
				nextTerm+=2;
			}else if(solution.get(i)[ExcelReader.TERM_COL]==1){
				nextTerm++;
				if(solution.get(i)[ExcelReader.WEEKTIME2_COL]!=1) {
					nextTerm++;
				}
			}else if(solution.get(i)[ExcelReader.TERM_COL]==0) {
				lastTerm++;
				if(solution.get(i)[ExcelReader.WEEKTIME2_COL]!=1) {
					lastTerm++;
				}
			}
		}
		
		double different=Math.abs(nextTerm-lastTerm);
		usableTime=GetUsable.getUsableTime(solution);
		double choosedNum=40-usableTime.size();
		double diffRate=different/choosedNum;
		return diffRate;
	}
	/*
	  * 每天上课节数，是否平均，利用标准差
	 */
	public double getDayAverage() {
		byte[] a= {4,4,4,4,4,4,4,4,4,4};
		for(int i=0;i<usableTime.size();i++) {
			
			switch(usableTime.get(i)/4) {
			case 0:
				a[0]-=1;
			case 1:
				a[1]-=1;
			case 3:
				a[3]-=1;
			case 4:
				a[4]-=1;
			case 5:
				a[5]-=1;
			case 6:
				a[6]-=1;
			case 7:
				a[7]-=1;
			case 8:
				a[8]-=1;
			case 9:
				a[9]-=1;
			default:
				break;
			}
		}
		double avg=0;
		double sqrt=0;
		for(int i=0;i<a.length;i++) {
			avg+=a[i];
		}
		for(int i=0;i<a.length;i++) {
			System.out.print(a[i]+" ");
		}

		avg=avg/10;
		for(int i=0;i<10;i++) {
			sqrt+=Math.pow((a[i]-avg),2);
		}
		sqrt=Math.sqrt(sqrt);
		return sqrt;
	
	}
	
	public ClassSolution myclone() {
		ClassSolution cs = null;
        try {
        	// 写入字节流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            cs = (ClassSolution) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cs;
	}
}

