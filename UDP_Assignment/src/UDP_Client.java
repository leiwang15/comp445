import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;

public class UDP_Client {

	private static String path = "C:/Users/AZNGT/Desktop/test.pdf";
 private static String pathOut = "C:/Users/AZNGT/Desktop/out2.pdf";
	private static int packetSize = 1500;
	private static String SERVER_ADDR = "localhost";
	private static int SERVER_PORT = 6000;

	public static void main(String[] args) throws IOException {

		DatagramChannel channel = UDP_Util.createChannel(5000, false);

		Selector selector = UDP_Util.getSelector(channel);

		InetSocketAddress isa = new InetSocketAddress(SERVER_ADDR, SERVER_PORT);

		long size = new File(path).length();
		ByteBuffer buffer = null;
		ByteBuffer ack = ByteBuffer.allocate(1);

		for (long i = 0; i < size; i += packetSize) {
			buffer = UDP_Util.readFileChunk(path, packetSize, i);
			UDP_Util.writeBufferToFile(pathOut, buffer);
			channel.send(buffer, isa);
			if (selector.select(10) > 0) {
				ack.clear();
				channel.receive(ack);
				ack.flip();
				int status = (int) ack.array()[0];
				
				System.out.println("Client: Received ack from server: " + status);

			} else {
				System.out.println("Client: Time out, retrying...");
				i -= packetSize;

			}
		}
	}
}