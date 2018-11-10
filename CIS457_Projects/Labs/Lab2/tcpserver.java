import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpserver{
	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();
			String m = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(m);
			c.bind(new InetSocketAddress(port));
			while(true){
				SocketChannel sc = c.accept();
				ByteBuffer buffer = ByteBuffer.allocate(4096);
				sc.read(buffer);
				String message = new String(buffer.array());
				System.out.println(message);

				//Sending message back.
				ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
				sc.write(buf);
				sc.close();
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
