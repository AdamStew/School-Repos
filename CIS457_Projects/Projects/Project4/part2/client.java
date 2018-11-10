import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import javax.xml.bind.DatatypeConverter;


/*
Authors: Adam Stewart, Trace Remick, Zach Hopman
Assignment: Secure Communication Project
Due Date: 04/17/2017
*/

class client{
    private PrivateKey privKey;
    private PublicKey pubKey;
	private static SecretKey s;
	private static byte[] encryptedsecret;

	public static void main(String args[]){
		try{
			SocketChannel sc = SocketChannel.open();
			Console cons = System.console();
			String ip = cons.readLine("Enter IP Address: ");
			String p = cons.readLine("Enter desired port number: ");
			int port = Integer.parseInt(p);
			byte[] iv;
			sc.connect(new InetSocketAddress(ip, port));


			///////////////////
			cryptotest c = new cryptotest();
			c.setPublicKey("RSApub.der"); //Gets our public key.
			s = c.generateAESKey(); //Gets our secret key.
			log("Our secret key: " + s);
			encryptedsecret = c.RSAEncrypt(s.getEncoded()); //Encrypts our secret key, with our public key.

			ByteBuffer bufInital = ByteBuffer.wrap(encryptedsecret); //Sends our encrypted secret key.
			sc.write(bufInital);
			///////////////////

			tcpclientthread t = new tcpclientthread(sc, encryptedsecret, s);
			t.start();

			System.out.println("Connected to chat system. Please enter username to start off. Type '/quit' to leave. \n");
			System.out.print("Username: ");

			String message = "";
			while (!message.toLowerCase().equals("Some inconceivable message.")){
				message = cons.readLine("");

				iv = generateIv();
				ByteBuffer IVbuf = ByteBuffer.wrap(iv); //Send our message's IV.
				sc.write(IVbuf);

				ByteBuffer buf = generateEncrypted(message.getBytes(), iv); //Encrypt our message with our IV and secret key, and send.
				sc.write(buf);
			}			
			sc.close();
			System.exit(0);
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}

	public static byte[] generateIv(){
		SecureRandom r = new SecureRandom();
		byte ivbytes[] = new byte[16];
		r.nextBytes(ivbytes);

		return ivbytes;
	}

	public static byte[] generateDecrypted(byte[] plaintext, IvParameterSpec iv){
		log("Plain: " + DatatypeConverter.printHexBinary(plaintext));
		log("Secret: " + DatatypeConverter.printHexBinary(encryptedsecret));

		cryptotest c = new cryptotest();
		c.setPublicKey("RSApub.der");

		byte decryptedplaintext[] = c.decrypt(plaintext, s, iv);
		String dpt = new String(decryptedplaintext);
		//System.out.printf("PlainText: %s%n", dpt);

		return decryptedplaintext; 
	}

	public static ByteBuffer generateEncrypted(byte[] plaintext, byte[] iv){
		cryptotest c = new cryptotest();
		IvParameterSpec iv2 = new IvParameterSpec(iv);
		c.setPublicKey("RSApub.der");

		byte ciphertext[] = c.encrypt(plaintext, s, iv2);
		//System.out.printf("CipherText: %s%n", DatatypeConverter.printHexBinary(ciphertext));

		return ByteBuffer.wrap(ciphertext);
	}	
	
	public static void log(String l){
		System.out.println(l);
	}
}

class tcpclientthread extends Thread {
	SocketChannel sc;
	static byte[] encryptedsecret;
	static byte[] iv;
	private static SecretKey s;
	
	tcpclientthread(SocketChannel channel, byte[] ec, SecretKey sk){
		sc = channel;
		encryptedsecret = ec;
		s = sk;
	}	

	public void run(){
		try{
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			String message = "";
			while(!message.equals("/quit") && !message.equals("/kicked")){

				int sizeBuffer = sc.read(buffer);

				byte[] newData = new byte[sizeBuffer];
				System.arraycopy(buffer.array(), 0, newData, 0, sizeBuffer);

				if(iv == null){
					iv = newData; //Retrieve the message we're receiving's IV.
					//log("IV SET TO: " + DatatypeConverter.printHexBinary(iv));
				}else{
					//log("NEW DATA: " + DatatypeConverter.printHexBinary(newData));
					message = new String(generateDecrypted(newData, new IvParameterSpec(iv))).trim(); //Decrypt message with the IV and secret key.
					if(!message.toLowerCase().equals("/quit") && !message.equals("/kicked")){
						if(message != "") System.out.println(message);
						buffer.clear();
						buffer.put(new byte[4096]);
						buffer.clear();
					}
					iv = null;
				}
				buffer.clear();
				buffer.put(new byte[4096]);
				buffer.clear();
			}
			if(message.equals("/kicked")) {
				System.out.println("You have been kicked.");
			}else if(message.equals("/quit")) {
				System.out.println("You have left the chat room.");
			}
			sc.close();
			System.exit(0);
		}catch(IOException e){
			System.out.println("Got an IO Exception in a thread.");
		}
	}

	public static byte[] generateDecrypted(byte[] plaintext, IvParameterSpec iv){
		//log("Plain: " + DatatypeConverter.printHexBinary(plaintext));
		//log("Secret: " + DatatypeConverter.printHexBinary(encryptedsecret));

		cryptotest c = new cryptotest();
		c.setPublicKey("RSApub.der");

		byte decryptedplaintext[] = c.decrypt(plaintext, s, iv);
		String dpt = new String(decryptedplaintext);
		//System.out.printf("PlainText: %s%n", dpt);

		return decryptedplaintext;
	}
	
	public static void log(String l){
		System.out.println(l);
	}
}
