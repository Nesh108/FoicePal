import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class GUI {
	
	// GUI Components
	// Frames
	private static JFrame mainFrame;

	// Panels
	private static JPanel mainPanel;
	private static JPanel botPanel;
	private static JPanel coverPanel;
	private static JPanel shoppingPanel;
	
	// Buttons
	private static JButton newCustomerButton;
	private static JButton fpCustomerButton;
	private static JButton backToMainPanelButton;
	private static JButton checkoutButton;
	
	// Tables
	private static JTable productTable;
	String productTableColumns[] = {"Product ID","Product Photo","Product Name", "Quantity", "Price"};
	JScrollPane productTableScrollPane;
	DefaultTableModel productTableModel;
	
	// Icon
	private static ImageIcon redIcon;
	private static ImageIcon yellowIcon;
	private static ImageIcon greenIcon;
	
	// Labels
	private static JLabel faceRecognitionIconLabel;
	private static JLabel voiceRecognitionIconLabel;
	
	// TODO: implement voice
	private static boolean isVoiceRecognized = true;
	private static boolean isFaceRecognized = false;
	
	private static String customer_email = null;
	private static String customer_name = null;
	
	private static boolean isFPCustomer = false;
	
	private static Thread SRTask = new Thread(new FPTasks.SpeakerRecognitionTask());
	private static Thread FRTask = new Thread(new FPTasks.FaceRecognitionTask());
	private static Thread MTask = new Thread(new FPTasks.MotionRecognitionTask());

	
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
		
		// Shopping Panel
		shoppingPanel = new JPanel();
		shoppingPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		shoppingPanel.setLayout(null);
		shoppingPanel.setVisible(false);
		
		///// Buttons ////////////////////////////////////////////////
		// New Customer Button
		newCustomerButton = new JButton("New Customer");
		newCustomerButton.setBounds(766, 476,389, 93);
		newCustomerButton.setFont(new Font("Arial", Font.PLAIN, 40));
		newCustomerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Config.runRecognition = false;
				isFPCustomer = false;
				toggleShoppingPanel();
			}

        });
		
		// FP Customer Button
		fpCustomerButton = new JButton("FoicePal Customer");
		fpCustomerButton.setBounds(766, 602,389, 93);
		fpCustomerButton.setFont(new Font("Arial", Font.PLAIN, 40));
		fpCustomerButton.setVisible(false);
		fpCustomerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	isFPCustomer = true;
            	toggleShoppingPanel();
            }

        });
		
		// Back to Main menu
		backToMainPanelButton = new JButton("Back");
		backToMainPanelButton.setBounds(66, 993,389, 93);
		backToMainPanelButton.setFont(new Font("Arial", Font.PLAIN, 40));
		backToMainPanelButton.setVisible(false);
		backToMainPanelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	clearGUI(false);
            }

        });
		
		// Checkout
		checkoutButton = new JButton("Checkout");
		checkoutButton.setBounds(1467, 993,389, 93);
		checkoutButton.setFont(new Font("Arial", Font.PLAIN, 40));
		checkoutButton.setVisible(false);
		checkoutButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }

        });
		///// Tables ////////////////////////////////////////////////
		// Product table
		setupProductTable();
		
		ImageIcon image;
		try {
			image = new ImageIcon(Tools.getScaledImage(ImageIO.read(getClass().getResourceAsStream("/res/mouse.jpg")), 200, 200));
			
			Object[] objs = {"1010101",image,"Mouse", "1", "20.00E"};
			productTableModel.addRow(objs);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		///// Labels ////////////////////////////////////////////////
		// Recognition Icon Labels
		redIcon = new ImageIcon("/res/red_icon.png");
		yellowIcon = new ImageIcon("/res/yellow_icon.png");
		greenIcon = new ImageIcon("/res/green_icon.png");
		
		voiceRecognitionIconLabel = new JLabel(yellowIcon);
		voiceRecognitionIconLabel.setBounds(79, 1070, 80, 80);
		faceRecognitionIconLabel = new JLabel(yellowIcon);
		faceRecognitionIconLabel.setBounds(166, 1070, 80, 80);

		// Adding components to main panel
		mainPanel.add(botPanel);
		mainPanel.add(newCustomerButton);
		mainPanel.add(fpCustomerButton);
		mainPanel.add(voiceRecognitionIconLabel);
		mainPanel.add(faceRecognitionIconLabel);
		
		// Adding components to shopping panel
		shoppingPanel.add(productTableScrollPane);
		shoppingPanel.add(backToMainPanelButton);
		shoppingPanel.add(checkoutButton);
		
		// Adding components to main window
		mainFrame.add(mainPanel);
		mainFrame.add(coverPanel);
		mainFrame.add(shoppingPanel);
		
		mainFrame.setVisible(true);
	}
	
	private void setupProductTable(){
		productTableModel = new DefaultTableModel(productTableColumns, 0){
		    @Override
		    public Class<?> getColumnClass(int column) {
		        switch(column) {
		            case 0: return String.class;
		            case 1: return ImageIcon.class;
		            case 2: return String.class;
		            case 3: return String.class;
		            case 4: return String.class;
		            default: return Object.class;
		        }
		    }
		    
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		    
		};
		
		productTable = new JTable(productTableModel);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		productTable.setDefaultRenderer(Object.class, centerRenderer);
		
		productTable.setBounds(0, 0, 1144, 886);
		productTable.setFont(new Font("Arial", Font.PLAIN, 35));
		productTable.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 35));
		productTable.setRowHeight(200);
		productTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		productTable.getColumnModel().getColumn(1).setPreferredWidth(240);
		productTable.getColumnModel().getColumn(2).setPreferredWidth(320);
		productTable.getColumnModel().getColumn(3).setPreferredWidth(180);
		productTable.getColumnModel().getColumn(4).setPreferredWidth(160);
		
		productTableScrollPane = new JScrollPane(productTable);
		productTableScrollPane.setBounds(712, 92, 1144, 886);
		productTableScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
	}
	/**
	 *	Toggles the cover and the main panel
	*/
	protected static void toggleCoverPanel(){
		coverPanel.setVisible(!coverPanel.isVisible());
		mainPanel.setVisible(!mainPanel.isVisible());
	}
	
	protected static void showFPCustomerButton(boolean b){
		fpCustomerButton.setVisible(b);
	}
	
	protected static void setVoiceRecognized(String customer_email){
		
		// Customer not recognized
		if(customer_email == null)
		{
			voiceRecognitionIconLabel.setIcon(redIcon);
			return;
		}
		
		if(GUI.customer_email == null)
			GUI.customer_email = customer_email;
		else if(!GUI.customer_email.equals(customer_email))
			{
				faceRecognitionIconLabel.setIcon(redIcon);
				voiceRecognitionIconLabel.setIcon(redIcon);
				
				if(Config.DEBUG)
					System.out.println("Email mismatch!");
				
				return;	// mismatching email
			}
		
		isVoiceRecognized = true;
		voiceRecognitionIconLabel.setIcon(greenIcon);
		
		if(isFaceRecognized)
			customerRecognized();
	}
	
	protected static void setFaceRecognized(String customer_name, String customer_email){
		
		// Customer not recognized
		if(customer_name == null || customer_email == null)
		{
			faceRecognitionIconLabel.setIcon(redIcon);
			return;
		}
		
		if(GUI.customer_email == null)
			GUI.customer_email = customer_email;
		else if(!GUI.customer_email.equals(customer_email))
			{
				faceRecognitionIconLabel.setIcon(redIcon);
				voiceRecognitionIconLabel.setIcon(redIcon);
				
				if(Config.DEBUG)
					System.out.println("Email mismatch!");
								
				return;	// mismatching email
			}
		
		GUI.customer_name = customer_name;
		
		isFaceRecognized = true;

		faceRecognitionIconLabel.setIcon(greenIcon);
		
		if(isVoiceRecognized)
			customerRecognized();
	}
	
	protected static void clearGUI(boolean goToCover){
		isVoiceRecognized = false;
		isFaceRecognized = false;
		showFPCustomerButton(false);
		shoppingPanel.setVisible(false);
		
		// Checks if it needs to go to cover panel or not
		mainPanel.setVisible(!goToCover);
		coverPanel.setVisible(goToCover);

		faceRecognitionIconLabel.setIcon(null);
		voiceRecognitionIconLabel.setIcon(null);
		
		// Resets recognition
		Config.runRecognition = true;
		
		if(!goToCover)
		{
			SRTask.start();
			FRTask.start();
		}
		else
			MTask.start();
	}
	
	private static void customerRecognized(){
		showFPCustomerButton(true);

		Tools.speakText("Hey: " + customer_name + ". Welcome back!");
	}
	
	private static void toggleShoppingPanel(){
		shoppingPanel.setVisible(!shoppingPanel.isVisible());
		mainPanel.setVisible(!mainPanel.isVisible());
	}
	

}
