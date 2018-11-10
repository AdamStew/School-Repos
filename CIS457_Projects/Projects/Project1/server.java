import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
CIS 457 Project 1, Part 2
Due: 02/06/2017
*/

class server {
	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();

			while(c.isOpen()){
				try{
					String m = cons.readLine("Enter desired port number: ");
					int port = Integer.parseInt(m);
					c.bind(new InetSocketAddress(port));
	
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
			System.out.println("Got an IO Exception in server.");
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
			while(sc.isConnected()){
				sc.read(inBuffer);
				fileName = new String(inBuffer.array());
				fileName = fileName.trim();

				if(fileName.toUpperCase().equals("LS")){
					File f = new File(".");
					File[] list = f.listFiles();
					String[] strList = new String[list.length];
					String listOfFiles = "Files Available: ";
					for(int i=0; i<list.length; i++){
						listOfFiles += list[i].getName() + "\n";
					}
					ByteBuffer listBuf = ByteBuffer.wrap(listOfFiles.getBytes());
					sc.write(listBuf);
				}else if(!fileName.toUpperCase().trim().equals("QUIT")){
					File sendFile = new File(fileName);

					if(!sendFile.exists()){
						ByteBuffer outBuffer = ByteBuffer.wrap(new String("DNE").getBytes());
						sc.write(outBuffer);
						System.out.println("That file doesn't exist.");
					}else{
						try{
							String fileSize = (int)sendFile.length() + "";
							ByteBuffer sizeBuf = ByteBuffer.wrap(fileSize.getBytes());
							sc.write(sizeBuf);

							FileChannel fc = FileChannel.open(sendFile.toPath());
							ByteBuffer outBuffer = ByteBuffer.allocate(Integer.parseInt(fileSize));
							int bytesRead = fc.read(outBuffer);
							while (bytesRead != -1){
								outBuffer.flip();
								sc.write(outBuffer);
								outBuffer.compact();
								bytesRead = fc.read(outBuffer);
							}
							
							System.out.println("The requested file has been sent.");
						}catch(IOException e){
							System.out.println("Error while sending file to client");
						}
					}
				}else{
					sc.close();
					System.out.println("A client has safely disconnected");
				}
				inBuffer.clear();
				inBuffer.put(new byte[4096]);
				inBuffer.clear();
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception in server thread.");
		}
	}
}
