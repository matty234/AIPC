
public interface RCCommand {
	public static interface Modes {
		 public final byte  // Modes
		  	HANDSHAKE = 0x01,
		  	REPORT = 0x02,
		  	MANUAL = 0x03,
		  	PILOT = 0x04;
	}
	 
	  
	  public final int  // REPORT
	  	MACADDR = 0x01,
	  	BATTERY = 0x02;
	  
	  public final int  // MANUAL
	  	STOP = 0x01,
	  	FORWARD = 0x02,
	  	REVERSE = 0x03,
	  	TURN = 0x04;
	  	
	  public final int  // PILOT
	  	LOCATION = 0x01;
	  	
	  public final int  // PILOT
	  	HOME = 0x01,
	  	SHOP = 0x02;
}
