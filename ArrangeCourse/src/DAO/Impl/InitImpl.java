package DAO.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DAO.InitDAO;
import DAO.baseDAO;

public class InitImpl extends baseDAO implements InitDAO{

	@Override
	public void createTeacherTable() {
		// TODO 自动生成的方法存根
		Connection con=getConnection();
		ResultSet rs=null;
		String sql="create table if not exists Teacher(id int(3) primary key,name varchar(20) not null,profession varchar(20) not null)";
		PreparedStatement ps=null;
		try {
			ps=con.prepareStatement(sql);
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeAll(con,ps,rs);
	}

	@Override
	public void createCourseTable() {
		// TODO 自动生成的方法存根
		Connection con=getConnection();
		ResultSet rs=null;
		String sql="create table if not exists Course(name varchar(20) primary key,volume int(3) not null,type varchar(20),point double(2,1) not null,nums int(2) not null,"
				+ "extra varchar(20),terms varchar(10) not null)";
		PreparedStatement ps=null;
		try {
			ps=con.prepareStatement(sql);
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeAll(con,ps,rs);
		
	}

	@Override
	public void createClassroomTable() {
		// TODO 自动生成的方法存根
		Connection con=getConnection();
		ResultSet rs=null;
		String sql="create table if not exists Classroom(id int(20) primary key,volume int(3) not null,type varchar(20) not null";
		PreparedStatement ps=null;
		try {
			ps=con.prepareStatement(sql);
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		closeAll(con,ps,rs);
	}

}
