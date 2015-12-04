package node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import message.FindMessage;
import message.InsertMessage;
import message.Message;
import message.NotifyNewPredecessor;
import message.NotifyNewSuccessor;
import message.PeerStartAck;
import message.PeerStopRequest;
import utility.Configuration;

public class Peer_input implements Runnable{

	
	/*
	 * Three kind of input message 
	 * 1. EXIT        used to shutdown the peer
	 * 2. INSERT KEY VALUE		this is the message to insert the message into peer. The KEY being the search key, and the VALUE being value
	 * 3. FIND KEY	  used to query the specific message with the given key as KEY
	 */
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		attachShutDownHook();
		System.out.println("The System has stablized, ready to send files!");
		while(true)
		{
			String content = null;
			Scanner scanner = new Scanner(System.in);
			content = scanner.nextLine();
			String[] strArr = null;
			strArr = content.split(" ");
			switch(strArr[0])
			{
				case("EXIT"):
					onExit();
					break;
				case("INSERT"):
					insertMessage(strArr[1], strArr[2]);
					break;
				case("FIND"):
					queryMessage(strArr[1]);
					break;
				default:
					break;
			}
		}
	}
	
	private void onExit()
	{
		notifyServer();
		if(Peer.predecessor != -1)
		{
			notifyNeighbors();
		}
		transferMessage();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void transferMessage()
	{
		Iterator<Entry<String, String>> it = Peer.storage.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
//	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        InsertMessage new_message = new InsertMessage(Peer.ID, Peer.successor, (String)pair.getKey(), (String)pair.getValue());
			sendMessage(new_message);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public void attachShutDownHook()
	{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				//On shutting down the peer. Add exit code here!!!!!!!!
				System.out.println("Shutting down the peer " + Peer.ID);
				
			}
		});
	}
	
	
		
	private void notifyNeighbors()
	{
		//Notify the successor its predecessor should be my current predecessor
		NotifyNewPredecessor notifyPredecessor = new NotifyNewPredecessor(Peer.ID, Peer.successor, Peer.predecessor);
//		Peer_send nPredecessor_thread = new Peer_send(notifyPredecessor, Configuration.peerIP, 6000 + notifyPredecessor.to);
//		new Thread(nPredecessor_thread).start();
		sendMessage(notifyPredecessor);	
			
		//Notify the predecessor is successor should be my current successor
		NotifyNewSuccessor notifySuccessor = new NotifyNewSuccessor(Peer.ID, Peer.predecessor, Peer.successor);
//		Peer_send nSuccessor_thread = new Peer_send(notifySuccessor, Configuration.peerIP, 6000 + notifySuccessor.to);
//		new Thread(nSuccessor_thread).start();
		sendMessage(notifySuccessor);
	}

	
	private void notifyServer()
	{
		PeerStopRequest stop_request = new PeerStopRequest(Peer.ID, 0);
		Peer_send send_stop_request = new Peer_send(stop_request, Peer.serverIP, Peer.serverPort);
		new Thread(send_stop_request).start();
	}
	
	private void insertMessage(String key, String value)
	{
		int key_val = Integer.parseInt(key) % 32;
		//The key belongs to me and I am the first peer in the system
		if((Peer.ID < Peer.predecessor) && (key_val <= Peer.ID) && (key_val  >= 0))
		{
			Peer.storage.put(key, value);
			System.out.println(Peer.storage);
		}
		else if((Peer.ID < Peer.predecessor)  && (key_val  > Peer.predecessor))
		{
			Peer.storage.put(key, value);
			System.out.println(Peer.storage);
		}
		//The key belongs to me and I am not the first peer in the system
		else if((key_val <= Peer.ID) && (key_val  > Peer.predecessor))
		{
			
			Peer.storage.put(key, value);
			System.out.println(Peer.storage);
		}
		//The key belongs to the successor
		else if((key_val > Peer.ID) && (key_val <= Peer.successor))
		{
			InsertMessage new_message = new InsertMessage(Peer.ID, Peer.successor, key, value);
			sendMessage(new_message);
		}
		//Route based on finger table
		else
		{
			for(int i = 0; i < 5; i++)
			{
				if(Peer.fingerTable[i] >= key_val)
				{
					InsertMessage new_message = new InsertMessage(Peer.ID, Peer.fingerTable[i], key, value);
					sendMessage(new_message);
					break;
				} else if(i == 4)
				//Last entry in the finger table is still less than the search key, route to the last entry peer
				{
					InsertMessage new_message = new InsertMessage(Peer.ID, Peer.fingerTable[4], key, value);
					sendMessage(new_message);
					break;
				}
			}
		}
	}
	
	private void queryMessage(String key)
	{
		int key_val = Integer.parseInt(key) % 32;
		//The key belongs to me and I am the first peer in the system
		if((Peer.ID < Peer.predecessor) && (key_val <= Peer.ID) && (key_val  >= 0))
		{
			if(Peer.storage.containsKey(key))
			{
				String value = Peer.storage.get(key);
				System.out.println("Found the Message with key " + key);
				System.out.println("The value is " + value);
			}
			else 
			{
				System.out.println("There is no message with key " + key);
			}
		}
		else if((Peer.ID < Peer.predecessor)  && (key_val  > Peer.predecessor))
		{
			if(Peer.storage.containsKey(key))
			{
				String value = Peer.storage.get(key);
				System.out.println("Found the Message with key " + key);
				System.out.println("The value is " + value);
			}
			else 
			{
				System.out.println("There is no message with key " + key);
			}
		}
		//The key belongs to me and I am not the first peer in the system
		else if((key_val <= Peer.ID) && (key_val  > Peer.predecessor))
		{
			
			if(Peer.storage.containsKey(key))
			{
				String value = Peer.storage.get(key);
				System.out.println("Found the Message with key " + key);
				System.out.println("The value is " + value);
			}
			else 
			{
				System.out.println("There is no message with key " + key);
			}
		}
		//The key belongs to the successor
		else if((key_val > Peer.ID) && (key_val <= Peer.successor))
		{
			FindMessage new_message = new FindMessage(Peer.ID, Peer.successor, key);
			sendMessage(new_message);
		}
		//Route based on finger table
		else
		{
			for(int i = 0; i < 5; i++)
			{
				if(Peer.fingerTable[i] >= key_val)
				{
					FindMessage new_message = new FindMessage(Peer.ID, Peer.fingerTable[i], key);
					sendMessage(new_message);
					break;
				} else if(i == 4)
				//Last entry in the finger table is still less than the search key, route to the last entry peer
				{
					FindMessage new_message = new FindMessage(Peer.ID, Peer.fingerTable[4], key);
					sendMessage(new_message);
					break;
				}
			}
		}
	}
	
	private void sendMessage(Message message)
	{
		Peer_send new_message_thread = new Peer_send(message, Configuration.peerIP, 6000 + message.to);
		new Thread(new_message_thread).start();
	}
}
