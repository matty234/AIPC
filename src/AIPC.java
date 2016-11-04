import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class AIPC {
	final static String NXT_MAC = "00165316455E";
	static DataOutputStream brickOutput;
	static DataInputStream brickInput;
	static int selectedNXT = 0;
	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00165316455E");			
		System.out.println("Opening connection...");
		nxtComm.open(nxtInfo);
		
			brickOutput = new DataOutputStream(nxtComm.getOutputStream());
			brickInput = new DataInputStream(nxtComm.getInputStream());
		
		for (int i = 0; i < 100; i++) {
			System.out.println("Sending: "+i);
			brickOutput.writeInt(i);
			brickOutput.flush();
			Thread.sleep(500);
			try {
				int read = brickInput.readInt();
				System.out.println("Received: "+read);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		close();
	}
	
	private static void close() throws IOException {
		brickOutput.close();
		brickInput.close();
	}

}
