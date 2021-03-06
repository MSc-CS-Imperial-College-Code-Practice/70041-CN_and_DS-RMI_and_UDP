package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc; // define DatagramSocket Object for sending
									// messages to UDP Server

	public static void main(String[] args) {
		InetAddress	serverAddr = null ; // IP address from UDP server
		int	recvPort;                   // receiving port from UDP Server
		int countTo;					// number of meesgaes going that will be
										// sent to UDP server
		
		// Get paramaters for serverAddr, recvPort and countTo from command 
		// line arguments. 
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv " + 
							   "port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]); // initialiaze UDP
														 // server IP address

		} catch (Exception e) {
			System.out.println("UDP Client Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		recvPort = Integer.parseInt(args[1]); // initiliaze receiving port from 
											  // UDP server
		countTo = Integer.parseInt(args[2]); // initiliaze number of messages
											 // that will be sent by UDP Client

		// TO-DO: Construct UDP client class and try to send messages
		UDPClient udpClient = new UDPClient(); // create new instance of a
											   // UDPClient 
		udpClient.testLoop(serverAddr, recvPort, countTo); // customized method
														   // for sending mess-	
														   // ages to UDP Server	
		
		// Printing message indicating that messages are being send to UDP
		// Server
		System.out.println("Sending messages to UDP Server...");
	}

	public UDPClient() {

		// TO-DO: Initialise the UDP socket for sending data
		try{
			this.sendSoc = new DatagramSocket(); // create datagram socket and 
												 // bind it to any available 
												 // port from the client machine
		} catch (Exception e) {
			System.out.println("UDP Client Error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int tries = 0;

		// TO-DO: Send the messages to the server
		for(int i=1; i<=countTo;i++){
			
			// Create variable "msg" from Class MessageInfo that holds data 
			// that will be send by UDP Client
			MessageInfo msg = new MessageInfo(countTo,i); // 
			
			// Create variable "msgString" that is the information in "msg"
			// parsed to a string
			String msgString = new String();
			msgString = msg.toString();
			this.send(msgString, serverAddr, recvPort);
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		
		int	payloadSize;        // size of packets that will be send in message
		byte[] pktData;			// array that parse message to send in array of 
								// bytes
		DatagramPacket pkt;		// define DatagramPacket object for including,
								// data to send and destination and port address

		// TO-DO: build the datagram packet and send it to the server
		
		pktData = payload.getBytes(); // parsing payload to array of bytes
		payloadSize = pktData.length; // initilaizing size of payload

		// Creating DatagramPacket Object to send messages
		pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
		
		try {
			this.sendSoc.send(pkt); // send DatagramPacket object to UDP Server
									// from sender socket

		} catch (Exception e) {
			System.out.println("UDP Client Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
