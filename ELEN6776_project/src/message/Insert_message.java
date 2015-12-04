package message;

public class Insert_message extends Message{
	
	public String key;
	public String value;

	public Insert_message(int from, int to, String key, String value) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.key = key;
		this.value = value;
	}

}
