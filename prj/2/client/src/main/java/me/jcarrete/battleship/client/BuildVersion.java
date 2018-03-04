package me.jcarrete.battleship.client;

public class BuildVersion {

	public static String getImplVersion() {
		return BuildVersion.class.getPackage().getImplementationVersion();
	}

	public static String getImplTitle() {
		return BuildVersion.class.getPackage().getImplementationTitle();
	}
}
