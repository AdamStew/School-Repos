import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class server{
	public static void main(String args[]){
		try{
			int packetSize = 1024; //1kb
			DatagramChannel dc = DatagramChannel.open();
			Console cons = System.console();

			//User-inputs and error checking.
			while(true){
				try{
					String m = cons.readLine("Enter desired port number: ");		
					int port = Integer.parseInt(m);
					dc.bind(new InetSocketAddress(port));
					break;
				}catch(IOException | NumberFormatException e){
					System.out.println("There was an error connecting to that port number.");
				}
			}

			//Comes here after a valid port is set-up.
			while(true){

				//Receiving the desired file's name.
				ByteBuffer inBuf = ByteBuffer.allocate(4096);
				SocketAddress clientaddr = dc.receive(inBuf);
				String fileName = new String(inBuf.array());
				fileName = fileName.trim();
				File sendFile = new File(fileName);
	
				if(sendFile.exists()){
					int fileSize = (int)sendFile.length();
					String numPackets = "";

					//Send how many packets there are (if-stmt to avoid integer division).
					if(fileSize%packetSize == 0){
						numPackets = fileSize/packetSize + "";
					}else{
						numPackets = fileSize/packetSize + 1 + "";
					}

					ByteBuffer sizeBuf = ByteBuffer.wrap(numPackets.getBytes());
					dc.send(sizeBuf, clientaddr);

					//Now send the packets of the file.
					ByteBuffer outBuf = ByteBuffer.allocate(packetSize);
					FileChannel fc = FileChannel.open(sendFile.toPath());
					while(fc.read(outBuf) > 0){
						outBuf.flip();
						dc.send(outBuf, clientaddr);
						outBuf.compact();
						System.out.println("Packet sent to client.");
					}
					System.out.println("");
				}else{
					//Let the server know that that file doesn't exist.
					ByteBuffer outBuffer = ByteBuffer.wrap(new String("DNE").getBytes());
					dc.send(outBuffer, clientaddr);
					System.out.println("That file doesn't exist.");
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
