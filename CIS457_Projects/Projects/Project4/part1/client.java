import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class client{
	public static void main(String args[]){
		try{
			SocketChannel sc = SocketChannel.open();
			Console cons = System.console();
			String ip = "127.0.0.1";
			//String ip = cons.readLine("Enter IP Address: ");
			//String p = cons.readLine("Enter desired port number: ");
			//int port = Integer.parseInt(p);
			int port = 3334;
			sc.connect(new InetSocketAddress(ip, port));

			tcpclientthread t = new tcpclientthread(sc);
			t.start();

			System.out.println("Connected to chat system. Please enter username to start off. Type '/quit' to leave. \n");
			System.out.print("Username: ");
			String message = "";
			while (!message.toLowerCase().equals("Some inconceivable message.")){
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
			while(!message.equals("/quit") && !message.equals("/kicked")){
				sc.read(buffer);
				message = new String(buffer.array()).trim();
				//message = message.toLowerCase();
				if(!message.toLowerCase().equals("/quit") && !message.equals("/kicked")){
					if(message != "") System.out.println(message);
					buffer.clear();
					buffer.put(new byte[4096]);
					buffer.clear();
				}
			}
			if(message.equals("/kicked")) {
				System.out.println("You have been kicked.");
			}else if(message.equals("/quit")) {
				System.out.println("You have left the chat room.");
			}
			sc.close();
			System.exit(0);
		}catch(IOException e){
			System.out.println("Got an IO Exception in a thread.");
		}
	}
}
