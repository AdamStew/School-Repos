import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
CIS 457 Project 1, Part 2
Due: 02/06/2017
*/

class client {
	public static void main(String args[]){
		try{
			SocketChannel sc;
			Console cons = System.console();
			//This loop is incase invalid IP/Port is given.
			while(true){ // TODO: !sc.isConnected()
				sc = SocketChannel.open(); //SocketChannel is instantiated in the loop for an error checking reason.
				String ip = cons.readLine("Enter IP Address: ");
				String p = cons.readLine("Enter desired port number: ");
				//String ip = "127.0.0.1";
				//String p = "3335";
				try{
					int port = Integer.parseInt(p.trim());
					sc.connect(new InetSocketAddress(ip.trim(), port));
					break;
				}catch(IllegalArgumentException | IOException e){
					sc.close();
					System.out.println("There was an error connecting to that IP address or using that port number.");
				}
			}
			
			String fileName = cons.readLine("Commands:\n*********\nquit: disconnects from the server\nls: displays the contents of the working directory\nOtherwise specify the file to download.\n\n");
			while(!fileName.toUpperCase().equals("QUIT")){
				System.out.println(fileName + "!");
				ByteBuffer outBuffer = ByteBuffer.wrap(fileName.getBytes());
				sc.write(outBuffer);

				if(fileName.toUpperCase().equals("LS")){
					ByteBuffer inBuffer = ByteBuffer.allocate(4096);
					sc.read(inBuffer);
					String list = new String(inBuffer.array()).trim();
					System.out.println(list);
				}else{
					ByteBuffer sizeBuf = ByteBuffer.allocate(4096);
					sc.read(sizeBuf);
					int bufferSize = Integer.parseInt(new String(sizeBuf.array()).trim());
					int bytesRead = bufferSize;
					fileName = fileName + "2";

					ByteBuffer inBuffer = ByteBuffer.allocate(bufferSize);
					String message = new String(inBuffer.array());

					if(message.trim().equals("DNE")) {
						System.out.println("File does not exist, please try again.\n");
					}else if(bytesRead > 0) {
						
						System.out.println("Downloading file...\n");
						File file = new File(fileName);
						FileOutputStream fos = new FileOutputStream(file);
						FileChannel fc = fos.getChannel();

						System.out.println(bytesRead);
						System.out.println(bufferSize);
						bytesRead = sc.read(inBuffer);
						System.out.println(bytesRead);	
						inBuffer.flip();
						fc.write(inBuffer);
						inBuffer.compact();

						System.out.println("File successfully receieved.");
					}
				}
				fileName = cons.readLine("Enter a file name, or 'ls' for a list of files. Enter 'quit' when ready to exit. \n").trim();
			}
			ByteBuffer outBuffer = ByteBuffer.wrap(fileName.getBytes());
			sc.write(outBuffer);
			sc.close();
		}catch(IOException e){
			System.out.println("There was an IO exception, somewhere.");
		}
	}
}
