package com.AIPC;


public class RobotPacket implements RCCommand{
	public byte mode;
	public byte[] commands;
	
	public RobotPacket(byte mode, byte[] commands) {
		setMode(mode);
		setCommands(commands);
	}
	
	public RobotPacket(byte mode, byte command) {
		setMode(mode);
		byte[] commands = { command };
		setCommands(commands);
	}

	public byte getMode() {
		return mode;
	}
	public byte[] getCommands() {
		return commands;
	}
	public int getLength() {
		return commands.length;
	}
	public void setMode(byte mode) {
		this.mode = mode;
	}
	public void setCommands(byte[] commands) {
		this.commands = commands;
	}

	
	
}
