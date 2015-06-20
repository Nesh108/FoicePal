import javax.swing.JFrame;
import javax.swing.JPanel;


public class GUI {
	
	// GUI Components
	private JFrame mainFrame;
	private JPanel mainPanel;
	
	public GUI(){
		// Main Frame
		mainFrame = new JFrame();
		mainFrame.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);
		
		// Panels
		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainPanel.setLayout(null);
		
		// Adding components to main window
		mainFrame.add(mainPanel);
		
		mainFrame.setVisible(true);
	}

}
