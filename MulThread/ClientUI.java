import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ClientUI extends JFrame {
	JTextArea mainArea;
	JTextArea sendArea;
	ChatClient client;
	JTextField ipArea;
	JButton btnLink;
	public void setClient(ChatClient client){
		this.client = client;
	}
	
	public ClientUI(){
		super("�ͻ���");
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout());
		mainArea = new JTextArea();
		JScrollPane mainAreaP = new JScrollPane(mainArea);
		JPanel panel = new JPanel(new BorderLayout());
		sendArea = new JTextArea(3,8);
		JButton sendBtn = new JButton("����");
		
		sendBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				client.sendMsg(sendArea.getText());
				mainArea.append("���ͻ��ˡ�"+sendArea.getText() + "\n");
				sendArea.setText("");
			}
		});

		JPanel ipPanel = new JPanel();
		ipPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		ipPanel.add(new JLabel("������:"));
		ipArea = new JTextField(12);
		ipArea.setText("127.0.0.1");
		ipPanel.add(ipArea);
		btnLink = new JButton("����");
		ipPanel.add(btnLink);
		btnLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				client = new ChatClient(ipArea.getText(),2345,ClientUI.this);
				ClientUI.this.setClient(client);
			}
		});
		
		panel.add(sendBtn,BorderLayout.EAST);
		panel.add(sendArea,BorderLayout.CENTER);
		contain.add(ipPanel,BorderLayout.NORTH);
		contain.add(mainAreaP,BorderLayout.CENTER);
		contain.add(panel,BorderLayout.SOUTH);
		setSize(500,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String []args) {
		ClientUI ui = new ClientUI();
	}
}

class ChatClient extends Thread{
	Socket sc;
	BufferedReader in;
	PrintWriter out;
	ClientUI ui;
	
	public ChatClient(String ip,int port,ClientUI ui){
		this.ui = ui;
		try {
			sc = new Socket(ip,port);
			System.out.println("��˳�����ӵ���������");
			in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			out = new PrintWriter(sc.getOutputStream(),true);
		}catch(Exception ex) {
			System.out.println(ex);
		}
		start();
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
				ui.mainArea.append(msg+"\n");
			}
		}
	}
	
	public void sendMsg(String msg) {
		try {
			out.println("���ͻ��ˡ�"+msg);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
