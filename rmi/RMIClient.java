/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null; // defining variable for Remote object
									  // class (RMIServer) that implements 
									  // methods of Interface RMIServerI
		
		Registry registry = null; // Defining variable for a registry object 
								  // used to advertise availability of server's 
								  // remote object

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress,"+ 
			"TotalMessageCount");
			System.exit(-1);
		}

		int numMessages = Integer.parseInt(args[1]); // auxiliar variable for 
													 // parsing # of messages 
													 // given as command line
													 // argument

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Bind to RMIServer
		try {
			
			// Get registry from RMI Server
			registry = LocateRegistry.getRegistry(args[0],1099);
			// Get Remote object (RMI Server) that is bound to the name
			// "holaServer"
			iRMIServer = (RMIServerI) registry.lookup("holaServer");

		// TO-DO: Attempt to send messages the specified number of times
			
			for(int i=0; i<numMessages; i++) {
				// Create variable "msg" from Class MessageInfo with data that 
				// we want to serialize for sending it to a Remote Objecjt (RMI 
				// Server)
				MessageInfo msg = new MessageInfo(numMessages,i);
				// Invoke receiveMessage method on Remote Object (RMI Server) 
				iRMIServer.receiveMessage(msg); 
			}
		
		} catch(Exception e){
			System.out.println("RMI Client Error: " + e.getMessage());
			e.printStackTrace();
		}
	
	}
}



