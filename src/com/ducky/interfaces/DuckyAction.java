package com.ducky.interfaces;

import java.net.Socket;

public interface DuckyAction {
	default void doActionOnServer(Socket socket, Object data) {};
	default void doActionOnClient(Socket socket, Object data) {};
}
