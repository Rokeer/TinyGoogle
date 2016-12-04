import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;

public class IndexingMasterThread implements Runnable {
	String file = "";
	Hashtable<String, InvertedIndex> iiList;
	HelperToken ht;
	InvertedIndex ii = new InvertedIndex();
	String mStrMSG = "";
	Queue<HelperToken> helperQueue;

	public IndexingMasterThread(String file, Hashtable<String, InvertedIndex> iiList, HelperToken ht,
			Queue<HelperToken> helperQueue) {
		this.file = file;
		this.iiList = iiList;
		this.ht = ht;
		this.helperQueue = helperQueue;
	}

	@Override
	public void run() {
		Socket mSocket = null;
		try {

			mStrMSG = "1," + file;

			// Create the server
			System.out.println("Indexing Master Thread: Connect to helper " + ht.getServer() + ":" + ht.getPort()
					+ " to handle " + file);
			mSocket = new Socket(ht.getServer(), ht.getPort());

			PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
			BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			mPrintWriter.flush();
			mPrintWriter.println(mStrMSG);

			while (((mStrMSG = mBufferedReader.readLine()) != null)) {
				// mStrMSG = mStrMSG.trim();
				System.out.println("Indexing Master Thread: Receiver result " + mStrMSG + " for " + file
						+ " from helper " + ht.getServer() + ":" + ht.getPort());

				String[] msgs = mStrMSG.split(",");
				if (msgs[0].equals("0")) {
					throw new Exception();
				}
				mStrMSG = mStrMSG.substring(2, mStrMSG.length());
				InvertedIndex ii = (InvertedIndex) Util.fromString(mStrMSG);
				//System.out.println("Indexing Master Thread: Ready to enter critical section");
				synchronized (iiList) {
					//System.out.println("Indexing Master Thread: Update indexed file list");
					iiList.put(file, ii);
				}
				synchronized (helperQueue) {
					//System.out.println("Indexing Master Thread: Put the helper back to the queue");
					helperQueue.add(ht);
				}
				break;
			}

			mSocket.close();
			System.out.println("Indexing Master Thread: Finish creating index for " + file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (mSocket != null) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			System.out.println("Indexing Master Thread: Problem occur when handling " + file);
		}

	}

}