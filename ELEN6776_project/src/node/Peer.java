package node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

import utility.Configuration;

public class Peer {
    /**
     * Constants
     */

	
	/**
	 * Variables
	 */
	public static int ID;
	
	public static void main(String[] args)
	{
		System.out.println("Launching a peer");
		launchPeer();
	}
	

	
	private static void launchPeer()
	{
		System.out.println("Plase enter the peer ID, between 0 - 31");
		Scanner sc = new Scanner(System.in);
		ID = sc.nextInt();
		Peer_send send_thread = new Peer_send();
		new Thread(send_thread).start();
	}
	

	
}
