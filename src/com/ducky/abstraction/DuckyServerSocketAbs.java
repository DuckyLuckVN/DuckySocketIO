package com.ducky.abstraction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckyServerSocket;
import com.ducky.model.DuckyPackageSender;
import com.ducky.thread.DuckyServerThread;
import com.ducky.util.IOUtil;

public abstract class DuckyServerSocketAbs implements IDuckyServerSocket
{
	//ServerSocket host chính
	private ServerSocket serverSocket;
	
	//Mảng các socket đang kết nối đến server
	private List<Socket> sockets = new ArrayList<>();
	
	//Mảng các luồng xử lý của duckyServerThreads
	private List<DuckyServerThread> duckyServerThreads = new ArrayList<>();
	
	
	//HashMap quản lý các key sự kiện và action tương ứng với key sự kiện đó
	private HashMap<String, DuckyAction> eventHalders = new HashMap<>();
	
	
	public DuckyServerSocketAbs( ) { }
	
	//Hàm tạo khởi tạo server socket với serverSocket truyền vào
	public DuckyServerSocketAbs(ServerSocket serverSocket) 
	{
		this.serverSocket = serverSocket;
	}
	
	//Khởi tạo server socket với port truyền vào
	public DuckyServerSocketAbs(String host, int port) throws IOException 
	{
		this.createServer(port);
	}
	
	//Khởi tạo server socket với host và port truyền vào
	public ServerSocket createServer(int port) throws IOException 
	{
		this.serverSocket = new ServerSocket(port);
		
		return this.serverSocket;
	}
	
	//Trả về đối tượng serverSocket của class
	public ServerSocket getServerSocket() 
	{
		return this.serverSocket;
	}
	
	//Khởi tạo các sự kiện cần bắt với key và hành động action tương ứng với key
	public void on(String key, DuckyAction duckyAction) 
	{
		eventHalders.put(key, duckyAction);
	}
	
	//Bắn sự kiện về các socket còn lại ngoài trừ socket đang truyền vào
	public void broadcast(Socket socket, String key, Object data) throws IOException 
	{
		for (Socket socket_temp : sockets) 
		{
			if (socket != socket_temp) 
			{
				DuckyPackageSender packageSender = new DuckyPackageSender(key, data);
				IOUtil.sendPackageSender(socket_temp, packageSender);
			}
		}
	}
	
	//bắn các sự kiện về cho các socket client đang kết nối
	public void emit(String key, Object data) throws IOException 
	{
		emit(new DuckyPackageSender(key, data));
	}
	
	public void emit(DuckyPackageSender packageSender) throws IOException
	{
		//duyệt các mảng socket đang quản lý và bắn sự kiệ
		for (Socket socket : sockets)
		{
			IOUtil.sendPackageSender(socket, packageSender);
		}
	}
	
	//bắn sự kiện chỉ với duy nhất 1 socket client truyền vào
	public void emitOnly(Socket socket, String key, Object data) throws IOException
	{
		emitOnly(socket, new DuckyPackageSender(key, data));
	}
	
	//bắn sự kiện chỉ với duy nhất 1 socket client truyền vào
	public void emitOnly(Socket socket, DuckyPackageSender packageSender) throws IOException
	{
		IOUtil.sendPackageSender(socket, packageSender);
	}
	
	//Khởi động server socket để nhận các kết nối socket và xử lý
	public void startServer() throws IOException 
	{
		int count = 0;
		while (true) 
		{
			//chờ và nhận socket client kết nối đến
			Socket socket = this.serverSocket.accept();
			
//			System.out.println("client: " + ++count);
			//lưu socket client vào array list
			this.sockets.add(socket);
			
			//khởi tạo luồng xử lý socket client
			DuckyServerThread duckyServerThread = new DuckyServerThread(socket, eventHalders);
			duckyServerThread.start();
			
			//lưu luồng xử lý vào array list
			this.duckyServerThreads.add(duckyServerThread);
		}
	}
	
	@Override
	public void shutdown() throws IOException {
		//chạy vòng lặp shutdown các luồng đang xử lý
		for (DuckyServerThread serverThread : duckyServerThreads) {
			serverThread.shutDown();
		}
		
		//đóng kết nối tại server socket
		this.serverSocket.close();
		
	}
	
}
