package message;

public class NotifyNewSuccessor extends Message{
	public int successor;
	
	public NotifyNewSuccessor(int from, int to, int successor) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.successor = successor;
	}

}
