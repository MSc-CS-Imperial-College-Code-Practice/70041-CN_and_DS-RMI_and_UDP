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

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Bind to RMIServer
		try {
			//iRMIServer = (RMIServerI) Naming.lookup(urlServer); //error here
			Registry r = LocateRegistry.getRegistry(args[0],1099);
			RMIServerI s = (RMIServerI) r.lookup("holaServer");

		// TO-DO: Attempt to send messages the specified number of times
		
			for(int i=0; i<numMessages; i++) {
				MessageInfo msg = new MessageInfo(numMessages,i);
				iRMIServer.receiveMessage(msg);
				// try to print this because iRMIServer is found null?
			}
		
		} catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	
	}
}



