package me.jcarrete.battleship.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static me.jcarrete.battleship.client.BattleshipClient.LOGGER;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection implements AutoCloseable {

	private enum NetMessage {
		READY;
	}

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	PartnerConnection(Socket s) throws IOException {
		socket = s;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
	}

	public String remoteAddressAndPortAsString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}

	/**
	 * Tell foe that we are ready to play.
	 */
	public void ready() {
		LOGGER.fine("Sending ready message");
	}

	@Override
	public void close() throws Exception {
		socket.close();
	}
}
