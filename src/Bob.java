
// File Name GreetingServer.java

import java.io.IOException;

public class Bob extends Thread {
	
	public static void main(String[] args) {
		try {
			BobFrame frame = new BobFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}
