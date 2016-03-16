import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer.flip();
		return buffer;

	}
}
