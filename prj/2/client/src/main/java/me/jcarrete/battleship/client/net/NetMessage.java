package me.jcarrete.battleship.client.net;

import java.nio.ByteBuffer;

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
	public static final int MSG_READY = 1;

	private final int msgType;
	private final int bodyLength;
	private final ByteBuffer body;

	NetMessage(int msgType, int bodyLength, byte[] body) {
		this.msgType = msgType;
		this.bodyLength = bodyLength;
		this.body = ByteBuffer.wrap(body);
	}

	public int getMsgType() {
		return msgType;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public ByteBuffer getBody() {
		return body.asReadOnlyBuffer();
	}
}
