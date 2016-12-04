import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private int port = 0;
	private ServerSocket mServerSocket;
	private String filename = "";
	private int numOfSQM = 0;
	private Queue<SearchTask> workQueue = new LinkedList<SearchTask>();
	// thread pool
	private ExecutorService mExecutorService;
	private Hashtable<String, Integer> helperList = new Hashtable<String, Integer>();
	private Queue<HelperToken> helperQueue = new LinkedList<HelperToken>();

	private InvertedIndex mainII = new InvertedIndex();
	private Hashtable<String, ArrayList<String>> indexedFolders = new Hashtable<String, ArrayList<String>>();

	public Server(String filename, int port, int numOfSQM) {
		this.filename = filename;
		this.port = port;
		this.numOfSQM = numOfSQM;
	}

	public void start() {
		try {
			// Create the server
			mServerSocket = new ServerSocket(port);
			// create a thread pool
			mExecutorService = Executors.newCachedThreadPool();
			System.out.println("Server: Created!");

			saveToFile();
			System.out.println("Server: Store Server information to " + filename);

			System.out.println("Server: Initializing...");
			mExecutorService.execute(
					new WorkQueueMonitor(workQueue, numOfSQM, helperList, helperQueue, mainII, indexedFolders));
			mExecutorService.execute(new HelperChecker(helperList));

			// Start listening for connections. The program waits until some
			// client connects to the socket.
			System.out.println("Server: Start listening on port " + port + ".");
			while (true) {
				// Wait for incoming connections
				Socket socket = mServerSocket.accept();
				System.out.println("Server: New people is comming in.");
				// open a thread to handle the request
				mExecutorService
						.execute(new ServerThread(socket, helperList, helperQueue, workQueue, mainII, indexedFolders));
				// workQueue.add(new SearchTask(null, filename));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (mServerSocket != null) {
				try {
					// Close the server
					mServerSocket.close();
					System.out.println("Server: Closed!");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	private void saveToFile() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println("Server: IP Address: " + addr.getHostAddress());
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			out.write(addr.getHostAddress() + ":" + port);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String path =
		// "/afs/cs.pitt.edu/usr0/colinzhang/public/Prj1HaoranZhang/";
		String path = "";
		String filename = path + "server.txt";
		int port = 15222;
		int numOfSQM = 10;
		Server s = new Server(filename, port, numOfSQM);
		s.start();
	}

}
