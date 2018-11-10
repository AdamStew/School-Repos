import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class client{
	public static void main(String args[]){
		try{
			DatagramChannel dc = DatagramChannel.open();
			Console cons = System.console();
			String ip = "";
			int port = 0;

			//User-inputs and error checking.
			while(true){
				try{	
					ip = cons.readLine("Enter IP Address: ");
					String p = cons.readLine("Enter desired port number: ");
					port = Integer.parseInt(p);
	
					//Testing the port and IP-address.
					int timeout = 10000;
					if(InetAddress.getByName(ip).isReachable(timeout)) {
						InetSocketAddress testaddr = new InetSocketAddress(ip, port);
						break;
					}
				}catch(IOException | IllegalArgumentException e){
					System.out.println("There was an error connecting to that IP address or using that port number.");
				}
			}

			//Comes here after connection.
			while(true){
				String fileName = cons.readLine("Enter desired file: ");
				if(!fileName.equals("/quit")){
					//Sending the file name.
					ByteBuffer outBuf = ByteBuffer.wrap(fileName.getBytes());
					dc.send(outBuf,new InetSocketAddress(ip, port));
		
					//One of two inputs will come: "DNE" or the number of expected packets.
					ByteBuffer resultBuf = ByteBuffer.allocate(4096);
					SocketAddress serveraddr = dc.receive(resultBuf);
					String numPackets = new String(resultBuf.array()).trim();
					
					//Checks if file exists.
					if(!numPackets.trim().equals("DNE")){
						File file = new File(fileName + "2");
						FileOutputStream fos = new FileOutputStream(file);
						FileChannel fc = fos.getChannel();
						int numP = Integer.parseInt(numPackets);
						ByteBuffer inBuf = ByteBuffer.allocate(1024);

						//Runs until it has received all of the inputs (may have problems later, with packet dupe)
						while(numP != 0) {
							serveraddr = dc.receive(inBuf);
							inBuf.flip();
							fc.write(inBuf);
							inBuf.compact();
							numP -= 1;
							System.out.println("Packet received from server.");
						}
					}else{
						System.out.println("That file doesn't exist.");
					}
				}else{
					break;
				}
			}
			dc.close();
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
