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
			//Selector s = Selector.open();
			//dc.configureBlocking(false);
			//dc.register(s,SelectionKey.OP_READ);
			//int time = s.select(500); //0.5 seconds
			String ip = "";
			int port = 0;

			//User-inputs and error checking.
			while(true){
				try{	
					//ip = cons.readLine("Enter IP Address: ");
					//String p = cons.readLine("Enter desired port number: ");
					//port = Integer.parseInt(p);
					ip = "127.0.0.1";
					port = 3333;
	
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
				boolean ack = false;
				String numPackets = "";
				int numP = 0;
				String fileName = cons.readLine("Enter desired file: ");
				if(!fileName.equals("/quit")){
					//Sending the file name.
					ByteBuffer outBuf = ByteBuffer.wrap(fileName.getBytes());
					dc.send(outBuf,new InetSocketAddress(ip, port));
		
					//One of two inputs will come: "DNE" or the number of expected packets.


					//while(true){

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
									ByteBuffer resultBuf = ByteBuffer.allocate(500);
									SocketAddress clientaddr = mychannel.receive(resultBuf);
									numPackets = new String(resultBuf.array()).trim();
									ByteBuffer ackBuf = ByteBuffer.wrap("Ack-1".getBytes());
									mychannel.send(ackBuf, clientaddr);
									ack = true;
									i.remove();
								}
							}
						}
					//}
	
					//Checks if file exists.
					if(!ack){
						System.out.println("There was a packet loss, opps! Try again.");			
					}else if(!numPackets.trim().equals("DNE")){
						File file = new File(fileName + "2");
						FileOutputStream fos = new FileOutputStream(file);
						FileChannel fc = fos.getChannel();
						numP = Integer.parseInt(numPackets);
						ByteBuffer inBuf = ByteBuffer.allocate(1024);

						//Setting up our array to keep track of our window.
						ByteBuffer[] seqNumBuf = new ByteBuffer[10];
						boolean[] seqNumsRec = new boolean[10];
						for (int i=0; i<seqNumsRec.length; i++){
							seqNumsRec[i] = false;
							seqNumBuf[i] = null;
						}

						//Runs until it has received all of the packets.
						while(numP != 0) {
/*
							serveraddr = dc.receive(inBuf);
							inBuf.flip();
							int seqNum = Integer.parseInt(inBuf.get(0) + "");
							inBuf.position(1); //Skips the sequence number.
							fc.write(inBuf); 
							inBuf.compact();
							numP -= 1;
							System.out.println("Packet received from server.");
							ByteBuffer ackBuf = ByteBuffer.wrap(("Ack" + seqNum).getBytes());
							dc.send(ackBuf, serveraddr);
							if(numP == 0){
								break;
							}
*/


							//Selector s = Selector.open();
							dc.configureBlocking(false);
							dc.register(s,SelectionKey.OP_READ);
							
							while(true) {
								int time = s.select(500);
								if(time==0){
									break; //Timeout.
								}else{
									System.out.println("No timeout.");
									Iterator i = s.selectedKeys().iterator();
									while(i.hasNext()){
										SelectionKey k = (SelectionKey)i.next();
										DatagramChannel mychannel = (DatagramChannel)k.channel();
										inBuf = ByteBuffer.allocate(1024);
										SocketAddress serveraddr = mychannel.receive(inBuf);
										inBuf.flip();
										if(inBuf!=null){
											int seqNum = Integer.parseInt(inBuf.get(0) + ""); //out of bounds when dropping acks
											inBuf.position(1); //Skips the sequence number.
											seqNumBuf[seqNum] = inBuf.duplicate();
											numP -= 1;
											System.out.println("Packet received from server.");
											ByteBuffer ackBuf = ByteBuffer.wrap(("Ack" + seqNum).getBytes());
											dc.send(ackBuf, serveraddr);
										}
										i.remove();
									}
								}
							}
							for(int i=0; i<seqNumBuf.length; i++){
								if(seqNumBuf[i]!=null){
									fc.write(seqNumBuf[i]);
								}
								seqNumBuf[i] = null;
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
