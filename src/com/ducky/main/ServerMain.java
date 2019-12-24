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
		//Tạo một server socket để nhận và xử lý các sự kiện của socket client
		DuckyServerSocket serverSocket = new DuckyServerSocket();
		
		//khởi tạo server với port là 5555
		serverSocket.createServer(5555);
		
		//định nghĩa các handler để bắt lấy các sự kiện gửi lên (emit) từ socket client
		serverSocket.on("clientChat", new DuckyAction() {
			/*
			 * Thực hiện hành động khi có sự kiện bắn từ server về client
			 * param - socket: đối tượng đại diện cho socket đang giao tiếp với server
			 * param - data: dữ liệu gửi kèm theo từ socket client gửi lên
			 */
			@Override
			public void doActionOnServer(Socket socket, Object data)
			{
				try 
				{
					/*
					 * sau khi nhận được sự kiện "clientChat" từ socket client gửi lên thì
					 * serverSocket sẽ gửi lại (emit) một sự kiện có key là "serverChat" về client
					 * với dữ liệu kèm theo là 1 String "server da chat lai"
					 */
					System.out.println("client da chat: " + data);
					serverSocket.emit("serverChat", "server da chat lai!");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
		
		//Tiến hành khởi động server để giao tiếp với các socket client
		serverSocket.startServer();
	}
}
