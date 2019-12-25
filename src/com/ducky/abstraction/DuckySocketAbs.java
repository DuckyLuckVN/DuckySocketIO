package com.ducky.abstraction;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import com.ducky.constant.SocketConstant;
import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckySocket;
import com.ducky.model.DuckyPackageSender;
import com.ducky.model.SocketWrapper;
import com.ducky.model.SystemInfo;
import com.ducky.thread.DuckyClientThread;
import com.ducky.util.IOUtil;

public abstract class DuckySocketAbs implements IDuckySocket{
	private Socket socket = null;
	private DuckyClientThread clientThread;
	private String keyGroup = SocketConstant.KEY_SOCEKT_GROUP_DEFAULT;
	
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
			//bắn sự kiện khởi tạo khi lần đầu kết nối đến server
			initOnServer();
			
			clientThread = new DuckyClientThread(socket, eventHalders);
			clientThread.start();
		}
		//Chạy vòng lặp vô tận để xử lý các action nếu isAsync = false;
		else 
		{
			
		}
		
	}
	
	//Hàm bắn sự kiện với key là KEY_EVENT_INIT_SOCKET lên server khi lần đầu kết nối để
	//server nắm được các thông tin của socket (keyGroup, SystemInfo)
	public void initOnServer() throws IOException {
		
		this.emit(SocketConstant.KEY_EVENT_INIT_SOCKET, 
				new SocketWrapper(null, keyGroup, new SystemInfo()));
		System.out.println("emit from client");
	}
	
	
	/*
	 * Sau khi gửi sự kiện KEY_EVENT_INIT_SOCKET đến server thì server sẽ trả về 1 sự kiện
	 * KEY_EVENT_AUTH_SIDE kèm theo dữ liệu boolean isAuthOnServerSide của server.
	 * 
	 * Dựa vào data isAuthGroupSocketOnServer gửi về mà ta sẽ xác định cách các luồng hoạt động
	 * khác nhau
	 * 
	 * nếu isAuthGroupSocketOnServer là TRUE thì luồng xử lý ở phía client vẫn xử lý như thường không
	 * cần kiểm tra xem thông tin server gửi về có cùng group của mình hay không (keyGroup).
	 * 
	 * nếu isAuthGroupSocketOnServer là FALSE thì luồng xử lý phía client sẽ phải kiểm tra thông tin trả
	 * về từ server (DuckyPackageData) có keySocketGroup có trùng với group của mình hay không?, nếu 
	 * trùng thì mới thực hiện việc xử lý
	 * 
	 * Sau đó Server sẽ lưu các thông tin này lại vào ArrayList<SocketWrapper> dùng để xử lý các thao tác khác.
	 */
	public void defineInitSocket() 
	{
		this.on(SocketConstant.KEY_EVENT_AUTH_GROUP_SIDE, new DuckyAction() {
			@Override
			public void doActionOnClient(Socket socket, Object data) {
				DuckyAction.super.doActionOnClient(socket, data);
			}
		});
	}
	
	//Đóng kết nối socket
	public void closeConnect() throws IOException {
		clientThread.shutDown();
		socket.close();
	}
	
	public Socket getSocket() { return this.socket; }
}
