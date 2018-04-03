package me.jcarrete.battleship.client.net;

public class NetMessage {

	/*
	 * Message Structure:
	 * Message Type: 4 bytes
	 * Body Length (n): 4 bytes
	 * Body: n bytes
	 */

	/**
	 * Body Length: 0 bytes
	 */
	public static final int MSG_READY = 0x1;

	NetMessage(int msgType, int bodyLength, byte[] body) {

	}
}
