package com.ducky.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket serverSocket = new ServerSocket(5555);
		
		System.out.println("waiting...");
		Socket socket = serverSocket.accept();
		
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		System.out.println("data tu client: " + ois.readObject());
		
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject("data tu server");
		System.out.println("data sent");
		
	}
}
