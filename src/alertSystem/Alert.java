package alertSystem;

import java.util.GregorianCalendar;

public class Alert {

	String alertTitle = null;
	String alertText = null;
	GregorianCalendar alertTime = null;
	String repeat = null;
	String phoneNumber = null;
	
	public Alert(String alertTitle, String alertText,
			String repeat, String phoneNumber, GregorianCalendar alertTime) {
		this.alertTitle = alertTitle;
		this.alertText = alertText;
		this.alertTime = alertTime;
		this.repeat = repeat;
		this.phoneNumber = phoneNumber;
	}

	public String getAlertTitle() {
		return alertTitle;
	}

	public void setAlertTitle(String alertTitle) {
		this.alertTitle = alertTitle;
	}

	public String getAlertText() {
		return alertText;
	}

	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}

	public GregorianCalendar getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(GregorianCalendar alertTime) {
		this.alertTime = alertTime;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
