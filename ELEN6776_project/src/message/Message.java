package message;

import java.io.Serializable;

public class Message implements Serializable {

	public int from;
	public int to;
	
	public Message(int from, int to)
	{
		this.from = from;
		this.to = to;
	}
	
	public boolean isRegular()
	{
		return this instanceof RegularMessage;
		
	}
	
	public boolean isPeerStartRequest()
	{
		return this instanceof PeerStartRequest;
	}
	
	public boolean isPeerStartAck()
	{
		return this instanceof PeerStartAck;
	}
	
	public boolean isPeerPredecessorRequest()
	{
		return this instanceof PeerPredecessorRequest;
	}
	
	public boolean isPeerPredecessorReply()
	{
		return this instanceof PeerPredecessorReply;
	}
	
	public boolean isNotifyNewPredecessor()
	{
		return this instanceof NotifyNewPredecessor;
	}
	
	public boolean isNofityNewSuccessor()
	{
		return this instanceof NotifyNewSuccessor;
	}
	
	
}
