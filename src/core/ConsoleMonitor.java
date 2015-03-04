package core;

import java.util.Iterator;
import java.util.Scanner;

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
			if (userInput.equals("user_list")) {
				Iterator<UserInfo> it = dataCenter.getUserList().values().iterator(); 
				while (it.hasNext()) {
				    System.out.println(it.next()); 
				}
			} else if (userInput.equals("alert_list")) {
				
			}
		}
		
	}
	
}
