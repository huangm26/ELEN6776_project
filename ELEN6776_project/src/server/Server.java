package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import message.Message;
import message.RegularMessage;
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
	public static ServerSocketChannel serverSocket;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Launching the server");
		launchServer();

	}

	
	private static void launchServer() throws IOException
	{
		Configuration.getInstance();
		setServerIP(Configuration.serverIP);
		//Initialize the server socket. Using TCP socket channel for non-blocking IO
		listen();
	}
	
	private static void listen() throws IOException
	{

		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(InetAddress.getByName(serverIP), serverPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = null;
		ByteBuffer buffer = ByteBuffer.allocate(1000);
		
//		while (serverSocket.read(buffer) <= 0) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		while (true)
		{
			SocketChannel newChannel = serverSocket.accept();
			newChannel.read(buffer);
			buffer.flip();
			ByteArrayInputStream in = new ByteArrayInputStream(buffer.array());
			ObjectInputStream is = new ObjectInputStream(in);
			try {
				message = (Message) is.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(message.isRegular())
				System.out.println(((RegularMessage)message).content);
		}
		
		
		
	}
	
	public static void setServerIP(String myIP)
	{
		serverIP = myIP;
	}
}
