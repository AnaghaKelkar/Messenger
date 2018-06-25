Java-Messenger:Develop a messenger in JAVA.

An instant messenger service in java using sockets and threads. 
1. One device will create a server. 
2. Other devices which act as clients can connect to server.

SDK:Eclipse 
UI:Java Swing

Classes:
1. ChatServer
- Default server address: 0.0.0.0 
- Create a local server by opening a ServerSocket on given port. 
//ServerSocket server=new ServerSocket(port); 
- Listen to socket for accepting connection from client. 
//Socket socket = server.accept(); 
- Open an DataInputStream as soon as server get the connection from client to read input from chatClient. 
// DataInputStream streamIn =new DataInputStream(socket.getInputStream());

2. ChatClient
- Connect to open socket in chatServer class using same port and default address as 0.0.0.0. 
//Socket socket = new Socket(port);
- Open an DataOutputStream to send text to chatServer. 
// DataOutputStream streamOut= new DataOutputStream(socket.getOutputStream()); 

3. Login
- Open Login window and taking input from user : loginName.
- Invoke ChatClient with that loginName.
// ChatClient client = new ChatClient(loginName);
