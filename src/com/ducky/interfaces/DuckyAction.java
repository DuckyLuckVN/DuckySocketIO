package com.ducky.interfaces;

import java.net.Socket;

public interface DuckyAction {
	default void doActionOnServer(Socket socket, Object data) {};
	default void doActionOnServer(Socket socket, String keyGroupSocket, Object data) 
	{
		doActionOnServer(socket, data);
	};
	default void doActionOnClient(Socket socket, Object data) {};
	
	default void doActionOnThread() {};
}
