import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ChatClient extends JFrame implements Runnable{
	
	Socket socket;
	JTextArea ta;
	JButton send, logout;
	JTextField tf;
	
	Thread thread;
	
	DataInputStream din;
	DataOutputStream dout;
	
	String LoginName;
	
	ChatClient(String login) throws UnknownHostException, IOException{
		super(login);
		LoginName = login;
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				try {
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		ta = new JTextArea(18, 50);
		tf = new JTextField(50);
		
		tf.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					try {
						if(tf.getText().length()>0)
							dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
						tf.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					};
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		send = new JButton("Send");
		logout = new JButton("Logout");
		
		send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(tf.getText().length()>0)
						dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
					tf.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				};
			}
			
		});
		
		logout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				};
			}
			
		});
		
		socket = new Socket("localhost", 5213);
		
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(LoginName);
		dout.writeUTF(LoginName + " " + "LOGIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	
	private void setup() {
		setSize(600, 400);
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(ta));
		panel.add(tf);
		panel.add(send);
		panel.add(logout);
		add(panel);
		setVisible(true);
	}

	@Override
	public void run() {
		while(true) {
			try {
				ta.append("\n" + din.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		ChatClient client = new ChatClient("User3");
	}
}
