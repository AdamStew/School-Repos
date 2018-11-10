import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

class test{
	public static void main(String args[]){
		ByteBuffer buf = ByteBuffer.allocate(1024);
		Integer n = 10;
		Integer m = 10;
		buf.put(n.byteValue());
		buf.position(1);
		buf.put(m.byteValue());
		System.out.println(getParity(buf));
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
