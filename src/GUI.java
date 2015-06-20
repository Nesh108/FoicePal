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
	
	// Tables
	private static JTable productTable;
	String productTableColumns[] = {"Product ID","Product Photo","Product Name", "Quantity", "Price"};
	JScrollPane productTableScrollPane;
	DefaultTableModel productTableModel;
	
	// TODO: implement voice
	private static boolean isVoiceRecognized = true;
	private static boolean isFaceRecognized = false;
	
	private static String customer_email = null;
	private static String customer_name = null;
	
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
            	toggleShoppingPanel();
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
		
		
		
		// Adding components to main panel
		mainPanel.add(botPanel);
		mainPanel.add(newCustomerButton);
		mainPanel.add(fpCustomerButton);
		
		// Adding components to shopping panel
		shoppingPanel.add(productTableScrollPane);
		
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
		
		if(GUI.customer_email == null)
			GUI.customer_email = customer_email;
		else if(!GUI.customer_email.equals(customer_email))
			return;	// mismatching email
		
		isVoiceRecognized = true;
		
		if(isFaceRecognized)
			customerRecognized();
	}
	
	protected static void setFaceRecognized(String customer_name, String customer_email){
		
		if(GUI.customer_email == null)
			GUI.customer_email = customer_email;
		else if(!GUI.customer_email.equals(customer_email))
			return;	// mismatching email
		
		GUI.customer_name = customer_name;
		
		isFaceRecognized = true;

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
		
		// Resets recognition
		Config.runRecognition = true;
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
