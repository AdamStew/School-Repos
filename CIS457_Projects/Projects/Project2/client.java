import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

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

					int timeoutTotal = 0;
					int seqNum = 0;
					int index = 0;
					int start = 0;
					int end = 5;

					Selector s = Selector.open();
					dc.configureBlocking(false);
					dc.register(s,SelectionKey.OP_READ);
							
					//Receive as many packets as we can.
					while(true){
						//Breakout if we timed-out.
						while(true){
							int time = s.select(500);
							if(time==0){
								timeoutTotal++;
								break; //Timeout.
							}else{
								Iterator i = s.selectedKeys().iterator();
								while(i.hasNext()){
									SelectionKey k = (SelectionKey)i.next();
									DatagramChannel mychannel = (DatagramChannel)k.channel();
									inBuf = ByteBuffer.allocate(packetSize);
									SocketAddress serveraddr = mychannel.receive(inBuf);
									inBuf.flip();
									int parity = getParity(inBuf);
									if(inBuf!=null && parity == 0){
										//try{
											seqNum = Integer.parseInt(inBuf.get(0) + "");

											System.out.println("Seq num received: " + seqNum);
											if(seqNum > 9) seqNum = 0;
									
											inBuf.position(1); //Skips the sequence number & parity.
											seqNumBuf[seqNum] = inBuf.duplicate();
											ByteBuffer ackBuf = ByteBuffer.wrap(("Ack" + seqNum).getBytes());
											dc.send(ackBuf, serveraddr);
										//}catch(IndexOutOfBoundsException e){
											//System.out.println("Something went wrong with reordering with high delay.");
										//}
									}
									i.remove();
								}
							}
						}

						//This is horrid, but will do for now.
						if(timeoutTotal > 10){
							break;
						}

						//We only want to write until we run into a packet we're missing.
						while(start < end){
							if(seqNumBuf[index]!=null){
								fc.write(seqNumBuf[index]);
								System.out.println("Packet received from server.");
								start++;
								index = (index+1)%10;
							}else{
								break;
							}
						}

						start = index;
						end = (start+5)%10;
						if(start > end){
							end += 10;
						}

						//Reset the buffers we got.
						for(int i=0; i<seqNumBuf.length; i++){
							seqNumBuf[i] = null;
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
		for(int i=0; i<buf.remaining(); i++){
			parity += getByteParity(buf.get(i));
		}
		return parity%2;
	}

	public static int getByteParity(Byte byteA){
		int decValue = byteA.intValue();
		int numOnes = 0;
		if(decValue > 127){
			decValue -= 128;
			numOnes++;
		}
		if(decValue > 63){
			decValue -= 64;
			numOnes++;
		}
		if(decValue > 31){
			decValue -= 32;
			numOnes++;
		}
		if(decValue > 15){
			decValue -= 16;
			numOnes++;
		}
		if(decValue > 7){
			decValue -= 8;
			numOnes++;
		}
		if(decValue > 3){
			decValue -= 4;
			numOnes++;
		}
		if(decValue > 1){
			decValue -= 2;
			numOnes++;
		}
		if(decValue == 1){
			numOnes++;
		}
		return numOnes%2;
	}
}
