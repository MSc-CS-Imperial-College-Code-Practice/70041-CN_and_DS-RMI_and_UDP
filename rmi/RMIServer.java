/*
 * Created on 01-Mar-2016
 */
package rmi;
package sun.rmi.transport.tcp;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import sun.rmi.transport.tcp.responseTimeout;

import common.MessageInfo;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private int totalReceived = 0;

	public RMIServer() throws RemoteException {
		super(); // call super() constructor from UnicastRemoteObject for 
				 // RMI linking & object initialisation
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		try{
			
			// TO-DO: On receipt of first message, initialise the receive buffer
			if(this.receivedMessages == null) {
				this.totalMessages = msg.totalMessages;
				this.receivedMessages = new int[this.totalMessages];
				Arrays.fill(this.receivedMessages, 0);  // Init array with zeros 
			}

			// TO-DO: Log receipt of the message
			this.receivedMessages[msg.messageNum] = 1; // 1 - Received Message
															// 0 - Unreceived Message
			this.totalReceived++;

			// TO-DO: If this is the last expected message, then identify
			//        any missing messages
		
			if(msg.messageNum == this.totalMessages - 1) {
		
				System.out.print("\nReceived messages are: [ ");
				for(int k = 0; k < totalMessages; k++) {
					if(receivedMessages[k] == 1) {
						System.out.print(k+1 + ", ");
					}
				}
	
				System.out.print("and nothing more...]\n\n");
		
			System.out.println("Received: " + this.totalReceived + "/" + this.totalMessages);
			System.out.println("Missed messages: " + (this.totalMessages - this.totalReceived));

			this.totalMessages = -1;
			System.out.println("Closing connection..."); //havent actually closed connection
			}
		
		} catch(Exception e) {
			System.out.println("RMI Server Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		RMIServer rmiServer = null; // defining variable for a server object

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager()); // Create Security Manager
		}

		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
		try {
			rmiServer = new RMIServer(); // creating server object
			rebindServer("holaServer", rmiServer); // customized method for creating registry
												   // and biniding server remote object
		} catch(Exception e) {
			System.out.println("RMI Server Error: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Binding complete!");
		sun.rmi.transport.tcp.responseTimeout(10000);
	}

	protected static void rebindServer(String serverURL, RMIServer server) {


		Registry registry = null; // Defining variable for a registry object 
								  // used to advertise availability of server's
								  // remote object

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		
		try {
			registry = LocateRegistry.createRegistry(1099); // creates registry in port 1099
		}
		catch (Exception e) {
			System.out.println("RMI Server Error: " + e.getMessage());
			e.printStackTrace();
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		
		try {
      		registry.rebind(serverURL, server); //making server remote object available
      		System.out.println("Rebind successful!");
		
		} catch (Exception e) {
			System.out.println("RMI Server Error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
