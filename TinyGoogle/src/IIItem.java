import java.io.Serializable;

public class IIItem implements Serializable{
	private String id = "";
	private int count = 0;
	
	public IIItem(String id, int count) {
		this.id = id;
		this.count = count;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
