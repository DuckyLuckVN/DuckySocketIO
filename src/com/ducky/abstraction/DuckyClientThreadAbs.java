package com.ducky.abstraction;

import java.net.Socket;
import java.util.HashMap;

import com.ducky.interfaces.DuckyAction;
import com.ducky.interfaces.IDuckyClientThread;

public class DuckyClientThreadAbs extends Thread implements IDuckyClientThread {
	
	protected Socket socket = null;
	protected HashMap<String, DuckyAction> eventHalders = null;
	protected boolean isEnable = true;
	
	public DuckyClientThreadAbs(Socket socket, HashMap<String, DuckyAction> eventHalders) 
	{
		this.socket = socket;
		this.eventHalders = eventHalders;
	}
	
	public void shutDown() { this.isEnable = false; }
}
