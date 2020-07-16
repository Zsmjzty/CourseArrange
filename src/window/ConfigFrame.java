package window;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tools.ExcelReader;

public class ConfigFrame extends JFrame{
	public ConfigFrame() {
	//setLayout(null);
	setResizable(false);
	setTitle("当前配置文件");
	Container container=getContentPane();
	JPanel jp=new JPanel(new GridLayout(12,1,10,0));
	setBounds(800,450,400,600);
	JLabel classroomInfo=new JLabel("教室信息url:"+"		"+ExcelReader.ClassroomInfo);
	JLabel teacherInfo=new JLabel("教师信息url:"+"	"+ExcelReader.TeacherInfo);
	JLabel arrangeInfo=new JLabel("学分安排url:"+"	"+ExcelReader.ArrangeInfo);
	JLabel courseInfo=new JLabel("课程信息url:"+"	"+ExcelReader.CourseInfo);
	JLabel classNum = new JLabel("班级个数:"+"	  "+String.valueOf(ExcelReader.classNum));
	JLabel studentsNum=new JLabel("每班学生个数:"+"	"+String.valueOf(ExcelReader.studentsNum));
	JLabel savePath=new JLabel("保存路径:"+"		"+ExcelReader.savePath);
	JLabel mutate=new JLabel("变异概率:"+"	"+String.valueOf(ExcelReader.mutatePossibility));
	JLabel crossover=new JLabel("交叉概率:"+"	"+String.valueOf(ExcelReader.crossoverPossibility));
	JLabel dayaverage=new JLabel("每日平均优化:"+"	 "+ExcelReader.selectByDayAverage);
	JLabel effectivity=new JLabel("学习效率优化:"+"	  "+ExcelReader.selectByPoint);
	JLabel halfterm=new JLabel("前后八周课时优化:"+"	  "+ExcelReader.selectByHalfTerm);
	jp.add(classroomInfo);
	jp.add(teacherInfo);
	jp.add(arrangeInfo);
	jp.add(courseInfo);
	jp.add(savePath);
	jp.add(classNum);
	jp.add(studentsNum);
	jp.add(mutate);
	jp.add(crossover);
	jp.add(dayaverage);
	jp.add(effectivity);
	jp.add(halfterm);
	container.add(jp,BorderLayout.CENTER);
	//setBounds(700,700,800,800);
	}
}
