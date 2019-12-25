package com.ducky.abstraction;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckyServerThread;
import com.ducky.model.SocketWrapper;

public class DuckyServerThreadAbs extends Thread implements IDuckyServerThread {

	//Đối tượng socket đang xử lý
	protected Socket socket = null;
	
	//danh sách các handlers sự kiện và hành động action sẽ chạy tương ứng
	protected HashMap<String, DuckyAction> eventHalders = null;
	
	//gọi shutDown để set isEnable = false (dùng để check tắt luồng)
	protected boolean isEnable = true;

	public DuckyServerThreadAbs(Socket socket, HashMap<String, DuckyAction> eventHalders) {
		this.socket = socket;
		this.eventHalders = eventHalders;
	}

	@Override
	public void shutDown() {
		isEnable = false;
	}

}
