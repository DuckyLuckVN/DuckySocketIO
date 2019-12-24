# DuckySocketIO Overview
This project is an open-source Java implementation of Socket.IO server. The idea is based on the javascript Socket.IO library.

_This project is still in the process of development, if there is have bugs or complaints please contact me at address **duckyluckvn.@gmai.com**. And my english level is not very good so if there is anything unhappy about this document please forgive me._

# Features

* Support multi thread (asynchronous) when you working with Socket
* Support multi thread (asynchronous) when you working with ServerSocket
* Help you working with socket easier with event capture and sending mechanisms
* Support realtime processing with socket easier
* Lightweight and optimal when dealing with multiple sockets at the same time (on server side)
* No 3rd library required

# Performance
To stress test the solution i run 3000 socket clients and managed to peak at total of about 30 000 messages per second with less than 1 second average delay.

I ran at the same time 3000 socket clients and ServerSocket on my laptop and everything is still under control.

My laptop information:\
**OS:** Windows 10\
**RAM:** 10GB (DDR3, bus 1333)\
**CPU:** Core(TM) **i7-2640M** (4 CPUs), ~2.8GHz

# How To Use?
I will make a tutorial based on an example of using socket to chat together (between client and server)

**On Server Side:** 
* **Step 1:** Using **DuckyServerSocket** to to create a new instance for **ServerSocket**.
```
	DuckyServerSocket serverSocket = new DuckyServerSocket();
```
* **Step 2:** Using method **createServer(int port)** to setup port working for ServerSocket.
```
	//I setup socket server to working with in port 5555
	serverSocket.createServer(5555);
```
* **Step 3:** Using method **on(String key, DuckyAction action)** to define a handler to catch an event sent (emit) from a socket client with a key and an action will called when event was catched with method **doActionOnServer(Socket socket, Object data)**.

```
	serverSocket.on("clientChat", new DuckyAction() 
    {
			/*
			 * param - socket: the object representing the socket is working with the socket server
			 * param - data: the data is sent (emit) from the socket client (nullable)
			 */
			@Override
			public void doActionOnServer(Socket socket, Object data)
			{
				try 
				{
                	System.out.println("client sent chat: " + data);
                    
					/*
					 * after receiving the "clientChat" event from the client socket
					 * serverSocket will send back (emit) an event with the key "serverChat" to the client
					 * with the data attached as a String "this is message from socket server ^^!"
					 */
					serverSocket.emit("serverChat", "this is message from socket server ^^!");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		});
```

* **Step 4:** Send an event and data to all socket clients with **emit(String key, Object data)** method.
```
	/*
    * after receiving the "clientChat" event from the client socket
    * serverSocket will send back (emit) an event with the key "serverChat" to the client
    * with the data attached as a String "this is message from socket server ^^!"
    */
    serverSocket.emit("serverChat", "this is message from socket server ^^!");
```
* **Step 5:** Start socket server with method **startServer()**. And now socket server is waiting and do something we defined before when socket clients connected.

```
	serverSocket.startServer();
```


**On Client Side:** 
* **Step 1:** Using **DuckySocket** to to create a new instance for **Socket**.
```
	DuckySocket socket = new DuckySocket();
```


* **Step 2:** Using method **on(String key, DuckyAction action)** to define a handler to catch an event sent (emit) from a socket server with a key and an action will called when event was catched with method **doActionOnClient(Socket socket, Object data)**.
```
	socket.on("serverChat", new DuckyAction() {
			/*
			 * param - socket: the object representing the socket is working with the socket server
			 * param - data: the data is sent (emit) from the socket client (nullable)
			 */
			@Override
			public void doActionOnClient(Socket socket, Object data) 
			{
				System.out.println("server sent chat: " + data);
			}
		});
```
* **Step 3:** end an event and data to socket server with **emit(String key, Object data)** method.
```
	/* loop and receive input from console then send event (emit) 
    to server with data attached is a message from input as a String */
	while (true) 
	{
		String message = new Scanner(System.in).nextLine();
		System.out.println("me: " + message);
			
		//emit an event with key is "clientChat" to socket server with data attached is message
		socket.emit("clientChat", message);
	}
```

* **Step 4:** Using method **connectToServer(String host, int port)** to connect socket to a socket server.
```
	socket.connectToServer("localhost", 5555);
```

**note:** you can call the **connectToServer()** on client side and **createServer()** on server side before you define some event to catched from server or client. It's okay. and in the process of running, you can define more the handler to catched event.

I hope this example will help you know how to use this library. And remember you should run code from server side first and after that you run code from client side.

# Recent Releases
**24-12-2019: First release (version 1.0.0)** \
First stable release and Merry Christmas :3