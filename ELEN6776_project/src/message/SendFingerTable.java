package message;

public class SendFingerTable extends Message{

	//Since our node space is 5 bits from 0 - 31. The finger table is NodeID + 2^i ( i from 0 - 4 inclusive)
	
	public int[] finger_table;
	
	public SendFingerTable(int from, int to, int[] table) {
		super(from, to);
		// TODO Auto-generated constructor stub
		this.finger_table = table;
	}

}
