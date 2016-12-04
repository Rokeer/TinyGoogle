import java.net.Socket;
import java.util.ArrayList;

public class SearchTask {
	Socket socket = null;
	ArrayList<String> wordList = new ArrayList<String>();
	String query = "";
	
	public SearchTask(Socket socket, String query) {
		this.socket = socket;
		String[] tmp = query.split(",");
		for (int i = 0; i < tmp.length; i++) {
			wordList.add(tmp[i].trim());
		}
		this.query = query;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	
	
}
