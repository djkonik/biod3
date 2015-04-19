import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.util.Random;

public class BobCore extends Thread {
	
	private int port = 6666;
	private ServerSocket serverSocket;
	private BobFrame frame;

	public BobCore(BobFrame frame) throws IOException {
		serverSocket = new ServerSocket(port);
		this.frame = frame;
	}
	
	private static BigInteger createPublicKey(BigInteger p, BigInteger g, BigInteger privateKey) {
		return new BigInteger(g.toString()).modPow(privateKey, p);
	};
	
	private static BigInteger createSecretKey(BigInteger p, BigInteger publicKey, BigInteger privateKey) {
		return new BigInteger(publicKey.toString()).modPow(privateKey, p);
	};

	public void run() {
		while (true) {
			try {
				frame.log("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				frame.log("Successfully connected to " + server.getRemoteSocketAddress());
				frame.log("");
				frame.log("----------------------------------------------");
				frame.log("");
				
				DataInputStream in = new DataInputStream(server.getInputStream());
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				
			    int pBitLength = 128;
			    int gBitLength = 64;
			    int kBitLength = 64;
				
			    //BigInteger r = BigInteger.probablePrime(32, new Random());
				
//			    BigInteger p = new BigInteger("17");
			    BigInteger p = BigInteger.probablePrime(pBitLength, new SecureRandom());
			    frame.log("Set primary to " + p.toString());
			    frame.log("Sending primary...");
			    out.writeUTF(p.toString());
			    frame.log("");
			    
//			    BigInteger g = new BigInteger("3");
			    BigInteger g = new BigInteger (gBitLength, new SecureRandom());
			    frame.log("Set generator to " + g.toString());
			    frame.log("Sending generator...");
			    out.writeUTF(g.toString());
			    frame.log("");
			    
//			    BigInteger privateKey = new BigInteger("15");
			    BigInteger privateKey = new BigInteger (kBitLength, new SecureRandom());
			    frame.log("Set private key to " + privateKey.toString());
			    frame.log("");
			    
			    BigInteger publicKey1 = createPublicKey(p,g,privateKey);
			    frame.log("Public key is " + publicKey1.toString());
			    frame.log("");
			    
			    frame.log("Sending public key...");
			    out.writeUTF(publicKey1.toString());
			    frame.log("");
			    
			    frame.log("Receiving public key...");
			    BigInteger publicKey2 = new BigInteger(in.readUTF());
			    frame.log("Other public key is " + publicKey2.toString());
			    frame.log("");
			    
			    BigInteger secretKey = createSecretKey(p,publicKey2,privateKey);
			    frame.log("Secret key is " + secretKey.toString());
			    frame.log("");

				frame.log("----------------------------------------------");
				frame.log("");
			    server.close();
				
			} catch (SocketTimeoutException s) {
				frame.log("Socket timed out!");
				break;
			} catch (IOException e) {
				//frame.log(e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void interrupt() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			//frame.log(e.getMessage());
			e.printStackTrace();
		}
	}

}
