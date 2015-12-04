package node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import message.Message;
import message.RegularMessage;
import utility.Configuration;


/*
 * This is the thread used to send the message without blocking other operation. Use the constructor to pass message, destination IP address 
 * and destination port number. The thread will send the message to destination and quit. 
 */

public class Peer_send implements Runnable{
	private Message message;
	private String destIP;
	private int destPort;
	public Peer_send(Message message, String destIP, int destPort)
	{
		this.message = message;
		this.destIP = destIP;
		this.destPort = destPort;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sendMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	
	@SuppressWarnings("null")
	private void sendMessage(Message message) throws IOException
	{
		SocketChannel sendSocket = SocketChannel.open();
		InetSocketAddress destAddress = new InetSocketAddress(
				InetAddress.getByName(destIP), destPort);
		sendSocket.connect(destAddress);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(message);
		byte[] data = outputStream.toByteArray();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int bytesend = sendSocket.write(buffer);
		sendSocket.finishConnect();
//		System.out.println("send "+ bytesend + " bytes");
		sendSocket.close();
	}
}
