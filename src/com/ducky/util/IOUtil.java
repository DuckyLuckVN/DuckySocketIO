package com.ducky.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.ducky.model.DuckyPackageSender;

public class IOUtil 
{
	
	public static Object getObject (InputStream is) 
			throws IOException, ClassNotFoundException 
	{
//		System.out.println("available: " + is.available());
		if (is.available() > 0)
		{
			ObjectInputStream ois = new ObjectInputStream(is);
			return ois.readObject();
		}
		return null;
	}
	
	public static Object getDataPackageSender(Socket socket) 
			throws ClassNotFoundException, IOException {
		return getPackageSender(socket).getData();
	}
	
	public static String getKeyPackageSender(Socket socket) 
			throws ClassNotFoundException, IOException 
	{
		return getPackageSender(socket).getKey();
	}
	
	public static Object getDataPackageSender(InputStream is) 
			throws ClassNotFoundException, IOException {
		return getPackageSender(is).getData();
	}
	
	public static String getKeyPackageSender(InputStream is) 
			throws ClassNotFoundException, IOException 
	{
		return getPackageSender(is).getKey();
	}
	
	public static DuckyPackageSender getPackageSender(InputStream is) 
			throws ClassNotFoundException, IOException 
	{
		if (is.available() > 0)
			return (DuckyPackageSender) getObject(is);
		else
			return null;
	}
	
	public static DuckyPackageSender getPackageSender(Socket socket) 
			throws ClassNotFoundException, IOException
	{
		return getPackageSender(socket.getInputStream());
	}
	
	public static void sendObject(OutputStream os, Object data) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(data);
	}
	
	public static void sendObject(Socket socket, Object data) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(data);
	}
	
	public static void sendPackageSender(OutputStream os, DuckyPackageSender packageSender) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(packageSender);
	}
	
	public static void sendPackageSender(Socket socket, DuckyPackageSender packageSender) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(packageSender);
	}
	
	
}
