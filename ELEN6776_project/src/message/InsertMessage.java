package message;

public class InsertMessage extends Message{
	
	public String key;
	public String value;

	public InsertMessage(int from, int to, String key, String value) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.key = key;
		this.value = value;
	}

}
