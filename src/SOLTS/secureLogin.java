package SOLTS;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

public class secureLogin {
	JPanel login;
	JButton btnLoginSubmit;
	JTextField txtFieldUser;
	JPasswordField ptxtFieldPass;
	JLabel lblLogin, lblUser, lblPass;
	
	boolean check = false;
	
	public secureLogin( final JFrame frame ) {
		
		// TODO: remove when done checking
		LC layC = new LC().fill().wrap();
		AC colC = new AC().align("right", 0).fill(1, 3).grow(100, 1, 3).align("right", 2).gap("15", 1);
		AC rowC = new AC().index(6).gap("15!").align("top").grow(100, 8);
		
		login = new JPanel();
		
		login.setBackground( Color.decode( "#757575" ) );
		login.setLayout( new MigLayout(layC, colC, rowC) );		
		
		lblLogin = new JLabel( "Login" );
		lblLogin.setForeground( Color.WHITE );
		//lblLogin.setBounds( 200, 30, 40, 20 );
		login.add( lblLogin, new CC().spanX(2).growX(0).wrap() );
		
		lblUser = new JLabel( "Username:" );
		lblUser.setForeground( Color.WHITE );
		//lblUser.setBounds( 60, 60, 70, 14 );
		login.add( lblUser );
		
		txtFieldUser = new JTextField();
		txtFieldUser.setBackground( Color.LIGHT_GRAY );
		txtFieldUser.setColumns( 10 );
		//txtFieldUser.setBounds( 125, 60, 200, 15 );
		login.add( txtFieldUser, new CC().spanX().grow() );		

		lblPass = new JLabel( "Password:" );
		lblPass.setForeground( Color.WHITE );
		//lblPass.setBounds( 60, 90, 70, 14 );
		login.add( lblPass );
		
		ptxtFieldPass = new JPasswordField();
		ptxtFieldPass.setEchoChar( '*' );
		ptxtFieldPass.setBackground( Color.LIGHT_GRAY );
		//ptxtFieldPass.setBounds( 125, 90, 200, 15 );
		login.add( ptxtFieldPass, new CC().spanX().grow()  );
		
		btnLoginSubmit = new JButton( "Submit" );
		//btnLoginSubmit.setBounds( 243, 120, 80, 20 );
		login.add( btnLoginSubmit, new CC().spanX(4) );

		ptxtFieldPass.addKeyListener( new KeyAdapter() {
			public void keyReleased( KeyEvent e ) {
				if( e.getKeyChar() == KeyEvent.VK_ENTER ) {
					try {
						securityCheck( frame );
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		btnLoginSubmit.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event ) {
				try {
					securityCheck( frame );
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private boolean loginSubmission(String u, String p) throws FileNotFoundException, IOException {
		//get sys properties
		Properties props = new Properties();
		props.load(new FileInputStream("sts.properties"));
				
		String user = props.getProperty("username");
		String pass = props.getProperty("password");
		
		if( u.equals( user ) && p.equals( pass )) {
			return true;
		}
		return false;
	}
	
	private void securityCheck( JFrame frame ) throws FileNotFoundException, IOException {
		String uname = txtFieldUser.getText();
		String pword = new String( ptxtFieldPass.getPassword() );
		
		check = loginSubmission( uname, pword );
		if( check ){	
			/*[DEBUG]*/
			System.out.println( "User: " + uname + " Password: " + pword );
			
			frame.getContentPane().remove( login );
			menuSys menu1 = new menuSys( frame );
			frame.getContentPane().add( menu1.menu );
			frame.invalidate();
			frame.validate();
		}
		else {
			lblLogin.setBounds( 150, 30, 200, 20 );
			lblLogin.setText( "Login Failed...Try Again" );
			
			/*[DEBUG]*/
			System.err.println("Invalid username and password: " + uname + " " + pword);
			
			txtFieldUser.setText( "" );
			ptxtFieldPass.setText( "" );
		}
	}
}
