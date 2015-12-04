package message;

public class FindMessage extends Message{

	public String key;
	public FindMessage(int from, int to, String key) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.key = key;
	}

}
