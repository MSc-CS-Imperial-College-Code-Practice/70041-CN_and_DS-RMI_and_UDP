/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

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
			iRMIServer = (RMIServerI) Naming.lookup(urlServer); //error here

		// TO-DO: Attempt to send messages the specified number of times
		
		for(int i=0; i<numMessages; i++) {
			MessageInfo msg = new MessageInfo(numMessages,i);
			iRMIServer.receiveMessage(msg);
		}

		} catch(MalformedURLException e){
			System.out.println("Error with URL: " + e.getMessage());
		} catch(RemoteException e) {
			System.out.println("Error with Remote Server: " + e.getMessage());
		} catch(NotBoundException e){
			System.out.println("Error in bounding with remote server: " + e.getMessage());
		}
	}
}



