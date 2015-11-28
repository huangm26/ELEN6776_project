package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import message.Message;
import message.PeerStartAck;
import message.PeerStartRequest;
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
	private static LinkedList<Integer> peerList;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Launching the server");
		peerList = new LinkedList();
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
			if(message.isPeerStartRequest())
			{
				onReceivePeerStartRequest((PeerStartRequest)message);
			}
		}		
	}
	
	private static void onReceivePeerStartRequest(PeerStartRequest request)
	{
		PeerStartAck ack;
		int predecessor = -1;
		int successor = -1;
		String destIP = request.IP;
		int destPort = request.from + 6000;
		if(peerList.contains(request.from)){
			// The peer to be started has a invalid ID
			System.out.println("The Peer with ID " + request.from + " already exists");
			ack = new PeerStartAck(request.to, request.from, false, predecessor, successor);
		} else
		{
			// The peer to be started has a valid ID
			System.out.println("Launching the Peer with ID " + request.from);
			
			if(peerList.size() != 0)
			//The list is not empty
			{
				int i = 0;
				for(i = 0; i < peerList.size(); i++)
				{
					if(peerList.get(i) > request.from)
						break;
				}
				if(i == 0)
				//smallest element in the list
				{
					peerList.addFirst(request.from);
					predecessor = peerList.getLast();
					successor = peerList.get(1);
				}
				else if(i == peerList.size())
				//largest element in the list
				{
					peerList.addLast(request.from);
					predecessor = peerList.get(peerList.size()-2);
					successor = peerList.getFirst();
				}
				else
				//in the middle of the list
				{
					peerList.add(i,request.from);
					predecessor = peerList.get(i-1);
					successor = peerList.get(i+1);
				}
			}
			else
			//The list is empty
			{
				peerList.add(request.from);
			}
			ack = new PeerStartAck(request.to, request.from, true, predecessor, successor);
		}
		Server_send sendStartAck = new Server_send(ack, destIP, destPort);
		new Thread(sendStartAck).start();
		System.out.println("New List");
		for(int i = 0; i < peerList.size(); i++)
		{
			System.out.println(peerList.get(i));
		}
	}
	
	public static void setServerIP(String myIP)
	{
		serverIP = myIP;
	}
}
