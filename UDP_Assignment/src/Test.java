import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test {

	static String inFile = "D:/1.zip";
	static String outFile = "D:/2.zip";


	public static void main(String[] args) throws IOException {
		
		
		FileInputStream fin = new FileInputStream(inFile);
		FileOutputStream fout = new FileOutputStream(outFile);
		
		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();



		ByteBuffer buffer = ByteBuffer.allocate(48);
		
		while (true) {
			
			buffer.clear();
			
			int r = fcin.read(buffer);
			
			if (r == -1) {
				break;
			}
			
			buffer.flip();
			
			fcout.write(buffer);
		}
		
	}
	
	
}