import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI {
	
	// GUI Components
	// Frames
	private static JFrame mainFrame;

	// Panels
	private static JPanel mainPanel;
	private static JPanel botPanel;
	private static JPanel coverPanel;
	
	// Buttons
	private static JButton newCustomerButton;
	private static JButton fpCustomerButton;
	
	public GUI(){
		// Main Frame
		mainFrame = new JFrame();
		mainFrame.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);
		
		///// Panels ////////////////////////////////////////////////
		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainPanel.setLayout(null);
		mainPanel.setVisible(false);
		
		// Cover Panel
		coverPanel = new JPanel();
		coverPanel.setBackground(Color.BLUE);
		coverPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		coverPanel.setLayout(null);
		
		// Bot Panel
		botPanel = new JPanel();
		botPanel.setBounds(82, 91, 597, 604);
		botPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
		botPanel.setLayout(null);
		
		///// Buttons ////////////////////////////////////////////////
		// New Customer Button
		newCustomerButton = new JButton("New Customer");
		newCustomerButton.setBounds(766, 476,389, 93);
		newCustomerButton.setFont(new Font("Arial", Font.PLAIN, 40));
		newCustomerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}

        });
		
		// FP Customer Button
		fpCustomerButton = new JButton("FoicePal Customer");
		fpCustomerButton.setBounds(766, 602,389, 93);
		fpCustomerButton.setFont(new Font("Arial", Font.PLAIN, 40));
		fpCustomerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }

        });
		
		// Adding components to main panel
		mainPanel.add(botPanel);
		mainPanel.add(newCustomerButton);
		mainPanel.add(fpCustomerButton);
		
		// Adding components to main window
		mainFrame.add(mainPanel);
		mainFrame.add(coverPanel);

		mainFrame.setVisible(true);
	}
	
	/**
	 *	Toggles the cover and the main panel
	*/
	protected static void toggleCoverPanel(){
		coverPanel.setVisible(!coverPanel.isVisible());
		mainPanel.setVisible(!mainPanel.isVisible());
	}

}
