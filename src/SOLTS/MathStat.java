package SOLTS;

public class MathStat {
	
	private String Teacher;
	private int Year;
	private String PerformanceLvl;
	private int Count;
	private String TestType = "M";
	
	public MathStat( String t, String p, int y, int c ) {
		this.Teacher = t;
		this.PerformanceLvl = p;
		this.Year = y;
		this.Count = c;
	}

	public String getTeacher() { return Teacher; }
	public String getTestType() { return TestType; }
	public String getPerfomanceLvl() { return PerformanceLvl; }
	public int getYear() { return Year; }
	public int getCount() { return Count; }
	
	public String toString() {
		return (  Teacher + " " + PerformanceLvl + " " + Count );
	}
}
