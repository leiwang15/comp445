import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Test {

	static String inFile = "D:/1.zip";
	static String outFile = "D:/2.zip";
	private static int packetSize = 1500;

	public static void main(String[] args) throws IOException {

		// FileInputStream fin = new FileInputStream(inFile);
		// FileOutputStream fout = new FileOutputStream(outFile);
		//
		// FileChannel fcin = fin.getChannel();
		// FileChannel fcout = fout.getChannel();
		//
		//
		//
		// ByteBuffer buffer = ByteBuffer.allocate(48);
		//
		// while (true) {
		//
		// buffer.clear();
		//
		// int r = fcin.read(buffer);
		//
		// if (r == -1) {
		// break;
		// }
		//
		// buffer.flip();
		//
		// fcout.write(buffer);
		// }

		String path = "C:/Users/AZNGT/Desktop/test.pdf";
		String pathOut = "C:/Users/AZNGT/Desktop/out.pdf";

		long size = new File(path).length();
		ByteBuffer buffer = null;

		for (long i = 0; i < size; i += packetSize) {
			buffer = UDP_Util.readFileChunk(path, packetSize, i);
			UDP_Util.writeBufferToFile(pathOut, buffer);
		}

	}

}