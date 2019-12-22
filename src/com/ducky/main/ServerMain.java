package com.ducky.main;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.ducky.interfaces.DuckyAction;
import com.ducky.model.DuckyPackageSender;
import com.ducky.socket.DuckyServerSocket;
import com.ducky.thread.DuckyServerThread;
import com.ducky.util.IOUtil;

public class ServerMain {
	public static void main(String[] args) throws IOException 
	{
		System.out.println("------- SERVER -------");
		
		DuckyServerSocket serverSocket = new DuckyServerSocket();
		serverSocket.createServer(5555);
		
		serverSocket.on("clientChat", new DuckyAction() {
			@Override
			public void doActionOnServer(Socket socket, Object data)
			{
				try {
					serverSocket.emit("serverChat", "server da chat lai!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		serverSocket.startServer();
	}
}
