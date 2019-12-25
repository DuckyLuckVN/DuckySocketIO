package com.ducky.model;

import java.io.Serializable;

/*
 * Là một class đại diện cho việc truyền dữ liệu đi giữa socket - ServerSocket
 * - key: là một key đại diện cho loại action muốn gửi đi
 * - data: là dữ liệu gửi đi kèm theo (nếu có)
 */
public class DuckyPackageSender implements Serializable{
	
	public static final String KEY_SOCKET_GROUP_DEFAULT = "default";
	
	private static final long serialVersionUID = 1L;
	private String keySocketGroup = KEY_SOCKET_GROUP_DEFAULT;
	private String key;
	private Object data;
	
	
	
	public DuckyPackageSender() {
	}
	
	public DuckyPackageSender(String key, Object data) {
		this.key = key;
		this.data = data;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public String getKeySocketGroup() {
		return keySocketGroup;
	}

	public void setKeySocketGroup(String keySocketGroup) {
		this.keySocketGroup = keySocketGroup;
	}

	
	
	
		
}
