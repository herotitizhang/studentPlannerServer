package alertSystem;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.GregorianCalendar;

public class Alert implements Serializable{

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
	
	@Override
	public String toString(){
		  DateFormat formatter = DateFormat.getInstance(); 

		  StringBuilder sb = new StringBuilder();
		  String newLine = System.getProperty("line.separator");
		  sb.append("===================").append(newLine)
		  .append("Alert title: ").append(this.alertTitle).append(newLine)
		  .append("Alert text: ").append(this.alertText).append(newLine)
		  .append("Alert time: ").append(formatter.format(alertTime.getTime())).append(newLine)
		  .append("Repeating: ").append(repeat.toString()).append(newLine)
		  .append("Phone number: ").append(phoneNumber).append(newLine);

		  return sb.toString();
	}

}
