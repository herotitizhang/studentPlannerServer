package alertSystem;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fileAccess.SynchronizedDataCenter;

public class CheckAlertsTask implements Runnable{
	
	SynchronizedDataCenter dataCenter = null;
	ExecutorService threadExecutor = null;
	
	public CheckAlertsTask (SynchronizedDataCenter dataCenter, ExecutorService threadExecutor) {
		this.dataCenter = dataCenter;
		this.threadExecutor = threadExecutor;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60*1000); // do this every minute
			} catch (Exception e) {
				e.printStackTrace();
			}

			// lock it in case a new alert is added
			synchronized (dataCenter) {
				HashMap<String, Alert> alerts = dataCenter.getAlertList();
				for (Alert alert: alerts.values()) {
					checkAlert(alert);

				}
			}
		}

	}

	public void checkAlert(Alert alert) {
		DateFormat formatter = DateFormat.getInstance(); 
		String specifiedTime = formatter.format(alert.getAlertTime().getTime());
		String now = formatter.format(((GregorianCalendar)Calendar.getInstance()).getTime());

		if (specifiedTime.compareTo(now) < 0) {
			dataCenter.removeAlert(alert.getAlertTitle());
			return; // we don't want to delete an alert because the user may want to change its time later (e.g., change repeat).
		}

		if(now.equals(specifiedTime)) {
			EmailService.sendToPhone(alert.getPhoneNumber(), alert.getAlertTitle(), alert.getAlertText(), threadExecutor);
			String repeat = alert.getRepeat();
			if (repeat.equals("NONE")) {
				dataCenter.removeAlert(alert.getAlertTitle());
				return;
			} else {
				if (repeat.equals("YEARLY")) {
					alert.getAlertTime().add(Calendar.YEAR, 1);
				} else if (repeat.equals("MONTHLY")) {
					alert.getAlertTime().add(Calendar.MONTH, 1);
				} else if (repeat.equals("WEEKLY")) {
					alert.getAlertTime().add(Calendar.DAY_OF_YEAR, 7);						
				}
				// test only
				// alert.getAlertTime().add(Calendar.MINUTE,2);						

				specifiedTime = formatter.format(alert.getAlertTime().getTime());
			}
		}
	}
}

	
	/*
	
		
		
		
		// TODO Evernote
		GregorianCalendar alert = new GregorianCalendar(2015, Calendar.MARCH, 7, 0, 47);
		DateFormat formatter = DateFormat.getInstance(); 
		System.out.println(formatter.format(now.getTime()));
		System.out.println(formatter.format(alert.getTime()));

		while (true) {
			try {
				Thread.sleep(60 * 1000); // check it every minute
			} catch (Exception e) {
				e.printStackTrace();
			}
			GregorianCalendar now = (GregorianCalendar)Calendar.getInstance();

			if(formatter.format(now.getTime()).equals(formatter.format(alert.getTime()))) {
				System.out.println("Alert!!!");
			}
		}
	
	
	
	
	
	
	 */
