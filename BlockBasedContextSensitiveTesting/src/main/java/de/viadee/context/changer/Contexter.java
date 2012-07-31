package de.viadee.context.changer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.viadee.context.util.ConnectionStatus;
import de.viadee.context.util.Util;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Contexter {

	protected static final String TAG = "Contexter";
	private final Instrumentation instrumentation;
	private final ConnectivityManager connectivityManager;
	private final Object sync = new Object();

	public Contexter(Instrumentation instr) {
		instrumentation = instr;
		connectivityManager = (ConnectivityManager) instrumentation.getTargetContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d(TAG, "Thread " + getThreadId() + ": initialised contexter");
	}

	public void establishContext(de.viadee.context.Context context) throws Exception {
		setNetworkStatus(context.getInternetConnectionStatus());
	}

	public void setNetworkStatus(ConnectionStatus status) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, NoSuchFieldException, InterruptedException {
		Log.d(TAG, "Mobile Internet is " + getMobileNetworkStatus());
		Log.d(TAG, "Setting Mobile internet to " + status);
		setMobileDataConnection(status);
	}

	private void setMobileDataConnection(final ConnectionStatus desiredStatus) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, InterruptedException {
		if (!desiredStatus.equals(getMobileNetworkStatus())) {
			Log.d(TAG, "Thread " + getThreadId() + ": called setMobileDataEnabled: changing state to " + desiredStatus);
			synchronized (sync) {
				ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver(sync, ConnectivityManager.TYPE_MOBILE, desiredStatus);
				registerBroadcastReceiver(receiver);
				setMobileData(desiredStatus);
				Log.d(TAG, "Thread " + getThreadId() + ": waiting for sync object after having invoked change method");
				sync.wait();
				Log.d(TAG, "Thread " + getThreadId() + ": resuming");
				unregisterBroadcastReceiver(receiver);
			}
		} else {
			Log.d(TAG, "Thread " + getThreadId() + ": called setMobileDataEnabled: Nothing to change");
		}
	}

	private void setMobileData(final ConnectionStatus desiredStatus) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		final boolean status = Util.getConnectionStatusAsBool(desiredStatus);

		final Class<? extends ConnectivityManager> conmanClass = connectivityManager.getClass();
		final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);

		final Object iConnectivityManager = iConnectivityManagerField.get(connectivityManager);
		final Class<? extends Object> iConnectivityManagerClass = iConnectivityManager.getClass();
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);
		// invocation of method on ConnectivityManager with argument 'status'
		setMobileDataEnabledMethod.invoke(iConnectivityManager, status);
	}

	private long getThreadId() {
		return Thread.currentThread().getId();
	}

	// private void setWirlessDataEnabled(boolean desiredStatus){
	// boolean connectionStatus = isWifiConnected();
	// if(connectionStatus != desiredStatus){
	// WifiManager wifiManager = (WifiManager)
	// instrumentation.getTargetContext().getSystemService(Context.WIFI_SERVICE);
	// wifiManager.setWifiEnabled(desiredStatus);
	// } else {
	// Log.i(TAG, "Did nothing");
	// }
	// }

	// private ConnectionStatus getWifiStatus(){
	// return getNetworkStatus(ConnectivityManager.TYPE_WIFI);
	// }

	private ConnectionStatus getMobileNetworkStatus() {
		return getNetworkStatus(ConnectivityManager.TYPE_MOBILE);
	}

	private ConnectionStatus getNetworkStatus(int type) {
		NetworkInfo[] activeNetworks = connectivityManager.getAllNetworkInfo();
		for (NetworkInfo currentNetwork : activeNetworks) {
			if (currentNetwork.isConnected() && type == currentNetwork.getType()) {
				return ConnectionStatus.CONNECTED;
			}
		}
		return ConnectionStatus.DISCONNECTED;
	}

	private void registerBroadcastReceiver(BroadcastReceiver receiver) {
		instrumentation.getTargetContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	private void unregisterBroadcastReceiver(BroadcastReceiver receiver) {
		instrumentation.getTargetContext().unregisterReceiver(receiver);
	}

}
