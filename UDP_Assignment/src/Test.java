import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

public class Test {

	static String inFile = "D:/1.zip";
	static String outFile = "D:/2.zip";
	private static int packetSize = 1500;
	

	public static void main(String[] args) throws IOException {
		
		
//		FileInputStream fin = new FileInputStream(inFile);
//		FileOutputStream fout = new FileOutputStream(outFile);
//		
//		FileChannel fcin = fin.getChannel();
//		FileChannel fcout = fout.getChannel();
//
//
//
//		ByteBuffer buffer = ByteBuffer.allocate(48);
//		
//		while (true) {
//			
//			buffer.clear();
//			
//			int r = fcin.read(buffer);
//			
//			if (r == -1) {
//				break;
//			}
//			
//			buffer.flip();
//			
//			fcout.write(buffer);
//		}
		
		String path = "C:/Users/AZNGT/Desktop/test.pdf";
		String pathOut = "C:/Users/AZNGT/Desktop/out.pdf";
		File fileIn = new File(path);
		long size = fileIn.length();
		File file = new File(pathOut);
		FileOutputStream fStream = new FileOutputStream(file , true);
		FileChannel fChannel = fStream.getChannel();
		
		ByteBuffer buffer = null;
		
		for(long i=0; i<size; i+=packetSize){
			buffer = UDP_Util.readFileChunk(path, packetSize, i);
			fChannel.write(buffer);
			
		}
		
		
		
//		while(buffer.hasRemaining()){
//			System.out.print((char) buffer.get());
//		}
		
		fStream.close();
		fChannel.close();
	}
	
	
}