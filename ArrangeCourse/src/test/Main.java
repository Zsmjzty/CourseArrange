package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;

import GA.ClassSolution;
import GA.WholeSolution;
import tools.ExcelReader;
import tools.ExcelWriter;


public class Main {
	public static void main(String[] args) throws EncryptedDocumentException, IOException {

		ExcelReader.loadArrangeInfo();
		ExcelReader.loadCourse();
		ExcelReader.loadTeacher();
		ExcelReader.loadClassroom();
	
		WholeSolution as=new WholeSolution();
		as.init();
		for(int i=0;i<ExcelReader.circumsNum;i++) {
		as.select();
		as.crossover();
		as.mutate();
		}
		
		ExcelWriter.outputSolution(as.best);
		
		
}
	

		
	
		

	
}
	    
	    
		
	
	

