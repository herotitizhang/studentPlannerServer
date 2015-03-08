package fileAccess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import alertSystem.Alert;
import alertSystem.AlertTask;
import utilities.ServerIOSystem;


public class SynchronizedDataCenter implements Serializable{
	
	private HashMap<String, UserInfo> userList; // key:username value:userinfo
	private HashMap<String, Alert> alertList; // key:alertname value:alert
	
	public SynchronizedDataCenter() {
		// first try to load. if there is no saved data (first time), 
		// then initialize the 2 lists
		if (! load()) {
			userList = new HashMap<String, UserInfo>();
			alertList = new HashMap<String, Alert>();
		} else {
			// TODO starts threads to run alerts
		}

	}
	
	// should be called upon initialization
	public synchronized void startAlerts(ExecutorService threadExecutor) {
		for (Alert alert: alertList.values()) {
			threadExecutor.execute(new AlertTask(alert));
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
	
	/**
	 * returns an old value if there is any. otherwise, returns null
	 * @param alert
	 * @return
	 */
	public synchronized Alert addAlert(Alert alert) {
		Alert previousAlert = alertList.put(alert.getAlertTitle(), alert); 
		save();
		return previousAlert; 
		
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
