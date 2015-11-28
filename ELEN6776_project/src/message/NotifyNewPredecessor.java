package message;

public class NotifyNewPredecessor extends Message{
	public int predecessor;

	public NotifyNewPredecessor(int from, int to, int predecessor) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.predecessor = predecessor;
	}

}
