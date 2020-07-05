package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;


import GA.ClassSolution;
import GA.WholeSolution;
import tools.ExcelReader;


public class Main {
	public static void main(String[] args) throws EncryptedDocumentException, IOException {
		
		ExcelReader.loadArrangeInfo();
		ExcelReader.loadCourse();
		
		ExcelReader.loadClassroom();
		
		/*
		for(int i=0;i<ExcelReader.allCourse.size();i++) {
			for(int j=0;j<ExcelReader.allCourse.get(i).length;j++) {
				System.out.print(ExcelReader.allCourse.get(i)[j]+"   ");
			}
			System.out.println();
		}*/
		
		WholeSolution as=new WholeSolution();
		as.init();
		for(int i=0;i<8;i++) {
			ClassSolution a=as.wholeSolution.get(i);
			System.out.println("第"+i+"个班级的课表");
			if(a.solution.size()==0) {
				System.out.println("该班级没有新的课程可以选择初始化");
			}
			else {
				for(int k=0;k<a.solution.size();k++) {
					for(int j=0;j<a.solution.get(k).length;j++) {
						System.out.print(a.solution.get(k)[j]+" ");
					}
					System.out.println();
				}
			}
		}
		
		
		/*
		Set<Entry<String,Float>> st=ExcelReader.coursePointHashMap.entrySet();
		long start=System.currentTimeMillis();
		for(Entry<String,Float> a:st) {
			System.out.print(a.getKey()+"   "+a.getValue());
			System.out.println();
		}
	    long end=System.currentTimeMillis();
	    System.out.println(end-start);
	    
		*/
	}
	
}
