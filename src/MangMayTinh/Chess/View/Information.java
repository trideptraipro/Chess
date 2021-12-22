package MangMayTinh.Chess.View;


import MangMayTinh.Chess.Model.Entity.UserInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Information extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldID;
	private JTextField textFieldName;
	private JTextField textFieldEmail;
	private JTextField textFieldPoint;
	private UserInfo userInfo;
//	private ObjectOutputStream oos;
//	private ObjectInputStream ois;

	//Method

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Information information = new Information(new UserInfo(12,"tri123","tri","trithemoonlight3@gmail.com",12));
					information.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Information(UserInfo userInfo) {
		this.userInfo=userInfo;
		setTitle("Information");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 368, 190);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.setBackground(UILib.colorBackground);
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setFont(UILib.fontLabel);
		lblNewLabel.setBounds(33, 27, 45, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setFont(UILib.fontLabel);
		lblNewLabel_1.setBounds(33, 53, 45, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Email");
		lblNewLabel_2.setFont(UILib.fontLabel);
		lblNewLabel_2.setBounds(33, 76, 76, 13);
		contentPane.add(lblNewLabel_2);
		
		textFieldID = new JTextField();
		textFieldID.setEditable(false);
		textFieldID.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldID.setBounds(106, 24, 204, 19);
		contentPane.add(textFieldID);
		textFieldID.setColumns(30);
		
		textFieldName = new JTextField();
		textFieldName.setEditable(false);
		textFieldName.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldName.setBounds(106, 50, 204, 19);
		contentPane.add(textFieldName);
		textFieldName.setColumns(30);
		
		textFieldEmail = new JTextField();
		textFieldEmail.setEditable(false);
		textFieldEmail.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldEmail.setBounds(106, 73, 204, 19);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(30);
		
		JLabel lblNewLabel_3 = new JLabel("Point");
		lblNewLabel_3.setFont(UILib.fontLabel);
		lblNewLabel_3.setBounds(33, 99, 63, 13);
		contentPane.add(lblNewLabel_3);
		
		textFieldPoint = new JTextField();
		textFieldPoint.setEditable(false);
		textFieldPoint.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		textFieldPoint.setBounds(106, 99, 204, 19);
		contentPane.add(textFieldPoint);
		textFieldPoint.setColumns(30);
		loadUI();
	}

	private void loadUI() {
		textFieldID.setText(Integer.toString(userInfo.getId()));
		textFieldName.setText(userInfo.getName());
		textFieldEmail.setText(userInfo.getEmail());
		textFieldPoint.setText(Integer.toString(userInfo.getPoint()));
	}
}
