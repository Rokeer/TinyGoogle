import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;

public class HelperChecker implements Runnable{
	Hashtable<String, Integer> helperList = null;
	
	public HelperChecker (Hashtable<String, Integer> helperList) {
		this.helperList = helperList;
	}
	
	@Override
	public void run() {
		try {
			while (true){
				Thread.sleep(60000);
				BufferedReader br = null;
				PrintWriter pw = null;
				//ArrayList<String> removeList = new ArrayList<String>();
				for (String key : helperList.keySet()) {
					System.out.println("Server: Checking helper availability: " + key);
					String[] tmp = key.split(":");
					int port = Integer.parseInt(tmp[1]);
					String server = tmp[0];
					Socket mSocket = null;
					try {
						mSocket = new Socket(server, port);
						br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
						pw = new PrintWriter(mSocket.getOutputStream(), true);
						pw.println("0");
						String msg = "";
						while (((msg = br.readLine()) != null)) {
							msg = msg.trim();
							System.out.println("Server: Receive mssage from helper: " + msg);
							if (!msg.equals("1")) {
								System.out.println("Server: This helper has problem, remove it from helper list");
								helperList.remove(key);
								//removeList.add(key);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Server: This helper has problem, remove it from helper list");
						helperList.remove(key);
						//removeList.add(key);
					}
				}
				
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
