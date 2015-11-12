package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import utility.Configuration;

public class Server {
    /**
     * Constants
     */
	private static final int serverPort = 6000;
	
	/**
	 * Variables
	 */
	private static String serverIP;
	public static SocketChannel serverSocket;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Launching the server");
		launchServer();

	}

	
	private static void launchServer()
	{
		Configuration.getInstance();
		setServerIP(Configuration.serverIP);
		//Initialize the server socket. Using TCP socket channel for non-blocking IO
		try {
			serverSocket = SocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(InetAddress.getByName(serverIP), serverPort));
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void listen()
	{
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
	}
	
	public static void setServerIP(String myIP)
	{
		serverIP = myIP;
	}
}
