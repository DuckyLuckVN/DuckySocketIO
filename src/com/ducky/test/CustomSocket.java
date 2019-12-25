package com.ducky.test;

import java.io.Serializable;
import java.net.Socket;

public class CustomSocket extends Socket implements Serializable {
	String keyGroup = "ducky";

	public String getKeyGroup() {
		return keyGroup;
	}

	public void setKeyGroup(String keyGroup) {
		this.keyGroup = keyGroup;
	}
	
	
}
