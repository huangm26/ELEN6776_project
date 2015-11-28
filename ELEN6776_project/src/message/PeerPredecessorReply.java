package message;

public class PeerPredecessorReply extends Message{

	public int predecessor;
	public PeerPredecessorReply(int from, int to, int predecessor) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.predecessor = predecessor;
	}

}
