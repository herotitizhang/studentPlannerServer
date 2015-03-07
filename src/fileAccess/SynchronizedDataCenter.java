package fileAccess;

import java.io.Serializable;
import java.util.HashMap;

import model.CategoryI;
import model.EventI;
import utilities.ServerIOSystem;


public class SynchronizedDataCenter implements Serializable{
	
	private HashMap<String, UserInfo> userList; // key:username value:userinfo
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

	public synchronized boolean checkCredential(String username, String password) {
		
		if (!userList.containsKey(username)) return false;
		if (!userList.get(username).getPassword().equals(password)) return false;
		return true;
		
	}
	
	// check if the authenticate code matches the one stored in the userInfo
	public synchronized boolean checkAuthenCode(String username, String authCode) {
		
		if (!userList.containsKey(username)) return false;
		if (!userList.get(username).getSentCode().equals(authCode)) return false;
		return true;
		
	}
	
	// check if the user has been authenticated
	public synchronized boolean checkAuthenticated(String username) {
		
		if (!userList.containsKey(username)) return false;
		if (!userList.get(username).isAuthenticated()) return false;
		return true;
		
	}
	
	// check if the user has been authenticated
	public synchronized boolean checkEventExists(String username, String categoryName, String eventName) {

		// no such user
		if (!userList.containsKey(username)) return false;
		
		// the category does not exist
		CategoryI ctgr = userList.get(username).getSchedule().getCategoriesMap().get(categoryName);
		if (ctgr == null) return false;
		
		for (EventI event: ctgr.getAllEvents()) {
			if (event.getName().equals(eventName)) {
				return true;
			}
		}
		
		return false;

	}
	
	// check if an event has its alert turned on
	public synchronized boolean checkEventHasAlert(String username, String categoryName, String eventName) {

		// no such user
		if (!userList.containsKey(username)) return false;
		
		// the category does not exist
		CategoryI ctgr = userList.get(username).getSchedule().getCategoriesMap().get(categoryName);
		if (ctgr == null) return false;
		
		for (EventI event: ctgr.getAllEvents()) {
			if (event.getName().equals(eventName)) {
				if (event.hasAlert()) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;

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
