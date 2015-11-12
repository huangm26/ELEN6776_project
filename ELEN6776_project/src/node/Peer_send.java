package node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import message.RegularMessage;
import utility.Configuration;

public class Peer_send implements Runnable{

	private static final int serverPort = 6000;

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		RegularMessage message = new RegularMessage(Peer.ID, 0, "Hello world");
		try {
			sendMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	@SuppressWarnings("null")
	private void sendMessage(RegularMessage message) throws IOException
	{
		SocketChannel sendSocket = SocketChannel.open();
		InetSocketAddress destAddress = new InetSocketAddress(
				InetAddress.getByName(Configuration.serverIP), serverPort);
		sendSocket.connect(destAddress);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(message);
		byte[] data = outputStream.toByteArray();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int bytesend = sendSocket.write(buffer);
		sendSocket.finishConnect();
		System.out.println("send "+ bytesend + " bytes");
		sendSocket.close();
	}
}
