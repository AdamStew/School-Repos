import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/******************************************************
 * @Authors: Adam Stewart, Josh Hopman, Trace Remik.
 * Project 2: UDP Reliability
 ******************************************************/

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
				ByteBuffer inBuf = ByteBuffer.allocate(packetSize);
				SocketAddress clientaddr = dc.receive(inBuf);
				String fileName = new String(inBuf.array());
				fileName = fileName.trim();
				File sendFile = new File(fileName);
	
				if(sendFile.exists()){

					int fileSize = (int)sendFile.length();
					int numPackets = 0;

					//Send how many packets there are (if-stmt to avoid integer division).
					if(fileSize%(packetSize-2) == 0){ //Minus 2 for seqNum and parity header.
						numPackets = fileSize/packetSize;
					}else{
						numPackets = fileSize/packetSize + 1;
					}

					//Now send the packets of the file.
					FileChannel fc = FileChannel.open(sendFile.toPath());

					//Setting up our array to keep track of our window.
					boolean[] seqNumsAck = new boolean[10];
					ByteBuffer[] seqNumBuf = new ByteBuffer[10];
					for(int i=0; i<seqNumBuf.length; i++){
						seqNumBuf[i] = null;
					}

					Integer seqNum = 0;

					ByteBuffer outBuf = ByteBuffer.allocate(packetSize-1); //Allocates without parity header.
					outBuf.put(seqNum.byteValue()); //Writes the packet number to the start.

					int temp = 0;
					int window = 0;
					int index = 0;
					int start = 0;
					int end = 5;


					//Send until we're confirmed to be done.
					while(numPackets > 0){

						//This will add any NEW packets that it needs to, to send.
						while(start<end){
							//If there's no more packets to read, then break.
							if(fc.read(outBuf) > 0){
								outBuf.flip();
								Integer parity = getParity(outBuf);

								ByteBuffer newBuf = ByteBuffer.allocate(packetSize);
								newBuf.put(parity.byteValue());
								while(outBuf.hasRemaining()){
									newBuf.put(outBuf.get());
								}

								newBuf.flip();
								seqNumBuf[index] = newBuf.duplicate(); //Keeping the old buffers, incase of resend.

								//Starting the next packet.
								outBuf = ByteBuffer.allocate(packetSize-1);
								index = (index+1)%10;
								start++;


								outBuf.clear();
								outBuf.put(new byte[packetSize-1]); //wipe it clean
								outBuf.clear();
								seqNum = (seqNum+1)%10;
								outBuf.put(seqNum.byteValue()); //Writes the packet number to the start
							}else{
								break;
							}
						}

						//Sending the packets in the window.
						if(numPackets >= 5){
							for(int i=0; i<5; i++){
								try{
									if(!seqNumsAck[(window+i)%10]){
										dc.send(seqNumBuf[(window+i)%10], clientaddr);
										System.out.println("Packet number " + (window+i)%10 + " sent to client.");
									}
								}catch(NullPointerException e){
									//Because block.
								}
							}
						}else{
							//Comes here if we're on our last few packets, so it doesn't go out of bounds.
							int j = numPackets;
							for(int i=0; i<j; i++){
								try{
									if(!seqNumsAck[(window+i)%10]){
										dc.send(seqNumBuf[(window+i)%10], clientaddr);
										System.out.println("Packet number " + (window+i)%10 + " sent to client.");
									}
								}catch(NullPointerException e){
									//Because block.
								}
							}
						}

						//Resets the array of acknowledgements for the values outside our current window.
						for (int i=5; i<10; i++){
							seqNumsAck[(i+window)%10] = false;
						}

						//Check what acknowledgements we got, times-out after 0.5 seconds.
						Selector s = Selector.open();
						dc.configureBlocking(false);
						dc.register(s,SelectionKey.OP_READ);
						while(true){
							int time = s.select(700); //ms
							if(time==0){
								break;
							}else{
								Iterator i = s.selectedKeys().iterator();
								while(i.hasNext()){
									SelectionKey k = (SelectionKey)i.next();
									DatagramChannel mychannel = (DatagramChannel)k.channel();
									ByteBuffer ackBuf = ByteBuffer.allocate(500);
									clientaddr = mychannel.receive(ackBuf);
									if(getParity(ackBuf) == 0){
										String ackStr = new String(ackBuf.array()).trim();
										try{
											int ackNum = Integer.parseInt(ackStr.charAt(4) + "");
											seqNumsAck[ackNum] = true;
											System.out.println("Got " + ackStr.substring(1,4) + "nowledgement for packet number " + ackNum + ".");
										}catch(NumberFormatException e){
											//Duplicate file names were sent.
										}
									}
									i.remove();
								}
							}
						}

						//It will slide the window for every consecutive acknowledgement.
						while(seqNumsAck[temp]){
							window = (window+1)%10;
							temp = (temp+1)%10;
							numPackets--;
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
						if(start > end){
							end += 10;
						}

						//Gets rid of packets we don't need anymore.
						for(int i=5; i<10; i++){
							seqNumBuf[(window+i)%10] = null; 
						}

						//Need to flip the packets we're resending.
						for(int i=0; i<seqNumBuf.length; i++){
							if(seqNumBuf[i] != null){
								seqNumBuf[i].flip();
							}
						}

						System.out.println("End of wave.");
					}
					System.out.println("File transfer completed.");
				}
			}



		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}

	public static int getParity(ByteBuffer buf){
		int parity = 0;
		while(buf.hasRemaining()){
			parity += getByteParity(buf.get());
		}
		buf.flip();
		return parity%2;
	}

	public static int getByteParity(Byte byteA){
		int decValue = byteA.intValue();
		int numOnes = 0;
		if(decValue >= 128){
			decValue -= 128;
			numOnes++;
		}
		if(decValue >= 64){
			decValue -= 64;
			numOnes++;
		}
		if(decValue >= 32){
			decValue -= 32;
			numOnes++;
		}
		if(decValue >= 16){
			decValue -= 16;
			numOnes++;
		}
		if(decValue >= 8){
			decValue -= 8;
			numOnes++;
		}
		if(decValue >= 4){
			decValue -= 4;
			numOnes++;
		}
		if(decValue >= 2){
			decValue -= 2;
			numOnes++;
		}
		if(decValue >= 1){
			decValue -= 1;
			numOnes++;
		}
		return numOnes%2;
	}
}
