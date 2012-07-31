package de.viadee.context.changer;

import de.viadee.context.util.ConnectionStatus;
import de.viadee.context.util.Util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "ConnectivityBroadcastReceiver";
	private final Object waitLock;
	private final boolean expectedStatus;
	private final int type;

	/**
	 * Receiver for Broadcast Events sent by the Android Platform
	 * 
	 * @param waitLock
	 *            Object used as lock, the calling thread waits for the
	 *            broadcast receiver to call nofiy() on this object when the
	 *            correct event was received
	 * @param type
	 *            The type of network connection events this receiver waits for
	 * @param desiredStatus
	 *            The desired connectivity state of the specified network type
	 */
	public ConnectivityBroadcastReceiver(final Object waitLock, final int type, final ConnectionStatus desiredStatus) {
		super();
		this.waitLock = waitLock;
		this.type = type;
		this.expectedStatus = Util.getConnectionStatusAsBool(desiredStatus);
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
		boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		Log.d(TAG, "Thread " + getThreadId() + ": onReceive()");
		logNetworkBroadcastEvent(noConnectivity, currentNetworkInfo);

		if (currentNetworkInfo != null && (currentNetworkInfo.isConnected() == expectedStatus) && (currentNetworkInfo.getType() == type)) {
			synchronized (waitLock) {
				Log.d(TAG, "Thread " + getThreadId() + ": Waking up the first waiting thread on monitor sync from");
				waitLock.notify();
			}
		}
	}

	private long getThreadId() {
		return Thread.currentThread().getId();
	}

	private void logNetworkBroadcastEvent(final boolean noConnectivity, final NetworkInfo currentNetworkInfo) {
		StringBuilder logMessage = new StringBuilder();
		logMessage.append("Broadcast Event Occured, Connection Status ");
		if (noConnectivity) {
			logMessage.append("disconnected");
		} else {
			logMessage.append("connected");
		}
		if (currentNetworkInfo != null) {
			logMessage.append(" | Affected Network: ");
			logMessage.append(currentNetworkInfo.getTypeName() + " (" + currentNetworkInfo.isConnected() + ")");
		}
		Log.d(TAG, logMessage.toString());
	}

}
