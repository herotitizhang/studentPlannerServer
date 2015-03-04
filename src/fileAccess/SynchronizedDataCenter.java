package fileAccess;

import java.io.Serializable;
import java.util.HashMap;

import utilities.ServerIOSystem;


public class SynchronizedDataCenter implements Serializable{
	
	private HashMap<String, UserInfo> userList;
	private HashMap alertList;
	
	public SynchronizedDataCenter() {
		// first try to load. if there is no saved data (first time), 
		// then initialize the 2 lists
		if (! load()) {
			userList = new HashMap<String, UserInfo>();
			alertList = new HashMap();
		}

	}
	
	public synchronized boolean addUser(UserInfo userInfo) {
		
		if (!userList.containsKey(userInfo.getUsername())) {
			userList.put(userInfo.getUsername(), userInfo);
			save();
			return true;
		} else {
			return false;
		}
		
	}
	
	public synchronized boolean checkCredential(String username, String password) {
		
		if (!userList.containsKey(username)) return false;
		if (!userList.get(username).getPassword().equals(password)) return false;
		return true;
		
	}
	
	public synchronized boolean removeUser(UserInfo userInfo) {
		
		// I don't think we need this method as of yet..
		return false;
		
	}
	
	public synchronized boolean addAlert(/* alert */) {
		
//		if (!userList.containsKey(userInfo.getUsername())) {
//			userList.put(userInfo.getUsername(), userInfo);
//			save();
//			return true;
//		} else {
//			return false;
//		}
		return false;
		
	}
	
	public synchronized boolean removeAlert(/* alert */) {
		
		// I don't think we need this method as of yet..
		return false;
		
	}
	
	public synchronized void save() {
		
		// TODO TODO NEEDS TEST 
		ServerIOSystem.saveDataCenter(this);
		
	}
	
	public synchronized boolean load() {
		
		// TODO maybe integrate some of ServerIOSystem's methods into this class?
		SynchronizedDataCenter synchronizedDataCenter = ServerIOSystem.loadDataCenter();
		if (synchronizedDataCenter == null) {
			return false;
		} else {
			this.userList = synchronizedDataCenter.getUserList();
			this.alertList =  synchronizedDataCenter.getAlertList();
			return true;
		}
		
	}

	public synchronized HashMap<String, UserInfo> getUserList() {
		return userList;
	}

	public synchronized HashMap getAlertList() {
		return alertList;
	}
	
	
	
	/*
	 * TODO
	 * 
	 * save to database
	 * 
	 * load from databse
	 * 
	 * add/delete to userlist
	 * 
	 * add/delete to alertlist
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
}
