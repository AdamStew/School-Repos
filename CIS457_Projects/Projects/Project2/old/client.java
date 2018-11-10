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

						//Setting up our array to keep track of our window.
						boolean[] seqNumsRec = new boolean[10];
						for (int i=0; i<seqNumsRec.length; i++){
							seqNumsRec[i] = false;
						}

						int index = 0; //Index will keep track of what packet we SHOULD be on.

						//Runs until it has received all of the inputs
						while(numP != 0) {
							serveraddr = dc.receive(inBuf);
							inBuf.flip();

							int seqNum = Integer.parseInt(inBuf.get(0) + "");


							fc.write(inBuf); //Might accidentally write the seqNums into the file
							inBuf.compact();

							//TODO: Rework all of this.  Might be easier to do with a window of 0-14.

							//The first part of the if-stmt should correctly work, assumming the packets are received perfectly.
							if(index == seqNum && seqNumsRec[seqNum] == false){ //Only want it to be a received packet if we got the packet we're expecting.
								numP -= 1;
								seqNumsRec[seqNum] = true; //This acknowledges what packets we've received.
								System.out.println("Packet received from server.");

								if(index+1 > 9){ //Resets, since our window is only 0-9.
									index = 0;
								}else{
									index += 1;
								}
							}else{ //This junk doesn't work if packets are sent out of order yet, ie: 3-4-6-5-7 will only work for 3-4-6-7
								seqNumsRec[seqNum] = true; //This acknowledges what packets we've received.

								//This will make sure the index is kept on track of the sequence number.
								if(seqNum+1 > 9){
									index = 0;
								}else{
									index = seqNum+1;
								}
							}
							
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
