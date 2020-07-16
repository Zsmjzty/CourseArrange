package DAO.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DAO.InitDAO;
import DAO.baseDAO;
import tools.ExcelReader;

public class InitImpl extends baseDAO implements InitDAO{

	public void createSolution() {
		// TODO 自动生成的方法存根
		Connection con=getConnection();
		ResultSet rs=null;
		PreparedStatement ps=null;
		for(int i=1;i<=ExcelReader.classNum;i++) {
			String tableName="Class"+String.valueOf(i);
			String sql="create table if not exists "+tableName+"(name int(3) not null,volume int(3) not null,type int(3) not null,classroomtype int(2) not null,learntime int(2) not null,"
				+ "weektime1 int(2) not null,weektime2 int(2) not null,classroom int(2) not null,term int(2) not null,teacher int(3) not null,primary key(name,teacher,classroom))";
			try {
				ps=con.prepareStatement(sql);
				ps.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		closeAll(con,ps,rs);
	}


}
