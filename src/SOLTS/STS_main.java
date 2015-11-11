package SOLTS;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class STS_main {
	private JFrame frame;
	
	public static void main( String[] args ) {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				STS_main window = new STS_main();
				window.frame.setVisible(true);
			}
		});
	}
	
	public STS_main() {
		/*[DEBUG]*/
		System.out.println("Starting STS Software");
		System.out.println("initializing frame...");
		System.out.println("SecureLogin generated");
		
		initialize();
		
		secureLogin sLogin = new secureLogin(frame);
		frame.getContentPane().add(sLogin.login); 
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("STS - SOL Testing Statistics");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		frame.setBounds(100, 100, 450, 350);
		frame.setBackground(Color.decode("#757575"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
	}
}
