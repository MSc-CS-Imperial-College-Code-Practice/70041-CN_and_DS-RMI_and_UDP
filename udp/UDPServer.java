/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int totalReceived = 0;
	private int[] receivedMessages;
	private boolean close;
	

	private void run() {
		int	pacSize;
		byte[] pacData;
		DatagramPacket pac;
		

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		
		this.close = false;

		while(!this.close){
			
			try {
				
				pacData = new byte[10];	  	  // buffer for incoming data packets
				pacSize = pacData.length;     // length of each packet
				this.recvSoc.setSoTimeout(10000);  // Set timeout for reciving socket

				pac = new DatagramPacket(pacData, pacSize);  // DatagramPacket object receiving
																// for receiving data packets
				this.recvSoc.receive(pac);         // receive sent packet from server socket
				String data = new String(pac.getData()).trim(); // processing data inside socket
				processMessage(data);

			} catch(SocketException e) {
				System.out.println("Error in server socket: " + e.getMessage());
				this.close = true;
			} catch(IllegalArgumentException e){
				System.out.println("Illegal Arguments: " + e.getMessage());
				this.close = true;
			} catch(IOException e) {
				System.out.println("Error in receiving client message: " + e.getMessage());
				this.close = true;
			}
		}


		System.out.print("\nReceived messages are: [ ");
		for(int k = 0; k < totalMessages; k++) {
			if(receivedMessages[k] == 1) {		
				System.err.print((k+1) + ", ");
			}
		}
		System.out.print("and nothing more...]\n\n");
		
		System.out.println("Received: " + this.totalReceived + "/" + this.totalMessages);
		System.out.println("Missed messages: " + (this.totalMessages - this.totalReceived));

		this.totalMessages = -1;
		this.recvSoc.close();

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);
		 
			// TO-DO: On receipt of first message, initialise the receive buffer
			if(this.receivedMessages == null) {
				this.totalMessages = msg.totalMessages;
				this.receivedMessages = new int[this.totalMessages];
				Arrays.fill(this.receivedMessages, 0);  // Init array with zeros 
			}

			// TO-DO: Log receipt of the message
			this.receivedMessages[msg.messageNum-1] = 1; // 1 - Received Message
														// 0 - Unreceived Message
			this.totalReceived++;

			// TO-DO: If this is the last expected message, then identify
			// any missing messages
	
			if(msg.messageNum == this.totalMessages) {
	
				System.out.println("Closing connection...");
				this.close = true;
			}
		
		} catch(Exception e) {
			System.out.println("Error in processing message");
		}

		
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
	
		try{
			this.recvSoc = new DatagramSocket(rp);
		} catch(SocketException e) {
			System.out.println("Error in server socket: " + e.getMessage());
		}
		
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpServer = new UDPServer(recvPort);
		udpServer.run();
	}

}
