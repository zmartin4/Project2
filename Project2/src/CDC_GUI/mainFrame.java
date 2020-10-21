package CDC_GUI;


import javax.swing.JFrame;


public class mainFrame {

	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setSize(1200, 800);
		frame.setLocation(200,200);
		frame.getContentPane().add(new mf());
		//frame.pack();
		frame.setVisible(true);
	}

}

