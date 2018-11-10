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
				tcpserverthread t = new tcpserverthread(sc);
				t.start();
				System.out.println("Please chat away with other users.  Type 'Quit' to leave. \n");
				String message = "";
				do{
					message = cons.readLine("");					
					ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
					sc.write(buf);
				}while(!message.equals("Quit"));
				sc.close();
				System.exit(0);
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}

class tcpserverthread extends Thread {
	SocketChannel sc;
	
	tcpserverthread(SocketChannel channel){
		sc = channel;
	}	

	public void run(){
		try{
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			String message = "";
			while(!message.equals("Quit")){
				sc.read(buffer);
				message = new String(buffer.array()).trim();
				if(!message.equals("Quit")){					
					String output = "Client Message: " + message;
					System.out.println(output);
					buffer.clear();
					buffer.put(new byte[4096]);
					buffer.clear();
				}
			}
			System.out.println("The user has left.");	
			sc.close();
			System.exit(0);
		}catch(IOException e){
			System.out.println("Got an IO Exception in a thread.");
		}
	}
}
