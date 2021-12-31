package ssl.ipe.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

//Thread handling the socket to server
class ServerThread extends Thread {
	private SSLSocket sslSocket = null;
	private String payload = null;

	ServerThread(SSLSocket sslSocket, String payload) {
		this.sslSocket = sslSocket;
		this.payload = payload;
	}

	public void run() {
		sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

		try {
			// Start handshake
			sslSocket.startHandshake();

			// Get session after the connection is established
			SSLSession sslSession = sslSocket.getSession();

			System.out.println("SSLSession :");
			System.out.println("\tProtocol : " + sslSession.getProtocol());
			System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

			// Start handling application content
			InputStream inputStream = sslSocket.getInputStream();
			OutputStream outputStream = sslSocket.getOutputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

			// Write data
			printWriter.println(payload);
			printWriter.flush();

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println("Input : " + line);

				if (line.trim().equals("HTTP/1.1 200\r\n")) {
					break;
				}
			}

			sslSocket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}