/*
 * Created on 01-Mar-2016
 */
package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DecimalFormat;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc; // define DatagramSocket Object for 
									// receiving messages from UDP client
	private int totalMessages = -1; // total messages sent by client. Initialize
									// with "-1" from the beggining
	private int messageCounter = 0; // counter for messages recieved
	private int[] receivedMessages; // array with flags indicating which 
									// messages were received
	private boolean close;          // flag for checking if communication is 
									// closed
	
	private void run() {
		int	payloadSize;        // size of packets received
		byte[] pktData;			// array that receives bytes of data from mess-
								// age sent
		DatagramPacket pkt;		// define DatagramPacket object that handles 
								// data sent by UDP Client to UDP Server
		

		// TO-DO: Receive the messages and process them by calling 
		// processMessage(...)- Use a timeout (e.g. 30 secs) to ensure the 
		// program doesn't block forever
		
		this.close = false;      // When starting server set close flag to false

		while(!this.close){
			
			try {
				
				pktData = new byte[256];	     // initialzie receiving array
												 // for packets transmitted 
				payloadSize = pktData.length;    // initilaizing size of payload
				this.recvSoc.setSoTimeout(10000); // Set timeout for reciving 
												  // socket in UDP Server

				// Creating DatagramPacket Object to receive messages and handle
				// iformation received
				pkt = new DatagramPacket(pktData, payloadSize);
				// Receive sent data from UDP Client
				this.recvSoc.receive(pkt);
				// Create string variable "data" for parsing information in
				// DatagramPacket "pkt" as String
				String data = new String(pkt.getData()).trim(); 
				processMessage(data); // customized method for processing mess-
									  // ages and check if data is received
			

			} catch (Exception e) {
				this.close = true;
				System.out.println("Closing connection: " + e.getMessage());
			}
		
		}

		
		// Printing Stats for messages received and missed
		System.out.print("\nReceived messages are: [ ");
		for(int k = 0; k < totalMessages; k++) {
			if(receivedMessages[k] == 1) {		
				System.err.print((k+1) + ", ");
			}
		}
		System.out.print("and nothing more...]\n\n");
		
		System.out.println("Received: " + this.messageCounter + "/" + 
						   this.totalMessages);
		System.out.println("Missed messages: " + (this.totalMessages - 
						   this.messageCounter));
		DecimalFormat df = new DecimalFormat("##.##%");
		double Efficiency = ( (double) this.messageCounter 
									/ this.totalMessages);
		System.out.println("Efficiency: " + df.format(Efficiency));
		System.out.println("Closing connection...");
		
		// After finishing set totalMessages to default value and close
		// receivig socket from UDP Server
		this.totalMessages = -1;
		this.recvSoc.close();
		
	}

	public void processMessage(String data) {

		// Define variable "msg" from Class MessageInfo to handle "data" 
		// sent by UDP Client
		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{

			// Initiliaze variable "msg" with data sent by UDP Client	
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
			this.messageCounter++; // update message counter by 1

			// TO-DO: If this is the last expected message, then identify
			// any missing messages
	
			if(msg.messageNum == this.totalMessages) {
				
				// NOTE: missing message information are handled by run(...)
				// method
				this.close = true; // set close flag to true for indicating the
								   // closing of connection with UDP server 
			}
		
		}catch (Exception e) {
			System.out.println("UDP Server Error: " + e.getMessage());
			e.printStackTrace();
		}		
	}

	public UDPServer(int rp) {
		
		// TO-DO: Initialise UDP socket for receiving data
		try{
			this.recvSoc = new DatagramSocket(rp); // initialize receing socket
												   // in reciving port "rc"
		}catch (Exception e) {
			System.out.println("UDP Server Error: " + e.getMessage());
			e.printStackTrace();
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
		recvPort = Integer.parseInt(args[0]); //parsing reciving port of UDP
											  // Server to Integer Object

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpServer = new UDPServer(recvPort);
		udpServer.run();
	}

}
