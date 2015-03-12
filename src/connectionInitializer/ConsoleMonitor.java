/**
 * ConsoleMonitor class allows a server admin to monitor the status of the server
 * by typing in comments.
 * 
 * Author: Tony Zhang
 */

package connectionInitializer;

import java.util.Iterator;
import java.util.Scanner;

import alertSystem.Alert;
import fileAccess.SynchronizedDataCenter;
import fileAccess.UserInfo;

public class ConsoleMonitor implements Runnable {

	SynchronizedDataCenter dataCenter;
	Scanner console;
	
	public ConsoleMonitor(SynchronizedDataCenter dataCenter) {
		this.dataCenter = dataCenter;
		console = new Scanner(System.in);
	}

	@Override
	public void run() {
		System.out.println("Enter \"help\" to see a list of available commands");
		while (true) {
			String userInput = console.nextLine();
			if (userInput.equals("list_users")) {
				Iterator<UserInfo> it = dataCenter.getUserList().values().iterator(); 
				while (it.hasNext()) {
				    System.out.println(it.next()); 
				}
			} else if (userInput.equals("list_alerts")) {
				Iterator<Alert> it = dataCenter.getAlertList().values().iterator(); 
				while (it.hasNext()) {
				    System.out.println(it.next()); 
				}
			} else if (userInput.equals("exit")) {
				System.exit(0);
			} else {
				printHelp();
			}
		}
		
	}
	
	private void printHelp() {
		System.out.println("list_users - lists all the users.");
		System.out.println("list_alerts - lists all the alerts.");
		System.out.println("exit/quit - exits the program.");
		System.out.println("help - prints all commands.");
	}
	
}
