import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;



public class UDP_Client {

	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(6000));
		channel.configureBlocking(false);
		String newData = "Sent String";

		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		
		
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		
		int bytesSent = channel.send(buf, new InetSocketAddress("localhost", 5000));
		
		

			   if(selector.select(20000) > 0){

			       //**CODE NEVER REACHES THIS POINT**

			       ByteBuffer packet = ByteBuffer.allocate(200);
			       channel.receive(packet);
			       
			       String s = new String(packet.array());
			       System.out.println(s);
			       
			   }
			   
			
	}

}