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
	private int[] receivedMessages;
	private boolean close;
	private boolean lost = false;

	private void run() {
		int	pacSize;
		byte[] pacData;
		DatagramPacket pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		
		this.close = false;
		try {
			
			recvSoc.setSoTimeout(15000);
			pacData = new byte[256];
			pacSize = pacData.length;
			
			//int i = 1;
			while(!this.close){
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac);
				String data = new String(pac.getData()).trim();
				System.out.println("Iteration..." + i);
				//System.out.println("Packet..." + pac.getData());
				System.out.println("Data Received: " + data);
				processMessage(data);
				i++;
			}
			
			System.out.print("Received: " + this.totalMessages + "/" + msg.totalMessages);
			System.out.println(" ("+(((double)this.totalMessages)*100/msg.totalMessages)+"%)");

		
			for(int k = 0; k < totalMessages; k++) {
				if(receivedMessages[k] == 0) {
					if (!lost)
						System.out.print("Unreceived messages are: ");
					System.err.print((k+1) + ", ");
					lost = true;
				}
			}
			if(lost) {
					System.out.println("and that is all.");
			} else { 
			System.out.println("No Datagram packets were lost!");
			}

			totalMessages = -1;
			recvSoc.close();

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

			// TO-DO: If this is the last expected message, then identify
			// any missing messages

			int missedMessagesCounter = 0;
			//System.out.println(msg.messageNum + " | " + this.totalMessages);
			if(msg.messageNum == this.totalMessages) {
				// for(int i=0; i<this.totalMessages; i++) {
				// 	if(this.receivedMessages[i] == 0) {
				// 		missedMessagesCounter++;
				// 	}
				// }
			
				//System.out.println("Messages sent: " + this.totalMessages);
				//System.out.println("Missed messages: " + missedMessagesCounter);
				//System.out.println("Received messages: " + (this.totalMessages - missedMessagesCounter));

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
