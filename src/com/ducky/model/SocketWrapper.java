package com.ducky.model;

import java.io.Serializable;
import java.net.Socket;

public class SocketWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private String keyGroup;
	private SystemInfo systemInfo;
	
	public SocketWrapper() {
	}
	
	public SocketWrapper(Socket socket, String keyGroup, SystemInfo systemInfo) {
		this.socket = socket;
		this.keyGroup = keyGroup;
		this.systemInfo = systemInfo;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getKeyGroup() {
		return keyGroup;
	}
	
	public void setKeyGroup(String keyGroup) {
		this.keyGroup = keyGroup;
	}

	public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	@Override
	public String toString() {
		return "SocketWrapper [socket=" + socket + ", keyGroup=" + keyGroup + ", systemInfo=" + systemInfo + "]";
	}
	
	
	
}
