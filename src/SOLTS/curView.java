package SOLTS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.JToggleButton;

public class curView {
	JPanel view;
	JTable tblView;
	JButton btnMath, btnEnglish, btnOverall, btnBack;
	JToggleButton tglbtnPrintable;
	private db db_conn;
	
	public curView(final JFrame frame) throws Exception {
		db_conn = new db();
		
		view = new JPanel();
		view.setBackground(Color.decode("#757575"));
		view.setLayout(null);
		
		btnMath = new JButton("Math");
		btnMath.setBounds(340, 30, 90, 25);
		view.add(btnMath);
		
		btnEnglish = new JButton("English");
		btnEnglish.setBounds(340, 80, 90, 25);
		view.add(btnEnglish);
		
		btnOverall = new JButton("Overall");
		btnOverall.setBounds(340, 130, 90, 25);
		view.add(btnOverall);
		
		btnMath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
            	try {
            		/*[Debug]*/
            		System.out.println("Running: Math Stats");
               		
            		List<MathStat> records = null;
            		List<MathStatRow> srecords = null;
            		records = db_conn.getMathStats();
            		srecords = db_conn.getMathStatPrintable(records);
            		db_conn.close();
            		
            		/*[Debug]*/
            		System.out.println("Finished: Math Stats");
            		
            		tableModel model = new tableModel(records);
            		printableTable ptable = new printableTable(srecords);
            		
            		if(tglbtnPrintable.isSelected()) {
            			tblView.setModel(ptable);
            		}
            		else {
            			tblView.setModel(model);
            		}
            	}
            	catch(Exception exc) {
            		System.out.println(exc);
            	}
		    }
		});
		
		tblView = new JTable();
		tblView.setColumnSelectionAllowed(true);
		tblView.setCellSelectionEnabled(true);
		tblView.setBounds(30, 30, 250, 200);
		tblView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane viewPane = new JScrollPane( tblView );
		viewPane.setBackground(Color.WHITE);
		viewPane.setBounds(30, 30, 300, 200);
		view.add(viewPane);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(355, 200, 70, 25);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
            	try {
            		frame.getContentPane().remove( view );
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
		view.add(btnBack);
		
		tglbtnPrintable = new JToggleButton("Printable");
		tglbtnPrintable.setBounds(103, 250, 121, 23);
		view.add(tglbtnPrintable);
	}
	
	private class printableTable extends AbstractTableModel {
		private static final int TEACHER_COL = 0;
		private static final int F_COL = 1;
		private static final int PP_COL = 2;
		private static final int PA_COL = 3;
		private static final int PR_COL = 4;
		
		//private static final int YEAR_COL = 5;
		
		private String[] columnNames = {"Teacher","Fail","Pass/Proficient","Pass/Advanced","Pass Ratio"};
		private List<MathStatRow> stats;
		
		public printableTable(List<MathStatRow> statRecords) {
			stats = statRecords;
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return stats.size();
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public float ratio(MathStatRow t, int p) {
			int count = t.getCount();
			float num = 0;
			switch(p) {
				case 0: num = (float)t.getF(); break;
				case 1: num = (float)t.getPP(); break;
				case 2: num = (float)t.getPA(); break;
				case 3: num = (float)t.getPR(); break;
			}
			float ratio = ( num / count );
			System.out.println(" Num: " + num + "Count: " + count + "Ration: " + ratio);
			
			if(num == 0) {
				return 0;
			}
			
			return ratio;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			DecimalFormat df = new DecimalFormat("#%");

			MathStatRow tempStat = stats.get(row);
			
			switch (col) {
			case TEACHER_COL:
				return tempStat.getTeacher();
			case F_COL:
				return (df.format(ratio(tempStat,0)));
			case PP_COL:
				return (df.format(ratio(tempStat,1)));
			case PA_COL:
				return (df.format(ratio(tempStat,2)));
			case PR_COL:
				return (df.format(ratio(tempStat,3)));
			default:
				return tempStat.getTeacher();
			}
		}

		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}

	private class tableModel extends AbstractTableModel {

		private static final int TEACHER_COL = 0;
		private static final int PERFORMANCE_LVL_COL = 1;
		private static final int COUNT_COL = 2;
		private static final int RATIO_COL = 3;

		//private static final int YEAR_COL = 4;
		
		private String[] columnNames = {"Teacher","Performance Level","Count","Ratio"};
		private List<MathStat> stats;
		
		public tableModel(List<MathStat> studentRecords) {
			stats = studentRecords;
		}
		
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return stats.size();
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public float ratio(MathStat t) {
			int count = t.getCount();
			float sum = 0;
			float ratio = 0;
			for(MathStat m : stats){
				sum += m.getCount();
				//System.out.println("SUM: " + sum);
			}
			ratio = ( count / sum );
			System.out.println("Count: " + count + " Sum: " + sum + "Ration: " + ratio);
			return ratio;
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			DecimalFormat df = new DecimalFormat("#%");

			MathStat tempStat = stats.get(row);

			switch (col) {
			case TEACHER_COL:
				return tempStat.getTeacher();
			case PERFORMANCE_LVL_COL:
				return tempStat.getPerfomanceLvl();
			case COUNT_COL:
				return tempStat.getCount();
			case RATIO_COL:
				return (df.format(ratio(tempStat)));
			default:
				return tempStat.getTeacher();
			}
		}

		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
	}
}

