/**
 * EmailService is a utility class used to send an email.
 * Author: Tony Zhang
 */

package alertSystem;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class EmailService {

    public static final String DEFAULT_USERNAME = "fanntastic422@gmail.com";
    public static final String DEFAULT_PASSWORD = "cis422ftw";
    
    public static final HashMap<String, String> carrierList = new HashMap<String, String>();
    static {
    	carrierList.put("ATT", "@txt.att.net");
    	carrierList.put("TMOBILE", "@tmomail.net");
    	carrierList.put("VERIZON",  "@vtext.com");
    	carrierList.put("SPRINT1", "@messaging.sprintpcs.com");
    	carrierList.put("SPRINT2", "@pm.sprint.com");
    	carrierList.put("VIRGINMOBILE","@vmobl.com");
    	carrierList.put("TRACFONE", "@mmst5.tracfone.com");
    	carrierList.put("METROPCS", "@mymetropcs.com");
    	carrierList.put("BOOSTMOBILE", "@myboostmobile.com");
    	carrierList.put("CRICKET", "@sms.mycricket.com");
    	carrierList.put("NEXTEL", "@messaging.nextel.com");
    	carrierList.put("ALLTEL", "@message.alltel.com");
    	carrierList.put("PTEL", "@ptel.com");
    	carrierList.put("SUNCOM", "@tms.suncom.com");
    	carrierList.put("QWEST", "@qwestmp.com");
    	carrierList.put("USCELLULAR", "@email.uscc.net");

    
    }
	
    // create a new thread to send the text
    public static void sendToPhone(final String number, String title, String body, ExecutorService threadExecutor) {
    	
    	threadExecutor.execute(new Runnable() {

			@Override
			public void run() {
				System.out.println("sending");
				sendToPhone(number, title, body);
				System.out.println("sent");
			}
    		
    	});
    }
    
    public static void sendToPhone(String number, String title, String body) {
    	for (String carrierSuffix: carrierList.values()) {
        	send(EmailService.DEFAULT_USERNAME, EmailService.DEFAULT_PASSWORD, 
        			number+carrierSuffix, null, title, body);
    	}
    }
    
    public static void send(final String username, final String password, String recipientAddress, String ccAddress, String title, String body) {

        Properties props = new Properties();
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtps.auth", "true");
        /* 
         * false means the connection is closed immediately after the QUIT command is sent
         * true (default value) lets the transport to wait for the response to QUIT command.
         */
        props.put("mail.smtps.quitwait", "false"); 

        Session session = Session.getInstance(props, new Authenticator() {
        	
        	@Override
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(username, password);
        	}
        });

        Message msg = new MimeMessage(session);
        try {
        	msg.setSubject(title);
        	msg.setContent(body, "text/html; charset=utf-8");
        	msg.setFrom(new InternetAddress(username));
        	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress, false));
        	if (ccAddress != null && ccAddress.length() != 0) {
        		msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress, false));
        	}
        	msg.setSentDate(new Date());

        	SMTPTransport transport = (SMTPTransport)session.getTransport("smtps");
        	transport.connect("smtp.gmail.com", username, password);
        	transport.sendMessage(msg, msg.getAllRecipients());      
        	transport.close();
        
        } catch (AddressException e) {
        	e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
    
//    /* For testing
    public static void main (String[] args) {
    	
    	String newLine = System.getProperty("line.separator");
    	String code = "This is a test message"+newLine+"Does it work or not";
    	
    	EmailService.sendToPhone("5034736577","", "testbody");
    }
//	*/
}
