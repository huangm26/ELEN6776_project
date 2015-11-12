package message;

public class RegularMessage extends Message{

	public String content;
	public RegularMessage(int from, int to, String content)
	{
		super(from ,to);
		this.content = content;
	}
}
