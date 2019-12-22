package com.ducky.thread;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import com.ducky.interfaces.DuckyAction;
import com.ducky.model.DuckyPackageSender;
import com.ducky.util.IOUtil;

public class DuckyClientThread extends Thread{
	
	Socket socket = null;
	HashMap<String, DuckyAction> eventHalders = null;
	private boolean isEnable = true;
	
	public DuckyClientThread(Socket socket, HashMap<String, DuckyAction> eventHalders) 
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
				System.out.println("clients wait....");
				socket.getInputStream();
				//nhận về gói dữ liệu ServerSocket con gửi về, sau đó lấy key, chạy sự kiện
				DuckyPackageSender packageSender = IOUtil.getPackageSender(socket);
				
				//chạy sự kiện với key truyền lên
				if (packageSender != null && eventHalders.get(packageSender.getKey()) != null) 
				{
					eventHalders.get(packageSender.getKey()).doActionOnClient(socket, packageSender.getData());
				}
				System.out.println("clients done");
				
				
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
