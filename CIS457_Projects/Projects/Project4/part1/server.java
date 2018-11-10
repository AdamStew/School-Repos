import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;

class server{
	static ConcurrentHashMap<String, SocketChannel> m = new ConcurrentHashMap<String, SocketChannel>();

	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();
			//String m = cons.readLine("Enter desired port number: ");
			//int port = Integer.parseInt(m);
			int port = 3334;
			while(c.isOpen()){
				c.bind(new InetSocketAddress(port));
				
				while(true){
					SocketChannel sc = c.accept();
					tcpserverthread t = new tcpserverthread(sc, m);
					t.start();
					System.out.println("Client has connected to server.");
					
					//sc.close();
				}
				//System.exit(0);
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}

class tcpserverthread extends Thread {
	SocketChannel sc;
	String username;
	ConcurrentHashMap<String, SocketChannel> hash;
	
	tcpserverthread(SocketChannel channel, ConcurrentHashMap<String, SocketChannel> m){
		sc = channel;
		hash = m;
	}	

	public void run(){
		try{
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			String message = "";
			while(!message.toLowerCase().equals("/quit")){
				sc.read(buffer);
				message = new String(buffer.array()).trim();
				//if(username != null) username = username.toLowerCase();
				
				if(username == null){
					username = message;
					hash.put(username, sc);
					System.out.println("Username set to: " + username);
				}else if(message != null && message.length() > new String("/whisper").length() && message.toLowerCase().substring(0, new String("/whisper").length()).equals("/whisper")){
					String[] parts = message.split(" ");
					String whisperTo = parts[1];
					message = "Whisper from " + username + ": " + message.substring(whisperTo.length() + 2 + new String("/whisper").length(), message.length());
				
					SocketChannel userObj = hash.get(whisperTo);
					if(userObj != null){
						userObj.write(ByteBuffer.wrap(message.getBytes()));
						log("Whisper sent to " + whisperTo);
					}else{
						log("No such user named: " + whisperTo + "...");
					}
				}else if(message != null && message.equals("/users")){
					String userStr = "Users currently connected: ";
					
					for(String key : hash.keySet()){
						userStr += key + ", ";
					}

					userStr = userStr.substring(0, userStr.length() - 2);
					sc.write(ByteBuffer.wrap(userStr.getBytes()));
				}else if(username != null && (username.toLowerCase().equals("trace") || username.toLowerCase().equals("adam") || username.toLowerCase().equals("zach")) && message != null && message.toLowerCase().contains("/kick")){
					String[] parts = message.split(" ");

					SocketChannel userObj = hash.get(parts[1]);

					if(userObj != null){
						log("Kicking " + parts[1] + "...");

						userObj.write(ByteBuffer.wrap(new String("/kicked").getBytes()));
						userObj.close();
						hash.remove(parts[1]);
					}else{
						log("No such user named: " + parts[1] + "...");
					}
				}else if(message.toLowerCase().equals("/quit")){
					System.out.println(username + " has left.");
					sc.write(ByteBuffer.wrap(new String("/quit").getBytes()));	
					sc.close();
					hash.remove(username);
					//System.exit(0);
				}else{
					String output = username + ": " + message;

					broadcastMsg(username, output, hash);
					System.out.println(output);
				}

				clearBuf(buffer);
			}
		}catch(IOException e){
			//System.out.println("Got an IO Exception in a thread.");
		}
	}

	public static void clearBuf(ByteBuffer buffer){
		buffer.clear();
		buffer.put(new byte[4096]);
		buffer.clear();
	}

	public static void broadcastMsg(String username, String message, ConcurrentHashMap<String, SocketChannel> map) {
		try{
			for(String key : map.keySet()){
				if(!username.equals(key)){
					map.get(key).write(ByteBuffer.wrap(message.getBytes()));
					System.out.println("Sent message to: " + key);
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception in server");
		}
	}

	public void log(String l){
		System.out.println(l);
	}
}
