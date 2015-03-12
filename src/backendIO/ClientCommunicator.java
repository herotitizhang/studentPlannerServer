package backendIO;
/**
 * This handles all client communications after the initial connection 
 * request is made. It's responsible for passing information to and from 
 * the client, creating new data if this is their first access, establishing 
 * login credentials, and authenticating the user's cell number.
 * 
 * Author: Tony Zhang
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import backendIO.ClientRequest;
import backendIO.ClientRequest.RequestType;
import alertSystem.Alert;
import alertSystem.CheckAlertsTask;
import alertSystem.EmailService;
import fileAccess.ServerIOSystem;
import fileAccess.SynchronizedDataCenter;
import fileAccess.UserInfo;


public class ClientCommunicator implements Runnable {

	private Socket socket;
	private ExecutorService executorService;
	private SynchronizedDataCenter dataCenter;
	
	public ClientCommunicator(Socket socket, ExecutorService executorService, SynchronizedDataCenter dataCenter) {
		this.socket = socket;
		this.executorService = executorService;
		this.dataCenter = dataCenter;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				if (socket.isConnected() && !socket.isClosed() && socket.getInputStream().available() > 0){
					InputStream in = socket.getInputStream(); // get inputStream (content entered) of the socket
					byte[] content = new byte[in.available()]; // initialize a byte array
					in.read(content); // read bytes from inputStream, and put them into the byte array
					ClientRequest clientRequest = (ClientRequest)ServerIOSystem.getObject(content);
					processClientRequest(clientRequest);
					socket.close();
					break; // close the thread
				} 
				Thread.sleep(10); // keep the server from dominating the resource
			} catch (IOException e) {	
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processClientRequest(ClientRequest clientRequest) {
		if (clientRequest == null) return;

		if (clientRequest.getType() == RequestType.CREATE) {
			processCreate(clientRequest);
		} else if (clientRequest.getType() == RequestType.LOGIN) {
			processLogin(clientRequest);
		} else if (clientRequest.getType() == RequestType.SAVE) {
			processSave(clientRequest);
		} else if (clientRequest.getType() == RequestType.LOAD) {
			processLoad(clientRequest);
		} else if (clientRequest.getType() == RequestType.REQUEST_AUTH) {
			processRequestAuth(clientRequest);
		} else if (clientRequest.getType() == RequestType.AUTH) {
			processAuth(clientRequest);
		} else if (clientRequest.getType() == RequestType.ALERT) {
			processAlert(clientRequest);
		}
	}

	
	// TODO encryption
	private void processCreate(ClientRequest clientRequest) {
		
		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null) return;
		ServerResponse serverResponse = new ServerResponse();
		
		UserInfo newUserInfo = new UserInfo(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()));
		if (dataCenter.addUser(newUserInfo) ) {
			serverResponse.setAccepted(true);
		} else {
			serverResponse.setAccepted(false);
			serverResponse.setFailureNotice("Couldn't add to user list!");
		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// TODO encryption
	private void processLogin(ClientRequest clientRequest) {

		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null) return;
		ServerResponse serverResponse = new ServerResponse();
		
		if (dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setAccepted(true);
		} else {
			serverResponse.setAccepted(false);
			serverResponse.setFailureNotice("False credentials!");

		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void processSave(ClientRequest clientRequest) {

		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null
				|| clientRequest.getSchedule() == null) return;
		ServerResponse serverResponse = new ServerResponse();
		
		if (!dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setAccepted(false);
			serverResponse.setFailureNotice("False credentials!");

		} else {
			serverResponse.setAccepted(true);
			synchronized(dataCenter) { // TODO may be make it a set method? 
				UserInfo userInfo = dataCenter.getUserList().get(clientRequest.getUserName());
				userInfo.setSchedule(clientRequest.getSchedule());
				dataCenter.save();
			}
		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void processLoad(ClientRequest clientRequest) {
		
		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null) return;

		ServerResponse serverResponse = new ServerResponse();
		
		if (!dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setAccepted(false);
			serverResponse.setFailureNotice("False credentials!");

		} else {
			serverResponse.setAccepted(true);
			byte[] schedule = dataCenter.getUserList().get(clientRequest.getUserName()).getSchedule();
			serverResponse.setSchedule(schedule);
		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void processRequestAuth(ClientRequest clientRequest) {
		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null 
				|| clientRequest.getPhoneNumber() == null) return;
		
		ServerResponse serverResponse = new ServerResponse();
		
		if (!dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setAccepted(false);
			serverResponse.setFailureNotice("False credentials!");

		} else {
			serverResponse.setAccepted(true);
		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// send the authentication code.
		// do it after server sends the response so that there is less delay.
		if (serverResponse.isAccepted()) {
			// generate random code
			StringBuilder sb = new StringBuilder();
			Random r = new Random();
			String alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			for (int i = 0; i < 5; i++) {
				sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
			}

			// set phone number and authentication code
			String sentCode = sb.toString();
			synchronized(dataCenter) { // TODO may be make it a set method? 
				dataCenter.getUserList().get(clientRequest.getUserName()).setAuthenticated(false);
				// although the server saved the phone number, it can't be used unless the user is authenticated.
				dataCenter.getUserList().get(clientRequest.getUserName()).setPhoneNumber(clientRequest.getPhoneNumber());
				dataCenter.getUserList().get(clientRequest.getUserName()).setSentCode(sentCode);
				dataCenter.save();
			}
			EmailService.sendToPhone(clientRequest.getPhoneNumber(), "Student Planner Verification", sentCode.toString());
			
			
		}
		
	}
	
	private void processAuth(ClientRequest clientRequest) {
		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null 
				|| clientRequest.getAuthenCode() == null) return;
		
		ServerResponse serverResponse = new ServerResponse();
		serverResponse.setAccepted(false);
		if (!dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setFailureNotice("False credentials!");

		} else if (!dataCenter.checkAuthenCode(clientRequest.getUserName(), clientRequest.getAuthenCode())) {
			serverResponse.setFailureNotice("The authentication code does not match!");
		} else {
			serverResponse.setAccepted(true);
			
			// the reason we lock the entire data center instead of just the hashmap is 
			// becasue of the save method call. what if one thread is saving something 
			// being processed by another thread? 
			synchronized(dataCenter) { // TODO may be make it a set method? 
				dataCenter.getUserList().get(clientRequest.getUserName()).setAuthenticated(true);
				dataCenter.save();
			}
			
		}
		
		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void processAlert(ClientRequest clientRequest) {
		if (clientRequest.getUserName() == null || clientRequest.getPassword() == null 
				|| clientRequest.getAlertTitle() == null || clientRequest.getAlertText() == null
				|| clientRequest.getAlertTime() == null || clientRequest.getRepeat() == null) return;
		
		ServerResponse serverResponse = new ServerResponse();
		serverResponse.setAccepted(false);
		if (!dataCenter.checkCredential(clientRequest.getUserName(), decryptPassword(clientRequest.getPassword()))) {
			serverResponse.setFailureNotice("False credentials!");
		} else if (!dataCenter.checkAuthenticated(clientRequest.getUserName())) {
			serverResponse.setFailureNotice("The user has not authenticated the phone number!");
		} else {
			String phoneNumber = dataCenter.getUserList().get(clientRequest.getUserName()).getPhoneNumber();
			Alert alert = new Alert(clientRequest.getAlertTitle(), clientRequest.getAlertText(),
					clientRequest.getRepeat(), phoneNumber, clientRequest.getAlertTime());
			dataCenter.addAlert(alert);
			serverResponse.setAccepted(true);
		}

		try {
			socket.getOutputStream().write(ServerIOSystem.getByteArray(serverResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String encryptPassword(String password) {
		String encryptedPassword = "";
		for (int i = 0; i < password.length(); i++) {
			encryptedPassword += (char)(password.charAt(i)+1);
		}
		return encryptedPassword;
	}
	
	public static String decryptPassword(String password) {
		String decryptedPassword = "";
		for (int i = 0; i < password.length(); i++) {
			decryptedPassword += (char)(password.charAt(i)-1);
		}
		return decryptedPassword;
	}
	
}
