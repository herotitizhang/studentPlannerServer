/**
 * This handles initial connection requests - importantly, it must NOT wait for 
 * further feedback from client. Instead it immediately forks off a client 
 * communication thread, and waits for further input. It is the implementation of
 * Client Initialization thread.
 * @author Tony Zhang
 *
 */

package connectionInitializer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import backendIO.ClientCommunicator;
import alertSystem.Alert;
import alertSystem.CheckAlertsTask;
import fileAccess.SynchronizedDataCenter;

public class ServerDriver {
	
	private static ServerSocket serverSocket;
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private static SynchronizedDataCenter dataCenter = new SynchronizedDataCenter();
	
	public static void main (String[] args) {
		
		try {
			serverSocket = new ServerSocket(12345);
			System.out.println("Server started!");
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// for server admin
		threadExecutor.execute(new ConsoleMonitor(dataCenter));
		
		// for run alert tasks
		threadExecutor.execute(new CheckAlertsTask(dataCenter, threadExecutor) );
		
		while(true) {
			try {
				Socket client = serverSocket.accept();
				// FORK OFF!!!
				threadExecutor.execute(new ClientCommunicator(client, threadExecutor, dataCenter));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
