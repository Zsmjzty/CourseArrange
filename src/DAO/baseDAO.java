package DAO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
public  class baseDAO {
			private static String driver;
			private static String url;
			private static String user;
			private static String pwd;
			Connection con=null;
			
		 static{
			   init();
		   }
		 public static void init() {
			   Properties params=new Properties();
			   String configFile="database.properties";
			   InputStream is =baseDAO.class.getClassLoader().getResourceAsStream(configFile);
			   try {
				   params.load(is);
			   }catch(IOException e) {
				   e.printStackTrace();
			   }
			   driver=params.getProperty("driver");
			   url=params.getProperty("url");
			   user=params.getProperty("user");
			   pwd=params.getProperty("pwd");
		   }
		   
		 public Connection getConnection()  {
			   try {
			   Class.forName(driver);
			   }catch(ClassNotFoundException e) {
				   e.printStackTrace();
			   }
			   try {
				   con=DriverManager.getConnection(url,user,pwd);
			   }catch(SQLException e) {
				   e.printStackTrace();
			   }
			   return con;
			   
			   
		   }
		 public void closeAll(Connection conn,Statement stmt,ResultSet rs)  {
			   try{
				   if(rs!=null) rs.close();
				   if(stmt!=null) stmt.close();
				   if(conn!=null) conn.close();
			   }catch(SQLException e) {
				   e.printStackTrace();
			   }
		   }
		 public int executeUpdate(String preparedSql,Object[] param) {
			   PreparedStatement pstmt=null;
			   con=getConnection();
			   int num=0;
			   try {
				   pstmt=con.prepareStatement(preparedSql);
				   if(param!=null) {
					   for(int i=0;i<param.length;i++)
						   pstmt.setObject(i+1,param[i]);
				   		}
				   num=pstmt.executeUpdate();
			   }catch(SQLException e) {
				   e.printStackTrace();
			   }
			   return num;
		   }
	}
