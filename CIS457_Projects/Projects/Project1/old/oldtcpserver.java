import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
CIS 457 Project 1, Part A
Due: 01/24/2017
*/

class tcpserver{
	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();
			String m = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(m);
			try{
				c.bind(new InetSocketAddress(port));
				while(true){
					SocketChannel sc = c.accept();
					System.out.println("The server has connected to a client.");
					ByteBuffer inBuffer = ByteBuffer.allocate(4096);
					sc.read(inBuffer);
					String fileName = new String(inBuffer.array());
					fileName = fileName.trim();
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
					}
	
				}
			}catch(IllegalArgumentException e){
				System.out.println("There was an error connecting to that port number.");
			}	
		}catch(IOException e){
			System.out.println("Got an IO Exception.");
		}
	}
}

