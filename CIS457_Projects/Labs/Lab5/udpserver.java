import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class udpserver{
	public static void main(String args[]){
		try{
			DatagramChannel dc = DatagramChannel.open();
			Console cons = System.console();
			String m = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(m);
			dc.bind(new InetSocketAddress(port));

			while(true){
				//Receiving the client messages.
				ByteBuffer inBuf = ByteBuffer.allocate(4096);
				SocketAddress clientaddr = dc.receive(inBuf);
				String message = new String(inBuf.array());
				if(!message.equals("/quit")){
					System.out.println("Client message: " + message);

					//Sending the message back.
					ByteBuffer outBuf = ByteBuffer.wrap(message.getBytes());
					dc.send(outBuf,clientaddr);
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
