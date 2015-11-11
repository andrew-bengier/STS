package SOLTS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.opencsv.CSVReader;

public class db {
	
	private Properties props, queries;
	private Connection conn;
	
	public db() throws Exception {
		
		//get db properties
		props = new Properties();
		props.load(new FileInputStream("sts.properties"));

		//get sql queries
		queries = new Properties();
		queries.load(new FileInputStream("sts.queries"));
		
		String user = props.getProperty("user");
		String pass = props.getProperty("pass");
		String dburl = props.getProperty("dburl");
		
		//connect to database
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(dburl, user, pass);
		
		System.out.println("DB connection successful to: " + dburl);
	}
	
	public void insertStudent(List<String> csv, String type, String inst, int y) {
		String[] rc = null;
		String typeValues = "";
		String queryInsert = "";
		
		switch( type ) {
			case "M":
				rc = new String[3];
				typeValues = " values (?,?,?,?,?,?,?,?,?,?,?,?)";
				queryInsert = queries.getProperty("insertStudent3") + typeValues;
				break;
			case "E":
				rc = new String[4];
				typeValues = " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				queryInsert = queries.getProperty("insertStudent4") + typeValues;
				break;
			default:
				rc = new String[4];
				typeValues = " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				queryInsert = queries.getProperty("insertStudent4") + typeValues;
		}
		
		/*[DEBUG*/
		System.out.println("called insertStudent");
		
		for(String file : csv) {
			int header = 0;
			try {
				CSVReader reader = new CSVReader(new FileReader(file));
				String [] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					if(header == 0) {
						//header row
						for(int h = 0; h < nextLine.length; h++) {
							System.out.print(nextLine[h] + "*");
						}
						System.out.println();
					}
					else {
						//student record
						for(int r = 0; r < nextLine.length - 8; r++) {
							//System.out.println("RC0" + (r+1) + ": " + nextLine[r+7]);
								rc[r] = nextLine[r+7];
						}
						Student tempStudent = new Student(type,nextLine[2],nextLine[1],nextLine[3],nextLine[4],nextLine[5],nextLine[6],rc,inst,y);
						
						/*[DEBUG]*/
						System.out.println(tempStudent.toString(true));
						System.out.println();
						System.out.println(tempStudent.fullString());
						
						PreparedStatement preparedStmt = conn.prepareStatement(queryInsert);
						preparedStmt.setString(1,tempStudent.getTType());
						preparedStmt.setInt(2,tempStudent.getYear());
						preparedStmt.setString(3,tempStudent.getTeacher());
						preparedStmt.setString(4,tempStudent.getLName());
						preparedStmt.setString(5,tempStudent.getFName());
						preparedStmt.setString(6,tempStudent.getTID());
						preparedStmt.setString(7,tempStudent.getSID());
						preparedStmt.setString(8,tempStudent.getScore());
						preparedStmt.setString(9,tempStudent.getPLvl());
						for(int l = 0; l < rc.length; l++) {
							preparedStmt.setString((l+10),tempStudent.getRC()[l]);
						}
						
						System.out.println(preparedStmt.toString());
						
						preparedStmt.execute();
					}
					header++;
				}
				//conn.close();
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeRetakes(String type) {
		
		String queryTemp = queries.getProperty("tempTableM");
		String queryType = queries.getProperty("updateTypeM");
		String querySID = queries.getProperty("updateSIDM");
		
		try {
			Statement stmtTemp = conn.createStatement();
			Statement stmtType = conn.createStatement();
			Statement stmtSID = conn.createStatement();
			
			System.out.println(queryTemp);
			System.out.println(queryType);
			System.out.println(querySID);
			
			stmtTemp.execute(queryTemp);
			stmtType.execute(queryType);
			stmtSID.execute(querySID);
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Student> getAllStudents() throws Exception {
		List<Student> list = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			String queryView = queries.getProperty("curStat");
			rs = stmt.executeQuery(queryView);
			
			//[Debug]
			System.out.println("Running Query: " + queryView);
			
			while(rs.next()) {
				Student tempStudent = convertRowToStudent(rs);
				list.add(tempStudent);
				
				//[Debug]
				System.out.println(tempStudent.toString());
			}
			
			return list;
		}
		finally {
			//close(stmt, rs);
		}
	}
	
	public List<MathStat> getMathStats() throws Exception {
		List<MathStat> lists = new ArrayList<>();
		
		Statement mstmt = null;
		ResultSet mrs = null;
		
		queries = new Properties();
		queries.load(new FileInputStream("sts.queries"));
		
		/*[Debug]*/
		System.out.println("Loaded Query List");
		
		try {
			mstmt = conn.createStatement();
			String queryView = queries.getProperty("mathByTeacher");
			mrs = mstmt.executeQuery(queryView);
			
			//[Debug]
			System.out.println("Running Query: " + queryView);
			
			while(mrs.next()) {
				MathStat tempStat = convertRowtoMath(mrs);
				lists.add(tempStat);
				
				//[Debug]
				System.out.println(tempStat.toString());
			}
			return lists;
		}
		finally {
			//close(mstmt, mrs);
		}
	}
	
	private static MathStat convertRowtoMath(ResultSet r) throws SQLException {
		int ydate = r.getInt("Year");
		String teach = r.getString("Teacher");
		String perf = r.getString("PerformanceLevel");
		int count = r.getInt("Count");
		
		MathStat tempStat = new MathStat(teach, perf, ydate, count);
		return tempStat;
	}
	
	private static Student convertRowToStudent(ResultSet r) throws SQLException {
		String[] rc = new String[4];
		int ydate = r.getInt("Year");
		String inst = r.getString("Teacher");
		String tType = r.getString("TestType");
		String fname = r.getString("FirstName");
		String lname = r.getString("LastName");
		String tid = r.getString("TestID");
		String sid = r.getString("StudentID");
		String sc = r.getString("Score");
		String plvl = r.getString("PerformanceLevel");
		if(tType == "M" || tType == "D") {
			rc[0] = r.getString("RC01");
			rc[1] = r.getString("RC02");
			rc[2] = r.getString("RC03");
			rc[3] = r.getString("RC04");
		}
		if(tType == "E") {
			rc[0] = r.getString("RC01");
			rc[1] = r.getString("RC02");
			rc[2] = r.getString("RC03");
		}
		
		//[Debug]
		/*
		for(int d = 0; d < rc.length; d++) {
			System.out.println("RC0" + (d+1) + ": " + rc[d]);
		}
		*/
		
		Student tempStudent = new Student(tType, fname, lname, tid, sid, sc, plvl, rc, inst, ydate);
		return tempStudent;	
	}
	
	private static void close(Statement myStmt, ResultSet myRs) throws SQLException {
		close(null, myStmt, myRs);		
	}

	private static void close(Connection myConn, Statement myStmt, ResultSet myRs)
			throws SQLException {

		if (myRs != null) {
			myRs.close();
		}

		if (myStmt != null) {
			
		}
		
		if (myConn != null) {
			myConn.close();
		}
	}

	public void close() {
		//this.close();
	}

	public List<MathStatRow> getMathStatPrintable(List<MathStat> records) {
		List<MathStatRow> statPrint = new ArrayList<>();
		boolean cycled = false;
		boolean flag = false;
		
		System.err.println("EXECUTION>>>");
		
		for(MathStat ms : records) {
			int f = 0, fb = 0, pp = 0, pa = 0, ot = 0, c = 0;
			
			String pLvl = ms.getPerfomanceLvl();
			switch(pLvl) {
				case "Fail/Below Basic":
					fb += ms.getCount();
					break;
				case "Fail/Basic":
					f += ms.getCount();
					break;
				case "Pass/Proficient":
					pp += ms.getCount();
					break;
				case "Pass/Advanced":
					pa += ms.getCount();
					break;
				default:
					ot += ms.getCount();
			}
			
			if(flag) {
				for(MathStatRow r : statPrint) {
					if(r.getTeacher().equals(ms.getTeacher())) {
						cycled = true;
						//System.out.println("Teacher: " + ms.getTeacher());
						r.addCounts(f, fb, pp, pa, ot);
					}
				}

				if(!cycled) {
					MathStatRow tempRow = new MathStatRow(ms.getTeacher(),f,fb,pp,pa,ot,ms.getYear());
					statPrint.add(tempRow);
					System.out.println(tempRow.toString());
				}
			}
			else {
				flag = true;
				MathStatRow tempRow = new MathStatRow(ms.getTeacher(),f,fb,pp,pa,ot,ms.getYear());
				statPrint.add(tempRow);
				System.out.println(tempRow.toString());
			}
			
		}
		return statPrint;
	}
}
