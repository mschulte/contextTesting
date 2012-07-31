package de.viadee.context.util;


public class Util {

	private Util() {
	}

	public static boolean getConnectionStatusAsBool(ConnectionStatus status) {
		if (status.equals(ConnectionStatus.CONNECTED)) {
			return true;
		} else {
			return false;
		}
	}

}
