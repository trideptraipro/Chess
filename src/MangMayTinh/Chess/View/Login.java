package MangMayTinh.Chess.View;



import MangMayTinh.Chess.Connection.Client.Client;
import MangMayTinh.Chess.Model.Entity.Account;
import MangMayTinh.Chess.Model.Entity.UserInfo;
import MangMayTinh.Chess.Model.Enum.MessageType;
import MangMayTinh.Chess.Packet.LoginPacket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField tFusername;
	private JTextField tFpass;
	private Socket socket;
	private Register register;
	private ObjectOutputStream sender;
	private ObjectInputStream receiver;
	private boolean isOn=true;
	private UserInfo userInfo=null;
	public ObjectOutputStream getSender() {
		return sender;
	}

	public ObjectInputStream getReceiver() {
		return receiver;
	}

	private Thread listener;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean on) {
		isOn = on;
	}

	public Thread getListener() {
		return listener;
	}

	public Socket getSocket() {
		return socket;
	}

	/**
	 * Create the frame.
	 */

	public Login() {
		try{
			this.socket=new Socket("116.110.15.77",5555);
			this.sender= new ObjectOutputStream(socket.getOutputStream());
			this.receiver= new ObjectInputStream(socket.getInputStream());

		}catch (Exception e){

		}
		Login login= this;
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					sender.writeObject(MessageType.out);
					socket.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}
		});
		this.listener=new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					try {
						MessageType mess= (MessageType) receiver.readObject();
						switch (mess){
							case login:
								LoginPacket loginPacket=(LoginPacket) receiver.readObject();
								String rep = loginPacket.getRep();
								Account account= loginPacket.getAccount();
								if (rep.equals("Accept")){
									userInfo=loginPacket.getUserInfo();
									Client client= new Client( userInfo,login );
									login.setVisible(false);
									client.setVisible(true);
								}else {
									System.out.println("Thất Bại");
									JOptionPane.showMessageDialog(new Frame(),"Đăng nhập thất bại");
								}
								break;
							case register:
								String rep1=(String) receiver.readObject();
								System.out.println(rep1);
								if(rep1.equals("Accept")) {
									System.out.println("thêm thành công");
									JOptionPane.showMessageDialog(new Frame(),"Thêm thành công");
								}
								if(rep1.equals("Deny")) {
									System.out.println("thêm thất bại");
									JOptionPane.showMessageDialog(new Frame(),"Thêm thất bại");
								}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException classNotFoundException) {
						classNotFoundException.printStackTrace();
					}
				}
			}
		});
		this.listener.start();
		setTitle("VTChess");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 347, 199);

		contentPane = new JPanel();
		contentPane.setBackground(/*new Color(255, 255, 204)*/UILib.colorBackground);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(UILib.fontText);
		lblNewLabel.setBounds(34, 24, 78, 14);
		contentPane.add(lblNewLabel);
		
		tFusername = new JTextField();
		tFusername.setBounds(122, 21, 157, 20);
		tFusername.setFont(UILib.fontText);
		contentPane.add(tFusername);
		tFusername.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(UILib.fontText);
		lblNewLabel_1.setBounds(34, 62, 78, 14);
		contentPane.add(lblNewLabel_1);
		
		tFpass = new JPasswordField();
		tFpass.setBounds(122, 59, 157, 20);
		contentPane.add(tFpass);
		tFpass.setColumns(10);
		tFpass.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						Account account= new Account(tFusername.getText()+"",tFpass.getText()+"");
						sender.writeObject(MessageType.login);
						sender.writeObject(account);
					}catch (Exception e1){

					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		JButton btnLogin = new JButton("Login");
		btnLogin.setForeground(UILib.colorButtonForeground);
		btnLogin.setBackground(UILib.colorButtonBackground);
		btnLogin.setFont(UILib.fontButton);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					System.out.println("button");
					Account account= new Account(tFusername.getText()+"",tFpass.getText()+"");
					sender.writeObject(MessageType.login);
					sender.writeObject(account);
				}catch (Exception e1){

				}
			}
		});
		btnLogin.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					try{
						Account account= new Account(tFusername.getText()+"",tFpass.getText()+"");
						sender.writeObject(MessageType.login);
						sender.writeObject(account);
					}catch (Exception e1){

					}
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		btnLogin.setBounds(190, 90, 89, 23);
		contentPane.add(btnLogin);
		
		JLabel lblRegister = new JLabel("Create a new account");
		lblRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblRegisterMouseListener(e);
			}
		});
		lblRegister.setForeground(new Color(255, 0, 0));
		lblRegister.setBackground(Color.GRAY);
		lblRegister.setFont(new Font("Forte", Font.PLAIN, 14));
		lblRegister.setBounds(89, 123, 140, 14);
		contentPane.add(lblRegister);
	}

	public JTextField gettFusername() {
		return tFusername;
	}

//	@Override
//	public void run() {
//		while (true){
//			try{
//				String rep=(String) receiver.readObject();
//				System.out.println(rep);
//				if(rep.equals("Accept")) {
//					String us=this.tFusername.getText();
//					this.setVisible(false);
//					System.out.println("thành công");
////					Main frame = new Main(this);
////					frame.setVisible(true);
//				}
//				if(rep.equals("Deny")) JOptionPane.showMessageDialog(new Frame(),"Thất Bại");
//			}catch (Exception e){
//
//			}
//		}
//
//	}

	private void lblRegisterMouseListener(MouseEvent e) {
		isOn=false;
		register= new Register(this);
		this.setVisible(false);
		register.setVisible(true);

	}
}
