package com.AIPC;

import java.io.IOException;

import com.AIPC.RCCommand.Modes;
import com.AIPC.models.FareHandler;

public class WaitForPacket extends Thread {
	RobotConnection connection;
	FareHandler fareHandler;
	public WaitForPacket(RobotConnection robotConnection) {
		this.connection = robotConnection;
	}
	@Override
	public void run() {
		try {
			while(true) {
				RobotPacket rb = connection.readRobotPacket();
				System.out.println("Rec: RB"+rb.getMode());
				switch (rb.getMode()) {
				case Modes.FARE:
					fareHandler.handleFare(rb.getCommands()[0] & 0xFF);
		
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFareHandler(FareHandler fareHandler) {
		this.fareHandler = fareHandler;
	}
}
