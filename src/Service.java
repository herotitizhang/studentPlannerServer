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

import networkCommunication.ClientRequest;
import networkCommunication.ClientRequest.RequestType;
import utilities.IOSystem;


public class Service implements Runnable {

	private Socket socket;
	private ExecutorService executorService;
	
	public Service(Socket socket, ExecutorService executorService) {
		this.socket = socket;
		this.executorService = executorService;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				if (socket.getInputStream().available() > 0){
					InputStream in = socket.getInputStream(); // get inputStream (content entered) of the socket
					byte[] content = new byte[in.available()]; // initialize a byte array
					in.read(content); // read bytes from inputStream, and put them into the byte array
					ClientRequest clientRequest = (ClientRequest)IOSystem.getObject(content);
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

	private void processCreate(ClientRequest clientRequest) {

	}
	
	private void processLogin(ClientRequest clientRequest) {

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
