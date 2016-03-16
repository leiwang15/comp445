import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;

public class UDP_Server{
	
	
	
	public static void main(String[] args) throws IOException{
		
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress(5000));
		channel.configureBlocking(false);

		//Create a selector and register it:

		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		String data = "server data";
		
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(data.getBytes());
		buf.flip();
		
		

		//Spin

		while(true){

		   //If there's a packet available, fetch it:

		   if(selector.select(20000) > 0){

		       //**CODE NEVER REACHES THIS POINT**

		       ByteBuffer packet = ByteBuffer.allocate(200);
		       channel.receive(packet);
		       
		       String s = new String(packet.array());
		       System.out.println(s);
		      // int bytesSent = channel.send(buf, new InetSocketAddress("localhost", 6000));
		       
		   }
		   
		}
		
		
		
		
		
	}
	
	
	
}