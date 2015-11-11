package SOLTS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
public class curStat {
	JPanel stat;
	private JButton btnRun, btnPrint, btnBack;
	private JTable tblStat;
	private db db_conn;
	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public curStat(final JFrame frame) throws Exception {
		db_conn = new db();
		
		stat = new JPanel();
		stat.setBackground(Color.decode("#757575"));
		stat.setLayout(null);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(60, 250, 80, 20);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
            	try {
            		/*[Debug]*/
            		System.out.println("Running: Get All Students");
               		
            		List<Student> students = null;
            		students = db_conn.getAllStudents();
            		db_conn.close();
            		
            		tableModel model = new tableModel(students);
            		tblStat.setModel(model);
            	}
            	catch(Exception exc) {
            		System.out.println(exc);
            	}
		    }
		});
		stat.add(btnRun);
		
		tblStat = new JTable();
		tblStat.setColumnSelectionAllowed(true);
		tblStat.setCellSelectionEnabled(true);
		tblStat.setBounds(30, 30, 390, 200);
		tblStat.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane statPane = new JScrollPane( tblStat );
		statPane.setBackground(Color.WHITE);
		statPane.setBounds(30, 30, 300, 200);
		stat.add(statPane);
		
		btnPrint = new JButton("Print");
		btnPrint.setBounds(320, 250, 80, 20);
		stat.add(btnPrint);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(355, 200, 70, 25);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
            	try {
            		frame.getContentPane().remove( stat );
        			menuSys menu1 = new menuSys( frame );
        			frame.getContentPane().add( menu1.menu );
        			frame.invalidate();
        			frame.validate();
            	}
            	catch(Exception exc) {
            		System.out.println(exc);
            	}
		    }
		});
		stat.add(btnBack);
	}

	private class tableModel extends AbstractTableModel {

		private static final int NAME_COL = 0;
		private static final int TEST_ID_COL = 1;
		private static final int STUDENT_ID_COL = 2;
		private static final int SCORE_COL = 3;
		private static final int PERFORMANCE_LVL_COL = 4;
		/*
		private static final int RC01_COL = 5;
		private static final int RC02_COL = 6;
		private static final int RC03_COL = 7;
		private static final int RC04_COL = 8;
		*/
		
		private String[] columnNames = {"Name","Test ID","Student ID","Score","Performance Level"}; //,"RC01","RC02","RC03","RC04"};
		private List<Student> student;
		
		public tableModel(List<Student> studentRecords) {
			student = studentRecords;
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return student.size();
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		@Override
		public Object getValueAt(int row, int col) {

			Student tempStudent = student.get(row);
			int[] rcat = new int[4];

			switch (col) {
			case NAME_COL:
				return tempStudent.getName();
			case TEST_ID_COL:
				return tempStudent.getTID();
			case STUDENT_ID_COL:
				return tempStudent.getSID();
			case SCORE_COL:
				return tempStudent.getScore();
			case PERFORMANCE_LVL_COL:
				return tempStudent.getPLvl();
			/*
			case RC01_COL:
				rcat = tempStudent.getRC();
				return rcat[0];
			case RC02_COL:
				rcat = tempStudent.getRC();
				return rcat[1];
			case RC03_COL:
				rcat = tempStudent.getRC();
				return rcat[2];
			case RC04_COL:
				rcat = tempStudent.getRC();
				return rcat[3];
			*/
			default:
				return tempStudent.getName();
			}
		}

		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}
