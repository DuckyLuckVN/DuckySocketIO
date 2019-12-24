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
		
		//tạo một socket để giao tiếp với server
		DuckySocket socket = new DuckySocket();
		
		//tiến hành kết nối đến server theo host: localhost, port: 5555
		socket.connectToServer("localhost", 5555);
		
		//định nghĩa các handler để bắt lấy các sự kiện trả về từ server
		socket.on("serverChat", new DuckyAction() {
			/*
			 * Thực hiện hành động khi có sự kiện bắn từ server về client
			 * param - socket: đối tượng đại diện cho socket đang giao tiếp với server
			 * param - data: dữ liệu gửi kèm theo từ server trả về 
			 */
			@Override
			public void doActionOnClient(Socket socket, Object data) 
			{
				System.out.println("server chat: " + data);
			}
		});
		
		//do something
		while (true) 
		{
			String message = new Scanner(System.in).nextLine();
			System.out.println("me: " + message);
			
			//bắn sự kiện có key = "clientChat" lên server với dữ liệu kèm theo là message
			socket.emit("clientChat", message);
		}
		
	}
}
