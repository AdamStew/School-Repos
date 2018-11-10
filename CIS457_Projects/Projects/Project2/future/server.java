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
					//int port = 3333;
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


					//Because what are the odds the packet is dropped 30 times in a row??
					for(int i=0; i<30; i++){
						dc.send(sizeBuf.duplicate(), clientaddr);
					}

					//Now send the packets of the file.
					ByteBuffer outBuf = ByteBuffer.allocate(packetSize);
					FileChannel fc = FileChannel.open(sendFile.toPath());

					//Setting up our array to keep track of our window.
					boolean[] seqNumsAck = new boolean[10];
					ByteBuffer[] seqNumBuf = new ByteBuffer[10];
					for(int i=0; i<seqNumBuf.length; i++){
						seqNumBuf[i] = null;
					}

					boolean broke = false;
					Integer seqNum = 0;
					int window = 0; //Keeps track to what window starts at.
					int start = 0; //Keeps track of what new packets we need.
					int end = 5; //Keeps track of when to stop getting new packets.
					int index = 0;
					int temp = 0;
					int numP = Integer.parseInt(numPackets);
					outBuf.allocate(packetSize);
					outBuf.put(seqNum.byteValue()); //Writes the packet number to the start.

					while(numP > 0){ 

						//This will add any NEW packets that it needs to, to send.
						while(start<end){
							//If there's no more packets to read, then break.
							if(fc.read(outBuf) > 0){
								outBuf.flip();
								seqNumBuf[index] = outBuf.duplicate(); //Keeping the old buffers, incase of resend.

								//Starting the next packet.
								outBuf = ByteBuffer.allocate(packetSize);
								index = (index+1)%10;
								start++;

								outBuf.clear();
								outBuf.put(new byte[packetSize]); //wipe it clean
								outBuf.clear();
								System.out.println("SeqNumSent: " + seqNum);
								seqNum = (seqNum+1)%10;
								outBuf.put(seqNum.byteValue()); //Writes the packet number to the start
							}else{
								break;
							}
							
						}
						
						//Sending the packets in the window.
						if(numP >= 5){
							for(int i=0; i<5; i++){
								try{
									System.out.println(seqNumBuf[(window+i)%10].array());
									dc.send(seqNumBuf[(window+i)%10], clientaddr);
									System.out.println("Packet sent to client." + (window+i)%10);
								}catch(NullPointerException e){
									//Because block.
								}
							}
						}else{
							//Comes here if we're on our last few packets, so it doesn't go out of bounds.
							int j = numP;
							for(int i=0; i<j; i++){
								try{
									//tring fileName = new String(inBuf.array());
									System.out.println(seqNumBuf[(window+i)%10].array());
									dc.send(seqNumBuf[(window+i)%10], clientaddr);
									System.out.println("Packet sent to client." + (window+i)%10);
								}catch(NullPointerException e){
									//Because block.
								}
							}
						}/*else{
							//Can't do anything but pray it gets there.
							for(int i=0; i<30; i++){
								dc.send(seqNumBuf[window], clientaddr);
							}
							System.out.println("Packet sent to client." + window;
							break;
						}*/

						//Reset the array, for the incoming wave of acknowledgments.
						for (int i=0; i<seqNumsAck.length; i++){
							seqNumsAck[i] = false;
						}

						//Check what acknowledgements we got, times-out after 0.5 seconds.
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
									int ackNum2 = Integer.parseInt(ackStr.charAt(3) + "");
									seqNumsAck[ackNum2] = true;
									i.remove();
								}
							}
						}

						System.out.println(temp);
						//It will slide the window for every consecutive acknowledgement.
						while(seqNumsAck[temp]){
							window = (window+1)%10;
							temp = (temp+1)%10;
							numP--;
							System.out.println("Window incremented: " + window);
						}

						//Finds new start and end spots of the new packets to get.
						if(seqNumBuf[window]==null){
							start = window;
							end = (start+5)%10;
						}else if(seqNumBuf[(window+1)%10]==null){
							start = (window+1)%10;
							end = (start+4)%10;
						}else if(seqNumBuf[(window+2)%10]==null){
							start = (window+2)%10;
							end = (start+3)%10;
						}else if(seqNumBuf[(window+3)%10]==null){
							start = (window+3)%10;
							end = (start+2)%10;
						}else if(seqNumBuf[(window+4)%10]==null){
							start = (window+4)%10;
							end = (start+1)%10;
						}

						index = start;
						temp = start;
						if(start > end){
							end += 10;
						}

						//Gets rid of packets we don't need anymore.
						for(int i=5; i<10; i++){
							seqNumBuf[(window+i)%10] = null; 
						}
						
					}
				}else{
					try{
						ByteBuffer outBuffer = ByteBuffer.wrap(new String("DNE").getBytes());
						//Because what are the odds the packet is dropped 30 times in a row??
						for(int i=0; i<30; i++){
							dc.send(outBuffer.duplicate(), clientaddr);
						}
						System.out.println("That file doesn't exist.");
					}catch(NullPointerException e){
						//Because block.
					}
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
