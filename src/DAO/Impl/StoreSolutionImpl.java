package DAO.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DAO.StoreSolution;
import DAO.baseDAO;
import GA.ClassSolution;
import tools.ExcelReader;

public class StoreSolutionImpl extends baseDAO implements StoreSolution{

	@Override
	public void saveSolution(ClassSolution cs) {
		// TODO 自动生成的方法存根
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int classNum=1;
		do {
			String tabelName="Class"+String.valueOf(classNum);
			int name = 0;
			int volume = 0;
			int type = 0;
			int classroomtype = 0;
			int learntime = 0;
			int weektime1= 0;
			int weektime2 =0;
			int classroom=0;
			int term=0;
			int teacher=0;
			String sql2="delete from "+tabelName;
			try {
				pstmt=con.prepareStatement(sql2);
				pstmt.executeUpdate();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			for(int i=0;i<cs.solution.size();i++) {
			/**
			 * 每次循环添加一行数据，直到把该班级课表全部写入
			 */
				name=cs.solution.get(i)[ExcelReader.NAME_COL];
				volume=cs.solution.get(i)[ExcelReader.VOLUME_COL];
				type=cs.solution.get(i)[ExcelReader.TYPE_COL];
				classroomtype=cs.solution.get(i)[ExcelReader.CLASSROOMTYPE_COL];
				learntime=cs.solution.get(i)[ExcelReader.LEARNTIME_COL];
				weektime1=cs.solution.get(i)[ExcelReader.WEEKTIME1_COL];
				weektime2=cs.solution.get(i)[ExcelReader.WEEKTIME2_COL];
				classroom=cs.solution.get(i)[ExcelReader.CLASSROOM_COL];
				term=cs.solution.get(i)[ExcelReader.TERM_COL];
				teacher=cs.solution.get(i)[ExcelReader.TEACHER_COL];
				String sql="insert into "+tabelName+" values(?,?,?,?,?,?,?,?,?,?)";
				
				try {
					pstmt=con.prepareStatement(sql);
					pstmt.setInt(1,name);
					pstmt.setInt(2,volume);
					pstmt.setInt(3,type);
					pstmt.setInt(4,classroomtype);
					pstmt.setInt(5,learntime);
					pstmt.setInt(6,weektime1);
					pstmt.setInt(7,weektime2);
					pstmt.setInt(8,classroom);
					pstmt.setInt(9,term);
					pstmt.setInt(10,teacher);
					pstmt.executeUpdate();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			cs=cs.next;
			classNum++;
		}while(cs!=null);
		closeAll(con,pstmt,rs);
	}

	@Override
	public ClassSolution OutputSolution(int classNum) {
		// TODO 自动生成的方法存根
		ClassSolution cs=new ClassSolution();
		
		Connection con=getConnection();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String tabelName="Class"+String.valueOf(classNum);
		String sql="select * from "+tabelName;
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				int[] tempCourse=new int[10];
				for(int i=0;i<10;i++) {
					tempCourse[i]=rs.getInt(i+1);
				}
				cs.solution.add(tempCourse);
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return cs;
		
		
	}

}
