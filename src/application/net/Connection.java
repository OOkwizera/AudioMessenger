package application.net;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Connection {
	String IP;
	ArrayBlockingQueue<String> information = new ArrayBlockingQueue<>(20);
	Server server;
	int port;
	
	public Connection() {
		port = 9800;
		new Thread(() -> {
			try {
				server = new Server(port, information);
				server.runServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public void handShake(String info, String IP) {
		new Thread(() -> {
			try {
				Socket socket = new Socket(InetAddress.getByName((IP)), port);
				send(socket, info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	void send(Socket target, String info) throws IOException { 
		PrintWriter sockout = new PrintWriter(target.getOutputStream());
		sockout.println(info);
		sockout.flush();
	}
	
	public String retrieve() throws InterruptedException {
		return information.poll(500L, TimeUnit.MILLISECONDS);
	}
	
	public Boolean hasMessage() {
		return !information.isEmpty();
	}
	
	public void Clear() {
		information.clear();
	}
	
}
