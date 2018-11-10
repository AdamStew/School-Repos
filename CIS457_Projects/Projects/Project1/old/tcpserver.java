import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
CIS 457 Project 1, Part 2
Due: 02/06/2017
*/

class tcpserver{
	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();

			//The reason this for loop is here, is so the user can retry their port, if an invalid one is answered.
			//Had to loop everything, since I kept getting thread errors, so if there's another way to do it, go ahead.
			while(true){
				try{
					String m = cons.readLine("Enter desired port number: ");
					int port = Integer.parseInt(m);
					c.bind(new InetSocketAddress(port));
	
					//sc accept, and thread is in the loop so that it works for multiple clients.
					while(true){
						SocketChannel sc = c.accept();
						tcpserverthread t = new tcpserverthread(sc);
						t.start();
						System.out.println("The server has connected to a client.");		
					}
				}catch(IOException | IllegalArgumentException e){ 
					System.out.println("There was an error connecting to that port number.");
				}	
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception.");
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
			ByteBuffer inBuffer = ByteBuffer.allocate(4096);
			String fileName = "";
			while(true){
				sc.read(inBuffer);
				fileName = new String(inBuffer.array());
				fileName = fileName.trim();		
				if(fileName.toUpperCase().equals("LS")){
					File f = new File(".");
					File[] list = f.listFiles();
					String[] strList = new String[list.length];
					String listOfFiles = "Files Available: ";
					for(int i=0; i<list.length; i++){
						listOfFiles += list[i].getName() + "   ";
					}
					ByteBuffer listBuf = ByteBuffer.wrap(listOfFiles.getBytes());
					sc.write(listBuf);
				}else if(!fileName.toUpperCase().equals("QUIT")){	
					
					File sendFile = new File(fileName);
					try{					
						FileChannel fc = FileChannel.open(sendFile.toPath());
						ByteBuffer outBuffer = ByteBuffer.allocate(1500);
						int bytesRead = fc.read(outBuffer);
						while (bytesRead != -1){
							outBuffer.flip();
							sc.write(outBuffer);
							outBuffer.compact();
							bytesRead = fc.read(outBuffer);
						}
						System.out.println("The requested file has been sent.");
					}catch(IOException e){
						System.out.println("That file doesn't exist.");
						ByteBuffer buf = ByteBuffer.wrap("DNE".getBytes());
						sc.write(buf);
					}
					inBuffer.clear();
					inBuffer.put(new byte[4096]);
					inBuffer.clear();
					
				}else{
					break;
				}
			}
			sc.close();
			System.out.println("A client has safely disconnected");
		}catch(IOException e){
			System.out.println("Got an IO Exception in a thread.");
		}
	}
}
