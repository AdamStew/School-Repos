import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

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
					if(fileSize%(packetSize-1) == 0){
						numPackets = fileSize/packetSize + "";
					}else{
						numPackets = fileSize/packetSize + 1 + "";
					}

					ByteBuffer sizeBuf = ByteBuffer.wrap(numPackets.getBytes());
					dc.send(sizeBuf, clientaddr);

					//Now send the packets of the file.
					ByteBuffer outBuf = ByteBuffer.allocate(packetSize);
					FileChannel fc = FileChannel.open(sendFile.toPath());

					boolean broke = false;
					Integer seqNum = 0;
					int window = 0;
					int index = 0;
					int end = window + 5;
					int numP = Integer.parseInt(numPackets);
					outBuf.put(seqNum.byteValue()); //Writes the packet number to the start.
					while(!broke){ //The rest of the bytebuffer is file bytes.
						//Sending five packets.
						while(index < end){
							if(fc.read(outBuf) > 0) {
								outBuf.flip();
								dc.send(outBuf, clientaddr); //send
								System.out.println("Packet sent to client.");
								index++;
								outBuf.clear();
								outBuf.put(new byte[packetSize]); //wipe it clean
								outBuf.clear();
								seqNum = (seqNum + 1)%10;
								outBuf.put(seqNum.byteValue()); //Writes the packet number to the start
							}else{
								broke = true;
								break;
							}
						}

						Selector s = Selector.open();
						dc.configureBlocking(false);
						dc.register(s,SelectionKey.OP_READ);
						while(true){
							int time = s.select(500); //ms
							if(time==0){
								break;
							}else{
								Iterator i = s.selectedKeys().iterator();
								while(i.hasNext()){
									SelectionKey k = (SelectionKey)i.next();
									DatagramChannel mychannel = (DatagramChannel)k.channel();
									ByteBuffer ackBuf = ByteBuffer.allocate(500);
									clientaddr = mychannel.receive(ackBuf);
									String ackStr = new String(ackBuf.array()).trim();
									int ackNum = Integer.parseInt(ackStr.charAt(3) + "");
									window = (window+1)%10; //moves window
									i.remove();
								}
							}
						}
						index = window;
						end = window + 5;
					}
				}else{
					//Let the server know that that file doesn't exist.
					try{
						ByteBuffer outBuffer = ByteBuffer.wrap(new String("DNE").getBytes());
						dc.send(outBuffer, clientaddr);
						System.out.println("That file doesn't exist.");
					}catch(NullPointerException e){
						
					}
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
