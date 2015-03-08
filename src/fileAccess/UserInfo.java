package fileAccess;

import java.io.Serializable;
import java.util.UUID;

public class UserInfo implements Serializable {
	
	String username = null;
	String password = null;
	String phoneNumber = null;
	String emailAddress = null;
	
	boolean authenticated = false;
	String sentCode = null;

	byte[] schedule = null;
	
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
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getSentCode() {
		return sentCode;
	}

	public void setSentCode(String sentCode) {
		this.sentCode = sentCode;
	}
	
	public byte[] getSchedule() {
		return schedule;
	}

	public void setSchedule(byte[] schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("========================").append(newLine)
		.append("Username: ").append(getUsername()).append(newLine)
		.append("Password: ").append(getPassword()).append(newLine);
		if (phoneNumber != null) sb.append("Phone Number: ").append(phoneNumber).append(newLine);
		if (emailAddress != null) sb.append("Email: ").append(emailAddress).append(newLine);
		sb.append("Authenticated: ").append(authenticated).append(newLine);
		if (sentCode != null) sb.append("Verification code: ").append(sentCode.toString()).append(newLine);
		return sb.toString();
	}

}
