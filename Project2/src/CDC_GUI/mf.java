package CDC_GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Button;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JLayeredPane;

public class mf extends JPanel implements ActionListener {

	

	private JPasswordField passwordField;

	private static String LOGIN = "Login";
	//private DatabaseManagerGUI  DBScreen;

	private int width = 1200, height = 800;
	int xx,xy;
	Color cdcBlue = new Color(0, 65, 119);
	JPanel contentPanel;
	JPanel logoPanel;

	
	
	

	/**
	 * Create the frame.
	 */
	public mf() {

		//because it is a gridlayout, you do not see to set bounds for the panels
		super(new GridLayout(1,2));

	
		
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.white);
		contentPanel.setLayout(null);
		
		
		//creates left logo panel
		logoPanel = new JPanel();
		logoPanel.setBackground(cdcBlue);
		logoPanel.setLayout(null);
		
		add(logoPanel);
		add(contentPanel);
		
	

		
		//Authorization Key Label
		JLabel authKey = new JLabel("Authorization Key" );
		authKey.setFont(new Font("Geneva", Font.PLAIN, 24));
		authKey.setBounds(120, 250, 200, 40);  //can use setLocation() and setSize() as well
		contentPanel.add(authKey);
		
		//Password Submission Field : CONTENT PANEL
		passwordField = new JPasswordField();
		passwordField.setBounds(120, 300, 320, 35); //can use setLocation() and setSize() as well
	    passwordField.setActionCommand(LOGIN);
	    passwordField.addActionListener(this);
		contentPanel.add(passwordField);
	
		//LoginButton that submits key : CONTENT PANEL
		Button loginButton = new Button("Login");
		loginButton.setActionCommand(LOGIN);
		loginButton.addActionListener(this);
		loginButton.setBackground(cdcBlue);
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Geneva", Font.PLAIN, 16));

		loginButton.setBounds(120, 350, 320, 45);
		contentPanel.add(loginButton);
	
		
		
		
		//ExitButton : LOGO PANEL
		JButton exitButton = new JButton("Exit");
		exitButton.setBackground(Color.WHITE);
		exitButton.setBounds(10,10,90,30);   //can use setLocation() and setSize() as well
		exitButton.setForeground(Color.BLACK);
		exitButton.setFont(new Font("Geneva", Font.PLAIN, 20));
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				System.exit(0);
			}
		});
		logoPanel.add(exitButton);

		//adds logo to the panel : LOGO PANEL
		JLabel logo = new JLabel("");
		ImageIcon LogoIcon = new ImageIcon (this.getClass().getResource("/CDC_GUI/CDC-Logo2.jpg"));
		logo.setIcon(LogoIcon);
		logo.setBounds(100,120,LogoIcon.getIconWidth(), LogoIcon.getIconHeight());  //can use setLocation() and setSize() as well
		logoPanel.add(logo);
		
		
		

		//Allows the Panel to be moved around
		contentPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				 xx = e.getX();
			     xy = e.getY();
			}
		}); 
		
	
	}
	
	public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (LOGIN.equals(cmd)) { //Process the password.
            char[] input = passwordField.getPassword();
            if (isPasswordCorrect(input)) {
            	
            		removeAll();
            		add(new DatabaseManagerGUI());
            		repaint();
            		
            		revalidate();
            		
            } else {
                JOptionPane.showMessageDialog(
                  contentPanel, "Invalid password. Try again.",
                   "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            }

            //Zero out the possible password, for security.
            Arrays.fill(input, '0');
            passwordField.selectAll();
            
     
        }
    }
 
	private static boolean isPasswordCorrect(char[] input) {
        boolean isCorrect = true;
        char[] correctPassword = { 'h', 'i' };

        if (input.length != correctPassword.length) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals (input, correctPassword);
        }

        //Zero out the password.
        Arrays.fill(correctPassword,'0');

        return isCorrect;
    }
}

