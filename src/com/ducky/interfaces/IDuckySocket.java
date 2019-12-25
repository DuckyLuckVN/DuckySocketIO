package com.ducky.interfaces;

import java.io.IOException;
import java.net.Socket;

public interface IDuckySocket {
	public void on(String key, DuckyAction action);
	public void emit(String key, Object data) throws IOException;
	
	public void connectToServer(String host, int port) throws IOException;
	public void closeConnect() throws IOException;
	
	public Socket getSocket();
	public void defineInitSocket();
}
