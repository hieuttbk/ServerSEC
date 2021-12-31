package ssl.ipe.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ClientIPE {
	private static String host = "127.0.0.1";
	private static int port = 10000;

	// Provide security information for apache https client that needs to
	// communicate with the OM2M server
	private static final String KEY_STORE_LOCATION = "C:\\Users\\Thinkpad\\git\\AuthorizationServer\\certs\\keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "C:\\Users\\Thinkpad\\git\\AuthorizationServer\\certs\\trustStore.jks";

	private static final String ALIAS = "das";

	public static void main(String[] args) {

		SSLSocketGenerator sslSocketGen = new SSLSocketGenerator(ALIAS, KEY_STORE_LOCATION, TRUST_STORE_LOCATION);
		SSLContext sslContext;

		try {
			sslContext = sslSocketGen.getSSLSocketFactory();
			// Create socket factory
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			// Create socket
			SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);

			System.out.println("SSL client started");
			ClientThread c = new ClientThread(sslSocket, "payload"); 
			c.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


}
