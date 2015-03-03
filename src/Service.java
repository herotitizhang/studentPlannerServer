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


public class Service implements Runnable {

	private Socket socket;
	
	public Service(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				if (socket.getInputStream().available() > 0){
					InputStream in = socket.getInputStream(); // get inputStream (content entered) of the socket
					byte[] content = new byte[in.available()]; // initialize a byte array
					in.read(content); // read bytes from inputStream, and put them into the byte array
				
				
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
