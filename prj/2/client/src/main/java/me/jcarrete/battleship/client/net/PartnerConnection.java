package me.jcarrete.battleship.client.net;

import java.net.Socket;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection {

	private Socket socket;

	PartnerConnection(Socket s) {
		socket = s;
	}

	public String remoteAddressAndPortAsString() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}
}
