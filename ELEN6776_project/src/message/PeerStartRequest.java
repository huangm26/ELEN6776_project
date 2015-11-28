package message;

public class PeerStartRequest extends Message{

	public String IP;
	public PeerStartRequest(int from, int to, String IP) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.IP = IP;
	}


}
