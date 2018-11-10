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

			tcpclientthread t = new tcpclientthread(sc);
			t.start();

			System.out.println("Please chat away with other users.  Type 'Quit' to leave. \n");
			String message = "";
			while (!message.equals("Quit")){
				message = cons.readLine("");
				ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
				sc.write(buf);
			}			
			sc.close();
			System.exit(0);
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}

class tcpclientthread extends Thread {
	SocketChannel sc;
	
	tcpclientthread(SocketChannel channel){
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
					String output = "Server Message: " + message;
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
