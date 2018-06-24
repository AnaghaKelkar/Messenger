import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	static Vector ClientSockets;
	static Vector LoginNames;

	ChatServer() throws IOException{
		ServerSocket server = new ServerSocket(5213); // server handling all clients in messenger
		ClientSockets = new Vector(); // clients interacting in messenger
		LoginNames = new Vector(); // login names of clients in messenger
		
		while(true) {
			Socket client = server.accept(); // accept new client created
			AcceptClient acceptClient = new AcceptClient(client); // pass it to acceptclient class
		}
	}
	
	public static void main(String[] args) throws IOException {
		ChatServer server = new ChatServer();
	}
	
	class AcceptClient extends Thread{
		Socket ClientSocket;
		DataInputStream din;
		DataOutputStream dout;
		
		AcceptClient(Socket client) throws IOException{
			ClientSocket = client; // client newly created and accepted
			din = new DataInputStream(ClientSocket.getInputStream()); // to read input
			dout = new DataOutputStream(ClientSocket.getOutputStream()); // to write output
			
			String LoginName = din.readUTF(); // login name of client
			
			LoginNames.add(LoginName); // add login name to vector of login names
			ClientSockets.add(ClientSocket); // add client to vector of clientsockets
			
			start();
		}
		
		public void run() {
			while(true){
				try {
					String msgFromClient = din.readUTF();
					StringTokenizer st = new StringTokenizer(msgFromClient);
					String LoginName = st.nextToken(); // extract login name
					String msgType = st.nextToken();
					
					int lo = -1;
					
					String msg = "";
					
					while(st.hasMoreTokens()) {
						msg = msg + " " + st.nextToken();
					}
					
					if(msgType.equals("LOGIN")){
						for(int i=0; i<LoginNames.size(); i++) {
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							if(!LoginName.equals(LoginNames.elementAt(i)))
								pOut.writeUTF(LoginName + " has logged in."); // msg displayed for other logged in user
						}
					}
					else if(msgType.equals("LOGOUT")){
						for(int i=0; i<LoginNames.size(); i++) {
							if(LoginName.equals(LoginNames.elementAt(i)))
								lo = i;
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							if(!LoginName.equals(LoginNames.elementAt(i)))
								pOut.writeUTF(LoginName + " has logged out."); // msg displayed for other logged in user
						}
						if(lo >= 0) {
							LoginNames.removeElementAt(lo);
							ClientSockets.removeElementAt(lo);
						}
					}
					else {
						for(int i=0; i<LoginNames.size(); i++) {
							Socket pSocket = (Socket) ClientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							if(!LoginName.equals(LoginNames.elementAt(i)))
								pOut.writeUTF(LoginName + ": " + msg); // msg displayed for other logged in user
						}
					}
					if(msgType.equals("LOGOUT"))
						break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
