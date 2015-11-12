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
	
}
