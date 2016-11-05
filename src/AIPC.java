import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import spark.Spark;

public class AIPC {
	final static String NXT_MAC = "00165316455E";
	
	static RobotConnection robotConnection;
	public static void main(String[] args) throws NXTCommException, IOException, InterruptedException {
		//NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", "00165316455E");			
		
		//robotConnection = new RobotConnection(nxtInfo);
		//robotConnection.handshake();

		//robotConnection.writeRobotPacket(null);
	
		
		//robotConnection.close();
		
	}
	
	

}
