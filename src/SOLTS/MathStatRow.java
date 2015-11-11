/**
 * 
 */
package SOLTS;

/**
 * @author AB
 *
 */
public class MathStatRow {
	private String Teacher;
	private int Year;
	private int Count;
	private int f, fb, pp, pa, ot, pr;
	private String TestType = "M";
	
	public MathStatRow( String t, int p1, int p11, int p2, int p3, int p4, int y) {
		this.Teacher = t;
		this.f = p1;
		this.fb = p11;
		this.pp = p2;
		this.pa = p3;
		this.ot = p4;
		this.Year = y;
		
		this.pr = (p3 + p2);
		this.Count = p1 + p11 + p2 + p3 + p4;
	}

	public void addCounts(int p1, int p11, int p2, int p3, int p4) {
		this.f += p1;
		this.fb += p11;
		this.pp += p2;
		this.pa += p3;
		this.ot += p4;
		
		this.Count += (p1 + p11 + p2 + p3 + p4);
	}
	
	public int getF() { return (f + fb); }
	public int getPP() { return pp; }
	public int getPA() { return pa; }
	public int getPR() { return pr;}
	public int getOT() { return ot; };
	public String getTeacher() { return Teacher; }
	public String getTestType() { return TestType; }
	public int[] getPerformance() {
		int[] values = {f, pp, pa, pr};
		return values;
	}
	public int getYear() { return Year; }
	public int getCount() { return Count; }
	
	public String toString() {
		return (  Teacher + " " + f + " " + pp + " " + pa + " " + pr );
	}
}
