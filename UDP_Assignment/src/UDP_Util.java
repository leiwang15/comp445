import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class UDP_Util {

	public static DatagramChannel createChannel(int port, boolean blocking) {
		DatagramChannel channel = null;

		try {
			channel = DatagramChannel.open();
			channel.socket().bind(new InetSocketAddress(port));

			channel.configureBlocking(blocking);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return channel;
	}

	public static Selector getSelector(DatagramChannel ch) {
		Selector selector = null;
		try {

			selector = Selector.open();
			ch.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return selector;

	}

	public static ByteBuffer readFileChunk(String path, int length, long offset) {
		FileInputStream fileInputStream = null;
		FileChannel fChannel;
		ByteBuffer buffer = null;
		try {
			fileInputStream = new FileInputStream(path);
			fChannel = fileInputStream.getChannel();
			fChannel.position(offset);
			buffer = ByteBuffer.allocate(length);
			fChannel.read(buffer);
		} catch (IOException e) {

			e.printStackTrace();
		}
		buffer.flip();
		return buffer;

	}

	public static void writeBufferToFile(String path, ByteBuffer buffer) {

		try {
			File file = new File(path);
			FileOutputStream fStream = new FileOutputStream(file, true);
			FileChannel fChannel = fStream.getChannel();
			fChannel.write(buffer);
			fStream.close();
			fChannel.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public static ByteBuffer sendPacketWithAck(ByteBuffer buffer, int timeout, DatagramChannel ch, InetSocketAddress isa){
		ByteBuffer ack = ByteBuffer.allocate(1);
		Selector s = UDP_Util.getSelector(ch);
		
		try {
			ch.send(buffer, isa);
			if (s.select(timeout) > 0) {
				
				ch.receive(ack);
				ack.flip();
				//System.out.println("Received ack: " + status);
			}
			ch.send(buffer, isa);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return ack;
		
	}
	
	
}
