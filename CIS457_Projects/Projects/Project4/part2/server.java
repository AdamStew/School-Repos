import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;
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

class server{
	static ConcurrentHashMap<String, SocketChannel> m = new ConcurrentHashMap<String, SocketChannel>();
	static ConcurrentHashMap<String, SecretKey> secretM = new ConcurrentHashMap<String, SecretKey>();

	public static void main(String args[]){
		try{
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();
			//String m = cons.readLine("Enter desired port number: ");
			//int port = Integer.parseInt(m);
			int port = 3335;
			while(c.isOpen()){
				c.bind(new InetSocketAddress(port));
				
				while(true){
					SocketChannel sc = c.accept();
					tcpserverthread t = new tcpserverthread(sc, m, secretM);
					t.start();
					System.out.println("Client has connected to server.");
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}

class tcpserverthread extends Thread {
    private PrivateKey privKey;
    private PublicKey pubKey;
	private static byte[] byteIV = null;
	private static IvParameterSpec iv = null;

	SocketChannel sc;
	String username;
	ConcurrentHashMap<String, SocketChannel> hash;
	static ConcurrentHashMap<String, SecretKey> secretHash = new ConcurrentHashMap<String, SecretKey>();
	
	tcpserverthread(SocketChannel channel, ConcurrentHashMap<String, SocketChannel> m, ConcurrentHashMap<String, SecretKey> secretM){
		sc = channel;
		hash = m;
		secretHash = secretM;
	}	

	public void run(){
		try{
			SecretKey s;
			byte[] encryptedsecret = null;
			ByteBuffer buffer = ByteBuffer.allocate(4096);
			String message = "";

			while(!message.toLowerCase().equals("/quit")){
				int sizeBuffer = sc.read(buffer);

				byte[] newData = new byte[sizeBuffer];
				System.arraycopy(buffer.array(), 0, newData, 0, sizeBuffer);
				log("new data: " + DatatypeConverter.printHexBinary(newData));
				if((username != null && secretHash.get(username) == null) || encryptedsecret == null){
					encryptedsecret = newData; //The client's encrypted secret key, in bytes.

				} else if(iv == null){
					byteIV = newData; //The client's IV used with the message received.
					iv = new IvParameterSpec(byteIV);
				}else{

					if(username != null) {
						message = new String(generateDecrypted(newData, iv, secretHash.get(username))).trim(); //If we have our username, decrypt our message.
					}
				
					if(username == null){
						s = generateDecryptedKey(encryptedsecret); //Decrypted secret key from client.
						log("Key I'm about to use for this username: " + s);
						username = new String(generateDecrypted(newData, iv, s)).trim(); //Decrypted username from client.
						hash.put(username, sc);
						secretHash.put(username, s);

						System.out.println("Username set to: " + username);
					}else if(message != null && message.length() > new String("/whisper").length() && message.toLowerCase().substring(0, new String("/whisper").length()).equals("/whisper")){
						String[] parts = message.split(" ");
						String whisperTo = parts[1];
						message = "Whisper from " + username + ": " + message.substring(whisperTo.length() + 2 + new String("/whisper").length(), message.length());
				
						SocketChannel userObj = hash.get(whisperTo);
						if(userObj != null){
							byte whisperIv[] = generateIv();
							userObj.write(ByteBuffer.wrap(whisperIv));
							userObj.write(generateEncrypted(message.getBytes(), secretHash.get(whisperTo), new IvParameterSpec(whisperIv)));
							log("Whisper sent to " + whisperTo);
						}else{
							log("No such user named: " + whisperTo + "...");
						}
					}else if(message != null && message.equals("/users")){
						String userStr = "Users currently connected: ";
					
						for(String key : hash.keySet()){
							userStr += key + ", ";
						}

						userStr = userStr.substring(0, userStr.length() - 2);

						byte userIv[] = generateIv();

						sc.write(ByteBuffer.wrap(userIv));
						sc.write(generateEncrypted(userStr.getBytes(), secretHash.get(username), new IvParameterSpec(userIv)));
					}else if(username != null && (username.toLowerCase().equals("trace") || username.toLowerCase().equals("adam") || username.toLowerCase().equals("zach")) && message != null && message.toLowerCase().contains("/kick")){
						String[] parts = message.split(" ");

						SocketChannel userObj = hash.get(parts[1]);

						if(userObj != null){
							log("Kicking " + parts[1] + "...");

							byte kickIv[] = generateIv();
							userObj.write(ByteBuffer.wrap(kickIv));
							userObj.write(generateEncrypted(new String("/kicked").getBytes(), secretHash.get(parts[1]), new IvParameterSpec(kickIv)));
							userObj.close();
							hash.remove(parts[1]);
						}else{
							log("No such user named: " + parts[1] + "...");
						}
					}else if(message.toLowerCase().equals("/quit")){
						System.out.println(username + " has left.");
						byte quitIv[] = generateIv();
						sc.write(ByteBuffer.wrap(quitIv));
						sc.write(generateEncrypted(new String("/quit").getBytes(), secretHash.get(username), new IvParameterSpec(quitIv)));	
						sc.close();
						hash.remove(username);
					}else{
						String output = username + ": " + message;

						broadcastMsg(username, output, hash);
						System.out.println(output);
					}
				
					iv = null;
				}

				clearBuf(buffer);
			}
		}catch(IOException e){
			//System.out.println("Got an IO Exception in a thread.");
		}
	}

	public static byte[] generateIv(){
		SecureRandom r = new SecureRandom();
		byte ivbytes[] = new byte[16];
		r.nextBytes(ivbytes);

		return ivbytes;
	}


	public static void clearBuf(ByteBuffer buffer){
		buffer.clear();
		buffer.put(new byte[4096]);
		buffer.clear();
	}

	public static ByteBuffer generateEncrypted(byte[] plaintext, SecretKey sk1, IvParameterSpec iv){	
		cryptotest c = new cryptotest();
		c.setPublicKey("RSApub.der");

		byte ciphertext[] = c.encrypt(plaintext, sk1, iv);
		System.out.printf("CipherText: %s%n", DatatypeConverter.printHexBinary(ciphertext));

		return ByteBuffer.wrap(ciphertext);
	}

	public static byte[] generateDecrypted(byte[] plaintext, IvParameterSpec iv, SecretKey sk3){
		cryptotest c = new cryptotest();
		c.setPrivateKey("RSApriv.der");

		byte decryptedplaintext[] = c.decrypt(plaintext, sk3, iv);
		String dpt = new String(decryptedplaintext);

		return decryptedplaintext;
	}

	public static SecretKeySpec generateDecryptedKey(byte[] plaintext){

		cryptotest c = new cryptotest();
		c.setPublicKey("RSApub.der");
		c.setPrivateKey("RSApriv.der");

		byte decryptedsecret[] = c.RSADecrypt(plaintext);

		return new SecretKeySpec(decryptedsecret, "AES");
	}

	public static void broadcastMsg(String username, String message, ConcurrentHashMap<String, SocketChannel> map) {
		try{
			for(String key : map.keySet()){
				if(!username.equals(key)){
					map.get(key).write(ByteBuffer.wrap(byteIV));
					map.get(key).write(generateEncrypted(message.getBytes(), secretHash.get(key), iv));
					
					System.out.println("Sent message to: " + key);
				}
			}
		}catch(IOException e){
			System.out.println("Got an IO Exception in server");
		}
	}

	public static void log(String l){
		System.out.println(l);
	}
}
