package SOLTS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class menuSys {

	JPanel menu;
	boolean menuCheck = false;
	
	public menuSys(final JFrame frame ) {
		
		menu = new JPanel();
		menu.setBackground(Color.decode("#757575"));
		menu.setLayout(null);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(30, 40, 80, 20);
		menu.add(btnAdd);
		
		JButton btnView = new JButton("View");
		btnView.setBounds(30, 70, 80, 20);
		menu.add(btnView);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(30, 100, 80, 20);
		menu.add(btnRun);
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				//[Debug]
				System.out.println("Add Files to Database");
				
				frame.getContentPane().remove(menu);
				curAdd add1 = null;
				try {
					add1 = new curAdd(frame);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.getContentPane().add(add1.add);
				frame.invalidate();
				frame.validate();
			}
		});
		
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				//[Debug]
				System.out.println("View Database");
				
				frame.getContentPane().remove(menu);
				curView view1 = null;
				try {
					view1 = new curView(frame);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.getContentPane().add(view1.view);
				frame.invalidate();
				frame.validate();
			}
		});
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				//[Debug]
				System.out.println("Run Database");
				
				frame.getContentPane().remove(menu);
				curStat stat1 = null;
				try {
					stat1 = new curStat(frame);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.getContentPane().add(stat1.stat);
				frame.invalidate();
				frame.validate();
			}
		});
	} 
}