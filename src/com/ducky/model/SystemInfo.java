package com.ducky.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class SystemInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String localAddress;
	private String publicAddress;
	private String osName;
	private String pcName;
	
	public SystemInfo() {
		try 
		{
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			this.localAddress = InetAddress.getLocalHost().getHostAddress();
			this.publicAddress = in.readLine();
			this.osName = System.getProperty("os.name");
			this.pcName = System.getProperty("user.name");
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getPublicAddress() {
		return publicAddress;
	}

	public void setPublicAddress(String publicAddress) {
		this.publicAddress = publicAddress;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	@Override
	public String toString() {
		return "SystemInfo [localAddress=" + localAddress + ", publicAddress=" + publicAddress + ", osName=" + osName
				+ ", pcName=" + pcName + "]";
	}

	
	
	
}
