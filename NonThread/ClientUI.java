import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ClientUI extends JFrame {
	JTextArea mainArea;
	JTextArea sendArea;
	
	Socket sc;
	BufferedReader in;
	PrintWriter out;
	
	public ClientUI(){
		super("客户端");
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout());
		mainArea = new JTextArea();
		JScrollPane mainAreaP = new JScrollPane(mainArea);
		JPanel panel = new JPanel(new BorderLayout());
		sendArea = new JTextArea(3,8);
		JButton sendBtn = new JButton("发送");
		
		sendBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				sendMsg(sendArea.getText());
				mainArea.append("【客户端】"+sendArea.getText() + "\n");
				sendArea.setText("");
			}
		});

		
		panel.add(sendBtn,BorderLayout.EAST);
		panel.add(sendArea,BorderLayout.CENTER);
		contain.add(mainAreaP,BorderLayout.CENTER);
		contain.add(panel,BorderLayout.SOUTH);
		setSize(500,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			sc = new Socket("127.0.0.1",2345);
			System.out.println("已顺利连接到服务器。");
			in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			out = new PrintWriter(sc.getOutputStream(),true);
		}catch(Exception ex) {
			System.out.println(ex);
		}
		run();
	}
	public void run() {
		String msg = "";
		while(true) {
			try {
				msg = in.readLine();
			}catch(SocketException ex) {
				System.out.println(ex);
				break;
			}catch(Exception ex) {
				System.out.println(ex);
			}
			if(msg != null && msg.trim() != "") {
				System.out.println(">>"+msg);
				mainArea.append(msg+"\n");
			}
		}
	}
	
	public void sendMsg(String msg) {
		try {
			out.println("【客户端】"+msg);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String []args) {
		ClientUI ui = new ClientUI();
	}
}

