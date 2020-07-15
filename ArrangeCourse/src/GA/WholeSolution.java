package GA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tools.ExcelReader;
import tools.GetUsable;
import tools.clashDetect;

public class WholeSolution {
	public ArrayList<ClassSolution> wholeSolution=new ArrayList<ClassSolution>();
	public ClassSolution best=null;
	/*
	 * 将所有的课程都初始化，保证了所有的课程都被初始化了
	 */
	public void init() {
			for(int i=0;i<ExcelReader.speciesNum;i++) {
				/*
				 * 初始化所有课程信息
				 */
				 ArrayList<int[]> allCourse=new ArrayList<int[]>();
				ArrayList<int[]> usableTeacher=new ArrayList<int[]>();
				ArrayList<Integer> freeTeacher=new ArrayList<Integer>();
				for(int allCourseNum=0; allCourseNum<ExcelReader.allCourse.size(); allCourseNum++) {
					
					int[] temp=new int[10];
					
					for(int j=0;j<ExcelReader.allCourse.get(allCourseNum).length;j++) {
						
						temp[j]=ExcelReader.allCourse.get(allCourseNum)[j];
						
					}
					allCourse.add(temp);
				}
				
				/*
				 * 初始化所有可用教师信息
				 */
				
				for(int teacherNum=0;teacherNum<ExcelReader.usableTeacher.size();teacherNum++) {
					int[] temp=new int[40];
					for(int j=0;j<ExcelReader.usableTeacher.get(teacherNum).length;j++) {
						temp[j]=ExcelReader.usableTeacher.get(teacherNum)[j];
					}
					freeTeacher.add(teacherNum+1);
					usableTeacher.add(temp);
				}
				
				/*
				 * 初始化各个班级的课表
				 */
				
				ClassSolution cs=new ClassSolution(allCourse,usableTeacher,freeTeacher);
				cs.init();
				
				for(int classnum=1;classnum<ExcelReader.classNum;classnum++) {
					System.out.println("向链表尾添加第"+classnum+"个课表");
					ClassSolution cn=new ClassSolution(allCourse,usableTeacher,freeTeacher);
					cn.init();
					tools.AddIndividul.add(cs, cn);
				}
				wholeSolution.add(cs);
			}
			System.out.println("课表初始化全部结束");
	}
	
	public  void traverse(int speciesNo) {
		ClassSolution cs=wholeSolution.get(speciesNo);
		int classNo=0;
		do{
			classNo++;
			System.out.println(classNo);
			for(int i=0;i<cs.solution.size();i++) {
				for(int j=0;j<cs.solution.get(i).length;j++) {
					System.out.print(cs.solution.get(i)[j]+"    ");
				}
				System.out.println();
			}
			cs=cs.next;
			
		}while(cs!=null);
	}
	/*
	 * 交叉操作,撞车的概率太大，此处选择直接新建
	 */
	public void crossover() {
		init();
	}
	
