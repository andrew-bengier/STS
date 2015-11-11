package SOLTS;

public class Student {
	private String TestType;
	private String fName;
	private String lName;
	private String TestID;
	private String StudentID;
	private String Score;
	private String PerformanceLvl;
	private String[] RC;
	private int Year;
	private String Teacher;
	
	public Student( String tt, String fn, String ln, String ti, String si, String sc, String p, String[] r, String t, int y ) {
		this.TestType = tt;
		this.fName = fn;
		this.lName = ln;
		this.TestID = ti;
		this.StudentID = si;
		this.Score = sc;
		this.PerformanceLvl = p;
		this.RC = r;
		this.Teacher = t;
		this.Year = y;
	}
	
	public String getSID() { return StudentID; }
	public void setSID( String sid ) { this.StudentID = sid; }
	
	public int getYear() { return Year; }
	public String getTeacher() { return Teacher; }
	public String getTType() { return TestType; }
	public String getName() { return (lName + ", " + fName); }
	public String getLName() { return lName; }
	public String getFName() { return fName; }
	public String getTID() { return TestID; }
	public String getScore() { return Score; }
	public String getPLvl() { return PerformanceLvl; }
	public String[] getRC() { return RC; }
	
	private String rcString(boolean t) {
		String r = "";
		for(int i = 0; i < RC.length; i++) {
			if(i == 0) {
				r += RC[i];
			}
			else {
				if(t) {
					r = r + "," + RC[i];
				}
				else {
					r = r + " " + RC[i];
				}
			}
		}
		return r;
	}
	
	public String fullString() {
		return ( this.getTType()+ "," + this.getYear() + "," + this.getTeacher() + "," + this.getLName() + "," + this.getFName() + "," + this.getTID() + "," + this.getSID() + "," + this.getScore() + "," + this.getPLvl() + "," + rcString(true) );
	}
	
	public String toString(boolean c) {
		return ( this.getName() + " " + this.getTID() + " " + this.getSID() + " " + this.getScore() + " " + this.getPLvl() + " " + rcString(false) );
	}
	
	@Override
	public String toString() {
		return ( this.getName() + " " + this.getTID() + " " + this.getSID() + " " + this.getScore() + " " + this.getPLvl() );
	}
}