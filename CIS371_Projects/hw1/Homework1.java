
import java.io.*;
import java.net.*;

/*************************
* Author: Adam Stewart   *
* Class: CIS 371         *
* Assignment: Homework 1 *
* Date: 09/01/2017       *
**************************/

public class Homework1 {

    public static void main(String[] args) {
        String hostName = "cis.gvsu.edu";
        int portNumber = 80;

		try {
			// Step 1: Create a socket that connects to the above host and port number
   			Socket sock = new Socket(hostName, portNumber);

        	// Step 2: Create a PrintWriter from the socket's output stream
        	//         Use the autoFlush option
			PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

        	// Step 3: Create a BufferedReader from the socket's input stream
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

       		// Step 4: Send an HTTP GET request via the PrintWriter.
        	//         Remember to print the necessary blank line
			String data = "/~dulimarh/demo.html";
			out.println("GET " + data + " HTTP 1.0\n");

        	// Step 5a: Read the status line of the response
        	// Step 5b: Read the HTTP response via the BufferedReader until
        	//         you get a blank line
			String line;
			while((line = in.readLine()) != null && !line.trim().equals("")) {
				System.out.println(line);
			}
			System.out.println("");

        	// Step 6a: Create a FileOutputStream for storing the payload
        	// Step 6b: Wrap the FileOutputStream in another PrintWriter
			File file = new File("hwOutput.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter payload = new PrintWriter(fos, true);

        	// Step 7: Read the rest of the input from BufferedReader and write
        	//         it to the second PrintWriter.
        	//         Hint: readLine() returns null when there is no more data
        	//         to read
			while((line = in.readLine()) != null) {
				payload.println(line);
			}

        	// Step 8: Remember to close the writer
			sock.close();

		} catch(IOException err) {
			System.out.println("There seemed to be an error.");
		}
    }
}
