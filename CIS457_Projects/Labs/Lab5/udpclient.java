import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class udpclient{
	public static void main(String args[]){
		try{
			DatagramChannel dc = DatagramChannel.open();
			Console cons = System.console();
			String ip = cons.readLine("Enter IP Address: ");
			String p = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(p);

			while(true){
				String m = cons.readLine("Enter your message: ");
				if(!m.equals("/quit")){
					//Sending the message.
					ByteBuffer outBuf = ByteBuffer.wrap(m.getBytes());
					dc.send(outBuf,new InetSocketAddress(ip, port));
		
					//Receiving the message back.
					ByteBuffer inBuf = ByteBuffer.allocate(4096);
					SocketAddress serveraddr = dc.receive(inBuf);
					String message = new String(inBuf.array());
					System.out.println("Echo: " + message);
				}else{
					break;
				}
			}
			dc.close();
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
