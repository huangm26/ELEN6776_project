package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

import message.PeerStartRequest;
import message.RegularMessage;
import utility.Configuration;

public class Peer {
    /*
     * Constants
     */

	
	/*
	 * Variables
	 */
	public static int ID;
	public static String IP;
	public static String serverIP;
	public static final int serverPort = 6000;
	public static int predecessor;
	public static int successor;
	
	
	public static void main(String[] args)
	{
		System.out.println("Launching a peer");
		// This should be change to real IP of peer, if needed in the future.
		IP = Configuration.peerIP;
		serverIP = Configuration.serverIP;
		predecessor = -1;
		successor = -1;
		System.out.println("Plase enter the peer ID, between 0 - 31");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		ID = sc.nextInt();
		if(ID < 0 || ID > 31)
		{
			System.out.println("Please enter a valid ID from 0 - 31");
			return;
		}
		launchPeer();
		startListener();
		startReadingInput();
	}
	

	
	private static void launchPeer()
	{		
		//send out the request to server for starting new peer
		Peer_send send_startRequest = new Peer_send(new PeerStartRequest(Peer.ID, 0, IP), serverIP, serverPort);
		new Thread(send_startRequest).start();
	}
	
	private static void startListener()
	{
		Peer_receive receive_thread = new Peer_receive(ID, IP);
		new Thread(receive_thread).start();
	}
	
	private static void startReadingInput()
	{
		Peer_input read_input = new Peer_input();
		new Thread(read_input).start();
	}
	
}
