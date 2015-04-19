import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;
public class AliceCore extends Thread {

	private String serverName = "localhost";
	private int port = 6666;
	private AliceFrame frame;
	
	public AliceCore(AliceFrame frame) throws IOException {
		this.frame = frame;
	}
	
	public AliceCore(AliceFrame frame, String serverName) throws IOException {
		this.frame = frame;
		this.serverName = serverName;
	}
	
	private static BigInteger createPublicKey(BigInteger p, BigInteger g, BigInteger privateKey) {
		return new BigInteger(g.toString()).modPow(privateKey, p);
	};
	
	private static BigInteger createSecretKey(BigInteger p, BigInteger publicKey, BigInteger privateKey) {
		return new BigInteger(publicKey.toString()).modPow(privateKey, p);
	};
	
	public void run() {
		try {
			frame.log("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);
			frame.log("Successfully connected to " + client.getRemoteSocketAddress());
			frame.log("");
			frame.log("----------------------------------------------");
			frame.log("");
			
			DataInputStream in = new DataInputStream(client.getInputStream());
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			
		    int kBitLength = 64;
		    
		    frame.log("Receiving primary...");
		    BigInteger p = new BigInteger(in.readUTF());
		    frame.log("Set primary to " + p.toString());
		    frame.log("");
		    
		   
		    frame.log("Receiving generator...");
		    BigInteger g = new BigInteger(in.readUTF());
		    frame.log("Set generator to " + g.toString());
		    frame.log("");
		    
//		    BigInteger privateKey = new BigInteger("13");
		    BigInteger privateKey = new BigInteger (kBitLength, new SecureRandom());
		    frame.log("Set private key to " + privateKey.toString());
		    frame.log("");
		    
		    BigInteger publicKey1 = createPublicKey(p,g,privateKey);
		    frame.log("Public key is " + publicKey1.toString());
		    frame.log("");
		    
		    frame.log("Receiving public key...");
		    BigInteger publicKey2 = new BigInteger(in.readUTF());
		    frame.log("Other public key is " + publicKey2.toString());
		    frame.log("");
		    
		    frame.log("Sending public key...");
		    out.writeUTF(publicKey1.toString());
		    frame.log("");
		    
		    BigInteger secretKey = createSecretKey(p,publicKey2,privateKey);
		    frame.log("Secret key is " + secretKey.toString());
		    frame.log("");
			
			frame.log("----------------------------------------------");
			frame.log("");
			client.close();
			frame.stopClient();
			
		} catch (IOException e) {
			e.printStackTrace();
			frame.log(e.getMessage());
			frame.stopClient();
		}
	}

}