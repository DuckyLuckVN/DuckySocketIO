package com.ducky.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import com.ducky.interfaces.DuckyAction;
import com.ducky.model.DuckyPackageSender;
import com.ducky.util.IOUtil;

public class DuckyServerThread extends Thread{
	
	Socket socket = null;
	HashMap<String, DuckyAction> eventHalders = null;
	private boolean isEnable = true;
	
	public DuckyServerThread(Socket socket, HashMap<String, DuckyAction> eventHalders) 
	{
		this.socket = socket;
		this.eventHalders = eventHalders;
	}
	
	//Nội dung chính của hoạt động trong thread socket
	@Override
	public void run() 
	{
		while ( isEnable && !socket.isClosed() ) 
		{
			if (isEnable == false) { break; }
			
			try
			{
				System.out.println("server wait....");
				socket.getInputStream();
				//nhận về gói dữ liệu socket con gửi lên, sau đó lấy key, chạy sự kiện và trả về cho socket client
				DuckyPackageSender packageSender = IOUtil.getPackageSender(socket);
				
				//chạy sự kiện với key truyền lên
				if (packageSender != null && eventHalders.get(packageSender.getKey()) != null) 
				{
					eventHalders.get(packageSender.getKey()).doActionOnServer(socket, packageSender.getData());
				}
				System.out.println("server done");
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void shutDown() { this.isEnable = false; }
	
}
