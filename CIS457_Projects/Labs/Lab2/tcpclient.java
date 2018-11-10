import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpclient{
	public static void main(String args[]){
		try{
			SocketChannel sc = SocketChannel.open();
			Console cons = System.console();
			String ip = cons.readLine("Enter IP Address: ");
			String p = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(p);
			sc.connect(new InetSocketAddress(ip, port));
			String m = cons.readLine("Enter your message: ");
			ByteBuffer buf = ByteBuffer.wrap(m.getBytes());
			sc.write(buf);

			//Receiving message back.
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			sc.read(buffer);
			String message = new String(buffer.array());
			System.out.println(message);
			sc.close();
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
