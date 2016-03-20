import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

public class UDP_Server {
	private static final int BUFFER_SIZE = 20;
	private static final int PORT = 6789;
	static String outputFile = "D:/2.txt";
	
	
	public static void main(String[] args) throws IOException {
		UDP_Server server = new UDP_Server();
		server.receiveFileChunks();
		
	}
	
	private void receiveFileChunks() throws IOException{
		// Create a server socket
		DatagramSocket serverSocket = new DatagramSocket( PORT );

		// Set up byte arrays for sending/receiving data
		byte[] receiveData = new byte[ BUFFER_SIZE ];
		byte[] dataForSend = new byte[ BUFFER_SIZE ];
		ArrayList<byte[]> fileChunks = new ArrayList<byte[]>();
		int counter = 0;
		// Infinite loop to check for connections 
		while(true){

			// Get the received packet
			DatagramPacket received = new DatagramPacket( receiveData, receiveData.length );
			serverSocket.receive( received );

			// Get the message from the packet
			int message = ByteBuffer.wrap(received.getData()).getInt();

			Random random = new Random( );
			int chance = random.nextInt( 100 );

			// 1 in 2 chance of responding to the message
			if( ((chance % 1) == 0) ){
				
				byte[] data = received.getData();
				fileChunks.add(counter, data);
				counter++;
				for(byte[] b : fileChunks){
					String str = new String(b, "UTF-8");
		    		System.out.println(str);
				}
				
				
				System.out.println("FROM CLIENT: " + message);

				// Get packet's IP and port
				InetAddress IPAddress = received.getAddress();
				int port = received.getPort();

				// Convert message to uppercase 
				dataForSend = ByteBuffer.allocate(4).putInt( message ).array();

				// Send the packet data back to the client
				DatagramPacket packet = new DatagramPacket( dataForSend, dataForSend.length, IPAddress, port );
				serverSocket.send( packet ); 
				
				String rcvd = new String(received.getData(), 0, received.getLength());
				if (rcvd.contains("end of file")) break;
			} else {
				System.out.println( "Oops, packet with sequence number "+ message + " was dropped");
			}
		}
		
		mergeFile(fileChunks);
	}

	private void mergeFile(ArrayList<byte[]> list) throws IOException{
		for(byte[] b : list){
			String str = new String(b, "UTF-8");
    		System.out.println(str);
		}
		
		byte[] lastPacket = list.get(list.size() - 1);
		String str = new String(lastPacket, "UTF-8");
		String[] s = str.split(",");
		int lastPacketSize = Integer.parseInt(s[1]);
		File f = new File(outputFile);
		FileOutputStream fos = new FileOutputStream(f);
		int offset = 0;
		int size = BUFFER_SIZE * (list.size() - 2) + lastPacketSize;
		byte[] data = new byte[size]; 
		
		for(int i = 0; i < list.size() - 2; i++){
			System.arraycopy(list.get(i), 0, data, offset, BUFFER_SIZE);
//			fos.write(list.get(i), offset, BUFFER_SIZE);
			offset += BUFFER_SIZE;
//			System.out.println(offset);
		}
		System.arraycopy(list.get(list.size() - 2), 0, data, offset, lastPacketSize);
		String s1 = new String(data, "UTF-8");
		System.out.println(s1.toString()); 
		
		fos.write(data, 0, size);
		fos.flush();
		fos.close();
	}
}