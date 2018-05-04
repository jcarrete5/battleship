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
	/**
	 * Body Length: 4 bytes,
	 * Body:
	 *      targetIndex: 4 bytes
	 */
	public static final int MSG_FIRE = 2;
	/**
	 * Body Length: 0 bytes
	 */
	public static final int MSG_QUIT = 3;
	/**
	 * Body Length: 8 bytes,
	 * Body:
	 *      targetIndex: 4 bytes,
	 *      hitStatus: 4 bytes
	 */
	public static final int MSG_FIRE_RESULT = 4;
	/**
	 * Body Length: 0 bytes
	 */
	public static final int MSG_LOSE = 5;

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

	@Override
	public String toString() {
		return String.format("NetMessage: {type: %d, length: %d}", msgType, bodyLength);
	}
}
