package GA;

import java.util.ArrayList;

import tools.ExcelReader;

public class WholeSolution {
	public ArrayList<ClassSolution> wholeSolution=new ArrayList<ClassSolution>();
	/*
	 * 将所有的课程都初始化，保证了所有的课程都被初始化了
	 */
	public void init() {
		for(int i=0;i<ExcelReader.classNum;i++) {
			ClassSolution cs=new ClassSolution();
			cs.init();
			wholeSolution.add(cs);
		}
	}
	
	public void select1() {
		
	}
}
