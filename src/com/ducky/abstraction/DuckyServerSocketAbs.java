package com.ducky.abstraction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ducky.constant.SocketConstant;
import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckyServerSocket;
import com.ducky.interfaces.IDuckyServerThread;
import com.ducky.model.DuckyPackageSender;
import com.ducky.model.SocketWrapper;
import com.ducky.thread.DuckyServerThread;
import com.ducky.util.IOUtil;

public abstract class DuckyServerSocketAbs implements IDuckyServerSocket
{
	//ServerSocket host chính
	private ServerSocket serverSocket;
	
	//Giá trị kiểm tra về socket group trên server (false - kiểm tra phía client)
	private boolean isAuthGroupSocketOnServer = false;
	
	//Mảng các đối tượng bao của socket đang kết nối đến server
	private List<SocketWrapper> socketWrapers = new ArrayList<>();
	
	//Mảng các luồng xử lý của duckyServerThreads
	private List<DuckyServerThreadAbs> duckyServerThreads = new ArrayList<>();
	
	//HashMap quản lý các key sự kiện và action tương ứng với key sự kiện đó
	private HashMap<String, DuckyAction> eventHalders = new HashMap<>();
	
	
	public DuckyServerSocketAbs( ) { 
		//Định nghĩa cơ chế khởi tạo socket khi socket đã kết nối đến server
	}
	
	public DuckyServerSocketAbs(int port, boolean isAuthGroupSocketOnServer) throws IOException
	{
		this(port);
		this.isAuthGroupSocketOnServer = isAuthGroupSocketOnServer;
	}
	
	//Khởi tạo server socket với port truyền vào
	public DuckyServerSocketAbs(int port) throws IOException 
	{
		this.createServer(port);
	}
	
	/*
	 * Khi có một kết nối từ socket client lên, phía client sẽ mặc định gửi kèm theo 1 sự kiện
	 * có key là KEY_EVENT_INIT_SOCKET đến server để khai báo các thông tin cần thiết (keyGroup, SystemInfo)
	 * 
	 * Sau đó Server sẽ lưu các thông tin này lại vào ArrayList<SocketWrapper> dùng để xử lý các thao tác khác.
	 */
	public void defineInitSocket() {
		this.on(SocketConstant.KEY_EVENT_INIT_SOCKET, new DuckyAction() {
			@Override
			public void doActionOnServer(Socket socket, Object data) {
				SocketWrapper socketWraper = (SocketWrapper) data;
				socketWraper.setSocket(socket);
				socketWrapers.add(socketWraper);
				try 
				{
					//Server trả về sự kiện để khai báo với client chế độ authGroupSocket là ở server side hay client side
					DuckyServerSocketAbs.this.emitOnly(socket, SocketConstant.KEY_EVENT_AUTH_GROUP_SIDE, isAuthGroupSocketOnServer);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
		
	}

	//Khởi tạo server socket với host và port truyền vào
	public ServerSocket createServer(int port) throws IOException 
	{
		this.serverSocket = new ServerSocket(port);
		
		//Định nghĩa cơ chế khởi tạo socket khi socket đã kết nối đến server
		this.defineInitSocket();
		
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
		for (SocketWrapper socketWrapper : socketWrapers) 
		{
			if (socket != socketWrapper.getSocket()) 
			{
				DuckyPackageSender packageSender = new DuckyPackageSender(key, data);
				IOUtil.sendPackageSender(socketWrapper.getSocket(), packageSender);
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
		for (SocketWrapper wraper : socketWrapers)
		{
			IOUtil.sendPackageSender(wraper.getSocket(), packageSender);
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
			
			//khởi tạo luồng xử lý socket client
			DuckyServerThread duckyServerThread = new DuckyServerThread(socket, eventHalders, isAuthGroupSocketOnServer);
			duckyServerThread.start();
			
			//lưu luồng xử lý vào array list
			this.duckyServerThreads.add(duckyServerThread);
		}
	}
	
	@Override
	public void shutdown() throws IOException {
		//chạy vòng lặp shutdown các luồng đang xử lý
		for (DuckyServerThreadAbs serverThread : duckyServerThreads) {
			serverThread.shutDown();
		}
		
		//đóng kết nối tại server socket
		this.serverSocket.close();
		
	}
	
}
