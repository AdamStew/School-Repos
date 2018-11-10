import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class udpclient{
	public static void main(String args[]){
		try{
			DatagramChannel dc = DatagramChannel.open();
			Console cons = System.console();
			//String ip = cons.readLine("Enter IP Address: ");
			//String p = cons.readLine("Enter desired port number: ");
			//int port = Integer.parseInt(p);
			//sc.connect(new InetSocketAddress(ip, port));
			String m = cons.readLine("Enter your message: ");
			ByteBuffer buf = ByteBuffer.wrap(m.getBytes());
			dc.send(buf,new InetSocketAddress("127.0.0.1", 2482));
			dc.close();
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
