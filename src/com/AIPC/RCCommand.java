package com.AIPC;
import lejos.robotics.navigation.Waypoint;

public interface RCCommand {
	public static interface Modes {
		 public final byte  // Send Modes
		  	HANDSHAKE = 0x01,
		  	REPORT = 0x02,
		  	MANUAL = 0x03,
		  	NAVIGATE = 0x04;
		 
		 public final byte  // Active Modes
		  	MOVING = 0x01,
		  	STOPPED = 0x02;
	}
	 
	  
	  public final byte  // REPORT
	  	MACADDR = 0x01,
	  	BATTERY = 0x02;
	  
	  public final byte  // MANUAL
	  	STOP = 0x01,
	  	FORWARD = 0x02,
	  	REVERSE = 0x03,
	  	TURN = 0x04;
	  	
	  public final byte  // PILOT
	  	LOCATION = 0x01;
	  	
	  public final byte  // PILOT
	  	HOME = 0x01,
	  	SHOP = 0x02,
	  	PARK = 0x03,
	  	OFFICE = 0x04;
	  
	  public final Waypoint  // PILOT
	  	HOMEPOINT = new Waypoint(0, 0),
	  	SHOPPOINT = new Waypoint(300, 900),
	  	PARKPOINT = new Waypoint(350,400),
	  	OFFICEPOINT = new Waypoint(800, 500);
}
