package ssl.ipe.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ServerIPE {
	private static String host = "127.0.0.1";
	private static int port = 10000;

	// Provide security information for apache https client that needs to
	// communicate with the OM2M server
	private static final String KEY_STORE_LOCATION = "C:\\Users\\Thinkpad\\git\\AuthorizationServer\\certs\\keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "C:\\Users\\Thinkpad\\git\\AuthorizationServer\\certs\\trustStore.jks";

	private static final String ALIAS = "das";

	public static void main(String[] args) {

		SSLServerSocketGenerator sslSocketGen = new SSLServerSocketGenerator(ALIAS, KEY_STORE_LOCATION,
				TRUST_STORE_LOCATION);
		SSLContext sslContext;

		try {
			sslContext = sslSocketGen.getSSLServerSocketFactory();
			// Create socket factory
		//	SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			// Create socket
		//	SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);

	//		System.out.println("SSL client started");
	//		ServerThread c = new ServerThread(sslSocket, "payload");
	//		c.start();

			//newcode
			SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
			SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(port);

			System.out.println("SSLServerSocket.accept()");
			SSLSocket sslSocket = (SSLSocket) s.accept();
			
			PrintWriter printWriter = new PrintWriter(sslSocket.getOutputStream(),true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            System.out.println("Client message  : " + bufferedReader.readLine());

            while(true)
            {
            	printWriter.println(bufferedReader.readLine() + " echo");
//                String recivedMessage = inputStream.readUTF();
//                System.out.println("Client Said : " + recivedMessage);
//                if(recivedMessage.equals("close"))
//                {
//                    outputStream.writeUTF("Bye");
//                    outputStream.close();
//                    inputStream.close();
//                    sslSocket.close();
//                    sslServerSocket.close();
//                    break;
//                }
//                else
//                {
//                    outputStream.writeUTF("You Said : "+recivedMessage);
//                }
            }
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
