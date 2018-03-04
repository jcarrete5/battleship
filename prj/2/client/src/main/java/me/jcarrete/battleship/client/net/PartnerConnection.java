package me.jcarrete.battleship.client.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Acts as a way to send data to another player over a network.
 */
public class PartnerConnection extends Socket {

	PartnerConnection(InetAddress address, int port) throws IOException {
		super(address, port);
	}
}
