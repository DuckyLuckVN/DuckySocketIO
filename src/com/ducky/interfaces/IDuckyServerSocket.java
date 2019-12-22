package com.ducky.interfaces;

import java.io.IOException;
import java.net.ServerSocket;

public interface IDuckyServerSocket {
	
	public ServerSocket createServer(int port) throws IOException;
	public void startServer() throws IOException;
	public void shutdown() throws IOException;
	
	public void on(String key, DuckyAction duckyAction);
	public void emit(String key, Object data) throws IOException;
}
