package SOLTS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class curAdd {
	JPanel add;
	private JLabel lblTeacher;
	private JButton btnMath, btnEnglish, btnBack;
	private JTextArea linkPath;
	private JScrollPane impDrop;
	private db db_conn;
	private JTextField txtTeacher;
	
	public curAdd(final JFrame frame) throws Exception {
		db_conn = new db();
		
		add = new JPanel();
		add.setBackground(Color.decode("#757575"));
		add.setLayout(null);
		
		linkPath = new JTextArea();
		linkPath.setBounds(25, 25, 275, 125);
		linkPath.setBackground(Color.WHITE);
		linkPath.setEditable(true);
		
		impDrop = new JScrollPane( linkPath );
		impDrop.setBackground(Color.WHITE);
		impDrop.setBounds(25, 25, 275, 125);
		add.add(impDrop);
		
		new FileDrop(impDrop, new FileDrop.Listener() {
			public void filesDropped( File[] files) {
				for(int i = 0; i < files.length;  i++) {
					try {
						linkPath.append(files[i].getCanonicalPath());
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		lblTeacher = new JLabel("Teacher Name:");
		lblTeacher.setForeground(Color.WHITE);
		lblTeacher.setBounds(25, 160, 193, 25);
		add.add(lblTeacher);
		
		txtTeacher = new JTextField();
		txtTeacher.setBounds(25, 183, 275, 20);
		add.add(txtTeacher);
		
		btnMath = new JButton("Math Import");
		btnMath.setBounds(310, 25, 115, 25);
		btnMath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
				String teach = txtTeacher.getText();
				if(teach.equals("") || teach.equals(" ")) {
					lblTeacher.setBounds(25, 160, 150, 25);
					lblTeacher.setText( "Teacher Name is Invalid..." );
					txtTeacher.setText( "" );
				}
				else {
					List<String> lines = Arrays.asList(linkPath.getText().split("\n"));	
					
					/*[DEBUG]*/
					for(String filename : lines) {
						System.out.println(filename);
					}
					
					try {
						importToDB importer = new importToDB(lines, teach);
						List<String> csv = importer.csvfiles;
						db_conn.insertStudent(csv,"M",importer.getTeacher(),importer.getYear());
						db_conn.removeRetakes("M");
						db_conn.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
		    }
		});
		add.add(btnMath);
		
		btnEnglish = new JButton("English Import");
		btnEnglish.setBounds(310, 75, 115, 25);
		add.add(btnEnglish);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(355, 200, 70, 25);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
		    {    
            	try {
            		frame.getContentPane().remove( add );
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
		add.add(btnBack);
	}
}
