package com.ducky.abstraction;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckySocket;
import com.ducky.model.DuckyPackageSender;
import com.ducky.thread.DuckyClientThread;
import com.ducky.util.IOUtil;

public abstract class DuckySocketAbs implements IDuckySocket{
	private Socket socket = null;
	private DuckyClientThread clientThread;
	
	//mô tả cách thực hiện các action là đồng bộ hoặc bất đồng bộ
	private boolean isAsync = true;
	
	//cờ để tắt vòng lặp (chỉ hiệu quả nếu isAsync = true)
	private boolean isEnable = true;
	
	//Danh sách các sự kiện cần bắt và xử lý
	private HashMap<String, DuckyAction> eventHalders = new HashMap<>();
	
	//hàm tạo khởi tạo socket mới
	public DuckySocketAbs() {
		socket = new Socket();
	}
	
	//hàm tạo khởi tạo socket mới
	public DuckySocketAbs(boolean isAsync) {
		this();
		this.isAsync = isAsync;
	}
	
	//Bắt sự kiện với key và action tương ứng với key đó
	public void on(String key, DuckyAction duckyAction) 
	{
		eventHalders.put(key, duckyAction);
	}
	
	//bắn sự kiện và action lên ServerSocket
	public void emit(String key) throws IOException
	{
		emit(new DuckyPackageSender(key, null));
	}
	
	//bắn sự kiện và action lên ServerSocket
	public void emit(String key, Object data) throws IOException
	{
		emit(new DuckyPackageSender(key, data));
	}
	
	public void emit(DuckyPackageSender packageSender) throws IOException
	{
//		IOUtil.sendPackageSender(socket, packageSender);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(packageSender);
		oos.flush();
	}

	//kết nối đến ServerSocket với host và port truyền vào
	public void connectToServer(String host, int port) throws IOException 
	{
		socket.connect(new InetSocketAddress(host, port));
		
		//khởi tạo luồng chạy riêng để xử lý các action nếu isAsync = true
		if (isAsync) 
		{
			clientThread = new DuckyClientThread(socket, eventHalders);
			clientThread.start();
		}
		//Chạy vòng lặp vô tận để xử lý các action nếu isAsync = false;
		else 
		{
			
		}
		
	}
	
	//Đóng kết nối socket
	public void closeConnect() throws IOException {
		clientThread.shutDown();
		socket.close();
	}
	
	public Socket getSocket() { return this.socket; }
}
