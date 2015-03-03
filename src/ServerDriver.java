import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This handles initial connection requests - importantly, it must NOT wait for 
 * further feedback from client. Instead it immediately forks off a client 
 * communication thread, and waits for further input. It is the implementation of
 * Client Initialization thread.
 * @author Tony Zhang
 *
 */

public class ServerDriver {
	
	private static ServerSocket serverSocket;
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	public static void main (String[] args) {
		while(true) {
			try {
				Socket client = serverSocket.accept();
				threadExecutor.execute(new Service(client));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
