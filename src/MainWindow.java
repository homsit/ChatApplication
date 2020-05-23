import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	public JTextField txtMessage;
	public JTextField txtTargetIp;
	public JComboBox comboBox;
	public JLabel lblMessagesLog;
	public JButton btnConnect;
	public JButton btnSend;
	public boolean btnSend_is_clicked;
	static Messenger messenger;
	static String option;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		// Frame settings
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 481);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtMessage = new JTextField();
		txtMessage.setText("Enter your message here");
		txtMessage.setBounds(125, 322, 255, 131);
		contentPane.add(txtMessage);
		txtMessage.setColumns(10);
		
		lblMessagesLog = new JLabel("");
		lblMessagesLog.setVerticalAlignment(SwingConstants.TOP);
		lblMessagesLog.setBackground(Color.WHITE);
		lblMessagesLog.setBounds(6, 58, 374, 252);
		contentPane.add(lblMessagesLog);
		
		txtTargetIp = new JTextField();
		txtTargetIp.setEnabled(false);
		txtTargetIp.setText("Enter Target ip address");
		txtTargetIp.setToolTipText("Enter Target Pc ip address");
		txtTargetIp.setBounds(6, 30, 297, 26);
		contentPane.add(txtTargetIp);
		txtTargetIp.setColumns(10);
		
		////////////////////////////////////////////////////////
		// Messenger
		option="Wait for a connection";
		btnSend_is_clicked=false;
		lblMessagesLog.setText("Wait for a connection");
		messenger= new Messenger();
		
		//////////////////////////////////////////////////////
		// btnSend
		btnSend = new JButton("Send");
		btnSend.setBounds(6, 322, 117, 131);
		contentPane.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSend_is_clicked=true;
			}
		});
		
		//////////////////////////////////////////////////////
		// btnConnect
		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.setBounds(301, 30, 79, 29);
		contentPane.add(btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				work_as_client();
			}
		});
		
		////////////////////////////////////////////////////////
		// ComboBox
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Wait for a connection", "Make a connection", "Exit"}));
		comboBox.setBounds(6, 6, 297, 27);
		contentPane.add(comboBox);
		comboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				option = (String)comboBox.getSelectedItem();
				if(option.contentEquals("Wait for a connection")) {
					work_as_Server();						
				}else if(option.equals("Make a connection")) {
					txtTargetIp.setEnabled(true);
					btnConnect.setEnabled(true);
				}else if(option.equals("Exit")){
					System.exit(0);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			}
		});
	}
	
	public void work_as_Server() {
		try{
			messenger.init_server(9988);
			while(option.contentEquals("Wait for a connection")) {
				messenger.wait_for_connection();
				String new_msgs=messenger.get_messages();
				if(!new_msgs.isEmpty()) {
					lblMessagesLog.setText(lblMessagesLog.getText() + "\n"+ new_msgs);
				}
				if(txtMessage.getText().endsWith("\n")) {
					messenger.send_message(txtMessage.getText());
					txtMessage.setText("");
					btnSend_is_clicked=false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void work_as_client() {
		try {
			lblMessagesLog.setText("starting a new chat with "+ txtTargetIp.getText());
			messenger.connect_to(txtTargetIp.getText(), 9998);
			while(option.contentEquals("Make a connection")) {
				String new_msgs=messenger.get_messages();
				if(!new_msgs.isEmpty()) {
					lblMessagesLog.setText(lblMessagesLog.getText() + "\n"+ new_msgs);
				}
				if(txtMessage.getText().endsWith("\n")) {
					messenger.send_message(txtMessage.getText());
					txtMessage.setText("");
					btnSend_is_clicked=false;
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
