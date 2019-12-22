package com.ducky.main;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.ducky.interfaces.DuckyAction;
import com.ducky.socket.DuckyServerSocket;
import com.ducky.socket.DuckySocket;
import com.ducky.thread.DuckyServerThread;

public class ClientMain {
	public static void main(String[] args) throws IOException 
	{
		System.out.println("------- CLIENT 1 -------");
		
		DuckySocket socket = new DuckySocket();
		socket.connectToServer("localhost", 5555);
		
		socket.on("serverChat", new DuckyAction() {
			@Override
			public void doActionOnClient(Socket socket, Object data) 
			{
				System.out.println("server chat: " + data);
			}
		});
		
		socket.on("aaa", new DuckyAction() {
			@Override
			public void doActionOnClient(Socket socket, Object data) 
			{
				System.out.println("server chat: " + data);
			}
		});
		
		while (true) 
		{
			String message = new Scanner(System.in).nextLine();
			
			System.out.println("me: " + message);
			
			socket.emit("clientChat", message);
		}
		
	}
}
