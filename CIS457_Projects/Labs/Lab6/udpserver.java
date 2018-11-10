import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class udpserver{
	public static void main(String args[]){
		try{
			DatagramChannel dc = DatagramChannel.open();
			Selector s = Selector.open();
			dc.configureBlocking(false);
			dc.register(s,SelectionKey.OP_READ);
			dc.bind(new InetSocketAddress(2482));
			while(true){
				int num = s.select(5000); //ms
				if(num==0){
					System.out.println("Nobody sent anything in time.");
				}else{
					Iterator i = s.selectedKeys().iterator();
					while(i.hasNext()){
						SelectionKey k = (SelectionKey)i.next();
						DatagramChannel mychannel = (DatagramChannel)k.channel();
						ByteBuffer buffer = ByteBuffer.allocate(4096);
						SocketAddress clientaddr = mychannel.receive(buffer);
						String message = new String(buffer.array());
						System.out.println(message);
						i.remove();
					}
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
