import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;

public class UDP_Server {

	private static String path = "C:/Users/AZNGT/Desktop/test.pdf";
	private static String pathOut = "C:/Users/AZNGT/Desktop/out.pdf";
	private static int packetSize = 1500;
	private static String CLIENT_ADDR = "localhost";
	private static int CLIENT_PORT = 5000;

	public static void main(String[] args) throws IOException {

		DatagramChannel channel = UDP_Util.createChannel(6000, false);

		Selector selector = UDP_Util.getSelector(channel);
		InetSocketAddress isa = new InetSocketAddress(CLIENT_ADDR, CLIENT_PORT);

		ByteBuffer packet = ByteBuffer.allocateDirect(packetSize);
		ByteBuffer ack = ByteBuffer.allocate(1);
		
		// Spin

		while (true) {

			// If there's a packet available, fetch it:

			if (selector.selectNow() > 0) {
				ack.clear();
				ack.put((byte) 1);
				
				System.out.println("Server: reveived packet.");
				packet.clear();
				packet.position(0);
				channel.receive(packet);
				packet.flip();
				UDP_Util.writeBufferToFile(pathOut, packet);
				
				
				System.out.println("Server: writing to file");
				System.out.println("Server: sending ack.");
				ack.flip();
				channel.send(ack, isa);
				

			}else{
				//System.out.println("Server: not receiving");
			}
		}

	}

}