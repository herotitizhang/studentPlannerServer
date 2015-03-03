import java.security.Security;
import java.util.Date;
import java.util.Properties;

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
    
    public static final String ATT = "@txt.att.net";
    public static final String TMOBILE = "@tmomail.net";
    public static final String VERIZON = "@vtext.com";
    public static final String SPRINT1 = "@messaging.sprintpcs.com";
    public static final String SPRINT2 = "@pm.sprint.com";
    public static final String VIRGINMOBILE ="@vmobl.com";
    public static final String TRACFONE = "@mmst5.tracfone.com";
    public static final String METROPCS = "@mymetropcs.com";
    public static final String BOOSTMOBILE = "@myboostmobile.com";
    public static final String CRICKET = "@sms.mycricket.com";
    public static final String NEXTEL = "@messaging.nextel.com";
    public static final String ALLTEL = "@message.alltel.com";
    public static final String PTEL = "@ptel.com";
    public static final String SUNCOM = "@tms.suncom.com";
    public static final String QWEST = "@qwestmp.com";
    public static final String USCELLULAR = "@email.uscc.net";
	
    public static void send(final String username, final String password, String recipientAddress, String ccAddress, String title, String body) {
//        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

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
    
    public static void main (String[] args) {
    	
    	String newLine = System.getProperty("line.separator");
    	String code = "This is a test message"+newLine+"Does it work or not";
    	
    	EmailService.send(EmailService.DEFAULT_USERNAME, EmailService.DEFAULT_PASSWORD, 
    			"5034736577"+EmailService.TMOBILE, null, "", code);
    }
	
}
