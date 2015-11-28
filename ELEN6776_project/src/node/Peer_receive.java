package node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import message.Message;
import message.NotifyNewPredecessor;
import message.NotifyNewSuccessor;
import message.PeerPredecessorReply;
import message.PeerPredecessorRequest;
import message.PeerStartAck;
import message.PeerStartRequest;
import utility.Configuration;

public class Peer_receive implements Runnable{

	/*
	 * MY ID, MY IP ADDRESS and MY PORT NUMBER
	 */
	public int ID;
	public String IPAddr;
	public int portNum;
	
	public static ServerSocketChannel receiveSocket;
	
	public Peer_receive(int ID, String IP)
	{
		this.ID = ID;
		this.IPAddr = IP;
		this.portNum = 6000 + ID;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			listen();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void listen() throws IOException
	{

		try {
			receiveSocket = ServerSocketChannel.open();
			receiveSocket.socket().bind(new InetSocketAddress(InetAddress.getByName(IPAddr), portNum));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = null;
		ByteBuffer buffer = ByteBuffer.allocate(4000);
		
		while (true)
		{
			SocketChannel newChannel = receiveSocket.accept();
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
			if(message.isPeerStartAck())
			{
				onReceiveStartAck((PeerStartAck)message);
			} 
			else if(message.isNofityNewSuccessor())
			{
				updateSuccessor((NotifyNewSuccessor) message);
			}
			else if(message.isNotifyNewPredecessor())
			{
				updatePredecessor((NotifyNewPredecessor) message);
			}
		}		
	}
	
	//On receiving the Start request ACK from server. The peer will be notified about its successor's ID
	private void onReceiveStartAck(PeerStartAck ack)
	{
		if(ack.successful)
		{
			System.out.println("Successful launching peer with ID " + ack.to);
//			System.out.println("The predecessor is " + ack.prev_id);
//			System.out.println("The successor is " + ack.next_id);
			updateNeighbors(ack);
		}
		else
		{
			System.out.println("The ID you entered already exists, please enter another one!");
		}
		
	}
	
	//Update the predecessor and successor of the current node, and notify the neighbors if have one
	private void updateNeighbors(PeerStartAck ack)
	{
		//This peer is the first node up in the system.
		if(ack.next_id == -1)
		{
			Peer.predecessor = -1;
			Peer.successor = -1;
		} else
		{
			Peer.predecessor = ack.prev_id;
			Peer.successor = ack.next_id;
			System.out.println("My initial predecessor " + Peer.predecessor);
			System.out.println("My initial successor "+ Peer.successor);
			notifyNeighbors();
		}
		
	}
	
	private void notifyNeighbors()
	{
		//Notify the successor that I am the new predecessor
		NotifyNewPredecessor notifyPredecessor = new NotifyNewPredecessor(ID, Peer.successor, ID);
		Peer_send nPredecessor_thread = new Peer_send(notifyPredecessor, Configuration.peerIP, 6000 + notifyPredecessor.to);
		new Thread(nPredecessor_thread).start();
		
		
		//Notify the predecessor that I am the new successor
		NotifyNewSuccessor notifySuccessor = new NotifyNewSuccessor(ID, Peer.predecessor, ID);
		Peer_send nSuccessor_thread = new Peer_send(notifySuccessor, Configuration.peerIP, 6000 + notifySuccessor.to);
		new Thread(nSuccessor_thread).start();
	}
	
	private void updateSuccessor(NotifyNewSuccessor newSuccessor)
	{
		Peer.successor = newSuccessor.from;
		System.out.println("New successor with ID " + Peer.successor);
	}
	
	private void updatePredecessor(NotifyNewPredecessor newPredecessor)
	{
		Peer.predecessor = newPredecessor.from;
		System.out.println("New Predecessor with ID " + Peer.predecessor);
	}
	
	
}
