import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
CIS 457 Project 1, Part 2
Due: 02/06/2017
*/

//If a client is ever force-closed (like with ctrl-c on the command line), then the server will go in an infinite loop.
class tcpclient{
	public static void main(String args[]){
		try{
			SocketChannel sc;
			Console cons = System.console();
			//This loop is incase invalid IP/Port is given.
			while(true){
				sc = SocketChannel.open(); //SocketChannel is instantiated in the loop for an error checking reason.
				String ip = cons.readLine("Enter IP Address: ");
				String p = cons.readLine("Enter desired port number: ");
				try{
					int port = Integer.parseInt(p.trim());
					sc.connect(new InetSocketAddress(ip.trim(), port));
					break;
				}catch(IllegalArgumentException | IOException e){
					sc.close();
					System.out.println("There was an error connecting to that IP address or using that port number.");
				}
			}
			
			//If an invalid file is selected, the server will print it, however the client doesn't know, and an
			//empty version of the file is created still.	
			String fileName = cons.readLine("Enter desired file name: ");
			while(!fileName.toUpperCase().equals("QUIT")){
				ByteBuffer outBuffer = ByteBuffer.wrap(fileName.getBytes());
				sc.write(outBuffer);

				if(fileName.toUpperCase().equals("LS")){
					ByteBuffer inBuffer = ByteBuffer.allocate(4096);
					sc.read(inBuffer);
					String list = new String(inBuffer.array()).trim();
					System.out.println(list);
					fileName = cons.readLine("Enter a file name, or type 'Quit' to quit. \n").trim();
				}else{
				
					int bufferSize = 15000000;
					fileName = fileName + "2";
					
					ByteBuffer inBuffer = ByteBuffer.allocate(bufferSize);
					int bytesRead = bufferSize;
					if(sc.read(inBuffer) > 0) {
						
						File file = new File(fileName);
						FileOutputStream fos = new FileOutputStream(file);
						FileChannel fc = fos.getChannel();
			
						//So since it kept getting stuck in the while loop after calling sc.read
						//when there was nothing left to read, it now keeps track to see if it's
						//on its' last cycle, since the last cycle will always be in theory, less
						//bytes left, compared to the buffer size.  This will do for now..			
						while(bytesRead == bufferSize){
							bytesRead = sc.read(inBuffer);
							inBuffer.flip();
							fc.write(inBuffer);
							inBuffer.compact();
						}
						System.out.println("File receieved.");
						fileName = cons.readLine("Want to request another file?  Type 'Quit' if not. \n").trim();
					}else{
						System.out.println("else");
						ByteBuffer buffer = ByteBuffer.allocate(4096);
						sc.read(buffer);
						String message = new String(buffer.array());
						if(message.trim().equals("DNE")) {
							System.out.println("File does not exist.");
						}
					}
				}
			}
			ByteBuffer outBuffer = ByteBuffer.wrap(fileName.getBytes());
			sc.write(outBuffer);
			sc.close();
		}catch(IOException e){
			System.out.println("There was an IO exception, somewhere.");
		}
	}
}
