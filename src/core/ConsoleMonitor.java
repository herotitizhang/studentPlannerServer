package core;

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
			}
		}
		
	}
	
}
