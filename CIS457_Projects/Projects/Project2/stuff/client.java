import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

/******************************************************
 * @Authors: Adam Stewart, Josh Hopman, Trace Remik.
 * Project 2: UDP Reliability
 ******************************************************/

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
					}else{
						System.out.println("Having trouble reaching this port, try again.");
					}
				}catch(IOException | IllegalArgumentException e){
					System.out.println("There was an error connecting to that IP address or using that port number.");
				}
			}

			//Comes here after connection.
			int packetSize = 1024; //1kb
			while(true){
				String fileName = cons.readLine("Enter desired file: ");
				if(!fileName.equals("/quit")){
					//Sending the file name.
					ByteBuffer outBuf = ByteBuffer.wrap(fileName.getBytes());
					dc.send(outBuf,new InetSocketAddress(ip, port));

					File file = new File(fileName + "2");
					FileOutputStream fos = new FileOutputStream(file);
					FileChannel fc = fos.getChannel();
					ByteBuffer inBuf = ByteBuffer.allocate(packetSize);

					//Setting up our array to keep track of our window.
					ByteBuffer[] seqNumBuf = new ByteBuffer[10];
					boolean[] seqNumsRec = new boolean[10];
					for (int i=0; i<seqNumsRec.length; i++){
						seqNumsRec[i] = false;
						seqNumBuf[i] = null;
					}

					int timeoutTotal = 10;
					int seqNum = 0;
					int index = 0;
					int start = 0;
					int end = 5;
					boolean fileFlag = true;
					

					Selector s = Selector.open();
					dc.configureBlocking(false);
					dc.register(s,SelectionKey.OP_READ);

					//Receive as many packets as we can.
					while(true){
						//Breakout if we timed-out.
						while(true){
							int time = s.select(500);
							if(time==0){
								timeoutTotal--;
								break; //Timeout.
							}else{
								Iterator i = s.selectedKeys().iterator();
								while(i.hasNext()){
									SelectionKey k = (SelectionKey)i.next();
									DatagramChannel mychannel = (DatagramChannel)k.channel();
									inBuf = ByteBuffer.allocate(packetSize);
									SocketAddress serveraddr = mychannel.receive(inBuf);
									inBuf.flip();
									if(inBuf!=null && inBuf.remaining() != 0 && getParity(inBuf) == 0){
										try{
											seqNum = Integer.parseInt(inBuf.get(1) + "");

											System.out.println("Packet number " + seqNum + " received.");
									
											inBuf.position(2); //Skips the sequence number and parity.
											seqNumBuf[seqNum] = inBuf.duplicate();
											ByteBuffer ackBuf = ByteBuffer.wrap(("Ack" + seqNum).getBytes());
											Integer parity = getParity(ackBuf);

											ByteBuffer newBuf = ByteBuffer.wrap((parity + "Ack" + seqNum).getBytes());
											
											dc.send(newBuf, serveraddr);
											fileFlag = false;
											timeoutTotal = 10;
										}catch(IndexOutOfBoundsException e){
											System.out.println("Something went wrong with reordering with high delay.");
										}
									}
									i.remove();
								}
							}
						}

						//If we got no packets on our first cycle, it's probably because the name of the file, never got there.
						//That isn't necessarily true however, since we could have just gotten very unlucky on the first wave of packets.
						if(fileFlag){
							System.out.println("It seems the file name you requested is lost, corrupted, or nonexistent.");
							file.delete();
							break;
						}

						//Basically, if we get no packets 10 cycles in a row, it'll quitout.
						//Sometimes this gets triggered with 80% packetloss, just from being unlucky.
						if(timeoutTotal == 0){
							break;
						}

						//We only want to write until we run into a packet we're missing.
						while(start < end){
							if(seqNumBuf[index]!=null){
								fc.write(seqNumBuf[index]);
								System.out.println("Packet number " + index + " written to file.");
								start++;
								seqNumBuf[index] = null;
								index = (index+1)%10;
							}else{
								break;
							}
						}

						//Gets rid of packets we don't need anymore.
						for(int i=5; i<10; i++){
							seqNumBuf[(index+i)%10] = null; 
						}

						start = index;
						end = start+5;

						//Just for a print statement.
						if(timeoutTotal == 9){
							System.out.println("End of wave.");
						}

					}
				}else{
					//Shutdown if '/quit' typed.
					break;
				}
			}
			dc.close();
		}catch(IOException e){
			System.out.println("Got an IO Exception");
			System.out.println(e.getMessage());
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
