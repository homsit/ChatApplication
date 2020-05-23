import java.net.*;
import java.io.*;
public class Messenger extends Thread{
	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private String status;
	Messenger()  {
	}
	public void init_server(int port_as_server)  throws IOException{
		serverSocket = new ServerSocket(port_as_server);
		serverSocket.setSoTimeout(1000000);
		status="wait_for_connection";
	}
	
	public void wait_for_connection() throws IOException{
		socket=serverSocket.accept();
		in = new DataInputStream(socket.getInputStream());
		out= new DataOutputStream(socket.getOutputStream());
	}
	
	public void connect_to(String target_ip, int target_port) throws UnknownHostException, IOException {
		status="connect_to";
		socket = new Socket(target_ip, target_port);
		in = new DataInputStream(socket.getInputStream());
		out= new DataOutputStream(socket.getOutputStream());
	}
	
	public void send_message(String msg) throws IOException {
		out.writeUTF(msg);
	}

	public String get_messages() throws IOException {
		return in.readUTF();
	}
	
	public void logout() throws IOException{
		if(!serverSocket.isClosed()){
			serverSocket.close();
		}
		socket.close();
		status="None";
	}
	
}