package com.ducky.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import com.ducky.abstraction.DuckyServerThreadAbs;
import com.ducky.constant.SocketConstant;
import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckyServerThread;
import com.ducky.model.DuckyPackageSender;
import com.ducky.util.IOUtil;

public class DuckyServerThread extends DuckyServerThreadAbs {
	
	private DuckyAction action;
	private boolean isAuthGroupSocketOnServer = false;
	private String keySocketGroup = SocketConstant.KEY_SOCEKT_GROUP_DEFAULT;
	
	public DuckyServerThread(Socket socket, HashMap<String, DuckyAction> eventHalders, boolean isAuthGroupSocketOnServer) 
	{
		super(socket, eventHalders);
		this.isAuthGroupSocketOnServer = isAuthGroupSocketOnServer;
		
		//khởi tạo hành động cho thread
		if(isAuthGroupSocketOnServer) 
		{
			setActionAuthOnServer();
		}
		else
		{
			setActionAuthOnClient();
		}
	}
	
	public DuckyServerThread(Socket socket, HashMap<String, DuckyAction> eventHalders) 
	{
		super(socket, eventHalders);
	}
	
	//Nội dung chính của hoạt động trong thread socket
	@Override
	public void run() 
	{
		action.doActionOnThread();
	}
	
	public void setActionAuthOnServer()
	{
		this.action = new DuckyAction() {
			@Override
			public void doActionOnThread() {
				while ( isEnable && !socket.isClosed() ) 
				{
					if (isEnable == false) { break; }
					
					try
					{
						DuckyPackageSender packageSender = (DuckyPackageSender) IOUtil.getObject(socket.getInputStream());
						
						//chạy sự kiện với key truyền lên
						if (packageSender != null && eventHalders.get( packageSender.getKey() ) != null) 
						{
							//kiểm tra nếu keyGroup từ packageData gửi lên có trùng với socket đang giao tiếp không
							//nếu trùng thì mới thực hiện action
							if(packageSender.getKeySocketGroup().equals(DuckyServerThread.this.socket))
								eventHalders.get(packageSender.getKey())
								.doActionOnServer(socket, packageSender.getKeySocketGroup(), packageSender.getData());
						}
					} 
					catch (IOException e) 
					{
						shutDown();
						e.printStackTrace();
					} 
					catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					}
					
				}
			}
		};
	}
	
	public void setActionAuthOnClient()
	{
		this.action = new DuckyAction() {
			@Override
			public void doActionOnThread() {
				while ( isEnable && !socket.isClosed() ) 
				{
					if (isEnable == false) { break; }
					
					try
					{
						DuckyPackageSender packageSender = (DuckyPackageSender) IOUtil.getObject(socket.getInputStream());
						
						//chạy sự kiện với key truyền lên
						if (packageSender != null && eventHalders.get( packageSender.getKey() ) != null) 
						{
							eventHalders.get(packageSender.getKey())
										.doActionOnServer(socket, packageSender.getKeySocketGroup(), packageSender.getData());
						}
					} 
					catch (IOException e) 
					{
						shutDown();
						e.printStackTrace();
					} 
					catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					}
					
				}
			}
		};
	}

	public String getKeySocketGroup() {
		return keySocketGroup;
	}

	public void setKeySocketGroup(String keySocketGroup) {
		this.keySocketGroup = keySocketGroup;
	}
	
	
	
}
