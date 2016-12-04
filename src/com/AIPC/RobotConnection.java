package com.AIPC;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class RobotConnection  implements RCCommand {
	
	NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);

	DataOutputStream brickOutput;
	DataInputStream brickInput;
	
	public RobotConnection(NXTInfo nxtInfo, NXTComm nxtComm) throws NXTCommException  {
		setNxtComm(nxtComm);
		openConnection(nxtInfo);
	}
	
	public RobotConnection(NXTInfo nxtInfo) throws NXTCommException {
		openConnection(nxtInfo);
	}
	
	private void openConnection(NXTInfo nxtInfo) throws NXTCommException {
		nxtComm.open(nxtInfo);
		brickOutput = new DataOutputStream(nxtComm.getOutputStream());
		brickInput = new DataInputStream(nxtComm.getInputStream());
	}
	
	public boolean handshake() {
		try {
			writeByte(RCCommand.Modes.HANDSHAKE);
			if(readByte()==RCCommand.Modes.HANDSHAKE) {
				return true; 
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public NXTComm getNxtComm() {
		return nxtComm;
	}
	private void setNxtComm(NXTComm nxtComm) {
		this.nxtComm = nxtComm;
	}
	
	public void writeInt(int value) throws IOException {
		brickOutput.writeInt(value);
		brickOutput.flush();
	}
	
	public void writeByte(byte value) throws IOException {
		brickOutput.writeByte(value);
		brickOutput.flush();
	}
	
	public void writeRobotPacket(RobotPacket rb) throws IOException {
		brickOutput.write(rb.getMode());
		brickOutput.write(rb.getLength());
		for (int i = 0; i < rb.getLength(); i++) {
			brickOutput.write(rb.getCommands()[i]);
		}
		brickOutput.flush();
	}
	
	public byte readByte() throws IOException {
		return brickInput.readByte();
	}
	public RobotPacket readRobotPacket() throws IOException {
		byte mode = readByte();
		int length = readByte();
		byte[] commands = new byte[length];
		for (int i = 0; i < length; i++) {
			commands[i] = readByte();
		}
		RobotPacket robotPacket = new RobotPacket(mode, commands);
		return robotPacket;
	}
	public void close() throws IOException {
		brickOutput.close();
		brickInput.close();
	}
}
