package application.net;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.*;

public class Server {
	private ServerSocket serverSocket;
	public int port = 9700;
	public ArrayBlockingQueue<String> information;
	
	public Server(int port, ArrayBlockingQueue<String> information) throws IOException {
		serverSocket = new ServerSocket(port);
		this.information = information;
	}
	
	public void runServer() throws IOException {
		while(true) {
			Socket socket = serverSocket.accept();
			new ServerThread(socket).start();
		}
	} 
	
	private class ServerThread extends Thread {
		Socket socket;
		
		ServerThread(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
	        try {
	            String message;
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            message = bufferedReader.readLine();
	            information.add(message); 
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}
