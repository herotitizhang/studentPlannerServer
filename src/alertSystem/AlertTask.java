package alertSystem;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import utilities.EmailService;

public class AlertTask implements Runnable{
	
	Alert alert = null;
	
	public AlertTask (Alert alert) {
		this.alert = alert;
	}

	@Override
	public void run() {
		if (alert.getAlertTime().compareTo( (GregorianCalendar)Calendar.getInstance() ) < 0) {
			return; // we don't want to delete an alert because the user may want to change its time later (e.g., change repeat).
		}
		DateFormat formatter = DateFormat.getInstance(); 
		String specifiedTime = formatter.format(alert.getAlertTime().getTime());
		
		while (true) {
			try {
				Thread.sleep(60*1000); // do this every minute
			} catch (Exception e) {
				e.printStackTrace();
			}
			String now = formatter.format(((GregorianCalendar)Calendar.getInstance()).getTime());

			if(now.equals(specifiedTime)) {
				EmailService.sendToPhone(alert.getPhoneNumber(), alert.getAlertTitle(), alert.getAlertText());
				String repeat = alert.getRepeat();
				if (repeat.equals("NONE")) {
					break;
				} else {
					if (repeat.equals("YEARLY")) {
						alert.getAlertTime().add(Calendar.YEAR, 1);
					} else if (repeat.equals("MONTHLY")) {
						alert.getAlertTime().add(Calendar.MONTH, 1);
					} else if (repeat.equals("WEEKLY")) {
						alert.getAlertTime().add(Calendar.DAY_OF_YEAR, 7);						
					}
					// test only
//					alert.getAlertTime().add(Calendar.MINUTE,2);						

					specifiedTime = formatter.format(alert.getAlertTime().getTime());
				}
			}
		}
		
	}

	
	/*
	DateFormat formatter = DateFormat.getInstance(); 
		
		
		
		// TODO Evernote
		GregorianCalendar alert = new GregorianCalendar(2015, Calendar.MARCH, 7, 0,
                47);

//		System.out.println(formatter.format(now.getTime()));
		System.out.println(formatter.format(alert.getTime()));


		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			GregorianCalendar now = (GregorianCalendar)Calendar.getInstance();

			if(formatter.format(now.getTime()).equals(formatter.format(alert.getTime()))) {
				System.out.println("Alert!!!");
			}
		}
	
	
	
	
	
	
	 */
}
