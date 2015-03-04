package fileAccess;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	String username = null;
	String password = null;
	String phoneNumber = null;
	String emailAddress = null;
	
	boolean Authenticated = false;
	String sentCode = null;
	
	// these 2 fields are necessary
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
		
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public boolean isAuthenticated() {
		return Authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		Authenticated = authenticated;
	}

	public String getSentCode() {
		return sentCode;
	}

	public void setSentCode(String sentCode) {
		this.sentCode = sentCode;
	}
	
}
