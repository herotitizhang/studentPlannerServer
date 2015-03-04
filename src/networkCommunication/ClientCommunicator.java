package networkCommunication;
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
import java.util.concurrent.ExecutorService;

import fileAccess.SynchronizedDataCenter;
import fileAccess.UserInfo;
import networkCommunication.ClientRequest;
import networkCommunication.ClientRequest.RequestType;
import utilities.ServerIOSystem;


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
				if (socket.getInputStream().available() > 0){
					InputStream in = socket.getInputStream(); // get inputStream (content entered) of the socket
					byte[] content = new byte[in.available()]; // initialize a byte array
					in.read(content); // read bytes from inputStream, and put them into the byte array
					ClientRequest clientRequest = (ClientRequest)ServerIOSystem.getObject(content);
					processClientRequest(clientRequest);
				}
			} catch (IOException e) {
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
		
		UserInfo newUserInfo = new UserInfo(clientRequest.getUserName(), clientRequest.getPassword());
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
		
		if (dataCenter.checkCredential(clientRequest.getUserName(), clientRequest.getPassword())) {
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

	}

	private void processLoad(ClientRequest clientRequest) {

	}
	
	private void processRequestAuth(ClientRequest clientRequest) {

	}
	
	private void processAuth(ClientRequest clientRequest) {

	}

	private void processAlert(ClientRequest clientRequest) {

	}
	
}