	/*
	 * 变异操作
	 */
	public void mutate() {
		Random r=new Random();
		System.out.println("开始进入变异流程");
		for(int species=0;species<wholeSolution.size();species++) {
			System.out.println("第"+species+"个体变异中");
			int mutateNum=r.nextInt(20);
			System.out.println("预计变异"+mutateNum+"个");
			for(int successNum=0;successNum<=mutateNum;successNum++) {
				System.out.println("正在变异第"+successNum+"个基因");
				double mutateP=r.nextDouble();
				if(mutateP<=ExcelReader.mutatePossibility) {
					System.out.println("进入变异");
					ClassSolution cs=wholeSolution.get(species);
					//确定要变异多少个课程编码
					int ok=0;
					do {
						int No=r.nextInt(cs.allCourse.size());
						/*
						 * 只有当确定变异的课程为已初始化过的课程后才可以退出循环
						 */
						if(cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL]!=1) {
							int classNo=cs.allCourse.get(No)[ExcelReader.NAME_COL];
							int teacherNo=cs.allCourse.get(No)[ExcelReader.TEACHER_COL];
							int term=cs.allCourse.get(No)[ExcelReader.TERM_COL];
							int classroomtemp=cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL];
							ArrayList<ArrayList<Integer>> usableTime=new ArrayList<ArrayList<Integer>>();
							/*
							 * 遍历每个班级的课表，如果选择了这门课，则加入链表，并且退出对课表的遍历
							 */
							int classN=0;
							do {
								for(int i=0;i<cs.solution.size();i++) {
									if(cs.solution.get(i)[ExcelReader.NAME_COL]==classNo&&cs.solution.get(i)[ExcelReader.TEACHER_COL]==teacherNo&&cs.solution.get(i)[ExcelReader.CLASSROOM_COL]==classroomtemp) {
										ArrayList<Integer> temp=GetUsable.getUsableTime(cs.solution);
										System.out.println("该班级课表为");
										for(int k=0;k<cs.solution.size();k++) {
											for(int l=0;l<cs.solution.get(k).length;l++) {
												System.out.print(cs.solution.get(k)[l]+"  ");
											}
											System.out.println();
										}
										System.out.println("未修正的可用时间");
										for(int k=0;k<temp.size();k++) {
											System.out.print(temp.get(k)+"  ");
										}
										System.out.println();
										if(term==0) {
											temp.add(cs.solution.get(i)[ExcelReader.WEEKTIME1_COL]);
											if(cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]!=-1) {
												temp.add(cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]);
											}
										}else if(term==1) {
											temp.add((cs.solution.get(i)[ExcelReader.WEEKTIME1_COL]+20));
											if(cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]!=-1) {
												temp.add((cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]+20));
											}
										}else if(term==2) {
											temp.add((cs.solution.get(i)[ExcelReader.WEEKTIME1_COL]+20));
											temp.add((cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]+20));
											temp.add(cs.solution.get(i)[ExcelReader.WEEKTIME1_COL]);
											temp.add(cs.solution.get(i)[ExcelReader.WEEKTIME2_COL]);
										}
										System.out.println("第"+classN+"个班级选择了这们课");
										System.out.println("修正后的可用时间");
										for(int k=0;k<temp.size();k++) {
											System.out.print(temp.get(k)+"  ");
										}
										System.out.println();
										usableTime.add(temp);
										break;
									}
								}
								cs=cs.next;
								classN++;
							}while(cs!=null);
							
							System.out.println("所有班级过滤完毕");
							
							cs=wholeSolution.get(species);
							/*
							 * 找出这些可用时间的共同值，然后分配给变异的课程
							 */
							ArrayList<Integer> commonTime=new ArrayList<Integer>();
							for(int i=0;i<usableTime.size();i++) {
								if(i==0) {
									for(int k=0;k<usableTime.get(i).size();k++) {
										commonTime.add(usableTime.get(i).get(k));
									}
								}else {
									for(int k=0;k<commonTime.size();) {
										if(usableTime.get(i).indexOf(commonTime.get(k))==-1) {
											commonTime.remove(k);
										}
										k++;
									}
								}
							}
							/*
							 * commonTime的时间可以随意分配
							 */
							 if(commonTime.size()!=0) {
								 
								 
								 int[] a=new int[commonTime.size()];
								 for(int i=0;i<a.length;i++) {
									 a[i]=commonTime.get(i);
								 }
								 Arrays.sort(a);
								 System.out.println("这些班级可用的共同时间为");
								 for(int i=0;i<a.length;i++) {
									 System.out.print(a[i]+"   ");
								 }
								 System.out.println();
								 
								 int half=0;
								 for(int i=0;i<a.length;i++) {
									 if(a[i]>20) {
										 half=i;
										 break;
									 }
								 }
								 /*
								    * 首先考虑只能是后半周的课表
								  */
								 if(half<2) {
									 /*
									  * 只能是后半周的课表，
									  */
									 System.out.println("这个时间只能选择后半周");
									 if(term==2) {
										 System.out.println("term为2无法实现变化");
										 continue;
									 }else {
										 /*
										  * 获取变异之后新的时间
										  * temp与该课程编码相同，检测到冲突后重新赋值，在所有冲突都避免之后重新反向赋值给该课程编码
										  */
										 int[] temp=new int[cs.allCourse.get(No).length];
										 int[] tempCourse=cs.allCourse.get(No);
										 for(int i=0;i<cs.allCourse.get(No).length;i++) {
											 temp[i]=cs.allCourse.get(No)[i];
										 }
										 term=1;
									     tempCourse[ExcelReader.TERM_COL]=1;
									     int tryNum=0;
									     do {
									    	 tryNum++;
									    	 System.out.println("第"+tryNum+"尝试为课程的时间赋值");
									    	 int newTime1=r.nextInt(a.length-half)+half;
									    	 /*
									    	  * 已经对课表更改过了
									    	  */
									    	 tempCourse[ExcelReader.WEEKTIME1_COL]=a[newTime1]-20;
									    	 int newTime2=0;
									    	 System.out.println("第1个"+"时间为"+(a[newTime1]-20));
									    	 if(temp[ExcelReader.WEEKTIME2_COL]!=1) {
									    		 do {
									    			 newTime2=r.nextInt(a.length-half)+half;
									    			 /*
											    	  * 已经对课表更改过了
											    	  */
									    			 tempCourse[ExcelReader.WEEKTIME2_COL]=a[newTime2]-20;
									    			 System.out.println("第2个"+"时间为"+(a[newTime2]-20));
									    		 }while(newTime1==newTime2);
									    	 }
									
										 /*
										  * 看教室是否冲突
										  * @注意该时间段会不会没有教室
										  */
										System.out.println("检测新时间是否会导致教室冲突");
										 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
											 System.out.println("有第二个时间");
											 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
											 boolean classroomClash=clashDetect.DetectClassroom(cs.allCourse, a[newTime1]-20, term, classroomNo)||clashDetect.DetectClassroom(cs.allCourse, a[newTime2]-20, term, classroomNo);
											 if(classroomClash) {
												 System.out.println("教室发生了冲突");
												 String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
												 ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1]-20, term, type);
												 ArrayList<Integer> classroom2=GetUsable.getUsableClassroom(cs.allCourse, a[newTime2]-20, term, type);
												 ArrayList<Integer> finalRoom=new ArrayList<Integer>();
												 for(int i=0;i<classroom.size();i++) {
													 if(classroom2.indexOf(classroom.get(i))!=-1) {
														 finalRoom.add(classroom.get(i));
													 }
												 }
												 if(finalRoom.size()==0) {
													 for(int i=0;i<tempCourse.length;i++) {
														 cs.allCourse.get(No)[i]=temp[i];
													 }
													 continue;
												 }
												 System.out.println("可以用的教室为");
												 for(int i=0;i<finalRoom.size();i++) {
													 System.out.print(finalRoom.get(i)+"  ");
												 }
												 int classroomIndex=r.nextInt(finalRoom.size());
												 System.out.println("随机到的教室为"+finalRoom.get(classroomIndex));
												 /*
										    	  * 已经对课表更改过了
										    	  */
												 tempCourse[ExcelReader.CLASSROOM_COL]=finalRoom.get(classroomIndex);
											 }
										}else {
											System.out.println("无第二个时间");
											 int classroomNo=cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL];
											 if(clashDetect.DetectClassroom(cs.allCourse, a[newTime1]-20, term, classroomNo)) {
												 System.out.println("发生了冲突");
												String type=ExcelReader.classroomHashMap.get(cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL]);
												ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1]-20, term, type);			
												int classroomIndex=r.nextInt(classroom.size());
												/*
										    	  * 已经对课表更改过了
										    	  */
												System.out.println("随机到的教室为"+classroom.get(classroomIndex));
												tempCourse[ExcelReader.CLASSROOM_COL]=classroom.get(classroomIndex);			
											}
										}
										 
										 /*
										  * 如果之后的时间与老师冲突，则随机选择一个新老师
										  * 注意会不会这时候没有教师有空
										  */
										 System.out.println("查看教师是否冲突");
										 int teacherIndex=temp[ExcelReader.TEACHER_COL];
										 int newteacher;
										 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
											 System.out.println("第二个时间不为空");
											 boolean teacherClash=clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1]-20,term)||clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime2]-20,term);
											 if(teacherClash) {
												 
												 System.out.println("教师发生了冲突");
												 String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
												 newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1]-20,a[newTime2]-20,term,type);
												 if(newteacher==-1) {
													 
													 for(int i=0;i<tempCourse.length;i++) {
														 cs.allCourse.get(No)[i]=temp[i];
													 }
													 
													 continue;
												 }else {
													 System.out.println("随机到的可用教师为"+newteacher);
												 }
												 tempCourse[ExcelReader.TEACHER_COL]=newteacher;
												 cs.usableTeacher.get(newteacher-1)[a[newTime1]-1-20]=1;
												 cs.usableTeacher.get(newteacher-1)[a[newTime2]-1-20]=1;
											 }
										 }else {
											 System.out.println("第二个时间为空");
										 		if(clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1]-20,term)){
										 			String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
										 			newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1]-20,-1,term,type);
										 			if(newteacher==-1) {
										 				
										 				for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
										 				continue;
										 			}else {
														 System.out.println("随机到的可用教师为"+newteacher);
													 }
										 			tempCourse[ExcelReader.TEACHER_COL]=newteacher;
										 			cs.usableTeacher.get(newteacher-1)[a[newTime1]-1-20]=1;
										 		}
										 }
										 tryNum=41;
									   }while(tryNum<40);
									     /*
									      * 如果是次数限制到达了出来的，则恢复课程内容
									      */
									     if(tryNum!=41) {
									    	 System.out.println("由于次数限制超过了，恢复了课程内容");
									    	 for(int i=0;i<tempCourse.length;i++) {
												 cs.allCourse.get(No)[i]=temp[i];
											 }
									     }
										ok=20;
									}
								 }
							/*
							 * 考虑只能是前半周	 
							 */
								 
							else if((a.length-half)<2){
								System.out.println("只能是前半周");
									 /*
									  * 只能是前半周
									  */
									 if(term==2) {
										 continue;
									 }else {
										 /*
										  * 获取变异之后新的时间
										  * temp与该课程编码相同，检测到冲突后重新赋值，在所有冲突都避免之后重新反向赋值给该课程编码
										  */
										 
										 int[] temp=new int[cs.allCourse.get(No).length];
										 int[] tempCourse=cs.allCourse.get(No);
										 for(int i=0;i<cs.allCourse.get(No).length;i++) {
											 temp[i]=cs.allCourse.get(No)[i];
										 }
										 term=0;
										 /*
								    	  * 已经对课表更改过了
								    	  */
										 tempCourse[ExcelReader.TERM_COL]=term;
										 int tryNum=0;
										 do {
										System.out.println("第"+tryNum+"次尝试");
										 tryNum++;
										 int newTime1=r.nextInt(half);
										 /*
								    	  * 已经对课表更改过了
								    	  */
										 tempCourse[ExcelReader.WEEKTIME1_COL]=a[newTime1];
										 System.out.println("随机到的时间1为"+a[newTime1]);
										 int newTime2=0;
										 if(temp[ExcelReader.WEEKTIME2_COL]!=1) {
											 do {
											 	newTime2=r.nextInt(half);
											 	tempCourse[ExcelReader.WEEKTIME2_COL]=a[newTime2];
											 	System.out.println("随机到的时间2为"+a[newTime2]);
											 }while(newTime1==newTime2);
										 }
										 /*
										  * 看教室是否冲突
										  * @注意该时间段会不会没有教室
										  */
										 System.out.println("查看是否会有教室冲突");
										 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
											 System.out.println("第二个时间不为空");
											 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
											 boolean classroomClash=clashDetect.DetectClassroom(cs.allCourse, a[newTime1], term, classroomNo)||clashDetect.DetectClassroom(cs.allCourse, a[newTime2], term, classroomNo);
											 if(classroomClash) {
												 System.out.println("教室发生了碰撞");
												 String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
												 ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1], term, type);
												 ArrayList<Integer> classroom2=GetUsable.getUsableClassroom(cs.allCourse, a[newTime2], term, type);
												 ArrayList<Integer> finalRoom=new ArrayList<Integer>();
												 for(int i=0;i<classroom.size();i++) {
													 if(classroom2.indexOf(classroom.get(i))!=-1) {
														 finalRoom.add(classroom.get(i));
													 }
												 }
												 if(finalRoom.size()==0) {
													 
													 for(int i=0;i<tempCourse.length;i++) {
														 cs.allCourse.get(No)[i]=temp[i];
													 }
													 continue;
												 }
												 System.out.println("可用教室为");
												 for(int i=0;i<finalRoom.size();i++) {
													 System.out.print(finalRoom.get(i)+"   ");
												 }
												 
												 System.out.println();
												 int classroomIndex=r.nextInt(finalRoom.size());
												 /*
										    	  * 已经对课表更改过了
										    	  */
												 tempCourse[ExcelReader.CLASSROOM_COL]=finalRoom.get(classroomIndex);
											 }
										}else {
											System.out.println("第二个时间为空");
											 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
											 if(clashDetect.DetectClassroom(cs.allCourse, a[newTime1], term, classroomNo)) {
												 System.out.println("发生了教室碰撞");
												String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
												ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1], term, type);			
												int classroomIndex=r.nextInt(classroom.size());
												/*
										    	  * 已经对课表更改过了
										    	  */
												System.out.println("随机到的教室为"+classroom.get(classroomIndex));
												tempCourse[ExcelReader.CLASSROOM_COL]=classroom.get(classroomIndex);			
											}
										}
										 
										 /*
										  * 如果之后的时间与老师冲突，则随机选择一个新老师
										  * 注意会不会这时候没有教师有空
										  */
										 System.out.println("查看是否会有教师冲突");
										 int teacherIndex=temp[ExcelReader.TEACHER_COL];
										 int newteacher;
										 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
											 System.out.println("第二个时间不为空");
											 boolean teacherClash=clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1],term)||clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime2],term);
											 if(teacherClash) {
												 System.out.println("教师发生了碰撞");
												 String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
												 newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1],a[newTime2],term,type);
												 if(newteacher==-1) {
													 for(int i=0;i<tempCourse.length;i++) {
														 cs.allCourse.get(No)[i]=temp[i];
													 }
													 continue;
												 }
												 /*
										    	  * 已经对课表更改过了
										    	  */
												 System.out.println("随机到的教师为"+newteacher);
												 tempCourse[ExcelReader.TEACHER_COL]=newteacher;
												 cs.usableTeacher.get(newteacher-1)[a[newTime1]-1]=1;
												 cs.usableTeacher.get(newteacher-1)[a[newTime2]-1]=1;
											 }
										 }else {
											 System.out.println("第二个时间为空");
										 		if(clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1],term)){
										 			System.out.println("发生了教师碰撞");
										 			String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
										 			newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1],-1,term,type);
										 			if(newteacher==-1) {
										 				
										 				for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
										 				continue;
										 			}
										 			System.out.println("随机到的教师为"+newteacher);
										 			/*
											    	  * 已经对课表更改过了
											    	  */
										 			tempCourse[ExcelReader.TEACHER_COL]=newteacher;
										 			cs.usableTeacher.get(newteacher-1)[a[newTime1]-1]=1;
										 		}
										 }
										 tryNum=41;
										}while(tryNum<40);
										 if(tryNum!=41) {
											 System.out.println("由于次数限制课程信息被重置");
											 for(int i=0;i<tempCourse.length;i++) {
												 cs.allCourse.get(No)[i]=temp[i];
											 }
										 }
										ok=20;
								} 
						}
							/*
							 * 考虑前后八周都可以
							 */
							
							else {
									 /*
									  * 前后八周都时间充足
									  */
								System.out.println("进入前后八周都可以的分段");
									 if(term==2) {
										 System.out.println("选到的课程是48课时");
										 ArrayList<Integer> usableTime2=new ArrayList<Integer>();
										 for(int i=0;i<half;i++) {
											 if(commonTime.indexOf((a[i])+20)!=-1) {
												 usableTime2.add(a[i]);
											 }
										 }
										 if(usableTime2.size()<2) {
											 break;
										 }else {
											 for(int i=0;i<usableTime2.size();i++) {
												 System.out.print(usableTime2.get(i)+"   ");
											 }
											 System.out.println();
											 int time1;
											 int time2;
											/*
											  * 获取变异之后新的时间
											  * temp与该课程编码相同，检测到冲突后重新赋值，在所有冲突都避免之后重新反向赋值给该课程编码
											  */
											  int[] tempCourse=cs.allCourse.get(No);
											 int[] temp=new int[cs.allCourse.get(No).length];
											 for(int i=0;i<cs.allCourse.get(No).length;i++) {
												 temp[i]=cs.allCourse.get(No)[i];
											 }
											 int tryNum=0;
											 do {
												 tryNum++;
												 do{
													 int time1Index=r.nextInt(usableTime2.size());
													 int time2Index=r.nextInt(usableTime2.size());
													 time1=usableTime2.get(time1Index);
													 time2=usableTime2.get(time2Index);
													 System.out.println("选到的时间分别为"+time1+" "+time2);
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 tempCourse[ExcelReader.WEEKTIME1_COL]=time1;
													 tempCourse[ExcelReader.WEEKTIME2_COL]=time2;
												 }while(time1==time2);
												 /*
												  * 如果教室出现了冲突
												  */
												 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
												 boolean classClash=clashDetect.DetectClassroom(cs.allCourse, time1, term, classroomNo)||clashDetect.DetectClassroom(cs.allCourse, time2, term, classroomNo);
												 if(classClash) {
													 System.out.println("发生了教室冲突");
													 String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
													 ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, time1, term, type);
													 ArrayList<Integer> classroom2=GetUsable.getUsableClassroom(cs.allCourse, time2, term, type);
													 ArrayList<Integer> finalRoom=new ArrayList<Integer>();
													 for(int i=0;i<classroom.size();i++) {
														 if(classroom2.indexOf(classroom.get(i))!=-1) {
															 finalRoom.add(classroom.get(i));
														 }
													 }
													 if(finalRoom.size()==0) {
														 
														 for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
														 continue;
													 }
													 System.out.println("找到的可用教室为");
													 for(int i=0;i<finalRoom.size();i++) {
														 System.out.print(finalRoom.get(i)+"  ");
													 }
													 System.out.println();
													 int classroomIndex=r.nextInt(finalRoom.size());
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 tempCourse[ExcelReader.CLASSROOM_COL]=finalRoom.get(classroomIndex);
												 }
												 /*
												  * 如果教师的时间出现了冲突
												  */
												 System.out.println("检测是否出现教师冲突");
												 int teacherIndex=temp[ExcelReader.TEACHER_COL];
												 int newteacher;
												 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
													 boolean teacherClash=clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,time1,term)||clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,time2,term);
													 if(teacherClash) {
														 System.out.println("教师冲突了");
														 String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
														 newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,time1,time2,term,type);
														 if(newteacher==-1) {
															 for(int i=0;i<tempCourse.length;i++) {
																 cs.allCourse.get(No)[i]=temp[i];
															 }
															 continue;
														 }
														 /*
												    	  * 已经对课表更改过了
												    	  */
														 System.out.println("新的教师为"+newteacher);
														 tempCourse[ExcelReader.TEACHER_COL]=newteacher;
														 cs.usableTeacher.get(newteacher-1)[time1-1]=1;
														 cs.usableTeacher.get(newteacher-1)[time2-1]=1;
													 }
												 }
												 tryNum=41;
											 }while(tryNum<40);
											 if(tryNum!=41) {
												 System.out.println("由于次数限制课程信息被重置");
												 for(int i=0;i<tempCourse.length;i++) {
													 cs.allCourse.get(No)[i]=temp[i];
												 }
											 }
											 ok=20;
										 }
									 }else {
										 /*
										  * 连前后八周都可以随机
										  */
										 System.out.println("进入前后八周都可以");
										 int logo=0;
										 int num=0;
										 term=r.nextInt(2);
										do {
											
											term=(term+1)%2;
											System.out.println(term+"八周");
											if(term==0) {
												int[] tempCourse=cs.allCourse.get(No);
										 	 int[] temp=new int[cs.allCourse.get(No).length];
										 	 for(int i=0;i<cs.allCourse.get(No).length;i++) {
										 		 temp[i]=cs.allCourse.get(No)[i];
										 	 }
										 	/*
									    	  * 已经对课表更改过了
									    	  */
											 tempCourse[ExcelReader.TERM_COL]=term;
											 int tryNum=0;
											 do {
											
											 tryNum++;
											 int newTime1=r.nextInt(half);
											 tempCourse[ExcelReader.WEEKTIME1_COL]=a[newTime1];
											 System.out.println("随机到的第一个时间为"+a[newTime1]);
											 int newTime2=0;
											 if(temp[ExcelReader.WEEKTIME2_COL]!=1) {
												 do {
												 	newTime2=r.nextInt(half);
												 	/*
											    	  * 已经对课表更改过了
											    	  */
												 	tempCourse[ExcelReader.WEEKTIME2_COL]=a[newTime2];
												 	System.out.println("随机到的第二个时间为"+a[newTime2]);
												 }while(newTime1==newTime2);
											 }
											 /*
											  * 看教室是否冲突
											  * @注意该时间段会不会没有教室
											  */
											System.out.println("检测教室冲突");
											 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
												 System.out.println("第二个时间不为空");
												 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
												 boolean classClash=clashDetect.DetectClassroom(cs.allCourse, a[newTime1], term, classroomNo)||clashDetect.DetectClassroom(cs.allCourse, a[newTime2], term, classroomNo);
												 if(classClash) {
													 System.out.println("教室冲突了");
													 String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
													 ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1], term, type);
													 ArrayList<Integer> classroom2=GetUsable.getUsableClassroom(cs.allCourse, a[newTime2], term, type);
													 ArrayList<Integer> finalRoom=new ArrayList<Integer>();
													 for(int i=0;i<classroom.size();i++) {
														 if(classroom2.indexOf(classroom.get(i))!=-1) {
															 finalRoom.add(classroom.get(i));
														 }
													 }
													 if(finalRoom.size()==0) {
														 for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
														 continue;
													 }
													 System.out.println("可用教室为");
													 for(int i=0;i<finalRoom.size();i++) {
														 System.out.print(finalRoom.get(i)+"  ");
													 }
													 System.out.println();
													 int classroomIndex=r.nextInt(finalRoom.size());
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 System.out.println("随机到的教室为"+finalRoom.get(classroomIndex));
													 tempCourse[ExcelReader.CLASSROOM_COL]=finalRoom.get(classroomIndex);
												 }
											}else {
												System.out.println("第二个时间为空");
												 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
												 System.out.println("检测教室冲突");
												 if(clashDetect.DetectClassroom(cs.allCourse, a[newTime1], term, classroomNo)) {
													 System.out.println("教室冲突了");
													String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
													ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1], term, type);			
													int classroomIndex=r.nextInt(classroom.size());
													System.out.println("随机到的教室为"+classroom.get(classroomIndex));
													/*
											    	  * 已经对课表更改过了
											    	  */
													tempCourse[ExcelReader.CLASSROOM_COL]=classroom.get(classroomIndex);			
												}
											}
											 
											 /*
											  * 如果之后的时间与老师冲突，则随机选择一个新老师
											  * 注意会不会这时候没有教师有空
											  */
											 System.out.println("检查教师冲突");
											 int teacherIndex=temp[ExcelReader.TEACHER_COL];
											 int newteacher;
											 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
												 System.out.println("第二个时间片不为空");
												 boolean teacherClash=clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1],term)||clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime2],term);
												 if(teacherClash) {
													 System.out.println("教师冲突了");
													 String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
													 newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1],a[newTime2],term,type);
													 if(newteacher==-1) {
														 for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
														 continue;
													 }
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 System.out.println("随机到的教师为"+newteacher);
													 tempCourse[ExcelReader.TEACHER_COL]=newteacher;
													 cs.usableTeacher.get(newteacher-1)[a[newTime1]-1]=1;
													 cs.usableTeacher.get(newteacher-1)[a[newTime2]-1]=1;
												 }
											 }else {
												 System.out.println("第二个时间片为空");
					
											 		if(clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1],term)){
											 			String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
											 			newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1],-1,term,type);
											 			if(newteacher==-1) {
											 				for(int i=0;i<tempCourse.length;i++) {
																 cs.allCourse.get(No)[i]=temp[i];
															 }
											 				continue;
											 			}
											 			/*
												    	  * 已经对课表更改过了
												    	  */
											 			tempCourse[ExcelReader.TEACHER_COL]=newteacher;
											 			cs.usableTeacher.get(newteacher-1)[a[newTime1]-1]=1;
											 			System.out.println("随机到的教师为"+newteacher);
											 		}
											 }
											
											 logo=1;
											 tryNum=41;
											}while(tryNum<40);
											 if(tryNum!=41) {
												 System.out.println("由于次数限制还原课程信息");
												 for(int i=0;i<tempCourse.length;i++) {
													 cs.allCourse.get(No)[i]=temp[i];
												 }
											 }
											 
										 }
										 /*
										  * 前半周完毕,转为后半周
										  */
										 
										 else {
											 System.out.println("进入后半周");
											 int[] tempCourse=cs.allCourse.get(No);
											 int[] temp=new int[cs.allCourse.get(No).length];
											 for(int i=0;i<cs.allCourse.get(No).length;i++) {
												 temp[i]=cs.allCourse.get(No)[i];
											 }
											 term=1;
											 /*
									    	  * 已经对课表更改过了
									    	  */
										     tempCourse[ExcelReader.TERM_COL]=term;
										     int tryNum=0;
										     do {
										    	 tryNum++;
										    	 int newTime1=r.nextInt(a.length-half)+half;
										    	 /*
										    	  * 已经对课表更改过了
										    	  */
										    	 tempCourse[ExcelReader.WEEKTIME1_COL]=a[newTime1]-20;
										    	 System.out.println("随机到的时间1为"+(a[newTime1]-20));
										    	 int newTime2=0;
										    	 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
										    		 do {
										    			 newTime2=r.nextInt(a.length-half)+half;
										    			 /*
												    	  * 已经对课表更改过了
												    	  */
										    			 tempCourse[ExcelReader.WEEKTIME2_COL]=a[newTime2]-20;
										    			 System.out.println("随机到的时间2为"+(a[newTime2]-20));
										    		 }while(newTime1==newTime2);
										    	 }
										
											 /*
											  * 看教室是否冲突
											  * @注意该时间段会不会没有教室
											  */
											System.out.println("检查教室冲突");
											 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
												 System.out.println("第二个时间片不为空");
												 int classroomNo=temp[ExcelReader.CLASSROOM_COL];
												 boolean classroomClash=clashDetect.DetectClassroom(cs.allCourse, a[newTime1]-20, term, classroomNo)||clashDetect.DetectClassroom(cs.allCourse, a[newTime2]-20, term, classroomNo);
												 if(classroomClash) {
													 System.out.println("教室冲突了");
													 String type=ExcelReader.classroomHashMap.get(temp[ExcelReader.CLASSROOM_COL]);
													 ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1]-20, term, type);
													 ArrayList<Integer> classroom2=GetUsable.getUsableClassroom(cs.allCourse, a[newTime2]-20, term, type);
													 ArrayList<Integer> finalRoom=new ArrayList<Integer>();
													 for(int i=0;i<classroom.size();i++) {
														 if(classroom2.indexOf(classroom.get(i))!=-1) {
															 finalRoom.add(classroom.get(i));
														 }
													 }
													 if(finalRoom.size()==0) {
														 for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
														 continue;
													 }
													 System.out.println("可用教室为");
													 for(int i=0;i<finalRoom.size();i++) {
														 System.out.print(finalRoom.get(i)+"   ");
													 }
													 System.out.println();
													 int classroomIndex=r.nextInt(finalRoom.size());
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 System.out.println("选择的教室为"+finalRoom.get(classroomIndex));
													 tempCourse[ExcelReader.CLASSROOM_COL]=finalRoom.get(classroomIndex);
												 }
											}else {
												System.out.println("第二个时间为空");
												 int classroomNo=cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL];
												 if(clashDetect.DetectClassroom(cs.allCourse, a[newTime1]-20, term, classroomNo)) {
													 System.out.println("发生了教室碰撞");
													String type=ExcelReader.classroomHashMap.get(cs.allCourse.get(No)[ExcelReader.CLASSROOM_COL]);
													ArrayList<Integer> classroom=GetUsable.getUsableClassroom(cs.allCourse, a[newTime1]-20, term, type);			
													int classroomIndex=r.nextInt(classroom.size());
													/*
											    	  * 已经对课表更改过了
											    	  */
													 System.out.println("可用教室为");
													 for(int i=0;i<classroom.size();i++) {
														 System.out.println(classroom.get(i)+"   ");
													 }
													 System.out.println();
													tempCourse[ExcelReader.CLASSROOM_COL]=classroom.get(classroomIndex);			
												}
											}
											 
											 /*
											  * 如果之后的时间与老师冲突，则随机选择一个新老师
											  * 注意会不会这时候没有教师有空
											  */
											 System.out.println("检查教师冲突");
											 int teacherIndex=temp[ExcelReader.TEACHER_COL];
											 int newteacher;
											 if(temp[ExcelReader.WEEKTIME2_COL]!=-1) {
												 System.out.println("第二个时间不为空");
												 boolean teacherClash=clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1]-20,term)||clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime2]-20,term);
												 if(teacherClash) {
													 System.out.println("教师发生了冲突");
													 String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
													 newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1]-20,a[newTime2]-20,term,type);
													 if(newteacher==-1) {
														 for(int i=0;i<tempCourse.length;i++) {
															 cs.allCourse.get(No)[i]=temp[i];
														 }
														 continue;
													 }
													 /*
											    	  * 已经对课表更改过了
											    	  */
													 System.out.println("随机到的教师为"+newteacher);
													 tempCourse[ExcelReader.TEACHER_COL]=newteacher;
													 cs.usableTeacher.get(newteacher-1)[a[newTime1]-1-20]=1;
													 cs.usableTeacher.get(newteacher-1)[a[newTime2]-1-20]=1;
												 }
											 }else {
												 System.out.println("第二个时间为空");
											 		if(clashDetect.DetectTeacher(cs.usableTeacher, teacherIndex,a[newTime1]-20,term)){
											 			String type=ExcelReader.courseTypeHashMap.get(temp[ExcelReader.NAME_COL]);
											 			newteacher=GetUsable.getUsableTeacher(cs.usableTeacher,a[newTime1]-20,-1,term,type);
											 			if(newteacher==-1) {
											 				for(int i=0;i<tempCourse.length;i++) {
																 cs.allCourse.get(No)[i]=temp[i];
															 }
											 				continue;
											 			}
											 			System.out.println("随机到的教师为"+newteacher);
											 			/*
												    	  * 已经对课表更改过了
												    	  */
											 			tempCourse[ExcelReader.TEACHER_COL]=newteacher;
											 			cs.usableTeacher.get(newteacher-1)[a[newTime1]-1-20]=1;
											 		}
											 }

											 logo=1;
											 tryNum=41;
										   }while(tryNum<40);
										     if(tryNum!=41) {
										    	 System.out.println("由于次数限制还原课程信息");
										    	 for(int i=0;i<tempCourse.length;i++) {
													 cs.allCourse.get(No)[i]=temp[i];
												 }
										     }
										}
											if(logo==1) {
												break;
											}else {
												num++;
											}
										}while(num<2);
										ok=20;
									 }
								 }
							 }//选择课程的班级没有共同时间 
							 
						}//选到了一门没有初始化的课程
						
					}while(ok<20);
				}
			}
		}
		System.out.println("变异结束");
	}
	
	public void select() {
		/*
		 * 选择的方案1:评分低的更可能被淘汰
		 */
		ClassSolution bestTemp = null;
		double bestPointTemp=0;
		ArrayList<double[]> individualPoint=new ArrayList<double[]>();
		ArrayList<double[]> incrementPoint;
		/*
		 * 三种都不选则默认
		 */
		for(int i=0;i<wholeSolution.size();i++) {
			double[] a= {i,0};
			individualPoint.add(a);			
		}
		
		if(ExcelReader.selectByDayAverage.equals("false")&&ExcelReader.selectByHalfTerm.equals("false")&&ExcelReader.selectByPoint.equals("false")){
			System.out.println("进入默认程序");
			/*
			 * example用来储存各个课表的分数
			 * individualPoint包含了下标以及分数
			 */
			double sum=0;
			double[] temp=new double[wholeSolution.size()];
			for(int speciesNum=0;speciesNum<wholeSolution.size();speciesNum++) {
				ClassSolution cs=wholeSolution.get(speciesNum);
				double point;
				do {
					point=cs.getPoint();
					temp[speciesNum]+=point;
					sum+=point;
					//System.out.print(point+" ");
					cs=cs.next;
				}while(cs!=null);
				/*
				 * if(temp[1]>bestPoint) {
					bestPoint=temp[1];
					best=wholeSolution.get(speciesNum).clone();
				}*/

			}
			
			for(int i=0;i<temp.length;i++) {
				temp[i]/=sum;
				individualPoint.get(i)[1]+=temp[i];
			}
		}
		/*
		 * 开启学分优化
		 */
		if(ExcelReader.selectByPoint.equals("true")) {
			System.out.println("开启学分优化");
			double sum=0;
			double[] temp=new double[wholeSolution.size()];
			for(int speciesNum=0;speciesNum<wholeSolution.size();speciesNum++) {
				ClassSolution cs=wholeSolution.get(speciesNum);
				double point;
				do {
					point=cs.getPoint();
					temp[speciesNum]+=point;
					sum+=point;
					//System.out.print(point+" ");
					cs=cs.next;
				}while(cs!=null);
				/*
				 * if(temp[1]>bestPoint) {
					bestPoint=temp[1];
					best=wholeSolution.get(speciesNum).clone();
				}*/

			}
			
			for(int i=0;i<temp.length;i++) {
				temp[i]/=sum;
				individualPoint.get(i)[1]+=temp[i];
			}
		}
		/*
		 * 开启半学期优化
		 */
		if(ExcelReader.selectByHalfTerm.equals("true")){
			System.out.println("开启半学期优化");
			double sum=0;
			double[] temp=new double[wholeSolution.size()];
			for(int speciesNum=0;speciesNum<wholeSolution.size();speciesNum++) {
				ClassSolution cs=wholeSolution.get(speciesNum);
				double point;
				do {
					point=cs.getHalfTerm();
					temp[speciesNum]+=point;
					sum+=point;
					//System.out.print(point+" ");
					cs=cs.next;
				}while(cs!=null);
				/*
				 * if(temp[1]>bestPoint) {
					bestPoint=temp[1];
					best=wholeSolution.get(speciesNum).clone();
				}*/

			}
			
			for(int i=0;i<temp.length;i++) {
				temp[i]/=sum;
				individualPoint.get(i)[1]+=temp[i];
			}
		}
		/*
		 * 开启每天平均
		 */
		if(ExcelReader.selectByDayAverage.equals("true")){
			System.out.println("开启每天平均");
			double sum=0;
			double[] temp=new double[wholeSolution.size()];
			for(int speciesNum=0;speciesNum<wholeSolution.size();speciesNum++) {
				ClassSolution cs=wholeSolution.get(speciesNum);
				double point;
				do {
					point=cs.getDayAverage();
					temp[speciesNum]+=point;
					sum+=point;
					//System.out.print(point+" ");
					cs=cs.next;
				}while(cs!=null);
				/*
				 * if(temp[1]>bestPoint) {
					bestPoint=temp[1];
					best=wholeSolution.get(speciesNum).clone();
				}*/

			}
			
			for(int i=0;i<temp.length;i++) {
				temp[i]/=sum;
				individualPoint.get(i)[1]+=temp[i];
			}
		}
		double sum=0;
		for(int i=0;i<individualPoint.size();i++) {
			sum+=individualPoint.get(i)[1];
		}
		
		for(int i=0;i<individualPoint.size();i++) {
			
			individualPoint.get(i)[1]/=sum;
			System.out.println(individualPoint.get(i)[1]);
			//得到当前这一代的最高分
			if(bestPointTemp<individualPoint.get(i)[1]) {
				bestPointTemp=individualPoint.get(i)[1];
				 bestTemp=wholeSolution.get((int)individualPoint.get(i)[0]).myclone();
			}
		}
		/*
		 * 将当代的最高分与历史最高进行比较
		 */
		double p1=0;
		double p2=0;
		if(best==null) {
			best=bestTemp.myclone();
		}else {
			if(ExcelReader.selectByDayAverage.equals("false")&&ExcelReader.selectByHalfTerm.equals("false")&&ExcelReader.selectByPoint.equals("false")) {
				p1+=bestTemp.getPoint()/(bestTemp.getPoint()+best.getPoint());
				p2+=best.getPoint()/(bestTemp.getPoint()+best.getPoint());
			}
			if(ExcelReader.selectByDayAverage.equals("true")) {
				p1+=bestTemp.getDayAverage()/(bestTemp.getDayAverage()+best.getDayAverage());
				p2+=best.getDayAverage()/(bestTemp.getDayAverage()+best.getDayAverage());
			}
			if(ExcelReader.selectByHalfTerm.equals("true")) {
				p1+=bestTemp.getHalfTerm()/(bestTemp.getHalfTerm()+best.getHalfTerm());
				p2+=best.getHalfTerm()/(bestTemp.getHalfTerm()+best.getHalfTerm());
			}
			if(ExcelReader.selectByPoint.equals("true")) {
				p1+=bestTemp.getPoint()/(bestTemp.getPoint()+best.getPoint());
				p2+=best.getPoint()/(bestTemp.getPoint()+best.getPoint());
			}
			if(p1>p2) {
				best=bestTemp.myclone();
				
			}	
		
		}
		
		incrementPoint=GetUsable.refreshPoint(individualPoint);
		
		//System.out.println("初始化时两个列表的值");
		/*for(int i=0;i<individualPoint.size();i++) {
			System.out.println(individualPoint.get(i)[0]+"  "+individualPoint.get(i)[1]);
		}
		System.out.println();
		for(int i=0;i<incrementPoint.size();i++) {
			System.out.println(incrementPoint.get(i)[0]+"  "+incrementPoint.get(i)[1]);
		}
		*/
		
		Random r=new Random();
		int[] remain=new int[ExcelReader.speciesNum];
		for(int i=0;i<ExcelReader.speciesNum;i++) {
				int op=r.nextInt(1000);
				double chance=(double)(op)/1000;
				for(int j=0;j<incrementPoint.size();j++) {
					if(j==0) {
						if(incrementPoint.get(j)[1]>=chance) {
							remain[i]=(int)incrementPoint.get(j)[0];
							individualPoint.remove(j);
							break;
						}
					}else if(incrementPoint.get(j)[1]>=chance&&incrementPoint.get(j-1)[1]<chance) {
							remain[i]=(int)incrementPoint.get(j)[0];
							individualPoint.remove(j);
							break;
					}
					
				}
				incrementPoint=GetUsable.refreshPoint(individualPoint);
				System.out.println("选择了"+(i+1)+"个后两个列表的值");
				for(int k=0;k<individualPoint.size();k++) {
					System.out.println(individualPoint.get(k)[0]+"  "+individualPoint.get(k)[1]);
				}
				/*
				System.out.println();
				for(int k=0;k<incrementPoint.size();k++) {
					System.out.println(incrementPoint.get(k)[0]+"  "+incrementPoint.get(k)[1]);
				}*/
		}
		/*
		 * 保留下留下的几分物种
		 */
		
		ArrayList<ClassSolution> tempSolution=new ArrayList<ClassSolution>();
		for(int i=0;i<ExcelReader.speciesNum;i++) {
			tempSolution.add(wholeSolution.get(remain[i]));
		}
		wholeSolution=tempSolution;
		
	}
}
