package ssl.simple;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import com.sun.net.ssl.internal.ssl.Provider;

public class SSLServerLoadKeyStore {

	public static void main(String args[]) {
		// The Port number through which this server will accept client connections
		int port = 35786;
		/*
		 * Adding the JSSE (Java Secure Socket Extension) provider which provides SSL
		 * and TLS protocols and includes functionality for data encryption, server
		 * authentication, message integrity, and optional client authentication.
		 */
		Security.addProvider(new Provider());
		// specifing the keystore file which contains the certificate/public key and the
		// private key
		//System.setProperty("javax.net.ssl.keyStore", "testkeys");
		// specifing the password of the keystore file
		//System.setProperty("javax.net.ssl.keyStorePassword", "hieuttbk");
		// This optional and it is just to show the dump of the details of the handshake
		// process
		// System.setProperty("javax.net.debug","all");
		try {
//			// SSLServerSocketFactory establishes the ssl context and and creates
//			// SSLServerSocket
//			SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory
//					.getDefault();
//			// Create SSLServerSocket using SSLServerSocketFactory established ssl context
//			SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
//			
//			System.out.println("Echo Server Started & Ready to accept Client Connection");
//			// Wait for the SSL client to connect to this server
//			SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
			
      	  // set up key manager to do server authentication
          SSLContext ctx;
          KeyManagerFactory kmf;
          KeyStore ks;
          char[] passphrase = "hieuttbk".toCharArray();

          ctx = SSLContext.getInstance("TLS");
          kmf = KeyManagerFactory.getInstance("SunX509");
          ks = KeyStore.getInstance("JKS");

          ks.load(new FileInputStream("testkeys"), passphrase);
          kmf.init(ks, passphrase);
          ctx.init(kmf.getKeyManagers(), null, null);

          SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
          SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(port);
          
          System.out.println("Echo Server Started & Ready to accept Client Connection");
          SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
			
			

			BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
			PrintWriter printWriter = new PrintWriter(sslSocket.getOutputStream(), true);

			while (true) {
				String recivedMessage = socketBufferedReader.readLine();
				System.out.println("Client Said : " + recivedMessage);
				if (recivedMessage.equals("close")) {
					printWriter.println("Bye");
					sslSocket.close();
					sslServerSocket.close();
					break;
				} else {
					printWriter.println("OK babe, I recv: " + recivedMessage);
				}
			}
		} catch (Exception ex) {
			System.err.println("Error Happened : " + ex.toString());
		}
	}
}
