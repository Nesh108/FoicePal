import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	protected static mVideoImage botPanel;
	private static JPanel coverPanel;
	public static JPanel shoppingPanel;
	private static JPanel checkoutPanel;

	// Buttons
	private static JButton newCustomerButton;
	private static JButton fpCustomerButton;
	private static JButton backToMainPanelButton;
	private static JButton checkoutButton;
	private static JButton FPcheckoutButton;

	// Tables
	private static JTable productTable;
	String productTableColumns[] = { "Product ID", "Product Photo",
			"Product Name", "Price", "Currency" };
	JScrollPane productTableScrollPane;
	private static DefaultTableModel productTableModel;

	// Icon
	private static ImageIcon redIcon;
	private static ImageIcon yellowIcon;
	private static ImageIcon greenIcon;

	private static ImageIcon voiceIcon;
	private static ImageIcon faceIcon;
	private static ImageIcon goodIcon;
	private static ImageIcon badIcon;
	// Labels
	private static JLabel faceRecognitionIconLabel;
	private static JLabel voiceRecognitionIconLabel;
	private static JLabel totalPriceLabel;
	private static JLabel statusLabel;
	private static JLabel transactionLabel;
	private static JLabel voiceIconLabel;
	private static JLabel faceIconLabel;

	private static JLabel transactionResultLabel;

	private static double totalPrice = 0.00;

	// TODO: implement voice
	public static boolean isVoiceRecognized = false;
	public static boolean isFaceRecognized = false;

	private static String customer_data = null;
	private static String customer_name = null;

	private static boolean isFPCustomer = false;

	private static Thread SRTask = new Thread(
			new FPTasks.SpeakerRecognitionTask());
	private static Thread FRTask = new Thread(new FPTasks.FaceRecognitionTask());
	private static Thread MTask = new Thread(
			new FPTasks.MotionRecognitionTask());
	private static Thread PayTask;
	private static Thread ScannerTask;
	private static Thread BotTask;
	private static Thread ChatterBotTask;

	private static ArrayList<Object[]> shoppingChart = new ArrayList<Object[]>();
	private static ArrayList<Object[]> productsList;

	public GUI() {

		javax.swing.UIManager.put("OptionPane.font", new Font("Arial",
				Font.PLAIN, 35));

		// Main Frame
		mainFrame = new JFrame();
		mainFrame.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);

		// /// Panels ////////////////////////////////////////////////
		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		mainPanel.setLayout(null);
		mainPanel.setVisible(false);

		// Cover Panel
		coverPanel = new ImagePanel("res/cover.jpg");
		coverPanel.setBackground(Color.BLUE);
		coverPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		coverPanel.setLayout(null);

		// Bot Panel
		botPanel = new mVideoImage();
		botPanel.setBounds(66, 91, 613, 806);
		botPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

		// Shopping Panel
		shoppingPanel = new JPanel();
		shoppingPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		shoppingPanel.setLayout(null);
		shoppingPanel.setVisible(false);

		// Checkout Panel
		checkoutPanel = new JPanel();
		checkoutPanel.setBounds(0, 0, Config.WIDTH, Config.HEIGTH);
		checkoutPanel.setLayout(null);
		checkoutPanel.setVisible(false);

		// /// Buttons ////////////////////////////////////////////////
		// New Customer Button
		newCustomerButton = new JButton("Normal Customer");
		newCustomerButton.setBounds(766, 476, 389, 93);
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
		fpCustomerButton.setBounds(766, 602, 389, 93);
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
		backToMainPanelButton.setBounds(66, 983, 389, 93);
		backToMainPanelButton.setFont(new Font("Arial", Font.PLAIN, 40));
		backToMainPanelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Config.runScanner = false;
				shoppingChart.clear();
				clearGUI(false);
			}

		});

		// Normal Checkout
		checkoutButton = new JButton("Normal Checkout");
		checkoutButton.setBounds(1467, 983, 389, 93);
		// checkoutButton.setBounds(100, 93,389, 93);
		checkoutButton.setFont(new Font("Arial", Font.PLAIN, 40));
		checkoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Config.runScanner = false;

				// Accepted by default
				goToCheckoutPanel(true, null);
			}

		});

		// Checkout with FoicePal
		FPcheckoutButton = new JButton("FoicePal Checkout");
		FPcheckoutButton.setBounds(1040, 983, 389, 93);
		// FPcheckoutButton.setBounds(100, 208,389, 93);
		FPcheckoutButton.setFont(new Font("Arial", Font.PLAIN, 40));
		FPcheckoutButton.setVisible(false);
		FPcheckoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFPCustomer) {
					Config.runScanner = false;
					totalPriceLabel.setText("Processing...");
					PayTask = new Thread(new FPTasks.PaymentTask());
					PayTask.start();
				} else if (Config.DEBUG)
					System.out.println("Payment: This should never happen.");
			}

		});
		// /// Tables ////////////////////////////////////////////////
		// Product table
		productsList = new ProductLoader().getProductList();
		setupProductTable();

		// /// Labels ////////////////////////////////////////////////
		// Recognition Icon Labels
		redIcon = new ImageIcon(getClass().getResource("/res/red_icon.png"));
		yellowIcon = new ImageIcon(getClass().getResource(
				"/res/yellow_icon.png"));
		greenIcon = new ImageIcon(getClass().getResource("/res/green_icon.png"));

		voiceRecognitionIconLabel = new JLabel(yellowIcon);
		voiceRecognitionIconLabel.setBounds(166, 970, 100, 100);
		faceRecognitionIconLabel = new JLabel(yellowIcon);
		faceRecognitionIconLabel.setBounds(79, 970, 100, 100);

		voiceIcon = new ImageIcon(getClass().getResource("/res/voice_icon.png"));
		faceIcon = new ImageIcon(getClass().getResource("/res/face_icon.png"));
		voiceIconLabel = new JLabel(voiceIcon);
		voiceIconLabel.setBounds(166, 890, 100, 100);

		faceIconLabel = new JLabel(faceIcon);
		faceIconLabel.setBounds(79, 890, 100, 100);

		goodIcon = new ImageIcon(getClass().getResource("/res/good.jpg"));
		badIcon = new ImageIcon(getClass().getResource("/res/bad.jpg"));
		transactionResultLabel = new JLabel();
		transactionResultLabel.setBounds(1034, 526, 458, 437);
		
		// Total Shopping Chart
		totalPriceLabel = new JLabel("Total: 0.00 EUR");
		totalPriceLabel.setFont(new Font("Arial", Font.PLAIN, 40));
		totalPriceLabel.setBounds(496, 993, 453, 74);
		// totalPriceLabel.setBounds(94, 99, 453, 74);

		// Total Shopping Chart
		statusLabel = new JLabel("Payment Completed!");
		statusLabel.setFont(new Font("Arial", Font.PLAIN, 40));
		statusLabel.setBounds(750, 214, 632, 74);

		// Total Shopping Chart
		transactionLabel = new JLabel("Transaction: ");
		transactionLabel.setFont(new Font("Arial", Font.PLAIN, 40));
		transactionLabel.setBounds(750, 367, 1068, 74);
		transactionLabel.setVisible(false);

		// Adding components to main panel
		mainPanel.add(botPanel);
		mainPanel.add(newCustomerButton);
		mainPanel.add(fpCustomerButton);
		mainPanel.add(voiceRecognitionIconLabel);
		mainPanel.add(faceRecognitionIconLabel);
		mainPanel.add(faceIconLabel);
		mainPanel.add(voiceIconLabel);

		// Adding components to shopping panel

		shoppingPanel.add(productTableScrollPane);
		shoppingPanel.add(backToMainPanelButton);
		shoppingPanel.add(checkoutButton);
		shoppingPanel.add(FPcheckoutButton);
		shoppingPanel.add(totalPriceLabel);

		// Adding components to checkout panel

		checkoutPanel.add(statusLabel);
		checkoutPanel.add(transactionLabel);
		checkoutPanel.add(transactionResultLabel);
		// Adding components to main window

		mainFrame.add(mainPanel);
		mainFrame.add(coverPanel);
		mainFrame.add(shoppingPanel);
		mainFrame.add(checkoutPanel);

		mainFrame.setVisible(true);
	}

	private void setupProductTable() {
		productTableModel = new DefaultTableModel(productTableColumns, 0) {
			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return ImageIcon.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				default:
					return Object.class;
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		productTable = new JTable(productTableModel);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		productTable.setDefaultRenderer(Object.class, centerRenderer);

		productTable.setBounds(0, 0, 1144, 886);
		productTable.setFont(new Font("Arial", Font.PLAIN, 35));
		productTable.getTableHeader()
				.setFont(new Font("Arial", Font.PLAIN, 35));
		productTable.setRowHeight(200);
		productTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		productTable.getColumnModel().getColumn(1).setPreferredWidth(240);
		productTable.getColumnModel().getColumn(2).setPreferredWidth(320);
		productTable.getColumnModel().getColumn(3).setPreferredWidth(140);
		productTable.getColumnModel().getColumn(4).setPreferredWidth(150);

		productTableScrollPane = new JScrollPane(productTable);
		productTableScrollPane.setBounds(712, 92, 1144, 886);
		productTableScrollPane.setBorder(BorderFactory.createLineBorder(
				Color.GRAY, 1, true));

		productTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				int row = productTable.rowAtPoint(evt.getPoint());

				int dialogResult = JOptionPane.showConfirmDialog(null,
						"Would You Like to Remove: '"
								+ shoppingChart.get(row)[2] + "'?", "Warning",
						JOptionPane.WARNING_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					removeProd((String) shoppingChart.get(row)[0], row);
				}

			}
		});
	}

	protected static void removeProd(String code, int row) {
		for (int i = 0; i < productsList.size(); i++) {
			if (productsList.get(i)[0].equals(code)) {
				removeFromShoppingChart(productsList.get(i), row);
				System.out.println("FOUND ITEM");
				break;
			}
		}
	}

	protected static void removeFromShoppingChart(Object[] prod, int row) {

		totalPrice -= Double.parseDouble((String) prod[3]);
		totalPriceLabel.setText("Total: " + totalPrice + " EUR");
		shoppingChart.remove(row);
		productTableModel.removeRow(row);
	}

	protected static void addProd(String code) {
		for (int i = 0; i < productsList.size(); i++) {
			if (productsList.get(i)[0].equals(code)) {
				addToShoppingChart(productsList.get(i));
				System.out.println("FOUND ITEM");
				break;
			}
		}

	}

	protected static void addToShoppingChart(Object[] prod) {

		totalPrice += Double.parseDouble((String) prod[3]);
		totalPriceLabel.setText("Total: " + totalPrice + " EUR");
		shoppingChart.add(prod);
		productTableModel.addRow(prod);
	}

	/**
	 * Toggles the cover and the main panel
	 */
	protected static void toggleCoverPanel() {
		coverPanel.setVisible(!coverPanel.isVisible());
		mainPanel.setVisible(!mainPanel.isVisible());

		if (mainPanel.isVisible()) {
			Config.runBot = true;
			BotTask = new Thread(new FPTasks.BotControllerTask());
			BotTask.start();
			ChatterBotTask = new Thread(new FPTasks.ChatterBotTask());
			ChatterBotTask.start();

		}
	}

	protected static void showFPCustomerButton(boolean b) {
		fpCustomerButton.setVisible(b);

	}

	protected static void setVoiceRecognized(String customer_email) {

		// Customer not recognized
		if (customer_email == null) {
			voiceRecognitionIconLabel.setIcon(redIcon);
			return;
		}

		if (GUI.customer_data == null)
			GUI.customer_data = Tools.convertTagIntoEmail(customer_email);
		else if (!GUI.customer_data.equals(Tools
				.convertTagIntoEmail(customer_email))) {
			faceRecognitionIconLabel.setIcon(redIcon);
			voiceRecognitionIconLabel.setIcon(redIcon);

			if (Config.DEBUG)
				System.out.println("Email mismatch!");

			return; // mismatching email
		}

		isVoiceRecognized = true;
		Tools.speak("I recognize your voice!");
		voiceRecognitionIconLabel.setIcon(greenIcon);

		if (isFaceRecognized)
			customerRecognized();
	}

	protected static void setFaceRecognized(String customer_name,
			String customer_email) {

		// Customer not recognized
		if (customer_name == null || customer_email == null) {
			faceRecognitionIconLabel.setIcon(redIcon);
			return;
		}

		if (GUI.customer_data == null)
			GUI.customer_data = customer_email;
		else if (!GUI.customer_data.equals(customer_email)) {
			faceRecognitionIconLabel.setIcon(redIcon);
			voiceRecognitionIconLabel.setIcon(redIcon);

			if (Config.DEBUG)
				System.out.println("Email mismatch!");

			return; // mismatching email
		}

		GUI.customer_name = customer_name;

		isFaceRecognized = true;
		Tools.speak("I recognize your face!");
		faceRecognitionIconLabel.setIcon(greenIcon);

		if (isVoiceRecognized)
			customerRecognized();
	}

	protected static void clearGUI(boolean goToCover) {
		isVoiceRecognized = false;
		isFaceRecognized = false;
		showFPCustomerButton(false);
		shoppingPanel.setVisible(false);

		// Checks if it needs to go to cover panel or not
		mainPanel.setVisible(!goToCover);
		coverPanel.setVisible(goToCover);

		if (mainPanel.isVisible())
			mainPanel.add(botPanel);

		faceRecognitionIconLabel.setIcon(yellowIcon);
		voiceRecognitionIconLabel.setIcon(yellowIcon);

		// Resets recognition
		Config.runRecognition = true;

		if (!goToCover) {
			SRTask = new Thread(new FPTasks.SpeakerRecognitionTask());
			FRTask = new Thread(new FPTasks.FaceRecognitionTask());
		} else {
			MTask = new Thread(new FPTasks.MotionRecognitionTask());
		}
	}

	private static void customerRecognized() {
		showFPCustomerButton(true);

		if (!isFPCustomer) {
			isFPCustomer = true;
			Tools.speak("Hey: " + customer_name.split(" ")[0]
					+ ". Welcome back!");
		}
	}

	private static void toggleShoppingPanel() {
		shoppingPanel.setVisible(!shoppingPanel.isVisible());
		mainPanel.setVisible(!mainPanel.isVisible());

		FPcheckoutButton.setVisible(isFPCustomer);

		if (shoppingPanel.isVisible()) {
			Config.runScanner = true;
			ScannerTask = new Thread(new FPTasks.ProductScannerTask());
			ScannerTask.start();
			shoppingPanel.add(botPanel);

			Tools.speak("So, how are you today?");
			Config.prev_question = "HOW_ARE_YOU";
		} else
			mainPanel.add(botPanel);
	}

	protected static void goToCheckoutPanel(boolean payment_accepted,
			String transaction_id) {
		shoppingPanel.setVisible(false);
		checkoutPanel.setVisible(true);
		checkoutPanel.add(botPanel);

		if (!payment_accepted)
			{
				statusLabel.setText("Payment Rejected.");
				transactionResultLabel.setIcon(badIcon);

				Tools.speak("There was a problem with your payment");
			}

		if (transaction_id != null) {
			transactionResultLabel.setIcon(goodIcon);
			transactionLabel.setText("Your Transaction ID: " + transaction_id);
			transactionLabel.setVisible(true);

			Tools.speak("Great! Payment completed!");
		}

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				Tools.speak(Speech
						.getRandomString(Speech.GREETINGS_OUTRO_TYPE));
				goToCoverPanel();
			}
		}, 10000);
	}

	protected static void goToCoverPanel() {
		transactionLabel.setVisible(false);
		mainPanel.add(botPanel);

		Config.runBot = false;

		checkoutPanel.setVisible(false);
		clearGUI(true);

		// Restarting the motion detector
		MTask.start();
	}

	protected static String getCustomerName() {
		return customer_name;

	}

	protected static String getCustomerData() {
		return customer_data;

	}

	protected static ArrayList<Object[]> getShoppingChart() {
		return shoppingChart;
	}

	class ImagePanel extends JPanel {

		private Image img;

		public ImagePanel(String img) {
			this(new ImageIcon(img).getImage());
		}

		public ImagePanel(Image img) {
			this.img = img;
			Dimension size = new Dimension(img.getWidth(null),
					img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}

		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}

	}
}
