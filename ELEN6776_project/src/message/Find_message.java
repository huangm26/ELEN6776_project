package message;

public class Find_message extends Message{

	public String key;
	public Find_message(int from, int to, String key) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.key = key;
	}

}
