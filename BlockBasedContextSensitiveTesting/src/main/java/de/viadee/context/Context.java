package de.viadee.context;

import de.viadee.context.util.ConnectionStatus;

public class Context {

	private final ConnectionStatus internetConnectionStatus;

	public Context(final ConnectionStatus status) {
		internetConnectionStatus = status;
	}

	public ConnectionStatus getInternetConnectionStatus() {
		return internetConnectionStatus;
	}

	@Override
	public int hashCode() {
		return internetConnectionStatus.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Context) {
			Context otherContext = (Context) obj;
			return this.internetConnectionStatus.equals(otherContext.internetConnectionStatus);
		}
		return false;

	}

	@Override
	public String toString() {
		return internetConnectionStatus.toString();
	}

}
