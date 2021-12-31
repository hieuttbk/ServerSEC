package ssl.simple;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.sun.net.ssl.internal.ssl.Provider;

import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLClientLoadCer {

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
		// System.setProperty("javax.net.ssl.trustStore", "testTrustStore");
		// specifing the password of the trustStore file
		// System.setProperty("javax.net.ssl.trustStorePassword", "hieuttbk");
		// This optional and it is just to show the dump of the details of the handshake
		// process
		// System.setProperty("javax.net.debug","all");
		try {
			// SSLSSocketFactory establishes the ssl context and and creates SSLSocket
			// SSLSocketFactory sslsocketfactory = (SSLSocketFactory)
			// SSLSocketFactory.getDefault();
			// Create SSLSocket using SSLServerFactory already established ssl context and
			// connect to server
			// SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(serverName,
			// serverPort);

			// 	LOAD TRUSTSTORE. https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/utils/custom-trust-store
//			// The trust store file and optional password to unlock it
//			File trustStoreFile = new File("/path/to/my/truststore.jks");
//			char[] trustStorePassword = null; // assuming no trust store password
//
//			// Load the trust store, the default type is "pkcs12", the alternative is "jks"
//			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
//
//			// Create a new SSLSocketFactory, you can keep it around for each HTTPS
//			// request as it's thread-safe. Use the most recent TLS version supported by
//			// the web server. TLS 1.3 has been standard since 2018 and is recommended.
//			SSLSocketFactory sslSocketFactory = TLSUtils.createSSLSocketFactory(
//			    trustStore,
//			    TLSVersion.TLS_1_3);
			
			
			SSLContext ctx;
			ctx = SSLContext.getInstance("TLS");
			
			String certificatePath = "testkeys.cer";
			InputStream inputStream = new FileInputStream(certificatePath);
			X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(inputStream);

			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, null);
			keyStore.setCertificateEntry(Integer.toString(1), certificate);

			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			ctx.init(null, trustManagerFactory.getTrustManagers(), null);

			// ctx.init(kmf.getKeyManagers(), null, null);
			SSLSocketFactory ssf = ctx.getSocketFactory();

			SSLSocket sslSocket = (SSLSocket) ssf.createSocket(serverName, serverPort);

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
