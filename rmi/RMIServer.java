/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.MessageInfo;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (this.receivedMessages == null) {
			this.receivedMessages = new int[this.totalMessages];
		}
		// TO-DO: Log receipt of the message
		this.receivedMessages[msg.messageNum-1] = 1; // 1 - Received Message
											   		 // 0 - Unreceived Message

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		int missedMessagesCounter = 0;
		if(msg.messageNum == this.totalMessages) {
			for(int i=0; i<this.totalMessages; i++) {
				if(this.receivedMessages[i] == 0) {
					missedMessagesCounter++;
				}
			}
		
			System.out.println("Messages sent: " + this.totalMessages);
			System.out.println("Received messages: " + missedMessagesCounter);
			System.out.println("Missed messages: " + (this.totalMessages - missedMessagesCounter));
		
		}	
		
		System.out.println("Closing connection...");
	}
	


	public static void main(String[] args) {

		RMIServer rmiServer = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Instantiate the server class
		try {
			rebindServer("RMIServerI", rmiServer);
		} catch(Exception e) {
			System.out.println("RMIServerI binding error");
		}
		// TO-DO: Bind to RMI registry
		
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		try {
			LocateRegistry.createRegistry(1099).rebind(serverURL, server);
		} catch (Exception e) {
			System.out.println("registry binding error");
		}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}
