package ssl.simple;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {

	public static void main(String args[]) {
		// The Port number through which the server will accept this clients connection
		int serverPort = 35786;
		// The Server Address
		String serverName = "localhost";
		/*
		 * Adding the JSSE (Java Secure Socket Extension) provider which provides SSL
		 * and TLS protocols and includes functionality for data encryption, server
		 * authentication, message integrity, and optional client authentication.
		 */
		Security.addProvider(new Provider());
		// specifing the trustStore file which contains the certificate & public of the
		// server
		System.setProperty("javax.net.ssl.trustStore", "testTrustStore");
		// specifing the password of the trustStore file
		System.setProperty("javax.net.ssl.trustStorePassword", "hieuttbk");
		// This optional and it is just to show the dump of the details of the handshake
		// process
		// System.setProperty("javax.net.debug","all");
		try {
			// SSLSSocketFactory establishes the ssl context and and creates SSLSocket
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			// Create SSLSocket using SSLServerFactory already established ssl context and
			// connect to server
			SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(serverName, serverPort);

			sslSocket.startHandshake();

			BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

			// System.out.println("Write a Message: ");
			PrintWriter printWriter = new PrintWriter(sslSocket.getOutputStream(), true);
			BufferedReader commandPromptBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String messageToSend = null;

			while (true) {
				System.out.println("Client Req: ");
				messageToSend = commandPromptBufferedReader.readLine();
				if (messageToSend.equals("close")) {
					sslSocket.close();
					break;
				}
				printWriter.println(messageToSend);
				System.out.println("Server Resp: " + socketBufferedReader.readLine());

			}
		} catch (Exception ex) {
			System.err.println("Error Happened : " + ex.toString());
		}
	}
}
