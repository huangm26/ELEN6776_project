package message;

public class PeerStartAck extends Message{
	
	public boolean successful;	//If the launch of peer is successful. 
	public int prev_id;		//ID of predecessor, -1 if null
	public int next_id;		//ID of successor, -1 if null
	public PeerStartAck(int from, int to, boolean success, int prev, int next) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.successful = success;
		this.prev_id = prev;
		this.next_id = next;
	}

}
