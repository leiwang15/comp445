import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.ArrayList;

public class UDP_Client {
	private static final int BUFFER_SIZE = 1024 * 50;
	private static final int PORT = 6789;
	private static final String HOSTNAME = "localhost";
	private static final int BASE_SEQUENCE_NUMBER = 42;
	static String inFile = "D:/1.zip";

    public static void main(String args[]) throws Exception{
   		// Create a socket
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout( 1000 );

		// The message we're going to send converted to bytes
		Integer sequenceNumber = BASE_SEQUENCE_NUMBER;

		ArrayList<byte[]> list = splitFile(inFile); 
		
		for (int i = 0; i < list.size(); i++) {
			byte[] data = list.get(i);
			boolean timedOut = true;

			while( timedOut ){
				sequenceNumber++;

				// Create a byte array for sending and receiving data
				//byte[] sendData = new byte[ BUFFER_SIZE ];
				byte[] receiveData = new byte[ BUFFER_SIZE ];

				// Get the IP address of the server
				InetAddress IPAddress = InetAddress.getByName( HOSTNAME );

				System.out.println( "Sending Packet (Sequence Number " + sequenceNumber + ")" );				
				// Get byte data for message 
				//sendData = ByteBuffer.allocate(4).putInt( sequenceNumber ).array();

				try{
					// Send the UDP Packet to the server
					DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, 6789);
					socket.send( packet );

					// Receive the server's packet
					DatagramPacket received = new DatagramPacket(receiveData, receiveData.length);
					socket.receive( received );
					
					// Get the message from the server's packet
					int returnMessage = ByteBuffer.wrap( received.getData( ) ).getInt();

					System.out.println( "FROM SERVER:" + returnMessage );
					// If we receive an ack, stop the while loop
					timedOut = false;
				} catch( SocketTimeoutException exception ){
					// If we don't get an ack, prepare to resend sequence number
					System.out.println( "Timeout (Sequence Number " + sequenceNumber + ")" );
					sequenceNumber--;
				}
			}	
		}

		socket.close();
   	}
    
    private static ArrayList<byte[]> splitFile(String path) throws IOException{
    	ArrayList<byte[]> fileChunks = new ArrayList<byte[]>();
    	File f = new File(path);
    	long fileLength = f.length();
    	BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
    	int lastPacketLength = 0;
    	while(true){
    		byte[] buffer = new byte[BUFFER_SIZE];
    		int r = bis.read(buffer);
    		if(r == -1) break;
    		fileChunks.add(buffer);
    		lastPacketLength = r;
    	}
    	String s = "end of file," + lastPacketLength + ",";
    	fileChunks.add(s.getBytes("UTF-8"));
    	
    	return fileChunks;
    }
}