package com.ducky.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientMain {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		CustomSocket socket = new CustomSocket();
		socket.connect(new InetSocketAddress("localhost", 5555));
		socket.setKeyGroup("DuckyID");
		
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject("data tu client");
		
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		System.out.println("return from server: " + ois.readObject());
	}
}
